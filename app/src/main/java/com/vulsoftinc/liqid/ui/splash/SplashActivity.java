package com.vulsoftinc.liqid.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.vulsoftinc.liqid.R;
import com.vulsoftinc.liqid.ui.BaseActivity;
import com.vulsoftinc.liqid.ui.auth.AuthActivity;
import com.vulsoftinc.liqid.ui.onboarding.OnboardingActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
            boolean hasAccount = prefs.contains("user_pin");

            if (hasAccount) {
                startActivity(new Intent(this, AuthActivity.class));
            } else {
                startActivity(new Intent(this, OnboardingActivity.class));
            }
            finish();
        }, 1500);
    }
}
