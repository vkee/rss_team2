package LocalNavigation;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.message.rss_msgs.OdometryMsg;
import org.ros.message.rss_msgs.MotionMsg;
import org.ros.message.lab5_msgs.ColorMsg;
import org.ros.node.Node;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;


public class LocalNavigation implements NodeMain{

    public Subscriber<org.ros.message.rss_msgs.SonarMsg> frontSonarSub;
    public Subscriber<org.ros.message.rss_msgs.SonarMsg> backSonarSub;
    public Subscriber<org.ros.message.rss_msgs.BumpMsg> bumpersSub;
    public Publisher<org.ros.message.std_msgs.String> statePub;
    public Publisher<MotionMsg> motionPub;
    public Subscriber<OdometryMsg> odometrySub;

    public Publisher<org.ros.message.lab5_msgs.GUILineMsg> guiLinePub;
    public Publisher<org.ros.message.lab5_msgs.GUISegmentMsg> guiSegPub;
    public Publisher<org.ros.message.lab5_msgs.GUIPointMsg> guiPtPub;
    public Publisher<org.ros.message.lab5_msgs.GUIEraseMsg> guiErasePub;

    public State state;
    //    Booleans representing whether the bumper is depressed
    //    True/1 denote depressed, False/0 denote unpressed
    public boolean leftBumper = false;
    public boolean rightBumper = false;

    public boolean saveErrors;

    //    Robot Odometry
    public double robotX = 0.0; // in meters
    public double robotY = 0.0; // in meters
    public double robotTheta = 0.0; // in radians

    //    Offset of the front sonar wrt the robot's origin
    public final double FRONT_SONAR_X = 0.0; // in meters
    public final double FRONT_SONAR_Y = 0.0; // in meters
    //    Offset of the Back Sonar wrt the robot's origin
    public final double BACK_SONAR_X = 0.0; // in meters
    public final double BACK_SONAR_Y = 0.0; // in meters
    public final double SONAR_DIST = Math.sqrt((FRONT_SONAR_X - BACK_SONAR_X)*(FRONT_SONAR_X - BACK_SONAR_X) - 
            (FRONT_SONAR_Y - BACK_SONAR_Y)*(FRONT_SONAR_Y - BACK_SONAR_Y)); // distance between the sonars in meters

    //    Obstacle Threshold segmenting obstacle points and non-obstacle points
    public final double threshold = 0.0;

    private boolean obstacleDetected = false;
    private double frontSonarDist = 0.0;
    private double backSonarDist = 0.0;

    //    Color Msgs
    private ColorMsg redMsg;
    private ColorMsg greenMsg;
    private ColorMsg blueMsg;
    private ColorMsg blackMsg;

    public LineEstimator lineEstimator = new LineEstimator();

    public enum State {
        STOP_ON_BUMP, ALIGN_ON_BUMP, ALIGNING, ALIGNED, ALIGNED_AND_ROTATED, BACKING_UP, 
        FINDING_WALL, TRACKING_WALL, WALL_ENDED, DONE
    }

    //    Velocity Constants
    public final double SLOW_FWD = 0.25; // slow forward translational velocity
    public final double SLOW_REV = -SLOW_FWD; // slow backwards translational velocity
    public final double FAST_FWD = 0.75; // fast forward translational velocity
    public final double FAST_REV = -FAST_FWD; // fast backwards translational velocity
    public final double SLOW_CCW = 0.25; // slow ccw rotational velocity
    public final double SLOW_CW = -SLOW_CCW; // slow cw rotational velocity
    public final double FAST_CCW = 0.75; // fast ccw rotational velocity
    public final double FAST_CW = -FAST_CCW; // fast cw rotational velocity
    public final double STOP = 0.0; // stop value
    public final MotionMsg stopMsg;

    public LocalNavigation(){
        setState(State.STOP_ON_BUMP);
        generateColorMsgs();

        stopMsg = new MotionMsg();
        stopMsg.translationalVelocity = STOP;
        stopMsg.rotationalVelocity = STOP;
System.out.println("in the constructor of LocalNavigation");
    }

