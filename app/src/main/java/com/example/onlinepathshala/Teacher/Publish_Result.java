package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Authority.Result_Form_Configuration2;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Authority.Subject;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.Result_item_info;
import com.example.onlinepathshala.VolleySingleton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Publish_Result extends AppCompatActivity {

    ArrayList<Marks> memberInfos=new ArrayList<>();
    ArrayList<Student > student_infos=new ArrayList<>();
    RecyclerView recyclerView1,recyclerView2;
    ProgressDialog progressDialog;
    final int FILE_CHOOSING_REQUEST_CODE=1;
    public File_Reader file_type;
    public String exam_id,class_id,exam_type,medium,section_id,subject_id,subject_name;
    AlertDialog alertDialog;
    boolean scroll0,scroll1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish__result);

        exam_id=getIntent().getStringExtra("exam_id").toString();
        exam_type=getIntent().getStringExtra("exam_type").toString();
        class_id=getIntent().getStringExtra("class_id").toString();
        medium=getIntent().getStringExtra("medium").toString();
        section_id=getIntent().getStringExtra("section_id").toString();
        subject_id=getIntent().getStringExtra("subject_id").toString();
        subject_name=getIntent().getStringExtra("subject_name").toString();
        if(Result_Form_Configuration.selected_student_infos.size()>0) student_infos=Result_Form_Configuration.selected_student_infos;
        else student_infos= Result_Form_Configuration2.selected_student_infos;
        recyclerView1=findViewById(R.id.recycle1);
        recyclerView2=findViewById(R.id.recycle2);
        progressDialog=new ProgressDialog(Publish_Result.this);
        progressDialog.setMessage("Please wait...");
        re_init_recycle();
    }

    public void re_init_recycle(){
        for(Student student:student_infos){

            memberInfos.add(new Marks(student.id,student.name,null,null,null,null,null,null,null,null));
        }
        Result_Form_Configuration.selected_student_infos.clear();
        RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView1.setAdapter(recycleAdapter);
        RecycleAdapter2 recycleAdapter2=new RecycleAdapter2(memberInfos);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView2.setAdapter(recycleAdapter2);


        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(scroll0) recyclerView2.scrollBy(dx, dy);
            }
        });
        recyclerView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scroll0=true;
                scroll1=false;

                return false;
            }
        });
        recyclerView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scroll1=true;
                scroll0=false;
                return false;
            }
        });


        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(scroll1) recyclerView1.scrollBy(dx, dy);
            }
        });

    }


    public void submit(View view){

        boolean valid=true;
        String message="";
        for(Marks marks:memberInfos){


            if(marks.student_id==null||marks.student_id.length()<1){

                valid=false;
                message+="Please Enter Student Id\n";
                //return;

            }
            if(marks.student_name==null||marks.student_name.length()<1){

                valid=false;
                message+="Please Enter Student Name\n";
                //return;
            }
            if(marks.total_marks==null||marks.total_marks.length()<1){

                valid=false;
                message+="Please Enter Total Marks\n";
                //return;
            }
            if(marks.subjective_total==null||marks.subjective_total.length()<1){

                valid=false;
                message+="Please Enter Subjective Total Marks\n";
               // return;
            }
            if(marks.objective_total==null||marks.objective_total.length()<1){

                valid=false;
                message+="Please Enter Objective Total Marks\n";
               // return;
            }
            if(marks.practical_total==null||marks.practical_total.length()<1){

                valid=false;
                message+="Please Enter Practical Total Marks\n";
                //return;
            }
            if(marks.obtain_marks_subjective==null||marks.obtain_marks_subjective.length()<1){

                valid=false;
                message+="Please Enter Subjective Obtain Marks\n";
                //return;
            }
            if(marks.obtain_marks_objective==null||marks.obtain_marks_objective.length()<1){

                valid=false;
                message+="Please Enter Objective Obtain Marks\n";
                //return;
            }
            if(marks.obtain_marks_practical==null||marks.obtain_marks_practical.length()<1){

                valid=false;
                message+="Please Enter Practical Obtain Marks\n";
                //return;
            }
            if(marks.weight==null||marks.weight.length()<1){

                valid=false;
                message+="Please Enter Weight\n";
               // return;
            }
            if(class_id==null||class_id.length()<1){

                message+="Class Id Missing.Please Go Previous Page\n";
               // return;
            }
            if(exam_id==null||exam_id.length()<1){
                valid=false;
                message+="Exam Id Missing.Please Go Previous Page\n";
               // return;
            }
            if(exam_type==null||exam_type.length()<1){

                valid=false;
                message+="Exam Type Missing.Please Go Previous Page\n";
               // return;
            }
            if(medium==null||medium.length()<1){

                valid=false;
                message+="Medium Missing.Please Go Previous Page\n";
               // return;

            }

            if(valid){

                String regex = "[0-9]+";
                if(!marks.total_marks.matches(regex)){

                    marks.total_marks="0";
                }
                if(!marks.subjective_total.matches(regex)){

                    marks.subjective_total="0";
                }
                if(!marks.objective_total.matches(regex)){

                    marks.objective_total="0";
                }
                if(!marks.practical_total.matches(regex)){

                    marks.practical_total="0";
                }
                if(!marks.obtain_marks_subjective.matches(regex)){

                    marks.obtain_marks_subjective="0";
                }
                if(!marks.obtain_marks_objective.matches(regex)){

                    marks.obtain_marks_objective="0";
                }
                if(!marks.obtain_marks_practical.matches(regex)){

                    marks.obtain_marks_practical="0";
                }
                if(!marks.weight.matches(regex)){

                    marks.weight="0";
                }
                double totalobtain=Double.parseDouble(marks.obtain_marks_subjective)+Double.parseDouble(marks.obtain_marks_objective)+Double.parseDouble(marks.obtain_marks_practical);
                double total_dis=Double.parseDouble(marks.subjective_total)+Double.parseDouble(marks.objective_total)+Double.parseDouble(marks.practical_total);
                double total=Double.parseDouble(marks.total_marks);
                if(total!=total_dis||totalobtain>total){

                    valid=false;
                    message+="Obtain Marks Can Not Be Greater Than Total Marks\n";
                }

            }




        }


        if(valid){



            insertResultData();
        }
        else {

            showMessage(message);
        }



    }

    public void showMessage(String message){

        AlertDialog.Builder builder=new AlertDialog.Builder(Publish_Result.this);
        builder.setTitle("Error in Result Data");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    public void insertResultData(){

        progressDialog.show();
        JSONArray marks=new JSONArray();
        for(Marks mk:memberInfos){

            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("student_id",mk.student_id);
            jsonObject.addProperty("student_name",mk.student_name);
            jsonObject.addProperty("total_marks",mk.total_marks);
            jsonObject.addProperty("subjective_total",mk.subjective_total);
            jsonObject.addProperty("objective_total",mk.objective_total);
            jsonObject.addProperty("practical_total",mk.practical_total);
            jsonObject.addProperty("obtain_marks_subjective",mk.obtain_marks_subjective);
            jsonObject.addProperty("obtain_marks_objective",mk.obtain_marks_objective);
            jsonObject.addProperty("obtain_marks_practical",mk.obtain_marks_practical);
            jsonObject.addProperty("weight",mk.weight);
            marks.put(jsonObject);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.insert_exam_result_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            memberInfos.clear();
                            class_id=null;
                            if(response.contains("message")){

                                response=response.replace("success","").replace("message","");
                                String[] str=response.split("-");
                                String message="";
                                for(int i=0;i<str.length;i++){
                                    message=message+str[i]+"\n";
                                }
                                RecycleAdapter recycleAdapter1=new RecycleAdapter(memberInfos);
                                recyclerView1.setAdapter(recycleAdapter1);
                                RecycleAdapter2 recycleAdapter2=new RecycleAdapter2(memberInfos);
                                recyclerView2.setAdapter(recycleAdapter2);
                                showSuccessMessage("Result Uploaded Successfully.Without Below Students:\n"+message);
                            }
                            else{

                                Toast.makeText(getApplicationContext(), "Result Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }



                        } else if(response.contains("fail")) {

                            Toast.makeText(getApplicationContext(), "Fail To Upload Result", Toast.LENGTH_SHORT).show();
                        }
                        else {


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id", class_id);
                params.put("exam_id", exam_id);
                params.put("exam_type", exam_type);
                params.put("medium", medium);
                params.put("mark_info",marks.toString());
                params.put("section_id",section_id);
                params.put("subject_id",subject_id);
                params.put("subject_name",subject_name);
                params.put("publisher_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("table", "Result");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        show_exit_dialog();
    }

    public void show_exit_dialog(){


        AlertDialog.Builder alert=new AlertDialog.Builder(Publish_Result.this);
        View view=getLayoutInflater().inflate(R.layout.exit_permission,null);
        Button yes=view.findViewById(R.id.yes);
        Button no=view.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                finish();

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

    public void showSuccessMessage(String message){

        AlertDialog.Builder builder=new AlertDialog.Builder(Publish_Result.this);
        builder.setTitle("Result Uploaded");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                finish();

            }
        });
        builder.show();
    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Marks> memberInfos;
        public RecycleAdapter(ArrayList<Marks> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
           EditText student_id,student_name,subject_name;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                student_id=itemView.findViewById(R.id.student_id);
                student_name=itemView.findViewById(R.id.student_name);

                student_id.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.student_id=s.toString();
                    }
                });

                student_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.student_name=s.toString();
                    }
                });


            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pb_result_item1,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewAdapter holder, final int position) {


            final Marks memberInfo=memberInfos.get(position);
            holder.student_id.setText(memberInfo.student_id);
            holder.student_name.setText(memberInfo.student_name);








        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }





    }

    public class RecycleAdapter2 extends RecyclerView.Adapter<RecycleAdapter2.ViewAdapter>{

        ArrayList<Marks> memberInfos;
        public RecycleAdapter2(ArrayList<Marks> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            EditText total,ht_sub,ht_obj,ht_prac,ob_sub,ob_obj,ob_prac,weight;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                total=mView.findViewById(R.id.total);
                ht_sub=mView.findViewById(R.id.ht_sub);
                ht_obj=mView.findViewById(R.id.ht_obj);
                ht_prac=mView.findViewById(R.id.ht_prac);
                ob_sub=mView.findViewById(R.id.ob_sub);
                ob_obj=mView.findViewById(R.id.ob_obj);
                ob_prac=mView.findViewById(R.id.ob_prac);
                weight=mView.findViewById(R.id.weight);

                total.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.total_marks=editable.toString();


                    }
                });

              ht_sub.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.subjective_total=editable.toString();


                    }
                });

                ht_obj.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.objective_total=editable.toString();

                    }
                });

                ht_prac.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.practical_total=editable.toString();

                    }
                });


                ob_sub.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.obtain_marks_subjective=editable.toString();


                    }
                });

               ob_obj.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.obtain_marks_objective=editable.toString();


                    }
                });

                ob_prac.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.obtain_marks_practical=editable.toString();

                    }
                });

                weight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Marks memberInfo=memberInfos.get(getAdapterPosition());
                        memberInfo.weight=editable.toString();

                    }
                });




            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pb_result_item2,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewAdapter holder, final int position) {


            final Marks memberInfo=memberInfos.get(position);
            if(memberInfo.total_marks!=null){

                holder.total.setText(memberInfo.total_marks);
            }
            if(memberInfo.subjective_total!=null){

                holder.ht_sub.setText(memberInfo.subjective_total);
            }
            if(memberInfo.objective_total!=null){

                holder.ht_obj.setText(memberInfo.objective_total);
            }
            if(memberInfo.practical_total!=null){

                holder.ht_prac.setText(memberInfo.practical_total);
            }
            if(memberInfo.obtain_marks_subjective!=null){

                holder.ob_sub.setText(memberInfo.obtain_marks_subjective);
            }
            if(memberInfo.obtain_marks_objective!=null){

                holder.ob_obj.setText(memberInfo.obtain_marks_objective);

            }
            if(memberInfo.obtain_marks_practical!=null){

                holder.ob_prac.setText(memberInfo.obtain_marks_practical);
            }
            if(memberInfo.weight!=null){

                holder.weight.setText(memberInfo.weight);

            }









        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }





    }
    public void choose_and_readFile(View view){
        showHeaderInfo();


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
                    file_type=new CSV_File_Reader();
                    ArrayList<String[]> marks=file_type.readfile(path,getApplicationContext());
                    String[] str2=marks.get(0);
                    if(str2[0].equalsIgnoreCase("error")){

                        showErrorMessage(str2[1]);
                    }
                    else{
                        memberInfos.clear();
                        for(String[] str:marks){
                            memberInfos.add(new Marks(str[0],str[1],str[2],str[3],str[4],str[5],str[6],str[7],str[8],str[9]));
                        }
                        RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
                        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView1.setAdapter(recycleAdapter);
                        RecycleAdapter2 recycleAdapter2=new RecycleAdapter2(memberInfos);
                        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView2.setAdapter(recycleAdapter2);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }



        }




    }

    public void showErrorMessage(String str){

        AlertDialog.Builder builder=new AlertDialog.Builder(Publish_Result.this);
        View view=getLayoutInflater().inflate(R.layout.result_table_header,null);
        builder.setView(view);
        Button ok=view.findViewById(R.id.ok);
        Button cancel=view.findViewById(R.id.cancel);
        TextView error=view.findViewById(R.id.error);
        error.setText("Error In Format\nHeader Number: "+str);
        TextView student_id=view.findViewById(R.id.student_id);
        student_id.setText("1."+Result_Header_Constant.STUDENT_ID);
        TextView student_name=view.findViewById(R.id.student_name);
        student_name.setText("2."+Result_Header_Constant.STUDENT_NAME);
        TextView total_marks=view.findViewById(R.id.total_marks);
        total_marks.setText("3."+Result_Header_Constant.TOTAL_MARKS);
        TextView subjective_total=view.findViewById(R.id.subjective_total);
        subjective_total.setText("4."+Result_Header_Constant.SUBJECTIVE_TOTAL);
        TextView objective_total=view.findViewById(R.id.objective_total);
        objective_total.setText("5."+Result_Header_Constant.OBJECTIVE_TOTAL);
        TextView practical_total=view.findViewById(R.id.practical_total);
        practical_total.setText("6."+Result_Header_Constant.PRACTICAL_TOTAL);
        TextView obtain_mark_subjective=view.findViewById(R.id.obtain_mark_subjective);
        obtain_mark_subjective.setText("7."+Result_Header_Constant.OBTAIN_MARKS_SUBJECTIVE);
        TextView obtain_mark_objective=view.findViewById(R.id.obtain_mark_objective);
        obtain_mark_objective.setText("8."+Result_Header_Constant.OBTAIN_MARKS_OBJECTIVE);
        TextView obtain_mark_practical=view.findViewById(R.id.obtain_mark_practical);
        obtain_mark_practical.setText("9."+Result_Header_Constant.OBTAIN_MARKS_PRACTICAL);
        TextView weight=view.findViewById(R.id.weight);
        weight.setText("10."+Result_Header_Constant.WEIGHT);
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

    public void showHeaderInfo(){


        AlertDialog.Builder builder=new AlertDialog.Builder(Publish_Result.this);
        View view=getLayoutInflater().inflate(R.layout.result_table_header,null);
        builder.setView(view);
        Button ok=view.findViewById(R.id.ok);
        Button cancel=view.findViewById(R.id.cancel);
        TextView error=view.findViewById(R.id.error);
        error.setVisibility(View.GONE);
        TextView student_id=view.findViewById(R.id.student_id);
        student_id.setText("1."+Result_Header_Constant.STUDENT_ID);
        TextView student_name=view.findViewById(R.id.student_name);
        student_name.setText("2."+Result_Header_Constant.STUDENT_NAME);
        TextView total_marks=view.findViewById(R.id.total_marks);
        total_marks.setText("3."+Result_Header_Constant.TOTAL_MARKS);
        TextView subjective_total=view.findViewById(R.id.subjective_total);
        subjective_total.setText("4."+Result_Header_Constant.SUBJECTIVE_TOTAL);
        TextView objective_total=view.findViewById(R.id.objective_total);
        objective_total.setText("5."+Result_Header_Constant.OBJECTIVE_TOTAL);
        TextView practical_total=view.findViewById(R.id.practical_total);
        practical_total.setText("6."+Result_Header_Constant.PRACTICAL_TOTAL);
        TextView obtain_mark_subjective=view.findViewById(R.id.obtain_mark_subjective);
        obtain_mark_subjective.setText("7."+Result_Header_Constant.OBTAIN_MARKS_SUBJECTIVE);
        TextView obtain_mark_objective=view.findViewById(R.id.obtain_mark_objective);
        obtain_mark_objective.setText("8."+Result_Header_Constant.OBTAIN_MARKS_OBJECTIVE);
        TextView obtain_mark_practical=view.findViewById(R.id.obtain_mark_practical);
        obtain_mark_practical.setText("9."+Result_Header_Constant.OBTAIN_MARKS_PRACTICAL);
        TextView weight=view.findViewById(R.id.weight);
        weight.setText("10."+Result_Header_Constant.WEIGHT);
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

}
