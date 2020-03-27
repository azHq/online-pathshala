package com.example.onlinepathshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.onlinepathshala.Student.Attendence_Info_Class2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Test extends AppCompatActivity {

    Spinner sp_month,sp_year;
    List<String> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }

    public void create_table(View view){

        MySQLiteDatabase mySQLiteDatabase=MySQLiteDatabase.getMySQLiteDatabase(getApplicationContext());
        mySQLiteDatabase.create_memeber_table();
    }
    public void drop_table(View view){

        MySQLiteDatabase mySQLiteDatabase=MySQLiteDatabase.getMySQLiteDatabase(getApplicationContext());
        mySQLiteDatabase.drop_table("Member");
    }
    public void insert(View view){

        MySQLiteDatabase mySQLiteDatabase=MySQLiteDatabase.getMySQLiteDatabase(getApplicationContext());
        long result=mySQLiteDatabase.insert_member_table_data("123","12345","Arambagh High School","Admin","12345","log_in");
        if(result>0){

            Toast.makeText(getApplicationContext(),"Inserted Successfully",Toast.LENGTH_LONG).show();
        }
        else{

            Toast.makeText(getApplicationContext(),"Inserted Fail",Toast.LENGTH_LONG).show();
        }
    }
    public void update(View view){

        MySQLiteDatabase mySQLiteDatabase=MySQLiteDatabase.getMySQLiteDatabase(getApplicationContext());
        mySQLiteDatabase.delete("Member");

    }


    public void check_num_raws(View view){

        MySQLiteDatabase mySQLiteDatabase=MySQLiteDatabase.getMySQLiteDatabase(getApplicationContext());
        int result=mySQLiteDatabase.numberOfRows("Member");
        Toast.makeText(getApplicationContext(),"Num Raws: "+result,Toast.LENGTH_LONG).show();
    }
    public void get_all_data(View view){

        MySQLiteDatabase mySQLiteDatabase=MySQLiteDatabase.getMySQLiteDatabase(getApplicationContext());
        Cursor result=mySQLiteDatabase.getAllRow("Member");
        result.moveToFirst();
        while (!result.isAfterLast()){

            System.out.println(result.getString(0));
            System.out.println(result.getString(1));
            System.out.println(result.getString(2));
            System.out.println(result.getString(3));
            System.out.println(result.getString(4));
            System.out.println(result.getString(5));

            result.moveToNext();

        }
    }


}
