package MotionPlanning;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Challenge.GrandChallengeMap;

/**
 * RRT is part of the MotionPlanning module. It deals with the path planning.
 *
 */
public class RRT {
    private GrandChallengeMap map;
    private Rectangle2D.Double worldRect;
    private double worldWidth;
    private double worldHeight;
    private double bottomLeftX;
    private double bottomLeftY;

    private final int NUM_TRIES = 1000000;

    public RRT(GrandChallengeMap map) {
        this.map = map;
        this.worldRect = map.getWorldRect();
        this.worldWidth = worldRect.getWidth();
        this.worldHeight = worldRect.getHeight();
        this.bottomLeftX = worldRect.getMinX();
        this.bottomLeftY = worldRect.getMinY();
    }

    /**
     * Attempts to find a path 
     * @param start
     * @param goal
     * @param tolerance as a percentage of the height and width of the entire map
     * @return
     */
    public List<Point2D.Double> getPath(Point2D.Double start,
            Point2D.Double goal, double tolerance) {

        //        Tolerance rectangle around the goal
        Rectangle2D goalRect = new Rectangle2D.Double(goal.x - worldWidth * tolerance / 2, 
                goal.y - worldHeight * tolerance / 2, worldWidth * tolerance, worldHeight * tolerance);

        //        Debugging Print Statements
        System.out.println("Map Width: " + worldWidth);
        System.out.println("Map Height: " + worldHeight);
        System.out.println("Goal Rect Width: " + goalRect.getWidth());
        System.out.println("Goal Rect Height: " + goalRect.getHeight());
        System.out.println("Start Point: " + start);
        System.out.println("Goal Point: " + goal);

        //        The orientation of the robot
        //        Note that the robot starts at 0 radians on the unit circle
        //        and can be oriented between 0 and 2PI radians
        double robotOrientation = 0.0;

        RRTreeNode startNode = new RRTreeNode(null, start);
        List<RRTreeNode> currTreeNodes = new ArrayList<RRTreeNode>();
        currTreeNodes.add(startNode);

        boolean goalFound = false;
        RRTreeNode goalNode = startNode;
        int tries = 0;

        //        Look until find goal or hit max number of tries
        while (!goalFound && tries < NUM_TRIES) {
            if (tries % 1000 == 0) {
                System.out.println("Number of tries: " + tries);
            }

            //          TODO: we may want to add a bias to selecting a point near the goal later on
            //            Getting the random point
                        
            double testX = Math.random() * worldWidth - Math.abs(bottomLeftX);
            double testY = Math.random() * worldHeight - Math.abs(bottomLeftY);
            Point2D.Double testPt = new Point2D.Double(testX, testY);
            
            //            Finding the closest node in the current RRT tree to the sampled node
            //          The mininum distance between 2 nodes, initialized to the longest distance possible in the map
            double minDist = Math.sqrt(Math.pow(worldHeight, 2) + Math.pow(worldWidth, 2));
            RRTreeNode closestNode = null;
            //System.out.println("Size of Tree " + currTreeNodes.size());
            for (RRTreeNode node : currTreeNodes) // slow search
            {
                //System.out.println("Node Coord: " + node.toString());
                double nodeDist = Math.sqrt(Math.pow(node.point.x - testX, 2) + Math.pow(node.point.y - testY, 2));
                //System.out.println("Node Dist: " + nodeDist);
                if (nodeDist < minDist) {
                    closestNode = node;
                    minDist = nodeDist;
                }
            }

            //            Checking whether the node can be added to the RRT
            boolean ptInObs = ptInObs(getCSpaceIndex(robotOrientation), testPt);			//why check, wont you discover this during line test? TODO

            if (!ptInObs) {

                //System.out.println("closestNode: " + closestNode.toString());
                //              TODO: Then rotate so that the robot is aligned with the line connecting the 2 points and make sure it doesn't collide with anything. Then make sure that this path is collision free.
                double angle2TestPt = RRT.getAngle(closestNode.point.x, closestNode.point.y, testX, testY);
                
                //                Keeping the angle between 0 and 2PI
                if (angle2TestPt < 0.0) {
                    angle2TestPt += 2*Math.PI; 
                }

                double robotAngleError = (angle2TestPt - robotOrientation) % (2*Math.PI);
                
                //                Keeping the error in angle between -PI and PI so that the robot minimizes rotation
                //robotAngleError = (robotAngleError+Math.PI)%(2*Math.PI)-Math.PI;
                
                if (robotAngleError > Math.PI) {				
                    robotAngleError -= 2*Math.PI;
                }
                
                //System.out.println("angle bad check:" + (Math.abs(robotAngleError) > Math.PI));

                //                Checking whether the robot will collide with any obstacles while it rotates to face the new point
                boolean collisionInRotation = collisionInRotation(robotOrientation, robotAngleError, closestNode.point);

                if (!collisionInRotation) {
                    //                Making the robot aligned pointing from the closest node to the test point
                    double testRobotOrientation = angle2TestPt;
                    //                Checking that there is a path in the robot orientation when moving straight to the point
                    boolean noClearPath = lineIntersectsObs(getCSpaceIndex(testRobotOrientation), testPt, closestNode.point);

                    if (!noClearPath) {
                        //                Adding the new node to the tree with an edge to the closest current node in the RRT
                        RRTreeNode newNode = new RRTreeNode(closestNode, testPt);
                        currTreeNodes.add(newNode);
                        
                        robotOrientation = angle2TestPt;
                        
                        //System.out.println("added a node, now:" + currTreeNodes.size());

                        //                If the test point is inside the goal rectangle, the goal is found
                        goalFound = goalRect.contains(testPt);
                        goalNode = newNode;
                    }
                }
            }

            tries++;
        }

        if (!goalFound) {
            System.err.println("ALERT: GOAL WAS NOT FOUND!");
        }

        //        Regardless of whether the goal is found (this executes even when RRT terminates),
        //        add the goal as the final node with its parent being the last node that could be added
        //        TODO: I don't think that this is a good thing to do... - Vincent
        RRTreeNode realGoalNode = new RRTreeNode(goalNode, goal);
        currTreeNodes.add(realGoalNode);

        return realGoalNode.pathFromParent();
    }

