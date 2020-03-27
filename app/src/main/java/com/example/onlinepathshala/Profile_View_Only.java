package com.example.onlinepathshala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Messenger;
import com.example.onlinepathshala.Student.Teacher_Evaluation;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_View_Only extends AppCompatActivity {

    String id,table_name,school_id,phone_number,email,name,image_path,device_id;
    ProgressDialog progressDialog;
    TextView tv_name,tv_email,tv_phone_number,tv_id;
    LinearLayout bt_rating,bt_message,bt_call,bt_details;
    CircleImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_principal__profile);
        id=getIntent().getStringExtra("id");
        school_id=getIntent().getStringExtra("school_id");
        table_name=getIntent().getStringExtra("table");
        phone_number=getIntent().getStringExtra("phone_number");
        email=getIntent().getStringExtra("email");
        name=getIntent().getStringExtra("name");
        image_path=getIntent().getStringExtra("image_path");
        device_id=getIntent().getStringExtra("device_id");
        profile_image=findViewById(R.id.profile_image);
        if(image_path!=null&&!image_path.equalsIgnoreCase("null")){


            Picasso.get().load(image_path).placeholder(R.drawable.user).into(profile_image);
        }
        progressDialog=new ProgressDialog(Profile_View_Only.this);
        tv_name=findViewById(R.id.name);
        tv_name.setText(name);
        tv_email=findViewById(R.id.email);
        tv_id=findViewById(R.id.id);
        tv_id.setText(id);
        tv_email.setText(email);
        tv_phone_number=findViewById(R.id.phone_number);
        tv_phone_number.setText("+88"+phone_number);

        bt_rating=findViewById(R.id.rating);
        bt_call=findViewById(R.id.call);
        bt_message=findViewById(R.id.message);
        bt_details=findViewById(R.id.more_info);
        
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(), Messenger.class);
                tnt.putExtra("sender_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                tnt.putExtra("receiver_id",id);
                tnt.putExtra("sender_type",SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type());
                tnt.putExtra("receiver_type","Teacher");
                tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getDevice_id());
                tnt.putExtra("receiver_device_id",device_id);
                tnt.putExtra("image_path",image_path);
                startActivity(tnt);

            }
        });
        bt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                Toast.makeText(getApplicationContext(),"call sending",Toast.LENGTH_LONG).show();
                phoneIntent.setData(Uri.parse("tel:"+phone_number));


                if (ActivityCompat.checkSelfPermission(Profile_View_Only.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(phoneIntent);
            }
        });
        bt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(school_id);
        postparams.put(id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.select_single_row,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }




                                progressDialog.dismiss();
                            }
                            else{

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));

                return headers;

            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);


    }
}
