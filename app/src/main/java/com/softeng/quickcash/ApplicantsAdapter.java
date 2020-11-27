package com.softeng.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsAdapter.ListItem> {
    private ArrayList<userProfile> applicantProfiles;   //Arraylist of user profiles
    public static String postId = "";
    /*
     *Applicants Adapter constructor,
     *Take in applicantProfiles, an arraylist of userProfiles
     * Take in postId
     */
    public ApplicantsAdapter(ArrayList<userProfile> applicantProfiles,String postId){

        this.applicantProfiles = applicantProfiles;
        this.postId = postId;

    }


    @Override
    public ListItem onCreateViewHolder( ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.applicant_item, parent, false);
        return (new ListItem(v,applicantProfiles));
    }

    @Override
    public void onBindViewHolder(ListItem listItem, int position) {

        listItem.userName.setText(applicantProfiles.get(position).getfName());  //Set the text for the applicant item to that of the position of adapter in applicantProfiles

    }

    @Override
    public int getItemCount() {
        return applicantProfiles.size();
    }
    public static class ListItem extends RecyclerView.ViewHolder {
        LinearLayout applicantItemLayout;
        TextView userName;
        TextView userRating;
        View mainView;
        ArrayList<userProfile> applicantProfilesM;
        ImageView jobIconSub;

        public ListItem(View listItemView, ArrayList<userProfile> applicantProfiles) {
            super(listItemView);
            applicantItemLayout = listItemView.findViewById(R.id.applicantItem);
            applicantProfilesM = applicantProfiles;
            mainView = listItemView;
            userRating = listItemView.findViewById(R.id.userRating);
            userName = listItemView.findViewById(R.id.userName);
            applicantItemLayout = listItemView.findViewById(R.id.applicantItem);
            jobIconSub = listItemView.findViewById(R.id.userPicture);

            applicantItemLayout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    goToViewProfile();          //When a user clicks on the individual item in recycler view
                }
            });

        }
        /*
         *This will bring the user to the respective user profile they clicked on in the applicants recycler view
         */
        private void goToViewProfile(){
            Intent intent = new Intent(mainView.getContext(), ViewProfile.class);
            intent.putExtra("postId", postId ); //pass postID to apply assigned employee
            intent.putExtra("userID",applicantProfilesM.get(getAdapterPosition()).getUserId()); //pass the uesrID of the profile the user wants to view
            intent.putExtra("isMine",true); //This boolean is to let view profile know to show accept applicant button
            mainView.getContext().startActivity(intent);
        }
    }

}
