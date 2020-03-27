package com.example.onlinepathshala.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.Result_Edit;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Final_Result extends AppCompatActivity {

    RecyclerView recyclerView0,recyclerView1;
    ArrayList<Result_item_info> result_info=new ArrayList<>();
    RecycleAdapter recycleAdapter0,recycleAdapter1;
    boolean scroll0=true,scroll1=true;
    ProgressDialog progressDialog;
    String student_id,class_id,section_id;
    TextView tv_student_name,tv_student_id;
    TextView tv_edit,tv_delete;
    AlertDialog alertDialog,alertDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__result);

        student_id=getIntent().getStringExtra("student_id");
        class_id=getIntent().getStringExtra("class_id");
        recyclerView0=findViewById(R.id.recycle);
        recyclerView1=findViewById(R.id.recycle1);
        progressDialog=new ProgressDialog(Final_Result.this);
        progressDialog.setMessage("Please Wait...");
        tv_student_id=findViewById(R.id.student_id);
        tv_student_name=findViewById(R.id.student_name);
        tv_student_id.setText(SharedPrefManager.getInstance(getApplicationContext()).get_student_info().id);
        tv_student_name.setText(SharedPrefManager.getInstance(getApplicationContext()).get_student_info().name);
        tv_edit=findViewById(R.id.edit);
        tv_delete=findViewById(R.id.delete);
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

        getAllData();

    }

    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("Result");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(student_id);
        postparams.put(class_id);
        postparams.put("Final Exam");
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_result_url,postparams,
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
                                        String subject_name=member.getString("subject_name");
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
                                        String tutorial = member.getString("tutorial_marks");
                                        String publisher_id= member.getString("publisher_id");
                                        String total_100=Double.parseDouble(obtain_total_3)+Double.parseDouble(tutorial)+"";
                                        String[] str=lp_gp(total_100);
                                        String highest_100="";
                                        String lp=str[0];
                                        String gp=str[1];
                                        result_info.add(new Result_item_info(id,subject_name,total_marks,subjective_total,objective_total,practical_total,subjective_highest,objective_highest,practical_highest,highest_total_3,obtain_marks_subjective,obtain_marks_objective,obtain_marks_practical,obtain_total_3,total_in_percentage,tutorial,total_100,highest_100,lp,gp,publisher_id));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                tv_student_id.setText(studentId);
                                //tv_student_name.setText(studentName);
                                recycleAdapter0.notifyDataSetChanged();
                                recycleAdapter1.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                            else{

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

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Result_item_info> home_task_infos;
        public int flag;
        public RecycleAdapter(ArrayList<Result_item_info> classinfos,int flag){
            this.home_task_infos=classinfos;
            this.flag=flag;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            LinearLayout edit,delete;
            TextView subject_name,total_marks,subjective_total,objective_total,practical_total,subjective_highest,objective_highest,practical_highest,highest_sop,obtain_subjective,obtain_objective,obtain_practical,obtaion_sop,total_percentage,tutorial,total_100,highest_100,lg,gp;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;


                if(flag==1){

                    subject_name=mView.findViewById(R.id.subject_name);
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
                }

            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

             View view=null;
             if(flag==1){

                 view= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_item2,parent,false);
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

                holder.subject_name.setText(memberInfo.subject_name);

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

        AlertDialog.Builder  builder=new AlertDialog.Builder(Final_Result.this);
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

        AlertDialog.Builder alert=new AlertDialog.Builder(Final_Result.this);
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
                            getAllData();

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
}
