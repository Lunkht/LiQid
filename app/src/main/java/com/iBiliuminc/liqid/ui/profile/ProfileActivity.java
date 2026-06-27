package com.iBiliuminc.liqid.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.splash.SplashActivity;

public class ProfileActivity extends BaseActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        loadUserData();

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Déconnexion")
                    .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        prefs.edit().clear().apply();
                        Intent intent = new Intent(this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    private void loadUserData() {
        String name = prefs.getString("user_name", "Lunkht");
        String phone = prefs.getString("user_phone", "+224 6X XX XX XX");

        TextView tvInitials = findViewById(R.id.tv_profile_initials);
        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvPhone = findViewById(R.id.tv_profile_phone);

        tvName.setText(name);
        tvPhone.setText(phone);

        if (!name.isEmpty()) {
            tvInitials.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }
    }
}
