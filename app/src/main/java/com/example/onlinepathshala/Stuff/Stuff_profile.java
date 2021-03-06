package com.example.onlinepathshala.Stuff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Stuff_profile extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView tv_name,tv_password,tv_email,tv_phone_number;
    ImageView edit_name,edit_phone,edit_password,edit_email;
    ProgressDialog progressDialog;
    String password,name,email,phone_number;
    String image_path;
    private int PICK_IMAGE_REQUEST = 1;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff_profile);
        tv_name=findViewById(R.id.name);
        tv_email=findViewById(R.id.email);
        tv_phone_number=findViewById(R.id.phone_number);


        edit_email=findViewById(R.id.edit_email);
        edit_name=findViewById(R.id.edit_name);
        edit_phone=findViewById(R.id.edit_phone);
        edit_password=findViewById(R.id.edit_password);
        circleImageView=findViewById(R.id.profile_image);

        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("email","Edit Email",email);
            }
        });

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("moderator_name","Edit User Name",name);
            }
        });

        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("phone_number","Edit Phone Number",phone_number);
            }
        });

        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("password","Edit Password",password);
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                change_profile_pic();
            }
        });

        progressDialog=new ProgressDialog(Stuff_profile.this);

        progressDialog.setMessage("Please Wait");
        getAllData();
    }

    public void showEditDialog(final String column_name, String title, String hint) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Stuff_profile.this);
        View view = getLayoutInflater().inflate(R.layout.profile_edit_dialogbox, null);
        TextView tv_title = view.findViewById(R.id.title);
        final EditText value = view.findViewById(R.id.value);
        value.setText(hint);
        tv_title.setText(title);
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

                if (value.getText().toString().length() > 1) {

                    progressDialog.show();
                    update(value.getText().toString(), column_name);
                    dialog.dismiss();
                } else {

                    Toast.makeText(getApplicationContext(), "Please Enter New Data", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void  change_profile_pic(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Stuff_profile.this);
        View view=getLayoutInflater().inflate(R.layout.change_profile_pic_dia_box,null);
        LinearLayout linearLayout=view.findViewById(R.id.change_pic);
        builder.setView(view);
        final AlertDialog dialog=builder.show();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                showFileChooser();
            }
        });

    }
    private void showFileChooser() {

        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,1);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .start(Stuff_profile.this);


        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode == RESULT_OK){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            byte[] bytes=compressImage1(resultUri);

            if(bytes!=null){
                String image = getStringImage(bytes);
                progressDialog.show();
                SendImage(image);
            }
            else {
                Toast.makeText(getApplicationContext(),"Fail to Upload Image",Toast.LENGTH_LONG).show();
            }



        }


    }


    public byte[] compressImage1(Uri resultUri){

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            return  imageBytes;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public byte[] compressImage2(Uri resultUri){

        File filePathForCompress=new File(resultUri.getPath());
        Bitmap compressBitmap=null;
        try{
            compressBitmap=new Compressor(getApplicationContext())
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(50)
                    .compressToBitmap(filePathForCompress);

        }catch(IOException e){

            e.printStackTrace();
        }

        ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
        compressBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArray);

        final byte[] thumByte=byteArray.toByteArray();
        return  thumByte;
    }

    public String getStringImage(byte[] imageBytes) {

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    private void SendImage( final String image) {

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.image_upload_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if(response.contains("success")){

                            Toast.makeText(getApplicationContext(),"Successfully image uploaded",Toast.LENGTH_LONG).show();
                            getAllData();
                        }
                        else{
                            System.out.println(response.toString());
                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new Hashtable<String, String>();
                params.put("id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("school_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                params.put("table","Moderator");
                params.put("image", image);
                params.put("path",Constant_URL.base_url);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }


    public void update(final String value, final String column){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.common_editor_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),column+" Updated Successfully", Toast.LENGTH_SHORT).show();
                            getAllData();

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
                params.put("id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("column",column);
                params.put("table", "Moderator");
                params.put("school_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("Moderator");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.select_single_row,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        String school_name=SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_name();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        name=member.getString("moderator_name");
                                        email=member.getString("email");
                                        phone_number=member.getString("phone_number");
                                        password =member.getString("password");
                                        image_path=member.getString("image_path");

                                        if(!image_path.equalsIgnoreCase("null")){


                                            Picasso.get().load(image_path).placeholder(R.drawable.profile2).into(circleImageView);
                                        }
                                        tv_name.setText(name);
                                        tv_email.setText(email);
                                        tv_phone_number.setText("+88"+phone_number);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_LONG).show();
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




}
