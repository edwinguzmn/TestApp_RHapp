package com.apck.proyectfx;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.apck.proyectfx.adapter.VPaddapter;
import com.apck.proyectfx.model.User;
import com.apck.proyectfx.utilities.Constants;
import com.apck.proyectfx.utilities.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

public class Main_main_Act extends AppCompatActivity {

    int id;

    private FirebaseAuth mAuth;
    private final String[] titles = new String[]{"Chats","V.llamada","eventos"};

    PreferenceManager preferenceManager;

    CircleImageView profile_image;
    TextView username;

    Toolbar toolbar;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    VPaddapter vPaddapter;
    TabLayout tableLayout;
    ViewPager2 viewPager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);

        preferenceManager = new PreferenceManager(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

        profile_image = findViewById(R.id.profile_imagen);
        username = findViewById(R.id.username);
        toolbar= findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);

        viewPager2 = findViewById(R.id.viewpager22);
        tableLayout = findViewById(R.id.tablayaout);

        vPaddapter = new VPaddapter(this);
        viewPager2.setAdapter(vPaddapter);

        new TabLayoutMediator(tableLayout, viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(android.R.drawable.sym_def_app_icon);
                }else{
                    Glide.with(Main_main_Act.this).load(user.getImageURL()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                sendFCMtokentodatabase(task.getResult());
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
            startActivity(new Intent(Main_main_Act.this, profileActivity.class));

        }else if(id == R.id.menuitemlogout){
            //Toast.makeText(getApplicationContext(), "singOut", Toast.LENGTH_SHORT).show();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference documentReference = database.collection(Constants.KEY_COLLECTIONS_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(updates).addOnSuccessListener(unused -> {
                mAuth.signOut();
                preferenceManager.clearPreferences();
                startActivity(new Intent(Main_main_Act.this, MainActivity_log_and_sing.class));
                finish();

            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "no se pudo serar la secion", Toast.LENGTH_SHORT).show());

        }
        return super.onOptionsItemSelected(item);


    }

    private void sendFCMtokentodatabase(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTIONS_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "failuse ", Toast.LENGTH_SHORT).show());

    }


}