package com.example.gbc_android_advanced_project.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.gbc_android_advanced_project.databinding.ActivityEventDetailsBinding;
import com.example.gbc_android_advanced_project.models.Event;
import com.example.gbc_android_advanced_project.viewmodels.EventViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "EventDetailsActivity";
    private ActivityEventDetailsBinding binding;

    private Event currentEvent;
    private EventViewModel eventViewModel;

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.eventViewModel = EventViewModel.getInstance(this.getApplication());

        this.currentEvent = this.getIntent().getParcelableExtra("event_detail");
        Log.d(TAG, "setUIElements: currentEvent = " + currentEvent.toString());

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
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
        Glide.with(this).load(currentEvent.getImageURL()).into(this.binding.ivEventDetail);

        // Set Description
        this.binding.tvOrganizerDetail.setText(currentEvent.getDescription());
        this.binding.tvDateDetails.setText(currentEvent.getTimestamp().toDate().toString());
        this.binding.tvLocationNameDetails.setText(geoEncoder(currentEvent.getLat(), currentEvent.getLng()));

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