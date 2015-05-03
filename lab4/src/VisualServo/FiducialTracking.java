package VisualServo;

import java.awt.Color;
import java.util.*;

public class FiducialTracking extends BlobTracking {

	private Color[] colorwheel = { Color.RED, Color.GREEN, Color.BLUE,
			Color.YELLOW, Color.ORANGE };

	private double[] targetHueLevels = { 0.0, 120.0 / 360, 240.0 / 360,
			28.0 / 360, 24.0 / 360 };
	// red, green, blue,yellow,orange

	private double[] hueThresholds = { 0.05, 0.1, 0.15, 0.05, 0.05 };
	private double other_hueThreshold = 0.05;

	protected double[] multiSaturationLevel = { 0.6, 0.3, 0.3, 0.8, 0.8 };
	double other_saturation = 0.6;

	double[] multiBrightnessLevel = { 0.55, 0.0, 0.0, 0.5, 0.1 };
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
			System.out.println("Center Hue: " + hsb_[0]);
			System.out.println("Center Saturation: " + hsb_[1]);
			System.out.println("Center Brightness: " + hsb_[2]);

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
			for (Map.Entry<Integer, Integer> entry : blob_info.entrySet()) {

				int colorMax = entry.getKey();
				int countMax = entry.getValue();
				int sx = 0;
				int sy = 0;

				// if (countMax > blobSizeThreshold * height * width) {
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
	}
}
