package com.apck.proyectfx;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apck.proyectfx.model.User;
import com.apck.proyectfx.utilities.Constants;
import com.apck.proyectfx.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;

    private EditText mEditTextNombre;
    private EditText mEditTextCorreo;
    private EditText mEditTextContrase;
    private EditText mConfircontraseña;
    private CheckBox checkusuario;
    private CheckBox checkAdmin;
    private int userNumber;

    private String nombre;
    private String id;
    private  String correo;
    private String contra;
    private String conpass;
    Locale id3 = new Locale("en", "EN");
    SimpleDateFormat simpleDateFormatNew = new SimpleDateFormat("yyyy-MM-dd", id3);
    SimpleDateFormat simpleDateFormatDateJoin = new SimpleDateFormat("dd/MM/yyy HH:mm aa", id3);

    List<User> users;

    private Boolean pass_mach = false;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());


        Calendar calendar = Calendar.getInstance();
        String query_SearchDate = simpleDateFormatNew.format(Calendar.getInstance().getTime());
        String currentDate = simpleDateFormatDateJoin.format(Calendar.getInstance().getTime());

        String year_Search = Calendar.getInstance().get(Calendar.YEAR) +":STRING_REFERENCE_KEY_YEAR";
        String Month_Search = Calendar.getInstance().get(Calendar.MONTH) + ":STRING_REFERENCE_KEY_MONTH";
        String Day_Search = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + ":STRING_REFERENCE_KEY_DAY";


        users = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mEditTextNombre = findViewById(R.id.editTextTextPersonName);
        mEditTextCorreo = findViewById(R.id.editTextTextEmailAddress);
        mEditTextContrase = findViewById(R.id.editTextTextPassword);
        mConfircontraseña = findViewById(R.id.Confirpassword);
        Button mButonreg = findViewById(R.id.buttonREG);
        checkusuario = findViewById(R.id.checkBoxUsuario);
        checkAdmin = findViewById(R.id.checkBoxAdmin);

        getUsersCount();

        checkusuario.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                checkAdmin.setChecked(false);
            }
        });

        checkAdmin.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                checkusuario.setChecked(false);
            }
        });

        mButonreg.setOnClickListener(view -> {
            nombre = mEditTextNombre.getText().toString();
            correo = mEditTextCorreo.getText().toString();
            contra = mEditTextContrase.getText().toString();
            conpass = mConfircontraseña.getText().toString();
            if(!nombre.isEmpty() && mailVal(correo) && !contra.isEmpty()  && !conpass.isEmpty() && checkAdmin.isChecked() || checkusuario.isChecked()){
                passmach(contra);
                if(contra.length() >= 6  && pass_mach){
                    //intent.putExtra("useMail",correo);
                    registrarusuario(currentDate, year_Search, Month_Search, Day_Search, query_SearchDate);
                }else{
                    Toast.makeText(MainActivity.this, "ingrese por lo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(MainActivity.this, "deve llenar los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarusuario(String currentDate, String year_Search,  String Month_Search, String Day_Search, String query_SearchDate){
        mAuth.createUserWithEmailAndPassword(correo, contra).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                userNumber = userNumber+1;
                id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("username", nombre);
                map.put("mail", correo);
                map.put("Contraseña", contra);
                map.put("imageURL", "default");
                map.put("Number", userNumber);
                map.put("Date_of_join", currentDate);
                map.put("search_KEY_MONTH", Month_Search);
                map.put("search_KEY_YEAR", year_Search);
                map.put("search_KEY_DAY", Day_Search);
                map.put("query_date_Search", query_SearchDate);
                if(checkusuario.isChecked()){
                    map.put("Admin", "0");
                    map.put("Rank","Usuario");
                    map.put("Status","Sin empezar");
                    map.put("job_Requesting", "not assigned");
                }
                if(checkAdmin.isChecked()){
                    map.put("Admin", "1");
                    map.put("Rank","Director de RH");
                    map.put("Status","Empleado");
                    map.put("job_Requesting", "Director de RH");
                }

                database = FirebaseFirestore.getInstance();
                HashMap<String, Object> user = new HashMap<>();
                user.put(Constants.KEY_USER_NAME, mEditTextNombre.getText().toString());
                user.put(Constants.KEY_USER_MAIL, mEditTextCorreo.getText().toString());
                user.put(Constants.KEY_USER_PASSWORD, mEditTextContrase.getText().toString());

                database.collection(Constants.KEY_COLLECTIONS_USERS).add(user).addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNEDIN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_USER_NAME, mEditTextNombre.getText().toString());
                    preferenceManager.putString(Constants.KEY_USER_MAIL, mEditTextCorreo.getText().toString());

                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());


                String id = mAuth.getCurrentUser().getUid();
                mDatabase.child("usuarios").child(id).setValue(map).addOnCompleteListener(task2 -> {
                    if(task2.isSuccessful()){
                        FirebaseUser user1 = mAuth.getCurrentUser();
                        user1.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Verificacion de correo embiado", Toast.LENGTH_SHORT).show();
                        pass_mach=false;

                        HashMap<String, String> Event = new HashMap<>();
                        Event.put("text1", "El usuario");
                        Event.put("username_LogEvent", nombre);
                        Event.put("text2", "se ha unido a la aplicación el dia ");
                        Event.put("date_LogEvent", currentDate);
                        mDatabase.child("LogEvents").push().setValue(Event);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        HashMap<String, Object> configmap = new HashMap<>();
                        configmap.put("notifications", "true");
                        configmap.put("configId", id);
                        databaseReference.child("Config").child(id).setValue(configmap).addOnCompleteListener(task4 -> {
                            if (task4.isSuccessful()){
                                Intent intent = new Intent( getApplicationContext(), ValidationActivity.class);
                                intent.putExtra("userMail", correo);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "no se pudo crear configuracion motivo: "+e.getMessage(), Toast.LENGTH_SHORT).show());
                    }else{
                        Toast.makeText(MainActivity.this, "sus datos no han sido guardados", Toast.LENGTH_SHORT).show();
                    }

                });
            }else{
                Toast.makeText(MainActivity.this, "no se pudo registrar", Toast.LENGTH_SHORT).show();
            }
        });
        }

    private Boolean mailVal(String correo){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (correo == null)
            return false;
        return pat.matcher(correo).matches();
    }

    private void passmach(String contra){
        if(contra.isEmpty()){
            pass_mach=false;
            Toast.makeText(getApplicationContext(), "Deve llenar el campo", Toast.LENGTH_SHORT).show();
        }
        if(contra.equals(conpass)){
            pass_mach=true;
        }else{
            Toast.makeText(getApplicationContext(), "No concuerda", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUsersCount(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    users.add(user);
                }
                userNumber = users.size();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}