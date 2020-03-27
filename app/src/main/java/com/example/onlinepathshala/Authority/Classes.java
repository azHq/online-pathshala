package com.example.onlinepathshala.Authority;

public class Classes {

    public String id;
    public String class_teacher;
    public String total_students;
    public String total_subjects;
    public String class_name;
    public String medium;

    public Classes(String id,String class_name, String class_teacher, String total_students, String total_subjects) {
        this.id = id;
        this.class_teacher = class_teacher;
        this.total_students = total_students;
        this.total_subjects = total_subjects;
        this.class_name=class_name;
    }

    public Classes(String id,String class_name, String class_teacher, String total_students, String total_subjects, String medium) {
        this.id = id;
        this.class_teacher = class_teacher;
        this.total_students = total_students;
        this.total_subjects = total_subjects;
        this.class_name = class_name;
        this.medium = medium;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClass_teacher() {
        return class_teacher;
    }

    public void setClass_teacher(String class_teacher) {
        this.class_teacher = class_teacher;
    }

    public String getTotal_students() {
        return total_students;
    }

    public void setTotal_students(String total_students) {
        this.total_students = total_students;
    }

    public String getTotal_subjects() {
        return total_subjects;
    }

    public void setTotal_subjects(String total_subjects) {
        this.total_subjects = total_subjects;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
