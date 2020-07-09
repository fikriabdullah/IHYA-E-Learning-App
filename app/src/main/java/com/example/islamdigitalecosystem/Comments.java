package com.example.islamdigitalecosystem;

import java.util.Date;

public class Comments {
    private String Message, userID;
    private Date Timestamp;

    public Comments(){

    }

    public Comments(String Message, String userID, Date Timestamp) {
        this.Message = Message;
        this.userID = userID;
        this.Timestamp = Timestamp;
    }

    public String getMessage() {
        return Message;
    }

    public String getUserID() {
        return userID;
    }

    public Date getTimestamp() {
        return Timestamp;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTimestamp(Date Timestamp) {
        this.Timestamp = Timestamp;
    }
}
