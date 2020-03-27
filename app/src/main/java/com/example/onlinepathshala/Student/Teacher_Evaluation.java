package com.example.onlinepathshala.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Authority.Questions;
import com.example.onlinepathshala.Authority.Student;
import com.example.onlinepathshala.Authority.Subject;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Teacher_Evaluation extends AppCompatActivity {

    Spinner sp_class,sp_student,sp_subject;
    AlertDialog dialog;
    int month,day,year;
    String class_name,class_id,date,teacher_id,subject_name,subject_id,time,meridiem,exam_name;
    ArrayList<Object> subject_infos=new ArrayList<>();
    ArrayList<Questioniare> question_infos=new ArrayList<>();
    RecyclerView recyclerView;
    String teacher_name;
    ProgressDialog progressDialog;
    ArrayList<Questions> questions=new ArrayList<>();
    ArrayList<ArrayList<Questions>> question_sets=new ArrayList<>();
    TextView tv_name,no_item,tv_id;
    int[][] rating_value=new int[5][4];
    int[] rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__evaluation);
        teacher_name=getIntent().getStringExtra("name");
        teacher_id=getIntent().getStringExtra("id");
        progressDialog=new ProgressDialog(Teacher_Evaluation.this);
        tv_name=findViewById(R.id.teacher_name);
        tv_name.setText("Teacher Name: "+teacher_name);
        tv_id=findViewById(R.id.teacher_id);
        tv_id.setText("Teacher Id: "+teacher_id);
        recyclerView=findViewById(R.id.recycle);
        no_item=findViewById(R.id.no_item);
        rating=new int[question_infos.size()];
        initialize_rating(5);

        get_all_questions();

    }

    public void initialize_rating(int rating_vale){

        for(int i=0;i<question_infos.size();i++){

            rating[i]=rating_vale;
        }
    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<ArrayList<Questions>> question_sets;
        public RecycleAdapter(ArrayList<ArrayList<Questions>> question_sets){
            this.question_sets=question_sets;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements  View.OnLongClickListener{

            View mView;
            RecyclerView recyclerView;
            TextView set_no,date;
            Button submit;
            FloatingActionButton add;
            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                recyclerView=mView.findViewById(R.id.recycle);
                set_no=mView.findViewById(R.id.set_no);
                date=mView.findViewById(R.id.date);
                submit=mView.findViewById(R.id.submit);
                add=mView.findViewById(R.id.add);
                add.hide();


            }

            @Override
            public boolean onLongClick(View view) {


                int position =getLayoutPosition();
                return true;
            }

        }


        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_set_layout_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {





            holder.set_no.setText("Question Set No."+question_sets.get(position).get(0).question_set_number);
            holder.date.setText("Create At :"+question_sets.get(position).get(0).date);
            if(question_sets.size()>0){

                ArrayList<Questions> questions=question_sets.get(position);
                RecycleAdapter2 recyclerView2=new RecycleAdapter2(questions);
                holder.recyclerView.setAdapter(recyclerView2);
            }
            holder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Questions> questions=question_sets.get(position);
                    calculate_avg_rating(questions.size(),questions.get(0).question_set_number);

                }
            });





        }

        @Override
        public int getItemCount() {
            return question_sets.size();
        }



    }

    public class RecycleAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<Questions> questions;
        public RecycleAdapter2(ArrayList<Questions> questions){
            this.questions=questions;
        }
        public  class ViewAdapter1 extends RecyclerView.ViewHolder {

            View mView;
            Button edit,delete;
            LinearLayout linearLayout;
            TextView question;
            CheckBox option1,option2,option3,option4;
            public ViewAdapter1(View itemView) {
                super(itemView);
                mView=itemView;
                question=mView.findViewById(R.id.question);
                option1=mView.findViewById(R.id.option1);
                option2=mView.findViewById(R.id.option2);
                edit=mView.findViewById(R.id.edit);
                delete=mView.findViewById(R.id.delete);

            }


        }

        public  class ViewAdapter2 extends RecyclerView.ViewHolder {

            View mView;
            Button edit,delete;
            LinearLayout linearLayout;
            TextView question;
            CheckBox option1,option2,option3,option4;
            public ViewAdapter2(View itemView) {
                super(itemView);
                mView=itemView;
                question=mView.findViewById(R.id.question);
                option1=mView.findViewById(R.id.option1);
                option2=mView.findViewById(R.id.option2);
                option3=mView.findViewById(R.id.option3);
                edit=mView.findViewById(R.id.edit);
                delete=mView.findViewById(R.id.delete);

            }


        }

        public  class ViewAdapter3 extends RecyclerView.ViewHolder {

            View mView;
            Button edit,delete;
            LinearLayout linearLayout;
            TextView question;
            CheckBox option1,option2,option3,option4;
            public ViewAdapter3(View itemView) {
                super(itemView);
                mView=itemView;
                question=mView.findViewById(R.id.question);
                option1=mView.findViewById(R.id.option1);
                option2=mView.findViewById(R.id.option2);
                option3=mView.findViewById(R.id.option3);
                option4=mView.findViewById(R.id.option4);
                edit=mView.findViewById(R.id.edit);
                delete=mView.findViewById(R.id.delete);
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

            }


        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=null;

            if(viewType==2){

                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.option_a_layout,parent,false);
                return new ViewAdapter1(view);
            }
            else if(viewType==3){

                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.option_b_layout,parent,false);
                return new ViewAdapter2(view);
            }
            else if(viewType==4){

                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.option_c_layout,parent,false);
                return new ViewAdapter3(view);
            }

            return new ViewAdapter1(view);

        }

        @Override
        public int getItemViewType(int position) {
            if (questions.get(position).total_options.equalsIgnoreCase("2")) {
                return 2;
            }
            else if(questions.get(position).total_options.equalsIgnoreCase("3"))
            {
                return 3;
            }
            else if(questions.get(position).total_options.equalsIgnoreCase("4"))
            {
                return 4;
            }

            return 2;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


            if(getItemViewType(position)==2){

                Questions question=questions.get(position);
                ViewAdapter1 holder1=(ViewAdapter1) holder;
                holder1.question.setText((position+1)+". "+question.question.replace("?","")+" ?");
                holder1.option1.setText(question.option_name1);
                holder1.option2.setText(question.option_name2);
                holder1.option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b){
                            rating_value[position][0]=Integer.parseInt(question.option_value1);
                            rating_value[position][1]=0;
                            holder1.option2.setChecked(false);
                        }


                    }
                });

                holder1.option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b){

                            rating_value[position][0]=0;
                            rating_value[position][1]=Integer.parseInt(question.option_value2);
                            holder1.option1.setChecked(false);
                        }

                    }
                });

            }
            if(getItemViewType(position)==3){

                Questions question=questions.get(position);
                ViewAdapter2 holder1=(ViewAdapter2) holder;
                holder1.question.setText((position+1)+". "+question.question.replace("?","")+" ?");
                holder1.option1.setText(question.option_name1);
                holder1.option2.setText(question.option_name2);
                holder1.option3.setText(question.option_name3);
                holder1.option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b) {
                            holder1.option2.setChecked(false);
                            holder1.option3.setChecked(false);

                            rating_value[position][0]=Integer.parseInt(question.option_value1);
                            rating_value[position][1]=0;
                            rating_value[position][2]=0;
                        }
                    }
                });

                holder1.option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {
                            holder1.option3.setChecked(false);
                            holder1.option1.setChecked(false);

                            rating_value[position][0]=0;
                            rating_value[position][1]=Integer.parseInt(question.option_value2);
                            rating_value[position][2]=0;
                        }
                    }
                });
                holder1.option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {
                            holder1.option2.setChecked(false);
                            holder1.option1.setChecked(false);

                            rating_value[position][0]=0;
                            rating_value[position][1]=0;
                            rating_value[position][2]=Integer.parseInt(question.option_value3);
                        }

                    }
                });



            }
            if(getItemViewType(position)==4){

                Questions question=questions.get(position);
                ViewAdapter3 holder1=(ViewAdapter3) holder;
                holder1.question.setText((position+1)+". "+question.question.replace("?","")+" ?");
                holder1.option1.setText(question.option_name1);
                holder1.option2.setText(question.option_name2);
                holder1.option3.setText(question.option_name3);
                holder1.option4.setText(question.option_name4);
                holder1.option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {
                            holder1.option2.setChecked(false);
                            holder1.option3.setChecked(false);
                            holder1.option4.setChecked(false);

                            rating_value[position][0]=Integer.parseInt(question.option_value1);
                            rating_value[position][1]=0;
                            rating_value[position][2]=0;
                            rating_value[position][3]=0;
                        }

                    }
                });

                holder1.option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {
                            holder1.option1.setChecked(false);
                            holder1.option3.setChecked(false);
                            holder1.option4.setChecked(false);

                            rating_value[position][0]=0;
                            rating_value[position][1]=Integer.parseInt(question.option_value2);
                            rating_value[position][2]=0;
                            rating_value[position][3]=0;
                        }
                    }
                });
                holder1.option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {
                            holder1.option2.setChecked(false);
                            holder1.option1.setChecked(false);
                            holder1.option4.setChecked(false);

                            rating_value[position][0]=0;
                            rating_value[position][1]=0;
                            rating_value[position][2]=Integer.parseInt(question.option_value3);
                            rating_value[position][3]=0;
                        }
                    }
                });
                holder1.option4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {

                            holder1.option2.setChecked(false);
                            holder1.option3.setChecked(false);
                            holder1.option1.setChecked(false);

                            rating_value[position][0]=0;
                            rating_value[position][1]=0;
                            rating_value[position][2]=0;
                            rating_value[position][3]=Integer.parseInt(question.option_value4);
                        }
                    }
                });



            }




        }

        @Override
        public int getItemCount() {
            return questions.size();
        }



    }

    public void get_all_questions(){

        JSONArray postparams = new JSONArray();
        postparams.put("Teacher_Evaluation_Question");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        postparams.put(teacher_id);

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_teacher_evaluation_question_for_student,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string=response.getString(0);

                            String question_set="0";
                            if(!string.contains("no item")){

                                question_sets.clear();
                                questions.clear();

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        if(!question_set.equalsIgnoreCase(member.getString("question_set_number"))) {

                                            System.out.println("set: "+member.getString("question_set_number"));
                                            if(questions.size()>0){

                                                question_sets.add(questions);
                                            }
                                            question_set=member.getString("question_set_number");
                                            questions=new ArrayList<>();
                                        }

                                        String id = member.getString("id");
                                        String question = member.getString("question");
                                        String total_options=member.getString("total_options");
                                        String option_name1 = member.getString("option_name1");
                                        String option_name2 = member.getString("option_name2");
                                        String option_name3 = member.getString("option_name3");
                                        String option_name4 = member.getString("option_name4");
                                        String option_value1 = member.getString("option_value1");
                                        String option_value2 = member.getString("option_value2");
                                        String option_value3 = member.getString("option_value3");
                                        String option_value4 = member.getString("option_value4");
                                        String question_set_number = member.getString("question_set_number");
                                        String date = member.getString("create_at");
                                        String live = member.getString("live");
                                        questions.add(new Questions(id,question,total_options,option_name1,option_name2,option_name3,option_name4,option_value1,option_value2,option_value3,option_value4,question_set_number,live,date));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if(questions.size()>0){

                                    question_sets.add(questions);
                                }
                                RecycleAdapter recycleAdapter=new RecycleAdapter(question_sets);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                no_item.setVisibility(View.GONE);
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(question_sets);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
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






    public void calculate_avg_rating(int size,String question_set_number){

        float sum=0;
        for(int i=0;i<size;i++){

            if(rating_value[i][0]==0&&rating_value[i][1]==0&&rating_value[i][2]==0&&rating_value[i][3]==0) {

                Toast.makeText(getApplicationContext(),"Please Choose Question "+(i+1)+" Answer",Toast.LENGTH_LONG).show();
                return;
            }
            sum+=rating_value[i][0]+rating_value[i][1]+rating_value[i][2]+rating_value[i][3];
        }

        float avg_rating=sum/(float)size;

        Toast.makeText(getApplicationContext(),"Your Rating : "+avg_rating,Toast.LENGTH_LONG).show();
        submit(avg_rating,question_set_number);
    }

    public void submit(float avg_rating,String question_set_number){

            progressDialog.setTitle("Submit....!");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.insert_rating_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();



                            if (response.contains("success")) {
                                Toast.makeText(getApplicationContext(),"Submitted Successfully", Toast.LENGTH_SHORT).show();

                                get_all_questions();

                            }
                            else {

                                Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
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
                    params.put("student_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                    params.put("teacher_id", teacher_id);
                    params.put("question_set_number", question_set_number);
                    params.put("rating",avg_rating+"");
                    params.put("table", "Rating");
                    params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                    return params;
                }
            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);




    }




}
