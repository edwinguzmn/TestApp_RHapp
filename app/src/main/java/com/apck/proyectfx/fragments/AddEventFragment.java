package com.apck.proyectfx.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apck.proyectfx.R;
import com.apck.proyectfx.utilities.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddEventFragment extends Fragment {

    private EditText editTextDay;
    private EditText editTextDayNumber;
    private EditText editTextMonth;
    private EditText editTextEventName;
    private EditText editTextEventDescription;
    private Button buttonAddEvent;
    int event_cont = 0;

    String day, dayNum, month, eventN, eventD;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        editTextDay = view.findViewById(R.id.editTextTextMultiLine);
        editTextDayNumber = view.findViewById(R.id.editTextTextMultiLine6);
        editTextMonth = view.findViewById(R.id.editTextTextMultiLine2);
        editTextEventName = view.findViewById(R.id.editTextTextMultiLine3);
        editTextEventDescription = view.findViewById(R.id.editTextTextMultiLine4);
        buttonAddEvent = view.findViewById(R.id.buttonAddEvent);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day = editTextDay.getText().toString();
                dayNum = editTextDayNumber.getText().toString();
                month = editTextMonth.getText().toString();
                eventN = editTextEventName.getText().toString();
                eventD = editTextEventDescription.getText().toString();

                if(!day.isEmpty() && !dayNum.isEmpty() && !month.isEmpty()  && !eventN.isEmpty()  && !eventD.isEmpty()){
                    addEvent();
                }
                else{
                    Toast.makeText(getContext(), "deve llenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    private void addEvent() {
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("eventid" , id);
        map.put(Constants.KEY_EVENT_DAY, day);
        map.put(Constants.KEY_EVENT_DAY_NUMBER, dayNum);
        map.put(Constants.KEY_EVENT_MONTH, month);
        map.put(Constants.KEY_EVENT_NAME, eventN);
        map.put(Constants.KEY_EVENT_DESC, eventD);

        String pos = String.valueOf(event_cont);
        mDatabase.child(Constants.KEY_DATABASE_EVENT_PHAT).child(pos).setValue(map).addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                event_cont = Integer.parseInt(pos);
                event_cont=event_cont+1;
                Toast.makeText(getContext(), "Se añadió el evento correcta mente", Toast.LENGTH_SHORT).show();
                editTextDay.setText("");
                editTextDayNumber.setText("");
                editTextMonth.setText("");
                editTextEventName.setText("");
                editTextEventDescription.setText("");
            }else{
                Toast.makeText(getContext(), "Ocurrió un problema al añadir el evento ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}