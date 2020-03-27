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

public class Stuff_Salary extends AppCompatActivity {

    Spinner sp_choose_teacher,sp_choose_payment_method,sp_choose_fees_type;
    ArrayList<Object> classes=new ArrayList<>();
    ArrayList<Object> payment_method=new ArrayList<>();
    ArrayList<Teacher> teachers=new ArrayList<>();
    String class_id="";
    ActionBar actionBar;
    String fee_types,teacher_id;
    String amount="";
    EditText et_amount;
    ProgressDialog progressDialog;
    boolean submit=true;
    String pay_method="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff__salary);

        progressDialog=new ProgressDialog(Stuff_Salary.this);
        progressDialog.setTitle("Please Wait");
        et_amount=findViewById(R.id.amount);
        sp_choose_teacher=findViewById(R.id.choose_teacher);
        sp_choose_payment_method=findViewById(R.id.choose_payment_method);

        sp_choose_payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                pay_method=(String)payment_method.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        payment_method.add("Choose Payment Method");
        payment_method.add("Cash");
        payment_method.add("Bkash");
        payment_method.add("Bank Check");
        sp_choose_payment_method.setAdapter(new CustomAdapter2(getApplicationContext(),1,payment_method));
        teachers.add(new Teacher("0","Choose Stuff",null,null,null));
        teachers.add(new Teacher("12dga3","Choose Stuff",null,null,null));
        getAllMemberData("Stuff",Constant_URL.get_all_data_url);
        sp_choose_teacher.setAdapter(new CustomAdapter(getApplicationContext(),0,teachers));
        sp_choose_teacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                teacher_id=teachers.get(i).id;
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



                                        String id = member.getString("id");
                                        String image_path=member.getString("image_path");
                                        String name = member.getString("teacher_name");
                                        teachers.add(new Teacher(id,name,null,null,image_path));


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                sp_choose_teacher.setAdapter(new CustomAdapter(getApplicationContext(),0,teachers));

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

                        Toast.makeText(getApplicationContext(),"Connection Error",Toast.LENGTH_LONG).show();
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
        if(amnt>0&&!teacher_id.equalsIgnoreCase("0")&&!pay_method.equalsIgnoreCase("Choose Payment Method")){

            uploadPayment();
        }
        else {

            String message=getErrorMessage();
            AlertDialog.Builder builder=new AlertDialog.Builder(Stuff_Salary.this);
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
                            sp_choose_teacher.setSelection(0);
                            sp_choose_payment_method.setSelection(0);
                            et_amount.setText("");
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
                params.put("sender_id",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                params.put("transaction_name", "Stuff Salary");
                params.put("transaction_type", "Stuff Salary");
                params.put("payment_method", pay_method);
                params.put("amount",amount);
                params.put("sender_type", "Authority");
                params.put("receiver_type", "Stuff");
                params.put("receiver_id",teacher_id);
                params.put("debit/credit","debit");
                params.put("table", "Transaction");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getErrorMessage() {

        if(teacher_id.equalsIgnoreCase("0")) return "Please Choose Teacher";
        else if(pay_method.equalsIgnoreCase("Choose Payment Method")) return "Please Choose Payment Method";
        else if(amount.length()==0) return "Please Enter Valid Fee Amount";

        return "Please Enter Valid Data";
    }

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Teacher> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<Teacher> info) {
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

            Teacher member=info.get(i);
            names.setText(member.name);
            if(!member.id.equalsIgnoreCase("0"))id.setText(member.id);
            else id.setVisibility(View.GONE);
            if(member.image_path!=null)  Picasso.get().load(member.image_path).placeholder(R.drawable.profile2).into(circleImageView);


            return view;
        }
    }

    public static class CustomAdapter2 extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Object> info;
        LayoutInflater inflter;

        public CustomAdapter2(Context applicationContext,int flag,ArrayList<Object> info) {
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
