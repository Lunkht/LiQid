package com.vulsoftinc.liqid.domain.model;

public class BankCard {
    private String id;
    private String cardNumber;
    private String cardholderName;
    private String expiryDate;
    private String plan;
    private String scheme;
    private boolean frozen;
    private boolean onlineEnabled;
    private boolean contactlessEnabled;
    private boolean atmEnabled;

    public BankCard(String id, String cardNumber, String cardholderName,
                    String expiryDate, String plan, String scheme,
                    boolean frozen, boolean onlineEnabled,
                    boolean contactlessEnabled, boolean atmEnabled) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
        this.expiryDate = expiryDate;
        this.plan = plan;
        this.scheme = scheme;
        this.frozen = frozen;
        this.onlineEnabled = onlineEnabled;
        this.contactlessEnabled = contactlessEnabled;
        this.atmEnabled = atmEnabled;
    }

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

    public void setFrozen(boolean frozen) { this.frozen = frozen; }
    public void setOnlineEnabled(boolean onlineEnabled) { this.onlineEnabled = onlineEnabled; }
    public void setContactlessEnabled(boolean contactlessEnabled) { this.contactlessEnabled = contactlessEnabled; }
    public void setAtmEnabled(boolean atmEnabled) { this.atmEnabled = atmEnabled; }
}
