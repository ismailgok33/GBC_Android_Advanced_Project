package com.example.gbc_android_advanced_project.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.gbc_android_advanced_project.models.Event;
import com.example.gbc_android_advanced_project.repositories.EventRepository;

import java.util.ArrayList;
import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private static final String TAG = "EventViewModel";
    private  final EventRepository repository = new EventRepository();
    private static EventViewModel instance;
    public MutableLiveData<List<Event>> allEvents = new MutableLiveData<>();
    public MutableLiveData<List<Event>> joinedEventsContainer = new MutableLiveData<>();

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
        this.allEvents = this.repository.allEvents;
    }

    public void fetchJoinedEvents() {
        this.repository.fetchJoinedEvents();
        this.joinedEventsContainer = this.repository.joinedEventsContainer;
    }
}
