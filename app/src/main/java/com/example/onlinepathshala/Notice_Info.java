package com.example.onlinepathshala;

public class Notice_Info {

    public String id;
    public String title;
    public String details;
    public String date;
    public String file_type;
    public String file_path;

    public Notice_Info(String id, String title, String details, String date, String file_type, String file_path) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.date = date;
        this.file_type = file_type;
        this.file_path = file_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
