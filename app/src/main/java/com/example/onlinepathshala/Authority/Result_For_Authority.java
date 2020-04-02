package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onlinepathshala.Final_Result_View_For_All;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Student.Final_Result;
import com.example.onlinepathshala.Student.Other_Result;

public class Result_For_Authority extends AppCompatActivity {

    String student_id,student_name;
    String class_id,section_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result__for__authority);
        student_id=getIntent().getStringExtra("student_id");
        student_name=getIntent().getStringExtra("student_name");
        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        Button class_test=findViewById(R.id.class_test);
        Button monthly_test=findViewById(R.id.monthly_test);
        Button half_yearly_test=findViewById(R.id.half_yearly);
        Button final_exam=findViewById(R.id.final_exam);
        Button final_result=findViewById(R.id.final_result);
        class_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(), Other_Result_For_Auth.class);
                tnt.putExtra("exam_type","Class Test");
                tnt.putExtra("student_id",student_id);
                tnt.putExtra("class_id",class_id);
                tnt.putExtra("section_id",section_id);
                tnt.putExtra("student_name",student_name);
                startActivity(tnt);

            }
        });
        monthly_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(),Other_Result_For_Auth.class);
                tnt.putExtra("exam_type","Monthly Test");
                tnt.putExtra("student_id",student_id);
                tnt.putExtra("class_id",class_id);
                tnt.putExtra("section_id",section_id);
                tnt.putExtra("student_name",student_name);
                startActivity(tnt);
            }
        });
        half_yearly_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(),Other_Result_For_Auth.class);
                tnt.putExtra("exam_type","Half Yearly");
                tnt.putExtra("student_id",student_id);
                tnt.putExtra("class_id",class_id);
                tnt.putExtra("section_id",section_id);
                tnt.putExtra("student_name",student_name);
                startActivity(tnt);
            }
        });
        final_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(),Other_Result_For_Auth.class);
                tnt.putExtra("exam_type","Final Exam");
                tnt.putExtra("student_id",student_id);
                tnt.putExtra("class_id",class_id);
                tnt.putExtra("section_id",section_id);
                tnt.putExtra("student_name",student_name);
                startActivity(tnt);
            }
        });
        final_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(), Final_Result_View_For_All.class);
                tnt.putExtra("exam_type","Final Exam");
                tnt.putExtra("student_id",student_id);
                tnt.putExtra("class_id",class_id);
                tnt.putExtra("section_id",section_id);
                tnt.putExtra("student_name",student_name);
                startActivity(tnt);
            }
        });
    }
}
