package com.example.onlinepathshala.Authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Constant_URL;
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

public class All_UnAssign_Student extends AppCompatActivity {

    ArrayList<Student2> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog,alertDialog2;
    TextView no_item;
    Button add;
    public String section_id="",class_id="";
    float posX=0;
    float posY=0;
    Button delete;
    CheckBox select_all;
    RecycleAdapter recycleAdapter;
    boolean show_checkbox=false,checked_all=false;
    ArrayList<String> student_ids=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__un_assign__student);
        no_item=findViewById(R.id.no_item);
        add = findViewById(R.id.add);
        section_id=getIntent().getStringExtra("section_id");
        class_id=getIntent().getStringExtra("class_id");
        recyclerView=findViewById(R.id.recycle);
        delete=findViewById(R.id.delete);
        select_all=findViewById(R.id.select_all);
        select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    select_all_student();
                    checked_all=true;
                }
                else {

                    student_ids.clear();
                    checked_all=false;
                }
                recyclerView.setAdapter(recycleAdapter);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(student_ids.size()>0){

                    add_multiple_student();;
                }
                else {

                    Toast.makeText(getApplicationContext(),"Please Choose Student",Toast.LENGTH_LONG).show();
                }

            }
        });

        progressDialog=new ProgressDialog(All_UnAssign_Student.this);
        progressDialog.setMessage("Please Wait.....");
        getAllMemberData();
    }

    public void select_all_student(){
        student_ids.clear();
        for(Student2 student:memberInfos){

            student_ids.add(student.id);
        }
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put("0");
        postparams.put("0");

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_student_info_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        memberInfos.clear();
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
                                        String email=member.getString("email");
                                        String phone=member.getString("phone_number");
                                        String image_path=member.getString("image_path");
                                        String device_id=member.getString("device_id");
                                        memberInfos.add(new Student2(id,name,email,phone,image_path,device_id));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                no_item.setVisibility(View.GONE);
                                recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{

                                no_item.setVisibility(View.VISIBLE);
                                recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                Toast.makeText(getApplicationContext(),"No Student Still Added",Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);


    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Student2> memberInfos;
        public RecycleAdapter(ArrayList<Student2> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

            View mView;
            Button view_profile,sendMessage;
            LinearLayout linearLayout;
            TextView id,name,email;
            CircleImageView profile_image;
            CheckBox select;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                mView.setOnClickListener(this);
                sendMessage=mView.findViewById(R.id.message);
                view_profile=mView.findViewById(R.id.view_profile);
                select=mView.findViewById(R.id.select);
                id=mView.findViewById(R.id.id);
                name=mView.findViewById(R.id.name);
                email=mView.findViewById(R.id.email);

                profile_image=mView.findViewById(R.id.profile);

            }


            @Override
            public void onClick(View view) {

                if(select.isChecked()){
                    student_ids.remove(memberInfos.get(getLayoutPosition()).id);
                    select.setChecked(false);
                }
                else{
                    student_ids.add(memberInfos.get(getLayoutPosition()).id);
                    select.setChecked(true);
                }
            }
        }


        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_rec_list_item4,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final  Student2 memberInfo=memberInfos.get(position);
            holder.id.setText(memberInfo.id);
            holder.name.setText(memberInfo.name);
            holder.name.setSelected(true);
            holder.email.setText(memberInfo.email);
            holder.email.setSelected(true);
            holder.select.setVisibility(View.VISIBLE);



            if(checked_all){

                holder.select.setVisibility(View.VISIBLE);
                holder.select.setChecked(true);
            }
            else {

                holder.select.setChecked(false);
            }






            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.profile10).into(holder.profile_image);
            }

            holder.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getApplicationContext(), Messenger.class);
                    tnt.putExtra("sender_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                    tnt.putExtra("receiver_id",memberInfo.id);
                    tnt.putExtra("sender_type",SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type());
                    tnt.putExtra("receiver_type","Student");
                    tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getDevice_id());
                    tnt.putExtra("receiver_device_id",memberInfo.device_id);
                    tnt.putExtra("image_path",memberInfo.image_path);
                    startActivity(tnt);
                }
            });

            holder.view_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent tnt=new Intent(getApplicationContext(),Student_Profile_For_View.class);
                    tnt.putExtra("id",memberInfo.id);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",section_id);
                    startActivity(tnt);
                }
            });



        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }



    }

    public void add_multiple_student(){

        JSONArray jsonArray=new JSONArray(student_ids);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_multiple_student,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            checked_all=false;
                            Toast.makeText(getApplicationContext(),"Student Added Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {

                            System.out.println(response.toString());
                            Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Connect Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",jsonArray.toString());
                params.put("table", "Student");
                params.put("class_id",class_id);
                params.put("section_id",section_id);
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
