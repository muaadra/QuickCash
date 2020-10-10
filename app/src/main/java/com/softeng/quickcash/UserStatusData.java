package com.softeng.quickcash;

import android.content.Context;
import android.content.SharedPreferences;


import static android.content.Context.MODE_PRIVATE;

public class UserStatusData {


    /**
     * get user Email
     */
    public static String getEmail(Context context) {
        return getUserData("email", context);
    }

    /**
     * get user UserName
     */
    public static String getUserName(Context context) {
        return getUserData("userName", context);
    }

    /**
     * gets user sign-in status
     */
    public static boolean isUserSignIn(Context context) {
        return (!getEmail(context).equals(""));
    }

    /**
     * set user sign-in status to false
     */
    public static void setUserSignInToFalse(Context context) {
        saveUserData("email", "", context);
    }

    /**
     * set user sign-in status to false
     */
    public static void setUserSignInToTrue(Context context, UserSignUpData userData) {
        saveUserData("email", userData.getEmail(), context);
        saveUserData("userName", userData.getPassword(), context);
    }

    /**
     * get user data from SharedPreferences
     */
    public static String getUserData(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "UserPreferences", MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
    }

    /**
     * save user data in SharedPreferences as a key and value set
     */
    public static void saveUserData(String key, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * clear all data stored in SharedPreferences
     */
    public static void removeAllUserPreferences(Context context) {
        SharedPreferences prefs = context
                .getSharedPreferences("email", MODE_PRIVATE);
        prefs.edit().clear().apply();
    }


}