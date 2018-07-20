package com.example.peterphchen.speechrecognizerservice;

public class CurserData {
    private String addrees ="";
    private String date = "";
    private String number ="";
    private String url ="";

    public CurserData(String addrees, String date, String number,String url) {
        this.addrees = addrees;
        this.date = date;
        this.number = number;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddrees() {
        return addrees;
    }

    public void setAddrees(String addrees) {
        this.addrees = addrees;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
