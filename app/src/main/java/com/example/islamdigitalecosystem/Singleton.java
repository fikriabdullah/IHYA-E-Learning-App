package com.example.islamdigitalecosystem;

public class Singleton {
    private static Singleton instance;
    String babReference ;

    private Singleton(){

    }

    public static Singleton getInstance(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public String getBabReference() {
        return babReference;
    }

    public void setBabReference(String babReference) {
        this.babReference = babReference;
    }

}
