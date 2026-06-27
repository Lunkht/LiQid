package com.iBiliuminc.liqid.domain.model;

import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum Type { DEBIT, CREDIT }
    public enum Status { PENDING, COMPLETED, FAILED }

    private String id;
    private String merchantName;
    private String date;
    private double amount;
    private Type type;
    private Status status;
    private String category;
    private String reference;
    private String description;

    public Transaction(String id, String merchantName, String date, double amount,
                       Type type, Status status, String category,
                       String reference, String description) {
        this.id = id;
        this.merchantName = merchantName;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.category = category;
        this.reference = reference;
        this.description = description;
    }

    public String getId() { return id; }
    public String getMerchantName() { return merchantName; }
    public String getDate() { return date; }
    public double getAmount() { return amount; }
    public Type getType() { return type; }
    public Status getStatus() { return status; }
    public String getCategory() { return category; }
    public String getReference() { return reference; }
    public String getDescription() { return description; }
}
