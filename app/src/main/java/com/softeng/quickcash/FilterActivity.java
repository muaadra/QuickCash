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

        createRecyclerView(TaskTypes.getTaskTypes());

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

        loadAndApplyUserFilterPreferences();
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
     * creates a RecyclerView view for main task posts
     * @param posts list of posts
     */
    public void createRecyclerView(String[] posts) {
        RecyclerView recyclerView = findViewById(R.id.FilterRV);

        // using a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter mAdapter = new FilterRVListAdapter(posts,
                UserStatusData.getUserFilterPrefs(this));

        recyclerView.setAdapter(mAdapter);
    }

    /**
     * runs when Apply button is clicked
     */
    public void applyFilterOnButtonClick(View v){

        UserStatusData.saveUserFilterPrefsData(getUserPreferences(),this);
        goToMain();
    }

    private FilterPreferences getUserPreferences() {
        FilterPreferences prefs = new FilterPreferences();
        prefs.setCategories(getSelectedItems());
        prefs.setMaxDistance(seekBar.getProgress());
        if(minPayTextView.getText().toString().length() > 0){
            prefs.setMinPay(Float.parseFloat(minPayTextView.getText().toString()));
        }

        return prefs;
    }

    private ArrayList<String> getSelectedItems(){
        ArrayList<String> checkedItems = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.FilterRV);

        int itemsCount = recyclerView.getChildCount();
        for(int i = 0; i < itemsCount ; i++){
            View view = recyclerView.getLayoutManager().getChildAt(i);
            boolean selected = ((CheckBox)view.findViewById(R.id.listCheckBox)).isChecked();
            if(selected){
                checkedItems.add(TaskTypes.getTaskTypes()[i]);
            }
        }
        return checkedItems;
    }


    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void loadAndApplyUserFilterPreferences() {
        FilterPreferences filterPrefs = UserStatusData.getUserFilterPrefs(this);
        if(filterPrefs == null){
            return;
        }
        minPayTextView.setText(filterPrefs.minPay + "");
        textView.setText(filterPrefs.maxDistance + " km");
        seekBar.setProgress(filterPrefs.maxDistance);

    }

    /**
     * runs when cancel button is clicked
     */
    public void cancel(View v){
        finish();
    }


}
