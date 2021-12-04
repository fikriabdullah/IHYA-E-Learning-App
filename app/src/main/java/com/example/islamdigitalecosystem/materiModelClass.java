package com.example.islamdigitalecosystem;

public class materiModelClass {

    public materiModelClass (){

    }

    public materiModelClass(String fileDwnldUrl, String materiContent, String author, boolean isPaidmateri, Double hargaMateri) {
        this.fileDwnldUrl = fileDwnldUrl;
        this.materiContent = materiContent;
        this.author = author;
        this.isPaidmateri = isPaidmateri;
        this.hargaMateri = hargaMateri;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isPaidmateri() {
        return isPaidmateri;
    }

    public void setPaidmateri(boolean paidmateri) {
        isPaidmateri = paidmateri;
    }

    public Double getHargaMateri() {
        return hargaMateri;
    }

    public void setHargaMateri(Double hargaMateri) {
        this.hargaMateri = hargaMateri;
    }

    String fileDwnldUrl, materiContent, author;
    boolean isPaidmateri;
    Double hargaMateri;







}
