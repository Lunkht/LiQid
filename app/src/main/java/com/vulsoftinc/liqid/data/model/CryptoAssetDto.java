package com.vulsoftinc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class CryptoAssetDto {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("price")
    private double price;
    @SerializedName("change_24h")
    private double change24h;
    @SerializedName("value")
    private double value;
    @SerializedName("amount")
    private double amount;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getChange24h() { return change24h; }
    public double getValue() { return value; }
    public double getAmount() { return amount; }
}
