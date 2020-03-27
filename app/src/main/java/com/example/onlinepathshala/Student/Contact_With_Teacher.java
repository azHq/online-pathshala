package com.example.onlinepathshala.Student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.onlinepathshala.Authority.Messenger;
import com.example.onlinepathshala.Authority.Teacher;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Profile_View_Only;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class Contact_With_Teacher extends Fragment {


    public Contact_With_Teacher() {

    }


    ArrayList<Teacher> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,final  ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_teacher__contact, container, false);
        frameLayout=view.findViewById(R.id.frame_layout);
        recyclerView=view.findViewById(R.id.recycle);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");



        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Teacher");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_teacher_info_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String school_name=SharedPrefManager.getInstance(getContext()).getUser().getSchool_name();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String user_name = member.getString("teacher_name");
                                        String image_path=member.getString("image_path");
                                        String class_name=member.getString("class_name");
                                        String subject_name=member.getString("subject_name");
                                        String phone_number=member.getString("phone_number");
                                        String email=member.getString("email");
                                        String device_id=member.getString("device_id");
                                        memberInfos.add(new Teacher(id,user_name,class_name,subject_name,image_path,phone_number,email,device_id,""));

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
    @Override
    public void onResume() {
        super.onResume();
        memberInfos=new ArrayList<>();
        getAllMemberData();
    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Teacher> memberInfos;
        public RecycleAdapter(ArrayList<Teacher> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            Button profile,sendMessage;
            LinearLayout linearLayout;
            CircleImageView imageView;
            TextView name,email,subject_name;
            CircleImageView profile_image;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                imageView=mView.findViewById(R.id.profile_image);
                sendMessage=mView.findViewById(R.id.message);
                profile=mView.findViewById(R.id.profile);
                linearLayout=mView.findViewById(R.id.view_profile);

                name=mView.findViewById(R.id.name);
                email=mView.findViewById(R.id.email);
                subject_name=mView.findViewById(R.id.subject_name);
                profile_image=mView.findViewById(R.id.profile_image);

            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_rec_list_item3,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final Teacher memberInfo=memberInfos.get(position);
            holder.name.setText(memberInfo.name);
            holder.subject_name.setText(memberInfo.subject_name);
            holder.email.setText(memberInfo.email);
            holder.email.setSelected(true);
            holder.name.setSelected(true);
            holder.subject_name.setSelected(true);
            holder.profile.setText("Profile");
            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getContext(),Profile_View_Only.class);
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
            holder.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getContext(), Messenger.class);
                    tnt.putExtra("sender_id",SharedPrefManager.getInstance(getContext()).getUser().getId());
                    tnt.putExtra("receiver_id",memberInfo.id);
                    tnt.putExtra("sender_type",SharedPrefManager.getInstance(getContext()).getUser().getUser_type());
                    tnt.putExtra("receiver_type","Teacher");
                    tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getContext()).getUser().getDevice_id());
                    tnt.putExtra("receiver_device_id",memberInfo.device_id);
                    tnt.putExtra("image_path",memberInfo.image_path);
                    startActivity(tnt);

                }
            });
            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.user).into(holder.profile_image);
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getContext(),Profile_View_Only.class);
                    tnt.putExtra("id",memberInfo.id);
                    tnt.putExtra("school_id",SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                    tnt.putExtra("table","Teacher");
                    tnt.putExtra("name",memberInfo.name);
                    tnt.putExtra("phone_number",memberInfo.phone_number);
                    tnt.putExtra("email",memberInfo.email);
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
