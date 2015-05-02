package MotionPlanning;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Challenge.GrandChallengeMap;

/**
 * CSpace is part of the MotionPlanning module. It contains methods for dealing with the 3D
 * configuration space of the environment.
 */
public class CSpace2D {

    public final static double ROBOT_LONGEST_DIM = 0.38;

    private final int NUM_SIDES = 8; // number of sides of the n side polygon
    // approximation of robot
    private PolygonObstacle robotPoly;

    public CSpace2D() {
        // Generating the robot polygon
        robotPoly = new PolygonObstacle();
        // manually defining robot polygon

        for (int i = 0; i < NUM_SIDES; i++) {
            double theta = 2 * Math.PI / NUM_SIDES * i;
            robotPoly.addVertex(ROBOT_LONGEST_DIM * Math.cos(theta),
                    ROBOT_LONGEST_DIM * Math.sin(theta));
        }

        robotPoly.close();
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
     * Computes the 2D configuration space of the provided map.
     * 
     * @param challengeMap
     *            the map of the environment to generate a configuration space
     *            of
     * @param isTest if testing cspace (generates a robot polygon)
     * @return the 2D C-Space obstacles of the map obstacles and the
     *         boundaries
     */
    public ArrayList<PolygonObstacle> generateCSpace(GrandChallengeMap challengeMap, boolean isTest) {

        ArrayList<PolygonObstacle> obs2DCSpace = new ArrayList<PolygonObstacle>();

        //            Adding robot obstacle when testing C-Space
        if (isTest) {
            obs2DCSpace.add(robotPoly);
        }

        //            Computing the configuration spaces for each obstacle

        for (PolygonObstacle obstacle : challengeMap.getPolygonObstacles()) {
            obs2DCSpace.add(obsCSpace(robotPoly, obstacle));
        }

        //            Adding an obstacle for the map boundaries
        Rectangle2D.Double envBounds = challengeMap.getWorldRect();

        PolygonObstacle boundaryObs = new PolygonObstacle();

        // bottom
        boundaryObs.addVertex(envBounds.getX(), envBounds.getY());
        boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
                envBounds.getY());
        obs2DCSpace.add(obsCSpace(robotPoly, boundaryObs));

        // right
        boundaryObs = new PolygonObstacle();
        boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
                envBounds.getY());
        boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
                envBounds.getY() + envBounds.getHeight());
        obs2DCSpace.add(obsCSpace(robotPoly, boundaryObs));

        // left
        boundaryObs = new PolygonObstacle();
        boundaryObs.addVertex(envBounds.getX(), envBounds.getY());
        boundaryObs.addVertex(envBounds.getX(),
                envBounds.getY() + envBounds.getHeight());
        obs2DCSpace.add(obsCSpace(robotPoly, boundaryObs));

        // top
        boundaryObs = new PolygonObstacle();
        boundaryObs.addVertex(envBounds.getX(),
                envBounds.getY() + envBounds.getHeight());
        boundaryObs.addVertex(envBounds.getX() + envBounds.getWidth(),
                envBounds.getY() + envBounds.getHeight());
        obs2DCSpace.add(obsCSpace(robotPoly, boundaryObs));


        return obs2DCSpace;
    }
}