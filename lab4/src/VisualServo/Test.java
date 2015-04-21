package VisualServo;

import java.util.concurrent.ArrayBlockingQueue;

import org.ros.message.MessageListener;
import org.ros.message.rss_msgs.MotionMsg;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import java.awt.Color;

/**
 * 
 * @author previous TA's, prentice, vona
 * 
 */
public class Test implements NodeMain, Runnable {
	
	public client cl = null;


	protected static final int width = 640;

	protected static final int height = 480;

	protected Publisher<MotionMsg> publisher; // (Solution)

	/**
	 * <p>
	 * The blob tracker.
	 * </p>
	 **/
//	protected BlobTracking blobTrack = null;
	protected MultipleBlobTracking blobTrack = null;

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

	private VisionGUI gui;

	public Subscriber<org.ros.message.sensor_msgs.Image> vidSub;
	public Subscriber<org.ros.message.rss_msgs.OdometryMsg> odoSub;

	/**
	 * <p>
	 * Create a new VisualServo object.
	 * </p>
	 */
	public Test() {
		setInitialParams();
		gui = new VisionGUI();
	}

	protected void setInitialParams() {

	}

	@Override
	public void run() {
		cl = new client();
//		while (true) {

			//assert image 
			Image src = null;
			try {
				src = cl.getTestImage(4);
			} catch (Exception e) {
				//continue;
			}
			Image dest = new Image(src);
			//while(true){
			blobTrack.apply(src, dest);
			// update newly formed vision message
			gui.setVisionImage(dest.toArray(), width, height);
			
			// Begin Student Code

			// publish velocity messages to move the robot towards the target
/*
			 MotionMsg msg = new MotionMsg(); // (Solution)
			 msg.translationalVelocity = blobTrack.translationVelocityCommand;
			// // (Solution)
			msg.rotationalVelocity = .75*blobTrack.rotationVelocityCommand; //
			// (Solution)
			 publisher.publish(msg); // (Solution)
*/
			// End Student Code
//		}

	}


	@Override
	public void onStart(Node node) {
		blobTrack = new MultipleBlobTracking(width, height);
//		blobTrack = new BlobTracking(width,height);
		// Begin Student Code

		// set parameters on blobTrack as you desire

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

		// initialize the ROS publication to command/Motors

//		publisher = node.newPublisher("command/Motors", "rss_msgs/MotionMsg"); // (Solution)

		// End Student Code

		Thread runningStuff = new Thread(this);
		runningStuff.start();
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
		return new GraphName("rss/visualservo");
	}
}

/*
			int pix = src.getPixel(x, y); // (Solution)
			int red = Image.pixelRed(pix);
			int blue = Image.pixelBlue(pix);
			int green = Image.pixelGreen(pix);
			
			if (red < 0)
				red += 255;
			if (blue < 0)
				blue += 255;
			if (green < 0)
				green += 255;
			
			float[] hsb = Color.RGBtoHSB(red,green,blue, null); // (Solution)
			double threshold = 0.05;
			
			if (hsb[0] == 0 && hsb[1] > .2 && hsb[2] > .2){
				dest.setPixel(x, y, (byte) 0xff, (byte) 0, (byte) 0);
			}

			if (Math.abs(hsb[0] - 120.0/360)< threshold && hsb[1] > .2 && hsb[2] > .1){
				dest.setPixel(x, y, (byte) 0, (byte) 0xff, (byte) 0);
			}

			if (Math.abs(hsb[0] - 120.0/360)< threshold && hsb[1] > .2 && hsb[2] > .2){
				dest.setPixel(x, y, (byte) 0, (byte) 0xff, (byte) 0);
			}

			if (Math.abs(hsb[0] - 240.0/360)< threshold && hsb[1] > .3 && hsb[2] > .2){
				dest.setPixel(x, y, (byte) 0, (byte) 0, (byte) 0xff);
			}

			if (Math.abs(hsb[0] - 60.0/360)< threshold && hsb[1] > .8 && hsb[2] > .2){
				dest.setPixel(x, y, (byte) 0xff, (byte) 0xff, (byte) 0);
			}
*/
