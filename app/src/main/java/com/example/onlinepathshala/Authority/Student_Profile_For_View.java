package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
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
import com.example.onlinepathshala.Student.Student_Additional_Info;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Student_Profile_For_View extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    CircleImageView circleImageView;
    ProgressDialog progressDialog;
    Button edit_name,edit_email,edit_gender,edit_birth_date,edit_blood_group,edit_religion,edit_father_name,edit_mother_name,edit_father_contact,edit_father_occupation,edit_mother_contact,edit_mother_occupation,edit_alt_email,edit_emg_contact,edit_present_address,edit_permanent_address;
    TextView user_name,email,student_id,class_roll,gender,birth_date,blood_group,religion,father_name,mother_name,father_contact,mother_contact,father_occupation,mother_occupation,alt_email,emergency_contact,present_address,permanent_address,phone_number,class_name,section_name;
    String id="",class_id="",section_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__profile__for__view);

        progressDialog=new ProgressDialog(Student_Profile_For_View.this);
        progressDialog.setMessage("Please wait...");
        id=getIntent().getStringExtra("id");
        class_id=getIntent().getStringExtra("class_id");
        section_id=getIntent().getStringExtra("section_id");

        class_name=findViewById(R.id.class_name);
        section_name=findViewById(R.id.section_name);
        phone_number=findViewById(R.id.phone_number);
        circleImageView=findViewById(R.id.profile_image);
        user_name=findViewById(R.id.username);
        email=findViewById(R.id.email);
        student_id=findViewById(R.id.student_id);
        class_roll=findViewById(R.id.class_roll);
        gender=findViewById(R.id.gender);
        birth_date=findViewById(R.id.birth);
        blood_group=findViewById(R.id.blood_group);
        religion=findViewById(R.id.religion);
        father_name=findViewById(R.id.father_name);
        mother_name=findViewById(R.id.mother_name);
        father_contact=findViewById(R.id.father_contact);
        father_occupation=findViewById(R.id.father_occupation);
        mother_contact=findViewById(R.id.mother_contact);
        mother_occupation=findViewById(R.id.mother_occupation);
        alt_email=findViewById(R.id.alt_email);
        emergency_contact=findViewById(R.id.emg_contact);
        present_address=findViewById(R.id.present_address);
        permanent_address=findViewById(R.id.permanent_address);

        getAllMemberData();
    }


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(id);

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
                                        String class_name=member.getString("class_name");
                                        String section_name=member.getString("section_name");
                                        String email = member.getString("email");
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

}
