package person.impaired.visual.app.com.visual_impaired_person.image.comp;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TTSManager
{
private TextToSpeech myTTS;
private boolean readyToSpeak = false;
private Context context;

public TTSManager(Context baseContext)
{
    this.context = baseContext;
}

public void initOrInstallTTS()
{
    myTTS = new TextToSpeech(context, new OnInitListener() 
    {               
        @Override
        public void onInit(int status) 
        {
            if (status == TextToSpeech.SUCCESS)
            {
                myTTS.setLanguage(Locale.US);
                readyToSpeak = true;
            }
            else
                installTTS();
        }
    });
}

private void installTTS()
{
    Intent installIntent = new Intent();
    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
    context.startActivity(installIntent);
}

public void speak(String text)
{       
    if (readyToSpeak)
        myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
}

}