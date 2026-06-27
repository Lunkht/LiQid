package com.iBiliuminc.liqid.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.core.di.AppContainer;
import com.iBiliuminc.liqid.data.api.LiqidApiService;
import com.iBiliuminc.liqid.data.model.AuthResponse;
import com.iBiliuminc.liqid.data.model.LoginRequest;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.home.HomeActivity;
import com.iBiliuminc.liqid.ui.onboarding.OnboardingActivity;

import java.util.concurrent.Executor;

import retrofit2.Response;

public class AuthActivity extends BaseActivity {

    private final StringBuilder pinBuffer = new StringBuilder(6);
    private final View[] pinDots = new View[6];
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private String storedPin;
    private String loginEmail;
    private LiqidApiService api;
    private java.util.concurrent.ExecutorService backgroundExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        AppContainer container = AppContainer.getInstance(this);
        api = container.getApiService();
        backgroundExecutor = container.getExecutorService();

        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
        storedPin = prefs.getString("user_pin", "000000");
        loginEmail = getIntent().getStringExtra("email");

        pinDots[0] = findViewById(R.id.pin_dot_1);
        pinDots[1] = findViewById(R.id.pin_dot_2);
        pinDots[2] = findViewById(R.id.pin_dot_3);
        pinDots[3] = findViewById(R.id.pin_dot_4);
        pinDots[4] = findViewById(R.id.pin_dot_5);
        pinDots[5] = findViewById(R.id.pin_dot_6);

        setupKeyListeners();
        setupBiometric();
        updateDots();
    }

    private void setupKeyListeners() {
        int[] keyIds = {R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4,
                R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9};

        View.OnClickListener keyListener = v -> {
            int id = v.getId();
            String digit = null;
            if (id == R.id.key_0) digit = "0";
            else if (id == R.id.key_1) digit = "1";
            else if (id == R.id.key_2) digit = "2";
            else if (id == R.id.key_3) digit = "3";
            else if (id == R.id.key_4) digit = "4";
            else if (id == R.id.key_5) digit = "5";
            else if (id == R.id.key_6) digit = "6";
            else if (id == R.id.key_7) digit = "7";
            else if (id == R.id.key_8) digit = "8";
            else if (id == R.id.key_9) digit = "9";

            if (digit != null) {
                onDigitPressed(digit);
            }
        };

        for (int i = 0; i < keyIds.length; i++) {
            View keyView = findViewById(keyIds[i]);
            keyView.setOnClickListener(keyListener);
            TextView tvDigit = keyView.findViewById(R.id.tv_key_digit);
            if (tvDigit != null) {
                tvDigit.setText(String.valueOf(i));
            }
        }

        findViewById(R.id.key_delete).setOnClickListener(v -> onDeletePressed());

        findViewById(R.id.btn_switch_account).setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        });

        findViewById(R.id.key_biometric).setOnClickListener(v -> {
            if (biometricPrompt != null) {
                biometricPrompt.authenticate(
                        new BiometricPrompt.PromptInfo.Builder()
                                .setTitle(getString(R.string.biometric_title))
                                .setSubtitle(getString(R.string.biometric_subtitle))
                                .setNegativeButtonText(getString(R.string.biometric_cancel))
                                .build()
                );
            }
        });
    }

    private void setupBiometric() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(
                            BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        navigateToHome();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
    }

    private void onDigitPressed(String digit) {
        if (pinBuffer.length() >= 6) return;
        pinBuffer.append(digit);
        updateDots();
        if (pinBuffer.length() == 6) {
            validatePin();
        }
    }

    private void onDeletePressed() {
        if (pinBuffer.length() == 0) return;
        pinBuffer.deleteCharAt(pinBuffer.length() - 1);
        updateDots();
    }

    private void updateDots() {
        for (int i = 0; i < 6; i++) {
            if (i < pinBuffer.length()) {
                pinDots[i].setBackgroundResource(R.drawable.bg_pin_dot_filled);
            } else {
                pinDots[i].setBackgroundResource(R.drawable.bg_pin_dot_empty);
            }
        }
    }

    private void validatePin() {
        String pin = pinBuffer.toString();

        if (loginEmail != null) {
            authenticateWithApi(loginEmail, pin);
        } else {
            if (storedPin.equals(pin)) {
                navigateToHome();
            } else {
                vibrateError();
                Toast.makeText(this, "Code PIN incorrect", Toast.LENGTH_SHORT).show();
                pinBuffer.setLength(0);
                updateDots();
            }
        }
    }

    private void authenticateWithApi(String email, String pin) {
        Toast.makeText(this, "Connexion...", Toast.LENGTH_SHORT).show();

        backgroundExecutor.execute(() -> {
            try {
                Response<AuthResponse> response = api.login(new LoginRequest(email, pin)).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    String userName = response.body().getUser() != null
                            ? response.body().getUser().getName() : "";
                    runOnUiThread(() -> {
                        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
                        prefs.edit()
                                .putString("auth_token", token)
                                .putString("user_email", email)
                                .putString("user_name", userName)
                                .putString("user_pin", pin)
                                .apply();
                        navigateToHome();
                    });
                } else {
                    runOnUiThread(() -> {
                        vibrateError();
                        Toast.makeText(this, "Email ou PIN incorrect", Toast.LENGTH_SHORT).show();
                        pinBuffer.setLength(0);
                        updateDots();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    // Fallback: validate locally if network unavailable
                    if (storedPin.equals(pin)) {
                        Toast.makeText(this, "Mode hors-ligne", Toast.LENGTH_SHORT).show();
                        navigateToHome();
                    } else {
                        vibrateError();
                        Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                        pinBuffer.setLength(0);
                        updateDots();
                    }
                });
            }
        });
    }

    private void vibrateError() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
