package analogIO;

import orc.AnalogInput;
import orc.Orc;

import org.ros.message.rss_msgs.AnalogStatusMsg;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

public class AnalogIOPublisher implements Runnable {

    Node node;
    Orc orc;
    AnalogInput[] inputs = new AnalogInput[8];
    AnalogStatusMsg msg;
    Publisher<AnalogStatusMsg> pub;
    Object lock;
	
    public AnalogIOPublisher(Node node, Orc orc, Object lock){
	this.node = node;
	this.orc = orc;
	this.lock = lock;
	for (int i = 0; i < 8; i++){
	    inputs[i] = new AnalogInput(orc, i);
	}
	pub = node.newPublisher("rss/AnalogIO", "rss_msgs/AnalogStatusMsg");
    }
	
    @Override public void run() {
	msg = new AnalogStatusMsg();
	while(true){
	    for (int i = 0; i < 8; i ++){
		synchronized(lock) {
		    msg.values[i] = inputs[i].getVoltage();
		}
	    }
	    pub.publish(msg);
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
