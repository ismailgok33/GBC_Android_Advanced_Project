package com.example.gbc_android_advanced_project.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

public class EventRepository {
    private static final String TAG = "EventRepository";

    private final FirebaseFirestore DB;
    private final String COLLECTION_EVENTS = "events";

    public EventRepository()
    {
        DB = FirebaseFirestore.getInstance();
    }

    public void fetchAllEvents() {

    }
}
