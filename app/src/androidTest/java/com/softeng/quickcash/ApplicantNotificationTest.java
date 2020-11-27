package com.softeng.quickcash;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ApplicantNotificationTest {
    @Rule
    public ActivityScenarioRule<ApplicantNotification> activityScenarioRule
            = new ActivityScenarioRule<>(ApplicantNotification.class);

    @After
    public void setup(){
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<ApplicantNotification>() {
                    @Override
                    public void perform(ApplicantNotification activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                    }
                });
    }

    @Test
    public void MyPostRecyclerViewIsEmptyWhenNoList_Test1() {
        //setup
        final ArrayList<TaskPost> posts = new ArrayList<>();
        final Boolean[] adapter = {true};
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<ApplicantNotification>() {
                    @Override
                    public void perform(ApplicantNotification activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        //restart the activity
                        Intent intent = new Intent(activity, MyPosts.class);
                        activity.startActivity(intent);

                        activity.createRecyclerView(posts);
                        adapter[0] =  ((RecyclerView) activity.findViewById(R.id.TaskPostsList))
                                .getAdapter() == null;
                    }
                });

        assertEquals(false, adapter[0]);
    }

    @Test
    public void MyRecyclerViewCount_Test2() {

        final ArrayList<TaskPost> posts = new ArrayList<>();
        TaskPost taskPost1 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        TaskPost taskPost2 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        posts.add(taskPost1);
        posts.add(taskPost2);

        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<ApplicantNotification>() {
                    @Override
                    public void perform(ApplicantNotification activity) {
                        activity.createRecyclerView(posts);
                        count[0] =  ((RecyclerView) activity.findViewById(R.id.TaskPostsList))
                                .getAdapter().getItemCount();
                    }
                });

        assertEquals(2, count[0]);
    }

    @After
    public void tearDown(){
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<ApplicantNotification>() {
                    @Override
                    public void perform(ApplicantNotification activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                    }
                });
    }
}
