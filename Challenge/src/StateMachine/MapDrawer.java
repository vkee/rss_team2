package StateMachine;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.message.rss_msgs.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;

import MotionPlanning.CSpaceTest;
import MotionPlanning.PolygonObstacle;
import Challenge.ConstructionObject;
import Challenge.Fiducial;
import Challenge.GrandChallengeMap;

/**
 * MapDrawer displays data to the GUI.
 *
 */
public class MapDrawer {

	private Publisher<GUIRectMsg> guiRectPub;
	private Publisher<GUIPolyMsg> guiPolyPub;
	private Publisher<GUIEraseMsg> guiErasePub;
	private Publisher<GUIPointMsg> guiPtPub;
	private Publisher<Object> ellipsePub;
	private Publisher<Object> stringPub;
	private Subscriber<OdometryMsg> odometrySub;
	private Publisher<MotionMsg> motionPub;

	// colors
	private final Color LIGHT_BLUE = new Color(115,115,230);
	private final Color DARK_BLUE = new Color(50,40,120);

	public MapDrawer(Publisher<GUIRectMsg> guiRectPub, Publisher<GUIPolyMsg> guiPolyPub,
			Publisher<GUIEraseMsg> guiErasePub, Publisher<GUIPointMsg> guiPtPub,
			Publisher<Object> ellipsePub, Publisher<Object> stringPub,
			Subscriber<OdometryMsg> odometrySub, Publisher<MotionMsg> motionPub){

		this.guiRectPub = guiRectPub;
		this.guiPolyPub = guiPolyPub;
		this.guiErasePub = guiErasePub;
		this.guiPtPub = guiPtPub;
		this.ellipsePub = ellipsePub;
		this.stringPub = stringPub;
		this.odometrySub = odometrySub;
		this.motionPub = motionPub;
	}

	/**
	 * Displays all the contents of the map in MapGUI
	 * @param challengeMap
	 */
	public void displayMap(GrandChallengeMap challengeMap) {
		System.out.println("Starting display map");
		// Based on instanceMain in PolygonMap
		// Erase the GUI
		guiErasePub.publish(new GUIEraseMsg());

		//        Displaying the obstacles
		GUIPolyMsg polyMsg = new GUIPolyMsg();
		for (PolygonObstacle obstacle : challengeMap.getPolygonObstacles()) {
			polyMsg = new GUIPolyMsg();
			fillPolyMsg(polyMsg, obstacle, DARK_BLUE, true, true);
			guiPolyPub.publish(polyMsg);
		}

		// Display the map border
		GUIRectMsg rectMsg = new GUIRectMsg();
		fillRectMsg(rectMsg, challengeMap.getWorldRect(), Color.BLACK, false);
		guiRectPub.publish(rectMsg);

		Point2D.Double robotStart = challengeMap.getRobotStart();
		Point2D.Double robotGoal = challengeMap.getRobotGoal();

		publishEllipse(robotStart.getX(), robotStart.getY(), 0.05, 0.05, Color.RED);
		publishEllipse(robotGoal.getX(), robotGoal.getY(), 0.05, 0.05, Color.GREEN);

		for(Fiducial f : challengeMap.getFiducials()){
			Point2D.Double pos = f.getPosition();
			publishEllipse(f.getPosition().x+0.1, f.getPosition().y+0.1, f.getBottomSize()*4.0, 
					f.getBottomSize()*4.0, f.getBottomColor());
			publishEllipse(f.getPosition().x, f.getPosition().y, f.getTopSize()*4.0, 
					f.getTopSize()*4.0, f.getTopColor());
		}

		System.out.println("Done running displayMap");
	}

	/**
	 * Displays the configuration space of the environment.
	 * @param cspace3D the 3d cspace
	 * @param cspaceIndex the index corresponding to the 2D cspace to be displayed in the provided 3D cspace
	 */
	public void displayMapCSpace(ArrayList<PolygonObstacle> obstacles) {
		//ArrayList<PolygonObstacle> obstacles = cspace3D.get(cspaceIndex);
		// print cspace around obstacles
		for (PolygonObstacle obstacle : obstacles) {
			GUIPolyMsg polyMsg = new GUIPolyMsg();
			fillPolyMsg(polyMsg, obstacle, LIGHT_BLUE, false, true);
			guiPolyPub.publish(polyMsg);
		}

		System.out.println("Done running displayMapCSpace");
	}

