package com.example.onlinepathshala.Student;

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

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Authority.Teacher;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Profile_View_Only;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Teacher_List extends Fragment {

    public Teacher_List() {

    }

    FloatingActionButton dragView;
    ArrayList<Teacher> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    String teacher_id,name,email,image_path,device_id,phone_number;
    TextView tv_name,tv_email,tv_phone_number,no_item,no_class_teacher;
    Button btn_rating,btn_profile;
    LinearLayout view_profile;

    @Override
    public View onCreateView(LayoutInflater inflater,final  ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_teacher__list, container, false);
        frameLayout=view.findViewById(R.id.frame_layout);
        recyclerView=view.findViewById(R.id.recycle);
        view_profile=view.findViewById(R.id.view_profile);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        tv_name=view.findViewById(R.id.name);
        tv_email=view.findViewById(R.id.email);
        no_item=view.findViewById(R.id.no_item);
        no_class_teacher=view.findViewById(R.id.no_class_teacher);
        tv_phone_number=view.findViewById(R.id.phone_number);
        btn_profile=view.findViewById(R.id.profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(), Profile_View_Only.class);
                tnt.putExtra("id",teacher_id);
                tnt.putExtra("school_id",SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                tnt.putExtra("table","Teacher");
                tnt.putExtra("name",name);
                tnt.putExtra("phone_number",phone_number);
                tnt.putExtra("email",email);
                tnt.putExtra("device_id",device_id);
                tnt.putExtra("image_path",image_path);
                startActivity(tnt);
            }
        });
        btn_rating=view.findViewById(R.id.rating);
        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getContext(),Teacher_Evaluation.class);
                tnt.putExtra("name",name);
                tnt.putExtra("id",teacher_id);
                startActivity(tnt);
            }
        });
        get_class_teacher();
        getAllMemberData();

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void get_class_teacher(){


        JSONArray postparams = new JSONArray();
        postparams.put("Teacher");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().section_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_class_teacher,postparams,
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
                                        teacher_id= member.getString("class_teacher_id");
                                        name= member.getString("teacher_name");
                                        image_path=member.getString("image_path");
                                        email=member.getString("email");
                                        phone_number=member.getString("phone_number");
                                        device_id=member.getString("device_id");

                                        tv_name.setText(name);
                                        tv_email.setText(email);
                                        tv_phone_number.setText(phone_number);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                progressDialog.dismiss();


                            }
                            else{



                                no_class_teacher.setVisibility(View.VISIBLE);
                                view_profile.setVisibility(View.GONE);
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


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Teacher");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().section_id);
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.select_all_teacher_in_a_secction_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);

                            memberInfos.clear();
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String user_name = member.getString("teacher_name");
                                        String image_path=member.getString("image_path");
                                        String email=member.getString("email");
                                        String phone_number=member.getString("phone_number");
                                        String device_id=member.getString("device_id");
                                        memberInfos.add(new Teacher(id,user_name,"","",image_path,phone_number,email,device_id,""));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                no_item.setVisibility(View.GONE);
                            }
                            else{

                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                Toast.makeText(getContext(),"No Teacher Still Added",Toast.LENGTH_LONG).show();
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



    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Teacher> memberInfos;
        public RecycleAdapter(ArrayList<Teacher> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            Button rating,profile;
            LinearLayout linearLayout;
            CircleImageView imageView;
            TextView name,class_name,subject_name;
            CircleImageView profile_image;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                imageView=mView.findViewById(R.id.profile_image);
                linearLayout=mView.findViewById(R.id.view_profile);

                name=mView.findViewById(R.id.name);
                class_name=mView.findViewById(R.id.class_name);
                subject_name=mView.findViewById(R.id.subject_name);
                profile_image=mView.findViewById(R.id.profile_image);
                rating=mView.findViewById(R.id.rating);
                profile=mView.findViewById(R.id.profile);

            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_rec_list_item2,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final Teacher memberInfo=memberInfos.get(position);
            holder.name.setText(memberInfo.name);
            holder.class_name.setText(memberInfo.email);
            holder.subject_name.setText(memberInfo.phone_number);
            holder.name.setSelected(true);
            holder.class_name.setSelected(true);
            holder.subject_name.setSelected(true);
            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.profile2).into(holder.profile_image);
            }

            holder.rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getContext(),Teacher_Evaluation.class);
                    tnt.putExtra("name",memberInfo.name);
                    tnt.putExtra("id",memberInfo.id);
                    startActivity(tnt);
                }
            });

            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getContext(), Profile_View_Only.class);
                    tnt.putExtra("id",memberInfo.id);
                    tnt.putExtra("school_id",SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                    tnt.putExtra("table","Teacher");
                    tnt.putExtra("name",memberInfo.name);
                    tnt.putExtra("phone_number",memberInfo.phone_number);
                    tnt.putExtra("email",memberInfo.email);
                    tnt.putExtra("device_id",memberInfo.device_id);
                    tnt.putExtra("image_path",memberInfo.image_path);
                    startActivity(tnt);
                }
            });


        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }



    }



}
