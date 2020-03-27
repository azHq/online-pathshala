package com.example.onlinepathshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.onlinepathshala.Authority.Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class Transaction_History_For_Authority extends AppCompatActivity {

    ArrayList<String> months=new ArrayList<String>(Arrays.asList("January","February","March","April","May","June","July","August","September","October","November","December"));
    ArrayList<String> year=new ArrayList<>();
    ArrayList<String> transaction_type=new ArrayList<>();
    ArrayList<String> search_type=new ArrayList<>();
    HashMap<String,ArrayList<String>> map=new HashMap<>();
    ArrayList<Transaction_History_Item> transaction_history_items=new ArrayList<>();
    ArrayList<Transaction_History_Item2> transaction_history_items2=new ArrayList<>();
    Spinner sp_month,sp_year,sp_year2,sp_transaction_type,sp_search_type,sp_debit_credit;
    LinearLayout show_monthly_transaction,show_yearly_transaction,show_daily_transaction,show_all_transaction;
    ProgressDialog progressDialog;
    RecycleAdapter recycleAdapter;
    RecyclerView recyclerView;
    String start_date="",end_date="";
    TextView start,end,tv_day;
    LinearLayout li_start_date,li_end_date,linear1,linear2;
    String transaction_type2="All Transaction";
    String debit_credit="Both";
    String date3;
    ArrayList<String> type=new ArrayList<>();
    RecyclerView recyclerView2;
    RecycleAdapter2 recycleAdapter2;
    int month;
    String year2;
    CustomAdapter customAdapter;
    private boolean isSpinnerInitial = true;
    int search_option=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction__history_for_authority);

        progressDialog=new ProgressDialog(Transaction_History_For_Authority.this);

        li_start_date=findViewById(R.id.start_date);
        li_end_date=findViewById(R.id.end_date);
        tv_day=findViewById(R.id.day);
        linear1=findViewById(R.id.linear1);
        linear1.setVisibility(View.VISIBLE);
        linear2=findViewById(R.id.linear2);
        linear2.setVisibility(View.GONE);
        recyclerView2=findViewById(R.id.recycle2);
        customAdapter=new CustomAdapter(getApplicationContext(),1, year);




        start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        Calendar cal = Calendar.getInstance();

        month=cal.get(Calendar.MONTH)+2;
        cal.add(Calendar.DATE, 1);

        Date date = cal.getTime();

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

        end_date=format1.format(date);
        date3=end_date;
        Date myDate = null;
        try {
            myDate = format1.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            date = format1.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(myDate);
        cal1.add(Calendar.DAY_OF_YEAR, -2);
        Date previousDate = cal1.getTime();
        start_date=format1.format(previousDate);
        start.setText(start_date);
        end.setText(end_date);
        year2=cal.get(Calendar.YEAR)+"";


        recyclerView=findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycleAdapter=new RecycleAdapter(transaction_history_items,1);
        recycleAdapter2=new RecycleAdapter2(transaction_history_items2,1);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView2.setAdapter(recycleAdapter2);
        sp_month=findViewById(R.id.month);
        sp_year=findViewById(R.id.year);
        sp_year2=findViewById(R.id.year2);
        sp_transaction_type=findViewById(R.id.transaction_type);
        sp_search_type=findViewById(R.id.search_type);
        show_all_transaction=findViewById(R.id.show_all_transaction);
        show_daily_transaction=findViewById(R.id.show_daily_transaction);
        show_monthly_transaction=findViewById(R.id.show_monthly_transaction);
        show_yearly_transaction=findViewById(R.id.show_yearly_transaction);

        sp_debit_credit=findViewById(R.id.debit_or_credit);

        li_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(Transaction_History_For_Authority.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                               String date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                try {
                                    if(new SimpleDateFormat("dd-mm-yyyy").parse(date).compareTo(new SimpleDateFormat("dd-mm-yyyy").parse(end_date))>0){

                                        Toast.makeText(getApplicationContext(),"Start date can not be after end date",Toast.LENGTH_LONG).show();
                                    }
                                    else{

                                        start_date=date;
                                        start.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        getAllMemberData(transaction_type2,debit_credit,start_date,end_date);

                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, year, month, day);
                picker.show();
            }
        });
        li_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(Transaction_History_For_Authority.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                try {
                                    if(new SimpleDateFormat("dd-mm-yyyy").parse(start_date).compareTo(new SimpleDateFormat("dd-mm-yyyy").parse(date))>0){

                                        Toast.makeText(getApplicationContext(),"Start date can not be after end date",Toast.LENGTH_LONG).show();
                                    }
                                    else{

                                        end_date=date;
                                        end.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        getAllMemberData(transaction_type2,debit_credit,start_date,end_date);

                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, year, month, day);
                picker.show();
            }
        });
        tv_day.setText(date3);
        show_daily_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(Transaction_History_For_Authority.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                date3=date;
                                tv_day.setText(date);
                                getAllMemberData(transaction_type2,debit_credit,date3,date3);
                                start_date=date3;
                                end_date=date3;
                                start.setText(date3);
                                end.setText(date3);




                            }
                        }, year, month, day);
                picker.show();
            }
        });




        transaction_type.add("All Transaction");
        transaction_type.add("Student Fee");
        transaction_type.add("Teacher Salary");
        transaction_type.add("Stuff Salary");
        transaction_type.add("Other Transaction");
        sp_transaction_type.setAdapter(new CustomAdapter(getApplicationContext(),1,transaction_type));
        sp_transaction_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                LinearLayout linearLayout=(LinearLayout) adapterView.getChildAt(0);
                TextView textView=(TextView) linearLayout.getChildAt(0);
                textView.setTextColor(Color.WHITE);


                if(search_option==1){

                    getAllMemberData(transaction_type.get(i),debit_credit,start_date,end_date);
                    transaction_type2=transaction_type.get(i);
                }
                else{

                    transaction_type2=transaction_type.get(i);
                    getAllMemberData2(transaction_type2,debit_credit,1+"-"+month+"-"+year2,31+"-"+month+"-"+year2);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        type.add("Both");
        type.add("Debit");
        type.add("Credit");
        sp_debit_credit.setAdapter(new CustomAdapter(getApplicationContext(),1,type));
        sp_debit_credit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                LinearLayout linearLayout=(LinearLayout) adapterView.getChildAt(0);
                TextView textView=(TextView) linearLayout.getChildAt(0);
                textView.setTextColor(Color.WHITE);

                if(search_option==1){

                    debit_credit=type.get(i);
                    getAllMemberData(transaction_type2,debit_credit,start_date,end_date);
                }
                else{

                    debit_credit=type.get(i);
                    getAllMemberData2(transaction_type2,debit_credit,1+"-"+month+"-"+year2,31+"-"+month+"-"+year2);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        search_type.add("Individual");
        search_type.add("Daily");
        search_type.add("Monthly");
        search_type.add("Yearly");
        sp_search_type.setAdapter(new CustomAdapter(getApplicationContext(),1,search_type));
        sp_search_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                LinearLayout linearLayout=(LinearLayout) adapterView.getChildAt(0);
                TextView textView=(TextView) linearLayout.getChildAt(0);
                textView.setTextColor(Color.WHITE);
                if(i==0){

                    search_option=1;
                    linear2.setVisibility(View.GONE);
                    linear1.setVisibility(View.VISIBLE);
                    show_daily_transaction.setVisibility(View.GONE);
                    show_monthly_transaction.setVisibility(View.GONE);
                    show_yearly_transaction.setVisibility(View.GONE);
                    show_all_transaction.setVisibility(View.VISIBLE);
                    getAllMemberData(transaction_type2,debit_credit,start_date,end_date);
                }
                else if(i==1){

                    search_option=1;
                    linear2.setVisibility(View.GONE);
                    linear1.setVisibility(View.VISIBLE);
                    show_daily_transaction.setVisibility(View.VISIBLE);
                    show_monthly_transaction.setVisibility(View.GONE);
                    show_yearly_transaction.setVisibility(View.GONE);
                    show_all_transaction.setVisibility(View.GONE);
                    getAllMemberData(transaction_type2,debit_credit,date3,date3);
                }
                else if(i==2){

                    search_option=2;
                    linear2.setVisibility(View.VISIBLE);
                    linear1.setVisibility(View.GONE);
                    show_daily_transaction.setVisibility(View.GONE);
                    show_monthly_transaction.setVisibility(View.VISIBLE);
                    show_yearly_transaction.setVisibility(View.GONE);
                    show_all_transaction.setVisibility(View.GONE);


                    getAllMemberData2(transaction_type2,debit_credit,1+"-"+month+"-"+year2,31+"-"+month+"-"+year2);
                }
                else if(i==3){

                    search_option=2;
                    linear2.setVisibility(View.VISIBLE);
                    linear1.setVisibility(View.GONE);
                    show_daily_transaction.setVisibility(View.GONE);
                    show_monthly_transaction.setVisibility(View.GONE);
                    show_yearly_transaction.setVisibility(View.VISIBLE);
                    show_all_transaction.setVisibility(View.GONE);
                    getAllMemberData2(transaction_type2,debit_credit,1+"-"+1+"-"+year2,31+"-"+12+"-"+year2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_month.setAdapter(new CustomAdapter(getApplicationContext(),1, months));
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                month=i+1;
                getAllMemberData2(transaction_type2,debit_credit,1+"-"+month+"-"+year2,31+"-"+month+"-"+year2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int initialSelectedPosition=sp_year2.getSelectedItemPosition();
        sp_year2.setSelection(initialSelectedPosition, false);
        initialSelectedPosition=sp_year.getSelectedItemPosition();
        sp_year.setSelection(initialSelectedPosition, false);
        sp_year2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                year2=year.get(i);
                getAllMemberData2(transaction_type2,debit_credit,1+"-"+1+"-"+year2,31+"-"+12+"-"+year2);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                year2=year.get(i);
                getAllMemberData2(transaction_type2,debit_credit,1+"-"+month+"-"+year2,31+"-"+month+"-"+year2);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_month.setSelection(cal.get(Calendar.MONTH));
        sp_year2.setAdapter(customAdapter);
        sp_year.setAdapter(customAdapter);




    }

    public void getAllMemberData(String row_value,String type,String start_date,String end_date){


        JSONArray postparams = new JSONArray();
        postparams.put("Transaction");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(start_date);
        postparams.put(end_date);
        postparams.put(row_value);
        postparams.put(type);
        isSpinnerInitial=true;
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_transaction_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);
                            transaction_history_items.clear();
                            year.clear();
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){


                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String sender_id=member.getString("sender_id");
                                        String receiver_id=member.getString("receiver_id");
                                        String sender_type=member.getString("sender_type");
                                        String receiver_type=member.getString("receiver_type");
                                        String fee_type=member.getString("transaction_name");
                                        String transaction_type=member.getString("transaction_type");
                                        String debit_credit=member.getString("debit_credit");
                                        String amount=member.getString("amount");
                                        String payment_method=member.getString("payment_method");
                                        String create_date=member.getString("create_date");
                                        String[] s=create_date.split("-");
                                        if(!year.contains(s[2])) year.add(s[2]);
                                        transaction_history_items.add(new Transaction_History_Item(id,sender_id,sender_type,receiver_id,receiver_type,fee_type,transaction_type,amount,debit_credit,payment_method,create_date));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(!year.contains("2020")) year.add("2020");
                                if(!year.contains("2021")) year.add("2021");
                                if(!year.contains("2022")) year.add("2022");
                                if(!year.contains("2023")) year.add("2023");
                                customAdapter.notifyDataSetChanged();
                                recycleAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                            else{
                                if(!year.contains("2020")) year.add("2020");
                                if(!year.contains("2021")) year.add("2021");
                                if(!year.contains("2022")) year.add("2022");
                                if(!year.contains("2023")) year.add("2023");
                                recycleAdapter.notifyDataSetChanged();
                                customAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No Transaction Still Added",Toast.LENGTH_LONG).show();
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
    public void getAllMemberData2(String row_value,String type,String start_date,String end_date){

        JSONArray postparams = new JSONArray();
        postparams.put("Transaction");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(start_date);
        postparams.put(end_date);
        postparams.put(row_value);
        postparams.put(type);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_transaction_url2,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);
                            transaction_history_items2.clear();
                            year.clear();
                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){


                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);

                                        String transaction_type=member.getString("transaction_type");
                                        String debit_credit=member.getString("debit_credit");
                                        String amount=member.getString("total");
                                        String create_date=member.getString("create_date");
                                        String[] s=create_date.split("-");
                                        if(!year.contains(s[2])) year.add(s[2]);
                                        System.out.println(transaction_type+","+amount);
                                        transaction_history_items2.add(new Transaction_History_Item2(transaction_type,amount,debit_credit,create_date));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(!year.contains("2020")) year.add("2020");
                                if(!year.contains("2021")) year.add("2021");
                                if(!year.contains("2022")) year.add("2022");
                                if(!year.contains("2023")) year.add("2023");
                                customAdapter.notifyDataSetChanged();
                                recycleAdapter2.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                            else{
                                if(!year.contains("2020")) year.add("2020");
                                if(!year.contains("2021")) year.add("2021");
                                if(!year.contains("2022")) year.add("2022");
                                if(!year.contains("2023")) year.add("2023");
                                recycleAdapter2.notifyDataSetChanged();
                                customAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No Transaction Still Added",Toast.LENGTH_LONG).show();
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

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Transaction_History_Item> attendence_info;
        public int flag;
        public RecycleAdapter(ArrayList<Transaction_History_Item> attendence_info,int flag){
            this.attendence_info=attendence_info;
            this.flag=flag;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView month;
            Button edit;
            TextView id,sender_id,sender_type,receiver_id,receiver_type,fee_type,transaction_type,amount,payment_method,date,debit_credit;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;

                id=mView.findViewById(R.id.transaction_id);
                sender_id =mView.findViewById(R.id.sender_id);
                sender_type=mView.findViewById(R.id.sender_type);
                receiver_id=mView.findViewById(R.id.receiver_id);
                receiver_type=mView.findViewById(R.id.receiver_type);
                fee_type=mView.findViewById(R.id.fee_type);
                transaction_type=mView.findViewById(R.id.transaction_type2);
                amount=mView.findViewById(R.id.amount);
                payment_method=mView.findViewById(R.id.payment_method);
                date=mView.findViewById(R.id.date);
                debit_credit=mView.findViewById(R.id.de_cre);
                edit=mView.findViewById(R.id.edit);


            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=null;


            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_rec_item,parent,false);

            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Transaction_History_Item transaction_history_item=attendence_info.get(position);

            holder.id.setText(transaction_history_item.id);
            holder.sender_id.setText(transaction_history_item.sender_id);
            holder.sender_type.setText(transaction_history_item.sender_type);
            holder.receiver_id.setText(transaction_history_item.receiver_id);
            holder.receiver_type.setText(transaction_history_item.receiver_type);
            holder.fee_type.setText(transaction_history_item.fee_type);
            holder.transaction_type.setText(transaction_history_item.transaction_type);
            holder.amount.setText(transaction_history_item.amount);
            holder.payment_method.setText(transaction_history_item.payment_method);
            holder.debit_credit.setText(transaction_history_item.debit_credit);
            holder.date.setText(transaction_history_item.date);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!transaction_history_item.sender_id.equalsIgnoreCase(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId())){

                        Intent tnt=new Intent(getApplicationContext(),Edit_Transaction.class);
                        tnt.putExtra("transaction_id",transaction_history_item.id);
                        tnt.putExtra("transaction_type",transaction_history_item.transaction_type);
                        tnt.putExtra("transaction_name",transaction_history_item.fee_type);
                        tnt.putExtra("receiver_type",transaction_history_item.receiver_type);
                        tnt.putExtra("receiver_id",transaction_history_item.receiver_id);
                        tnt.putExtra("amount",transaction_history_item.amount);
                        tnt.putExtra("debit_credit",transaction_history_item.debit_credit);
                        startActivity(tnt);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"You can not edit it",Toast.LENGTH_LONG).show();
                    }

                }
            });



        }

        @Override
        public int getItemCount() {
            return attendence_info.size();
        }



    }

    public class RecycleAdapter2 extends RecyclerView.Adapter<RecycleAdapter2.ViewAdapter>{

        ArrayList<Transaction_History_Item2> attendence_info;
        public int flag;
        public RecycleAdapter2(ArrayList<Transaction_History_Item2> attendence_info,int flag){
            this.attendence_info=attendence_info;
            this.flag=flag;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView month;
            TextView transaction_type,amount,debit_credit;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;


                transaction_type=mView.findViewById(R.id.transaction_type);
                amount=mView.findViewById(R.id.amount);
                debit_credit=mView.findViewById(R.id.debit_credit);


            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=null;


            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item2,parent,false);

            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Transaction_History_Item2 transaction_history_item=attendence_info.get(position);

            holder.transaction_type.setText(transaction_history_item.transaction_type);
            holder.amount.setText(transaction_history_item.amount);
            holder.debit_credit.setText(transaction_history_item.debit_credit);




        }

        @Override
        public int getItemCount() {
            return attendence_info.size();
        }



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
            view = inflter.inflate(R.layout.list_item, null);
            TextView names = (TextView) view.findViewById(R.id.teacher_name);
            if(flag==1){

              if(info.size()>0){
                  String member=(String) info.get(i);
                  names.setText(member);
              }


            }



            return view;
        }
    }

    public void showEditDialog(){

    }
}
