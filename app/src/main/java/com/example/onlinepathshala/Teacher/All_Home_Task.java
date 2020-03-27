package com.example.onlinepathshala.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.Home_Task_Info;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class All_Home_Task extends AppCompatActivity implements View.OnTouchListener {

    RecyclerView recyclerView;
    ArrayList<Home_Task_Info> home_task_infos;
    ProgressDialog progressDialog;
    float dX;
    float dY;
    int lastAction;
    String class_id,section_id;
    FloatingActionButton dragView;
    TextView no_item;
    float posX=0;
    float posY=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__home__task);
        recyclerView=findViewById(R.id.recycle);
        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");
        dragView = findViewById(R.id.fab);
        no_item=findViewById(R.id.no_item);
        dragView.setOnTouchListener(this);

        progressDialog=new ProgressDialog(All_Home_Task.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        home_task_infos=new ArrayList<>();
        getAllMemberData();
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
                if (lastAction == MotionEvent.ACTION_DOWN){
                    String task_number="0";
                    if(home_task_infos.size()>0){
                        task_number=home_task_infos.get(home_task_infos.size()-1).task_number;
                    }


                    Intent tnt=new Intent(getApplicationContext(), Assign_HomeTask.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",section_id);
                    tnt.putExtra("task_number",task_number);
                    tnt.putExtra("type","home task");
                    startActivity(tnt);
                }

                break;

            default:
                return false;
        }
        return true;
    }



    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Home_Task");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_section_wise_task,postparams,
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
                                        String id = member.getString("id");
                                        String subject_name=member.getString("subject_name");
                                        String assigned_date = member.getString("assigned_date");
                                        String submission_date = member.getString("submission_date");
                                        String title = member.getString("title");
                                        String body = member.getString("details");
                                        String task_number= member.getString("task_number");
                                        home_task_infos.add(new Home_Task_Info(id,subject_name,assigned_date,submission_date,title,body,null,task_number));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                no_item.setVisibility(View.GONE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(home_task_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(home_task_infos);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No Home Task Still Assigned",Toast.LENGTH_LONG).show();
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.layout2.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            return true;
        }
        else if(id==R.id.logout){


        }

        return super.onOptionsItemSelected(item);
    }



    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Home_Task_Info> home_task_infos;
        public RecycleAdapter(ArrayList<Home_Task_Info> classinfos){
            this.home_task_infos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView subject_name,assigned_date,submission_date,title,body;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                subject_name=mView.findViewById(R.id.subject_name);
                assigned_date=mView.findViewById(R.id.assign_date);
                submission_date=mView.findViewById(R.id.submission_date);
                title=mView.findViewById(R.id.title);
                body=mView.findViewById(R.id.body);
                body.setMovementMethod(new ScrollingMovementMethod());
                subject_name.setSelected(true);
                title.setSelected(true);
            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_task_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Home_Task_Info memberInfo=home_task_infos.get(position);
            holder.subject_name.setText(memberInfo.subject_name);
            holder.assigned_date.setText(memberInfo.assigned_date);
            holder.submission_date.setText(memberInfo.submission_date);
            holder.title.setText(memberInfo.tile);
            holder.body.setText(memberInfo.body);



        }

        @Override
        public int getItemCount() {
            return home_task_infos.size();
        }



    }

}
