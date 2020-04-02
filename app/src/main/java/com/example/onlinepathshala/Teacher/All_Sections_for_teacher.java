package com.example.onlinepathshala.Teacher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Authority.All_Student_In_A_Section;
import com.example.onlinepathshala.Authority.All_Subject_Activity;
import com.example.onlinepathshala.Authority.Assign_Subject_List_Activity2;
import com.example.onlinepathshala.Authority.Section;
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

public class All_Sections_for_teacher extends AppCompatActivity implements View.OnTouchListener {

    FloatingActionButton dragView;
    ArrayList<Section> sections=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    TextView no_item;
    String medium,shift_name;
    AlertDialog alertDialog2;
    String class_id,activity_type,class_name,section_id,section_name,teacher_id,teacher_name,class_teacher_id,assign_teacher_id="";
    LinearLayout my_class;
    TextView tv_section_name,tv_shift,tv_teacher_name,tv_header,tv_total_student;
    boolean assign_other_section=false;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__sections_for_teacher);

        class_id=getIntent().getStringExtra("class_id");
        class_name=getIntent().getStringExtra("class_name");
        activity_type=getIntent().getStringExtra("type");
        dragView = findViewById(R.id.fab);
        frameLayout=findViewById(R.id.frame_layout);
        recyclerView=findViewById(R.id.recycle);
        no_item=findViewById(R.id.no_item);
        tv_section_name=findViewById(R.id.section_name);
        tv_shift=findViewById(R.id.shift);
        tv_teacher_name=findViewById(R.id.teacher);
        my_class=findViewById(R.id.my_class);
        tv_header=findViewById(R.id.header);
        tv_total_student=findViewById(R.id.total_student);

        teacher_id=SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
        dragView.hide();
        progressDialog=new ProgressDialog(All_Sections_for_teacher.this);
        progressDialog.setMessage("Please wait...");
        cardView=findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(activity_type.equalsIgnoreCase("student")){

                    Intent tnt=new Intent(getApplicationContext(),All_Student_In_A_Section.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",section_id);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("subject")){

                    Intent tnt=new Intent(getApplicationContext(),All_Subject_Activity.class);
                    tnt.putExtra("class_id",class_id);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("home task")){

                    if(assign_other_section){
                        Intent tnt=new Intent(getApplicationContext(), All_Home_Task.class);
                        tnt.putExtra("class_id",class_id);
                        tnt.putExtra("class_name",class_name);
                        tnt.putExtra("section_id",section_id);
                        tnt.putExtra("section_name",section_name);
                        tnt.putExtra("type",activity_type);
                        startActivity(tnt);
                    }
                    else{

                        Toast.makeText(getApplicationContext(),assign_teacher_id+","+teacher_id,Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"You Can Not Give Home Task.Cause You Are Not Assigned Any Subject",Toast.LENGTH_LONG).show();
                    }

                }
                else if(activity_type.equalsIgnoreCase("attendence")){


                        Intent tnt=new Intent(getApplicationContext(), Attendance_Approval_View.class);
                        tnt.putExtra("class_id",class_id);
                        tnt.putExtra("class_name",class_name);
                        tnt.putExtra("section_id",section_id);
                        tnt.putExtra("teacher_id",class_teacher_id);
                        tnt.putExtra("section_name",section_name);
                        tnt.putExtra("type",activity_type);
                        startActivity(tnt);


                }
            }
        });
        get_own_class();

    }
    public void get_other_class(){


        JSONArray postparams = new JSONArray();
        postparams.put("Section");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_teacher_sections,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        sections.clear();
                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String shift = member.getString("shift_name");
                                        String section_name2=member.getString("section_name");
                                        String class_teacher_name2 = member.getString("class_teacher_name");
                                        String teacher_id2=member.getString("class_teacher_id");
                                        String total_student=member.getString("total_student");

                                        if(section_id.equalsIgnoreCase(id)){

                                            assign_other_section=true;
                                        }
                                        else{

                                            sections.add(new Section(id,shift,section_name2,class_teacher_name2,teacher_id2,total_student));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                RecycleAdapter recycleAdapter=new RecycleAdapter(sections);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                if(sections.size()>0) no_item.setVisibility(View.GONE);
                                else no_item.setVisibility(View.VISIBLE);
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                tv_header.setVisibility(View.GONE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(sections);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(),"no item",Toast.LENGTH_LONG).show();
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

    public void get_own_class(){


        JSONArray postparams = new JSONArray();
        postparams.put("Section");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.teacher_own_section,postparams,
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
                                        String id = member.getString("id");
                                        String shift = member.getString("shift_name");
                                        String section_name2=member.getString("section_name");
                                        String class_teacher_name2 = member.getString("class_teacher_name");
                                        String teacher_id2=member.getString("class_teacher_id");
                                        String total_student=member.getString("total_student");
                                        my_class.setVisibility(View.VISIBLE);
                                        tv_teacher_name.setText(class_teacher_name2);
                                        tv_section_name.setText(section_name2);
                                        tv_shift.setText(shift);
                                        tv_total_student.setText(total_student);
                                        section_id=id;
                                        section_name=section_name2;
                                        class_teacher_id=teacher_id2;
                                        teacher_name=class_teacher_name2;
                                        tv_teacher_name.setSelected(true);
                                        tv_section_name.setSelected(true);
                                        tv_shift.setSelected(true);
                                        tv_header.setVisibility(View.VISIBLE);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                progressDialog.dismiss();
                                get_other_class();
                            }
                            else{

                                progressDialog.dismiss();
                                get_other_class();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }


                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        get_other_class();
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


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter> {

        ArrayList<Section> classinfos;
        public RecycleAdapter(ArrayList<Section> classinfos){
            this.classinfos=classinfos;
        }



        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            View mView;
            LinearLayout linearLayout;
            TextView section_name,students,shift,total_student;

            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                section_name=mView.findViewById(R.id.section_name);
                students=mView.findViewById(R.id.teacher);
                shift=mView.findViewById(R.id.shift);
                linearLayout=mView.findViewById(R.id.view_profile);
                total_student=mView.findViewById(R.id.total_student);



            }
            @Override
            public void onClick(View view) {

                int position =getLayoutPosition();
                Section memberInfo=classinfos.get(position);
                if(activity_type.equalsIgnoreCase("student")){

                    Intent tnt=new Intent(getApplicationContext(),All_Student_In_A_Section.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",memberInfo.id);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("subject")){

                    Intent tnt=new Intent(getApplicationContext(),All_Subject_Activity.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("home task")){

                    Intent tnt=new Intent(getApplicationContext(), All_Home_Task.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("class_name",class_name);
                    tnt.putExtra("section_id",memberInfo.id);
                    tnt.putExtra("section_name",memberInfo.section_name);
                    tnt.putExtra("type",activity_type);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("attendence")){

                    Intent tnt=new Intent(getApplicationContext(), Attendance_Approval_View.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("class_name",class_name);
                    tnt.putExtra("section_id",memberInfo.id);
                    tnt.putExtra("teacher_id",memberInfo.teacher_id);
                    tnt.putExtra("section_name",memberInfo.section_name);
                    tnt.putExtra("type",activity_type);
                    startActivity(tnt);
                }

            }

            @Override
            public boolean onLongClick(View view) {


                int position =getLayoutPosition();
                Section memberInfo=classinfos.get(position);
                showOptionDialog(memberInfo.id,memberInfo.section_name,memberInfo.shift);
                return true;
            }

        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.section_rec_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Section memberInfo=classinfos.get(position);
            holder.total_student.setText(memberInfo.total_student);
            if(!memberInfo.teacher_name.equalsIgnoreCase("NULL")) holder.students.setText(memberInfo.teacher_name);
            holder.section_name.setText(memberInfo.section_name);
            holder.shift.setText(memberInfo.shift);
            holder.students.setSelected(true);

        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public void showOptionDialog(final String id,final String section_name,final String shift){

        AlertDialog.Builder builder=new AlertDialog.Builder(All_Sections_for_teacher.this);
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        alertDialog2=builder.show();
        assign_teacher.setVisibility(View.GONE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showEditDialog(section_name,shift,id);
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


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Sections_for_teacher.this);
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

                            Toast.makeText(getApplicationContext(),"Class Deleted Successfully", Toast.LENGTH_SHORT).show();
                            get_other_class();

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
                params.put("table", "Section");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {




        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels-view.getHeight();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:



                if(event.getRawY()<(height-view.getHeight()/1.2)&&event.getRawY()>view.getHeight()*2) view.setY(event.getRawY()+ dY);
                if(event.getRawX()<(width-view.getWidth()/1.2)&&event.getRawX()>view.getWidth()/1.3) view.setX(event.getRawX() + dX);

                lastAction = MotionEvent.ACTION_MOVE;
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


    public void showEditDialog(final String section_name, final String shift,final String id) {

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Sections_for_teacher.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        if( inflater!=null&&alert!=null){


            View view =inflater.inflate(R.layout.edit_section,null);
            alert.setView(view);
            final AlertDialog alertDialog=alert.show();

            ArrayList<String> list=new ArrayList<>();
            list.add("Choose Shift");
            list.add("Morning");
            list.add("Noon");
            final EditText et_section_name=view.findViewById(R.id.section_name);
            et_section_name.setText(section_name);
            final Spinner sp_shift_name=view.findViewById(R.id.shift_name);
            sp_shift_name.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_spinner_dropdown,list));
            if(shift.equalsIgnoreCase("Morning")){

                sp_shift_name.setSelection(1);
            }
            else{

                sp_shift_name.setSelection(2);
            }

            sp_shift_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    shift_name=list.get(i);
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




                    if(et_section_name.getText().length()>0&&!shift_name.equalsIgnoreCase("Choose Shift")){
                        alertDialog.cancel();
                        progressDialog.setMessage("Please wait ....");
                        progressDialog.show();
                        update(et_section_name.getText().toString(),shift_name.toString(),id);

                        // Toast.makeText(getContext(),class_name.getText().toString()+section_name.getText().toString()+shift_name.toString(),Toast.LENGTH_LONG).show();
                    }
                    else{

                        Toast.makeText(getApplicationContext(),"Please Enter a Class Name And Section Name",Toast.LENGTH_LONG).show();
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
    public void update(final String section_name, final String shift_name,String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.edit_section_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext()," Updated Successfully", Toast.LENGTH_SHORT).show();
                            get_other_class();

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
                params.put("section_name", section_name);
                params.put("id",id);
                params.put("shift",shift_name);
                params.put("table", "Section");
                params.put("school_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void showDialog(){


        AlertDialog.Builder alert=new AlertDialog.Builder(All_Sections_for_teacher.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        if( inflater!=null&&alert!=null){


            View view =inflater.inflate(R.layout.add_new_section,null);
            alert.setView(view);
            final AlertDialog alertDialog=alert.show();

            ArrayList<String> list=new ArrayList<>();
            list.add("Choose Shift");
            list.add("Morning");
            list.add("Noon");
            final EditText section_name=view.findViewById(R.id.section_name);
            final Spinner sp_shift_name=view.findViewById(R.id.shift_name);
            sp_shift_name.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_spinner_dropdown,list));
            sp_shift_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    shift_name=list.get(i);
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




                    if(section_name.getText().length()>0&&!shift_name.equalsIgnoreCase("Choose Shift")){
                        alertDialog.cancel();
                        progressDialog.setTitle("Adding New User");
                        progressDialog.setMessage("Please wait ....");
                        progressDialog.show();
                        add_new_user(class_id,section_name.getText().toString(),shift_name.toString());

                        // Toast.makeText(getContext(),class_name.getText().toString()+section_name.getText().toString()+shift_name.toString(),Toast.LENGTH_LONG).show();
                    }
                    else{

                        Toast.makeText(getApplicationContext(),"Please Enter a Class Name And Section Name",Toast.LENGTH_LONG).show();
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

    public void add_new_user(final String class_id,final String section_name,final String shift_name){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_new_section_info_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {


                            Toast.makeText(getApplicationContext(),"Section Added Successfully", Toast.LENGTH_SHORT).show();
                            get_other_class();

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
                params.put("section_name", section_name);
                params.put("shift_name", shift_name);
                params.put("class_id",class_id);
                params.put("table", "Section");
                params.put("school_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
