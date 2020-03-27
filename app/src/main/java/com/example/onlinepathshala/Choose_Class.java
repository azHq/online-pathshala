package com.example.onlinepathshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Choose_Class extends AppCompatActivity {

    ArrayList<Classes> class_infos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    RecycleAdapter recycleAdapter;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);


        frameLayout=findViewById(R.id.frame_layout);
        recyclerView=findViewById(R.id.recycle);
        recycleAdapter=new RecycleAdapter(class_infos);
        progressDialog=new ProgressDialog(Choose_Class.this);
        progressDialog.setMessage("Please wait...");
        getAllMemberData();

    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Class_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_data_url,postparams,
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
                                        String id = member.getString("class_id");
                                        String class_name=member.getString("class_name");
                                        String class_teacher = member.getString("class_teacher");
                                        String total_students=member.getString("total_students");
                                        String total_subjects=member.getString("total_subjects");
                                        class_infos.add(new Classes(id,class_name,class_teacher,total_students,total_subjects));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{

                                RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No Classs Still Added",Toast.LENGTH_LONG).show();
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


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter> {

        ArrayList<Classes> classinfos;
        public RecycleAdapter(ArrayList<Classes> classinfos){
            this.classinfos=classinfos;
        }



        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            View mView;
            LinearLayout linearLayout;
            TextView teacher,students,subject,class_name;

            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                teacher=mView.findViewById(R.id.teacher);
                students=mView.findViewById(R.id.student);
                subject=mView.findViewById(R.id.subject);
                class_name=mView.findViewById(R.id.class_name);
                linearLayout=mView.findViewById(R.id.view_profile);



            }
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Click",Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onLongClick(View view) {


                int position =getLayoutPosition();
                Classes memberInfo=classinfos.get(position);

                return true;
            }

        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Classes memberInfo=classinfos.get(position);
            if(memberInfo.class_teacher.equalsIgnoreCase("NULL")) holder.teacher.setText("Yet Not Assigned");
            else{

                holder.teacher.setText(memberInfo.class_teacher);
            }
            holder.teacher.setSelected(true);
            holder.students.setText(memberInfo.total_students);
            holder.subject.setText(memberInfo.total_subjects);
            holder.class_name.setText(memberInfo.class_name);
            holder.class_name.setSelected(true);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent tnt=new Intent(getApplicationContext(),Create_Exam.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    startActivity(tnt);
                }
            });


        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }


}
