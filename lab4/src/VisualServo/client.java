package VisualServo;


import java.net.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.*;

public class client{

	public client(){
	}

	public byte[] extractBytes (String ImageName) throws IOException {
	 // open image
	 File imgPath = new File(ImageName);
	 BufferedImage bufferedImage = ImageIO.read(imgPath);

	 // get DataBufferBytes from Raster
	 WritableRaster raster = bufferedImage .getRaster();
	 DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

	 return ( data.getData() );
	}

	public Image getTestImage(int image_index){
		try{
			File file = null;
			if (image_index == 1){
			 file = new File("/mnt/hgfs/snaps/rgb_4blocks.bin");
			}else if (image_index == 2){
			 file = new File("/mnt/hgfs/snaps/rgb_fiducial.bin");
			}else if (image_index == 3){
			 file = new File("/mnt/hgfs/snaps/rgb_fiducial_calibration.bin");
			}else if (image_index == 4){
			 file = new File("/mnt/hgfs/snaps/rgb_blue_calibration.bin");
			}else{

			 file = new File("/mnt/hgfs/snaps/rgb_4blocks.bin");			
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] data_ = new byte[(int)file.length()];
			fis.read(data_);
			int width = 640;//scan_width.nextInt();
			int height = 480;//scan_height.nextInt();
			byte[] data = Image.RGB2BGR(data_,width,height);
			return new Image(data,width,height);
		} catch(Exception e){
			return null;
		}
	
	}

	public Image getImage(){

		try{
//			byte[] data = extractBytes("/mnt/hgfs/snaps/test1.png");

			File file = new File("/mnt/hgfs/fast/rgb.bin");
			FileInputStream fis = new FileInputStream(file);
			byte[] data_ = new byte[(int)file.length()];
			fis.read(data_);

//			Scanner scan_width = new Scanner(new FileReader("/mnt/hgfs/snaps/width.txt"));
//			Scanner scan_height = new Scanner(new FileReader("/mnt/hgfs/snaps/height.txt"));
			int width = 640;//scan_width.nextInt();
			int height = 480;//scan_height.nextInt();
			byte[] data = Image.RGB2BGR(data_,width,height);
			return new Image(data,width,height);

		} catch(Exception e){
			return null;
		}
	}
}

