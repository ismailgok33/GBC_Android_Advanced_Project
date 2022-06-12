package com.example.gbc_android_advanced_project.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gbc_android_advanced_project.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private final FirebaseFirestore DB;
    private final String COLLECTION_USERS = "users";
    private final String FIELD_EMAIL = "email";
//    private final String FIELD_PASSWORD = "password";
    private final String FIELD_ID = "id";
    private final String TAG = this.getClass().getCanonicalName();

    public UserRepository()
    {
        DB = FirebaseFirestore.getInstance();
    }

    public void addUser(User user)
    {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_EMAIL, user.getEmail());
//            data.put(FIELD_PASSWORD, user.getPassword());
            data.put(FIELD_ID, user.getId());

            DB.collection(COLLECTION_USERS)
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Error adding document", e);
                        }
                    });
        }catch (Exception e)
        {
            Log.d(TAG,"Exception caught: " + e.getLocalizedMessage());
        }

    }
}
