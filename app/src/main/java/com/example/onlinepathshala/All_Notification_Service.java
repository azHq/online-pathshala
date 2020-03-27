package com.example.onlinepathshala;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Principal_Panel;
import com.example.onlinepathshala.Student.Student_Panel;
import com.example.onlinepathshala.Teacher.Teacher_Dashboard;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class All_Notification_Service extends Service {

    String title=null,message=null;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        title=(String)intent.getExtras().get("title");
        message=(String)intent.getExtras().get("message");

        if(MainActivity.countTextView!=null){
            generateNotification();
            getNumberOfNotificationUnSeen();
        }
        else{

            if(title!=null) generateNotification();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void generateNotification(){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ban)
                .setContentTitle(title)

                .setContentText(message);
        Intent resultIntent=null;
        if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type().equalsIgnoreCase("Student")){
            resultIntent= new Intent(this, Student_Panel.class);
        }
        else if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type().equalsIgnoreCase("Teacher")){

            resultIntent= new Intent(this, Teacher_Dashboard.class);
        }
        else if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_type().equalsIgnoreCase("Principal")){

            resultIntent= new Intent(this, Principal_Panel.class);
        }
        resultIntent.putExtra("notification",1);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(),0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        Notification note = mBuilder.build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId=(int) System.currentTimeMillis();
        mNotificationManager.notify(notificationId, note);

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
