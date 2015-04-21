package VisualServo;

import java.awt.Color;


public class MultipleBlobTracking extends BlobTracking {
	private double [] targetHueLevels = {0.0, 120.0/360, 240.0/360, 60.0/360, 24.0/360, 300.0/360};
//red, green, blue,yellow,orange,purple 
	private double [] hueThresholds = {0.1,0.1,0.05,0.05,0.05,0.05};

	private double [] multiSaturationLevel = {0.35,0.0,0.3,0.8,0.8,0.2};

	private double [] multiBrightnessLevel = {0.5,0.0,0.0,0.5,0.3,0.2};

	private int[][] multiBlobPixelMask = null;
	private int[][] multiBlobMask = null;
	private int[][] multiImageConnected = null;

	double[] centroidX = new double[targetHueLevels.length];
	double[] centroidY = new double[targetHueLevels.length];

	private boolean[] multiTargetDetected =  {false,false,false,false,false,false};


	public MultipleBlobTracking(int width, int height){
		super(width,height);	
		multiBlobPixelMask = new int[targetHueLevels.length][width*height];
		multiBlobMask = new int[targetHueLevels.length][width*height];
		multiImageConnected = new int[targetHueLevels.length][width*height];
	}

	protected void markBlob(Image src, Image dest) { // (Solution)

		for (int i = 0; i < multiBlobMask.length; i++){
			int maskIndex = 0; //(Solution)
			for (int y = 0; y < height; y++) { //(Solution)
				for (int x = 0; x < width; x++) { //(Solution)
//red, green, blue,yellow,orange,purple 	
				if (multiBlobMask[i][maskIndex] > 0){
					if (i ==0){
						dest.setPixel(x, y, (byte) 0xff,(byte) 0,(byte) 0);
					}else if (i == 1){
						dest.setPixel(x, y, (byte) 0,(byte) 0xff,(byte) 0);
					}else if (i == 2){
						dest.setPixel(x, y, (byte) 0,(byte) 0,(byte) 0xff);
					}else if (i == 3){
						dest.setPixel(x, y, (byte) 0xff,(byte) 0xff,(byte) 0);
					}else if (i == 4){
						dest.setPixel(x, y, (byte) 0xff,(byte) 0x66,(byte) 0);
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
					
					if (hsb[2] > multiBrightnessLevel[i] && hsb[1] > multiSaturationLevel[i] && Math.abs(hsb[0]-hue) < hue_Threshold){
						mask[i][maskIndex++] = 255; // (Solution)
					}else if(i==0&&hsb[2]>0.5&&hsb[1]>0.5&&Math.abs(hsb[0]-(360.0/360))<0.1 ){
						mask[i][maskIndex++] = 255; // (Solution)
					}else{ 
						mask[i][maskIndex++] = 0;
					}
				}
			} 
		} 
	}

	protected void blobPresent(int[][] m_threshIm, int[][] m_connIm, int[][] m_blobIm) {
		for(int i = 0; i < m_threshIm.length; i++){
			int sx = 0;
			int sy = 0;
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
							sx += x; // (Solution)
							sy += y; // (Solution)
							m_blobIm[i][destIndex++] = 255; // (Solution)
						} else { // (Solution)
							m_blobIm[i][destIndex++] = 0; // (Solution)
						} // (Solution)
					} // (Solution)
				} // (Solution)
				centroidX[i] = sx / (double) countMax; 
				centroidY[i] = sy / (double) countMax; 
			} 
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

		if (dest != null) { // (Solution)
			//dest = Histogram.getHistogram(src, dest, true); // (Solution)
			markBlob(src, dest); // (Solution)
		} // (Solution)
		for(int i = 0; i < multiTargetDetected.length; i++){
			int x = (int)centroidX[i];
			int y = (int)centroidY[i];
			if (x>10 && y>10){
			for(int j = 0; j< 10; j++){
				for(int k = 0; k< 10; k++){				
			dest.setPixel(x+j, y+k, (byte) 0,(byte) 0,(byte) 0);
			}}}
			//System.out.println("X" + i + " " + centroidX[i]);
			//System.out.println("Y" + i + " " + centroidY[i]);
		}
		
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

		// End Student Code
	}
}
