package com.example.onlinepathshala.Student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinepathshala.Authority.Admin_profile_For_Contact;
import com.example.onlinepathshala.Authority.All_Moderator;
import com.example.onlinepathshala.Authority.Principal_Profile_View;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Teacher.Contact_With_Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Contact extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    ActionBar actionBar;

    public Contact(ActionBar actionBar){

        this.actionBar=actionBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_contact, container, false);
        bottomNavigationView=view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new Contact_With_Teacher()).commit();
        actionBar.setTitle("All Teacher");
        return view;
    }

    public void changeFragmentView(Fragment fragment){


        fragmentManager =getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.teacher:
                actionBar.setTitle("All Teacher");
                changeFragmentView(new Contact_With_Teacher());
                return true;
            case R.id.student:
                actionBar.setTitle("All Students");
                changeFragmentView(new Contact_With_Student());
                return true;
            case R.id.authority:
                actionBar.setTitle("All Moderator");
                changeFragmentView(new All_Moderator("view only"));
                return true;
            case R.id.principal:
                actionBar.setTitle("Principal");
                changeFragmentView(new Principal_Profile_View());
                return true;

        }
        return true;
    }
}
