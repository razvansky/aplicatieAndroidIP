package com.example.aplicatieandroidip;

public class Pacient {
    String pacientName;
    String pacientID;
    int pacientImage;

    public Pacient(String pacientName, String pacientID, int pacientImage) {
        this.pacientName = pacientName;
        this.pacientID = pacientID;
        this.pacientImage = pacientImage;
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
}
