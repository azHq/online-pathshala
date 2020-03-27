package com.example.onlinepathshala.Student;

import androidx.appcompat.app.ActionBar;
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


public class My_Classes extends Fragment {


    ActionBar actionBar;
    public My_Classes(ActionBar actionBar) {

        this.actionBar=actionBar;
    }


    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_my__classes, container, false);
        frameLayout=view.findViewById(R.id.frame_layout);
        bottomNavigationView=view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.today:
                        actionBar.setTitle("Today's Classes");
                        changeFragmentView(new Today_Classes());
                        return  true;
                    case R.id.all:
                        actionBar.setTitle("All Classes");
                        changeFragmentView(new All_Classes());
                        return true;

                }
                return true;
            }
        });
        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new Today_Classes()).commit();

        return view;
    }



    public void changeFragmentView(Fragment fragment){

        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();

    }

}
