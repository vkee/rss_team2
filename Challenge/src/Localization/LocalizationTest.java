package Localization;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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
import MotionPlanning.CSpaceTest;
import MotionPlanning.PolygonObstacle;

/**
 * Tests the Localization modules (ParticleFilter, RobotParticle).
 *
 */
public class LocalizationTest implements NodeMain {
    private Publisher<GUIRectMsg> guiRectPub;
    private Publisher<GUIPolyMsg> guiPolyPub;
    private Publisher<GUIEraseMsg> guiErasePub;
    private Publisher<GUIPointMsg> guiPtPub;

    private ColorMsg redMsg;
    private ColorMsg greenMsg;
    private ColorMsg blueMsg;
    private ColorMsg blackMsg;

    private String mapFileName;
    private GrandChallengeMap challengeMap;
    // colors
    private Color lightBlue = new Color(115, 115, 230);
    private Color darkBlue = new Color(50, 40, 120);

    public LocalizationTest() {
        
    }

    @Override
    public void onStart(Node node) {
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

            displayMap(); // --Works: Remember to plug into Robot
        } catch (Exception e) {
            System.err.println("Failed trying to load file " + mapFileName);
            e.printStackTrace();
        }

        try {
//            here is where should run the test stuff
            System.out.println("Done");
            
        } catch (Exception e) {
            System.err.println("Failed to find path...");
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

        GUIPolyMsg polyMsg = new GUIPolyMsg();
        for (PolygonObstacle obstacle : challengeMap.getPolygonObstacles()) {
            polyMsg = new GUIPolyMsg();
            CSpaceTest.fillPolyMsg(polyMsg, obstacle, darkBlue, true, true);
            guiPolyPub.publish(polyMsg);
        }

        // print border
        GUIRectMsg rectMsg = new GUIRectMsg();
        CSpaceTest.fillRectMsg(rectMsg, challengeMap.getWorldRect(),
                Color.BLACK, false);
        guiRectPub.publish(rectMsg);

        System.out.println("Num obstacles " + challengeMap.getPolygonObstacles().length);
        System.out.println("Done running displayMap");
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
        return new GraphName("rss/LocalizationTest");
    }
}