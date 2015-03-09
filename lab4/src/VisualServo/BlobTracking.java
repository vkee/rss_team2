package VisualServo;

import java.awt.Color;

/**
 * BlobTracking performs image processing and tracking for the VisualServo
 * module. BlobTracking filters raw pixels from an image and classifies blobs,
 * generating a higher-level vision image.
 *
 * @author previous TA's, prentice
 */
public class BlobTracking {

    private static int H_INDEX = 0;
    private static int S_INDEX = 1;
    private static int B_INDEX = 2;

    private static double[] CHANNEL_HUES = new double[3];
    static {
	CHANNEL_HUES[Image.Channel.RED.offset] = 0.;
	CHANNEL_HUES[Image.Channel.GREEN.offset] = 1. / 3;
	CHANNEL_HUES[Image.Channel.BLUE.offset] = 2. / 3;
    }

    protected int stepCounter = 0;
    protected double lastStepTime = 0.0;

    // image width
    public int width;

    // image height
    public int height;

    // Variables used for velocity controller that are available to calling
    // process. Visual results are valid only if targetDetected==true
    // motor velocities should do something sane in this case.
    public boolean targetDetected = false;
    public double centroidX = 0.0;
    public double centroidY = 0.0;
    public double targetArea = 0.0;

    public double targetRange = 0.0;

    /**
     * <p>
     * Create a BlobTracking object
     * </p>
     *
     * @param width
     *            image width
     * @param height
     *            image height
     */
    public BlobTracking(int width, int height) {
	this.width = width;
	this.height = height;
    }

    /**
     * <p>
     * Computes frame rate of vision processing
     * </p>
     */
    private void stepTiming() {
	double currTime = System.currentTimeMillis();
	stepCounter++;
	// if it's been a second, compute frames-per-second
	if (currTime - lastStepTime > 1000.0) {
	    // double fps = (double) stepCounter * 1000.0
	    // / (currTime - lastStepTime);
	    // System.err.println("FPS: " + fps);
	    stepCounter = 0;
	    lastStepTime = currTime;
	}
    }

    /**
     * <p>
     * Segment out a blob from the src image (if a good candidate exists).
     * </p>
     *
     * <p>
     * <code>dest</code> is a packed RGB image for a java image drawing routine.
     * If it's not null, the blob is highlighted.
     * </p>
     *
     * @param src
     *            the source RGB image, not packed
     * @param dest
     *            the destination RGB image, packed, may be null
     */
    public void apply(Image src, Image dest) {

	stepTiming(); // monitors the frame rate
	// Begin Student Code

	// -- overlay the histogram -- //
	// boolean hsb = true;
	// Histogram.getHistogram(src, dest, hsb);

	// -- print the central pixels -- //
	// int squareWidth = 4;
	// printCentralPixels(src, squareWidth);

	// -- apply Gaussian blur -- //
	byte[] srcArr = src.toArray();
	GaussianBlur.apply(src.toArray(), srcArr, src.getWidth(),
		src.getHeight());
	src = new Image(srcArr, src.getWidth(), src.getHeight());

	// -- ball color of interest -- //
	Image.Channel ballColor = Image.Channel.RED;

	// -- filter the source image to red and greyscale --//
	blobPixel(src, dest, ballColor);

	// -- detect if a ball is present -- //
	blobPresent(src, dest, ballColor);

	// -- update targetRange and Bearing Params -- //
	blobFix();

	// End Student Code
    }

