package com.iBiliuminc.liqid.domain.model;

public class User {
    private String name;
    private String initials;
    private String phone;
    private String plan;

    public User(String name, String initials, String phone, String plan) {
        this.name = name;
        this.initials = initials;
        this.phone = phone;
        this.plan = plan;
    }

    public String getName() { return name; }
    public String getInitials() { return initials; }
    public String getPhone() { return phone; }
    public String getPlan() { return plan; }
}
