package com.vulsoftinc.liqid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vulsoftinc.liqid.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
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
