package com.apck.proyectfx.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apck.proyectfx.R;
import com.apck.proyectfx.adapter.Event_Adapter;
import com.apck.proyectfx.model.Event_model;
import com.apck.proyectfx.utilities.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventViewFragment extends Fragment {

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private Event_Adapter event_adapter;
    private List<Event_model> mlist;

    CircleImageView profile_image;
    TextView username;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_view, container, false);

        mAuth = FirebaseAuth.getInstance();

        profile_image = view.findViewById(R.id.profile_imagen);
        username = (TextView) view.findViewById(R.id.username);


        recyclerView = view.findViewById(R.id.Recicler_view_events);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mlist = new ArrayList<>();

        readEvents();

        return view;
    }

    private void readEvents() {
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.KEY_DATABASE_EVENT_PHAT);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Event_model eventModel = snapshot.getValue(Event_model.class);
                    mlist.add(eventModel);
                }
                event_adapter = new Event_Adapter(getContext(), mlist);
                recyclerView.setAdapter(event_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}