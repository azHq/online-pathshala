package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
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
import com.example.onlinepathshala.Authority.Assign_Subject_List_Item;
import com.example.onlinepathshala.Authority.Classes;
import com.example.onlinepathshala.Authority.Section;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.All_Classes;
import com.example.onlinepathshala.Student.Today_Classes;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class My_Classes_Teacher extends AppCompatActivity {

    ArrayList<Student_List> absent_list=new ArrayList<>();
    ArrayList<Student_List> present_list=new ArrayList<>();

    RecyclerView present,absent;
    ProgressDialog progressDialog;
    ArrayList<Assign_Subject_List_Item> assign_subject_list_items=new ArrayList<>();
    String[] days={"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","*"};
    RecyclerView recyclerView;
    TextView no_item;
    ArrayList<Object> classes=new ArrayList<>();
    ArrayList<Object> sections=new ArrayList<>();
    ArrayList<Object> mediums=new ArrayList<>();
    AlertDialog alertDialog2,alertDialog;
    ArrayList<Object> days2=new ArrayList<Object>(Arrays.asList("Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","All"));

    float dX;
    float dY;
    int lastAction;
    Spinner sp_medium,sp_class,sp_section,sp_day;
    String search_day;
    String medium="Bangla",class_id="",section_id="";
    String date;
    TextView tv_date,absent_header_text,present_header_text;
    Button chooseDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__classes__teacher);
        progressDialog=new ProgressDialog(My_Classes_Teacher.this);
        recyclerView=findViewById(R.id.recycle);




        no_item=findViewById(R.id.no_item);




        sp_medium=findViewById(R.id.medium);
        sp_class=findViewById(R.id.class_name);
        sp_section=findViewById(R.id.section);
        no_item=findViewById(R.id.no_item);
        sp_day=findViewById(R.id.day);


        Calendar calendar=Calendar.getInstance();
        String day2=days[calendar.get(Calendar.DAY_OF_WEEK)];
        search_day=day2;
        days2.remove(day2);
        days2.add(0,"Today");
        sp_day.setAdapter(new CustomAdapter(getApplicationContext(),1,days2));
        sp_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0){
                    search_day=day2;
                }
                else {
                    search_day=(String) days2.get(i);
                }
                if(search_day.equalsIgnoreCase("All")) getAllAssignSubjectData("all");
                else getAllAssignSubjectData("today");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
               if(search_day.equalsIgnoreCase("All")) getAllAssignSubjectData("all");
               else getAllAssignSubjectData("today");

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
                                            sections.add(new Section(id, "", name, "",""));
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







    public void recycle_init(){

        RecycleAdapter recycleAdapter=new RecycleAdapter(assign_subject_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recycleAdapter);
    }
    public void getAllAssignSubjectData(String type){


        Calendar calendar=Calendar.getInstance();
        JSONArray postparams = new JSONArray();
        postparams.put("Subject_Assigned_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        postparams.put(search_day);
        postparams.put(type);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.today_classes_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        assign_subject_list_items.clear();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("ass_id");
                                        String class_name=member.getString("class_name");
                                        String teacher_name = member.getString("teacher_name");
                                        String subject_name=member.getString("subject_name");
                                        String class_id = member.getString("class_id");
                                        String teacher_id=member.getString("teacher_id");
                                        String section_id = member.getString("section_id");
                                        String subject_id = member.getString("subject_id");
                                        String start_time = member.getString("start_time");
                                        String end_time = member.getString("end_time");
                                        String day = member.getString("day");
                                        assign_subject_list_items.add(new Assign_Subject_List_Item(id,class_id,class_name,teacher_id,teacher_name,subject_id,subject_name,section_id,"",start_time,end_time,day));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                no_item.setVisibility(View.GONE);
                                recycle_init();
                                progressDialog.dismiss();
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                                recycle_init();
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

        ArrayList<Assign_Subject_List_Item> assign_subject_list_items;
        public RecycleAdapter(ArrayList<Assign_Subject_List_Item> assign_subject_list_items){
            this.assign_subject_list_items=assign_subject_list_items;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            LinearLayout linearLayout;
            RecyclerView recyclerView;
            TextView teacher_name,subject_name,start_time,end_time,day;
            LinearLayout main_layout;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                subject_name=mView.findViewById(R.id.subject_name);
                teacher_name=mView.findViewById(R.id.teacher_name);
                start_time=mView.findViewById(R.id.start);
                end_time=mView.findViewById(R.id.end);

                day=mView.findViewById(R.id.day);





            }





        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_class_item3,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Assign_Subject_List_Item assign_subject_list_item=assign_subject_list_items.get(position);
            holder.subject_name.setText(assign_subject_list_item.subject_name);
            holder.teacher_name.setText(assign_subject_list_item.teacher_name);
            holder.start_time.setText(assign_subject_list_item.start_time);
            holder.end_time.setText(assign_subject_list_item.end_time);
            holder.day.setText(assign_subject_list_item.day);
            holder.subject_name.setSelected(true);
            holder.teacher_name.setSelected(true);
            holder.start_time.setSelected(true);
            holder.end_time.setSelected(true);
            holder.day.setSelected(true);




        }

        @Override
        public int getItemCount() {
            return assign_subject_list_items.size();
        }



    }
}
