package com.apck.proyectfx.NavigationDrawerUsersFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apck.proyectfx.R;


public class NavigationDrawer_User_comunicacion_Fragment extends Fragment {


    public NavigationDrawer_User_comunicacion_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer__user_comunicacion_, container, false);
    }
}