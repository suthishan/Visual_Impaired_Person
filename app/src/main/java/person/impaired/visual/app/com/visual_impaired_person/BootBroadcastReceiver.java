
package person.impaired.visual.app.com.visual_impaired_person;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver implements SensorEventListener{
Context context=null;
SensorManager sm;
SensorEvent event;
private static long lastTime;
private static Sensor accelerometer;
	@Override
	public void onReceive(Context context, Intent i) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		boolean start = sp.getBoolean(context.getResources().getString(R.string.pref_start_on_boot_key), false);
		Log.v("SBBBR", "Boot completed event, starting service: " + start);
		this.context=context;
		//sm = (SensorManager) getSystemService(context);
		
		lastTime = System.currentTimeMillis();
		
		getAccelerometer(event);
		if (start) {
	        //detect the shake and do your work here
	   
			Toast.makeText(context, "Boot completed event, starting service: " + start, Toast.LENGTH_SHORT).show();
			
			Intent nextScreen = new Intent(context, FrameDetection.class);
	    	
			context.startActivity(nextScreen);
			//Intent serviceIntent = new Intent(context, BackgroundService.class);
			//context.startService(serviceIntent);
		}
		if (Intent.EXTRA_KEY_EVENT.equals(i.getAction())) {
			
			Toast.makeText(context, "key"+i.getAction().toString(), Toast.LENGTH_SHORT);
			
		}
		
		//sm.registerListener(this, sm.getDefaultSensor
			//	(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		if (event.sensor.TYPE_ACCELEROMETER== Sensor.TYPE_ACCELEROMETER) {
	        //detect the shake and do your work here
	   
					
			Intent nextScreen = new Intent(context, FrameDetection.class);
	    	
			context.startActivity(nextScreen);
			//Intent serviceIntent = new Intent(context, BackgroundService.class);
			//context.startService(serviceIntent);
		}
	}

private static void getAccelerometer(SensorEvent event) {
	

	//sm = (SensorManager) getSystemService(context.SENSOR_SERVICE);

	/* accelerometer = SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	 accelerometer.registerListener(context, accelerometer.getDefaultSensor
				(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		float[] value = event.values;
		
		float x = value[0];
		float y = value[1];
		float z = value[2];
		
		float accelationSquareRoot = (x*x + y*y + z*z) 
				/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
		
		long actualTime = System.currentTimeMillis();
		
		if(accelationSquareRoot >= 2) {
			
			if(actualTime-lastTime < 200) {
				
				return;
			}
			
			lastTime = actualTime;
			
			// Perform your Action Here..
			
			Intent i = new Intent(MainActivity.this, NextPage.class);
			startActivity(i);
			
			
			
		}*/
	}









}
