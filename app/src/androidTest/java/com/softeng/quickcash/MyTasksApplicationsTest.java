package com.softeng.quickcash;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class MyTasksApplicationsTest {
    @Rule
    public ActivityScenarioRule<MyTasksApplications> activityScenarioRule
            = new ActivityScenarioRule<>(MyTasksApplications.class);


    @Test
    public void recyclerViewCount_Test() {

        final ArrayList<TaskPost> posts = new ArrayList<>();
        TaskPost taskPost1 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        TaskPost taskPost2 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        posts.add(taskPost1);
        posts.add(taskPost2);

        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MyTasksApplications>() {
                    @Override
                    public void perform(MyTasksApplications activity) {
                        activity.createRecyclerView(posts);
                        count[0] =  ((RecyclerView) activity.findViewById(R.id.TaskPostsList))
                                .getAdapter().getItemCount();
                    }
                });

        assertEquals(2, count[0]);
    }

}
