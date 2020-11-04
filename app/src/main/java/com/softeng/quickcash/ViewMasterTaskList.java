package com.softeng.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewMasterTaskList extends  AppCompatActivity{



        final FirebaseDatabase db = FirebaseDatabase.getInstance();

        private RecyclerView recyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager layoutManager;

        ArrayList<TaskPost> taskPosts;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_master_task_list);
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
            final ArrayList<String> uEmail = new ArrayList<String>();
            final ArrayList<TaskPost> posts = new ArrayList<>();
            String path = "users/";
            new DbRead<DataSnapshot>(path,DataSnapshot.class, db){
                // haev to add email to container.
                @Override
                public void getReturnedDbData(DataSnapshot dataFromDb) {
                    for(DataSnapshot users : dataFromDb.getChildren()){
                        final String uEmail = users.getKey();
                        String path = "users/"+uEmail+"/TaskPosts";
                        Log.d("Email: ",uEmail);

                        new DbRead<DataSnapshot>(path,DataSnapshot.class,db){

                            @Override
                            public void getReturnedDbData(DataSnapshot dataFromDb) {
                                for(DataSnapshot uTasks : dataFromDb.getChildren()){
                                    Log.d("Email: ",uEmail);
                                    Log.d("task1",uTasks.getKey().toString());

                                    TaskPost taskPost = (TaskPost) uTasks.getValue(TaskPost.class);
                                    Log.d("auth123",taskPost.getAuthor());
                                    if(taskPost != null){
                                        posts.add(taskPost); //id have to get count for array and print in random order
                                    }
                                    Log.d("authPosts",posts.get(posts.size()-1).getAuthor()); //this works fine in MyPosts.java
                                }
                                //This uncommented will result say size is 0, but it runs correctly in 98...
                                Log.d("authPosts2",posts.get(posts.size()-1).getAuthor());//Its like it forgets about it afterwards... ?@override?
                                taskPosts = posts;
                                //Log.d("post",posts.get(1).getAuthor()); //If this is uncommented, will receive Null error.
                                Log.d("log","ehre");
                            }
                        };
                        //showActivePosts();
                    }

                   //
                }

            };  //how to associate email with list of tasks.
            //Need to have a nested loop for iterating through the DB.
            //Outer layer will be to loop through users.
            //Access user.TaskPosts
            //Inner layer will be to loop through tasks post IDs
            // String userId = "users"


        }

        private void showActivePosts() {
            ArrayList<TaskPost> activePosts = new ArrayList<>();
          //  Log.d("taskPost1",taskPosts.get(1).getAuthor());
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




    }

