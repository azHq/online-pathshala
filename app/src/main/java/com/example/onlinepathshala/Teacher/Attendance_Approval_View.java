package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.onlinepathshala.Text;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Attendance_Approval_View extends AppCompatActivity {

    RecyclerView recyclerView;
    Button skip;
    ArrayList<Attendance_Approval_Info> attendance_approval_infos=new ArrayList<>();
    ProgressDialog progressDialog;
    String class_id,section_id,class_name,section_name;
    String class_teacher_id="",teacher_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance__approval__view);
        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        class_name=getIntent().getStringExtra("class_name");
        section_name=getIntent().getStringExtra("section_name");
        class_teacher_id=getIntent().getStringExtra("teacher_id");
        skip=findViewById(R.id.skip);
        recyclerView=findViewById(R.id.recycle);
        progressDialog=new ProgressDialog(Attendance_Approval_View.this);
        progressDialog.setMessage("Please Wait");
        teacher_id=SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();

        if(class_teacher_id.equalsIgnoreCase(teacher_id)){
            getAllMemberData();
        }
        else{

            Intent tnt=new Intent(getApplicationContext(), Attendence.class);
            tnt.putExtra("class_id",class_id);
            tnt.putExtra("class_name",class_name);
            tnt.putExtra("section_id",section_id);
            tnt.putExtra("teacher_id",class_teacher_id);
            tnt.putExtra("section_name",section_name);
            startActivity(tnt);
            finish();
        }
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(), Attendence.class);
                tnt.putExtra("class_id",class_id);
                tnt.putExtra("class_name",class_name);
                tnt.putExtra("section_id",section_id);
                tnt.putExtra("teacher_id",class_teacher_id);
                tnt.putExtra("section_name",section_name);
                startActivity(tnt);
                finish();
            }
        });
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Attendance_Approval_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_attendance_approval,postparams,
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
                                        String id = member.getString("id");
                                        String class_id=member.getString("class_id");
                                        String section_id = member.getString("section_id");
                                        String teacher_id = member.getString("teacher_id");
                                        String teacher_name = member.getString("teacher_name");
                                        String date = member.getString("attendance_date");
                                        attendance_approval_infos.add(new Attendance_Approval_Info(id,class_id,section_id,teacher_id,teacher_name,date));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                RecycleAdapter recycleAdapter=new RecycleAdapter(attendance_approval_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{
                                Intent tnt=new Intent(getApplicationContext(), Attendence.class);
                                tnt.putExtra("class_id",class_id);
                                tnt.putExtra("class_name",class_name);
                                tnt.putExtra("section_id",section_id);
                                tnt.putExtra("teacher_id",class_teacher_id);
                                tnt.putExtra("section_name",section_name);
                                startActivity(tnt);
                                finish();
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


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Attendance_Approval_Info> home_task_infos;
        public RecycleAdapter(ArrayList<Attendance_Approval_Info> classinfos){
            this.home_task_infos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView title,date;
            Button cancel,approve;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                title=mView.findViewById(R.id.title);
                date=mView.findViewById(R.id.date);
                cancel=mView.findViewById(R.id.cancel);
                approve=mView.findViewById(R.id.approve);

            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_approval_layout,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Attendance_Approval_Info memberInfo=home_task_infos.get(position);
            holder.title.setText(memberInfo.teacher_name+"(ID: "+memberInfo.teacher_id+")\n Gave Your Class Attendance");
            holder.date.setText(memberInfo.attendance_date);
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    approval_cancel(memberInfo.class_id,memberInfo.section_id,memberInfo.attendance_date);
                }
            });

            holder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    approve_attendance(memberInfo.class_id,memberInfo.section_id,memberInfo.attendance_date);
                }
            });


        }

        @Override
        public int getItemCount() {
            return home_task_infos.size();
        }



    }

    public void approval_cancel(String class_id,String section_id,String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.attendance_approve_cancel,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Attendance Approval Cancel", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {


                            Toast.makeText(getApplicationContext(), "Fail To Cancel Attendance Approval", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id",class_id);
                params.put("section_id",section_id);
                params.put("date",date);
                params.put("table", "Attendance_Approval_Table");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void approve_attendance(String class_id,String section_id,String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.approve_attendance,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Attendance Approved Successfully", Toast.LENGTH_SHORT).show();
                             getAllMemberData();

                        } else {


                            Toast.makeText(getApplicationContext(), "Fail To Approve Attendance", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id",class_id);
                params.put("section_id",section_id);
                params.put("date",date);
                params.put("table", "Attendance_Approval_Table");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}
