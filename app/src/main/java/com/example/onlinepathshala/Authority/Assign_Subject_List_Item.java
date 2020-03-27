package com.example.onlinepathshala.Authority;

public class Assign_Subject_List_Item {

    public String id;
    public String class_name;
    public String class_id;
    public String teacher_name;
    public String teacher_id;
    public String subject_id;
    public String subject_name;
    public String section_id;
    public String section_name;
    public String start_time;
    public String end_time;
    public String day;


    public Assign_Subject_List_Item(String id, String class_id,String class_name, String teacher_id,String teacher_name,String subject_id,String subject_name,String section_id, String section_name,String start_time,String end_time, String day) {
        this.id = id;
        this.class_name = class_name;
        this.class_id=class_id;
        this.teacher_name = teacher_name;
        this.subject_id=subject_id;
        this.teacher_id=teacher_id;
        this.section_id=section_id;
        this.subject_name = subject_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }
}
