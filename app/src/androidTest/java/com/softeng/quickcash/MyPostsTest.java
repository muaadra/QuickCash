package com.softeng.quickcash;

import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyPostsTest {
    @Rule
    public ActivityScenarioRule<MyPosts> activityScenarioRule
            = new ActivityScenarioRule<>(MyPosts.class);



    @Test
    public void goToPostATaskActivityTest(){

        onView(withId(R.id.postNewTaskButton)).perform(click());

        //check screen is displayed
        onView(withId(R.id.postTasksLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void MyRecyclerViewCount_Test1() {
        //setup
        final ArrayList<TaskPost> posts = new ArrayList<>();
        TaskPost taskPost1 = new TaskPost("1","t","d"
                ,"5",false,false, Calendar.getInstance().getTime(),"hh");
        TaskPost taskPost2 = new TaskPost("1","t","d"
                ,"5",false,false, Calendar.getInstance().getTime(),"hh");
        posts.add(taskPost1);
        posts.add(taskPost2);

        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MyPosts>() {
                    @Override
                    public void perform(MyPosts activity) {
                        activity.createRecyclerView(posts);
                        count[0] =  ((RecyclerView) activity.findViewById(R.id.postsList_MyPosts))
                                .getAdapter().getItemCount();
                    }
                });

        assertEquals(2, count[0]);
    }

    @Test
    public void MyPostRecyclerViewIsEmptyWhenNoList_Test2() {
        //setup
        final ArrayList<TaskPost> posts = new ArrayList<>();
        final boolean[] noRecyclerView = new boolean[1];
        noRecyclerView[0] = false;

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MyPosts>() {
                    @Override
                    public void perform(MyPosts activity) {
                        activity.createRecyclerView(posts);
                        noRecyclerView[0] =  (((RecyclerView) activity.findViewById(R.id.postsList_MyPosts))
                                .getAdapter() == null);
                    }
                });


        assertTrue(noRecyclerView[0]);
    }

    @Test
    public void clickOnRecyclerViewGoesToPostATaskActivity() throws InterruptedException {
        //setup
        final ArrayList<TaskPost> posts = new ArrayList<>();
        TaskPost taskPost1 = new TaskPost("1","t","d"
                ,"5",false,false, Calendar.getInstance().getTime(),"hh");
        TaskPost taskPost2 = new TaskPost("1","t","d"
                ,"5",false,false, Calendar.getInstance().getTime(),"hh");
        posts.add(taskPost1);
        posts.add(taskPost2);

        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MyPosts>() {
                    @Override
                    public void perform(MyPosts activity) {
                        activity.createRecyclerView(posts);
                        count[0] =  ((RecyclerView) activity.findViewById(R.id.postsList_MyPosts))
                                .getAdapter().getItemCount();
                    }
                });


        onView(withId(R.id.postsList_MyPosts)).perform(click());

        //check screen is displayed
        onView(withId(R.id.postTasksLayout)).check(matches(isDisplayed()));
    }
}
