package com.example.onlinepathshala.Student;

public class Result_item_info {
    public String id;
    public String subject_name;
    public String total_marks;
    public String subjective_highest;
    public String objective_highest;
    public String practical_highest;
    public String obtain_marks_subjective;
    public String obtain_marks_objective;
    public String obtain_marks_practical;
    public String total_in_percentage;
    public String highest_total_3;
    public String obtain_total_3;
    public String tutorial;
    public String total_100;
    public String highest_100;
    public String lp;
    public String gp;
    public String student_id;
    public String student_name;
    public String subjective_total;
    public String objective_total;
    public String practical_total;
    public String publisher_id;
    public String weight;
    public String total_in_weight;
    public String total_in_100;
    public String date;

    public Result_item_info(String id,String subject_name, String total_marks,String subjective_total,String objective_total,String practical_total, String subjective_highest, String objective_highest, String practical_highest,String highest_total_3, String obtain_marks_subjective, String obtain_marks_objective, String obtain_marks_practical,String obtain_total_3, String total_in_percentage,String tutorial, String total_100, String highest_100, String lp, String gp,String publisher_id,String date) {

        this.id=id;
        this.subject_name = subject_name;
        this.total_marks = total_marks;
        this.subjective_total=subjective_total;
        this.objective_total=objective_total;
        this.practical_total=practical_total;
        this.subjective_highest = subjective_highest;
        this.objective_highest = objective_highest;
        this.practical_highest = practical_highest;
        this.obtain_marks_subjective = obtain_marks_subjective;
        this.obtain_marks_objective = obtain_marks_objective;
        this.obtain_marks_practical = obtain_marks_practical;
        this.total_in_percentage = total_in_percentage;
        this.highest_total_3 = highest_total_3;
        this.obtain_total_3 = obtain_total_3;
        this.tutorial = tutorial;
        this.total_100 = total_100;
        this.highest_100 = highest_100;
        this.lp = lp;
        this.gp = gp;
        this.publisher_id=publisher_id;
        this.date=date;
    }

    public Result_item_info(String id,String subject_name, String total_marks,String subjective_total,String objective_total,String practical_total, String subjective_highest, String objective_highest, String practical_highest,String highest_total_3, String obtain_marks_subjective, String obtain_marks_objective, String obtain_marks_practical,String obtain_total_3, String weight,String total_in_weight, String total_in_100, String lp, String gp,String publisher_id,String date) {

        this.id=id;
        this.subject_name = subject_name;
        this.total_marks = total_marks;
        this.subjective_total=subjective_total;
        this.objective_total=objective_total;
        this.practical_total=practical_total;
        this.subjective_highest = subjective_highest;
        this.objective_highest = objective_highest;
        this.practical_highest = practical_highest;
        this.obtain_marks_subjective = obtain_marks_subjective;
        this.obtain_marks_objective = obtain_marks_objective;
        this.obtain_marks_practical = obtain_marks_practical;
        this.total_in_percentage = total_in_percentage;
        this.highest_total_3 = highest_total_3;
        this.obtain_total_3 = obtain_total_3;
        this.weight = weight;
        this.total_in_weight = total_in_weight;
        this.total_in_100 = total_in_100;
        this.lp = lp;
        this.gp = gp;
        this.publisher_id=publisher_id;
        this.date=date;
    }



    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(String total_marks) {
        this.total_marks = total_marks;
    }

    public String getSubjective_highest() {
        return subjective_highest;
    }

    public void setSubjective_highest(String subjective_highest) {
        this.subjective_highest = subjective_highest;
    }

    public String getObjective_highest() {
        return objective_highest;
    }

    public void setObjective_highest(String objective_highest) {
        this.objective_highest = objective_highest;
    }

    public String getPractical_highest() {
        return practical_highest;
    }

    public void setPractical_highest(String practical_highest) {
        this.practical_highest = practical_highest;
    }



    public String getTotal_in_percentage() {
        return total_in_percentage;
    }

    public void setTotal_in_percentage(String total_in_percentage) {
        this.total_in_percentage = total_in_percentage;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public String getTotal_100() {
        return total_100;
    }

    public void setTotal_100(String total_100) {
        this.total_100 = total_100;
    }

    public String getHighest_100() {
        return highest_100;
    }

    public void setHighest_100(String highest_100) {
        this.highest_100 = highest_100;
    }

    public String getLp() {
        return lp;
    }

    public void setLp(String lp) {
        this.lp = lp;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getHighest_total_3() {
        return highest_total_3;
    }

    public void setHighest_total_3(String highest_total_3) {
        this.highest_total_3 = highest_total_3;
    }

    public String getObtain_total_3() {
        return obtain_total_3;
    }

    public void setObtain_total_3(String obtain_total_3) {
        this.obtain_total_3 = obtain_total_3;
    }

    public String getObtain_marks_subjective() {
        return obtain_marks_subjective;
    }

    public void setObtain_marks_subjective(String obtain_marks_subjective) {
        this.obtain_marks_subjective = obtain_marks_subjective;
    }

    public String getObtain_marks_objective() {
        return obtain_marks_objective;
    }

    public void setObtain_marks_objective(String obtain_marks_objective) {
        this.obtain_marks_objective = obtain_marks_objective;
    }

    public String getObtain_marks_practical() {
        return obtain_marks_practical;
    }

    public void setObtain_marks_practical(String obtain_marks_practical) {
        this.obtain_marks_practical = obtain_marks_practical;
    }
}
