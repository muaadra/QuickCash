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
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

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
        if(emptyListTextViewOriginalHeight == -1){
            emptyListTextViewOriginalHeight = emptyListTV.getHeight();
        }

        if(posts != null && posts.size() > 0){

            //hide message saying that the list is empty
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

    @Override
    protected void onResume() {
        super.onResume();


        final ArrayList<TaskPost> posts = new ArrayList<>();

        String userId = UserStatusData.getEmail(this).replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/TaskPosts";

        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                //loop through all children in path
                for (DataSnapshot userdata : dataFromDb.getChildren()) {
                    TaskPost taskPost = (TaskPost) userdata.getValue(TaskPost.class);
                    if(taskPost != null){
                        posts.add(taskPost);
                    }
                }
                createRecyclerView(posts);
            }
        };


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