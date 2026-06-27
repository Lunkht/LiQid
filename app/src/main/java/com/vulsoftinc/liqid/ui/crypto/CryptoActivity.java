package com.vulsoftinc.liqid.ui.crypto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vulsoftinc.liqid.R;
import com.vulsoftinc.liqid.core.di.AppContainer;
import com.vulsoftinc.liqid.domain.model.CryptoAsset;
import com.vulsoftinc.liqid.ui.BaseActivity;
import com.vulsoftinc.liqid.ui.BottomNavHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CryptoActivity extends BaseActivity {

    private View loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto);

        AppContainer container = AppContainer.getInstance(this);
        ExecutorService executor = container.getExecutorService();

        loadingOverlay = findViewById(R.id.loading_overlay);
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.VISIBLE);

        BottomNavHelper.setup(this, R.id.nav_crypto);

        TextView tvPortfolioValue = findViewById(R.id.tv_portfolio_value);
        TextView tvPortfolioChange = findViewById(R.id.tv_portfolio_change);
        RecyclerView rvAssets = findViewById(R.id.rv_crypto_assets);

        executor.execute(() -> {
            List<CryptoAsset> assets = container.getRepository().getCryptoAssets();

            runOnUiThread(() -> {
                double totalValue = 0;
                for (CryptoAsset asset : assets) {
                    totalValue += asset.getValue();
                }
                tvPortfolioValue.setText(String.format("%.2f", totalValue) + " \u20AC");

                double totalChange = 0;
                for (CryptoAsset asset : assets) {
                    totalChange += asset.getChange24h();
                }
                String changeSign = totalChange >= 0 ? "+" : "";
                tvPortfolioChange.setText(changeSign + String.format("%.2f", totalChange) + "% aujourd'hui");
                tvPortfolioChange.setTextColor(ContextCompat.getColor(this,
                        totalChange >= 0 ? R.color.success : R.color.error));

                rvAssets.setLayoutManager(new LinearLayoutManager(this));
                rvAssets.setAdapter(new CryptoAdapter(assets));

                if (loadingOverlay != null) loadingOverlay.setVisibility(View.GONE);
            });
        });
    }

    private static class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

        private final List<CryptoAsset> assets;

        CryptoAdapter(List<CryptoAsset> assets) {
            this.assets = assets;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_crypto_asset, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CryptoAsset asset = assets.get(position);
            holder.tvName.setText(asset.getName());
            holder.tvPrice.setText(String.format("%.2f", asset.getPrice()) + " \u20AC");

            String changeSign = asset.getChange24h() >= 0 ? "+" : "";
            holder.tvChange.setText(changeSign + String.format("%.1f", asset.getChange24h()) + "%");
            holder.tvChange.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                    asset.getChange24h() >= 0 ? R.color.success : R.color.error));

            holder.tvValue.setText(String.format("%.2f", asset.getValue()) + " \u20AC");
            holder.tvAmount.setText(String.format("%.4f", asset.getAmount()) + " " + asset.getSymbol());
        }

        @Override
        public int getItemCount() {
            return assets.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvName;
            final TextView tvPrice;
            final TextView tvChange;
            final TextView tvValue;
            final TextView tvAmount;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_crypto_name);
                tvPrice = itemView.findViewById(R.id.tv_crypto_price);
                tvChange = itemView.findViewById(R.id.tv_crypto_change);
                tvValue = itemView.findViewById(R.id.tv_crypto_value);
                tvAmount = itemView.findViewById(R.id.tv_crypto_amount);
            }
        }
    }
}
