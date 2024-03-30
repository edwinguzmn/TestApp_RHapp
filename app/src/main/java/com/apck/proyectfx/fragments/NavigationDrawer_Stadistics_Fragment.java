package com.apck.proyectfx.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.apck.proyectfx.R;
import com.apck.proyectfx.model.Form;
import com.apck.proyectfx.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NavigationDrawer_Stadistics_Fragment extends Fragment {

    DatabaseReference databaseReference;
    DatabaseReference mDatabase;
    DatabaseReference mDataReference;
    List<User> mlist;
    List<Form> mListforms;

    private int usuarios = 0;
    private int empleados = 0;
    private int aplicantes = 0;
    private int telecomunicaciones = 0;
    private int gestordecalidad = 0;
    private int frontend = 0;
    private int backend = 0;

    private int inUseTelecomunicaciones = 0;
    private int inUsegestordecalidad = 0;
    private int inUsefrontend = 0;
    private int inUsebackend = 0;


    public NavigationDrawer_Stadistics_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer__stadistics_, container, false);

        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.PieChart);
        mlist = new ArrayList<>();
        mListforms = new ArrayList<>();

        TextView usersCount = view.findViewById(R.id.countUsers);
        TextView usersEmployees = view.findViewById(R.id.countEmpleados);
        TextView usersRequesting = view.findViewById(R.id.countsinempezar);
        TextView oferTelecommunication = view.findViewById(R.id.countUsers2);
        TextView oferQualityManager = view.findViewById(R.id.countUsers3);
        TextView oferBackend = view.findViewById(R.id.countUsers4);
        TextView oferFrontend = view.findViewById(R.id.countUsers5);

        TextView userTelecom = view.findViewById(R.id.countUsers23);
        TextView userGesCall = view.findViewById(R.id.countUsers2333);
        TextView userBackend = view.findViewById(R.id.countUsers23344);
        TextView userFrontend = view.findViewById(R.id.countUsers2334454);

        getValuesUsers(anyChartView, usersCount, usersEmployees, usersRequesting);

        AnyChartView anyChartView2 = view.findViewById(R.id.PieChart2);

        getValuesForms(anyChartView2, oferTelecommunication, oferQualityManager, oferBackend, oferFrontend);

        AnyChartView anyChartView3 = view.findViewById(R.id.PieChart3);

        getValuesFormsChart(anyChartView3, userTelecom ,userGesCall, userBackend, userFrontend);

        return view;
    }

    private void getValuesUsers(AnyChartView anyChartView, TextView usersCount, TextView usersEmployees, TextView usersRequesting) {
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;

                    if (user.getRank().equals("Usuario")){
                        usuarios = usuarios+1;
                    }
                    if (user.getStatus().equals("Empleado")){
                        empleados = empleados+1;
                    }
                    if (user.getStatus().equals("En proceso")){
                        aplicantes = aplicantes+1;
                    }
                }
                usersCount.setText(String.valueOf(usuarios));
                usersEmployees.setText(String.valueOf(empleados));
                usersRequesting.setText(String.valueOf(aplicantes));
                setupGrapPie1(anyChartView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setupGrapPie1(AnyChartView anyChartView){
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Usuarios", usuarios));
        data.add(new ValueDataEntry("empleados", empleados));
        data.add(new ValueDataEntry("aplicantes", aplicantes));

        pie.data(data);
        anyChartView.setChart(pie);

    }

    private void getValuesForms(AnyChartView anyChartView2, TextView oferTelecommunication, TextView oferQualityManager, TextView oferBackend, TextView oferFrontend) {
        mDatabase = FirebaseDatabase.getInstance().getReference("forms");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Form form = snapshot.getValue(Form.class);
                    assert form != null;

                    if (form.getJob_Requesting().equals("Especialista en redes y telecomunicaciones") && !form.getStatus().equals("Empleado")){
                        telecomunicaciones = telecomunicaciones+1;
                        Log.d("MYTAG", "adde telecomunicaciones");
                    }
                    if (form.getJob_Requesting().equals("Gestor de calidad de software") && !form.getStatus().equals("Empleado")){
                        gestordecalidad = gestordecalidad+1;
                        Log.d("MYTAG", "adde Gestor de calidad");
                    }
                    if (form.getJob_Requesting().equals("Desarrollador de software (front-end)") && !form.getStatus().equals("Empleado")){
                        frontend = frontend+1;
                        Log.d("MYTAG", "adde front-end");
                    }
                    if (form.getJob_Requesting().equals("Desarrollador de software (back-end)") && !form.getStatus().equals("Empleado")){
                        backend = backend+1;
                        Log.d("MYTAG", "adde back-end");
                    }
                }
                oferTelecommunication.setText(String.valueOf(telecomunicaciones));
                oferQualityManager.setText(String.valueOf(gestordecalidad));
                oferBackend.setText(String.valueOf(backend));
                oferFrontend.setText(String.valueOf(frontend));

                setupGrapPie2(anyChartView2,  telecomunicaciones,  gestordecalidad,  backend,  frontend);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setupGrapPie2(AnyChartView anyChartView2, int telecomunicaciones, int gestordecalidad, int backend, int frontend){
        APIlib.getInstance().setActiveAnyChartView(anyChartView2);
        Pie pie2 = AnyChart.pie();

        List<DataEntry> data1 = new ArrayList<>();
        data1.add(new ValueDataEntry("Telecomunicaciones y redes", telecomunicaciones));
        data1.add(new ValueDataEntry("Gestión de calidad", gestordecalidad));
        data1.add(new ValueDataEntry("front-end", frontend));
        data1.add(new ValueDataEntry("back-end", backend));

        pie2.data(data1);
        anyChartView2.setChart(pie2);
    }

    private void getValuesFormsChart(AnyChartView anyChartView3, TextView userTelecom, TextView userGesCall, TextView userBackend, TextView userFrontend) {
        mDatabase = FirebaseDatabase.getInstance().getReference("forms");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Form form = snapshot.getValue(Form.class);
                    if (form.getJob_Requesting().equals("Especialista en redes y telecomunicaciones") && form.getStatus().equals("Empleado")){
                        inUseTelecomunicaciones = inUseTelecomunicaciones+1;
                    }
                    if (form.getJob_Requesting().equals("Gestor de calidad de software") && form.getStatus().equals("Empleado")){
                        inUsegestordecalidad = inUsegestordecalidad+1;
                    }
                    if (form.getJob_Requesting().equals("Desarrollador de software (front-end)") && form.getStatus().equals("Empleado")){
                        inUsefrontend = inUsefrontend+1;
                    }
                    if (form.getJob_Requesting().equals("Desarrollador de software (back-end)") && form.getStatus().equals("Empleado")){
                        inUsebackend = inUsebackend+1;
                    }
                }
                setupGrapcartesian(anyChartView3);
                userTelecom.setText(String.valueOf(inUseTelecomunicaciones));
                if (userTelecom.getText().equals("0") || userTelecom.getText().equals("1") || userTelecom.getText().equals("2")){
                    userTelecom.setTextColor(Color.parseColor("#FF6060"));
                }else if(userTelecom.getText().equals("3") || userTelecom.getText().equals("4")){
                    userTelecom.setTextColor(Color.parseColor("#C4AC2A"));
                }else if(userTelecom.getText().equals("5") || userTelecom.getText().equals("6")){
                    userTelecom.setTextColor(Color.parseColor("#90D487"));
                }
                userGesCall.setText(String.valueOf(inUsegestordecalidad));
                if (userGesCall.getText().equals("0") || userGesCall.getText().equals("1") || userGesCall.getText().equals("2")){
                    userGesCall.setTextColor(Color.parseColor("#FF6060"));
                }else if(userGesCall.getText().equals("3") || userGesCall.getText().equals("4")){
                    userGesCall.setTextColor(Color.parseColor("#C4AC2A"));
                }else if(userGesCall.getText().equals("5") || userGesCall.getText().equals("6")){
                    userGesCall.setTextColor(Color.parseColor("#90D487"));
                }
                userBackend.setText(String.valueOf(inUsebackend));
                if (userBackend.getText().equals("0") || userBackend.getText().equals("1") || userBackend.getText().equals("2")){
                    userBackend.setTextColor(Color.parseColor("#FF6060"));
                }else if(userBackend.getText().equals("3") || userBackend.getText().equals("4")){
                    userBackend.setTextColor(Color.parseColor("#C4AC2A"));
                }else if(userBackend.getText().equals("5") || userBackend.getText().equals("6")){
                    userBackend.setTextColor(Color.parseColor("#90D487"));
                }
                userFrontend.setText(String.valueOf(inUsefrontend));
                if (userFrontend.getText().equals("0") || userFrontend.getText().equals("1") || userFrontend.getText().equals("2")){
                    userFrontend.setTextColor(Color.parseColor("#FF6060"));
                }else if(userFrontend.getText().equals("3") || userFrontend.getText().equals("4")){
                    userFrontend.setTextColor(Color.parseColor("#C4AC2A"));
                }else if(userFrontend.getText().equals("5") || userFrontend.getText().equals("6")){
                    userFrontend.setTextColor(Color.parseColor("#90D487"));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setupGrapcartesian(AnyChartView anyChartView3) {

        APIlib.getInstance().setActiveAnyChartView(anyChartView3);
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("teleCom.", inUseTelecomunicaciones));
        data.add(new ValueDataEntry("calidad", inUsegestordecalidad));
        data.add(new ValueDataEntry("front-end", inUsebackend));
        data.add(new ValueDataEntry("back-end", inUsefrontend));
        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        anyChartView3.setChart(cartesian);
    }
}