package com.example.onlinepathshala.Authority;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Admin.Member;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Notice_Upload extends AppCompatActivity {


    public Notice_Upload() {

    }

    ArrayList<String> class_ids=new ArrayList<>();
    ArrayList<String> shifts=new ArrayList<>();
    Spinner sp_medium;
    RelativeLayout choose_class;
    Button bt_submit,bt_cancel,bt_choose_file;
    EditText et_title,et_details;
    String class_name,class_id,medium,teacher_id,time,day,meridiem;
    ProgressDialog progressDialog;
    ArrayList<Object> memberInfos=new ArrayList<>();
    ArrayList<Classes> class_infos=new ArrayList<>();
    AlertDialog dialog;
    CheckBox select_all;
    String title,details;
    boolean ALl_Classes;
    final int FILE_CHOOSING_REQUEST_CODE=1;
    String file_type="";
    String file;
    boolean submit=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notice_upload);
        choose_class=findViewById(R.id.choose_class);
        sp_medium=findViewById(R.id.choose_shift);
        bt_submit=findViewById(R.id.confirm);
        bt_cancel=findViewById(R.id.cancel);
        bt_choose_file=findViewById(R.id.choose_file);
        et_title=findViewById(R.id.title);
        et_details=findViewById(R.id.details);
        medium="Bangla";
        getAllMemberData(medium);
        progressDialog=new ProgressDialog(Notice_Upload.this);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=et_title.getText().toString();
                details=et_details.getText().toString();
                if((!class_ids.isEmpty()||medium.equalsIgnoreCase("Whole School"))&&!medium.equalsIgnoreCase("Choose Medium")&&title.length()>2&&details.length()>5&&file!=null){

                    if(submit){
                        submit=false;
                        submit();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),error_message(),Toast.LENGTH_LONG).show();
                }
            }
        });
        choose_class=findViewById(R.id.choose_class);
        choose_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
        shifts.add("Choose Medium");
        shifts.add("Whole School");
        shifts.add("Bangla");
        shifts.add("English");
        sp_medium.setAdapter(new CustomAdapter(getApplicationContext(),1,shifts));
        sp_medium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                medium=shifts.get(position);
                if(medium.equalsIgnoreCase("Whole School")){
                    choose_class.setVisibility(View.GONE);
                }
                else {
                    choose_class.setVisibility(View.VISIBLE);
                    getAllMemberData(medium);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bt_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                choose_file();
            }
        });


    }

    public void choose_file(){


        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*|application/pdf");
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(galleryIntent,FILE_CHOOSING_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_CHOOSING_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            Uri uri=data.getData();
            if(uri.getPath().contains(".pdf")) file_type="pdf";
            else file_type="image";
            byte[] bytes=convertUriToByte(data.getData());
            file=getStringImage(bytes);
        }




    }

    public byte[] convertUriToByte(Uri uri){

            InputStream inputStream = null;
            ByteArrayOutputStream byteBuffer=null;

        try {
            inputStream =getContentResolver().openInputStream(uri);
            byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while (true) {

                    if (!((len = inputStream.read(buffer)) != -1)) break;


                byteBuffer.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){

        }



            return byteBuffer.toByteArray();

    }

    public String getStringImage(byte[] imageBytes) {

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<String> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<String> info) {
            this.context = applicationContext;
            this.flag = flag;
            this.info=info;
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
            view = inflter.inflate(R.layout.teachers_list_item, null);
            TextView names = (TextView) view.findViewById(R.id.teacher_name);
            CircleImageView circleImageView=view.findViewById(R.id.image);
            circleImageView.setVisibility(View.GONE);
            names.setText(info.get(i));
            return view;
        }
    }

    public void showAlertDialog()
    {

        class_ids.clear();
        AlertDialog.Builder builder=new AlertDialog.Builder(Notice_Upload.this);
        View view=getLayoutInflater().inflate(R.layout.classlist_recycle,null);
        final RecyclerView recyclerView=view.findViewById(R.id.recycle);
        Button ok=view.findViewById(R.id.ok);
        Button cancel=view.findViewById(R.id.cancel);

        CardView cardView=view.findViewById(R.id.all);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(select_all.isChecked()){


                    ALl_Classes=false;
                    select_all.setChecked(false);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else {

                    ALl_Classes=true;
                    select_all.setChecked(true);
                    recyclerView.setVisibility(View.GONE);
                }


            }
        });
        select_all=view.findViewById(R.id.select_all);
        select_all.setClickable(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecycleAdapter recycleAdapter=new RecycleAdapter(class_infos);
        recyclerView.setAdapter(recycleAdapter);
        builder.setView(view);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });
        dialog=builder.show();



    }

    public void getAllMemberData(String medium){


        JSONArray postparams = new JSONArray();
        postparams.put("Class_Table");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(medium);


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_class_info_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        class_infos.clear();
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("class_id");
                                        String class_name=member.getString("class_name");
                                        String total_students=member.getString("total_student");
                                        String total_subjects=member.getString("total_section");
                                        class_infos.add(new Classes(id,class_name,"",total_students,total_subjects));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }





                            }
                            else{

                                Toast.makeText(getApplicationContext(),"No Classs Still Added",Toast.LENGTH_LONG).show();
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



    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Classes> classinfos;
        public RecycleAdapter(ArrayList<Classes> classinfos){
            this.classinfos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            TextView student_name,ID,roll;
            CircleImageView priofile;
            CheckBox checkBox;
            LinearLayout linearLayout;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                student_name=mView.findViewById(R.id.name);
                ID=mView.findViewById(R.id.teacher_name);
                checkBox=mView.findViewById(R.id.choose_subject);
                linearLayout=mView.findViewById(R.id.view_profile);




            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final  ViewAdapter holder, final int position) {


            final Classes memberInfo=(Classes) classinfos.get(position);
            holder.student_name.setText(memberInfo.class_name);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if( holder.checkBox.isChecked()){

                        class_ids.remove(position);
                        holder.checkBox.setChecked(false);


                    }
                    else {

                        class_ids.add(memberInfo.id);
                        holder.checkBox.setChecked(true);

                    }

                }
            });
            holder.checkBox.setClickable(false);



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public void submit(){
        chooseAllStudent();
        uploadNotice(true);
    }

    public  void uploadNotice(boolean whole_school){


        JSONArray class_id_list = new JSONArray(class_ids);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.get_upload_notice_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            et_title.setText("");
                            et_details.setText("");
                            class_ids.clear();
                            Toast.makeText(getApplicationContext(),"Notice Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            // startActivity(new Intent(getApplicationContext(), Add_New_Teacher.class));

                        } else {
                            System.out.println(response.toString());
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                        submit=true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        submit=true;
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id", class_id_list.toString());
                params.put("title", title);
                params.put("whole_school",whole_school+"");
                params.put("details", details);
                params.put("file",file);
                params.put("id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("file_type",file_type);
                params.put("table", "Notice");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                params.put("path",Constant_URL.base_url);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public String error_message(){

        if(title.length()<=2) return  "Please enter 2 character length title";
        else if(details.length()<=5) return  "Please enter 6 character length details";
        else if(medium.contains("Choose Medium")) return "Please Choose a Medium";
        else if(class_ids.isEmpty()&&medium.contains("Choose Medium")) return "Please Choose Class";
        else if(file==null) return "Please Choose file";
        else return "Please Enter All Required Info";

    }

    public void chooseAllStudent(){

        class_ids.clear();
        for(Classes student:class_infos){

            class_ids.add(student.id);

        }



    }



}
