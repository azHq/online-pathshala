package com.example.onlinepathshala;

public class Result_Info2 {

    public String id;
    public String student_id;
    public String student_name;
    public String subject_name;
    public String total_marks;
    public String subjective_total;
    public String objective_total;
    public String practical_total;
    public String obtain_subjective;
    public String obtain_objective;
    public String obtain_practical;
    public String weight;

    public Result_Info2(String id,String student_id, String student_name, String subject_name, String total_marks, String subjective_total, String objective_total, String practical_total, String obtain_subjective, String obtain_objective, String obtain_practical, String weight) {
        this.id=id;
        this.student_id = student_id;
        this.student_name = student_name;
        this.subject_name = subject_name;
        this.total_marks = total_marks;
        this.subjective_total = subjective_total;
        this.objective_total = objective_total;
        this.practical_total = practical_total;
        this.obtain_subjective = obtain_subjective;
        this.obtain_objective = obtain_objective;
        this.obtain_practical = obtain_practical;
        this.weight = weight;
    }
}
