package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.ActionBar;

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

public class Section_Wise_Student extends Fragment {

    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    ActionBar actionBar;
    String activity_type="";

    public Section_Wise_Student(ActionBar actionBar,String activity_type){
        this.actionBar=actionBar;
        this.activity_type=activity_type;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_section__wise__student, container, false);
        frameLayout=view.findViewById(R.id.frame_layout);
        bottomNavigationView=view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.class_routine:
                        if(actionBar!=null) actionBar.setTitle("Bangla Medium");
                        changeFragmentView(new Medium_Wise_Classes("Bangla",activity_type));
                        return  true;
                    case R.id.exam_routine:
                        if(actionBar!=null) actionBar.setTitle("English Medium");
                        changeFragmentView(new Medium_Wise_Classes("English",activity_type));
                        return true;
                }
                return true;
            }
        });
        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new Medium_Wise_Classes("Bangla",activity_type)).commit();
        return view;
    }

    public void changeFragmentView(Fragment fragment){

        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();

    }

}
