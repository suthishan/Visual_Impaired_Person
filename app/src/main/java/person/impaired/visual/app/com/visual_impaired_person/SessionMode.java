package person.impaired.visual.app.com.visual_impaired_person;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionMode {

	SharedPreferences prefs;

    Editor editors;

    // an editor is used to edit your preferences

    Context context;

    // Shared Preference file name

    private static final String PREF_NAME = "Previsual";

    // Shared Preferences Key

    public static final String KEY_COMPRESSION = "screen";
 

	
	public SessionMode(Context context) 
	{
		// TODO Auto-generated constructor stub
		   this.context = context;
		   prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		   editors = prefs.edit();
		 
 	}

	 public void setComp()
	 {
		 
		 editors.putString(KEY_COMPRESSION, "off");
	       editors.commit();
	 }
	 
	  public void createCompress(String keyScreenValue)
	    {
	        
		    editors.remove(KEY_COMPRESSION);
	        editors.putString(KEY_COMPRESSION,keyScreenValue);
	        editors.commit();
	    }

	  
	
	  public String getSharedCompress()
	    {
	    	String strCompressValue="";

	    	try
	    	{
	    		if (prefs.contains(KEY_COMPRESSION))
	    	      {
	    			strCompressValue= prefs.getString(KEY_COMPRESSION ,"");
	    			Log.i("strCompressValue: ",""+strCompressValue);
	    	      }
	    	}
	    	catch(Exception e)
	    	{
	    		Log.i("strCompressValue error",e.getMessage().toString().trim());
	    	}
	    	
	    	return strCompressValue;
	    }

	 
	  
	
	  
}
