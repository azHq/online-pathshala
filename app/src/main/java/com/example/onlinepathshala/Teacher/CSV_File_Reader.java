package com.example.onlinepathshala.Teacher;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSV_File_Reader extends File_Reader {


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

                if(count==0){
                    count++;
                    str=line.split(",");
                    boolean error=false;
                    String s="";
                    if(str.length>=11){

                        if(!str[0].contains(Result_Header_Constant.STUDENT_ID)){

                            s+="1,";
                            error=true;
                        }
                        if(!str[1].contains(Result_Header_Constant.STUDENT_NAME)){

                            s+="2,";
                            error=true;
                        }
                        if(!str[2].contains(Result_Header_Constant.TOTAL_MARKS)){

                            s+="3,";
                            error=true;
                        }
                        if(!str[3].contains(Result_Header_Constant.SUBJECTIVE_TOTAL)){

                            s+="4,";
                            error=true;
                        }
                        if(!str[4].contains(Result_Header_Constant.OBJECTIVE_TOTAL)){

                            s+="5,";
                            error=true;
                        }
                        if(!str[5].contains(Result_Header_Constant.PRACTICAL_TOTAL)){

                            s+="6,";
                            error=true;
                        }
                        if(!str[6].contains(Result_Header_Constant.OBTAIN_MARKS_SUBJECTIVE)){

                            s+="7,";
                            error=true;
                        }
                        if(!str[7].contains(Result_Header_Constant.OBTAIN_MARKS_OBJECTIVE)){

                            s+="8,";
                            error=true;
                        }
                        if(!str[8].contains(Result_Header_Constant.OBTAIN_MARKS_PRACTICAL)){

                            s+="9,";
                            error=true;
                        }
                        if(!str[9].contains(Result_Header_Constant.WEIGHT)){

                            s+="10";
                            error=true;
                        }
                    }


                   if(error){
                       String[] message={"error",s};
                       marks.add(message);
                       return  marks;
                   }

                }
                else{

                    str=line.split(",");

                    if(str.length>=10){
                        marks.add(str);
                    }
                    else{

                        String[] message={"error"," format error"};
                        marks.add(message);
                        return  marks;
                    }

                }

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
