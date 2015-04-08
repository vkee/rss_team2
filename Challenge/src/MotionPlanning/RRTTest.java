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
public class RRTTest implements NodeMain{
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
    private RRT rrt;
    // colors
    private Color lightBlue = new Color(115,115,230);
    private Color darkBlue = new Color(50,40,120);

    //    Index of the obstacle cspace list to display 
    //    (corresponds to the angle in degrees if num angles is 360)
    private final int cspaceIndex = 90;

    public RRTTest() {
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

            guiErasePub.publish(new GUIEraseMsg());
            Thread.sleep(1000);

            challengeMap = GrandChallengeMap.parseFile(mapFileName);
            obsCSpaces = cSpace.generateCSpace(challengeMap, true);
            challengeMap.set3DCSpace(obsCSpaces);
            rrt = new RRT(challengeMap);
            rrtTests();
            displayMap(); // --Works: Remember to plug into Robot
            displayMapCSpace();
        } catch(Exception e){
            System.err.println("Failed trying to load file " + mapFileName);
            e.printStackTrace();
        }

    }

    /**
     * Quick method to test RRT getAngle b/c can't get JUnit tests to work
     */
    private void rrtTests() {
        System.out.println(RRT.getAngle(-1.03, 1.0, 4.07, 1.0) == 0.0);
        System.out.println(RRT.getAngle(1.0, 1.0, 2.0, 2.0) == Math.PI/4);
        System.out.println(RRT.getAngle(1.0, -1.0, 1.0, 2.0) == Math.PI/2);
        System.out.println(RRT.getAngle(-2.0, -2.0, -4.0, 0.0) == 3*Math.PI/4);
        System.out.println(RRT.getAngle(2.0, -2.0, -2.0, -2.0) == 4*Math.PI/4);
        System.out.println(RRT.getAngle(0.0, 0.0, -1.0, -1.0) == -3*Math.PI/4);
        System.out.println(RRT.getAngle(0.0, 0.0, 0.0, -1.0) == -2*Math.PI/4);
        System.out.println(RRT.getAngle(1.0, -1.0, 2.0, -2.0) == -1*Math.PI/4);
        System.out.println("Done running angle tests");
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
        return new GraphName("rss/RRTTest");
    }
}