	public void displayCObj(ConstructionObject[] constrObjs){
		for(int i=0; i < constrObjs.length; i++) {
			ConstructionObject b = constrObjs[i];
			Point2D.Double pos = b.getPosition();
			System.out.println("ConstructionObject at: "+pos.getX()+", "+pos.getY());
			Rectangle2D.Double r = new Rectangle2D.Double();
			if ( b.getSize() == 2 ) {
				r = new Rectangle2D.Double(b.getPosition().x, b.getPosition().y, 0.05, 0.05);
			} else { 
				r = new Rectangle2D.Double(b.getPosition().x, b.getPosition().y, 0.05, 0.1);
			}
			publishRect(r, true, b.getColor());
			publishString(b.getPosition().x, b.getPosition().y, Integer.toString(i));
		}
	}

	/**
	 * Outputs the path to the MapGUI
	 *
	 * @param points
	 * @param color
	 */
	public void outputPath(List<Point2D.Double> points, java.awt.Color color) {

		GUIPolyMsg poMsg = new GUIPolyMsg();

		PolygonObstacle poly = new PolygonObstacle();

		for (Point2D.Double point : points) {
			poly.addVertex(point.x, point.y);
		}

		fillPolyMsg(poMsg, poly, color, false, false);
		guiPolyPub.publish(poMsg);
	}

	/**
	 * Outputs the path to the MapGUI
	 *
	 * @param points
	 * @param color
	 */
	public void outputPath(List<Point2D.Double> points) {
		Random rand = new Random();
		outputPath(points, new Color(rand.nextFloat(), rand.nextFloat(),rand.nextFloat()));

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
	public void fillRectMsg(GUIRectMsg msg, java.awt.geom.Rectangle2D.Double r, 
			java.awt.Color c, boolean filled) {
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
	public void fillPolyMsg(GUIPolyMsg msg, PolygonObstacle obstacle,
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

	public void publishEllipse(double x, double y, double w, double h, Color color) {
		GUIEllipseMessage ellipseMsg = new GUIEllipseMessage();
		ellipseMsg.x = (float)x;
		ellipseMsg.y = (float)y;
		ellipseMsg.width = (float)w;
		ellipseMsg.height = (float)h;
		ellipseMsg.filled = 1;
		ellipseMsg.c.r = color.getRed();
		ellipseMsg.c.g = color.getGreen();
		ellipseMsg.c.b = color.getBlue();
		ellipsePub.publish(ellipseMsg);
	}

	protected void publishPoint(double x, double y, int shape, Color color) {
		GUIPointMsg msg = new GUIPointMsg();
		msg.x = x;
		msg.y = y;
		msg.shape = shape;
		msg.color.r = color.getRed();
		msg.color.g = color.getGreen();
		msg.color.b = color.getBlue();
		guiPtPub.publish(msg);
	}

	protected void publishRect(Rectangle2D.Double r, boolean filled, Color c) {
		GUIRectMsg msg = new GUIRectMsg();
		msg.filled = filled ? 1 : 0;
		if ( c == null ) {
			c = Color.GREEN;
		}
		msg.c.r = c.getRed();
		msg.c.b = c.getBlue();
		msg.c.g = c.getGreen();
		msg.height = (float) r.height;
		msg.width = (float) r.width;
		msg.x = (float) r.x;
		msg.y = (float) r.y;
		guiRectPub.publish(msg);
	}

	protected void publishPoly(PolygonObstacle obstacle, Color c, boolean filled, boolean closed) {
		List<Point2D.Double> vertices = obstacle.getVertices();
		GUIPolyMsg msg = new GUIPolyMsg();
		msg.numVertices = vertices.size();
		float[] x = new float[msg.numVertices];
		float[] y = new float[msg.numVertices];
		for (int i = 0; i < msg.numVertices; i++){
			x[i] = (float) vertices.get(i).x;
			y[i] = (float) vertices.get(i).y;
		}
		msg.c.r = c.getRed();
		msg.c.b = c.getBlue();
		msg.c.g = c.getGreen();
		msg.x = x;
		msg.y = y;
		msg.closed = closed?1:0;
		msg.filled = filled?1:0;
		guiPolyPub.publish(msg);
	}

	protected void publishString(double x, double y, String s) {
		GUIStringMessage msg = new GUIStringMessage();
		msg.x = (float)x;
		msg.y = (float)y;
		msg.text = s;
		stringPub.publish(msg);
	}

}