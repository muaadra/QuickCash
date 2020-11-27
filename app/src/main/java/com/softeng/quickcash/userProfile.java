package com.softeng.quickcash;

import androidx.annotation.Keep;

class userProfile{
    private String fName ="";
    private String aboutMe ="";
    private String userId ="";
    //public avatar;
    @Keep
    public userProfile(){}
    public userProfile(String fName, String aboutMe){
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAboutMe() {
        return aboutMe;
    }

}