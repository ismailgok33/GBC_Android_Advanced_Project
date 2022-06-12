package com.example.gbc_android_advanced_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    private String id;
    private String email;
    private ArrayList<String> joinedEvents;

    public User() {}

    public User(String email) {
        this.email = email;
        joinedEvents = new ArrayList<>();
    }

    public User(String email, ArrayList<String> joinedEvents) {
        this.email = email;
        this.joinedEvents = joinedEvents;
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        joinedEvents = in.readArrayList(String.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getJoinedEvents() {
        return joinedEvents;
    }

    public void setJoinedEvents(ArrayList<String> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", joinedEvents=" + joinedEvents +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeList(joinedEvents);
    }

}
