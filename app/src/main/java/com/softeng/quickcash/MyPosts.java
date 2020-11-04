package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyPosts extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<TaskPost> taskPosts;

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

        recyclerView = (RecyclerView) findViewById(R.id.postsList_MyPosts);

        // using a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new MyAdapter(posts);
        recyclerView.setAdapter(mAdapter);

        if(posts != null && posts.size() > 0){
            //hide message saying that the list is empty
            emptyListTV.setHeight(0);
        }else {
            emptyListTV.setHeight(emptyListTextViewOriginalHeight);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromDbAndShowOnUI();
    }


    private void getDataFromDbAndShowOnUI() {
        resetToggle();
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
                    Log.d("post",posts.get(posts.size()-1).getPostId());
                }
                taskPosts = posts;

                showActivePosts();
            }
        };
    }

    private void showActivePosts() {
        ArrayList<TaskPost> activePosts = new ArrayList<>();


        for (TaskPost task: taskPosts) {
            if(!task.isPostDeleted()){
                activePosts.add(task);
            }
        }
        createRecyclerView(activePosts);
    }

    /**
     * runs when "post a task" button is clicked
     */
    public void goToPostATaskOnClickButton(View view) {
        //go to next activity
        Intent intent = new Intent(this, PostATaskActivity.class);
        startActivity(intent);
    }
    /**
     * runs when "show all posts" button is clicked
     */
    boolean toggle;
    public void showAllPostsOnClickButton(View view) {
        if(taskPosts == null){
            Toast.makeText(this,"There are no posts to show",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!toggle){
            ((Button)findViewById(R.id.deletedPosts)).setText("< back");
            ((TextView)findViewById(R.id.emptyStatusMyPosts)).setText(R.string.noDeletedPosts);
            showDeletedPosts();
        }else {
            ((Button)findViewById(R.id.deletedPosts)).setText(R.string.showDeletedPosts);
            ((TextView)findViewById(R.id.emptyStatusMyPosts)).setText(R.string.emptyStringMessage);
            showActivePosts();

        }
        toggle = !toggle;

    }

    private void resetToggle(){
        toggle = false;
        ((Button)findViewById(R.id.deletedPosts)).setText(R.string.showDeletedPosts);
    }

    private void showDeletedPosts(){
        ArrayList<TaskPost> deletedPosts = new ArrayList<>();
        for (TaskPost task: taskPosts) {
            if(task.isPostDeleted()){
                deletedPosts.add(task);
            }
        }
        createRecyclerView(deletedPosts);
    }
}