package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.ArrayList;

public class MyPosts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
    }

    /**
     * runs when "post a task" button is clicked
     */
    public void goToPostATaskOnClickButton(View view) {
        //go to next activity
        Intent intent = new Intent(this, PostATaskActivity.class);
        startActivity(intent);
    }



}