package com.iBiliuminc.liqid.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.home.HomeActivity;

public class TransactionSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_success);

        @SuppressWarnings("deprecation")
        Transaction tx = (Transaction) getIntent().getSerializableExtra("transaction");

        TextView tvSubtitle = findViewById(R.id.tv_success_subtitle);
        TextView tvAmount = findViewById(R.id.tv_summary_amount);
        TextView tvRecipient = findViewById(R.id.tv_summary_recipient);
        TextView tvRef = findViewById(R.id.tv_summary_ref);
        TextView tvDate = findViewById(R.id.tv_summary_date);

        if (tx != null) {
            tvSubtitle.setText(String.format("%.2f \u20AC ont \u00e9t\u00e9 envoy\u00e9s\n\u00e0 %s",
                    tx.getAmount(), tx.getMerchantName()));
            tvAmount.setText(String.format("%.2f \u20AC", tx.getAmount()));
            tvRecipient.setText(tx.getMerchantName());
            tvRef.setText(tx.getReference());
            tvDate.setText(tx.getDate());
        }

        findViewById(R.id.btn_share_receipt).setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Virement Liqid - " +
                            (tx != null ? tx.getReference() : "") +
                            " : " +
                            (tx != null ? String.format("%.2f", tx.getAmount()) : "") + " \u20AC");
            startActivity(Intent.createChooser(shareIntent, "Partager le re\u00e7u"));
        });

        findViewById(R.id.btn_go_home).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
