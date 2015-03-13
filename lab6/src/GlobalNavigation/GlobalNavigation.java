package GlobalNavigation;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.ros.message.rss_msgs.*;
import org.ros.message.MessageListener;

import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.node.parameter.ParameterTree;
import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;

import LocalNavigation.*;

import java.io.*;

public class GlobalNavigation implements NodeMain {
	private Publisher<org.ros.message.lab6_msgs.GUIRectMsg> guiRectPub;
	private Publisher<org.ros.message.lab6_msgs.GUIPolyMsg> guiPolyPub;
	private Publisher<org.ros.message.lab5_msgs.GUIEraseMsg> guiErasePub;
	private Publisher<org.ros.message.lab5_msgs.GUIPointMsg> guiPtPub;
	private Subscriber<OdometryMsg> odometrySub;
	private Publisher<MotionMsg> motionPub;

	private ColorMsg redMsg;
	private ColorMsg greenMsg;
	private ColorMsg blueMsg;
	private ColorMsg blackMsg;
	private List<Point2D.Double> waypoints;
	private int currWaypt = 0;

	private String mapFileName;
	private PolygonMap polyMap;
	private CSpace cSpace;
	private MotionPlanner motionPlanner;

	// Robot Odometry
	public double robotX = 0.0; // in meters
	public double robotY = 0.0; // in meters
	public double robotTheta = 0.0; // in radians

	// Shift in Robot Position adjusting for robotX,Y, and theta not always
	// zeroed at start
	// Essentially the shift to get it to the start position, assuming that the
	// robot is at the starting position
	private double xShift = 0.0; // in meters
	private double yShift = 0.0; // in meters
	private double thetaShift = 0.0; // in radians

	// The waypoint tolerance defining whether the robot is at a waypoint
	private final double WAYPT_TOL = 0.05; // in meters
	private final double WAYPT_TOL_THETA = 0.1; // in radians

	double FWD_GAIN = .5; // may need to change to 0.1 if runs into things and
							// off waypoints
	double ROT_GAIN = .2;

	private boolean navWaypts = false;
	private boolean atGoal = false;

	public GlobalNavigation() {
		cSpace = new CSpace();
	}

	@Override
	public void onStart(Node node) {
		// To wait for the GUI
		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}

		guiRectPub = node.newPublisher("gui/Rect", "lab6_msgs/GUIRectMsg");
		guiPolyPub = node.newPublisher("gui/Poly", "lab6_msgs/GUIPolyMsg");
		guiErasePub = node.newPublisher("gui/Erase", "lab5_msgs/GUIEraseMsg");
		guiPtPub = node.newPublisher("gui/Point", "lab5_msgs/GUIPointMsg");
		motionPub = node.newPublisher("command/Motors", "rss_msgs/MotionMsg");
		odometrySub = node.newSubscriber("/rss/odometry",
				"rss_msgs/OdometryMsg");

		odometrySub
				.addMessageListener(new MessageListener<org.ros.message.rss_msgs.OdometryMsg>() {
					@Override
					public void onNewMessage(
							org.ros.message.rss_msgs.OdometryMsg message) {
						robotX = message.x;
						robotY = message.y;
						robotTheta = message.theta;
						// System.out.println("navWaypts state: " + navWaypts);
						// System.out.println("atGoal state: " + atGoal);
						if (navWaypts && !atGoal) {
							wayptNav();
						}
					}
				});

		// Reading in a map file whose name is set as the parameter mapFileName
		ParameterTree paramTree = node.newParameterTree();
		mapFileName = paramTree.getString(node.resolveName("~/mapFileName"));
		try {
			polyMap = new PolygonMap(mapFileName);
			List<PolygonObstacle> obsCSpaces = cSpace.envConfSpace(polyMap);
			// System.out.println("length of spaces " + obsCSpaces.size());
			polyMap.addCSpace(obsCSpaces);
			motionPlanner = new MotionPlanner(polyMap);

		} catch (Exception e) {

		}
		displayMapCSpace();
		displayMap(); // --Works: Remember to plug into Robot
		// testConvexHull(); // -- Works need to find a set of "non-trivial"
		// points.
		// Remember to turn off displayMap when testing
		Point2D.Double robotStart = polyMap.getRobotStart();
		waypoints = motionPlanner.getPath(robotStart, polyMap.getRobotGoal(),
				.2);

