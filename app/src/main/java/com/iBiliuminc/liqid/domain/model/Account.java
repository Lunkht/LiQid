package com.iBiliuminc.liqid.domain.model;

public class Account {
    private String id;
    private double balance;
    private String currency;
    private double dailyChange;

    public Account(String id, double balance, String currency, double dailyChange) {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
        this.dailyChange = dailyChange;
    }

    public String getId() { return id; }
    public double getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public double getDailyChange() { return dailyChange; }
}
