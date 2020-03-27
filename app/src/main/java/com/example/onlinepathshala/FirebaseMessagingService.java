package com.example.onlinepathshala;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Admin.Log_In;
import com.example.onlinepathshala.Authority.Messages;
import com.example.onlinepathshala.Authority.Messenger;
import com.example.onlinepathshala.Student.Home_Task;
import com.example.onlinepathshala.Student.Student_Panel;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.onlinepathshala.Authority.Messenger.sender_id;
import static com.example.onlinepathshala.Authority.Messenger.receiver_id;
import static com.example.onlinepathshala.Authority.Messenger.receiver_type;
import static com.example.onlinepathshala.Authority.Messenger.sender_type;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    String message,title,sender_id2,sender_type2,sender_device_id,receiver_device_id;
    public JSONObject jsonObject=null,dataObject;
    public  FirebaseMessagingService(){
        Messenger.messages.clear();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        try {
            jsonObject = new JSONObject(remoteMessage.getData().toString());
            JSONObject messageObject=jsonObject.getJSONObject("message");
            title = messageObject.getString("title");
            message=messageObject.getString("message");
            dataObject = jsonObject.getJSONObject("data");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(title.contains("New Message From")&&dataObject!=null){

            try {
                sender_id2=dataObject.getString("sender_id");
                sender_type2=dataObject.getString("sender_type");
                sender_device_id=dataObject.getString("sender_device_id");
                receiver_device_id=dataObject.getString("receiver_device_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(Messenger.context!=null&&receiver_id.equalsIgnoreCase(sender_id2)){

                    refresh();

            }
            else{

                createOverLayFloatingIcon();
            }
        }

        else if(title.contains("Home Task")){

            Intent tnt=new Intent(FirebaseMessagingService.this, All_Notification_Service.class);
            tnt.putExtra("title","New Home Task");
            tnt.putExtra("message",message);
            startService(tnt);
        }
        else if(title.contains("Result")){

            Intent tnt=new Intent(FirebaseMessagingService.this, All_Notification_Service.class);
            tnt.putExtra("title","Result Publish");
            tnt.putExtra("message",message);
            startService(tnt);
        }
        else if(title.contains("Notice")){

            Intent tnt=new Intent(FirebaseMessagingService.this, All_Notification_Service.class);
            tnt.putExtra("title","Notice");
            tnt.putExtra("message",message);
            startService(tnt);
        }
        else if(title.contains("Routine")){

            Intent tnt=new Intent(FirebaseMessagingService.this, All_Notification_Service.class);
            tnt.putExtra("title","Routine Uploaded");
            tnt.putExtra("message",message);
            startService(tnt);
        }
        else if(title.contains("Exam")){

            Intent tnt=new Intent(FirebaseMessagingService.this, All_Notification_Service.class);
            tnt.putExtra("title","New Exam");
            tnt.putExtra("message",message);
            startService(tnt);
        }


    }

    public void createOverLayFloatingIcon(){

        Intent tnt=new Intent(FirebaseMessagingService.this, FloatingViewService.class);
        tnt.putExtra("sender_id",sender_id2);
        tnt.putExtra("sender_type",sender_type2);
        tnt.putExtra("sender_device_id",sender_device_id);
        tnt.putExtra("receiver_device_id",receiver_device_id);
        startService(tnt);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        updateDeviceId(s);
    }

    public void refresh(){
        getAllMessage();
    }
    private void getAllMessage() {

        JSONArray postparams = new JSONArray();
        postparams.put("Message_Table");
        postparams.put(SharedPrefManager.getInstance(Messenger.context).getUser().getSchool_id());
        postparams.put(sender_id);
        postparams.put(receiver_id);
        postparams.put(sender_type);
        postparams.put(receiver_type);


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_message_url,postparams,
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
                                        String message = member.getString("message");
                                        String sender_id=member.getString("sender_id");
                                        String sender_type=member.getString("sender_type");
                                        String receiver_id=member.getString("receiver_id");
                                        String receiver_type=member.getString("receiver_type");
                                        String time=member.getString("sent_time");
                                        String date=member.getString("sent_date");
                                        Messenger.messages.add(new Messages(id,message,sender_id,sender_type,receiver_id,receiver_type,time,date));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Messenger.recycleAdapter.notifyDataSetChanged();
                                Messenger.recyclerView.scrollToPosition(Messenger.recycleAdapter.getItemCount()-1);
                            }
                            else{
                                Toast.makeText(Messenger.context,"No Message",Toast.LENGTH_LONG).show();

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
        VolleySingleton.getInstance(Messenger.context).addToRequestQueue(jsonObjectRequest);
    }

    public void updateDeviceId(final String device_id){

        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.update_device_token_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            if (response.contains("success")) {


                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                    params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                    params.put("type", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type());
                    params.put("device_id", device_id);
                    return params;
                }
            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }


}
