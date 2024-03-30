package com.apck.proyectfx.NavigationDrawerUsersFragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apck.proyectfx.R;
import com.apck.proyectfx.adapter.LogEventAdapter;
import com.apck.proyectfx.model.LogEvents;
import com.apck.proyectfx.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NavigationDrawer_home_Fragment extends Fragment {

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private TextView username;

    public TextView textName;
    public TextView textMail;
    public TextView userID;
    public TextView userRank;
    public TextView userStatus;
    public TextView dateOfJoin;

    public NavigationDrawer_home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer_home_, container, false);

        username = view.findViewById(R.id.Username);

        textName = view.findViewById(R.id.username);
        textMail = view.findViewById(R.id.Usermaill);
        userID = view.findViewById(R.id.User_id);
        userRank = view.findViewById(R.id.Rank);
        userStatus = view.findViewById(R.id.userStatus);
        dateOfJoin = view.findViewById(R.id.Dateofjoint);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    username.setText(user.getUsername());
                    textName.setText(user.getUsername());
                    textMail.setText(user.getMail());
                    userID.setText(user.getId());
                    userRank.setText(user.getRank());
                    userStatus.setText(user.getStatus());
                    dateOfJoin.setText(user.getDate_of_join());
                    if(user.getRank().equals("Director de RH")){
                        userRank.setTextColor(Color.parseColor("#90D487"));
                    }
                    if(user.getStatus().equals("Empleado")){
                        userStatus.setTextColor(Color.parseColor("#90D487"));
                    }
                    if(user.getStatus().equals("Sin empezar")){
                        userStatus.setTextColor(Color.parseColor("#FF6060"));
                    }
                    if(user.getStatus().equals("En proceso")){
                        userStatus.setTextColor(Color.parseColor("#FFE660"));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }
}