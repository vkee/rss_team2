package VisualServo;

/**
 * @author Edsinger, from http://in3www.epfl.ch/~jpilet/serie6/GaussianBlur.java
 * 
 * Apply a gaussian filter of size 5x5, with sigma=1.
 * <br>
 * The 2D filter:
 * <pre>
 *         [  0.7  3.3  5.5  3.3  0.7 ]
 *         [  3.3 15.0 24.7 15.0  3.3 ]
 * 1/256 * [  5.5 24.7 40.7 24.7  5.5 ]
 *         [  3.3 15.0 24.7 15.0  3.3 ]
 *         [  0.7  3.3  5.5  3.3  0.7 ]
 * </pre>
 * <br>
 * can be separated into 2 1D filter:
 * 
 * <pre>
 * 1/16 * [ 0.9  3.9  6.4  3.9  0.9 ]
 * </pre>
 * <br>
 * the  following approximation is acceptable:
 * <pre>
 * 1/16 * [ 1 4 6 4 1 ]
 * </pre>
 */
public class GaussianBlur {
	/**
	 * Apply a 5x5 gaussian blur of sigma = 1.
	 * Both images must have the same size, defined by the width and height.
	 * 
	 * @param srcpix The source image.
	 * @param dstpix The destination where the result is stored.
	 * @param w width
	 * @param h height
	 */
	public static void apply(byte[] srcpix, byte[] dstpix, int w, int h) {
		if (srcpix.length != dstpix.length) {
			throw new IllegalArgumentException("Source and destination must have the same size!");
		}
		if (srcpix.length != w*h*3) {
			throw new IllegalArgumentException("Size of image does not match width and height!");
		}

		byte[] tmppix = new byte[w*h*3];

		// horizontal filtering
		for (int y=0; y < h; y++) {
			for(int x=0; x < w; x++) {
				int pos = (y*w + x)*3;
				for (int c=0; c<3; c++) {
					int acc = 0;
					for (int i=-2;i<=2;i++) {
						int xi = ((x+i)%w + w) % w;
						int mult;
						if (i == -2) mult = 1;
						else if (i == -1) mult = 4;
						else if (i == 0) mult = 6;
						else if (i == 1) mult = 4;
						else mult = 1;
						// [1 4 6 4 1] filter
						acc += (srcpix[(y*w + xi)*3 + c]&0xff)*mult;
					}
					tmppix[pos + c] = (byte) (acc/16);
				}
			}
		}

		// vertical filtering
		for (int y=0; y < h; y++) {
			for(int x=0; x < w; x++) {
				int pos = (y*w + x)*3;
				for (int c=0; c<3; c++) {
					int acc = 0;
					for (int i=-2;i<=2;i++) {
						int yi = ((y+i)%h + h) % h;
						int mult;
						if (i == -2) mult = 1;
						else if (i == -1) mult = 4;
						else if (i == 0) mult = 6;
						else if (i == 1) mult = 4;
						else mult = 1;

						// [1 4 6 4 1] filter
						acc += (tmppix[(yi*w + x)*3 + c]&0xff)*mult;
					}
					dstpix[pos + c] = (byte) (acc/16);
				}
			}
		}
	}

	private static void applyBoxHelper(byte[] srcpix, byte[] dstpix, int w, int h) {
		byte[] tmppix = new byte[w*h*3];

		//horizontal filtering
		for (int y=0;y<h;y++) {
			for (int c=0;c<3;c++) {
				int sum = (srcpix[y*w*3 + c]&0xff) +
				(srcpix[(y*w + 1)*3 + c]&0xff) +
				(srcpix[(y*w + 2)*3 + c]&0xff) +
				(srcpix[(y*w + w - 2)*3 + c]&0xff) +
				(srcpix[(y*w + w - 2)*3 + c]&0xff);
				for (int x=0;x<w;x++) {
					tmppix[(y*w + x)*3 + c] = (byte) (sum/5);
					sum -= srcpix[(y*w + (x+w-2)%w)*3 + c]&0xff;
					sum += srcpix[(y*w + (x+3)%w)*3 + c]&0xff;
				}
			}
		}

		//vertical filtering
		for (int x=0;x<w;x++) {
			for (int c=0;c<3;c++) {
				int sum = (tmppix[x*3 + c]&0xff) +
				(tmppix[(w + x)*3 + c]&0xff) +
				(tmppix[(2*w + x)*3 + c]&0xff) +
				(tmppix[((h-2)*w + x)*3 + c]&0xff) +
				(tmppix[((h-1)*w + x)*3 + c]&0xff);
				for (int y=0;y<h;y++) {
					dstpix[(y*w + x)*3 + c] = (byte) (sum/5);
					sum -= tmppix[(((y+h-2)%h)*w + x)*3 + c]&0xff;
					sum += tmppix[(((y+3)%h)*w + x)*3 + c]&0xff;
				}
			}
		}
	}

	public static void applyBox(byte[] srcpix, byte[] dstpix, int w, int h) {
		if (srcpix.length != dstpix.length) {
			throw new IllegalArgumentException("Source and destination must have the same size!");
		}
		if (srcpix.length != w*h*3) {
			throw new IllegalArgumentException("Size of image does not match width and height!");
		}

		// 3 iterations approximates gaussian blur to within 3%
		// See http://en.wikipedia.org/wiki/Box_blur
		for (int i=0; i<3; i++) {
			applyBoxHelper(srcpix, dstpix, w, h);
		}
	}
}
