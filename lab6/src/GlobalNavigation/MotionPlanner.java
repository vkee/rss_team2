package GlobalNavigation;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements RRT
 */
public class MotionPlanner {
	// TODO: need to define some constant for the square surrounding the goal
	// point to determine if the robot is at the goal point

	private PolygonMap map;

	public MotionPlanner(PolygonMap map) {

		this.map = map;

	}

	/**
	 * 
	 * @param start
	 * @param goal
	 * @param tolerance
	 *            : as a percentage of the height and width
	 * @return
	 */

	public List<Point2D.Double> getPath(Point2D.Double start,
			Point2D.Double goal, double tolerance) {
		// bruteforce RRT for now should work alex said

		// find a tree data structure or make your own
		// initialize tree with the start coordinate of the map

		Rectangle2D goalRect = new Rectangle2D.Double(goal.x
				- map.worldRect.width * tolerance / 2, goal.y
				- map.worldRect.height * tolerance / 2, map.worldRect.width
				* tolerance, map.worldRect.height * tolerance);

		RRTreeNode treeroot = new RRTreeNode(null, start);
		List<RRTreeNode> nodes = new ArrayList<RRTreeNode>();
		nodes.add(treeroot);

		boolean goalFound = false;
		RRTreeNode goalNode = treeroot;
		int tries = 0;

		// to get the x, y coordinates of the point, generate a random number
		// and multiply by the height and width to get the respective x y

		while (!goalFound && tries < 1000000) {
			// System.out.println(tries);
			if (tries % 1000 == 0)
				System.out.println(tries);

			double testX = Math.random() * map.worldRect.width;
			double testY = Math.random() * map.worldRect.height;
			;
			Point2D.Double testPoint = new Point2D.Double(testX, testY);

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
				if (lineIntersects(p, testPoint, closest.point)) {
					canAdd = false;
					break;
				}
			}

			tries++;

			if (!canAdd)
				continue;

			RRTreeNode newNode = new RRTreeNode(closest, testPoint);
			nodes.add(newNode);

			goalFound = goalRect.contains(testPoint);
			goalNode = newNode;
			// break;

		}

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
		return goalNode.pathFromParent();
	}

	public Point2D.Double getClosestPt(List<Point2D.Double> path,
			Point2D.Double pt) {
		// just check the distance between each of the points in the path and
		// compare to the point and return the closest point
		Point2D.Double pd = new Point2D.Double();
		return pd;
	}

	/*
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

	public boolean lineIntersects(PolygonObstacle p, Point2D.Double x1,
			Point2D.Double x2) {
		Line2D path = new Line2D.Double(x1.x, x1.y, x2.x, x2.y);
		List<Point2D.Double> verts = p.getVertices();
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

class RRTreeNode {

	public RRTreeNode parent;
	public Point2D.Double point;

	public RRTreeNode(RRTreeNode parentNode, Point2D.Double coord) {
		parent = parentNode;
		point = coord;
	}

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