package digitalIO;

import orc.DigitalInput;
import orc.Orc;

import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.ros.message.rss_msgs.*;

public class BreakBeamPublisher implements Runnable {
	
    Node node;
    Orc orc;
    DigitalInput input;
    BreakBeamMsg msg;
    Publisher<BreakBeamMsg> pub;
    Object lock;

    public BreakBeamPublisher(Node node, Orc orc, Object lock) {
	this.node = node;
	this.orc = orc;
	this.lock = lock;
	input = new DigitalInput(orc, 7, false, false);
	pub = node.newPublisher("rss/BreakBeam", "rss_msgs/BreakBeamMsg");
    }

    @Override public void run() {
	// TODO Auto-generated method stub
	msg = new BreakBeamMsg();
	while (true){
	    synchronized(lock) {
		msg.beamBroken = input.getValue();
	    }
	    pub.publish(msg);
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
    
}
