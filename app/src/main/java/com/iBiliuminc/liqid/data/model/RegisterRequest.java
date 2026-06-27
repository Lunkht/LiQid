package com.iBiliuminc.liqid.data.model;

public class RegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String pin;

    public RegisterRequest(String name, String email, String phone, String pin) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pin = pin;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPin() { return pin; }
}
