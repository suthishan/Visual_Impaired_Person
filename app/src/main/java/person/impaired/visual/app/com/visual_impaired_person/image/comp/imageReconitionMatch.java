package person.impaired.visual.app.com.visual_impaired_person.image.comp;

import android.graphics.Bitmap;

public class imageReconitionMatch
{

	public imageReconitionMatch() {
		// TODO Auto-generated constructor stub
	}
	public  int CompareBitMap(Bitmap train,Bitmap test){
		
		int width = test.getWidth();
		int height = test.getHeight();
		
		int MatchCount=0;
		int UnMatchCount=0;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (train.getPixel(j, i)-test.getPixel(j, i)<=5) {
					MatchCount++;	
				}
				else
				{
					UnMatchCount++;
				}
			}
			
		}
		
		
		return UnMatchCount;
	}
	
	
	public void compareMethode(Bitmap bitmap,Bitmap bitmpa2)
	{
		int Counter=0;
		for (int i = 0; i < bitmap.getWidth(); i++) {
	        for (int j = 0; j < bitmap.getHeight(); j++) {
	        	
	        	

	            for (int k = 0 ; k<bitmpa2.getWidth(); k++) 
	            {
	                               for (int l = 0 ; l<bitmpa2.getHeight(); l++) 
	                               {
	                            	   
	                            	   int Trainmatch=bitmap.getPixel(i, j);
	                            	   int TestMatch=bitmpa2.getPixel(i, j);
	                            	   
	                            	   int Distance=Trainmatch-TestMatch;

	                            if(Distance>10  )
	                            {
	                              Counter++ ;       
	                            }
	              }   
	           }
	        }
	    }
	}
	
}
