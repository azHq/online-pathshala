package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class System_Configuration extends AppCompatActivity {

    AlertDialog assign_teacher,edit_dialog;
    public NumberPicker nb_hour,nb_minute,nb_day,daypart;
    Spinner sp_class,sp_teacher,sp_duration;
    ArrayList<Object> arr_duration=new ArrayList<>();
    int[] duration={0,15,30,30,35,40,45,50,55,60,70,80,90,100,110,120};
    ArrayList<Integer> durations=new ArrayList<Integer>(Arrays.asList(0,15,30,30,35,40,45,50,55,60,70,80,90,100,110,120));
    int duratin_index=0;
    String[] days={"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
    String[] dayslist={"","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    String[] str={"AM","PM"};
    String[] hours_display=new String[12];
    String[] minutes_display=new String[60];
    int hours,minutes;
    String teacher_name,teacher_id,subject_name,subject_id,time,day,meridiem;
    ArrayList<Object> memberInfos=new ArrayList<>();
    ArrayList<Object> class_infos=new ArrayList<>();
    String formated_time="";
    String start_time="00:00:00";
    String end_time="00:00:00";
    String start_time2="00:00:00";
    String end_time2="00:00:00";
    Button ok,cancel;
    String teacher_id2="";
    int teacher_selection_index=0;
    TextView tv_school_id,tv_start_time,tv_end_time;
    ProgressDialog progressDialog;
    Button edit,teacher_evaluation_question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system__configuration);
        edit=findViewById(R.id.edit);
        teacher_evaluation_question=findViewById(R.id.teacher_evaluation_question);
        teacher_evaluation_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),Teacher_Evaluation_Question.class));
            }
        });
        tv_start_time=findViewById(R.id.start_time);
        tv_end_time=findViewById(R.id.end_time);
        progressDialog=new ProgressDialog(System_Configuration.this);
        progressDialog.setMessage("Please Wait..");
        getTiffin_Time();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show_edit_dialog(start_time2,end_time2);
            }
        });
        tv_school_id=findViewById(R.id.id);
        tv_school_id.setText("My School Id: "+SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

    }

    public void get_Teacher_Evaluation_Question(){

        JSONArray postparams = new JSONArray();
        postparams.put("Teacher_Evaluation_Question");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_data_url,postparams,
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

    public void getTiffin_Time(){

        JSONArray postparams = new JSONArray();
        postparams.put("School");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_data_url,postparams,
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
                                            if(member.has("start_time_for_break")&&member.has("end_time_for_break")){

                                               start_time2=member.getString("start_time_for_break");
                                               end_time2=member.getString("end_time_for_break");

                                                DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                                                DateFormat f2 = new SimpleDateFormat("hh:mm aa");
                                                Date d1 = null,d2=null;
                                                try {
                                                    d1 = f1.parse(start_time2);
                                                    d2 = f1.parse(end_time2);

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                               tv_start_time.setText(f2.format(d1));
                                               tv_end_time.setText(f2.format(d2));
                                            }
                                            else{

                                                start_time2="00:00:00";
                                                end_time2="00:00:00";

                                                tv_start_time.setText(start_time2);
                                                tv_end_time.setText(end_time2);
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

    public void calculate_start_time_and_end_time(){

        if(duratin_index>0){

            int end_min=hours*60+minutes+duration[duratin_index];

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
        else {
            end_time=null;
        }

    }

    public void show_edit_dialog(String start_time,String end_time){

        AlertDialog.Builder builder=new AlertDialog.Builder(System_Configuration.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.set_break_time,null);
        builder.setView(view);
        progressDialog=new ProgressDialog(System_Configuration.this);


        DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
        Date d1 = null,d2=null;
        try {
            d1 = f1.parse(start_time);
            d2 = f1.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int diff=(int)(((d2.getTime()-d1.getTime())/1000)/60);
        int duration2=durations.indexOf(diff);
        display_edit_value(view,start_time2,duration2);
        assign_teacher=builder.show();
    }

    public void display_edit_value(View view,String start_time,int duration2){



        for(int i=0;i<60;i++){

            if(i<12) hours_display[i]=String.format("%02d",i+1);
            minutes_display[i]=String.format("%02d",i);
        }
        arr_duration.clear();
        arr_duration.add("Choose Duration");
        arr_duration.add("0 Minutes");
        arr_duration.add("15 Minutes");
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
        sp_duration=view.findViewById(R.id.duration);
        sp_duration.setAdapter(new CustomAdapter(getApplicationContext(),1,arr_duration));
        sp_duration.setSelection(duration2);
        sp_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                duratin_index=i;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Calendar calendar=Calendar.getInstance();
        nb_hour=view.findViewById(R.id.hour);
        nb_hour.setMaxValue(12);
        nb_hour.setMinValue(01);
        nb_hour.setDisplayedValues(hours_display);
        if(hours==0) hours=12;
        nb_hour.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                hours=newVal;
            }
        });




        nb_minute=view.findViewById(R.id.minutes);
        nb_minute.setMaxValue(59);
        nb_minute.setMinValue(00);
        nb_minute.setDisplayedValues(minutes_display);
        nb_minute.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                minutes=newVal;
            }
        });

        daypart=view.findViewById(R.id.daypart);
        daypart.setMaxValue(1);
        daypart.setMinValue(0);
        daypart.setDisplayedValues(str);
        daypart.setValue(calendar.get(Calendar.AM_PM));
        daypart.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                meridiem=str[newVal];
            }
        });

        String[] str=start_time.split(":");
        int hr=Integer.parseInt(str[0]);
        if(hr>=12){
            if(hr==12){

                daypart.setValue(1);
                meridiem="PM";
            }else{
                hr-=12;
                daypart.setValue(1);
                meridiem="PM";
            }
            nb_hour.setValue(hr);
        }
        else{
            meridiem="AM";
            nb_hour.setValue(hr);
        }
        hours=hr;
        minutes=Integer.parseInt(str[1]);

        nb_minute.setValue(Integer.parseInt(str[1]));
        ok=view.findViewById(R.id.addmember);
        cancel=view.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculate_start_time_and_end_time();
                time=String.format("%02d",hours)+":"+minutes_display[minutes]+" "+meridiem;

                if(end_time!=null){

                    assign_teacher.dismiss();
                    update();
                }
                else{

                    androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(System_Configuration.this);
                    builder.setTitle("Please Fill Up All Required Data");
                    builder.setMessage("Please Choose Duration");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assign_teacher.dismiss();

            }
        });


    }

    public void update(){

        if(meridiem.contains("AM")) {
            formated_time=String.format("%02d",hours)+":"+String.format("%02d",minutes);
        }
        else{

            if( hours!=12) formated_time=String.format("%02d",hours+12)+":"+String.format("%02d",minutes);
            else{
                formated_time=String.format("%02d",hours)+":"+String.format("%02d",minutes);
            }
        }
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.update_break_time,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();


                        if (response.contains("success")) {


                            Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                            getTiffin_Time();

                        } else {
                            Toast.makeText(getApplicationContext(), "Fail To Update", Toast.LENGTH_SHORT).show();
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
                params.put("start_time",formated_time);
                params.put("end_time",end_time);
                params.put("table", "School");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
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
}
