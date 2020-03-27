package com.example.onlinepathshala.Authority;

public class Section {

    public String id;
    public String section_name;
    public String shift;
    public String teacher_name;
    public String teacher_id;
    public String total_student;

    public Section(String id, String shift,String section_name, String teacher_name,String teacher_id) {
        this.id = id;
        this.shift=shift;
        this.section_name = section_name;
        this.teacher_name = teacher_name;
        this.teacher_id=teacher_id;
    }

    public Section(String id,String shift, String section_name,  String teacher_name, String teacher_id, String total_student) {
        this.id = id;
        this.section_name = section_name;
        this.shift = shift;
        this.teacher_name = teacher_name;
        this.teacher_id = teacher_id;
        this.total_student = total_student;
    }
}
