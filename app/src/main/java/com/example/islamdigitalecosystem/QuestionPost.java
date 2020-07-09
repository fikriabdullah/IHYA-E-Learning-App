package com.example.islamdigitalecosystem;

import java.sql.Timestamp;
import java.util.Date;

public class QuestionPost extends ForumPostId {
    private String UserID, Content;
    private Date TimeStamp;

    public QuestionPost(){

    }

    public QuestionPost(String UserID, String content, Date TimeStamp) {
        this.UserID = UserID;
        Content = content;
        this.TimeStamp = TimeStamp;
    }

    public String getUserID() {
        return UserID;
    }

    public String getContent() {
        return Content;
    }

    public Date getTimestamp() {
        return TimeStamp;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setTimestamp(Timestamp TimeStamp) {
        this.TimeStamp = TimeStamp;
    }
}
