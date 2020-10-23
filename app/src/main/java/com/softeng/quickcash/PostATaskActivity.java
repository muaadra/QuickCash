package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.util.Calendar;

public class PostATaskActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_a_task);
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