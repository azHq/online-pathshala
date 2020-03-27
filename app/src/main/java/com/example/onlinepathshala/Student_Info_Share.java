package com.example.onlinepathshala;

public class Student_Info_Share {

    public String id;
    public String name;
    public String image_path;
    public String email;
    public String phone;
    public String class_id;
    public String section_id;

    public Student_Info_Share(String id, String name ,String email,String image_path, String phone, String class_id, String section_id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.class_id = class_id;
        this.section_id = section_id;
        this.image_path=image_path;
    }
}
