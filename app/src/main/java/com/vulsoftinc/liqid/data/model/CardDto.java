package com.vulsoftinc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class CardDto {
    @SerializedName("id")
    private String id;
    @SerializedName("account_id")
    private String accountId;
    @SerializedName("card_number")
    private String cardNumber;
    @SerializedName("cardholder_name")
    private String cardholderName;
    @SerializedName("expiry_month")
    private int expiryMonth;
    @SerializedName("expiry_year")
    private int expiryYear;
    @SerializedName("type")
    private String type;
    @SerializedName("status")
    private String status;
    @SerializedName("scheme")
    private String scheme;
    @SerializedName("contactless_enabled")
    private boolean contactlessEnabled;
    @SerializedName("online_payments_enabled")
    private boolean onlinePaymentsEnabled;
    @SerializedName("atm_enabled")
    private boolean atmEnabled;
    @SerializedName("spending_limit")
    private double spendingLimit;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardholderName() { return cardholderName; }
    public void setCardholderName(String cardholderName) { this.cardholderName = cardholderName; }
    public int getExpiryMonth() { return expiryMonth; }
    public void setExpiryMonth(int expiryMonth) { this.expiryMonth = expiryMonth; }
    public int getExpiryYear() { return expiryYear; }
    public void setExpiryYear(int expiryYear) { this.expiryYear = expiryYear; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getScheme() { return scheme; }
    public void setScheme(String scheme) { this.scheme = scheme; }
    public boolean isContactlessEnabled() { return contactlessEnabled; }
    public void setContactlessEnabled(boolean contactlessEnabled) { this.contactlessEnabled = contactlessEnabled; }
    public boolean isOnlinePaymentsEnabled() { return onlinePaymentsEnabled; }
    public void setOnlinePaymentsEnabled(boolean onlinePaymentsEnabled) { this.onlinePaymentsEnabled = onlinePaymentsEnabled; }
    public boolean isAtmEnabled() { return atmEnabled; }
    public void setAtmEnabled(boolean atmEnabled) { this.atmEnabled = atmEnabled; }
    public double getSpendingLimit() { return spendingLimit; }
    public void setSpendingLimit(double spendingLimit) { this.spendingLimit = spendingLimit; }
}
