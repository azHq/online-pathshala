package com.example.onlinepathshala.Authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Admin.Member;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Teacher.Attendence;
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

public class All_Sections extends AppCompatActivity implements View.OnTouchListener {

    FloatingActionButton dragView;
    ArrayList<Section> sections=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    TextView no_item;
    String medium,shift_name;
    AlertDialog alertDialog2;
    String class_id,activity_type,class_name;
    AlertDialog alertDialog3;
    String teacher_name,teacher_id;
    ArrayList<Member> members=new ArrayList<>();
    ArrayList<String> section_names=new ArrayList<>();
    float posX=0;
    float posY=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__sections);

        class_id=getIntent().getStringExtra("class_id");
        class_name=getIntent().getStringExtra("class_name");
        activity_type=getIntent().getStringExtra("type");
        dragView = findViewById(R.id.fab);
        frameLayout=findViewById(R.id.frame_layout);
        recyclerView=findViewById(R.id.recycle);
        no_item=findViewById(R.id.no_item);
        dragView.setOnTouchListener(this);
        progressDialog=new ProgressDialog(All_Sections.this);
        progressDialog.setMessage("Please wait...");
        getAllMemberData();
        get_all_teacher_data("Teacher",Constant_URL.get_all_data_url);
    }
    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Section");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_section_info_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        sections.clear();
                        section_names.clear();
                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String shift = member.getString("shift_name");
                                        String section_name=member.getString("section_name");
                                        String teacher_name = member.getString("class_teacher_name");
                                        String teacher_id = member.getString("class_teacher_id");
                                        String total_student=member.getString("total_student");
                                        sections.add(new Section(id,shift,section_name,teacher_name,teacher_id,total_student));
                                        section_names.add(section_name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                RecycleAdapter recycleAdapter=new RecycleAdapter(sections);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                no_item.setVisibility(View.GONE);
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(sections);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(),"no item",Toast.LENGTH_LONG).show();
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


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter> {

        ArrayList<Section> classinfos;
        public RecycleAdapter(ArrayList<Section> classinfos){
            this.classinfos=classinfos;
        }



        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            View mView;
            LinearLayout linearLayout;
            TextView section_name,teacher_name,shift,total_student;

            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                section_name=mView.findViewById(R.id.section_name);
                teacher_name=mView.findViewById(R.id.teacher);
                shift=mView.findViewById(R.id.shift);
                linearLayout=mView.findViewById(R.id.view_profile);
                total_student=mView.findViewById(R.id.total_student);



            }
            @Override
            public void onClick(View view) {

                int position =getLayoutPosition();
                Section memberInfo=classinfos.get(position);
                if(activity_type.equalsIgnoreCase("student")){

                    Intent tnt=new Intent(getApplicationContext(),All_Student_In_A_Section.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",memberInfo.id);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("subject")){

                    Intent tnt=new Intent(getApplicationContext(),All_Subject_Activity.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("assign_subject")){

                    Intent tnt=new Intent(getApplicationContext(), Assign_Subject_List_Activity2.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("class_name",class_name);
                    tnt.putExtra("section_id",memberInfo.id);
                    tnt.putExtra("section_name",memberInfo.section_name);
                    tnt.putExtra("type",activity_type);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("attendence")){

                    Intent tnt=new Intent(getApplicationContext(), Attendence.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("class_name",class_name);
                    tnt.putExtra("section_id",memberInfo.id);
                    tnt.putExtra("section_name",memberInfo.section_name);
                    tnt.putExtra("type",activity_type);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("exam")){

                    Intent tnt=new Intent(getApplicationContext(), All_Exam.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("class_name",class_name);
                    tnt.putExtra("section_id",memberInfo.id);
                    tnt.putExtra("section_name",memberInfo.section_name);
                    tnt.putExtra("type",activity_type);
                    startActivity(tnt);
                }

            }

            @Override
            public boolean onLongClick(View view) {


                int position =getLayoutPosition();
                Section memberInfo=classinfos.get(position);
                showOptionDialog(memberInfo.id,memberInfo.section_name,memberInfo.shift,memberInfo.teacher_name);
                return true;
            }

        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.section_rec_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Section memberInfo=classinfos.get(position);
            holder.total_student.setText(memberInfo.total_student);
            holder.teacher_name.setSelected(true);
            if(memberInfo.teacher_name.equalsIgnoreCase("NULL")||memberInfo.teacher_name.length()<1){

                holder.teacher_name.setText("Not Assigned");
            }
            else{
                holder.teacher_name.setText(memberInfo.teacher_name);
            }
            holder.section_name.setText(memberInfo.section_name);
            holder.shift.setText(memberInfo.shift);


        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public void showOptionDialog(final String id,final String section_name,final String shift,final String teacher_id){

        AlertDialog.Builder builder=new AlertDialog.Builder(All_Sections.this);
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        TextView remove_teacher=view.findViewById(R.id.remove_teacher);
        alertDialog2=builder.show();

        if(teacher_id.equalsIgnoreCase("NULL")) remove_teacher.setVisibility(View.GONE);
        remove_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
                permission_message(id);
            }
        });
        assign_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
                assignTeacher(id);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showEditDialog(section_name,shift,id);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showDeleteDialogBox(id);
            }
        });


    }
    public void permission_message(String section_id){

        AlertDialog.Builder builder=new AlertDialog.Builder(All_Sections.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.delete_dialog_box,null);
        builder.setView(view);
        TextView tv_title=view.findViewById(R.id.title);
        TextView tv_body=view.findViewById(R.id.message);
        Button yes=view.findViewById(R.id.yes);
        Button no=view.findViewById(R.id.no);
        tv_title.setText("Teacher Remove");
        tv_body.setText("Are Sure To Remove Class Teacher ?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                remove_teacher( section_id);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog=builder.show();



    }

    public void remove_teacher(String section_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.common_editor_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Teacher Removed Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getApplicationContext(),"Fail To Remove", Toast.LENGTH_SHORT).show();
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
                params.put("id",section_id);
                params.put("value","NULL");
                params.put("column","class_teacher_id");
                params.put("table", "Section");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Sections.this);
        View view=getLayoutInflater().inflate(R.layout.delete_dialog_box,null);
        Button yes=view.findViewById(R.id.yes);
        Button no=view.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                progressDialog.show();
                deleteItem(id);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
        alert.setView(view);
        alertDialog= alert.show();


    }

    public void deleteItem(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.delete_section_or_class_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Section Deleted Successfully", Toast.LENGTH_SHORT).show();
                           getAllMemberData();

                        } else {
                            Toast.makeText(getApplicationContext(),"Fail To Delete", Toast.LENGTH_SHORT).show();
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
                params.put("id",id);
                params.put("table", "Section");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {




        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels-view.getHeight();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                posX=view.getX();
                posY=view.getY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:

                if(event.getRawY()<(height-view.getHeight()/1.2)&&event.getRawY()>view.getHeight()*2) view.setY(event.getRawY()+ dY);
                if(event.getRawX()<(width-view.getWidth()/1.2)&&event.getRawX()>view.getWidth()/1.3) view.setX(event.getRawX() + dX);
                if(Math.abs(view.getX()-posX)>=50||Math.abs(view.getY()-posY)>=50){
                    lastAction = MotionEvent.ACTION_MOVE;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    showDialog();
                break;

            default:
                return false;
        }
        return true;
    }


    public void showEditDialog(final String section_name, final String shift,final String id) {

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Sections.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        if( inflater!=null&&alert!=null){


            View view =inflater.inflate(R.layout.edit_section,null);
            alert.setView(view);
            final AlertDialog alertDialog=alert.show();

            ArrayList<String> list=new ArrayList<>();
            list.add("Choose Shift");
            list.add("Morning");
            list.add("Noon");
            final EditText et_section_name=view.findViewById(R.id.section_name);
            et_section_name.setText(section_name);
            final Spinner sp_shift_name=view.findViewById(R.id.shift_name);
            sp_shift_name.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_spinner_dropdown,list));
            if(shift.equalsIgnoreCase("Morning")){

                sp_shift_name.setSelection(1);
            }
            else{

                sp_shift_name.setSelection(2);
            }

            sp_shift_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    shift_name=list.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            Button confirm=view.findViewById(R.id.addmember);
            Button cancel=view.findViewById(R.id.cancel);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String section=et_section_name.getText().toString().trim();
                    if(!section_names.contains(section)){

                        if(section.length()>0&&!shift_name.equalsIgnoreCase("Choose Shift")){
                            alertDialog.cancel();
                            progressDialog.setMessage("Please wait ....");
                            progressDialog.show();
                            update(section,shift_name.toString(),id);

                            // Toast.makeText(getContext(),class_name.getText().toString()+section_name.getText().toString()+shift_name.toString(),Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(getApplicationContext(),"Please Enter a Class Name And Section Name",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{

                        Toast.makeText(getApplicationContext(),"This Section Already Exist",Toast.LENGTH_LONG).show();
                    }




                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.cancel();
                }
            });


        }



    }
    public void update(final String section_name, final String shift_name,String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.edit_section_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext()," Updated Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getApplicationContext(), "Fail to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("section_name", section_name);
                params.put("id",id);
                params.put("shift",shift_name);
                params.put("table", "Section");
                params.put("school_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void showDialog(){


        AlertDialog.Builder alert=new AlertDialog.Builder(All_Sections.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        if( inflater!=null&&alert!=null){


            View view =inflater.inflate(R.layout.add_new_section,null);
            alert.setView(view);
            final AlertDialog alertDialog=alert.show();

            ArrayList<String> list=new ArrayList<>();
            list.add("Choose Shift");
            list.add("Morning");
            list.add("Noon");
            final EditText section_name=view.findViewById(R.id.section_name);
            final Spinner sp_shift_name=view.findViewById(R.id.shift_name);
            sp_shift_name.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_spinner_dropdown,list));
            sp_shift_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    shift_name=list.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            Button confirm=view.findViewById(R.id.addmember);
            Button cancel=view.findViewById(R.id.cancel);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                 String selected_section=section_name.getText().toString().trim();
                  if(!section_names.contains(selected_section)){

                      if(selected_section.length()>0&&!shift_name.equalsIgnoreCase("Choose Shift")){
                          alertDialog.cancel();
                          progressDialog.setTitle("Adding New User");
                          progressDialog.setMessage("Please wait ....");
                          progressDialog.show();
                          add_new_user(class_id,selected_section,shift_name.toString());

                          // Toast.makeText(getContext(),class_name.getText().toString()+section_name.getText().toString()+shift_name.toString(),Toast.LENGTH_LONG).show();
                      }
                      else{

                          Toast.makeText(getApplicationContext(),"Please Enter a Class Name And Section Name",Toast.LENGTH_LONG).show();
                      }

                  }
                  else{

                      Toast.makeText(getApplicationContext(),"This Section Name Already Exist",Toast.LENGTH_LONG).show();
                  }



                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.cancel();
                }
            });


        }

    }

    public void add_new_user(final String class_id,final String section_name,final String shift_name){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_new_section_info_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {


                            Toast.makeText(getApplicationContext(),"Section Added Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("section_name", section_name);
                params.put("shift_name", shift_name);
                params.put("class_id",class_id);
                params.put("table", "Section");
                params.put("school_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void get_all_teacher_data(final String table_name,String url){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        members.clear();
                        members.add(new Member("0","Choose Teacher",null,"NULL",null));
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);

                                        if(table_name.equalsIgnoreCase("Teacher")){
                                            String id = member.getString("id");
                                            String name = member.getString("teacher_name");
                                            String image_path=member.getString("image_path")+" ";
                                            members.add(new Member(id,name,"",image_path,""));
                                        }


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
    public void assignTeacher(final String section_id){


        AlertDialog.Builder builder=new AlertDialog.Builder(All_Sections.this);
        View view=getLayoutInflater().inflate(R.layout.select_teacher,null);
        builder.setView(view);
        Spinner spinner=view.findViewById(R.id.teacher_name);
        spinner.setAdapter(new CustomAdapter(getApplicationContext(),1,members));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                teacher_name=members.get(i).user_name;
                teacher_id=members.get(i).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button confirm=view.findViewById(R.id.addmember);
        Button cancel=view.findViewById(R.id.cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(class_id.length()>0&&!teacher_name.equalsIgnoreCase("Choose Teacher")){
                    alertDialog3.cancel();
                    progressDialog.setTitle("Adding New User");
                    progressDialog.setMessage("Please wait ....");
                    progressDialog.show();

                    check_teacher_already_assign_or_not(section_id,teacher_id);

                }
                else{

                    Toast.makeText(getApplicationContext(),"Please Enter a Class Name And Section Name",Toast.LENGTH_LONG).show();
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog3.cancel();
            }
        });
        alertDialog3=builder.show();

    }

    public void check_teacher_already_assign_or_not(String section_id,String teacher_id){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.check_class_teacher_already_assign_or_not,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Teacher Already Assign In This Class Or Other Class", Toast.LENGTH_SHORT).show();

                        }
                        else if(response.contains("Fail")){

                            Toast.makeText(getApplicationContext(),"Fail To Assign", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            assign_new_teacher(section_id,teacher_id);
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
                params.put("section_id",section_id);
                params.put("teacher_id",teacher_id);
                params.put("table", "Section");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void assign_new_teacher(String section_id,String teacher_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.common_editor_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Teacher Assigned Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getApplicationContext(),"Fail To Assign", Toast.LENGTH_SHORT).show();
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
                params.put("id",section_id);
                params.put("value",teacher_id);
                params.put("column","class_teacher_id");
                params.put("table", "Section");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Member> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<Member> info) {
            this.context = applicationContext;
            this.flag = flag;
            this.info = info;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return info.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.teachers_list_item, null);
            CircleImageView circleImageView=view.findViewById(R.id.image);
            TextView names = (TextView) view.findViewById(R.id.teacher_name);
            Member member=(Member) info.get(i);
            if(!member.profile_image.equalsIgnoreCase("null")){

                Picasso.get().load(member.profile_image).placeholder(R.drawable.profile10).into(circleImageView);
            }
            names.setText(member.user_name);
            return view;
        }
    }

}
