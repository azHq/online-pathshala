package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.onlinepathshala.Authority.All_Students;
import com.example.onlinepathshala.Authority.Attendence_History_Canlander_View_For_Authority;
import com.example.onlinepathshala.Authority.Classes;
import com.example.onlinepathshala.Authority.Section;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.Attendence_History_Canlander_View;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Attendance_View_For_Teacher extends AppCompatActivity {

    ArrayList<Student_List> absent_list=new ArrayList<>();
    ArrayList<Student_List> present_list=new ArrayList<>();

    RecyclerView present,absent;
    ProgressDialog progressDialog;
    RecycleAdapter_Present recycleAdapter_present;
    RecycleAdapter_Absent recycleAdapter_absent;
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
    String date,date2;
    TextView tv_date,absent_header_text,present_header_text;
    String class_teacher_id="",class_name="",section_name="";
    Button chooseDate,calendar;
    int status=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance__view__for__teacher);

        progressDialog=new ProgressDialog(Attendance_View_For_Teacher.this);
        present=findViewById(R.id.recycle1);
        absent=findViewById(R.id.recycle2);
        absent_header_text=findViewById(R.id.absent_header_text);
        present_header_text=findViewById(R.id.present_header_text);
        calendar=findViewById(R.id.calendar);
        no_item=findViewById(R.id.no_item);
        Calendar cal=Calendar.getInstance();
        Date date1 = cal.getTime();

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        date=format1.format(date1);
        format1 = new SimpleDateFormat("yyyy-MM-dd");
        date2=format1.format(date1);

        tv_date=findViewById(R.id.date);
        tv_date.setText(date);
        chooseDate=findViewById(R.id.choose_date);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!class_id.equalsIgnoreCase("")){
                    Intent tnt=new Intent(getApplicationContext(), All_Student2.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",section_id);
                    tnt.putExtra("class_name",class_name);
                    tnt.putExtra("section_name",section_name);
                    tnt.putExtra("class_teacher_id",class_teacher_id);
                    startActivity(tnt);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Class And Section",Toast.LENGTH_LONG).show();
                }

            }
        });

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(Attendance_View_For_Teacher.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String date3=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;


                                date=date3;
                                date2=year+ "-" + (monthOfYear + 1) + "-" +dayOfMonth ;
                                tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                getAllMemberData(class_id,section_id,date,"present");
                                getAllMemberData(class_id,section_id,date,"absent");




                            }
                        }, year, month, day);
                picker.show();


            }
        });

        recycleAdapter_present=new RecycleAdapter_Present(present_list);
        present.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        present.setAdapter(recycleAdapter_present);

        recycleAdapter_absent=new RecycleAdapter_Absent(absent_list);
        absent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        absent.setAdapter(recycleAdapter_absent);

        sp_medium=findViewById(R.id.medium);
        sp_class=findViewById(R.id.class_name);
        sp_section=findViewById(R.id.section);
        no_item=findViewById(R.id.no_item);


        mediums.add("Bangla");
        mediums.add("English");
        sp_medium.setAdapter(new CustomAdapter(getApplicationContext(),1,mediums));
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
                class_name=((Classes)classes.get(i)).class_name;
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
                section_name=((Section)sections.get(i)).section_name;
                class_teacher_id=((Section)sections.get(i)).teacher_id;
                getAllMemberData(class_id,section_id,date,"present");
                getAllMemberData(class_id,section_id,date,"absent");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
    public static class CustomAdapter2 extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<String> info;
        LayoutInflater inflter;

        public CustomAdapter2(Context applicationContext,int flag,ArrayList<String> info) {
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
            view = inflter.inflate(R.layout.month_year_item_view, null);
            TextView names = (TextView) view.findViewById(R.id.month_year);

            if(flag==1){

                String member= info.get(i);
                names.setText(member);
            }


            return view;
        }
    }





    public void getAllData(final String table_name,String url,String class_id,String section_id,String medium){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());

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
                                    sp_class.setAdapter(new CustomAdapter(getApplicationContext(), 2, classes));
                                }
                                else {

                                    classes.add(new Classes("0","No Class","","",""));
                                    sp_class.setAdapter(new CustomAdapter(getApplicationContext(),2,classes));
                                }
                            }
                            else if (table_name.equalsIgnoreCase("Section")) {

                                sections.clear();
                                if (!string.contains("no item")) {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);

                                            String id = member.getString("id");
                                            String name = member.getString("section_name");
                                            String teacher_id=member.getString("class_teacher_id");
                                            sections.add(new Section(id, "", name, "",teacher_id));
                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }
                                    }
                                    sp_section.setAdapter(new CustomAdapter(getApplicationContext(), 3, sections));
                                }
                                else {



                                    sections.add(new Section("0","","No Sections","",""));
                                    sp_section.setAdapter(new CustomAdapter(getApplicationContext(),3,sections));

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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);


    }




    public class RecycleAdapter_Present extends RecyclerView.Adapter<RecycleAdapter_Present.ViewAdapter>{

        ArrayList<Student_List> classinfos;
        public RecycleAdapter_Present(ArrayList<Student_List> classinfos){
            this.classinfos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

            View mView;
            LinearLayout linearLayout;
            CircleImageView profile;
            TextView student_name,roll,name_text,roll_text,id;
            ImageView cause;
            TextView tv_cause;
            LinearLayout cause_li;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                mView.setOnLongClickListener(this);
                mView.setOnClickListener(this);
                student_name=mView.findViewById(R.id.student_name);
                roll=mView.findViewById(R.id.roll);
                profile=mView.findViewById(R.id.profile);
                linearLayout=mView.findViewById(R.id.view_profile);
                cause=mView.findViewById(R.id.cause);
                tv_cause=mView.findViewById(R.id.causetext);
                cause_li=mView.findViewById(R.id.cause_li);
                id=mView.findViewById(R.id.id);



            }


            @Override
            public boolean onLongClick(View view) {

                String teacher_id=SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                if(teacher_id.equalsIgnoreCase(class_teacher_id)){
                    int position =getLayoutPosition();
                    Student_List memberInfo=classinfos.get(position);
                    show_attendance_edit_dialog(memberInfo.id,memberInfo.cause,1+"",date,date2);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You Are Not Class Teacher.So You Can Not Edit Attendance"+class_teacher_id,Toast.LENGTH_LONG).show();
                }

                return true;
            }
            @Override
            public void onClick(View view) {
                int position =getLayoutPosition();
                Student_List memberInfo=classinfos.get(position);
                Intent tnt=new Intent(getApplicationContext(), Attendence_History_Canlander_View_For_Teacher.class);
                tnt.putExtra("id",memberInfo.id);
                tnt.putExtra("name",memberInfo.student_name);
                tnt.putExtra("class_teacher_id",class_teacher_id);
                startActivity(tnt);
            }
        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendence_recycle_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Student_List memberInfo=classinfos.get(position);
            holder.student_name.setText(memberInfo.student_name);
            holder.roll.setText(memberInfo.roll);
            holder.student_name.setSelected(true);
            holder.roll.setSelected(true);
            holder.cause.setVisibility(GONE);
            holder.cause_li.setVisibility(GONE);
            holder.id.setText(memberInfo.id);
            if(!memberInfo.image_path.equalsIgnoreCase("null")){

                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.male_profile).into(holder.profile);
            }
            else {

                holder.profile.setImageResource(R.drawable.male_profile);
            }




        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public class RecycleAdapter_Absent extends RecyclerView.Adapter<RecycleAdapter_Absent.ViewAdapter>{

        ArrayList<Student_List> classinfos;
        public RecycleAdapter_Absent(ArrayList<Student_List> classinfos){
            this.classinfos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnLongClickListener , View.OnClickListener {

            View mView;
            LinearLayout linearLayout;
            CircleImageView profile;
            TextView student_name,roll,name_text,roll_text,id;
            ImageView cause;
            TextView tv_cause;
            LinearLayout cause_li;
            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                mView.setOnLongClickListener(this);
                mView.setOnClickListener(this);
                student_name=mView.findViewById(R.id.student_name);
                roll=mView.findViewById(R.id.roll);
                profile=mView.findViewById(R.id.profile);
                linearLayout=mView.findViewById(R.id.view_profile);
                cause=mView.findViewById(R.id.cause);
                tv_cause=mView.findViewById(R.id.causetext);
                cause_li=mView.findViewById(R.id.cause_li);
                id=mView.findViewById(R.id.id);


            }

            @Override
            public boolean onLongClick(View view) {

                String teacher_id=SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                if(teacher_id.equalsIgnoreCase(class_teacher_id)){
                    int position =getLayoutPosition();
                    Student_List memberInfo=classinfos.get(position);
                    show_attendance_edit_dialog(memberInfo.id,memberInfo.cause,0+"",date,date2);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You Are Not Class Teacher.So You Can Not Edit Attendance"+class_teacher_id,Toast.LENGTH_LONG).show();
                }

                return true;
            }


            @Override
            public void onClick(View view) {
                int position =getLayoutPosition();
                Student_List memberInfo=classinfos.get(position);
                Intent tnt=new Intent(getApplicationContext(), Attendence_History_Canlander_View_For_Teacher.class);
                tnt.putExtra("id",memberInfo.id);
                tnt.putExtra("name",memberInfo.student_name);
                tnt.putExtra("class_teacher_id",class_teacher_id);
                startActivity(tnt);
            }
        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendence_recycle_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Student_List memberInfo=classinfos.get(position);
            holder.student_name.setText(memberInfo.student_name);
            holder.roll.setText(memberInfo.roll);
            holder.student_name.setSelected(true);
            holder.roll.setSelected(true);
            holder.cause_li.setVisibility(VISIBLE);
            if(memberInfo.getCause().length()>1) holder.tv_cause.setText(memberInfo.getCause());
            holder.tv_cause.setSelected(true);
            holder.id.setText(memberInfo.id);
            holder.cause.setVisibility(GONE);

            if(!memberInfo.image_path.equalsIgnoreCase("null")){


                Picasso.get().load(memberInfo.image_path).placeholder(R.drawable.male_profile).into(holder.profile);
            }
            else {

                holder.profile.setImageResource(R.drawable.male_profile);
            }



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }
    public void show_attendance_edit_dialog(String student_id,String cause,String state,String date,String date2){

        AlertDialog.Builder builder=new AlertDialog.Builder(Attendance_View_For_Teacher.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.attendance_edit_layout,null);
        builder.setView(view);
        Spinner spinner=view.findViewById(R.id.status);
        ArrayList<String> status_list=new ArrayList<>();
        status_list.add("Absent");
        status_list.add("Present");
        spinner.setAdapter(new CustomAdapter2(getApplicationContext(),1,status_list));
        spinner.setSelection(Integer.parseInt(state));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                status=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        EditText et_cause=view.findViewById(R.id.cause);
        TextView tv_date=view.findViewById(R.id.date);
        tv_date.setText(date);
        Button ok=view.findViewById(R.id.yes);
        Button cancel=view.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                update(student_id,date2,status+"",et_cause.getText().toString());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        et_cause.setText(cause);

        alertDialog=builder.show();
    }

    public void update(String student_id,String date2,String status,String cause){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.attendance_update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Attendance Updated Successfully",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            getAllMemberData(class_id,section_id,date2,"present");
                            getAllMemberData(class_id,section_id,date2,"absent");

                        } else {
                            Toast.makeText(getApplicationContext(),"Fail To Update Attendance",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id",student_id);
                params.put("date", date2);
                params.put("status",status);
                params.put("cause",cause);
                params.put("table", "Attendance");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void recycle_re_Initialize(){


        recycleAdapter_present.notifyDataSetChanged();
        recycleAdapter_present.notifyDataSetChanged();
        String header="Absent("+absent_list.size()+")";
        absent_header_text.setText(header);
        header="Present("+present_list.size()+")";
        present_header_text.setText(header);

        if(absent_list.size()==0&&present_list.size()==0){

            no_item.setVisibility(VISIBLE);
        }
        else {
            no_item.setVisibility(GONE);
        }
    }

    public void  getAllMemberData(String class_id,String section_id,String date,String type) {

        JSONArray postparams = new JSONArray();
        postparams.put("Attendance");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);
        postparams.put(date);
        postparams.put(type);

        //Toast.makeText(getApplicationContext(),""+SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id(),Toast.LENGTH_LONG).show();

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_everydays_attendance,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string = response.getString(0);

                            if(type.equalsIgnoreCase("absent")){

                                absent_list.clear();
                                if (!string.contains("no item")) {

                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);
                                            String id = member.getString("student_id");
                                            String student_name = member.getString("student_name");
                                            String image_path = member.getString("image_path");
                                            String cause = member.getString("cause");
                                            String roll="Not Assigned";
                                            if(!member.getString("class_roll").equalsIgnoreCase("NULL")){
                                                roll=member.getString("class_roll");
                                            }
                                            absent_list.add(new Student_List(id, student_name, roll, image_path,cause));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    recycle_re_Initialize();
                                    progressDialog.dismiss();
                                } else {

                                    recycle_re_Initialize();
                                    progressDialog.dismiss();
                                }

                            }
                            else {

                                present_list.clear();
                                if (!string.contains("no item")) {

                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject member = null;
                                        try {
                                            member = response.getJSONObject(i);
                                            String id = member.getString("student_id");
                                            String student_name = member.getString("student_name");
                                            String image_path = member.getString("image_path");
                                            String cause = member.getString("cause");
                                            String roll="Not Assigned";
                                            if(!member.getString("class_roll").equalsIgnoreCase("NULL")){
                                                roll=member.getString("class_roll");
                                            }
                                            present_list.add(new Student_List(id, student_name, roll, image_path,cause));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    recycle_re_Initialize();
                                    progressDialog.dismiss();
                                } else {

                                    recycle_re_Initialize();
                                    progressDialog.dismiss();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
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



}
