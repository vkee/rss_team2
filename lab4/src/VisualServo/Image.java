package VisualServo;

/**
 * An Image object is a easy-to-use representation of the pixelated image data
 * from Carmen
 * 
 * @author Vinayak Ranade 
 *
 */
public class Image {


	public static void fillImageMsg(org.ros.message.sensor_msgs.Image pubImage,
			Image dest) {
		pubImage.width = 640;
		pubImage.height = 480;
		pubImage.encoding = "rgb8";
		pubImage.is_bigendian = 0;
		pubImage.step = 640 * 3;
		pubImage.data = dest.toArray();
	}

	public static float[] convertDistanceArray(float[] inDepthArray) {
		float fT = 42.775668509f;
		float[] out = new float[inDepthArray.length];
		for (int i = 0; i < inDepthArray.length; i++) {
			if (inDepthArray[i] > 0) {
				out[i] = fT / inDepthArray[i];
			} else {
				out[i] = 0;
			}
		}
		return out;
	}

	public static void filterImage(Image inColor, Image outColor,
			float[] _inDepthArray, int width, int height, double field_width,
			double field_height, double locX, double locY, double direction,
			double buffer) {
		// direction is a degree measurement from 0-360? or is in radians, yet
		// to decide
		int index = 0;
		double threshold = 2.25;
		float[] inDepthArray = convertDistanceArray(_inDepthArray);
		// threshold = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				
				if (index < inDepthArray.length && inDepthArray[index] > threshold) {
					// System.out.println(inDepthArray[index]);
					outColor.setPixel(j, i, (byte) 0, (byte) 0, (byte) 0);
				}
				index++;
			}
		}
	}

	public static Image floatRGB(float[] inFloat) {
		Image ret = new Image(640, 480);// width = 640, height = 480
		// System.out.println(inFloat.length + " " + inFloat.length/640.0);
		int r = 0;
		int g = 0;
		int b = 0;
		// float max = 0;
		double ratio;
		int value;
		for (int i = 0; i < inFloat.length; i++) {
			ratio = inFloat[i] / 100.0;
			value = (int) (ratio * 255);
			if (value > 255) {
				value = 255;
			}
			if (ratio > 0.75) {
				r = value;
				g = 0;
				b = 0;
			} else if (ratio > 0.5) {
				r = 0;
				g = value;
				b = 0;
			} else if (ratio > .25) {
				r = 0;
				g = 0;
				b = value;
			} else {
				r = value;
				g = value;
				b = value;
			}

			ret.setPixel((int) (i % 640.0), (int) (i / 640.0), (byte) r,
					(byte) g, (byte) b);
		}
		// System.out.println(max);
		return ret;
	}

	/**
	 * Represents a color channel, for use with packed ARGB pixels and byte
	 * arrays.
	 */
	public static enum Channel {
		RED(16, 0), GREEN(8, 1), BLUE(0, 2);

		/**
		 * The amount that this channel is bit-shifted by.
		 */
		public final int shift;
		/**
		 * The order in which this channel comes in an array.
		 */
		public final int offset;

		private Channel(int shift, int offset) {
			this.shift = shift;
			this.offset = offset;
		}
	}

	/**
	 * Extract a channel from a packed pixel.
	 * 
	 * @param p
	 *            The pixel.
	 * @param channel
	 *            The channel.
	 * @return A byte containing the intensity of the requested channel.
	 */
	public static byte pixelChannel(int p, Channel channel) {
		return (byte) ((p >>> channel.shift) & 0xff);
	}

	/**
	 * Extracts the red channel from a packed pixel.
	 * 
	 * @param p
	 *            The pixel.
	 * @return A byte containing the amount of red in the pixel.
	 */
	public static byte pixelRed(int p) {
		return pixelChannel(p, Channel.RED);
	}

	/**
	 * Extracts the green channel from a packed pixel.
	 * 
	 * @param p
	 *            The pixel.
	 * @return A byte containing the amount of green in the pixel.
	 */
	public static byte pixelGreen(int p) {
		return pixelChannel(p, Channel.GREEN);
	}

	/**
	 * Extracts the blue channel from a packed pixel.
	 * 
	 * @param p
	 *            The pixel.
	 * @return A byte containing the amount of blue in the pixel.
	 */
	public static byte pixelBlue(int p) {
		return pixelChannel(p, Channel.BLUE);
	}

	/**
	 * Make a packed ARGB pixel from the channels.
	 * 
	 * @param r
	 *            The amount of red in the pixel.
	 * @param g
	 *            The amount of green in the pixel.
	 * @param b
	 *            The amount of blue in the pixel.
	 * @return A packed ARGB pixel with the requested colors and full opacity.
	 */
	public static int makePixel(byte r, byte g, byte b) {
		return 0xff000000 | (r & 0xff) << Channel.RED.shift
				| (g & 0xff) << Channel.GREEN.shift
				| (b & 0xff) << Channel.BLUE.shift;
	}

	private static int index(int x, int y, int width) {
		return (y * width + x) * 3;
	}

	// FIELDS
	private int width, height;
	private byte[] pixels;

	/**
	 * @return the width of this image
	 **/
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height of this image
	 **/
	public int getHeight() {
		return height;
	}

	/**
	 * Takes in the source character array (the Carmen compatible form) along
	 * with the width and height of the Image. This information is all available
	 * in any CameraMessage
	 * 
	 * @param src
	 *            the source character array (Carmen compatible form)
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 */
	public Image(byte[] src, int width, int height) {
		if (src.length != width * height * 3) {
			throw new IllegalArgumentException(
					"Length does not match width and height.");
		}
		pixels = src;
		this.width = width;
		this.height = height;
	}

	/**
	 * Makes a default Image with all pixels set to white
	 * 
	 * @param width
	 * @param height
	 */
	public Image(int width, int height) {
		this.pixels = new byte[width * height * 3];
		this.width = width;
		this.height = height;
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = (byte) 0xff;
		}
	}

	/**
	 * Creates a copy of the given image
	 **/
	public Image(Image image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.pixels.clone();
	}

	/**
	 * Gets a pixel from the pixel matrix
	 * 
	 * @param x
	 *            The x coordinate of the pixel (0&lt;=x&lt;width)
	 * @param y
	 *            The y coordinate of the pixel (0&lt;=y&lt;height)
	 * @return An ARGB-packed int containing the information of this pixel.
	 */
	public int getPixel(int x, int y) {
		int index = index(x, y, width);
		return makePixel(pixels[index + Channel.RED.offset], pixels[index
				+ Channel.GREEN.offset], pixels[index + Channel.BLUE.offset]);
	}

	/**
	 * Extract a single channel from a location.
	 * 
	 * @param x
	 *            The x coordinate of the pixel.
	 * @param y
	 *            The y coordinate of the pixel.
	 * @param channel
	 *            The channel.
	 * @return The intensity of the requested channel in the pixel at the
	 *         specified location.
	 */
	public byte getPixelChannel(int x, int y, Channel channel) {
		int index = index(x, y, width);
		return pixels[index + channel.offset];
	}

	/**
	 * Extract the red channel from a location.
	 * 
	 * @param x
	 *            The x coordintate of the pixel.
	 * @param y
	 *            The y coordinate of the pixel.
	 * @return The amount of red in the pixel at the specified location.
	 */
	public byte getPixelRed(int x, int y) {
		return getPixelChannel(x, y, Channel.RED);
	}

	/**
	 * Extract the green channel from a location.
	 * 
	 * @param x
	 *            The x coordintate of the pixel.
	 * @param y
	 *            The y coordinate of the pixel.
	 * @return The amount of green in the pixel at the specified location.
	 */
	public byte getPixelGreen(int x, int y) {
		return getPixelChannel(x, y, Channel.GREEN);
	}

	/**
	 * Extract the blue channel from a location.
	 * 
	 * @param x
	 *            The x coordintate of the pixel.
	 * @param y
	 *            The y coordinate of the pixel.
	 * @return The amount of blue in the pixel at the specified location.
	 */
	public byte getPixelBlue(int x, int y) {
		return getPixelChannel(x, y, Channel.BLUE);
	}

	/**
	 * Set a pixel in the image.
	 * 
	 * @param x
	 *            The x coordinate of the pixel.
	 * @param y
	 *            The y coordinate of the pixel.
	 * @param r
	 *            The amount of red.
	 * @param g
	 *            The amount of green.
	 * @param b
	 *            The amount of blue.
	 */
	public void setPixel(int x, int y, byte r, byte g, byte b) {
		int index = index(x, y, width);
		pixels[index + Channel.RED.offset] = r;
		pixels[index + Channel.GREEN.offset] = g;
		pixels[index + Channel.BLUE.offset] = b;
	}

	/**
	 * Sets a pixel in the pixel matrix. Any transparency will be ignored.
	 * 
	 * @param x
	 *            The x coordinate of the pixel (0&lt;=x&lt;width)
	 * @param y
	 *            The y coordinate of the pixel (0&lt;=y&lt;height)
	 * @param p
	 *            The ARGB packed pixel to set.
	 */
	public void setPixel(int x, int y, int p) {
		int index = index(x, y, width);
		pixels[index + Channel.RED.offset] = pixelChannel(p, Channel.RED);
		pixels[index + Channel.GREEN.offset] = pixelChannel(p, Channel.GREEN);
		pixels[index + Channel.BLUE.offset] = pixelChannel(p, Channel.BLUE);
	}

	/**
	 * Converts the Image into a byte[] array. This array can be modified
	 * without affecting this Image.
	 */
	public byte[] toArray() {
		return pixels.clone();
	}

	/**
	 * Convert between RGB and BGR
	 * 
	 * @param data
	 *            A byte[] image
	 * @param width
	 *            The width of the image
	 * @param height
	 *            The height of the image
	 * @return
	 */
	public static byte[] RGB2BGR(byte[] data, int width, int height) {
		byte[] ret = new byte[width * height * 3];
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				int i = index(c, r, width);
				ret[i + Channel.RED.offset] = data[i + Channel.BLUE.offset];
				ret[i + Channel.GREEN.offset] = data[i + Channel.GREEN.offset];
				ret[i + Channel.BLUE.offset] = data[i + Channel.RED.offset];
			}
		}
		return ret;
	}

	public static void smoothDepthImage(float[] float_array, int smoothRadius) {

		// TODO Auto-generated method stub

	}
}
