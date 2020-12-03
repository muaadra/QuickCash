package com.softeng.quickcash;

import android.util.Log;

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

    /*
     * Test count of recycler view items
     */
    @Test
    public void testApplicantRecyclerViewTest(){
        final ArrayList<userProfile> profiles = new ArrayList<>();
        userProfile up1 = new userProfile("Jonathan","about me!");
        userProfile up2 = new userProfile("Robichaud", "my last name!");
        profiles.add(up1);
        profiles.add(up2);

        final int[] count = {-1};


        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<Applicants>() {
                    @Override
                    public void perform(Applicants activity) {
                        activity.createRecyclerView(profiles);
                        count[0] = (((RecyclerView) activity.findViewById(R.id.applicantsRecyclerView)).getAdapter().getItemCount());
                    }
                });

        assertEquals(2,count[0]);

        profiles.remove(0);
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<Applicants>() {
                    @Override
                    public void perform(Applicants activity) {
                        activity.createRecyclerView(profiles);
                        count[0] = (((RecyclerView) activity.findViewById(R.id.applicantsRecyclerView)).getAdapter().getItemCount());
                    }
                });

        assertEquals(1,count[0]);

    }
}
