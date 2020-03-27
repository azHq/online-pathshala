package com.example.onlinepathshala.Student;

public class Attendence_Info_Class2 {
    public int year;
    public int month;
    public String[][] status=new String[31][2];


    public Attendence_Info_Class2(int year,int month, String[][] status) {
        this.month = month;
        this.status = status;
        this.year=year;
    }
}
