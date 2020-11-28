package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class represents the "My posts" activity, it displays all user's posts
 * (i.e active and deleted posts) into a toggleable Recycler view
 *
 * @author Muaad Alrawhani
 */
public class MyPosts extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    int emptyListTextViewOriginalHeight = -1; // to store original height of the TextView
    ArrayList<TaskPost> taskPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
    }


    @Override
    protected void onResume() {
        super.onResume();

        getDataFromDbAndShowOnUI();
    }

    /**
     * creates the Recycler view for all my task posts
     * @param posts list of all my posts
     */
    public void createRecyclerView(ArrayList<TaskPost> posts) {

        //a message shows/expands in the recycleView if it's empty and collapses otherwise
        TextView emptyListTV = (TextView)findViewById(R.id.emptyStatusMyPosts);
        if(emptyListTextViewOriginalHeight == -1){
            emptyListTextViewOriginalHeight = emptyListTV.getHeight();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.TaskPostsList);

        // using a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        RecyclerView.Adapter mAdapter = new MyPostsAdapter(posts,db);
        recyclerView.setAdapter(mAdapter);

        if(posts != null && posts.size() > 0){
            //hide message that says the list is empty
            emptyListTV.setHeight(0);
        }else {
            emptyListTV.setHeight(emptyListTextViewOriginalHeight);
        }

    }

    private void getDataFromDbAndShowOnUI() {
        // to toggle between the "deleted posts" and active posts button
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
     * runs when "show deleted posts" button is clicked
     */
    boolean toggle;
    public void showDeletedPostsOnClickButton(View view) {
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