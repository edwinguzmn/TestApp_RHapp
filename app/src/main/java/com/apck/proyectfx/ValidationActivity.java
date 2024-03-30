package com.apck.proyectfx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apck.proyectfx.drawerMenu.DrawerMenuMainActivity;
import com.apck.proyectfx.drawerMenu.NavigationDrawerMunuUserMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ValidationActivity extends AppCompatActivity {

    private Button mReeviar;
    private String isadmin;
    private TextView textView;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    private Boolean current = true;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        Button cancelBut = findViewById(R.id.REembutton_Cancel);
        user.sendEmailVerification();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userUpdate();

        mReeviar = findViewById(R.id.REembutton);
        textView = findViewById(R.id.userMailV);
        String userMail = getIntent().getStringExtra("userMail");
        textView.setText(userMail);
        mReeviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.sendEmailVerification();
                Toast.makeText(ValidationActivity.this, "Verificacion de correo embiado", Toast.LENGTH_SHORT).show();

            }
        });

        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity_log_and_sing.class));
                finish();
            }
        });

    }


    public void userUpdate(){
        if (current) {

            if (user.isEmailVerified()) {
                acceslevel(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                current=false;

            } else {
                user.reload();
            }
            update();
        }
    }

    private void update(){

        final Handler handler = new Handler();
        final Runnable runable = new Runnable() {
            @Override
            public void run() {
                userUpdate();
            }
        };

        handler.postDelayed(runable, 2000);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ValidationActivity.this, ValidationActivity.class);
        startActivity(intent);
        finish();
    }

    private void acceslevel(String uid) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()){
                    isadmin= Objects.requireNonNull(datasnapshot.child("Admin").getValue()).toString();
                }
                if(isadmin.equals("1")){
                    startActivity(new Intent(ValidationActivity.this, DrawerMenuMainActivity.class));
                }else{
                    startActivity(new Intent(ValidationActivity.this, NavigationDrawerMunuUserMainActivity.class));
                }
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}