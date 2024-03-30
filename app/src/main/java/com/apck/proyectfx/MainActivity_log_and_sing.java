package com.apck.proyectfx;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.apck.proyectfx.drawerMenu.DrawerMenuMainActivity;
import com.apck.proyectfx.drawerMenu.NavigationDrawerMunuUserMainActivity;
import com.apck.proyectfx.utilities.Constants;
import com.apck.proyectfx.utilities.Main_User_Activity;
import com.apck.proyectfx.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Objects;

public class MainActivity_log_and_sing extends AppCompatActivity {

    PreferenceManager preferenceManager;

    private EditText mEditTextNombre;
    private EditText mEditTextContrase;


    private String nombre = "";
    private String mail = "";
    private String isadmin;

    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main_log_and_sing);

        preferenceManager = new PreferenceManager(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextNombre = findViewById(R.id.editTextTextPersonName2);
        mEditTextContrase = findViewById(R.id.editTextTextPassword2);

        TextView mButonregis = findViewById(R.id.Regist);
        Button mButonLogin =  findViewById(R.id.Login);
        CardView mideoma =  findViewById(R.id.cambiarideoma);



        mButonregis.setOnClickListener(view -> startActivity(new Intent(MainActivity_log_and_sing.this, MainActivity.class)));

        mButonLogin.setOnClickListener(view -> {
            nombre = mEditTextNombre.getText().toString();
            mail = mEditTextContrase.getText().toString();

            if(!nombre.isEmpty() &&  !mail.isEmpty()){
                login();
            }else{
                Toast.makeText(MainActivity_log_and_sing.this, "Deve llenar los campos para continuar", Toast.LENGTH_SHORT).show();
            }
        });
        mideoma.setOnClickListener(view -> ShowChangeLanguageDialog());
    }

    private void login(){
            mAuth.signInWithEmailAndPassword(nombre, mail).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    singin();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo iniciar secion", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void acceslevel(String uid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTIONS_USERS)
                .whereEqualTo(Constants.KEY_USER_MAIL, mEditTextNombre.getText().toString())
                .whereEqualTo(Constants.KEY_USER_PASSWORD, mEditTextContrase.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0 ){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNEDIN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_USER_NAME, documentSnapshot.getString(Constants.KEY_USER_NAME));
                        preferenceManager.putString(Constants.KEY_USER_MAIL, documentSnapshot.getString(Constants.KEY_USER_MAIL));
                    }else{
                        Toast.makeText(getApplicationContext(), "unable to singin", Toast.LENGTH_SHORT).show();
                    }
                });

        mdatabase = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()){

                    isadmin= Objects.requireNonNull(datasnapshot.child("Admin").getValue()).toString();
                }
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser.isEmailVerified()){
                    if(isadmin.equals("1")){
                        startActivity(new Intent(MainActivity_log_and_sing.this, DrawerMenuMainActivity.class));
                    }else{
                        startActivity(new Intent(MainActivity_log_and_sing.this, NavigationDrawerMunuUserMainActivity.class));
                    }
                    finish();
                }else{
                    startActivity(new Intent(MainActivity_log_and_sing.this, ValidationActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            String curentuserid = FirebaseAuth.getInstance().getUid();
            FirebaseUser user = mAuth.getCurrentUser();
            assert curentuserid != null;
            mdatabase = FirebaseDatabase.getInstance().getReference().child("usuarios").child(curentuserid);
            mdatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    if(datasnapshot.exists()){
                        isadmin= Objects.requireNonNull(datasnapshot.child("Admin").getValue()).toString();
                    }
                    if (!user.isEmailVerified()){
                        startActivity(new Intent(MainActivity_log_and_sing.this, ValidationActivity.class));
                        finish();
                    }
                    if (user.isEmailVerified()){
                        if(isadmin.equals("1")){
                            startActivity(new Intent(MainActivity_log_and_sing.this, DrawerMenuMainActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(MainActivity_log_and_sing.this, NavigationDrawerMunuUserMainActivity.class));
                            finish();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }

    private void ShowChangeLanguageDialog(){
        final String[] listitems = {"español", "English"};
        AlertDialog.Builder nbuldier = new AlertDialog.Builder(MainActivity_log_and_sing.this);
        nbuldier.setSingleChoiceItems(listitems, -1, (dialogInterface, i) -> {
            if(i==0){
                setlocale("es");
                recreate();
            }else if(i==1){
                setlocale("en");
                recreate();
            }
            dialogInterface.dismiss();
        });
        AlertDialog mdialog = nbuldier.create();
        mdialog.show();
    }

    private void setlocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("my_lang", lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String lamguage = prefs.getString("my_lang", "");
        setlocale(lamguage);
    }

    private void singin(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTIONS_USERS)
                .whereEqualTo(Constants.KEY_USER_MAIL, mEditTextNombre.getText().toString())
                .whereEqualTo(Constants.KEY_USER_PASSWORD, mEditTextContrase.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0 ){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNEDIN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_USER_NAME, documentSnapshot.getString(Constants.KEY_USER_NAME));
                        preferenceManager.putString(Constants.KEY_USER_MAIL, documentSnapshot.getString(Constants.KEY_USER_MAIL));

                    }
                    acceslevel(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                });
    }
}