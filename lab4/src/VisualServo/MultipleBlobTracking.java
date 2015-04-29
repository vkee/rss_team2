package VisualServo;

import java.awt.Color;
import java.util.*;

public class MultipleBlobTracking extends BlobTracking {

	private double[] targetHueLevels = { 0.0, 120.0 / 360, 240.0 / 360,
			60.0 / 360, 24.0 / 360 };
	// red, green, blue,yellow,orange

	private Color[] colorwheel = { Color.RED, Color.GREEN, Color.BLUE,
			Color.YELLOW, Color.ORANGE };

	private double[] hueThresholds = { 0.1, 0.1, 0.15, 0.05, 0.05 };
	private double other_hueThreshold = 0.1;

	protected double[] multiSaturationLevel = { 0.6, 0.3, 0.3, 0.8, 0.8 };
	double other_saturation = 0.6;
	double[] multiBrightnessLevel = { 0.55, 0.0, 0.0, 0.5, 0.3 };
	double other_brightness = 0.55;
	int[][] multiBlobPixelMask = null;
	int[][] multiBlobMask = null;
	int[][] multiImageConnected = null;
	int[] blobPixelMask = null;

	int[] targetArea = new int[targetHueLevels.length];
	boolean[] multiTargetDetected = { false, false, false, false, false };

	List<BlobObject> bos;
	List<FiducialObject> fos;
	List<BlockObject> blos;

	public MultipleBlobTracking() {
		super(640, 480);
		blobPixelMask = new int[width * height];
		multiBlobPixelMask = new int[targetHueLevels.length][width * height];
		multiBlobMask = new int[targetHueLevels.length][width * height];
		multiImageConnected = new int[targetHueLevels.length][width * height];
	}

	public MultipleBlobTracking(int width, int height) {
		super(width, height);
		blobPixelMask = new int[width * height];
		multiBlobPixelMask = new int[targetHueLevels.length][width * height];
		multiBlobMask = new int[targetHueLevels.length][width * height];
		multiImageConnected = new int[targetHueLevels.length][width * height];
	}

	/**
	 * fix to a particular color based on index
	 * 
	 * @param index
	 */
	protected void blobFix() {
		targetRadius = 0.01;
		focalPlaneDistance = 0.01;
		double deltaX = bos.get(0).getCentroidX() - width / 2.0;
		targetRange = focalPlaneDistance * targetRadius
				/ Math.sqrt(bos.get(0).getTargetArea() / Math.PI);
		targetBearing = Math.atan2(deltaX, focalPlaneDistance);
	}

	private void blobFix(int index) {
		double deltaX = bos.get(0).getCentroidX() - width / 2.0;
		targetRange = focalPlaneDistance * targetRadius
				/ Math.sqrt(targetArea[0] / Math.PI);
		targetBearing = Math.atan2(deltaX, focalPlaneDistance);
	}

	/**
	 * Color blob for debugging
	 */
	protected void markBlob(Image src, Image dest) { // (Solution)

		for (BlobObject bo : bos) { // loop through every blob
			int maskIndex = 0; // (Solution)
			for (int y = 0; y < height; y++) { // (Solution)
				for (int x = 0; x < width; x++) { // (Solution)
					// red, green, blue,yellow,orange,purple
					if (bo.getBlobArrIndex(maskIndex) > 0) {
						Color pixel_color = bo.getColor();
						if (pixel_color == Color.RED) {
							dest.setPixel(x, y, (byte) 0xff, (byte) 0, (byte) 0);
						} else if (pixel_color == Color.GREEN) {
							dest.setPixel(x, y, (byte) 0, (byte) 0xff, (byte) 0);
						} else if (pixel_color == Color.BLUE) {
							dest.setPixel(x, y, (byte) 0, (byte) 0, (byte) 0xff);
						} else if (pixel_color == Color.YELLOW) {
							dest.setPixel(x, y, (byte) 0xff, (byte) 0xff,
									(byte) 0);
						} else if (pixel_color == Color.ORANGE) {
							dest.setPixel(x, y, (byte) 0xff, (byte) 0x66,
									(byte) 0);
						}/*
						 * else if (pixel_color == Color.PURPLE) {
						 * dest.setPixel(x, y, (byte) 0xff, (byte) 0, (byte)
						 * 0xff); }
						 */
					}
					maskIndex++;
				}
			}
		}
	}

