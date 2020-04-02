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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Attendence_History_Canlander_View_For_Authority;
import com.example.onlinepathshala.Authority.Messenger;
import com.example.onlinepathshala.Authority.Result_For_Authority;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Authority.Student2;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Final_Result_Data_Class;
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

public class All_Student2 extends AppCompatActivity {
    ArrayList<Student> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    RecyclerView recyclerView;
    RecycleAdapter recycleAdapter;
    ProgressDialog progressDialog;
    AlertDialog alertDialog,alertDialog2;
    TextView no_item;
    FloatingActionButton dragView;
    public String section_id="",class_id="",class_name="",section_name="";
    float posX=0;
    float posY=0;
    String class_teacher_id="";
    TextView tv_class,tv_section;
    Button search_btn;
    EditText et_search;
    String search_string,user_type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__student2);
        no_item=findViewById(R.id.no_item);
        dragView = findViewById(R.id.fab);
        user_type=SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type();
        section_id=getIntent().getStringExtra("section_id");
        class_id=getIntent().getStringExtra("class_id");
        section_name=getIntent().getStringExtra("section_name");
        class_name=getIntent().getStringExtra("class_name");
        class_teacher_id=getIntent().getStringExtra("class_teacher_id");
        recyclerView=findViewById(R.id.recycle);
        progressDialog=new ProgressDialog(All_Student2.this);
        progressDialog.setMessage("Please Wait.....");
        tv_class=findViewById(R.id.class_name);
        tv_section=findViewById(R.id.section_name);
        progressDialog.setMessage("Please wait...");
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
        tv_class.setText(class_name);
        tv_section.setText(section_name);
        getAllMemberData();
    }
    public void set_search_match_item(String search_string,int condition){

        search_string=search_string.toLowerCase();
        ArrayList<Student> search_list=new ArrayList<>();
        search_list.clear();
        for(Student student2:memberInfos){

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


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);

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
                                        String roll=member.getString("class_roll");
                                        String phone=member.getString("phone_number");
                                        String image_path=member.getString("image_path");
                                        String device_id=member.getString("device_id");
                                        memberInfos.add(new Student(id,name,"",image_path,roll,device_id));

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

        ArrayList<Student> memberInfos;
        public RecycleAdapter(ArrayList<Student> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;

            LinearLayout linearLayout;
            TextView name,id,roll;
            CircleImageView profile_image;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                linearLayout=mView.findViewById(R.id.view_profile);

                name=mView.findViewById(R.id.name);
                id=mView.findViewById(R.id.id);
                roll=mView.findViewById(R.id.roll);
                profile_image=mView.findViewById(R.id.profile);

            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_rec_list_item5,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final  Student memberInfo=memberInfos.get(position);
            holder.name.setText(memberInfo.name);
            holder.name.setSelected(true);
            holder.id.setText(memberInfo.id);
            holder.id.setSelected(true);
            if(!memberInfo.roll.equalsIgnoreCase("null")) holder.roll.setText(memberInfo.roll);
            else  holder.roll.setText("Yet Not Assigned");
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(user_type.equalsIgnoreCase("Teacher")){
                        Intent tnt=new Intent(getApplicationContext(), Attendence_History_Canlander_View_For_Teacher.class);
                        tnt.putExtra("id",memberInfo.id);
                        tnt.putExtra("name",memberInfo.name);
                        tnt.putExtra("class_teacher_id",class_teacher_id);
                        startActivity(tnt);
                    }
                    else{

                        Intent tnt=new Intent(getApplicationContext(), Attendence_History_Canlander_View_For_Authority.class);
                        tnt.putExtra("id",memberInfo.id);
                        tnt.putExtra("name",memberInfo.name);
                        tnt.putExtra("class_teacher_id",class_teacher_id);
                        startActivity(tnt);
                    }



                }
            });

            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.profile10).into(holder.profile_image);
            }



        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }



    }

}
