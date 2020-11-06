package com.softeng.quickcash;

import java.util.ArrayList;

public class FilterPreferences {
    ArrayList<String> categories = new ArrayList<>();
    int maxDistance;
    float minPay;

    public FilterPreferences() {
    }

    public FilterPreferences(ArrayList<String> categories, int maxDistance, float minPay) {
        this.categories = categories;
        this.maxDistance = maxDistance;
        this.minPay = minPay;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public float getMinPay() {
        return minPay;
    }

    public void setMinPay(float minPay) {
        this.minPay = minPay;
    }
}
