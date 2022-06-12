package com.example.gbc_android_advanced_project.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.gbc_android_advanced_project.repositories.EventRepository;

public class EventViewModel extends AndroidViewModel {

    private static final String TAG = "EventViewModel";
    private  final EventRepository repository = new EventRepository();
    private static EventViewModel instance;

    public EventViewModel(@NonNull Application application) {
        super(application);
    }

    public static EventViewModel getInstance(Application application){
        if (instance == null){
            instance = new EventViewModel(application);
        }
        return instance;
    }

    public void fetchAllEvents() {
        this.repository.fetchAllEvents();
    }
}
