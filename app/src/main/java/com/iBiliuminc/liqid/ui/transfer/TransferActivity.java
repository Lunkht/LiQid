package com.iBiliuminc.liqid.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.core.di.AppContainer;
import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.BottomNavHelper;

public class TransferActivity extends BaseActivity {

    private TransferViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        AppContainer container = AppContainer.getInstance(this);

        BottomNavHelper.setup(this, R.id.nav_transfer);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
                if (modelClass.isAssignableFrom(TransferViewModel.class)) {
                    return (T) new TransferViewModel(container.getSendMoneyUseCase(), container.getExecutorService());
                }
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }).get(TransferViewModel.class);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        TextInputEditText etIban = findViewById(R.id.et_iban);
        TextInputEditText etAmount = findViewById(R.id.et_amount);
        TextInputEditText etDescription = findViewById(R.id.et_description);
        MaterialButton btnConfirm = findViewById(R.id.btn_confirm_transfer);
        View loadingOverlay = findViewById(R.id.loading_overlay);
        TextView tvAvailableBalance = findViewById(R.id.tv_available_balance);

        tvAvailableBalance.setText("00 \u20AC");

        btnConfirm.setOnClickListener(v -> {
            String iban = etIban.getText() != null ? etIban.getText().toString() : "";
            String amount = etAmount.getText() != null ? etAmount.getText().toString() : "";
            String description = etDescription.getText() != null ? etDescription.getText().toString() : "";
            viewModel.executeTransfer(iban, amount, description);
        });

        viewModel.getLoading().observe(this, loading -> {
            loadingOverlay.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
            btnConfirm.setEnabled(!Boolean.TRUE.equals(loading));
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                TextView tvError = findViewById(R.id.tv_error);
                if (tvError != null) {
                    tvError.setText(error);
                    tvError.setVisibility(View.VISIBLE);
                    tvError.postDelayed(() -> tvError.setVisibility(View.GONE), 3000);
                }
            }
        });

        viewModel.getResult().observe(this, transaction -> {
            if (transaction != null) {
                Intent intent = new Intent(this, TransactionSuccessActivity.class);
                intent.putExtra("transaction", transaction);
                startActivity(intent);
            }
        });
    }
}
