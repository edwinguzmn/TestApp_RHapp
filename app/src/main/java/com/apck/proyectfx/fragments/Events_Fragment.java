package com.apck.proyectfx.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apck.proyectfx.EventActivity;
import com.apck.proyectfx.R;


public class Events_Fragment extends Fragment {

    CardView cardViewEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_opt3_, container, false);

        cardViewEvents = view.findViewById(R.id.event);
        cardViewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EventActivity.class));
            }
        });


        return view;
    }
}