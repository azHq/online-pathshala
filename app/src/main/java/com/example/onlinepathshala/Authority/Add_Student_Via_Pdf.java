package com.example.onlinepathshala.Authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Admin.Member;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Teacher.CSV_File_Reader;
import com.example.onlinepathshala.Teacher.Marks;
import com.example.onlinepathshala.Teacher.Result_Header_Constant;
import com.example.onlinepathshala.VolleySingleton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add_Student_Via_Pdf extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Student_Item_Class> student_info=new ArrayList<>();
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    String path,class_id,section_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__student__via__pdf);

        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        student_info=Add_New_Student.student_info;
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new RecycleAdapter2(student_info));
        progressDialog=new ProgressDialog(Add_Student_Via_Pdf.this);


    }

    public void submit(View view){


        JSONArray marks=new JSONArray();
        boolean uploadable=false;
        for(Student_Item_Class mk:student_info){

            if(mk.student_id.length()>0&&mk.student_name.length()>0&&mk.email.length()>0&&mk.phone_number.length()>0&&phone_number_validation(mk.phone_number)&&emailValidation(mk.email)){

                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("student_id",mk.student_id);
                jsonObject.addProperty("student_name",mk.student_name);
                jsonObject.addProperty("email",mk.email);
                jsonObject.addProperty("phone_number",mk.phone_number);
                marks.put(jsonObject);
                uploadable=true;
            }
            else{

                uploadable=false;
                break;
            }

        }
        if(uploadable){

                add_student(marks);

        }
        else{
            error();
        }



    }

    public void error(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Add_Student_Via_Pdf.this);
        builder.setMessage("Please Re-check Your File.Here Occurs Some Miss Match..!.For This Reason Fail To Upload File");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    public boolean phone_number_validation( String phone_number){

        phone_number=phone_number.replace("-","");
        phone_number=phone_number.replaceAll("\\s","");
        String pattern="(^([+]{1}[8]{2}|88)?(01){1}[2-9]{1}\\d{8})$";

        return phone_number.matches(pattern);
    }
    public boolean emailValidation(String email){

        Pattern pattern;
        Matcher matcher;
        String regex = "^(.+)@(.+)$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void add_student(JSONArray students){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_student_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();


                        if (response.contains("success")) {
                            student_info.clear();
                            recyclerView.setAdapter(new RecycleAdapter2(student_info));
                            showSuccessMessage();


                        } else {

                            showFailMessage(response.toString());
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
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("student_info",students.toString());
                params.put("table", "Student");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void showFailMessage(String str){

        AlertDialog.Builder builder=new AlertDialog.Builder(Add_Student_Via_Pdf.this);
        builder.setTitle("Fail to Add Some Students");
        builder.setMessage(str +" Student ID's Already Exist.Other Students Added Successfully.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                finish();

            }
        });
        builder.show();
    }

    public void showSuccessMessage(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Add_Student_Via_Pdf.this);
        builder.setTitle("Student Added");
        builder.setMessage("Student Added Uploaded Successfully");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();
                finish();

            }
        });
        builder.show();
    }
    public class RecycleAdapter2 extends RecyclerView.Adapter<RecycleAdapter2.ViewAdapter>{

        ArrayList<Student_Item_Class> memberInfos;
        public RecycleAdapter2(ArrayList<Student_Item_Class> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            EditText et_student_id,et_student_name,et_email,et_phone_number;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                et_student_id=mView.findViewById(R.id.student_id);
                et_student_name=mView.findViewById(R.id.student_name);
                et_email=mView.findViewById(R.id.email);
                et_phone_number=mView.findViewById(R.id.phone_number);

               et_student_id.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Student_Item_Class memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.student_id=editable.toString();

                    }
                });

               et_student_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Student_Item_Class memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.student_name=editable.toString();

                    }
                });

                et_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Student_Item_Class memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.email=editable.toString();


                    }
                });

                et_phone_number.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Student_Item_Class memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.phone_number=editable.toString();

                    }
                });





            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_add_rec_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewAdapter holder, final int position) {


            final Student_Item_Class memberInfo=memberInfos.get(position);
            if(memberInfo.student_id!=null){

                holder.et_student_id.setText(memberInfo.student_id);
            }
            if(memberInfo.student_name!=null){

                holder.et_student_name.setText(memberInfo.student_name);
            }
            if(memberInfo.email!=null){

                holder.et_email.setText(memberInfo.email);
            }
            if(memberInfo.phone_number!=null){

                holder.et_phone_number.setText(memberInfo.phone_number);
            }








        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }





    }

}
