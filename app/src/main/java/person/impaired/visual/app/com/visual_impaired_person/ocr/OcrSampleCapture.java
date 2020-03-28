package person.impaired.visual.app.com.visual_impaired_person.ocr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.CommonStatusCodes;

import person.impaired.visual.app.com.visual_impaired_person.R;
import person.impaired.visual.app.com.visual_impaired_person.ocr.activities.OcrCaptureActivity;

public class OcrSampleCapture extends AppCompatActivity {
    private static final int RC_OCR_CAPTURE = 9003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_sample_capture);

        Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
        intent.putExtra(OcrCaptureActivity.AutoFocus, true);
        intent.putExtra(OcrCaptureActivity.UseFlash, true);

        startActivityForResult(intent, RC_OCR_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                   // String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                      try {
                          Log.d("ocr data", "Text read: " + data.toString());

                          Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("ocr err", "Text read: " );
                } else {

                    Log.d("ocr err", "No Text captured, intent data is null");
                }
            } else {
                        CommonStatusCodes.getStatusCodeString(resultCode);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
