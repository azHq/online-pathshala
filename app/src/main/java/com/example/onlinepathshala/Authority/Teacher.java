package com.example.onlinepathshala.Authority;

public class Teacher {

    public String id;
    public String name;
    public String class_name;
    public String subject_name;
    public String image_path;
    public String phone_number;
    public String email;
    public String device_id;
    public String rating;



    public Teacher(String id, String name, String class_name, String subject_name, String image_path) {
        this.id = id;
        this.name = name;
        this.class_name = class_name;
        this.subject_name = subject_name;
        this.image_path = image_path;
    }

    public Teacher(String id, String name, String class_name, String subject_name, String image_path, String phone_number, String email,String device_id,String rating ) {
        this.id = id;
        this.name = name;
        this.class_name = class_name;
        this.subject_name = subject_name;
        this.image_path = image_path;
        this.phone_number = phone_number;
        this.email = email;
        this.device_id=device_id;
        this.rating=rating;
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

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
