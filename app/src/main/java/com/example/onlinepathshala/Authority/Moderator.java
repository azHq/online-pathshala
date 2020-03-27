package com.example.onlinepathshala.Authority;

public class Moderator {

    public String id;
    public String name;
    public String email;
    public String phone_number;
    public String image_path;
    public String device_id;
    public String moderator_type;

    public Moderator(String id, String name, String email, String phone_number, String image_path, String device_id,String moderator_type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.image_path = image_path;
        this.device_id = device_id;
        this.moderator_type=moderator_type;
    }
}
