package person.impaired.visual.app.com.visual_impaired_person.image.comp;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.Settings.Global;
import android.util.Log;

public class ImagePixel {

	public ImagePixel() {
		// TODO Auto-generated constructor stub
	}
	public static boolean compareImages(Bitmap bm1, Bitmap bm2, boolean expected)
    {
        boolean result = true;
        //if(bm1.getHeight() != bm2.getHeight() ) result = false;
        //if(bm1.getWidth() != bm2.getWidth()) result = false;
 
        outerLoop:
        for(int x =0; x < bm1.getWidth(); x++)
            for(int y = 0; y < bm1.getHeight(); y++)
                if(bm1.getPixel(x, y) != bm2.getPixel(x, y)){
                    result = false;
                    break outerLoop;
                }
 
        if(expected != result)
        {
            Log.i(Global._ID, "Not expected, here save these two bitmap as PNG");
            ImagePixel.saveAsPNGFile(bm1, "bm1");
            ImagePixel.saveAsPNGFile(bm2, "bm2");
        }
 
        return result;
 
    }
	
	
	 public static void saveAsPNGFile(Bitmap bm, String fileNameExtension){
	        String path = Environment.getExternalStorageDirectory().getPath()
	                + "/imgprocess/";
	        String filename;
	        try {
	            Date date = new Date();
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmms");
	            filename = sdf.format(date);
	            File dic = new File(path);
	            if(!dic.exists())
	                dic.mkdirs();
	            File file = new File(path, filename +"_"+fileNameExtension + ".PNG");
	            FileOutputStream out = new FileOutputStream(file);
	            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
	            out.flush();
	            out.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            Log.e("Save bitmap file failed", ex.getMessage());
	        }
	    }
	
}
