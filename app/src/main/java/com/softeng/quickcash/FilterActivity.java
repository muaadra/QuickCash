package com.softeng.quickcash;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                textView.setText(progress + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_cancel = (Button) findViewById(R.id.btn_filter_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(0);
                textView.setText("0m");
                toggleButton1.setChecked(false);
                toggleButton2.setChecked(false);
                toggleButton3.setChecked(false);
                toggleButton4.setChecked(false);
                priceMinText.setText("");
                priceMaxText.setText("");
            }
        });
        btn_apply = (Button) findViewById(R.id.btn_filter_apply);

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashSet<String> categories = new HashSet<>();
                int[] price = new int[2];
                long[] date = new long[2];
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
                    price[0] = Integer.parseInt(priceMinText.getText().toString());
                }
                if (!priceMaxText.getText().toString().equals("")) {
                    price[1] = Integer.parseInt(priceMaxText.getText().toString());
                }
                if (price.length > 0) {
                    params.put("price", price);
                }
                if (expectedDateFrom != null) {
                    date[0] = expectedDateFrom.getTimeInMillis();
                }
                if (expectedDateTo != null) {
                    date[1] = expectedDateFrom.getTimeInMillis();
                }
                    params.put("expected", date);
                AllTaskList<TaskList> taskLists = new AllTaskList<TaskList>("/users", db) {
                    @Override
                    public void getReturnedDbData(TaskList taskList) {
                        filterList(taskList);
                    }
                };
            }
        });

    }

    public void filterList(TaskList allList) {
        for (int i = 0; i < allList.getTaskPosts().size(); i++) {
            boolean isTaskTitle = false;
            boolean isTaskCost = false;
            boolean isExpectedDate = false;
            if (params.get("categories") != null) {
                for (String category : (HashSet<String>) params.get("categories")) {
                    if (category.equals(allList.getTaskPosts().get(i).getTaskTitle())) {
                        isTaskTitle = true;
                        break;
                    }
                }
            }
            if (params.get("price") != null) {
                int taskCost = Integer.parseInt(allList.getTaskPosts().get(i).getTaskCost());
                if (((int []) params.get("price"))[0]<= taskCost &&
                        ((int []) params.get("price"))[1] >= taskCost) {
                    isTaskCost = true;
                }
            }
            if (params.get("expected") != null) {
                long time = allList.getTaskPosts().get(i).getExpectedDate().getTime();
                if (((long []) params.get("expected"))[0] <= time && time <= ((long []) params.get("expected"))[1]) {
                    isExpectedDate = true;
                }
            }
            if (!isTaskTitle && !isTaskCost && !isExpectedDate) {
                allList.getTaskPosts().remove(i);
            }
        }
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
