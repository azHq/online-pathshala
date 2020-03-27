package com.example.onlinepathshala;

public class Notification_list_Item {

    public String id;
    public String user_id;
    public String user_type;
    public String sender_id;
    public String sender_type;
    public String notification_type;
    public String body;
    public String date;
    public String time;
    public String status;

    public Notification_list_Item(String id, String user_id, String user_type, String sender_id, String sender_type, String notification_type, String body, String date, String time, String status) {
        this.id = id;
        this.user_id = user_id;
        this.user_type = user_type;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.notification_type = notification_type;
        this.body = body;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
