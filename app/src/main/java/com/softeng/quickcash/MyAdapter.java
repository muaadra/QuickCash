package com.softeng.quickcash;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<TaskPost> posts;

    // post list item will have variable titles and icons
    public MyAdapter(ArrayList<TaskPost> postTitles) {
        this.posts = postTitles;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.post_list_item, parent, false);

        return (new MyViewHolder(v));
    }

    //
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        // - replace the contents of the view with that element
        int pos = posts.size() - (position + 1);//to reverse order

        viewHolder.postTitle.setText(posts.get(pos).getTaskTitle());

        //set description and shorten length if needed
        String desc = posts.get(pos).getTaskDescription();
        if(desc.length() > 20){
            desc = desc.substring(0,20) + "...";
        }
        viewHolder.postDescription.setText(desc);

        //setting up icon
        Context context =  viewHolder.postDescription.getContext();
        String drawableName = posts.get(pos).getTaskTitle().replace(" ","_");
        System.out.println(drawableName);
        int id = context.getResources().getIdentifier(
                drawableName, "drawable", context.getPackageName());

        viewHolder.jobIcon.setBackgroundResource(id);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // this is the class that represents list object/item
    //need to be instantiated for every item in the list
    // Provide a reference to the views for each list item
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView postTitle;
        TextView postDescription;
        ImageView jobIcon;

        public MyViewHolder(View listItemView) {
            super(listItemView);
            postTitle = listItemView.findViewById(R.id.postTitle);
            jobIcon = listItemView.findViewById(R.id.jobIcon);
            postDescription = listItemView.findViewById(R.id.postDescTextView);
        }
    }
}