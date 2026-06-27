package com.iBiliuminc.liqid.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.BaseActivity;

public class PersonalInfoActivity extends BaseActivity {

    private TextInputEditText etName, etEmail, etPhone;
    private TextInputLayout tilName, tilEmail, tilPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPhone = findViewById(R.id.til_phone);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);

        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
        etName.setText(prefs.getString("user_name", ""));
        etEmail.setText(prefs.getString("user_email", ""));
        etPhone.setText(prefs.getString("user_phone", ""));

        MaterialButton btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> saveInfo());
    }

    private void saveInfo() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        boolean valid = true;
        if (name.isEmpty()) { tilName.setError("Requis"); valid = false; }
        else { tilName.setError(null); }

        if (email.isEmpty()) { tilEmail.setError("Requis"); valid = false; }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { tilEmail.setError("Invalide"); valid = false; }
        else { tilEmail.setError(null); }

        if (phone.isEmpty()) { tilPhone.setError("Requis"); valid = false; }
        else { tilPhone.setError(null); }

        if (!valid) return;

        SharedPreferences prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("user_name", name)
                .putString("user_email", email)
                .putString("user_phone", phone)
                .apply();
        Toast.makeText(this, "Informations mises à jour", Toast.LENGTH_SHORT).show();
        finish();
    }
}
