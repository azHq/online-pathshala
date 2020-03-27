package com.example.onlinepathshala.Authority;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.onlinepathshala.Admin.Member;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Medium_Wise_Classes extends Fragment implements View.OnTouchListener {


    FloatingActionButton dragView;
    ArrayList<Classes> class_infos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    TextView no_item;
    String medium,shift_name;
    AlertDialog alertDialog2,alertDialog3;
    ArrayList<Member> members=new ArrayList<>();
    String teacher_name,teacher_id;
    String activity_type="";
    ArrayList<String> all_class_name=new ArrayList<>();
    float posX=0;
    float posY=0;
    public Medium_Wise_Classes(String medium,String activity_type) {

        this.medium=medium;
        this.activity_type=activity_type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_all__classes, container, false);
        dragView = view.findViewById(R.id.fab);
        frameLayout=view.findViewById(R.id.frame_layout);
        recyclerView=view.findViewById(R.id.recycle);
        no_item=view.findViewById(R.id.no_item);
        dragView.setOnTouchListener(this);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        class_infos=new ArrayList<>();
        getAllMemberData();
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Class_Table");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(medium);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_class_info_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        class_infos.clear();
                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("class_id");
                                        String class_name=member.getString("class_name");
                                        String total_student = member.getString("total_student");
                                        String total_section = member.getString("total_section");
                                        class_infos.add(new Classes(id,class_name,"",total_student,total_section));
                                        all_class_name.add(class_name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                no_item.setVisibility(View.GONE);
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();

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

                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter> {

        ArrayList<Classes> classinfos;
        public RecycleAdapter(ArrayList<Classes> classinfos){
            this.classinfos=classinfos;
        }



        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            View mView;
            LinearLayout linearLayout;
            TextView teacher,students,sections,class_name;

            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                students=mView.findViewById(R.id.student);
                sections=mView.findViewById(R.id.section);
                class_name=mView.findViewById(R.id.class_name);
                linearLayout=mView.findViewById(R.id.view_profile);



            }
            @Override
            public void onClick(View view) {

                int position =getLayoutPosition();
                Classes memberInfo=classinfos.get(position);
                if(activity_type.equalsIgnoreCase("student")){


                    Intent tnt=new Intent(getContext(),All_Sections.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    tnt.putExtra("class_name",memberInfo.class_name);
                    tnt.putExtra("type",activity_type);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("subject")){

                    Intent tnt=new Intent(getContext(),All_Subject_Activity.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("assign_subject")){

                    Intent tnt=new Intent(getContext(),All_Sections.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    tnt.putExtra("class_name",memberInfo.class_name);
                    tnt.putExtra("type",activity_type);
                    startActivity(tnt);
                }
                else if(activity_type.equalsIgnoreCase("exam")){

                    Intent tnt=new Intent(getContext(),All_Sections.class);
                    tnt.putExtra("class_id",memberInfo.id);
                    tnt.putExtra("class_name",memberInfo.class_name);
                    tnt.putExtra("type",activity_type);
                    startActivity(tnt);
                }


            }

            @Override
            public boolean onLongClick(View view) {


                 int position =getLayoutPosition();
                 Classes memberInfo=classinfos.get(position);
                 showOptionDialog(memberInfo.id,memberInfo.class_name,memberInfo.class_teacher);
                return true;
            }

        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Classes memberInfo=classinfos.get(position);
            holder.students.setText(memberInfo.total_students);
            holder.sections.setText(memberInfo.total_subjects);
            holder.class_name.setText(memberInfo.class_name);
            holder.class_name.setSelected(true);



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public void showOptionDialog(final String id,final String class_name,final String teacher_name){

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
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
                showEditDialog("class_name","Edit Class Name",class_name,id);
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

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Member> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<Member> info) {
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
            TextView names = (TextView) view.findViewById(R.id.teacher_name);
            Member member=(Member) info.get(i);
            if(!member.profile_image.equalsIgnoreCase("null")){

                Picasso.get().load(member.profile_image).placeholder(R.drawable.profile10).into(circleImageView);
            }
            names.setText(member.user_name);
            return view;
        }
    }


    public void get_all_teacher_data(final String table_name,String url){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        members.clear();
                        members.add(new Member("0","Choose Teacher",null,"NULL",null));
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);

                                        if(table_name.equalsIgnoreCase("Teacher")){
                                            String id = member.getString("id");
                                            String name = member.getString("teacher_name");
                                            String image_path=member.getString("image_path")+" ";
                                            members.add(new Member(id,name,"",image_path,""));
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                            else{


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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }
    public void assignTeacher(final String class_id){


        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.select_teacher,null);
        builder.setView(view);
        Spinner spinner=view.findViewById(R.id.teacher_name);
        spinner.setAdapter(new CustomAdapter(getContext(),1,members));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                teacher_name=members.get(i).user_name;
                teacher_id=members.get(i).id;
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

                if(class_id.length()>0&&!teacher_name.equalsIgnoreCase("Choose Teacher")){
                    alertDialog3.cancel();
                    progressDialog.setTitle("Adding New User");
                    progressDialog.setMessage("Please wait ....");
                    progressDialog.show();
                    assign_new_teacher(class_id,teacher_id);

                     Toast.makeText(getContext(),class_id+teacher_id,Toast.LENGTH_LONG).show();
                }
                else{

                    Toast.makeText(getContext(),"Please Enter a Class Name And Section Name",Toast.LENGTH_LONG).show();
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog3.cancel();
            }
        });
        alertDialog3=builder.show();

    }

    public void assign_new_teacher(String class_id,String teacher_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.common_editor_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getContext(),"Teacher Assigned Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getContext(),"Fail To Assign", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Connect Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",class_id);
                params.put("value",teacher_id);
                params.put("column","class_teacher_id");
                params.put("table", "Class_Table");
                params.put("school_id", SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.delete_section_or_class_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getContext(),"Class Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getContext(),"Fail To Delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Connect Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("table", "Class_Table");
                params.put("school_id", SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {




        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
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
                if (lastAction == MotionEvent.ACTION_DOWN)
                    showDialog();
                break;

            default:
                return false;
        }
        return true;
    }


    public void showEditDialog(final String column_name, String title, String hint,String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

                String class_name=value.getText().toString();
                if(!all_class_name.contains(class_name)){

                    if (class_name.length() > 1) {

                        progressDialog.show();
                        update(class_name, column_name,id);
                        dialog.dismiss();
                    } else {

                        Toast.makeText(getContext(), "Please Enter New Data", Toast.LENGTH_LONG).show();
                    }

                }
                else{

                    Toast.makeText(getContext(), "This Class Name Already Exist", Toast.LENGTH_LONG).show();
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

                            Toast.makeText(getContext(),column+" Updated Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getContext(), "Fail to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("value", value);
                params.put("id",id);
                params.put("column",column);
                params.put("table", "Class_Table");
                params.put("school_id",SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public void showDialog(){


        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if( inflater!=null&&alert!=null){


            View view =inflater.inflate(R.layout.add_new_class,null);
            alert.setView(view);
            final AlertDialog alertDialog=alert.show();

            ArrayList<String> list=new ArrayList<>();
            list.add("Choose Shift");
            list.add("Morning");
            list.add("Noon");
            final EditText class_name=view.findViewById(R.id.class_name);
            final EditText section_name=view.findViewById(R.id.section_name);
            final Spinner sp_shift_name=view.findViewById(R.id.shift_name);
            sp_shift_name.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.simple_spinner_dropdown,list));
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

                    String class_name2=class_name.getText().toString().trim();
                    if(!all_class_name.contains(class_name2)){

                        if(class_name2.length()>1&&section_name.getText().length()>0&&!shift_name.equalsIgnoreCase("Choose Shift")){
                            alertDialog.cancel();
                            progressDialog.setTitle("Adding New User");
                            progressDialog.setMessage("Please wait ....");
                            progressDialog.show();
                            add_new_user(class_name2,section_name.getText().toString(),shift_name.toString());

                            // Toast.makeText(getContext(),class_name.getText().toString()+section_name.getText().toString()+shift_name.toString(),Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(getContext(),"Please Enter a Class Name And Section Name",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{

                        Toast.makeText(getContext(),"This Class Name Already Exist",Toast.LENGTH_LONG).show();
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

    public void add_new_user(final String class_name,final String section_name,final String shift_name){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.add_new_class_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {


                            Toast.makeText(getContext(),"Class Added Successfully", Toast.LENGTH_SHORT).show();
                            onResume();

                        } else {
                            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_name", class_name);
                params.put("section_name", section_name);
                params.put("shift_name", shift_name);
                params.put("medium", medium);
                params.put("table", "Class_Table");
                params.put("school_id",SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
