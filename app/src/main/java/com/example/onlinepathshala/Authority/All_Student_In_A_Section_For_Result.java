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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class All_Student_In_A_Section_For_Result extends AppCompatActivity {


    ArrayList<Student> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    public String section_id="",class_id="";
    TextView no_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__student__in__a__section);
        section_id=getIntent().getStringExtra("section_id");
        class_id=getIntent().getStringExtra("class_id");
        recyclerView=findViewById(R.id.recycle);
        progressDialog=new ProgressDialog(All_Student_In_A_Section_For_Result.this);
        progressDialog.setMessage("Please Wait.....");
        no_item=findViewById(R.id.no_item);
        getAllMemberData();


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
                                        String image_path=member.getString("image_path");
                                        String roll=member.getString("class_roll");
                                        String device_id=member.getString("device_id");
                                        memberInfos.add(new Student(id,name,"",image_path,roll,device_id));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                no_item.setVisibility(View.GONE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
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
                    Intent tnt=new Intent(getApplicationContext(), Result_For_Authority.class);
                    tnt.putExtra("student_id",memberInfos.get(position).id);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",section_id);
                    tnt.putExtra("student_name",memberInfo.name);
                    startActivity(tnt);
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
