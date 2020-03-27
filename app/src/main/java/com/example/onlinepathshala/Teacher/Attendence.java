package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Admin.Member;
import com.example.onlinepathshala.Authority.Subject;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Attendence extends AppCompatActivity {

    ArrayList<Student_List> absent_list=new ArrayList<>();
    ArrayList<Student_List> present_list=new ArrayList<>();
    RecyclerView absence,present;
    ProgressDialog progressDialog;
    TextView absent_header_text,present_header_text;
    RecycleAdapter_Absent recycleAdapter;
    RecycleAdapter_Present recycleAdapter2;
    JSONArray absent_info_list=new JSONArray();
   JSONArray present_info_list=new JSONArray();
    boolean attendance=true;
    String class_id,section_id,class_name,section_name;
    String class_teacher_id="",teacher_id="";
    Button choose_date;
    String date2;
    TextView tv_date;
    LinearLayout date_choose_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        class_name=getIntent().getStringExtra("class_name");
        section_name=getIntent().getStringExtra("section_name");
        class_teacher_id=getIntent().getStringExtra("teacher_id");
        teacher_id=SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();


        absence=findViewById(R.id.recycle);
        present=findViewById(R.id.recycle2);
        recycleAdapter = new RecycleAdapter_Absent(absent_list);
        absence.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        absence.setAdapter(recycleAdapter);

        recycleAdapter2 = new RecycleAdapter_Present(present_list);
        present.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        present.setAdapter(recycleAdapter2);
        choose_date=findViewById(R.id.choose_date);
        tv_date=findViewById(R.id.date);
        date_choose_layout=findViewById(R.id.date_choose_layout);
        if(!teacher_id.equalsIgnoreCase(class_teacher_id)){
            date_choose_layout.setVisibility(GONE);
        }

        Calendar cal=Calendar.getInstance();
        Date date1 = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        tv_date.setText(format1.format(date1));
        format1 = new SimpleDateFormat("yyyy-MM-dd");
        date2=format1.format(date1);

        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(Attendence.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                date2=year + "-" +String.format ("%02d",(monthOfYear + 1)) + "-" + String.format ("%02d",dayOfMonth);
                                String date1=String.format ("%02d",dayOfMonth)+ "-" +String.format ("%02d",(monthOfYear + 1)) + "-" + year;
                                tv_date.setText(date1);




                            }
                        }, year, month, day);
                picker.show();


            }
        });

        progressDialog=new ProgressDialog(Attendence.this);
        absent_header_text=findViewById(R.id.absent_header_text);
        present_header_text=findViewById(R.id.present_header_text);
        progressDialog.setMessage("Please wait...");
        getAllMemberData();


    }


    public void recycle_re_Initialize(){


        recycleAdapter.notifyDataSetChanged();
        recycleAdapter2.notifyDataSetChanged();
        String header="Absent("+absent_list.size()+")";
        absent_header_text.setText(header);
        header="Present("+present_list.size()+")";
        present_header_text.setText(header);

    }



    public class RecycleAdapter_Present extends RecyclerView.Adapter<RecycleAdapter_Present.ViewAdapter>{

        ArrayList<Student_List> classinfos;
        public RecycleAdapter_Present(ArrayList<Student_List> classinfos){
            this.classinfos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            LinearLayout linearLayout;
            CircleImageView profile;
            TextView student_name,roll,name_text,roll_text,id;
            ImageView cause;
            TextView tv_cause;
            LinearLayout cause_li;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                student_name=mView.findViewById(R.id.student_name);
                roll=mView.findViewById(R.id.roll);
                profile=mView.findViewById(R.id.profile);
                linearLayout=mView.findViewById(R.id.view_profile);
                cause=mView.findViewById(R.id.cause);
                tv_cause=mView.findViewById(R.id.causetext);
                cause_li=mView.findViewById(R.id.cause_li);
                id=mView.findViewById(R.id.id);



            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendence_recycle_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Student_List memberInfo=classinfos.get(position);
            holder.student_name.setText(memberInfo.student_name);
            holder.roll.setText(memberInfo.roll);
            holder.student_name.setSelected(true);
            holder.roll.setSelected(true);
            holder.cause.setVisibility(GONE);
            holder.cause_li.setVisibility(GONE);
            holder.id.setText(memberInfo.id);
            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.male_profile).into(holder.profile);
            }
            else {

                holder.profile.setImageResource(R.drawable.male_profile);
            }


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   if(present_list!=null&&absent_list!=null&&present_list.size()>position) {

                       absent_list.add(present_list.get(position));
                       present_list.remove(position);
                       recycle_re_Initialize();
                   }
                }
            });



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public class RecycleAdapter_Absent extends RecyclerView.Adapter<RecycleAdapter_Absent.ViewAdapter>{

        ArrayList<Student_List> classinfos;
        public RecycleAdapter_Absent(ArrayList<Student_List> classinfos){
            this.classinfos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            LinearLayout linearLayout;
            CircleImageView profile;
            TextView student_name,roll,name_text,roll_text,id;
            ImageView cause;
            TextView tv_cause;
            LinearLayout cause_li;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                student_name=mView.findViewById(R.id.student_name);
                roll=mView.findViewById(R.id.roll);
                profile=mView.findViewById(R.id.profile);
                linearLayout=mView.findViewById(R.id.view_profile);
                cause=mView.findViewById(R.id.cause);
                tv_cause=mView.findViewById(R.id.causetext);
                cause_li=mView.findViewById(R.id.cause_li);
                id=mView.findViewById(R.id.id);


            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendence_recycle_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Student_List memberInfo=classinfos.get(position);
            holder.student_name.setText(memberInfo.student_name);
            holder.roll.setText(memberInfo.roll);
            holder.student_name.setSelected(true);
            holder.roll.setSelected(true);
            holder.cause_li.setVisibility(VISIBLE);
            if(memberInfo.getCause().length()>1) holder.tv_cause.setText(memberInfo.getCause());
            holder.tv_cause.setSelected(true);
            holder.id.setText(memberInfo.id);
            holder.cause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showEditDialog("Absent Cause...?","Write Absent Cause",position,memberInfo.getCause());
                }
            });

            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.male_profile).into(holder.profile);
            }
            else {

                holder.profile.setImageResource(R.drawable.male_profile);
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(present_list!=null&&absent_list!=null&&absent_list.size()>position) {

                        present_list.add(absent_list.get(position));
                        absent_list.remove(position);

                        recycle_re_Initialize();
                    }
                }
            });



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public void showEditDialog(String title, String hint,final int position,String text) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Attendence.this);
        View view = getLayoutInflater().inflate(R.layout.profile_edit_dialogbox, null);
        TextView tv_title = view.findViewById(R.id.title);
        final EditText value = view.findViewById(R.id.value);
        if(text.length()>1) value.setText(text);
        tv_title.setText(title);
        value.setHint(hint);
        Button cancel = view.findViewById(R.id.cancel);
        Button confirm = view.findViewById(R.id.confirm);
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (value.getText().toString().length() > 1) {

                    Student_List student_list = absent_list.get(position);
                    absent_list.remove(position);
                    student_list.setCause(value.getText().toString());
                    absent_list.add(position,student_list);
                    dialog.dismiss();
                    recycle_re_Initialize();
                } else {

                    Toast.makeText(getApplicationContext(), "Please Enter New Data", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    public void  getAllMemberData() {

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
                            string = response.getString(0);

                            if (!string.contains("no item")) {

                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String school_name = member.getString("student_name");
                                        String image_path = member.getString("image_path");
                                        String roll="Not Assigned";
                                        if(!member.getString("class_roll").equalsIgnoreCase("NULL")){
                                            roll=member.getString("class_roll");
                                        }
                                        absent_list.add(new Student_List(id, school_name, roll, image_path));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                recycle_re_Initialize();
                                String header="All Students("+absent_list.size()+")";
                                absent_header_text.setText(header);
                                progressDialog.dismiss();
                            } else {

                                Toast.makeText(getApplicationContext(), "No Student Still Added", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.attendence_all:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.attendence_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void present_all(){


        present_list.addAll(absent_list);
        absent_list.clear();
        recycle_re_Initialize();
    }

    public void absent_all(){

        absent_list.addAll(present_list);
        present_list.clear();
        recycle_re_Initialize();
    }

    private LinearLayout linearLayout;
    private CheckBox checkBox;
    private int alertCount = 0;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.attendence_all);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linear);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(checkBox.isChecked()){
                   absent_all();
                   checkBox.setChecked(false);
               }
               else{
                   present_all();
                   checkBox.setChecked(true);
               }
            }
        });
        checkBox=linearLayout.findViewById(R.id.present_all);
        return super.onPrepareOptionsMenu(menu);
    }

    public void submit(View view){

        if(attendance){
            attendance=false;
            submit_attendence();
        }
    }

    public void submit_attendence(){

        check_attendance_already_given_or_not();


    }

    public void check_attendance_already_given_or_not(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.check_attendance_already_given_or_not,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.contains("success")) {

                            attendance=true;
                            Toast.makeText(getApplicationContext(),"This Day's Attendance Already Have Given",Toast.LENGTH_LONG).show();

                        } else {

                            if(class_teacher_id.equalsIgnoreCase(teacher_id)){
                                attendance_upload(Constant_URL.attendence_upload_url,"1");
                            }
                            else{
                                approval_table();
                                attendance_upload(Constant_URL.attendence_upload_url,"0");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("date",date2);
                params.put("table", "Attendance");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void approval_table(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.attendance_approval,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();

                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("class_name",class_name);
                params.put("section_name",section_name);
                params.put("date",date2);
                params.put("teacher_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("table", "Attendance_Approval_Table");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void attendance_upload(String url,String approval_status){

        convert_list_to_jsonarray();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Attendance Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            attendance=true;

                        } else {
                            attendance=true;
                            System.out.println(response.toString());
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        attendance=true;
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("absent_info", absent_info_list.toString());
                params.put("present_info", present_info_list.toString());
                params.put("class_id",class_id);
                params.put("section_id",section_id);
                params.put("approval_status",approval_status);
                params.put("date",date2);
                params.put("table", "Attendance");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void convert_list_to_jsonarray(){

        absent_info_list=new JSONArray();
        for(Student_List student_list:absent_list){

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("id",student_list.id);
             if(student_list.cause.length()>1){
                 jsonObject.addProperty("cause",student_list.cause);
             }
             else{

                 jsonObject.addProperty("cause","no cause");
             }
            absent_info_list.put(jsonObject);
        }
        present_info_list=new JSONArray();
        for(Student_List student_list:present_list){

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("id",student_list.id);
            jsonObject.addProperty("cause","present");
            present_info_list.put(jsonObject);
        }
    }


}
