package com.vulsoftinc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class TransactionDto {
    @SerializedName("id")
    private String id;
    @SerializedName("merchant_name")
    private String merchantName;
    @SerializedName("date")
    private String date;
    @SerializedName("amount")
    private double amount;
    @SerializedName("type")
    private String type;
    @SerializedName("status")
    private String status;
    @SerializedName("category")
    private String category;
    @SerializedName("reference")
    private String reference;
    @SerializedName("description")
    private String description;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
