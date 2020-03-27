package com.example.onlinepathshala.Authority;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.Routine;
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

public class All_Students extends Fragment {


    ArrayList<Student2> memberInfos=new ArrayList<>();

    RecyclerView recyclerView;
    ArrayList<Routine> notice_infos=new ArrayList<>();
    ProgressDialog progressDialog;
    RecycleAdapter recycleAdapter;
    ArrayList<Object> classes=new ArrayList<>();
    ArrayList<Object> sections=new ArrayList<>();
    ArrayList<Object> mediums=new ArrayList<>();
    AlertDialog alertDialog2,alertDialog;

    float dX;
    float dY;
    int lastAction;
    Spinner sp_medium,sp_class,sp_section;
    String medium="Bangla",class_id="",section_id="";
    TextView no_item;
    Button delete;
    CheckBox select_all;
    boolean show_checkbox=false,checked_all=false;
    EditText et_search;
    ArrayList<String> student_ids=new ArrayList<>();
    ArrayList<Student2> search_list=new ArrayList<>();

    public All_Students() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_all__students, container, false);
        progressDialog=new ProgressDialog(getContext());
        recyclerView=view.findViewById(R.id.recycle);
        recycleAdapter=new RecycleAdapter(memberInfos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recycleAdapter);
        sp_medium=view.findViewById(R.id.medium);
        sp_class=view.findViewById(R.id.class_name);
        sp_section=view.findViewById(R.id.section);
        no_item=view.findViewById(R.id.no_item);
        delete=view.findViewById(R.id.delete);
        select_all=view.findViewById(R.id.select_all);
        et_search=view.findViewById(R.id.search);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(student_ids.size()>0){
                    Toast.makeText(getContext(),student_ids.size()+"",Toast.LENGTH_LONG).show();
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

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                set_search_match_item(charSequence.toString());


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        mediums.add("Bangla");
        mediums.add("English");
        sp_medium.setAdapter(new CustomAdapter(getContext(),1,mediums));
        sp_medium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                medium=(String)mediums.get(i);
                getAllData("Class_Table", Constant_URL.get_all_class_info_url,class_id,section_id,medium);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                class_id=((Classes)classes.get(i)).id;
                getAllData("Section", Constant_URL.get_all_section_info_url,class_id,section_id,medium);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                section_id=((Section)sections.get(i)).id;
                getAllMemberData(class_id,section_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }


    public void set_search_match_item(String search_string){

        search_string=search_string.toLowerCase();
        search_list.clear();
        for(Student2 student2:memberInfos){

            String id=student2.id.toLowerCase();
            String name=student2.name.toLowerCase();
            if(id.contains(search_string)||name.contains(search_string)){

                search_list.add(student2);
            }

        }
        recycleAdapter=new RecycleAdapter(search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
            view = inflter.inflate(R.layout.drop_down_item, null);
            final TextView names = (TextView) view.findViewById(R.id.name);


            if(flag==1){

                String s=(String)info.get(i);
                names.setText(s);
            }
            else if(flag==2){
                Classes classes=(Classes) info.get(i);
                names.setText(classes.class_name);

            }
            else if(flag==3){

                Section classes=(Section) info.get(i);
                names.setText(classes.section_name);
            }


            return view;
        }
    }




    public void getAllData(final String table_name,String url,String class_id,String section_id,String medium){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());

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
                                classes.add(new Classes("0", "Not Assigned", "", "", ""));
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
                                    sp_class.setAdapter(new CustomAdapter(getContext(), 2, classes));
                                }
                                else {

                                    classes.add(new Classes("0","No Class","","",""));
                                    sp_class.setAdapter(new CustomAdapter(getContext(),2,classes));
                                }
                            }
                            else if (table_name.equalsIgnoreCase("Section")) {

                                sections.clear();
                                sections.add(new Section("0", "", "Not Assigned", "",""));
                                if (!string.contains("no item")) {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);

                                            String id = member.getString("id");
                                            String name = member.getString("section_name");
                                            sections.add(new Section(id, "", name, "",""));
                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }
                                    }
                                    sp_section.setAdapter(new CustomAdapter(getContext(), 3, sections));
                                }
                                else {



                                    sections.add(new Section("0","","No Sections","",""));
                                    sp_section.setAdapter(new CustomAdapter(getContext(),3,sections));

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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }




    public void getAllMemberData(String class_id,String section_id){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
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
                                        String roll=member.getString("class_roll");
                                        String email=member.getString("email");
                                        String phone=member.getString("phone_number");
                                        String image_path=member.getString("image_path");
                                        String device_id=member.getString("device_id");
                                        String class_id=member.getString("class_id");
                                        String section_id=member.getString("section_id");
                                        memberInfos.add(new Student2(id,name,email,phone,image_path,device_id,class_id,"",section_id,"",roll));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                no_item.setVisibility(View.GONE);
                               recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{

                                no_item.setVisibility(View.VISIBLE);
                                recycleAdapter=new RecycleAdapter(memberInfos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(recycleAdapter);
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






    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Student2> memberInfos;
        public RecycleAdapter(ArrayList<Student2> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {


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

                    Intent tnt=new Intent(getContext(), Messenger.class);
                    tnt.putExtra("sender_id",SharedPrefManager.getInstance(getContext()).getUser().getId());
                    tnt.putExtra("receiver_id",memberInfo.id);
                    tnt.putExtra("sender_type",SharedPrefManager.getInstance(getContext()).getUser().getUser_type());
                    tnt.putExtra("receiver_type","Student");
                    tnt.putExtra("sender_device_id",SharedPrefManager.getInstance(getContext()).getUser().getDevice_id());
                    tnt.putExtra("receiver_device_id",memberInfo.device_id);
                    tnt.putExtra("image_path",memberInfo.image_path);
                    startActivity(tnt);
                }
            });

            holder.view_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent tnt=new Intent(getContext(),Student_Profile_For_View.class);
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

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView remove_teacher=view.findViewById(R.id.remove_teacher);
        remove_teacher.setVisibility(View.GONE);
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

        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.delete_multiple_item,
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
                            Toast.makeText(getContext(),"Student Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData(class_id,section_id);

                        } else {

                            System.out.println(response.toString());
                            Toast.makeText(getContext(),response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("id",jsonArray.toString());
                params.put("table", "Student");
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.single_row_delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getContext(),"Student Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData(class_id,section_id);

                        } else {
                            Toast.makeText(getContext(),response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("table", "Student");
                params.put("school_id", SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
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

                if (value.getText().toString().length() > 1&&value.getText().toString().length()<=10) {

                    progressDialog.show();
                    update(value.getText().toString(), column_name,id);
                    dialog.dismiss();
                } else {

                    Toast.makeText(getContext(), "Please Enter Valid ID", Toast.LENGTH_LONG).show();
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
                            getAllMemberData(class_id,section_id);

                        } else {
                            showFailMessage(value);
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
                params.put("table", "Student");
                params.put("school_id",SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public void showFailMessage(String str){

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
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
