package com.iBiliuminc.liqid.data.model;

public class LoginRequest {
    private String email;
    private String pin;

    public LoginRequest(String email, String pin) {
        this.email = email;
        this.pin = pin;
    }

    public String getEmail() { return email; }
    public String getPin() { return pin; }
}
