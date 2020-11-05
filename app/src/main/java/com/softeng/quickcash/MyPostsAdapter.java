package com.softeng.quickcash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * this class is the adapter for "My posts" activity recyclerView
 *
 * @author Muaad Alrawhani
 */

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ListItem> {
    private ArrayList<TaskPost> posts;

    /**
     * constructor
     * @param posts array of TaskPost objects to show on the list
     */
    public MyPostsAdapter(ArrayList<TaskPost> posts) {
        this.posts = posts;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.post_list_item, parent, false);

        return (new ListItem(v,posts));
    }


    @Override
    public void onBindViewHolder(ListItem listItem, int position) {
        // - replace the contents of the view with that element
        int pos = posts.size() - (position + 1);//to reverse order

        listItem.postTitle.setText(posts.get(pos).getTaskTitle());

        //set description and shorten length if needed
        String desc = posts.get(pos).getTaskDescription();
        if(desc.length() > 20){
            desc = desc.substring(0,20) + "...";
        }
        listItem.postDescription.setText(desc);

        //setting up icon
        Context context =  listItem.postDescription.getContext();
        String drawableName = posts.get(pos).getTaskTitle().replace(" ","_");
        System.out.println(drawableName);
        int id = context.getResources().getIdentifier(
                drawableName, "drawable", context.getPackageName());

        listItem.jobIcon.setBackgroundResource(id);

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // this is the class that represents list object/item
    //need to be instantiated for every item in the list
    // Provide a reference to the views for each list item
    public static class ListItem extends RecyclerView.ViewHolder {
        LinearLayout postLayout;
        TextView postTitle;
        TextView postDescription;
        ImageView jobIcon;
        View mainView;
        ArrayList<TaskPost> myPosts;

        public ListItem(View listItemView, ArrayList<TaskPost> posts) {
            super(listItemView);
            myPosts = posts;
            mainView = listItemView;
            postTitle = listItemView.findViewById(R.id.postTitle);
            jobIcon = listItemView.findViewById(R.id.jobIcon);
            postDescription = listItemView.findViewById(R.id.postDescTextView);
            postLayout = listItemView.findViewById(R.id.postItem);


            //make item clickable
            postLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get position
                    gotToPostATask();
                }
            });
        }

        private void gotToPostATask(){
            Intent intent = new Intent(mainView.getContext(), PostATaskActivity.class);
            int pos = myPosts.size() - (getAdapterPosition() + 1);//to reverse order
            intent.putExtra("postID",myPosts.get(pos).getPostId());
            mainView.getContext().startActivity(intent);
        }


    }
}