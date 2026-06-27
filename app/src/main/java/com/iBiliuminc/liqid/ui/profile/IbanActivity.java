package com.iBiliuminc.liqid.ui.profile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.BaseActivity;

import androidx.core.content.ContextCompat;

public class IbanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iban);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        String iban = "FR76 3000 6000 0123 4567 8901 234";
        String bic = "LIQIDFR2P";

        findViewById(R.id.btn_copy).setOnClickListener(v -> {
            ClipboardManager clipboard = ContextCompat.getSystemService(this, ClipboardManager.class);
            if (clipboard != null) {
                clipboard.setPrimaryClip(ClipData.newPlainText("IBAN", iban));
                Toast.makeText(this, "IBAN copié", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_copy_bic).setOnClickListener(v -> {
            ClipboardManager clipboard = ContextCompat.getSystemService(this, ClipboardManager.class);
            if (clipboard != null) {
                clipboard.setPrimaryClip(ClipData.newPlainText("BIC", bic));
                Toast.makeText(this, "BIC copié", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
