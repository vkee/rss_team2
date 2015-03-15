package Grasping;

import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.message.rss_msgs.*;
import org.ros.message.MessageListener;
import org.ros.node.Node;
import org.ros.message.lab7_msgs.*;

public class Grasping implements NodeMain {
    private Publisher<ArmMsg> armPWMPub;
    private Subscriber<ArmMsg> armStatusSub;
    private State currState;
    private ShoulderController shoulderServo;
    private WristController wristServo;
    private GripperController gripperServo;
    
//    States dividing up space so that no servo can move more than 1 radian per iteration
    public enum State {
        UP, DOWN
    }

    public Grasping() {
        currState = State.UP;
        shoulderServo = ShoulderController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
        wristServo = WristController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
        gripperServo = GripperController(minPWM, maxPWM, thetaRange, pwm0, pwm90);
    }

    @Override
    public void onStart(Node node) {
        armPWMPub = node.newPublisher("command/Arm", "rss_msgs/ArmMsg");
        armStatusSub = node.newSubscriber("rss/ArmStatus", "rss_msgs/ArmMsg");
    
        armStatusSub
        .addMessageListener(new MessageListener<ArmMsg>() {
            @Override
            public void onNewMessage(ArmMsg msg) {
//                Simply printing out the PWM values
                int[] pwmVals = msg.pwm;
                for (int i = 0; i < pwmVals.length; i++) {
                    System.out.println("PWM Value at Channel " + i + " is: " + pwmVals[i]);
                }
                
//              rotateAllServos(msg.pwms[0], msg.pwms[1], msg.pwms[2]);
//                gripperServo.close();
//                moveArm(desX, desZ, msg.pwms[0], msg.pwms[1]);
            }
        });
        
    }
    
    /**
     * Moves the arm to the desired x, z position in the robot frame
     * @param desX the desired x coordinate for the end effector
     * @param desZ the desired z coordinate for the end effector
     */
    public void moveArm(double desX, double desZ, int currShoulderPWM, int currWristPWM){
//      Inverse Kinematic Equation
//      TODO: don't know how to solve for the required angles
//      Let shoulderTheta, wristTheta be the angles to write for the shoulder and wrist respectively
                
        int desShoulderPWM = shoulderServo.getPWM(shoulderTheta);
        int desWristPWM = wristServo.getPWM(wristTheta);
        
        ArmMsg msg = new ArmMsg();
        msg.pwms[0] = shoulderServo.getSafePWM(currShoulderPWM, desShoulderPWM);
        msg.pwms[1] = wristServo.getSafePWM(currWristPWM, desWristPWM);
        armPWMPub.publish(msg);
    }
    
    /**
     * Rotates all of the servos concurrently (the handle(ArmMsg msg) method)
     */
    private void rotateAllServos(int shoulderPWM, int wristPWM, int gripperPWM) {
        if (currState == State.UP) {
//            If all servos are at the UP state
            if (shoulderServo.atMax(shoulderPWM) && wristServo.atMax(wristPWM) && gripperServo.atMax(gripperPWM)){
                currState = State.DOWN;
            } else {
                ArmMsg msg = new ArmMsg();
                msg.pwms[0] = shoulderServo.fullRotation(shoulderPWM, true);
                msg.pwms[1] = wristServo.fullRotation(wristPWM, true);
                msg.pwms[2] = gripperServo.fullRotation(gripperPWM, true);
                armPWMPub.publish(msg);
            }
        } else if (currState == State.DOWN) {
            
//          If all servos are at the DOWN state
            if (shoulderServo.atMin(shoulderPWM) && wristServo.atMin(wristPWM) && gripperServo.atMin(gripperPWM)){
                currState = State.UP;
            } else {
                ArmMsg msg = new ArmMsg();
                msg.pwms[0] = shoulderServo.fullRotation(shoulderPWM, false);
                msg.pwms[1] = wristServo.fullRotation(wristPWM, false);
                msg.pwms[2] = gripperServo.fullRotation(gripperPWM, false);
                armPWMPub.publish(msg);
            }
        }
//        TODO: Not quite sure what supposed to be doing, check with the TA, makes no sense why should be publishing messages each time receive arm pwm msg
//        clamped ff control step for each servo?  is this just moving at most 1 radian per control step?
//        need to first see the range of motion of the servo, most likely 180 deg of rotation so only need 2 states for each direction
//        or maybe publish a message to get to the endgoal if going up or down, feed into the controlle rthe current position and whether
//        going up or down, and then it will return a pwm that is at most 1 radian max towards the max/min pwm at the end of the state?
        
//        public armMsg as follows
//      long bigPWM = msg.pwms[0];
//      long wristPWM = msg.pwms[1];
//      long gripperPWM = msg.pwms[2];
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