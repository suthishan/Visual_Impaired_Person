package person.impaired.visual.app.com.visual_impaired_person;

import android.content.Context;

public class CameraHelper {
	protected Context mContext;
	protected FrameDetector mCallback;
	
	/**
	 * Class constructor
	 * @param context
	 */
	public CameraHelper(Context context) {
		mContext = context;
	}
	
	/**
	 * Set sensor callback
	 * @param camRevoker
	 */
	public void setSensorCallback(CamRevoker camRevoker) {
		mCallback = (FrameDetector) camRevoker;
	}
	
	/**
	 * On activity resume
	 */
	public void onResume() {
		
	}
	
	/**
	 * On activity pause
	 */
	public void onPause() {
	
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
}
