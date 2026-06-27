package com.iBiliuminc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class UserDto {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("initials")
    private String initials;
    @SerializedName("phone")
    private String phone;
    @SerializedName("plan")
    private String plan;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getInitials() { return initials; }
    public void setInitials(String initials) { this.initials = initials; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
}
