package com.example.android.robotichand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.value;
import static android.media.CamcorderProfile.get;
import static com.example.android.robotichand.R.id.seekBar;
import static com.example.android.robotichand.R.id.textView;

/**
 * Created by Amandine on 05/12/2018.
 */

public class ListAdapter extends ArrayAdapter<Servo>{
    private Context mcontext;
    ServoViewHolder viewHolder;
    SeekBar seekBar;
    int progress = 0;
    ArrayList<Servo> servo;

    private static final String LOGTAG = "SeekBarDemo";

    ListAdapter(Context context, ArrayList<Servo> servo){
        super(context, 0, servo);
        this.mcontext = context;
        this.servo = servo;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

       /* if (row == null){
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_servomotors, parent, false);
            //row = LayoutInflater.from(getContext()).inflate(R.layout.list_servomotors,parent, false);
        }
*/

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_servomotors, parent, false);

            viewHolder = new ServoViewHolder();

            viewHolder.nameTextView = (TextView) row.findViewById(R.id.servo_name);
            viewHolder.graduation_end = (TextView) row.findViewById(R.id.graduation_end);
            //viewHolder.progressTextView = (TextView) row.findViewById(R.id.textView);
            seekBar = (SeekBar) row.findViewById(R.id.seekBar);
            seekBar.setTag(position);

            row.setTag(viewHolder);
        }

         viewHolder = (ServoViewHolder) row.getTag();

        final Servo currentServo = getItem(position);

        viewHolder.nameTextView.setText(currentServo.getName());
        seekBar.setProgress(currentServo.getProgress());
        seekBar.setMax(currentServo.getMax());

        viewHolder.graduation_end.setText("" + currentServo.getMax());

        //viewHolder.progressTextView.setText("Progress: " + seekBar.getProgress() + "/" + seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                //viewHolder.progressTextView.setText("Progress: " + progressValue + "/" + seekBar.getMax());
                Log.i(LOGTAG, "Changing seekbar's progress");
               // Toast.makeText(mcontext, "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            // Notification that the user has started a touch gesture.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(LOGTAG, "Started tracking seekbar");
                //Toast.makeText(mcontext, "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //viewHolder.progressTextView.setText("Progress: " + progress + "/" + seekBar.getMax());
                Log.i(LOGTAG, "Stopped tracking seekbar");
                currentServo.setProgress(progress);
                Toast.makeText(mcontext, "Angle: " + progress, Toast.LENGTH_SHORT).show();

            }
        });

        return row;
    }

    private class ServoViewHolder{
        public TextView nameTextView;
        public TextView graduation_end;
        //public TextView progressTextView;
    }

}
