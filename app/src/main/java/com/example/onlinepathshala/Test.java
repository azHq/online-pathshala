package com.example.onlinepathshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

        if(isStoragePermissionGranted()) createPdf("hekllo" );


    }
    private void createPdf(String sometext){

        File root =new File(Environment.getExternalStorageDirectory()+File.separator+"Eduate");


        if(!root.exists()){

           boolean filecreated= root.mkdirs();
            Toast.makeText(getApplicationContext(),filecreated+"Directory successfully",Toast.LENGTH_LONG).show();
        }
        try {
            String path=root+File.separator+"azaz2.csv";
            File file=new File(path);
            FileWriter fileWriter=new FileWriter(file);
            String header="a,b,c,d";
            fileWriter.write(header);

            for(int i=0;i<10;i++){

                fileWriter.write(i+","+(i+1)+","+(i+2)+","+(i+3));
            }
            Toast.makeText(getApplicationContext(),"File Writen successfully",Toast.LENGTH_LONG).show();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_LONG).show();
                return false;
            } else {

                Toast.makeText(getApplicationContext(),"Permission Invoke",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_LONG).show();
            return false;
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_LONG).show();
            //resume tasks needing this permission
        }
    }


}
