package com.vulsoftinc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class AccountDto {
    @SerializedName("id")
    private String id;
    @SerializedName("balance")
    private double balance;
    @SerializedName("currency")
    private String currency;
    @SerializedName("daily_change")
    private double dailyChange;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public double getDailyChange() { return dailyChange; }
    public void setDailyChange(double dailyChange) { this.dailyChange = dailyChange; }
}
