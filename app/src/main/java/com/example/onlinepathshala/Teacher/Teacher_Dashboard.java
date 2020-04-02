package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Accountant.Notice_View;
import com.example.onlinepathshala.Admin.Log_In;
import com.example.onlinepathshala.Authority.Result_Form_Configuration2;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.MainActivity;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Teacher_Dashboard extends AppCompatActivity {

    public static int num_notificatio=0;
    RecyclerView recyclerView;
    int[][] image_path={{R.drawable.male_profile,R.drawable.hometask},{R.drawable.result3,R.drawable.my_student},{R.drawable.attendence,R.drawable.result3},{R.drawable.result3,R.drawable.result3},{R.drawable.routine,R.drawable.contact3},{R.drawable.transaction3,R.drawable.notice2}};
    String[][] name={{"Profile","Home Task"},{"My Classes","My Students"},{"Attendance","Attendance History"},{"Publish Result","View Result"},{"Routine","Contact"},{"Transaction","Notice"}};
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__panel);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecycleAdapter recycleAdapter=new RecycleAdapter(image_path,name);
        recyclerView.setAdapter(recycleAdapter);

    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        int[][] image_path;
        String[][] name;
        public RecycleAdapter(int[][] image_path, String[][] name){
            this.image_path=image_path;
            this.name=name;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            CardView card1,card2;
            TextView text1,text2;
            ImageView image1,image2;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                card1=mView.findViewById(R.id.card1);
                card2=mView.findViewById(R.id.card2);
                text1=mView.findViewById(R.id.text1);
                text2=mView.findViewById(R.id.text2);
                image1=mView.findViewById(R.id.image1);
                image2=mView.findViewById(R.id.image2);



            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_panel_recycle_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            holder.card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(position==0){

                        startActivity(new Intent(getApplicationContext(),Teacher_Profile.class));
                    }
                    else if(position==1){

                        startActivity(new Intent(getApplicationContext(), My_Classes_Teacher.class));
                    }
                    else if(position==2){

                        Intent tnt=new Intent(getApplicationContext(),Teacher_Classes_For_Attendence.class);
                        tnt.putExtra("activity_type","attendence");
                        startActivity(new Intent(tnt));

                    }
                    else if(position==3){

                        startActivity(new Intent(getApplicationContext(), Result_Form_Configuration.class));

                    }
                    else if(position==4){

                        startActivity(new Intent(getApplicationContext(),Routine_View.class));

                    }
                    else if(position==5){
                        startActivity(new Intent(getApplicationContext(),Teacher_Transaction.class));
                    }

                }
            });
            holder.card2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(position==0){

                        Intent tnt=new Intent(getApplicationContext(),Teacher_Classes_For_Attendence.class);
                        tnt.putExtra("activity_type","home task");
                        startActivity(new Intent(tnt));
                    }
                    else if(position==1){

                        startActivity(new Intent(getApplicationContext(), My_Students.class));

                    }
                    else if(position==2){

                        startActivity(new Intent(getApplicationContext(),Attendance_View_For_Teacher.class));


                    }
                    else if(position==3){

                        startActivity(new Intent(getApplicationContext(), View_Result2_Form_Config.class));

                    }
                    else if(position==4){

                        startActivity(new Intent(getApplicationContext(), Teacher_Contact.class));

                    }
                    else if(position==5){

                        startActivity(new Intent(getApplicationContext(), Notice_View.class));
                    }
                }
            });


            holder.image1.setImageResource(image_path[position][0]);
            holder.image2.setImageResource(image_path[position][1]);

            holder.text1.setText(name[position][0]);
            holder.text2.setText(name[position][1]);

            holder.text1.setSelected(true);
            holder.text2.setSelected(true);




        }

        @Override
        public int getItemCount() {
            return name.length;
        }



    }









    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_main_alerts_menu_item:
               startActivity(new Intent(getApplicationContext(),Notification_For_Teacher.class));
            case R.id.logout:
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(getApplicationContext(),Log_In.class));
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onBackPressed() {

            show_exit_dialog();

    }

    public void show_exit_dialog(){


        AlertDialog.Builder alert=new AlertDialog.Builder(Teacher_Dashboard.this);
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
