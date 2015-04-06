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
    //    are aligned with the polar axes)
    //    NOTE: These are not necessary the corners of the robot, but the outer dimension of the entire robot
    //    TODO: double check these measurements
    private final double ROBOT_TL_X = 0.190; // robot top left x dimension
    private final double ROBOT_TL_Y = 0.215; // robot top left y dimension
    private final double ROBOT_TR_X = 0.190; // robot top right x dimension
    private final double ROBOT_TR_Y = -0.215; // robot top right y dimension
    private final double ROBOT_BR_X = -0.310; // robot bottom right x dimension
    private final double ROBOT_BR_Y = -0.190; // robot bottom right y dimension
    private final double ROBOT_BL_X = -0.310; // robot bottom left x dimension
    private final double ROBOT_BL_Y = 0.190; // robot bottom left y dimension

    //    Number of angles to compute the configuration space for
    protected final int NUM_ANGLES = 360;

    //    Reflected robot polygons
    private final ArrayList<PolygonObstacle> robotPolys = new ArrayList<PolygonObstacle>();

    public CSpace() {

        //        Constructing a robot polygon for each angle
        for (int i = 0; i < NUM_ANGLES; i++) {
            PolygonObstacle robotPoly = new PolygonObstacle();

            double theta = 2* Math.PI / NUM_ANGLES * i;
            robotPoly.addVertex(rotateReflectPt(ROBOT_TL_X, ROBOT_TL_Y, theta));
            robotPoly.addVertex(rotateReflectPt(ROBOT_TR_X, ROBOT_TR_Y, theta));
            robotPoly.addVertex(rotateReflectPt(ROBOT_BR_X, ROBOT_BR_Y, theta));
            robotPoly.addVertex(rotateReflectPt(ROBOT_BL_X, ROBOT_BL_Y, theta));
            robotPoly.close();

            robotPolys.add(robotPoly);
        }
    }

    /**
     * Rotates a point about the origin CCW using a rotation matrix 
     * and then reflects it about the origin (negate x and y coordinates)
     * See http://en.wikipedia.org/wiki/Rotation_matrix for rotation matrix
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @param theta the angle to rotate the point
     * @return the new rotated point
     */
    private Point2D.Double rotateReflectPt(double x, double y, double theta) {
        return new Point2D.Double(-(Math.cos(theta)*x - Math.sin(theta)*y), -(Math.sin(theta)*x + Math.cos(theta)*y));
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
    public PolygonObstacle obsCSpace(PolygonObstacle robotPoly, PolygonObstacle obsPoly) {
        return GeomUtils.convexHull(computeMSum(robotPoly, obsPoly)
                .getVertices());
    }

    /**
     * Computes the 3D configuration space of the provided map.
     * 
     * @param polyMap
     *            the map of the environment to generate a configuration space
     *            of
     * @return the 3D C-Space obstacles of the map obstacles and the
     *         boundaries, where index of the returned list corresponds
     *         to the C-Space obstacles generated for robot's orientation
     *         at index degrees wrt 0
     */
    public ArrayList<ArrayList<PolygonObstacle>> generateCSpace(PolygonMap polyMap) {
        ArrayList<ArrayList<PolygonObstacle>> obs3DCSpace = new ArrayList<ArrayList<PolygonObstacle>>();

        //        Generating the 2D C-Space for each theta
        for (int i=0; i < NUM_ANGLES; i++) {
            ArrayList<PolygonObstacle> obs2DCSpace = new ArrayList<PolygonObstacle>();
            
            //            Computing the configuration spaces for each obstacle
            for (PolygonObstacle obstacle : polyMap.getObstacles()) {
                obs2DCSpace.add(obsCSpace(robotPolys.get(i), obstacle));
            }

            //            Adding an obstacle for the map boundaries
            Rectangle2D.Double envBounds = polyMap.worldRect;

            PolygonObstacle boundaryObs = new PolygonObstacle();

            // bottom
            boundaryObs.addVertex(envBounds.getX(), envBounds.getY());
            boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
                    envBounds.getY());
            obs2DCSpace.add(obsCSpace(robotPolys.get(i), boundaryObs));

            // right
            boundaryObs = new PolygonObstacle();
            boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
                    envBounds.getY());
            boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
                    envBounds.getY() + envBounds.getHeight());
            obs2DCSpace.add(obsCSpace(robotPolys.get(i), boundaryObs));

            // left
            boundaryObs = new PolygonObstacle();
            boundaryObs.addVertex(envBounds.getX(), envBounds.getY());
            boundaryObs.addVertex(envBounds.getX(),
                    envBounds.getY() + envBounds.getHeight());
            obs2DCSpace.add(obsCSpace(robotPolys.get(i), boundaryObs));

            // top
            boundaryObs = new PolygonObstacle();
            boundaryObs.addVertex(envBounds.getX(),
                    envBounds.getY() + envBounds.getHeight());
            boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
                    envBounds.getY() + envBounds.getHeight());
            obs2DCSpace.add(obsCSpace(robotPolys.get(i), boundaryObs));
            
            obs3DCSpace.add(obs2DCSpace);
        }
        
        return obs3DCSpace;
    }
}