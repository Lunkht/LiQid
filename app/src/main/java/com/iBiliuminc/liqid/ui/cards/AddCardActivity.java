package com.iBiliuminc.liqid.ui.cards;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.core.di.AppContainer;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        TextInputEditText etCardholder = findViewById(R.id.et_cardholder);
        AutoCompleteTextView tvPlan = findViewById(R.id.tv_plan);
        MaterialButton btnSubmit = findViewById(R.id.btn_submit_card);

        tvPlan.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"STANDARD", "PREMIUM", "METAL"}));
        tvPlan.setText("STANDARD", false);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        AppContainer container = AppContainer.getInstance(this);
        ExecutorService executor = container.getExecutorService();

        btnSubmit.setOnClickListener(v -> {
            String name = etCardholder.getText().toString().trim();
            String plan = tvPlan.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Entrez le nom du titulaire", Toast.LENGTH_SHORT).show();
                return;
            }

            String cardId = UUID.randomUUID().toString().substring(0, 8);
            String cardNumber = String.valueOf((int)(Math.random() * 9000) + 1000);
            String expiry = "12/" + (java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) + 3);
            String scheme = plan.equals("METAL") ? "mastercard" : "visa";

            String finalPlan = plan;
            executor.execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {}
                runOnUiThread(() -> {
                    Toast.makeText(this, "Carte " + finalPlan + " ajoutée (" + cardNumber + ")", Toast.LENGTH_LONG).show();
                    finish();
                });
            });
        });
    }
}
