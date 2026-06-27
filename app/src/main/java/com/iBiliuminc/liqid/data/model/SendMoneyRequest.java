package com.iBiliuminc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class SendMoneyRequest {
    @SerializedName("iban")
    private String iban;
    @SerializedName("amount")
    private double amount;
    @SerializedName("description")
    private String description;

    public SendMoneyRequest(String iban, double amount, String description) {
        this.iban = iban;
        this.amount = amount;
        this.description = description;
    }
}
