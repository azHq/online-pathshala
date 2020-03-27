package com.example.onlinepathshala.Student;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.onlinepathshala.Transaction_History_Item;
import com.example.onlinepathshala.Transaction_History_Item2;
import com.example.onlinepathshala.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Transaction_History_For_Student extends Fragment {

    public Transaction_History_For_Student() {
        // Required empty public constructor
    }


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
    String transaction_type2="Student Fee";
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_transaction__history__for__student, container, false);
        progressDialog=new ProgressDialog(getContext());

        li_start_date=view.findViewById(R.id.start_date);
        li_end_date=view.findViewById(R.id.end_date);
        tv_day=view.findViewById(R.id.day);
        linear1=view.findViewById(R.id.linear1);
        linear1.setVisibility(View.VISIBLE);
        linear2=view.findViewById(R.id.linear2);
        linear2.setVisibility(View.GONE);
        recyclerView2=view.findViewById(R.id.recycle2);
        customAdapter=new CustomAdapter(getActivity(),1, year);




        start=view.findViewById(R.id.start);
        end=view.findViewById(R.id.end);
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


        recyclerView=view.findViewById(R.id.recycle1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleAdapter=new RecycleAdapter(transaction_history_items,1);
        recycleAdapter2=new RecycleAdapter2(transaction_history_items2,1);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView2.setAdapter(recycleAdapter2);
        sp_month=view.findViewById(R.id.month);
        sp_year=view.findViewById(R.id.year);
        sp_year2=view.findViewById(R.id.year2);
        sp_search_type=view.findViewById(R.id.search_type);
        show_all_transaction=view.findViewById(R.id.show_all_transaction);
        show_daily_transaction=view.findViewById(R.id.show_daily_transaction);
        show_monthly_transaction=view.findViewById(R.id.show_monthly_transaction);
        show_yearly_transaction=view.findViewById(R.id.show_yearly_transaction);


        li_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                try {
                                    if(new SimpleDateFormat("dd-mm-yyyy").parse(date).compareTo(new SimpleDateFormat("dd-mm-yyyy").parse(end_date))>0){

                                        Toast.makeText(getContext(),"Start date can not be after end date",Toast.LENGTH_LONG).show();
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
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                try {
                                    if(new SimpleDateFormat("dd-mm-yyyy").parse(start_date).compareTo(new SimpleDateFormat("dd-mm-yyyy").parse(date))>0){

                                        Toast.makeText(getContext(),"Start date can not be after end date",Toast.LENGTH_LONG).show();
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
                DatePickerDialog picker = new DatePickerDialog(getContext(),
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






        search_type.add("Individual");
        search_type.add("Daily");
        search_type.add("Monthly");
        search_type.add("Yearly");
        sp_search_type.setAdapter(new CustomAdapter(getContext(),1,search_type));
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

                    Toast.makeText(getContext(),year2,Toast.LENGTH_LONG).show();
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


        sp_month.setAdapter(new CustomAdapter(getContext(),1, months));
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getContext(),year2+"1",Toast.LENGTH_LONG).show();
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

        return view;
    }

    public void getAllMemberData(String row_value,String type,String start_date,String end_date){


        JSONArray postparams = new JSONArray();
        postparams.put("Transaction");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(start_date);
        postparams.put(end_date);
        postparams.put(row_value);
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getId());
        isSpinnerInitial=true;
        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_teacher_transaction,postparams,
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
                                Toast.makeText(getContext(),"No Transaction Still Added",Toast.LENGTH_LONG).show();
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
    public void getAllMemberData2(String row_value,String type,String start_date,String end_date){

        JSONArray postparams = new JSONArray();
        postparams.put("Transaction");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(start_date);
        postparams.put(end_date);
        postparams.put(row_value);
        postparams.put("Student");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getId());

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_teacher_transaction2,postparams,
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

                                        String transaction_type=member.getString("transaction_name");
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
                                Toast.makeText(getContext(),"No Transaction Still Added",Toast.LENGTH_LONG).show();
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
            TextView transaction_type,amount,date,id;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;

                id=mView.findViewById(R.id.id);
                transaction_type=mView.findViewById(R.id.transaction_type);
                amount=mView.findViewById(R.id.amount);
                date=mView.findViewById(R.id.date);


            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=null;


            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item_for_student,parent,false);

            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Transaction_History_Item transaction_history_item=attendence_info.get(position);

            holder.id.setText(transaction_history_item.id);
            holder.transaction_type.setText(transaction_history_item.fee_type);
            holder.amount.setText(transaction_history_item.amount);
            holder.date.setText(transaction_history_item.date);




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
            TextView transaction_type,amount;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;


                transaction_type=mView.findViewById(R.id.transaction_type);
                amount=mView.findViewById(R.id.amount);


            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=null;


            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item_for_student2,parent,false);

            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Transaction_History_Item2 transaction_history_item=attendence_info.get(position);

            holder.transaction_type.setText(transaction_history_item.transaction_type);
            holder.amount.setText(transaction_history_item.amount);





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


}
