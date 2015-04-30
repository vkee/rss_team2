package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;
import VisualServo.*;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.rss_msgs.*;
import org.ros.node.topic.Publisher;

/**
 * This state uses the visual servoing to approach the block.
 */
public class VisualServoCollect implements FSMState {

	// protected Publisher<MotionMsg> publisher; // (Solution)
	protected MultipleBlobTracking blobTrack = null;
	private FSM fsm;
	client cl = null;
	protected static final int width = 640;
	protected static final int height = 480;


	public VisualServoCollect(FSM stateMachine, MultipleBlobTracking blobTrack_in) {
		fsm = stateMachine;
		// publisher = node.newPublisher("command/Motors",
		// "rss_msgs/MotionMsg"); // How do I actually get the publisher?
		//blobTrack = new MultipleBlobTracking(width, height);
		blobTrack = blobTrack_in;
		// init any variables for this state
		cl = new client();
	}

	public stateENUM getName() {
		return stateENUM.VISUALSERVOCOLLECT;
	}

	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.WHEELS || msgType == msgENUM.FLAP) return true;
		return false;
	}

	public void update(GenericMessage msg) {
		if (msg.type == msgENUM.WHEELS)  updateDrive(msg);
		else if (msg.type == msgENUM.FLAP) updateGate(msg);
	}

	private void updateDrive(GenericMessage msg){

//		System.out.println("Beginning drive Update");
		// do stuff
		org.ros.message.rss_msgs.OdometryMsg message = (org.ros.message.rss_msgs.OdometryMsg) msg.message;

		Image src = null;
		float[] depth_array = null;
		try {
			src = cl.getImage();
			depth_array = cl.getDepthImage();
			blobTrack.apply(src,depth_array);
		} catch (Exception e) {
		}

		MotionMsg mo_msg = new MotionMsg(); // (Solution)
		mo_msg.translationalVelocity = .2 * blobTrack.translationVelocityCommand;
		mo_msg.rotationalVelocity = .2 * blobTrack.rotationVelocityCommand;
		//System.out.println(" TRANS VEL: " + mo_msg.translationalVelocity + " ROT VEL: " + mo_msg.rotationalVelocity);
		fsm.motionPub.publish(mo_msg); // (Solution)

		//if condition to leave state
		if(blobTrack.isDone()){		
			fsm.updateState(new MoveForward(fsm));
		}
//		System.out.println("End Update");
	}

	private void updateGate(GenericMessage msg){

		//TODO: add bump message handling
		//TODO: increment counter of collected blocks
			// do we have a counter for this yet?
}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

}
