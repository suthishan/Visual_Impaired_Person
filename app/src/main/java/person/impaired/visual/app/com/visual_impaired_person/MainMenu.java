package person.impaired.visual.app.com.visual_impaired_person;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import person.impaired.visual.app.com.visual_impaired_person.ocr.activities.MainActivity;

public class MainMenu extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
    public void OCR(View v)
    {
        Intent i = new Intent(MainMenu.this, MainActivity.class);
        startActivity(i);
    }
}