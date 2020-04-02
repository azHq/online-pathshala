package com.example.onlinepathshala;

public class Final_Result_Data_Class implements Comparable<Final_Result_Data_Class> {

    public String student_id;
    public String student_name;
    public String subject_name;
    public String total_marks;
    public String total_highest;
    public String class_test_marks;
    public String monthly_test_marks;
    public String half_yearly_marks;
    public String final_exam_marks;
    public String total_obtain_marks;
    public String lp;
    public String gp;
    public float sum_of_all_sub_marks;
    public int position;
    public float highest_marks;
    public float highest_marks2;
    public String date;
    public float total_marks2;


    public Final_Result_Data_Class(String student_id, String student_name, String subject_name, String total_marks, String total_highest, String class_test_marks, String monthly_test_marks, String half_yearly_marks, String final_exam_marks, String total_obtain_marks, String lp, String gp,String date) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.subject_name = subject_name;
        this.total_marks = total_marks;
        this.total_highest = total_highest;
        this.class_test_marks = class_test_marks;
        this.monthly_test_marks = monthly_test_marks;
        this.half_yearly_marks = half_yearly_marks;
        this.final_exam_marks = final_exam_marks;
        this.total_obtain_marks = total_obtain_marks;
        this.lp = lp;
        this.gp = gp;
        this.date=date;
    }

    public Final_Result_Data_Class(String student_id, String student_name,float sum_of_all_sub_marks,int position,float total_marks2,float highest_marks,String date){
        this.student_id = student_id;
        this.student_name = student_name;
        this.sum_of_all_sub_marks=sum_of_all_sub_marks;
        this.position=position;
        this.total_marks2=total_marks2;
        this.highest_marks2=highest_marks;
        this.date=date;
    }

    @Override
    public int compareTo(Final_Result_Data_Class final_result_data_class) {

        if(this.sum_of_all_sub_marks>final_result_data_class.sum_of_all_sub_marks){

            return -1;
        }
        else{
            return 1;
        }
    }
}
