package com.apck.proyectfx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apck.proyectfx.adapter.HorizontalRecyclerViewAdaptger;
import com.apck.proyectfx.model.Form;
import com.apck.proyectfx.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawerUserRolModActivity extends AppCompatActivity {

    Button button;
    DatabaseReference databaseReference;
    DatabaseReference mDatabase;

    public TextView textName;
    public TextView textMail;
    public TextView userID;
    public TextView userRank;
    public TextView userStatus;
    public TextView dateOfJoin;
    public ImageView profileimage;
    public TextView requesting;

    String aplication;

    Spinner spinnerSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_user_rol_mod);

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

        String userId = getIntent().getStringExtra("uSerId");
        requesting = findViewById(R.id.selection);

        spinnerSelection = findViewById(R.id.spoinerRoleSelect);
        String[] options = {"Seleccionar rol: ","Empleado","Usuario"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, options);
        spinnerSelection.setAdapter(adapter);
        spinnerSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    requesting.setText("Seleccione un rol para el usuario");
                }
                if(i==1){
                    requesting.setText("Empleado");
                }
                if(i==2){
                    requesting.setText("Usuario");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textName = findViewById(R.id.username);
        textMail = findViewById(R.id.Usermaill);
        userID = findViewById(R.id.User_id);
        userRank = findViewById(R.id.Rank);
        userStatus = findViewById(R.id.userStatus);
        dateOfJoin = findViewById(R.id.Dateofjoint);
        profileimage = findViewById(R.id.profile_imagen);

        button = findViewById(R.id.editRool);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requesting.getText().equals("Empleado")){
                    updateValuesEmployee(userId);
                }
                if (requesting.getText().equals("Usuario")){
                    updateValuesUser(userId);
                }
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

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
                if(user.getImageURL().equals("default")){
                    profileimage.setImageResource(R.drawable.user13);
                }else{
                    Glide.with(Objects.requireNonNull(getApplicationContext())).load(user.getImageURL()).into(profileimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateValuesUser(String userId) {
        Toast.makeText(getApplicationContext(), "Start Role Changing", Toast.LENGTH_SHORT).show();
        HashMap<String, Object> map22 = new HashMap<>();
        map22.put("Status", "En proceso");
        mDatabase = FirebaseDatabase.getInstance().getReference("forms").child(userId);
        mDatabase.updateChildren(map22).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> taskUpd) {
                if (taskUpd.isSuccessful()){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Status", "En proceso");
                    databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);
                    databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mDatabase = FirebaseDatabase.getInstance().getReference("forms").child(userId);
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            Form form = snapshot.getValue(Form.class);
                                            assert form != null;
                                            aplication = form.getJob_Requesting();

                                            HashMap<String, Object> mapuser = new HashMap<>();
                                            mapuser.put("Rank", "Usuario");
                                            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);
                                            databaseReference.updateChildren(mapuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateValuesEmployee(String userId){
        Toast.makeText(getApplicationContext(), "Start Role Changing", Toast.LENGTH_SHORT).show();
        HashMap<String, Object> map22 = new HashMap<>();
        map22.put("Status", "Empleado");
        mDatabase = FirebaseDatabase.getInstance().getReference("forms").child(userId);
        mDatabase.updateChildren(map22).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> taskUpd) {
                if (taskUpd.isSuccessful()){

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Status", "Empleado");
                    databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);
                    databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mDatabase = FirebaseDatabase.getInstance().getReference("forms").child(userId);
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            Form form = snapshot.getValue(Form.class);
                                            assert form != null;
                                            aplication = form.getJob_Requesting();
                                            Log.d("Mytag", aplication);

                                            HashMap<String, Object> mapuser = new HashMap<>();
                                            mapuser.put("Rank", aplication);
                                            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);
                                            databaseReference.updateChildren(mapuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}