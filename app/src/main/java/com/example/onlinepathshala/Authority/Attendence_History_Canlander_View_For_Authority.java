package com.example.onlinepathshala.Authority;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Create_CSV_File;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.Attendence_Info_Class2;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Attendence_History_Canlander_View_For_Authority extends AppCompatActivity {



    RecyclerView recyclerView;
    ArrayList<String> result_data=new ArrayList<>();
    ArrayList<Attendence_Info_Class2> attendence_info=new ArrayList<>();
    ArrayList<String> months=new ArrayList<>();
    ArrayList<String> years=new ArrayList<>();
    Spinner sp_month,sp_year;
    ProgressDialog progressDialog;
    HashMap<String,String[][][]> hashMap=new HashMap<>();
    int selected_year,selected_month;
    String student_id="",class_id="",section_id="",studnet_name;
    int total_working_day=0,absent=0,present=0;
    float percentage=0;
    AlertDialog alertDialog;
    Button download_pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_attendence__history__canlander__view);
        student_id=getIntent().getStringExtra("id");
        studnet_name=getIntent().getStringExtra("name");
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        progressDialog=new ProgressDialog(Attendence_History_Canlander_View_For_Authority.this);
        getAllData();
        sp_month=findViewById(R.id.month);
        sp_year=findViewById(R.id.year);
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_month=position;
                if(position<12) re_init_listView(selected_year,selected_month);
                else re_init_listView_All_month(selected_year);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_year.setAdapter(new CustomAdapter(getApplicationContext(),1,years));
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_year=Integer.parseInt(years.get(position));
                re_init_listView(selected_year,selected_month);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        download_pdf=findViewById(R.id.download_pdf);
        download_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hashMap!=null&hashMap.size()>0) download_attendance_history();
            }
        });
    }

    public void download_attendance_history() {

        result_data = new ArrayList<>();
        String header = "Month,";
        for(int i=1;i<31;i++){
            header+=i+",";
        }
        header+="31,Absent,Present,Total Working Day,Present Percentage\n";
        result_data.add(header);
        String[][][] attendence_info_array = hashMap.get(selected_year + "");
        int absent2=0,present2=0,working_day=0,total_absent=0,total_present=0,total_working_day=0;
        float percentage=0,total_percentage=0;
        String row_data="";
        if(selected_month==12){

            for (int i = 0; i < 12; i++) {

                String[][] status = new String[31][2];
                row_data=months.get(i)+",";
                absent2=0;
                present2=0;
                working_day=0;
                for (int j = 0; j < status.length; j++) {

                    if (attendence_info_array != null) {

                        status[j][0] = attendence_info_array[i][j][0];
                        status[j][1] = attendence_info_array[i][j][1];

                        if (attendence_info_array[i][j][0] != null && attendence_info_array[i][j][0].equalsIgnoreCase("0")) {
                            row_data+="A,";
                            working_day += 1;
                            absent2 += 1;
                        } else if (attendence_info_array[i][j][0] != null && attendence_info_array[i][j][0].equalsIgnoreCase("1")) {
                            row_data+="P,";
                            working_day += 1;
                            present2 += 1;
                        }
                        else {
                            row_data+="Off Day,";
                        }
                    }
                }
                float percentage2=0;
                if(working_day!=0){
                    percentage2=(present2/(float)working_day);
                }
                String str=String.format("%.02f",percentage2);
                row_data+=absent2+","+present2+","+working_day+","+str+"%\n";
                result_data.add(row_data);
                total_absent+=absent2;
                total_present+=present2;
                total_working_day+=working_day;

            }
            row_data="";
            for(int i=1;i<31;i++){
                row_data+="-,";
            }
            total_percentage=0;
            if(total_working_day!=0){
                total_percentage=(total_present/(float)total_working_day);
            }

            String str=String.format("%.02f",total_percentage);
            row_data+="-,Total,"+total_absent+","+total_present+","+total_working_day+","+str+"%";
            result_data.add(row_data);
        }
        else if(selected_month<12){



            String[][] status = new String[31][2];
            row_data=months.get(selected_month)+",";
            absent2=0;
            present2=0;
            working_day=0;
            for (int j = 0; j < status.length; j++) {

                if (attendence_info_array != null) {

                    status[j][0] = attendence_info_array[selected_month][j][0];
                    status[j][1] = attendence_info_array[selected_month][j][1];

                    if (attendence_info_array[selected_month][j][0] != null && attendence_info_array[selected_month][j][0].equalsIgnoreCase("0")) {
                        row_data+="A,";
                        working_day += 1;
                        absent2 += 1;
                    } else if (attendence_info_array[selected_month][j][0] != null && attendence_info_array[selected_month][j][0].equalsIgnoreCase("1")) {
                        row_data+="P,";
                        working_day += 1;
                        present2 += 1;
                    }
                    else {
                        row_data+="Off Day,";
                    }
                }

            }
            float percentage2=0;
            if(working_day!=0){
                percentage2=(present2/(float)working_day);
            }

            String str=String.format("%.02f",percentage2);
            row_data+=absent2+","+present2+","+working_day+","+str+"%\n";
            result_data.add(row_data);



        }

        if(isStoragePermissionGranted()) create_pdf();




    }

    public void create_pdf(){

        Create_CSV_File create_csv_file=new Create_CSV_File(getApplicationContext());

        String path=create_csv_file.create_attendance_csv(studnet_name+"("+student_id+").csv","Attendance",result_data);
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

        AlertDialog.Builder builder=new AlertDialog.Builder(Attendence_History_Canlander_View_For_Authority.this);
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



    public void re_init_listView_All_month(int year){

        attendence_info.clear();
        String[][] str=new String[31][2];
        attendence_info.add(new Attendence_Info_Class2(1, 1, str));
        total_working_day=0;
        absent=0;
        present=0;
        percentage=0;
        String[][][] attendence_info_array=hashMap.get(year+"");

        for (int i = 0; i < 12; i++) {

            String[][] status = new String[31][2];

            for (int j = 0; j < status.length; j++) {

                if(attendence_info_array!=null) {

                    status[j][0] = attendence_info_array[i][j][0];
                    status[j][1] = attendence_info_array[i][j][1];

                    if (attendence_info_array[i][j][0] != null && attendence_info_array[i][j][0].equalsIgnoreCase("0")) {

                        total_working_day += 1;
                        absent += 1;
                    } else if (attendence_info_array[i][j][0] != null && attendence_info_array[i][j][0].equalsIgnoreCase("1")) {

                        total_working_day += 1;
                        present += 1;
                    }
                }
            }



            attendence_info.add(new Attendence_Info_Class2(year, i, status));

        }

        if(total_working_day>0){

            percentage=(present/(float)total_working_day)*100;
        }
        else{
            percentage=0;
        }
        RecycleAdapter recycleAdapter=new RecycleAdapter(getApplicationContext(),0,attendence_info);
        recyclerView.setAdapter(recycleAdapter);
    }

    public void re_init_listView(int year,int month){

        attendence_info.clear();
        String[][] str=new String[31][2];
        attendence_info.add(new Attendence_Info_Class2(1, 1, str));
        String[][][] attendence_info_array=hashMap.get(year+"");
        String[][] status=new String[31][2];
        total_working_day=0;
        absent=0;
        present=0;
        percentage=0;



        for(int j=0;j<status.length;j++){

            if(attendence_info_array!=null) {
                status[j][0] = attendence_info_array[month][j][0];
                status[j][1] = attendence_info_array[month][j][1];

                if (attendence_info_array[month][j][0] != null && attendence_info_array[month][j][0].equalsIgnoreCase("0")) {

                    total_working_day += 1;
                    absent += 1;
                } else if (attendence_info_array[month][j][0] != null && attendence_info_array[month][j][0].equalsIgnoreCase("1")) {

                    total_working_day += 1;
                    present += 1;
                }
            }
        }



        if(total_working_day>0){

            percentage=(present/(float) total_working_day)*100;
        }
        else{
            percentage=0;
        }


        attendence_info.add(new Attendence_Info_Class2(year,month,status));
        RecycleAdapter recycleAdapter=new RecycleAdapter(getApplicationContext(),0,attendence_info);
        recyclerView.setAdapter(recycleAdapter);


    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<Attendence_Info_Class2> info;
        Context context;
        public RecycleAdapter(Context context, int resource,ArrayList<Attendence_Info_Class2> info){
            this.info=info;
            this.context=context;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView subject_name,assigned_date,submission_date,title,body;
            CalendarView calendarView;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                calendarView=mView.findViewById(R.id.calendarView);

            }



        }
        public  class ViewAdapter2 extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView tv_name,tv_id,tv_absent,tv_present,tv_total_working_day,tv_percentage;
            CalendarView calendarView;

            public ViewAdapter2(View itemView) {
                super(itemView);
                mView=itemView;
                tv_id=mView.findViewById(R.id.id);
                tv_name=mView.findViewById(R.id.name);
                tv_absent=mView.findViewById(R.id.absent);
                tv_present=mView.findViewById(R.id.present);
                tv_total_working_day=mView.findViewById(R.id.total_working_day);
                tv_percentage=mView.findViewById(R.id.percent);
                tv_id.setText("ID: "+student_id);
                tv_name.setText(studnet_name);
            }



        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if(viewType==0){

                View view= LayoutInflater.from(context).inflate(R.layout.attendance_top_data,parent,false);
                return new ViewAdapter2(view);
            }
            else{
                View view= LayoutInflater.from(context).inflate(R.layout.calender_item,parent,false);
                return new ViewAdapter(view);
            }

        }
        @Override
        public int getItemViewType(int position) {

                return position;


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {



            if(position==0){

                ViewAdapter2 viewAdapter2=(ViewAdapter2)holder;
                viewAdapter2.tv_total_working_day.setText("Total Working Day: "+total_working_day+"");
                viewAdapter2.tv_absent.setText("Absent: "+absent+"");
                viewAdapter2.tv_present.setText("Present: "+present+"");
                viewAdapter2.tv_percentage.setText("Percentage: "+percentage+"%");
            }
            else{

                ViewAdapter holder2=(ViewAdapter) holder;
                holder2.calendarView.setOnDayClickListener(new OnDayClickListener() {
                    @Override
                    public void onDayClick(EventDay eventDay) {


                    }
                });


                Calendar calendar = Calendar.getInstance();
                Attendence_Info_Class2 memberInfo=info.get(position);
                int month=memberInfo.month;
                int year=memberInfo.year;
                calendar.set(year,month,calendar.get(Calendar.DAY_OF_MONTH));
                try {
                    holder2.calendarView.setDate(calendar);
                } catch (OutOfDateRangeException e) {
                    e.printStackTrace();
                }
                String[][] status=memberInfo.status;
                List<EventDay> events2 = new ArrayList<>();
                calendar=Calendar.getInstance();
                for(int j=0;j<31;j++){

                    calendar=Calendar.getInstance();
                    calendar.set(year,month,j+1);
                    if(status[j][0]!=null&&status[j][0].equalsIgnoreCase("1")) events2.add(new EventDay(calendar,R.drawable.right));
                    else if(status[j][0]!=null&&status[j][0].equalsIgnoreCase("0")) events2.add(new EventDay(calendar,R.drawable.cross));

                }

//            calendar=Calendar.getInstance();
//            calendar.set(Calendar.YEAR,Calendar.MONTH,Calendar.DATE);
//            calendarView.setSelected(true);


                holder2.calendarView.setEvents(events2);
            }





        }

        @Override
        public int getItemCount() {

            return info.size();
        }



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
            view = inflter.inflate(R.layout.month_year_item_view, null);
            TextView names = (TextView) view.findViewById(R.id.month_year);

            if(flag==1){

                String member= info.get(i);
                names.setText(member);
            }


            return view;
        }
    }




    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("Attendance");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(student_id);
        progressDialog.setTitle("Please Wait..");
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_attendence_info_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String school_name=SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_name();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                String year="1111";
                                String[][][] attendece_info=null;
                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String[] date=member.getString("attendance_date").split("-");
                                        if(attendece_info==null){
                                            year=date[0];
                                            attendece_info=new String[12][31][2];
                                        }
                                        if(!date[0].equalsIgnoreCase(year)){

                                            years.add(year);
                                            hashMap.put(year,attendece_info);
                                            attendece_info=new String[12][31][2];
                                            year=date[0];
                                        }
                                        String status=member.getString("status");


                                        String cause=member.getString("cause");
                                        int month=Integer.parseInt(date[1])-1;
                                        int day=Integer.parseInt(date[2])-1;
                                        attendece_info[month][day][0]=status;
                                        attendece_info[month][day][1]=cause;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                hashMap.put(year,attendece_info);
                                years.add(year);
                                intit_listView();
                                progressDialog.dismiss();
                            }
                            else{
                                Calendar calendar=Calendar.getInstance();
                                int year=calendar.get(Calendar.YEAR);
                                years.add(year+"");
                                intit_listView();
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

    public void intit_listView(){
        attendence_info.clear();
        String[][] str=new String[31][2];
        attendence_info.add(new Attendence_Info_Class2(1, 1, str));
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        months.add("All");
        sp_month.setAdapter(new CustomAdapter(getApplicationContext(),1,months));
        sp_year.setAdapter(new CustomAdapter(getApplicationContext(),1,years));
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        sp_month.setSelection(month);
        sp_year.setSelection(years.indexOf(year+""));
        selected_year=year;

        total_working_day=0;
        absent=0;
        present=0;
        percentage=0;
        String[][][] attendence_info_array=hashMap.get(year+"");
        String[][] status=new String[31][2];



        for(int j=0;j<status.length;j++){

            if(attendence_info_array!=null) {
                status[j][0] = attendence_info_array[month][j][0];
                status[j][1] = attendence_info_array[month][j][1];

                if (attendence_info_array[month][j][0] != null && attendence_info_array[month][j][0].equalsIgnoreCase("0")) {

                    total_working_day += 1;
                    absent += 1;
                } else if (attendence_info_array[month][j][0] != null && attendence_info_array[month][j][0].equalsIgnoreCase("1")) {

                    total_working_day += 1;
                    present += 1;
                }
            }
        }



        if(total_working_day>0){

            percentage=(present/ (float) total_working_day)*100;
        }
        else{
            percentage=0;
        }
        attendence_info.add(new Attendence_Info_Class2(year,month,status));
        RecycleAdapter recycleAdapter=new RecycleAdapter(getApplicationContext(),0,attendence_info);
        recyclerView.setAdapter(recycleAdapter);







    }



}
