package StateMachine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import Challenge.GrandChallengeMap;
import MotionPlanning.GoalAdjLists;
import MotionPlanning.MultiRRT2D;
import MotionPlanning.MultiRRT3D;
import MotionPlanning.RRT;
import Servoing.*;

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
        NECK, WHEELS, BLOCK, IMAGE, GATE, FLAP
    }

    private FSMState currentState;
    private boolean inState;

    public final long TIME_LIMIT = 10*60*1000;

    public long startTime;
    public final double RRT_TOLERANCE = 0.02;
    public final double ROBOTVEL = 0.1;			//coords/ms


    public int blocksCollected;
    public MultiRRT3D RRTengine;
    public Point2D.Double currentLocation;			//the current goal point we are at
    public GoalAdjLists foundPaths;
    public NeckController topNeckServo;
    public NeckController bottomNeckServo;
    public GateController gateServo;
        //public HashSet<Integer> visited;

    protected String mapFileName;
    private Publisher<GUIRectMsg> guiRectPub;
    private Publisher<GUIPolyMsg> guiPolyPub;
    private Publisher<GUIEraseMsg> guiErasePub;
    private Publisher<GUIPointMsg> guiPtPub;
    private Publisher<Object> ellipsePub;
    private Publisher<Object> stringPub;

    private Subscriber<OdometryMsg> odometrySub;

    // bumper subscriber
    private Subscriber<BumpMsg> bumpersSub;

    // servo control publishers and subscribers
    private Publisher<ArmMsg> armPWMPub;
    private Subscriber<ArmMsg> armStatusSub;

    public Publisher<MotionMsg> motionPub;


	public MapDrawer mapDrawer;


    // public wheels publishers
    // public servos publishers

    public FSM(){

        //initialize all listeners with dispatchState as the callback

    }

    public void copyToInitialize() {
        TopNeckController topNeckServo = new TopNeckController();
        BottomNeckController bottomNeckServo = new BottomNeckController();
        GateController gateServo = new GateController();
    }

    public void setStartTime(){ 
        startTime = System.currentTimeMillis();
    }

    public void updateState(FSMState newState)
    {
        System.out.println("Just Entered "+newState.getName());
        currentState = newState;
        currentState.onStart();
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
        odometrySub = node.newSubscriber("/rss/odometry", "rss_msgs/OdometryMsg");

        odometrySub
        .addMessageListener(new MessageListener<OdometryMsg>() {
            @Override
            public void onNewMessage(OdometryMsg message) {
                //robotX = message.x;
                //robotY = message.y;
                //robotTheta = message.theta;
                //message.type = msgENUM.WHEELS;
                dispatchState(new GenericMessage<OdometryMsg>(message, msgENUM.WHEELS));
            }
        });

        // add bumper message listener

        bumpersSub = node.newSubscriber("/rss/BumpSensors", "rss_msgs/BumpMsg");

        bumpersSub
        .addMessageListener(new MessageListener<org.ros.message.rss_msgs.BumpMsg>() {
            @Override
            public void onNewMessage(org.ros.message.rss_msgs.BumpMsg message) {
                dispatchState(new GenericMessage<BumpMsg>(message, msgENUM.FLAP));
            }
        });

        //TODO: add servo subscriber

        armPWMPub = node.newPublisher("command/Arm", "rss_msgs/ArmMsg");
        armStatusSub = node.newSubscriber("rss/ArmStatus", "rss_msgs/ArmMsg");

        armStatusSub.addMessageListener(new MessageListener<ArmMsg>() {

            int[] pwm_stat = new int[3];

            //TODO: rename these to correspond to proper neck/gate servos
            int wrist = 0;
            int shoulder = 0;
            int gripper = 0;

            public void sendCommands() {

                //TODO: rename these to correspond to proper neck/gate servos
                pwm_stat[0] = shoulder;
                pwm_stat[1] = wrist;
                pwm_stat[2] = gripper;

                ArmMsg msg = new ArmMsg();
                msg.pwms[0] = pwm_stat[0];
                msg.pwms[1] = pwm_stat[1];
                msg.pwms[2] = pwm_stat[2];
                armPWMPub.publish(msg);

                // System.out.println("message sent");
                // System.out.println(Arrays.toString(pwm_stat));
            }

            @Override
            public void onNewMessage(ArmMsg msg) {
                // long[] pwmVals = msg.pwms;
                int[] pwmVals = pwm_stat;

                //TODO: rename these to correspond to proper neck/gate servos
                int shoulderPWM = (int) pwmVals[0];
                int wristPWM = (int) pwmVals[1];
                int gripperPWM = (int) pwmVals[2];

                //TODO: update lab 7 code to work with current servo setup
                /*
                // System.out.println(Arrays.toString(pwmVals));

                double sum = InverseKinematics.ARM_LENGTH
                        + InverseKinematics.WRIST_LENGTH;
                // moveArm(sum*Math.cos(Math.PI/4), sum*Math.sin(Math.PI/4),
                // shoulderPWM, wristPWM);

                // System.out.println("Current State: " + graspState);
                if (graspState == ArmGraspState.STOP) {
                    System.out.println("graspState: STOP");
                }
                if (graspState == ArmGraspState.INITIALIZE) {
                    int shoulder_init_value = shoulderServo.GYM_GROUND_PWM;
                    int wrist_init_value = wristServo.COLLECT_PWM;
                    int gripper_init_value = gripperServo.MIN_PWM;

                    gripper = gripper_init_value;
                    wrist = wrist_init_value;
                    shoulder = shoulder_init_value;

                    System.out.println("Gripper Init Value: " + gripper);

                    graspState = ArmGraspState.OPEN_GRIPPER;

                } else if (graspState == ArmGraspState.OPEN_GRIPPER) {
                    // Opens gripper
                    if (!gripperServo.isOpen(gripperPWM)) {
                        gripper = gripperServo.open(gripperPWM);
                    } else {
                        graspState = ArmGraspState.FIND_OBJ;
                    }
                } else if (graspState == ArmGraspState.FIND_OBJ) {
                    if (objDetected) { // Bump sensor
                        MotionMsg moveMsg = new MotionMsg();
                        moveMsg.translationalVelocity = 0.0;
                        moveMsg.rotationalVelocity = 0.0;
                        motionPub.publish(moveMsg);
                        System.out.println("Object Detected!");
                        graspState = ArmGraspState.GRASP;
                    } else {
                        System.out.println("trying to find the block");
                        // Part 4 Stuff
                        Image src = null;
                        try {
                            src = new Image(visionImage.take(), width, height);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Image dest = new Image(src);

                        blobTrack.apply(src, dest);

                        org.ros.message.sensor_msgs.Image pubImage = new org.ros.message.sensor_msgs.Image();
                        pubImage.width = width;
                        pubImage.height = height;
                        pubImage.encoding = "rgb8";
                        pubImage.is_bigendian = 0;
                        pubImage.step = width * 3;
                        pubImage.data = dest.toArray();
                        vidPub.publish(pubImage);

                        // Begin Student Code

                        // // publish velocity messages to move the robot
                        // towards the target
                        MotionMsg vidMsg = new MotionMsg(); // (Solution)
                        vidMsg.translationalVelocity = blobTrack.translationVelocityCommand; // (Solution)
                        vidMsg.rotationalVelocity = blobTrack.rotationVelocityCommand; // (Solution)
                        System.out.println("Trans Vel: "
                                + vidMsg.translationalVelocity);
                        System.out.println("Rot Vel: "
                                + vidMsg.rotationalVelocity);
                        motionPub.publish(vidMsg); // (Solution)

                        // End Student Code
                    }
                } else if (graspState == ArmGraspState.GRASP) {
                    if (!gripperServo.isClosed(gripperPWM)) {
                        System.out.println("closing gripper" + wristPWM
                                + " of " + gripperServo.MIN_PWM);
                        gripper = gripperServo.close(gripperPWM);
                    } else {
                        graspState = ArmGraspState.LIFT;
                    }
                } else if (graspState == ArmGraspState.LIFT) {
                    if (!shoulderServo.isGymUp(shoulderPWM)) {
                        shoulder = shoulderServo.moveGymUp(shoulderPWM);
                    } else {
                        // System.out.println("Arm should be at 45 degrees and now switching into move");
                        graspState = ArmGraspState.MOVE;
                        startX = robotX;
                        startY = robotY;
                        startTheta = robotTheta;
                        goalX = robotX + MOVE_DIST * Math.cos(robotTheta);
                        goalY = robotY + MOVE_DIST * Math.sin(robotTheta);
                        goalTheta = robotTheta;
                    }
                } else if (graspState == ArmGraspState.MOVE) {
                    double remDist = getDist(robotX, robotY, goalX, goalY);

                    if (remDist > DIST_TOL) {
                        MotionMsg moveMsg = new MotionMsg();
                        moveMsg.translationalVelocity = Math.min(FWD_GAIN
                                * remDist, 0.25);
                        moveMsg.rotationalVelocity = 0.0;
                        System.out.println("Rem Dist: " + remDist);

                        System.out.println("Trans Vel: "
                                + moveMsg.translationalVelocity);
                        motionPub.publish(moveMsg);
                    } else {
                        MotionMsg moveMsg = new MotionMsg();
                        moveMsg.translationalVelocity = 0.0;
                        moveMsg.rotationalVelocity = 0.0;
                        motionPub.publish(moveMsg);
                        graspState = ArmGraspState.DEPOSIT_WRIST;
                    }
                } else if (graspState == ArmGraspState.DEPOSIT_WRIST) {
                    if (!wristServo.isCollect(wristPWM)) {
                        wrist = wristServo.collect(wristPWM);
                    } else {
                        graspState = ArmGraspState.DEPOSIT_SHOULDER;
                    }
                } else if (graspState == ArmGraspState.DEPOSIT_SHOULDER) {
                    if (!shoulderServo.onGround(shoulderPWM)) {
                        shoulder = shoulderServo.moveToGround(shoulderPWM);
                    } else {
                        graspState = ArmGraspState.DEPOSIT_GRIPPER;
                    }
                } else if (graspState == ArmGraspState.DEPOSIT_GRIPPER) {
                    // Opens gripper
                    if (!gripperServo.isOpen(gripperPWM)) {
                        gripper = gripperServo.open(gripperPWM);
                    } else {
                        graspState = ArmGraspState.RETURN;
                        startX = robotX;
                        startY = robotY;
                        startTheta = robotTheta;
                        goalX = robotX - MOVE_DIST * Math.cos(robotTheta);
                        goalY = robotY - MOVE_DIST * Math.sin(robotTheta);
                        goalTheta = robotTheta;
                    }
                } else if (graspState == ArmGraspState.RETURN) {
                    double remDist = getDist(robotX, robotY, goalX, goalY);

                    if (remDist > DIST_TOL) {
                        System.out.println("Rem Dist: " + remDist);
                        MotionMsg moveMsg = new MotionMsg();
                        moveMsg.translationalVelocity = -Math.min(FWD_GAIN
                                * remDist, 0.25);
                        moveMsg.rotationalVelocity = 0.0;

                        System.out.println("Trans Vel: "
                                + moveMsg.translationalVelocity);
                        motionPub.publish(moveMsg);
                    } else {
                        MotionMsg moveMsg = new MotionMsg();
                        moveMsg.translationalVelocity = 0.0;
                        moveMsg.rotationalVelocity = 0.0;
                        motionPub.publish(moveMsg);
                        System.out.println("DONE with Grasp and Transport");
                    }
                }

                */

                sendCommands();

            }

            /**
             * Moves the arm to the desired x, z position in the robot frame
             *
             * @param desX
             *            the desired x coordinate for the end effector
             * @param desZ
             *            the desired z coordinate for the end effector
             */
            /*
            public void moveArm(double desX, double desZ, int currShoulderPWM,
                                int currWristPWM) {

                double[] angles = InverseKinematics.getThetaPhi(desX, desZ,
                        shoulderServo.getThetaRad(currShoulderPWM),
                        wristServo.getThetaRad(currWristPWM));
                System.out.println("Shoulder Angle: " + angles[0]);
                System.out.println("Wrist Angle: " + angles[1]);
                shoulder = shoulderServo.getPWM(angles[0]);
                wrist = wristServo.getPWM(angles[1]);
            }*/
        });

        mapDrawer = new MapDrawer(guiRectPub, guiPolyPub, guiErasePub, guiPtPub, ellipsePub, 
        		stringPub, odometrySub, motionPub);
        
        currentState = new Initialize(this);
        inState = false;
        dispatchState(new GenericMessage(null, null));

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