    /**
     * Determines whether the robot will collide with any obstacles as it rotates to face the next waypoint
     * @param robotOrientation the robot's current orientation
     * @param robotAngleError the angles the robot will have to rotate
     * @param robotLoc the robot's current location
     * @return whether the robot will collide with any obstacles
     */
    private boolean collisionInRotation(double robotOrientation, double robotAngleError, Point2D.Double robotLoc) {
        int robotIndex = getCSpaceIndex(robotOrientation);
        
        int errorIndex = getErrorIndex(robotAngleError);
        
        int goalIndex = (robotIndex + errorIndex + CSpace.NUM_ANGLES) % CSpace.NUM_ANGLES;
        
       // System.out.println("robotIndex: " + robotIndex);
       // System.out.println("errorIndex: " + errorIndex);

        //        If no rotation required, no collision
        if (0 == errorIndex) {
            return false;
        } else {
            //            Direction to rotate
            int direction = (int) (errorIndex)/Math.abs(errorIndex);
//            System.out.println("direction: " + direction);

            while (robotIndex != goalIndex){
                //System.out.println("robotIndex for pt in obs: " + robotIndex);
                //            If the point is in an obstacle, return collision
                if (ptInObs(robotIndex, robotLoc)) {
                    System.out.println("collision at angle "+robotIndex);
                	return true;
                } else {
                    robotIndex += direction + CSpace.NUM_ANGLES;  //neg values not handled well with mod
                    robotIndex %= CSpace.NUM_ANGLES;
                    //System.out.println("trying next:"+robotIndex);
                }
            }

            return false;
        }
    }

