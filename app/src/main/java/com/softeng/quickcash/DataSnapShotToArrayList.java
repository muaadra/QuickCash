package com.softeng.quickcash;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DataSnapShotToArrayList{

    public static <T> ArrayList<T>  getArrayList(DataSnapshot dataSnapshot, Class<T> returnListType ){
        ArrayList<T> list = new ArrayList<>();

        for (DataSnapshot item : dataSnapshot.getChildren()) {
            list.add(item.getValue(returnListType));
        }
        return list;
    }
}
