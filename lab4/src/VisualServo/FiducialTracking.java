package VisualServo;

import java.awt.Color;
import java.util.*;

public class FiducialTracking extends BlobTracking {

	private Color[] colorwheel = { Color.RED, Color.GREEN, Color.BLUE,
			Color.YELLOW, Color.ORANGE };

	private double[] targetHueLevels = { 0.0, 120.0 / 360, 240.0 / 360,
			60.0 / 360, 24.0 / 360 };
	// red, green, blue,yellow,orange

	private double[] hueLowThresholds = { 0.0, 75.0 / 360, 168.0 / 360,
			28.0 / 360, 15.0 / 360 };

	private double[] hueHighThresholds = { 9.0 / 360, 162.0 / 360, 245.0 / 360,
			48.0 / 360, 24.0 / 360 };

	private double[] hueThresholds = { 0.05, 0.1, 0.15, 0.1, 0.1 };
	private double other_hueThreshold = 0.05;

	protected double[] multiSaturationLevel = { 0.6, 0.3, 0.3, 0.5, 0.5 };
	double other_saturation = 0.6;

	protected double[] multiSaturationUpper = { 1, 1, 1, .9, 1 };
	double other_upper = 1;

	double[] multiBrightnessLevel = { 0.45, 0.0, 0.0, 0.6, 0.3 };
	double other_brightness = 0.45;

	int[][] multiBlobPixelMask = null;
	int[][] multiBlobMask = null;
	int[][] multiImageConnected = null;
	int[] blobPixelMask = null;

	int[] targetArea = new int[targetHueLevels.length];
	boolean[] multiTargetDetected = { false, false, false, false, false };

	List<BlobObject> bos;
	List<FiducialObject> fos;
	List<BlockObject> blos;

