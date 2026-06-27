package com.iBiliuminc.liqid.ui.cards;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.core.di.AppContainer;
import com.iBiliuminc.liqid.domain.model.BankCard;
import com.iBiliuminc.liqid.domain.repository.BankRepository;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.BottomNavHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CardsActivity extends BaseActivity {

    private List<BankCard> cards = new ArrayList<>();
    private int currentCardIndex = 0;
    private CardPagerAdapter pagerAdapter;
    private ViewPager2 viewPager;
    private View loadingOverlay;
    private ExecutorService executor;
    private BankRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        AppContainer container = AppContainer.getInstance(this);
        executor = container.getExecutorService();
        repository = container.getRepository();

        loadingOverlay = findViewById(R.id.loading_overlay);
        viewPager = findViewById(R.id.viewpager_cards);
        TabLayout tabIndicator = findViewById(R.id.tab_indicator);

        if (loadingOverlay != null) loadingOverlay.setVisibility(View.VISIBLE);

        executor.execute(() -> {
            List<BankCard> loaded = container.getRepository().getCards();
            runOnUiThread(() -> {
                cards = loaded;
                pagerAdapter = new CardPagerAdapter(cards);
                viewPager.setAdapter(pagerAdapter);

                new TabLayoutMediator(tabIndicator, viewPager,
                        (tab, position) -> tab.setCustomView(R.layout.item_tab_dot)).attach();

                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        currentCardIndex = position;
                        updateControls(cards.get(position));
                    }
                });

                setupControlLayouts();

                if (!cards.isEmpty()) {
                    updateControls(cards.get(0));
                }

                if (loadingOverlay != null) loadingOverlay.setVisibility(View.GONE);
            });
        });

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
        BottomNavHelper.setup(this, R.id.nav_cards);

        findViewById(R.id.btn_add_card).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCardActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_show_card_number).setOnClickListener(v -> {
            if (!cards.isEmpty()) {
                BankCard card = cards.get(currentCardIndex);
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Détails de la carte")
                        .setMessage("Numéro : " + card.getCardNumber() + "\n" +
                                "Titulaire : " + card.getCardholderName() + "\n" +
                                "Expiration : " + card.getExpiryDate() + "\n" +
                                "CVV : •••")
                        .setPositiveButton("Fermer", null)
                        .show();
            }
        });

        findViewById(R.id.btn_spending_limit).setOnClickListener(v ->
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Limite de dépenses")
                        .setMessage("Limite mensuelle actuelle : 10 000 \u20AC\nContactez le service client pour la modifier.")
                        .setPositiveButton("Fermer", null)
                        .show());
    }

    private void updateControls(BankCard card) {
        setSwitchState(R.id.control_freeze, card.isFrozen());
        setSwitchState(R.id.control_online, card.isOnlineEnabled());
        setSwitchState(R.id.control_contactless, card.isContactlessEnabled());
        setSwitchState(R.id.control_atm, card.isAtmEnabled());
    }

    private void setSwitchState(int controlId, boolean checked) {
        View control = findViewById(controlId);
        if (control != null) {
            MaterialSwitch sw = control.findViewById(R.id.switch_control);
            if (sw != null) sw.setChecked(checked);
        }
    }

    private void setupControlLayouts() {
        setControl(R.id.control_freeze, R.drawable.ic_freeze,
                "Geler la carte", "Bloquer tous les paiements", (card, newState) -> {
                    card.setFrozen(newState);
                    pagerAdapter.notifyItemChanged(currentCardIndex);
                    persistCard(card);
                });

        setControl(R.id.control_online, R.drawable.ic_card,
                "Paiements en ligne", "Achats sur internet", (card, newState) -> {
                    card.setOnlineEnabled(newState);
                    persistCard(card);
                });

        setControl(R.id.control_contactless, R.drawable.ic_chip,
                "Sans contact", "Paiements NFC", (card, newState) -> {
                    card.setContactlessEnabled(newState);
                    persistCard(card);
                });

        setControl(R.id.control_atm, R.drawable.ic_atm,
                "Retrait DAB", "Distributeurs automatiques", (card, newState) -> {
                    card.setAtmEnabled(newState);
                    persistCard(card);
                });
    }

    private void persistCard(BankCard card) {
        executor.execute(() -> {
            repository.updateCard(card.getId(), card.isFrozen(),
                    card.isOnlineEnabled(), card.isContactlessEnabled(), card.isAtmEnabled());
        });
    }

    private void setControl(int controlId, int iconRes, String label, String sublabel, OnControlChangedListener listener) {
        View control = findViewById(controlId);
        if (control == null) return;

        ((ImageView) control.findViewById(R.id.iv_control_icon)).setImageResource(iconRes);
        ((TextView) control.findViewById(R.id.tv_control_label)).setText(label);
        TextView tvSub = control.findViewById(R.id.tv_control_sublabel);
        tvSub.setText(sublabel);
        tvSub.setVisibility(View.VISIBLE);

        MaterialSwitch sw = control.findViewById(R.id.switch_control);
        sw.setClickable(false);
        sw.setFocusable(false);

        control.setOnClickListener(v -> {
            boolean newState = !sw.isChecked();
            sw.setChecked(newState);
            listener.onChanged(cards.get(currentCardIndex), newState);
        });
    }

    interface OnControlChangedListener {
        void onChanged(BankCard card, boolean newState);
    }

    private static class CardPagerAdapter extends RecyclerView.Adapter<CardPagerAdapter.ViewHolder> {
        private final List<BankCard> cards;

        CardPagerAdapter(List<BankCard> cards) { this.cards = cards; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BankCard card = cards.get(position);
            holder.tvCardPlan.setText(card.getPlan());
            holder.tvCardNumber.setText(card.getCardNumber());
            holder.tvCardholderName.setText(card.getCardholderName());
            holder.tvCardExpiry.setText(card.getExpiryDate());

            boolean frozen = card.isFrozen();
            holder.overlayFrozen.setVisibility(frozen ? View.VISIBLE : View.GONE);
            holder.frozenIndicator.setVisibility(frozen ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() { return cards.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvCardPlan, tvCardNumber, tvCardholderName, tvCardExpiry;
            final View overlayFrozen, frozenIndicator;
            ViewHolder(View itemView) {
                super(itemView);
                tvCardPlan = itemView.findViewById(R.id.tv_card_plan);
                tvCardNumber = itemView.findViewById(R.id.tv_card_number);
                tvCardholderName = itemView.findViewById(R.id.tv_cardholder_name);
                tvCardExpiry = itemView.findViewById(R.id.tv_card_expiry);
                overlayFrozen = itemView.findViewById(R.id.overlay_frozen);
                frozenIndicator = itemView.findViewById(R.id.layout_frozen_indicator);
            }
        }
    }
}
