package com.apck.proyectfx.NavigationDrawerUsersFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apck.proyectfx.R;
import com.apck.proyectfx.adapter.EventAdapterUser;
import com.apck.proyectfx.adapter.Event_Adapter;
import com.apck.proyectfx.model.Event_model;
import com.apck.proyectfx.utilities.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NavigationDrawer_User_Events_Fragment extends Fragment {

    private FloatingActionButton addEventButton;

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private EventAdapterUser event_adapter;
    private List<Event_model> mlist;

    CircleImageView profile_image;
    TextView username;
    DatabaseReference databaseReference;
    private SearchView searchView;
    private Spinner spinner;

    public NavigationDrawer_User_Events_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer__user__events_, container, false);

        searchView = view.findViewById(R.id.SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (event_adapter != null){
                    event_adapter.getFilter().filter(s);
                }
                return false;
            }
        });

        spinner = view.findViewById(R.id.SpinerSearch);

        String[] options = {"Clasificar por mes","Enero", "febrero", "marzo", "abril", "mayo"," junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
                if(i==1){
                    searchView.setQuery("1", false);
                    searchView.clearFocus();
                }
                if(i==2){
                    searchView.setQuery("2", false);
                    searchView.clearFocus();
                }
                if(i==3){
                    searchView.setQuery("3", false);
                    searchView.clearFocus();
                }
                if (i==4){
                    searchView.setQuery("4", false);
                    searchView.clearFocus();
                }
                if(i==5){
                    searchView.setQuery("5", false);
                    searchView.clearFocus();
                }
                if(i==6){
                    searchView.setQuery("6", false);
                    searchView.clearFocus();
                }
                if(i==7){
                    searchView.setQuery("7", false);
                    searchView.clearFocus();
                }
                if (i==8){
                    searchView.setQuery("8", false);
                    searchView.clearFocus();
                }
                if(i==9){
                    searchView.setQuery("9", false);
                    searchView.clearFocus();
                }
                if(i==10){
                    searchView.setQuery("10", false);
                    searchView.clearFocus();
                }
                if(i==11){
                    searchView.setQuery("11", false);
                    searchView.clearFocus();
                }
                if (i==12){
                    searchView.setQuery("12", false);
                    searchView.clearFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        profile_image = view.findViewById(R.id.profile_imagen);
        username = (TextView) view.findViewById(R.id.username);
        recyclerView = view.findViewById(R.id.ReciclerviewEvents);
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
                event_adapter = new EventAdapterUser(getContext(), mlist);
                recyclerView.setAdapter(event_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}