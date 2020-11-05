package com.softeng.quickcash;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Calendar expectedDateFrom, expectedDateTo;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    SeekBar seekBar;
    TextView textView;
    EditText priceMinText, priceMaxText;
    Button btn_apply, btn_cancel;
    ToggleButton toggleButton1, toggleButton2, toggleButton3, toggleButton4;
    HashMap<String, Object> params = new HashMap<String, Object>();
    int date_type;
    private MyLocation myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        expectedDateFrom = Calendar.getInstance();
        expectedDateTo = Calendar.getInstance();
        toggleButton1 = (ToggleButton) findViewById(R.id.btn_categories_1);
        toggleButton2 = (ToggleButton) findViewById(R.id.btn_categories_2);
        toggleButton3 = (ToggleButton) findViewById(R.id.btn_categories_3);
        toggleButton4 = (ToggleButton) findViewById(R.id.btn_categories_4);
        priceMinText = (EditText) findViewById(R.id.price_minText);
        priceMaxText = (EditText) findViewById(R.id.price_maxText);
        textView = (TextView) findViewById(R.id.distance_text);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(1000);
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
        myLocation = new MyLocation(this) {
            @Override
            public void LocationResult(final Location location) {
                btn_cancel = (Button) findViewById(R.id.btn_filter_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seekBar.setProgress(0);
                        textView.setText("0Km");
                        toggleButton1.setChecked(false);
                        toggleButton2.setChecked(false);
                        toggleButton3.setChecked(false);
                        toggleButton4.setChecked(false);
                        priceMinText.setText("");
                        priceMaxText.setText("");
                        ((Button) findViewById(R.id.datePickerPostAtask)).setText("Pick A date");
                        ((Button) findViewById(R.id.datePickerPostAtask2)).setText("Pick A date");
                        expectedDateFrom = Calendar.getInstance();
                        expectedDateTo = Calendar.getInstance();

                    }
                });
                btn_apply = (Button) findViewById(R.id.btn_filter_apply);

                btn_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashSet<String> categories = new HashSet<>();
                        ArrayList<Integer> price = new ArrayList<>();
                        ArrayList<Long> date = new ArrayList<>();
                        Button dateFrom = (Button) findViewById(R.id.datePickerPostAtask);
                        Button dateTo = (Button) findViewById(R.id.datePickerPostAtask);
                        if (toggleButton1.isChecked()) {
                            categories.add(toggleButton1.getText().toString().toLowerCase());
                        }
                        if (toggleButton2.isChecked()) {
                            categories.add(toggleButton2.getText().toString().toLowerCase());
                        }
                        if (toggleButton3.isChecked()) {
                            categories.add(toggleButton3.getText().toString().toLowerCase());
                        }
                        if (toggleButton4.isChecked()) {
                            categories.add(toggleButton4.getText().toString().toLowerCase());
                        }
                        if (categories.size() > 0) {
                            params.put("categories", categories);
                        }
                        if (!priceMinText.getText().toString().equals("")) {
                            price.add(Integer.parseInt(priceMinText.getText().toString()));
                        }
                        if (!priceMaxText.getText().toString().equals("")) {
                            price.add(Integer.parseInt(priceMaxText.getText().toString()));
                        }
                        if (price.size() > 0) {
                            params.put("price", price);
                        }
                        if (!dateFrom.getText().toString().equals("Pick A date")) {
                            date.add(expectedDateFrom.getTimeInMillis());
                        }
                        if (!dateTo.getText().toString().equals("Pick A date")) {
                            date.add(expectedDateTo.getTimeInMillis());
                        }
                        if (date.size() > 0) {
                            params.put("expected", date);
                        }

                        AllTaskList<TaskList> taskLists = new AllTaskList<TaskList>("/users", db) {
                            @Override
                            public void getReturnedDbData(TaskList taskList) {
                                filterList(taskList, location);
                            }
                        };
                    }
                });
            }
        };




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

            if (params.get("categories") != null && !((HashSet<String>) params.get("categories")).contains(allList.getTaskPosts().get(i).getTaskTitle())) {
                    isTaskTitle = false;
            }
            if (params.get("price") != null) {
                Float taskCost = Float.valueOf(allList.getTaskPosts().get(i).getTaskCost());
                if (((ArrayList<Integer>) params.get("price")).get(0) > taskCost || ((ArrayList<Integer>) params.get("price")).get(1) < taskCost) {
                    isTaskCost = false;
                }
            }
            if (params.get("expected") != null) {
                long time = allList.getTaskPosts().get(i).getExpectedDate().getTime();
                if (((ArrayList<Long>) params.get("expected")).get(0) > time || ((ArrayList<Long>) params.get("expected")).get(1) < time) {
                    isTaskCost = false;
                }
            }
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
        // --
        //  It need to implement return main lists view.
        //
    }

    /**
     * runs when calendar button is clicked
     */
    public void openCalenderOnClickButton(View view) {
        DatePicker datePicker = new DatePicker();
        datePicker.show(getSupportFragmentManager(), "date picker");
        switch (view.getId()) {
            case R.id.datePickerPostAtask:
                date_type = 1;
                break;
            case R.id.datePickerPostAtask2:
                date_type = 2;
                break;
        }
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        if (date_type == 1) {
            expectedDateFrom = Calendar.getInstance();
            expectedDateFrom.set(expectedDateFrom.YEAR, year);
            expectedDateFrom.set(expectedDateFrom.MONTH, month);
            expectedDateFrom.set(expectedDateFrom.DAY_OF_MONTH, dayOfMonth);
            ((Button) findViewById(R.id.datePickerPostAtask)).setText(DateFormat.getDateInstance().format(expectedDateFrom.getTime()));
        } else {
            expectedDateTo = Calendar.getInstance();
            expectedDateTo.set(expectedDateTo.YEAR, year);
            expectedDateTo.set(expectedDateTo.MONTH, month);
            expectedDateTo.set(expectedDateTo.DAY_OF_MONTH, dayOfMonth);
            ((Button) findViewById(R.id.datePickerPostAtask2)).setText(DateFormat.getDateInstance().format(expectedDateTo.getTime()));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "This app requires permissions to get Location data"
                    , Toast.LENGTH_SHORT).show();
        }
    }

}
