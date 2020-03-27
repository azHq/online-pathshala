package com.example.onlinepathshala.Student;

public class Student_Additional_Info {

    public String id;
    public String name;
    public String image_path;
    public String email;
    public String class_roll;
    public String gender;
    public String birth_date;
    public String blood_group;
    public String religion;
    public String father_name;
    public String mother_name;
    public String father_contact;
    public String mother_contact;
    public String father_occupation;
    public String mother_occupation;
    public String alt_email;
    public String emergency_contact;
    public String present_address;
    public String permanent_address;
    public String class_name;
    public String section_name;
    public String phone_number;

    public Student_Additional_Info(String id, String name,String class_name,String section_name,String phone_number, String image_path, String email, String class_roll, String gender, String birth_date, String blood_group, String religion, String father_name, String mother_name, String father_contact, String mother_contact, String father_occupation, String mother_occupation, String alt_email, String emergency_contact, String present_address, String permanent_address) {
        this.id = id;
        this.name = name;
        this.class_name=class_name;
        this.section_name=section_name;
        this.phone_number=phone_number;
        this.image_path = image_path;
        this.email = email;
        this.class_roll = class_roll;
        this.gender = gender;
        this.birth_date = birth_date;
        this.blood_group = blood_group;
        this.religion = religion;
        this.father_name = father_name;
        this.mother_name = mother_name;
        this.father_contact = father_contact;
        this.mother_contact = mother_contact;
        this.father_occupation = father_occupation;
        this.mother_occupation = mother_occupation;
        this.alt_email = alt_email;
        this.emergency_contact = emergency_contact;
        this.present_address = present_address;
        this.permanent_address = permanent_address;
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClass_roll() {
        return class_roll;
    }

    public void setClass_roll(String class_roll) {
        this.class_roll = class_roll;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getFather_contact() {
        return father_contact;
    }

    public void setFather_contact(String father_contact) {
        this.father_contact = father_contact;
    }

    public String getMother_contact() {
        return mother_contact;
    }

    public void setMother_contact(String mother_contact) {
        this.mother_contact = mother_contact;
    }

    public String getFather_occupation() {
        return father_occupation;
    }

    public void setFather_occupation(String father_occupation) {
        this.father_occupation = father_occupation;
    }

    public String getMother_occupation() {
        return mother_occupation;
    }

    public void setMother_occupation(String mother_occupation) {
        this.mother_occupation = mother_occupation;
    }

    public String getAlt_email() {
        return alt_email;
    }

    public void setAlt_email(String alt_email) {
        this.alt_email = alt_email;
    }

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public String getPresent_address() {
        return present_address;
    }

    public void setPresent_address(String present_address) {
        this.present_address = present_address;
    }

    public String getPermanent_address() {
        return permanent_address;
    }

    public void setPermanent_address(String permanent_address) {
        this.permanent_address = permanent_address;
    }
}
