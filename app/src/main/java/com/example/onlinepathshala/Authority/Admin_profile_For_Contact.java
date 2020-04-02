package com.example.onlinepathshala.Authority;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Admin_profile_For_Contact extends Fragment {


    public Admin_profile_For_Contact() {

    }


    String id="";
    String device_id="";
    CircleImageView circleImageView;
    TextView tv_name,tv_password,tv_email,tv_phone_number;
    ImageView edit_name,edit_phone,edit_password,edit_email,tv_details;
    ProgressDialog progressDialog;
    String password,name,email,phone_number,company_info,service_info;
    String image_path;
    private int PICK_IMAGE_REQUEST = 1;
    LinearLayout rating,phone_call,message_send;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_admin_profile__for__contact, container, false);
        tv_name=view.findViewById(R.id.name);
        tv_email=view.findViewById(R.id.email);
        tv_phone_number=view.findViewById(R.id.phone_number);
        circleImageView=view.findViewById(R.id.profile_image);
        phone_call=view.findViewById(R.id.call);
        message_send=view.findViewById(R.id.message);
        tv_details=view.findViewById(R.id.details);

        tv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(),Admin_About_For_View.class);
                tnt.putExtra("company_info",company_info);
                tnt.putExtra("service_info",service_info);
                startActivity(tnt);
            }
        });
        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                Toast.makeText(getContext(),"call sending",Toast.LENGTH_LONG).show();
                phoneIntent.setData(Uri.parse("tel:01795528283"));


                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(phoneIntent);
            }
        });
        message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(), Messenger.class);
                tnt.putExtra("sender_id", SharedPrefManager.getInstance(getContext()).getUser().getId());
                tnt.putExtra("receiver_id",id);
                tnt.putExtra("sender_type",SharedPrefManager.getInstance(getContext()).getUser().getUser_type());
                tnt.putExtra("receiver_type","Principal");
                tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getContext()).getUser().getDevice_id());
                tnt.putExtra("receiver_device_id",device_id);
                tnt.putExtra("image_path",image_path);
                startActivity(tnt);
            }
        });

        progressDialog=new ProgressDialog(getContext());

        progressDialog.setMessage("Please Wait");
        getAllData();
        return view;
    }

    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("admin");
        postparams.put("online_pathshala");
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_admin_info,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String school_name=SharedPrefManager.getInstance(getContext()).getUser().getSchool_name();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        name=member.getString("name");
                                        password =member.getString("password");
                                        id=member.getString("id");


                                        if(member.has("company_info")){
                                            company_info=member.getString("company_info");
                                        }
                                        else{
                                            company_info="Yet Not Publish";
                                        }
                                        if(member.has("service_info")){
                                            service_info=member.getString("service_info");
                                        }
                                        else{
                                            service_info="Yet Not Publish";
                                        }

                                        if(member.has("device_id")){
                                            device_id=member.getString("device_id");
                                        }
                                        else{
                                            device_id="None";
                                        }


                                        if(member.has("email")){
                                            email=member.getString("email");
                                        }
                                        else{
                                            email="None";
                                        }

                                        if(member.has("phone_number")){

                                            phone_number=member.getString("phone_number");
                                        }
                                        else{

                                            phone_number="";
                                        }

                                        if(member.has("image_path")){
                                            image_path=member.getString("image_path");
                                            if(!image_path.equalsIgnoreCase("null")){


                                                Picasso.get().load(image_path).placeholder(R.drawable.profile2).into(circleImageView);
                                            }
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
                                Toast.makeText(getContext(),"No Data",Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }


}
