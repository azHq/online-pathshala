package com.example.onlinepathshala.Authority;

import android.content.Context;

import com.example.onlinepathshala.Teacher.File_Reader;
import com.example.onlinepathshala.Teacher.Result_Header_Constant;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSV_File_Reader2 extends File_Reader {


    @Override
    public ArrayList<String[]> readfile(InputStream path, Context context) {

        ArrayList<String[]> marks=new ArrayList<>();
        try {
            InputStreamReader fileReader=new InputStreamReader(path);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            String line="";
            int count=0;
            while ((line=bufferedReader.readLine())!=null){
                String[] str;
                line=line.trim();
                line=line.toLowerCase();

                str=line.split(",");
                marks.add(str);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return marks;
    }
}
