package com.example.android.robotichand;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityAngles extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView textView;
    private Button buttonDecrease;
    private Button buttonIncrease;

    private static int DELTA_VALUE = 5;

    private static final String LOGTAG = "SeekBarDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angles);

        this.seekBar = (SeekBar) findViewById(R.id.seekBar );
        this.textView = (TextView) findViewById(R.id.textView);


        this.buttonDecrease= (Button) findViewById(R.id.button_decrease);
        this.buttonIncrease= (Button) findViewById(R.id.button_increase);


        this.textView.setText("Progress: " + seekBar.getProgress() + "/" + seekBar.getMax());
        //
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

                // When Progress value changed.
        @Override
        public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
            progress = progressValue;
            textView.setText("Progress: " + progressValue + "/" + seekBar.getMax());
            Log.i(LOGTAG, "Changing seekbar's progress");
            Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
        }

        // Notification that the user has started a touch gesture.
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.i(LOGTAG, "Started tracking seekbar");
            Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
        }

        // Notification that the user has finished a touch gesture
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            textView.setText("Progress: " + progress + "/" + seekBar.getMax());
            Log.i(LOGTAG, "Stopped tracking seekbar");
            Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();

        }
    });

      this.buttonDecrease.setOnClickListener(new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            decreateProgress();
        }
    });

      this.buttonIncrease.setOnClickListener(new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            increateProgress();
        }
    });

}

    private void decreateProgress()  {
        int progress= this.seekBar.getProgress();
        if(progress - DELTA_VALUE < 0)  {
            this.seekBar.setProgress(0);
        } else  {
            this.seekBar.setProgress(progress - DELTA_VALUE);
        }
        textView.setText("Progress: " + seekBar.getProgress()+ "/" + seekBar.getMax());
    }

    private void increateProgress()  {
        int progress= this.seekBar.getProgress();
        if(progress + DELTA_VALUE > this.seekBar.getMax())  {
            this.seekBar.setProgress(0);
        }else {
            this.seekBar.setProgress(progress + DELTA_VALUE);
        }
        textView.setText("Progress: " + seekBar.getProgress()+ "/" + seekBar.getMax());
    }
}
