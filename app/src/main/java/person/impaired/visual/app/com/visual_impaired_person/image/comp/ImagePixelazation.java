package person.impaired.visual.app.com.visual_impaired_person.image.comp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ImagePixelazation
{

	Context mContext1=null;

	public ImagePixelazation(Context mContext)
	{
		// TODO Auto-generated constructor stub
			 mContext1= mContext;
	}

	public static final int BLACK =0, WHITE= 0xFFFFFF, RED=0x580000;
	  
	  public void getResizeInputTrainImage()
	  {
		 List objlst= getTrainSdcardImages();
		 Log.d(" img no of count : ", ""+objlst.size());
		 getResizedImage(objlst);
	  }
	  
	  public void getResizedImage(List mList)
	  {
		  try
		  {
		  String strPathDir="";
		  for (int i = 0; i < mList.size(); i++)
			{
			  try
			  {
			 strPathDir=mList.get(i).toString().trim();
			//Bitmap strPath= getImageBitmap(strPathDir);
			//Bitmap ResizeImg= toGrayscale(strPath);
			 Bitmap ResizeImg=getImage(strPathDir);
			 storeRezier(ResizeImg,strPathDir);
			 ResizeImg.recycle();
			 ResizeImg = null;
			 System.gc();
			  }
			  catch(Exception e)
			  {
				  Log.d("Errors1 : ", e.getMessage());
				  e.getMessage();
			  }
			 
			}
		  
		  }
		  catch(Exception e)
		  {
			  Log.d("Errors 2 : ", e.getMessage());
			  displayHandleMsg( e.getMessage()); 
		  }
				
	  }
	  
	
	  public List getTrainSdcardImages()
		{
            List imageUrls=new ArrayList();
			String IMAGE_DIRECTORY_Train = "ObjectTracking/TrainObject";
			//String IMAGE_DIRECTORY_CLIENT = sessUser.getSharedClient();

			File[] listFile;
			File filetrainrawdir = null;
		
			filetrainrawdir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						IMAGE_DIRECTORY_Train);


			imageUrls.clear();
			
			//file = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
			if (filetrainrawdir.isDirectory()) {
				listFile = filetrainrawdir.listFiles();

				for (int i = 0; i < listFile.length; i++)
				{
					imageUrls.add(listFile[i].getAbsolutePath());
				}
				Log.i("blind", ""+imageUrls.size());


			}
			
			return imageUrls;
		}
	  
	  
	  public void storeRezier(Bitmap bm,String strfilename)
	  {
		  File file=null;
		  boolean btrue=false;
		try
		{
			
			File fextract=new File(strfilename);
			
			strfilename=fextract.getName().toString().trim();
		    String	pathresizer ="ObjectTracking/TrainObjectRezier";

			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					pathresizer);

			   String outputfile=file.getAbsolutePath()+"/"+strfilename;
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			   bm.compress(Bitmap.CompressFormat.JPEG,100 , baos);
			   byte[] b = baos.toByteArray(); 
			   
			   FileOutputStream fout = new FileOutputStream(outputfile); 
			  fout.write(b);
			  bm.recycle();
			   
		}
		catch(Exception e)
		{
			 displayHandleMsg( e.getMessage());
			e.getMessage();
		}
		
	  }
	  
	  public String getDateTime()
	  {
		  Calendar calendar = Calendar.getInstance();
			String sequencefile ="Seq-" +  calendar.get(Calendar.YEAR) + "-" 
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH)+ "-" 
					+ calendar.get(Calendar.HOUR) + "-" 
					+ calendar.get(Calendar.MINUTE)+ "-"
					+ calendar.get(Calendar.SECOND);
			
			String strgeneratefile=sequencefile+".jpg";
			 displayHandleMsg(strgeneratefile);
		return 	strgeneratefile;
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

			byte[] buf = new byte[(int) fob.length()];
			int len = 0;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
				// pDialogs.setProgress((int)((total*100)/lenghtOfFile));
			}
			in.close();
			out.close();
			}
		}
			catch(Exception e)
			{
				 displayHandleMsg( e.getMessage());
				Log.d("Byte file write err: ",e.getMessage());
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
				 displayHandleMsg( e.getMessage());
				//logger.severe("Error in Moving file : " + e.getMessage().toString());
				StrReturn = "Error in Moving file : " + e.getMessage().toString();
			}
			finally
			{
				try
				{
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
			      // bm.recycle();
			       if(bm!=null)
			       {
			         bm.recycle();
			         bm=null;
			        }
			      bm = BitmapFactory.decodeStream(fis);
			      try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
			   catch (FileNotFoundException e)
			   {
				   Log.d("convert bitmap err: ",e.getMessage());
				   displayHandleMsg( e.getMessage());
			   }
		  
		  return bm;
	  }
	  
	  
	  public Bitmap toGrayscale(Bitmap bmpOriginal)
	  {   
		  Bitmap bmpGrayscale=null;
		  try
		  {
	      int width, height;
	      height = bmpOriginal.getHeight();
	      width = bmpOriginal.getWidth();    
	      if(bmpGrayscale!=null)
	       {
	    	  bmpGrayscale.recycle();
	    	  bmpGrayscale=null;
	        }
	  // bmpGrayscale = Bitmap.createBitmap(640,480, Bitmap.Config.RGB_565);

	      bmpGrayscale = Bitmap.createBitmap(640,480, Bitmap.Config.ARGB_8888);
	      Canvas c = new Canvas(bmpGrayscale);
	      Paint paint = new Paint();
	      ColorMatrix cm = new ColorMatrix();
	      cm.setSaturation(0);
	      ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	      paint.setColorFilter(f);
	      c.drawBitmap(bmpOriginal, 0, 0, paint);
		  }
		  catch(Exception e)
		  {
			  Log.d("Err gray filt ",e.getMessage());
			 e.getMessage();
			 
		  }
	      
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
			  displayHandleMsg( e.getMessage());
			  e.getMessage();
		  }
		  
		  return pixel;
	  }
	  
	  public void displayHandleMsg(String strMsg)
	  {
		 Toast.makeText(mContext1, strMsg, Toast.LENGTH_SHORT).show();
	  }
	  
	  ////=======================================================================================
	  
	  public Bitmap getImage(String path) throws IOException
	    {
		  
		  Bitmap resizedBitmap =null;
          System.gc();
	        BitmapFactory.Options options=null;
	        try
	        {
	        	options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(path, options);        
	        int srcWidth = options.outWidth;
	        int srcHeight = options.outHeight;
	       // srcWidth= srcWidth/2;
	        int[] newWH =  new int[2];
	        newWH[0] = srcWidth/2;
	        newWH[1] = (newWH[0]*srcHeight)/srcWidth;
	        //srcWidth=newWH[0];
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
	        Bitmap r=Bitmap.createScaledBitmap(sampledSrcBitmap,  srcWidth, newh, true);
	        resizedBitmap = Bitmap.createBitmap(r, 0, 0,   srcWidth, newh,matrix, true);
	        resizedBitmap= toGrayscales(resizedBitmap);
	        }
	        catch(Exception e)
	  	  {
	  		  Log.d("Err : ", e.getMessage());
	  	  }
	        
	        
	        return resizedBitmap;
	    }
	  
	  public Bitmap toGrayscales(Bitmap bmpOriginal)
	  {        
		  Bitmap bmpGrayscale = null;
	      int width, height;
	      height = bmpOriginal.getHeight();
	      width = bmpOriginal.getWidth();    
try
{
	     bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	      Canvas c = new Canvas(bmpGrayscale);
	      Paint paint = new Paint();
	      ColorMatrix cm = new ColorMatrix();
	      cm.setSaturation(0);
	      ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	      paint.setColorFilter(f);
	      c.drawBitmap(bmpOriginal, 0, 0, paint);
}
catch(Exception e)
{
	Log.d("err gray ", e.getMessage());
}
	
	      return bmpGrayscale;
	  }
	  
	  ///================================================================================
	  
		 public boolean isBetween(int r,int g,int b,int r1,int g1,int b1)
	    {
	        System.out.println("MinY : "+"r= "+r+"g= "+g+"b= "+b+"r1= "+r1+"g1= "+g1+"b1= "+b1); 
	        
	     return ((r1 <= r) && (g1 <= g) && (b1 <= b) );
	    }
	}