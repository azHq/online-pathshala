package com.example.onlinepathshala.Student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.onlinepathshala.Authority.All_Exam;
import com.example.onlinepathshala.Authority.Exam_Infos;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Up_Coming_Exam extends Fragment {


    public Up_Coming_Exam() {
        // Required empty public constructor
    }
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    TextView no_item;
    String medium,shift_name;
    AlertDialog alertDialog2;
    String class_id,activity_type,class_name,section_id,section_name;
    AlertDialog alertDialog3;
    String teacher_name,teacher_id;
    ArrayList<Exam_Infos> exams=new ArrayList<>();
    ArrayList<String> all_exam_name=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_up__coming__exam, container, false);
        frameLayout=view.findViewById(R.id.frame_layout);
        recyclerView=view.findViewById(R.id.recycle);
        no_item=view.findViewById(R.id.no_item);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        getAllMemberData();
        return view;
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Exam");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().section_id);


        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_upcomming_exam,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        exams.clear();
                        all_exam_name.clear();
                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String exam_name = member.getString("exam_name");
                                        String exam_type=member.getString("exam_type");
                                        String subject_id = member.getString("subject_id");
                                        String subject_name = member.getString("subject_name");
                                        String date = member.getString("create_date");
                                        String time = member.getString("create_time");
                                        String result = member.getString("result_status");
                                        String marks = member.getString("marks");
                                        String topic = member.getString("topic");
                                        exams.add(new Exam_Infos(id,exam_name,exam_type,subject_id,subject_name,date,time,result,marks,topic));
                                        all_exam_name.add(exam_name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                RecycleAdapter recycleAdapter=new RecycleAdapter(exams);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                no_item.setVisibility(View.GONE);
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(exams);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
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

        ArrayList<Exam_Infos> classinfos;
        public RecycleAdapter(ArrayList<Exam_Infos> classinfos){
            this.classinfos=classinfos;
        }



        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView exam_name,exam_type,subject_name,date,time,mark,topic;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                exam_name=mView.findViewById(R.id.exam_name);
                exam_type=mView.findViewById(R.id.exam_type);
                subject_name=mView.findViewById(R.id.subject_name);
                date=mView.findViewById(R.id.date);
                mark=mView.findViewById(R.id.marks);
                topic=mView.findViewById(R.id.topic_name);
                linearLayout=mView.findViewById(R.id.view_profile);



            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_rec_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Exam_Infos memberInfo=classinfos.get(position);

            holder.exam_name.setText(memberInfo.exam_name);
            holder.exam_type.setText(memberInfo.exam_type);
            holder.subject_name.setText(memberInfo.subject_name);
            holder.date.setText(memberInfo.date+" "+memberInfo.time);
            holder.mark.setText(memberInfo.marks);
            if(memberInfo.topic!=null&&!memberInfo.topic.equalsIgnoreCase("NULL")) holder.topic.setText("Topic: "+memberInfo.topic);
            holder.exam_name.setSelected(true);
            holder.exam_type.setSelected(true);
            holder.subject_name.setSelected(true);
            holder.date.setSelected(true);
            holder.mark.setSelected(true);



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }


}
