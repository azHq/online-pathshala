package com.example.onlinepathshala.Student;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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


public class Home_Task extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Home_Task_Info> home_task_infos;
    TextView no_item;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home__task, container, false);


        progressDialog=new ProgressDialog(getContext());
        recyclerView=view.findViewById(R.id.recycle);
        no_item=view.findViewById(R.id.no_item);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        home_task_infos=new ArrayList<>();
        getAllMemberData();
    }


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Home_Task");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getId());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_home_task,postparams,
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
                                        String subject_name=member.getString("subject_name");
                                        String assigned_date = member.getString("assigned_date");
                                        String submission_date = member.getString("submission_date");
                                        String title = member.getString("title");
                                        String body = member.getString("details");
                                        String task_number = member.getString("task_number");
                                        home_task_infos.add(new Home_Task_Info(id,subject_name,assigned_date,submission_date,title,body,null,task_number));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                no_item.setVisibility(View.GONE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(home_task_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{

                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(home_task_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"No Home Task Still Assigned",Toast.LENGTH_LONG).show();
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.layout2.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            return true;
        }
        else if(id==R.id.logout){


        }

        return super.onOptionsItemSelected(item);
    }



    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Home_Task_Info> home_task_infos;
        public RecycleAdapter(ArrayList<Home_Task_Info> classinfos){
            this.home_task_infos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView subject_name,assigned_date,submission_date,title,body;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                subject_name=mView.findViewById(R.id.subject_name);
                assigned_date=mView.findViewById(R.id.assign_date);
                submission_date=mView.findViewById(R.id.submission_date);
                title=mView.findViewById(R.id.title);
                body=mView.findViewById(R.id.body);
                body.setMovementMethod(new ScrollingMovementMethod());
                subject_name.setSelected(true);
                title.setSelected(true);
            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_task_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Home_Task_Info memberInfo=home_task_infos.get(position);
            holder.subject_name.setText(memberInfo.subject_name);
            holder.assigned_date.setText(memberInfo.assigned_date);
            holder.submission_date.setText(memberInfo.submission_date);
            holder.title.setText(memberInfo.tile);
            holder.body.setText(memberInfo.body);



        }

        @Override
        public int getItemCount() {
            return home_task_infos.size();
        }



    }



}
