package com.example.gbc_android_advanced_project.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.gbc_android_advanced_project.R;
import com.example.gbc_android_advanced_project.databinding.ActivityEventDetailsBinding;
import com.example.gbc_android_advanced_project.models.Event;
import com.example.gbc_android_advanced_project.viewmodels.EventViewModel;
import com.example.gbc_android_advanced_project.viewmodels.UserViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "EventDetailsActivity";
    private final String STORAGE_IMAGES = "event_images";
    private ActivityEventDetailsBinding binding;

    private Event currentEvent;
    private EventViewModel eventViewModel;
    private UserViewModel userViewModel;

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.eventViewModel = EventViewModel.getInstance(this.getApplication());
        this.userViewModel = UserViewModel.getInstance(this.getApplication());

        this.currentEvent = this.getIntent().getParcelableExtra("event_detail");
        Log.d(TAG, "setUIElements: currentEvent = " + currentEvent.toString());

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = this.binding.mapEventDetail;
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

         setUIElements();
    }

    private void setUIElements() {
        // Load Image from URL
        // get image downloadURL from Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child(STORAGE_IMAGES + "/" + currentEvent.getImageURL());
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess: downloadURL = " + uri);
                Glide.with(EventDetailsActivity.this).load(uri).into(binding.ivEventDetail);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: failed to get the downloadURL from Firebase Storage = " + e.getLocalizedMessage());
            }
        });

        // Set Description
        this.binding.tvOrganizerDetail.setText(currentEvent.getDescription());
        this.binding.tvDateDetails.setText(currentEvent.getTimestamp().toDate().toString());
        this.binding.tvLocationNameDetails.setText(geoEncoder(currentEvent.getLat(), currentEvent.getLng()));

        this.setInitialButtonStyle();
        this.binding.btnJoinEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleEventButtonStyle();
            }
        });

    }

    private String geoEncoder(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String fullAddress = "";
//        String stateName = "";
//        String countryName = "";
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            fullAddress = addresses.get(0).getAddressLine(0);
//            stateName = addresses.get(0).getAddressLine(1);
//            countryName = addresses.get(0).getAddressLine(2);
        } catch (IOException e) {
            Log.e(TAG, "geoEncoder: Failed to encode to an address", e);
        }

        return fullAddress;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latLng = new LatLng(currentEvent.getLat(), currentEvent.getLng());
        map.addMarker(new MarkerOptions().position(latLng).title(currentEvent.getName()));
//        map.setMaxZoomPreference(20f);
//        map.setMaxZoomPreference(15f);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void toggleEventButtonStyle() {
        if (this.binding.btnJoinEvent.getText().equals(getString(R.string.join_event_detail))) {

            // leave event first
            this.userViewModel.joinEvent(currentEvent.getId());

            // set button style accordingly
            this.binding.btnJoinEvent.setText(getString(R.string.leave_event_detail));
            this.binding.btnJoinEvent.setBackgroundColor(getColor(R.color.leave_event_color));
        }
        else {
            // join event first
            this.userViewModel.leaveEvent(currentEvent.getId());

            // set button style accordingly
            this.binding.btnJoinEvent.setText(getString(R.string.join_event_detail));
            this.binding.btnJoinEvent.setBackgroundColor(getColor(R.color.join_event_color));
        }
    }

    private void setInitialButtonStyle() {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (this.currentEvent.getJoinedUsers().stream().anyMatch(userID -> userID.equalsIgnoreCase(currentUserID))) {
            this.binding.btnJoinEvent.setText(getString(R.string.leave_event_detail));
            this.binding.btnJoinEvent.setBackgroundColor(getColor(R.color.leave_event_color));
        }
        else {
            this.binding.btnJoinEvent.setText(getString(R.string.join_event_detail));
            this.binding.btnJoinEvent.setBackgroundColor(getColor(R.color.join_event_color));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}