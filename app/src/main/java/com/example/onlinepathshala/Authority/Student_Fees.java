package com.example.onlinepathshala.Authority;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Student_Fees extends AppCompatActivity {

    Spinner sp_choose_class,sp_choose_student,sp_choose_fees_type;
    ArrayList<Object> classes=new ArrayList<>();
    ArrayList<Object> fee_type=new ArrayList<>();
    ArrayList<Student> students=new ArrayList<>();
    String class_id="";
    ActionBar actionBar;
    String fee_types,student_id;
    String amount="";
    EditText et_amount;
    ProgressDialog progressDialog;
    boolean submit=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__fees);

        progressDialog=new ProgressDialog(Student_Fees.this);
        progressDialog.setTitle("Please Wait");
        et_amount=findViewById(R.id.amount);
        sp_choose_class=findViewById(R.id.class_name);
        sp_choose_student=findViewById(R.id.student_name);
        sp_choose_fees_type=findViewById(R.id.fee_type);

        classes.add(new Classes("0","Choose Class",null,null,null));
        students.add(new Student("0","Choose Student",null,null,null,null));
        sp_choose_student.setAdapter(new CustomAdapter2(getApplicationContext(),0,students));
        sp_choose_student.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                student_id=students.get(i).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getAllMemberData("Class_Table", Constant_URL.get_all_data_url);
        sp_choose_class.setAdapter(new CustomAdapter(getApplicationContext(),2,classes));
        sp_choose_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Classes cl=(Classes)classes.get(i);
                class_id=cl.id;
                getAllMemberData("Student", Constant_URL.get_all_student_in_a_class_url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        fee_type.add("Choose Fee Type");
        fee_type.add("Exam Fee");
        fee_type.add("Montly Fee");
        fee_type.add("Tutorial Fee");
        fee_type.add("Fine");
        fee_type.add("Others");
        sp_choose_fees_type.setAdapter(new CustomAdapter(getApplicationContext(),1,fee_type));
        sp_choose_fees_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                fee_types=(String) fee_type.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getAllMemberData(final String table_name,String url){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                url,postparams,
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

                                        if(table_name.equalsIgnoreCase("Class_Table")){
                                            String id = member.getString("class_id");
                                            String name = member.getString("class_name");
                                            classes.add(new Classes(id,name,null,null,null));
                                            sp_choose_class.setAdapter(new CustomAdapter(getApplicationContext(),2,classes));
                                        }
                                        else if(table_name.equalsIgnoreCase("Student")){

                                            String id = member.getString("id");
                                            String image_path=member.getString("image_path");
                                            String name = member.getString("student_name");
                                            students.add(new Student(id,name,null,image_path,null,null));
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                            else{


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

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



    public void onSubmit(View view){

        int amnt=0;
        amount=et_amount.getText().toString();
        if(amount.length()>0) amnt=Integer.parseInt(et_amount.getText().toString());
        if(amnt>0&&!student_id.equalsIgnoreCase("0")&&!fee_types.equalsIgnoreCase("Choose Fee Type")){

            uploadPayment();
        }
        else {

            String message=getErrorMessage();
            AlertDialog.Builder builder=new AlertDialog.Builder(Student_Fees.this);
            builder.setTitle("Please Enter Required Info");
            builder.setMessage(message);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }

    }

    public void uploadPayment(){

        submit=false;
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.insert_transaction_info_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {
                            sp_choose_class.setSelection(0);
                            et_amount.setText("");
                            sp_choose_student.setSelection(0);
                            Toast.makeText(getApplicationContext(),"Payment Successful", Toast.LENGTH_SHORT).show();

                            //finish();
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
                params.put("sender_id",student_id);
                params.put("transaction_name", fee_types);
                params.put("sender_type", "Student");
                params.put("receiver_type", "Authority");
                params.put("transaction_type", "Student Fee");
                params.put("payment_method", "Cash");
                params.put("amount",amount);
                params.put("receiver_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("debit/credit","credit");
                params.put("table", "Transaction");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getErrorMessage() {

        if(student_id.equalsIgnoreCase("0")) return "Please Choose Student";
        else if(student_id.equalsIgnoreCase("Choose Fee Type")) return "Please Choose Fee Type";
        else if(amount.length()==0) return "Please Enter Valid Fee Amount";

        return "Please Enter Valid Data";
    }

    public static class CustomAdapter2 extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Student> info;
        LayoutInflater inflter;

        public CustomAdapter2(Context applicationContext,int flag,ArrayList<Student> info) {
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
            view = inflter.inflate(R.layout.student_rec_list_item3, null);
            TextView names = (TextView) view.findViewById(R.id.name);
            TextView id = (TextView) view.findViewById(R.id.id);
            CircleImageView circleImageView=view.findViewById(R.id.profile);

            Student member=info.get(i);
            names.setText(member.name);
            if(!member.id.equalsIgnoreCase("0"))id.setText(member.id);
            else id.setVisibility(View.GONE);
            if(member.image_path!=null)  Picasso.get().load(member.image_path).placeholder(R.drawable.profile2).into(circleImageView);


            return view;
        }
    }
    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Object> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<Object> info) {
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
            view = inflter.inflate(R.layout.teachers_list_item, null);
            TextView names = (TextView) view.findViewById(R.id.teacher_name);
            CircleImageView circleImageView=view.findViewById(R.id.image);
            circleImageView.setVisibility(View.GONE);
            if(flag==1){

                String member=(String) info.get(i);
                names.setText(member);

            }
            else if(flag==2){

                Classes member=(Classes) info.get(i);
                names.setText(member.class_name);
            }


            return view;
        }
    }
}
