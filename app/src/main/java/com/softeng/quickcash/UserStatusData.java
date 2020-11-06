package com.softeng.quickcash;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class UserStatusData {


    /**
     * get user Email
     */
    public static String getEmail(Context context) {
        return getUserPreferenceData("email", context);
    }

    /**
     * set user first run status/flag; if it's not user's firs run set to false
     */
    public static void setUserFirstRun(Context context, boolean firstRun) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstRun", firstRun);
        editor.apply();
    }

    /**
     * get first run status
     * @return true if user first run
     */
    public static boolean getUserFirstRunStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "UserPreferences", MODE_PRIVATE);
        return sharedPreferences.getBoolean("firstRun", true);
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
    public static String getUserPreferenceData(String key, Context context) {
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
     * save user data in SharedPreferences as a key and value set
     */
    public static void saveUserFilterPrefsData(FilterPreferences filterPrefs, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(filterPrefs);
        editor.putString("UserPrefs", json);
        editor.apply();
    }

    public static FilterPreferences getUserFilterPrefs(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "UserPreferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("UserPrefs", null);

        FilterPreferences userPrefs = null;

        if(json != null){
            userPrefs = gson.fromJson(json, FilterPreferences.class);
        }

        return userPrefs;
    }

    /**
     * clear all data stored in SharedPreferences
     */
    public static void removeAllUserPreferences(Context context) {
        SharedPreferences prefs = context
                .getSharedPreferences("UserPreferences", MODE_PRIVATE);
        prefs.edit().clear().apply();
    }


}