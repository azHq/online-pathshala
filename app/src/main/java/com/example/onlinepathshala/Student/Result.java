package com.example.onlinepathshala.Student;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.onlinepathshala.Final_Result_View_For_All;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;


public class Result extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_result, container, false);
        Button class_test=view.findViewById(R.id.class_test);
        Button monthly_test=view.findViewById(R.id.monthly_test);
        Button half_yearly_test=view.findViewById(R.id.half_yearly);
        Button final_exam=view.findViewById(R.id.final_exam);
        Button final_result=view.findViewById(R.id.final_result);
        class_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(), Other_Result.class);
                tnt.putExtra("exam_type","Class Test");
                tnt.putExtra("student_id", SharedPrefManager.getInstance(getContext()).getUser().getId());
                tnt.putExtra("class_id",SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
                startActivity(tnt);

            }
        });
        monthly_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(),Other_Result.class);
                tnt.putExtra("exam_type","Monthly Test");
                tnt.putExtra("student_id", SharedPrefManager.getInstance(getContext()).getUser().getId());
                tnt.putExtra("class_id",SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
                startActivity(tnt);
            }
        });
        half_yearly_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(),Other_Result.class);
                tnt.putExtra("exam_type","Half Yearly");
                tnt.putExtra("student_id", SharedPrefManager.getInstance(getContext()).getUser().getId());
                tnt.putExtra("class_id",SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
                startActivity(tnt);
            }
        });
        final_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(),Other_Result.class);
                tnt.putExtra("exam_type","Final Exam");
                tnt.putExtra("student_id", SharedPrefManager.getInstance(getContext()).getUser().getId());
                tnt.putExtra("class_id",SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
                startActivity(tnt);
            }
        });
        final_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(), Final_Result_View_For_All.class);
                tnt.putExtra("exam_type","Final Exam");
                tnt.putExtra("student_id", SharedPrefManager.getInstance(getContext()).getUser().getId());
                tnt.putExtra("class_id",SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
                tnt.putExtra("section_id",SharedPrefManager.getInstance(getContext()).get_student_info().section_id);
                startActivity(tnt);
            }
        });
        return view;
    }


}