		// System.out.println(waypoints);

		// Updating the shifts so that the robot is at 0,0 with 0 rad heading at
		// start
		// xShift = robotStart.getX() - robotX;
		// yShift = robotStart.getY() - robotY;
		// thetaShift = 0 - robotTheta;
		// TODO: worst case get rid of these shifts and just restart morcboard
		// each run

		outputPath(waypoints, MapGUI.makeRandomColor());

		System.out.println("Number of waypoints: " + waypoints.size());

		navWaypts = true;
	}

	/**
	 * Navigates the robot from waypoint to waypoint, called by odometry handler
	 * Assuming that the robot starts at the first waypoint
	 * 
	 * @param points
	 *            the set of waypoints for the robot to navigate
	 */
	private void wayptNav() {
		// System.out.println("In waypt nav");
		Point2D.Double wayPoint = waypoints.get(currWaypt);

		double currX = robotX;
		double currY = robotY;
		double currTheta = robotTheta;

		// Accounting for robot initial position without restarting orcboard or
		// reseting odometry
		// double currX = robotX + xShift;
		// double currY = robotY + yShift;
		// double currTheta = robotTheta + thetaShift;

		// System.out.println("currX: " + currX);
		// System.out.println("currY: " + currY);
		System.out.println("currTheta: " + currTheta);

		double xError = wayPoint.getX() - currX;
		double yError = wayPoint.getY() - currY;
		double thetaToPoint = Math.atan2(yError, xError);

		// Keeping within 0 to 2PI
		if (thetaToPoint < 0) {
			thetaToPoint += 2 * Math.PI;
		}

		// System.out.println("xError: " + xError);
		// System.out.println("yError: " + yError);
		System.out.println("thetaToPoint: " + thetaToPoint);

		// make sure currTheta is actual angle of robot, may need to play with
		// thetashift
		// TODO make sure that this is actually the theta error
		double thetaError = currTheta - thetaToPoint;

		System.out.println("Theta Error: " + thetaError);

		// Keeping the theta error always in the range -pi to pi
		if (thetaError < -Math.PI) {
			thetaError += 2 * Math.PI;
		} else if (thetaError > Math.PI) {
			thetaError -= 2 * Math.PI;
		}

		System.out.println("Theta Error after correction: " + thetaError);

		// While not at the current waypoint, adjust proportionally to the error

		MotionMsg msg = new MotionMsg();

		// adjust theta error first
		if (Math.abs(thetaError) > WAYPT_TOL_THETA) {
			msg.rotationalVelocity = -Math.min(ROT_GAIN * thetaError, 0.25);
			msg.translationalVelocity = 0.0;
			// only when theta has been reached, adjust translation
		} else if ((xError > WAYPT_TOL) || (yError > WAYPT_TOL)) {
			msg.translationalVelocity = Math.min(
					FWD_GAIN * motionPlanner.getDist(0.0, 0.0, xError, yError),
					0.5);
			msg.rotationalVelocity = 0;
		} else {
			msg.translationalVelocity = 0.0;
			msg.rotationalVelocity = 0.0;
			if (currWaypt < waypoints.size() - 1) {
				System.out.println("WAYPOINT: " + currWaypt + " REACHED at X: "
						+ wayPoint.getX() + " Y: " + wayPoint.getY()
						+ " ROBOT-X:" + currX + " ROBOT-Y:" + currY + "out of "
						+ waypoints.size() + " waypoints.");
				currWaypt += 1;

			} else {
				atGoal = true;
				System.out.println("At the goal!");
			}
		}
		motionPub.publish(msg);

		System.out.println("Trans Vel: " + msg.translationalVelocity);
		System.out.println("Rot Vel: " + msg.rotationalVelocity);

	}

	/**
	 * Outputs the path to the MapGUI
	 * 
	 * @param points
	 * @param color
	 */
	private void outputPath(List<Point2D.Double> points, java.awt.Color color) {

		GUIPolyMsg poMsg = new GUIPolyMsg();

		PolygonObstacle poly = new PolygonObstacle();

		for (Point2D.Double point : points) {
			poly.addVertex(point.x, point.y);
		}
		// poly.close();

		// System.out.println(poly);
		fillPolyMsg(poMsg, poly, color, false, false);
		guiPolyPub.publish(poMsg);

		//
		// GUIPointMsg ptMsg = new GUIPointMsg();
		//
		// for (Point2D.Double point : points) {
		// //System.out.println(point);
		// fillPointMsg(ptMsg, point, color);
		// guiPtPub.publish(ptMsg);
		// }
	}

	// /**
	// * Tests the convex hull algorithm in GeomUtils
	// */

	private void testConvexHull() {
		// // TODO come up with more non trivial sets of test points to test
		// // convexHull
		List<Point2D.Double> points = new ArrayList<Point2D.Double>();
		//
		// Random r = new Random();
		// double randomDoubles[] = new double[15];
		// for (double d : randomDoubles)
		// d = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

		// Point2D.Double p1 = new
		// Point2D.Double(randomDoubles[1],randomDoubles[8]);
		// Point2D.Double p2 = new
		// Point2D.Double(randomDoubles[2],randomDoubles[9]);
		// Point2D.Double p3 = new
		// Point2D.Double(randomDoubles[3],randomDoubles[10]);
		// Point2D.Double p4 = new
		// Point2D.Double(randomDoubles[4],randomDoubles[11]);
		// Point2D.Double p5 = new
		// Point2D.Double(randomDoubles[5],randomDoubles[12]);
		// Point2D.Double p6 = new
		// Point2D.Double(randomDoubles[6],randomDoubles[13]);
		// Point2D.Double p7 = new
		// Point2D.Double(randomDoubles[7],randomDoubles[14]);
		// guiErasePub.publish(new GUIEraseMsg());
		//
		// GUIPointMsg ptMsg = new GUIPointMsg();
		// // LocalNavigation ln = new LocalNavigation();
		// redMsg = new ColorMsg();
		// redMsg.r = 255;
		// redMsg.g = 0;
		// redMsg.b = 0;
		// ptMsg.color = redMsg;
		//
		// // double rangeMin = 0;
		// // double rangeMax = 4;
		// //
		// // Random r = new Random();
		// // double randomDoubles[] = new double[15];
		// // for (double d : randomDoubles)
		// // d = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		//
		// // Point2D.Double p1 = new
		// Point2D.Double(randomDoubles[1],randomDoubles[8]);
		// // Point2D.Double p2 = new
		// Point2D.Double(randomDoubles[2],randomDoubles[9]);
		// // Point2D.Double p3 = new
		// Point2D.Double(randomDoubles[3],randomDoubles[10]);
		// // Point2D.Double p4 = new
		// Point2D.Double(randomDoubles[4],randomDoubles[11]);
		// // Point2D.Double p5 = new
		// Point2D.Double(randomDoubles[5],randomDoubles[12]);
		// // Point2D.Double p6 = new
		// Point2D.Double(randomDoubles[6],randomDoubles[13]);
		// // Point2D.Double p7 = new
		// Point2D.Double(randomDoubles[7],randomDoubles[14]);
		//
		// points.add(p1);
		// points.add(p2);
		// points.add(p3);
		// points.add(p4);
		// points.add(p5);
		// points.add(p6);
		// points.add(p7);
		//
		// for (Point2D.Double point : points) {
		// ptMsg.x = point.getX();
		// ptMsg.y = point.getY();
		// guiPtPub.publish(ptMsg);
		// }
		//
		// //
		// // ptMsg.x = p2.getX();
		// // ptMsg.y = p2.getY();
		// // guiPtPub.publish(ptMsg);
		// //
		// // ptMsg.x = p3.getX();
		// // ptMsg.y = p3.getY();
		// // guiPtPub.publish(ptMsg);
		// //
		// // ptMsg.x = p4.getX();
		// // ptMsg.y = p4.getY();
		// // guiPtPub.publish(ptMsg);
		//
		// GUIPolyMsg gpm = new GUIPolyMsg();
		// GlobalNavigation.fillPolyMsg(gpm, GeomUtils.convexHull(points),
		// MapGUI.makeRandomColor(), false, true);
		//
		// guiPolyPub.publish(gpm);
		//
		// System.out.println("Done running testConvexHull");
		// }
		Point2D.Double p1 = new Point2D.Double(0.5, 0.8);
		Point2D.Double p2 = new Point2D.Double(1.5, 2.2);
		Point2D.Double p3 = new Point2D.Double(3.0, 3.2);
		Point2D.Double p4 = new Point2D.Double(3.3, 0.3);
		Point2D.Double p5 = new Point2D.Double(1.0, 1.0);
		Point2D.Double p6 = new Point2D.Double(2.7, 2.0);
		Point2D.Double p7 = new Point2D.Double(4.2, 3.5);

		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);

		GUIPointMsg ptMsg = new GUIPointMsg();
		Color color = MapGUI.makeRandomColor();
		for (Point2D.Double point : points) {
			fillPointMsg(ptMsg, point, color);
			guiPtPub.publish(ptMsg);
		}

		//
		// ptMsg.x = p2.getX();
		// ptMsg.y = p2.getY();
		// guiPtPub.publish(ptMsg);
		//
		// ptMsg.x = p3.getX();
		// ptMsg.y = p3.getY();
		// guiPtPub.publish(ptMsg);
		//
		// ptMsg.x = p4.getX();
		// ptMsg.y = p4.getY();
		// guiPtPub.publish(ptMsg);

		// GUIPolyMsg gpm = new GUIPolyMsg();
		// GlobalNavigation.fillPolyMsg(gpm, GeomUtils.convexHull(points),
		// MapGUI.makeRandomColor(), false, true);

		// guiPolyPub.publish(gpm);

		System.out.println("Done running testConvexHull");
	}

	/**
	 * Displays all the contents of the map in MapGUI
	 */
	private void displayMap() {

		// should be doing something similar to the below, from instanceMain in
		// PolygonMap
		// Based on instanceMain in PolygonMap

		guiErasePub.publish(new GUIEraseMsg());

		GUIRectMsg rectMsg = new GUIRectMsg();
		GlobalNavigation.fillRectMsg(rectMsg, polyMap.getWorldRect(), null,
				false);
		guiRectPub.publish(rectMsg);
		GUIPolyMsg polyMsg = new GUIPolyMsg();
		for (PolygonObstacle obstacle : polyMap.getObstacles()) {
			polyMsg = new GUIPolyMsg();
			GlobalNavigation.fillPolyMsg(polyMsg, obstacle,
					MapGUI.makeRandomColor(), true, true);
			guiPolyPub.publish(polyMsg);
		}

		System.out.println("Done running displayMap");
	}

	/**
	 * Displays the configuration space of the environment.
	 */
	private void displayMapCSpace() {
		List<PolygonObstacle> obsCSpaces = cSpace.envConfSpace(polyMap);

		// Erase the GUI
		guiErasePub.publish(new GUIEraseMsg());

		GUIRectMsg rectMsg = new GUIRectMsg();
		GlobalNavigation.fillRectMsg(rectMsg, polyMap.getWorldRect(),
				MapGUI.makeRandomColor(), false);
		guiRectPub.publish(rectMsg);
		GUIPolyMsg polyMsg = new GUIPolyMsg();
		for (PolygonObstacle obstacle : obsCSpaces) {
			polyMsg = new GUIPolyMsg();
			GlobalNavigation.fillPolyMsg(polyMsg, obstacle,
					MapGUI.makeRandomColor(), false, true);
			guiPolyPub.publish(polyMsg);
		}
		System.out.println("Done running displayMapCSpace");

	}

	/**
	 * Given an empty GUIPointMsg and the appropriate parameters, fills in the
	 * message
	 * 
	 * @param msg
	 *            the empty GUIPointMsg to be filled
	 * @param point
	 * @param color
	 * @param shape
	 */
	public void fillPointMsg(GUIPointMsg msg,
			java.awt.geom.Point2D.Double point, java.awt.Color color) {
		msg.x = (float) point.getX();
		msg.y = (float) point.getY();
		ColorMsg colorMsg = new ColorMsg();
		colorMsg.r = color.getRed();
		colorMsg.g = color.getGreen();
		colorMsg.b = color.getBlue();
		msg.color = colorMsg;
		// msg.shape = 0L;
	}

	/**
	 * Given an empty GUIRectMsg and the appropriate parameters, fills in the
	 * message
	 * 
	 * @param msg
	 *            the empty GUIRectMsg to be filled
	 * @param r
	 *            the rectangle as a Java Rectangle 2D double
	 * @param c
	 *            the color of the rectangle
	 * @param filled
	 *            whether the rectangle should be filled
	 */
	public static void fillRectMsg(GUIRectMsg msg,
			java.awt.geom.Rectangle2D.Double r, java.awt.Color c, boolean filled) {
		msg.x = (float) r.getX();
		msg.y = (float) r.getY();
		msg.width = (float) r.getWidth();
		msg.height = (float) r.getHeight();
		msg.filled = filled ? 1 : 0;
		if (c != null) {
			ColorMsg colorMsg = new ColorMsg();
			colorMsg.r = c.getRed();
			colorMsg.g = c.getGreen();
			colorMsg.b = c.getBlue();
			msg.c = colorMsg;
		}
	}

	/**
	 * Given an empty GUIPolyMsg and the appropriate parameters, fills in the
	 * message
	 * 
	 * @param msg
	 *            the empty GUIPolyMsg to be filled
	 * @param obstacle
	 *            the obstacle forming the polygon
	 * @param c
	 *            the color of the polygon
	 * @param filled
	 *            whether the polygon is filled
	 * @param closed
	 *            whether the obstacle is closed
	 */
	public static void fillPolyMsg(GUIPolyMsg msg, PolygonObstacle obstacle,
			java.awt.Color c, boolean filled, boolean closed) {

		List<Point2D.Double> vertices = obstacle.getVertices();
		msg.numVertices = vertices.size();
		float[] x = new float[msg.numVertices];
		float[] y = new float[msg.numVertices];

		for (int i = 0; i < msg.numVertices; i++) {
			Point2D.Double vertex = vertices.get(i);
			x[i] = (float) vertex.getX();
			y[i] = (float) vertex.getY();
		}
		msg.x = x;
		msg.y = y;
		ColorMsg colorMsg = new ColorMsg();
		colorMsg.r = c.getRed();
		colorMsg.g = c.getGreen();
		colorMsg.b = c.getBlue();
		msg.c = colorMsg;
		// msg.filled = 0;
		msg.filled = filled ? 1 : 0;
		msg.closed = closed ? 1 : 0;
	}

	@Override
	public void onShutdown(Node node) {
		if (node != null) {
			node.shutdown();
		}
	}

	@Override
	public void onShutdownComplete(Node node) {
	}

	@Override
	public GraphName getDefaultNodeName() {
		return new GraphName("rss/globalNav");
	}
}