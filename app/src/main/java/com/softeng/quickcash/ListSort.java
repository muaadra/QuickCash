package com.softeng.quickcash;

import java.util.Comparator;

public class ListSort implements Comparator<TaskPost> {
    @Override
    public int compare(TaskPost o1, TaskPost o2) {
        return 0;
    }
}

class DistanceSort extends ListSort {
    public static String sortName = "Distance";
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


class CostSort extends ListSort {
    public static String sortName = "cost per hour";
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

class ExpectedDateSort extends ListSort {
    public static String sortName = "Expected Date";
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

class LatestDateSort extends ListSort {
    public static String sortName = "Latest Posted";
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

        return Double.compare(t1.getTimeStamp().getTime(),
                t2.getTimeStamp().getTime()) * ascending;
    }
}