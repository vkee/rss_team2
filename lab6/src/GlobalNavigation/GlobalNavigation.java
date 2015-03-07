package GlobalNavigation;

import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.message.lab6_msgs.*;

public class GlobalNavigation implements NodeMain{

    public GlobalNavigation(){
        
    }
    
    @Override
    public void onStart(Node node) {
        ParameterTree paramTree = node.newParameterTree();
        String mapFileName = paramTree.getString(node.resolveName("~/mapFileName"));
        
//        should be storing the map as an instance variable
        PolygonMap polygonMap = new PolygonMap(mapFileName);
//        
    }
    
    /**
     *  Displays all the contents of the map in MapGUI
     */
    private void displayMap(){
//        should be publishing to these subscribers which are in MapGUI
//        guiRectSub = node.newSubscriber("gui/Rect", "lab6_msgs/GUIRectMsg");
//        guiRectSub.addMessageListener(new RectMessageListener(this));
//        guiPolySub = node.newSubscriber("gui/Poly", "lab6_msgs/GUIPolyMsg");
//        guiPolySub.addMessageListener(new PolyMessageListener(this));
//        guiEraseSub = node.newSubscriber("gui/Erase", "lab5_msgs/GUIEraseMsg");
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
