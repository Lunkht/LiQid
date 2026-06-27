package com.iBiliuminc.liqid.domain.model;

public class Country {
    private final String name;
    private final String code;
    private final String isoCode;
    private final String flagEmoji;

    public Country(String name, String code, String isoCode, String flagEmoji) {
        this.name = name;
        this.code = code;
        this.isoCode = isoCode;
        this.flagEmoji = flagEmoji;
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public String getIsoCode() { return isoCode; }
    public String getFlagEmoji() { return flagEmoji; }
}
