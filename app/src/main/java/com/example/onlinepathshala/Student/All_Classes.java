package com.example.onlinepathshala.Student;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Assign_Subject_List_Item;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class All_Classes extends Fragment {


    public All_Classes() {
        // Required empty public constructor
    }
    ArrayList<Assign_Subject_List_Item> assign_subject_list_items=new ArrayList<>();
    String[] days={"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    TextView no_item;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_all__classes2, container, false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..!");
        getAllAssignSubjectData();
        recyclerView=view.findViewById(R.id.recycle);
        no_item=view.findViewById(R.id.no_item);

        return view;
    }


    public void recycle_init(){

        RecycleAdapter recycleAdapter=new RecycleAdapter(assign_subject_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recycleAdapter);
    }
    public void getAllAssignSubjectData(){


        Calendar calendar=Calendar.getInstance();
        String day=days[calendar.get(Calendar.DAY_OF_WEEK)];
        JSONArray postparams = new JSONArray();
        postparams.put("Subject_Assigned_Table");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().section_id);
        postparams.put(day);
        postparams.put("all");


        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.today_classes_for_student,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        assign_subject_list_items.clear();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("ass_id");
                                        String class_name=member.getString("class_name");
                                        String teacher_name = member.getString("teacher_name");
                                        String subject_name=member.getString("subject_name");
                                        String class_id = member.getString("class_id");
                                        String teacher_id=member.getString("teacher_id");
                                        String section_id = member.getString("section_id");
                                        String subject_id = member.getString("subject_id");
                                        String start_time = member.getString("start_time");
                                        String end_time = member.getString("end_time");
                                        String day = member.getString("day");
                                        assign_subject_list_items.add(new Assign_Subject_List_Item(id,class_id,class_name,teacher_id,teacher_name,subject_id,subject_name,section_id,"",start_time,end_time,day));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                no_item.setVisibility(View.GONE);
                                recycle_init();
                                progressDialog.dismiss();
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                                recycle_init();
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

        ArrayList<Assign_Subject_List_Item> assign_subject_list_items;
        public RecycleAdapter(ArrayList<Assign_Subject_List_Item> assign_subject_list_items){
            this.assign_subject_list_items=assign_subject_list_items;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            LinearLayout linearLayout;
            RecyclerView recyclerView;
            FloatingActionButton add;
            TextView teacher_name,subject_name,start_time,end_time,day;
            LinearLayout main_layout;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                subject_name=mView.findViewById(R.id.subject_name);
                teacher_name=mView.findViewById(R.id.teacher_name);
                start_time=mView.findViewById(R.id.start);
                end_time=mView.findViewById(R.id.end);
                day=mView.findViewById(R.id.day);







            }





        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_class_item2,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Assign_Subject_List_Item assign_subject_list_item=assign_subject_list_items.get(position);
            holder.subject_name.setText(assign_subject_list_item.subject_name);
            holder.teacher_name.setText(assign_subject_list_item.teacher_name);
            holder.start_time.setText(assign_subject_list_item.start_time);
            holder.end_time.setText(assign_subject_list_item.end_time);
            holder.day.setText(assign_subject_list_item.day);


            holder.subject_name.setSelected(true);
            holder.teacher_name.setSelected(true);
            holder.start_time.setSelected(true);
            holder.end_time.setSelected(true);
            holder.day.setSelected(true);


        }

        @Override
        public int getItemCount() {
            return assign_subject_list_items.size();
        }



    }


}
