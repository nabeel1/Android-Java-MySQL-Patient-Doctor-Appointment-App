package com.example.shahidhussain.assignemnt2.DataBase;

public class dbPath {
    private String path="https://phealthcarecom.000webhostapp.com/FYP/";
    public dbPath(String path) {
        this.path = this.path+path+".php";
    }
    public dbPath(){}

    public String getPath() {
        return path;
    }
}
