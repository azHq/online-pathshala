package com.example.onlinepathshala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Student.Result_item_info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Result_Edit extends AppCompatActivity {

    ProgressDialog progressDialog;
    String id="";
    Button submit;
    boolean valid=true;
    ArrayList<Result_Info2> result_info2=new ArrayList<>();
    String student_id="",student_name="",subject_name="",total_marks="",subjective_total="",objective_total="",practical_total="",obtain_subjective="",obtain_objective="",obtain_practical="",weight="";
    EditText et_student_id,et_student_name,et_subject_name,et_total_marks,et_subjective_total,et_objective_total,et_practical_total,et_obtain_subjective,et_obtain_objective,et_obtain_practical,et_weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result__edit);
        id=getIntent().getStringExtra("id");
        et_student_id=findViewById(R.id.student_id);
        et_student_name=findViewById(R.id.student_name);
        et_subject_name=findViewById(R.id.subject_name);
        et_total_marks=findViewById(R.id.total_marks);
        et_subjective_total=findViewById(R.id.subjective_total);
        et_objective_total=findViewById(R.id.objective_total);
        et_practical_total=findViewById(R.id.practical_total);
        et_obtain_subjective=findViewById(R.id.subjective_obtain);
        et_obtain_objective=findViewById(R.id.objective_obtain);
        et_obtain_practical=findViewById(R.id.practical_obtain);
        et_weight=findViewById(R.id.weight);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid=true;
                student_id=et_student_id.getText().toString();
                student_name=et_student_name.getText().toString();
                subject_name=et_subject_name.getText().toString();
                total_marks=et_total_marks.getText().toString();
                subjective_total=et_subjective_total.getText().toString();
                objective_total=et_objective_total.getText().toString();
                practical_total=et_practical_total.getText().toString();
                obtain_subjective=et_obtain_subjective.getText().toString();
                obtain_objective=et_obtain_objective.getText().toString();
                obtain_practical=et_obtain_practical.getText().toString();
                weight=et_weight.getText().toString();

                if(student_id.length()>0&&student_name.length()>0&&subject_name.length()>0&&total_marks.length()>0&&subjective_total.length()>0&&objective_total.length()>0&&practical_total.length()>0&&obtain_subjective.length()>0&&obtain_objective.length()>0&&obtain_practical.length()>0&&weight.length()>0){

                    check_marks_validity();
                }
                else{

                    showMessage();
                }

            }
        });
        progressDialog=new ProgressDialog(Result_Edit.this);
        getAllData();

    }

    public void check_marks_validity(){

        double obtain_mark_subjective2=Double.parseDouble(obtain_subjective);
        double obtain_mark_objective2=Double.parseDouble(obtain_objective);
        double obtain_marks_practical2=Double.parseDouble(obtain_practical);
        double total_subjective2=Double.parseDouble(subjective_total);
        double total_objective2=Double.parseDouble(objective_total);
        double total_practical2=Double.parseDouble(practical_total);
        double total_obtain=obtain_mark_subjective2+obtain_mark_objective2+obtain_marks_practical2;

        String message="";
        if(obtain_mark_subjective2>total_subjective2){

            valid=false;
            message+="Obtain Marks In Subjective Can Not Be Greater Than Total Subjective Marks\n";
        }
        if(obtain_mark_objective2>total_objective2){

            valid=false;
            message+="Obtain Marks In Objective Can Not Be Greater Than Total Objective Marks\n";
        }
        if(obtain_marks_practical2>total_practical2){

            valid=false;
            message+="Obtain Marks In Practical Can Not Be Greater Than Total Practical Marks\n";
        }
        double total_mark=total_subjective2+total_objective2+total_practical2;
        double total=Double.parseDouble(total_marks);
        if(total_mark>total){

            valid=false;
            message+="Sum Of (subjective_total,objective_total,practical_total) Can Not Be Greater Than Total Marks\n";
        }
        if(total_obtain>total){

            valid=false;
            message+="Obtain Marks Can Not Be Greater Than Total Marks\n";
        }

        if(valid){

            submit();
        }
        else{

            showMessage( message);
        }
    }
    public void showMessage(String message){

        AlertDialog.Builder builder=new AlertDialog.Builder(Result_Edit.this);
        builder.setTitle("Error in Result Data");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    public void showMessage(){

        AlertDialog.Builder builder=new AlertDialog.Builder(Result_Edit.this);
        builder.setMessage("Please Enter All Required Data");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void submit(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.get_edit_result,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Result Edited Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            //startActivity(new Intent(getApplicationContext(), Admin_Panel.class));

                        } else {
                            Toast.makeText(getApplicationContext(), "Fail to Edit", Toast.LENGTH_SHORT).show();
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
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                params.put("table", "Result");
                params.put("id",id);
                params.put("student_id",student_id);
                params.put("student_name",student_name);
                params.put("subject_name",subject_name);
                params.put("total_marks",total_marks);
                params.put("subjective_total",subjective_total);
                params.put("objective_total",objective_total);
                params.put("practical_total",practical_total);
                params.put("obtain_subjective",obtain_subjective);
                params.put("obtain_objective",obtain_objective);
                params.put("obtain_practical",obtain_practical);
                params.put("weight",weight);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void getAllData(){

        JSONArray postparams = new JSONArray();
        postparams.put("Result");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(id);
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
                                        String student_id=member.getString("student_id");
                                        String student_name=member.getString("student_name");
                                        String subject_name=member.getString("subject_name");
                                        String total_marks=member.getString("total_marks");
                                        String subjective_total=member.getString("subjective_total");
                                        String objective_total=member.getString("objective_total");
                                        String practical_total=member.getString("practical_total");
                                        String obtain_marks_subjective=member.getString("obtain_marks_subjective");
                                        String obtain_marks_objective=member.getString("obtain_marks_objective");
                                        String obtain_marks_practical=member.getString("obtain_marks_practical");
                                        String weight=member.getString("weight");
                                        et_student_id.setText(student_id);
                                        et_student_name.setText(student_name);
                                        et_total_marks.setText(total_marks);
                                        et_subject_name.setText(subject_name);
                                        et_subjective_total.setText(subjective_total);
                                        et_objective_total.setText(objective_total);
                                        et_practical_total.setText(practical_total);
                                        et_obtain_subjective.setText(obtain_marks_subjective);
                                        et_obtain_objective.setText(obtain_marks_objective);
                                        et_obtain_practical.setText(obtain_marks_practical);
                                        et_weight.setText(weight);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No Teacher Still Added",Toast.LENGTH_LONG).show();
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
