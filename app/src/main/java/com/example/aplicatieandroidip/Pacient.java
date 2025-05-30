package com.example.aplicatieandroidip;

public class Pacient {
    String pacientName;
    String pacientID;
    int pacientImage;
    String pacientPhoneNo;

    public Pacient(String pacientName, String pacientID, int pacientImage) {
        this.pacientName = pacientName;
        this.pacientID = pacientID;
        this.pacientImage = pacientImage;
    }

    public Pacient(String pacientName, String pacientID, String pacientPhoneNo) {
        this.pacientName = pacientName;
        this.pacientID = pacientID;
        this.pacientPhoneNo = pacientPhoneNo;
    }

    public String getPacientName() {
        return pacientName;
    }

    public String getPacientID() {
        return pacientID;
    }

    public int getPacientImage() {
        return pacientImage;
    }

    public String getPacientPhoneNo() {
        return pacientPhoneNo;
    }
}
