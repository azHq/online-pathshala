package com.example.onlinepathshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.example.onlinepathshala.Student.Result_item_info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Final_Result_View_For_All extends AppCompatActivity {

    RecyclerView recyclerView0,recyclerView1,recyclerView2;
    ArrayList<Final_Result_Data_Class> result_info=new ArrayList<>();
    ArrayList<Final_Result_Data_Class> result_info_with_position=new ArrayList<>();
    RecycleAdapter recycleAdapter0,recycleAdapter1;
    RecycleAdapter2 recycleAdapter2;
    boolean scroll0=true,scroll1=true;
    ProgressDialog progressDialog;
    TextView et_exam_type;
    String exam_type;
    String student_id,exam_id,exam_name,class_id,section_id,date2;
    TextView tv_student_id,tv_student_name,tv_date;
    AlertDialog alertDialog,alertDialog2;
    Button download_pdf;
    ArrayList<String> result_data;
    LinearLayout result1,result2;
    EditText et_search;
    Spinner sp_subject,sp_search_type,sp_search_type2,sp_year;
    ArrayList<String> year_list=new ArrayList<>();
    RelativeLayout subject_layout,section_or_class;
    ArrayList<String> subject_list=new ArrayList<>();
    ArrayList<String> search_type_list=new ArrayList<>();
    ArrayList<String> search_type_list2=new ArrayList<>();
    HashMap<String,Float> highest=new HashMap<>();
    String year;
    Button search_btn;
    float highest_marks=0;
    boolean with_position=false;
    String search_string,search_type;
    int pre_search_year_index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__result__view__for__all);

        exam_id=getIntent().getStringExtra("exam_id");
        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        exam_type=getIntent().getStringExtra("exam_type");
        et_exam_type=findViewById(R.id.exam_type);
        et_exam_type.setText(exam_type);
        recyclerView0=findViewById(R.id.recycle);
        recyclerView1=findViewById(R.id.recycle1);
        recyclerView2=findViewById(R.id.recycle2);
        tv_date=findViewById(R.id.date);
        result1=findViewById(R.id.result1);
        result2=findViewById(R.id.result2);
        et_search=findViewById(R.id.search);
        search_btn=findViewById(R.id.search_btn);
        subject_layout=findViewById(R.id.subject_layout);
        section_or_class=findViewById(R.id.section_or_class_layout);
        sp_subject=findViewById(R.id.subject_sp);
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                set_individual_subject(subject_list.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_year=findViewById(R.id.year);
        sp_search_type=findViewById(R.id.search_type);
        sp_search_type2=findViewById(R.id.sp_search_type2);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                search_string=charSequence.toString();
                if(!with_position){

                    set_search_match_item(charSequence.toString(),1);

                }
                else{

                    set_search_match_item2(charSequence.toString(),1);

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!with_position){
                    set_search_match_item(search_string,2);
                }
                else{
                    set_search_match_item2(search_string,2);
                }
            }
        });
        search_type_list.add("See Result Without Position");
        search_type_list.add("See Result With Position");
        sp_search_type.setAdapter(new CustomAdapter(getApplicationContext(),search_type_list));
        sp_search_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0){

                    subject_layout.setVisibility(View.VISIBLE);
                    result1.setVisibility(View.VISIBLE);
                    result2.setVisibility(View.GONE);
                    with_position=false;
                }
                else{
                    result1.setVisibility(View.GONE);
                    subject_layout.setVisibility(View.GONE);
                    result2.setVisibility(View.VISIBLE);
                    get_result_with_position();
                    with_position=true;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR)+"";
        year_list.add(year);
        sp_year.setAdapter(new CustomAdapter(getApplicationContext(),year_list));
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(pre_search_year_index!=i){
                    year=year_list.get(i);
                    getAllData(year);
                    pre_search_year_index=i;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        search_type_list2.add("Section Wise");
        search_type_list2.add("Class Wise");
        sp_search_type2.setAdapter(new CustomAdapter(getApplicationContext(),search_type_list2));
        sp_search_type2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                search_type=search_type_list2.get(i);
                getAllData(year);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        download_pdf=findViewById(R.id.download_pdf);
        progressDialog=new ProgressDialog(Final_Result_View_For_All.this);
        progressDialog.setMessage("Please Wait...");
        recyclerView0.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(scroll0) recyclerView1.scrollBy(dx, dy);
            }
        });
        recyclerView0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scroll0=true;
                scroll1=false;

                return false;
            }
        });
        recyclerView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scroll1=true;
                scroll0=false;
                return false;
            }
        });

        download_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                if(!with_position){

                    download_pdf();
                }
                else{

                    download_pdf2();
                }

            }
        });



        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(scroll1) recyclerView0.scrollBy(dx, dy);
            }
        });



        getAllData(year);
    }

    public void download_pdf()
    {
        result_data=new ArrayList<>();
        String header="Student Id,Student Name,Subject_Name,Total Marks,Highest Marks,Class Test Marks,Monthly Test Marks,Half Yearly Marks,Final Exam Marks,Total,LP,GP,Date\n";
        exam_name="Final Result Without Position";
        if(result_info.size()>0){
            result_data.add(header);
            for(Final_Result_Data_Class result_item_info:result_info){
                String result_row="";
                float highest_marks2=highest.get(result_item_info.subject_name);
                result_row+=result_item_info.student_id+","+result_item_info.student_name+","+result_item_info.subject_name+","+result_item_info.total_marks+","+highest_marks2+","+result_item_info.class_test_marks+","+result_item_info.monthly_test_marks+","+result_item_info.half_yearly_marks+","+result_item_info.final_exam_marks+","+result_item_info.total_obtain_marks+","+result_item_info.lp+","+result_item_info.gp+","+result_item_info.date+"\n";
                result_data.add(result_row);
            }

            if(isStoragePermissionGranted()){

                create_pdf(exam_name);
            }

        }

    }
    public void download_pdf2()
    {
        result_data=new ArrayList<>();
        String header="Student Id,Student Name,Total Marks,Highest Marks,Obtain Marks,Position,Date\n";
        exam_name="Final Result With Position";
        if(result_info_with_position.size()>0){
            result_data.add(header);
            for(Final_Result_Data_Class result_item_info:result_info_with_position){
                String result_row="";
                result_row+=result_item_info.student_id+","+result_item_info.student_name+","+result_item_info.total_marks2+","+result_item_info.highest_marks2+","+result_item_info.sum_of_all_sub_marks+","+result_item_info.position+","+result_item_info.date+"\n";
                result_data.add(result_row);
            }

            if(isStoragePermissionGranted()){

                create_pdf(exam_name);
            }

        }

    }

    public void set_individual_subject(String subject)
    {
        if(!subject.equalsIgnoreCase("All")){

            ArrayList<Final_Result_Data_Class> search_list=new ArrayList<>();
            for(Final_Result_Data_Class final_result_data_class:result_info){

                if(final_result_data_class.subject_name.equalsIgnoreCase(subject)){

                    search_list.add(final_result_data_class);
                }
            }
            recycleAdapter0=new RecycleAdapter(search_list,0);
            recyclerView0.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView0.setAdapter(recycleAdapter0);
            recycleAdapter1=new RecycleAdapter(search_list,1);
            recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView1.setAdapter(recycleAdapter1);
            recyclerView1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        }
        else{

            recycleAdapter0=new RecycleAdapter(result_info,0);
            recyclerView0.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView0.setAdapter(recycleAdapter0);
            recycleAdapter1=new RecycleAdapter(result_info,1);
            recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView1.setAdapter(recycleAdapter1);
            recyclerView1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        }

    }

    public void set_highest_marks(){

        for(Final_Result_Data_Class final_result_data_class:result_info){

            if(highest.get(final_result_data_class.subject_name)==null){

                highest.put(final_result_data_class.subject_name,Float.parseFloat(final_result_data_class.total_obtain_marks));
            }
            else{

                Float marks=Float.parseFloat(final_result_data_class.total_obtain_marks);
                if(highest.get(final_result_data_class.subject_name)!=null&&marks>highest.get(final_result_data_class.subject_name)){

                    highest.put(final_result_data_class.subject_name,marks);
                }
            }
        }

    }

    public void set_search_match_item(String search_string,int condition){

        search_string=search_string.toLowerCase();
        ArrayList<Final_Result_Data_Class> search_list=new ArrayList<>();
        search_list.clear();
        for(Final_Result_Data_Class student2:result_info){

            String id=student2.student_id.toLowerCase();
            String name=student2.student_name.toLowerCase();

            if(condition==1){
                if(id.contains(search_string)||name.contains(search_string)){

                    search_list.add(student2);
                }
            }
            else if(condition==2){
                if(id.equalsIgnoreCase(search_string)||name.equalsIgnoreCase(search_string)){

                    search_list.add(student2);
                }
            }


        }
        recycleAdapter0=new RecycleAdapter(search_list,0);
        recyclerView0.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView0.setAdapter(recycleAdapter0);
        recycleAdapter1=new RecycleAdapter(search_list,1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(recycleAdapter1);
        recyclerView1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);

    }
    public void set_search_match_item2(String search_string,int condition){

        search_string=search_string.toLowerCase();
        ArrayList<Final_Result_Data_Class> search_list=new ArrayList<>();
        search_list.clear();
        for(Final_Result_Data_Class student2:result_info_with_position){

            String id=student2.student_id.toLowerCase();
            String name=student2.student_name.toLowerCase();
            if(condition==1){
                if(id.contains(search_string)||name.contains(search_string)){

                    search_list.add(student2);
                }
            }
            else if(condition==2){
                if(id.equalsIgnoreCase(search_string)||name.equalsIgnoreCase(search_string)){

                    search_list.add(student2);
                }
            }

        }
        recycleAdapter2=new RecycleAdapter2(search_list,0);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView2.setAdapter(recycleAdapter2);


    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void create_pdf(String exam_name){

        Create_CSV_File create_csv_file=new Create_CSV_File(getApplicationContext());

        String path=create_csv_file.create_result_csv(exam_name+".csv",exam_type,result_data);
        if(path!=null){

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Result Pdf Successfully Downloaded",Toast.LENGTH_LONG).show();
            open_downloaded_file(path);
        }
        else{

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Fail To Download Pdf",Toast.LENGTH_LONG).show();
        }
    }

    public void open_downloaded_file(String file_path){

        AlertDialog.Builder builder=new AlertDialog.Builder(Final_Result_View_For_All.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.open_downloaded_file,null);
        builder.setView(view);
        TextView tv_path=view.findViewById(R.id.path);
        tv_path.setText("Path : "+file_path);
        Button open=view.findViewById(R.id.open);
        Button cancel=view.findViewById(R.id.cancel);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File pdfFile = new File(file_path);  // -> filename = maven.pdf
                Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/csv");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "No Application available to open CSV", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
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


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            create_pdf(exam_name);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllData(year);
    }

    public void getAllData(String year){

        JSONArray postparams = new JSONArray();
        postparams.put("Result");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);
        postparams.put(search_type);
        postparams.put(year);
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_final_result,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        result_info.clear();
                        String string;
                        String studentId="",studentName="";
                        try {
                            string=response.getString(0);
                            subject_list.clear();
                            year_list.clear();
                            year_list.add(year);
                            subject_list.add("All");
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String student_id=member.getString("student_id");
                                        String student_name=member.getString("student_name");
                                        String subject_name=member.getString("subject_name");
                                        String total_marks = member.getString("final_total_marks");
                                        String highest_marks="";//member.getString("highest_marks");
                                        String class_test=member.getString("class_test");
                                        String monthly_test=member.getString("monthly_test");
                                        String half_yearly = member.getString("half_yearly");
                                        String final_exam = member.getString("final_exam");
                                        String total_in_100 = member.getString("total_obtain");
                                        String date=member.getString("created_date");
                                        String[] str=lp_gp(total_in_100);
                                        String highest_100="";
                                        String lp=str[0];
                                        String gp=str[1];
                                        date2=date;
                                        String[] year_arr=date.split("-");
                                        if(!year_list.contains(year_arr[2])) year_list.add(year_arr[2]);
                                        if(!subject_list.contains(subject_name)) subject_list.add(subject_name);
                                        result_info.add(new Final_Result_Data_Class(student_id,student_name,subject_name,total_marks,highest_marks,class_test,monthly_test,half_yearly,final_exam,total_in_100,lp,gp,date));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                set_highest_marks();
                                tv_date.setText(date2);
                                sp_year.setAdapter(new CustomAdapter(getApplicationContext(),year_list));
                                sp_subject.setAdapter(new CustomAdapter(getApplicationContext(),subject_list));
                                recycleAdapter0=new RecycleAdapter(result_info,0);
                                recyclerView0.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerView0.setAdapter(recycleAdapter0);
                                recycleAdapter1=new RecycleAdapter(result_info,1);
                                recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerView1.setAdapter(recycleAdapter1);
                                recyclerView1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
                                get_result_with_position();
                                progressDialog.dismiss();
                            }
                            else{
                                set_highest_marks();
                                sp_year.setAdapter(new CustomAdapter(getApplicationContext(),year_list));
                                sp_subject.setAdapter(new CustomAdapter(getApplicationContext(),subject_list));
                                recycleAdapter0=new RecycleAdapter(result_info,0);
                                recyclerView0.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerView0.setAdapter(recycleAdapter0);
                                recycleAdapter1=new RecycleAdapter(result_info,1);
                                recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerView1.setAdapter(recycleAdapter1);
                                recyclerView1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No Result Still Uploaded",Toast.LENGTH_LONG).show();
                                get_result_with_position();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    public String[] lp_gp(String total){

                        double marks= Double.parseDouble(total);
                        if(marks>=80){

                            String[] str={"A+","5.00"};

                            return str;
                        }
                        else if(marks>=70&&marks<80){

                            String[] str={"A","4.00"};
                            return str;
                        }
                        else if(marks>=60&&marks<70){

                            String[] str={"A-","3.50"};
                            return str;
                        }
                        else if(marks>=50&&marks<60){

                            String[] str={"B","3.00"};
                            return str;
                        }
                        else if(marks>=40&&marks<50){

                            String[] str={"C","2.00"};
                            return str;
                        }
                        else if(marks>=33&&marks<40){

                            String[] str={"D","1.00"};
                            return str;
                        }
                        else {

                            String[] str={"F","0.00"};
                            return str;
                        }



                    }


                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                        System.out.println(error.toString());
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

    public void get_result_with_position(){

        result_info_with_position.clear();
        HashMap<String,Final_Result_Data_Class> hashMap=new HashMap<>();
       for(int i=0;i<result_info.size();i++)
       {
           Final_Result_Data_Class final_result_data_class=result_info.get(i);
           if(hashMap.get(final_result_data_class.student_id)==null){

               hashMap.put(final_result_data_class.student_id,new Final_Result_Data_Class(final_result_data_class.student_id,final_result_data_class.student_name,Float.parseFloat(final_result_data_class.total_obtain_marks),0,Float.parseFloat(final_result_data_class.total_marks),0,final_result_data_class.date));
           }
           else{

               Final_Result_Data_Class final_result_data_class1=hashMap.get(final_result_data_class.student_id);
               if(final_result_data_class1!=null){
                   final_result_data_class1.sum_of_all_sub_marks+=Float.parseFloat(final_result_data_class.total_obtain_marks);
                   final_result_data_class1.total_marks2+=Float.parseFloat(final_result_data_class.total_marks);
               }
           }
       }

       result_info_with_position.addAll(hashMap.values());

        Collections.sort(result_info_with_position);
        float highest=result_info_with_position.get(0).sum_of_all_sub_marks;
        for(int i=0;i<result_info_with_position.size();i++){

            Final_Result_Data_Class final_result_data_class=result_info_with_position.get(i);
            final_result_data_class.position=i+1;
            final_result_data_class.highest_marks2=highest;
        }

        recycleAdapter2=new RecycleAdapter2(result_info_with_position,0);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView2.setAdapter(recycleAdapter2);


    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Final_Result_Data_Class> home_task_infos;
        public int flag;
        public RecycleAdapter(ArrayList<Final_Result_Data_Class> classinfos,int flag){
            this.home_task_infos=classinfos;
            this.flag=flag;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            LinearLayout edit,delete;
            TextView student_id,student_name,subject_name,total_marks,highest_marks,class_test,montly_test,half_yearly,final_exam,total_obtain,lg,gp;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;


                if(flag==1){

                    student_id=mView.findViewById(R.id.student_id);
                    student_name=mView.findViewById(R.id.student_name);
                    subject_name=mView.findViewById(R.id.subject_name);

                }
                else{

                    total_marks=mView.findViewById(R.id.total_marks);
                    highest_marks=mView.findViewById(R.id.highest_marks);
                    class_test=mView.findViewById(R.id.class_test);
                    montly_test=mView.findViewById(R.id.monthly_test);
                    half_yearly=mView.findViewById(R.id.half_yearly);
                    final_exam=mView.findViewById(R.id.final_exam);
                    total_obtain=mView.findViewById(R.id.total_obtain);
                    lg=mView.findViewById(R.id.lp);
                    gp=mView.findViewById(R.id.gp);
                }

            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=null;
            if(flag==1){

                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_item6,parent,false);
            }
            else{

                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycler_item5,parent,false);
            }
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Final_Result_Data_Class memberInfo=home_task_infos.get(position);

            if(flag==1){


                holder.student_id.setText(memberInfo.student_id);
                holder.student_name.setText(memberInfo.student_name);
                holder.subject_name.setText(memberInfo.subject_name);
                holder.student_name.setSelected(true);
                holder.subject_name.setSelected(true);

            }
            else{

                float highest_marks=highest.get(memberInfo.subject_name);
                holder.total_marks.setText(memberInfo.total_marks);
                holder.highest_marks.setText(highest_marks+"");
                holder.class_test.setText(memberInfo.class_test_marks);
                holder.montly_test.setText(memberInfo.monthly_test_marks);
                holder.half_yearly.setText(memberInfo.half_yearly_marks);
                holder.final_exam.setText(memberInfo.final_exam_marks);
                holder.total_obtain.setText(memberInfo.total_obtain_marks);
                holder.lg.setText(memberInfo.lp);
                holder.gp.setText(memberInfo.gp);
            }






        }

        @Override
        public int getItemCount() {
            return home_task_infos.size();
        }



    }

    public class RecycleAdapter2 extends RecyclerView.Adapter<RecycleAdapter2.ViewAdapter>{

        ArrayList<Final_Result_Data_Class> home_task_infos;
        public int flag;
        public RecycleAdapter2(ArrayList<Final_Result_Data_Class> classinfos,int flag){
            this.home_task_infos=classinfos;
            this.flag=flag;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            LinearLayout edit,delete;
            TextView student_id,student_name,highest_marks,obtain_marks,position,total_marks;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;




                    student_id=mView.findViewById(R.id.student_id);
                    student_name=mView.findViewById(R.id.student_name);
                    highest_marks=mView.findViewById(R.id.highest_marks);
                    obtain_marks=mView.findViewById(R.id.obtain_marks);
                    position=mView.findViewById(R.id.position);
                    total_marks=mView.findViewById(R.id.total_marks);


            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_item7,parent,false);

            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Final_Result_Data_Class memberInfo=home_task_infos.get(position);

            holder.student_id.setText(memberInfo.student_id);
            holder.student_name.setText(memberInfo.student_name);
            holder.highest_marks.setText(memberInfo.highest_marks2+"");
            holder.obtain_marks.setText(memberInfo.sum_of_all_sub_marks+"");
            holder.position.setText(memberInfo.position+"");
            holder.total_marks.setText(memberInfo.total_marks2+"");





        }

        @Override
        public int getItemCount() {
            return home_task_infos.size();
        }



    }

    public void showMessage(String message){

        AlertDialog.Builder  builder=new AlertDialog.Builder(Final_Result_View_For_All.this);
        View view=getLayoutInflater().inflate(R.layout.permission_denied_dialog,null);
        builder.setView(view);
        TextView textView=view.findViewById(R.id.message);
        textView.setText(message);
        Button ok=view.findViewById(R.id.yes);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();

            }
        });
        alertDialog2= builder.show();
    }


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(Final_Result_View_For_All.this);
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

                            Toast.makeText(getApplicationContext(),"Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllData(year);

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
                params.put("table", "Result");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<String> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,ArrayList<String> info) {
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
            view = inflter.inflate(R.layout.custom_dropdown_item, null);
            final TextView names = (TextView) view.findViewById(R.id.name);

            names.setText(info.get(i));
            return view;
        }
    }
}
