package com.softeng.quickcash;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * use to ease reading data from firebase database
 * @param <T>
 */
public abstract class DbRead<T> implements ValueEventListener {
    public String path;
    private Class<T> returnType;
    DatabaseReference ref;

    public DbRead(String path, Class<T> returnType, FirebaseDatabase db) {
        this.path = path;
        this.returnType = returnType;
        ref = db.getReference(path);
        ref.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        //get user sign up info as a UserSignUpData object
        T dataFromDb = dataSnapshot.getValue(returnType);
        getReturnedDbData(dataFromDb);
    }

    public abstract void getReturnedDbData(T dataFromDb);


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


}
