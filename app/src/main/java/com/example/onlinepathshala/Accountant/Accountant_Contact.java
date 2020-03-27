package com.example.onlinepathshala.Accountant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.onlinepathshala.Authority.All_Moderator;
import com.example.onlinepathshala.Authority.Principal_Profile_View;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Student.Contact_With_Teacher;
import com.example.onlinepathshala.Teacher.Contact_With_Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Accountant_Contact extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountant__contact);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new Contact_With_Teacher()).commit();
        actionBar=getSupportActionBar();
        actionBar.setTitle("Contact");
    }
    public void changeFragmentView(Fragment fragment){


        fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.teacher:
                actionBar.setTitle("All Teachers");
                changeFragmentView(new Contact_With_Teacher());
                return true;
            case R.id.authority:
                actionBar.setTitle("All Moderator");
                changeFragmentView(new All_Moderator("view only"));
                return true;
            case R.id.principal:
                actionBar.setTitle("Principal");
                changeFragmentView(new Principal_Profile_View());
                return true;
            case R.id.student:
                actionBar.setTitle("All Students");
                changeFragmentView(new Contact_With_Student());
                return true;

        }
        return true;
    }
}
