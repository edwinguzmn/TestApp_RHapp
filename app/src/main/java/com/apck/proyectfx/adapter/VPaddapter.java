package com.apck.proyectfx.adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.apck.proyectfx.fragments.Chat_Fragment;
import com.apck.proyectfx.fragments.Videollamada_Fragment;
import com.apck.proyectfx.fragments.Events_Fragment;


public class VPaddapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Chats","V.llamada","eventos"};

    public VPaddapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new Chat_Fragment();
            case 1:
                return new Videollamada_Fragment();
            case 2:
                return new Events_Fragment();
        }

        return new Chat_Fragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
