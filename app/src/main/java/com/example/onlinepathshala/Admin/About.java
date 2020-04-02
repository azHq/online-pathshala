package com.example.onlinepathshala.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
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


public class About extends Fragment {


    public About() {
        // Required empty public constructor
    }


    ProgressDialog progressDialog;
    TextView tv_company_info,tv_service_info;
    String company_info,service_info;
    Button edit_company_info,edit_service_info;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_about, container, false);
        edit_company_info=view.findViewById(R.id.edit_company_info);
        edit_service_info=view.findViewById(R.id.edit_service_info);
        tv_company_info=view.findViewById(R.id.company);
        tv_service_info=view.findViewById(R.id.service);
        progressDialog=new ProgressDialog(getContext());

        edit_company_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("company_info","Edit Company Info",company_info);
            }
        });
        edit_service_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("service_info","Edit Service Info",service_info);
            }
        });
        getAllData();
        return view;
    }

    public void showEditDialog(final String column_name, String title, String hint) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.admin_profile_edit_dialogbox, null);
        TextView tv_title = view.findViewById(R.id.title);
        final EditText value = view.findViewById(R.id.value);
        value.setText(hint);
        tv_title.setText(title);
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

                if (value.getText().toString().length() > 1) {

                    progressDialog.show();
                    update(value.getText().toString(), column_name);
                    dialog.dismiss();
                } else {

                    Toast.makeText(getContext(), "Please Enter New Data", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void update(final String value, final String column){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.edit_admin_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getContext(),column+" Updated Successfully", Toast.LENGTH_SHORT).show();
                            getAllData();

                        } else {
                            Toast.makeText(getContext(), "Fail to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("value", value);
                params.put("id",SharedPrefManager.getInstance(getContext()).getUser().getId());
                params.put("column",column);
                params.put("table", "admin");
                params.put("school_id","online_pathshala");
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("admin");
        postparams.put("online_pathshala");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getId());
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.select_single_row,postparams,
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


                                        if(member.has("company_info")){
                                            company_info = member.getString("company_info");
                                            tv_company_info.setText(company_info);
                                        }
                                        else{

                                            tv_company_info.setText("Yet Not Publish");
                                        }

                                        if(member.has("service_info")){

                                            service_info = member.getString("service_info");
                                            tv_service_info.setText(service_info);
                                        }
                                        else{

                                            tv_service_info.setText("Yet Not Publish");
                                        }

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
