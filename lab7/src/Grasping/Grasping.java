package Grasping;

import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.message.rss_msgs.*;
import org.ros.message.MessageListener;
import org.ros.node.Node;
import org.ros.node.NodeMain;

import VisualServo.BlobTracking;
import VisualServo.Image;
import VisualServo.VisionGUI;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;


public class Grasping implements NodeMain {
    private Publisher<ArmMsg> armPWMPub;
    private Subscriber<ArmMsg> armStatusSub;
    private Subscriber<BumpMsg> bumpersSub;
    private Publisher<MotionMsg> motionPub;
    private Subscriber<OdometryMsg> odometrySub;
    private Subscriber<org.ros.message.sensor_msgs.Image> vidSub;
    private VisionGUI gui;

    private State currState;
    private ArmGymState gymState;
    private ArmGraspState graspState;
    private ShoulderController shoulderServo;
    private WristController wristServo;
    private GripperController gripperServo;
    protected boolean objDetected = false;
    protected boolean objGrasped = false;

    // Robot Odometry
    private double robotX = 0.0; // in meters
    private double robotY = 0.0; // in meters
    private double robotTheta = 0.0; // in radians

    // Start Positions of the Move
    private double startX = 0.0; // in meters
    private double startY = 0.0; // in meters
    private double startTheta = 0.0; // in radians
    // Goal Positions of the Move
    private double goalX = 0.0; // in meters
    private double goalY = 0.0; // in meters
    private double goalTheta = 0.0; // in radians

    private double MOVE_DIST = 0.25; // in meters

    double FWD_GAIN = .5;
    double ROT_GAIN = .2;
    double DIST_TOL = 0.05; // tolerance to move MOVE_DIST in meters

    //    For Part 4
    protected BlobTracking blobTrack = null;

    protected static final int width = 160;

    protected static final int height = 120;

    private double target_hue_level = 0.0; // (Solution)
    private double hue_threshold = 0.08; // (Solution)
    private double saturation_level = 0.5; // (Solution)
    // // Units are fraction of total number of pixels detected in blob //
    // (Solution)
    private double blob_size_threshold = 0.005; // (Solution)
    private double target_radius = 0.25; // (Solution)
    private double desired_fixation_distance = .5; // (Solution)
    private double translation_error_tolerance = .05;// (Solution)
    private double translation_velocity_gain = .75;// (Solution)
    private double translation_velocity_max = .75;// (Solution)
    private double rotation_error_tolerance = 0.2; // (Solution)
    private double rotation_velocity_gain = 0.15; // (Solution)
    private double rotation_velocity_max = 0.15; // (Solution)
    private boolean use_gaussian_blur = true;// (Solution)
    private boolean approximate_gaussian = false;// (Solution)
    protected ArrayBlockingQueue<byte[]> visionImage = new ArrayBlockingQueue<byte[]>(
            1);

    public enum ArmGraspState {
        INITIALIZE, OPEN_GRIPPER, FIND_OBJ, GRASP, LIFT, MOVE, DEPOSIT_WRIST, DEPOSIT_SHOULDER, DEPOSIT_GRIPPER, RETURN
    }

    // States for Arm Gymnastics
    public enum ArmGymState {
        INITIALIZE, OPEN_GRIPPER, CLOSE_GRIPPER, MOVE_UP, BEND_ELBOW, MOVE_TO_GROUND
    }

    // States dividing up space so that no servo can move more than 1 radian per
    // iteration
    public enum State {
        UP, DOWN
    }

    public Grasping() {
        currState = State.DOWN;
        gymState = ArmGymState.INITIALIZE; // gymnastics
        graspState = ArmGraspState.FIND_OBJ; // gymnastics
        shoulderServo = new ShoulderController(525, 2375, Math.PI, 1500, 525);
        wristServo = new WristController(350, 2250, Math.PI, 1250, 2025);
        gripperServo = new GripperController(1700, 2450, Math.PI, 1700, 2450);
        blobTrack = new BlobTracking(width, height);

        //    For Part 4
        blobTrack.targetHueLevel = target_hue_level;// (Solution)
        blobTrack.hueThreshold = hue_threshold;// (Solution)
        blobTrack.saturationLevel = saturation_level;// (Solution)
        blobTrack.blobSizeThreshold = blob_size_threshold;// (Solution)
        blobTrack.targetRadius = target_radius;// (Solution)
        blobTrack.desiredFixationDistance = desired_fixation_distance;// (Solution)
        blobTrack.translationErrorTolerance = translation_error_tolerance;// (Solution)
        blobTrack.translationVelocityGain = translation_velocity_gain;// (Solution)
        blobTrack.translationVelocityMax = translation_velocity_max;// (Solution)
        blobTrack.rotationErrorTolerance = rotation_error_tolerance;// (Solution)
        blobTrack.rotationVelocityGain = rotation_velocity_gain;// (Solution)
        blobTrack.rotationVelocityMax = rotation_velocity_max;// (Solution)
        blobTrack.useGaussianBlur = use_gaussian_blur;// (Solution)
        blobTrack.approximateGaussian = approximate_gaussian;// (Solution)

        gui = new VisionGUI();

    }

