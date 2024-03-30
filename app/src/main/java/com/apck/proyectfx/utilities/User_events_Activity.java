package com.apck.proyectfx.utilities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.apck.proyectfx.R;
import com.apck.proyectfx.adapter.EventAdapterUser;
import com.apck.proyectfx.adapter.Event_Adapter;
import com.apck.proyectfx.model.Event_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_events_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private EventAdapterUser eventAdapterUser;
    private List<Event_model> mlist;

    CircleImageView profile_image;
    TextView username;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);

        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        profile_image = findViewById(R.id.profile_imagen);
        username = (TextView) findViewById(R.id.username);


        recyclerView = findViewById(R.id.Recicler_view_events);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mlist = new ArrayList<>();

        readEvents();
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
                eventAdapterUser = new EventAdapterUser(getApplicationContext(), mlist);
                recyclerView.setAdapter(eventAdapterUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}