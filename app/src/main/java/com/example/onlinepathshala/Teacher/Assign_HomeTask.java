package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.onlinepathshala.Authority.Add_New_Teacher;
import com.example.onlinepathshala.Authority.Classes;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Authority.Subject;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Assign_HomeTask extends AppCompatActivity {

    public NumberPicker nb_month,nb_day,nb_year;
    Spinner sp_class,sp_student,sp_subject;
    String[] months=new String[12];
    String[] days=new String[31];
    AlertDialog dialog;
    int month,day,year;
    String class_name,class_id,section_id,date,teacher_id,subject_name,subject_id,time,meridiem,exam_name,task_number;
    ProgressDialog progressDialog;
    ArrayList<Student> student_infos=new ArrayList<>();
    ArrayList<Object> subject_infos=new ArrayList<>();
    ArrayList<String> student_ids=new ArrayList<>();
    ArrayList<String> device_ids=new ArrayList<>();
    RelativeLayout choose_student1;
    boolean All_students=false;
    String formated_time="";
    Button confirm,cancel;
    String submission_date;
    EditText et_title,et_details;
    String title,details;
    CheckBox select_all;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign__home_task);

        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        task_number=getIntent().getStringExtra("task_number");


        progressDialog=new ProgressDialog(Assign_HomeTask.this);
        subject_infos.add(new Subject("0","Choose Subject",null));
        getAllMemberData("Student", Constant_URL.get_all_student_info_url,class_id,section_id);
        getAllMemberData("Subject_Table", Constant_URL.all_subject_for_teacher,class_id,section_id);
        setDisplayValue();
    }

    public void getAllMemberData(final String table_name,String url,String class_id,String section_id){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());

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

                                        if(table_name.equalsIgnoreCase("Student")){
                                            String id = member.getString("id");
                                            String name = member.getString("student_name");
                                            String image_path=member.getString("image_path");
                                            String roll=member.getString("class_roll");
                                            String device_id=member.getString("device_id");
                                            student_infos.add(new Student(id,name,null,image_path,roll,device_id));
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


    public void setDisplayValue(){

        for(int i=0;i<31;i++){

            if(i<12) months[i]=String.format("%02d",i+1);
            days[i]=String.format("%02d",i+1);
        }


        et_title=findViewById(R.id.title);
        et_details=findViewById(R.id.details);
        confirm=findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title=et_title.getText().toString();
                details=et_details.getText().toString();
                if(title.length()>2&&details.length()>5&&!subject_name.contains("Choose Subject")&&(!student_ids.isEmpty()||All_students||!device_ids.isEmpty())){



                    assign_hometask();
                }
                else{

                    AlertDialog.Builder builder=new AlertDialog.Builder(Assign_HomeTask.this);
                    builder.setTitle("Please Give Required Info");
                    builder.setMessage(error_message());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }


            }
        });
        cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        choose_student1=findViewById(R.id.choose_student1);
        choose_student1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });



        sp_subject=findViewById(R.id.choose_subject);
        sp_subject.setAdapter(new CustomAdapter(getApplicationContext(),1,subject_infos));

        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Subject subject=(Subject)subject_infos.get(position);
                subject_name=subject.subject_name;
                subject_id=subject.id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        nb_year.setMaxValue(2220);
        nb_year.setMinValue(2019);
        nb_year.setValue(calendar.get(Calendar.YEAR));
        year=calendar.get(Calendar.YEAR);
        nb_year.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {

                year=newVal;
            }
        });


    }
    public String error_message(){

        if(title.length()<=2) return  "Please enter 2 character length title";
        else if(details.length()<=5) return  "Please enter 6 character length details";
        else if(subject_name.contains("Choose Subject")) return "Please Choose a Subject";
        else if(student_ids.isEmpty()||!All_students||device_ids.isEmpty()) return "Please Choose Student";
        else return "Please Enter All Required Info";

    }
    public void assign_hometask(){

        int task_num=Integer.parseInt(task_number)+1;
        submission_date=year+"-"+(month+1)+"-"+day;
        if(All_students) chooseAllStudent();
        final JSONArray student_id_list=new JSONArray(student_ids);
        final JSONArray device_id_list=new JSONArray(device_ids);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.assign_home_task_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            All_students=false;
                            et_title.setText("");
                            et_details.setText("");
                            student_ids.clear();
                            device_ids.clear();
                            Toast.makeText(getApplicationContext(),"Home Task Added Successfully", Toast.LENGTH_SHORT).show();
                            finish();


                        } else {
                            System.out.println(response.toString());
                            Toast.makeText(getApplicationContext(), "Fail To Assign Home Task", Toast.LENGTH_SHORT).show();
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
                params.put("teacher_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("student_id", student_id_list.toString());
                params.put("device_id", device_id_list.toString());
                params.put("subject_name", subject_name);
                params.put("subject_id", subject_id);
                params.put("task_number",task_num+"");
                params.put("title", title);
                params.put("details", details);
                params.put("submission_date", submission_date);
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("table", "Home_Task");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void showAlertDialog()
    {

        if(student_infos.size()>0){

            AlertDialog.Builder builder=new AlertDialog.Builder(Assign_HomeTask.this);
            View view=getLayoutInflater().inflate(R.layout.studentlist_recycle,null);
            final RecyclerView recyclerView=view.findViewById(R.id.recycle);
            Button ok=view.findViewById(R.id.ok);
            Button cancel=view.findViewById(R.id.cancel);
            CardView cardView=view.findViewById(R.id.all);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(select_all.isChecked()){


                        All_students=false;
                        select_all.setChecked(false);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {

                        All_students=true;
                        select_all.setChecked(true);
                        recyclerView.setVisibility(View.GONE);
                    }


                }
            });
            select_all=view.findViewById(R.id.select_all);
            select_all.setClickable(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            RecycleAdapter recycleAdapter=new RecycleAdapter(student_infos);
            recyclerView.setAdapter(recycleAdapter);
            builder.setView(view);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.cancel();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.cancel();
                }
            });
            dialog=builder.show();

        }
        else {

            showFailMessage();
        }



    }

    public void showFailMessage(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Assign_HomeTask.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_box,null);
        builder.setView(view);
        TextView title=view.findViewById(R.id.title);
        TextView message=view.findViewById(R.id.message);
        Button ok=view.findViewById(R.id.yes);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        title.setText("There is no student");
        message.setText("Please add new student");
        alertDialog=builder.show();
    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Student> classinfos;
        public RecycleAdapter(ArrayList<Student> classinfos){
            this.classinfos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            TextView student_name,ID,roll;
            CircleImageView priofile;
            CheckBox checkBox;
            LinearLayout linearLayout;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                student_name=mView.findViewById(R.id.name);
                ID=mView.findViewById(R.id.id);
                roll=mView.findViewById(R.id.roll);
                checkBox=mView.findViewById(R.id.choose_subject);
                priofile=mView.findViewById(R.id.profile);
                linearLayout=mView.findViewById(R.id.view_profile);




            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_rec_list_item2,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final  ViewAdapter holder, final int position) {


            final Student memberInfo=(Student) classinfos.get(position);
            holder.student_name.setText(memberInfo.name);
            holder.ID.setText(memberInfo.id);
            if(memberInfo.roll!=null&&!memberInfo.roll.equalsIgnoreCase("null")) holder.roll.setText(memberInfo.roll);
            if(memberInfo.image_path!=null&&!memberInfo.image_path.equalsIgnoreCase("null"))  Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.profile2).into(holder.priofile);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if( holder.checkBox.isChecked()){


                        student_ids.remove(memberInfo.id);
                        device_ids.remove(memberInfo.device_id);
                        holder.checkBox.setChecked(false);


                    }
                    else {


                        student_ids.add(memberInfo.id);
                        device_ids.add(memberInfo.device_id);
                        holder.checkBox.setChecked(true);

                    }
                    All_students=false;
                }
            });
            holder.checkBox.setClickable(false);



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public void chooseAllStudent(){

        student_ids.clear();
        device_ids.clear();
        for(Student student:student_infos){

            student_ids.add(student.id);
            device_ids.add(student.device_id);
        }



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

                Subject member=(Subject) info.get(i);
                names.setText(member.subject_name);
                circleImageView.setImageResource(R.drawable.subject3);
            }
            else if(flag==2){

                Student member=(Student) info.get(i);
                names.setText(member.name);
                circleImageView.setImageResource(R.drawable.profile2);
            }


            return view;
        }
    }

}
