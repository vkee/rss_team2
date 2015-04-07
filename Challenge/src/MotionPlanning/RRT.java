package MotionPlanning;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RRT is part of the MotionPlanning module. It deals with the path planning.
 *
 */
public class RRT {
    private PolygonMap map;
    private final int NUM_TRIES = 1000000;

    public RRT(PolygonMap map) {
        this.map = map;
    }

    /**
     * Attempts 
     * @param start
     * @param goal
     * @param tolerance as a percentage of the height and width of the entire map
     * @return
     */
    public List<Point2D.Double> getPath(Point2D.Double start,
            Point2D.Double goal, double tolerance) {

        //        Tolerance rectangle around the goal
        Rectangle2D goalRect = new Rectangle2D.Double(goal.x
                - map.worldRect.width * tolerance / 2, goal.y
                - map.worldRect.height * tolerance / 2, map.worldRect.width
                * tolerance, map.worldRect.height * tolerance);

        //        Debugging Print Statements
        System.out.println("Map Width: " + map.worldRect.width);
        System.out.println("Map Height: " + map.worldRect.height);
        System.out.println("Goal Rect Width: " + goalRect.getWidth());
        System.out.println("Goal Rect Height: " + goalRect.getHeight());
        System.out.println("Start Point: " + start);
        System.out.println("Goal Point: " + goal);

        RRTreeNode treeroot = new RRTreeNode(null, start);
        List<RRTreeNode> nodes = new ArrayList<RRTreeNode>();
        nodes.add(treeroot);

        boolean goalFound = false;
        RRTreeNode goalNode = treeroot;
        int tries = 0;

        while (!goalFound && tries < NUM_TRIES) {
            // System.out.println(tries);
            if (tries % 1000 == 0)
                System.out.println("Number of tries: " + tries);

            //          TODO: we may want to add a bias to selecting a point near the goal later on
            double testX = Math.random() * map.worldRect.width - .5;
            double testY = Math.random() * map.worldRect.height - .5;
            Point2D.Double testPoint = new Point2D.Double(testX, testY);

            //          The mininum distance between 2 nodes, initialized to the longest distance possible in the map
            double mindist = Math.sqrt(Math.pow(map.worldRect.height, 2)
                    + Math.pow(map.worldRect.width, 2));
            RRTreeNode closest = null;
            for (RRTreeNode node : nodes) // slow search
            {
                double dist = Math.sqrt(Math.pow(node.point.x - testX, 2)
                        + Math.pow(node.point.y - testY, 2));
                if (dist < mindist) {
                    closest = node;
                    mindist = dist;
                }
            }

            boolean canAdd = true;
            for (PolygonObstacle p : map.cspace_obstacles) {
                //              If the path to the new node intersects a polygon, we cannot add the node
                if (lineIntersects(p, testPoint, closest.point)) {
                    canAdd = false;
                    break;
                }
            }

            tries++;

            if (!canAdd) {
                continue;
            }

            RRTreeNode newNode = new RRTreeNode(closest, testPoint);
            nodes.add(newNode);

            goalFound = goalRect.contains(testPoint);
            goalNode = newNode;
            // break;

        }

        RRTreeNode realGoalNode = new RRTreeNode(goalNode, goal);
        nodes.add(realGoalNode);

        // try to connect the new point with the closest point in the tree (run
        // the algorithm david wrote to check if the point intersects any of the
        // obstacles in the map
        // if it does, then do nothing
        // if it doesn't intersect, add it to the tree
        // check if the point is within the goal square tolerance, if it is,
        // just navigate up the tree to the root which will be the start point
        // and return this path

        // this should be a for loop with some defined max num of runs or else
        // may run forever, if no solution found, return none
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
     * Determines whether the line segment representing the path intersects the obstacle
     * @param obs the polygon obstacle
     * @param pt1 the first point of the line segment
     * @param pt2 the second point of the line segment
     * @return whether
     */
    public boolean lineIntersects(PolygonObstacle obs, Point2D.Double pt1,
            Point2D.Double pt2) {
        Line2D path = new Line2D.Double(pt1.x, pt1.y, pt2.x, pt2.y);
        List<Point2D.Double> verts = obs.getVertices();
        for (int i = 0; i < verts.size(); i++) {
            Point2D.Double point1 = verts.get(i);
            Point2D.Double point2 = verts.get((i + 1) % verts.size());
            Line2D side = new Line2D.Double(point1.x, point1.y, point2.x,
                    point2.y);
            if (side.intersectsLine(path))
                return true;
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