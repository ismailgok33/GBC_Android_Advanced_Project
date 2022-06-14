package com.example.gbc_android_advanced_project.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gbc_android_advanced_project.OnRowClicked;
import com.example.gbc_android_advanced_project.databinding.CustomRowLayoutBinding;
import com.example.gbc_android_advanced_project.models.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private static final String TAG = "EventAdapter";
    private final Context context;
    private final ArrayList<Event> dataSourceArray;
    CustomRowLayoutBinding binding;
    private final OnRowClicked clickListener;

    public EventAdapter(Context context, ArrayList<Event> dataSourceArray, OnRowClicked clickListener) {
        this.context = context;
        this.dataSourceArray = dataSourceArray;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(CustomRowLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event item = dataSourceArray.get(position);
        holder.bind(context, item, clickListener);
    }

    @Override
    public int getItemCount() {
        Log.d("MyAdapter", "getItemCount: Number of items " +this.dataSourceArray.size() );
        return this.dataSourceArray.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CustomRowLayoutBinding itemBinding;
        private final String STORAGE_IMAGES = "event_images";

        public EventViewHolder(CustomRowLayoutBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, Event currentEvent, OnRowClicked clickListener){
            itemBinding.nameofEvent.setText(currentEvent.getName());
            itemBinding.date.setText(currentEvent.getTimestamp().toDate().toString());

            // get image downloadURL from Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imagesRef = storageRef.child(STORAGE_IMAGES + "/" + currentEvent.getImageURL());
            imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d(TAG, "onSuccess: downloadURL = " + uri);
                    Glide.with(context).load(uri).into(itemBinding.imgView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: failed to get the downloadURL from Firebase Storage = " + e.getLocalizedMessage());
                }
            });

            Integer total = 0;
            //itemBinding.date.setText("Price is: $" + currProduct.getTimestamp());
            for (int i = 0; i < currentEvent.getJoinedUsers().size(); i++) {
                total = total + 1;
            }
            itemBinding.userGoing.setText(total.toString()+ " USERS ARE GOING");
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRowClicked(currentEvent);
                }
            });
        }

    }

}
