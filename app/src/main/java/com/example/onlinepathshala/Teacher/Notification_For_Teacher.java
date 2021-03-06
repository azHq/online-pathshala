package com.example.onlinepathshala.Teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.MainActivity;
import com.example.onlinepathshala.Notification_list_Item;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.Result;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Notification_For_Teacher extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Notification_list_Item> notification_info=new ArrayList<>();
    ProgressDialog progressDialog;
    RecycleAdapter recycleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        progressDialog=new ProgressDialog(Notification_For_Teacher.this);
        recyclerView=findViewById(R.id.recycle);
        recycleAdapter=new RecycleAdapter(notification_info);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        notification_info=new ArrayList<>();
        recycleAdapter=new RecycleAdapter(notification_info);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.notifyDataSetChanged();
        getAllMemberData();
    }


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Notification");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type());
        postparams.put("All");

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_notification_url,postparams,
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
                                        String user_id=member.getString("user_id");
                                        String user_type = member.getString("user_type");
                                        String sender_id = member.getString("sender_id");
                                        String sender_type = member.getString("sender_type");
                                        String notification_type = member.getString("notification_type");
                                        String body = member.getString("body");
                                        String date = member.getString("create_at");
                                        String create_time = member.getString("create_time");
                                        String status = member.getString("status");
                                        notification_info.add(new Notification_list_Item(id,user_id,user_type,sender_id,sender_type,notification_type,body,date,create_time,status));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                recycleAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                            else{
                                recycleAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No Notification Comes Yet",Toast.LENGTH_LONG).show();
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

        ArrayList<Notification_list_Item> notification_info;
        public RecycleAdapter(ArrayList<Notification_list_Item> notification_info){
            this.notification_info=notification_info;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            RelativeLayout item;
            LinearLayout status;
            TextView type,body,date,time;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                type=mView.findViewById(R.id.notification_type);
                body=mView.findViewById(R.id.body);
                date=mView.findViewById(R.id.date);
                time=mView.findViewById(R.id.time);
                status=mView.findViewById(R.id.status);
                item=mView.findViewById(R.id.item);

            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            final Notification_list_Item memberInfo=notification_info.get(position);
            holder.type.setText(memberInfo.notification_type);
            holder.body.setText(memberInfo.body);
            holder.date.setText(memberInfo.date);
            holder.time.setText(memberInfo.time);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(MainActivity.countTextView!=null&&MainActivity.countTextView.getText()!=null&&!MainActivity.countTextView.getText().toString().equalsIgnoreCase("")){
                        int count=Integer.parseInt(MainActivity.countTextView.getText().toString())-1;
                        MainActivity.countTextView.setText(count+"");
                    }
                    changestatus(memberInfo.id);

                    if(memberInfo.notification_type.equalsIgnoreCase("Message")) startActivity(new Intent(getApplicationContext(), Assign_HomeTask.class));
                }
            });

            if( memberInfo.status.equalsIgnoreCase("0")) holder.status.setVisibility(View.VISIBLE);



        }

        @Override
        public int getItemCount() {
            return notification_info.size();
        }



    }

    public void changestatus(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.change_notification_status_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("table", "Notification");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
