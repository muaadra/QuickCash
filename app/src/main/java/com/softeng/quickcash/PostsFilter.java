package com.softeng.quickcash;

import android.app.Activity;

import java.util.ArrayList;

public class PostsFilter {
    FilterPreferences FilterPrefs;

    public PostsFilter(FilterPreferences FilterPrefs) {
        this.FilterPrefs = FilterPrefs;
    }
    public PostsFilter(Activity activity) {
        FilterPrefs = UserStatusData.getUserFilterPrefs(activity);
    }

    public ArrayList<TaskPost> applyFilters(ArrayList<TaskPost> posts){
        ArrayList<TaskPost> deletedPostsFilter = filterDeletedPosts(posts);
        ArrayList<TaskPost> payFilter = filterByMinHourlyPay(deletedPostsFilter);
        ArrayList<TaskPost> DistFilter = filterByDistance(payFilter);
        return filterByCategory(DistFilter);
    }

    private ArrayList<TaskPost> filterDeletedPosts(ArrayList<TaskPost> posts){
        ArrayList<TaskPost> filteredPosts = new ArrayList<>();
        for (TaskPost post: posts) {
            if(!post.isPostDeleted()){
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }

    private ArrayList<TaskPost> filterByMinHourlyPay(ArrayList<TaskPost> posts){
        ArrayList<TaskPost> filteredPosts = new ArrayList<>();
        for (TaskPost post: posts) {
            if(post.getTaskCost() >= FilterPrefs.getMinPay()){
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }

    private ArrayList<TaskPost> filterByDistance(ArrayList<TaskPost> posts){
        ArrayList<TaskPost> filteredPosts = new ArrayList<>();
        for (TaskPost post: posts) {
            if((post.getDistance()/1000) <= FilterPrefs.getMaxDistance()){
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }

    private ArrayList<TaskPost> filterByCategory(ArrayList<TaskPost> posts){
        ArrayList<TaskPost> filteredPosts = new ArrayList<>();
        for (TaskPost post: posts) {
            if(FilterPrefs.getCategories().contains(post.getTaskTitle())){
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }
}
