package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.example.onlinepathshala.All_Dialog_Message;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student_Pdf_Header_Constant;
import com.example.onlinepathshala.Teacher.CSV_File_Reader;
import com.example.onlinepathshala.Teacher.Marks;
import com.example.onlinepathshala.Teacher.Result_Header_Constant;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_New_Student extends AppCompatActivity {

    static ArrayList<Student_Item_Class> student_info=new ArrayList<>();
    EditText et_teacher_name,et_email,et_phone_number,et_student_id;
    TextView error;
    Spinner sp_class,sp_subject;
    ArrayList<Classes> class_info=new ArrayList<>();
    String[] subjects={"Choose Subject","sub-1","sub-2","sub-3","sub-4"};
    String student_name,email="",phone_number="",class_id="",class_name="",student_id="",section_id="";
    ProgressDialog progressDialog;
    Button upload_pdf,previous;
    AlertDialog alertDialog;
    final int FILE_CHOOSING_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__student);
        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        progressDialog=new ProgressDialog(Add_New_Student.this);
        et_teacher_name=findViewById(R.id.school);
        et_email=findViewById(R.id.email);
        et_phone_number=findViewById(R.id.phone);
        et_student_id=findViewById(R.id.id);
        error=findViewById(R.id.error);
        upload_pdf=findViewById(R.id.upload_pdf);
        previous=findViewById(R.id.previous);

        upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                showHeaderInfo();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tnt=new Intent(getApplicationContext(),All_UnAssign_Student.class);
                tnt.putExtra("class_id",class_id);
                tnt.putExtra("section_id",section_id);
                startActivity(tnt);
            }
        });

    }

    public void showDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Add_New_Student.this);
        builder.setTitle("On Development");
        builder.setMessage("Student Will Add From Existing Databases");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();

    }



    public boolean emailValidation(String email){

        Pattern pattern;
        Matcher matcher;
        String regex = "^(.+)@(.+)$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(email);
        return matcher.matches();
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


    public String remove_country_code(String phone_number){
        phone_number=phone_number.trim().replace("+","");
        if(phone_number.startsWith("88")){

            phone_number=phone_number.substring(2,phone_number.length()-1);
        }

        return phone_number;
    }
    public void onSubmit(View view){

        student_name=et_teacher_name.getText().toString();
        email=et_email.getText().toString();
        phone_number=et_phone_number.getText().toString();
        student_id=et_student_id.getText().toString();
        phone_number=remove_country_code(  phone_number);
        student_id=student_id.replaceAll("\\s","");
        email=email.replaceAll("\\s","");
        if(student_name!=null&&student_name.length()>=3&&emailValidation(email)&&phone_number_validation(phone_number)&&student_id.length()>0&&student_id.length()<=10){

            add_new_user();
        }
        else{


            show_message(set_error_message(student_name,email,phone_number));
        }

    }

    public boolean phone_number_validation( String phone_number){

        phone_number=phone_number.replace("-","");
        phone_number=phone_number.replaceAll("\\s","");
        String pattern="(^([+]{1}[8]{2}|88)?(01){1}[2-9]{1}\\d{8})$";

        return phone_number.matches(pattern);
    }

    public String set_error_message(String student_name,String email,String phone_number){

        if(student_name.length()<3){

            return "Student name length must be 3 charecter";
        }
        else if(!emailValidation(email)) return "please enter valid email address";
        else if(!phone_number_validation(phone_number)) return "please enter valid phone number";
        else if(student_id.length()<1||student_id.length()>10) return "please enter valid student id";


        return "Please Fillup The Form With Correct Info";
    }


    public void add_new_user(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_new_teacher_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            student_name="";
                            student_id="";
                            et_student_id.setText("");
                            et_teacher_name.setText("");
                            et_email.setText("");
                            et_phone_number.setText("");
                            show_success_message();


                        } else {
                            showFailMessage(response.toString());
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
                params.put("student_id", student_id);
                params.put("student_name", student_name);
                params.put("email", email);
                params.put("phone", phone_number);
                params.put("section_id", section_id);
                params.put("class_id", class_id);
                params.put("table", "Student");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void showFailMessage(String str){

        AlertDialog.Builder builder=new AlertDialog.Builder(Add_New_Student.this);
        builder.setTitle("Fail to Add Some Students");
        builder.setMessage(str +" Student ID's Already Exist.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();


            }
        });
        builder.show();
    }

    public void show_message(String message){

        AlertDialog.Builder builder=new AlertDialog.Builder(Add_New_Student.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void showHeaderInfo(){


        AlertDialog.Builder builder=new AlertDialog.Builder(Add_New_Student.this);
        View view=getLayoutInflater().inflate(R.layout.student_add_pdf_header,null);
        builder.setView(view);
        Button ok=view.findViewById(R.id.ok);
        Button cancel=view.findViewById(R.id.cancel);
        TextView error=view.findViewById(R.id.error);
        error.setVisibility(View.GONE);
        TextView student_id=view.findViewById(R.id.student_id);
        student_id.setText("1."+ Student_Pdf_Header_Constant.id);
        TextView student_name=view.findViewById(R.id.student_name);
        student_name.setText("2."+Student_Pdf_Header_Constant.name);
        TextView subject_name=view.findViewById(R.id.email);
        subject_name.setText("3."+Student_Pdf_Header_Constant.email);
        TextView total_marks=view.findViewById(R.id.phone_number);
        total_marks.setText("4."+Student_Pdf_Header_Constant.phone_number);
        alertDialog=builder.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                choose_file();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


    public void choose_file(){


        String[] mimeTypes = {"text/csv"};
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("text/*");
        startActivityForResult(galleryIntent,FILE_CHOOSING_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_CHOOSING_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            Uri uri=data.getData();

            if(uri.getPath().contains(".csv")){
                InputStream path= null;
                try {
                    path = getContentResolver().openInputStream(uri);
                    CSV_File_Reader2 file_type=new CSV_File_Reader2();
                    ArrayList<String[]> marks=file_type.readfile(path,getApplicationContext());
                    String[] str2=marks.get(0);
                    marks.remove(0);
                   if(headerCheck(str2)){
                        student_info.clear();
                        boolean uploadable=true;
                        for(String[] str:marks){

                            if(str.length==4&&phone_number_validation(str[3])&&emailValidation(str[2])){

                                str[0]=str[0].replaceAll("\\s","");
                                str[2]=str[2].replaceAll("\\s","");
                                str[3]=str[3].replaceAll("\\s","");
                                student_info.add(new Student_Item_Class(str[0],str[1],str[2],str[3]));
                            }
                            else{

                                uploadable=false;
                                error();
                            }

                        }

                        if(uploadable&&student_info.size()>0){

                            Intent tnt=new Intent(Add_New_Student.this,Add_Student_Via_Pdf.class);
                            tnt.putExtra("class_id",class_id);
                            tnt.putExtra("section_id",section_id);
                            startActivity(tnt);
                        }
                    }
                   else{

                       showErrorMessage();
                   }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }



        }




    }

    public void change(){

    }

    public void error(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Add_New_Student.this);
        builder.setMessage("Please Re-check Your File.Here Occurs Some Miss Match..!.For This Reason Fail To Upload File");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public boolean headerCheck(String[] str){

        if(str.length==4){

            if(!str[0].contains(Student_Pdf_Header_Constant.id.toLowerCase())){

                return false;
            }
            if(!str[1].contains(Student_Pdf_Header_Constant.name.toLowerCase())){

                return false;
            }
            if(!str[2].contains(Student_Pdf_Header_Constant.email.toLowerCase())){

                return false;
            }
            if(!str[3].contains(Student_Pdf_Header_Constant.phone_number.toLowerCase())){

                return false;
            }
        }
        else{

            return false;
        }

        return true;
    }

    public void showErrorMessage(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Add_New_Student.this);
        View view=getLayoutInflater().inflate(R.layout.student_add_pdf_header,null);
        builder.setView(view);
        Button ok=view.findViewById(R.id.ok);
        Button cancel=view.findViewById(R.id.cancel);
        TextView error=view.findViewById(R.id.error);
        error.setText("Error In  Header");
        TextView student_id=view.findViewById(R.id.student_id);
        student_id.setText("1."+ Student_Pdf_Header_Constant.id);
        TextView student_name=view.findViewById(R.id.student_name);
        student_name.setText("2."+Student_Pdf_Header_Constant.name);
        TextView subject_name=view.findViewById(R.id.email);
        subject_name.setText("3."+Student_Pdf_Header_Constant.email);
        TextView total_marks=view.findViewById(R.id.phone_number);
        total_marks.setText("4."+Student_Pdf_Header_Constant.phone_number);
        alertDialog=builder.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    public void show_success_message(){

        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.success_message,null);
        All_Dialog_Message all_dialog_message=new All_Dialog_Message(Add_New_Student.this);
        all_dialog_message.show_success_message("Success","Student Added Successfully",view);
    }
}
