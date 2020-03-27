package com.example.onlinepathshala.Authority;

public class Subject {

    public String id;
    public String subject_name;
    public String total_assigned_classes;

    public Subject(String id, String subject_name, String total_assigned_classes) {
        this.id = id;
        this.subject_name = subject_name;
        this.total_assigned_classes = total_assigned_classes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getTotal_assigned_classes() {
        return total_assigned_classes;
    }

    public void setTotal_assigned_classes(String total_assigned_classes) {
        this.total_assigned_classes = total_assigned_classes;
    }
}
