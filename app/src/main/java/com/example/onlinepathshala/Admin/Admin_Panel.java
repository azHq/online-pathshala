package com.example.onlinepathshala.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
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

public class Admin_Panel extends AppCompatActivity implements View.OnTouchListener {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__panel);
        dragView = findViewById(R.id.fab);
        recyclerView=findViewById(R.id.recycle);
        no_item=findViewById(R.id.no_item);
        progressDialog=new ProgressDialog(Admin_Panel.this);
        progressDialog.setMessage("Please wait...");
        dragView.setOnTouchListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        memberInfos=new ArrayList<>();
        getAllMemberData();
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Authority");
        postparams.put("online_pathshala");

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_school,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        System.out.println("Response: "+response);
                        String string= "";
                        try {
                            string = response.getString(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        memberInfos.clear();
                        if(!string.contains("no item")){

                            for(int i=0;i<response.length();i++){

                                JSONObject member = null;
                                try {
                                    member = response.getJSONObject(i);
                                    String authority_id = member.getString("id");
                                    String school_name = member.getString("school_name");
                                    String school_id = member.getString("school_id");
                                    String authority_name = member.getString("authority_name");
                                    String image_path=member.getString("image_path");
                                    String status=member.getString("account_status");

                                    String email = member.getString("email");
                                    String phone=member.getString("phone_number");
                                    String date=member.getString("create_at");
                                    String device_id=member.getString("device_id");
                                    memberInfos.add(new School(school_id,school_name,authority_id,authority_name,email,phone,date,status,image_path,device_id));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            no_item.setVisibility(GONE);
                            RecycleAdapter recycleAdapter=new RecycleAdapter(memberInfos);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(recycleAdapter);
                            progressDialog.dismiss();
                        }
                        else{

                            no_item.setVisibility(VISIBLE);
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

        ArrayList<School> memberInfos;
        public RecycleAdapter(ArrayList<School> memberInfos){
            this.memberInfos=memberInfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            View mView;
            Button active,de_active;
            LinearLayout linearLayout;
            ImageView school_image;
            TextView authority_name,authority_id,school_name,id,status;
            SwitchCompat stw_acc;
            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                school_image=mView.findViewById(R.id.school_image);
                status=mView.findViewById(R.id.status);
                linearLayout=mView.findViewById(R.id.view_profile);

                id=mView.findViewById(R.id.id);
                school_name=mView.findViewById(R.id.school_name);
                authority_name=mView.findViewById(R.id.authority_name);
                authority_id=mView.findViewById(R.id.authority_id);
                stw_acc=mView.findViewById(R.id.swith);

            }
            @Override
            public boolean onLongClick(View view) {


                int position =getLayoutPosition();
                School memberInfo=memberInfos.get(position);
                showOptionDialog(memberInfo.authority_id,memberInfo.school_id);
                return true;
            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_school,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            School memberInfo=memberInfos.get(position);
            holder.id.setText(memberInfo.school_id);
            holder.school_name.setText(memberInfo.school_name);
            holder.authority_id.setText(memberInfo.authority_id);
            holder.authority_name.setText(memberInfo.authority_name);
            if(!memberInfo.image.equalsIgnoreCase("null")){

                Picasso.get().load(memberInfo.image).placeholder(R.drawable.teacher_panel_header).into(holder.school_image);
            }
            if(memberInfo.account_status.equalsIgnoreCase("1")){

                holder.status.setText("Active");
                holder.stw_acc.setChecked(true);
            }
            else {
                holder.status.setText("De-Active");
                holder.stw_acc.setChecked(false);
            }
            holder.stw_acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b){

                        holder.status.setText("Active");
                    }
                    else{

                        holder.status.setText("De-Active");
                    }
                }
            });

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(getApplicationContext(),School_Profile.class);
                    intent.putExtra("school_id",memberInfo.school_id);
                    intent.putExtra("school_name",memberInfo.school_name);
                    intent.putExtra("auth_id",memberInfo.authority_id);
                    intent.putExtra("auth_name",memberInfo.authority_name);
                    intent.putExtra("image_path",memberInfo.image);
                    intent.putExtra("phone_number",memberInfo.phone_number);
                    intent.putExtra("email",memberInfo.email);
                    intent.putExtra("device_id",memberInfo.device_id);
                    intent.putExtra("status",memberInfo.account_status);
                    intent.putExtra("date",memberInfo.joining_date);
                    startActivity(intent);
                }
            });

            holder.stw_acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b){

                        holder.status.setText("Active");
                        showDialog("Are you sure to Active this account ?","1",memberInfo.authority_id);
                    }
                    else{

                        holder.status.setText("De-Active");
                        showDialog("Are you sure to De-active this account ?","0",memberInfo.authority_id);
                    }
                }
            });




        }

        @Override
        public int getItemCount() {
            return memberInfos.size();
        }



    }
    public void showDialog(String message,String value,String id){

        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Panel.this);
        View view = getLayoutInflater().inflate(R.layout.profile_edit_dialogbox2, null);
        TextView tv_title = view.findViewById(R.id.title);
        tv_title.setText(message);
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
                dialog.dismiss();
                progressDialog.show();
                update(value, "account_status",id);

            }
        });
    }






    public void update(final String value, final String column,String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.change_account_status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),column+" Updated Successfully", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getApplicationContext(), "Fail to update", Toast.LENGTH_SHORT).show();
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
                params.put("value", value);
                params.put("id",id);
                params.put("column",column);
                params.put("table", "Authority");
                params.put("db","online_pathshala");
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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

    @Override
    public boolean onTouch(View view, MotionEvent event) {




        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels-view.getHeight();
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
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    Intent tnt = new Intent(getApplicationContext(), Add_School.class);
                    startActivity(tnt);
                }
                break;

            default:
                return false;
        }
        return true;
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


    public void showOptionDialog(final String id,final String school_id){

        AlertDialog.Builder builder=new AlertDialog.Builder(Admin_Panel.this);
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        alertDialog2=builder.show();
        assign_teacher.setVisibility(View.GONE);
        edit.setVisibility(GONE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showDeleteDialogBox(id,school_id);
            }
        });


    }


    public void showDeleteDialogBox(final String id,final String school_id){

        AlertDialog.Builder alert=new AlertDialog.Builder(Admin_Panel.this);
        View view=getLayoutInflater().inflate(R.layout.delete_dialog_box,null);
        Button yes=view.findViewById(R.id.yes);
        Button no=view.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                progressDialog.show();
                deleteItem(id,school_id);
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

    public void deleteItem(final String id,final String school_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.delete_authority,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Student Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

                        } else {
                            Toast.makeText(getApplicationContext(),"Fail To Delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Connect Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("table", "Authority");
                params.put("db","online_pathshala");
                params.put("school_id",school_id);

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }





}
