package com.iBiliuminc.liqid.ui.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.core.di.AppContainer;
import com.iBiliuminc.liqid.domain.model.Account;
import com.iBiliuminc.liqid.domain.model.Transaction;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.BottomNavHelper;
import com.iBiliuminc.liqid.ui.profile.ProfileActivity;
import com.iBiliuminc.liqid.ui.transfer.TransferActivity;

import java.util.List;

public class HomeActivity extends BaseActivity {

    private HomeViewModel viewModel;
    private TransactionAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvBalance;
    private TextView tvError;
    private View loadingOverlay;
    private View emptyTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppContainer container = AppContainer.getInstance(this);

        HomeViewModelFactory factory = new HomeViewModelFactory(
                container.getGetBalanceUseCase(),
                container.getGetTransactionsUseCase(),
                container.getExecutorService()
        );
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);

        tvBalance = findViewById(R.id.tv_balance);
        RecyclerView rvTransactions = findViewById(R.id.rv_transactions);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        tvError = findViewById(R.id.tv_error);
        loadingOverlay = findViewById(R.id.loading_overlay);
        emptyTransactions = findViewById(R.id.layout_empty_transactions);

        rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(java.util.Collections.emptyList(),
                transaction -> {
                    Intent intent = new Intent(this, TransactionDetailActivity.class);
                    intent.putExtra("transaction", transaction);
                    startActivity(intent);
                });
        rvTransactions.setAdapter(adapter);

        setupBottomNav();
        setupQuickActions();
        setupSwipeRefresh();
        observeData();
    }

    private void setupBottomNav() {
        BottomNavHelper.setup(this, R.id.nav_home);
    }

    private void setupQuickActions() {
        findViewById(R.id.btn_send).setOnClickListener(v ->
                startActivity(new Intent(this, TransferActivity.class)));
        findViewById(R.id.btn_receive).setOnClickListener(v ->
                Toast.makeText(this, "Recevoir de l'argent", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btn_exchange).setOnClickListener(v ->
                Toast.makeText(this, "Échanger des devises", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btn_top_up).setOnClickListener(v ->
                Toast.makeText(this, "Recharger le compte", Toast.LENGTH_SHORT).show());

        setQuickAction(R.id.btn_send, R.drawable.ic_send, R.string.action_send);
        setQuickAction(R.id.btn_receive, R.drawable.ic_receive, R.string.action_receive);
        setQuickAction(R.id.btn_exchange, R.drawable.ic_exchange, R.string.action_exchange);
        setQuickAction(R.id.btn_top_up, R.drawable.ic_topup, R.string.action_top_up);
    }

    private void setQuickAction(int actionId, int iconRes, int labelRes) {
        View action = findViewById(actionId);
        if (action == null) return;
        ImageView icon = action.findViewById(R.id.iv_quick_action_icon);
        if (icon != null) icon.setImageResource(iconRes);
        TextView label = action.findViewById(R.id.tv_quick_action_label);
        if (label != null) label.setText(labelRes);
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.loadData();
            swipeRefresh.setRefreshing(false);
        });
    }

    private void observeData() {
        android.content.SharedPreferences prefs = getSharedPreferences("liqid_prefs", MODE_PRIVATE);
        String name = prefs.getString("user_name", "Lunkht");
        TextView tvUsername = findViewById(R.id.tv_username);
        if (tvUsername != null) tvUsername.setText(name);

        boolean isBalanceHidden = prefs.getBoolean("hide_balance_enabled", false);

        viewModel.getAccount().observe(this, account -> {
            if (account != null) {
                if (isBalanceHidden) {
                    tvBalance.setText("•••• €");
                } else {
                    tvBalance.setText(String.format("%02d", (int)account.getBalance()) + " \u20AC");
                }
            }
        });

        viewModel.getTransactions().observe(this, transactions -> {
            if (transactions != null) {
                adapter = new TransactionAdapter(transactions,
                        transaction -> {
                            Intent intent = new Intent(this, TransactionDetailActivity.class);
                            intent.putExtra("transaction", transaction);
                            startActivity(intent);
                        });
                RecyclerView rv = findViewById(R.id.rv_transactions);
                rv.setAdapter(adapter);

                boolean empty = transactions.isEmpty();
                emptyTransactions.setVisibility(empty ? View.VISIBLE : View.GONE);
                rv.setVisibility(empty ? View.GONE : View.VISIBLE);
            }
        });

        viewModel.getLoading().observe(this, loading -> {
            loadingOverlay.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
        });
    }
}
