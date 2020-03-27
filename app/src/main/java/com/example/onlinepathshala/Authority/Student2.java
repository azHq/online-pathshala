package com.example.onlinepathshala.Authority;

public class Student2 {

    public String id;
    public String name;
    public String email;
    public String phone_number;
    public String image_path;
    public String device_id;
    public String class_id;
    public String class_name;
    public String section_id;
    public String section_name;
    public String roll;


    public Student2(String id, String name, String email, String phone_number, String image_path, String device_id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.image_path = image_path;
        this.device_id = device_id;
    }


    public Student2(String id, String name, String email, String phone_number, String image_path, String device_id, String class_id, String class_name, String section_id, String section_name,String roll) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.image_path = image_path;
        this.device_id = device_id;
        this.class_id = class_id;
        this.class_name = class_name;
        this.section_id = section_id;
        this.section_name = section_name;
        this.roll=roll;
    }
}
