package com.softeng.quickcash;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;


import static org.junit.Assert.assertEquals;


public class ApplicantsTest {


    @Rule
    public ActivityScenarioRule<Applicants> activityScenarioRule
            = new ActivityScenarioRule<>(Applicants.class);


    @Test
    public void MyPostRecyclerViewAdapterCreated_Test1() {
        //setup
        final Boolean[] adapter = {true};
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<Applicants>() {
                    @Override
                    public void perform(Applicants activity) {
                        ArrayList<userProfile> users = new ArrayList<>();
                        activity.createRecyclerView(users );
                        adapter[0] =  ((RecyclerView) activity.findViewById(R.id.applicantsRecyclerView))
                                .getAdapter() == null;
                    }
                });

        assertEquals(false, adapter[0]);
    }
}
