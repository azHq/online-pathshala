package com.example.onlinepathshala.Authority;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinepathshala.Constant_URL;
import com.example.onlinepathshala.DownloadTask;
import com.example.onlinepathshala.Exam;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Student.Routine;
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


public class Class_Routine_For_Auth extends Fragment implements View.OnTouchListener {


    RecyclerView recyclerView;
    ArrayList<Routine> notice_infos=new ArrayList<>();
    ProgressDialog progressDialog;
    RecycleAdapter recycleAdapter;
    FloatingActionButton dragView;
    ArrayList<Object> classes=new ArrayList<>();
    ArrayList<Object> sections=new ArrayList<>();
    ArrayList<Object> mediums=new ArrayList<>();
    AlertDialog alertDialog2,alertDialog;
    float posX=0;
    float posY=0;

    float dX;
    float dY;
    int lastAction;
    Spinner sp_medium,sp_class,sp_section;
    String medium="Bangla",class_id="",section_id="";
    TextView textView;
    String user_type="";
    public Class_Routine_For_Auth( String user_type){

        this.user_type=user_type;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_class__routine_for_auth, container, false);
        progressDialog=new ProgressDialog(getContext());
        recyclerView=view.findViewById(R.id.recycle);
        recycleAdapter=new RecycleAdapter(notice_infos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recycleAdapter);
        dragView = view.findViewById(R.id.fab);
        dragView.setOnTouchListener(this);
        if(user_type.equalsIgnoreCase("teacher")) dragView.hide();
        sp_medium=view.findViewById(R.id.medium);
        sp_class=view.findViewById(R.id.class_name);
        sp_section=view.findViewById(R.id.section);
        textView=view.findViewById(R.id.no_item);


