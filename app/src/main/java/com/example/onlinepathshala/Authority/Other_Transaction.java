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

public class Other_Transaction extends AppCompatActivity {

    Spinner sp_choose_teacher,sp_choose_payment_method,sp_choose_transaction_type;
    ArrayList<Object> transaction_type2=new ArrayList<>();
    ArrayList<Object> payment_method=new ArrayList<>();
    ArrayList<Teacher> teachers=new ArrayList<>();
    String class_id="";
    ActionBar actionBar;
    String fee_types,teacher_id,receiver_id,transaction_name;
    String amount="";
    EditText et_amount,et_transaction_name,et_receiver_id;
    ProgressDialog progressDialog;
    boolean submit=true;
    String pay_method="",type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other__transaction);

        progressDialog=new ProgressDialog(Other_Transaction.this);
        progressDialog.setTitle("Please Wait");
        et_amount=findViewById(R.id.amount);
        et_receiver_id=findViewById(R.id.receiver_id);
        et_transaction_name=findViewById(R.id.transaction_name);
        sp_choose_transaction_type=findViewById(R.id.choose_transaction_type);
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

        transaction_type2.add("Choose Transaction Type");
        transaction_type2.add("Debit");
        transaction_type2.add("Credit");
        sp_choose_transaction_type.setAdapter(new CustomAdapter2(getApplicationContext(),1,transaction_type2));
        sp_choose_transaction_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                type = (String) transaction_type2.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    public void onSubmit(View view){

        int amnt=0;
        amount=et_amount.getText().toString();
        transaction_name=et_transaction_name.getText().toString();
        receiver_id=et_receiver_id.getText().toString();
        if(amount.length()>0) amnt=Integer.parseInt(et_amount.getText().toString());
        if(amnt>0&&!pay_method.equalsIgnoreCase("Choose Payment Method")&&!type.equalsIgnoreCase("Choose Transaction Type")&&transaction_name.length()>4&&receiver_id.length()>5){

            uploadPayment();
        }
        else {

            String message=getErrorMessage();
            AlertDialog.Builder builder=new AlertDialog.Builder(Other_Transaction.this);
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
                            sp_choose_transaction_type.setSelection(0);
                            sp_choose_payment_method.setSelection(0);
                            et_amount.setText("");
                            et_transaction_name.setText("");
                            et_receiver_id.setText("");
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
                params.put("transaction_name",transaction_name );
                params.put("transaction_type","Other Transaction" );
                params.put("payment_method", pay_method);
                params.put("amount",amount);
                params.put("receiver_id",receiver_id);
                params.put("sender_type", "Authority");
                params.put("receiver_type", "Unknown");
                params.put("debit/credit",type);
                params.put("table", "Transaction");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getErrorMessage() {

        if(!(receiver_id.length()>5)) return "Receiver id length must be greater than 5";
        else if(transaction_name.length()>4) return "Transaction name length have to be greater than 4";
        else if(pay_method.equalsIgnoreCase("Choose Payment Method")) return "Please Choose Payment Method";
        else if(type.equalsIgnoreCase("Choose Transaction Type")) return "Please Choose Payment Type";
        else if(amount.length()==0) return "Please Enter Valid Fee Amount";

        return "Please Enter Valid Data";
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
