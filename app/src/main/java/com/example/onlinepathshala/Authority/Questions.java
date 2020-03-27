package com.example.onlinepathshala.Authority;

public class Questions {

    public String id;
    public String question;
    public String total_options;
    public String option_name1;
    public String option_name2;
    public String option_name3;
    public String option_name4;
    public String option_value1;
    public String option_value2;
    public String option_value3;
    public String option_value4;
    public String question_set_number;
    public String date;
    public String live;

    public Questions(String id, String question,String total_options, String option_name1, String option_name2, String option_name3, String option_name4, String option_value1, String option_value2, String option_value3, String option_value4, String question_set_number, String live,String date) {
        this.id = id;
        this.question = question;
        this.total_options=total_options;
        this.option_name1 = option_name1;
        this.option_name2 = option_name2;
        this.option_name3 = option_name3;
        this.option_name4 = option_name4;
        this.option_value1 = option_value1;
        this.option_value2 = option_value2;
        this.option_value3 = option_value3;
        this.option_value4 = option_value4;
        this.question_set_number = question_set_number;
        this.date=date;
        this.live = live;
    }
}
