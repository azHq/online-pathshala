package com.example.onlinepathshala.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Account_Deactive_Message;
import com.example.onlinepathshala.Accountant.Accountant_Dashboard;
import com.example.onlinepathshala.Authority.All_Student_In_A_Section;
import com.example.onlinepathshala.Authority.Principal_Panel;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.MainActivity;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.Student_Panel;
import com.example.onlinepathshala.Student_Info_Share;
import com.example.onlinepathshala.Stuff.Stuff_Dashboard;
import com.example.onlinepathshala.Teacher.Teacher_Dashboard;
import com.example.onlinepathshala.User;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Log_In extends AppCompatActivity {

    Spinner user_type_spinner,school_name_spinner;
    EditText et_user_name,et_password;
    TextView error;
    String[] users= {"Choose User Type","Student","Teacher","Principal","Vice-Principal","Accountant","Stuff","Admin"};
    String user_type="",school_name,user_id,password,school_id;
    ProgressDialog progressDialog;
    RelativeLayout relative_sch;
    ArrayList<School> school_info=new ArrayList<>();
    public String device_id="";
    public String table="";
    TextView tv_forget_password;
    String user_type2,school_name2,school_id2;
    final static int CODE_DRAW_OVER_OTHER_APP_PERMISSION=1;
    public boolean message_overlay_permission=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);
        progressDialog=new ProgressDialog(Log_In.this);
        user_type_spinner = (Spinner) findViewById(R.id.user_type);
        relative_sch=(RelativeLayout) findViewById(R.id.relative_sch);
        tv_forget_password=findViewById(R.id.forget_pasword);

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password_send_dialog();
            }
        });
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_text, users );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        user_type_spinner.setAdapter(langAdapter);


        user_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                user_type=users[position];
                if(user_type.contains("Admin")) relative_sch.setVisibility(View.GONE);
                else{

                    relative_sch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        school_name_spinner = (Spinner) findViewById(R.id.school_name);
        setSchoolList();
        school_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                school_name=school_info.get(position).school_name;
                school_id=school_info.get(position).school_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        error=findViewById(R.id.error);
        et_user_name=findViewById(R.id.username);
        et_password=findViewById(R.id.password);

    }
    public void password_send_dialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Log_In.this);
        View view = getLayoutInflater().inflate(R.layout.password_send_dialog, null);
        TextView tv_title = view.findViewById(R.id.title);
        Spinner user_type_sp= view.findViewById(R.id.user_type);
        Spinner school_name_sp= view.findViewById(R.id.school_name);
        RelativeLayout school=view.findViewById(R.id.relative_sch);
        final EditText value = view.findViewById(R.id.user_id);
        tv_title.setText("We Will Send Your Password To Your Phone Number");

        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_text, users );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        user_type_sp.setAdapter(langAdapter);



        user_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                user_type2=users[position];
                if(user_type2.contains("Admin")) school.setVisibility(View.GONE);
                else{

                    school.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        school_name_sp.setAdapter(new CustomAdapter(getApplicationContext(),1,school_info));
        school_name_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                school_name2=school_info.get(position).school_name;
                school_id2=school_info.get(position).school_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //5a3bea
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

                if (value.getText().toString().length() > 1&&value.getText().toString().length()<=10&&!user_type2.contains("Choose User Type")&&!school_name2.contains("Choose Your School")) {

                    progressDialog.show();
                    send_password_to_phone(value.getText().toString(),user_type2,school_id2);

                } else {

                    Toast.makeText(getApplicationContext(), "Please Enter Valid Info", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public  void send_password_to_phone(String id,String user_type,String schoo_id){



        if(user_type.equalsIgnoreCase("Admin")){

            table="Admin";

        }
        else if(user_type.equalsIgnoreCase("Principal")){

            table="Authority";

        }
        else if(user_type.equalsIgnoreCase("Vice-Principal")||user_type.equalsIgnoreCase("Accountant")||user_type.equalsIgnoreCase("Stuff")){

            table="Moderator";

        }
        else if(user_type.equalsIgnoreCase("Student")){

            table="Student";

        }
        else if(user_type.equalsIgnoreCase("Teacher")){

            table="Teacher";

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.send_password_to_phone_number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Password Sent Successfully", Toast.LENGTH_SHORT).show();


                        } else {

                            Toast.makeText(getApplicationContext(),"Fail to Send", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("table", table);
                params.put("school_id",schoo_id);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }
    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<School> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<School> info) {
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
            view = inflter.inflate(R.layout.school_list_item, null);
            CircleImageView circleImageView=view.findViewById(R.id.image);
            final TextView names = (TextView) view.findViewById(R.id.teacher_name);



            if(flag==1){
                School school=(School) info.get(i);
                names.setText(school.school_name);
                circleImageView.setImageResource(R.drawable.ic_school_black_24dp);
            }

            return view;
        }
    }

    public void signIn(View v){

        user_id=et_user_name.getText().toString();
        password=et_password.getText().toString();

        if(user_id.length()>0&&password.length()>0&&!user_type.contains("Choose User Type")&&!school_name.contains("Choose Your School")){

            error.setVisibility(View.INVISIBLE);
            progressDialog.setTitle("Sign In");
            progressDialog.setMessage("Please wait,You are signing ....");
            progressDialog.show();
            getDeviceId();
        }
        else if(user_id.length()>0&&password.length()>0&&user_type.contains("Admin")){

            error.setVisibility(View.INVISIBLE);
            progressDialog.setTitle("Sign In");
            progressDialog.setMessage("Please wait,You are signing ....");
            progressDialog.show();

            getDeviceId();

        }
        else{

            error.setVisibility(View.VISIBLE);
            error.setText(set_error_message(user_id,password,user_type,school_name));
        }


    }

    public String set_error_message(String user_id,String password,String user_type,String school_name){

        if(user_id.length()==0){

            return "please enter user id";
        }
        else if(password.length()==0) return "please enter password";
        else if(user_type.contains("Choose User Type")) return "please choose user type";
        else if(school_name.contains("Choose Your School")) return "please choose school name";

        return "Invalid input";
    }

    public void setSchoolList(){

        school_info.add(new School("0","Choose Your School"));
        school_name_spinner.setAdapter(new CustomAdapter(getApplicationContext(),1,school_info));
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_school_info_url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String school_id = member.getString("school_id");
                                        String school_name=member.getString("school_name");
                                        school_info.add(new School(school_id,school_name));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                school_name_spinner.setAdapter(new CustomAdapter(getApplicationContext(),1,school_info));


                            }
                            else{

                                Toast.makeText(getApplicationContext(),"No School Still Added",Toast.LENGTH_LONG).show();
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



    public void signIn(final String user_id, final String password, String user_type2, final String school_name,final String school_id,final String device_id){


        String url="";
        if(user_type.equalsIgnoreCase("Admin")){

            table="Admin";
            url= Constant_URL.admin_authority_login_url;
        }
        else if(user_type.equalsIgnoreCase("Principal")){

            table="Authority";
            url= Constant_URL.admin_authority_login_url;
        }
        else if(user_type.equalsIgnoreCase("Vice-Principal")||user_type.equalsIgnoreCase("Accountant")||user_type.equalsIgnoreCase("Stuff")){

            table="Moderator";
            url= Constant_URL.moderator_log_in_url;
        }
        else if(user_type.equalsIgnoreCase("Student")){

            table="Student";
            url= Constant_URL.student_teacher_log_in_url;
        }
        else if(user_type.equalsIgnoreCase("Teacher")){

            table="Teacher";
            url= Constant_URL.student_teacher_log_in_url;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();


                        if (response.contains("success")) {

                            Log_In.this.user_id=user_id;
                            Log_In.this.password=password;
                            Log_In.this.user_type2=user_type2;
                            Log_In.this.school_name=school_name;
                            Log_In.this.school_id=school_id;
                            Log_In.this.device_id=device_id;

                            Toast.makeText(getApplicationContext(), "Signed In Successfully", Toast.LENGTH_SHORT).show();


                            take_all_required_permission();


                        } else if (response.contains("de-active")) {


                            User user = new User(user_id, password, user_type, school_name, school_id, device_id);
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            setStudentInfo(user_id, school_id);
                            finish();
                            startActivity(new Intent(getApplicationContext(), Account_Deactive_Message.class));
                        } else {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("user_id", user_id);
                params.put("password", password);
                params.put("school_id", school_id);
                params.put("table",table);
                params.put("device_id",device_id);
                params.put("type",user_type);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void start_target_activity(){

            if(user_type.equalsIgnoreCase("Admin")){

                User user = new User(user_id,password,user_type,school_name,school_id,device_id);
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                finish();
                startActivity(new Intent(getApplicationContext(), Admin_Panel.class));
            }
            else if(user_type.equalsIgnoreCase("Principal")){

                User user = new User(user_id,password,user_type,school_name,school_id,device_id);
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                finish();
                startActivity(new Intent(getApplicationContext(), Principal_Panel.class));

            }
            else if(user_type.equalsIgnoreCase("Stuff")){

                User user = new User(user_id,password,user_type,school_name,school_id,device_id);
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                finish();
                startActivity(new Intent(getApplicationContext(), Stuff_Dashboard.class));
            }
            else if(user_type.equalsIgnoreCase("Accountant")){

                User user = new User(user_id,password,user_type,school_name,school_id,device_id);
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                finish();
                startActivity(new Intent(getApplicationContext(), Accountant_Dashboard.class));
            }
            else if(user_type.equalsIgnoreCase("Teacher")){


                User user = new User(user_id,password,user_type,school_name,school_id,device_id);
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                finish();
                startActivity(new Intent(getApplicationContext(), Teacher_Dashboard.class));
            }
            else if(user_type.equalsIgnoreCase("Student")){

                User user = new User(user_id,password,user_type,school_name,school_id,device_id);
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                setStudentInfo(user_id,school_id);
                finish();
                startActivity(new Intent(getApplicationContext(), Student_Panel.class));
            }




    }

    public void connection_error_message(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Log_In.this);
        builder.setMessage("Please Check Your Internet Connection");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void getDeviceId(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
                            return;
                        }

                        device_id = task.getResult().getToken();
                        signIn(user_id,password,user_type,school_name,school_id,device_id);


                    }
                });

    }

    public void setStudentInfo(String user_id,String school_id){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(school_id);
        postparams.put(user_id);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_single_student_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);
                            Student_Info_Share student_additional_info=null;
                            if(!string.contains("no item")) {

                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String name = member.getString("student_name");
                                        String email = member.getString("email");
                                        String class_id = member.getString("class_id");
                                        String section_id = member.getString("section_id");
                                        String image_path = member.getString("image_path");
                                        String phone_number = member.getString("phone_number");
                                        student_additional_info = new Student_Info_Share(id, name,image_path,email,phone_number,class_id,section_id );
                                        SharedPrefManager.getInstance(getApplicationContext()).student_info(student_additional_info);

                                        System.out.println(id+","+name+","+email+","+class_id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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

    public void take_all_required_permission()
    {
        OvelayIconPerrmission();

    }

    public void OvelayIconPerrmission(){

        if(Build.VERSION.SDK_INT >= 23)
        {
            if(!Settings.canDrawOverlays(Log_In.this))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:"+getPackageName()));
                startActivityForResult(intent,CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }
            else {

                start_target_activity();
            }
        }
        else{
            Intent intent = new Intent(Log_In.this, Service.class);
            startService(intent);
            start_target_activity();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {

                start_target_activity();


            } else {

                start_target_activity();

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
