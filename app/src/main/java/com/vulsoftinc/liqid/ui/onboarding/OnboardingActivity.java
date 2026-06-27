package com.vulsoftinc.liqid.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.vulsoftinc.liqid.R;
import com.vulsoftinc.liqid.ui.BaseActivity;
import com.vulsoftinc.liqid.ui.auth.LoginActivity;
import com.vulsoftinc.liqid.ui.auth.PhoneEntryActivity;
import com.vulsoftinc.liqid.ui.home.HomeActivity;

public class OnboardingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button btnCreateAccount = findViewById(R.id.btn_create_account);
        Button btnLogin = findViewById(R.id.btn_login);

        if (btnCreateAccount != null) {
            btnCreateAccount.setOnClickListener(v ->
                    startActivity(new Intent(this, PhoneEntryActivity.class)));
        }
        
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v ->
                    startActivity(new Intent(this, LoginActivity.class)));
        }

        findViewById(R.id.btn_skip).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}
