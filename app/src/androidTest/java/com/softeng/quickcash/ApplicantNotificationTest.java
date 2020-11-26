package com.softeng.quickcash;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ApplicantNotificationTest {
    @Rule
    public ActivityScenarioRule<ApplicantNotification> activityScenarioRule
            = new ActivityScenarioRule<>(ApplicantNotification.class);

    @Test
    public void MyPostRecyclerViewIsEmptyWhenNoList_Test1() {
        //setup
        final ArrayList<TaskPost> posts = new ArrayList<>();
        final Boolean[] adapter = {true};
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<ApplicantNotification>() {
                    @Override
                    public void perform(ApplicantNotification activity) {
                        activity.createRecyclerView(posts);
                        adapter[0] =  ((RecyclerView) activity.findViewById(R.id.TaskPostsList))
                                .getAdapter() == null;
                    }
                });

        assertEquals(false, adapter[0]);
    }
}
