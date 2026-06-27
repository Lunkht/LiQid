package com.vulsoftinc.liqid.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vulsoftinc.liqid.R;
import com.vulsoftinc.liqid.ui.BaseActivity;
import com.vulsoftinc.liqid.ui.home.HomeActivity;

public class OtpVerificationActivity extends BaseActivity {

    private String phoneNumber;
    private final StringBuilder otpBuffer = new StringBuilder(6);
    private TextView[] otpViews = new TextView[6];
    private TextView tvSubtitle;
    private TextView btnResend;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        phoneNumber = getIntent().getStringExtra("phone_number");
        
        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
        
        tvSubtitle = findViewById(R.id.tv_otp_subtitle);
        if (phoneNumber != null) {
            tvSubtitle.setText("Entrez le code à 6 chiffres envoyé au " + phoneNumber);
        }

        btnResend = findViewById(R.id.btn_resend);
        startResendTimer();

        otpViews[0] = findViewById(R.id.otp_1);
        otpViews[1] = findViewById(R.id.otp_2);
        otpViews[2] = findViewById(R.id.otp_3);
        otpViews[3] = findViewById(R.id.otp_4);
        otpViews[4] = findViewById(R.id.otp_5);
        otpViews[5] = findViewById(R.id.otp_6);

        setupKeyListeners();
        
        btnResend.setOnClickListener(v -> {
            if (btnResend.isEnabled()) {
                Toast.makeText(this, "Code renvoyé", Toast.LENGTH_SHORT).show();
                startResendTimer();
            }
        });
    }

    private void startResendTimer() {
        btnResend.setEnabled(false);
        btnResend.setTextColor(getResources().getColor(R.color.text_secondary));
        
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnResend.setText("Renvoyer le code (" + (millisUntilFinished / 1000) + "s)");
            }

            @Override
            public void onFinish() {
                btnResend.setText("Renvoyer le code");
                btnResend.setEnabled(true);
                btnResend.setTextColor(getResources().getColor(R.color.accent_orange));
            }
        }.start();
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

        findViewById(R.id.btn_skip).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }

    private void onDigitPressed(String digit) {
        if (otpBuffer.length() >= 6) return;
        otpBuffer.append(digit);
        updateOtpViews();

        if (otpBuffer.length() == 6) {
            // Simulation de validation
            validateOtp();
        }
    }

    private void onDeletePressed() {
        if (otpBuffer.length() == 0) return;
        otpBuffer.deleteCharAt(otpBuffer.length() - 1);
        updateOtpViews();
    }

    private void updateOtpViews() {
        for (int i = 0; i < 6; i++) {
            if (i < otpBuffer.length()) {
                otpViews[i].setText(String.valueOf(otpBuffer.charAt(i)));
                otpViews[i].setBackgroundResource(R.drawable.bg_otp_box); // Could add a focused style
            } else {
                otpViews[i].setText("");
            }
        }
    }

    private void validateOtp() {
        // Pour cet exemple, n'importe quel code à 6 chiffres fonctionne
        Toast.makeText(this, "Numéro vérifié !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
