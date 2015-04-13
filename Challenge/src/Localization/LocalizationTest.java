package Localization;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import MotionPlanning.CSpace;
import MotionPlanning.CSpaceTest;
import MotionPlanning.PolygonObstacle;
import MotionPlanning.RRT;

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
    private CSpace cSpace;
    private ArrayList<ArrayList<PolygonObstacle>> obsCSpaces = new ArrayList<ArrayList<PolygonObstacle>>();
    private RRT rrt;
    private List<Point2D.Double> rrtPath;
    private ParticleFilter particleFilter;

    private Point2D.Double robotStart;
    private Point2D.Double robotGoal;
    private Point2D.Double prevPt;


    // colors
    private Color lightBlue = new Color(115, 115, 230);
    private Color darkBlue = new Color(50, 40, 120);

    // Index of the obstacle cspace list to display
    // (corresponds to the angle in degrees if num angles is 360)
    private final int cspaceIndex = 90;
    private final double TOLERANCE = 0.02;

    public LocalizationTest() {
        cSpace = new CSpace();
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
            //            Thread.sleep(2000);

            guiErasePub.publish(new GUIEraseMsg());
            //            Thread.sleep(1000);

            challengeMap = GrandChallengeMap.parseFile(mapFileName);
            obsCSpaces = cSpace.generateCSpace(challengeMap, false);            //this was adding the robot as an OBSTACLE!!! was true TODO
            challengeMap.set3DCSpace(obsCSpaces);
            rrt = new RRT(challengeMap);
            displayMap(); // --Works: Remember to plug into Robot
            //            displayMapCSpace(); don't display 
        } catch (Exception e) {
            System.err.println("Failed trying to load file " + mapFileName);
            e.printStackTrace();
        }

        try {
            //            Generate RRT
            rrtPath = rrt.getPath(challengeMap.getRobotStart(),
                    challengeMap.getRobotGoal(), TOLERANCE);
            outputPath(rrtPath, Color.RED);
            System.out.println("Done with RRT");

            //            Localization Tests
            try {
                //                Initialize Particle Filter
                particleFilter = new ParticleFilter(1000, challengeMap, 0.5, 0.5, 5.0);

                publishParticles();

                prevPt = robotStart;
                for (Point2D.Double pt : rrtPath){
                    double transDist = RRT.getDist(prevPt.x, prevPt.y, pt.x, pt.y);
                    double rotAng = RRT.getAngle(prevPt.x, prevPt.y, pt.x, pt.y);

                    //                    Converting rotAng to go from 0 to 2*PI
                    if (rotAng < 0.0) {
                        rotAng += 2*Math.PI;
                    }

                    particleFilter.motionUpdate(transDist, rotAng);

                    //                    Determining which fiducials are in the FOV of the robot
                    ArrayList<Integer> measuredFiducials = getFidsInFOV(pt);
                    //                    Determining the distances to the fiducials that are in the FOV of the robot
                    HashMap<Integer, java.lang.Double> measuredDists = getFidsDists(pt, measuredFiducials);

                    particleFilter.measurementUpdate(measuredFiducials, measuredDists);

                    //                    Display the state after the motion and measurement update
                    prevPt = pt;
                    refreshDisplay();
                    Thread.sleep(1000); // Waiting 1 second between each step
                }

                System.out.println("Done with particle filter");
            } catch (Exception e) {
                System.err.println("Failed to localize");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Failed to find path...");
            e.printStackTrace();
        }
    }

    /**
     * Determines the fiducials that are in the robot's field of view (FOV)
     * @param robotPos the robot's current position
     * @return the indices corresponding to the fiducials that are in the robot's FOV
     */
    private ArrayList<Integer> getFidsInFOV(Point2D.Double robotPos) {
        ArrayList<Integer> fidsInFOV = new ArrayList<Integer>();
        Fiducial[] fiducials = challengeMap.getFiducials();
        for (int i = 0; i < fiducials.length; i++) {
            Point2D.Double fidPos = fiducials[i].getPosition();

            ArrayList<PolygonObstacle> obstacles = obsCSpaces.get(cspaceIndex);
            if (!RRT.lineIntersectsObs(obstacles, robotPos, fidPos)) {
                fidsInFOV.add(i);
            }
        }

        return fidsInFOV;
    }

    /**
     * Determines the distances from the robot to the fiducials in the robot's FOV
     * @param robotPos the robot's current position
     * @param measuredFiducials the indices of the fiducials in the robot's FOV
     * @return the distances from the robot's position to the fiducials
     */
    private HashMap<Integer, java.lang.Double> getFidsDists(Point2D.Double robotPos, ArrayList<Integer> measuredFiducials) {
        HashMap<Integer, java.lang.Double> fidsDists = new HashMap<Integer, java.lang.Double>();
        Fiducial[] fiducials = challengeMap.getFiducials();

        for (Integer index : measuredFiducials) {
            Point2D.Double fidPos = fiducials[index].getPosition();

            double dist = RRT.getDist(fidPos.x, fidPos.y, robotPos.x, robotPos.y);
            fidsDists.put(index, dist);
        }

        return fidsDists;
    }

    private void publishParticles() {
        //        TODO: may need to replace these with tiny ellipses if too hard to see
        for (RobotParticle particle : particleFilter.getParticles()) {
            publishPoint(particle.getX(), particle.getY(), 0, Color.MAGENTA);
        }
    }

    private void refreshDisplay() {
        guiErasePub.publish(new GUIEraseMsg());
        displayMap(); 
        outputPath(rrtPath, Color.RED);
        publishParticles();
        //        Displaying the robot's current location
        publishEllipse(prevPt.getX(), robotGoal.getY(), 0.1, 0.1, Color.ORANGE);
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

        CSpaceTest.fillPolyMsg(poMsg, poly, color, false, false);
        guiPolyPub.publish(poMsg);
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

        robotStart = challengeMap.getRobotStart();
        robotGoal = challengeMap.getRobotGoal();

        publishEllipse(robotStart.getX(), robotStart.getY(), 0.1, 0.1, Color.RED);
        publishEllipse(robotGoal.getX(), robotGoal.getY(), 0.1, 0.1, Color.CYAN);
        System.out.println("Robot Start: " + robotStart);
        System.out.println("Robot Goal: " + robotGoal);
        System.out.println("Num obstacles " + challengeMap.getPolygonObstacles().length);
        System.out.println("Done running displayMap");
    }

    /**
     * Displays the configuration space of the environment.
     */
    private void displayMapCSpace() {
        ArrayList<PolygonObstacle> obstacles = obsCSpaces.get(cspaceIndex);
        // print cspace around obstacles
        for (PolygonObstacle obstacle : obstacles) {
            GUIPolyMsg polyMsg = new GUIPolyMsg();
            CSpaceTest.fillPolyMsg(polyMsg, obstacle, lightBlue, false, true);
            guiPolyPub.publish(polyMsg);
        }

        System.out.println(obsCSpaces.size());
        System.out.println("Done running displayMapCSpace");

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