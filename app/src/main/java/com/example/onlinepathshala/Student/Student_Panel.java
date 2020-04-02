package com.example.onlinepathshala.Student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Admin.Log_In;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.MainActivity;
import com.example.onlinepathshala.Notice;
import com.example.onlinepathshala.Notification;
import com.example.onlinepathshala.Payment;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Teacher.Teacher_Dashboard;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Student_Panel extends AppCompatActivity  {

    public DrawerLayout drawer;
    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    public static Context context;
    private int alertCount = 0;
    public int notification=0;
    public static int num_notificatio=0;
    public ActionBar actionBar;
    AlertDialog alertDialog;
    TextView tv_name,tv_email;
    CircleImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__panel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notification=getIntent().getIntExtra("notification",0);
        fragmentManager =getSupportFragmentManager();
        tv_name=findViewById(R.id.name);
        tv_email=findViewById(R.id.email);
        profile_image=findViewById(R.id.profile_image);
        actionBar=getSupportActionBar();
        context=getApplicationContext();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getAllData();
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ft = getSupportFragmentManager().beginTransaction();

        LinearLayout profile=findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("Profile");
                changeFragmentView(new Student_profile());

            }
        });
        LinearLayout home_task=findViewById(R.id.home_task);
        home_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("Home Task");
                changeFragmentView(new Home_Task());
            }
        });
        LinearLayout my_classes=findViewById(R.id.my_classes);
        my_classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("My Classes");
                changeFragmentView(new My_Classes(actionBar));
            }
        });

        LinearLayout teachers=findViewById(R.id.teachers);
        teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("All Teachers");
                changeFragmentView(new Teacher_List());
            }
        });
        LinearLayout pay_fees=findViewById(R.id.pay_fees);
        pay_fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               startActivity(new Intent(getApplicationContext(), Payment.class));
            }
        });
        LinearLayout notice=findViewById(R.id.notice);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("Notice");
                changeFragmentView(new Notice());
            }
        });

        LinearLayout attendence=findViewById(R.id.attendence);
        attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              startActivity(new Intent(getApplicationContext(),Attendence_History_Canlander_View.class));
            }
        });

        LinearLayout result=findViewById(R.id.result);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("Result");
                changeFragmentView(new Result());
            }
        });

        LinearLayout routine=findViewById(R.id.routine);
        routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("Routine");
                changeFragmentView(new Student_Routine());
            }
        });

        LinearLayout exam=findViewById(R.id.exam);
        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("Up Coming Exams");
                changeFragmentView(new Up_Coming_Exam());
            }
        });

        LinearLayout contact=findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionBar.setTitle("Contact");
                changeFragmentView(new Contact(actionBar));
            }
        });

        LinearLayout transation_history=findViewById(R.id.transaction_history);
        transation_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("Transaction History");
                changeFragmentView(new Transaction_History_For_Student());
            }
        });

        LinearLayout logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPrefManager.getInstance(getApplicationContext()).logout();
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), Log_In.class));

            }
        });

        if(notification==1){

            actionBar.setTitle("Notification");
            changeFragmentView(new Notification(fragmentManager));
        }
        actionBar.setTitle("Profile");
        changeFragmentView(new Student_profile());
        drawer.openDrawer(GravityCompat.START);

    }

    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.select_single_row,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String school_name=SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_name();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String name=member.getString("student_name");

                                        if(member.has("email")){
                                            String email=member.getString("email");
                                            tv_email.setText(email);
                                        }
                                        else{

                                        }



                                        if(member.has("image_path")){
                                            String  image_path=member.getString("image_path");
                                            if(!image_path.equalsIgnoreCase("null")){


                                                Picasso.get().load(image_path).placeholder(R.drawable.profile2).into(profile_image);
                                            }
                                        }







                                        tv_name.setText(name);

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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.countTextView=null;
        MainActivity.redCircle=null;
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    public void changeFragmentView(Fragment fragment){

        fragmentManager =getSupportFragmentManager();
        fragmentManager.popBackStack();
        int count = fragmentManager.getBackStackEntryCount();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment)
                .addToBackStack(null).commit();
        drawer.closeDrawer(GravityCompat.START);
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.activity_main_alerts_menu_item:
                changeFragmentView(new Notification(fragmentManager));
            case R.id.logout:
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(getApplicationContext(),Log_In.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.activity_main_alerts_menu_item);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        MainActivity.redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        MainActivity.countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);
        MainActivity.redCircle.setVisibility(INVISIBLE);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeFragmentView(new Notification(fragmentManager));
            }
        });
        getNumberOfNotificationUnSeen();
        return super.onPrepareOptionsMenu(menu);
    }

    public void getNumberOfNotificationUnSeen(){

        JSONArray postparams = new JSONArray();
        postparams.put("Notification");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type());
        postparams.put("count");
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_notification_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")&&!string.contains("Failed")){
                                MainActivity.redCircle.setVisibility(VISIBLE);
                                MainActivity.countTextView.setText(string);
                                num_notificatio=Integer.parseInt(string);

                            }
                            else{

                                MainActivity.redCircle.setVisibility(INVISIBLE);

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





    @Override
    public void onBackPressed() {

        int count = fragmentManager.getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(count==1){

            show_exit_dialog();
        }
        else {

            super.onBackPressed();
        }




    }

    public void show_exit_dialog(){


        AlertDialog.Builder alert=new AlertDialog.Builder(Student_Panel.this);
        View view=getLayoutInflater().inflate(R.layout.exit_permission,null);
        Button yes=view.findViewById(R.id.yes);
        Button no=view.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                finish();

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
}
