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
	protected int stepCounter = 0;
	protected double lastStepTime = 0.0;

	public int width;
	public int height;

	private int RED = 0;
	private int GREEN = 1;
	private int BLUE = 2;

	private int preference = RED;

	private boolean DRAW_HISTOGRAM = false;
	private boolean HSB_HISTOGRAM = true;
	private boolean PERFORM_CLASSIFICATION = true;

	private double RED_HUE = 0.0 / 3.0;
	private double GREEN_HUE = 1.0 / 3.0;
	private double BLUE_HUE = 2.0 / 3.0;
	private double hues[] = { RED_HUE, GREEN_HUE, BLUE_HUE };

	// Variables used for velocity controller that are available to calling
	// process. Visual results are valid only if targetDetected==true; motor
	// velocities should do something sane in this case.
	public boolean targetDetected = false; // set in blobPresent()
	public double centroidX = 0.0; // set in blobPresent()
	public double centroidY = 0.0; // set in blobPresent()
	public double targetArea = 0.0; // set in blobPresent()
	public double targetRange = 0.0; // set in blobFix()
	public double targetBearing = 0.0; // set in blobFix()
	public boolean targetFar = false;

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
	 *            the src RGB image, not packed
	 * @param dest
	 *            the destination RGB image, packed, may be null
	 */
	public void apply(Image src, Image dest) {

		long redCoordinateSumX = 0;
		long redCoordinateSumY = 0;
		int numberOfPixels = 0;

		stepTiming(); // monitors the frame rate

		// Begin Student Code
		int red_pixel_counter = 0;
		int i_counter = 0;
		int j_counter = 0;
		for (int i = 0; i < src.getWidth(); i++) {
			for (int j = 0; j < src.getHeight(); j++) {
				int red = src.getPixelRed(i, j);
				// byte red_b = src.getPixelRed(i,j);
				int green = src.getPixelGreen(i, j);
				// byte green_b = src.getPixelGreen(i,j);
				int blue = src.getPixelBlue(i, j);
				// int blue_b = src.getPixelBlue(i,j);
				if (red < 0) {
					red += 256;
				}
				if (green < 0) {
					green += 256;
				}
				if (blue < 0) {
					blue += 256;
				}
				// System.out.println(red);

				if (red > blue * 1.4 && red > green * 1.4) {
					dest.setPixel(i, j, (byte) 255, (byte) 0, (byte) 0);
					// System.err.println("Pixel at i = " + i + "j= " + j);
					i_counter += i;
					j_counter += j;
					red_pixel_counter++;

				} else {
					int value = red / 3 + blue / 3 + green / 3;
					dest.setPixel(i, j, (byte) value, (byte) value,
							(byte) value);
				}

			}
		}
		// System.out.println(red_pixel_counter);
		if (red_pixel_counter > 300) {
			targetArea = red_pixel_counter;
			targetDetected = true;
			targetFar = false;
			// System.out.println("BALL IS IN IMAGE. DO SOMETHING CRAZY!");
			int centroid_i = i_counter / red_pixel_counter;
			int centroid_j = j_counter / red_pixel_counter;
			centroidX = i_counter / red_pixel_counter - src.getWidth() / 2;
			centroidY = j_counter / red_pixel_counter - src.getHeight() / 2;
			// System.out.println(centroid_i);
			// System.out.println(centroid_j);
			dest.setPixel(centroid_i, centroid_j, (byte) 0, (byte) 255,
					(byte) 0);
			for (int k = 0; k < 4; k++) {
				dest.setPixel(centroid_i + k, centroid_j + k, (byte) 0,
						(byte) 255, (byte) 0);
				dest.setPixel(centroid_i - k, centroid_j - k, (byte) 0,
						(byte) 255, (byte) 0);
				dest.setPixel(centroid_i + k, centroid_j - k, (byte) 0,
						(byte) 255, (byte) 0);
				dest.setPixel(centroid_i - k, centroid_j + k, (byte) 0,
						(byte) 255, (byte) 0);

			}
			blobFix();
//			System.out.println(targetArea);
		} else if (red_pixel_counter < 300) {
			targetFar = true;
			targetDetected = true;
			
		} else {
			targetFar = false;
			targetDetected = false;

		}

	}

	/*
	 * int h = this.height; int w = this.width;
	 * 
	 * int start_h = h/2-2; int start_w = w/2-2;
	 * 
	 * int end_h = h/2+2; int end_w = w/2+2;
	 */
	// Histogram.getHistogram(src,dest,true);
	// classify(src,dest);
	/*
	 * blobPresent(src,dest);
	 * 
	 * for (int i=0;i<src.getWidth();i++){ for(int j=0;j<src.getHeight;j++){ int
	 * red = src.getPixelRed(i,j); int green = src.getPixelGreen(i,j); int blue
	 * = src.getPixelBlue(i,j); if (red < 0){ red += 256; } if (green < 0){
	 * green += 256; } if (blue < 0){ blue += 256; } float [] hsb =
	 * java.awt.Color.RGBtoHSB(red,green,blue,null);
	 * if(src.isHue(src.getPixel(i,j),hues[preference],0.05,hsb)){
	 * dest.setPixel(i, j, 255,255,255); //replaces desired pixels with white
	 * pixels. numberOfPixels++; redCoordinateSumX += i; redCoordinateSumY += j;
	 * 
	 * } } / }
	 * 
	 * if(numberOfPixels !=0){ redCoordinateSumX /= (long)numberOfPixels;
	 * redCoordinateSumY /= (long)numberOfPixels; } if(redCoordinateSumX > 0 &&
	 * redCoordinateSumY > 0){
	 * System.err.println("Ball Found : Estimated center at X -> " +
	 * redCoordinateSumX + "Y -> " +redCoordinateSumY); } //End Student Code
	 * 
	 * } /* Tests if a pixel is a particular hue, with reasonable saturation and
	 * brightness to make such a determination
	 */

	/*
	 * public boolean isHue(int pixel ,double hue,double tolerance,float hsb
	 * []){ double pixHue = hsb[0]; if (hsb[1] <0.35 || hsb[2] < 0.5) { return
	 * false; }
	 * 
	 * pixHue -= hue;
	 * 
	 * while (pixHue > 0.5) { pixHue -= 1; } while (pixHue < -0.5) { pixHue +=
	 * 1; }
	 * 
	 * return Math.abs(pixHue) < tolerance; }
	 */
	/*
	 * Classifies each pixel in the image as target pixel or not and converts
	 * classified targetPixels into saturated colors, eg. Pixel(255,0,0) for a
	 * red ball and non- blob pixels are converted into gray-scale.
	 */

	/*
	 * public void classify(Image src,Image dest){ stepTiming(); for (int
	 * i=0;i<src.getWidth();i++){ for(int j=0;j<src.getHeight();j++){ int red =
	 * src.getPixelRed(i,j); int green = src.getPixelGreen(i,j); int blue =
	 * src.getPixelBlue(i,j); if (red < 0){ red += 256; } if (green < 0){ green
	 * += 256; } if (blue < 0){ blue += 256; } float [] hsb =
	 * java.awt.Color.RGBtoHSB(red,green,blue,null);
	 * if(src.isHue(src.getPixel(i,j),hues[preference],0.05,hsb)){ if
	 * (preference == RED){ dest.setPixel(i,j,255,0,0); }else if
	 * (preference==GREEN){ dest.setPixel(i,j,0,255,0); } else{
	 * dest.setPixel(i,j,0,0,255); } }
	 * 
	 * else{ int grayScale=(red+blue+green)/3;
	 * dest.setPixel(i,j,grayScale,grayScale,grayScale); } } } }
	 */

	// Begin Student Code
	/*
	 * This method takes the data outputed from blobPresent.
	 * 
	 * @return Writes data to public double targetRange public double
	 * targetBearing
	 * 
	 * It is very simple, not precise and can be improved a lot.
	 */
	// TODO TEST
	public void blobFix() {
		if (!targetDetected) {
			return;
		}

		// parameters
		double ballImageAreaAtOneMeter = 500;
		double cameraHeight = 0.35;
		double imageToCameraXConversion = 1 / 600;
		double imageToCameraAngleXConversion = 1.0 / 140;

		double ballImageX = centroidX;
		double ballImageY = centroidY;

		double ballImageArea = targetArea;

		double ballCameraDistance = Math.sqrt(ballImageAreaAtOneMeter
				/ ballImageArea);

		double heightAngle = Math.asin(cameraHeight / ballCameraDistance);

		double ballRobotDistance = Math.cos(heightAngle) * ballCameraDistance;

		// Assumes that the camera is perfectly in the center of the robot and
		// that it is image is linear; which is a lie, but it is a good
		// approximation
		// Assumes that there is a linear conversion between ballImageX and
		// ballCameraX
		// double ballCameraX=ballImageX*imageToCameraXConversion;

		// Converts now from the camera frame to the robot frame. Since the
		// camera is in the center of the robotm this conversion will be simple.
		double ballRobotX = 0;// ballCameraX;
		double ballRobotY;
		if (ballRobotX < ballRobotDistance) {
			ballRobotY = Math.sqrt(ballRobotDistance * ballRobotDistance
					- ballRobotX * ballRobotX);
		} else { // Should never happen, but if happens, we will define the
					// distance in y to be the total distance, to protect
					// against imaginary numbers
			ballRobotY = ballRobotDistance;
		}

		// Now converts from cartesian coordinates to radial coordinates
		double ballRobotAngle = imageToCameraAngleXConversion * ballImageX;// Math.atan2(ballRobotY,ballRobotX);

		// Writes the data
		targetRange = ballRobotDistance;
		targetBearing = ballRobotAngle;
//		System.out.println();
//		System.out.println(targetRange);
//		System.out.println(targetBearing);
//		System.out.println(ballImageX);
//		System.out.println();
		//

	}

	// End student code

	/*
	 * This method counts the number of target pixels in the frame, calculates
	 * the centroid and draws a cross at the centroid of the ball.It also does
	 * the classification of the image using classify(Image,Image) in
	 * BlobTracking.
	 * 
	 * @param src - src image from the camera dest - The Destination image that
	 * is going to the display for being displayed.
	 * 
	 * @return returns an int[] consisting of Area,X,Y of the blob found on the
	 * screen.
	 */
	/*
	 * public int[] blobPresent(Image src, Image dest){ if
	 * (PERFORM_CLASSIFICATION) { classify(src,dest); } stepTiming();
	 * 
	 * int[] returnArray = new int[3]; int matchingPixels = 0; long
	 * matchingPixelsXCount = 0; long matchingPixelsYCount = 0;
	 * 
	 * for(int i=0;i<dest.getWidth();i++){ for(int j=0;j<dest.getHeight();j++){
	 * int nextPixel= dest.getPixel(i,j); byte
	 * nextPixelRed=dest.getPixelRed(i,j); byte
	 * nextPixelGreen=dest.getPixelGreen(i,j); byte
	 * nextPixelBlue=dest.getPixelBlue(i,j);
	 * 
	 * boolean condition = (preference == RED && nextPixelRed == 255 &&
	 * nextPixelGreen== 0 && nextPixelBlue== 0) || (preference == GREEN &&
	 * nextPixelRed == 0 && nextPixelGreen == 255 && nextPixelBlue == 0) ||
	 * (preference == BLUE && nextPixelRed == 0 && nextPixelGreen == 0 &&
	 * nextPixelBlue == 255); if (condition){ matchingPixels++;
	 * matchingPixelsXCount += i; matchingPixelsYCount += j; } } }
	 * if(matchingPixels >50){ matchingPixelsXCount /= (long)matchingPixels;
	 * matchingPixelsYCount /= (long)matchingPixels; }else{ matchingPixelsXCount
	 * = 0; matchingPixelsYCount = 0; } returnArray[0] = (int) matchingPixels;
	 * //area returnArray[1] = (int)matchingPixelsXCount -
	 * (int)(dest.getWidth()/2); // Averaged X with (0,0) as center of image
	 * returnArray[2] = (int)matchingPixelsYCount - (int)(dest.getHeight()/2);
	 * // Averaged Y with (0,0) as center of image if(matchingPixelsXCount > 0
	 * && matchingPixelsYCount > 0){ for (int i = (int)matchingPixelsXCount - 8;
	 * i< (int)matchingPixelsXCount + 9;i++){ if(i < dest.getWidth() && i > 0){
	 * dest.setPixel(i,(int)matchingPixelsYCount,255,255,255); } } for (int i =
	 * (int)matchingPixelsYCount - 8; i< (int)matchingPixelsYCount + 9;i++){
	 * if(i < dest.getHeight() && i > 0){
	 * dest.setPixel((int)matchingPixelsXCount ,i,255,255,255); } } } if
	 * (DRAW_HISTOGRAM) { Histogram.getHistogram(src, dest, HSB_HISTOGRAM); }
	 * 
	 * centroidX=returnArray[1]; centroidY=returnArray[2];
	 * targetArea=returnArray[0];
	 * 
	 * return returnArray;
	 * 
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

	// BlobPixel();

	// System.out.println("NEW MEASUREMENT");
	/*
	 * for (int i = 0; i < w; i++) { for (int j = 0; j <h; j++) { int redPixel =
	 * src.getPixelRed(i,j); if (redPixel < 0){ redPixel += 256; } int
	 * greenPixel = src.getPixelGreen(i,j); if (greenPixel < 0){ greenPixel +=
	 * 256; } int bluePixel = src.getPixelBlue(i,j); if (bluePixel < 0){
	 * bluePixel += 256; } System.out.print("RED: ");
	 * System.out.println(redPixel); System.out.print("GREEN: ");
	 * System.out.println(greenPixel); System.out.print("BLUE: ");
	 * System.out.println(bluePixel); System.out.println(""); } }
	 */
	// End Student Code

	/*
	 * public void BlobPixel(){ for (int i = 0; i < w; i++) { for (int j = 0; j
	 * <h; j++) { int redPixel = src.getPixelRed(i,j); if (redPixel < 0){
	 * redPixel += 256; } int greenPixel = src.getPixelGreen(i,j); if
	 * (greenPixel < 0){ greenPixel += 256; } int bluePixel =
	 * src.getPixelBlue(i,j); if (bluePixel < 0){ bluePixel += 256; }
	 * System.out.print("RED: "); System.out.println(redPixel);
	 * System.out.print("GREEN: "); System.out.println(greenPixel);
	 * System.out.print("BLUE: "); System.out.println(bluePixel);
	 * System.out.println(""); } }
	 * 
	 * 
	 * }
	 */

}
