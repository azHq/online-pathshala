package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.onlinepathshala.Authority.All_Moderator;
import com.example.onlinepathshala.Authority.Principal_Profile_View;
import com.example.onlinepathshala.Authority.Teacher;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Student.Contact_With_Teacher;
import com.example.onlinepathshala.Student.Exam_Routine;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Teacher_Contact extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__contact);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new Contact_With_Student()).commit();
        actionBar=getSupportActionBar();
        actionBar.setTitle("All Students");
    }


    public void changeFragmentView(Fragment fragment){


        fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.student:
                actionBar.setTitle("All Students");
                changeFragmentView(new Contact_With_Student());
                return true;
            case R.id.teacher:
                actionBar.setTitle("All Teachers");
                changeFragmentView(new Contact_With_Teacher());
                return true;
            case R.id.authority:
                actionBar.setTitle("All Moderators");
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
