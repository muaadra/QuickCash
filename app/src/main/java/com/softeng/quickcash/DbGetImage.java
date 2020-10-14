package com.softeng.quickcash;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public abstract class DbGetImage {

    FirebaseStorage fbStorage;
    String imagePathAndName;

    public DbGetImage(FirebaseStorage fbStorage, String DbImagePathAndName) throws IOException {
        this.fbStorage = fbStorage;
        this.imagePathAndName = DbImagePathAndName;
        getImage();
    }

    /**
     * upload image to firebase when clicked on save data
     */
    public void getImage() throws IOException {
        StorageReference imageRef = fbStorage.getReference().child(imagePathAndName);

        final File localFile = File.createTempFile(imagePathAndName.replace("/",""), "jpg");

        imageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap profileImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        // Successfully downloaded data to local file
                        imageGetResult(profileImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                System.out.println("ERROR GETTING IMAGE: " + exception.getMessage());
                imageGetResult(null);
            }
        });

    }

    /**
     * this method called after an image get attempt
     * @param profileImage null if no image
     */
    public abstract void imageGetResult( Bitmap profileImage);
}

