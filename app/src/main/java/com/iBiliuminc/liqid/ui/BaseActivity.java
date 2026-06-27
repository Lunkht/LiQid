package com.iBiliuminc.liqid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.auth.AuthActivity;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        applyLanguage();
        applyAppTheme();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
        if (prefs.getBoolean("incognito_enabled", false)) {
            prefs.edit().putLong("lock_time", System.currentTimeMillis()).apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
        if (prefs.getBoolean("incognito_enabled", false)) {
            long lockTime = prefs.getLong("lock_time", 0);
            if (lockTime > 0 && System.currentTimeMillis() - lockTime > 3000) {
                Intent intent = new Intent(this, AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    private void applyLanguage() {
        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
        String lang = prefs.getString("app_lang", "fr");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void applyAppTheme() {
        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
        String theme = prefs.getString("app_theme", "dark");
        
        switch (theme) {
            case "light":
                setTheme(R.style.Theme_Banking_Light);
                break;
            case "neon":
                setTheme(R.style.Theme_Banking_Neon);
                break;
            default:
                setTheme(R.style.Theme_Banking);
                break;
        }
    }
}
