package com.example.onlinepathshala.Teacher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.onlinepathshala.Authority.Messenger;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Authority.Student_Profile_For_View;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class Contact_With_Student extends Fragment {

    ArrayList<Student> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_contact__with__student, container, false);
        frameLayout=view.findViewById(R.id.frame_layout);
        recyclerView=view.findViewById(R.id.recycle);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        memberInfos=new ArrayList<>();
        getAllMemberData();
    }



    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_student_for_contact,postparams,
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
                                        String name = member.getString("student_name");
                                        String class_name=member.getString("class_name");
                                        String image_path=member.getString("image_path");
                                        String roll=member.getString("class_roll");
                                        String device_id=member.getString("device_id");
                                        memberInfos.add(new Student(id,name,class_name,image_path,roll,device_id));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{
                                RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                Toast.makeText(getContext(),"No Student Still Added",Toast.LENGTH_LONG).show();
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

        ArrayList<Student> memberInfos;
        public RecycleAdapter(ArrayList<Student> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            Button profile,sendMessage;
            LinearLayout linearLayout;
            CircleImageView imageView;
            TextView name,class_name,id,roll;
            CircleImageView profile_image;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                imageView=mView.findViewById(R.id.profile_image);
                sendMessage=mView.findViewById(R.id.message);
                profile=mView.findViewById(R.id.delete);
                linearLayout=mView.findViewById(R.id.view_profile);

                id=mView.findViewById(R.id.id);
                roll=mView.findViewById(R.id.roll);
                name=mView.findViewById(R.id.name);
                profile_image=mView.findViewById(R.id.profile_image);

            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_rec_list_item6,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final  Student memberInfo=memberInfos.get(position);
            holder.name.setText(memberInfo.name);
            holder.name.setSelected(true);
            holder.profile.setText("Profile");
            holder.id.setText(memberInfo.id);
            holder.roll.setText(memberInfo.roll);
            if(memberInfo.roll.equalsIgnoreCase("null")) holder.roll.setText("Yet Not Assigned");
            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent tnt=new Intent(getContext(), Student_Profile_For_View.class);
                    tnt.putExtra("id",memberInfo.id);
                    tnt.putExtra("class_id","");
                    tnt.putExtra("section_id","");
                    startActivity(tnt);


                }
            });
            holder.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getContext(), Messenger.class);
                    tnt.putExtra("sender_id",SharedPrefManager.getInstance(getContext()).getUser().getId());
                    tnt.putExtra("receiver_id",memberInfo.id);
                    tnt.putExtra("sender_type",SharedPrefManager.getInstance(getContext()).getUser().getUser_type());
                    tnt.putExtra("receiver_type","Student");
                    tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getContext()).getUser().getDevice_id());
                    tnt.putExtra("receiver_device_id",memberInfo.device_id);
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
