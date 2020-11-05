package com.softeng.quickcash;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
     * test if one element sort remains unchanged
     */
    @Test
    public void DistanceSortAscendingTest2() {
        TaskPost taskPost1 = new TaskPost();
        taskPost1.setPostId("id_0");
        taskPost1.setDistance(6.3f);

        ArrayList<TaskPost> posts = new ArrayList<>();
        posts.add(taskPost1);

        DistanceSort distanceSort = new DistanceSort(true);
        Collections.sort(posts,distanceSort);

        assertEquals("id_0",posts.get(0).getPostId());
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

    /**
     * test if elements are sorted in Descending order
     */
    @Test
    public void DistanceSortDescendingTest2() {
        TaskPost taskPost1 = new TaskPost();
        taskPost1.setPostId("id_0");
        taskPost1.setDistance(8.3f);

        TaskPost taskPost2 = new TaskPost();
        taskPost2.setPostId("id_1");
        taskPost2.setDistance(1f);

        TaskPost taskPost3 = new TaskPost();
        taskPost3.setPostId("id_2");
        taskPost3.setDistance(10.2f);

        ArrayList<TaskPost> posts = new ArrayList<>();
        posts.add(taskPost1);
        posts.add(taskPost2);
        posts.add(taskPost3);

        DistanceSort distanceSort = new DistanceSort(false);
        Collections.sort(posts,distanceSort);

        assertEquals("id_2",posts.get(0).getPostId());
        assertEquals("id_0",posts.get(1).getPostId());
        assertEquals("id_1",posts.get(2).getPostId());
    }
}
