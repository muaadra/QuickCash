package com.softeng.quickcash;

import androidx.annotation.Keep;

class UserProfile {
    public String fName ="";
    public String aboutMe ="";
    //public avatar;
    @Keep
    public UserProfile(){}
    public UserProfile(String fName, String aboutMe){
        this.fName = fName;
        this.aboutMe = aboutMe;
        //val avatar
    }
    public void setfName(String fName){
        this.fName = fName;
    }
    public void setAboutMe(String aboutMe){
        this.aboutMe = aboutMe;
    }
    public String getfName() {
        return fName;
    }

    public String getAboutMe() {
        return aboutMe;
    }

}