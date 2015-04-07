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

        RRTreeNode startNode = new RRTreeNode(null, start);
        //        The robot starts at 0 radians orientation
        double robotOrientation = 0.0;
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
            double testX = Math.random() * worldWidth - bottomLeftX;
            double testY = Math.random() * worldHeight - bottomLeftY;
            Point2D.Double testPoint = new Point2D.Double(testX, testY);

            //            Finding the closest node in the current RRT tree to the sampled node
            //          The mininum distance between 2 nodes, initialized to the longest distance possible in the map
            double minDist = Math.sqrt(Math.pow(worldHeight, 2) + Math.pow(worldWidth, 2));
            RRTreeNode closestNode = null;
            for (RRTreeNode node : currTreeNodes) // slow search
            {
                double nodeDist = Math.sqrt(Math.pow(node.point.x - testX, 2) + Math.pow(node.point.y - testY, 2));
                if (nodeDist < minDist) {
                    closestNode = node;
                    minDist = nodeDist;
                }
            }

            //            Checking whether the node can be added to the RRT
            boolean canAdd = true;

            //            Checking to see if the path between the current and new point intersects any obstacles
            for (PolygonObstacle obstacle : map.get2DCSpace((int) Math.round(robotOrientation*180/Math.PI))) {
                //              If the path to the new node intersects a polygon, we cannot add the node to the tree
                if (lineIntersects(obstacle, testPoint, closestNode.point)) {
                    canAdd = false;
                    break;
                }
            }
            
//            TODO: Then rotate so that the robot is aligned with the line connecting the 2 points and make sure it doesn’t collide with anything. Then make sure that this path is collision free.

            tries++;

            if (canAdd) {
                //                Adding the new node to the tree with an edge to the closest current node in the RRT
                RRTreeNode newNode = new RRTreeNode(closestNode, testPoint);
                currTreeNodes.add(newNode);

                //                If the test point is inside the goal rectangle, the goal is found
                goalFound = goalRect.contains(testPoint);
                goalNode = newNode;
            }
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
    public double getDist(double pt1X, double pt1Y, double pt2X, double pt2Y) {
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
}