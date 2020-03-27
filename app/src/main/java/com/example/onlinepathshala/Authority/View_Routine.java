package com.example.onlinepathshala.Authority;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.onlinepathshala.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class View_Routine extends Fragment {

    public View_Routine() {
        // Required empty public constructor
    }

    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_view__routine, container, false);
        // Inflate the layout for this fragment
        frameLayout=view.findViewById(R.id.frame_layout);
        bottomNavigationView=view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.class_routine:
                        changeFragmentView(new Class_Routine_For_Auth("authority"));
                        return  true;
                    case R.id.exam_routine:
                        changeFragmentView(new Exam_Routine_For_Auth("authority"));
                        return true;

                }
                return true;
            }
        });
        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new Class_Routine_For_Auth("authority")).commit();
        return view;
    }

    public void changeFragmentView(Fragment fragment){

        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();

    }


}
