package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_Exam extends AppCompatActivity {

    public NumberPicker nb_month,nb_day,nb_year;
    String class_id="",subject_id,section_id,exam_id;
    Spinner sp_class,sp_student,sp_subject;
    String[] months=new String[12];
    String[] days=new String[31];
    AlertDialog dialog;
    int month,day,year;
    String meridiem;
    ProgressDialog progressDialog;
    Spinner sp_exam_type;
    EditText et_subject_name;
    EditText et_exam_name,et_marks;
    ArrayList<Object> exam_types=new ArrayList<>();
    ArrayList<Object> subjects=new ArrayList<>();
    String exam_type,exam_name,subject_name;
    public NumberPicker nb_hour,nb_minute,daypart;
    String[] str={"AM","PM"};
    String[] hours_display=new String[12];
    String[] minutes_display=new String[60];
    String total_marks;
    int hours,minutes;
    int choosed_sub_index=0;
    ArrayList<String> all_exam_name=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__exam);

        progressDialog=new ProgressDialog(Edit_Exam.this);
        progressDialog.setTitle("Please Wait..");
        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        exam_id=getIntent().getStringExtra("exam_id");
        exam_name=getIntent().getStringExtra("exam_name");
        exam_type=getIntent().getStringExtra("exam_type");
        subject_id=getIntent().getStringExtra("subject_id");
        subject_name=getIntent().getStringExtra("subject_name");
        total_marks=getIntent().getStringExtra("marks");
        all_exam_name=getIntent().getStringArrayListExtra("exam_names");
        sp_exam_type=findViewById(R.id.exam_type);
        et_exam_name=findViewById(R.id.exam_name);
        et_marks=findViewById(R.id.marks);
        exam_types.add("Choose Exam Type");
        exam_types.add("Class Test");
        exam_types.add("Monthly Test");
        exam_types.add("Half Yearly");
        exam_types.add("Final");

        et_marks.setText(total_marks);
        et_exam_name.setText(exam_name);
        sp_exam_type.setAdapter(new CustomAdapter(getApplicationContext(),2,exam_types));

        if(exam_type.equalsIgnoreCase("Class Test")){

            sp_exam_type.setSelection(1);
        }
        else if(exam_type.equalsIgnoreCase("Monthly Test")){

            sp_exam_type.setSelection(2);
        }
        else if(exam_type.equalsIgnoreCase("Half Yearly")){

            sp_exam_type.setSelection(3);
        }
        else if(exam_type.equalsIgnoreCase("Final")){

            sp_exam_type.setSelection(4);
        }

        sp_exam_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int li=adapterView.getChildCount();
                if(li>0) {
                    View linearLayout=adapterView.getChildAt(0);
                    LinearLayout linearLayout1=linearLayout.findViewById(R.id.linear1);
                    LinearLayout linearLayout2=(LinearLayout) linearLayout1.getChildAt(0);
                    TextView textView=(TextView) linearLayout2.getChildAt(1);
                    textView.setTextColor(Color.WHITE);
                }
                exam_type=(String)exam_types.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        sp_subject=findViewById(R.id.subject);
        subjects.add(new Subject("-1","Choose Subject",""));
        get_all_subject();
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int li=adapterView.getChildCount();
                if(li>0) {
                    View linearLayout=adapterView.getChildAt(0);
                    LinearLayout linearLayout1=linearLayout.findViewById(R.id.linear1);
                    LinearLayout linearLayout2=(LinearLayout) linearLayout1.getChildAt(0);
                    TextView textView=(TextView) linearLayout2.getChildAt(1);
                    textView.setTextColor(Color.WHITE);
                }
                Subject subject=(Subject)subjects.get(i);
                subject_name=subject.subject_name;
                subject_id=subject.id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        for(int i=0;i<31;i++){

            if(i<12) months[i]=String.format("%02d",i+1);
            days[i]=String.format("%02d",i+1);
        }

        Calendar calendar=Calendar.getInstance();
        nb_month=findViewById(R.id.month);
        nb_month.setMaxValue(12);
        nb_month.setMinValue(01);
        nb_month.setValue(calendar.get(Calendar.MONTH));
        nb_month.setDisplayedValues(months);
        month=calendar.get(Calendar.MONTH);
        nb_month.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                month=newVal;
            }
        });


        nb_day=findViewById(R.id.day);
        nb_day.setMaxValue(31);
        nb_day.setMinValue(1);
        nb_day.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        nb_day.setDisplayedValues(days);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        nb_day.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                day=newVal;
            }
        });

        nb_year=findViewById(R.id.year);
        nb_year.setMaxValue(2330);
        nb_year.setMinValue(2020);
        nb_year.setValue(calendar.get(Calendar.YEAR));
        year=calendar.get(Calendar.YEAR);
        nb_year.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                year=newVal;
            }
        });

        for(int i=0;i<60;i++){

            if(i<12) hours_display[i]=String.format("%02d",i+1);
            minutes_display[i]=String.format("%02d",i);
        }
        calendar=Calendar.getInstance();
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

    public void get_all_subject(){

        JSONArray postparams = new JSONArray();
        postparams.put("Subject_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_subjects_url,postparams,
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
                                        String id = member.getString("subject_id");
                                        String subject_name=member.getString("subject_name");
                                        String total_assigned_classes = member.getString("total_assigned_classes");
                                        subjects.add(new Subject(id,subject_name,total_assigned_classes));

                                        if(subject_id.equalsIgnoreCase(id)) choosed_sub_index=i+1;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                sp_subject.setAdapter(new CustomAdapter(getApplicationContext(),3,subjects));
                                sp_subject.setSelection(choosed_sub_index);
                                progressDialog.dismiss();
                            }
                            else{
                                sp_subject.setAdapter(new CustomAdapter(getApplicationContext(),3,subjects));
                                progressDialog.dismiss();
                                // Toast.makeText(getApplicationContext(),"No Subject Still Added",Toast.LENGTH_LONG).show();
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
            final TextView names = (TextView) view.findViewById(R.id.teacher_name);



            if(flag==1){
                Classes classes=(Classes) info.get(i);
                names.setText(classes.class_name);
                circleImageView.setVisibility(View.GONE);
            }
            else if(flag==2){

                String s=(String)info.get(i);
                circleImageView.setVisibility(View.GONE);
                names.setText(s);
            }
            else if(flag==3){

                Subject s=(Subject) info.get(i);
                circleImageView.setVisibility(View.GONE);
                names.setText(s.subject_name);
            }

            return view;
        }
    }


    public void submit(View view){

        exam_name=et_exam_name.getText().toString().trim();
        total_marks=et_marks.getText().toString();
        final String time=String.format("%02d",hours)+":"+minutes_display[minutes]+" "+meridiem;
        final String date=year+"-"+(month+1)+"-"+day;
        int marks=Integer.parseInt(total_marks);

        if(!all_exam_name.contains(exam_name)){

            if(exam_type!=null&&exam_name!=null&&subject_name!=null&&!exam_type.contains("Choose Exam Type")&&exam_name.length()>2&&!subject_name.equalsIgnoreCase("Choose Subject")&&marks<=100){


                SubmitExamInfo(date,time);
            }
            else{

                AlertDialog.Builder builder=new AlertDialog.Builder(Edit_Exam.this);
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
        else{

            Toast.makeText(getApplicationContext(),"This Exam Name Already Exist",Toast.LENGTH_LONG).show();
        }


    }
    public void SubmitExamInfo(String date,String time){

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.edit_exam_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            subject_name="";
                            et_exam_name.setText("");
                            sp_subject.setSelection(0);
                            sp_exam_type.setSelection(0);
                            et_marks.setText("");
                            Toast.makeText(getApplicationContext(),"Exam Edited Successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("exam_type", exam_type);
                params.put("exam_name", exam_name);
                params.put("subject_name", subject_name);
                params.put("subject_id", subject_id);
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("date", date);
                params.put("time", time);
                params.put("marks", total_marks);
                params.put("table", "Exam");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public String set_error_message(){
        if(exam_type!=null&&exam_type.contains("Choose Exam Type")){

            return "Please Choose Exam Type";
        }
        else if(exam_name!=null&&exam_name.length()<=2) return "Exam Name length Should be 2";
        else if(subject_name!=null&&subject_name.length()<=2) return "Subject Name length Should be 2";
        else return "Please Enter Valid Data";

    }
}
