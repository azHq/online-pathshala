package com.example.onlinepathshala.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onlinepathshala.R;

import java.util.ArrayList;

public class Attendence_History extends AppCompatActivity {

    RecyclerView recyclerView0,recyclerView1;
    ArrayList<Attendence_Info_Class> attendence_info=new ArrayList<>();
    RecycleAdapter recycleAdapter;
    boolean scroll0=true,scroll1=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence__history);

        recyclerView0=findViewById(R.id.recycle);
        recyclerView1=findViewById(R.id.recycle1);
        getAllData();

        recyclerView0.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(scroll0) recyclerView1.scrollBy(dx, dy);
            }
        });
        recyclerView0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scroll0=true;
                scroll1=false;

                return false;
            }
        });
        recyclerView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scroll1=true;
                scroll0=false;
                return false;
            }
        });


        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(scroll1) recyclerView0.scrollBy(dx, dy);
            }
        });

        recycleAdapter=new RecycleAdapter(attendence_info,0);
        recyclerView0.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView0.setAdapter(recycleAdapter);
        recycleAdapter=new RecycleAdapter(attendence_info,1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(recycleAdapter);
        recyclerView1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
    }

    public void getAllData(){

        for(int i=0;i<12;i++){

            attendence_info.add(new Attendence_Info_Class("January","A","A","P","A","P","A","A","P","A","P","A","A","P","A","P","A","A","P","A","P","A","A","P","A","P","A","A","P","A","P","A","86","16","70"));
        }
    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        ArrayList<Attendence_Info_Class> attendence_info;
        public int flag;
        public RecycleAdapter(ArrayList<Attendence_Info_Class> attendence_info,int flag){
            this.attendence_info=attendence_info;
            this.flag=flag;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView month;
            TextView day1,day2,day3,day4,day5,day6,day7,day8,day9,day10,day11,day12,day13,day14;
            TextView day15,day16,day17,day18,day19,day20,day21,day22,day23,day24,day25,day26,day27,day28,day29,day30,day31,total_working_day,absent,present;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;


                if(flag==1){

                    month=mView.findViewById(R.id.month_name);
                }
                else{
                    day1=mView.findViewById(R.id.day1);
                    day2=mView.findViewById(R.id.day2);
                    day3=mView.findViewById(R.id.day3);
                    day4=mView.findViewById(R.id.day4);
                    day5=mView.findViewById(R.id.day5);
                    day6=mView.findViewById(R.id.day6);
                    day7=mView.findViewById(R.id.day7);
                    day8=mView.findViewById(R.id.day8);
                    day9=mView.findViewById(R.id.day9);
                    day10=mView.findViewById(R.id.day10);
                    day11=mView.findViewById(R.id.day11);
                    day12=mView.findViewById(R.id.day12);
                    day13=mView.findViewById(R.id.day13);
                    day14=mView.findViewById(R.id.day14);
                    day15=mView.findViewById(R.id.day15);
                    day16=mView.findViewById(R.id.day16);
                    day17=mView.findViewById(R.id.day17);
                    day18=mView.findViewById(R.id.day18);
                    day19=mView.findViewById(R.id.day19);
                    day20=mView.findViewById(R.id.day20);
                    day21=mView.findViewById(R.id.day21);
                    day22=mView.findViewById(R.id.day22);
                    day23=mView.findViewById(R.id.day23);
                    day24=mView.findViewById(R.id.day24);
                    day25=mView.findViewById(R.id.day25);
                    day26=mView.findViewById(R.id.day26);
                    day27=mView.findViewById(R.id.day27);
                    day28=mView.findViewById(R.id.day28);
                    day29=mView.findViewById(R.id.day29);
                    day30=mView.findViewById(R.id.day30);
                    day31=mView.findViewById(R.id.day31);
                    present=mView.findViewById(R.id.total_present);
                    absent=mView.findViewById(R.id.total_absent);
                    total_working_day=mView.findViewById(R.id.total_working_day);

                }

            }



        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=null;
            if(flag==1){

                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.month_recycle_item,parent,false);
            }
            else{

                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendence_history_list_item,parent,false);
            }
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            Attendence_Info_Class attendence=attendence_info.get(position);

            if(flag==1){

                holder.month.setText(attendence.month);

            }
            else{
                holder.day1.setText(attendence.day1+"");
                holder.day2.setText(attendence.day2);
                holder.day3.setText(attendence.day3);
                holder.day4.setText(attendence.day4);
                holder.day5.setText(attendence.day5);
                holder.day6.setText(attendence.day6);
                holder.day7.setText(attendence.day7);
                holder.day8.setText(attendence.day8);
                holder.day9.setText(attendence.day9);
                holder.day10.setText(attendence.day10);
                holder.day11.setText(attendence.day11);
                holder.day12.setText(attendence.day12);
                holder.day13.setText(attendence.day13);
                holder.day14.setText(attendence.day14);
                holder.day15.setText(attendence.day15);
                holder.day16.setText(attendence.day16);
                holder.day17.setText(attendence.day17);
                holder.day18.setText(attendence.day18);
                holder.day19.setText(attendence.day19);
                holder.day20.setText(attendence.day20);
                holder.day21.setText(attendence.day21);
                holder.day22.setText(attendence.day22);
                holder.day23.setText(attendence.day23);
                holder.day24.setText(attendence.day24);
                holder.day25.setText(attendence.day25);
                holder.day26.setText(attendence.day26);
                holder.day27.setText(attendence.day27);
                holder.day28.setText(attendence.day28);
                holder.day29.setText(attendence.day29);
                holder.day30.setText(attendence.day30);
                holder.day31.setText(attendence.day31);
                holder.present.setText(attendence.present);
                holder.absent.setText(attendence.absent);
                holder.total_working_day.setText(attendence.total_working_day);
            }




        }

        @Override
        public int getItemCount() {
            return attendence_info.size();
        }



    }
}
