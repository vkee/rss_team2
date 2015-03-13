package GlobalNavigation;

import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;

/**
 * Contains methods to deal with configuration spaces
 */
public class CSpace {
	public double robotXShift = 0.0;
	public double robotYShift = 0.0;

	// Note that we are assuming that the robot rotates at its center of the
	// square approximation
	// This will almost certainly need to be accounted for as the robot does not
	// turn at its center

	// This approximation needs to be changed when we navigate robot on the
	// field. True center
	// of robot is "higher up" than the current estimated version

	// REAL private final double ROBOT_LONGEST_DIM = 0.375; // Center to
	// diagonal length in meters
	private final double ROBOT_LONGEST_DIM = 0.201; // Center to diagonal length
													// in meters
	private final int NUM_SIDES = 1000; // number of sides of the n side polygon
										// approximation of robot
	private PolygonObstacle robotPoly;

	public CSpace() {
		// Generating the robot polygon
		robotPoly = new PolygonObstacle();
		// manually defining robot polygon

		for (int i = 0; i < NUM_SIDES; i++) {
			double theta = 2 * Math.PI / NUM_SIDES * i;
			robotPoly.addVertex(ROBOT_LONGEST_DIM * Math.cos(theta),
					ROBOT_LONGEST_DIM * Math.sin(theta));
		}

		robotPoly.close();

		// origRobotPoly.addVertex(0.0, 0.0);
		// origRobotPoly.addVertex(ROBOT_WIDTH, 0.0);
		// origRobotPoly.addVertex(ROBOT_WIDTH, ROBOT_HEIGHT);
		// origRobotPoly.addVertex(0.0, ROBOT_HEIGHT);
		// origRobotPoly.close();
		// robotPoly = origRobotPoly;
		// robotPoly = changeOrigin(origRobotPoly, new Point2D.Double(0.0,
		// 0.0));
	}

	/**
	 * Computes the Minkowski sum of two polygons
	 * 
	 * @param poly1
	 *            the first polygon
	 * @param poly2
	 *            the second polygon
	 * @return the Minkowski sum of the polygons
	 */
	public PolygonObstacle computeMSum(PolygonObstacle poly1,
			PolygonObstacle poly2) {
		PolygonObstacle mSum = new PolygonObstacle();

		for (Point2D.Double vertex1 : poly1.getVertices()) {
			for (Point2D.Double vertex2 : poly2.getVertices()) {
				mSum.addVertex(vertex1.getX() + vertex2.getX(), vertex1.getY()
						+ vertex2.getY());
			}
		}

		mSum.close();

		return mSum;
	}

	/**
	 * Rescales the polygon's vertices to be with respect to the new reference
	 * point (centered in the polygon's vertices)
	 * 
	 * @param origPoly
	 *            the polygon whose vertices are to be rescaled
	 * @param refPoint
	 *            the new reference point
	 * @return the new rescaled polygon
	 */
	public PolygonObstacle changeOrigin(PolygonObstacle origPoly,
			Point2D.Double refPoint) {

		int numVertices = origPoly.getVertices().size();
		double xSum = 0.0;
		double ySum = 0.0;

		// Computing the centroid of the polygon
		for (Point2D.Double vertex : origPoly.getVertices()) {
			xSum += vertex.getX();
			ySum += vertex.getY();
		}

		xSum /= numVertices;
		ySum /= numVertices;

		// Computing the difference in the x and y components of the centroid
		// and ref point
		robotXShift = xSum - refPoint.getX();
		robotYShift = ySum - refPoint.getY();

		// Rescaling each vertex to have the reference point as the centroid
		return shiftObs(origPoly, robotXShift, robotYShift);
	}

	/**
	 * Shifts all the vertices of a polygon by the designated x and y shifts
	 * 
	 * @param origPoly
	 * @param xShift
	 * @param yShift
	 * @return
	 */
	public PolygonObstacle shiftObs(PolygonObstacle origPoly, double xShift,
			double yShift) {
		PolygonObstacle newPoly = new PolygonObstacle();

		for (Point2D.Double vertex : origPoly.getVertices()) {
			newPoly.addVertex(vertex.getX() + xShift, vertex.getY() + yShift);
		}

		newPoly.close();

		return newPoly;
	}

	/**
	 * Computes the configuration space of an obstacle
	 * 
	 * @param obsPoly
	 *            the polygon of the obstacle
	 * @param robotPoly
	 *            the polygon of the robot (not centered at the reference point)
	 * @param refPoint
	 *            the robot's reference point
	 * @return the obstacle configuration space
	 */
	public PolygonObstacle obsCSpace(PolygonObstacle obsPoly,
			PolygonObstacle robotPoly, Point2D.Double refPoint,
			boolean computeRobotPoly) {

		if (computeRobotPoly) {
			// Setting the robot at the origin
			robotPoly = changeOrigin(robotPoly, refPoint);
		}

		// Actually probably don't need to shift the obstacle
		// Shifting the obstacle by the same amount the robot polygon is shifted
		// to keep everything the same
		// PolygonObstacle shiftedObsPoly = shiftObs(obsPoly, robotXShift,
		// robotYShift);

		// To compute the config space of the obstacle, probably need to have
		// the ref point at origin or else when compute minkowski sum, values
		// may be off
		return GeomUtils.convexHull(computeMSum(robotPoly, obsPoly)
				.getVertices());
	}

	/**
	 * Computes the configuration space of the provided map.
	 * 
	 * @param polyMap
	 *            the map of the environment to generate a configuration space
	 *            of
	 * @return the configuration space obstacles of the map obstacles and the
	 *         boundaries
	 */
	public List<PolygonObstacle> envConfSpace(PolygonMap polyMap) {
		List<PolygonObstacle> obsCSpaces = new ArrayList<PolygonObstacle>();

		// obsCSpaces.add(robotPoly);

		// Computed the configuration spaces of the obstacle

		for (PolygonObstacle obstacle : polyMap.getObstacles()) {
			obsCSpaces.add(obsCSpace(obstacle, robotPoly, null, false));
		}

		// PolygonObstacle obstacle = polyMap.getObstacles().get(0);
		// obsCSpaces.add(obsCSpace(obstacle, robotPoly, null, false));

		// obsCSpaces.add(obsCSpace(obstacle, robotPoly, null, false));

		// build obstacle for the boundaries
		Rectangle2D.Double envBounds = polyMap.worldRect;

		PolygonObstacle boundaryObs = new PolygonObstacle();

		// bottom
		boundaryObs.addVertex(envBounds.getX(), envBounds.getY());
		boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
				envBounds.getY());
		obsCSpaces.add(obsCSpace(boundaryObs, robotPoly, null, false));

		// right
		boundaryObs = new PolygonObstacle();
		boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
				envBounds.getY());
		boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
				envBounds.getY() + envBounds.getHeight());
		obsCSpaces.add(obsCSpace(boundaryObs, robotPoly, null, false));

		// left
		boundaryObs = new PolygonObstacle();
		boundaryObs.addVertex(envBounds.getX(), envBounds.getY());
		boundaryObs.addVertex(envBounds.getX(),
				envBounds.getY() + envBounds.getHeight());
		obsCSpaces.add(obsCSpace(boundaryObs, robotPoly, null, false));

		// top
		boundaryObs = new PolygonObstacle();
		boundaryObs.addVertex(envBounds.getX(),
				envBounds.getY() + envBounds.getHeight());
		boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
				envBounds.getY() + envBounds.getHeight());
		obsCSpaces.add(obsCSpace(boundaryObs, robotPoly, null, false));

		return obsCSpaces;
	}
}