package com.example.onlinepathshala.Authority;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Transaction_History_For_Authority;

public class Transaction extends Fragment {


    public Transaction() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_transaction, container, false);
        Button btn_student_fee=view.findViewById(R.id.student_fee);
        Button btn_teacher_salary=view.findViewById(R.id.teacher_salary);
        Button btn_stuff_salary=view.findViewById(R.id.stuff_salary);
        Button btn_other_transaction=view.findViewById(R.id.other_transaction);
        Button btn_transaction_history=view.findViewById(R.id.transaction_history);

        btn_student_fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(),Student_Fees.class));
            }
        });
        btn_teacher_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(),Teacher_Salary.class));
            }
        });
        btn_stuff_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Stuff_Salary.class));
            }
        });
        btn_other_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(),Other_Transaction.class));
            }
        });
        btn_transaction_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), Transaction_History_For_Authority.class));
            }
        });
        return view;
    }


}
