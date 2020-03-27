package com.example.onlinepathshala.Stuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinepathshala.Accountant.Accountant_Contact;
import com.example.onlinepathshala.Accountant.Notice_View;
import com.example.onlinepathshala.Accountant.Transaction2;
import com.example.onlinepathshala.Admin.Log_In;
import com.example.onlinepathshala.R;
import com.example.onlinepathshala.SharedPrefManager;
import com.example.onlinepathshala.Transaction_History_For_Authority;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Stuff_Dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    int[][] image_path={{R.drawable.male_profile,R.drawable.transaction},{R.drawable.transaction3,R.drawable.contact1},{R.drawable.notice2,R.drawable.contact3}};
    String[][] name={{"Profile","Transaction"},{"Transaction History","Contact"},{"Notice","Contact"}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff__dashboard);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecycleAdapter recycleAdapter=new RecycleAdapter(image_path,name);
        recyclerView.setAdapter(recycleAdapter);
    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewAdapter>{

        int[][] image_path;
        String[][] name;
        public RecycleAdapter(int[][] image_path, String[][] name){
            this.image_path=image_path;
            this.name=name;
        }
        public  class ViewAdapter extends RecyclerView.ViewHolder{

            View mView;
            CardView card1,card2;
            TextView text1,text2;
            ImageView image1,image2;

            public ViewAdapter(View itemView) {
                super(itemView);
                mView=itemView;
                card1=mView.findViewById(R.id.card1);
                card2=mView.findViewById(R.id.card2);
                text1=mView.findViewById(R.id.text1);
                text2=mView.findViewById(R.id.text2);
                image1=mView.findViewById(R.id.image1);
                image2=mView.findViewById(R.id.image2);



            }


        }
        @NonNull
        @Override
        public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_panel_recycle_item,parent,false);
            return new ViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewAdapter holder, final int position) {


            if(position==2) holder.card2.setVisibility(GONE);
            holder.card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(position==0){

                        startActivity(new Intent(getApplicationContext(),Stuff_profile.class));
                    }
                    else if(position==1){

                        startActivity(new Intent(getApplicationContext(), Transaction_History_For_Authority.class));
                    }
                    else if(position==2){

                        startActivity(new Intent(getApplicationContext(), Notice_View.class));
                    }


                }
            });
            holder.card2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(position==0){

                        startActivity(new Intent(getApplicationContext(), Transaction2.class));
                    }
                    else if(position==1){

                        startActivity(new Intent(getApplicationContext(), Accountant_Contact.class));
                    }
                    else if(position==2){


                    }

                }
            });


            holder.image1.setImageResource(image_path[position][0]);
            holder.image2.setImageResource(image_path[position][1]);

            holder.text1.setText(name[position][0]);
            holder.text1.setSelected(true);
            holder.text2.setText(name[position][1]);
            holder.text2.setSelected(true);



        }

        @Override
        public int getItemCount() {
            return name.length;
        }



    }









    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_main_alerts_menu_item:
                // TODO update alert menu icon
                Toast.makeText(this, "count cleared", Toast.LENGTH_SHORT).show();
            case R.id.logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                startActivity(new Intent(getApplicationContext(), Log_In.class));

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateAlertIcon() {
        // if alert count extends into two digits, just show the red circle
        if (0 < alertCount && alertCount < 10) {
            countTextView.setText(String.valueOf(alertCount));
        } else {

            countTextView.setText("4");
        }

        redCircle.setVisibility((alertCount >=0) ? VISIBLE : GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.moderator_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private FrameLayout redCircle;
    private TextView countTextView;
    private int alertCount = 0;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.activity_main_alerts_menu_item);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);
        countTextView.setText("4");
        return super.onPrepareOptionsMenu(menu);
    }
}
