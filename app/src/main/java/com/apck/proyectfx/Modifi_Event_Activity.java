package com.apck.proyectfx;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apck.proyectfx.model.Event_model;
import com.apck.proyectfx.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Modifi_Event_Activity extends AppCompatActivity {

    private EditText editTextDate;
    private EditText editTextHourStart;
    private EditText editTextHourFinish;
    private EditText editTextEventName;
    private EditText editTextEventDescription;
    private Button buttonmodEvent;
    private Button buttonDeleteEvent;
    String month_val;
    String day_val;
    String Eventid;

    int t2Hour, t2Minute;

    DatePickerDialog.OnDateSetListener setListener;

    String date, hourStart, hourFinish, eventN, eventD;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    int event_cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifi_event);

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

        editTextDate = findViewById(R.id.editTextTextMultiLine2);
        editTextHourStart = findViewById(R.id.edittexthorainicio);
        editTextHourFinish = findViewById(R.id.edittexthorafinal);
        editTextEventName = findViewById(R.id.editTextTextMultiLine3);
        editTextEventDescription = findViewById(R.id.editTextTextMultiLine4);
        buttonmodEvent = findViewById(R.id.buttonModifiE);
        buttonDeleteEvent = findViewById(R.id.buttonDeleteEvent);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Eventid = getIntent().getStringExtra("eventid");

        mDatabase.child("events").child(Eventid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Event_model event_model = snapshot.getValue(Event_model.class);
                    String dat = event_model.getEvent_date();
                    String start = event_model.getEvent_hourStart();
                    String finish = event_model.getEvent_hourFinish();
                    String ename = event_model.getEvent_name();
                    String edesc = event_model.getEvent_desc();
                    String mont = event_model.getMonth();
                    String daybal = event_model.getDay();

                    editTextDate.setText(dat);
                    editTextHourStart.setText(start);
                    editTextHourFinish.setText(finish);
                    editTextEventName.setText(ename);
                    editTextEventDescription.setText(edesc);
                    month_val = mont;
                    day_val = daybal;

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Calendar calendarPiker = Calendar.getInstance();
        final int year = calendarPiker.get(Calendar.YEAR);
        final int month = calendarPiker.get(Calendar.MONTH);
        final int day = calendarPiker.get(Calendar.DAY_OF_MONTH);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextDate.setOnClickListener(view1 -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    setListener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (datePicker, i, i1, i2) -> {
            i1 = i1+1;
            month_val = String.valueOf(i1);
            day_val = String.valueOf(i2);
            String Date = i2+" / "+i1+" / "+i;
            editTextDate.setText(Date);
        };

        editTextHourStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        Modifi_Event_Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t2Hour = i;
                                t2Minute = i1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0, 0, t2Hour, t2Minute);
                                editTextHourStart.setText(DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false
                );
                timePickerDialog.updateTime(t2Hour,t2Minute);
                timePickerDialog.show();
            }
        });

        editTextHourFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        Modifi_Event_Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t2Hour = i;
                                t2Minute = i1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0, 0, t2Hour, t2Minute);
                                editTextHourFinish.setText(DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false
                );
                timePickerDialog.updateTime(t2Hour,t2Minute);
                timePickerDialog.show();
            }
        });

        buttonmodEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = editTextDate.getText().toString();
                hourStart = editTextHourStart.getText().toString();
                hourFinish = editTextHourFinish.getText().toString();
                eventN = editTextEventName.getText().toString();
                eventD = editTextEventDescription.getText().toString();

                if(!date.isEmpty() && !hourStart.isEmpty()  && !eventN.isEmpty()  && !eventD.isEmpty()){
                    updateEvent(Eventid);
                }
                else{
                    Toast.makeText(getApplicationContext(), "deve llenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference Event = FirebaseDatabase.getInstance().getReference("events").child(Eventid);
                Event.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Evento Removido", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    private void updateEvent(String Eventid) {
        Map<String, Object> map = new HashMap<>();
        map.put("month", month_val);
        map.put("day", day_val);
        map.put("event_date", date);
        map.put("event_hourStart", hourStart);
        map.put("event_hourFinish", hourFinish);
        map.put("event_name", eventN);
        map.put("event_desc", eventD);
        mDatabase.child(Constants.KEY_DATABASE_EVENT_PHAT).child(Eventid).updateChildren(map).addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                Toast.makeText(getApplicationContext(), "Se añadió el evento correcta mente", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Ocurrió un problema al añadir el evento ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}