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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.core.di.AppContainer;
import com.iBiliuminc.liqid.data.api.LiqidApiService;
import com.iBiliuminc.liqid.data.model.AuthResponse;
import com.iBiliuminc.liqid.data.model.RegisterRequest;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.home.HomeActivity;

import java.util.concurrent.ExecutorService;

import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private View layoutForm, layoutPin;
    private TextInputLayout tilName, tilEmail, tilPhone;
    private TextInputEditText etName, etEmail, etPhone;
    private MaterialButton btnNext;
    private TextView tvPinTitle, tvPinHint, tvPinError;
    private final View[] pinDots = new View[6];
    private final StringBuilder pinBuffer = new StringBuilder(6);
    private String firstPin = null;
    private boolean isConfirming = false;
    private boolean isChangePinOnly = false;
    private LiqidApiService api;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AppContainer container = AppContainer.getInstance(this);
        api = container.getApiService();
        executor = container.getExecutorService();

        isChangePinOnly = getIntent().getBooleanExtra("change_pin_only", false);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        layoutForm = findViewById(R.id.layout_form);
        layoutPin = findViewById(R.id.layout_pin);

        tilName = findViewById(R.id.til_full_name);
        tilEmail = findViewById(R.id.til_email);
        tilPhone = findViewById(R.id.til_phone);

        etName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        btnNext = findViewById(R.id.btn_next);

        if (isChangePinOnly) {
            layoutForm.setVisibility(View.GONE);
            layoutPin.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.toolbar).findViewById(com.google.android.material.R.id.title)).setText("Changer le PIN");
        }

        String prefilledPhone = getIntent().getStringExtra("phone_number");
        if (prefilledPhone != null) {
            etPhone.setText(prefilledPhone);
        }

        tvPinTitle = findViewById(R.id.tv_pin_title);
        tvPinHint = findViewById(R.id.tv_pin_hint);
        tvPinError = findViewById(R.id.tv_pin_error);

        pinDots[0] = findViewById(R.id.pin_dot_1);
        pinDots[1] = findViewById(R.id.pin_dot_2);
        pinDots[2] = findViewById(R.id.pin_dot_3);
        pinDots[3] = findViewById(R.id.pin_dot_4);
        pinDots[4] = findViewById(R.id.pin_dot_5);
        pinDots[5] = findViewById(R.id.pin_dot_6);

        btnNext.setOnClickListener(v -> validateAndNext());

        setupKeyListeners();
    }

    private void validateAndNext() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        boolean isValid = true;

        if (name.isEmpty()) {
            tilName.setError("Nom complet requis");
            isValid = false;
        } else {
            tilName.setError(null);
        }

        if (email.isEmpty()) {
            tilEmail.setError("Email requis");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email invalide");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        if (phone.isEmpty()) {
            tilPhone.setError("Téléphone requis");
            isValid = false;
        } else {
            tilPhone.setError(null);
        }

        if (isValid) {
            layoutForm.setVisibility(View.GONE);
            layoutPin.setVisibility(View.VISIBLE);
            resetPinEntry();
        }
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

            if (digit != null) onDigitPressed(digit);
        };

        for (int id : keyIds) {
            View keyView = findViewById(id);
            if (keyView != null) {
                keyView.setOnClickListener(keyListener);
                TextView tvDigit = keyView.findViewById(R.id.tv_key_digit);
                if (tvDigit != null) {
                    String digit = id == R.id.key_0 ? "0" :
                            String.valueOf(id - R.id.key_1 + 1);
                    tvDigit.setText(digit);
                }
            }
        }

        findViewById(R.id.key_delete).setOnClickListener(v -> onDeletePressed());
    }

    private void onDigitPressed(String digit) {
        if (pinBuffer.length() >= 6) return;
        pinBuffer.append(digit);
        updateDots();

        if (pinBuffer.length() == 6) {
            if (!isConfirming) {
                firstPin = pinBuffer.toString();
                isConfirming = true;
                pinBuffer.setLength(0);
                updateDots();
                tvPinTitle.setText("Confirmez le code PIN");
                tvPinHint.setText("Saisissez à nouveau les 6 chiffres");
            } else {
                String secondPin = pinBuffer.toString();
                if (firstPin != null && firstPin.equals(secondPin)) {
                    if (isChangePinOnly) {
                        saveLocally(secondPin);
                    } else {
                        registerWithApi(secondPin);
                    }
                } else {
                    tvPinError.setVisibility(View.VISIBLE);
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    if (vibrator != null && vibrator.hasVibrator()) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                    resetPinEntry();
                    tvPinError.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void onDeletePressed() {
        if (pinBuffer.length() == 0) return;
        pinBuffer.deleteCharAt(pinBuffer.length() - 1);
        updateDots();
    }

    private void updateDots() {
        for (int i = 0; i < 6; i++) {
            pinDots[i].setBackgroundResource(
                    i < pinBuffer.length() ? R.drawable.bg_pin_dot_filled : R.drawable.bg_pin_dot_empty);
        }
    }

    private void resetPinEntry() {
        firstPin = null;
        isConfirming = false;
        pinBuffer.setLength(0);
        updateDots();
        tvPinTitle.setText("Choisissez un code PIN");
        tvPinHint.setText("6 chiffres");
        tvPinError.setVisibility(View.GONE);
    }

    private void registerWithApi(String pin) {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        Toast.makeText(this, "Création du compte...", Toast.LENGTH_SHORT).show();

        executor.execute(() -> {
            try {
                Response<AuthResponse> response = api.register(
                        new RegisterRequest(name, email, phone, pin)).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    runOnUiThread(() -> {
                        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
                        prefs.edit()
                                .putString("auth_token", token)
                                .putString("user_name", name)
                                .putString("user_email", email)
                                .putString("user_phone", phone)
                                .putString("user_pin", pin)
                                .apply();
                        Toast.makeText(this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        tvPinError.setVisibility(View.VISIBLE);
                        tvPinError.setText("Erreur lors de l'inscription");
                        resetPinEntry();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    saveLocally(pin);
                });
            }
        });
    }

    private void saveLocally(String pin) {
        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (!isChangePinOnly) {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            editor.putString("user_name", name);
            editor.putString("user_email", email);
            editor.putString("user_phone", phone);
        }

        editor.putString("user_pin", pin);
        editor.apply();

        if (isChangePinOnly) {
            Toast.makeText(this, "Code PIN modifié avec succès !", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}
