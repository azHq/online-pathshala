package com.example.onlinepathshala.Teacher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.onlinepathshala.Admin.Member;
import com.example.onlinepathshala.Authority.All_Sections;
import com.example.onlinepathshala.Authority.All_Subject_Activity;
import com.example.onlinepathshala.Authority.Classes;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Teacher_Classes extends Fragment {

    FloatingActionButton dragView;
    ArrayList<Classes> class_infos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    TextView no_item;
    String medium,shift_name;
    AlertDialog alertDialog2,alertDialog3;
    ArrayList<Member> members=new ArrayList<>();
    String teacher_name,teacher_id;
    String activity_type="";
    public Teacher_Classes(String medium,String activity_type) {

        this.medium=medium;
        this.activity_type=activity_type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_teacher__classes, container, false);
        frameLayout=view.findViewById(R.id.frame_layout);
        recyclerView=view.findViewById(R.id.recycle);
        no_item=view.findViewById(R.id.no_item);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        class_infos=new ArrayList<>();
        getAllMemberData();
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Class_Table");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(medium);
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getId());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_teacher_classes,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        class_infos.clear();
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
                                        String total_student = member.getString("total_student");
                                        String total_section = member.getString("total_section");
                                        class_infos.add(new Classes(id,class_name,"",total_student,total_section));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                no_item.setVisibility(View.GONE);
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();

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


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter> {

        ArrayList<Classes> classinfos;
        public RecycleAdapter(ArrayList<Classes> classinfos){
            this.classinfos=classinfos;
        }



        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

            View mView;
            LinearLayout linearLayout;
            TextView teacher,students,sections,class_name;

            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                mView=itemView;
                students=mView.findViewById(R.id.student);
                sections=mView.findViewById(R.id.section);
                class_name=mView.findViewById(R.id.class_name);
                linearLayout=mView.findViewById(R.id.view_profile);



            }
            @Override
            public void onClick(View view) {

                int position =getLayoutPosition();
                Classes memberInfo=classinfos.get(position);

                if(activity_type.equalsIgnoreCase("attendence")){

                    Intent tnt=new Intent(getContext(), All_Sections_for_teacher.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    tnt.putExtra("class_name",memberInfo.class_name);
                    tnt.putExtra("type","attendence");
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("home task")){

                    Intent tnt=new Intent(getContext(), All_Sections_for_teacher.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    tnt.putExtra("class_name",memberInfo.class_name);
                    tnt.putExtra("type","home task");
                    startActivity(tnt);
                }




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

            holder.students.setText(memberInfo.total_students);
            holder.sections.setText(memberInfo.total_subjects);
            holder.class_name.setText(memberInfo.class_name);
            holder.class_name.setSelected(true);


        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

}
