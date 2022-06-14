package com.example.gbc_android_advanced_project.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.gbc_android_advanced_project.models.Event;
import com.example.gbc_android_advanced_project.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private static final String TAG = "EventRepository";

    private final FirebaseFirestore DB;
    private final String COLLECTION_EVENTS = "events";
    private final String COLLECTION_USERS = "users";
    public MutableLiveData<List<Event>> allEvents = new MutableLiveData<>();
    public MutableLiveData<List<Event>> joinedEventsContainer = new MutableLiveData<>();

    public EventRepository()
    {
        DB = FirebaseFirestore.getInstance();
    }

    public void fetchAllEvents() {
        try {

            DB.collection(COLLECTION_EVENTS)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<Event> eventArrayList = new ArrayList<>();

                            if (queryDocumentSnapshots.isEmpty()) {
                                Log.d(TAG, "onSuccess: No document to fetch!");
                            }
                            else {
                                for(DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                    Event event = documentChange.getDocument().toObject(Event.class);
                                    event.setId(documentChange.getDocument().getId());
                                    eventArrayList.add(event);
                                }

                                allEvents.postValue(eventArrayList);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        catch (Exception e) {
            Log.e(TAG, "fetchAllEvents: throw an error = " + e.getLocalizedMessage());
        }
    }

    public void fetchJoinedEvents() {
        ArrayList<String> joinedEvents = new ArrayList<>();
        ArrayList<Event> eventArrayList = new ArrayList<>();

        try {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Fetch current user model
            DB.collection(COLLECTION_USERS)
                    .document(userID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            joinedEvents.removeAll(joinedEvents);
                            joinedEvents.addAll(user.getJoinedEvents());

                            // check if joinedEvents is empty for the current user
                            if (user.getJoinedEvents() == null || user.getJoinedEvents().size() == 0) {
                                joinedEventsContainer.setValue(eventArrayList);
                                return;
                            }

                            // Fetch the joined events for the current user
                            for (String eventID : joinedEvents) {
                                DB.collection(COLLECTION_EVENTS)
                                        .document(eventID)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Event event = documentSnapshot.toObject(Event.class);
                                                eventArrayList.add(event);
                                                joinedEventsContainer.setValue(eventArrayList);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: faield to fetch the joined event = " + e.getLocalizedMessage());
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Failed to fetch the current user from firestore = " + e.getLocalizedMessage());
                        }
                    });
        }
        catch (Exception e) {
            Log.e(TAG, "fetchJoinedEvents: Error while accessing Firestore collection = " + e.getLocalizedMessage());
        }
    }

}