    @Override
    public void onStart(Node node) {
        armPWMPub = node.newPublisher("command/Arm", "rss_msgs/ArmMsg");
        armStatusSub = node.newSubscriber("rss/ArmStatus", "rss_msgs/ArmMsg");
        bumpersSub = node.newSubscriber("/rss/BumpSensors", "rss_msgs/BumpMsg");
        motionPub = node.newPublisher("command/Motors", "rss_msgs/MotionMsg");
        odometrySub = node.newSubscriber("/rss/odometry",
                "rss_msgs/OdometryMsg");

        final boolean reverseRGB = node.newParameterTree().getBoolean(
                "reverse_rgb", false);

        vidSub = node.newSubscriber("/rss/video", "sensor_msgs/Image");
        vidSub.addMessageListener(new MessageListener<org.ros.message.sensor_msgs.Image>() {
            @Override
            public void onNewMessage(org.ros.message.sensor_msgs.Image message) {
                byte[] rgbData;
                if (reverseRGB) {
                    rgbData = Image.RGB2BGR(message.data, (int) message.width,
                            (int) message.height);
                } else {
                    rgbData = message.data;
                }
                assert ((int) message.width == width);
                assert ((int) message.height == height);
                handle(rgbData);
            }
        });

        // Initialization State

        // For debugging
        /*
         * System.out.println("Shoulder Servo Slope: " +
         * shoulderServo.LINE_SLOPE);
         * System.out.println("Shoulder Servo Intercept: " +
         * shoulderServo.LINE_THETA_INTERCEPT);
         * System.out.println("Wrist Servo Slope: " + wristServo.LINE_SLOPE);
         * System.out.println("Wrist Servo Intercept: " +
         * wristServo.LINE_THETA_INTERCEPT);
         */
        // PART 4

        // blobTrack = new BlobTracking(width, height);

        // blobTrack.targetHueLevel = target_hue_level;

        // vidPub = node.newPublisher("/rss/blobVideo", "sensor_msgs/Image");
        // vidSub = node.newSubscriber("/rss/video","sensor_msgs/Image");
        // Image dest = new Image(src);
        // ;
        /*
         * vidSub.addMessageListener(new
         * MessageListener<org.ros.message.sensor_msgs.Image>() {
         * 
         * @Override public void onNewMessage(org.ros.message.sensor_msgs.Image
         * message) {
         * 
         * 
         * org.ros.message.sensor_msgs.Image pubImage = new
         * org.ros.message.sensor_msgs.Image(); pubImage.width = width;
         * pubImage.height = height; pubImage.encoding = "rgb8";
         * pubImage.is_bigendian = 0; pubImage.step = width*3; pubImage.data =
         * dest.toArray(); vidPub.publish(pubImage); } });
         */

        // END PART 4

        armStatusSub.addMessageListener(new MessageListener<ArmMsg>() {

            int[] pwm_stat = new int[3];
            int wrist = 0;
            int shoulder = 0;
            int gripper = 0;

            public void sendCommands() {
                pwm_stat[0] = shoulder;
                pwm_stat[1] = wrist;
                pwm_stat[2] = gripper;

                ArmMsg msg = new ArmMsg();
                msg.pwms[0] = pwm_stat[0];
                msg.pwms[1] = pwm_stat[1];
                msg.pwms[2] = pwm_stat[2];
                armPWMPub.publish(msg);

                //System.out.println("message sent");
                //System.out.println(Arrays.toString(pwm_stat));
            }

            @Override
            public void onNewMessage(ArmMsg msg) {

                // long[] pwmVals = msg.pwms;
                int[] pwmVals = pwm_stat;
                int shoulderPWM = (int) pwmVals[0];
                int wristPWM = (int) pwmVals[1];
                int gripperPWM = (int) pwmVals[2];

                System.out.println(Arrays.toString(pwmVals));

                System.out.println("Current State: " + graspState);
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
                        //                        Part 4 Stuff
                        Image src = null;
                        try {
                            src = new Image(visionImage.take(), width, height);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Image dest = new Image(src);

                        blobTrack.apply(src, dest);

                        // update newly formed vision message
                        gui.setVisionImage(dest.toArray(), width, height);

                        // Begin Student Code

                        // publish velocity messages to move the robot towards the target
                        MotionMsg vidMsg = new MotionMsg(); // (Solution)
                        vidMsg.translationalVelocity = blobTrack.translationVelocityCommand; // (Solution)
                        vidMsg.rotationalVelocity = blobTrack.rotationVelocityCommand; // (Solution)
                        System.out.println("Trans Vel: " + vidMsg.translationalVelocity);
                        System.out.println("Rot Vel: " + vidMsg.rotationalVelocity);

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
                        //                        System.out.println("Arm should be at 45 degrees and now switching into move");
                        graspState = ArmGraspState.MOVE;
                        startX = robotX;
                        startY = robotY;
                        startTheta = robotTheta;
                        goalX = robotX + MOVE_DIST*Math.cos(robotTheta);
                        goalY = robotY + MOVE_DIST*Math.sin(robotTheta);
                        goalTheta = robotTheta;
                    }
                } else if (graspState == ArmGraspState.MOVE) {
                    double remDist = getDist(robotX, robotY, goalX, goalY);

                    if (remDist > DIST_TOL) {
                        MotionMsg moveMsg = new MotionMsg();
                        moveMsg.translationalVelocity = Math.min(FWD_GAIN * remDist, 0.25);
                        moveMsg.rotationalVelocity = 0.0;
                        System.out.println("Rem Dist: " + remDist);

                        System.out.println("Trans Vel: " + moveMsg.translationalVelocity);
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
                        goalX = robotX - MOVE_DIST*Math.cos(robotTheta);
                        goalY = robotY - MOVE_DIST*Math.sin(robotTheta);
                        goalTheta = robotTheta;
                    }
                } else if (graspState == ArmGraspState.RETURN) {
                    double remDist = getDist(robotX, robotY, goalX, goalY);

                    if (remDist > DIST_TOL) {
                        System.out.println("Rem Dist: " + remDist);
                        MotionMsg moveMsg = new MotionMsg();
                        moveMsg.translationalVelocity = -Math.min(FWD_GAIN * remDist, 0.25);
                        moveMsg.rotationalVelocity = 0.0;

                        System.out.println("Trans Vel: " + moveMsg.translationalVelocity);
                        motionPub.publish(moveMsg);
                    } else {
                        MotionMsg moveMsg = new MotionMsg();
                        moveMsg.translationalVelocity = 0.0;
                        moveMsg.rotationalVelocity = 0.0;                        
                        motionPub.publish(moveMsg);
                        System.out.println("DONE with Grasp and Transport");
                    }
                } 

                sendCommands();

            }
        });

