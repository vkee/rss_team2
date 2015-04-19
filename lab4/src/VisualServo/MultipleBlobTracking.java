package VisualServo;

import java.awt.Color;

public class MultipleBlobTracking extends BlobTracking {
	private double [] targetHueLevels = {0, 120.0/360, 300.0/360, 55.0/360, 45.0/360, 240.0/360};//red, green, blue,yellow,orange,purple 	
	private double [] hueThresholds = {0.05,0.05,0.05,0.05,0.05,0.05};

	private int[][] multiBlobPixelMask = null;
	private int[][] multiBlobMask = null;
	private int[][] multiImageConnected = null;

	private boolean[] multiTargetDetected = {false,false,false,false,false,false};

	private boolean notSelected = true;

	public MultipleBlobTracking(int width, int height){
		super(width,height);	
		multiBlobPixelMask = new int[6][width*height];
		multiBlobMask = new int[6][width*height];
		multiImageConnected = new int[6][width*height];
	}

	protected void markBlob(Image src, Image dest) { // (Solution)

/*
		for (int i = 0; i < multiBlobMask.length; i++){
			int maskIndex = 0; //(Solution)
			for (int y = 0; y < height; y++) { //(Solution)
				for (int x = 0; x < width; x++) { //(Solution)
					int pix = src.getPixel(x, y); // (Solution)
					float[] hsb = Color.RGBtoHSB(Image.pixelRed(pix), // (Solution)
							Image.pixelGreen(pix), // (Solution)
							Image.pixelBlue(pix), null); // (Solution)
					double hue = targetHueLevels[i];
					double hue_Threshold = hueThresholds[i];
					if (hsb[1] > saturationLevel && Math.abs(hsb[0]-hue) < hue_Threshold){
						multiBlobMask[i][maskIndex] = 255; // (Solution)
					}else{
						multiBlobMask[i][maskIndex] = 0;
					}
					maskIndex++;
				} //(Solution)
			} //(Solution)
		} // (Solution)
*/

		for (int i = 0; i < multiBlobMask.length; i++){
			int maskIndex = 0; //(Solution)
			for (int y = 0; y < height; y++) { //(Solution)
				for (int x = 0; x < width; x++) { //(Solution)
				if (multiBlobMask[i][maskIndex] > 0){
					if (i ==0){
						dest.setPixel(x, y, (byte) 0xff,(byte) 0,(byte) 0);
					}else if (i == 1){
						dest.setPixel(x, y, (byte) 0,(byte) 0xff,(byte) 0);
					}else if (i == 2){
						dest.setPixel(x, y, (byte) 0,(byte) 0,(byte) 0xff);
					}else if (i == 3){
						dest.setPixel(x, y, (byte) 0,(byte) 0xff,(byte) 0xff);
					}else if (i == 4){
						dest.setPixel(x, y, (byte) 0xff,(byte) 0xff,(byte) 0);
					}else if (i == 5){
						dest.setPixel(x, y, (byte) 0xff,(byte) 0,(byte) 0xff);
					}
				} maskIndex++;
			}
		}
	}
	}
	protected void blobPixel(Image src, int[][] mask){
		for (int i = 0; i < targetHueLevels.length; i++){
			int maskIndex = 0;
			for (int y = 0; y < height; y++) { // (Solution)
				for (int x = 0; x < width; x++) { // (Solution)
					int pix = src.getPixel(x, y); // (Solution)
					float[] hsb = Color.RGBtoHSB(Image.pixelRed(pix), // (Solution)
							Image.pixelGreen(pix), // (Solution)
							Image.pixelBlue(pix), null); // (Solution)
					double hue = targetHueLevels[i];
					double hue_Threshold = hueThresholds[i];
					if (hsb[1] > saturationLevel && Math.abs(hsb[0]-hue) < hue_Threshold){
						mask[i][maskIndex++] = 255; // (Solution)
					}else{
						mask[i][maskIndex++] = 0;
					}
				}
			} 
		} 
	}//end blobPixel

	protected void blobPresent(int[][] m_threshIm, int[][] m_connIm, int[][] m_blobIm) { // (Solution)
		// (Solution)
		for(int i = 0; i < m_threshIm.length; i++){
			int[] threshIm = m_threshIm[i]; 
			int[] connIm = m_connIm[i];
			int[] blobIm = m_blobIm[i];
			ConnectedComponents connComp = new ConnectedComponents(); //(Solution)
			connComp.doLabel(threshIm, connIm, width, height); // (Solution)
			int colorMax = connComp.getColorMax(); // (Solution)
			int countMax = connComp.getCountMax(); // (Solution)

			if (countMax > blobSizeThreshold * height * width) { // (Solution)
				targetArea = countMax; // (Solution)
				int destIndex = 0; // (Solution)
				for (int y = 0; y < height; y++) { // (Solution)
					for (int x = 0; x < width; x++) { // (Solution)
						if (connIm[destIndex] == colorMax) { // (Solution)
							//sx += x; // (Solution)
							//sy += y; // (Solution)
							m_blobIm[i][destIndex++] = 255; // (Solution)
						} else { // (Solution)
							m_blobIm[i][destIndex++] = 0; // (Solution)
						} // (Solution)
					} // (Solution)
				} // (Solution)
			} // (Solution)
		} //End of blob present
	}

	public void apply(Image src, Image dest) {

		stepTiming(); 
		
		if (useGaussianBlur) {// (Solution)
			byte[] srcArray = src.toArray();// (Solution)
			byte[] destArray = new byte[srcArray.length]; // (Solution)
			if (approximateGaussian) { // (Solution)
				GaussianBlur.applyBox(srcArray, destArray, src.getWidth(), src.getHeight());
			} // (Solution)
			else { // (Solution)
				GaussianBlur.apply(srcArray, destArray, width, height); // (Solution)
			} // (Solution)
			src = new Image(destArray, src.getWidth(), src.getHeight()); // (Solution)
		}

		blobPixel(src, multiBlobPixelMask); //(Solution)
		blobPresent(multiBlobPixelMask, multiImageConnected, multiBlobMask); //(Solution)
/*
		if (true) { // (Solution)
			blobFix(); // (Solution)
			computeTranslationVelocityCommand(); // (Solution)
			computeRotationVelocityCommand(); // (Solution)
			
		} else { // (Solution)
			translationVelocityCommand = 0.0; // (Solution)
			rotationVelocityCommand = 0.0; // (Solution)
		} /// (Solution)
		/**/

		// (Solution)
		// For a start, just copy src to dest. // (Solution)
		if (dest != null) { // (Solution)
			// (Solution)
			//dest = Histogram.getHistogram(src, dest, true); // (Solution)
			markBlob(src, dest); // (Solution)
			// (Solution)
		} // (Solution)
		// End Student Code
	}
}