    @Override
    public void onStart(Node node) {
System.out.println("in onstart");
        frontSonarSub = node.newSubscriber("/rss/Sonars/Front", "rss_msgs/SonarMsg");
        backSonarSub = node.newSubscriber("/rss/Sonars/Back", "rss_msgs/SonarMsg");
        bumpersSub = node.newSubscriber("/rss/BumpSensors", "rss_msgs/BumpMsg");
        statePub = node.newPublisher("/rss/state", "std_msgs/String");
        odometrySub = node.newSubscriber("/rss/Odometry", "rss_msgs/OdometryMsg");

        guiLinePub = node.newPublisher("gui/Line", "lab5_msgs/GUILineMsg");
        guiSegPub = node.newPublisher("gui/Segment", "lab5_msgs/GUISegmentMsg");
        guiPtPub = node.newPublisher("gui/Point", "lab5_msgs/GUIPointMsg");
        guiErasePub = node.newPublisher("gui/Erase", "lab5_msgs/GUIEraseMsg");
        motionPub = node.newPublisher("command/Motors", "rss_msgs/MotionMsg");

        bumpersSub
        .addMessageListener(new MessageListener<org.ros.message.rss_msgs.BumpMsg>() {
            @Override
            public void onNewMessage(
                    org.ros.message.rss_msgs.BumpMsg message) {

                //              3.1 //TODO: print out the sensor data

                System.out.println("Left " + message.left);
		System.out.println("Right " + message.right);
                leftBumper = message.left;
                rightBumper = message.right;

                //                //                3.2 Stop Robot when state == STOP_ON_BUMP and either bumper is pressed
                //                if (state == State.STOP_ON_BUMP){
                //                    if (leftBumper || rightBumper){
                //                        motionPub.publish(stopMsg);
                //                    }
                //                }
                //
                //                //                3.3 
                //                if ((state == State.ALIGN_ON_BUMP) && (leftBumper || rightBumper)){
                //                    setState(State.ALIGNING);
                //                }
                //
                //                if (state == State.ALIGNING){
                //                    if (!leftBumper && !rightBumper){
                //                        //                      Move slowly forward

                //                MotionMsg msg = new MotionMsg();
                //                msg.translationalVelocity = SLOW_FWD;
                //                msg.rotationalVelocity = STOP;
                //                        motionPub.publish(msg);
                //                    } else if (leftBumper && rightBumper){
                //                        motionPub.publish(stopMsg);
                //                        setState(State.ALIGNED);
                //                    } else {
                //                        if (leftBumper){
                //                            //                          rotate right/CCW
                //                MotionMsg msg = new MotionMsg();
                //                msg.translationalVelocity = STOP;
                //                msg.rotationalVelocity = SLOW_CCW;
                //                        motionPub.publish(msg);
                //                        } else {
                //                            //                          rotate left / CW

                //                MotionMsg msg = new MotionMsg();
                //                msg.translationalVelocity = STOP;
                //                msg.rotationalVelocity = SLOW_CW;
                //                        motionPub.publish(msg);
                //                        }
                //                    }
                //                }
                //
                //                //                4
                //                if (state == State.ALIGNED){
                //                    //                    back up a small amount, stop, rotate pi/2 cw, stop
                //                need to make a loop where do not exit until rotate pi/2
                //                        motionPub.publish(stopMsg);
                //                    setState(State.ALIGNED_AND_ROTATED);
                //                }
                //
                //                //                4.1
                //                //                need to check that the state doesn't become algined and rotated anywhere else b/c need
                //                //                this same behavior, maybe just use a method to change state to algined and rotated and then
                //                //                run the appropriate code
                //                if (state == State.ALIGNED_AND_ROTATED){
                //                    setState(State.BACKING_UP);
                //                }
            }
        });        

        frontSonarSub
        .addMessageListener(new MessageListener<org.ros.message.rss_msgs.SonarMsg>() {
            @Override
            public void onNewMessage(
                    org.ros.message.rss_msgs.SonarMsg message) {

                //			    3.1 //TODO: print out the sensor data
                System.out.println(message.range);


            }
        });

        backSonarSub
        .addMessageListener(new MessageListener<org.ros.message.rss_msgs.SonarMsg>() {
            @Override
            public void onNewMessage(
                    org.ros.message.rss_msgs.SonarMsg message) {

                //              3.1 //TODO: print out the sensor data
                System.out.println(message.range);

            }
        });

        odometrySub
        .addMessageListener(new MessageListener<org.ros.message.rss_msgs.OdometryMsg>() {
            @Override
            public void onNewMessage(
                    org.ros.message.rss_msgs.OdometryMsg message) {
                robotX = message.x;
                robotY = message.y;
                robotTheta = message.theta;
                //                Not quite sure what supposed to do about ensuring that the robot is at (0,0,0) at the start)

            }
        });

        //        For 3.5
        //        Robot.resetRobotBase();
        //        Robot.setVelocity(0.0, 0.0);
        //                        motionPub.publish(stopMsg);
System.out.println("at end of onstart");
    }

