package com.apck.proyectfx.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.apck.proyectfx.fragments.AddEventFragment;
import com.apck.proyectfx.fragments.Chat_Fragment;
import com.apck.proyectfx.fragments.EventViewFragment;
import com.apck.proyectfx.fragments.Events_Fragment;
import com.apck.proyectfx.fragments.Videollamada_Fragment;

public class EventAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Agregar Evento","Eventos"};

    public EventAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new AddEventFragment();
            case 1:
                return new EventViewFragment();
        }
        return new AddEventFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
