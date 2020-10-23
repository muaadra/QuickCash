package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyPosts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
    }

    int emptyListTextViewOriginalHeight = -1;
    public void createRecyclerView(ArrayList<TaskPost> posts) {
        TextView emptyListTV = (TextView)findViewById(R.id.emptyStatusMyPosts);

        if(posts.size() > 0){

            //hide message saying that the list is empty
            if(emptyListTextViewOriginalHeight == -1){
                emptyListTextViewOriginalHeight = emptyListTV.getHeight();
            }
            emptyListTV.setHeight(0);

            ((TextView)findViewById(R.id.emptyStatusMyPosts)).setText("");
            recyclerView = (RecyclerView) findViewById(R.id.postsList_MyPosts);

            // using a linear layout manager
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);


            mAdapter = new MyAdapter(posts);
            recyclerView.setAdapter(mAdapter);
        }else {
            emptyListTV.setHeight(emptyListTextViewOriginalHeight);
        }

    }





}