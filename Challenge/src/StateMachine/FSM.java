package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import Challenge.GrandChallengeMap;
import MotionPlanning.GoalAdjLists;
import MotionPlanning.MultiRRT;
import MotionPlanning.RRT;

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

/**
 * This class is the root node for all listeners and publishers and offers the control flow for the state machine. 
 * It is also the repository for any shared state variables
 */
public class FSM implements NodeMain{

    public enum stateENUM {
        INITIALIZE, SCAN, WNCLOSE, MOVEFORWARD, OPENGATE, APPROACHBLOCK, BLOCKCOLLECTED, RRTUPDATE, ORIENTDEPOSIT, VISUALSERVOCOLLECT, WNDEPOSIT
    }

    public enum msgENUM { 
        NECK, WHEELS, BLOCK, IMAGE, GATE, DOOR
    }

    private FSMState currentState;
    private boolean inState;

    public final long TIME_LIMIT = 10*60*1000;

    public long startTime;
    public final double RRT_TOLERANCE = 0.02;
    public final double ROBOTVEL = 0.1;			//coords/ms


    public int blocksCollected;
    public MultiRRT RRTengine;
    public Point2D.Double currentLocation;			//the current goal point we are at
    public GoalAdjLists foundPaths;
    //public HashSet<Integer> visited;

    protected String mapFileName;
    private Publisher<GUIRectMsg> guiRectPub;
    private Publisher<GUIPolyMsg> guiPolyPub;
    private Publisher<GUIEraseMsg> guiErasePub;
    private Publisher<GUIPointMsg> guiPtPub;
    private Publisher<Object> ellipsePub;
    private Publisher<Object> stringPub;

    private Subscriber<OdometryMsg> odometrySub;
    public Publisher<MotionMsg> motionPub;


    // public wheels publishers
    // public servos publishers

    public FSM(){




        //initialize all listeners with dispatchState as the callback

    }

    public void setStartTime(){
        startTime = System.currentTimeMillis();
    }

    public void updateState(FSMState newState)
    {
        System.out.println("Just Entered "+newState.getName());
        currentState = newState;
    }

    public void dispatchState(GenericMessage msg)
    {
        if (inState) return;					// may instead use a LOCK and queue for other msgs instead
        inState = true;
        if (currentState.accepts(msg.type))		//may not need this check
        {currentState.update(msg);}		
        inState = false;
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
        System.out.println("mapFileName " + mapFileName);
        //initialize publishers
        motionPub = node.newPublisher("command/Motors", "rss_msgs/MotionMsg");

        //intialize subscribers
        odometrySub = node.newSubscriber("/rss/odometry",
                "rss_msgs/OdometryMsg");

        odometrySub
        .addMessageListener(new MessageListener<org.ros.message.rss_msgs.OdometryMsg>() {
            @Override
            public void onNewMessage(org.ros.message.rss_msgs.OdometryMsg message) {
                //robotX = message.x;
                //robotY = message.y;
                //robotTheta = message.theta;
                //message.type = msgENUM.WHEELS;
                dispatchState(new GenericMessage<org.ros.message.rss_msgs.OdometryMsg>(message, msgENUM.WHEELS));
            }
        });
        currentState = new Initialize(this);
        inState = false;
        dispatchState(null);

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
        return new GraphName("rss/FSM");
    }
}