	/**
	 * checks if the two blob objects are the proper colors and orientation for
	 * fiducials
	 * 
	 * @param blob1
	 * @param blob2
	 * @return
	 */
	protected boolean isFiducialColorMatch(BlobObject top, BlobObject bottom) {
		// top & bottom
		// yellow & red
		// red & green
		// green & blue
		// blue & yellow
		// green & orange
		// blue & red
		if (top.getColor() == Color.YELLOW && bottom.getColor() == Color.RED) {

		} else if (top.getColor() == Color.RED
				&& bottom.getColor() == Color.GREEN) {

		} else if (top.getColor() == Color.GREEN
				&& bottom.getColor() == Color.BLUE) {

		} else if (top.getColor() == Color.GREEN
				&& bottom.getColor() == Color.ORANGE) {

		} else if (top.getColor() == Color.BLUE
				&& bottom.getColor() == Color.RED) {

		} else {
			return false;// if the color pair is not one of the above, it is NOT
							// a fidcuial
		}
		return true;
	}

	/**
	 * Classifies pixels by color. Sorted into masks
	 * 
	 * @param src
	 * @param mask
	 */
	protected void blobPixel(Image src, int[][] mask) {
		for (int i = 0; i < targetHueLevels.length; i++) { // loop through every
															// color
			int maskIndex = 0;
			for (int y = 0; y < height; y++) { // (Solution)
				for (int x = 0; x < width; x++) { // (Solution)
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
					float[] hsb = Color.RGBtoHSB(red, green, blue, null);
					double hue = targetHueLevels[i];
					double hue_Threshold = hueThresholds[i];

					// Using HSB thresholds, set pixels
					if (hsb[2] > multiBrightnessLevel[i]
							&& hsb[1] > multiSaturationLevel[i]
							&& Math.abs(hsb[0] - hue) < hue_Threshold) {
						mask[i][maskIndex++] = 255; // (Solution)
						// blob[maskIndex++] = 255;
					} else if (i == 0
							&& hsb[2] > other_brightness
							&& hsb[1] > other_saturation
							&& Math.abs(hsb[0] - (360.0 / 360)) < other_hueThreshold) {
						mask[i][maskIndex++] = 255; // (Solution)
						// blob[maskIndex++] = 255;
					} else {
						mask[i][maskIndex++] = 0;
						// blob[maskIndex++] = 0;
					}
				}
			}
		}
	}

