package com.example.onlinepathshala;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edit_Transaction extends AppCompatActivity {

    EditText et_transation_type,et_transaction_name,et_receiver_type,et_receiver_id,et_amount;
    String transaction_type="",transaction_name="",receiver_type="",receiver_id="",amount="",debit_credit="",transaction_id="";
    Spinner sp_debit_credit;
    ArrayList<String> list=new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__transaction);
        transaction_type=getIntent().getStringExtra("transaction_type");
        transaction_name=getIntent().getStringExtra("transaction_name");
        receiver_type=getIntent().getStringExtra("receiver_type");
        receiver_id=getIntent().getStringExtra("receiver_id");
        amount=getIntent().getStringExtra("amount");
        transaction_id=getIntent().getStringExtra("transaction_id");
        debit_credit=getIntent().getStringExtra("debit_credit");
        sp_debit_credit=findViewById(R.id.debit_credit);
        progressDialog=new ProgressDialog(Edit_Transaction.this);
        progressDialog.setMessage("Please Wait.....");
        list.add("Debit");
        list.add("Credit");
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),R.layout.simple_spinner_dropdown,list);
        sp_debit_credit.setAdapter(arrayAdapter);
        et_transation_type=findViewById(R.id.transaction_type);
        et_transaction_name=findViewById(R.id.transaction_name);
        et_receiver_type=findViewById(R.id.receiver_type);
        et_receiver_id=findViewById(R.id.receiver_id);
        et_amount=findViewById(R.id.amount);
        et_transaction_name=findViewById(R.id.transaction_name);

        et_transation_type.setText(transaction_type);
        et_transaction_name.setText(transaction_name);
        et_receiver_type.setText(receiver_type);
        et_receiver_id.setText(receiver_id);
        et_amount.setText(amount);

        sp_debit_credit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                debit_credit=list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void submit(View view){

        transaction_type=et_transation_type.getText().toString();
        transaction_name=et_transaction_name.getText().toString();
        receiver_type=et_receiver_type.getText().toString();
        receiver_id=et_receiver_id.getText().toString();
        amount=et_amount.getText().toString();

        if(transaction_type.length()>0&&transaction_name.length()>0&&receiver_id.length()>0&&receiver_type.length()>0&&amount.length()>0){

            insert_edit_data();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Enter Valid Data",Toast.LENGTH_LONG).show();
        }


    }

    public void insert_edit_data(){

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.get_edit_transaction_url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {


                            Toast.makeText(getApplicationContext(),"Transaction Edited Successfully", Toast.LENGTH_SHORT).show();
                            finish();


                        } else {
                            Toast.makeText(getApplicationContext(),"Fail to Edit", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("transaction_id", transaction_id);
                params.put("transaction_type", transaction_name);
                params.put("transaction_name", transaction_name);
                params.put("receiver_type", receiver_type);
                params.put("receiver_id", receiver_id);
                params.put("amount", amount);
                params.put("table", "Transaction");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
