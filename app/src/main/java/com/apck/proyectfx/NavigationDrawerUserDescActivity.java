package com.apck.proyectfx;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apck.proyectfx.model.Form;
import com.apck.proyectfx.model.User;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class NavigationDrawerUserDescActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButtonVideoCall, floatingActionButtonChat, floatingActionButtonEditUserRole;
    TextView textViewUserid;

    private TextView textName;
    private TextView textMail;
    private TextView userRank;
    private TextView userStatus;
    private TextView dateOfJoin;
    private ImageView profileimage;
    DatabaseReference databaseReference;

    private TextView company_1;
    private TextView Company_1_dateStart;
    private TextView Company_1_dateEnd;
    private TextView Company_1_inCharge;
    private TextView company_2;
    private TextView Company_2_dateStart;
    private TextView Company_2_dateEnd;
    private TextView Company_2_inCharge;
    private TextView degree_TimeStar;
    private TextView degree_TimeEnd;
    private TextView degree_school;
    private TextView degree_Name;
    private TextView degree_TimeStar_2;
    private TextView degree_TimeEnd_2;
    private TextView degree_school_2;
    private TextView degree_Name_2;
    private TextView requesting;

    private TextView TextEdu;
    private TextView TextExpProf;
    private TextView TextApli;
    private TextView Space,space2,space33,space332;
    private ImageView image1, image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_user_desc);

        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        textName = findViewById(R.id.usename);
        textMail = findViewById(R.id.usermail);
        userRank = findViewById(R.id.userRank);
        userStatus = findViewById(R.id.userStatus);
        dateOfJoin = findViewById(R.id.UserDateOfJoint);
        profileimage = findViewById(R.id.profile_imagen);

        company_1 = findViewById(R.id.factoriname);
        Company_1_dateStart = findViewById(R.id.fechadetrabajoinicial);
        Company_1_dateEnd = findViewById(R.id.fechadetrabajofinal);
        Company_1_inCharge = findViewById(R.id.descripciondeaempresa);
        company_2 = findViewById(R.id.factoriname2);
        Company_2_dateStart = findViewById(R.id.fechadetrabajoinicial2);
        Company_2_dateEnd = findViewById(R.id.fechadetrabajofinal2);
        Company_2_inCharge = findViewById(R.id.descripciondeaempresa2);
        degree_TimeStar = findViewById(R.id.date);
        degree_TimeEnd = findViewById(R.id.dateend);
        degree_school = findViewById(R.id.school1);
        degree_Name = findViewById(R.id.degree);
        degree_TimeStar_2 = findViewById(R.id.date2);
        degree_TimeEnd_2 = findViewById(R.id.dateend2);
        degree_school_2 = findViewById(R.id.school12);
        degree_Name_2 = findViewById(R.id.degree2);
        requesting = findViewById(R.id.jobAplication);
        Space = findViewById(R.id.space);
        space2 = findViewById(R.id.space2);
        space33 = findViewById(R.id.space33);
        space332 = findViewById(R.id.space332);
        image1 = findViewById(R.id.woooooooooo);
        image2 = findViewById(R.id.woooooooooo21);


        TextEdu = findViewById(R.id.TextEducation);
        TextExpProf = findViewById(R.id.TextProfecional);
        TextApli = findViewById(R.id.text_aplication);

        floatingActionButtonChat = findViewById(R.id.floatingActionButtonChat);
        floatingActionButtonVideoCall = findViewById(R.id.floatingActionButtonVideoCall);
        floatingActionButtonEditUserRole = findViewById(R.id.floatingActionButtonEditRole);

        textViewUserid = findViewById(R.id.Userid);

        String userid = getIntent().getStringExtra("userid");
        textViewUserid.setText(userid);


        floatingActionButtonVideoCall.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "videocall selected", Toast.LENGTH_SHORT).show());

        floatingActionButtonChat.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
            intent.putExtra("userid", userid);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);

        });

        floatingActionButtonEditUserRole.setOnClickListener(view -> {
            Intent intentNav = new Intent(getApplicationContext(), NavigationDrawerUserRolModActivity.class);
            intentNav.putExtra("uSerId",  userid);
            intentNav.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intentNav);
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);
                assert user != null;
                textName.setText(user.getUsername());
                textMail.setText(user.getMail());
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
                    userStatus.setTextColor(Color.parseColor("#C4AC2A"));
                }
                if(user.getImageURL().equals("default")){
                    profileimage.setImageResource(R.drawable.user13);
                }else{
                    Glide.with(NavigationDrawerUserDescActivity.this).load(user.getImageURL()).into(profileimage);
                }
                if (user.getAdmin().equals("1")){
                    company_1.setVisibility(View.GONE);
                    Company_1_dateStart.setVisibility(View.GONE);
                    Company_1_dateEnd.setVisibility(View.GONE);
                    Company_1_inCharge.setVisibility(View.GONE);
                    company_2.setVisibility(View.GONE);
                    Company_2_dateStart.setVisibility(View.GONE);
                    Company_2_dateEnd.setVisibility(View.GONE);
                    Company_2_inCharge.setVisibility(View.GONE);
                    degree_TimeStar.setVisibility(View.GONE);
                    degree_TimeEnd.setVisibility(View.GONE);
                    degree_school.setVisibility(View.GONE);
                    degree_Name.setVisibility(View.GONE);
                    degree_TimeStar_2.setVisibility(View.GONE);
                    degree_TimeEnd_2.setVisibility(View.GONE);
                    degree_school_2.setVisibility(View.GONE);
                    degree_Name_2.setVisibility(View.GONE);
                    requesting.setVisibility(View.GONE);
                    TextEdu.setVisibility(View.GONE);
                    TextExpProf.setVisibility(View.GONE);
                    TextApli.setVisibility(View.GONE);
                    Space.setVisibility(View.GONE);
                    space2.setVisibility(View.GONE);
                    space33.setVisibility(View.GONE);
                    space332.setVisibility(View.GONE);
                    image1.setVisibility(View.GONE);
                    image2.setVisibility(View.GONE);
                    floatingActionButtonEditUserRole.setVisibility(View.GONE);
                }
                if(user.getStatus().equals("En proceso") || user.getStatus().equals("Empleado") && user.getAdmin().equals("0")){
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("forms").child(userid);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Form form= dataSnapshot.getValue(Form.class);
                            assert form != null;
                            company_1.setText(form.getEmp_name1());
                            Company_1_dateStart.setText(form.getEmp_name1_date_start());
                            Company_1_dateEnd.setText(form.getEmp_name1_date_finish());
                            Company_1_inCharge.setText(form.getEmp_name1_Role());
                            if (form.getEmp_name2().equals("")){
                                company_2.setVisibility(View.GONE);
                                Company_2_dateStart.setVisibility(View.GONE);
                                Company_2_dateEnd.setVisibility(View.GONE);
                                Company_2_inCharge.setVisibility(View.GONE);
                                image2.setVisibility(View.GONE);
                                space2.setVisibility(View.GONE);
                            }else {
                                company_2.setText(form.getEmp_name2());
                                Company_2_dateStart.setText(form.getEmp_name2_date_start());
                                Company_2_dateEnd.setText(form.getEmp_name2_date_finish());
                                Company_2_inCharge.setText(form.getEmp_name2_Role());
                            }
                            degree_TimeStar.setText(form.getSchool_name1_date_start());
                            degree_TimeEnd.setText(form.getSchool_name1_date_finish());
                            degree_school.setText(form.getSchool_name1());
                            degree_Name.setText(form.getSchool_name1_Degree());

                            if (form.getSchool_name2().equals("")){
                                degree_TimeStar_2.setVisibility(View.GONE);
                                degree_TimeEnd_2.setVisibility(View.GONE);
                                degree_school_2.setVisibility(View.GONE);
                                degree_Name_2.setVisibility(View.GONE);
                                space332.setVisibility(View.GONE);
                            }else {
                                degree_TimeStar_2.setText(form.getEmp_name2_date_start());
                                degree_TimeEnd_2.setText(form.getSchool_name2_date_finish());
                                degree_school_2.setText(form.getSchool_name2());
                                degree_Name_2.setText(form.getSchool_name2_Degree());
                            }
                            requesting.setText(form.getJob_Requesting());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
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