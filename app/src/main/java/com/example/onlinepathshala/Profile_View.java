package com.example.onlinepathshala;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_View extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView tv_name,tv_password,tv_email,tv_phone_number;
    ImageView edit_name,edit_phone,edit_password,edit_email;
    ProgressDialog progressDialog;
    String password,name,email,phone_number;
    String image_path;
    private int PICK_IMAGE_REQUEST = 1;
    String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__view);

        id=getIntent().getStringExtra("id");
        tv_name=findViewById(R.id.name);
        tv_email=findViewById(R.id.email);
        tv_phone_number=findViewById(R.id.phone_number);


        edit_email=findViewById(R.id.edit_email);
        edit_name=findViewById(R.id.edit_name);
        edit_phone=findViewById(R.id.edit_phone);
        edit_password=findViewById(R.id.edit_password);
        circleImageView=findViewById(R.id.profile_image);


        progressDialog=new ProgressDialog(Profile_View.this);

        progressDialog.setMessage("Please Wait");
        getAllData();
    }

    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("Moderator");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(id);
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.select_single_row,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String school_name=SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_name();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        name=member.getString("moderator_name");
                                        email=member.getString("email");
                                        phone_number=member.getString("phone_number");
                                        password =member.getString("password");
                                        image_path=member.getString("image_path");

                                        if(!image_path.equalsIgnoreCase("null")){


                                            Picasso.get().load(image_path).placeholder(R.drawable.profile2).into(circleImageView);
                                        }
                                        tv_name.setText(name);
                                        tv_email.setText(email);
                                        tv_phone_number.setText("+88"+phone_number);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_LONG).show();
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
