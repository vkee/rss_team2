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
//                Siply print out the PWM values
                int[] pwmVals = msg.pwm;
                for (int i = 0; i < pwmVals.length; i++) {
                    System.out.println("PWM Value at Channel " + i + " is: " + pwmVals[i]);
                }
            }
        });
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