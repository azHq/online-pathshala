package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.onlinepathshala.R;

public class Admin_About_For_View extends AppCompatActivity {

    TextView tv_company_info,tv_service_info;
    String company_info,service_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__about__for__view);
        company_info=getIntent().getStringExtra("company_info");
        service_info=getIntent().getStringExtra("service_info");

        tv_company_info=findViewById(R.id.company);
        tv_service_info=findViewById(R.id.service);

        tv_company_info.setText(company_info);
        tv_service_info.setText(service_info);

    }
}
