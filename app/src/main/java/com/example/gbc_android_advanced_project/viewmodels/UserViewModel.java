package com.example.gbc_android_advanced_project.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.gbc_android_advanced_project.models.User;
import com.example.gbc_android_advanced_project.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private  final UserRepository repository = new UserRepository();
    private static UserViewModel instance;
    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public static UserViewModel getInstance(Application application){
        if (instance == null){
            instance = new UserViewModel(application);
        }
        return instance;
    }

    public void addUser(User user){
        this.repository.addUser(user);
    }

    public void joinEvent(String eventID) {
        this.repository.joinEvent(eventID);
    }

    public void leaveEvent(String eventID) {
        this.repository.leaveEvent(eventID);
    }

}
