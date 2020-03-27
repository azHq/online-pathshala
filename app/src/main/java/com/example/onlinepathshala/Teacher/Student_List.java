package com.example.onlinepathshala.Teacher;

public class Student_List {

    public String id;
    public String student_name;
    public String roll;
    public String image_path;
    public String cause="";


    public Student_List(String id, String student_name, String roll, String image_path) {
        this.id = id;
        this.student_name = student_name;
        this.roll = roll;
        this.image_path = image_path;
    }
    public Student_List(String id, String student_name, String roll, String image_path,String cause) {
        this.id = id;
        this.student_name = student_name;
        this.roll = roll;
        this.image_path = image_path;
        this.cause=cause;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
