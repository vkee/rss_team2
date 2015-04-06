package MotionPlanning;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import GlobalNavigation.GeomUtils;
import GlobalNavigation.PolygonMap;
import GlobalNavigation.PolygonObstacle;

/**
 * CSpace is part of the MotionPlanning module. It contains methods for dealing with the 3D
 * configuration space of the environment.
 */
public class CSpace {
//    Robot Corner Coordinates wrt coordinate origin at the robot axis of rotation, where x is pointing forward,
//    y is pointing left, and z is pointing up (when the robot is at 0 rad position in polar coordinates, x and y
//    are aligned with the polar axes
//    NOTE: These are not necessary the corners of the robot, but the outer dimension of the entire robot
//    TODO: double check these measurements
    private final double ROBOT_TL_X = 0.190; // robot top left x dimension
    private final double ROBOT_TL_Y = 0.215; // robot top left y dimension
    private final double ROBOT_TR_X = 0.190; // robot top right x dimension
    private final double ROBOT_TR_Y = -0.215; // robot top right y dimension
    private final double ROBOT_BL_X = -0.310; // robot bottom left x dimension
    private final double ROBOT_BL_Y = 0.190; // robot bottom left y dimension
    private final double ROBOT_BR_X = -0.310; // robot bottom right x dimension
    private final double ROBOT_BR_Y = -0.190; // robot bottom right y dimension

    public CSpace() {
        // Generating the robot polygon
        robotPoly = new PolygonObstacle();
        // manually defining robot polygon

        for (int i = 0; i < NUM_SIDES; i++) {
            double theta = 2 * Math.PI / NUM_SIDES * i;
            robotPoly.addVertex(ROBOT_SIDE_DIM * Math.cos(theta),
                    ROBOT_SIDE_DIM * Math.sin(theta));
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