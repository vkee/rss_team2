package VisualServo;

import java.util.concurrent.ArrayBlockingQueue;

import org.ros.message.MessageListener;
import org.ros.message.rss_msgs.MotionMsg;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

/**
 * 
 * @author previous TA's, prentice, vona
 * 
 */
public class VisualServo implements NodeMain, Runnable {

	public client cl = null;

	protected static final int width = 640;

	protected static final int height = 480;

	protected Publisher<MotionMsg> publisher; // (Solution)

	/**
	 * <p>
	 * The blob tracker.
	 * </p>
	 **/
	protected MultipleBlobTracking blobTrack = null;

	private VisionGUI gui;
	protected ArrayBlockingQueue<byte[]> visionImage = new ArrayBlockingQueue<byte[]>(
			1);

	protected boolean firstUpdate = true;

	public Subscriber<org.ros.message.rss_msgs.OdometryMsg> odoSub;

	/**
	 * <p>
	 * Create a new VisualServo object.
	 * </p>
	 */
	public VisualServo() {

		setInitialParams();

		gui = new VisionGUI();
	}

	protected void setInitialParams() {

	}

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

	@Override
	public void run() {
		cl = new client();
		while (true) {
			Image src = null;
			float[] depth_float_array = null;
			double[] depth_double_array = null;

			try {
				src = cl.getImage();
				depth_float_array = cl.getDepthImage();
				depth_double_array = cl.getDepthArray();

				if (src == null)
					continue;
			} catch (Exception e) {
				continue;
			}
			Image dest = new Image(src);
			// Image depth = Image.floatRGB(depth_float_array);
			// Image depth = Image.doubleRGB(depth_double_array);
			blobTrack.apply(src, dest, depth_float_array);// , depth_array);
			//
			// update newly formed vision message
			gui.setVisionImage(dest.toArray(), width, height);

			// Begin Student Code
			// publish velocity messages to move the robot towards the target

			MotionMsg msg = new MotionMsg(); // (Solution)
			msg.translationalVelocity = blobTrack.translationVelocityCommand;
			msg.rotationalVelocity = blobTrack.rotationVelocityCommand;
			publisher.publish(msg); // (Solution)

			// End Student Code
		}
	}

	/**
	 * <p>
	 * Run the VisualServo process
	 * </p>
	 * 
	 * @param node
	 *            optional command-line argument containing hostname
	 */
	@Override
	public void onStart(Node node) {
		blobTrack = new MultipleBlobTracking(width, height);
		// initialize the ROS publication to command/Motors

		publisher = node.newPublisher("command/Motors", "rss_msgs/MotionMsg"); // (Solution)

		// End Student Code

		final boolean reverseRGB = node.newParameterTree().getBoolean(
				"reverse_rgb", false);
		odoSub = node.newSubscriber("/rss/odometry", "rss_msgs/OdometryMsg");
		odoSub.addMessageListener(new MessageListener<org.ros.message.rss_msgs.OdometryMsg>() {
			@Override
			public void onNewMessage(
					org.ros.message.rss_msgs.OdometryMsg message) {
				if (firstUpdate) {
					firstUpdate = false;
					gui.resetWorldToView(message.x, message.y);
				}
				gui.setRobotPose(message.x, message.y, message.theta);
			}
		});
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
