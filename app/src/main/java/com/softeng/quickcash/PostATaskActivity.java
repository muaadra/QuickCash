package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Calendar;

public class PostATaskActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    String[] taskTypes = {"Task1", "Task2","Task3","Task4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_a_task);
        spinnerSetup();
    }


    /**
     * runs when calendar button is clicked
     */
    public void openCalenderOnClickButton(View view) {
        DialogFragment datePicker = new DatePicker();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    public boolean isStringEmpty(String text){
        if(text == null || text.equals("")){
            return true;
        }
        return false;
    }

    void spinnerSetup() {
        Spinner spinner = (Spinner) findViewById(R.id.tasksTypeSpinner_PostATask);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, taskTypes);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.YEAR,year);
        calendar.set(calendar.MONTH,month);
        calendar.set(calendar.DAY_OF_MONTH,dayOfMonth);

        String date = DateFormat.getDateInstance().format(calendar.getTime());
        ((Button)findViewById(R.id.datePickerPostAtask)).setText(date);
    }
}