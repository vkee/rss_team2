package digitalIO;

import orc.DigitalInput;
import orc.Orc;

import org.ros.message.rss_msgs.DigitalStatusMsg;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

public class DigitalIOPublisher implements Runnable {

    Node node;
    Orc orc;
    DigitalInput[] slowInputs = new DigitalInput[8];
    DigitalInput[] fastInputs = new DigitalInput[8];
    DigitalStatusMsg msg;
    Publisher<DigitalStatusMsg> pub;
    Object lock;

    public DigitalIOPublisher(Node node, Orc orc, Object lock){
	this.node = node;
	this.orc = orc;
	this.lock = lock;
	for (int i = 0; i < 16; i++){
	    if(i < 8){
		slowInputs[i] = new DigitalInput(orc, i, false, false); 
	    } else {
		int j = i - 8;
		fastInputs[j] = new DigitalInput(orc, i, false, false);
	    }
	}
	pub = node.newPublisher("rss/DigitalIO", "rss_msgs/DigitalStatusMsg");
    }


    @Override public void run() {
	msg = new DigitalStatusMsg();
	while (true){
	    for (int i = 0; i <8; i ++) {
		synchronized(lock) {
		    msg.fast[i] = fastInputs[i].getValue();
		    msg.slow[i] = slowInputs[i].getValue();
		}
	    }
	    pub.publish(msg);
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e){
		e.printStackTrace();
	    }
	}
    }
}
