package com.example.onlinepathshala.Authority;

public class Exam_Infos {

    public String exam_id;
    public String exam_name;
    public String exam_type;
    public String subject_name;
    public String subject_id;
    public String date;
    public String time;
    public String result_publish;
    public String marks;
    public String topic;

    public Exam_Infos(String exam_id, String exam_name, String exam_type,String subject_id, String subject_name,  String date, String time, String result_publish,String marks,String topic) {
        this.exam_id = exam_id;
        this.exam_name = exam_name;
        this.exam_type = exam_type;
        this.subject_name = subject_name;
        this.subject_id = subject_id;
        this.date = date;
        this.time = time;
        this.result_publish = result_publish;
        this.marks=marks;
        this.topic=topic;
    }
}
