package com.example.islamdigitalecosystem;

public class model2  {
    private int image1;
    private String title1;

    public model2 (int image, String title){
        this.image1 = image;
        this.title1 = title;
    }

    public int getImage() {
        return image1;
    }

    public void setImage(int image) {
        this.image1 = image;
    }

    public String getTitle() {
        return title1;
    }

    public void setTitle(String title) {
        this.title1 = title;
    }
}
