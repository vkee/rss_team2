package MotionPlanning;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import org.ros.message.Challenge_msgs.*;

import Challenge.GrandChallengeMap;

/**
 * Tests the CSpace Module.
 *
 */
public class CSpaceTest implements NodeMain{
    private Publisher<GUIRectMsg> guiRectPub;
    private Publisher<GUIPolyMsg> guiPolyPub;
    private Publisher<GUIEraseMsg> guiErasePub;
    private Publisher<GUIPointMsg> guiPtPub;
    private Publisher<Object> ellipsePub;
    private Publisher<Object> stringPub;
    
    private ColorMsg redMsg;
    private ColorMsg greenMsg;
    private ColorMsg blueMsg;
    private ColorMsg blackMsg;

    private String mapFileName;
    private GrandChallengeMap challengeMap;
    private CSpace cSpace;
    private ArrayList<ArrayList<PolygonObstacle>> obsCSpaces = new ArrayList<ArrayList<PolygonObstacle>>();

    // colors
    private Color lightBlue = new Color(115,115,230);
    private Color darkBlue = new Color(50,40,120);

    //    Index of the obstacle cspace list to display 
    //    (corresponds to the angle in degrees if num angles is 360)
    private final int cspaceIndex = 90;

    public CSpaceTest() {
        cSpace = new CSpace();
    }

    @Override
    public void onStart(Node node) {
        stringPub = node.newPublisher("gui/String", "Challenge_msgs/GUIStringMessage");
        ellipsePub = node.newPublisher("/gui/Ellipse", "Challenge_msgs/GUIEllipseMessage");
        guiRectPub = node.newPublisher("gui/Rect", "lab6_msgs/GUIRectMsg");
        guiPolyPub = node.newPublisher("gui/Poly", "lab6_msgs/GUIPolyMsg");
        guiErasePub = node.newPublisher("gui/Erase", "lab5_msgs/GUIEraseMsg");
        guiPtPub = node.newPublisher("gui/Point", "lab5_msgs/GUIPointMsg");

        // Reading in a map file whose name is set as the parameter mapFileName
        ParameterTree paramTree = node.newParameterTree();
        mapFileName = paramTree.getString(node.resolveName("~/mapFileName"));


        try {
            Thread.sleep(2000);

            erasePub.publish(new GUIEraseMsg());
            Thread.sleep(1000);

            challengeMap = GrandChallengeMap.parseFile(mapFileName);
            obsCSpaces = cSpace.generateCSpace(challengeMap, true);

            displayMap(); // --Works: Remember to plug into Robot
            displayMapCSpace();
        } catch(Exception e){
            System.err.println("Failed trying to load file " + mapFileName);
            e.printStackTrace();
        }

    }

    /**
     * Displays all the contents of the map in MapGUI
     */
    private void displayMap() {

        // Based on instanceMain in PolygonMap
        // Erase the GUI
        guiErasePub.publish(new GUIEraseMsg());

        GUIRectMsg rectMsg = new GUIRectMsg();
        CSpaceTest.fillRectMsg(rectMsg, challengeMap.getWorldRect(), null,
                false);
        guiRectPub.publish(rectMsg);
        GUIPolyMsg polyMsg = new GUIPolyMsg();
        for (PolygonObstacle obstacle : challengeMap.getPolygonObstacles()) {
            polyMsg = new GUIPolyMsg();
            CSpaceTest.fillPolyMsg(polyMsg, obstacle,
                    darkBlue, true, true);
            guiPolyPub.publish(polyMsg);
        }

        // print border
        rectMsg = new GUIRectMsg();
        CSpaceTest.fillRectMsg(rectMsg, challengeMap.getWorldRect(),
                Color.BLACK, false);
        guiRectPub.publish(rectMsg);

        System.out.println(challengeMap.getPolygonObstacles().length);
        System.out.println("Done running displayMap");
    }

    /**
     * Displays the configuration space of the environment.
     */
    private void displayMapCSpace() {
        ArrayList<PolygonObstacle> obstacles = obsCSpaces.get(cspaceIndex);
        //print cspace around obstacles
        for (PolygonObstacle obstacle : obstacles) {
            GUIPolyMsg polyMsg = new GUIPolyMsg();
            CSpaceTest.fillPolyMsg(polyMsg, obstacle,
                    lightBlue, false, true);
            guiPolyPub.publish(polyMsg);
        }

        System.out.println(obsCSpaces.size());
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
        return new GraphName("rss/cspaceTest");
    }
}