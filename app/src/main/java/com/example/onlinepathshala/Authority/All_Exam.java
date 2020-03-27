package com.example.onlinepathshala.Authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.onlinepathshala.Admin.Member;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.Create_Exam;
import com.example.onlinepathshala.Exam;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Teacher.Attendence;
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

public class All_Exam extends AppCompatActivity implements View.OnTouchListener {

    FloatingActionButton dragView;
    float dX;
    float dY;
    int lastAction;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    TextView no_item;
    String medium,shift_name;
    AlertDialog alertDialog2;
    String class_id,activity_type,class_name,section_id,section_name;
    AlertDialog alertDialog3;
    String teacher_name,teacher_id;
    ArrayList<Exam_Infos> exams=new ArrayList<>();
    ArrayList<String> all_exam_name=new ArrayList<>();
    float posX=0;
    float posY=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__exam);

        class_id=getIntent().getStringExtra("class_id");
        class_name=getIntent().getStringExtra("class_name");
        section_id=getIntent().getStringExtra("section_id");
        section_name=getIntent().getStringExtra("section_name");
        activity_type=getIntent().getStringExtra("type");
        dragView = findViewById(R.id.fab);
        frameLayout=findViewById(R.id.frame_layout);
        recyclerView=findViewById(R.id.recycle);
        no_item=findViewById(R.id.no_item);
        dragView.setOnTouchListener(this);
        progressDialog=new ProgressDialog(All_Exam.this);
        progressDialog.setMessage("Please wait...");
        getAllMemberData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllMemberData();
    }


    public void getAllMemberData(){


        JSONArray postparams = new JSONArray();
        postparams.put("Exam");
        postparams.put(SharedPrefManager.getInstance(getApplicationContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);


        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_exam_info_in_a_section,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        exams.clear();
                        all_exam_name.clear();
                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {
                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String exam_name = member.getString("exam_name");
                                        String exam_type=member.getString("exam_type");
                                        String subject_id = member.getString("subject_id");
                                        String subject_name = member.getString("subject_name");
                                        String date = member.getString("create_date");
                                        String time = member.getString("create_time");
                                        String result = member.getString("result_status");
                                        String marks = member.getString("marks");
                                        String topic = member.getString("topic");
                                        exams.add(new Exam_Infos(id,exam_name,exam_type,subject_id,subject_name,date,time,result,marks,topic));
                                        all_exam_name.add(exam_name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                RecycleAdapter recycleAdapter=new RecycleAdapter(exams);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(recycleAdapter);
                                progressDialog.dismiss();
                                no_item.setVisibility(View.GONE);
                            }
                            else{
                                no_item.setVisibility(View.VISIBLE);
                                RecycleAdapter recycleAdapter=new RecycleAdapter(exams);
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


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter> {

        ArrayList<Exam_Infos> classinfos;
        public RecycleAdapter(ArrayList<Exam_Infos> classinfos){
            this.classinfos=classinfos;
        }



        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            View mView;
            LinearLayout linearLayout;
            TextView exam_name,exam_type,subject_name,date,time,mark,topic;

            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                exam_name=mView.findViewById(R.id.exam_name);
                exam_type=mView.findViewById(R.id.exam_type);
                subject_name=mView.findViewById(R.id.subject_name);
                date=mView.findViewById(R.id.date);
                mark=mView.findViewById(R.id.marks);
                topic=mView.findViewById(R.id.topic_name);
                linearLayout=mView.findViewById(R.id.view_profile);



            }
            @Override
            public void onClick(View view) {


            }

            @Override
            public boolean onLongClick(View view) {


                int position =getLayoutPosition();
                Exam_Infos memberInfo=classinfos.get(position);
                showOptionDialog(memberInfo.exam_id,memberInfo.exam_name,memberInfo.marks,memberInfo.exam_type,memberInfo.subject_id,memberInfo.subject_name);
                return true;
            }

        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_rec_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Exam_Infos memberInfo=classinfos.get(position);

            holder.exam_name.setText(memberInfo.exam_name);
            holder.exam_type.setText(memberInfo.exam_type);
            holder.subject_name.setText(memberInfo.subject_name);
            holder.date.setText(memberInfo.date+" "+memberInfo.time);
            holder.mark.setText(memberInfo.marks);
            if(memberInfo.topic!=null&&!memberInfo.topic.equalsIgnoreCase("NULL")) holder.topic.setText("Topic: "+memberInfo.topic);
            holder.exam_name.setSelected(true);
            holder.exam_type.setSelected(true);
            holder.subject_name.setSelected(true);
            holder.date.setSelected(true);
            holder.mark.setSelected(true);



        }

        @Override
        public int getItemCount() {
            return classinfos.size();
        }



    }

    public void showOptionDialog(final String id,final String exam_name,final String marks,final String exam_type,final String subject_id,final String subject_name){

        AlertDialog.Builder builder=new AlertDialog.Builder(All_Exam.this);
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        alertDialog2=builder.show();
        assign_teacher.setVisibility(View.GONE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                Intent tnt=new Intent(getApplicationContext(),Edit_Exam.class);
                tnt.putExtra("class_id",class_id);
                tnt.putExtra("section_id",section_id);
                tnt.putExtra("exam_id",id);
                tnt.putExtra("exam_name",exam_name);
                tnt.putExtra("marks",marks);
                tnt.putExtra("exam_type",exam_type);
                tnt.putExtra("subject_id",subject_id);
                tnt.putExtra("subject_name",subject_name);
                tnt.putStringArrayListExtra("exam_names",all_exam_name);
                startActivity(tnt);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showDeleteDialogBox(id);
            }
        });


    }


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(All_Exam.this);
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

                            Toast.makeText(getApplicationContext(),"Class Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData();

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
                params.put("table", "Exam");
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
                if (lastAction == MotionEvent.ACTION_DOWN){
                    Intent tnt=new Intent(getApplicationContext(), Create_Exam.class);
                    tnt.putExtra("class_id",class_id);
                    tnt.putExtra("section_id",section_id);
                    tnt.putStringArrayListExtra("exam_names",all_exam_name);
                    startActivity(tnt);
                }
                break;

            default:
                return false;
        }
        return true;
    }




    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flag;
        ArrayList<Member> info;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext,int flag,ArrayList<Member> info) {
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
            CircleImageView circleImageView=view.findViewById(R.id.image);
            TextView names = (TextView) view.findViewById(R.id.teacher_name);
            Member member=(Member) info.get(i);
            if(!member.profile_image.equalsIgnoreCase("null")){

                Picasso.get().load(member.profile_image).placeholder(R.drawable.profile10).into(circleImageView);
            }
            names.setText(member.user_name);
            return view;
        }
    }
}
