package com.example.gbc_android_advanced_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Event implements Parcelable {
    String id;
    String name;
    String description;
    String organizer;
    double lat;
    double lng;
    String imageURL;
    ArrayList<String> joinedUsers;
    Timestamp timestamp;

    public Event() {}

    public Event(String name, String description, String organizer, double lat, double lng, String imageURL, Timestamp timestamp) {
        this.name = name;
        this.description = description;
        this.organizer = organizer;
        this.lat = lat;
        this.lng = lng;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
    }

    public Event(String name, String description, String organizer, double lat, double lng, String imageURL, ArrayList<String> joinedUsers, Timestamp timestamp) {
        this.name = name;
        this.description = description;
        this.organizer = organizer;
        this.lat = lat;
        this.lng = lng;
        this.imageURL = imageURL;
        this.joinedUsers = joinedUsers;
        this.timestamp = timestamp;
    }

    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        organizer = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        imageURL = in.readString();
        joinedUsers = in.readArrayList(String.class.getClassLoader());
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<String> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(ArrayList<String> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", organizer='" + organizer + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", imageURL='" + imageURL + '\'' +
                ", joinedUsers=" + joinedUsers +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(organizer);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(imageURL);
        dest.writeList(joinedUsers);
        dest.writeParcelable(timestamp, flags);
    }
}
