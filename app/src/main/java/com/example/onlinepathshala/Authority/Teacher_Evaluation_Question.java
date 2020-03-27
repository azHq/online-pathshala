package com.example.onlinepathshala.Authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.onlinepathshala.Profile_View;
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

public class Teacher_Evaluation_Question extends AppCompatActivity implements View.OnTouchListener {

    float posX=0;
    float posY=0;
    ArrayList<Questions> questions=new ArrayList<>();
    ArrayList<ArrayList<Questions>> question_sets=new ArrayList<>();
    float dX;
    float dY;
    int lastAction;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog,alertDialog2;
    TextView no_item;
    FloatingActionButton dragView;
    public String section_id="",class_id="";
    public int total_options=0;
    public int option_value1,option_value2,option_value3,option_value4;
    public int question_set_number=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__evaluation__question);
        progressDialog=new ProgressDialog(Teacher_Evaluation_Question.this);
        progressDialog.setMessage("Please Wait....");
        no_item=findViewById(R.id.no_item);
        dragView =findViewById(R.id.fab);
        dragView.setOnTouchListener(this);
        recyclerView=findViewById(R.id.recycle);


        get_all_questions();
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
            FloatingActionButton add;
            Button submit;
            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                recyclerView=mView.findViewById(R.id.recycle);
                add=mView.findViewById(R.id.add);
                set_no=mView.findViewById(R.id.set_no);
                date=mView.findViewById(R.id.date);
                submit=mView.findViewById(R.id.submit);

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

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    question_set_number=Integer.parseInt(question_sets.get(position).get(0).question_set_number);
                    show_dialog();
                }
            });

            if(question_sets.get(position).get(0).live.equalsIgnoreCase("1")){

                holder.submit.setText("Unsubmit");
            }
            holder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(question_sets.get(position).size()==5){

                        if(question_sets.get(position).get(0).live.equalsIgnoreCase("1")){

                            question_sets.get(position).get(0).live="0";
                            set_live(question_sets.get(position).get(0).question_set_number,holder.submit,0+"");
                        }
                        else{

                            question_sets.get(position).get(0).live="1";
                            set_live(question_sets.get(position).get(0).question_set_number,holder.submit,1+"");
                        }

                    }
                    else{

                        Toast.makeText(getApplicationContext(),"Please Add 5 Questions To Submit It",Toast.LENGTH_LONG).show();
                    }
                }
            });

            if(question_sets.get(position).size()==5) holder.add.hide();

            holder.set_no.setText("Question Set No."+question_sets.get(position).get(0).question_set_number);
            holder.date.setText("Create At :"+question_sets.get(position).get(0).date);
            if(question_sets.size()>0){

                ArrayList<Questions> questions=question_sets.get(position);
                RecycleAdapter2 recyclerView2=new RecycleAdapter2(questions);
                holder.recyclerView.setAdapter(recyclerView2);
            }
            question_set_number=Integer.parseInt(question_sets.get(0).get(0).question_set_number)+1;




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
                            holder1.option2.setChecked(false);
                        }


                    }
                });

                holder1.option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b){
                            holder1.option1.setChecked(false);
                        }

                    }
                });
                holder1.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        show_edit_dialog(question.id,question.total_options,question.question,question.option_name1,question.option_name2,question.option_name3,question.option_name4,question.option_value1,question.option_value2,question.option_value3,question.option_value4);
                    }
                });
                holder1.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        showDeleteDialogBox(question.id);
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
                        }
                    }
                });

                holder1.option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {
                            holder1.option3.setChecked(false);
                            holder1.option1.setChecked(false);
                        }
                    }
                });
                holder1.option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {
                            holder1.option2.setChecked(false);
                            holder1.option1.setChecked(false);
                        }

                    }
                });


                holder1.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        show_edit_dialog(question.id,question.total_options,question.question,question.option_name1,question.option_name2,question.option_name3,question.option_name4,question.option_value1,question.option_value2,question.option_value3,question.option_value4);
                    }
                });
                holder1.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        showDeleteDialogBox(question.id);
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
                        }
                    }
                });

                holder1.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        show_edit_dialog(question.id,question.total_options,question.question,question.option_name1,question.option_name2,question.option_name3,question.option_name4,question.option_value1,question.option_value2,question.option_value3,question.option_value4);
                    }
                });
                holder1.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        showDeleteDialogBox(question.id);
                    }
                });

            }




        }

        @Override
        public int getItemCount() {
            return questions.size();
        }



    }


    public void showOptionDialog(final String id){

        AlertDialog.Builder builder=new AlertDialog.Builder(Teacher_Evaluation_Question.this);
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        alertDialog2=builder.show();
        assign_teacher.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showDeleteDialogBox(id);
            }
        });


    }


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(Teacher_Evaluation_Question.this);
        View view=getLayoutInflater().inflate(R.layout.delete_dialog_box,null);
        Button yes=view.findViewById(R.id.yes);
        Button no=view.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                progressDialog.show();
                deleteItem(id);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
        alert.setView(view);
        alertDialog= alert.show();


    }

    public void deleteItem(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.single_row_delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            Toast.makeText(getApplicationContext(),"Question Deleted Successfully", Toast.LENGTH_SHORT).show();
                            get_all_questions();

                        } else {
                            Toast.makeText(getApplicationContext(),"Fail To Delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Connect Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("table", "Teacher_Evaluation_Question");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {




        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels-view.getHeight();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                posX=view.getX();
                posY=view.getY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:

                if(event.getRawY()<(height-view.getHeight()/1.2)&&event.getRawY()>view.getHeight()*2) view.setY(event.getRawY()+ dY);
                if(event.getRawX()<(width-view.getWidth()/1.2)&&event.getRawX()>view.getWidth()/1.3) view.setX(event.getRawX() + dX);
                if(Math.abs(view.getX()-posX)>=50||Math.abs(view.getY()-posY)>=50){
                    lastAction = MotionEvent.ACTION_MOVE;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN) {

                    show_dialog();
                }
                break;

            default:
                return false;
        }
        return true;
    }

    public void show_dialog()
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(Teacher_Evaluation_Question.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.question_set_layout,null);
        builder.setView(view);
        Spinner spinner=view.findViewById(R.id.num_of_option);
        LinearLayout option1=view.findViewById(R.id.option1);
        LinearLayout option2=view.findViewById(R.id.option2);
        LinearLayout option3=view.findViewById(R.id.option3);
        LinearLayout option4=view.findViewById(R.id.option4);

        EditText question=view.findViewById(R.id.question);
        EditText editText1=view.findViewById(R.id.text1);
        EditText editText2=view.findViewById(R.id.text2);
        EditText editText3=view.findViewById(R.id.text3);
        EditText editText4=view.findViewById(R.id.text4);

        ArrayList<String> values=new ArrayList<>();
        values.add("Choose Option Value");
        values.add("0");
        values.add("1");
        values.add("2");
        values.add("3");
        values.add("4");
        values.add("5");

        Spinner spinner1=view.findViewById(R.id.value1);
        Spinner spinner2=view.findViewById(R.id.value2);
        Spinner spinner3=view.findViewById(R.id.value3);
        Spinner spinner4=view.findViewById(R.id.value4);
        spinner1.setAdapter(new CustomAdapter(getApplicationContext(),1,values));
        spinner2.setAdapter(new CustomAdapter(getApplicationContext(),1,values));
        spinner3.setAdapter(new CustomAdapter(getApplicationContext(),1,values));
        spinner4.setAdapter(new CustomAdapter(getApplicationContext(),1,values));

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                option_value1=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                option_value2=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                option_value3=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                option_value4=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> num_of_options=new ArrayList<>();
        num_of_options.add("Choose Number Of Options");
        num_of_options.add("2");
        num_of_options.add("3");
        num_of_options.add("4");
        spinner.setAdapter(new CustomAdapter(getApplicationContext(),1,num_of_options));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                total_options=i+1;
                if(i==0){
                  option3.setVisibility(View.INVISIBLE);
                  option4.setVisibility(View.INVISIBLE);
                }
                else if(i==1){
                    option3.setVisibility(View.INVISIBLE);
                    option4.setVisibility(View.INVISIBLE);
                }
                else if(i==2){
                    option3.setVisibility(View.VISIBLE);

                }
                else if(i==3){
                    option3.setVisibility(View.VISIBLE);
                    option4.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button cancel=view.findViewById(R.id.cancel);
        Button save=view.findViewById(R.id.save);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(total_options>1&&question.getText().toString().length()>4){

                    if(total_options==2){

                        String option_name1=editText1.getText().toString();
                        String option_name2=editText2.getText().toString();
                        if(option_name1.length()>0&&option_name2.length()>0){

                            if(option_value1>=0&&option_value2>=0){

                                upload_question(question.getText().toString(),total_options+"",option_name1,option_value1+"", option_name2, option_value2+"","","","","");
                            }
                            else{

                                show_format_error("Please Choose Option Value","Please Enter Required Info..");
                            }


                        }
                        else {

                            show_format_error("Please Enter Option Name","Please Enter Required Info..");
                        }

                    }
                    else if(total_options==3){

                        String option_name1=editText1.getText().toString();
                        String option_name2=editText2.getText().toString();
                        String option_name3=editText3.getText().toString();
                        if(option_name1.length()>0&&option_name2.length()>0&&option_name3.length()>0){

                            if(option_value1>=0&&option_value2>=0&&option_value3>=0){

                                upload_question(question.getText().toString(),total_options+"",option_name1,option_value1+"", option_name2, option_value2+"",option_name3, option_value3+"","","");
                            }
                            else{

                                show_format_error("Please Choose Option Value","Please Enter Required Info..");
                            }


                        }
                        else {

                            show_format_error("Please Enter Option Name","Please Enter Required Info..");
                        }

                    }
                    else if(total_options==4){

                        String option_name1=editText1.getText().toString();
                        String option_name2=editText2.getText().toString();
                        String option_name3=editText3.getText().toString();
                        String option_name4=editText4.getText().toString();
                        if(option_name1.length()>0&&option_name2.length()>0&&option_name3.length()>0&&option_name4.length()>0){

                            if(option_value1>=0&&option_value2>=0&&option_value3>=0&&option_value4>=0){

                                upload_question(question.getText().toString(),total_options+"",option_name1,option_value1+"", option_name2, option_value2+"",option_name3, option_value3+"",option_name4, option_value4+"");
                            }
                            else{

                                show_format_error("Please Choose Option Value","Please Enter Required Info..");
                            }


                        }
                        else {

                            show_format_error("Please Enter Option Name","Please Enter Required Info..");
                        }
                    }
                }
                else{

                    if(question.getText().toString().length()<=4){

                        show_format_error("Question Length Must Be Greater Than 4","Please Enter Required Info..");
                    }
                    else{

                        show_format_error("Please Choose Number Of Options","Please Enter Required Info..");
                    }

                }


            }
        });
        alertDialog=builder.show();



    }

    public void show_edit_dialog(String id,String total_options2,String question_text,String option_name1,String option_name2,String option_name3,String option_name4,String option_value11,String option_value12,String option_value13,String option_value14)
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(Teacher_Evaluation_Question.this);
        View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.question_set_layout,null);
        builder.setView(view);
        Spinner spinner=view.findViewById(R.id.num_of_option);
        LinearLayout option1=view.findViewById(R.id.option1);
        LinearLayout option2=view.findViewById(R.id.option2);
        LinearLayout option3=view.findViewById(R.id.option3);
        LinearLayout option4=view.findViewById(R.id.option4);

        EditText question=view.findViewById(R.id.question);
        EditText editText1=view.findViewById(R.id.text1);
        EditText editText2=view.findViewById(R.id.text2);
        EditText editText3=view.findViewById(R.id.text3);
        EditText editText4=view.findViewById(R.id.text4);

        question.setText(question_text);
        editText1.setText(option_name1);
        editText2.setText(option_name2);
        editText3.setText(option_name3);
        editText4.setText(option_name4);

        ArrayList<String> values=new ArrayList<>();
        values.add("Choose Option Value");
        values.add("0");
        values.add("1");
        values.add("2");
        values.add("3");
        values.add("4");
        values.add("5");

        Spinner spinner1=view.findViewById(R.id.value1);
        Spinner spinner2=view.findViewById(R.id.value2);
        Spinner spinner3=view.findViewById(R.id.value3);
        Spinner spinner4=view.findViewById(R.id.value4);
        spinner1.setAdapter(new CustomAdapter(getApplicationContext(),1,values));
        spinner2.setAdapter(new CustomAdapter(getApplicationContext(),1,values));
        spinner3.setAdapter(new CustomAdapter(getApplicationContext(),1,values));
        spinner4.setAdapter(new CustomAdapter(getApplicationContext(),1,values));

        int index2=values.indexOf(option_value11);
        spinner1.setSelection(index2);
        index2=values.indexOf(option_value12);
        spinner2.setSelection(index2);
        index2=values.indexOf(option_value13);
        spinner3.setSelection(index2);
        index2=values.indexOf(option_value14);
        spinner4.setSelection(index2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                option_value1=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                option_value2=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                option_value3=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                option_value4=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> num_of_options=new ArrayList<>();
        num_of_options.add("Choose Number Of Options");
        num_of_options.add("2");
        num_of_options.add("3");
        num_of_options.add("4");
        spinner.setAdapter(new CustomAdapter(getApplicationContext(),1,num_of_options));
        int index=num_of_options.indexOf(total_options2);
        spinner.setSelection(index);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                total_options=i+1;
                if(i==0){
                    option3.setVisibility(View.INVISIBLE);
                    option4.setVisibility(View.INVISIBLE);
                }
                else if(i==1){
                    option3.setVisibility(View.INVISIBLE);
                    option4.setVisibility(View.INVISIBLE);
                }
                else if(i==2){
                    option3.setVisibility(View.VISIBLE);

                }
                else if(i==3){
                    option3.setVisibility(View.VISIBLE);
                    option4.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button cancel=view.findViewById(R.id.cancel);
        Button save=view.findViewById(R.id.save);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(total_options>1&&question.getText().toString().length()>4){

                    if(total_options==2){

                        String option_name1=editText1.getText().toString();
                        String option_name2=editText2.getText().toString();
                        if(option_name1.length()>0&&option_name2.length()>0){

                            if(option_value1>=0&&option_value2>=0){

                                update_question(id,question.getText().toString(),total_options+"",option_name1,option_value1+"", option_name2, option_value2+"","","","","");
                            }
                            else{

                                show_format_error("Please Choose Option Value","Please Enter Required Info..");
                            }


                        }
                        else {

                            show_format_error("Please Enter Option Name","Please Enter Required Info..");
                        }

                    }
                    else if(total_options==3){

                        String option_name1=editText1.getText().toString();
                        String option_name2=editText2.getText().toString();
                        String option_name3=editText3.getText().toString();
                        if(option_name1.length()>0&&option_name2.length()>0&&option_name3.length()>0){

                            if(option_value1>=0&&option_value2>=0&&option_value3>=0){

                                update_question(id,question.getText().toString(),total_options+"",option_name1,option_value1+"", option_name2, option_value2+"",option_name3, option_value3+"","","");
                            }
                            else{

                                show_format_error("Please Choose Option Value","Please Enter Required Info..");
                            }


                        }
                        else {

                            show_format_error("Please Enter Option Name","Please Enter Required Info..");
                        }

                    }
                    else if(total_options==4){

                        String option_name1=editText1.getText().toString();
                        String option_name2=editText2.getText().toString();
                        String option_name3=editText3.getText().toString();
                        String option_name4=editText4.getText().toString();
                        if(option_name1.length()>0&&option_name2.length()>0&&option_name3.length()>0&&option_name4.length()>0){

                            if(option_value1>=0&&option_value2>=0&&option_value3>=0&&option_value4>=0){

                                update_question(id,question.getText().toString(),total_options+"",option_name1,option_value1+"", option_name2, option_value2+"",option_name3, option_value3+"",option_name4, option_value4+"");
                            }
                            else{

                                show_format_error("Please Choose Option Value","Please Enter Required Info..");
                            }


                        }
                        else {

                            show_format_error("Please Enter Option Name","Please Enter Required Info..");
                        }
                    }
                }
                else{

                    if(question.getText().toString().length()<=4){

                        show_format_error("Question Length Must Be Greater Than 4","Please Enter Required Info..");
                    }
                    else{

                        show_format_error("Please Choose Number Of Options","Please Enter Required Info..");
                    }

                }


            }
        });
        alertDialog=builder.show();



    }

    public void upload_question(String question,String total_options,String option_name1,String option_value1,String option_name2,String option_value2,String option_name3,String option_value3,String option_name4,String option_value4){

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.teacher_evaluation_question_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            alertDialog.dismiss();
                            get_all_questions();


                        } else {
                            showFailMessage(response.toString());
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
                params.put("question", question);
                params.put("option_name1", option_name1);
                params.put("option_name2", option_name2);
                params.put("option_name3", option_name3);
                params.put("option_name4", option_name4);
                params.put("option_value1", option_value1);
                params.put("option_value2", option_value2);
                params.put("option_value3", option_value3);
                params.put("option_value4", option_value4);
                params.put("total_options", total_options);
                params.put("question_set_number", question_set_number+"");
                params.put("table", "Teacher_Evaluation_Question");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void update_question(String id,String question,String total_options,String option_name1,String option_value1,String option_name2,String option_value2,String option_name3,String option_value3,String option_name4,String option_value4){

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.update_question_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                        if (response.contains("success")) {

                            alertDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Successfully Updated", Toast.LENGTH_SHORT).show();
                            get_all_questions();


                        } else {
                            showFailMessage(response.toString());
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
                params.put("id", id);
                params.put("question", question);
                params.put("option_name1", option_name1);
                params.put("option_name2", option_name2);
                params.put("option_name3", option_name3);
                params.put("option_name4", option_name4);
                params.put("option_value1", option_value1);
                params.put("option_value2", option_value2);
                params.put("option_value3", option_value3);
                params.put("option_value4", option_value4);
                params.put("total_options", total_options);
                params.put("question_set_number", question_set_number+"");
                params.put("table", "Teacher_Evaluation_Question");
                params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    public void get_all_questions(){

        JSONArray postparams = new JSONArray();
        postparams.put("Teacher_Evaluation_Question");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());


        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_question_data_url,postparams,
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

    public void show_format_error(String message,String title){

        AlertDialog.Builder builder1=new AlertDialog.Builder(Teacher_Evaluation_Question.this);
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder1.show();
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
            view = inflter.inflate(R.layout.custom_dropdown_item, null);
            final TextView names = (TextView) view.findViewById(R.id.name);

            names.setText(info.get(i));
            return view;
        }
    }

    public void set_live(String id,Button btn,String value){


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant_URL.question_submit_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();



                            if (response.contains("success")) {

                                Toast.makeText(getApplicationContext(),"Submit Successfully", Toast.LENGTH_SHORT).show();
                                if(value.equalsIgnoreCase("0")){
                                    btn.setText("Submit");
                                }
                                else{

                                    btn.setText("Unsubmit");
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),"Fail To Submit", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Connect Error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id",id);
                    params.put("value",value);
                    params.put("column","live");
                    params.put("table", "Teacher_Evaluation_Question");
                    params.put("school_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
                    return params;
                }
            };

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }


    public void showFailMessage(String str){

        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Fail to Add Some Students");
        builder.setMessage(str +" Student ID's Already Exist.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();


            }
        });
        builder.show();
    }
}


