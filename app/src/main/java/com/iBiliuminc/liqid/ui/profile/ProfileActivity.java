package com.iBiliuminc.liqid.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.splash.SplashActivity;

import java.io.File;
import java.io.FileOutputStream;

public class ProfileActivity extends BaseActivity {

    private SharedPreferences prefs;
    private TextView tvInitials;

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        savePhotoUri(uri.toString());
                        tvInitials.setVisibility(android.view.View.GONE);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        loadUserData();

        findViewById(R.id.btn_edit_photo).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

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

        tvInitials = findViewById(R.id.tv_profile_initials);
        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvPhone = findViewById(R.id.tv_profile_phone);

        tvName.setText(name);
        tvPhone.setText(phone);

        String photoUri = prefs.getString("photo_uri", null);
        if (photoUri != null) {
            tvInitials.setVisibility(android.view.View.GONE);
        } else if (!name.isEmpty()) {
            tvInitials.setVisibility(android.view.View.VISIBLE);
            tvInitials.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }
    }

    private void savePhotoUri(String uri) {
        prefs.edit().putString("photo_uri", uri).apply();
    }
}
