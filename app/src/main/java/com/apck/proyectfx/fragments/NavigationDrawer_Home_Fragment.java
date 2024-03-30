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
import android.widget.Toast;

import com.apck.proyectfx.MessageActivity;
import com.apck.proyectfx.R;
import com.apck.proyectfx.adapter.LogEventAdapter;
import com.apck.proyectfx.adapter.SortModeAdapter;
import com.apck.proyectfx.model.ConfigModel;
import com.apck.proyectfx.model.LogEvents;
import com.apck.proyectfx.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NavigationDrawer_Home_Fragment extends Fragment {

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private TextView username;
    private List<LogEvents> mlist;
    private LogEventAdapter logEventAdapter;
    private RecyclerView recyclerView;

    public NavigationDrawer_Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer__home_, container, false);

        username = view.findViewById(R.id.Username);
        recyclerView = view.findViewById(R.id.ReciclerViewUsersLog);
        mlist = new ArrayList<>();
        //loadConfig();

        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager1);

        readLogEvents();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    return view;
    }

    private void readLogEvents() {
        reference = FirebaseDatabase.getInstance().getReference("LogEvents");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LogEvents logEvent = snapshot.getValue(LogEvents.class);
                    assert logEvent != null;
                    mlist.add(logEvent);
                }
                logEventAdapter = new LogEventAdapter(getContext(), mlist);
                recyclerView.setAdapter(logEventAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadConfig(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Config").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ConfigModel configModel = snapshot.getValue(ConfigModel.class);
                if (configModel.getNotifications().equals("true")){
                    Toast.makeText(getContext(), "make message", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}