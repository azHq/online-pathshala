package com.example.onlinepathshala.Authority;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.onlinepathshala.Profile_View_Only;
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

public class All_Teachers extends Fragment implements View.OnTouchListener,View.OnClickListener{

    public All_Teachers() {

    }

    FloatingActionButton dragView;
    ArrayList<Teacher> memberInfos=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    ArrayList<Subject> subject_list=new ArrayList<>();
    float posX=0;
    float posY=0;
    String subject_id="0";
    Button delete;
    CheckBox select_all;
    RecycleAdapter recycleAdapter;
    boolean show_checkbox=false,checked_all=false;
    ArrayList<String> student_ids=new ArrayList<>();
    EditText et_search;
    Button search_btn;
    String search_string;
    ArrayList<Teacher> search_list=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater,final  ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_all__teachers, container, false);
        dragView = view.findViewById(R.id.fab);
        frameLayout=view.findViewById(R.id.frame_layout);
        recyclerView=view.findViewById(R.id.recycle);
        dragView.setOnTouchListener(this);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        dragView.setOnClickListener(this);
        et_search=view.findViewById(R.id.search);
        search_btn=view.findViewById(R.id.search_btn);
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

        get_all_subject();

        return view;
    }

    public void set_search_match_item(String search_string,int condition){

        search_string=search_string.toLowerCase();
        search_list.clear();
        for(Teacher student2:memberInfos){

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recycleAdapter);


    }

    public void get_all_subject(){


        JSONArray postparams = new JSONArray();
        postparams.put("Subject_Table");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_subject_name,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String school_name=SharedPrefManager.getInstance(getContext()).getUser().getSchool_name();
                        try {
                            string=response.getString(0);

                            subject_list.clear();
                            subject_list.add(new Subject("0","Choose Subject",null));
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try{
                                        member=response.getJSONObject(i);
                                        String id = member.getString("subject_id");
                                        String name = member.getString("subject_name");
                                        subject_list.add(new Subject(id,name,null));

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


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Teacher");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_teacher_info_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String school_name=SharedPrefManager.getInstance(getContext()).getUser().getSchool_name();
                        try {
                            string=response.getString(0);
                            memberInfos.clear();
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String user_name = member.getString("teacher_name");
                                        String image_path=member.getString("image_path");
                                        String class_name=member.getString("class_name");
                                        String subject_name=member.getString("subject_name");
                                        String phone_number=member.getString("phone_number");
                                        String email=member.getString("email");
                                        String device_id=member.getString("device_id");
                                        String rating=member.getString("rating");

                                        float rate=Float.parseFloat(rating);
                                        if(rate<1){

                                            rating=5+"";
                                        }
                                        memberInfos.add(new Teacher(id,user_name,class_name,subject_name,image_path,phone_number,email,device_id,rating));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{

                                recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                Toast.makeText(getContext(),"No Teacher Still Added",Toast.LENGTH_LONG).show();
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
    @Override
    public void onResume() {
        super.onResume();
        memberInfos=new ArrayList<>();
        getAllMemberData();
    }

    @Override
    public void onClick(View view) {

        Toast.makeText(getContext(),"Click",Toast.LENGTH_LONG).show();
    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Teacher> memberInfos;
        public RecycleAdapter(ArrayList<Teacher> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

            View mView;
            Button delete,sendMessage;
            LinearLayout linearLayout;
            CircleImageView imageView;
            TextView name,class_name,subject_name,rating;
            CircleImageView profile_image;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                mView.setOnLongClickListener(this);
                mView.setOnClickListener(this);
                imageView=mView.findViewById(R.id.profile_image);
                sendMessage=mView.findViewById(R.id.message);
                delete=mView.findViewById(R.id.delete);
                linearLayout=mView.findViewById(R.id.view_profile);

                name=mView.findViewById(R.id.name);
                class_name=mView.findViewById(R.id.class_name);
                subject_name=mView.findViewById(R.id.subject_name);
                profile_image=mView.findViewById(R.id.profile_image);
                rating=mView.findViewById(R.id.rating);
            }


            @Override
            public boolean onLongClick(View view) {

                int position =getLayoutPosition();
                Teacher memberInfo=memberInfos.get(position);
                show_assign_subject_dialog(memberInfo.id);
                return true;
            }

            @Override
            public void onClick(View view) {

                Teacher memberInfo=memberInfos.get(getLayoutPosition());
                Intent tnt=new Intent(getContext(), Profile_View_Only.class);
                tnt.putExtra("id",memberInfo.id);
                tnt.putExtra("school_id",SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                tnt.putExtra("table","Teacher");
                tnt.putExtra("name",memberInfo.name);
                tnt.putExtra("phone_number",memberInfo.phone_number);
                tnt.putExtra("email",memberInfo.email);
                tnt.putExtra("device_id",memberInfo.device_id);
                tnt.putExtra("image_path",memberInfo.image_path);
                startActivity(tnt);
            }
        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_rec_list_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final Teacher memberInfo=memberInfos.get(position);
            holder.name.setText(memberInfo.name);
            holder.rating.setText(memberInfo.rating);
            holder.subject_name.setText(memberInfo.subject_name);
            holder.name.setSelected(true);
            holder.subject_name.setSelected(true);

            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.profile10).into(holder.profile_image);
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showDeleteDialogBox(memberInfo.id);
                }
            });
            holder.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent tnt=new Intent(getContext(), Messenger.class);
                    tnt.putExtra("sender_id",SharedPrefManager.getInstance(getContext()).getUser().getId());
                    tnt.putExtra("receiver_id",memberInfo.id);
                    tnt.putExtra("sender_type",SharedPrefManager.getInstance(getContext()).getUser().getUser_type());
                    tnt.putExtra("receiver_type","Teacher");
                    tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getContext()).getUser().getDevice_id());
                    tnt.putExtra("receiver_device_id",memberInfo.device_id);
                    tnt.putExtra("image_path",memberInfo.image_path);
                    startActivity(tnt);

                }
            });







        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }



    }
    public void show_assign_subject_dialog(String teacher_id){

        AlertDialog.Builder  builder=new AlertDialog.Builder(getContext());
        View view=LayoutInflater.from(getContext()).inflate(R.layout.assign_subject,null);
        builder.setView(view);
        Spinner subject_spinner=view.findViewById(R.id.subject);
        subject_spinner.setAdapter(new CustomAdapter(getContext(),1,subject_list));
        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                subject_id=subject_list.get(i).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button cancel=view.findViewById(R.id.cancel);
        Button assign=view.findViewById(R.id.ok);
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!subject_id.equalsIgnoreCase("0")){
                    alertDialog.dismiss();
                    assign_subject(subject_id,teacher_id);

                }
                else{

                    Toast.makeText(getContext(),"Please Choose Subject",Toast.LENGTH_LONG).show();
                }

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

    public void assign_subject(String subject_id,String teacher_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.common_editor_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getContext(),"Subject Assigned Successfully", Toast.LENGTH_SHORT).show();
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
                params.put("id",teacher_id);
                params.put("value",subject_id);
                params.put("column","subject_id");
                params.put("table", "Teacher");
                params.put("school_id", SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Subject> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<Subject> info) {
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

            if(flag==1){

                Subject member=(Subject) info.get(i);
                names.setText(member.subject_name);
                circleImageView.setImageResource(R.drawable.subject3);
            }



            return view;
        }
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.single_row_delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getContext(),"Teacher Deleted Successfully", Toast.LENGTH_SHORT).show();
                            onResume();

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
                params.put("table", "Teacher");
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
                if (lastAction == MotionEvent.ACTION_DOWN){
                    startActivity(new Intent(getContext(),Add_New_Teacher.class));
                }

                break;

              default:
                 return false;
        }
        return true;
    }


}
