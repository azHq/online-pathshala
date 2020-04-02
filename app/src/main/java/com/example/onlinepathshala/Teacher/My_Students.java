package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Messenger;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Authority.Student2;
import com.example.onlinepathshala.Authority.Student_Profile_For_View;
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

public class My_Students extends AppCompatActivity {

    ArrayList<Student2> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog,alertDialog2;
    TextView no_item;
    FloatingActionButton dragView;
    public String section_id="",class_id="";
    float posX=0;
    float posY=0;
    Button delete;
    CheckBox select_all;
    RecycleAdapter recycleAdapter;
    boolean show_checkbox=false,checked_all=false;
    ArrayList<String> student_ids=new ArrayList<>();
    EditText et_search;
    Button search_btn;
    String search_string;
    ArrayList<Student2> search_list=new ArrayList<>();
    TextView tv_class_name,tv_section_name,tv_medium_name,total_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__students);
        no_item=findViewById(R.id.no_item);
        dragView = findViewById(R.id.fab);
        et_search=findViewById(R.id.search);
        recyclerView=findViewById(R.id.recycle);
        tv_class_name=findViewById(R.id.class_name);
        tv_section_name=findViewById(R.id.section_name);
        tv_medium_name=findViewById(R.id.medium);
        total_student=findViewById(R.id.total_student);


        et_search=findViewById(R.id.search);
        search_btn=findViewById(R.id.search_btn);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                search_string=charSequence.toString();
                set_search_match_item(charSequence.toString(),1);




            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                set_search_match_item(search_string,2);

            }
        });
        progressDialog=new ProgressDialog(My_Students.this);
        progressDialog.setMessage("Please Wait.....");
        get_section_data();
        getAllMemberData();
    }

    public void set_search_match_item(String search_string,int condition){

        search_string=search_string.toLowerCase();
        ArrayList<Student2> search_list=new ArrayList<>();
        search_list.clear();
        for(Student2 student2:memberInfos){

            String id=student2.id.toLowerCase();
            String name=student2.name.toLowerCase();

            if(condition==1){
                if(id.contains(search_string)||name.contains(search_string)){

                    search_list.add(student2);
                }
            }
            else if(condition==2){
                if(id.equalsIgnoreCase(search_string)||name.equalsIgnoreCase(search_string)){

                    search_list.add(student2);
                }
            }


        }
        recycleAdapter=new RecycleAdapter(search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recycleAdapter);


    }


    public void get_section_data(){

        JSONArray postparams = new JSONArray();
        postparams.put("Section");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_section_info2,postparams,
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
                                        String section_name = member.getString("section_name");
                                        String class_name=member.getString("class_name");
                                        String medium=member.getString("medium");

                                        tv_section_name.setText("Section : "+section_name);
                                        tv_class_name.setText("Class : "+class_name);
                                        tv_medium_name.setText("Medium : "+medium);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                            else{


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



    @Override
    protected void onResume() {
        super.onResume();
        getAllMemberData();
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_my_student,postparams,
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
                                total_student.setText("Total Students : "+memberInfos.size() );
                                no_item.setVisibility(View.GONE);
                                recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{

                                no_item.setVisibility(View.VISIBLE);
                                total_student.setText("Total Students : "+memberInfos.size());
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
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            Button view_profile,sendMessage;
            LinearLayout linearLayout;
            TextView id,name,email;
            CircleImageView profile_image;
            CheckBox select;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                sendMessage=mView.findViewById(R.id.message);
                view_profile=mView.findViewById(R.id.view_profile);
                select=mView.findViewById(R.id.select);
                id=mView.findViewById(R.id.id);
                name=mView.findViewById(R.id.name);
                email=mView.findViewById(R.id.email);

                profile_image=mView.findViewById(R.id.profile);

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

            if(show_checkbox){
                holder.select.setVisibility(View.VISIBLE);

                if(checked_all){

                    holder.select.setVisibility(View.VISIBLE);
                    holder.select.setChecked(true);
                }
                else {

                    holder.select.setChecked(false);
                }

            }
            else{

                holder.select.setVisibility(View.GONE);
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


                    Intent tnt=new Intent(getApplicationContext(), Student_Profile_For_View.class);
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
}