	public FiducialTracking() {
		super(640, 480);
		blobPixelMask = new int[width * height];
		multiBlobPixelMask = new int[targetHueLevels.length][width * height];
		multiBlobMask = new int[targetHueLevels.length][width * height];
		multiImageConnected = new int[targetHueLevels.length][width * height];
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
						}
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
	protected int isFiducialColorMatch(BlobObject top, BlobObject bottom) {
		// top & bottom
		// yellow & red
		// red & green
		// green & blue
		// blue & yellow
		// green & orange
		// blue & red
		if (top.getColor() == Color.RED && bottom.getColor() == Color.BLUE) {
			return 2;
		} else if (top.getColor() == Color.RED
				&& bottom.getColor() == Color.ORANGE) {
			return 1;
		} else if (top.getColor() == Color.GREEN
				&& bottom.getColor() == Color.BLUE) {
			return 0;

		} else if (top.getColor() == Color.YELLOW
				&& bottom.getColor() == Color.ORANGE) {
			return 3;

		} else if (top.getColor() == Color.YELLOW
				&& bottom.getColor() == Color.GREEN) {
			return 4;
		} else if (top.getColor() == Color.RED
				&& bottom.getColor() == Color.GREEN) {
			return 5;

		} else if (top.getColor() == Color.YELLOW
				&& bottom.getColor() == Color.BLUE) {
			return 6;

		} else {
			return -1;
		}
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
			int pix_ = src.getPixel(height / 2, width / 2);
			int red_ = Image.pixelRed(pix_);
			int blue_ = Image.pixelBlue(pix_);
			int green_ = Image.pixelGreen(pix_);
			if (red_ < 0)
				red_ += 255;
			if (blue_ < 0)
				blue_ += 255;
			if (green_ < 0)
				green_ += 255;

			float[] hsb_ = Color.RGBtoHSB(red_, green_, blue_, null);
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

					if (y == height / 2 && x == width / 2) {
//						System.out.println("0: " + hsb[0] + " 1: " + hsb[1]
	//							+ " 2: " + hsb[2]);
					}
					// Using HSB thresholds, set pixels

					if (hsb[2] > multiBrightnessLevel[i]
							&& hsb[1] > multiSaturationLevel[i]
							&& hsb[1] < multiSaturationUpper[i]
							&& hsb[0] > hueLowThresholds[i]
							&& hsb[0] < hueHighThresholds[i]) {
						mask[i][maskIndex] = 255; // (Solution)
					} else if (i == 0
							&& hsb[2] > other_brightness
							&& hsb[1] > other_saturation
							&& Math.abs(hsb[0] - (360.0 / 360)) < other_hueThreshold) {
						mask[i][maskIndex] = 255;
					} else {
						mask[i][maskIndex] = 0;
					}
					/*					
										if (hsb[2] > multiBrightnessLevel[i]
												&& hsb[1] > multiSaturationLevel[i]
												&& hsb[1] < multiSaturationUpper[i]
												&& Math.abs(hsb[0] - hue) < hue_Threshold) {
											mask[i][maskIndex] = 255; // (Solution)
										} else if (i == 0
												&& hsb[2] > other_brightness
												&& hsb[1] > other_saturation
												&& Math.abs(hsb[0] - (360.0 / 360)) < other_hueThreshold) {
											mask[i][maskIndex] = 255;
										} else {
											mask[i][maskIndex] = 0;
										}
										
										*/

					/*//ORANGE HACK --RED & YELLOW--
					if (i == 4) {
						if (mask[0][maskIndex] == 255
								&& mask[3][maskIndex] == 255) {
							mask[0][maskIndex] = 0;
							mask[3][maskIndex] = 0;
						} else if (mask[4][maskIndex] == 255
								&& mask[3][maskIndex] == 255) {
							mask[3][maskIndex] = 0;
						}
					}*/
					maskIndex++;
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
			double centroidX = 0;
			double centroidY = 0;
			// System.out.println(blob_info.entrySet().size());
			for (Map.Entry<Integer, Integer> entry : blob_info.entrySet()) {

				int colorMax = entry.getKey();
				int countMax = entry.getValue();
				int sx = 0;
				int sy = 0;

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
				float fT = 42.775668509f;

				if (ind < depth_img.length) {
					int distToCentroid = (int) (depth_img[ind]);
					if (distToCentroid > 0) {
						BlobObject bo = new BlobObject(centroidX, centroidY,
								(fT / distToCentroid), countMax, colorwheel[i],
								m_blobIm[i]);
						bos.add(bo);
					}
				}
			}
		}
	}

	public List<FiducialObject> sortBlobs(List<BlobObject> bos) {
		List<FiducialObject> ret = new ArrayList<FiducialObject>();
		for (int j = 0; j < bos.size(); j++) {
			BlobObject top = bos.get(j);
			for (int k = 0; k < bos.size(); k++) {
				BlobObject bottom = bos.get(k);
				if (!top.equals(bottom)
						&& isFiducialColorMatch(top, bottom) != -1
						&& isAbove(top, bottom, 12, 5)) {
					FiducialObject fo = new FiducialObject(top, bottom,
							isFiducialColorMatch(top, bottom));
					ret.add(fo);
				}
			}
		}
		return ret;

	}

	/**
	 * sortBlobs Iterates through blob objects to sort into fiducial and block
	 */

	public void sortBlobs() {
		boolean isTopFiducial;
		// System.out.println("-- " + bos.size() + "--");
		for (int j = 0; j < bos.size(); j++) {
			BlobObject top = bos.get(j);
			isTopFiducial = false;
			for (int k = 0; k < bos.size(); k++) {
				BlobObject bottom = bos.get(k);
				/*
				if (!top.equals(bottom)) {
					if (!isAbove(top, bottom, 5, 5)) {
						System.out.print("X: ");
						System.out.println(top.getCentroidX()
								- bottom.getCentroidX());
						System.out.print("Y: ");

						System.out.println(Math.abs(bottom.getCentroidY()
								- top.getCentroidY()
								- (top.getRadius() + bottom.getRadius())));
					}
					System.out.println("isAbove "
							+ isAbove(top, bottom, 5, 5));
				}*/
				if (!top.equals(bottom)
						&& isFiducialColorMatch(top, bottom) != -1
						&& isAbove(top, bottom, 12, 5)) {
					FiducialObject fo = new FiducialObject(top, bottom,
							isFiducialColorMatch(top, bottom));
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

		if (top.getCentroidY() < bottom.getCentroidY()
				&& Math.abs(bottom.getCentroidY() - top.getCentroidY()
						- (top.getRadius() + bottom.getRadius())) < thresholdY
				&& (Math.abs(top.getCentroidX() - bottom.getCentroidX()) < thresholdX)) {
			return true;
		} else {
			// System.out.println("Y: " + Math.abs(bottom.getCentroidY() -
			// top.getCentroidY()
			// - (top.getRadius() + bottom.getRadius())));
			// System.out.println("X: " + Math.abs(top.getCentroidX() -
			// bottom.getCentroidX()));
			return false;
		}
	}

	/**
	 * finds fiducials present in an image
	 * 
	 * @param src
	 */
	public List<FiducialObject> getFiducials(Image src, float[] array) {
		stepTiming();
		bos = new ArrayList<BlobObject>();
		fos = new ArrayList<FiducialObject>();
		blos = new ArrayList<BlockObject>();

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
		sortBlobs();

		return fos;
	}

	public List<BlobObject> getBlobs(Image src, float[] array) {
		stepTiming();
		bos = new ArrayList<BlobObject>();
		fos = new ArrayList<FiducialObject>();
		blos = new ArrayList<BlockObject>();

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
		sortBlobs();

		return bos;
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

			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < 10; k++) {
					dest.setPixel(width / 2 + j, height / 2 + k, (byte) 0,
							(byte) 0, (byte) 0);
				}
			}

		}
		sortBlobs();
		if (fos.size() > 0) {
			System.out.println("Number of Fiducials " + fos.size());
			for (FiducialObject fo : fos) {
				System.out.println("Fiducial Number " + fo.getFiducialNumber()
						+ " at " + fo.getDistanceTo() + "m away.");
			}
		}
	}
}
