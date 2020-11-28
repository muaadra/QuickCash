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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsAdapter.ListItem> {
    private ArrayList<userProfile> applicantProfiles;   //Arraylist of user profiles
    public static String postId = "";
    private FirebaseStorage fbStorage;

    /*
     *Applicants Adapter constructor,
     *Take in applicantProfiles, an arraylist of userProfiles
     * Take in postId
     */
    public ApplicantsAdapter(ArrayList<userProfile> applicantProfiles,String postId,
                             FirebaseStorage fbStorage){

        this.applicantProfiles = applicantProfiles;
        this.postId = postId;
        this.fbStorage = fbStorage;

    }


    @Override
    public ListItem onCreateViewHolder( ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.applicant_item, parent, false);
        return (new ListItem(v,applicantProfiles,fbStorage));
    }

    @Override
    public void onBindViewHolder(ListItem listItem, int position) {

        listItem.userName.setText(applicantProfiles.get(position).getfName());  //Set the text for the applicant item to that of the position of adapter in applicantProfiles
        listItem.userAbout.setText(applicantProfiles.get(position).getAboutMe());  //Set the text for the applicant item to that of the position of adapter in applicantProfiles
        //show image
        listItem.setProfileImage();
    }

    @Override
    public int getItemCount() {
        return applicantProfiles.size();
    }


    public static class ListItem extends RecyclerView.ViewHolder {
        private FirebaseStorage fbStorage;
        LinearLayout applicantItemLayout;
        TextView userName;
        TextView userAbout;
        ImageView profileImage;
        View mainView;
        ArrayList<userProfile> applicantProfilesM;

        public ListItem(View listItemView, ArrayList<userProfile> applicantProfiles,
                        FirebaseStorage fbStorage) {
            super(listItemView);
            applicantItemLayout = listItemView.findViewById(R.id.applicantItem);
            applicantProfilesM = applicantProfiles;
            mainView = listItemView;
            userAbout = listItemView.findViewById(R.id.userAbout);
            profileImage = listItemView.findViewById(R.id.userPicture);
            userName = listItemView.findViewById(R.id.userName);
            applicantItemLayout = listItemView.findViewById(R.id.applicantItem);
            this.fbStorage = fbStorage;

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

        /**
         * get image to firebase when clicked on save data
         */
        public void setProfileImage() {
            String id = applicantProfilesM.get(getAdapterPosition()).getUserId();

            String imagePathAndName = "Images/user_ProfileImages/" + id;
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
