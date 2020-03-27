package com.example.onlinepathshala.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Authority.Messenger;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class School_Profile extends AppCompatActivity {

    String school_id,school_name,auth_id,auth_name,email,phone_number,date,status,image_path,device_id,account_status;
    TextView tv_school_id,tv_school_name,tv_auth_id,tv_auth_name,tv_email,tv_phone_number,tv_date,tv_status,tv_device_id,tv_account_status;
    ImageView imageView;
    SwitchCompat switchCompat;
    ImageView message,phone;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__profile);

        progressDialog=new ProgressDialog(School_Profile.this);
        progressDialog.setMessage("Please Wait...");
        school_id=getIntent().getStringExtra("school_id");
        school_name=getIntent().getStringExtra("school_name");
        auth_id=getIntent().getStringExtra("auth_id");
        auth_name=getIntent().getStringExtra("auth_name");
        image_path=getIntent().getStringExtra("image_path");
        phone_number=getIntent().getStringExtra("phone_number");
        email=getIntent().getStringExtra("email");
        device_id=getIntent().getStringExtra("device_id");
        status=getIntent().getStringExtra("status");
        date=getIntent().getStringExtra("date");
        switchCompat=findViewById(R.id.swith);
        tv_phone_number=findViewById(R.id.phone_number);
        tv_date=findViewById(R.id.join_date);
        tv_email=findViewById(R.id.email);
        tv_auth_name=findViewById(R.id.authority_name);
        tv_auth_id=findViewById(R.id.authority_id);
        tv_school_name=findViewById(R.id.school_name);
        tv_school_id=findViewById(R.id.school_id);
        tv_phone_number=findViewById(R.id.phone_number);
        tv_status=findViewById(R.id.status);
        imageView=findViewById(R.id.school_image);
        message=findViewById(R.id.message);
        phone=findViewById(R.id.call);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(), Messenger.class);
                tnt.putExtra("sender_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                tnt.putExtra("receiver_id",auth_id);
                tnt.putExtra("sender_type",SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type());
                tnt.putExtra("receiver_type","Authority");
                tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getDevice_id());
                tnt.putExtra("receiver_device_id",device_id);
                tnt.putExtra("image_path","");
                startActivity(tnt);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_school_id.setText(school_id);
        tv_school_name.setText(school_name);
        tv_auth_id.setText(auth_id);
        tv_auth_name.setText(auth_name);
        tv_email.setText(email);
        tv_date.setText(date);
        tv_phone_number.setText(phone_number);
        if(!image_path.equalsIgnoreCase("null")){

            Picasso.get().load(image_path).placeholder(R.drawable.teacher_panel_header).into(imageView);
        }
        tv_status.setText(status);

        if(status.equalsIgnoreCase("1")){

            switchCompat.setChecked(true);
            tv_status.setText("Active");
        }
        else{
            switchCompat.setChecked(false);
            tv_status.setText("De-Active");
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                   tv_status.setText("Active");
                    showDialog("Are you sure to Active this account ?","1");
                }
                else{

                    tv_status.setText("De-Active");
                    showDialog("Are you sure to De-active this account ?","0");
                }
            }
        });

    }

    public void showDialog(String message,String value){

            AlertDialog.Builder builder = new AlertDialog.Builder(School_Profile.this);
            View view = getLayoutInflater().inflate(R.layout.profile_edit_dialogbox2, null);
            TextView tv_title = view.findViewById(R.id.title);
            tv_title.setText(message);
            Button cancel = view.findViewById(R.id.cancel);
            Button confirm = view.findViewById(R.id.confirm);
            builder.setView(view);
            final AlertDialog dialog = builder.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        dialog.dismiss();
                        progressDialog.show();
                        update(value, "account_status");

                }
            });
    }

    public void update(final String value, final String column){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.change_account_status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),column+" Updated Successfully", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getApplicationContext(), "Fail to update", Toast.LENGTH_SHORT).show();
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
                params.put("value", value);
                params.put("id",auth_id);
                params.put("column",column);
                params.put("table", "Authority");
                params.put("db","online_pathshala");
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
