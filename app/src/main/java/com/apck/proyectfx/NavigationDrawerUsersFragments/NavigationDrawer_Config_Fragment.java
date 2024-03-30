package com.apck.proyectfx.NavigationDrawerUsersFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.apck.proyectfx.R;
import com.apck.proyectfx.model.ConfigModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class NavigationDrawer_Config_Fragment extends Fragment {

    private SwitchCompat optionSwitch;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public NavigationDrawer_Config_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer__config_, container, false);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();

        optionSwitch = view.findViewById(R.id.switch1);
        assert userId != null;
        databaseReference = FirebaseDatabase.getInstance().getReference("Config").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ConfigModel configModel = snapshot.getValue(ConfigModel.class);
                    if (configModel.getNotifications().equals("true")){
                        optionSwitch.setText("Activado");
                        optionSwitch.setChecked(true);
                    }else{
                        optionSwitch.setText("Desactivado");
                        optionSwitch.setChecked(false);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        optionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    optionSwitch.setText("Activado");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("notifications", "true");
                    databaseReference.updateChildren(map);
                }
                if (!b){
                    optionSwitch.setText("Desactivado");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("notifications", "false");
                    databaseReference.updateChildren(map);
                }
            }
        });

        return view;
    }

}