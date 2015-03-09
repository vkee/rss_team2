package GlobalNavigation;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Contains methods to deal with configuration spaces
 */
public class CSpace {
    public double robotXShift = 0.0;
    public double robotYShift = 0.0;

    public CSpace() {

    }

    /**
     * Computes the Minkowski sum of two polygons
     * @param poly1 the first polygon
     * @param poly2 the second polygon
     * @return the Minkowski sum of the polygons
     */
    public PolygonObstacle computeMSum(PolygonObstacle poly1, PolygonObstacle poly2) {
        PolygonObstacle mSum = new PolygonObstacle();

        for (Point2D.Double vertex1 : poly1.getVertices()) {
            for (Point2D.Double vertex2 : poly2.getVertices()) {
                mSum.addVertex(new Point2D.Double(vertex1.getX() + vertex2.getX(), vertex1.getY() + vertex2.getY()));
            }
        }

        mSum.close();

        return mSum;
    }

    /**
     * Rescales the polygon's vertices to be with respect to the new reference point (centered in the polygon's vertices)
     * @param origPoly the polygon whose vertices are to be rescaled
     * @param refPoint the new reference point
     * @return the new rescaled polygon
     */
    public PolygonObstacle changeOrigin(PolygonObstacle origPoly, Point2D.Double refPoint) {

        int numVertices = origPoly.getVertices().size();
        double xSum = 0.0;
        double ySum = 0.0;

        //        Computing the centroid of the polygon
        for (Point2D.Double vertex : origPoly.getVertices()) {
            xSum += vertex.getX();
            ySum += vertex.getY();
        }

        xSum /= numVertices;
        ySum /= numVertices;

        //        Computing the difference in the x and y components of the centroid and ref point
        robotXShift = xSum - refPoint.getX();
        robotYShift = ySum - refPoint.getY();

        //        Rescaling each vertex to have the reference point as the centroid
        return shiftObs(origPoly, robotXShift, robotYShift);
    }

    /**
     * Shifts all the vertices of a polygon by the designated x and y shifts
     * @param origPoly
     * @param xShift
     * @param yShift
     * @return
     */
    public PolygonObstacle shiftObs(PolygonObstacle origPoly, double xShift, double yShift) {
        PolygonObstacle newPoly = new PolygonObstacle();

        for (Point2D.Double vertex : origPoly.getVertices()) {
            newPoly.addVertex(vertex.getX() + xShift, vertex.getY() + yShift);
        }

        newPoly.close();

        return newPoly;
    }

    /**
     * Computes the configuration space of an obstacle
     * @param obsPoly the polygon of the obstacle
     * @param robotPoly the polygon of the robot (not centered at the reference point)
     * @param refPoint the robot's reference point
     * @return the obstacle configuration space
     */
    public PolygonObstacle obsCSpace(PolygonObstacle obsPoly, PolygonObstacle robotPoly, Point2D.Double refPoint) {
        PolygonObstacle obsCSpace = new PolygonObstacle();

        //        Setting the robot at the origin
        PolygonObstacle shiftedRobotPoly = changeOrigin(robotPoly, refPoint);
        //        Shifting the obstacle by the same amount the robot polygon is shifted to keep everything the same
        PolygonObstacle shiftedObsPoly = shiftObs(obsPoly, robotXShift, robotYShift);

//        To compute the config space of the obstacle, probably need to have the ref point at origin or else when compute minkowski sum, values may be off
        return computeMSum(shiftedRobotPoly, shiftedObsPoly);
    }
}