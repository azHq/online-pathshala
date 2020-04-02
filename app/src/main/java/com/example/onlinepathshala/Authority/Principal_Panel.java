package com.example.onlinepathshala.Authority;

import androidx.annotation.NonNull;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Teacher.Result_Form_Configuration;
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

public class Principal_Panel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBar actionBar;
    public DrawerLayout drawer;
    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    ProgressDialog progressDialog;
    static  String name,email,password,phone_number,image_path;
    TextView tv_email,tv_name;
    CircleImageView circleImageView;
    int num_notificatio;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal__panel2);
        fragmentManager=getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(Principal_Panel.this);
        progressDialog.setMessage("Please Wait...");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navheader=navigationView.getHeaderView(0);
        ft = getSupportFragmentManager().beginTransaction();
        tv_name=navheader.findViewById(R.id.name);
        tv_email=navheader.findViewById(R.id.email);

        circleImageView=navheader.findViewById(R.id.profile_image);

        navigationView.setNavigationItemSelectedListener(Principal_Panel.this);
         actionBar=getSupportActionBar();
        if(actionBar!=null) actionBar.setTitle("All Teachers");
        Fragment  fragment=new Authority_Profile();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
        if(name!=null&&name.length()>0){

            tv_email.setText(email);
            tv_name.setText(name);

            if(!image_path.equalsIgnoreCase("null")){


                Picasso.get().load(image_path).placeholder(R.drawable.profile10).into(circleImageView);
            }
        }
        else {
            getAllData();
        }



    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment = null;

        if (id == R.id.profile) {

            if(actionBar!=null) actionBar.setTitle("Profile");
            fragment=new Authority_Profile();
            changeFragmentView(fragment);

        }
        else if (id == R.id.teacher) {

            if(actionBar!=null) actionBar.setTitle("All Teachers");
            fragment=new All_Teachers();
            changeFragmentView(fragment);

        }
        else if (id == R.id.section) {

            if(actionBar!=null) actionBar.setTitle("Bangla Medium");
            fragment=new Section_Wise_Student(actionBar,"student");
            changeFragmentView(fragment);

        }
        else if (id == R.id.student) {

            if(actionBar!=null) actionBar.setTitle("All Students");
            fragment=new All_Students();
            changeFragmentView(fragment);

        } else if (id == R.id.classes) {

            if(actionBar!=null) actionBar.setTitle("All Classes");
            fragment=new Medium_Wise_Classes("Bangla","student");
            changeFragmentView(fragment);

        } else if (id == R.id.subject) {

            if(actionBar!=null) actionBar.setTitle("Bangla Medium");
            fragment=new Section_Wise_Student(actionBar,"subject");
            changeFragmentView(fragment);

        } else if (id == R.id.assign_sub) {

            if(actionBar!=null) actionBar.setTitle("Bangla Medium");
            fragment=new Section_Wise_Student(actionBar,"assign_subject");
            changeFragmentView(fragment);
        }
        else if (id == R.id.create_exam) {

            if(actionBar!=null) actionBar.setTitle("Create Exam");
            fragment=new Section_Wise_Student(actionBar,"exam");
            changeFragmentView(fragment);


        }
        else if (id == R.id.all_moderator) {

            if(actionBar!=null) actionBar.setTitle("All Moderators");
            fragment=new All_Moderator("editable");
            changeFragmentView(fragment);
        }
        else if (id == R.id.transaction) {

            if(actionBar!=null) actionBar.setTitle("Transaction");
            fragment=new Transaction();
            changeFragmentView(fragment);
        }
        else if (id == R.id.notice) {

            if(actionBar!=null) actionBar.setTitle("Upload Notice");
            fragment=new All_Notice_For_Auth();
            changeFragmentView(fragment);
        }
        else if (id == R.id.view_result) {


            if(actionBar!=null) actionBar.setTitle("View Result");
            fragment=new View_Result_Form_Config();
            changeFragmentView(fragment);

            //startActivity(new Intent(getApplicationContext(),View_Result3.class));

        }
        else if (id == R.id.attendance) {


            if(actionBar!=null) actionBar.setTitle("Attendance History");
            fragment=new All_Students_For_Attendance_History();
            changeFragmentView(fragment);

        }
        else if (id == R.id.contact) {


            if(actionBar!=null) actionBar.setTitle("Contact");
            fragment=new Authority_Contact_Activity(actionBar);
            changeFragmentView(fragment);

        }
        else if (id == R.id.upload_result) {


           startActivity(new Intent(getApplicationContext(), Result_Form_Configuration2.class));

        }
        else if (id == R.id.routine) {

            if(actionBar!=null) actionBar.setTitle("Routine");
            fragment=new View_Routine();
            changeFragmentView(fragment);
        }

        return true;
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


        AlertDialog.Builder alert=new AlertDialog.Builder(Principal_Panel.this);
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
        getMenuInflater().inflate(R.menu.principal_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.activity_main_alerts_menu_item:
                changeFragmentView(new All_Notifications_For_Auth(fragmentManager));
                return  true;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), System_Configuration.class));
                return true;
            case R.id.logout:
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(getApplicationContext(),Log_In.class));
                return true;
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

                actionBar.setTitle("Notifications");
               changeFragmentView(new All_Notifications_For_Auth(fragmentManager));
            }
        });
        getNumberOfNotificationUnSeen();
        return super.onPrepareOptionsMenu(menu);
    }

    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("Authority");
        postparams.put("online_pathshala");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        progressDialog.show();
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
                                        name=member.getString("authority_name");
                                        email=member.getString("email");
                                        phone_number=member.getString("phone_number");
                                        password =member.getString("password");
                                        image_path=member.getString("image_path");
                                        tv_name.setText(name);
                                        tv_email.setText(email);
                                        if(!image_path.equalsIgnoreCase("null")){


                                            Picasso.get().load(image_path).placeholder(R.drawable.profile10).into(circleImageView);
                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                progressDialog.dismiss();
                            }
                            else{
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
}
