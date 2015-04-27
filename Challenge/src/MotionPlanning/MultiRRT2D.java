package MotionPlanning;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Challenge.GrandChallengeMap;

/**
 * MultiRRT is part of the MotionPlanning module to handle finding multiple goals. 
 * It deals with the path planning that may be used in a graph search.
 *
 */
public class MultiRRT2D {
    private GrandChallengeMap map;
    private Rectangle2D.Double worldRect;
    private double worldWidth;
    private double worldHeight;
    private double bottomLeftX;
    private double bottomLeftY;

    private final int NUM_TRIES = 40000; //100K not 1M
    private final int SAMPLE_GOALS = 300;

    public MultiRRT2D(GrandChallengeMap map) {
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
     * @param goals
     * @param tolerance as a percentage of the height and width of the entire map
     * @return
     */
    public RRTreeNode[] getPaths(Point2D.Double start,
            ArrayList<Point2D.Double> goals, double tolerance) {

        int goalsFound = 0;
        final int numGoals = goals.size();

        Rectangle2D[] goalRects = new Rectangle2D[numGoals];
        RRTreeNode[] goalEndPoints = new RRTreeNode[numGoals];
        List<Point2D.Double>[] goalPaths = (List<Point2D.Double>[]) new ArrayList[numGoals];


        for (int i=0; i<numGoals; i++)
        {
            Point2D.Double goal = goals.get(i);
            //        Tolerance rectangle around the goal
            goalRects[i] = new Rectangle2D.Double(goal.x - worldWidth * tolerance / 2, 
                    goal.y - worldHeight * tolerance / 2, worldWidth * tolerance, worldHeight * tolerance);
        }

        //        The orientation of the robot
        //        Note that the robot starts at 0 radians on the unit circle
        //        and can be oriented between 0 and 2PI radians
        double robotOrientation = 0.0;

        RRTreeNode startNode = new RRTreeNode(null, start);
        List<RRTreeNode> currTreeNodes = new ArrayList<RRTreeNode>();
        currTreeNodes.add(startNode);


        //RRTreeNode goalNode = startNode;
        int tries = 0;

        //        Look until find goal or hit max number of tries
        while (goalsFound!=numGoals && tries < NUM_TRIES) {
            if (tries % 1000 == 0) {
                System.out.println("Number of tries: " + tries);
            }

            //          TODO: we may want to add a bias to selecting a point near the goal later on
            //            Getting the random point

            Point2D.Double testPt;

            if (tries % SAMPLE_GOALS < numGoals){
                testPt = goals.get(tries%SAMPLE_GOALS);
            }			//TODO: continues to sample even if its already found... this is just in case there is a better path to be found

            else {
                double testX = Math.random() * worldWidth + bottomLeftX;
                double testY = Math.random() * worldHeight + bottomLeftY;
                testPt = new Point2D.Double(testX, testY);
            }

            //            Finding the closest node in the current RRT tree to the sampled node
            //          The minimum distance between 2 nodes, initialized to the longest distance possible in the map
            double minDist = Math.sqrt(Math.pow(worldHeight, 2) + Math.pow(worldWidth, 2));
            RRTreeNode closestNode = null;
            //System.out.println("Size of Tree " + currTreeNodes.size());
            for (RRTreeNode node : currTreeNodes) // slow search
            {
                //System.out.println("Node Coord: " + node.toString());
                double nodeDist = Math.sqrt(Math.pow(node.point.x - testPt.x, 2) + Math.pow(node.point.y - testPt.y, 2));
                //System.out.println("Node Dist: " + nodeDist);
                if (nodeDist < minDist) {
                    closestNode = node;
                    minDist = nodeDist;
                }
            }

            //            Checking whether the node can be added to the RRT
            boolean ptInObs = ptInObs(testPt);			//why check, wont you discover this during line test? TODO
            //System.out.println("pt in obs "+Boolean.toString(ptInObs));
            
            if (!ptInObs) {
            	

                    //                Checking that there is a path in the robot orientation when moving straight to the point
                    boolean noClearPath = lineIntersectsObs(testPt, closestNode.point);

                   // System.out.println("no clear path "+Boolean.toString(noClearPath));
                    
                    if (!noClearPath) 
                    	{
                        //                Adding the new node to the tree with an edge to the closest current node in the RRT
                        RRTreeNode newNode = new RRTreeNode(closestNode, testPt);
                        currTreeNodes.add(newNode);

                        //System.out.println("added a node, now:" + currTreeNodes.size());

                        //                If the test point is inside the goal rectangle, the goal is found
                        for (int i=0; i<numGoals; i++)
                        	{if (goalRects[i].contains(testPt))
                        		{if (goalEndPoints[i] == null)
                        			{goalsFound++;
                        			goalEndPoints[i] = newNode;}
                        		else if (goalEndPoints[i].distFromRoot > newNode.distFromRoot)
                        			{goalEndPoints[i] = newNode;}
                        		}
                        	}
                    	}
                    if (goalsFound==numGoals) System.out.println("Found: "+Integer.toString(goalsFound)+" of "+Integer.toString(numGoals));
                }

            tries++;
        }

        if (goalsFound!=numGoals) {
            System.err.println("ALERT: SOME GOALS NOT FOUND! "+Integer.toString(goalsFound)+" of "+Integer.toString(numGoals));
        }

        //        Regardless of whether the goal is found (this executes even when RRT terminates),
        //        add the goal as the final node with its parent being the last node that could be added
        //        TODO: I don't think that this is a good thing to do... - Vincent
        //RRTreeNode realGoalNode = new RRTreeNode(goalNode, goal);
        //currTreeNodes.add(realGoalNode);

        /*for (int i=0; i<numGoals; i++)
        	{if (goalEndPoints[i] != null)
        		goalPaths[i] = goalEndPoints[i].pathFromParent();}*/

        //System.out.println("DONE");
        return goalEndPoints;
    }

    

    /**
     * Determines whether the test point is in any of the obstacles in the 2D C-Space specified by the index
     * @param index the index that specifies the 2D CSpace 
     * @param testPt the point being checked
     * @return whether the test point is in any of the obstacles
     */
    private boolean ptInObs(Point2D.Double testPt) {
        //      Checking to see if the path between the current and new point intersects any obstacles
        //System.out.println("how many obs:" + map.get2DCSpace(index).size());

        for (PolygonObstacle obstacle : map.cSpace) {
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
    private boolean lineIntersectsObs(Point2D.Double testPt, Point2D.Double closestNodePt) {
        //      Checking to see if the path between the current and new point intersects any obstacles
        for (PolygonObstacle obstacle : map.cSpace) {
            //              If the path to the new node intersects a polygon, we cannot add the node to the tree
            if (lineIntersects(obstacle, testPt, closestNodePt)) {
                return true;
            }
        }

        return false;
    }

 

    /**
     * Returns the distance between two points
     * 
     * @param pt1X the x coordinate of point 1
     * @param pt1Y the y coordinate of point 1
     * @param pt2X the x coordinate of point 2
     * @param pt2Y the y coordinate of point 2
     */
    public static double getDist(double pt1X, double pt1Y, double pt2X, double pt2Y) {
        return Math.sqrt((pt1X - pt2X) * (pt1X - pt2X) + (pt1Y - pt2Y)
                * (pt1Y - pt2Y));
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

