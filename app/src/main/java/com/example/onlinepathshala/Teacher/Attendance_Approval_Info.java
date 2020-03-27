package com.example.onlinepathshala.Teacher;

public class Attendance_Approval_Info {

    public String id;
    public String class_id;
    public String section_id;
    public String teacher_id;
    public String teacher_name;
    public String attendance_date;

    public Attendance_Approval_Info(String id, String class_id, String section_id, String teacher_id, String teacher_name, String attendance_date) {
        this.id = id;
        this.class_id = class_id;
        this.section_id = section_id;
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
        this.attendance_date = attendance_date;
    }
}