    /**
     * Determines whether the test point is in any of the obstacles in the 2D C-Space specified by the index
     * @param index the index that specifies the 2D CSpace 
     * @param testPt the point being checked
     * @return whether the test point is in any of the obstacles
     */
    private boolean ptInObs(int index, Point2D.Double testPt) {
        //      Checking to see if the path between the current and new point intersects any obstacles
        //System.out.println("how many obs:" + map.get2DCSpace(index).size());
    	
    	for (PolygonObstacle obstacle : map.get2DCSpace(index)) {
            //              If the path to the new node intersects a polygon, we cannot add the node to the tree
            if (obstacle.contains(testPt)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether the line determined by the two points intersects any obstacles in the 2D C-Space specified by the index
     * @param index the index that specifies the 2D CSpace 
     * @param testPt the first point of the line
     * @param closestNodePt the second point of the line
     * @return whether the line intersects any obstacles
     */
    private boolean lineIntersectsObs(int index, Point2D.Double testPt, Point2D.Double closestNodePt) {
        //      Checking to see if the path between the current and new point intersects any obstacles
        for (PolygonObstacle obstacle : map.get2DCSpace(index)) {
            //              If the path to the new node intersects a polygon, we cannot add the node to the tree
            if (lineIntersects(obstacle, testPt, closestNodePt)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates the index of the 2D CSpace in the 3D CSpace data structure
     * @param robotOrientation the orientation of the robot in radians
     * @return the index from 0 - 359
     */
    private int getCSpaceIndex(double robotOrientation) {
        return ((int) Math.round(robotOrientation*180/Math.PI)) % (CSpace.NUM_ANGLES);
    }
    
    /**
     * Converts the error in radians to degrees between -359 and 359 deg
     * @param robotAngleError the robot's angle of error in radians from -pi to pi
     * @return the index from -180 to 180
     */
    private int getErrorIndex(double robotAngleError) {
        int errorIndex = (int) Math.round(robotAngleError*180/Math.PI);
        
        /*
        /// +pi %2pi -pi to put between -pi and pi? TODO?

//        Keeping the error index between -2PI and 2PI
        while (errorIndex >= CSpace.NUM_ANGLES) {
            System.out.println("getErrorIndex -= 360");
            errorIndex -= 360;
        }
        
        while (errorIndex <= -CSpace.NUM_ANGLES) {
            System.out.println("getErrorIndex += 360");
            errorIndex += 360;
        }
             */   
        return errorIndex;
    }
    
    /**
     * Returns the distance between two points
     * 
     * @param pt1X the x coordinate of point 1
     * 
     * @param pt1Y the y coordinate of point 1
     * 
     * @param pt2X the x coordinate of point 2
     * 
     * @param pt2Y the y coordinate of point 2
     */
    public static double getDist(double pt1X, double pt1Y, double pt2X, double pt2Y) {
        return Math.sqrt((pt1X - pt2X) * (pt1X - pt2X) + (pt1Y - pt2Y)
                * (pt1Y - pt2Y));
    }

    /**
     * Returns the angle between two points between -PI to PI (due to use of atan2)
     * starting from point 1 to point 2
     * 
     * @param pt1X the x coordinate of point 1
     * 
     * @param pt1Y the y coordinate of point 1
     * 
     * @param pt2X the x coordinate of point 2
     * 
     * @param pt2Y the y coordinate of point 2
     */
    public static double getAngle(double pt1X, double pt1Y, double pt2X, double pt2Y) {
        return Math.atan2(pt2Y - pt1Y, pt2X - pt1X);
    }

    /**
     * Determines whether the line segment representing the path between the two points intersects the obstacle
     * @param obs the polygon obstacle
     * @param pt1 the first point of the line segment
     * @param pt2 the second point of the line segment
     * @return whether
     */
    public boolean lineIntersects(PolygonObstacle obs, Point2D.Double pt1,
            Point2D.Double pt2) {
        Line2D path = new Line2D.Double(pt1.x, pt1.y, pt2.x, pt2.y);
        List<Point2D.Double> verts = obs.getVertices();
        //        Constructing segments between all adjacent vertices to see if the 
        //        line segment intersects any of them
        for (int i = 0; i < verts.size(); i++) {
            Point2D.Double point1 = verts.get(i);
            Point2D.Double point2 = verts.get((i + 1) % verts.size());
            Line2D side = new Line2D.Double(point1.x, point1.y, point2.x,
                    point2.y);
            if (side.intersectsLine(path)) {
                return true;
            }
        }
        return false;
    }



}

/**
 * RRTreeNode represents a node in a tree running RRT
 */
class RRTreeNode {
    public RRTreeNode parent;
    public Point2D.Double point;

    /**
     * Creates an RRTreeNode
     * @param parentNode the parent node of the current node
     * @param coord the coordinates of the current node
     */
    public RRTreeNode(RRTreeNode parentNode, Point2D.Double coord) {
        parent = parentNode;
        point = coord;
    }

    /**
     * Returns the path to the current node
     * @return the path
     */
    public List<Point2D.Double> pathFromParent() {
        List<Point2D.Double> pathBack = new ArrayList<Point2D.Double>();
        RRTreeNode currNode = this;
        while (currNode != null) {
            pathBack.add(currNode.point);
            currNode = currNode.parent;
        }
        Collections.reverse(pathBack);
        return pathBack;
    }

    @Override
    public String toString() {
        return "point=" + point.toString();
    }
    

}