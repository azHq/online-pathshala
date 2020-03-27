package com.example.onlinepathshala.Admin;

public class School {

    String school_id;
    String school_name;
    String authority_id;
    String authority_name;
    String email;
    String phone_number;
    String joining_date;
    String account_status;
    String image;
    String device_id;


    public School(String school_id, String school_name, String authority_id, String authority_name, String email, String phone_number, String joining_date, String account_status,String image, String device_id) {
        this.school_id = school_id;
        this.school_name = school_name;
        this.authority_id = authority_id;
        this.authority_name = authority_name;
        this.email = email;
        this.phone_number = phone_number;
        this.joining_date = joining_date;
        this.account_status = account_status;
        this.image=image;
        this.device_id=device_id;
    }

    public School(String school_id, String school_name) {
        this.school_id = school_id;
        this.school_name = school_name;
    }
}
