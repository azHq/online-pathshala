package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.onlinepathshala.Authority.Class_Routine_For_Auth;
import com.example.onlinepathshala.Authority.Exam_Routine_For_Auth;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Student.Class_Routine;
import com.example.onlinepathshala.Student.Exam_Routine;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Routine_View extends AppCompatActivity {

    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine__view);

        frameLayout=findViewById(R.id.frame_layout);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.class_routine:
                        changeFragmentView(new Class_Routine_For_Auth("teacher"));
                        return  true;
                    case R.id.exam_routine:
                        changeFragmentView(new Exam_Routine_For_Auth("teacher"));
                        return true;

                }
                return true;
            }
        });
        fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new Class_Routine_For_Auth("teacher")).commit();
    }

    public void changeFragmentView(Fragment fragment){

        fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();

    }
}
