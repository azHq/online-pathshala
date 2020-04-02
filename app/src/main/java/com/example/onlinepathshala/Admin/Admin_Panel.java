package com.example.onlinepathshala.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Authority.Add_New_Student;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.MainActivity;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Teacher.Notification_For_Teacher;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Admin_Panel extends AppCompatActivity{

    ArrayList<School> memberInfos=new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    public static int num_notificatio=0;
    float dX;
    float dY;
    int lastAction;
    FloatingActionButton dragView;
    TextView no_item;
    AlertDialog alertDialog,alertDialog2;
    float posX=0;
    float posY=0;
    public DrawerLayout drawer;
    public FragmentTransaction ft;
    public FragmentManager fragmentManager;
    public static Context context;
    private int alertCount = 0;
    public int notification=0;
    public ActionBar actionBar;
    TextView tv_name,tv_email;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__panel);
        progressDialog=new ProgressDialog(Admin_Panel.this);
        progressDialog.setMessage("Please wait...");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_name=findViewById(R.id.name);
        tv_email=findViewById(R.id.email);
        profile_image=findViewById(R.id.profile_image);
        setSupportActionBar(toolbar);
        notification=getIntent().getIntExtra("notification",0);
        fragmentManager =getSupportFragmentManager();
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
                changeFragmentView(new Admin_Profile());

            }
        });
        LinearLayout all_school=findViewById(R.id.all_schools);
        all_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("All School");
                changeFragmentView(new All_Schools());
            }
        });
        LinearLayout all_authority=findViewById(R.id.all_authority);
        all_authority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("All Authority");
                changeFragmentView(new All_Authority(actionBar));
            }
        });

//        LinearLayout contact=findViewById(R.id.contact);
//        contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                actionBar.setTitle("Contact");
//                changeFragmentView(new Contact());
//            }
//        });
        LinearLayout about=findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionBar.setTitle("About");
                changeFragmentView(new About());
            }
        });

        actionBar.setTitle("All Schools");
        changeFragmentView(new All_Schools());

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

    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("admin");
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
                                        String name=member.getString("name");

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.teacher_menu, menu);
        return super.onCreateOptionsMenu(menu);
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

                startActivity(new Intent(getApplicationContext(),Notification_For_Teacher.class));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_main_alerts_menu_item:
                startActivity(new Intent(getApplicationContext(), Notification_For_Teacher.class));
            case R.id.logout:
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(getApplicationContext(),Log_In.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
