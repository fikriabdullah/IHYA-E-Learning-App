package com.example.islamdigitalecosystem;

import java.sql.Timestamp;
import java.util.Date;

public class QuestionPost {
    private String UserID, Content;
    private Date timestamp;

    public QuestionPost(){

    }

    public QuestionPost(String UserID, String content, Date timestamp) {
        this.UserID = UserID;
        Content = content;
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return UserID;
    }

    public String getContent() {
        return Content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
