package person.impaired.visual.app.com.visual_impaired_person.image.comp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class TrainSetImageProcess {

	public TrainSetImageProcess() {
		// TODO Auto-generated constructor stub
	}
	
	public  String getImageBitmapPath(Bitmap objTestImg)
	{
		
		
		List objlst=getTrainSdcardImages();
		String strPathDir="";
		 TreeMap map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
		File path=null;
		
		  for (int i = 0; i < objlst.size(); i++)
			{
			 strPathDir=objlst.get(i).toString().trim();
			 path=new File(strPathDir);
			 Bitmap objTrainimg= getImageBitmap(strPathDir);
			 
			 imageReconitionMatch obj =new imageReconitionMatch();
			 
		     int distance= obj.CompareBitMap(objTrainimg,objTestImg);
		     map.put(distance,path.getName());
		     Log.d(path.getName(),""+distance);
			
			}
		  
		  TreeMap DistanceMin = new TreeMap(map.comparator());
		  DistanceMin.putAll(map);
		  
		  Set set = map.entrySet(); 
		// Get an iterator 
		Iterator i = set.iterator(); 
		// Display elements 
		
		int count=0;
		
		String strKey="";
				String strValue="";
		
		while(i.hasNext()) { 
			
			Map.Entry me = (Map.Entry)i.next(); 
			if(count==0)
			{
				strKey=me.getKey().toString().trim();
				strValue=me.getValue().toString().trim();
			}
			count++;
		System.out.print(me.getKey() + ": "); 
		System.out.println(me.getValue()); 
		} 
		    // Iterate through TreeMap entries
		    System.out.println("TreeMap entries : ");
		    StringBuffer strMin=new StringBuffer();
		  
		    	strMin.append("matched Object is "+strValue+"  With Distance is "+strKey);
		    	

	
		
		 return strMin.toString();
	}
	
	
	
	 public  List getTrainSdcardImages()
		{
         List imageUrls=new ArrayList();
			
			File[] listFile;
			String	pathresizer ="Blind/TrainRezier";

			File fileResizer = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					pathresizer);


			imageUrls.clear();
			
			//file = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
			if (fileResizer.isDirectory()) {
				listFile = fileResizer.listFiles();

				for (int i = 0; i < listFile.length; i++)
				{
					imageUrls.add(listFile[i].getAbsolutePath());
				}

			}
			
			return imageUrls;
		}
	 
	 
	 
	 public  Bitmap getImageBitmap(String filepath)
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
				   Log.d(" bitmap from file err: ",e.getMessage());
			   }
		  
		  return bm;
	  }

}
