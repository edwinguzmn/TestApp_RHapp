package com.apck.proyectfx;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apck.proyectfx.adapter.SortModeAdapter;
import com.apck.proyectfx.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

public class DrawerUserSortModeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SortModeAdapter userAdapter;
    private SortModeAdapter DateSearchAdapter;
    private List<User> DateList;
    private List<User> mlist;
    private List<User> usersList;
    private List<User> usersListFiltered;
    private EditText SearchDateStart, SearchDateEnd;
    DatabaseReference databaseReference;
    Long DateMin;
    Date date_minimal;
    Date date_maximal;
    private FloatingActionButton pdfGenButton;


    String DateStart ="", DateEnd ="";

    Locale id = new Locale("es", "ES");
    Locale id3 = new Locale("en", "EN");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", id);
    SimpleDateFormat simpleDateFormatNew = new SimpleDateFormat("yyyy-MM-dd", id3);
    Calendar calendar = Calendar.getInstance();

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_user_sort_mode);

        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
        DateList = new ArrayList<>();
        pdfGenButton = findViewById(R.id.ButtonPdfGen);

        readUsers();

        //--------------------------------------------Search Elements------------------------------------------------------//

         SearchDateStart = findViewById(R.id.DateStartSearch);
         SearchDateEnd = findViewById(R.id.DateEndSearch);
        Button performSearch = findViewById(R.id.ButtonSearch);
        Button clearSearch = findViewById(R.id.ClearSearch);

        SearchDateStart.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(DrawerUserSortModeActivity.this, (datePicker, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                SearchDateStart.setText(simpleDateFormat.format(calendar.getTime()));
                date_minimal = calendar.getTime();
                DateStart = simpleDateFormatNew.format(calendar.getTime());
                DateMin = calendar.getTimeInMillis();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        SearchDateEnd.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(DrawerUserSortModeActivity.this, (datePicker, year, month, dayOfMonth) -> {
                datePicker.setMinDate(DateMin);
                calendar.set(year, month, dayOfMonth);
                SearchDateEnd.setText(simpleDateFormat.format(calendar.getTime()));
                DateEnd = simpleDateFormatNew.format(calendar.getTime());
                date_maximal = calendar.getTime();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        performSearch.setOnClickListener(view -> {
            if (!DateStart.isEmpty() && !DateEnd.isEmpty()){
                Toast.makeText(getApplicationContext(), "Ready To Search", Toast.LENGTH_SHORT).show();
               Query query = FirebaseDatabase.getInstance().getReference("usuarios").orderByChild("query_date_Search").startAt(DateStart).endAt(DateEnd);
               query.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                           DateList.clear();
                           for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                               User user = dataSnapshot.getValue(User.class);
                               DateList.add(user);
                           }
                           DateSearchAdapter = new SortModeAdapter(getApplicationContext(), DateList);
                           recyclerView.setAdapter(DateSearchAdapter);
                       }
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                   }
               });
            }else {
                Toast.makeText(getApplicationContext(), "llenar los dos Campos", Toast.LENGTH_SHORT).show();
            }
        });

        clearSearch.setOnClickListener(view -> {
            SearchDateEnd.setText("");
            DateEnd = SearchDateEnd.getText().toString();
            SearchDateStart.setText("");
            DateStart = SearchDateStart.getText().toString();
            DateList.clear();
            recyclerView.setAdapter(userAdapter);

        });

        //-----------------------------------------------------------------------------------------------------------------//
        //--------------------------------------------pdf Gen--------------------------------------------------------------//

        pdfGenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CreatePdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        //-----------------------------------------------------------------------------------------------------------------//

        usersListFiltered = new ArrayList<>();

        Spinner spinner = findViewById(R.id.SpinerSearch);

        String[] options = {"Clasificar por estatus ","Estatus: sin empezar","Estatus: en proceso", "Estatus: empleado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, options);
        spinner.setAdapter(adapter);

        Spinner spinnerSearchAplication = findViewById(R.id.SpinerSearchAplication);

        String[] options2 = {"Clasificar por aplicación","redes y telecomunicaciones","calidad de software", "front-end", "back-end"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, options2);
        spinnerSearchAplication.setAdapter(adapter2);

        Spinner spinnerSearchYear = findViewById(R.id.SpinerSearchYear);

        String[] options3 = {"Año:","2020","2021","2022","2023","2024","2025","2026","2027","2028"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, options3);
        spinnerSearchYear.setAdapter(adapter3);

        Spinner spinnerSearchMonth = findViewById(R.id.SpinerSearchMonth);

        String[] options4 = {"Mes:","Enero", "febrero", "marzo", "abril", "mayo"," junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, options4);
        spinnerSearchMonth.setAdapter(adapter4);

        Spinner spinnerSearchDay = findViewById(R.id.SpinerSearchDay);

        String[] options5 = {"Dia del mes:","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, options5);
        spinnerSearchDay.setAdapter(adapter5);


        searchView = findViewById(R.id.SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                userAdapter.getFilter().filter(s);
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
                if(i==1){
                    searchView.setQuery("sin empezar", false);
                    searchView.clearFocus();
                }
                if(i==2){
                    searchView.setQuery("en proceso", false);
                    searchView.clearFocus();
                }
                if(i==3){
                    searchView.setQuery("empleado", false);
                    searchView.clearFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerSearchAplication.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
                if(i==1){
                    searchView.setQuery("Especialista en redes y telecomunicaciones", false);
                    searchView.clearFocus();
                }
                if(i==2){
                    searchView.setQuery("Gestor de calidad de software", false);
                    searchView.clearFocus();
                }
                if(i==3){
                    searchView.setQuery("Desarrollador de software (front-end)", false);
                    searchView.clearFocus();
                }
                if(i==4){
                    searchView.setQuery("Desarrollador de software (back-end)", false);
                    searchView.clearFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSearchYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
                if(i==1){
                    searchView.setQuery("2020:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
                if(i==2){
                    searchView.setQuery("2021:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
                if(i==3){
                    searchView.setQuery("2022:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
                if(i==4){
                    searchView.setQuery("2023:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
                if(i==5){
                    searchView.setQuery("2024:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
                if(i==6){
                    searchView.setQuery("2025:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
                if(i==7){
                    searchView.setQuery("2026:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
                if(i==8){
                    searchView.setQuery("2027:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
                if(i==9){
                    searchView.setQuery("2028:STRING_REFERENCE_KEY_YEAR", false);
                    searchView.clearFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerSearchMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
                if(i==1){
                    searchView.setQuery("01:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==2){
                    searchView.setQuery("02:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==3){
                    searchView.setQuery("03:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==4){
                    searchView.setQuery("04:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==5){
                    searchView.setQuery("05:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==6){
                    searchView.setQuery("06:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==7){
                    searchView.setQuery("07:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==8){
                    searchView.setQuery("08:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==9){
                    searchView.setQuery("09:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==10){
                    searchView.setQuery("10:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==11){
                    searchView.setQuery("11:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
                if(i==12){
                    searchView.setQuery("12:STRING_REFERENCE_KEY_MONTH" , false);
                    searchView.clearFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerSearchDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
                if(i==1){
                    searchView.setQuery("01:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==2){
                    searchView.setQuery("02:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==3){
                    searchView.setQuery("03:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==4){
                    searchView.setQuery("04:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==5){
                    searchView.setQuery("05:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==6){
                    searchView.setQuery("06:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==7){
                    searchView.setQuery("07:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==8){
                    searchView.setQuery("08:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==9){
                    searchView.setQuery("09:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==10){
                    searchView.setQuery("10:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==11){
                    searchView.setQuery("11:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==12){
                    searchView.setQuery("12:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==13){
                    searchView.setQuery("13:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==14){
                    searchView.setQuery("14:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==15){
                    searchView.setQuery("15:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==16){
                    searchView.setQuery("16:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==17){
                    searchView.setQuery("17:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==18){
                    searchView.setQuery("18:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==19){
                    searchView.setQuery("19:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==20){
                    searchView.setQuery("20:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==21){
                    searchView.setQuery("21:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==22){
                    searchView.setQuery("22:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==23){
                    searchView.setQuery("23:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==24){
                    searchView.setQuery("24:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==25){
                    searchView.setQuery("25:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==26){
                    searchView.setQuery("26:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==27){
                    searchView.setQuery("27:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==28){
                    searchView.setQuery("28:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==29){
                    searchView.setQuery("29:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==30){
                    searchView.setQuery("30:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
                if(i==31){
                    searchView.setQuery("31:STRING_REFERENCE_KEY_DAY" , false);
                    searchView.clearFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        recyclerView = findViewById(R.id.ReciclerviewSearch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mlist = new ArrayList<>();



        LinearLayoutManager manager1 = new LinearLayoutManager(getApplicationContext());
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager1);
    }

    private void CreatePdf() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "Registro.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(String.valueOf(file));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        Paragraph paragraph = new Paragraph();
        Text text = new Text("RH app   ").setBold();
        Text text1 = new Text("Registro de Usuarios  ");
        Text text2 = new Text("De la fecha inicial:  "+SearchDateStart.getText().toString()+" hasta:  "+SearchDateEnd.getText().toString());
        paragraph.add(text)
                .add(text1)
                .add(text2);
        document.add(paragraph);
        float ColumnWidth[] = {105f,105f,105f,105f,100f};
        Table table = new Table(ColumnWidth);
        table.addCell("Id");
        table.addCell("nombre");
        table.addCell("fecha de ingreso");
        table.addCell("rango");
        table.addCell("estatus");

        for (User user : DateList){
            table.addCell(user.getId());
            table.addCell(user.getUsername());
            table.addCell(user.getDate_of_join());
            table.addCell(user.getRank());
            table.addCell(user.getStatus());
        }

        document.add(table);
        document.close();
        Toast.makeText(getApplicationContext(), "pdf Created", Toast.LENGTH_SHORT).show();
    }


    private void filteredList(String status) {
        usersListFiltered.clear();
        for(int i=0; i<usersList.size(); i++){
            if(usersList.get(i).getStatus().equals(status)){
                Toast.makeText(getApplicationContext(), usersList.get(i).getStatus(), Toast.LENGTH_SHORT).show();
                usersListFiltered.add(usersList.get(i));
            }
            userAdapter = new SortModeAdapter(getApplicationContext(), usersListFiltered);
            recyclerView.setAdapter(userAdapter);
        }
    }

    private void readUsers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    //if(!user.getId().equals(firebaseUser.getUid())){
                    mlist.add(user);
                    //}
                }
                usersList = new ArrayList<>(mlist);
                userAdapter = new SortModeAdapter(getApplicationContext(), mlist);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}