package com.example.onlinepathshala.Student;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.onlinepathshala.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Student_Routine extends Fragment{

    public Student_Routine() {

    }


    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_student__routine, container, false);
        frameLayout=view.findViewById(R.id.frame_layout);
        bottomNavigationView=view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.class_routine:
                        changeFragmentView(new Class_Routine());
                        return  true;
                    case R.id.exam_routine:
                        changeFragmentView(new Exam_Routine());
                        return true;

                }
                return true;
            }
        });
        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new Class_Routine()).commit();

        return view;
    }



    public void changeFragmentView(Fragment fragment){

        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();

    }
}
