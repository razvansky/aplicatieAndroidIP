package com.example.aplicatieandroidip;

public class LoginRequest {
    private String username;
    private String password;
    private boolean rememberMe;

    public LoginRequest(String username, String password, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }
}