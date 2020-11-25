package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;

public class Subscription extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        voidGetSubscriptionsFromDB();

    }

    /**
     * creates a RecyclerView view for main task posts
     * @param Tasks list of posts
     */
    public void createRecyclerView(String[] Tasks, List<Boolean> checked) {
        RecyclerView recyclerView = findViewById(R.id.SubscriptionRV);

        // using a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter mAdapter = new SubscriptionsRVListAdapter(Tasks,checked);

        recyclerView.setAdapter(mAdapter);
    }

    private void voidGetSubscriptionsFromDB(){
        String path = "users/"+ UserStatusData.getUserID(this) +"/Subscriptions";

        //read data from database
        DbRead<DataSnapshot> dbRead = new DbRead<DataSnapshot>(path,DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                showOnUI(dataFromDb);
            }
        };

    }

    private void showOnUI(DataSnapshot dataFromDb){
        ArrayList<Boolean> subscriptions = new ArrayList<>();
        if(dataFromDb == null){
            createRecyclerView(TaskTypes.getTaskTypes(),subscriptions);
            return;
        }

        subscriptions = DataSnapShotToArrayList.getArrayList(dataFromDb,Boolean.class);
        createRecyclerView(TaskTypes.getTaskTypes(),subscriptions);

    }


    public void subscribeToTasks(View v){
        ArrayList<Boolean> subscriptions  = getSelectedItems();
        //path where you want to write data to
        String path = "users/"+ UserStatusData.getUserID(this) +"/Subscriptions";

        new DbWrite< ArrayList<Boolean>>(path,subscriptions,db) {
            @Override
            public void writeResult( ArrayList<Boolean> userdata) {
                finishActivity();
            }
        };
    }

    private void finishActivity(){
        Toast.makeText(this,"Subscription updated",Toast.LENGTH_SHORT).show();
        finish();
    }

    private ArrayList<Boolean> getSelectedItems(){
        ArrayList<Boolean> checkedItems = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.SubscriptionRV);

        int itemsCount = recyclerView.getChildCount();
        for(int i = 0; i < itemsCount ; i++){
            View view = recyclerView.getLayoutManager().getChildAt(i);
            boolean selected = ((CheckBox)view.findViewById(R.id.listCheckBox)).isChecked();
            if(selected){
                checkedItems.add(true);
            }else {
                checkedItems.add(false);
            }
        }
        return checkedItems;
    }
}