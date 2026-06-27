package com.vulsoftinc.liqid.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vulsoftinc.liqid.R;
import com.vulsoftinc.liqid.domain.model.Transaction;
import com.vulsoftinc.liqid.util.ThemeUtils;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<Transaction> transactions;
    private final OnTransactionClickListener listener;

    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction transaction);
    }

    public TransactionAdapter(List<Transaction> transactions,
                              OnTransactionClickListener listener) {
        this.transactions = transactions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction tx = transactions.get(position);
        holder.bind(tx, listener);

        boolean isLast = position == transactions.size() - 1;
        holder.divider.setVisibility(isLast ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivMerchantIcon;
        final TextView tvMerchantName;
        final TextView tvDate;
        final TextView tvAmount;
        final TextView tvStatus;
        final View pendingIndicator;
        final View divider;

        ViewHolder(View itemView) {
            super(itemView);
            ivMerchantIcon = itemView.findViewById(R.id.iv_merchant_icon);
            tvMerchantName = itemView.findViewById(R.id.tv_merchant_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvStatus = itemView.findViewById(R.id.tv_status);
            pendingIndicator = itemView.findViewById(R.id.pending_indicator);
            divider = itemView.findViewById(R.id.divider);
        }

        void bind(Transaction tx, OnTransactionClickListener listener) {
            tvMerchantName.setText(tx.getMerchantName());
            tvDate.setText(tx.getDate());

            String sign = tx.getType() == Transaction.Type.DEBIT ? "- " : "+ ";
            tvAmount.setText(sign + String.format("%.2f", tx.getAmount()) + " \u20AC");

            if (tx.getType() == Transaction.Type.DEBIT) {
                tvAmount.setTextColor(ThemeUtils.resolveColor(itemView.getContext(), R.attr.liqid_error));
            } else {
                tvAmount.setTextColor(ThemeUtils.resolveColor(itemView.getContext(), R.attr.liqid_success));
            }

            if (tx.getStatus() == Transaction.Status.PENDING) {
                tvStatus.setVisibility(View.VISIBLE);
                pendingIndicator.setVisibility(View.VISIBLE);
            } else {
                tvStatus.setVisibility(View.GONE);
                pendingIndicator.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onTransactionClick(tx));
        }
    }
}
