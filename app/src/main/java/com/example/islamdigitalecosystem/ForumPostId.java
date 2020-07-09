package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class ForumPostId {
    @Exclude
    public String ForumPostId;


    public <T extends ForumPostId> T withId(@NonNull final String id){
        this.ForumPostId = id;
        return (T) this;
    }

}
