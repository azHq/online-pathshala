package com.example.onlinepathshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Admin.Admin_Panel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Text extends AppCompatActivity {

    EditText editText;
    private WindowManager mWindowManager;
    final static int CODE_DRAW_OVER_OTHER_APP_PERMISSION=1;
    private BubblesManager bubblesManager;
    private NotificationBadge mBadge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        editText=findViewById(R.id.device_id);

        if(Build.VERSION.SDK_INT >= 23)
        {
            if(!Settings.canDrawOverlays(Text.this))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:"+getPackageName()));
                startActivityForResult(intent,CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }
        }
        else{
            Intent intent = new Intent(Text.this, Service.class);
            startService(intent);
        }
       // initBubble();
    }

    public void get_ID(View view){

        updateDeviceId();
    }
    public void getDeviceId(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {


            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            startService(new Intent(Text.this, FloatingViewService.class));
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initBubble();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initBubble() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_remove)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
                        addNewBubble();
                    }
                }).build();
        bubblesManager.initialize();
    }

    private void addNewBubble() {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(Text.this)
                .inflate(R.layout.bubble_layout,null);
        mBadge = (NotificationBadge)bubbleView.findViewById(R.id.count);
        mBadge.setNumber(88);

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                Toast.makeText(Text.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(Text.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView,60,20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }

    public void show(){

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final float screenWidth = size.x;
        final float screenHeight = size.y;

        LayoutInflater inflater = LayoutInflater.from(this);
        final  View mChatHead = inflater.inflate(R.layout.layout_floating_widget, null);
        final CircleImageView mChatHeadImageView =  mChatHead
                .findViewById(R.id.profile_image);
        final TextView mChatHeadTextView = (TextView) mChatHead
                .findViewById(R.id.close_btn);
        final RelativeLayout mLayout =mChatHead
                .findViewById(R.id.root_container);


        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        parameters.x = 0;
        parameters.y = 50;
        parameters.gravity = Gravity.TOP | Gravity.LEFT;

        mChatHead.setOnTouchListener(new View.OnTouchListener() {


            int initialX, initialY;
            float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = parameters.x;
                        initialY = parameters.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        mChatHeadTextView.setText("BDCS!!!");
                        Toast.makeText(getApplication(),
                                "Drag and remove!", Toast.LENGTH_SHORT)
                                .show();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mChatHeadTextView.setVisibility(View.GONE);
                        parameters.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        parameters.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mChatHead, parameters);
                        return true;

                    case MotionEvent.ACTION_UP:

                        if (parameters.y > screenHeight * 0.8) {
                            mChatHead.setVisibility(View.GONE);
                            Toast.makeText(getApplication(), "Removed!",
                                    Toast.LENGTH_SHORT).show();

                        }

                        if (parameters.x < screenWidth / 2) {
                            mLayout.removeAllViews();
                            mLayout.addView(mChatHeadImageView);
                            mLayout.addView(mChatHeadTextView);
                            mChatHeadTextView.setVisibility(View.VISIBLE);

                        } else { // Set textView to left of image
                            mLayout.removeAllViewsInLayout();
                            mLayout.addView(mChatHeadTextView);
                            mLayout.addView(mChatHeadImageView);
                            mChatHeadTextView.setVisibility(View.VISIBLE);
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });

        mWindowManager.addView(mChatHead, parameters);
    }

    public void updateDeviceId(){

        if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId()!=null) {

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
                    params.put("device_id", "device id");
                    return params;
                }
            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

}
