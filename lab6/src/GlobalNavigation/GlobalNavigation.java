package GlobalNavigation;

import java.awt.geom.Point2D;
import java.util.List;

import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.message.lab6_msgs.*;

import LocalNavigation.Publisher;

public class GlobalNavigation implements NodeMain{
    private Publisher<org.ros.message.lab6_msgs.GUIRectMsg> guiRectPub;
    private Publisher<org.ros.message.lab6_msgs.GUIPolyMsg> guiPolyPub;
    private Publisher<org.ros.message.lab5_msgs.GUIEraseMsg> guiErasePub;

    private String mapFileName;
    private PolygonMap polyMap;

    public GlobalNavigation(){

    }

    @Override
    public void onStart(Node node) {

        guiRectPub = node.newPublisher("gui/Rect", "lab6_msgs/GUIRectMsg");
        guiPolyPub = node.newPubscriber("gui/Poly", "lab6_msgs/GUIPolyMsg");
        guiErasePub = node.newPublisher("gui/Erase", "lab5_msgs/GUIEraseMsg");

        //        Reading in a map file whose name is set as the parameter mapFileName
        ParameterTree paramTree = node.newParameterTree();
        mapFileName = paramTree.getString(node.resolveName("~/mapFileName"));        
        polyMap = new PolygonMap(mapFileName);
        //        
    }

    /**
     *  Displays all the contents of the map in MapGUI
     */
    private void displayMap(){
        
//        should be doing something similar to the below, from instanceMain in PolygonMap
//        Based on instanceMain in PolygonMap
        
        guiErasePub.publish(new GUIEraseMsg());
        
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

    /**
     * Given an empty GUIRectMsg and the appropriate parameters, fills in the message
     * @param msg the empty GUIRectMsg to be filled
     * @param r the rectangle as a Java Rectangle 2D double
     * @param c the color of the rectangle
     * @param filled whether the rectangle should be filled
     */
    public static void fillRectMsg(GUIRectMsg msg, java.awt.geom.Rectangle2D.Double r, 
            java.awt.Color c, boolean filled) {
        msg.x = r.getX();
        msg.y = r.getY();
        msg.width = r.getWidth();
        msg.height = r.getHeight();
        msg.filled = filled ? 1 : 0;
        if (c != null) {
            msg.c = c;
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
        double[] x = new double[msg.numVertices];
        double[] y = new double[msg.numVertices];
        
        for (int i = 0; i < msg.numVertices; i++) {
            Point2D.Double vertex = vertices.get(i);
            x[i] = vertex.getX();
            y[i] = vertex.getY();
        }
        msg.x = x;
        msg.y = y;
        msg.c = c;
        msg.filled = filled ? 1 : 0; 
        msg.closed = closed ? 1 : 0;
    }
    
    /**
     * Tests the convex hull algorithm in GeomUtils
     */
    private void testConvexHull(){

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