    /**
     * Set the state and publish the new state to the /rss/state topic
     * @param newState
     */
    public void setState(State newState){
        state = newState;
//        need to figure out how to use string message
//        org.ros.message.std_msgs.String str = new org.ros.message.std_msgs.String();
//        str = state.toString();
//        statePub.publish(str);
    }

    /**
     * Handles the sonar message as both sonars utilize pretty much the same logic at least for now
     * @param message
     * @param frontSonar whether the sonar is the front sonar
     */
    public void sonarHandler(org.ros.message.rss_msgs.SonarMsg message){

        if (message.range < threshold){
            obstacleDetected = true;
            if (message.isFront){
                frontSonarDist = message.range;
            } else {
                backSonarDist = message.range;
            }
        } else {
            obstacleDetected = false;
        }

        //        3.5 plotting the location of each sonar ping in the world frame

        //        GUIPointMsg ptMsg = new GUIPointMsg();
        //        if (message.isFront){
        //            //            Adding a PI/2 shift b/c the sonar is on the left face of the robot
        //            ptMsg.x = robotX + FRONT_SONAR_X + message.range*Math.cos(robotTheta + Math.PI/2);
        //            ptMsg.y = robotY + FRONT_SONAR_Y + message.range*Math.sin(robotTheta + Math.PI/2);
        //            //            Readings from the front sensor are red
        //            ptMsg.color = redMsg;
        //        } else {
        //            //          Adding a PI/2 shift b/c the sonar is on the left face of the robot
        //            ptMsg.x = robotX + BACK_SONAR_X + message.range*Math.cos(robotTheta + Math.PI/2);
        //            ptMsg.y = robotY + BACK_SONAR_Y + message.range*Math.sin(robotTheta + Math.PI/2);
        //            //          Readings from the back sensor are blue
        //            ptMsg.color = blueMsg;
        //        }
        //        //        TODO: may need to add shape to the point message, but not sure what to do there
        //
        //        guiPtPub.publish(ptMsg);

        //       3.5 Plotting non obstacles and obstacle points
        //        System.out.println(message.range);
        //        May need to also check if the range is 0 which may be for infinite distance
        //        GUIPointMsg ptMsg = new GUIPointMsg();
        //        if (!obstacleDetected){
        ////            Non obstacle points are in green
        //            ptMsg.color = greenMsg;
        //        } else {
        ////            Obstacle points are in black
        //            ptMsg.color = blackMsg;
        //        }
        //
        //        guiPtPub.publish(ptMsg);


        //        3.6 Linear Filter Stuff
        //        double x;
        //        double y;
        //        if (obstacleDetected){
        //            if (message.isFront){
        //                //            Adding a PI/2 shift b/c the sonar is on the left face of the robot
        //                x = robotX + FRONT_SONAR_X + message.range*Math.cos(robotTheta + Math.PI/2);
        //                y = robotY + FRONT_SONAR_Y + message.range*Math.sin(robotTheta + Math.PI/2);
        //            } else {
        //                //          Adding a PI/2 shift b/c the sonar is on the left face of the robot
        //                x = robotX + BACK_SONAR_X + message.range*Math.cos(robotTheta + Math.PI/2);
        //                y = robotY + BACK_SONAR_Y + message.range*Math.sin(robotTheta + Math.PI/2);
        //            }
        //            
        ////            Updating and replotting line
        //            lineEstimator.updateTerms(x, y);
        //            GUILineMsg lineMsg = new GUILineMsg();
        //            lineMsg.lineA = lineEstimator.getA();
        //            lineMsg.lineB = lineEstimator.getB();
        //            lineMsg.lineC = lineEstimator.getC();
        //            lineMsg.color = blackMsg;
        //            guiLinePub.publish(lineMsg);
        //        }

        // 4.1
        //        if (state == State.BACKING_UP){ 
        //            if (obstacleDetected){
        //                //              back up slowly and track the wall 
        //            } else {
        //                setState(State.FINDING_WALL);
        //            }
        //        }
        //      going to need to construct a loop that will publish the velocities (rotational and translational) every time step
        //        do the same type of controller to determine the rotational velocity and translational velocity
        //        but not quite sure how to do this
        //        distance too close need to rotate outwards so cw slightly
        //        too far need to rotate inwards ccw slightly
        //        for angle similar, need to compute angle from sonar sensors
        //        double transError = d - lineEstimator.getPerpDist(robotX, robotY); // where d is the desired distance to remain from the wall
        //
        //        double obsSlope = Math.atan2(-lineEstimator.getA(), lineEstimator.getB());
        //        double orientError = obsSlope - robotTheta;
        //        may also want to also store each sonar value when an obstacle is detected
        //        and then compute the angle from the triangle formed by their difference and the distance
        //        between the sonars and add this in as a factor of the orientation error
        //        orientError += (obsSlope - Math.atan2(frontSonarDist - backSonarDist, SONAR_DIST));
        //        determine the translational and rotational velocities

        //        4.2
        //        if (state == State.FINDING_WALL){
        //            if (!obstacleDetected){
        //                //            Robot moves forward slowly
        //                MotionMsg msg = new MotionMsg();
        //                msg.translationalVelocity = SLOW_FWD;
        //                msg.rotationalVelocity = STOP;
        //                motionPub.publish(msg);
        //            } else {
        //                setState(State.TRACKING_WALL);
        //                //             TODO   current robot pose and sonar readings store in fields
        //
        //                lineEstimator.resetFilter();
        //            }
        //        }
        //
        //        if (state == State.TRACKING_WALL){
        //            //            move slowly forward tracking wall with feedback controller, update linear filter, and SonarGUI see code above
        //
        //            if (!obstacleDetected){
        //                motionPub.publish(stopMsg);
        //                setState(State.WALL_ENDED);
        //
        //                //                erase wall fit line from SonarGUI, generate more accurate line segment
        //            }
        //        }
        
//        6
        if (state == State.WALL_ENDED){
            setState(State.ALIGN_ON_BUMP);
//            robot drives slowly ccw along circle radius d tangent to current heading
//            use random color generator to choose a new color
            
//            if back at the original state, enter state done
        }
        
    }

    private void generateColorMsgs(){
        redMsg = new ColorMsg();
        redMsg.r = 255;
        redMsg.g = 0;
        redMsg.b = 0;

        greenMsg = new ColorMsg();
        greenMsg.r = 0;
        greenMsg.g = 255;
        greenMsg.b = 0;

        blueMsg = new ColorMsg();
        blueMsg.r = 0;
        blueMsg.g = 0;
        blueMsg.b = 255;

        blackMsg = new ColorMsg();
        blackMsg.r = 255;
        blackMsg.g = 255;
        blackMsg.b = 255;
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
        return new GraphName("rss/localnavigation");
    }
}