        mediums.add("Bangla");
        mediums.add("English");
        sp_medium.setAdapter(new CustomAdapter(getContext(),1,mediums));
        sp_medium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                medium=(String)mediums.get(i);
                getAllData("Class_Table", Constant_URL.get_all_class_info_url,class_id,section_id,medium);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                class_id=((Classes)classes.get(i)).id;
                getAllData("Section", Constant_URL.get_all_section_info_url,class_id,section_id,medium);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                section_id=((Section)sections.get(i)).id;
                getAllMemberData(class_id,section_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllData("Class_Table", Constant_URL.get_all_class_info_url,class_id,section_id,medium);
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
            view = inflter.inflate(R.layout.drop_down_item, null);
            final TextView names = (TextView) view.findViewById(R.id.name);


            if(flag==1){

                String s=(String)info.get(i);
                names.setText(s);
            }
            else if(flag==2){
                Classes classes=(Classes) info.get(i);
                names.setText(classes.class_name);

            }
            else if(flag==3){

                Section classes=(Section) info.get(i);
                names.setText(classes.section_name);
            }


            return view;
        }
    }




    public void getAllData(final String table_name,String url,String class_id,String section_id,String medium){


        JSONArray postparams = new JSONArray();
        postparams.put(table_name);
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());

        if(table_name.equalsIgnoreCase("Class_Table")){
            postparams.put(medium);
        }
        else {

            postparams.put(class_id);
        }


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        String string;
                        try {
                            string = response.getString(0);






                                if (table_name.equalsIgnoreCase("Class_Table")) {

                                        classes.clear();

                                    if (!string.contains("no item")) {
                                        for (int i = 0; i < response.length(); i++) {

                                            JSONObject member = null;
                                            try {
                                                member = response.getJSONObject(i);

                                                String id = member.getString("class_id");
                                                String name = member.getString("class_name");
                                                classes.add(new Classes(id, name, "", "", ""));
                                            } catch (JSONException e) {

                                                e.printStackTrace();
                                            }
                                        }
                                        sp_class.setAdapter(new CustomAdapter(getContext(), 2, classes));
                                    }
                                    else {

                                        classes.add(new Classes("0","No Class","","",""));
                                        sp_class.setAdapter(new CustomAdapter(getContext(),2,classes));
                                    }
                                }
                                else if (table_name.equalsIgnoreCase("Section")) {

                                    sections.clear();
                                    if (!string.contains("no item")) {
                                        for (int i = 0; i < response.length(); i++) {

                                            JSONObject member = null;
                                            try {
                                                member = response.getJSONObject(i);

                                                String id = member.getString("id");
                                                String name = member.getString("section_name");
                                                sections.add(new Section(id, "", name, "",""));
                                            } catch (JSONException e) {

                                                e.printStackTrace();
                                            }
                                        }
                                        sp_section.setAdapter(new CustomAdapter(getContext(), 3, sections));
                                    }
                                    else {



                                        sections.add(new Section("0","","No Sections","",""));
                                        sp_section.setAdapter(new CustomAdapter(getContext(),3,sections));

                                    }
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }

    public void showOptionDialog(final String id,final String section_name,final String shift){

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.options_dialog,null);
        builder.setView(view);
        TextView assign_teacher=view.findViewById(R.id.assign_teacher);
        TextView edit=view.findViewById(R.id.edit);
        TextView delete=view.findViewById(R.id.delete);
        assign_teacher.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        alertDialog2=builder.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.dismiss();
                showDeleteDialogBox(id);
            }
        });


    }


    public void showDeleteDialogBox(final String id){

        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
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

                            Toast.makeText(getContext(),"Routine Deleted Successfully", Toast.LENGTH_SHORT).show();
                            getAllMemberData(class_id,section_id);

                        } else {
                            Toast.makeText(getContext(),"Fail To Delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Connect Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("table", "Routine");
                params.put("school_id", SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }



    public void getAllMemberData(String class_id,String section_id){


        JSONArray postparams = new JSONArray();
        postparams.put("Routine");
        postparams.put(SharedPrefManager.getInstance(getContext()).getUser().getSchool_id());
        postparams.put(class_id);
        postparams.put(section_id);
        postparams.put("Class Routine");

        progressDialog.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST,
                Constant_URL.get_all_routine_url,postparams,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        notice_infos.clear();
                        String string;
                        try {
                            string=response.getString(0);

                            if(!string.contains("no item")){

                                for(int i=0;i<response.length();i++){

                                    JSONObject member = null;
                                    try {

                                        member = response.getJSONObject(i);
                                        String id = member.getString("id");
                                        String routine_type=member.getString("routine_type");
                                        String exam_type=member.getString("exam_type");
                                        String date = member.getString("create_date");
                                        String file_path = member.getString("file_path");
                                        String file_type = member.getString("file_type");
                                        notice_infos.add(new Routine(id,routine_type,exam_type,date,file_path,file_type));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                textView.setVisibility(View.GONE);
                                recycleAdapter.notifyDataSetChanged();


                                progressDialog.dismiss();
                            }
                            else{
                                textView.setVisibility(View.VISIBLE);
                                recycleAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"No Routine Uploaded",Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getContext(),"Connection Error",Toast.LENGTH_LONG).show();
                        System.out.println(error.toString());
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

        ArrayList<Routine> notice_infos;
        public RecycleAdapter(ArrayList<Routine> classinfos){
            this.notice_infos=classinfos;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            View mView;
            LinearLayout linearLayout;
            TextView assigned_date,title,type,file_path;
            ImageView imageView;
            Button btn_download;

            public ViewAdapter(View itemView) {
                super(itemView);
                itemView.setOnLongClickListener(this);
                mView=itemView;
                title=mView.findViewById(R.id.title);
                assigned_date=mView.findViewById(R.id.date);
                type=mView.findViewById(R.id.type);
                type.setMovementMethod(new ScrollingMovementMethod());
                imageView=mView.findViewById(R.id.image);
                btn_download=mView.findViewById(R.id.download);
                title.setSelected(true);
            }

            @Override
            public boolean onLongClick(View view) {


                int position =getLayoutPosition();
                Routine memberInfo=notice_infos.get(position);
                showOptionDialog(memberInfo.id,"","");
                return true;
            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_class_routine_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Routine memberInfo=notice_infos.get(position);
            holder.assigned_date.setText("Uploaded Date: "+memberInfo.date);
            if(memberInfo.path!=null&&memberInfo.file_type.equalsIgnoreCase("image")) {
                Picasso.get().load(memberInfo.path).placeholder(R.drawable.routine).into(holder.imageView);
                holder.btn_download.setText("Download Routine");
            }
            else holder.imageView.setVisibility(View.GONE);
            holder.title.setText("Class Routine");
            holder.type.setVisibility(View.GONE);
            holder.btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new DownloadTask(getContext(), memberInfo.path);

                }
            });



        }

        @Override
        public int getItemCount() {
            return notice_infos.size();
        }



    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {




        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
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
                    Intent tnt=new Intent(getContext(), Upload_Routine.class);
                    tnt.putExtra("type","Class Routine");
                    startActivity(tnt);
                }
                break;

            default:
                return false;
        }
        return true;
    }


}
