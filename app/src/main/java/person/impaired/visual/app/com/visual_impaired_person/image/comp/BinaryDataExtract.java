package person.impaired.visual.app.com.visual_impaired_person.image.comp;

import java.util.*;
import android.graphics.Bitmap;
import android.util.Log;

public class BinaryDataExtract
{

	Bitmap  srcImage = null;
	private int width;
	private int height;
	private byte[] data;
	private String title;
	private int[] colOrder;
	
	public BinaryDataExtract()
	{
		
		
		
	}
	
	public static byte[] getShapeGrapperData(Bitmap srcImage,byte[] srcData)
	{
		// Load Source image
		
				int width =srcImage.getWidth();
				int height = srcImage.getHeight();
				
			Log.i("blind shape start","shape");
				// Get raw image data
			
					
				// Sanity check image
				if (width * height * 3 != srcData.length) {
					System.err.println("Unexpected image data size. Should be RGB image");
					//System.exit(1);
				}

				// Create Monochrome version - using basic threshold technique
				byte[] monoData = new byte[width * height];
				int srcPtr = 0;
				int monoPtr = 0;

				while (srcPtr < srcData.length)
				{
					int val = ((srcData[srcPtr]&0xFF) + (srcData[srcPtr+1]&0xFF) + (srcData[srcPtr+2]&0xFF)) / 3;
					monoData[monoPtr] = (val > 128) ? (byte) 0xFF : 0;

					srcPtr += 3;
					monoPtr += 1;
				}

				byte[] dstData = new byte[srcData.length];
				//byte[] dstData1 = new byte[srcData.length];
				// Create ShapeIdentify Finder
		Log.i("blind shape blop","blop");

		BinaryShapeExtraction finder = new BinaryShapeExtraction(width, height);
				ArrayList<BinaryShapeExtraction.ShapeIdentify> blobList = new ArrayList<BinaryShapeExtraction.ShapeIdentify>();
				byte[] dstData1=finder.detectBlobs(monoData, dstData, 0, -1, (byte)0, blobList);
				// List Blobs

				
return dstData1;

	}

		
	
}
