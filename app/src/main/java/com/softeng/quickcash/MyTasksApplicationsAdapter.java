package com.softeng.quickcash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.util.ArrayList;

public class MyTasksApplicationsAdapter extends RecyclerView.Adapter<MyTasksApplicationsAdapter.ListItem> {
    private ArrayList<TaskPost> posts;
    private FirebaseStorage fbStorage;
    private String myID;
    /**
     * constructor
     * @param posts array of TaskPost objects to show on the list
     */
    public MyTasksApplicationsAdapter(ArrayList<TaskPost> posts,
                                      FirebaseStorage fbStorage) {
        this.posts = posts;
        this.fbStorage = fbStorage;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.my_applications_list_item,
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
        if(myID == null){
           if((myID = UserStatusData.getUserID(listItem.mainView.getContext())) == null) {
               return;
           }
        }


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


        TextView statusTextV = ((TextView)listItem.mainView.findViewById(R.id.Accepted));
        if(post.getAssignedEmployee().equals(myID)
        && !post.isCompleted() && !post.isPostDeleted()){
            statusTextV.setVisibility(View.VISIBLE);
        }else if(post.getAssignedEmployee().equals(myID)
                && post.isCompleted()){
            statusTextV.setVisibility(View.VISIBLE);
            statusTextV.setText("*Completed*");
            statusTextV.setTextColor(Color.parseColor("#6f03fc"));

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
            String id = mPosts.get(getAdapterPosition()).getAuthor();

            String imagePathAndName = "Images/user_ProfileImages/" + id;
            System.out.println(getAdapterPosition() + "   " + id + mPosts.get(getAdapterPosition()).getPostId() + "    --------");
            DbGetImage dbGetImage = new DbGetImage(fbStorage,imagePathAndName) {
                @Override
                public void imageGetResult(Bitmap dbProfileImage) {
                    //set image on screen to image retrieved from db
                    if(dbProfileImage != null){
                        BitmapDrawable ob = new BitmapDrawable(mainView.getResources(),dbProfileImage);
                        profileImage.setBackground(ob);
                    }else {

                        profileImage.setBackground(ContextCompat.getDrawable(mainView.getContext(),
                                R.drawable.prifile_placeholder));

                    }

                }
            };
            dbGetImage.getImage();
        }

    }
}