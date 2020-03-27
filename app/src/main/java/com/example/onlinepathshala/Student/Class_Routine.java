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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.onlinepathshala.DownloadTask;
import com.example.onlinepathshala.Notice_Info;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Class_Routine extends Fragment {


    RecyclerView recyclerView;
    ArrayList<Routine> notice_infos=new ArrayList<>();
    ProgressDialog progressDialog;
    RecycleAdapter recycleAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_class__routine, container, false);
        progressDialog=new ProgressDialog(getContext());
        getAllMemberData();
        recyclerView=view.findViewById(R.id.recycle);
        recycleAdapter=new RecycleAdapter(notice_infos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recycleAdapter);
        return view;
    }


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Routine");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().section_id);
        postparams.put("Class Routine");

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_routine_url,postparams,
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
                                        String routine_type=member.getString("routine_type");
                                        String exam_type=member.getString("exam_type");
                                        String date = member.getString("create_date");
                                        String file_path = member.getString("file_path");
                                        String file_type = member.getString("file_type");
                                        notice_infos.add(new Routine(id,routine_type,exam_type,date,file_path,file_type));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                recycleAdapter.notifyDataSetChanged();


                                progressDialog.dismiss();
                            }
                            else{
                                recycleAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"No Routine Uploaded",Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getContext(),"Connection Error",Toast.LENGTH_LONG).show();
                        System.out.println(error.toString());
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

        ArrayList<Routine> notice_infos;
        public RecycleAdapter(ArrayList<Routine> classinfos){
            this.notice_infos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView assigned_date,title,type,file_path;
            ImageView imageView;
            Button btn_download;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                title=mView.findViewById(R.id.title);
                assigned_date=mView.findViewById(R.id.date);
                type=mView.findViewById(R.id.type);
                type.setMovementMethod(new ScrollingMovementMethod());
                imageView=mView.findViewById(R.id.image);
                btn_download=mView.findViewById(R.id.download);
                title.setSelected(true);
            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_class_routine_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Routine memberInfo=notice_infos.get(position);
            holder.assigned_date.setText("Uploaded Date: "+memberInfo.date);
            if(memberInfo.path!=null&&memberInfo.file_type.equalsIgnoreCase("image")) {
                Picasso.get().load(memberInfo.path).placeholder(R.drawable.routine).into(holder.imageView);
                holder.btn_download.setText("Download Routine");
            }
            else holder.imageView.setVisibility(View.GONE);
            holder.title.setText("Class Routine");
            holder.type.setVisibility(View.GONE);
            holder.btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new DownloadTask(getContext(), memberInfo.path);

                }
            });



        }

        @Override
        public int getItemCount() {
            return notice_infos.size();
        }



    }


}
