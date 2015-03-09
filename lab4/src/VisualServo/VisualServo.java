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

    private static final int width = 160;

    private static final int height = 120;

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

    public Publisher<MotionMsg> motionMsgPub;

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

	    // Begin Student Code
	    double desiredRange = 0.50;
	    double RANGE_KP = 0.5;
	    double RANGE_TOLERANCE = 0.08; // in centimeters

	    double desiredCentroid = width / 2.;
	    double CENTROID_KP = 0.001;
	    double CENTROID_TOLERANCE = 10; // in pixels

	    Image dest = new Image(src);

	    blobTrack.apply(src, dest);

	    // update newly formed vision message
	    gui.setVisionImage(dest.toArray(), width, height);

	    // Get estimated range (in meters) and bearing (in signed radians)
	    double rangeError = desiredRange - blobTrack.targetRange;
	    double rangeSignal = RANGE_KP * rangeError;
	    System.out.format("Target Range %f %n", blobTrack.targetRange);
	    System.out.format("Signal Range %f %n", rangeSignal);

	    double centroidError = desiredCentroid - blobTrack.centroidX;
	    double centroidSignal = CENTROID_KP * centroidError;
	    System.out.format("Target CentroidX %f %n", blobTrack.centroidX);
	    System.out.format("Signal Centroid %f %n", centroidSignal);

	    MotionMsg msg = new MotionMsg();
	    if (blobTrack.targetDetected) {
		if (Math.abs(rangeError) > RANGE_TOLERANCE)
		    msg.translationalVelocity = rangeSignal;
		if (Math.abs(centroidError) > CENTROID_TOLERANCE)
		    msg.rotationalVelocity = centroidSignal;
	    } else {
		msg.translationalVelocity = 0.;
		msg.rotationalVelocity = 0.;
	    }

	    motionMsgPub.publish(msg);

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
	motionMsgPub = node
		.newPublisher("command/Motors", "rss_msgs/MotionMsg");

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