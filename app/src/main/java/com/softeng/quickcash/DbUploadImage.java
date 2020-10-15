package com.softeng.quickcash;

import android.net.Uri;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public abstract class DbUploadImage {
    FirebaseStorage fbStorage;
    String imagePathAndName;
    Uri imageUri;

    public DbUploadImage(FirebaseStorage fbStorage, String imagePathAndName, Uri imageUri) {
        this.fbStorage = fbStorage;
        this.imagePathAndName = imagePathAndName;
        this.imageUri = imageUri;
        uploadImage();
    }

    /**
     * upload image to firebase when clicked on save data
     */
    public void uploadImage(){
        StorageReference imageRef = fbStorage.getReference().child(imagePathAndName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadResult(true,
                                "image uploaded successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        uploadResult(false,exception.getMessage());
                    }
                });
    }

    /**
     * this method called after an image upload attempt
     * @param success true if successfully uploaded
     * @param uploadMessage will contain error message if error uploading image
     */
    public abstract void uploadResult(boolean success, String uploadMessage);
}
