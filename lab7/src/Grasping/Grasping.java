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
    
//    States dividing up space so that no servo can move more than 1 radian per iteration
    public enum State {
        UP, DOWN
    }

    public Grasping() {

    }

    @Override
    public void onStart(Node node) {
        armPWMPub = node.newPublisher("command/Arm", "rss_msgs/ArmMsg");
        armStatusSub = node.newSubscriber("rss/ArmStatus", "rss_msgs/ArmMsg");
    
        armStatusSub
        .addMessageListener(new MessageListener<ArmMsg>() {
            @Override
            public void onNewMessage(ArmMsg msg) {
//                Si/ply print out the PWM values
//                long bigPWM = msg.pwms[0];
//                long wristPWM = msg.pwms[1];
//                long gripperPWM = msg.pwms[2];
                int[] pwmVals = msg.pwm;
                for (int i = 0; i < pwmVals.length; i++) {
                    System.out.println("PWM Value at Channel " + i + " is: " + pwmVals[i]);
                }
                
//                TODO: essentialyl based on the state, need to check if at desired position and then change state
//                if not, then execute the computed pwm
//              rotateAllServos();

            }
        });
        
    }
    
    /**
     * Rotates all of the servos concurrently (the handle(ArmMsg msg) method)
     */
    private void rotateAllServos() {
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