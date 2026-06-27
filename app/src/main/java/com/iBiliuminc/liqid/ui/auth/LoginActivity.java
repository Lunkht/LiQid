package com.iBiliuminc.liqid.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.core.di.AppContainer;
import com.iBiliuminc.liqid.data.api.LiqidApiService;
import com.iBiliuminc.liqid.data.model.LoginRequest;
import com.iBiliuminc.liqid.ui.BaseActivity;

import java.util.concurrent.ExecutorService;

public class LoginActivity extends BaseActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private MaterialButton btnContinue;
    private TextView btnForgotPassword;
    private LiqidApiService api;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppContainer container = AppContainer.getInstance(this);
        api = container.getApiService();
        executor = container.getExecutorService();

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        tilEmail = findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);
        btnContinue = findViewById(R.id.btn_continue);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);

        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                tilEmail.setError("Veuillez entrer votre email");
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.setError("Email invalide");
                return;
            }

            tilEmail.setError(null);

            Intent intent = new Intent(this, AuthActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        btnForgotPassword.setOnClickListener(v -> 
                Toast.makeText(this, "Réinitialisation de l'email envoyée", Toast.LENGTH_SHORT).show());
    }
}
