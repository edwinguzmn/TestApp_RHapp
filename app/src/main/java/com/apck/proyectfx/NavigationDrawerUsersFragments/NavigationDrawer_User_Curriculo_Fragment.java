package com.apck.proyectfx.NavigationDrawerUsersFragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.apck.proyectfx.R;
import com.apck.proyectfx.model.Form;
import com.apck.proyectfx.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class NavigationDrawer_User_Curriculo_Fragment extends Fragment {

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
    String stingrequesting;
    Calendar calendar;
    DatabaseReference mDatabase;

    DatePickerDialog.OnDateSetListener setListener;
    DatePickerDialog.OnDateSetListener setListener2;
    DatePickerDialog.OnDateSetListener setListener3;
    DatePickerDialog.OnDateSetListener setListener4;
    DatePickerDialog.OnDateSetListener setListener5;
    DatePickerDialog.OnDateSetListener setListener6;
    DatePickerDialog.OnDateSetListener setListener7;
    DatePickerDialog.OnDateSetListener setListener8;

    private String companyName1, CompanyDateStart1, CompanyDateEnd1, CompanyInCharge1, companyName2, CompanyDateStart2, CompanyDateEnd2, CompanyInCharge2, degreeStart1,
            degreeEnd1, degreeSchool1, degreeName1, degreeStart2, degreeEnd2, degreeSchool2, degreeName2;

    public NavigationDrawer_User_Curriculo_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_navigation_drawer__user__curriculo_, container, false);

        textName = view.findViewById(R.id.usename);
        textMail = view.findViewById(R.id.usermail);
        userRank = view.findViewById(R.id.userRank);
        userStatus = view.findViewById(R.id.userStatus);
        dateOfJoin = view.findViewById(R.id.UserDateOfJoint);
        profileimage = view.findViewById(R.id.profile_imagen);

        company_1 = view.findViewById(R.id.factoriname);
        Company_1_dateStart = view.findViewById(R.id.fechadetrabajoinicial);
        Company_1_dateEnd = view.findViewById(R.id.fechadetrabajofinal);
        Company_1_inCharge = view.findViewById(R.id.descripciondeaempresatext);
        company_2 = view.findViewById(R.id.factoriname2);
        Company_2_dateStart = view.findViewById(R.id.fechadetrabajoinicial2);
        Company_2_dateEnd = view.findViewById(R.id.fechadetrabajofinal2);
        Company_2_inCharge = view.findViewById(R.id.descripciondeaempresatext2);
        degree_TimeStar = view.findViewById(R.id.fechadetrabajoinicial3);
        degree_TimeEnd = view.findViewById(R.id.fechadetrabajofinal3);
        degree_school = view.findViewById(R.id.factoriname3);
        degree_Name = view.findViewById(R.id.descripciondeaempresatext3);
        degree_TimeStar_2 = view.findViewById(R.id.fechadetrabajoinicial4);
        degree_TimeEnd_2 = view.findViewById(R.id.fechadetrabajofinal4);
        degree_school_2 = view.findViewById(R.id.factoriname4);
        degree_Name_2 = view.findViewById(R.id.descripciondeaempresatext4);
        requesting = view.findViewById(R.id.TextAplicationnn);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar calendarPiker = Calendar.getInstance();
        final int year = calendarPiker.get(Calendar.YEAR);
        final int month = calendarPiker.get(Calendar.MONTH);
        final int day = calendarPiker.get(Calendar.DAY_OF_MONTH);


        Company_1_dateStart.setOnClickListener(view1 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            String Date = i2+" / "+i1+" / "+i;
            Company_1_dateStart.setText(Date);
        };

        Company_1_dateEnd.setOnClickListener(view12 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener2, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener2 = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            String Date = i2+" / "+i1+" / "+i;
            Company_1_dateEnd.setText(Date);
        };

        Company_2_dateStart.setOnClickListener(view13 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener3, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener3 = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            String Date = i2+" / "+i1+" / "+i;
            Company_2_dateStart.setText(Date);
        };
        Company_2_dateEnd.setOnClickListener(view14 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener4, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener4 = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            String Date = i2+" / "+i1+" / "+i;
            Company_2_dateEnd.setText(Date);
        };
        degree_TimeStar.setOnClickListener(view15 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener5, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener5 = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            String Date = i2+" / "+i1+" / "+i;
            degree_TimeStar.setText(Date);
        };
        degree_TimeEnd.setOnClickListener(view16 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener6, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener6 = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            String Date = i2+" / "+i1+" / "+i;
            degree_TimeEnd.setText(Date);
        };

        degree_TimeStar_2.setOnClickListener(view17 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener7, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener7 = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            String Date = i2+" / "+i1+" / "+i;
            degree_TimeStar_2.setText(Date);
        };
        degree_TimeEnd_2.setOnClickListener(view18 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener8, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener8 = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            String Date = i2+" / "+i1+" / "+i;
            degree_TimeEnd_2.setText(Date);
        };


        calendar = Calendar.getInstance();
        String currentDate = DateFormat.getInstance().format(calendar.getTime());

        assert firebaseUser != null;
        String userid = firebaseUser.getUid();

        Spinner spinnerAplicationSelection = view.findViewById(R.id.spinerAplication);

        String[] options = {"Especialista en redes y telecomunicaciones","Gestor de calidad de software","Desarrollador de software (front-end)", "Desarrollador de software (back-end)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        spinnerAplicationSelection.setAdapter(adapter);

        Button buttonContinue = view.findViewById(R.id.ButtonContinue);
        spinnerAplicationSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    stingrequesting = "Especialista en redes y telecomunicaciones";
                    requesting.setText("Especialista en redes y telecomunicaciones");
                }
                if(i==1){
                    stingrequesting = "Gestor de calidad de software";
                    requesting.setText("Gestor de calidad de software");
                }
                if(i==2){
                    stingrequesting = "Desarrollador de software (front-end)";
                    requesting.setText("Desarrollador de software (front-end)");
                }
                if(i==3){
                    stingrequesting = "Desarrollador de software (back-end)";
                    requesting.setText("Desarrollador de software (back-end)");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
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
                    Glide.with(NavigationDrawer_User_Curriculo_Fragment.this).load(user.getImageURL()).into(profileimage);
                }
                if(user.getStatus().equals("En proceso") || user.getStatus().equals("Empleado")){
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("forms").child(userid);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Form form= dataSnapshot.getValue(Form.class);
                                company_1.setText(form.getEmp_name1());
                                Company_1_dateStart.setText(form.getEmp_name1_date_start());
                                Company_1_dateEnd.setText(form.getEmp_name1_date_finish());
                                Company_1_inCharge.setText(form.getEmp_name1_Role());
                                company_2.setText(form.getEmp_name2());
                                Company_2_dateStart.setText(form.getEmp_name2_date_start());
                                Company_2_dateEnd.setText(form.getEmp_name2_date_finish());
                                Company_2_inCharge.setText(form.getEmp_name2_Role());
                                degree_TimeStar.setText(form.getSchool_name1_date_start());
                                degree_TimeEnd.setText(form.getSchool_name1_date_finish());
                                degree_school.setText(form.getSchool_name1());
                                degree_Name.setText(form.getSchool_name1_Degree());
                                degree_TimeStar_2.setText(form.getEmp_name2_date_start());
                                degree_TimeEnd_2.setText(form.getSchool_name2_date_finish());
                                degree_school_2.setText(form.getSchool_name2());
                                degree_Name_2.setText(form.getSchool_name2_Degree());
                                requesting.setText(form.getJob_Requesting());
                            }
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

        buttonContinue.setOnClickListener(view19 -> {

            companyName1 = company_1.getText().toString();
            CompanyDateStart1 = Company_1_dateStart.getText().toString();
            CompanyDateEnd1 = Company_1_dateEnd.getText().toString();
            CompanyInCharge1 = Company_1_inCharge.getText().toString();
            companyName2 = company_2.getText().toString();
            CompanyDateStart2 = Company_2_dateStart.getText().toString();
            CompanyDateEnd2 = Company_2_dateEnd.getText().toString();
            CompanyInCharge2 = Company_2_inCharge.getText().toString();
            degreeStart1 = degree_TimeStar.getText().toString();
            degreeEnd1 = degree_TimeEnd.getText().toString();
            degreeSchool1 = degree_school.getText().toString();
            degreeName1 = degree_Name.getText().toString();
            degreeStart2 = degree_TimeStar_2.getText().toString();
            degreeEnd2 = degree_TimeEnd_2.getText().toString();
            degreeSchool2 = degree_school_2.getText().toString();
            degreeName2 = degree_Name_2.getText().toString();

            if (!companyName1.isEmpty() && !CompanyDateStart1.isEmpty() && !CompanyDateEnd1.isEmpty() && !CompanyInCharge1.isEmpty()){
                if (!degreeStart1.isEmpty() && !degreeEnd1.isEmpty() && !degreeSchool1.isEmpty() && !degreeName1.isEmpty()){
                    StartForm(userid, currentDate);
                }else {
                    Toast.makeText(getContext(), "Debe de ingresar una licenciatura para poder aplicar a una vacante laboral.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Debe de llenar por lo menos un apartado para demostrar su experiencia profesional. ", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void StartForm(String userid, String currentDate) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Status", "En proceso");
        hashMap.put("job_Requesting", requesting.getText().toString());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userid);
        databaseReference.updateChildren(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Map<String, Object> map = new HashMap<>();
                map.put("id", userid);
                map.put("emp_name1", companyName1);
                map.put("emp_name1_Role", CompanyInCharge1);
                map.put("emp_name1_date_start", CompanyDateStart1);
                map.put("emp_name1_date_finish", CompanyDateEnd1);
                map.put("emp_name2", companyName2);
                map.put("emp_name2_Role", CompanyInCharge2);
                map.put("emp_name2_date_start", CompanyDateStart2);
                map.put("emp_name2_date_finish", CompanyDateEnd2);
                map.put("school_name1", degreeSchool1);
                map.put("school_name1_date_start", degreeStart1);
                map.put("school_name1_date_finish", degreeEnd1);
                map.put("school_name1_Degree", degreeName1);
                map.put("school_name2", degreeSchool2);
                map.put("school_name2_date_start", degreeStart2);
                map.put("school_name2_date_finish", degreeEnd2);
                map.put("school_name2_Degree", degreeName2);
                map.put("job_Requesting", requesting.getText().toString());
                map.put("Status", userStatus.getText().toString());

                mDatabase = FirebaseDatabase.getInstance().getReference().child("forms").child(userid);
                mDatabase.setValue(map).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()){
                        Toast.makeText(getContext(), "datos añadidos correcta mente", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> Event = new HashMap<>();
                        Event.put("text1", "El usuario");
                        Event.put("username_LogEvent", textName.getText().toString());
                        Event.put("text2", "A aplicado para el puesto "+requesting.getText().toString());
                        Event.put("date_LogEvent", currentDate);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("LogEvents");
                        databaseReference.push().setValue(Event);
                    }else {
                        Toast.makeText(getContext(), "Error al giuardar los datos" , Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getContext(), "No se pudo actualizar el estado de usuario", Toast.LENGTH_SHORT).show();
            }
        });

    }
}