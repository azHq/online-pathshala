package com.example.onlinepathshala;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Messenger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    TextView closeButtonCollapsed;
    String image_path="null",sender_id=null,sender_type,sender_device_id,receiver_device_id;
    View root;
    boolean shouldClick = true;
    public FloatingViewService() {

    }

    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(mWindowManager!=null&&mFloatingView!=null){
            mWindowManager.removeViewImmediate(mFloatingView);
            mFloatingView=null;
        }
        sender_id=(String)intent.getExtras().get("sender_id");
        sender_type=(String)intent.getExtras().get("sender_type");
        sender_device_id=(String)intent.getExtras().get("sender_device_id");
        receiver_device_id=(String)intent.getExtras().get("receiver_device_id");
        if(sender_id!=null) get_image_path();
        //showOverlayMessageIcon();
        return super.onStartCommand(intent, flags, startId);
    }

    public void get_image_path(){

       
        JSONArray postparams = new JSONArray();
        postparams.put(sender_type);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(sender_id);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.select_single_row,postparams,
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
                                        image_path = member.getString("image_path");
                                        showOverlayMessageIcon();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                            else{

                                //showOverlayMessageIcon();

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

    public void showOverlayMessageIcon(){

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        root=mFloatingView.findViewById(R.id.collapse_view);


        closeButtonCollapsed = (TextView) mFloatingView.findViewById(R.id.close_btn);
        String mss=SharedPrefManager.getInstance(getApplicationContext()).get_increaseUnseenMessage()+"";
        closeButtonCollapsed.setText(mss);
        CircleImageView circleImageView=mFloatingView.findViewById(R.id.profile_image);
        if(!image_path.equalsIgnoreCase("null")){
            Picasso.get().load(image_path).placeholder(R.drawable.male_profile).into(circleImageView);
        }


//        root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(FloatingViewService.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                stopSelf();
//            }
//        });


//        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//
//
//                        //remember the initial position.
//                        initialX = params.x;
//                        initialY = params.y;
//
//
//                        //get the touch location
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        //Calculate the X and Y coordinates of the view.
//                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
//
//
//                        //Update the layout with new X & Y coordinate
//                        mWindowManager.updateViewLayout(mFloatingView, params);
//                        return true;
//                }
//                return false;
//            }
//        });
        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final float screenWidth = size.x;
        final float screenHeight = size.y;

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPrefManager.getInstance(getApplicationContext()).clearUnseenMessage();
                startActivity();
                stopSelf();

            }
        });

        root.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        shouldClick = true;
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        if (shouldClick)
                            v.performClick();
                        if( Xdiff>20||Ydiff>20){

                            SharedPrefManager.getInstance(getApplicationContext()).clearUnseenMessage();
                            Toast.makeText(getApplication(),
                                    "Drag here to remove!", Toast.LENGTH_SHORT)
                                    .show();
                        }


                        //So that is click event.
//                        if (Xdiff < 10 && Ydiff < 10) {
//
//                            SharedPrefManager.getInstance(getApplicationContext()).clearUnseenMessage();
//                            startActivity();
//                            stopSelf();
//                        }

                        if (params.y > screenHeight * 0.7&&(params.x>screenWidth*.3&&params.x<screenWidth*.8)) {
                            mFloatingView.setVisibility(View.GONE);
                            stopSelf();

                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        shouldClick = false;

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    public void startActivity(){


        User user=SharedPrefManager.getInstance(getApplicationContext()).getUser();

        Intent intent = new Intent(FloatingViewService.this, Messenger.class);
        intent.putExtra("sender_id",user.getId());
        intent.putExtra("sender_type",user.getUser_type());
        intent.putExtra("receiver_id",sender_id);
        intent.putExtra("receiver_type",sender_type);
        intent.putExtra("image_path",image_path);
        intent.putExtra("receiver_device_id",sender_device_id);
        intent.putExtra("sender_device_id",receiver_device_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }




}
