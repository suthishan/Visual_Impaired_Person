package person.impaired.visual.app.com.visual_impaired_person.ocr.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import person.impaired.visual.app.com.visual_impaired_person.R;
import person.impaired.visual.app.com.visual_impaired_person.image.comp.TTSManager;


public class MainActivity extends AppCompatActivity {

    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;
    private Button copyButton;
    private Button mailTextButton;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

    TTSManager txtSpeech = null;

    private TextToSpeech tts;
    private static int TTS_DATA_CHECK = 1;
    private boolean isTTSInitialized = false;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue = (TextView)findViewById(R.id.text_value);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        this.context = getApplicationContext();
        txtSpeech = new TTSManager(context);
        txtSpeech.initOrInstallTTS();


        Button readTextButton = (Button) findViewById(R.id.read_text_button);
        readTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch Ocr capture activity.
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

        copyButton = (Button) findViewById(R.id.copy_text_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard =
                            (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(textValue.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard =
                            (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", textValue.getText().toString());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getApplicationContext(), R.string.clipboard_copy_successful_message, Toast.LENGTH_SHORT).show();
            }
        });

        mailTextButton = (Button) findViewById(R.id.mail_text_button);
        mailTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_SUBJECT, "Text Read");
                i.putExtra(Intent.EXTRA_TEXT, textValue.getText().toString());
                try {
                    startActivity(Intent.createChooser(i, getString(R.string.mail_intent_chooser_text)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            R.string.no_email_client_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textValue.getText().toString().isEmpty()) {
            copyButton.setVisibility(View.GONE);
            mailTextButton.setVisibility(View.GONE);
        } else {
            copyButton.setVisibility(View.VISIBLE);
            mailTextButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(text);
                    try {
                        txtSpeech.speak(text);

                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
