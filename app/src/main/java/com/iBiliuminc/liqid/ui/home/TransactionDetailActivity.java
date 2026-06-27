package com.iBiliuminc.liqid.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.core.di.AppContainer;
import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.ui.BaseActivity;

public class TransactionDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        AppContainer container = AppContainer.getInstance(this);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        @SuppressWarnings("deprecation")
        Transaction tx = (Transaction) getIntent().getSerializableExtra("transaction");

        if (tx == null) {
            finish();
            return;
        }

        ImageView ivIcon = findViewById(R.id.iv_detail_icon);
        TextView tvAmount = findViewById(R.id.tv_detail_amount);
        TextView tvOriginalAmount = findViewById(R.id.tv_detail_original_amount);
        TextView tvMerchant = findViewById(R.id.tv_detail_merchant);
        TextView tvStatus = findViewById(R.id.tv_detail_status);

        String sign = tx.getType() == Transaction.Type.DEBIT ? "- " : "+ ";
        tvAmount.setText(sign + String.format("%.2f", tx.getAmount()) + " \u20AC");

        if (tx.getType() == Transaction.Type.DEBIT) {
            tvAmount.setTextColor(ContextCompat.getColor(this, R.color.error));
        } else {
            tvAmount.setTextColor(ContextCompat.getColor(this, R.color.success));
        }

        tvMerchant.setText(tx.getMerchantName());

        if (tx.getStatus() == Transaction.Status.COMPLETED) {
            tvStatus.setText(R.string.label_completed);
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.success));
            tvStatus.setBackgroundResource(R.drawable.bg_badge_success);
        } else if (tx.getStatus() == Transaction.Status.PENDING) {
            tvStatus.setText(R.string.label_pending);
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.warning));
            tvStatus.setBackgroundResource(R.drawable.bg_badge_warning);
        } else {
            tvStatus.setText(R.string.label_failed);
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.error));
        }

        setDetailRowText(R.id.row_date, "Date", tx.getDate());
        setDetailRowText(R.id.row_category, "Catégorie", tx.getCategory());
        setDetailRowText(R.id.row_reference, "Référence", tx.getReference());
        setDetailRowText(R.id.row_account, "Compte", "Compte courant");
    }

    private void setDetailRowText(int rowId, String label, String value) {
        View row = findViewById(rowId);
        if (row != null) {
            TextView tvLabel = row.findViewById(R.id.tv_row_label);
            TextView tvValue = row.findViewById(R.id.tv_row_value);
            if (tvLabel != null) tvLabel.setText(label);
            if (tvValue != null) tvValue.setText(value);
        }
    }
}
