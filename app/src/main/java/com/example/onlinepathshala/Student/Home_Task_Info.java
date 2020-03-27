package com.example.onlinepathshala.Student;

public class Home_Task_Info {

    public String id;
    public String subject_name;
    public String assigned_date;
    public String submission_date;
    public String tile;
    public String body;
    public String teacher_id;
    public String task_number;

    public Home_Task_Info(String id, String subject_name, String assigned_date, String submission_date, String tile, String body, String teacher_id,String task_number) {
        this.id = id;
        this.subject_name = subject_name;
        this.assigned_date = assigned_date;
        this.submission_date = submission_date;
        this.tile = tile;
        this.body = body;
        this.teacher_id = teacher_id;
        this.task_number=task_number;
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

    public String getAssigned_date() {
        return assigned_date;
    }

    public void setAssigned_date(String assigned_date) {
        this.assigned_date = assigned_date;
    }

    public String getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(String submission_date) {
        this.submission_date = submission_date;
    }

    public String getTtile() {
        return tile;
    }

    public void setTtile(String ttile) {
        this.tile = ttile;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }
}
