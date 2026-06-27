package com.iBiliuminc.liqid;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class LiqidApp extends Application {

    private static LiqidApp instance;

    public static final String CHANNEL_TRANSACTIONS = "channel_transactions";
    public static final String CHANNEL_SECURITY = "channel_security";
    public static final String CHANNEL_PROMOTIONS = "channel_promotions";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createNotificationChannels();
    }

    public static LiqidApp getInstance() {
        return instance;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel transactions = new NotificationChannel(
                    CHANNEL_TRANSACTIONS,
                    "Transactions",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            transactions.setDescription("Transaction notifications");

            NotificationChannel security = new NotificationChannel(
                    CHANNEL_SECURITY,
                    "Security",
                    NotificationManager.IMPORTANCE_HIGH
            );
            security.setDescription("Security alerts");

            NotificationChannel promotions = new NotificationChannel(
                    CHANNEL_PROMOTIONS,
                    "Promotions",
                    NotificationManager.IMPORTANCE_LOW
            );
            promotions.setDescription("Promotional offers");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(transactions);
                manager.createNotificationChannel(security);
                manager.createNotificationChannel(promotions);
            }
        }
    }
}
