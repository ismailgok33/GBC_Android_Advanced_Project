package com.example.gbc_android_advanced_project.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gbc_android_advanced_project.R;
import com.example.gbc_android_advanced_project.databinding.ActivityMainBinding;
import com.example.gbc_android_advanced_project.models.Event;
import com.example.gbc_android_advanced_project.viewmodels.EventViewModel;
import com.google.firebase.Timestamp;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnGoToEventDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event("Test Event",
                        "This is a test event and this is its description",
                        "Organizer name and other descriptions about it",
                        43.661305213236865,
                        -79.43067294897202,
                        "https://picsum.photos/200",
                        new Timestamp(new Date()));

                Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);
                intent.putExtra("event_detail", event);
                startActivity(intent);
            }
        });
    }
}