package com.softeng.quickcash;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ListSortTest {
    /**
     * test if elements are sorted in Ascending order
     */
    @Test
    public void DistanceSortAscendingTest1() {
        TaskPost taskPost1 = new TaskPost();
        taskPost1.setPostId("id_0");
        taskPost1.setDistance(6.3f);

        TaskPost taskPost2 = new TaskPost();
        taskPost2.setPostId("id_1");
        taskPost2.setDistance(0f);

        ArrayList<TaskPost> posts = new ArrayList<>();
        posts.add(taskPost1);
        posts.add(taskPost2);

        DistanceSort distanceSort = new DistanceSort(true);
        Collections.sort(posts,distanceSort);

        assertEquals("id_1",posts.get(0).getPostId());
        assertEquals("id_0",posts.get(1).getPostId());
    }

    /**
     * test if elements are sorted in Descending order
     */
    @Test
    public void DistanceSortDescendingTest1() {
        TaskPost taskPost1 = new TaskPost();
        taskPost1.setPostId("id_0");
        taskPost1.setDistance(8.3f);

        TaskPost taskPost2 = new TaskPost();
        taskPost2.setPostId("id_1");
        taskPost2.setDistance(1f);

        ArrayList<TaskPost> posts = new ArrayList<>();
        posts.add(taskPost1);
        posts.add(taskPost2);

        DistanceSort distanceSort = new DistanceSort(false);
        Collections.sort(posts,distanceSort);

        assertEquals("id_0",posts.get(0).getPostId());
        assertEquals("id_1",posts.get(1).getPostId());
    }

}
