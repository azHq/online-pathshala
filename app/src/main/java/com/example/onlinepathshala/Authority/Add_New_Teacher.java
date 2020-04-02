package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Admin.Add_School;
import com.example.onlinepathshala.Admin.Log_In;
import com.example.onlinepathshala.All_Dialog_Message;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_New_Teacher extends AppCompatActivity {

    EditText et_teacher_name,et_email,et_phone_number;
    TextView error;
    Spinner sp_class,sp_subject;
    ArrayList<Classes> class_info=new ArrayList<>();
    ArrayList<Subject> subject_info=new ArrayList<>();
    String teacher_name,email="",phone_number,class_id="",subject_id="",class_name="",subject_name="";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__teacher);

        progressDialog=new ProgressDialog(Add_New_Teacher.this);
        et_teacher_name=findViewById(R.id.school);
        et_email=findViewById(R.id.email);
        et_phone_number=findViewById(R.id.phone);
        sp_class=findViewById(R.id.class_name);
        sp_subject=findViewById(R.id.subject);
        error=findViewById(R.id.error);

        setClassList();
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if(class_info.get(position).id.equalsIgnoreCase("0")){
                    class_id="";
                    class_name="Not assigned";
                }
                else{

                    class_id=class_info.get(position).id;
                    class_name=class_info.get(position).class_name;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setAllSubjectList();
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if(subject_info.get(position).id.equalsIgnoreCase("0")){
                    subject_id="";
                    subject_name="Not assigned";
                }
                else{

                    subject_id=subject_info.get(position).id;
                    subject_name=subject_info.get(position).subject_name;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAllSubjectList() {

        JSONArray postparams = new JSONArray();
        postparams.put("Subject_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        subject_info.add(new Subject("0","Choose Subject (Optional)",null));
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_subjects_url2,postparams,
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
                                        subject_info.add(new Subject(id,subject_name,total_assigned_classes));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                sp_subject.setAdapter(new CustomAdapter2(getApplicationContext(),2,subject_info));


                            }
                            else{

                                sp_subject.setAdapter(new CustomAdapter2(getApplicationContext(),2,subject_info));

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

    public void setClassList(){

        class_info.add(new Classes("0","Choose Class (Optional)",null,null,null));
        JSONArray postparams = new JSONArray();
        postparams.put("Class_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_data_url,postparams,
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
                                        String id = member.getString("class_id");
                                        String class_name=member.getString("class_name");
                                        class_info.add(new Classes(id,class_name,"","",""));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                sp_class.setAdapter(new CustomAdapter(getApplicationContext(),1,class_info));

                            }
                            else{
                                sp_class.setAdapter(new CustomAdapter(getApplicationContext(),1,class_info));


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



    public boolean emailValidation(){

        Pattern pattern;
        Matcher matcher;
        String regex = "^(.+)@(.+)$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean phone_number_validation( String phone_number){

        phone_number=phone_number.replace("-","");
        phone_number=phone_number.replaceAll("\\s","");
        String pattern="(^([+]{1}[8]{2}|88)?(01){1}[2-9]{1}\\d{8})$";

        return phone_number.matches(pattern);
    }

    public String remove_country_code(String phone_number){
        phone_number=phone_number.trim().replace("+","");
        if(phone_number.startsWith("88")){

            phone_number=phone_number.substring(2,phone_number.length()-1);
        }

        return phone_number;
    }
    public void onSubmit(View view){

        teacher_name=et_teacher_name.getText().toString();
        email=et_email.getText().toString();
        phone_number=et_phone_number.getText().toString();
        phone_number=remove_country_code(  phone_number);

        if(teacher_name.length()>=3&&emailValidation()&&phone_number_validation(phone_number)){

            review_info(view);
        }
        else{

            error.setVisibility(View.VISIBLE);
            error.setTextColor(Color.parseColor("#E64A19"));
            error.setText(set_error_message(teacher_name,email,phone_number));
        }

    }

    public String set_error_message(String teacher_name,String email,String phone_number){

        if(teacher_name.length()<3){

            return "Teacher name length must be 3 charecter";
        }
        else if(!emailValidation()) return "please enter valid email address";
        else if(!phone_number_validation(phone_number)) return "please enter valid phone number";



        return "Please Fillup The Form With Correct Info";
    }

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Classes> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<Classes> info) {
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
            view = inflter.inflate(R.layout.school_list_item, null);
            CircleImageView circleImageView=view.findViewById(R.id.image);
            final TextView names = (TextView) view.findViewById(R.id.teacher_name);

            Classes school=(Classes) info.get(i);
            names.setText(school.class_name);
            circleImageView.setImageResource(R.drawable.ic_school_black_24dp);

            return view;
        }
    }
    public static class CustomAdapter2 extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Subject> info;
        LayoutInflater inflter;

        public CustomAdapter2(Context applicationContext,int flag,ArrayList<Subject> info) {
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
            view = inflter.inflate(R.layout.school_list_item, null);
            CircleImageView circleImageView=view.findViewById(R.id.image);
            final TextView names = (TextView) view.findViewById(R.id.teacher_name);

                Subject school=(Subject) info.get(i);
                names.setText(school.subject_name);
                circleImageView.setImageResource(R.drawable.subject3);

            return view;
        }
    }



    public void review_info(View v){


        AlertDialog.Builder alert=new AlertDialog.Builder(Add_New_Teacher.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        ViewGroup viewGroup = findViewById(android.R.id.content);
        if( inflater!=null){


            View view =inflater.inflate(R.layout.adding_teacher_info_review,viewGroup,false);
            alert.setView(view);
            final AlertDialog alertDialog=alert.show();
            TextView school=view.findViewById(R.id.school_name);
            TextView auth=view.findViewById(R.id.authority);
            TextView type=view.findViewById(R.id.authority_type);
            TextView email2=view.findViewById(R.id.email);
            TextView phone=view.findViewById(R.id.phone);
            school.setText(teacher_name);
            auth.setText(email);
            type.setText(phone_number);
            email2.setText(class_name);
            phone.setText(subject_name);
            Button confirm=view.findViewById(R.id.confirm);
            Button cancel=view.findViewById(R.id.cancel);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    alertDialog.cancel();
                    error.setText("Add New Teacher");
                    error.setTextColor(Color.WHITE);
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
        else {

            add_new_user();
        }



    }
    public void add_new_user(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_new_teacher_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            teacher_name="";
                            et_teacher_name.setText("");
                            et_email.setText("");
                            et_phone_number.setText("");

                            show_success_message();


                        } else {
                            Toast.makeText(getApplicationContext(), "Fail To Add New Teacher", Toast.LENGTH_SHORT).show();
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
                params.put("teacher_name", teacher_name);
                params.put("email", email);
                params.put("phone", phone_number);
                params.put("class_id", class_id);
                params.put("subject_id", subject_id);
                params.put("table", "Teacher");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void show_success_message(){

        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.success_message,null);
        All_Dialog_Message all_dialog_message=new All_Dialog_Message(Add_New_Teacher.this);
        all_dialog_message.show_success_message("Success","Teacher Added Successfully",view);
    }


}
