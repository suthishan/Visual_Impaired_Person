package person.impaired.visual.app.com.visual_impaired_person;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.io.File;

import person.impaired.visual.app.com.visual_impaired_person.image.comp.ImagePixelazation;

public class HomeMenu extends AppCompatActivity
{
    LinearLayout layout_header;
    LinearLayout layout_board;
    LinearLayout layout_footer;
    Object objtxt="Object Detect";
    Context mContext;
    Display display;
    float width, height,leftFactor, topFactor;
    private Vibrator vibrator;

    TextView tvdetect;
    @SuppressWarnings("unused")
    private int currentlyTouched = -1;



    String strAuto="";
    private OnClickListener onDoubleClickListener;

    @SuppressWarnings("unused")
    private TimePicker timePicker1;


    @SuppressWarnings("unused")
    private int hour;
    @SuppressWarnings("unused")
    private int minute;

    static final int TIME_DIALOG_ID = 999;
    SessionMode sess;

    private TextToSpeech tts;
    private static int TTS_DATA_CHECK = 1;
    private boolean isTTSInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        layout_header = (LinearLayout) findViewById(R.id.home_root);

        mContext=getApplicationContext();
        sess=new SessionMode(getBaseContext());
       // setResizerTrainerImage();

        layout_header.setOnTouchListener(
                new LinearLayout.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent m) {
                        if(v.getId()==R.id.foot1 )
                        {
                           // setFileProcess();
                            Intent nextScreen = new Intent(HomeMenu.this, ProjectSettings.class);
                            startActivity(nextScreen);
                        }
                        else
                        {
                            handleTouch(m);
                        }
                        return true;
                    }


                }
        );


    }


    void handleTouch(MotionEvent m)
    {
        @SuppressWarnings("unused")
        int pointerCount = m.getPointerCount();

        int action = m.getActionMasked();
        @SuppressWarnings("unused")
        int actionIndex = m.getActionIndex();
        @SuppressWarnings("unused")
        String actionString;


        switch (action)
        {

            case MotionEvent.ACTION_DOWN:
                //	actionString = "DOWN";
                onTrackingProcess();
                break;
            case MotionEvent.ACTION_UP:
                //actionString = "UP";
                onTrackingProcess();
                //onTrackingProcess
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                actionString = "PNTR DOWN";
                onTrackingProcess();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                actionString = "PNTR UP";

                break;
            case MotionEvent.ACTION_MOVE:

                actionString = "MOVE";
                break;


            default:
                actionString = "";
        }

        //String touchStatus = "Action: " + actionString + " Index: " + actionIndex + " ID: " + id + " X: " + x + " Y: " + y;

    }

    public void onTrackingProcess()
    {

        sess.createCompress("off");
        Intent nextScreen = new Intent(HomeMenu.this, FrameDetection.class);

        startActivity(nextScreen);
    }

    public void onMotionProcess(View v)
    {
        sess.createCompress("off");


        Intent nextScreen = new Intent(HomeMenu.this, FrameDetection.class);

        startActivity(nextScreen);
    }


    public void callonSetting(View v)
    {

       // setFileProcess();
        Intent nextScreen = new Intent(HomeMenu.this, ProjectSettings.class);

        startActivity(nextScreen);
    }


    public void setResizerTrainerImage()
    {
      //  Toast.makeText(mContext," going to Training image dir : ", 100).show();
       ImagePixelazation objimgtrain=new ImagePixelazation(mContext);
       objimgtrain.getResizeInputTrainImage();
    }


    public void setFileProcess()
    {
        try
        {
            String IMAGE_DIRECTORY_Train = "ObjectTracking/TrainObject";
            File filetrainrawdir = null;

            filetrainrawdir = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_Train);

            if(!filetrainrawdir.exists())
            {
                filetrainrawdir.mkdirs();
            }


            ///-----------------------------------------------------------


            String	pathresizer ="ObjectTracking/TrainObjectRezier";

            File fileResizer = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    pathresizer);

            if(!fileResizer.exists())
            {
                fileResizer.mkdirs();
            }
            ///================================================================================


            String IMAGE_DIRECTORY_TEST = "ObjectTracking/LiveTrack";
            //String IMAGE_DIRECTORY_CLIENT = sessUser.getSharedClient();

            File[] listFile;
            File filetestrawdir = null;

            filetestrawdir = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_TEST);

            if(!filetestrawdir.exists())
            {
                filetestrawdir.mkdirs();
            }

            ///===================================================================================

            String	testpathresizer ="ObjectTracking/LiveTrackRezier";

            File	filetestresize = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    testpathresizer);

            if(!filetestresize.exists())
            {
                filetestresize.mkdirs();
            }

            Toast.makeText(mContext," created dir : ", Toast.LENGTH_SHORT).show();

        }

        catch(Exception e)
        {
            Log.d("Err Detect : ",e.getMessage());

            //Toast.makeText(mContext,"Err create dir : "+e.getMessage(), 100).show();
        }
    }



}
