package com.iBiliuminc.liqid.domain.model;

public class CryptoAsset {
    private String id;
    private String name;
    private String symbol;
    private double price;
    private double change24h;
    private double value;
    private double amount;

    public CryptoAsset(String id, String name, String symbol,
                       double price, double change24h,
                       double value, double amount) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.change24h = change24h;
        this.value = value;
        this.amount = amount;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getChange24h() { return change24h; }
    public double getValue() { return value; }
    public double getAmount() { return amount; }
}
