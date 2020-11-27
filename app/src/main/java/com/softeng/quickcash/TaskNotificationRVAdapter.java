package com.softeng.quickcash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.util.ArrayList;

public class TaskNotificationRVAdapter extends RecyclerView.Adapter<TaskNotificationRVAdapter.ListItem> {
    private ArrayList<TaskPost> posts;
    private FirebaseStorage fbStorage;
    private ArrayList<String> newPostsIds;
    /**
     * constructor
     * @param posts array of TaskPost objects to show on the list
     */
    public TaskNotificationRVAdapter(ArrayList<TaskPost> posts,
                                     FirebaseStorage fbStorage, ArrayList<String> newPostsIds) {
        this.posts = posts;
        this.fbStorage = fbStorage;
        this.newPostsIds = newPostsIds;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.main_activity_post_list_item,
                parent, false);

        return (new ListItem(v,posts,fbStorage));
    }

    /**
     * this method sets the sets up UI fields for each item in the list
     * @param listItem one element or item in the list
     * @param position the position of that item on the list
     */
    @Override
    public void onBindViewHolder(ListItem listItem, int position) {
        TaskPost post = posts.get(position);

        //start replacing/modifying the contents of the view

        listItem.postTitle.setText(post.getTaskTitle());

        //set description and shorten length if needed
        String desc = post.getTaskDescription();
        if(desc.length() > 50){
            desc = desc.substring(0,50) + "...";
        }
        listItem.postDescription.setText(desc);

        long expectedDate = post.getExpectedDate().getTime();
        listItem.expectedDate.setText(DateFormat.getDateInstance().format(expectedDate));

        //show image
        listItem.setProfileImage();

        listItem.postCost.setText(post.getTaskCost() + "/hr");

        float distanceToPost =((int)(post.getDistance()/10))/100f;
        listItem.postDistance.setText(distanceToPost+ "km away");

        if(newPostsIds.contains(post.getPostId())){
            TextView isNewTv = ((TextView)listItem.itemView.findViewById(R.id.isNew));
            isNewTv.setText("NEW");
        }

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
        private FirebaseStorage fbStorage;
        LinearLayout postLayout;
        TextView postTitle;
        TextView postDescription;
        ImageView profileImage;
        TextView expectedDate;
        TextView postCost;
        TextView postDistance;
        View mainView;
        ArrayList<TaskPost> mPosts;

        public ListItem(View listItemView, ArrayList<TaskPost> posts,
                        FirebaseStorage fbStorage) {
            super(listItemView);
            mPosts = posts;
            mainView = listItemView;
            postTitle = listItemView.findViewById(R.id.postTitle);
            profileImage = listItemView.findViewById(R.id.profileImageJobPost);
            postDescription = listItemView.findViewById(R.id.postDescTextView);
            postLayout = listItemView.findViewById(R.id.postItem);
            expectedDate = listItemView.findViewById(R.id.expectedDate);
            postCost = listItemView.findViewById(R.id.postCost);
            postDistance = listItemView.findViewById(R.id.postDistance);
            this.fbStorage = fbStorage;


            //make item clickable
            postLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get position
                    gotToViewPost();
                }
            });

        }



        private void gotToViewPost(){
            Intent intent = new Intent(mainView.getContext(), ViewPost.class);
            intent.putExtra("postID",mPosts.get(getAdapterPosition()).getPostId());
            intent.putExtra("authorID",mPosts.get(getAdapterPosition()).getAuthor());
            mainView.getContext().startActivity(intent);
        }


        /**
         * get image to firebase when clicked on save data
         */
        public void setProfileImage() {
            String id = mPosts.get(getAdapterPosition())
                    .getAuthor().replace(".", ";");

            String imagePathAndName = "Images/user_ProfileImages/" + id;

            DbGetImage dbGetImage = new DbGetImage(fbStorage,imagePathAndName) {
                @Override
                public void imageGetResult(Bitmap dbProfileImage) {
                    //set image on screen to image retrieved from db
                    if(dbProfileImage != null){
                        BitmapDrawable ob = new BitmapDrawable(mainView.getResources(),dbProfileImage);
                        profileImage.setBackground(ob);
                    }else {
                        System.out.println("error getting image");
                    }

                }
            };
            dbGetImage.getImage();
        }

    }
}