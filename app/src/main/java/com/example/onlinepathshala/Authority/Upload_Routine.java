package com.example.onlinepathshala.Authority;

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
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Exam;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Section;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Upload_Routine extends AppCompatActivity {

    String routine_type="",file_type="",file="";
    Spinner sp_routine_type;
    ArrayList<String> routine_types=new ArrayList<>();
    ProgressDialog progressDialog;
    final int FILE_CHOOSING_REQUEST_CODE=1;
    Spinner sp_class,sp_exam_type,sp_exam_name,sp_student,sp_medium,sp_section;
    String[] months=new String[12];
    String[] days=new String[31];
    AlertDialog dialog;
    int month,day,year;
    String class_name="",section_name="",section_id="",class_id="",exam_id,date,teacher_id,subject_name,subject_id,time,meridiem,exam_name;
    ArrayList<Object> exam_infos=new ArrayList<>();
    ArrayList<Object> medium=new ArrayList<>();
    ArrayList<Object> classes=new ArrayList<>();
    public ArrayList<Student> student_infos=new ArrayList<>();
    public static ArrayList<Student> selected_student_infos=new ArrayList<>();
    ArrayList<Object> exam_types=new ArrayList<>();
    ArrayList<String> student_ids=new ArrayList<>();
    ArrayList<Object> sections=new ArrayList<>();
    ArrayList<Object> examinfos2=new ArrayList<>();
    ArrayList<Object> new_classes_info=new ArrayList<>();
    String formated_time="";
    String medium_name="",exam_type="";
    EditText et_exam_name;
    public static ArrayList<Subject> choose_list;
    Button file_chooser;
    Button choose_sub;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__routine);

        file_chooser=findViewById(R.id.file_chooser);
        relativeLayout=findViewById(R.id.exam_type_layout);
        choose_list=new ArrayList<>();
        student_infos.clear();
        selected_student_infos.clear();
        setDisplayValue();
        progressDialog=new ProgressDialog(Upload_Routine.this);
        progressDialog.setMessage("Please Wait");
        sp_routine_type=findViewById(R.id.routine_type);
        routine_types.add("Choose Routine Type");
        routine_types.add("Class Routine");
        routine_types.add("Exam Routine");
        relativeLayout.setVisibility(View.GONE);
        sp_routine_type.setAdapter(new CustomAdapter2(getApplicationContext(),1,routine_types));
        sp_routine_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

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


                routine_type=routine_types.get(position);

                if(routine_type.equalsIgnoreCase("Class Routine")){

                    relativeLayout.setVisibility(View.GONE);

                }
                else if(routine_type.equalsIgnoreCase("Exam Routine")){

                    relativeLayout.setVisibility(View.VISIBLE);
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void choose_file(View view){

        file_chooser();
    }

    public void file_chooser(){


        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*|application/pdf");
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(galleryIntent,FILE_CHOOSING_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_CHOOSING_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            Uri uri=data.getData();
            if(uri.getPath().contains(".pdf")) file_type="pdf";
            else file_type="image";
            byte[] bytes=convertUriToByte(data.getData());
            file=getStringImage(bytes);
            file_chooser.setText(uri.getPath());
        }




    }

    public byte[] convertUriToByte(Uri uri){

        InputStream inputStream = null;
        ByteArrayOutputStream byteBuffer=null;

        try {
            inputStream = getContentResolver().openInputStream(uri);
            byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while (true) {

                if (!((len = inputStream.read(buffer)) != -1)) break;


                byteBuffer.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){

        }



        return byteBuffer.toByteArray();

    }

    public String getStringImage(byte[] imageBytes) {

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public void submit(View view){

        if(!medium_name.equalsIgnoreCase("Choose Medium")&&!class_name.equalsIgnoreCase("Choose Class")&&!section_name.equalsIgnoreCase("Choose Section")&&!routine_type.equalsIgnoreCase("Choose Routine Type")&&file.length()>=1&&!class_name.equalsIgnoreCase("No Class")&&!section_name.equalsIgnoreCase("No Section")){

            upload_routine();
        }
        else{

            AlertDialog.Builder builder=new AlertDialog.Builder(Upload_Routine.this);
            builder.setMessage(set_error_message());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });
            builder.show();

        }
    }


    public void upload_routine(){


        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.upload_routine_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            sp_exam_type.setSelection(0);
                            sp_class.setSelection(0);
                            sp_routine_type.setSelection(0);
                            sp_section.setSelection(0);
                            sp_medium.setSelection(0);
                            Toast.makeText(getApplicationContext(),"Notice Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            finish();


                        } else {
                            System.out.println(response.toString());
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
                params.put("routine_type",routine_type);
                params.put("class_id", class_id);
                params.put("exam_type", exam_type);
                params.put("medium", medium_name);
                params.put("section_id",section_id);
                params.put("id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("file",file);
                params.put("file_type",file_type);
                params.put("table", "Routine");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                params.put("path",Constant_URL.base_url);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void setDisplayValue(){



        medium.add("Choose Medium");
        medium.add("Bangla");
        medium.add("English");

        sp_medium=findViewById(R.id.medium);
        sp_class=findViewById(R.id.class_name);
        sp_section=findViewById(R.id.section);
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
                getAllData("Class_Table",Constant_URL.get_all_class_info_url,"","",medium_name);
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


                Classes my_class=(Classes)classes.get(position);
                class_name=my_class.class_name;
                class_id=my_class.id;
                getAllData("Section",Constant_URL.get_all_section_info_url,class_id,"",medium_name);

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


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_exam_type=findViewById(R.id.exam_type);
        exam_types.add("Choose Exam Type");
        exam_types.add("Class Test");
        exam_types.add("Monthly Test");
        exam_types.add("Half Yearly");
        exam_types.add("Final");
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







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

                                classes.clear();

                                classes.add(new Classes("", "Choose Class", "", "", ""));
                                if (!string.contains("no item")) {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);

                                            String id = member.getString("class_id");
                                            String name = member.getString("class_name");
                                            classes.add(new Classes(id, name, "", "", ""));
                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }
                                    }
                                    sp_class.setAdapter(new CustomAdapter(getApplicationContext(), 1, classes));
                                }
                                else {

                                    classes.add(new Classes("0","No Class","","",""));
                                    sp_class.setAdapter(new CustomAdapter(getApplicationContext(),1,classes));
                                }
                            }
                            else if (table_name.equalsIgnoreCase("Section")) {

                                sections.clear();
                                sections.add(new Section("0","Choose Section","0"));
                                if (!string.contains("no item")) {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);

                                            String id = member.getString("id");
                                            String name = member.getString("section_name");
                                            sections.add(new Section(id, name,""));
                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }
                                    }
                                    sp_section.setAdapter(new CustomAdapter(getApplicationContext(), 4, sections));
                                }
                                else {



                                    sections.add(new Section("0","No Section",""));
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

//    public void getAllMemberData(final String table_name,String url){
//
//
//        JSONArray postparams = new JSONArray();
//        postparams.put(table_name);
//        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
//
//
//        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
//                url,postparams,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                        String string;
//                        try {
//                            string=response.getString(0);
//                            if(!string.contains("no item")){
//
//                                for(int i=0;i<response.length();i++){
//
//                                    JSONObject member = null;
//                                    try {
//                                        member = response.getJSONObject(i);
//
//                                        if(table_name.equalsIgnoreCase("Class_Table")){
//                                            String id = member.getString("class_id");
//                                            String name = member.getString("class_name");
//                                            class_infos.add(new Classes(id,name,null,null,null,"Bangla"));
//                                            sp_class.setAdapter(new CustomAdapter(getApplicationContext(),1,class_infos));
//                                        }
//                                        else if(table_name.equalsIgnoreCase("Student")){
//
//
//
//                                            String id = member.getString("id");
//                                            String image_path=member.getString("image_path");
//                                            String name = member.getString("student_name");
//                                            student_infos.add(new Student(id,name,null,image_path,null,null));
//                                        }
//                                        else if(table_name.equalsIgnoreCase("Exam")){
//
//                                            String id = member.getString("id");
//                                            String exam_type=member.getString("exam_type");
//                                            String exam_name = member.getString("exam_name");
//                                            String class_id=member.getString("class_id");
//                                            exam_infos.add(new Exam(id,exam_name,exam_type,class_id));
//                                        }
//
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//
//
//                            }
//                            else{
//
//
//
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//
//                        }
//
//                    }
//
//
//                },
//                new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError error){
//
//
//                    }
//                })
//
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("User-agent", System.getProperty("http.agent"));
//
//                return headers;
//
//            }
//        };
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//
//
//    }


    public String set_error_message(){

        if(class_name.contains("Choose Class")){

            return "please choose class";
        }
        else if(section_name.equalsIgnoreCase("Choose Section")) return "please choose section";
        else if(medium_name.equalsIgnoreCase("Choose Medium")) return "please choose medium";
        else if(routine_type.equalsIgnoreCase("Choose Routine Type")) return "please choose routine";
        else if(file.length()<1) return "please choose file";




        return "Please Fillup The Form With Correct Info";
    }







    public static class CustomAdapter2 extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<String> info;
        LayoutInflater inflter;

        public CustomAdapter2(Context applicationContext,int flag,ArrayList<String> info) {
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
                String type= info.get(i);
                names.setText(type);
                circleImageView.setVisibility(View.GONE);
            }

            return view;
        }
    }

}