        bumpersSub.addMessageListener(new MessageListener<BumpMsg>() {
            @Override
            public void onNewMessage(BumpMsg msg) {
                // System.out.println("msg.left state: " + msg.left);
                // System.out.println("msg.right state: " + msg.right);
                // System.out.println("msg.gripper state: " + msg.gripper);
                objDetected = msg.gripper;

                if (objDetected) {
                    System.out.println("objDetected in bumper sub");
                }

            }
        });

        odometrySub.addMessageListener(new MessageListener<OdometryMsg>() {
            @Override
            public void onNewMessage(OdometryMsg msg) {
                robotX = msg.x;
                robotY = msg.y;
                robotTheta = msg.theta;
            }
        });
    }

    /**
     * Moves the arm to the desired x, z position in the robot frame
     * 
     * @param desX
     *            the desired x coordinate for the end effector
     * @param desZ
     *            the desired z coordinate for the end effector
     */
    public void moveArm(double desX, double desZ, int currShoulderPWM,
            int currWristPWM) {

        double[] angles = InverseKinematics.getThetaPhi(desX, desZ,
                shoulderServo.getThetaRad(currShoulderPWM),
                wristServo.getThetaRad(currWristPWM));

        int desShoulderPWM = shoulderServo.getPWM(angles[0]);
        int desWristPWM = wristServo.getPWM(angles[1]);

        ArmMsg msg = new ArmMsg();
        msg.pwms[0] = shoulderServo.getSafePWM(currShoulderPWM, desShoulderPWM);
        msg.pwms[1] = wristServo.getSafePWM(currWristPWM, desWristPWM);
        armPWMPub.publish(msg);
    }

    /**
     * Rotates all of the servos concurrently (the handle(ArmMsg msg) method)
     */
    private void rotateAllServos(int shoulderPWM, int wristPWM, int gripperPWM) {

        int SHIFT_AMOUNT = 100;
        if (currState == State.UP) {
            // If all servos are at the UP state
            if (shoulderServo.atMax(shoulderPWM) && wristServo.atMax(wristPWM)
                    && gripperServo.isClosed(gripperPWM)) {
                System.out.println("Switching to Down");
                currState = State.DOWN;
            } else {
                ArmMsg msg = new ArmMsg();

                // msg.pwms[0] = Math.min(shoulderPWM + (shoulderServo.MAX_PWM -
                // shoulderServo.MIN_PWM)/SHIFT_AMOUNT, shoulderServo.MAX_PWM);
                // msg.pwms[1] = Math.min(wristPWM + (wristServo.MAX_PWM -
                // wristServo.MIN_PWM)/SHIFT_AMOUNT, wristServo.MAX_PWM);
                // msg.pwms[2] = Math.min(gripperPWM + (gripperServo.MAX_PWM -
                // gripperServo.MIN_PWM)/SHIFT_AMOUNT, gripperServo.MAX_PWM);

                // System.out.println("Shoulder Theta: " +
                // shoulderServo.getThetaDeg(msg.pwms[0]));
                // System.out.println("Wrist Theta: " +
                // wristServo.getThetaDeg(msg.pwms[1]));

                msg.pwms[0] = shoulderServo.fullRotation(shoulderPWM, true);
                msg.pwms[1] = wristServo.fullRotation(wristPWM, true);
                msg.pwms[2] = gripperServo.fullRotation(gripperPWM, true);
                // System.out.println("Shoulder PWM: " + msg.pwms[0]);
                armPWMPub.publish(msg);
            }
        } else if (currState == State.DOWN) {

            // If all servos are at the DOWN state
            if (shoulderServo.atMin(shoulderPWM) && wristServo.atMin(wristPWM)
                    && gripperServo.isOpen(gripperPWM)) {
                System.out.println("Switching to Up");
                currState = State.UP;
            } else {
                ArmMsg msg = new ArmMsg();
                // msg.pwms[0] = Math.max(shoulderPWM - (shoulderServo.MAX_PWM -
                // shoulderServo.MIN_PWM)/SHIFT_AMOUNT, shoulderServo.MIN_PWM);
                // msg.pwms[1] = Math.max(wristPWM - (wristServo.MAX_PWM -
                // wristServo.MIN_PWM)/SHIFT_AMOUNT, wristServo.MIN_PWM);
                // msg.pwms[2] = Math.max(gripperPWM - (gripperServo.MAX_PWM -
                // gripperServo.MIN_PWM)/SHIFT_AMOUNT, gripperServo.MIN_PWM);
                msg.pwms[0] = shoulderServo.fullRotation(shoulderPWM, false);
                msg.pwms[1] = wristServo.fullRotation(wristPWM, false);
                msg.pwms[2] = gripperServo.fullRotation(gripperPWM, false);

                // System.out.println("Shoulder Theta: " +
                // shoulderServo.getThetaDeg(msg.pwms[0]));
                // System. System.out.println("2 " + wristPWM);
                // out.println("Wrist Theta: " +
                // wristServo.getThetaDeg(msg.pwms[1]));
                armPWMPub.publish(msg);
            }
        }
    }

    /**
     * Writes the provided PWM value for the shoulder servo
     * 
     * @param value
     *            the PWM value to write to the shoulder servo
     */
    /*
     * private void writeShoulderPWM(int value) { ArmMsg msg = new ArmMsg();
     * msg.pwms[0] = value; armPWMPub.publish(msg); }
     */

    /**
     * Writes the provided PWM value for the wrist servo
     * 
     * @param value
     *            the PWM value to write to the wrist servo
     */
    /*
     * private void writeWristPWM(int value) { ArmMsg msg = new ArmMsg();
     * msg.pwms[1] = value; armPWMPub.publish(msg); }
     */

    /**
     * Writes the provided PWM value for the gripper servo
     * 
     * @param value
     *            the PWM value to write to the gripper servo
     */
    /*
     * private void writeGripperPWM(int value) { ArmMsg msg = new ArmMsg();
     * msg.pwms[2] = value; armPWMPub.publish(msg); }
     */

    /**
     * <p>
     * Handle a CameraMessage. Perform blob tracking and servo robot towards
     * target.
     * </p>
     * 
     * @param rawImage
     *            a received camera message
     */
    public void handle(byte[] rawImage) {

        visionImage.offer(rawImage);
    }

    /**
     * Returns the distance between two points
     * 
     * @param pt1X
     *            the x coordinate of point 1
     * 
     * @param pt1Y
     *            the y coordinate of point 1
     * 
     * @param pt2X
     *            the x coordinate of point 2
     * 
     * @param pt2Y
     *            the y coordinate of point 2
     */
    public double getDist(double pt1X, double pt1Y, double pt2X, double pt2Y) {
        return Math.sqrt((pt1X - pt2X) * (pt1X - pt2X) + (pt1Y - pt2Y)
                * (pt1Y - pt2Y));
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
        return new GraphName("rss/grasping");
    }
}