    /**
     * <p>
     * Detects if a ball is present
     * </p>
     *
     * @param src
     *            the source RGB image
     * @param dest
     *            the dest image where the square box around the blob is drawn
     * @param channel
     *            color channel (RED, BLUE, GREEN) of the blob to search for
     */
    public void blobPresent(Image src, Image dest, Image.Channel channel) {

	// -- number of pixels in the ball -- //
	int ballArea = 0;

	centroidX = 0;
	centroidY = 0;

	for (int x = 0; x < dest.getWidth(); x++) {
	    for (int y = 0; y < dest.getHeight(); y++) {
		float[] hsb = dest.getPixelHSB(x, y);
		if (testHsbChannel(hsb, channel)) {
		    ballArea++;
		    centroidX += x;
		    centroidY += y;
		}
	    }
	}

	if (ballArea > 0) {
	    centroidX /= ballArea;
	    centroidY /= ballArea;
	    // System.out.format("Ball Area: %d %n", ballArea);
	    // System.out
	    // .format("Ball Centroid: (%f,%f) %n", centroidX, centroidY);
	}

	// -- did we detect a ball? -- //
	double areaThreshold = 0.003;
	targetDetected = (ballArea > areaThreshold * width * height);

	// -- if detected, update area and draw square and crosshair -- //
	if (targetDetected) {
	    targetArea = ballArea;
	    // -- draw a square around the centroid of the ball --//
	    int radius = (int) Math.sqrt(ballArea / Math.PI);
	    for (int i = -radius; i <= radius; i++) {
		if (centroidY + i >= 0 && centroidY + i < height) {
		    if (centroidX + radius < width) {
			dest.setPixel((int) (centroidX + radius),
				(int) (centroidY + i), (byte) 0, (byte) 0,
				(byte) 0);
		    }
		    if (centroidX - radius >= 0) {
			dest.setPixel((int) (centroidX - radius),
				(int) (centroidY + i), (byte) 0, (byte) 0,
				(byte) 0);
		    }
		}
		if (centroidX + i >= 0 && centroidX + i < width) {
		    if (centroidY + radius < height) {
			dest.setPixel((int) (centroidX + i),
				(int) (centroidY + radius), (byte) 0, (byte) 0,
				(byte) 0);
		    }
		    if (centroidY - radius >= 0) {
			dest.setPixel((int) (centroidX + i),
				(int) (centroidY - radius), (byte) 0, (byte) 0,
				(byte) 0);
		    }
		}
	    }

	    // -- draw a cross at the centroid --//
	    int crossRadius = 10;
	    for (int i = -crossRadius; i <= crossRadius; i++) {
		if (centroidX + i >= 0 && centroidX + i < width) {
		    dest.setPixel((int) (centroidX + i), (int) (centroidY),
			    (byte) 0, (byte) 0, (byte) 0);
		}
		if (centroidY + i >= 0 && centroidY + i < height) {
		    dest.setPixel((int) (centroidX), (int) (centroidY + i),
			    (byte) 0, (byte) 0, (byte) 0);
		}
	    }
	}
    }

