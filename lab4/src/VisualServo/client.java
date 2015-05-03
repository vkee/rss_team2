package VisualServo;

import java.net.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.*;

public class client {

	public client() {
	}

	public byte[] extractBytes(String ImageName) throws IOException {
		// open image
		File imgPath = new File(ImageName);
		BufferedImage bufferedImage = ImageIO.read(imgPath);

		// get DataBufferBytes from Raster
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

		return (data.getData());
	}

	public Image getTestImage(int image_index) {
		try {
			File file = null;
			if (image_index == 1) {
				file = new File("/mnt/hgfs/snaps/rgb_4blocks.bin");
			} else if (image_index == 2) {
				file = new File("/mnt/hgfs/snaps/rgb_fiducial.bin");
			} else if (image_index == 3) {
				file = new File("/mnt/hgfs/snaps/rgb_fiducial_calibration.bin");
			} else if (image_index == 4) {
				file = new File("/mnt/hgfs/snaps/rgb_blue_calibration.bin");
			} else {
				file = new File("/mnt/hgfs/snaps/rgb_4blocks.bin");
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] data_ = new byte[(int) file.length()];
			fis.read(data_);
			int width = 640;// scan_width.nextInt();
			int height = 480;// scan_height.nextInt();
			byte[] data = Image.RGB2BGR(data_, width, height);
			return new Image(data, width, height);
		} catch (Exception e) {
			return null;
		}

	}

	public double[] getDepthArray() {
		double[] out_array = null;
		float fT = 42.775668509f;

		try {
			File file = new File("/mnt/hgfs/fast/depth.bin");
			FileInputStream fis = new FileInputStream(file);
			byte[] data_ = new byte[(int) file.length()];
			fis.read(data_);
			int asInt;
			float asFloat;
			out_array = new double[data_.length / 4];

			for (int i = 0; i < data_.length - 3; i += 4) {
				asInt = (data_[i] & 0xFF) | ((data_[i + 1] & 0xFF) << 8)
						| ((data_[i + 2] & 0xFF) << 16)
						| ((data_[i + 3] & 0xFF) << 24);
				asFloat = Float.intBitsToFloat(asInt);
				if (asFloat > 0) {
					out_array[(int) (i / 4.0)] = (fT) / asFloat;
				} else {
					out_array[(int) (i / 4.0)] = 0;
				}
			}
		} catch (Exception e) {
		}
		return out_array;
	}

	public float[] getDepthImage() {
		float[] float_array = null;
		try {
			File file = new File("/mnt/hgfs/fast/depth.bin");
			FileInputStream fis = new FileInputStream(file);
			byte[] data_ = new byte[(int) file.length()];
			fis.read(data_);
			int asInt;
			float asFloat;
			float_array = new float[data_.length / 4];

			for (int i = 0; i < data_.length - 3; i += 4) {
				asInt = (data_[i] & 0xFF) | ((data_[i + 1] & 0xFF) << 8)
						| ((data_[i + 2] & 0xFF) << 16)
						| ((data_[i + 3] & 0xFF) << 24);
				asFloat = Float.intBitsToFloat(asInt);
				float_array[(int) (i / 4.0)] = asFloat;
			}
		} catch (Exception e) {
		}
		return float_array;
	}

	public Image getImage() {

		try {
			File file = new File("/mnt/hgfs/fast/rgb.bin");
			FileInputStream fis = new FileInputStream(file);
			byte[] data_ = new byte[(int) file.length()];
			fis.read(data_);
			int width = 640;// scan_width.nextInt();
			int height = 480;// scan_height.nextInt();
			byte[] data = Image.RGB2BGR(data_, width, height);
			return new Image(data, width, height);

		} catch (Exception e) {
			return null;
		}
	}
}
