package com.example.onlinepathshala.Student;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.onlinepathshala.Transaction_History_For_Authority;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class Student_profile extends Fragment {

    public Student_profile(){

    }
    String type;
    private int PICK_IMAGE_REQUEST = 1;
    CircleImageView circleImageView;
    ProgressDialog progressDialog;
    Button edit_name,edit_email,edit_gender,edit_birth_date,edit_blood_group,edit_religion,edit_father_name,edit_mother_name,edit_father_contact,edit_father_occupation,edit_mother_contact,edit_mother_occupation,edit_alt_email,edit_emg_contact,edit_present_address,edit_permanent_address;
    TextView user_name,email,student_id,class_roll,gender,birth_date,blood_group,religion,father_name,mother_name,father_contact,mother_contact,father_occupation,mother_occupation,alt_email,emergency_contact,present_address,permanent_address,class_name,section_name,phone_number;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_student_profile, container, false);

        class_name=view.findViewById(R.id.class_name);
        section_name=view.findViewById(R.id.section_name);
        phone_number=view.findViewById(R.id.phone_number);
        circleImageView=view.findViewById(R.id.profile_image);
        user_name=view.findViewById(R.id.username);
        email=view.findViewById(R.id.email);
        student_id=view.findViewById(R.id.student_id);
        class_roll=view.findViewById(R.id.class_roll);
        gender=view.findViewById(R.id.gender);
        birth_date=view.findViewById(R.id.birth);
        blood_group=view.findViewById(R.id.blood_group);
        religion=view.findViewById(R.id.religion);
        father_name=view.findViewById(R.id.father_name);
        mother_name=view.findViewById(R.id.mother_name);
        father_contact=view.findViewById(R.id.father_contact);
        father_occupation=view.findViewById(R.id.father_occupation);
        mother_contact=view.findViewById(R.id.mother_contact);
        mother_occupation=view.findViewById(R.id.mother_occupation);
        alt_email=view.findViewById(R.id.alt_email);
        emergency_contact=view.findViewById(R.id.emg_contact);
        present_address=view.findViewById(R.id.present_address);
        permanent_address=view.findViewById(R.id.permanent_address);

        edit_name=view.findViewById(R.id.edit_name);
        edit_email=view.findViewById(R.id.edit_email);
        edit_gender=view.findViewById(R.id.edit_gender);
        edit_birth_date=view.findViewById(R.id.edit_birth);
        edit_blood_group=view.findViewById(R.id.edit_blood);
        edit_religion=view.findViewById(R.id.edit_religion);
        edit_father_name=view.findViewById(R.id.edit_father_name);
        edit_mother_name=view.findViewById(R.id.edit_mother_name);
        edit_father_contact=view.findViewById(R.id.edit_father_contact);
        edit_father_occupation=view.findViewById(R.id.edit_father_occupation);
        edit_mother_contact=view.findViewById(R.id.edit_mother_contact);
        edit_mother_occupation=view.findViewById(R.id.edit_mother_occupation);
        edit_alt_email=view.findViewById(R.id.edit_alt_email);
        edit_emg_contact=view.findViewById(R.id.edit_emg_contact);
        edit_present_address=view.findViewById(R.id.edit_present_address);
        edit_permanent_address=view.findViewById(R.id.edit_permanent_address);
        progressDialog=new ProgressDialog(getContext());

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                change_profile_pic();
            }
        });

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("student_name","Edit Name","Enter Name");
            }
        });
        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("email","Edit Email","Enter Email");
            }
        });
        edit_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> gender=new ArrayList<>();
                gender.add("Choose Gender Type");
                gender.add("Male");
                gender.add("Female");
                gender.add("Other");
                showEditDialog2("gender","Edit Gender","Choose Gender Type",gender);
            }
        });
        edit_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show_date_picker();

            }
        });
        edit_blood_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<String> blood_group=new ArrayList<>();
                blood_group.add("Choose Blood Group");
                blood_group.add("A+");
                blood_group.add("O+");
                blood_group.add("B+");
                blood_group.add("AB+");
                blood_group.add("A-");
                blood_group.add("O-");
                blood_group.add("B-");
                blood_group.add("AB-");
                showEditDialog2("blood_group","Edit Blood Group","Choose Blood Group",blood_group);
            }
        });
        edit_religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> religions=new ArrayList<>();
                religions.add("Choose Religion");
                religions.add("Islam");
                religions.add("Hinduism");
                religions.add("Buddhism");
                religions.add("Christianity");
                showEditDialog2("religion","Edit Religion","Choose Religion",religions);
            }
        });
        edit_father_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("father_name","Edit Father Name","Enter New Value");
            }
        });
        edit_mother_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("mother_name","Edit Mother Name","Enter New Value");
            }
        });
        edit_father_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("father_contact","Edit Father Contact","Enter New Value");
            }
        });
        edit_father_occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("father_occupation","Edit Father Occupation","Enter New Value");
            }
        });
        edit_mother_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("mother_contact","Edit Mother contact","Enter New Value");
            }
        });
        edit_mother_occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("mother_occupation","Edit Mother Occupation","Enter New Value");
            }
        });
        edit_alt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("alt_email","Edit Email","Enter New Value");
            }
        });
        edit_emg_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("emergency_contact","Edit Emergency Contact","Enter New Value");
            }
        });
        edit_permanent_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("permanent_address","Edit Permanent Address","Enter New Value");
            }
        });
        edit_present_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog("present_address","Edit Present Address","Enter New Value");
            }
        });



        return view;
    }

    public void  change_profile_pic(){

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
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
                    .start(getContext(),this);


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
                Toast.makeText(getContext(),"Fail to Upload Image",Toast.LENGTH_LONG).show();
            }



        }


    }


    public byte[] compressImage1(Uri resultUri){

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);
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
            compressBitmap=new Compressor(getContext())
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

                            Toast.makeText(getContext(),"Successfully image uploaded",Toast.LENGTH_LONG).show();
                            onResume();
                        }
                        else{
                            System.out.println(response.toString());
                            Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new Hashtable<String, String>();
                params.put("id", SharedPrefManager.getInstance(getContext()).getUser().getId());
                params.put("school_id",SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                params.put("table","Student");
                params.put("image", image);
                params.put("path",Constant_URL.base_url);
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    public void show_date_picker(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String date=year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        update(date, "birth_date");


                    }
                },year, month, day);
        picker.show();
    }

    public void showEditDialog(final String column_name, String title, String hint){

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.profile_edit_dialogbox,null);
        TextView tv_title=view.findViewById(R.id.title);
       final  EditText value=view.findViewById(R.id.value);
        tv_title.setText(title);
        value.setHint(hint);
        Button cancel=view.findViewById(R.id.cancel);
        Button confirm=view.findViewById(R.id.confirm);
        builder.setView(view);
        final  AlertDialog dialog=builder.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(value.getText().toString().length()>1){

                    progressDialog.show();
                   update(value.getText().toString(),column_name);
                   dialog.dismiss();
               }
               else{

                   Toast.makeText(getContext(),"Please Enter New Data",Toast.LENGTH_LONG).show();
               }

            }
        });





    }

    public void showEditDialog2(final String column_name, String title, String hint,ArrayList<String> list){


        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.student_profile_edit_dialogbox,null);
        TextView tv_title=view.findViewById(R.id.title);
        final Spinner value=view.findViewById(R.id.type);
        tv_title.setText(title);
        value.setAdapter(new CustomAdapter(getContext(),1,list));
        value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type=list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button cancel=view.findViewById(R.id.cancel);
        Button confirm=view.findViewById(R.id.confirm);
        builder.setView(view);
        final  AlertDialog dialog=builder.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!type.equalsIgnoreCase(hint)){

                    progressDialog.show();
                    update(type,column_name);
                    dialog.dismiss();
                }
                else{

                    Toast.makeText(getContext(),"Please "+hint,Toast.LENGTH_LONG).show();
                }

            }
        });





    }

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<String> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<String> info) {
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
            view = inflter.inflate(R.layout.custom_list_item, null);
            final TextView names = (TextView) view.findViewById(R.id.teacher_name);

            String type=info.get(i);
            names.setText(type);

            return view;
        }
    }



    public void update(final String value, final String column){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.update_student_profile_data_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getContext(),column+" Updated Successfully", Toast.LENGTH_SHORT).show();
                            onResume();

                        } else {
                            Toast.makeText(getContext(), "Fail to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("value", value);
                params.put("id",SharedPrefManager.getInstance(getContext()).getUser().getId());
                params.put("column",column);
                params.put("table", "Student");
                params.put("school_id", SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

        getAllMemberData();
    }

    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getId());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_single_student_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);
                            Student_Additional_Info student_additional_info=null;
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String name=member.getString("student_name");
                                        String email = member.getString("email");
                                        String class_name=member.getString("class_name");
                                        String section_name=member.getString("section_name");
                                        String image_path = member.getString("image_path");
                                        String phone_number = member.getString("phone_number");
                                        String gender = member.getString("gender");
                                        String birth_date = member.getString("birth_date");
                                        String religion = member.getString("religion");
                                        String blood_group = member.getString("blood_group");
                                        String class_roll = member.getString("class_roll");
                                        String father_name = member.getString("father_name");
                                        String mother_name = member.getString("mother_name");
                                        String father_contact = member.getString("father_contact");
                                        String mother_contact = member.getString("mother_contact");
                                        String father_occupation = member.getString("father_occupation");
                                        String mother_occupation = member.getString("mother_occupation");
                                        String alt_email = member.getString("alt_email");
                                        String emergency_contact = member.getString("emergency_contact");
                                        String present_address= member.getString("present_address");
                                        String permanent_address= member.getString("permanent_address");

                                        student_additional_info=new Student_Additional_Info (id,name,class_name,section_name,phone_number,image_path,email,class_roll,gender,birth_date,blood_group,religion,father_name,mother_name,father_contact,mother_contact,father_occupation,mother_occupation,alt_email,emergency_contact,present_address,permanent_address);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                email.setText(student_additional_info.email);
                                user_name.setText(student_additional_info.name);
                                student_id.setText(student_additional_info.id);
                                if(!student_additional_info.image_path.equalsIgnoreCase("null")){


                                    Picasso.get().load(student_additional_info.image_path).placeholder(R.drawable.profile2).into(circleImageView);
                                }
                                if(!student_additional_info.class_roll.equalsIgnoreCase("null")){

                                    class_roll.setText(student_additional_info.class_roll);
                                }
                                if(!student_additional_info.birth_date.equalsIgnoreCase("null")){

                                    birth_date.setText(student_additional_info.birth_date);
                                }
                                if(!student_additional_info.blood_group.equalsIgnoreCase("null")){

                                    blood_group.setText(student_additional_info.blood_group);
                                }
                                if(!student_additional_info.religion.equalsIgnoreCase("null")){

                                    religion.setText(student_additional_info.religion);
                                }
                                if(!student_additional_info.gender.equalsIgnoreCase("null")){

                                    gender.setText(student_additional_info.gender);
                                }
                                if(!student_additional_info.father_name.equalsIgnoreCase("null")){

                                    father_name.setText(student_additional_info.father_name);
                                }
                                if(!student_additional_info.mother_name.equalsIgnoreCase("null")){

                                    mother_name.setText(student_additional_info.mother_name);
                                }
                                if(!student_additional_info.father_contact.equalsIgnoreCase("null")){

                                    father_contact.setText(student_additional_info.father_contact);
                                }
                                if(!student_additional_info.mother_contact.equalsIgnoreCase("null")){

                                    mother_contact.setText(student_additional_info.mother_contact);
                                }
                                if(!student_additional_info.mother_occupation.equalsIgnoreCase("null")){

                                    mother_occupation.setText(student_additional_info.mother_occupation);
                                }
                                if(!student_additional_info.father_occupation.equalsIgnoreCase("null")){

                                    father_occupation.setText(student_additional_info.father_occupation);
                                }
                                if(!student_additional_info.alt_email.equalsIgnoreCase("null")){

                                    alt_email.setText(student_additional_info.alt_email);
                                }
                                if(!student_additional_info.emergency_contact.equalsIgnoreCase("null")){

                                    emergency_contact.setText(student_additional_info.emergency_contact);
                                }
                                if(!student_additional_info.present_address.equalsIgnoreCase("null")){

                                    present_address.setText(student_additional_info.present_address);
                                }
                                if(!student_additional_info.permanent_address.equalsIgnoreCase("null")){

                                    permanent_address.setText(student_additional_info.permanent_address);
                                }

                                class_name.setText(student_additional_info.class_name);
                                section_name.setText(student_additional_info.section_name);
                                phone_number.setText(student_additional_info.phone_number);


                                progressDialog.dismiss();
                            }
                            else{

                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"No Home Task Still Assigned",Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }




}
