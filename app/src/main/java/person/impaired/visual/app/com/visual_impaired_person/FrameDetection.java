package person.impaired.visual.app.com.visual_impaired_person;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.content.Context;
import android.content.pm.ActivityInfo;

import android.graphics.Bitmap;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.ImageView;

public class FrameDetection extends AppCompatActivity
{

    private static final String TAG = "Interact Object Detection";

    private static final int height = 24;

    private static final int width = 32;

    private static final int intencityThreshold = 3;

    private static final int percentDifferenceThreshold = 32;

    protected PowerManager.WakeLock mWakeLock = null;
    SessionMode sess;
    public ImageView DetectFrame = null;
    CaptureFrame mCameraMotionSensor = null;

    CaptureProperty objcapturepropert=null;



    FrameDetector mCameraMotionSensorCallback = new FrameDetector() {
        public void TrackerView(Bitmap frame) {

            DetectFrame.setImageBitmap(frame);
        }

        public void TrackedObject(int diff) {

        }

        public void TrackingFrame(int percentDifference, int percentDifferenceThreshold, int minIntensity, int maxIntensity, int intencityThreshhold, int goodFrames) {


        }

        public void TrackMsg(Message message) {}
    };

    SurfaceView surfaceView = null;


    TextView mTextViewVoiceResult = null;
    TextView mTextViewVoiceStatus = null;



    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_frame_detection);
        sess=new SessionMode(getBaseContext());
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(pm.SCREEN_DIM_WAKE_LOCK, "DoNotDimScreen");
        sess.createCompress("on");

        objcapturepropert=new CaptureProperty();
        /**
         * Initialize motion detect
         */

        DetectFrame = (ImageView) findViewById(R.id.motionPreview);
        mCameraMotionSensor = new CaptureFrame(getBaseContext(),getFrameProperty(objcapturepropert));
        mCameraMotionSensor.setSensorCallback(mCameraMotionSensorCallback);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        mCameraMotionSensor.FrameAllocate(surfaceView);



    }

    public void onPause() {
        super.onPause();

        if (mWakeLock.isHeld())
        {mWakeLock.release();
        }


        if (null != mCameraMotionSensor)
        {mCameraMotionSensor.onPause();
            Log.d("call Main ", "pause");
            System.out.println("call Main "+"pause");
        }


    }

    public void onResume() {
        super.onResume();

        if (null != mCameraMotionSensor)
        {
            Log.d("call Main ", "resume");
            System.out.println("call Main "+"resume");
            mCameraMotionSensor.onResume();
            Log.d("call Main ", "resume1");
            System.out.println("call Main "+"resume1");
        }


        mWakeLock.acquire();
    }


    public CaptureProperty getFrameProperty(CaptureProperty objcapturepropert )
    {
        objcapturepropert.setHeight(height);
        objcapturepropert.setWidth(width);
        objcapturepropert.setIntencityThreshold(intencityThreshold);
        objcapturepropert.setPercentDifferenceThreshold(percentDifferenceThreshold);

        return objcapturepropert;
    }


}

