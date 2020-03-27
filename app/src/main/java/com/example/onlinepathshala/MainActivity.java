package com.example.onlinepathshala;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Accountant.Accountant_Dashboard;
import com.example.onlinepathshala.Admin.Admin_Panel;
import com.example.onlinepathshala.Admin.Log_In;
import com.example.onlinepathshala.Authority.Principal_Panel;
import com.example.onlinepathshala.Student.Student_Panel;
import com.example.onlinepathshala.Stuff.Stuff_Dashboard;
import com.example.onlinepathshala.Teacher.Teacher_Dashboard;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity  {
    final static int CODE_DRAW_OVER_OTHER_APP_PERMISSION=1;
    public boolean message_overlay_permission=false;
    public static TextView countTextView=null;
    public static FrameLayout redCircle=null;
    Handler handler;
    Runnable runnable;
    public String status;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        ImageView logo=findViewById(R.id.logo);
        logo.startAnimation(animation);
        progressDialog=new ProgressDialog(MainActivity.this);
        countTextView=null;
        redCircle=null;
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {

                if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) check_account_status_connection();
                else{
                    finish();
                    startActivity(new Intent(getApplicationContext(),Log_In.class));
                }
            }
        };
        handler.postDelayed(runnable,2000);











    }

    public void start_target_activity(String status){


        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            User user=SharedPrefManager.getInstance(getApplicationContext()).getUser();
            finish();
            if(user.user_type.equalsIgnoreCase("Admin")){

                startActivity(new Intent(getApplicationContext(), Admin_Panel.class));

            }
            else if(status.equalsIgnoreCase("active")){

                 if(user.user_type.equalsIgnoreCase("Principal")){

                    startActivity(new Intent(getApplicationContext(), Principal_Panel.class));
                }
                else if(user.user_type.equalsIgnoreCase("Vice-Principal")){

                    startActivity(new Intent(getApplicationContext(), Principal_Panel.class));
                }
                else if(user.user_type.equalsIgnoreCase("Stuff")){

                    startActivity(new Intent(getApplicationContext(), Stuff_Dashboard.class));
                }
                else if(user.user_type.equalsIgnoreCase("Accountant")){

                    startActivity(new Intent(getApplicationContext(), Accountant_Dashboard.class));
                }
                else if(user.user_type.equalsIgnoreCase("Teacher")){

                    startActivity(new Intent(getApplicationContext(), Teacher_Dashboard.class));
                }
                else if(user.user_type.equalsIgnoreCase("Student")){

                    startActivity(new Intent(getApplicationContext(), Student_Panel.class));

                }
                else {

                    startActivity(new Intent(getApplicationContext(), Log_In.class));
                }
            }
            else{

                finish();
                startActivity(new Intent(getApplicationContext(),Account_Deactive_Message.class));
            }


        }
        else {

            finish();
            startActivity(new Intent(getApplicationContext(), Log_In.class));
        }

    }


    public void check_account_status_connection(){

            progressDialog.setMessage("Please Wait...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.get_check_account_status_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                            progressDialog.dismiss();
                            System.out.println(response.toString());
                            if (response.contains("active")) {

                                take_all_required_permissions("active");


                            } else {
                                take_all_required_permissions("de-active");

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            connection_error_message();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                    return params;
                }
            };

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }

    public  void take_all_required_permissions(String status)
    {

        this.status=status;
        OvelayIconPerrmission();
    }

    public void connection_error_message(){

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please Check Your Internet Connection");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }



    public void OvelayIconPerrmission(){

        if(Build.VERSION.SDK_INT >= 23)
        {
            if(!Settings.canDrawOverlays(MainActivity.this))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:"+getPackageName()));
                startActivityForResult(intent,CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }
            else {

                start_target_activity(status);
            }
        }
        else{
            Intent intent = new Intent(MainActivity.this, Service.class);
            startService(intent);
            start_target_activity(status);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(handler!=null){
            handler.removeCallbacks(null);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {

               start_target_activity(status);


            } else {

                start_target_activity(status);

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
