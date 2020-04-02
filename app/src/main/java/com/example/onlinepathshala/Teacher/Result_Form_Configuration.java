package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Authority.Classes;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Authority.Student_Fees;
import com.example.onlinepathshala.Authority.Subject;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Exam;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Section;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.shawnlin.numberpicker.NumberPicker;
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

public class Result_Form_Configuration extends AppCompatActivity {

    AlertDialog alertDialog;
    public NumberPicker nb_month,nb_day,nb_year;
    Spinner sp_class,sp_exam_type,sp_exam_name,sp_subject,sp_medium,sp_section;
    String[] months=new String[12];
    String[] days=new String[31];
    int month,day,year;
    String class_name,section_name,section_id,class_id,exam_id,date,teacher_id,subject_name,subject_id,time,meridiem,exam_name;
    ProgressDialog progressDialog;
    ArrayList<Object> exam_infos=new ArrayList<>();
    ArrayList<Object> subject_list=new ArrayList<>();
    ArrayList<Object> medium=new ArrayList<>();
    ArrayList<Object> class_infos=new ArrayList<>();
    public ArrayList<Student> student_infos=new ArrayList<>();
    public static ArrayList<Student> selected_student_infos=new ArrayList<>();
    ArrayList<Object> exam_types=new ArrayList<>();
    ArrayList<String> student_ids=new ArrayList<>();
    ArrayList<Object> sections=new ArrayList<>();
    ArrayList<Object> examinfos2=new ArrayList<>();
    ArrayList<Object> new_classes_info=new ArrayList<>();
    String formated_time="";
    String medium_name="",exam_type;
    EditText et_exam_name;
    public static ArrayList<Subject> choose_list;
    Button choose_sub;
    public boolean All_students=false;
    CheckBox select_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result__form__configuration);
        choose_list=new ArrayList<>();
        selected_student_infos.clear();
        progressDialog=new ProgressDialog(Result_Form_Configuration.this);
        teacher_id=SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
        setDisplayValue();

    }

    public void setDisplayValue(){



        sp_class=findViewById(R.id.class_name);
        sp_section=findViewById(R.id.section);
        sp_exam_type=findViewById(R.id.exam_type);
        sp_exam_name=findViewById(R.id.exam_name);
        sp_subject=findViewById(R.id.subject_name);
        exam_types.add("Choose Exam Type");
        exam_types.add("Class Test");
        exam_types.add("Monthly Test");
        exam_types.add("Half Yearly");
        exam_types.add("Final Exam");

        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int li=parent.getChildCount();
                if(li>0) {
                    View linearLayout=parent.getChildAt(0);
                    LinearLayout linearLayout1=linearLayout.findViewById(R.id.linear1);
                    LinearLayout linearLayout2=(LinearLayout) linearLayout1.getChildAt(0);
                    TextView textView=(TextView) linearLayout2.getChildAt(1);
                    textView.setTextColor(Color.WHITE);
                }


                Classes classes=(Classes)class_infos.get(position);
                class_name=classes.class_name;
                class_id=classes.id;
                sp_exam_type.setAdapter(new CustomAdapter(getApplicationContext(),2,exam_types));
                getAllData("Section", Constant_URL.all_section_for_result,class_id,section_id,medium_name);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int li=parent.getChildCount();
                if(li>0) {
                    View linearLayout=parent.getChildAt(0);
                    LinearLayout linearLayout1=linearLayout.findViewById(R.id.linear1);
                    LinearLayout linearLayout2=(LinearLayout) linearLayout1.getChildAt(0);
                    TextView textView=(TextView) linearLayout2.getChildAt(1);
                    textView.setTextColor(Color.WHITE);
                }


                Section section=(Section)sections.get(position);
                section_name=section.section_name;
                section_id=section.section_id;
                get_all_students("Student", Constant_URL.get_all_student_info_url,class_id,section_id,medium_name);
                getAllData("Subject_Assigned_Table", Constant_URL.all_subject_for_result,class_id,section_id,medium_name);
                choose_sub.setText("Choose Student");



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subject_list.add(new Subject("0","Choose Subject","0"));
        sp_subject.setAdapter(new CustomAdapter(getApplicationContext(),5,subject_list));
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
                subject_name=((Subject)subject_list.get(i)).subject_name;
                subject_id=((Subject)subject_list.get(i)).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_exam_type.setAdapter(new CustomAdapter(getApplicationContext(),2,exam_types));
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
                if(!exam_type.equalsIgnoreCase("Final Exam")){

                    get_all_exams("Exam", Constant_URL.get_type_wise_exam,class_id,section_id,exam_type);
                }
                else{
                    examinfos2.clear();
                    examinfos2.add(new Exam("-1","final exam","",""));
                    sp_exam_name.setAdapter(new CustomAdapter(getApplicationContext(),3,examinfos2));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        sp_exam_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                int li=adapterView.getChildCount();
                if(li>0) {
                    View linearLayout=adapterView.getChildAt(0);
                    LinearLayout linearLayout1=linearLayout.findViewById(R.id.linear1);
                    LinearLayout linearLayout2=(LinearLayout) linearLayout1.getChildAt(0);
                    TextView textView=(TextView) linearLayout2.getChildAt(1);
                    textView.setTextColor(Color.WHITE);
                    Exam exam=(Exam)examinfos2.get(i);
                    exam_name=exam.exam_name;
                    if(exam_name.equalsIgnoreCase("Create New Exam")){

                        if(!section_name.equalsIgnoreCase("Choose Section")&&!class_name.equalsIgnoreCase("Choose Class")&&!exam_type.equalsIgnoreCase("Choose Exam Type")&&!medium_name.equalsIgnoreCase("Choose Medium")){

                            create_exam_dialog();
                        }
                        else{

                            if(medium_name.equalsIgnoreCase("Choose Medium")){

                                Toast.makeText(getApplicationContext(),"Please Choose Medium",Toast.LENGTH_LONG).show();
                            }
                            else if(class_name.equalsIgnoreCase("Choose Class")){

                                Toast.makeText(getApplicationContext(),"Please Choose Class",Toast.LENGTH_LONG).show();
                            }
                            else if(section_name.equalsIgnoreCase("Choose Section")){

                                Toast.makeText(getApplicationContext(),"Please Choose Section",Toast.LENGTH_LONG).show();
                            }
                            else if(exam_type.equalsIgnoreCase("Choose Exam Type")){

                                Toast.makeText(getApplicationContext(),"Please Choose Exam Type",Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                    else{

                        exam_id=exam.id;
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        choose_sub=findViewById(R.id.choose_student);
        choose_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertDialog();
            }
        });

        medium.add("Choose Medium");
        medium.add("Bangla");
        medium.add("English");

        sp_medium=findViewById(R.id.medium);
        sp_medium.setAdapter(new CustomAdapter(getApplicationContext(),2,medium));
        sp_medium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                medium_name=(String) medium.get(i);
                getAllData("Class_Table", Constant_URL.all_class_for_result,class_id,section_id,medium_name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }


    public void create_exam_dialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Result_Form_Configuration.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.create_exam_layout,null);
        builder.setView(view);
        EditText et_exam_name=view.findViewById(R.id.exam_name);
        EditText et_marks=view.findViewById(R.id.marks);
        Button ok=view.findViewById(R.id.yes);
        Button cancel=view.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                create_exam(et_exam_name.getText().toString(),et_marks.getText().toString());

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog=builder.show();
    }

    public void create_exam(String exam_name,String total_marks){

        Calendar cal=Calendar.getInstance();
        Date date1 = cal.getTime();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        date=format1.format(date1);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        time = sdf.format(date1);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.insert_create_exam_url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Exam Created Successfully", Toast.LENGTH_SHORT).show();
                            get_all_exams("Exam", Constant_URL.get_type_wise_exam,class_id,section_id,exam_type);
                        }
                        else if(response.contains("exist")){


                            sp_exam_name.setSelection(0);
                            Toast.makeText(getApplicationContext(), "Exam Name Already Exist", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            Toast.makeText(getApplicationContext(), "Fail To Create Exam", Toast.LENGTH_SHORT).show();
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
                params.put("subject_name", "");
                params.put("subject_id", "");
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("topic","");
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
                Exam classes=(Exam) info.get(i);
                names.setText(classes.exam_name);
                circleImageView.setVisibility(View.GONE);
            }
            else if(flag==4){

                Section classes=(Section) info.get(i);
                names.setText(classes.section_name);
                circleImageView.setVisibility(View.GONE);
            }
            else if(flag==5){

                Subject classes=(Subject) info.get(i);
                names.setText(classes.subject_name);
                circleImageView.setVisibility(View.GONE);
            }

            return view;
        }
    }

    public void getAllData(final String table_name,String url,String class_id,String section_id,String medium){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

        if(table_name.equalsIgnoreCase("Class_Table")){
            postparams.put(medium);
            postparams.put(teacher_id);
        }
        else if(table_name.equalsIgnoreCase("Section")){


            postparams.put(class_id);
            postparams.put(teacher_id);
        }
        else if(table_name.equalsIgnoreCase("Subject_Assigned_Table")){

            postparams.put(class_id);
            postparams.put(section_id);
            postparams.put(teacher_id);
        }

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string = response.getString(0);

                            if (table_name.equalsIgnoreCase("Class_Table")) {

                                class_infos.clear();
                                class_infos.add(new Classes("0","Choose Class","","",""));
                                if (!string.contains("no item")) {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);

                                            String id = member.getString("class_id");
                                            String name = member.getString("class_name");
                                            class_infos.add(new Classes(id, name, "", "", ""));
                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }
                                    }
                                    sp_class.setAdapter(new CustomAdapter(getApplicationContext(), 1, class_infos));
                                }
                                else {


                                    sp_class.setAdapter(new CustomAdapter(getApplicationContext(),1,class_infos));
                                }

                            }
                            else if (table_name.equalsIgnoreCase("Section")) {

                                sections.clear();
                                sections.add(new Section("0","Choose Section",""));
                                if (!string.contains("no item")) {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);

                                            String id = member.getString("id");
                                            String name = member.getString("section_name");
                                            sections.add(new Section(id, name, ""));
                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }
                                    }
                                    sp_section.setAdapter(new CustomAdapter(getApplicationContext(), 4, sections));
                                }
                                else {




                                    sp_section.setAdapter(new CustomAdapter(getApplicationContext(),4,sections));

                                }
                            }
                            else if(table_name.equalsIgnoreCase("Subject_Assigned_Table")){


                                subject_list.clear();
                                subject_list.add(new Subject("0","Choose Subject","0"));
                                if(!string.contains("no item")){

                                    for(int i=0;i<response.length();i++){

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);
                                            String id = member.getString("subject_id");
                                            String subject_name=member.getString("subject_name");
                                            subject_list.add(new Subject(id,subject_name,"0"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    sp_subject.setAdapter(new CustomAdapter(getApplicationContext(),5,subject_list));
                                }
                                else{
                                    sp_subject.setAdapter(new CustomAdapter(getApplicationContext(),5,subject_list));
                                }
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

    public void get_all_students(final String table_name,String url,String class_id,String section_id,String medium){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        student_infos.clear();
                        try {
                            string=response.getString(0);
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);





                                        String id = member.getString("id");
                                        String image_path=member.getString("image_path");
                                        String name = member.getString("student_name");
                                        student_infos.add(new Student(id,name,null,image_path,null,null));



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


    public void get_all_exams(final String table_name,String url,String class_id,String section_id,String exam_type){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);
        postparams.put(exam_type);


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        examinfos2.clear();
                        examinfos2.add(new Exam("0","Choose Exam Name","",""));
                        examinfos2.add(new Exam("0","Create New Exam","",""));
                        try {
                            string=response.getString(0);
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);



                                        String id = member.getString("id");
                                        String exam_type=member.getString("exam_type");
                                        String exam_name = member.getString("exam_name");
                                        String class_id=member.getString("class_id");
                                        examinfos2.add(new Exam(id,exam_name,exam_type,class_id));



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                sp_exam_name.setAdapter(new CustomAdapter(getApplicationContext(),3,examinfos2));

                            }
                            else{

                                sp_exam_name.setAdapter(new CustomAdapter(getApplicationContext(),3,examinfos2));

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

    public void showAlertDialog()
    {

        selected_student_infos.clear();
        AlertDialog.Builder builder=new AlertDialog.Builder(Result_Form_Configuration.this);
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

                String id_list="";
                if(!All_students){

                    for(Student student:selected_student_infos){

                        id_list+=student.id+",";
                    }
                }
                else {
                    for(Student student:student_infos){

                        id_list+=student.id+",";
                    }
                }

                choose_sub.setText(id_list);
                alertDialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_sub.setText("Choose Student");
                selected_student_infos.clear();
                alertDialog.cancel();
            }
        });
        if(student_infos.size()>0){
            alertDialog=builder.show();
        }
        else{

            Toast.makeText(getApplicationContext(),"Student List Is Empty",Toast.LENGTH_LONG).show();
        }




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


                        selected_student_infos.remove(memberInfo);
                        holder.checkBox.setChecked(false);


                    }
                    else {


                        selected_student_infos.add(memberInfo);
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

        selected_student_infos.clear();

        for(Student student:student_infos){

            selected_student_infos.add(student);
        }




    }


    public void submit(View view){


        if(All_students) chooseAllStudent();
        if(!class_name.contains("Choose Class")&&!subject_name.contains("Choose Subject")&&!medium_name.contains("Choose Medium")&&!exam_name.contains("Choose Exam Name")&&!exam_type.contains("Choose Exam Type")&&!section_name.equalsIgnoreCase("Choose Section")&&selected_student_infos.size()>0){


            Intent tnt=new Intent(getApplicationContext(),Publish_Result.class);

            tnt.putExtra("class_id",class_id);
            tnt.putExtra("exam_id",exam_id);
            tnt.putExtra("exam_type",exam_type);
            tnt.putExtra("medium",medium_name);
            tnt.putExtra("section_id",section_id);
            tnt.putExtra("subject_id",subject_id);
            tnt.putExtra("subject_name",subject_name);
            choose_sub.setText("Choose Student");
            sp_exam_name.setSelection(0);
            sp_exam_type.setSelection(0);
            sp_class.setSelection(0);
            sp_medium.setSelection(0);
            sp_subject.setSelection(0);
            startActivity(tnt);

        }
        else{


            AlertDialog.Builder builder=new AlertDialog.Builder(Result_Form_Configuration.this);
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

            return "Please Choose Class";
        }
        if(section_name.contains("Choose Section")){

            return "Please Choose Section";
        }
        if(medium_name.contains("Choose Medium")){

            return "Please Choose Medium";
        }
        if(subject_name.contains("Choose Subject")){
            return "Please Choose Subject";
        }
        if(exam_name.contains("Choose Exam Name")){

            return "Please Choose Exam Name";
        }
        if(exam_type.contains("Choose Exam Type")){

            return "Please Choose Exam Type";
        }
        else if(exam_name.length()<=1) return "please enter valid exam name";
        else if(selected_student_infos.size()<=0) return "please choose at least one Student";



        return "Please Fill up The Form With Correct Info";
    }








}
