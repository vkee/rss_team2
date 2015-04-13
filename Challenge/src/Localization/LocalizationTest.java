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

import Challenge.Fiducial;
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
    private Publisher<Object> ellipsePub;
    private Publisher<Object> stringPub;

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
        stringPub = node.newPublisher("gui/String",
                "Challenge_msgs/GUIStringMessage");
        ellipsePub = node.newPublisher("/gui/Ellipse",
                "Challenge_msgs/GUIEllipseMessage");
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

        for(Fiducial f : challengeMap.getFiducials()){
            Point2D.Double pos = f.getPosition();
            System.out.println("Fiducial at ("+f.getTopColor()+"/"+f.getBottomColor()+")at: "+pos.getX()+", "+pos.getY());
            publishEllipse(f.getPosition().x+0.1, f.getPosition().y+0.1, f.getBottomSize()*4.0, 
                    f.getBottomSize()*4.0, f.getBottomColor());
            publishEllipse(f.getPosition().x, f.getPosition().y, f.getTopSize()*4.0, 
                    f.getTopSize()*4.0, f.getTopColor());
        }
        
        Point2D.Double robotStart = challengeMap.getRobotStart();
        Point2D.Double robotGoal = challengeMap.getRobotGoal();

        publishPoint(robotStart.getX(), robotStart.getY(), 0, Color.RED);
        publishPoint(robotGoal.getX(), robotGoal.getY(), 0, Color.CYAN);
        System.out.println("Robot Start: " + robotStart);
        System.out.println("Robot Goal: " + robotGoal);
        System.out.println("Num obstacles " + challengeMap.getPolygonObstacles().length);
        System.out.println("Done running displayMap");
    }

    protected void publishEllipse(double x, double y, double w, double h, Color color) {
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