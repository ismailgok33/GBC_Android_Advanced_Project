package com.example.gbc_android_advanced_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<String> joinedEvents;

    public User() {}

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        joinedEvents = new ArrayList<>();
    }

    public User(String uid, String email, String firstName, String lastName) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        joinedEvents = new ArrayList<>();
    }

    public User(String email, String firstName, String lastName, ArrayList<String> joinedEvents) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinedEvents = joinedEvents;
    }

    public User(String uid, String email, String firstName, String lastName, ArrayList<String> joinedEvents) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinedEvents = joinedEvents;
    }

    protected User(Parcel in) {
        uid = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
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
        return uid;
    }

    public void setId(String id) {
        this.uid = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", joinedEvents=" + joinedEvents +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeList(joinedEvents);
    }

}
