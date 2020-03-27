package com.example.onlinepathshala.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.All_Dialog_Message;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.MainActivity;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.User;
import com.example.onlinepathshala.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add_School extends AppCompatActivity {

    EditText et_school_name,et_autority_name,et_email,et_phone_number;
    TextView error;
    Spinner sp_authority_type;
    String[] users={"Choose authority Type","Principal","Vice_Principal","Accountance","Stuff","Others"};
    String school_name,authority_name,email="",phone_number,user_type;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__school);

        progressDialog=new ProgressDialog(Add_School.this);
        et_school_name=findViewById(R.id.school);
        et_autority_name=findViewById(R.id.authority);
        et_email=findViewById(R.id.email);
        et_phone_number=findViewById(R.id.phone);
        sp_authority_type=findViewById(R.id.user_type);
        error=findViewById(R.id.error);

        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_text, users );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sp_authority_type.setAdapter(langAdapter);


        sp_authority_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                user_type=users[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public boolean emailValidation(){

        Pattern pattern;
        Matcher matcher;
        String regex = "^(.+)@(.+)$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean phone_number_validation( String phone_number){

        phone_number=phone_number.replace("-","");
        phone_number=phone_number.replaceAll("\\s","");
        String pattern="(^([+]{1}[8]{2}|88)?(01){1}[2-9]{1}\\d{8})$";

        return phone_number.matches(pattern);
    }

    public void onSubmit(View view){

        school_name=et_school_name.getText().toString();
        authority_name=et_autority_name.getText().toString();
        email=et_email.getText().toString();
        phone_number=et_phone_number.getText().toString();


        if(school_name.length()>3&&authority_name.length()>3&&emailValidation()&&phone_number_validation( phone_number)&&!user_type.contains("Choose authority Type")){

            review_info(view);
        }
        else{

            error.setVisibility(View.VISIBLE);
            error.setTextColor(Color.parseColor("#E64A19"));
            error.setText(set_error_message(school_name,authority_name,email,phone_number,user_type));
        }

    }

    public String set_error_message(String school_name,String authority_name,String email,String phone_number,String user_type){

        if(school_name.length()<3){

            return "School name length must be 4 charecter";
        }
        else if(authority_name.length()<3) return "authority_name length must be 4 charecter";
        else if(!emailValidation()) return "please enter valid email address";
        else if(!phone_number_validation( phone_number)) return "please enter valid phone number";
        else if(user_type.contains("Choose authority Type")) return "please choose authority type";


        return "Please Fillup The Form With Correct Info";
    }


    public void review_info(View v){


        AlertDialog.Builder alert=new AlertDialog.Builder(Add_School.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        ViewGroup viewGroup = findViewById(android.R.id.content);
        if( inflater!=null){


                View view =inflater.inflate(R.layout.info_review_builder,viewGroup,false);
                alert.setView(view);
                final AlertDialog alertDialog=alert.show();
                TextView school=view.findViewById(R.id.school_name);
                TextView auth=view.findViewById(R.id.authority);
                TextView type=view.findViewById(R.id.authority_type);
                TextView email2=view.findViewById(R.id.email);
                TextView phone=view.findViewById(R.id.phone);
                school.setText(school_name);
                auth.setText(authority_name);
                type.setText(user_type);
                email2.setText(email);
                phone.setText(phone_number);
                Button confirm=view.findViewById(R.id.confirm);
                Button cancel=view.findViewById(R.id.cancel);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        alertDialog.cancel();
                        error.setText("Add New School");
                        error.setTextColor(Color.WHITE);
                        progressDialog.setTitle("Adding New User");
                        progressDialog.setMessage("Please wait ....");
                        progressDialog.show();
                        add_new_user();


                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.cancel();
                    }
                });


        }
        else {

            add_new_user();
        }



    }
    public void add_new_user(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_athurity_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            authority_name="";
                            et_school_name.setText("");
                            et_autority_name.setText("");
                            et_email.setText("");
                            et_phone_number.setText("");
                            show_success_message();
                            //finish();
                            //startActivity(new Intent(getApplicationContext(), Admin_Panel.class));

                        } else {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("school_name", school_name);
                params.put("authority_name", authority_name);
                params.put("email", email);
                params.put("phone", phone_number);
                params.put("type", user_type);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void show_success_message(){

        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.success_message,null);
        All_Dialog_Message all_dialog_message=new All_Dialog_Message(Add_School.this);
        all_dialog_message.show_success_message("Success","Moderator Added Successfully",view);
    }
}
