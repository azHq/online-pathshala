package com.example.onlinepathshala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Account_Deactive_Message extends AppCompatActivity {

    Button paymet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__deactive__message);

        paymet=findViewById(R.id.payemt);
        if(!SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type().equalsIgnoreCase("Principal")) paymet.setVisibility(View.GONE);
        paymet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),Payment.class));
            }
        });
    }


}
