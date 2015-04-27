//package StateMachine;
//
//import java.awt.geom.Point2D;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import Challenge.GrandChallengeMap;
//import MotionPlanning.GoalAdjLists;
//import MotionPlanning.MultiRRT;
//import MotionPlanning.RRT;
//import Servoing.*;
//
//import org.ros.message.rss_msgs.*;
//import org.ros.message.MessageListener;
//import org.ros.node.topic.Publisher;
//import org.ros.node.topic.Subscriber;
//import org.ros.node.parameter.ParameterTree;
//import org.ros.node.NodeMain;
//import org.ros.namespace.GraphName;
//import org.ros.node.Node;
//import org.ros.message.lab5_msgs.*;
//import org.ros.message.lab6_msgs.*;
//import org.ros.message.Challenge_msgs.*;
//
///**
// * This class is the root node for all listeners and publishers and offers the control flow for the state machine. 
// * It is also the repository for any shared state variables
// */
//public class FSM implements NodeMain{
//
//    public enum stateENUM {
//        INITIALIZE, SCAN, WNCLOSE, MOVEFORWARD, OPENGATE, APPROACHBLOCK, BLOCKCOLLECTED, RRTUPDATE, ORIENTDEPOSIT, VISUALSERVOCOLLECT, WNDEPOSIT
//    }
//
//    public enum msgENUM { 
//        NECK, WHEELS, BLOCK, IMAGE, GATE, FLAP
//    }
//
//    private FSMState currentState;
//    private boolean inState;
//
//    public final long TIME_LIMIT = 10*60*1000;
//
//    public long startTime;
//    public final double RRT_TOLERANCE = 0.02;
//    public final double ROBOTVEL = 0.1;			//coords/ms
//
//
//    public int blocksCollected;
//    public MultiRRT RRTengine;
//    public Point2D.Double currentLocation;			//the current goal point we are at
//    public GoalAdjLists foundPaths;
//
//    // servo controllers
//    public TopNeckController topNeckServo;
//    public BottomNeckController bottomNeckServo;
//    public GateController gateServo;
//    public GateState gateState;
//
//    public enum GateState {
//        CLOSED, OPEN
//    }
//
//        //public HashSet<Integer> visited;
//
//    protected String mapFileName;
//    private Publisher<GUIRectMsg> guiRectPub;
//    private Publisher<GUIPolyMsg> guiPolyPub;
//    private Publisher<GUIEraseMsg> guiErasePub;
//    private Publisher<GUIPointMsg> guiPtPub;
//    private Publisher<Object> ellipsePub;
//    private Publisher<Object> stringPub;
//
//    private Subscriber<OdometryMsg> odometrySub;
//
//    // bumper subscriber
//    private Subscriber<BumpMsg> bumpersSub;
//
//    // servo control publishers and subscribers
//    private Publisher<ArmMsg> armPWMPub;
//    private Subscriber<ArmMsg> armStatusSub;
//
//    public Publisher<MotionMsg> motionPub;
//
//
//	public MapDrawer mapDrawer;
//
//
//    // public wheels publishers
//    // public servos publishers
//
//    public FSM(){
//
//        //initialize all listeners with dispatchState as the callback
//
//    }
//
//    public void copyToInitialize() {
//
//        gateState = GateState.CLOSED;
//
//        TopNeckController topNeckServo = new TopNeckController();
//        BottomNeckController bottomNeckServo = new BottomNeckController();
//        GateController gateServo = new GateController();
//
//
//
//    }
//
//    public void setStartTime(){ 
//        startTime = System.currentTimeMillis();
//    }
//
//    public void updateState(FSMState newState)
//    {
//        System.out.println("Just Entered "+newState.getName());
//        currentState = newState;
//        currentState.onStart();
//    }
//
//    public void dispatchState(GenericMessage msg)
//    {
//        if (inState) return;					// may instead use a LOCK and queue for other msgs instead
//        inState = true;
//        if (currentState.accepts(msg.type))		//may not need this check
//        {currentState.update(msg);}		
//        inState = false;
//    }
//
//
//    @Override
//    public void onStart(Node node) {
//        stringPub = node.newPublisher("gui/String",
//                "Challenge_msgs/GUIStringMessage");
//        ellipsePub = node.newPublisher("/gui/Ellipse",
//                "Challenge_msgs/GUIEllipseMessage");
//        guiRectPub = node.newPublisher("gui/Rect", "lab6_msgs/GUIRectMsg");
//        guiPolyPub = node.newPublisher("gui/Poly", "lab6_msgs/GUIPolyMsg");
//        guiErasePub = node.newPublisher("gui/Erase", "lab5_msgs/GUIEraseMsg");
//        guiPtPub = node.newPublisher("gui/Point", "lab5_msgs/GUIPointMsg");
//
//        // Reading in a map file whose name is set as the parameter mapFileName
//        ParameterTree paramTree = node.newParameterTree();
//        mapFileName = paramTree.getString(node.resolveName("~/mapFileName"));
//        System.out.println("mapFileName " + mapFileName);
//        //initialize publishers
//        motionPub = node.newPublisher("command/Motors", "rss_msgs/MotionMsg");
//
//        //intialize subscribers
//        odometrySub = node.newSubscriber("/rss/odometry", "rss_msgs/OdometryMsg");
//
//        odometrySub
//        .addMessageListener(new MessageListener<OdometryMsg>() {
//            @Override
//            public void onNewMessage(OdometryMsg message) {
//                //robotX = message.x;
//                //robotY = message.y;
//                //robotTheta = message.theta;
//                //message.type = msgENUM.WHEELS;
//                dispatchState(new GenericMessage<OdometryMsg>(message, msgENUM.WHEELS));
//            }
//        });
//
//        // add bumper message listener
//
//        bumpersSub = node.newSubscriber("/rss/BumpSensors", "rss_msgs/BumpMsg");
//
//        bumpersSub
//        .addMessageListener(new MessageListener<org.ros.message.rss_msgs.BumpMsg>() {
//            @Override
//            public void onNewMessage(org.ros.message.rss_msgs.BumpMsg message) {
//                dispatchState(new GenericMessage<BumpMsg>(message, msgENUM.FLAP));
//            }
//        });
//
//        //TODO: add servo subscriber
//
//        armPWMPub = node.newPublisher("command/Arm", "rss_msgs/ArmMsg");
//        armStatusSub = node.newSubscriber("rss/ArmStatus", "rss_msgs/ArmMsg");
//
//        armStatusSub.addMessageListener(new MessageListener<ArmMsg>() {
//
//            int[] pwm_stat = new int[3];
//
//            //TO DO? rename these to correspond to proper neck/gate servos
//            // done?
//            int topNeck = 0;
//            int bottomNeck = 0;
//            int gate = 0;
//
//            public void sendCommands() {
//
//                //TO DO: rename these to correspond to proper neck/gate servos
//                // done?
//                pwm_stat[0] = topNeck;
//                pwm_stat[1] = bottomNeck;
//                pwm_stat[2] = gate;
//
//                ArmMsg msg = new ArmMsg();
//                msg.pwms[0] = pwm_stat[0];
//                msg.pwms[1] = pwm_stat[1];
//                msg.pwms[2] = pwm_stat[2];
//                armPWMPub.publish(msg);
//
//                // System.out.println("message sent");
//                // System.out.println(Arrays.toString(pwm_stat));
//            }
//
//            @Override
//            public void onNewMessage(ArmMsg msg) {
//                // long[] pwmVals = msg.pwms;
//                int[] pwmVals = pwm_stat;
//
//                //TODO: rename these to correspond to proper neck/gate servos
//                int topNeckPWM = (int) pwmVals[0];
//                int bottomNeckPWM = (int) pwmVals[1];
//                int gatePWM = (int) pwmVals[2];
//
//                if (gateState == GateState.CLOSED) {
//                    if (!gateServo.isOpen(gatePWM)) { // if gate is not open
//                        gate = gateServo.open(gatePWM);
//                    } else {
//                        gateState = GateState.OPEN;
//                    }
//                }
//
//                sendCommands();
//
//            }
//        });
//
//        mapDrawer = new MapDrawer(guiRectPub, guiPolyPub, guiErasePub, guiPtPub, ellipsePub, 
//        		stringPub, odometrySub, motionPub);
//        
//        currentState = new Initialize(this);
//        inState = false;
//        dispatchState(new GenericMessage(null, null));
//
//    }
//
//    @Override
//    public void onShutdown(Node node) {
//        if (node != null) {
//            node.shutdown();
//        }
//    }
//
//    @Override
//    public void onShutdownComplete(Node node) {
//    }
//
//    @Override
//    public GraphName getDefaultNodeName() {
//        return new GraphName("rss/FSM");
//    }
//}