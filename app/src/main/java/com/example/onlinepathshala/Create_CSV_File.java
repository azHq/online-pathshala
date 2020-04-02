package com.example.onlinepathshala;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Create_CSV_File {

    Context context;
    public Create_CSV_File(Context context){
        this.context=context;
    }

    public String create_result_csv(String file_name,String file_path_sufix,ArrayList<String> data){

        File root=null;
        if(isSDCardPresent()){
            root=new File(Environment.getExternalStorageDirectory()+File.separator+"Eduate"+File.separator+"Result"+File.separator+file_path_sufix);
        }
        else{

            Toast.makeText(context,"Sorry,There is no SD Card",Toast.LENGTH_LONG).show();
            return null;
        }

        if(root!=null&&!root.exists()){

            root.mkdirs();

        }
        try {
            String path=root+File.separator+file_name;
            File file=new File(path);
            FileWriter fileWriter=new FileWriter(file);
           for(String reslut_row:data){

               fileWriter.write(reslut_row);
           }
           fileWriter.close();
           return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String create_attendance_csv(String file_name,String file_path_sufix,ArrayList<String> data){

        File root=null;
        if(isSDCardPresent()){
            root=new File(Environment.getExternalStorageDirectory()+File.separator+"Eduate"+File.separator+file_path_sufix);
        }
        else{

            Toast.makeText(context,"Sorry,There is no SD Card",Toast.LENGTH_LONG).show();
            return null;
        }

        if(root!=null&&!root.exists()){

            root.mkdirs();

        }
        try {
            String path=root+File.separator+file_name;
            File file=new File(path);
            FileWriter fileWriter=new FileWriter(file);
            for(String reslut_row:data){

                fileWriter.write(reslut_row);
            }
            fileWriter.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
