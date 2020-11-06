package com.softeng.quickcash;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class FilterActivity extends AppCompatActivity  {
    SeekBar seekBar;
    TextView textView;
    EditText minPayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        minPayTextView = (EditText) findViewById(R.id.minPayTV);
        textView = (TextView) findViewById(R.id.distance_text);


        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(MainActivity.MAX_LOCAL_DISTANCE/1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                textView.setText(progress + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }


    /**
     * implement filter option.
     *
     */
    public void filterList(TaskList allList, Location location) {

        int taskSize = allList.getTaskPosts().size();
        ArrayList <Integer> removeItem = new ArrayList<>();
        for (int i = 0; i < taskSize ; i++) {
            boolean isTaskTitle = true;
            boolean isTaskCost = true;
            boolean isExpectedDate = true;
            boolean isDistance = true;


            if (seekBar.getProgress() > 0  && seekBar.getProgress() < 1000) {
                MyLocation userLocation = new MyLocation(this) {
                    @Override
                    public void LocationResult(Location location) {

                    }
                };
                LongLatLocation tempLocation = new LongLatLocation(location.getLatitude(), location.getLatitude());
                userLocation.setLastLocation(tempLocation);
                String[] taskLocation = allList.getTaskPosts().get(i).getLatLonLocation().split(",");
                LongLatLocation location1 = new LongLatLocation(Double.parseDouble(taskLocation[0]),Double.parseDouble(taskLocation[1]));
                float distance = userLocation.calcDistanceToLocation(location1);
                if (Math.abs(distance) >= seekBar.getProgress() * 1000) {
                    isDistance = false;
                }
            }
            if ((!isTaskTitle || !isTaskCost || !isExpectedDate || !isDistance)) {
                removeItem.add(i);
            }

        }
        Collections.reverse(removeItem);
        for(int i : removeItem) {
            allList.getTaskPosts().remove(i);
        }
    }



    /**
     * runs when Apply button is clicked
     */
    public void applyFilterOnButtonClick(View v){
        goToMain();
    }




    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    /**
     * runs when cancel button is clicked
     */
    public void cancel(View v){
        finish();
    }


}