	protected void multiBlobPresent(float[] depth_img, int[][] threshIm,
			int[][] connIm, int[][] m_blobIm) {
		for (int i = 0; i < targetHueLevels.length; i++) {
			ConnectedComponents connComp = new ConnectedComponents();
			connComp.doLabel(threshIm[i], connIm[i], width, height);

			HashMap<Integer, Integer> blob_info = connComp.getBlobPixel(0.005
					* height * width);

			double centroidX;
			double centroidY;
			System.out.println(blob_info.entrySet() + " " + colorwheel[i]
					+ " found");// use this to debug 4/29/15
			for (Map.Entry<Integer, Integer> entry : blob_info.entrySet()) {

				int colorMax = entry.getKey();
				int countMax = entry.getValue();

				int sx = 0;
				int sy = 0;

				if (countMax > blobSizeThreshold * height * width) {
					int destIndex = 0;
					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							if (connIm[i][destIndex] == colorMax) {
								sx += x;
								sy += y;
								m_blobIm[i][destIndex++] = 255;
							} else {
								m_blobIm[i][destIndex++] = 0;
							}
						}
					}
					centroidX = sx / (double) countMax;
					centroidY = sy / (double) countMax;

					int ind = (int) (centroidY * width + centroidX);
					int distToCentroid;
					if (ind < depth_img.length) {
						distToCentroid = (int) depth_img[(int) (centroidY
								* width + centroidX)];
					} else {
						distToCentroid = 0;
					}
					BlobObject bo = new BlobObject(centroidX, centroidY,
							distToCentroid, countMax, colorwheel[i],
							m_blobIm[i]);

					bos.add(bo);
				}
			}
		}
		System.out.println(bos.size() + " blobs detected."); // use this to
																// debug 4/29
	}

	/**
	 * Determines the centroid of a blob. Currently runs on one blob/color
	 * 
	 * @param m_threshIm
	 * @param m_connIm
	 * @param m_blobIm
	 */
	protected void blobPresent(int[][] m_threshIm, int[][] m_connIm,
			int[][] m_blobIm) {

		double centroidX;
		double centroidY;
		for (int i = 0; i < m_threshIm.length; i++) { // iterate through
			int sx = 0;
			int sy = 0;
			int[] threshIm = m_threshIm[i];
			int[] connIm = m_connIm[i];
			int[] blobIm = m_blobIm[i];

			// int[] blobArr = new int[width * height];

			// FIND MULTILPLE BLOBS OF SAME COLOR
			ConnectedComponents connComp = new ConnectedComponents(); // (Solution)
			connComp.doLabel(threshIm, connIm, width, height); // (Solution)
			int colorMax = connComp.getColorMax(); // (Solution)
			int countMax = connComp.getCountMax(); // (Solution)
			// connComp.getBlobColor();
			// connComp.getBlobPixelCount();

			if (countMax > blobSizeThreshold * height * width) { // (Solution)
				targetArea[i] = countMax; // (Solution)
				int destIndex = 0; // (Solution)
				for (int y = 0; y < height; y++) { // (Solution)
					for (int x = 0; x < width; x++) { // (Solution)
						if (connIm[destIndex] == colorMax) { // (Solution)
							sx += x; // (Solution)
							sy += y; // (Solution)
							// blobArr[destIndex++] = 255;
							m_blobIm[i][destIndex++] = 255; // (Solution)
						} else { // (Solution)
							// blobArr[destIndex++] = 0;
							m_blobIm[i][destIndex++] = 0; // (Solution)
						}
					}
				}
				centroidX = sx / (double) countMax;
				centroidY = sy / (double) countMax;

				// BlobObject bo = new BlobObject(centroidX, centroidY,
				// countMax,
				// blobArr);
				// bos.add(bo);
			}
		} // End of blob present
	}

	/**
	 * Detects circles in blobs. Useful for fiducial tracking
	 * 
	 * @param blob
	 * @param threshold
	 * @param dest
	 */
	// DEPRECATED 4/28/15//
	public boolean detectCircle(BlobObject blob, double threshold, Image dest) {
		double rad_d = Math.sqrt((blob.getTargetArea() / Math.PI));
		int x = (int) blob.getCentroidX();
		int y = (int) blob.getCentroidY();
		int[] mask = blob.getBlobArr();
		int circle_counter = 0;
		// int z = y * this.width + x;
		double[] angles = { 0, Math.PI / 12.0, Math.PI / 10.0, Math.PI / 8.0,
				Math.PI / 7.0, Math.PI / 6.0, Math.PI / 5.5, Math.PI / 5.0,
				Math.PI / 4.5, Math.PI / 4.0, Math.PI / 3.5, Math.PI / 3.0,
				Math.PI / 2.5, Math.PI / 2.0 };
		for (double ang : angles) {
			int x_angle = (int) (rad_d * Math.cos(ang));
			int y_angle = (int) (rad_d * Math.sin(ang));

			if (x + x_angle < this.width && x - x_angle > 0
					&& y + y_angle < this.height && y - y_angle > 0) {
				if (mask[(x + x_angle) + ((y + y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (mask[(x - x_angle) + ((y - y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (mask[(x - x_angle) + ((y + y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (mask[(x + x_angle) + ((y - y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				dest.setPixel(x + x_angle, y + y_angle, (byte) 0xff,
						(byte) 0xff, (byte) 0xff);
				dest.setPixel(x - x_angle, y - y_angle, (byte) 0xff,
						(byte) 0xff, (byte) 0xff);
				dest.setPixel(x - x_angle, y + y_angle, (byte) 0xff,
						(byte) 0xff, (byte) 0xff);
				dest.setPixel(x + x_angle, y - y_angle, (byte) 0xff,
						(byte) 0xff, (byte) 0xff);

			}
		}
		System.out.println("How well "
				+ (circle_counter / ((double) angles.length * 4)));
		return (circle_counter / ((double) angles.length * 4)) > threshold;
	}

	/**
	 * 
	 * @param blob
	 * @param threshold
	 * @return
	 */
	// DEPRECATED 4/28/15//
	public boolean detectCircle(BlobObject blob, double threshold) {
		double rad_d = Math.sqrt((blob.getTargetArea() / Math.PI));
		int x = (int) blob.getCentroidX();
		int y = (int) blob.getCentroidY();
		int[] mask = blob.getBlobArr();
		int circle_counter = 0;
		// int z = y * this.width + x;
		double[] angles = { 0, Math.PI / 12.0, Math.PI / 10.0, Math.PI / 8.0,
				Math.PI / 7.0, Math.PI / 6.0, Math.PI / 5.5, Math.PI / 5.0,
				Math.PI / 4.5, Math.PI / 4.0, Math.PI / 3.5, Math.PI / 3.0,
				Math.PI / 2.5, Math.PI / 2.0 };
		for (double ang : angles) {
			int x_angle = (int) (rad_d * Math.cos(ang));
			int y_angle = (int) (rad_d * Math.sin(ang));

			if (x + x_angle < this.width && x - x_angle > 0
					&& y + y_angle < this.height && y - y_angle > 0) {
				if (mask[(x + x_angle) + ((y + y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (mask[(x - x_angle) + ((y - y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (mask[(x - x_angle) + ((y + y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (mask[(x + x_angle) + ((y - y_angle) * this.width)] > 0) {
					circle_counter++;
				}
			}
		}
		System.out.println("How well "
				+ (circle_counter / ((double) angles.length * 4)));
		return (circle_counter / ((double) angles.length * 4)) > threshold;
	}

	/**
	 * Deprecated findFiducial Method to help locate fiducials
	 * 
	 * @param multiBlobMask
	 * @param dest
	 */
	// DEPRECATED 4/28/15//
	public void findFiducial(int[][] multiBlobMask, Image dest) {
		// for (int i = 0; i < multiBlobMask.length; i++){
		double rad_d = Math.sqrt((targetArea[0] / Math.PI));
		int x = (int) bos.get(0).getCentroidX();
		int y = (int) bos.get(0).getCentroidY();
		int circle_counter = 0;
		int z = y * this.width + x;
		double[] angles = { 0, Math.PI / 12, Math.PI / 8, Math.PI / 6,
				Math.PI / 5, Math.PI / 4, Math.PI / 3, Math.PI / 2.5,
				Math.PI / 2 };
		for (double ang : angles) {
			int x_angle = (int) (rad_d * Math.cos(ang));
			int y_angle = (int) (rad_d * Math.sin(ang));

			if (x + x_angle < this.width && x - x_angle > 0
					&& y + y_angle < this.height && y - y_angle > 0) {
				if (multiBlobMask[0][(x + x_angle)
						+ ((y + y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (multiBlobMask[0][(x - x_angle)
						+ ((y - y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (multiBlobMask[0][(x - x_angle)
						+ ((y + y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				if (multiBlobMask[0][(x + x_angle)
						+ ((y - y_angle) * this.width)] > 0) {
					circle_counter++;
				}
				dest.setPixel(x + x_angle, y + y_angle, (byte) 0xff,
						(byte) 0xff, (byte) 0xff);
				dest.setPixel(x - x_angle, y - y_angle, (byte) 0xff,
						(byte) 0xff, (byte) 0xff);
				dest.setPixel(x - x_angle, y + y_angle, (byte) 0xff,
						(byte) 0xff, (byte) 0xff);
				dest.setPixel(x + x_angle, y - y_angle, (byte) 0xff,
						(byte) 0xff, (byte) 0xff);
			}
		}
		// }
		// System.out.println(circle_counter / (double) (4 * angles.length));
	}

	/**
	 * 
	 */
	// DEPRECATED 4/28/15//
	public void sortBlobs(Image dest) {
		boolean isTopFiducial;
		for (int j = 0; j < bos.size(); j++) {
			BlobObject top = bos.get(j);
			isTopFiducial = false;
			for (int k = 0; k < bos.size(); k++) {
				BlobObject bottom = bos.get(k);
				if (top != bottom && isFiducialColorMatch(top, bottom)
						&& isAbove(top, bottom, .1, .1)) {
					FiducialObject fo = new FiducialObject(top, bottom);
					fos.add(fo);
					isTopFiducial = true;
				}
			}
			if (detectCircle(top, .5, dest)) {
				System.out.println(" The Circle is " + top.getColor());
				// it is a blob but not a block
			} else if (!isTopFiducial) {
				BlockObject blo = new BlockObject(top);
				blos.add(blo);
			}
		}
	}

	/**
	 * sortBlobs Iterates through blob objects to sort into fiducial and block
	 */
	public void sortBlobs() {
		boolean isTopFiducial;
		for (int j = 0; j < bos.size(); j++) {
			BlobObject top = bos.get(j);
			isTopFiducial = false;
			for (int k = 0; k < bos.size(); k++) {
				BlobObject bottom = bos.get(k);
				if (top != bottom && isFiducialColorMatch(top, bottom)
						&& isAbove(top, bottom, 0.1, 0.1)) {
					FiducialObject fo = new FiducialObject(top, bottom);
					fos.add(fo);
					isTopFiducial = true;
				}
			}
			if (!isTopFiducial) {
				BlockObject blo = new BlockObject(top);
				blos.add(blo);
			}
		}
	}

	private boolean isAbove(BlobObject top, BlobObject bottom,
			double thresholdX, double thresholdY) {

		return top.getCentroidY() < bottom.getCentroidY()
				&& Math.abs(bottom.getCentroidY() - top.getCentroidY()
						- (top.getRadius() + bottom.getRadius())) < thresholdY
				&& (Math.abs(top.getCentroidX() - bottom.getCentroidX()) < thresholdX);
	}

	/**
	 * Checks if visual servo is done
	 * 
	 * @return bool
	 */
	public boolean isDone() {
		// if there are 0 blocks then it's Done
		return blos.size() == 0 || bos.size() == 0;
	}

	/**
	 * Use this apply for Challenge
	 * 
	 * @param src
	 */
	public void apply(Image src, float[] array) {
		stepTiming();

		if (useGaussianBlur) {
			byte[] srcArray = src.toArray();
			byte[] destArray = new byte[srcArray.length];
			if (approximateGaussian) {
				GaussianBlur.applyBox(srcArray, destArray, src.getWidth(),
						src.getHeight());
			} else {
				GaussianBlur.apply(srcArray, destArray, width, height);
			}
			src = new Image(destArray, src.getWidth(), src.getHeight());
		}

		blobPixel(src, multiBlobPixelMask);
		multiBlobPresent(array, multiBlobPixelMask, multiImageConnected,
				multiBlobMask);

		// sorts blobs into fiducials and blocks
		// sortBlobs();

		if (!isDone()) {
			blobFix();
			computeTranslationVelocityCommand();
			computeRotationVelocityCommand();
		} else {
			translationVelocityCommand = 0.0;
			rotationVelocityCommand = 0.0;
		}
	}

	/**
	 * Use this apply when wanting to print RGB to screen
	 */
	public void apply(Image src, Image dest) {
		stepTiming();

		if (useGaussianBlur) {// (Solution)
			byte[] srcArray = src.toArray();// (Solution)
			byte[] destArray = new byte[srcArray.length]; // (Solution)
			if (approximateGaussian) { // (Solution)
				GaussianBlur.applyBox(srcArray, destArray, src.getWidth(),
						src.getHeight());
			} // (Solution)
			else { // (Solution)
				GaussianBlur.apply(srcArray, destArray, width, height); // (Solution)
			} // (Solution)
			src = new Image(destArray, src.getWidth(), src.getHeight()); // (Solution)
		}

		blobPixel(src, multiBlobPixelMask); // (Solution)
		blobPresent(multiBlobPixelMask, multiImageConnected, multiBlobMask); // (Solution)

		sortBlobs();

		if (dest != null) { // (Solution)
			// dest = Histogram.getHistogram(src, dest, true); // (Solution)
			markBlob(src, dest); // (Solution)
		} // (Solution)
			// // findFiducial(multiBlobMask, dest);
			//
			// for (int i = 0; i < multiTargetDetected.length; i++) {
			// int x = (int) bos.get(0).getCentroidX();
			// int y = (int) bos.get(0).getCentroidY();
			// if (x > 10 && y > 10) {
			// for (int j = 0; j < 10; j++) {
			// for (int k = 0; k < 10; k++) {
			// dest.setPixel(x + j, y + k, (byte) 0, (byte) 0,
			// (byte) 0);
			// }
			// }
			// }
			// }

		/* Pick one blob to go to, find it's index and fix on it */
		/*
		 * if (targetArea[0] > 0) { // (Solution) int index = 0;// find's red
		 * block because 0 is the first index blobFix(index);
		 * computeTranslationVelocityCommand(); // (Solution)
		 * computeRotationVelocityCommand(); // (Solution) } else { //
		 * (Solution) translationVelocityCommand = 0.0; // (Solution)
		 * rotationVelocityCommand = 0.0; // (Solution) } // / (Solution)
		 */
	}

	/**
	 * Use this apply when wanting to print Depth Data to screen
	 * 
	 * @param src
	 * @param dest
	 * @param float_array
	 */
	public void apply(Image src, Image dest, float[] float_array) {
		stepTiming();
		bos = new ArrayList<BlobObject>();
		fos = new ArrayList<FiducialObject>();
		blos = new ArrayList<BlockObject>();

		if (useGaussianBlur) {// (Solution)
			byte[] srcArray = src.toArray();// (Solution)
			byte[] destArray = new byte[srcArray.length]; // (Solution)
			if (approximateGaussian) { // (Solution)
				GaussianBlur.applyBox(srcArray, destArray, src.getWidth(),
						src.getHeight());
			} else {
				GaussianBlur.apply(srcArray, destArray, width, height); // (Solution)
			}
			src = new Image(destArray, src.getWidth(), src.getHeight()); // (Solution)
		}

		blobPixel(src, multiBlobPixelMask);
		multiBlobPresent(float_array, multiBlobPixelMask, multiImageConnected,
				multiBlobMask);
		//System.out.println("Number of blobs " + bos.size());
		//System.out.println("Number of fiducials " + fos.size());
		//System.out.println("Number of blocks " + blos.size());

		if (dest != null) { // (Solution)
			// dest = Histogram.getHistogram(src, dest, true); // (Solution)
			markBlob(src, dest);
			for (BlobObject bo : bos) {
				int x = (int) bo.getCentroidX();
				int y = (int) bo.getCentroidY();
				if (x > 10 && y > 10) {
					for (int j = 0; j < 10; j++) {
						for (int k = 0; k < 10; k++) {
							dest.setPixel(x + j, y + k, (byte) 0, (byte) 0,
									(byte) 0);
						}
					}
				}
			}

		}
		//sortBlobs();

		if (!isDone()) {
			blobFix();
			computeTranslationVelocityCommand();
			computeRotationVelocityCommand();
		} else {
			translationVelocityCommand = 0.0;
			rotationVelocityCommand = 0.0;
		}

	}

}
