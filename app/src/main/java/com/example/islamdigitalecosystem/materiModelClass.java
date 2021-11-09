package com.example.islamdigitalecosystem;

public class materiModelClass {

    public materiModelClass (){

    }

    public materiModelClass(String fileDwnldUrl, String materiContent) {
        this.fileDwnldUrl = fileDwnldUrl;
        this.materiContent = materiContent;
    }

    public String getFileDwnldUrl() {
        return fileDwnldUrl;
    }

    public void setFileDwnldUrl(String fileDwnldUrl) {
        this.fileDwnldUrl = fileDwnldUrl;
    }

    public String getMateriContent() {
        return materiContent;
    }

    public void setMateriContent(String materiContent) {
        this.materiContent = materiContent;
    }

    String fileDwnldUrl, materiContent;







}
