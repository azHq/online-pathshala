package com.example.onlinepathshala;

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
import com.example.onlinepathshala.Student.Home_Task_Info;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Notice extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Notice_Info> notice_infos=new ArrayList<>();
    ProgressDialog progressDialog;
    RecycleAdapter recycleAdapter;
    public Notice() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notice, container, false);
        progressDialog=new ProgressDialog(getContext());
        getAllMemberData();
        recyclerView=view.findViewById(R.id.recycle);
        recycleAdapter=new RecycleAdapter(notice_infos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recycleAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Notice");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getContext()).get_student_info().class_id);
        postparams.put("Student");

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_notice_url,postparams,
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
                                        String title=member.getString("title");
                                        String details = member.getString("details");
                                        String date = member.getString("create_date");
                                        String file_path = member.getString("file_path");
                                        String file_type = member.getString("file_type");
                                        notice_infos.add(new Notice_Info(id,title,details,date,file_type,file_path));

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
                                Toast.makeText(getContext(),"Notice Board Empty",Toast.LENGTH_LONG).show();
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

        ArrayList<Notice_Info> notice_infos;
        public RecycleAdapter(ArrayList<Notice_Info> classinfos){
            this.notice_infos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView assigned_date,title,body,file_path;
            ImageView imageView;
            Button btn_download;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                title=mView.findViewById(R.id.title);
                assigned_date=mView.findViewById(R.id.date);
                body=mView.findViewById(R.id.body);
                body.setMovementMethod(new ScrollingMovementMethod());
                imageView=mView.findViewById(R.id.image);
                btn_download=mView.findViewById(R.id.download);
                title.setSelected(true);
            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Notice_Info memberInfo=notice_infos.get(position);
            holder.assigned_date.setText(memberInfo.date);
            if(memberInfo.file_path!=null&&memberInfo.file_type.equalsIgnoreCase("image")) Picasso.get().load(memberInfo.file_path).placeholder(R.drawable.profile2).into(holder.imageView);
            if(!memberInfo.file_type.equalsIgnoreCase("pdf")) holder.btn_download.setVisibility(View.GONE);
            holder.title.setText(memberInfo.title);
            holder.body.setText(memberInfo.details);
            holder.btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DownloadTask(getContext(), memberInfo.file_path);
                }
            });



        }

        @Override
        public int getItemCount() {
            return notice_infos.size();
        }



    }


}
