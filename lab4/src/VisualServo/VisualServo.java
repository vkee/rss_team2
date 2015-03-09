package VisualServo;

import java.util.concurrent.ArrayBlockingQueue;

import org.ros.message.MessageListener;
import org.ros.message.rss_msgs.MotionMsg;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

//import org.apache.commons.logging.Log;

/**
 * 
 * @author previous TA's, prentice, vona
 *
 */
public class VisualServo implements NodeMain, Runnable {

	private static final int width = 160;

	private static final int height = 120;

	private boolean done = false;
	// protected Log log;

	/**
	 * <p>
	 * The blob tracker.
	 * </p>
	 **/
	private BlobTracking blobTrack = null;

	private VisionGUI gui;
	private ArrayBlockingQueue<byte[]> visionImage = new ArrayBlockingQueue<byte[]>(
			1);

	protected boolean firstUpdate = true;

	public Subscriber<org.ros.message.sensor_msgs.Image> vidSub;
	public Subscriber<org.ros.message.rss_msgs.OdometryMsg> odoSub;
	public Publisher<org.ros.message.rss_msgs.MotionMsg> motionPub;

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
		while (true) {
			Image src = null;
			try {
				src = new Image(visionImage.take(), width, height);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}

			Image dest = new Image(src);

			blobTrack.apply(src, dest);

			// update newly formed vision message
			gui.setVisionImage(dest.toArray(), width, height);

			// // Get estimated range (in meters) and bearing (in signed
			// radians)
			// double rangeError = desiredRange - blobTrack.targetRange;
			// double rangeSignal = RANGE_KP * rangeError;
			// System.out.format("Target Range %f %n", blobTrack.targetRange);
			// System.out.format("Signal Range %f %n", rangeSignal);
			//
			// double centroidError = desiredCentroid - blobTrack.centroidX;
			// double centroidSignal = CENTROID_KP * centroidError;
			// System.out.format("Target CentroidX %f %n", blobTrack.centroidX);
			// System.out.format("Signal Centroid %f %n", centroidSignal);

			MotionMsg msg = new MotionMsg();

			// if (blobTrack.targetDetected) {
			// if (Math.abs(rangeError) > RANGE_TOLERANCE)
			// msg.translationalVelocity = rangeSignal;
			// if (Math.abs(centroidError) > CENTROID_TOLERANCE)
			// msg.rotationalVelocity = centroidSignal;
			// } else {
			// msg.translationalVelocity = 0.0;
			// msg.rotationalVelocity = 0.0;
			// }
			// Begin Student Code TODO
			double distance;
			double angle;
			distance = blobTrack.targetRange;
			angle = blobTrack.targetBearing;
			// org.ros.message.rss_msgs.MotionMsg msg = new
			// org.ros.message.rss_msgs.MotionMsg();
			double desiredDistance = 0.75;
			double desiredAngle = 0;
			double gainDistance = 0.5;
			double gainAngle = 0.1;
			double distanceError = desiredDistance - distance;
			if (blobTrack.targetDetected) {
				if (Math.abs(angle) > 0.1) {
					System.out.println("Correcting angle only");
					msg.rotationalVelocity = gainAngle * (desiredAngle - angle);
					msg.translationalVelocity = 0;
				} else if (Math.abs(distanceError) > 0.1) {
					System.out.println("Correcting distance only");
					msg.translationalVelocity = 0.25 * gainDistance
							* (distance-desiredDistance);
					msg.rotationalVelocity = 0;
				}
			} else {
				System.out.println("Stopped");
				msg.rotationalVelocity = 0;
				msg.translationalVelocity = 0;
			}
			System.out.println("Distance" + distance);
			System.out.println("Desired Distance" + desiredDistance);
			System.out.println("Distance Error" + distanceError);

			// if (blobTrack.targetDetected && !blobTrack.targetFar) {
			// System.out.println("tracking blob");
			// msg.translationalVelocity = 0.25* gainDistance
			// * (desiredDistance - distance);
			// msg.rotationalVelocity = 0.5*gainAngle * (desiredAngle - angle);
			// // publish velocity messages to move the robot towards the
			// // target
			// } else if (blobTrack.targetDetected && blobTrack.targetFar) {
			// System.out.println("searching");
			// msg.translationalVelocity = 0.1;
			// msg.rotationalVelocity = 0;
			// }
			motionPub.publish(msg);
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
		blobTrack = new BlobTracking(width, height);

		// Begin Student Code

		// set parameters on blobTrack as you desire

		// initialize the ROS publication to command/Motors
		motionPub = node.newPublisher("command/Motors", "rss_msgs/MotionMsg");

		// End Student Code

		final boolean reverseRGB = node.newParameterTree().getBoolean(
				"reverse_rgb", false);

		vidSub = node.newSubscriber("/rss/video", "sensor_msgs/Image");
		vidSub.addMessageListener(new MessageListener<org.ros.message.sensor_msgs.Image>() {
			@Override
			public void onNewMessage(org.ros.message.sensor_msgs.Image message) {
				byte[] rgbData;
				if (reverseRGB) {
					rgbData = Image.RGB2BGR(message.data, (int) message.width,
							(int) message.height);
				} else {
					rgbData = message.data;
				}
				assert ((int) message.width == width);
				assert ((int) message.height == height);
				handle(rgbData);
			}
		});

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
