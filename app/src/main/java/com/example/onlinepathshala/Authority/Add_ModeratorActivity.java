package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add_ModeratorActivity extends AppCompatActivity {

    AlertDialog alertDialog;
    EditText et_school_name,et_autority_name,et_email,et_phone_number;
    TextView error;
    Spinner sp_authority_type;
    String[] users={"Choose Moderator Type","Vice_Principal","Accountant","Stuff"};
    String school_name,authority_name,email="",phone_number,user_type;
    ProgressDialog progressDialog;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__moderator);

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                authority_name=et_autority_name.getText().toString();
                email=et_email.getText().toString();
                phone_number=et_phone_number.getText().toString();
                remove_country_code(phone_number);

                if(authority_name.length()>=3&&emailValidation()&&phone_number_validation(phone_number)&&!user_type.contains("Choose authority Type")){

                    add_new_user();
                }
                else{

                    show_error_message(set_error_message(authority_name,email,phone_number,user_type),"Invalid Info");

                }
            }
        });
        progressDialog=new ProgressDialog(getApplicationContext());
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

    public String remove_country_code(String phone_number){
        phone_number=phone_number.trim().replace("+","");
        if(phone_number.startsWith("88")){

            phone_number=phone_number.substring(2,phone_number.length()-1);
        }

        return phone_number;
    }

    public void show_error_message(String message,String title){

        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
        View view=getLayoutInflater().inflate(R.layout.error_message,null);
        builder.setView(view);
        Button ok=view.findViewById(R.id.ok);
        TextView tv_title=view.findViewById(R.id.title);
        TextView tv_message=view.findViewById(R.id.message);
        tv_title.setText(title);
        tv_message.setText(message);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
        alertDialog=builder.show();
    }

    public String set_error_message(String authority_name,String email,String phone_number,String user_type){


        if(authority_name.length()<3) return "authority_name length must be 4 charecter";
        else if(!emailValidation()) return "please enter valid email address";
        else if(!phone_number_validation(phone_number)) return "please enter valid phone number";
        else if(user_type.contains("Choose authority Type")) return "please choose authority type";
        return "Please Fillup The Form With Correct Info";
    }


    public void add_new_user(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_moderator_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            authority_name="";
                            et_autority_name.setText("");
                            et_email.setText("");
                            et_phone_number.setText("");
                            show_success_message();

                        } else {
                            Toast.makeText(getApplicationContext(), "Fail To Add Moderator", Toast.LENGTH_SHORT).show();
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
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                params.put("table","Moderator");
                params.put("authority_name", authority_name);
                params.put("email", email);
                params.put("phone", phone_number);
                params.put("type", user_type);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void show_success_message(){

        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.success_message,null);
        All_Dialog_Message all_dialog_message=new All_Dialog_Message(Add_ModeratorActivity.this);
        all_dialog_message.show_success_message("Success","Moderator Added Successfully",view);
    }

}
