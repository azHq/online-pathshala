package com.example.onlinepathshala.Authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class Assign_Subject_List_Activity2 extends AppCompatActivity {

    FloatingActionButton dragView;
    ArrayList<Subject> subject_infos=new ArrayList<>();
    ArrayList<Assign_Subject_List_Item> assign_subject_list_items=new ArrayList<>();
    ArrayList<String> days2=new ArrayList<String>(Arrays.asList("","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"));
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView,recyclerView2;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    static String class_id="",section_id,class_name,section_name;
    TextView no_item,tv_class,tv_section;
    RecycleAdapter recycleAdapter;
    AlertDialog assign_teacher,edit_dialog;
    public NumberPicker nb_hour,nb_minute,nb_day,daypart;
    Spinner sp_class,sp_teacher,sp_duration;
    ArrayList<Object> arr_duration=new ArrayList<>();
    int[] duration={30,30,35,40,45,50,55,60,70,80,90,100,110,120};
    ArrayList<Integer> durations=new ArrayList<Integer>(Arrays.asList(30,30,35,40,45,50,55,60,70,80,90,100,110,120));
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
    String start_time;
    String end_time=null;
    Button ok,cancel;
    String teacher_id2="";
    int teacher_selection_index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_subject);

        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        class_name=getIntent().getStringExtra("class_name");
        section_name=getIntent().getStringExtra("section_name");
        frameLayout=findViewById(R.id.frame_layout);
        no_item=findViewById(R.id.no_item);
        recyclerView=findViewById(R.id.recycle1);
        tv_class=findViewById(R.id.class_name);
        tv_section=findViewById(R.id.section_name);
        progressDialog=new ProgressDialog(Assign_Subject_List_Activity2.this);
        progressDialog.setMessage("Please wait...");

        tv_class.setText(class_name);
        tv_section.setText(section_name);
        getAllSubjectData();
        getAllAssignSubjectData();
        recycleAdapter=new RecycleAdapter(subject_infos,assign_subject_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recycleAdapter);



    }


    public void re_init() {

        getAllSubjectData();
        getAllAssignSubjectData();

    }


    public void generalize(){

        recycleAdapter.notifyDataSetChanged();

    }
    public void getAllSubjectData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Subject_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_subjects_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        subject_infos.clear();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);

                                        String subject_id=member.getString("subject_id");
                                        String subject_name=member.getString("subject_name");
                                        subject_infos.add(new Subject(subject_id,subject_name,""));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                generalize();
                                progressDialog.dismiss();
                                no_item.setVisibility(View.GONE);

                            }
                            else{

                                generalize();
                                no_item.setVisibility(View.VISIBLE);
                                no_item.setText("Yet Not Assign Any Subject In This Section");
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

    public void getAllAssignSubjectData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Subject_Assigned_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_assigned_subject_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        assign_subject_list_items.clear();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("ass_id");
                                        String class_name=member.getString("class_name");
                                        String teacher_name = member.getString("teacher_name");
                                        String subject_name=member.getString("subject_name");
                                        String class_id = member.getString("class_id");
                                        String teacher_id=member.getString("teacher_id");
                                        String section_id = member.getString("section_id");
                                        String subject_id = member.getString("subject_id");
                                        String start_time = member.getString("start_time");
                                        String end_time = member.getString("end_time");
                                        String day = member.getString("day");
                                        assign_subject_list_items.add(new Assign_Subject_List_Item(id,class_id,class_name,teacher_id,teacher_name,subject_id,subject_name,section_id,section_name,start_time,end_time,day));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                generalize();
                                progressDialog.dismiss();
                            }
                            else{
                                progressDialog.dismiss();
                                generalize();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                        Toast.makeText(getApplicationContext(),"Connection Error",Toast.LENGTH_LONG).show();
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

        ArrayList<Subject> classinfos;
        ArrayList<Assign_Subject_List_Item> assign_subject_list_items;
        public RecycleAdapter(ArrayList<Subject> classinfos,ArrayList<Assign_Subject_List_Item> assign_subject_list_items){
            this.classinfos=classinfos;
            this.assign_subject_list_items=assign_subject_list_items;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            LinearLayout linearLayout;
            RecyclerView recyclerView;
            FloatingActionButton add;
            TextView subject_name,recycle;
            LinearLayout main_layout;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                subject_name=mView.findViewById(R.id.subject_name);
                linearLayout=mView.findViewById(R.id.view_profile);
                recyclerView=mView.findViewById(R.id.recycle);
                add=mView.findViewById(R.id.add);
                main_layout=mView.findViewById(R.id.main_layout);







            }





        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_teacher_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Subject memberInfo=classinfos.get(position);


            holder.subject_name.setText(memberInfo.subject_name);
            ArrayList<Assign_Subject_List_Item> assign_subject_list_items2=new ArrayList<>();

            for(Assign_Subject_List_Item assign_subject_list_item:assign_subject_list_items){

               if(memberInfo.id.equalsIgnoreCase(assign_subject_list_item.subject_id)){
                   assign_subject_list_items2.add(assign_subject_list_item);
               }
            }
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            RecycleAdapter2 recycleAdapter2=new RecycleAdapter2(assign_subject_list_items2);
            holder.recyclerView.setAdapter(recycleAdapter2);

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    subject_name=memberInfo.subject_name;
                    subject_id=memberInfo.id;
                   show_add_dialog();
                }
            });

        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public class RecycleAdapter2 extends RecyclerView.Adapter<RecycleAdapter2.ViewAdapter>{

        ArrayList<Assign_Subject_List_Item> classinfos;
        public RecycleAdapter2(ArrayList<Assign_Subject_List_Item> classinfos){
            this.classinfos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            Button edit;
            LinearLayout layout;
            TextView teacher_name,start_time,end_time,day;
            View mView;

            public ViewAdapter(View itemView) {
                super(itemView);

                itemView.setOnLongClickListener(this);
                mView=itemView;
                teacher_name=mView.findViewById(R.id.teacher_name);
                start_time=mView.findViewById(R.id.start_time);
                end_time=mView.findViewById(R.id.end_time);
                day=mView.findViewById(R.id.day);
                edit=mView.findViewById(R.id.edit);
                layout=mView.findViewById(R.id.layout);



            }


            @Override
            public boolean onLongClick(View view) {
                int position =getLayoutPosition();
                Assign_Subject_List_Item memberInfo=classinfos.get(position);
                showOptionDialog(memberInfo.id);
                return true;
            }
        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final Assign_Subject_List_Item memberInfo=classinfos.get(position);
            holder.teacher_name.setText(memberInfo.teacher_name);


            holder.start_time.setText( formating(memberInfo.start_time));
            holder.end_time.setText(formating(memberInfo.end_time));
            holder.day.setText(memberInfo.day);


            holder.teacher_name.setSelected(true);
            holder.start_time.setSelected(true);
            holder.end_time.setSelected(true);
            holder.end_time.setSelected(true);

            if(position%2==0){

                holder.layout.setBackgroundColor(Color.parseColor("#57E78343"));
            }
            else{

                holder.layout.setBackgroundColor(Color.parseColor("#57214E49"));
            }
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subject_name=memberInfo.subject_name;
                    subject_id=memberInfo.subject_id;
                    show_edit_dialog(memberInfo.teacher_id,memberInfo.start_time,memberInfo.end_time,memberInfo.day,memberInfo.id);
                }
            });



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public String formating(String time){

        String s = time;
        DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
        Date d = null;
        try {
            d = f1.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("h:mm a");
        String output=f2.format(d).toLowerCase(); // "12:18am"

        return output;
    }



    public void showOptionDialog(final String id){

        AlertDialog.Builder builder=new AlertDialog.Builder(Assign_Subject_List_Activity2.this);
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        assign_teacher.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        final AlertDialog alertDialog2=builder.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showDeleteDialogBox(id);
            }
        });



    }


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(Assign_Subject_List_Activity2.this);
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.single_row_delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Class Deleted Successfully", Toast.LENGTH_SHORT).show();
                            re_init();

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
                params.put("table", "Subject_Assigned_Table");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void show_add_dialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Assign_Subject_List_Activity2.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.assign_teacher,null);
        builder.setView(view);
        getAllTeacherData("Teacher", Constant_URL.get_all_data_url,"add");
        progressDialog=new ProgressDialog(Assign_Subject_List_Activity2.this);
        memberInfos.add(new Member("0","Choose Teacher",null,null,null));
        setDisplayValue(view);
        assign_teacher=builder.show();
    }

    public void setDisplayValue(View view){

        for(int i=0;i<60;i++){

            if(i<12) hours_display[i]=String.format("%02d",i+1);
            minutes_display[i]=String.format("%02d",i);
        }

        sp_teacher=view.findViewById(R.id.teacher_name);
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
        arr_duration.clear();
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
        sp_duration=view.findViewById(R.id.duration);
        sp_duration.setAdapter(new CustomAdapter(getApplicationContext(),1,arr_duration));
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


        nb_minute=view.findViewById(R.id.minutes);
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

        nb_day=view.findViewById(R.id.day);
        nb_day.setMaxValue(6);
        nb_day.setMinValue(0);
        nb_day.setValue(calendar.get(Calendar.DAY_OF_WEEK));

        day=dayslist[calendar.get(Calendar.DAY_OF_WEEK)];
        nb_day.setDisplayedValues(days);
        nb_day.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                day=days[newVal];
            }
        });
        daypart=view.findViewById(R.id.daypart);
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

        ok=view.findViewById(R.id.addmember);
        cancel=view.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                submit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assign_teacher.dismiss();

            }
        });

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

    public void show_edit_dialog(String teacher_id3,String start_time2,String end_time,String day3,String ass_id){

        AlertDialog.Builder builder=new AlertDialog.Builder(Assign_Subject_List_Activity2.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.assign_teacher,null);
        builder.setView(view);
        progressDialog=new ProgressDialog(Assign_Subject_List_Activity2.this);


        DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
        Date d1 = null,d2=null;
        try {
            d1 = f1.parse(start_time2);
            d2 = f1.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int diff=(int)(((d2.getTime()-d1.getTime())/1000)/60);
        int duration2=durations.indexOf(diff);
        teacher_id2=teacher_id3;
        display_edit_value(view,teacher_id2,start_time2,duration2,day3,ass_id);
        assign_teacher=builder.show();
    }

    public void display_edit_value(View view,final String teacher_id2,String start_time2,int duration2,String day3,final String ass_id){


        for(int i=0;i<60;i++){

            if(i<12) hours_display[i]=String.format("%02d",i+1);
            minutes_display[i]=String.format("%02d",i);
        }



        getAllTeacherData("Teacher", Constant_URL.get_all_data_url,"edit");
        sp_teacher=view.findViewById(R.id.teacher_name);
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
        arr_duration.clear();
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

        nb_day=view.findViewById(R.id.day);
        nb_day.setMaxValue(6);
        nb_day.setMinValue(0);
        int day2=days2.indexOf(day3);
        nb_day.setValue(day2);
        nb_day.setDisplayedValues(days);
        nb_day.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                day=days[newVal];
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

        String[] str=start_time2.split(":");
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

                edit(ass_id);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assign_teacher.dismiss();

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

    public void getAllTeacherData(final String table_name,String url,String type){


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
                            memberInfos.clear();
                            memberInfos.add(new Member("0","Choose Teacher",null,null,null));
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

                                int index=0;


                                if(type.equalsIgnoreCase("edit")){

                                    for(Object obj:memberInfos){
                                        Member teacher=(Member) obj;
                                        if(teacher_id2.equalsIgnoreCase(teacher.id)){

                                            break;
                                        }
                                        index++;
                                        teacher_selection_index=index;


                                    }
                                    sp_teacher.setAdapter(new CustomAdapter(getApplicationContext(),3,memberInfos));
                                    sp_teacher.setSelection(teacher_selection_index);
                                }
                                else {
                                    sp_teacher.setAdapter(new CustomAdapter(getApplicationContext(),3,memberInfos));
                                }

                            }
                            else{
                                sp_teacher.setAdapter(new CustomAdapter(getApplicationContext(),3,memberInfos));
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

    public void submit(){

        calculate_start_time_and_end_time();
        time=String.format("%02d",hours)+":"+minutes_display[minutes]+" "+meridiem;



        if(class_name!=null&&teacher_name!=null&&subject_name!=null&&!class_name.contains("Choose Class")&&!teacher_name.contains("Choose Teacher")&&!subject_name.contains("Choose Subject")&&end_time!=null){

            assign_class();
        }
        else{

            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Assign_Subject_List_Activity2.this);
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
    public void edit(String ass_id){

        calculate_start_time_and_end_time();
        time=String.format("%02d",hours)+":"+minutes_display[minutes]+" "+meridiem;



        if(class_name!=null&&teacher_name!=null&&subject_name!=null&&!class_name.contains("Choose Class")&&!teacher_name.contains("Choose Teacher")&&!subject_name.contains("Choose Subject")&&end_time!=null){

            edit_assign_class(ass_id);
        }
        else{

            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Assign_Subject_List_Activity2.this);
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


    public void assign_class(){


        if(meridiem.contains("AM")) {
            formated_time=String.format("%02d",hours)+":"+String.format("%02d",minutes);
        }
        else{

            if( hours!=12) formated_time=String.format("%02d",hours+12)+":"+String.format("%02d",minutes);
            else{
                formated_time=String.format("%02d",hours)+":"+String.format("%02d",minutes);
            }
        }


      //  Toast.makeText(getApplicationContext(),"Teacher assign",Toast.LENGTH_LONG).show();
        check_teacher_already_assign_or_not("","add");
    }



    public void edit_assign_class(String ass_id){


        if(meridiem.contains("AM")) {
            formated_time=String.format("%02d",hours)+":"+String.format("%02d",minutes);
        }
        else{

            if( hours!=12) formated_time=String.format("%02d",hours+12)+":"+String.format("%02d",minutes);
            else{
                formated_time=String.format("%02d",hours)+":"+String.format("%02d",minutes);
            }
        }
        check_teacher_already_assign_or_not(ass_id,"edit");


    }

    public void check_teacher_already_assign_or_not(String ass_id,String type){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.check_teacher_already_assign_or_not,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {


                            Toast.makeText(getApplicationContext(),"This Teacher Already Have Assigned", Toast.LENGTH_SHORT).show();


                        }else if(response.contains("not assign"))
                        {
                            if(type.equalsIgnoreCase("add")){

                                upload_data(ass_id,Constant_URL.subject_assign_url);
                            }
                            else if(type.equalsIgnoreCase("edit")){

                                upload_data(ass_id,Constant_URL.edit_subject_assign_url);
                            }
                        }
                        else {

                            Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("teacher_id", teacher_id);
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

    public void upload_data(String ass_id,String url){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            teacher_name="";

                            assign_teacher.dismiss();
                            Toast.makeText(getApplicationContext(),"Subject Assigned Successfully", Toast.LENGTH_SHORT).show();

                            re_init();;

                        } else {
                            Toast.makeText(getApplicationContext(),"Fail To Assign Subject", Toast.LENGTH_SHORT).show();
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
                params.put("id", ass_id);
                params.put("table", "Subject_Assigned_Table");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }






}
