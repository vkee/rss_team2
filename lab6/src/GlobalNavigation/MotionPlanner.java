package GlobalNavigation;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements RRT
 */
public class MotionPlanner {
	// TODO: need to define some constant for the square surrounding the goal
	// point to determine if the robot is at the goal point
	public MotionPlanner() {

	}

	public List<Point2D.Double> getPath() {
		// bruteforce RRT for now should work alex said

		// TODO: find a tree data structure or make your own
		// initialize tree with the start coordinate of the map
		// to get the x, y coordinates of the point, generate a random number
		// and multiply by the heigh and width to get the respective x y
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
		List<Point2D.Double> ll = new LinkedList<Point2D.Double>();
		return ll;
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
		Line2D path = new Line2D.Double(100, 100, 200, 200);
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
