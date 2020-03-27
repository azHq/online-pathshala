package com.example.onlinepathshala.Admin;

public class Member {

    public String id;
    public String user_name;
    public String school_name;
    public String profile_image;
    public String school_id;

    public Member(String id, String user_name, String school_name, String profile_image, String school_id) {
        this.id = id;
        this.user_name = user_name;
        this.school_name = school_name;
        this.profile_image = profile_image;
        this.school_id = school_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }
}
