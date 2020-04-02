package com.example.onlinepathshala.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Messenger;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class All_Authority extends Fragment {


    ActionBar actionBar;
    FloatingActionButton dragView;
    ArrayList<School> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    float posX=0;
    float posY=0;
    String subject_id="0";
    Button delete;
    CheckBox select_all;
    RecycleAdapter recycleAdapter;
    boolean show_checkbox=false,checked_all=false;
    ArrayList<String> student_ids=new ArrayList<>();
    EditText et_search;
    TextView no_item;
    public All_Authority(ActionBar actionBar) {
        this.actionBar=actionBar;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_all__authority, container, false);
        dragView = view.findViewById(R.id.fab);
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
        postparams.put("Authority");
        postparams.put("online_pathshala");

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_school,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string= "";
                        try {
                            string = response.getString(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        memberInfos.clear();
                        if(!string.contains("no item")){

                            for(int i=0;i<response.length();i++){

                                JSONObject member = null;
                                try {
                                    member = response.getJSONObject(i);
                                    String authority_id = member.getString("id");
                                    String school_name = member.getString("school_name");
                                    String school_id = member.getString("school_id");
                                    String authority_name = member.getString("authority_name");
                                    String image_path=member.getString("image_path");
                                    String status=member.getString("account_status");

                                    String email = member.getString("email");
                                    String phone=member.getString("phone_number");
                                    String date=member.getString("create_at");
                                    String device_id=member.getString("device_id");
                                    memberInfos.add(new School(school_id,school_name,authority_id,authority_name,email,phone,date,status,image_path,device_id));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            no_item.setVisibility(GONE);
                            recycleAdapter=new RecycleAdapter(memberInfos);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(recycleAdapter);
                            progressDialog.dismiss();
                        }
                        else{

                            no_item.setVisibility(VISIBLE);
                            recycleAdapter=new RecycleAdapter(memberInfos);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(recycleAdapter);
                            progressDialog.dismiss();
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

        ArrayList<School> memberInfos;
        public RecycleAdapter(ArrayList<School> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder  {

            View mView;
            Button profile,sendMessage;
            LinearLayout linearLayout;
            CircleImageView imageView;
            TextView name,email,school_name,rating;
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
                school_name=mView.findViewById(R.id.school_name);
                profile_image=mView.findViewById(R.id.profile_image);
                rating=mView.findViewById(R.id.rating);
            }





        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.authority_list_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final School memberInfo=memberInfos.get(position);
            holder.name.setText(memberInfo.authority_name);
            holder.school_name.setText(memberInfo.school_name);
            holder.email.setText(memberInfo.email);
            holder.name.setSelected(true);
            holder.school_name.setSelected(true);
            if(!memberInfo.image.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image).placeholder(R.drawable.profile10).into(holder.profile_image);
            }
            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    School memberInfo=memberInfos.get(position);
                    Intent tnt=new Intent(getContext(), Profile_View_Only.class);
                    tnt.putExtra("id",memberInfo.authority_id);
                    tnt.putExtra("school_id",memberInfo.school_id);
                    tnt.putExtra("table","Teacher");
                    tnt.putExtra("name",memberInfo.authority_name);
                    tnt.putExtra("phone_number",memberInfo.phone_number);
                    tnt.putExtra("email",memberInfo.email);
                    tnt.putExtra("device_id",memberInfo.device_id);
                    tnt.putExtra("image_path",memberInfo.image);
                    startActivity(tnt);
                }
            });
            holder.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getContext(), Messenger.class);
                    tnt.putExtra("sender_id",SharedPrefManager.getInstance(getContext()).getUser().getId());
                    tnt.putExtra("receiver_id",memberInfo.authority_id);
                    tnt.putExtra("sender_type",SharedPrefManager.getInstance(getContext()).getUser().getUser_type());
                    tnt.putExtra("receiver_type","Teacher");
                    tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getContext()).getUser().getDevice_id());
                    tnt.putExtra("receiver_device_id",memberInfo.device_id);
                    tnt.putExtra("image_path",memberInfo.image);
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