    /**
     * <p>
     * Detects channel pixels in the source image, sets the corresponding pixel
     * in dest as RGB (255,0,0) if RED, RGB (0, 255, 0) if GREEN, and RGB
     * (0,0,255) if BLUE. Pixels from other channels will be converted to
     * greyscale.
     * </p>
     *
     * @param src
     *            the source RGB image, not packed
     * @param dest
     *            the destination RGB image, may be null. It is desirable for
     *            speed reasons that src == dest;
     * @param channel
     *            color of the blob to search for
     */
    public void blobPixel(Image src, Image dest, Image.Channel channel) {
	if (dest == null) {
	    dest = new Image(src);
	}

	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {

		int[] channel_rgbs = new int[] { 0, 0, 0 };

		float[] hsb = src.getPixelHSB(x, y);
		boolean match = testHsbChannel(hsb, channel);

		if (match) {
		    channel_rgbs[channel.offset] = 255;
		} else {
		    int red = src.getPixelRedInt(x, y);
		    int green = src.getPixelGreenInt(x, y);
		    int blue = src.getPixelBlueInt(x, y);
		    int greyscale = (red + green + blue) / 3;
		    channel_rgbs[Image.Channel.RED.offset] = greyscale;
		    channel_rgbs[Image.Channel.GREEN.offset] = greyscale;
		    channel_rgbs[Image.Channel.BLUE.offset] = greyscale;
		}
		dest.setPixel(x, y,
			(byte) channel_rgbs[Image.Channel.RED.offset],
			(byte) channel_rgbs[Image.Channel.GREEN.offset],
			(byte) channel_rgbs[Image.Channel.BLUE.offset]);
	    }
	}
    }

    /**
     * Computes the estimated distance (in meter) from the target, and angular
     * differential (in radians). Updates the result in targetRange and
     * targetBearing.
     * 
     * @return a pair of the form (distance, angle) between the robot's center
     *         of mass and the target
     */
    public void blobFix() {
	// -- estimated using a regression function -- //
	double SLOPE = -0.06;
	double INTERCEPT = 105;
	
	if (targetArea == width * height)
		targetRange = 0;
	
	targetRange = (SLOPE * targetArea + INTERCEPT) / 100.;
	// since we use regression on the test data from 0.5 m to 1 m,
    // the regression will fail when the distance is too small, which
    // means that the targetRange will become very negative. In this case, we 
    // want the robot to move back with constant speed as if the 
    // targetRange is 0. The targetRange breaks down when targetArea =
    // 1690 (with slope =-0.06 and intercept = 105) 
    if (targetRange < 0){
	    targetRange = 0.0;
	}
    }

    /**
     * <p>
     * Returns true if hsb is similar to the color specified by the Channel
     * </p>
     *
     * @param hsb
     *            the hsb to test
     * @param channel
     *            the reference Channel (RED, GREEN, or BLUE) to test against
     */
    private boolean testHsbChannel(float[] hsb, Image.Channel channel) {
	// -- reject very bright or dark hues -- //
	// if (hsb[B_INDEX] <= 0.1 || 0.85 <= hsb[B_INDEX])
	// return false;

	// -- reject low saturation -- //
	if (hsb[S_INDEX] <= 0.5)
	    return false;

	// -- extract the hues of interest -- //
	double channelHue = CHANNEL_HUES[channel.offset];
	double pixelHue = hsb[H_INDEX];
	double TOLERANCE = 0.05;

	// System.out.format("Testing %f vs %f %n", channelHue, pixelHue);

	// -- compare difference, and wrap around circle -- //
	pixelHue -= channelHue;
	while (pixelHue > 0.5)
	    pixelHue -= 1;
	while (pixelHue < -0.5)
	    pixelHue += 1;

	return Math.abs(pixelHue) <= TOLERANCE;
    }

    /**
     * <p>
     * Print to standard out the average RGB values in the center of an image
     * </p>
     *
     * @param src
     *            the source RGB image, not packed
     * @param squareWidth
     *            half the width of the central square to average
     */
    public void printCentralPixels(Image src, int squareWidth) {

	int redAvg = 0, greenAvg = 0, blueAvg = 0;
	float hueAvg = 0, satAvg = 0, brAvg = 0;
	double k = 0.;

	int xstart = (int) (src.getWidth() / 2. - squareWidth);
	int xend = (int) (src.getWidth() / 2. + squareWidth);

	int ystart = (int) (src.getHeight() / 2. - squareWidth);
	int yend = (int) (src.getHeight() / 2. + squareWidth);

	for (int x = xstart; x < xend; x++) {
	    for (int y = ystart; y < yend; y++) {
		// -- obtain pixel characteristics -- //
		int red = src.getPixelRedInt(x, y);
		int green = src.getPixelGreenInt(x, y);
		int blue = src.getPixelBlueInt(x, y);
		float[] hsb = Color.RGBtoHSB(red, green, blue, null);

		// -- update RGB averages -- //
		redAvg += red;
		greenAvg += green;
		blueAvg += blue;

		// -- update HSB averages --//
		hueAvg += hsb[H_INDEX];
		satAvg += hsb[S_INDEX];
		brAvg += hsb[B_INDEX];

		k++;
	    }
	}

	// -- compute final RGB averages --//
	redAvg = (int) (redAvg / k);
	greenAvg = (int) (greenAvg / k);
	blueAvg = (int) (blueAvg / k);

	// -- compute final HSB averages --//
	hueAvg /= k;
	satAvg /= k;
	brAvg /= k;

	System.out.format("Average Central RGB: [%d, %d, %d]%n", redAvg,
		greenAvg, blueAvg);
	System.out.format("Average Central HSB: [%f, %f, %f]%n", hueAvg,
		satAvg, brAvg);
    }
}
