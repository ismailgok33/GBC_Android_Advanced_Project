package com.example.gbc_android_advanced_project.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gbc_android_advanced_project.models.Event;
import com.example.gbc_android_advanced_project.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private final FirebaseFirestore DB;
    private final String COLLECTION_USERS = "users";
    private final String COLLECTION_EVENTS = "events";
    private final String FIELD_JOINED_USERS = "joinedUsers";
    private final String FIELD_JOINED_EVENTS = "joinedEvents";
    private final String FIELD_EMAIL = "email";
    private final String FIELD_ID = "id";
    private final String TAG = this.getClass().getCanonicalName();

    public UserRepository() {
        DB = FirebaseFirestore.getInstance();
    }

    public void addUser(User user) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_EMAIL, user.getEmail());
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
        } catch (Exception e) {
            Log.d(TAG, "Exception caught: " + e.getLocalizedMessage());
        }
    }

    public void joinEvent(String eventID) {

        try {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // STEP-1:
            // Fetch the event document and add the userID to joinedUsers
            DB.collection(COLLECTION_EVENTS)
                    .document(eventID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: successfully retrived the document!");
                            Event event = documentSnapshot.toObject(Event.class);
                            ArrayList<String> joinedUsers = event.getJoinedUsers();

                            // add the current user to joinedUsers list
                            joinedUsers.add(userID);

                            // Update the document with the new joinedUsers list
                            DB.collection(COLLECTION_EVENTS)
                                    .document(eventID)
                                    .update(FIELD_JOINED_USERS, joinedUsers)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: successfully updated the event document!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: failed to update the event document = " + e.getLocalizedMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: failed to retrive the document = " + e.getLocalizedMessage());
                        }
                    });


            // STEP-2:
            // get the User document and update the joinedEvents list
            DB.collection(COLLECTION_USERS)
                    .document(userID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: successfully fetched the user document!");

                            User user = documentSnapshot.toObject(User.class);
                            ArrayList<String> joinedEvents = user.getJoinedEvents();

                            // Add the current event to joinedEvents list
                            joinedEvents.add(eventID);

                            // Update the event document with new joinedEvents
                            DB.collection(COLLECTION_USERS)
                                    .document(userID)
                                    .update(FIELD_JOINED_EVENTS, joinedEvents)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: successfully updated the user document!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: failed to update the user document = " + e.getLocalizedMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: failed to fetch the user document = " + e.getLocalizedMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "joinEvent: Error while accessing the firestore collection = " + e.getLocalizedMessage());
        }


    }

    public void leaveEvent(String eventID) {

        try {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // STEP-1:
            // Fetch the event document and remove the userID from joinedUsers
            DB.collection(COLLECTION_EVENTS)
                    .document(eventID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: successfully retrived the document!");
                            Event event = documentSnapshot.toObject(Event.class);
                            ArrayList<String> joinedUsers = event.getJoinedUsers();

                            // remove the current user from joinedUsers list
                            joinedUsers.remove(userID);

                            // Update the document with the new joinedUsers list
                            DB.collection(COLLECTION_EVENTS)
                                    .document(eventID)
                                    .update(FIELD_JOINED_USERS, joinedUsers)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: successfully updated the event document!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: failed to update the event document = " + e.getLocalizedMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: failed to retrive the document = " + e.getLocalizedMessage());
                        }
                    });


            // STEP-2:
            // get the User document and update the joinedEvents list
            DB.collection(COLLECTION_USERS)
                    .document(userID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: successfully fetched the user document!");

                            User user = documentSnapshot.toObject(User.class);
                            ArrayList<String> joinedEvents = user.getJoinedEvents();

                            // Remove the current event from joinedEvents list
                            joinedEvents.remove(eventID);

                            // Update the event document with new joinedEvents
                            DB.collection(COLLECTION_USERS)
                                    .document(userID)
                                    .update(FIELD_JOINED_EVENTS, joinedEvents)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: successfully updated the user document!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: failed to update the user document = " + e.getLocalizedMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: failed to fetch the user document = " + e.getLocalizedMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "leaveEvent: Error while accessing the firestore collection = " + e.getLocalizedMessage());
        }

    }

}
