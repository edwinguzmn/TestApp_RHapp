package com.apck.proyectfx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.apck.proyectfx.adapter.EventAdapter;
import com.apck.proyectfx.adapter.VPaddapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EventActivity extends AppCompatActivity {

    EventAdapter eventAdapter;
    ViewPager2 viewPager2;
    TabLayout tabLayout;

    private final String[] titles = new String[]{"Agregar Evento","Eventos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

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

        viewPager2 = findViewById(R.id.viewpager222);
        tabLayout = findViewById(R.id.tablayout2);

        eventAdapter = new EventAdapter(this);
        viewPager2.setAdapter(eventAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();

    }
}