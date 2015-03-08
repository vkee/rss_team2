package GlobalNavigation;

import java.awt.geom.Point2D;
import java.util.List;


import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.node.parameter.ParameterTree;

import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
//import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;

//import LocalNavigation.Publisher;
import java.io.*;

public class GlobalNavigation implements NodeMain{
    private Publisher<org.ros.message.lab6_msgs.GUIRectMsg> guiRectPub;
    private Publisher<org.ros.message.lab6_msgs.GUIPolyMsg> guiPolyPub;
//    private Publisher<org.ros.message.lab5_msgs.GUIEraseMsg> guiErasePub;

    private String mapFileName;
    private PolygonMap polyMap;

    public GlobalNavigation(){

    }

    @Override
    public void onStart(Node node) {

        guiRectPub = node.newPublisher("gui/Rect", "lab6_msgs/GUIRectMsg");
        guiPolyPub = node.newPublisher("gui/Poly", "lab6_msgs/GUIPolyMsg");
//        guiErasePub = node.newPublisher("gui/Erase", "lab5_msgs/GUIEraseMsg");

        //        Reading in a map file whose name is set as the parameter mapFileName
        ParameterTree paramTree = node.newParameterTree();
        mapFileName = paramTree.getString(node.resolveName("~/mapFileName"));   
        try{
           polyMap = new PolygonMap(mapFileName);
        }
        catch(Exception e){
        	
        }
        //        
    }

    /**
     *  Displays all the contents of the map in MapGUI
     */
    private void displayMap(){

        //        should be doing something similar to the below, from instanceMain in PolygonMap
        //        Based on instanceMain in PolygonMap

//        guiErasePub.publish(new GUIEraseMsg());

        GUIRectMsg rectMsg = new GUIRectMsg();
        GlobalNavigation.fillRectMsg(rectMsg, polyMap.getWorldRect(), null, false);
        guiRectPub.publish(rectMsg);
        GUIPolyMsg polyMsg = new GUIPolyMsg();
        for (PolygonObstacle obstacle : polyMap.getObstacles()){
            polyMsg = new GUIPolyMsg();
            GlobalNavigation.fillPolyMsg(polyMsg, obstacle, MapGUI.makeRandomColor(), true, true);
            guiPolyPub.publish(polyMsg);
        }
    }

//    /**
//     * Given an empty GUIPointMsg and the appropriate parameters, fills in the message
//     * @param msg the empty GUIPointMsg to be filled
//     * @param point 
//     * @param color
//     * @param shape
//     */
//    public void fillPointMsg(GUIPointMsg msg, java.awt.geom.Point2D.Double point, 
//            java.awt.Color color, long shape) {
//        
//    }
//    
    /**
     * Given an empty GUIRectMsg and the appropriate parameters, fills in the message
     * @param msg the empty GUIRectMsg to be filled
     * @param r the rectangle as a Java Rectangle 2D double
     * @param c the color of the rectangle
     * @param filled whether the rectangle should be filled
     */
    public static void fillRectMsg(GUIRectMsg msg, java.awt.geom.Rectangle2D.Double r, 
            java.awt.Color c, boolean filled) {
        msg.x = (float) r.getX();
        msg.y = (float) r.getY();
        msg.width = (float) r.getWidth();
        msg.height = (float) r.getHeight();
        msg.filled = filled ? 1 : 0;
        if (c != null) {
//            ColorMsg colorMsg = new ColorMsg();
//            colorMsg.r = c.getRed();
//            colorMsg.g = c.getGreen();
//            colorMsg.b = c.getBlue();
//            msg.c = colorMsg;
        }
    }

    /**
     * Given an empty GUIPolyMsg and the appropriate parameters, fills in the message
     * @param msg the empty GUIPolyMsg to be filled
     * @param obstacle the obstacle forming the polygon
     * @param c the color of the polygon
     * @param filled whether the polygon is filled
     * @param closed whether the obstacle is closed
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
//        ColorMsg colorMsg = new ColorMsg();
//        colorMsg.r = c.getRed();
//        colorMsg.g = c.getGreen();
//        colorMsg.b = c.getBlue();
//        msg.c = colorMsg;
        msg.filled = filled ? 1 : 0; 
        msg.closed = closed ? 1 : 0;
    }

    /**
     * Tests the convex hull algorithm in GeomUtils
     */
    private void testConvexHull(){
        //TODO come up with more non trivial sets of test points to test convexHull

    }

    @Override
    public void onShutdown(Node node) {
        if(node!=null){
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
