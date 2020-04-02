package com.example.onlinepathshala.Authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class All_Subject_Activity extends AppCompatActivity implements View.OnTouchListener{

    FloatingActionButton dragView;
    ArrayList<Subject> class_infos=new ArrayList<>();
    ArrayList<String> all_subject_name=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    TextView no_item;
    String class_id;
    float posX=0;
    float posY=0;
    int total_marks,class_test_weight,monthly_test_weight,half_yearly_weight,final_exam_weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__subject_);
        class_id=getIntent().getStringExtra("class_id");
        no_item=findViewById(R.id.no_item);
        dragView = findViewById(R.id.fab);
        frameLayout=findViewById(R.id.frame_layout);
        recyclerView=findViewById(R.id.recycle);
        dragView.setOnTouchListener(this);
        progressDialog=new ProgressDialog(All_Subject_Activity.this);
        progressDialog.setMessage("Please wait...");
    }

    @Override
    public void onResume() {
        super.onResume();
        class_infos=new ArrayList<>();
        getAllMemberData();
    }


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Subject_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_subjects_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        all_subject_name.clear();
                        class_infos.clear();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("subject_id");
                                        String subject_name=member.getString("subject_name");
                                        String total_marks=member.getString("total_marks");
                                        String class_test_weight=member.getString("class_test_weight");
                                        String monthly_test_weight=member.getString("monthly_test_weight");
                                        String half_yearly_weight=member.getString("half_yearly_weight");
                                        String final_exam_weight=member.getString("final_exam_weight");
                                        class_infos.add(new Subject(id,subject_name,total_marks,class_test_weight,monthly_test_weight,half_yearly_weight,final_exam_weight,"0"));
                                        all_subject_name.add(subject_name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                no_item.setVisibility(View.GONE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No Subject Still Added",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
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

        ArrayList<Subject> classinfos;
        public RecycleAdapter(ArrayList<Subject> classinfos){
            this.classinfos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            View mView;
            LinearLayout linearLayout;
            TextView subject_name,total_marks,class_test_weight,monthly_test_weight,half_yearly_weight,final_exam_weight,class_weight1,monthly_weight1,half_weight1,final_weight1;

            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                subject_name=mView.findViewById(R.id.subject_name);
                linearLayout=mView.findViewById(R.id.view_profile);
                total_marks=mView.findViewById(R.id.total_marks);
                class_test_weight=mView.findViewById(R.id.class_test_weight2);
                class_weight1=mView.findViewById(R.id.class_weight1);
                monthly_test_weight=mView.findViewById(R.id.monthly_test_weight);
                monthly_weight1=mView.findViewById(R.id.monthly_weight1);
                half_yearly_weight=mView.findViewById(R.id.half_yearly_weight);
                half_weight1=mView.findViewById(R.id.half_weight1);
                final_exam_weight=mView.findViewById(R.id.final_exam_weight);
                final_weight1=mView.findViewById(R.id.final_weight1);



            }


            @Override
            public void onClick(View view) {

            }

            @Override
            public boolean onLongClick(View view) {
                int position =getLayoutPosition();
                Subject memberInfo=classinfos.get(position);
                showOptionDialog(memberInfo.id,memberInfo.subject_name,memberInfo);
                return true;
            }
        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Subject memberInfo=classinfos.get(position);
            holder.subject_name.setText(memberInfo.subject_name);
            holder.total_marks.setText(memberInfo.total_marks);
            holder.class_test_weight.setText(memberInfo.class_test_weight);
            holder.monthly_test_weight.setText(memberInfo.monthly_test_weight);
            holder.final_exam_weight.setText(memberInfo.final_exam_weight);
            holder.half_yearly_weight.setText(memberInfo.half_yearly_weight);


            holder.class_weight1.setSelected(true);
            holder.monthly_weight1.setSelected(true);
            holder.half_weight1.setSelected(true);
            holder.final_weight1.setSelected(true);





        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public void showOptionDialog(final String id,final String subejct_name,Subject subject){

        AlertDialog.Builder builder=new AlertDialog.Builder(All_Subject_Activity.this);
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView teacher_remover=view.findViewById(R.id.remove_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        final AlertDialog alertDialog2=builder.show();
        assign_teacher.setVisibility(View.GONE);
        teacher_remover.setVisibility(View.GONE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
                showEditDialog(subject);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showDeleteDialogBox(id);
            }
        });


    }

    public void showEditDialog(Subject subject) {

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Subject_Activity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        if( inflater!=null&&alert!=null){


            View view =inflater.inflate(R.layout.add_new_subject,null);
            alert.setView(view);
            final AlertDialog alertDialog=alert.show();

            final EditText editText=view.findViewById(R.id.class_name);
            editText.setText(subject.subject_name);
            ArrayList<String> total_marks_list=new ArrayList<>();
            int index=total_marks_list.indexOf(subject.total_marks);
            total_marks_list.add("Choose Total Marks");
            total_marks_list.add("50");
            total_marks_list.add("100");
            Spinner total_marks_sp=view.findViewById(R.id.total_marks);
            total_marks_sp.setAdapter(new CustomAdapter(getApplicationContext(),1,total_marks_list));
            total_marks_sp.setSelection(index);
            total_marks_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        total_marks=Integer.parseInt(total_marks_list.get(i));
                    }
                    else{
                        total_marks=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayList<String> weight_list=new ArrayList<>();
            weight_list.add("Choose Weight");
            weight_list.add("0");
            weight_list.add("5");
            weight_list.add("10");
            weight_list.add("15");
            weight_list.add("20");
            weight_list.add("25");
            weight_list.add("30");
            weight_list.add("40");
            weight_list.add("50");
            Spinner class_test=view.findViewById(R.id.class_test);
            class_test.setAdapter(new CustomAdapter(getApplicationContext(),1,weight_list));
            index=weight_list.indexOf(subject.class_test_weight);
            class_test.setSelection(index);
            class_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        class_test_weight=Integer.parseInt(weight_list.get(i));
                    }
                    else{
                        class_test_weight=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Spinner monthly_test=view.findViewById(R.id.monthly_test);
            monthly_test.setAdapter(new CustomAdapter(getApplicationContext(),1,weight_list));
            index=weight_list.indexOf(subject.monthly_test_weight);
            monthly_test.setSelection(index);
            monthly_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        monthly_test_weight=Integer.parseInt(weight_list.get(i));
                    }
                    else{
                        monthly_test_weight=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Spinner half_yearly=view.findViewById(R.id.half_yearly);
            half_yearly.setAdapter(new CustomAdapter(getApplicationContext(),1,weight_list));
            index=weight_list.indexOf(subject.half_yearly_weight);
            half_yearly.setSelection(index);
            half_yearly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        half_yearly_weight=Integer.parseInt(weight_list.get(i));
                    }
                    else{
                        half_yearly_weight=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayList<String> weight_list2=new ArrayList<>();
            weight_list2.add("Choose Weight");
            weight_list2.add("30");
            weight_list2.add("40");
            weight_list2.add("50");
            weight_list2.add("60");
            weight_list2.add("70");
            weight_list2.add("75");
            weight_list2.add("80");
            weight_list2.add("90");
            weight_list2.add("100");
            Spinner final_exam=view.findViewById(R.id.final_exam);
            final_exam.setAdapter(new CustomAdapter(getApplicationContext(),1,weight_list2) );
            index=weight_list2.indexOf(subject.final_exam_weight);
            final_exam.setSelection(index);
            final_exam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        final_exam_weight=Integer.parseInt(weight_list2.get(i));
                    }
                    else{
                        final_exam_weight=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            Button confirm=view.findViewById(R.id.addmember);
            confirm.setText("Submit");
            Button cancel=view.findViewById(R.id.cancel);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String subject_name=editText.getText().toString().trim();
                    if(!all_subject_name.contains(subject)){

                        int total_weight=class_test_weight+monthly_test_weight+half_yearly_weight+final_exam_weight;
                        if(subject_name.length()>0&&class_test_weight>=0&&monthly_test_weight>=0&&half_yearly_weight>=0&&final_exam_weight>=0&&total_weight==total_marks){
                            alertDialog.cancel();
                            progressDialog.setTitle("Updating Subject Data");
                            progressDialog.setMessage("Please wait ....");
                            progressDialog.show();
                            update(subject.id,subject_name);
                        }
                        else{

                            if(subject_name.length()<=0){

                                show_error_dialog("Error In Data","Please Enter Subject Name");
                            }
                            else if(total_marks<0){
                                show_error_dialog("Error In Data","Please Choose Total Marks");
                            }
                            else if(class_test_weight<0){
                                show_error_dialog("Error In Data","Please Choose Class Test Weight");
                            }
                            else if(monthly_test_weight<0){
                                show_error_dialog("Error In Data","Please Choose Monthly Test Weight");
                            }
                            else if(half_yearly_weight<0){
                                show_error_dialog("Error In Data","Please Choose Half Yearly Weight");
                            }
                            else if(final_exam_weight<0){
                                show_error_dialog("Error In Data","Please Choose Final Exam Weight");
                            }
                            else if(total_weight!=total_marks){

                                show_error_dialog("Error In Data","Total Weight Must Be Equal Total Marks");
                            }

                        }
                    }
                    else{

                        Toast.makeText(getApplicationContext(),"This Subject Name Already Exist",Toast.LENGTH_LONG).show();
                    }



                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.cancel();
                }
            });


        }


    }
    public void update(final String id,final String subject_name){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.edit_subject_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Subject Updated Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getApplicationContext(), "Fail to update", Toast.LENGTH_SHORT).show();
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
                params.put("subject_id", id);
                params.put("subject_name", subject_name);
                params.put("total_marks", total_marks+"");
                params.put("class_test_weight", class_test_weight+"");
                params.put("monthly_test_weight", monthly_test_weight+"");
                params.put("half_yearly_weight", half_yearly_weight+"");
                params.put("final_exam_weight", final_exam_weight+"");
                params.put("table", "Subject_Table");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Subject_Activity.this);
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

                            Toast.makeText(getApplicationContext(),"Subject Deleted Successfully", Toast.LENGTH_SHORT).show();
                            onResume();

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
                params.put("table", "Subject_Table");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }





    @Override
    public boolean onTouch(View view, MotionEvent event) {




        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                posX=view.getX();
                posY=view.getY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:

                if(event.getRawY()<(height-view.getHeight()/1.2)&&event.getRawY()>view.getHeight()*2) view.setY(event.getRawY()+ dY);
                if(event.getRawX()<(width-view.getWidth()/1.2)&&event.getRawX()>view.getWidth()/1.3) view.setX(event.getRawX() + dX);
                if(Math.abs(view.getX()-posX)>=50||Math.abs(view.getY()-posY)>=50){
                    lastAction = MotionEvent.ACTION_MOVE;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    showDialog();
                break;

            default:
                return false;
        }
        return true;
    }

    public void showDialog(){


        AlertDialog.Builder alert=new AlertDialog.Builder(All_Subject_Activity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        if( inflater!=null&&alert!=null){


            View view =inflater.inflate(R.layout.add_new_subject,null);
            alert.setView(view);
            final AlertDialog alertDialog=alert.show();

            final EditText editText=view.findViewById(R.id.class_name);
            ArrayList<String> total_marks_list=new ArrayList<>();
            total_marks_list.add("Choose Total Marks");
            total_marks_list.add("50");
            total_marks_list.add("100");
            Spinner total_marks_sp=view.findViewById(R.id.total_marks);
            total_marks_sp.setAdapter(new CustomAdapter(getApplicationContext(),1,total_marks_list));
            total_marks_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                           total_marks=Integer.parseInt(total_marks_list.get(i));
                    }
                    else{
                        total_marks=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            ArrayList<String> weight_list=new ArrayList<>();
            weight_list.add("Choose Weight");
            weight_list.add("0");
            weight_list.add("5");
            weight_list.add("10");
            weight_list.add("15");
            weight_list.add("20");
            weight_list.add("25");
            weight_list.add("30");
            weight_list.add("40");
            weight_list.add("50");
            Spinner class_test=view.findViewById(R.id.class_test);
            class_test.setAdapter(new CustomAdapter(getApplicationContext(),1,weight_list));
            class_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        class_test_weight=Integer.parseInt(weight_list.get(i));
                    }
                    else{
                        class_test_weight=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Spinner monthly_test=view.findViewById(R.id.monthly_test);
            monthly_test.setAdapter(new CustomAdapter(getApplicationContext(),1,weight_list));
            monthly_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        monthly_test_weight=Integer.parseInt(weight_list.get(i));
                    }
                    else{
                        monthly_test_weight=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Spinner half_yearly=view.findViewById(R.id.half_yearly);
            half_yearly.setAdapter(new CustomAdapter(getApplicationContext(),1,weight_list));
            half_yearly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        half_yearly_weight=Integer.parseInt(weight_list.get(i));
                    }
                    else{
                        half_yearly_weight=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayList<String> weight_list2=new ArrayList<>();
            weight_list2.add("Choose Weight");
            weight_list2.add("30");
            weight_list2.add("40");
            weight_list2.add("50");
            weight_list2.add("60");
            weight_list2.add("70");
            weight_list2.add("75");
            weight_list2.add("80");
            weight_list2.add("90");
            weight_list2.add("100");
            Spinner final_exam=view.findViewById(R.id.final_exam);
            final_exam.setAdapter(new CustomAdapter(getApplicationContext(),1,weight_list2) );
            final_exam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){

                        final_exam_weight=Integer.parseInt(weight_list2.get(i));
                    }
                    else{
                        final_exam_weight=-1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            Button confirm=view.findViewById(R.id.addmember);
            Button cancel=view.findViewById(R.id.cancel);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String subject=editText.getText().toString().trim();
                    if(!all_subject_name.contains(subject)){

                        int total_weight=class_test_weight+monthly_test_weight+half_yearly_weight+final_exam_weight;
                        if(subject.length()>0&&class_test_weight>=0&&monthly_test_weight>=0&&half_yearly_weight>=0&&final_exam_weight>=0&&total_weight==total_marks){
                            alertDialog.cancel();
                            progressDialog.setTitle("Adding New User");
                            progressDialog.setMessage("Please wait ....");
                            progressDialog.show();
                            add_new_user(subject);
                        }
                        else{

                            if(subject.length()<=0){

                                show_error_dialog("Error In Data","Please Enter Subject Name");
                            }
                            else if(total_marks<0){
                                show_error_dialog("Error In Data","Please Choose Total Marks");
                            }
                            else if(class_test_weight<0){
                                show_error_dialog("Error In Data","Please Choose Class Test Weight");
                            }
                            else if(monthly_test_weight<0){
                                show_error_dialog("Error In Data","Please Choose Monthly Test Weight");
                            }
                            else if(half_yearly_weight<0){
                                show_error_dialog("Error In Data","Please Choose Half Yearly Weight");
                            }
                            else if(final_exam_weight<0){
                                show_error_dialog("Error In Data","Please Choose Final Exam Weight");
                            }
                            else if(total_weight!=total_marks){

                                show_error_dialog("Error In Data","Total Weight Must Be Equal Total Marks");
                            }

                        }
                    }
                    else{

                        Toast.makeText(getApplicationContext(),"This Subject Name Already Exist",Toast.LENGTH_LONG).show();
                    }



                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.cancel();
                }
            });


        }

    }

    public void show_error_dialog(String titlle,String message){

        AlertDialog.Builder builder=new AlertDialog.Builder(All_Subject_Activity.this);
        builder.setTitle(titlle);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    public void add_new_user(final String subject_name){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_new_class_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {


                            Toast.makeText(getApplicationContext(),"Class Added Successfully", Toast.LENGTH_SHORT).show();
                            onResume();

                        } else {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("subject_name", subject_name);
                params.put("total_marks", total_marks+"");
                params.put("class_test_weight", class_test_weight+"");
                params.put("monthly_test_weight", monthly_test_weight+"");
                params.put("half_yearly_weight", half_yearly_weight+"");
                params.put("final_exam_weight", final_exam_weight+"");
                params.put("class_id", class_id);
                params.put("table", "Subject_Table");
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

        public CustomAdapter(Context applicationContext,int flag,ArrayList<String> info) {
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
