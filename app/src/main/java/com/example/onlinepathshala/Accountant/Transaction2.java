package com.example.onlinepathshala.Accountant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onlinepathshala.Authority.Other_Transaction;
import com.example.onlinepathshala.Authority.Student_Fees;
import com.example.onlinepathshala.Authority.Stuff_Salary;
import com.example.onlinepathshala.Authority.Teacher_Salary;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Transaction_History_For_Authority;

public class Transaction2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction2);

        Button btn_student_fee=findViewById(R.id.student_fee);
        Button btn_teacher_salary=findViewById(R.id.teacher_salary);
        Button btn_stuff_salary=findViewById(R.id.stuff_salary);
        Button btn_other_transaction=findViewById(R.id.other_transaction);
        Button btn_transaction_history=findViewById(R.id.transaction_history);

        btn_student_fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Student_Fees.class));
            }
        });
        btn_teacher_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Teacher_Salary.class));
            }
        });
        btn_stuff_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Stuff_Salary.class));
            }
        });
        btn_other_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Other_Transaction.class));
            }
        });
        btn_transaction_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Transaction_History_For_Authority.class));
            }
        });
    }
}
