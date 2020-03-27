package com.example.onlinepathshala.Teacher;

public class Marks {

    public String student_id;
    public String student_name;
    public String subject_name;
    public String subject_id;
    public String total_marks;
    public String subjective_total;
    public String objective_total;
    public String practical_total;
    public String obtain_marks_subjective;
    public String obtain_marks_objective;
    public String obtain_marks_practical;
    public String weight;


    public Marks(String student_id,String student_name,String total_marks, String subjective_total, String objective_total, String practical_total, String obtain_marks_subjective, String obtain_marks_objective, String obtain_marks_practical, String weight) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.subject_name = subject_name;
        this.subject_id=subject_id;
        this.total_marks = total_marks;
        this.subjective_total = subjective_total;
        this.objective_total = objective_total;
        this.practical_total = practical_total;
        this.obtain_marks_subjective = obtain_marks_subjective;
        this.obtain_marks_objective = obtain_marks_objective;
        this.obtain_marks_practical = obtain_marks_practical;
        this.weight = weight;
    }
}
