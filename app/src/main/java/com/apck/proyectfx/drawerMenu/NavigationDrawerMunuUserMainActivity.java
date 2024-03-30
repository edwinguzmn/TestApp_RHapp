package com.apck.proyectfx.drawerMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apck.proyectfx.MainActivity_log_and_sing;
import com.apck.proyectfx.R;
import com.apck.proyectfx.model.User;
import com.apck.proyectfx.utilities.Constants;
import com.apck.proyectfx.utilities.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
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

public class NavigationDrawerMunuUserMainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private TextView userName, userMail;
    private ImageView profileImage;
    private FirebaseAuth mAuth;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_munu_user_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.ImageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        preferenceManager = new PreferenceManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);
                View hView =  navigationView.getHeaderView(0);
                userName = (TextView)hView.findViewById(R.id.textViewName);
                userMail = (TextView)hView.findViewById(R.id.textMail);
                profileImage = (ImageView)hView.findViewById(R.id.profile_imagen);
                assert user != null;
                userName.setText(user.getUsername());
                userMail.setText(user.getMail());
                if(user.getImageURL().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(NavigationDrawerMunuUserMainActivity.this).load(user.getImageURL()).into(profileImage);
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

        navigationView.getMenu().findItem(R.id.menuLogout).setOnMenuItemClickListener(menuItem -> {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference documentReference = database.collection(Constants.KEY_COLLECTIONS_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(updates).addOnSuccessListener(unused -> {
                mAuth.signOut();
                preferenceManager.clearPreferences();
                startActivity(new Intent(NavigationDrawerMunuUserMainActivity.this, MainActivity_log_and_sing.class));
                finish();

            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "no se pudo serar la secion", Toast.LENGTH_SHORT).show());
            return true;
        });
    }
    private void sendFCMtokentodatabase(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTIONS_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "failuse ", Toast.LENGTH_SHORT).show());

    }
}