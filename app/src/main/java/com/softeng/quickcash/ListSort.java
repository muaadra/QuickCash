package com.softeng.quickcash;

import java.util.Comparator;

public class ListSort {
}
class DistanceSort implements Comparator<TaskPost> {

    public DistanceSort() {

    }

    @Override
    public int compare(TaskPost t1, TaskPost t2) {
        return Float.compare(t1.getDistance(), t2.getDistance()) ;
    }
}