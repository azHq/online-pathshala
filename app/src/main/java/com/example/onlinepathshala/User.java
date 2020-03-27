package com.example.onlinepathshala;

public class User {
    String user_id;
    String password;
    String school_name;
    String user_type;
    String school_id;
    String device_id;




    public User(String user_id, String password, String school_name, String user_type, String school_id, String device_id) {
        this.user_id = user_id;
        this.password = password;
        this.school_name = school_name;
        this.user_type = user_type;
        this.school_id = school_id;
        this.device_id = device_id;
    }

    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.user_id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
