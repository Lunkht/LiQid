package com.iBiliuminc.liqid.domain.model;

public class Card {

    public enum CardType {
        PHYSICAL, VIRTUAL
    }

    public enum CardStatus {
        ACTIVE, FROZEN, BLOCKED, EXPIRED
    }

    public enum CardScheme {
        VISA, MASTERCARD
    }

    private final String id;
    private final String accountId;
    private final String cardNumber;
    private final String cardholderName;
    private final int expiryMonth;
    private final int expiryYear;
    private final CardType type;
    private final CardStatus status;
    private final CardScheme scheme;
    private final boolean contactlessEnabled;
    private final boolean onlinePaymentsEnabled;
    private final boolean atmEnabled;
    private final double spendingLimit;

    private Card(Builder builder) {
        this.id = builder.id;
        this.accountId = builder.accountId;
        this.cardNumber = builder.cardNumber;
        this.cardholderName = builder.cardholderName;
        this.expiryMonth = builder.expiryMonth;
        this.expiryYear = builder.expiryYear;
        this.type = builder.type;
        this.status = builder.status;
        this.scheme = builder.scheme;
        this.contactlessEnabled = builder.contactlessEnabled;
        this.onlinePaymentsEnabled = builder.onlinePaymentsEnabled;
        this.atmEnabled = builder.atmEnabled;
        this.spendingLimit = builder.spendingLimit;
    }

    public String getId() { return id; }
    public String getAccountId() { return accountId; }
    public String getCardNumber() { return cardNumber; }
    public String getCardholderName() { return cardholderName; }
    public int getExpiryMonth() { return expiryMonth; }
    public int getExpiryYear() { return expiryYear; }
    public CardType getType() { return type; }
    public CardStatus getStatus() { return status; }
    public CardScheme getScheme() { return scheme; }
    public boolean isContactlessEnabled() { return contactlessEnabled; }
    public boolean isOnlinePaymentsEnabled() { return onlinePaymentsEnabled; }
    public boolean isAtmEnabled() { return atmEnabled; }
    public double getSpendingLimit() { return spendingLimit; }

    public boolean isFrozen() {
        return status == CardStatus.FROZEN;
    }

    public boolean isActive() {
        return status == CardStatus.ACTIVE;
    }

    public static class Builder {
        private String id;
        private String accountId;
        private String cardNumber;
        private String cardholderName;
        private int expiryMonth;
        private int expiryYear;
        private CardType type;
        private CardStatus status;
        private CardScheme scheme;
        private boolean contactlessEnabled;
        private boolean onlinePaymentsEnabled;
        private boolean atmEnabled;
        private double spendingLimit;

        public Builder id(String id) { this.id = id; return this; }
        public Builder accountId(String accountId) { this.accountId = accountId; return this; }
        public Builder cardNumber(String cardNumber) { this.cardNumber = cardNumber; return this; }
        public Builder cardholderName(String cardholderName) { this.cardholderName = cardholderName; return this; }
        public Builder expiryMonth(int expiryMonth) { this.expiryMonth = expiryMonth; return this; }
        public Builder expiryYear(int expiryYear) { this.expiryYear = expiryYear; return this; }
        public Builder type(CardType type) { this.type = type; return this; }
        public Builder status(CardStatus status) { this.status = status; return this; }
        public Builder scheme(CardScheme scheme) { this.scheme = scheme; return this; }
        public Builder contactlessEnabled(boolean contactlessEnabled) { this.contactlessEnabled = contactlessEnabled; return this; }
        public Builder onlinePaymentsEnabled(boolean onlinePaymentsEnabled) { this.onlinePaymentsEnabled = onlinePaymentsEnabled; return this; }
        public Builder atmEnabled(boolean atmEnabled) { this.atmEnabled = atmEnabled; return this; }
        public Builder spendingLimit(double spendingLimit) { this.spendingLimit = spendingLimit; return this; }

        public Card build() {
            return new Card(this);
        }
    }
}
