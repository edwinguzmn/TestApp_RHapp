package com.apck.proyectfx.utilities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.apck.proyectfx.Front_MainActivity;
import com.apck.proyectfx.MainActivity_log_and_sing;
import com.apck.proyectfx.R;
import com.apck.proyectfx.model.User;
import com.apck.proyectfx.profileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_User_Activity extends AppCompatActivity {

    int id;

    private FirebaseAuth mAuth;
    PreferenceManager preferenceManager;
    CircleImageView profile_image;
    TextView username;
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    boolean tokenState = false;

    CardView eventos;
    CardView organizacion;
    CardView comunicacion;
    CardView registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        preferenceManager = new PreferenceManager(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

        profile_image = findViewById(R.id.profile_imagen);
        username = findViewById(R.id.username);
        toolbar= findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);

        eventos= findViewById(R.id.event);
        organizacion = findViewById(R.id.organization);
        comunicacion = findViewById(R.id.comunication);
        registro = findViewById(R.id.regedit);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(Main_User_Activity.this).load(user.getImageURL()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                if(!tokenState){
                    sendFCMtokentodatabase(task.getResult());
                }
            }
        });

        eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), User_events_Activity.class));
            }
        });

        organizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "organization", Toast.LENGTH_SHORT).show();
            }
        });

        comunicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Front_MainActivity.class));
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Registro", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuflow, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        id = item.getItemId();

        if(id == R.id.menuitemprof){
            startActivity(new Intent(Main_User_Activity.this, profileActivity.class));

        }else if(id == R.id.menuitemlogout){
            Toast.makeText(getApplicationContext(), "singOut", Toast.LENGTH_SHORT).show();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference documentReference = database.collection(Constants.KEY_COLLECTIONS_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(updates).addOnSuccessListener(unused -> {
                mAuth.signOut();
                preferenceManager.clearPreferences();
                startActivity(new Intent(Main_User_Activity.this, MainActivity_log_and_sing.class));
                finish();

            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "no se pudo serar la secion", Toast.LENGTH_SHORT).show());

        }
        return super.onOptionsItemSelected(item);

    }

    private void sendFCMtokentodatabase(String token){
        tokenState = true;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTIONS_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "failuse ", Toast.LENGTH_SHORT).show());

    }

}