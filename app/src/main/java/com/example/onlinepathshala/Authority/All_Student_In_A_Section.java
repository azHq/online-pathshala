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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class All_Student_In_A_Section extends AppCompatActivity implements View.OnTouchListener  {

    ArrayList<Student2> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog,alertDialog2;
    TextView no_item;
    FloatingActionButton dragView;
    public String section_id="",class_id="",class_name,section_name,medium_name;
    float posX=0;
    float posY=0;
    Button delete;
    CheckBox select_all;
    RecycleAdapter recycleAdapter;
    boolean show_checkbox=false,checked_all=false;
    ArrayList<String> student_ids=new ArrayList<>();
    EditText et_search;
    Button search_btn;
    String search_string;
    ArrayList<Student2> search_list=new ArrayList<>();
    TextView tv_class_name,tv_section_name,tv_medium_name,total_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__student__in__a__section2);

        no_item=findViewById(R.id.no_item);
        dragView = findViewById(R.id.fab);
        et_search=findViewById(R.id.search);
        section_id=getIntent().getStringExtra("section_id");
        class_id=getIntent().getStringExtra("class_id");
        class_name=getIntent().getStringExtra("class_name");
        section_name=getIntent().getStringExtra("section_name");
        medium_name=getIntent().getStringExtra("medium_name");
        recyclerView=findViewById(R.id.recycle);
        delete=findViewById(R.id.delete);
        select_all=findViewById(R.id.select_all);

        tv_class_name=findViewById(R.id.class_name);
        tv_section_name=findViewById(R.id.section_name);
        tv_medium_name=findViewById(R.id.medium);
        total_student=findViewById(R.id.total_student);

        tv_class_name.setText("Class : "+class_name);
        tv_section_name.setText("Section : "+section_name);
        tv_medium_name.setText("Medium : "+medium_name);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(student_ids.size()>0){
                    Toast.makeText(getApplicationContext(),student_ids.size()+"",Toast.LENGTH_LONG).show();
                    show_multiple_item_delete_warning();
                }
                else {

                    if(show_checkbox){
                        select_all.setVisibility(View.GONE);
                        show_checkbox=false;
                        checked_all=false;
                        select_all.setChecked(false);
                        recyclerView.setAdapter(recycleAdapter);
                    }
                    else{
                        select_all.setVisibility(View.VISIBLE);
                        show_checkbox=true;
                        recyclerView.setAdapter(recycleAdapter);
                    }
                }


            }
        });
        select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    if(search_list.size()>0) select_all_student2();
                    else select_all_student();
                    checked_all=true;
                }
                else {

                    student_ids.clear();
                    checked_all=false;
                }
                recyclerView.setAdapter(recycleAdapter);
            }
        });

        et_search=findViewById(R.id.search);
        search_btn=findViewById(R.id.search_btn);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                search_string=charSequence.toString();
                set_search_match_item(charSequence.toString(),1);




            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                set_search_match_item(search_string,2);

            }
        });


        progressDialog=new ProgressDialog(All_Student_In_A_Section.this);
        progressDialog.setMessage("Please Wait.....");
        dragView.setOnTouchListener(this);
        getAllMemberData();
    }

    public void set_search_match_item(String search_string,int condition){

        search_string=search_string.toLowerCase();
        search_list.clear();
        for(Student2 student2:memberInfos){

            String id=student2.id.toLowerCase();
            String name=student2.name.toLowerCase();

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
        recycleAdapter=new RecycleAdapter(search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recycleAdapter);


    }

    public void select_all_student(){
        student_ids.clear();
        for(Student2 student:memberInfos){

            student_ids.add(student.id);
        }
    }

    public void select_all_student2(){
        student_ids.clear();
        for(Student2 student:search_list){

            student_ids.add(student.id);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllMemberData();
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_student_info_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        memberInfos.clear();
                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String name = member.getString("student_name");
                                        String email=member.getString("email");
                                        String phone=member.getString("phone_number");
                                        String image_path=member.getString("image_path");
                                        String device_id=member.getString("device_id");
                                        memberInfos.add(new Student2(id,name,email,phone,image_path,device_id));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                total_student.setText("Total Students : "+memberInfos.size() );
                                no_item.setVisibility(View.GONE);
                                recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{
                                total_student.setText("Total Students : "+memberInfos.size() );
                                no_item.setVisibility(View.VISIBLE);
                                recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                Toast.makeText(getApplicationContext(),"No Student Still Added",Toast.LENGTH_LONG).show();
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

        ArrayList<Student2> memberInfos;
        public RecycleAdapter(ArrayList<Student2> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements  View.OnLongClickListener,View.OnClickListener{

            View mView;
            Button view_profile,sendMessage;
            LinearLayout linearLayout;
            TextView id,name,email;
            CircleImageView profile_image;
            CheckBox select;
            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                mView.setOnClickListener(this);
                sendMessage=mView.findViewById(R.id.message);
                view_profile=mView.findViewById(R.id.view_profile);
                select=mView.findViewById(R.id.select);
                id=mView.findViewById(R.id.id);
                name=mView.findViewById(R.id.name);
                email=mView.findViewById(R.id.email);

                profile_image=mView.findViewById(R.id.profile);

            }

            @Override
            public boolean onLongClick(View view) {


                int position =getLayoutPosition();
                Student2 memberInfo=memberInfos.get(position);
                showOptionDialog(memberInfo.id);
                return true;
            }

            @Override
            public void onClick(View view) {

                if(select.isChecked()&&show_checkbox){
                    student_ids.remove(memberInfos.get(getLayoutPosition()).id);
                    select.setChecked(false);
                }
                else if(show_checkbox) {
                    student_ids.add(memberInfos.get(getLayoutPosition()).id);
                    select.setChecked(true);
                }
            }
        }


        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_rec_list_item4,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final  Student2 memberInfo=memberInfos.get(position);
            holder.id.setText(memberInfo.id);
            holder.name.setText(memberInfo.name);
            holder.name.setSelected(true);
            holder.email.setText(memberInfo.email);
            holder.email.setSelected(true);

            if(show_checkbox){
                holder.select.setVisibility(View.VISIBLE);

                if(checked_all){

                    holder.select.setVisibility(View.VISIBLE);
                    holder.select.setChecked(true);
                }
                else {

                    holder.select.setChecked(false);
                }

            }
            else{

                holder.select.setVisibility(View.GONE);
            }





            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.profile10).into(holder.profile_image);
            }

            holder.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getApplicationContext(), Messenger.class);
                    tnt.putExtra("sender_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                    tnt.putExtra("receiver_id",memberInfo.id);
                    tnt.putExtra("sender_type",SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type());
                    tnt.putExtra("receiver_type","Student");
                    tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getDevice_id());
                    tnt.putExtra("receiver_device_id",memberInfo.device_id);
                    tnt.putExtra("image_path",memberInfo.image_path);
                    startActivity(tnt);
                }
            });

            holder.view_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent tnt=new Intent(getApplicationContext(),Student_Profile_For_View.class);
                    tnt.putExtra("id",memberInfo.id);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",section_id);
                    startActivity(tnt);
                }
            });



        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }



    }


    public void showOptionDialog(final String id){

        AlertDialog.Builder builder=new AlertDialog.Builder(All_Student_In_A_Section.this);
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView remove_teacher=view.findViewById(R.id.remove_teacher);
        remove_teacher.setVisibility(View.GONE);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        alertDialog2=builder.show();
        assign_teacher.setVisibility(View.GONE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showEditDialog("id","Edit Student Id",id,id);
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

    public void show_multiple_item_delete_warning(){

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Student_In_A_Section.this);
        View view=getLayoutInflater().inflate(R.layout.delete_dialog_box,null);
        Button yes=view.findViewById(R.id.yes);
        Button no=view.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                progressDialog.show();
                delete_multiple_student();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select_all.setVisibility(View.GONE);
                show_checkbox=false;
                checked_all=false;
                select_all.setChecked(false);
                recyclerView.setAdapter(recycleAdapter);
                alertDialog.dismiss();
            }
        });
        alert.setView(view);
        alertDialog= alert.show();


    }
    public void delete_multiple_student(){

        JSONArray jsonArray=new JSONArray(student_ids);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.remove_multiple_student_from_class,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            select_all.setVisibility(View.GONE);
                            show_checkbox=false;
                            checked_all=false;
                            select_all.setChecked(false);
                            recyclerView.setAdapter(recycleAdapter);
                            Toast.makeText(getApplicationContext(),"Student Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {

                            System.out.println(response.toString());
                            Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("id",jsonArray.toString());
                params.put("table", "Student");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Student_In_A_Section.this);
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.remove_single_student_from_class,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Student Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("table", "Student");
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
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    Intent tnt = new Intent(getApplicationContext(), Add_New_Student.class);
                    tnt.putExtra("class_id", class_id);
                    tnt.putExtra("section_id",section_id);
                    startActivity(tnt);
                }
                break;

            default:
                return false;
        }
        return true;
    }


    public void showEditDialog(final String column_name, String title, String hint,String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(All_Student_In_A_Section.this);
        View view = getLayoutInflater().inflate(R.layout.profile_edit_dialogbox, null);
        TextView tv_title = view.findViewById(R.id.title);
        final EditText value = view.findViewById(R.id.value);
        value.setText(hint);
        tv_title.setText(title);
        Button cancel = view.findViewById(R.id.cancel);
        Button confirm = view.findViewById(R.id.confirm);
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (value.getText().toString().length() > 1&&value.getText().toString().length()<=10) {

                    progressDialog.show();
                    update(value.getText().toString(), column_name,id);
                    dialog.dismiss();
                } else {

                    Toast.makeText(getApplicationContext(), "Please Enter Valid ID", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    public void update(final String value, final String column,String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.common_editor_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),column+" Updated Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            showFailMessage(value);
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
                params.put("value", value);
                params.put("id",id);
                params.put("column",column);
                params.put("table", "Student");
                params.put("school_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void showFailMessage(String str){

        AlertDialog.Builder builder=new AlertDialog.Builder(All_Student_In_A_Section.this);
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

}
