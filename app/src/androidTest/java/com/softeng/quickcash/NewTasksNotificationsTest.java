package com.softeng.quickcash;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class NewTasksNotificationsTest {

    @Rule
    public ActivityScenarioRule<NewTasksNotifications> activityScenarioRule
            = new ActivityScenarioRule<>(NewTasksNotifications.class);


    @Test
    public void MyRecyclerViewCount_Test() {

        final ArrayList<TaskPost> posts = new ArrayList<>();
        TaskPost taskPost1 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        TaskPost taskPost2 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");

        posts.add(taskPost1);
        posts.add(taskPost2);

        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<NewTasksNotifications>() {
                    @Override
                    public void perform(NewTasksNotifications activity) {
                        ArrayList<String> newPostIds = new ArrayList<>();
                        newPostIds.add("Mkohjooh");
                        newPostIds.add("LLoPOIU");

                        activity.createRecyclerView(posts,newPostIds);
                        count[0] =  ((RecyclerView) activity.findViewById(R.id.TaskPostsList))
                                .getAdapter().getItemCount();
                    }
                });

        assertEquals(2, count[0]);
    }
}
