package com.example.onlinepathshala.Student;

public class Routine {

    public String id;
    public String path;
    public String routine_type;
    public String exam_type;
    public String file_type;
    public String date;

    public Routine(String id,String routine_type, String exam_type,String date, String path,String file_type) {
        this.id = id;
        this.path = path;
        this.routine_type = routine_type;
        this.exam_type = exam_type;
        this.file_type=file_type;
        this.date=date;
    }
}
