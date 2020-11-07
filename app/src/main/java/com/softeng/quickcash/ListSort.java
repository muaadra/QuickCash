package com.softeng.quickcash;

import java.util.Comparator;

/**
 * this collection of classes, define how Job/Task posts are to be sorted
 */

class DistanceSort implements Comparator<TaskPost> {
    public static String sortName = "Distance"; //displayed to user on "sort by" spinner
    private int ascending;

    public DistanceSort(boolean ascending) {
        if(ascending){
            this.ascending = 1;
        }else {
            this.ascending = -1;
        }
    }

    @Override
    public int compare(TaskPost t1, TaskPost t2) {
        return Float.compare(t1.getDistance(), t2.getDistance()) * ascending;
    }
}

class CostSort implements Comparator<TaskPost>  {
    public static String sortName = "hourly pay";//displayed to user on "sort by" spinner
    private int ascending;

    public CostSort(boolean ascending) {
        if(ascending){
            this.ascending = 1;
        }else {
            this.ascending = -1;
        }
    }

    @Override
    public int compare(TaskPost t1, TaskPost t2) {
        return Float.compare(t1.getTaskCost(),t2.getTaskCost()) * ascending;
    }
}

class ExpectedDateSort implements Comparator<TaskPost> {
    public static String sortName = "Expected Date";//displayed to user on "sort by" spinner
    private int ascending;

    public ExpectedDateSort(boolean ascending) {
        if(ascending){
            this.ascending = 1;
        }else {
            this.ascending = -1;
        }
    }

    @Override
    public int compare(TaskPost t1, TaskPost t2) {
        int comResult = 0;

        if(ascending == 1){
            comResult = Double.compare(t1.getExpectedDate().getTime(),
                    t2.getExpectedDate().getTime());
        }else {
            comResult = Double.compare(t2.getExpectedDate().getTime(),
                    t1.getExpectedDate().getTime());
        }

        return comResult;
    }
}

class LatestDateSort implements Comparator<TaskPost>  {
    public static String sortName = "Latest Posted";//displayed to user on "sort by" spinner
    private int ascending;

    public LatestDateSort(boolean ascending) {
        if(ascending){
            this.ascending = 1;
        }else {
            this.ascending = -1;
        }
    }

    @Override
    public int compare(TaskPost t1, TaskPost t2) {
        if(t1.getTimeStamp() == null){
            return 0;
        }
        return Double.compare(t1.getTimeStamp().getTime(),
                t2.getTimeStamp().getTime()) * ascending;
    }
}