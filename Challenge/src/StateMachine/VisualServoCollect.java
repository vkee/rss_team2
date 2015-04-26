package StateMachine;

import StateMachine.FSM.msgENUM;
import StateMachine.FSM.stateENUM;
import VisualServo.*;
/**
 * This state uses the visual servoing to approach the block.
 */
public class VisualServoCollect implements FSMState {

	//protected Publisher<MotionMsg> publisher; // (Solution)
	protected MultipleBlobTracking blobTrack = null;
	private FSM fsm;	
	client cl = null;
	protected static final int width = 640;
	protected static final int height = 480;

	private double target_hue_level = 0; // (Solution)
	private double hue_threshold = 0.08; // (Solution)
	private double saturation_level = 0.5; // (Solution)
	/*
	private double target_hue_level = 130/360; // (Solution)
	private double hue_threshold = 0.1; // (Solution)
	private double saturation_level = 0.4; // (Solution)
	*/
	// // Units are fraction of total number of pixels detected in blob //
	// (Solution)
	private double blob_size_threshold = 0.005; // (Solution)
	private double target_radius = 0.1; // (Solution)
	private double desired_fixation_distance = .5; // (Solution)
	private double translation_error_tolerance = .05;// (Solution)
	private double translation_velocity_gain = .75;// (Solution)
	private double translation_velocity_max = .75;// (Solution)
	private double rotation_error_tolerance = 0.2; // (Solution)
	private double rotation_velocity_gain = 0.15; // (Solution)
	private double rotation_velocity_max = 0.15; // (Solution)
	private boolean use_gaussian_blur = true;// (Solution)
	private boolean approximate_gaussian = false;// (Solution)


	public VisualServoCollect(FSM stateMachine)
		{
		fsm = stateMachine;
		//publisher = node.newPublisher("command/Motors", "rss_msgs/MotionMsg"); // How do I actually get the publisher?
		blobTrack = new MultipleBlobTracking(width, height);
		//init any variables for this state
		cl = new client();

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
		}	

	
	public stateENUM getName()
		{return stateENUM.VISUALSERVOCOLLECT;}

	
	public boolean accepts(msgENUM msgType)
		{
		if (msgType == msgENUM.WHEELS) return true;
		return false;
		}


	public void update(GenericMessage msg)
		{
		//do stuff
		org.ros.message.rss_msgs.OdometryMsg message = (org.ros.message.rss_msgs.OdometryMsg)msg.message;
		

		Image src = null;
		try {
			src = cl.getImage();
			blobTrack.apply(src);			
		} catch (Exception e) {
		}	
	
		// publish velocity messages to move the robot towards the target
   		//message.translationalVelocity = blobTrack.translationVelocityCommand; //how do i use message?
		//message.rotationalVelocity = .75*blobTrack.rotationVelocityCommand;  //how do i use message?
		//publisher.publish(message); 


		//if condition to leave state
		if(blobTrack.isDone()){		
			fsm.updateState(new MoveForward(fsm));
		}

		}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}


}
