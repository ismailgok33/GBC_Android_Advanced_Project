package com.example.gbc_android_advanced_project.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gbc_android_advanced_project.R;
import com.example.gbc_android_advanced_project.adapters.EventAdapter;
import com.example.gbc_android_advanced_project.databinding.FragmentAllEventsBinding;
import com.example.gbc_android_advanced_project.databinding.FragmentJoinedEventsBinding;
import com.example.gbc_android_advanced_project.models.Event;
import com.example.gbc_android_advanced_project.view.EventDetailsActivity;
import com.example.gbc_android_advanced_project.viewmodels.EventViewModel;

import java.util.ArrayList;
import java.util.List;

public class JoinedEventsFragment extends Fragment {

    private static final String TAG = "JoinedEventsFragment";
    FragmentJoinedEventsBinding binding;

    private ArrayList<Event> eventArrayList = new ArrayList<>();
    private EventAdapter adapter;
    private EventViewModel eventViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = binding.inflate(inflater, container, false);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.eventViewModel = eventViewModel.getInstance(this.getActivity().getApplication());
        super.onViewCreated(view, savedInstanceState);

        this.adapter = new EventAdapter(view.getContext(), this.eventArrayList, this::OnRowClicked);
        // configure the recyclerview to use the adapter
        binding.rvItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.rvItems.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        binding.rvItems.setAdapter(this.adapter);

        this.eventViewModel.fetchJoinedEvents();
        this.eventViewModel.joinedEventsContainer.observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                if (events.isEmpty()){
                    Log.e(TAG, "onChanged: No documents received");
                }else{
                    for(Event event : events){
                        Log.d(TAG, "onChanged: event : " + event.toString() );
                    }
                    eventArrayList.clear();
                    eventArrayList.addAll(events);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        this.eventViewModel.fetchJoinedEvents();
    }


    private void OnRowClicked(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
        intent.putExtra("event_detail", event);
        startActivity(intent);
    }
}