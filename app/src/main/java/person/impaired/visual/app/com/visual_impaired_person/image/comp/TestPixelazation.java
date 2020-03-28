package person.impaired.visual.app.com.visual_impaired_person.image.comp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class TestPixelazation {
	Context context=null;
	public TestPixelazation(Context context1) {
		// TODO Auto-generated constructor stub
		this.context=context1;
	}
	public static final int BLACK =0, WHITE= 0xFFFFFF, RED=0x580000;
	  
	
	
	public String setImg(String strpathLoc)
	{
		String strreturn="";
		try
		{
		//displayHandleMsg("Data Receive : "+b.length+" bytes...");
		String strSrcPath=ImageDate();
		//strSrcPath=getTestImages(strpathLoc);
		//getSavedFile(strSrcPath,b);
		
		Bitmap ResizeImg=getImage(strpathLoc);
		Bitmap strPath= getImageBitmap(strSrcPath);
		//Bitmap ResizeImg= toGrayscale(strPath);
		
		File fobjtest=new File(strpathLoc);
		displayHandleMsg("Gray Filter: "+ResizeImg);
	    String strreturnpath=storeRezier(ResizeImg,fobjtest.getName());
	    strreturn=strreturnpath;
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return strreturn;
	}
	  
	  
	  
	
	  public String getTestImages(String strtestfile)
		{
         String strpathloc="";
			String IMAGE_DIRECTORY_Train = "Blind/testObstacles";
			//String IMAGE_DIRECTORY_CLIENT = sessUser.getSharedClient();

			File[] listFile;
			File filetrainrawdir = null;
		
			filetrainrawdir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						IMAGE_DIRECTORY_Train);

			strpathloc=filetrainrawdir.getAbsolutePath()+File.pathSeparator+strtestfile;
			
			return strpathloc;
		}
	  
	  
	  public String storeRezier(Bitmap bm,String strfilename)
	  {
		  String strgetPath="";
		  File file=null;
		  boolean btrue=false;
		try
		{
			
			File fextract=new File(strfilename);
			
			strfilename=fextract.getName().toString().trim();
		    String	pathresizer ="Blind/TestRezier";

			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					pathresizer);
			strgetPath=file.getAbsolutePath();
			String outputfile=file.getAbsolutePath()+"/"+strfilename;
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			   bm.compress(Bitmap.CompressFormat.JPEG,97 , baos);
			   byte[] b = baos.toByteArray(); 
			   FileOutputStream fout = new FileOutputStream(outputfile);
			   fout.write(b);

			   
		}
		catch(Exception e)
		{
			e.getMessage();
			displayHandleMsg(e.getMessage());
		}
		return strgetPath;
	  }
	  
	  public String getDateTime()
	  {
		  Calendar calendar = Calendar.getInstance();

			String sequencefile ="Seq - " +  calendar.get(Calendar.YEAR) + "-" 
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH)+ "-" 
					+ calendar.get(Calendar.HOUR) + "-" 
					+ calendar.get(Calendar.MINUTE)+ "-"
					+ calendar.get(Calendar.SECOND);
			
			String strgeneratefile=sequencefile+".jpg";
			displayHandleMsg("generate file : "+strgeneratefile);
		return 	strgeneratefile;
	  }
	  
	  
	  public String ImageDate()
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// "yyyy-MM-dd HH:mm:ss"
			Date date = new Date();
			
			String uploadOn = dateFormat.format(date);
			//System.out.println(dateFormat.format(date).substring(0, 4));
			return uploadOn; // dateFormat.format(date).toString();
		}
	///===============================================================================================
	  
	public void getSavedFile(String strSrcPath,byte data[])
	{
		String StrReturn = "";
		FileInputStream in = null;

		OutputStream out = null;

		try {
			File fob = new File(strSrcPath);

			String strTempPath = strSrcPath;//+ File.separator + fob.getName();
			File imagefile = new File(strTempPath);
			String Strfilename = fob.getName().toString().trim();

			if(fob.exists())
			{
			in = new FileInputStream(fob);

			out = new FileOutputStream(imagefile);

			// Copy the bits from instream to outstream

			/*byte[] buf = new byte[(int) fob.length()];
			int len = 0;
			while ((len = in.read(buf)) > 0) {*/
				out.write(data);
				// pDialogs.setProgress((int)((total*100)/lenghtOfFile));
		//	}
			in.close();
			}
		}
			catch(Exception e)
			{
				Log.d("Byte file write err: ",e.getMessage());
				displayHandleMsg(e.getMessage());
			}
			
	}
	  
	  
	  
	  public String getProcessFile(String strSrcPath, String strDstPath) {

			String StrReturn = "";
			FileInputStream in = null;

			OutputStream out = null;

			try {
				File fob = new File(strSrcPath);

				String strTempPath = strDstPath + File.separator + fob.getName();
				File imagefile = new File(strTempPath);
				String Strfilename = fob.getName().toString().trim();

				if(fob.exists())
				{
				in = new FileInputStream(fob);

				out = new FileOutputStream(imagefile);

				// Copy the bits from instream to outstream

				byte[] buf = new byte[(int) fob.length()];
				int len = 0;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
					// pDialogs.setProgress((int)((total*100)/lenghtOfFile));
				}
				
				in.close();

				boolean b = fob.delete();

				if (b == true && imagefile.exists()) {
					StrReturn = Strfilename.concat(" Moved Successfull");
				} else {
					StrReturn = Strfilename.concat(" not Moved Successfull");
				}
				}
				out.close();
				//logger.info("file moved : " + StrReturn);

			}
			catch (Exception e)
			{
				//logger.severe("Error in Moving file : " + e.getMessage().toString());
				StrReturn = "Error in Moving file : " + e.getMessage().toString();
				displayHandleMsg(e.getMessage());
			} finally {
				try {
					in.close();
					out.close();
				} catch (Exception e) {
				
	 
				}

			}

			return StrReturn;
		}
	  
	  
	  
	  public Bitmap getImageBitmap(String filepath)
	  {
		  
		  Bitmap bm=null;
		  try
		   {
			   File imagefile = new File(filepath);
			   FileInputStream fis = null;
			  
			       fis = new FileInputStream(imagefile);
			      bm = BitmapFactory.decodeStream(fis);
			   }
			   catch (FileNotFoundException e)
			   {
				   Log.d("convert bitmap  err: ",e.getMessage());
				   displayHandleMsg(e.getMessage());
			   }
		  
		  return bm;
	  }
	  
	  
	  public Bitmap toGrayscale(Bitmap bmpOriginal)
	  {        
	      int width, height;
	      height = bmpOriginal.getHeight();
	      width = bmpOriginal.getWidth();    

	      Bitmap bmpGrayscale = Bitmap.createBitmap(250,250, Bitmap.Config.RGB_565);
	      Canvas c = new Canvas(bmpGrayscale);
	      Paint paint = new Paint();
	      ColorMatrix cm = new ColorMatrix();
	      cm.setSaturation(0);
	      ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	      paint.setColorFilter(f);
	      c.drawBitmap(bmpOriginal, 0, 0, paint);
	      
	      return bmpGrayscale;
	  	}
	  
	  public int CompareBitMap(Bitmap train,Bitmap test){
			
			int width = test.getWidth();
			int height = test.getHeight();
			
			int MatchCount=0;
			int UnMatchCount=0;
			
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (train.getPixel(j, i)-test.getPixel(j, i)==0) {
						MatchCount++;	
					}
					else
					{
						UnMatchCount++;
					}
				}
				
			}
			
			//307200 pixel var
					
			/*if(degil>((esit+degil)*0.1))
				return true;
				else
				return false;*/
			return UnMatchCount;
		}
		
	  
	  
	  public int[][]  getPixelforEachImage(Bitmap bm,int width,int height)
	  {
		  int pixel[][]={};
		  try
		   {
			  Bitmap mCurrentFrame = Bitmap.createScaledBitmap(bm, width, height, false);
		   }
		  catch(Exception e)
		  {
			  e.getMessage();
			  displayHandleMsg(e.getMessage());
		  }
		  
		  return pixel;
	  }
	  
	  public void displayHandleMsg(String strMsg)
	  {
		 Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
	  }
	  
	  
 ////=======================================================================================
	  
	  public Bitmap getImage(String path) throws IOException
	    {
		  
		 
            System.gc();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(path, options);        
	        int srcWidth = options.outWidth;
	        int srcHeight = options.outHeight;
	        int[] newWH =  new int[2];
	        newWH[0] = srcWidth/2;
	        newWH[1] = (newWH[0]*srcHeight)/srcWidth;

	        int inSampleSize = 1;
	        while(srcWidth / 2 >= newWH[0]){
	            srcWidth /= 2;
	            srcHeight /= 2;
	            inSampleSize *= 2;
	        }

	        //      float desiredScale = (float) newWH[0] / srcWidth;
	        // Decode with inSampleSize
	        options.inJustDecodeBounds = false;
	        options.inDither = false;
	        options.inSampleSize = inSampleSize;
	        options.inScaled = false;
	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	        Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(path,options);
	        ExifInterface exif = new ExifInterface(path);
	        String s=exif.getAttribute(ExifInterface.TAG_ORIENTATION);
	        System.out.println("Orientation>>>>>>>>>>>>>>>>>>>>"+s);
	        Matrix matrix = new Matrix();
	       
	        int newh = ( srcWidth * sampledSrcBitmap.getHeight() ) /sampledSrcBitmap.getWidth();
	        Bitmap r=Bitmap.createScaledBitmap(sampledSrcBitmap,  newh, newh, true);
	        Bitmap resizedBitmap = Bitmap.createBitmap(r, 0, 0, srcWidth, newh,matrix, true);
	        resizedBitmap= toGrayscales(resizedBitmap);

	        return resizedBitmap;
	    }
	  
	  public Bitmap toGrayscales(Bitmap bmpOriginal)
	  {        
	      int width, height;
	      height = bmpOriginal.getHeight();
	      width = bmpOriginal.getWidth();    

	      Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	      Canvas c = new Canvas(bmpGrayscale);
	      Paint paint = new Paint();
	      ColorMatrix cm = new ColorMatrix();
	      cm.setSaturation(0);
	      ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	      paint.setColorFilter(f);
	      c.drawBitmap(bmpOriginal, 0, 0, paint);

	      return bmpGrayscale;
	  }
	  
	  ///================================================================================
	  
	
	  ////=======================================================================================
	  
		 public boolean isBetween(int r,int g,int b,int r1,int g1,int b1)
	    {
	        System.out.println("MinY : "+"r= "+r+"g= "+g+"b= "+b+"r1= "+r1+"g1= "+g1+"b1= "+b1); 
	        
	     return ((r1 <= r) && (g1 <= g) && (b1 <= b) );
	    }
	}