package com.example.onlinepathshala.Authority;

public class Student {

    public String id;
    public String name;
    public String class_name;
    public String image_path;
    public String roll;
    public String device_id;



    public Student(String id, String name, String class_name, String image_path, String roll, String device_id) {
        this.id = id;
        this.name = name;
        this.class_name = class_name;
        this.image_path = image_path;
        this.roll = roll;
        this.device_id = device_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
