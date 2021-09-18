package com.apps.jm.seeweather;

/**
 * Created by Jatin on 8/21/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Locale;



public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener {

    private ImageView speechButton;
    private TextToSpeech engine;
    private EditText editText;
    private SeekBar seekPitch;
    private SeekBar seekSpeed;
    private double pitch=1.0;
    private double speed=1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.texttospeech_layout);

        // Get reference
        speechButton = (ImageView) findViewById(R.id.speechImg);
        editText = (EditText) findViewById(R.id.sentence);
        engine = new TextToSpeech(this, this);

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech();
            }
        });
        seekPitch = (SeekBar) findViewById(R.id.seekPitch);
        seekPitch.setThumbOffset(5);
        seekPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Log.d("Speech", "Progress ["+progress+"]");
                pitch = (float) progress / (seekBar.getMax() / 2);
                //Log.d("Speech", "Pitch ["+pitch+"]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekSpeed = (SeekBar) findViewById(R.id.seekSpeed);
        seekSpeed.setThumbOffset(5);
        seekSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Log.d("Speech", "Progress ["+progress+"]");
                speed = (float) progress / (seekBar.getMax() / 2);
                //Log.d("Speech", "Pitch ["+pitch+"]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onInit(int status) {
        Log.d("Speech", "OnInit - Status ["+status+"]");

        if (status == TextToSpeech.SUCCESS) {
            Log.d("Speech", "Success!");
            engine.setLanguage(Locale.UK);
        }
    }

    private void speech() {
        engine.setPitch((float) pitch);
        engine.setSpeechRate((float) speed);
        engine.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void spealWeather(String weather){
        engine.setPitch((float) 1);
        engine.setSpeechRate((float) 1);
        engine.speak(weather, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}