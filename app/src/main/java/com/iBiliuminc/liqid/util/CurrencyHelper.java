package com.iBiliuminc.liqid.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class CurrencyHelper {

    private static final LinkedHashMap<String, CurrencyInfo> CURRENCIES = new LinkedHashMap<>();
    static {
        CURRENCIES.put("EUR", new CurrencyInfo("EUR", "\u20AC", "Euro"));
        CURRENCIES.put("USD", new CurrencyInfo("USD", "$", "Dollar US"));
        CURRENCIES.put("GBP", new CurrencyInfo("GBP", "\u00A3", "Livre sterling"));
        CURRENCIES.put("CHF", new CurrencyInfo("CHF", "Fr", "Franc suisse"));
        CURRENCIES.put("JPY", new CurrencyInfo("JPY", "\u00A5", "Yen japonais"));
        CURRENCIES.put("CAD", new CurrencyInfo("CAD", "CA$", "Dollar canadien"));
        CURRENCIES.put("AUD", new CurrencyInfo("AUD", "A$", "Dollar australien"));
    }

    public static Map<String, CurrencyInfo> getAll() {
        return CURRENCIES;
    }

    public static String[] getCodes() {
        return CURRENCIES.keySet().toArray(new String[0]);
    }

    public static String[] getLabels() {
        String[] labels = new String[CURRENCIES.size()];
        int i = 0;
        for (CurrencyInfo info : CURRENCIES.values()) {
            labels[i++] = info.code + " \u2014 " + info.name + " (" + info.symbol + ")";
        }
        return labels;
    }

    public static String symbolFor(String code) {
        CurrencyInfo info = CURRENCIES.get(code);
        return info != null ? info.symbol : code;
    }

    public static class CurrencyInfo {
        public final String code;
        public final String symbol;
        public final String name;

        public CurrencyInfo(String code, String symbol, String name) {
            this.code = code;
            this.symbol = symbol;
            this.name = name;
        }
    }
}
