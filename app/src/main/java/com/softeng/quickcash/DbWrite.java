package com.softeng.quickcash;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class DbWrite<T> implements OnSuccessListener<Void>, OnFailureListener {
    public String path;
    DatabaseReference ref;
    T objectTobeWritten;

    public DbWrite(String path,T objectTobeWritten, FirebaseDatabase db) {
        this.path = path;
        this.objectTobeWritten = objectTobeWritten;
        ref = db.getReference(path);
        ref.setValue(objectTobeWritten).addOnFailureListener(this)
        .addOnSuccessListener(this);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        writeResult(null);
    }

    @Override
    public void onSuccess(Void aVoid) {
        writeResult(objectTobeWritten);
    }

    /**
     * if userdata is null, means your data could not be written to db
     * otherwise the object you sent is passed back
     * @param userdata the object you sent to db
     */
    public abstract void writeResult(T userdata);
}
