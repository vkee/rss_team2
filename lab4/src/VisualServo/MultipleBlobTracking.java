package VisualServo;

import java.awt.Color;


public class MultipleBlobTracking extends BlobTracking {
	private double [] targetHueLevels = {0.0, 120.0/360, 240.0/360, 60.0/360, 24.0/360, 300.0/360};
//red, green, blue,yellow,orange,purple 
	private double [] hueThresholds = {0.1,0.1,0.15,0.05,0.05,0.05};
	private double other_hueThreshold = 0.1;

	protected double [] multiSaturationLevel = {0.6,0.3,0.3,0.8,0.8,0.2};
	double other_saturation = 0.6;
	double [] multiBrightnessLevel = {0.55,0.0,0.0,0.5,0.3,0.2};
	double other_brightness = 0.55;
	int[][] multiBlobPixelMask = null;
	int[][] multiBlobMask = null;
	int[][] multiImageConnected = null;

	double[] centroidX = new double[targetHueLevels.length];
	double[] centroidY = new double[targetHueLevels.length];
	int[] targetArea = new int[targetHueLevels.length];
	 boolean[] multiTargetDetected =  {false,false,false,false,false,false};


	public MultipleBlobTracking(int width, int height){
		super(width,height);	
		multiBlobPixelMask = new int[targetHueLevels.length][width*height];
		multiBlobMask = new int[targetHueLevels.length][width*height];
		multiImageConnected = new int[targetHueLevels.length][width*height];
	}

	protected void blobFix(int index) { //(Solution)
		double deltaX = centroidX[index] - width / 2.0; //(Solution)
		targetRange = //(Solution)
			focalPlaneDistance * targetRadius / Math.sqrt(targetArea[index] / Math.PI); //(Solution)
		targetBearing = Math.atan2(deltaX, focalPlaneDistance); //(Solution)
	} //(Solution)

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
					}else if(i==0&&hsb[2]>other_brightness&&hsb[1]>other_saturation&&Math.abs(hsb[0]-(360.0/360))<other_hueThreshold ){
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
				targetArea[i] = countMax; // (Solution)
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
	
	public void findFiducial(int[][] multiBlobMask, Image dest){
//		for (int i = 0; i < multiBlobMask.length; i++){
			double rad_d = Math.sqrt((targetArea[0]/Math.PI));
			int x = (int)centroidX[0];
			int y = (int)centroidY[0];
			int circle_counter = 0;
			int z = y*this.width+x;
			double[] angles = {0,Math.PI/12,Math.PI/8,Math.PI/6,Math.PI/5,Math.PI/4,Math.PI/3,Math.PI/2.5,Math.PI/2};
			for (double ang : angles){
				int x_angle = (int)(rad_d*Math.cos(ang));
				int y_angle = (int)(rad_d*Math.sin(ang));
				
				if(x+x_angle<this.width && x-x_angle>0 && y+y_angle<this.height && y-y_angle>0){
				if (multiBlobMask[0][(x+x_angle)+((y+y_angle)*this.width)] > 0){
					circle_counter++;
				}
				if (multiBlobMask[0][(x-x_angle)+((y-y_angle)*this.width)] > 0){
					circle_counter++;
				}
				if (multiBlobMask[0][(x-x_angle)+((y+y_angle)*this.width)] > 0){
					circle_counter++;
				}
				if (multiBlobMask[0][(x+x_angle)+((y-y_angle)*this.width)] > 0){
					circle_counter++;
				}
				dest.setPixel(x+x_angle, y+y_angle, (byte) 0xff,(byte) 0xff,(byte) 0xff);
				dest.setPixel(x-x_angle, y-y_angle, (byte) 0xff,(byte) 0xff,(byte) 0xff);
				dest.setPixel(x-x_angle, y+y_angle, (byte) 0xff,(byte) 0xff,(byte) 0xff);
				dest.setPixel(x+x_angle, y-y_angle, (byte) 0xff,(byte) 0xff,(byte) 0xff);
			}}
//		}
		System.out.println(circle_counter/(double)(4*angles.length));
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
		findFiducial(multiBlobMask,dest);

		for(int i = 0; i < multiTargetDetected.length; i++){
			int x = (int)centroidX[i];
			int y = (int)centroidY[i];
			if (x>10 && y>10){
				for(int j = 0; j< 10; j++){
					for(int k = 0; k< 10; k++){				
						dest.setPixel(x+j, y+k, (byte) 0,(byte) 0,(byte) 0);
					}
				}
			}
		}

	/*Pick one blob to go to, find it's index and fix on it*/
/*
		if (true) { // (Solution)
			index = 0;//find's red block because 0 is the first index
			blobFix(index); // (Solution)
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
