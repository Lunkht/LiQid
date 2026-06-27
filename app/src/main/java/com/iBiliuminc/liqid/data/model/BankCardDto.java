package com.iBiliuminc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class BankCardDto {
    @SerializedName("id")
    private String id;
    @SerializedName("card_number")
    private String cardNumber;
    @SerializedName("cardholder_name")
    private String cardholderName;
    @SerializedName("expiry_date")
    private String expiryDate;
    @SerializedName("plan")
    private String plan;
    @SerializedName("scheme")
    private String scheme;
    @SerializedName("frozen")
    private boolean frozen;
    @SerializedName("online_enabled")
    private boolean onlineEnabled;
    @SerializedName("contactless_enabled")
    private boolean contactlessEnabled;
    @SerializedName("atm_enabled")
    private boolean atmEnabled;

    public String getId() { return id; }
    public String getCardNumber() { return cardNumber; }
    public String getCardholderName() { return cardholderName; }
    public String getExpiryDate() { return expiryDate; }
    public String getPlan() { return plan; }
    public String getScheme() { return scheme; }
    public boolean isFrozen() { return frozen; }
    public boolean isOnlineEnabled() { return onlineEnabled; }
    public boolean isContactlessEnabled() { return contactlessEnabled; }
    public boolean isAtmEnabled() { return atmEnabled; }
}
