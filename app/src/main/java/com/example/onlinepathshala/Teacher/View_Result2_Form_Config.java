package com.example.onlinepathshala.Teacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.All_Student_In_A_Section_For_Result;
import com.example.onlinepathshala.Authority.Classes;
import com.example.onlinepathshala.Authority.Exam_Wise_Result;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Authority.Subject;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Exam;
import com.example.onlinepathshala.Final_Result_View_For_All;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Section;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class View_Result2_Form_Config extends AppCompatActivity {

    public NumberPicker nb_month,nb_day,nb_year;
    Spinner sp_class,sp_exam_type,sp_exam_name,sp_student,sp_medium,sp_section,sp_search_type;
    String[] months=new String[12];
    String[] days=new String[31];
    AlertDialog dialog;
    int month,day,year;
    String class_name="",section_name="",section_id="",class_id="",exam_id="",date,teacher_id,subject_name,subject_id,time,meridiem,exam_name="";
    ProgressDialog progressDialog;
    ArrayList<Object> exam_infos=new ArrayList<>();
    ArrayList<Object> medium=new ArrayList<>();
    ArrayList<Object> class_infos=new ArrayList<>();
    public ArrayList<Student> student_infos=new ArrayList<>();
    public ArrayList<Object> search_types=new ArrayList<>();
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
    String search_option="Choose Search Option";
    Button submit;
    RelativeLayout exam_type_layout,exam_name_layout,class_name_layout,medium_layout,section_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__result2);

        exam_name_layout=findViewById(R.id.exam_name_layout);
        exam_type_layout=findViewById(R.id.exam_type_layout);
        class_name_layout=findViewById(R.id.class_name_layout);
        medium_layout=findViewById(R.id.medium_layout);
        section_layout=findViewById(R.id.section_layout);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((exam_type!=null&&!exam_type.equalsIgnoreCase("Choose Exam Type")&&!exam_type.equalsIgnoreCase("Final Result")&&exam_name!=null&&!exam_name.equalsIgnoreCase("Choose Exam Name")&&search_option!=null&&search_option.equalsIgnoreCase("Exam Wise")&&class_name!=null&&!class_name.equalsIgnoreCase("Choose Class")&&medium_name!=null&&!medium_name.equalsIgnoreCase("Choose Medium")&&section_name!=null&&!section_name.equalsIgnoreCase("Choose Section"))||(class_name!=null&&!class_name.equalsIgnoreCase("Choose Class")&&medium_name!=null&&!medium_name.equalsIgnoreCase("Choose Medium")&&section_name!=null&&!section_name.equalsIgnoreCase("Choose Section")&&search_option!=null&&search_option.equalsIgnoreCase("Student Wise")))
                {

                    if(search_option.equalsIgnoreCase("Exam Wise")){



                        Intent tnt=new Intent(getApplicationContext(),Exam_Wise_Result.class);
                        tnt.putExtra("exam_id",exam_id);
                        tnt.putExtra("exam_type",exam_type);
                        tnt.putExtra("section_id",section_id);
                        tnt.putExtra("class_id",class_id);
                        startActivity(tnt);
                    }
                    else{

                        Intent tnt=new Intent(getApplicationContext(), All_Student_In_A_Section_For_Result.class);
                        tnt.putExtra("section_id",section_id);
                        tnt.putExtra("class_id",class_id);
                        startActivity(tnt);

                    }
                }
                else if(exam_type!=null&&!exam_type.equalsIgnoreCase("Choose Exam Type")&&exam_type.equalsIgnoreCase("Final Result")&&search_option!=null&&search_option.equalsIgnoreCase("Exam Wise")&&(class_name!=null&&!class_name.equalsIgnoreCase("Choose Class")&&medium_name!=null&&!medium_name.equalsIgnoreCase("Choose Medium")&&section_name!=null&&!section_name.equalsIgnoreCase("Choose Section"))){

                    Intent tnt=new Intent(getApplicationContext(), Final_Result_View_For_All.class);
                    tnt.putExtra("exam_id",exam_id);
                    tnt.putExtra("exam_type",exam_type);
                    tnt.putExtra("section_id",section_id);
                    tnt.putExtra("class_id",class_id);
                    startActivity(tnt);
                }
                else{

                    if(exam_type!=null&&exam_type.equalsIgnoreCase("Choose Exam Type")){
                        showMessage("Error In Data","Please Choose Class Type");
                    }

                    else if(search_option!=null&&search_option.equalsIgnoreCase("Choose Search Option")){

                        showMessage("Error In Data","Please Choose Search Option");
                    }
                    else if(class_name!=null&&class_name.equalsIgnoreCase("Choose Class")){

                        showMessage("Error In Data","Please Choose Class Name");
                    }
                    else if(medium_name!=null&&medium_name.equalsIgnoreCase("Choose Medium")){

                        showMessage("Error In Data","Please Choose Medium");
                    }
                    else if(section_name!=null&&section_name.equalsIgnoreCase("Choose Section")){
                        showMessage("Error In Data","Please Choose Section");
                    }
                    else if(exam_name!=null&&exam_name.equalsIgnoreCase("Choose Exam Name")){

                        showMessage("Error In Data","Please Choose Exam Name");
                    }
                    else{

                        showMessage("Error In Data","Please Fill Up All Required Data");
                    }
                }
            }
        });
        choose_list=new ArrayList<>();
        student_infos.clear();
        selected_student_infos.clear();
        progressDialog=new ProgressDialog(getApplicationContext());
        setDisplayValue();
    }

    public void showMessage(String title,String message){

        AlertDialog.Builder builder=new AlertDialog.Builder(View_Result2_Form_Config.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void setDisplayValue(){



        sp_search_type=findViewById(R.id.search_type);
        sp_class=findViewById(R.id.class_name);
        sp_exam_type=findViewById(R.id.exam_type);
        sp_section=findViewById(R.id.section);
        sp_exam_name=findViewById(R.id.exam_name);

        exam_types.add("Choose Exam Type");
        exam_types.add("Class Test");
        exam_types.add("Monthly Test");
        exam_types.add("Half Yearly");
        exam_types.add("Final Exam");
        exam_types.add("Final Result");

        search_types.add("Choose Search Option");
        search_types.add("Exam Wise");
        search_types.add("Student Wise");
        sp_search_type.setAdapter(new CustomAdapter(getApplicationContext(),2,search_types));
        sp_search_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                search_option=(String)search_types.get(i);
                int li=adapterView.getChildCount();
                if(li>0) {
                    View linearLayout=adapterView.getChildAt(0);
                    LinearLayout linearLayout1=linearLayout.findViewById(R.id.linear1);
                    LinearLayout linearLayout2=(LinearLayout) linearLayout1.getChildAt(0);
                    TextView textView=(TextView) linearLayout2.getChildAt(1);
                    textView.setTextColor(Color.WHITE);
                }
                if(search_option.equalsIgnoreCase("Exam Wise")){

                    exam_name_layout.setVisibility(View.VISIBLE);
                    exam_type_layout.setVisibility(View.VISIBLE);
                }
                else{

                    exam_name_layout.setVisibility(View.GONE);
                    exam_type_layout.setVisibility(View.GONE);
                    medium_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                getAllData("Section", Constant_URL.get_all_section_info_url,class_id,section_id,medium_name);

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
                sp_exam_type.setAdapter(new CustomAdapter(getApplicationContext(),2,exam_types));
                get_all_students("Student", Constant_URL.get_all_student_info_url,class_id,section_id,medium_name);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                if(exam_type.equalsIgnoreCase("Final Result")){

                    exam_name_layout.setVisibility(View.GONE);
                }
                else{

                    exam_name_layout.setVisibility(View.VISIBLE);
                }
                get_all_exams("Exam", Constant_URL.get_type_wise_exam,class_id,section_id,exam_type);
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
                   if(i<examinfos2.size())
                   {
                       Exam exam=(Exam)examinfos2.get(i);
                       exam_name=exam.exam_name;
                       exam_id=exam.id;
                   }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                medium_name=(String) medium.get(i);
                getAllData("Class_Table", Constant_URL.get_all_class_info_url,class_id,section_id,medium_name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }
    public void choose_mediumwise_class(String medium){

        new_classes_info.clear();
        new_classes_info.add(new Classes("0","Choose Class",null,null,null,""));
        for(Object classes:class_infos){

            Classes class_info=(Classes)classes;
            if(class_info.medium.equalsIgnoreCase(medium)){
                new_classes_info.add(class_info);
            }

        }
        sp_class.setAdapter(new CustomAdapter(getApplicationContext(),1,new_classes_info));
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

            return view;
        }
    }

    public void getAllData(final String table_name,String url,String class_id,String section_id,String medium){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

        if(table_name.equalsIgnoreCase("Class_Table")){
            postparams.put(medium);
        }
        else {

            postparams.put(class_id);
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



}
