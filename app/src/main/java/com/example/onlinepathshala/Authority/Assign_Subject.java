package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.onlinepathshala.VolleySingleton;
import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Assign_Subject extends AppCompatActivity {

    public NumberPicker nb_hour,nb_minute,nb_day,daypart;
    Spinner sp_class,sp_teacher,sp_duration;
    ArrayList<Object> arr_duration=new ArrayList<>();
    int[] duration={30,30,35,40,45,50,55,60,70,80,90,100,110,120};
    String[] days={"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
    String[] str={"AM","PM"};
    String[] hours_display=new String[12];
    String[] minutes_display=new String[60];
    int hours,minutes;
    String class_name,class_id,teacher_name,teacher_id,subject_name,subject_id,time,day,meridiem,section_id;
    ProgressDialog progressDialog;
    ArrayList<Object> memberInfos=new ArrayList<>();
    ArrayList<Object> class_infos=new ArrayList<>();
    ArrayList<Object> subject_infos=new ArrayList<>();
    String formated_time="";
    String start_time;
    String end_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign__subject);

        class_id=getIntent().getStringExtra("class_id");
        subject_id=getIntent().getStringExtra("subject_id");
        section_id=getIntent().getStringExtra("section_id");
        subject_name=getIntent().getStringExtra("subject_name");
        class_name=getIntent().getStringExtra("class_name");


        progressDialog=new ProgressDialog(Assign_Subject.this);
        memberInfos.add(new Member("0","Choose Teacher",null,null,null));
        getAllMemberData("Teacher", Constant_URL.get_all_data_url);
        setDisplayValue();

    }

    public void setDisplayValue(){

        for(int i=0;i<60;i++){

            if(i<12) hours_display[i]=String.format("%02d",i+1);
            minutes_display[i]=String.format("%02d",i);
        }

        sp_teacher=findViewById(R.id.teacher_name);
        sp_teacher.setAdapter(new CustomAdapter(getApplicationContext(),3,memberInfos));
        sp_teacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Member member=(Member) memberInfos.get(position);
                teacher_name=member.user_name;
                teacher_id=member.id;



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arr_duration.add("Choose Duration");
        arr_duration.add("30 Minutes");
        arr_duration.add("35 Minutes");
        arr_duration.add("40 Minutes");
        arr_duration.add("45 Minutes");
        arr_duration.add("50 Minutes");
        arr_duration.add("55 Minutes");
        arr_duration.add("1 Hour");
        arr_duration.add("1 Hour 10 Minutes");
        arr_duration.add("1 Hour 20 Minutes");
        arr_duration.add("1 Hour 30 Minutes");
        arr_duration.add("1 Hour 40 Minutes");
        arr_duration.add("2 Hours");
        sp_duration=findViewById(R.id.duration);
        sp_duration.setAdapter(new CustomAdapter(getApplicationContext(),1,arr_duration));
        sp_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int end_min=hours*60+minutes+duration[i];

                int hour=end_min/60;
                int minute=end_min%60;

                if(meridiem.contains("AM")) {
                    end_time=String.format("%02d",hour)+":"+String.format("%02d",minute);
                }
                else{

                    if( hour!=12) end_time=String.format("%02d",hour+12)+":"+String.format("%02d",minute);
                    else{
                        end_time=String.format("%02d",hour)+":"+String.format("%02d",minute);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Calendar calendar=Calendar.getInstance();
        nb_hour=findViewById(R.id.hour);
        nb_hour.setMaxValue(12);
        nb_hour.setMinValue(01);
        nb_hour.setValue(calendar.get(Calendar.HOUR));
        nb_hour.setDisplayedValues(hours_display);
        hours=calendar.get(Calendar.HOUR);
        if(hours==0) hours=12;
        nb_hour.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                hours=newVal;
            }
        });


        nb_minute=findViewById(R.id.minutes);
        nb_minute.setMaxValue(59);
        nb_minute.setMinValue(00);
        nb_minute.setValue(calendar.get(Calendar.MINUTE));
        nb_minute.setDisplayedValues(minutes_display);
        minutes=calendar.get(Calendar.MINUTE);
        nb_minute.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                minutes=newVal;
            }
        });

        nb_day=findViewById(R.id.day);
        nb_day.setMaxValue(6);
        nb_day.setMinValue(0);
        nb_day.setValue(calendar.get(Calendar.DAY_OF_WEEK));
        day=days[calendar.get(Calendar.DAY_OF_WEEK)-1];
        nb_day.setDisplayedValues(days);
        nb_day.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                day=days[newVal];
            }
        });
        daypart=findViewById(R.id.daypart);
        daypart.setMaxValue(1);
        daypart.setMinValue(0);
        daypart.setDisplayedValues(str);
        daypart.setValue(calendar.get(Calendar.AM_PM));
        meridiem=str[calendar.get(Calendar.AM_PM)];
        daypart.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                meridiem=str[newVal];
            }
        });

    }

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Object> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<Object> info) {
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


            if(flag==1){
                String member=(String) info.get(i);
                names.setText(member);
                circleImageView.setVisibility(View.GONE);
            }
            if(flag==3){

                Member member=(Member) info.get(i);
                names.setText(member.user_name);
            }
            return view;
        }
    }

    public void getAllMemberData(final String table_name,String url){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                url,postparams,
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

                                        if(table_name.equalsIgnoreCase("Teacher")){
                                            String id = member.getString("id");
                                            String name = member.getString("teacher_name");
                                            String image_path=member.getString("image_path");
                                            memberInfos.add(new Member(id,name,null,image_path,null));
                                        }
                                        else if(table_name.equalsIgnoreCase("Class_Table")){

                                            String id = member.getString("class_id");
                                            String name = member.getString("class_name");
                                            class_infos.add(new Classes(id,name,null,null,null));
                                        }
                                        else if(table_name.equalsIgnoreCase("Subject_Table")){

                                            String id = member.getString("subject_id");
                                            String name = member.getString("subject_name");
                                            subject_infos.add(new Subject(id,name,null));
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

    public void submit(View view){


        time=String.format("%02d",hours)+":"+minutes_display[minutes]+" "+meridiem;



        if(class_name!=null&&teacher_name!=null&&subject_name!=null&&!class_name.contains("Choose Class")&&!teacher_name.contains("Choose Teacher")&&!subject_name.contains("Choose Subject")&&end_time!=null){

            review_info(view);
        }
        else{

            AlertDialog.Builder builder=new AlertDialog.Builder(Assign_Subject.this);
            builder.setTitle("Please Fill Up All Required Data");
            builder.setMessage(set_error_message());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }

    }

    public String set_error_message(){

        if(class_name.contains("Choose Class")){

            return "Please Select Class Name";
        }
        else if(subject_name.contains("Choose Subject")){

            return "Please Select Subject Name";
        }
        else if(teacher_name.contains("Choose Teacher")){

            return "Please Select Teacher Name";
        }
        else if(end_time==null){

            return "Please Select Duration";
        }



        return "Please Fillup The Form With All Required Info";
    }

    public void review_info(View v){


        android.app.AlertDialog.Builder alert=new android.app.AlertDialog.Builder(Assign_Subject.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        ViewGroup viewGroup = findViewById(android.R.id.content);
        if( inflater!=null){


            View view =inflater.inflate(R.layout.assign_subject_info_review,viewGroup,false);
            alert.setView(view);
            final android.app.AlertDialog alertDialog=alert.show();
            TextView et_class_name=view.findViewById(R.id.class_name);
            TextView et_teacher_name=view.findViewById(R.id.teacher_name);
            TextView et_subject_name=view.findViewById(R.id.subject_name);
            TextView et_time=view.findViewById(R.id.time);
            TextView et_day=view.findViewById(R.id.day);
            et_class_name.setText(class_name);
            et_teacher_name.setText(teacher_name);
            et_subject_name.setText(subject_name);
            et_time.setText(time);
            et_day.setText(day);
            Button confirm=view.findViewById(R.id.confirm);
            Button cancel=view.findViewById(R.id.cancel);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    alertDialog.cancel();
                    nb_hour.setValue(1);
                    nb_minute.setValue(0);
                    nb_day.setValue(0);
                    sp_teacher.setSelection(0);

                    progressDialog.setTitle("Adding New User");
                    progressDialog.setMessage("Please wait ....");
                    progressDialog.show();
                    add_new_user();


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

    public void add_new_user(){


        if(meridiem.contains("AM")) {
            formated_time=String.format("%02d",hours)+":"+String.format("%02d",minutes);
        }
        else{

            if( hours!=12) formated_time=String.format("%02d",hours+12)+":"+String.format("%02d",minutes);
            else{
                formated_time=String.format("%02d",hours)+":"+String.format("%02d",minutes);
            }
        }

        System.out.println(formated_time+"end"+end_time);
        System.out.println("subject id"+subject_id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.subject_assign_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            teacher_name="";

                            Toast.makeText(getApplicationContext(),"Subject Assigned Successfully", Toast.LENGTH_SHORT).show();
                            finish();

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
                params.put("class_name", class_name);
                params.put("teacher_name", teacher_name);
                params.put("subject_name", subject_name);
                params.put("class_id", class_id);
                params.put("teacher_id", teacher_id);
                params.put("subject_id", subject_id);
                params.put("section_id", section_id);
                params.put("start_time", formated_time);
                params.put("end_time", end_time);
                params.put("day", day);
                params.put("table", "Subject_Assigned_Table");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}
