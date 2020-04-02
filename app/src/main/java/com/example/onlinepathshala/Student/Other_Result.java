package com.example.onlinepathshala.Student;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Create_CSV_File;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Result_Edit;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Other_Result extends AppCompatActivity {

    RecyclerView recyclerView0,recyclerView1;
    ArrayList<Result_item_info> result_info=new ArrayList<>();
    RecycleAdapter recycleAdapter0,recycleAdapter1;
    boolean scroll0=true,scroll1=true;
    ProgressDialog progressDialog;
    TextView et_exam_type;
    String exam_type;
    String student_id,class_id;
    TextView tv_student_id,tv_student_name;
    TextView tv_edit,tv_delete;
    AlertDialog alertDialog,alertDialog2;
    ArrayList<String> result_data;
    Spinner sp_year;
    ArrayList<String> year_list=new ArrayList<>();
    String year;
    int pre_search_year_index=0;
    String student_name="",exam_name="";
    Button download_pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other__result);
        student_id=SharedPrefManager.getInstance(getApplicationContext()).get_student_info().id;
        student_name=SharedPrefManager.getInstance(getApplicationContext()).get_student_info().name;
        class_id=getIntent().getStringExtra("class_id");
        exam_type=getIntent().getStringExtra("exam_type");
        et_exam_type=findViewById(R.id.exam_type);
        et_exam_type.setText(exam_type);
        recyclerView0=findViewById(R.id.recycle);
        recyclerView1=findViewById(R.id.recycle1);
        progressDialog=new ProgressDialog(Other_Result.this);
        progressDialog.setMessage("Please Wait...");
        tv_student_id=findViewById(R.id.student_id);
        tv_student_name=findViewById(R.id.student_name);
        download_pdf=findViewById(R.id.download_pdf);
        tv_student_id.setText(student_id);
        tv_student_name.setText(student_name);
        tv_edit=findViewById(R.id.edit);
        tv_delete=findViewById(R.id.delete);

        sp_year=findViewById(R.id.year);
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR)+"";
        year_list.add(year);
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
        if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type().equalsIgnoreCase("Student")){
            tv_edit.setVisibility(View.GONE);
            tv_delete.setVisibility(View.GONE);
        }
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
                result_data=new ArrayList<>();
                String header="Subject Name,Exam Name,Total Marks,Subjective Total,Objective Total,Practical Total,Subjective Highest,Objective Highest,Practical Highest,Total Highest,Obtain Marks In Subjective,Obtain Marks In Objective,Obtain Marks In Practical,Total Obtain,Weight,Total(Weight),Total(100%),LP,GP,Date\n";

                if(result_info.size()>0){
                    result_data.add(header);
                    for(Result_item_info result_item_info:result_info){
                        String result_row="";
                        String[] str=result_item_info.subject_name.split(",");
                        exam_name=str[1];
                        result_row+=str[0]+","+str[1]+","+result_item_info.total_marks+","+result_item_info.subjective_total+","+result_item_info.objective_total+","+result_item_info.practical_total+","+result_item_info.subjective_highest+","+result_item_info.objective_highest+","+result_item_info.practical_highest+","+result_item_info.highest_total_3+","+result_item_info.obtain_marks_subjective+","+result_item_info.obtain_marks_objective+","+result_item_info.obtain_marks_practical+","+result_item_info.obtain_total_3+","+result_item_info.weight+","+result_item_info.total_in_weight+","+result_item_info.total_in_100+","+result_item_info.lp+","+result_item_info.gp+","+result_item_info.date+"\n";
                        result_data.add(result_row);
                    }

                    if(isStoragePermissionGranted()){

                        create_pdf();
                    }

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

        recycleAdapter0=new RecycleAdapter(result_info,0);
        recyclerView0.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView0.setAdapter(recycleAdapter0);
        recycleAdapter1=new RecycleAdapter(result_info,1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(recycleAdapter1);
        recyclerView1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);

        getAllData(year);

    }

    public void getAllData(String year){

        JSONArray postparams = new JSONArray();
        postparams.put("Result");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(student_id);
        postparams.put(class_id);
        postparams.put(exam_type);
        postparams.put(year);
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_other_result_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String studentId="",studentName="";
                        result_info.clear();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        studentId=member.getString("student_id");
                                        studentName=member.getString("student_name");
                                        String subject_name=member.getString("subject_name")+","+member.getString("exam_name");
                                        String total_marks = member.getString("total_marks");
                                        String subjective_total=member.getString("subjective_total");
                                        String objective_total=member.getString("objective_total");
                                        String practical_total=member.getString("practical_total");
                                        String subjective_highest = member.getString("highest_subject");
                                        String objective_highest = member.getString("highest_objective");
                                        String practical_highest = member.getString("highest_practical");
                                        String obtain_marks_subjective = member.getString("obtain_marks_subjective");
                                        String obtain_marks_objective = member.getString("obtain_marks_objective");
                                        String obtain_marks_practical = member.getString("obtain_marks_practical");
                                        String total_in_percentage = member.getString("highest_practical");
                                        String highest_total_3 = member.getString("total_highest");
                                        String obtain_total_3 = member.getString("total_obtain");
                                        String publisher_id= member.getString("publisher_id");
                                        String weight = member.getString("weight");
                                        String total_in_weight = member.getString("total_in_weight");
                                        String total_in_100=member.getString("total_in_100");;
                                        String created_date=member.getString("created_date");;
                                        String[] str=lp_gp(total_in_100);
                                        String highest_100="";
                                        String lp=str[0];
                                        String gp=str[1];
                                        String[] year_arr=created_date.split("-");
                                        if(!year_list.contains(year_arr[2])) year_list.add(year_arr[2]);
                                        result_info.add(new Result_item_info(id,subject_name,total_marks,subjective_total,objective_total,practical_total,subjective_highest,objective_highest,practical_highest,highest_total_3,obtain_marks_subjective,obtain_marks_objective,obtain_marks_practical,obtain_total_3,weight,total_in_weight,total_in_100,lp,gp,publisher_id,created_date));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                sp_year.setAdapter(new CustomAdapter(getApplicationContext(),year_list));
                                tv_student_id.setText(studentId);
                                tv_student_name.setText(studentName);
                                recycleAdapter0.notifyDataSetChanged();
                                recycleAdapter1.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                            else{
                                sp_year.setAdapter(new CustomAdapter(getApplicationContext(),year_list));
                                recycleAdapter0.notifyDataSetChanged();
                                recycleAdapter1.notifyDataSetChanged();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No Result Still Uploaded",Toast.LENGTH_LONG).show();
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

    public void create_pdf(){

        Create_CSV_File create_csv_file=new Create_CSV_File(getApplicationContext());

        String path=create_csv_file.create_result_csv(student_name+"("+student_id+").csv",exam_type,result_data);
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

        AlertDialog.Builder builder=new AlertDialog.Builder(Other_Result.this);
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


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            create_pdf();
        }
    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Result_item_info> home_task_infos;
        public int flag;
        public RecycleAdapter(ArrayList<Result_item_info> classinfos,int flag){
            this.home_task_infos=classinfos;
            this.flag=flag;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout edit,delete;
            LinearLayout linearLayout;
            TextView subject_name,exam_name,total_marks,subjective_total,objective_total,practical_total,subjective_highest,objective_highest,practical_highest,highest_sop,obtain_subjective,obtain_objective,obtain_practical,obtaion_sop,total_percentage,tutorial,total_100,highest_100,lg,gp,date;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;


                if(flag==1){

                    subject_name=mView.findViewById(R.id.subject_name);
                    exam_name=mView.findViewById(R.id.exam_name);
                }
                else{

                    subjective_total=mView.findViewById(R.id.subjective_total);
                    objective_total=mView.findViewById(R.id.objective_total);
                    practical_total=mView.findViewById(R.id.practical_total);
                    total_marks=mView.findViewById(R.id.total);
                    subjective_highest=mView.findViewById(R.id.subjective_highest);
                    objective_highest=mView.findViewById(R.id.objective_highest);
                    practical_highest=mView.findViewById(R.id.practical_highest);
                    obtain_subjective=mView.findViewById(R.id.obtain_in_subjective);
                    obtain_objective=mView.findViewById(R.id.obtain_in_objective);
                    obtain_practical=mView.findViewById(R.id.obtain_in_practical);
                    highest_sop=mView.findViewById(R.id.total_highest_sop);
                    obtaion_sop=mView.findViewById(R.id.total_obtain_sop);
                    total_percentage=mView.findViewById(R.id.total_in_percentage);
                    tutorial=mView.findViewById(R.id.tutorial_percentage);
                    total_100=mView.findViewById(R.id.total_100);
                    lg=mView.findViewById(R.id.lp);
                    gp=mView.findViewById(R.id.gp);
                    edit=mView.findViewById(R.id.edit);
                    delete=mView.findViewById(R.id.delete);
                    date=mView.findViewById(R.id.date);
                }

            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=null;
            if(flag==1){

                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_item3,parent,false);
            }
            else{

                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycler_item,parent,false);
            }
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Result_item_info memberInfo=home_task_infos.get(position);

            if(flag==1){

                String[] str=memberInfo.subject_name.split(",");
                holder.subject_name.setText(str[0]);
                holder.exam_name.setText(str[1]+"");

            }
            else{
                holder.total_marks.setText(memberInfo.total_marks);
                holder.subjective_total.setText(memberInfo.subjective_total);
                holder.objective_total.setText(memberInfo.objective_total);
                holder.practical_total.setText(memberInfo.practical_total);
                holder.subjective_highest.setText(memberInfo.subjective_highest);
                holder.objective_highest.setText(memberInfo.objective_highest);
                holder.practical_highest.setText(memberInfo.practical_highest);
                holder.highest_sop.setText(memberInfo.highest_total_3);
                holder.obtain_subjective.setText(memberInfo.obtain_marks_subjective);
                holder.obtain_objective.setText(memberInfo.obtain_marks_objective);
                holder.obtain_practical.setText(memberInfo.obtain_marks_practical);
                holder.obtaion_sop.setText(memberInfo.obtain_total_3);
                holder.total_percentage.setText(memberInfo.total_in_percentage);
                holder.tutorial.setText(memberInfo.tutorial);
                holder.total_100.setText(memberInfo.total_100);
                holder.lg.setText(memberInfo.lp);
                holder.gp.setText(memberInfo.gp);
                holder.date.setText(memberInfo.date);

                if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type().equalsIgnoreCase("Student")){
                    holder.edit.setVisibility(View.GONE);
                    holder.delete.setVisibility(View.GONE);
                }

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId().equalsIgnoreCase(memberInfo.publisher_id)){

                            Intent tnt=new Intent(getApplicationContext(), Result_Edit.class);
                            tnt.putExtra("id",memberInfo.id);
                            startActivity(tnt);
                        }
                        else{

                            showMessage("You  are not publisher.So can not edit it");
                        }


                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId().equalsIgnoreCase(memberInfo.publisher_id)){

                            showDeleteDialogBox(memberInfo.id);
                        }
                        else{

                            showMessage("You  are not publisher.So can not delete it");
                        }

                    }
                });
            }




        }

        @Override
        public int getItemCount() {
            return home_task_infos.size();
        }



    }

    public void showMessage(String message){

        AlertDialog.Builder  builder=new AlertDialog.Builder(Other_Result.this);
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

        AlertDialog.Builder alert=new AlertDialog.Builder(Other_Result.this);
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
