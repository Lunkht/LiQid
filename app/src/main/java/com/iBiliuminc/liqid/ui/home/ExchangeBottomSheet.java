package com.iBiliuminc.liqid.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.util.CurrencyHelper;

import java.util.LinkedHashMap;
import java.util.Locale;

public class ExchangeBottomSheet extends BottomSheetDialogFragment {

    private static final LinkedHashMap<String, Double> RATES = new LinkedHashMap<>();
    static {
        RATES.put("EUR", 1.0);
        RATES.put("USD", 1.083);
        RATES.put("GBP", 0.834);
        RATES.put("CHF", 0.976);
        RATES.put("JPY", 168.5);
        RATES.put("CAD", 1.482);
        RATES.put("AUD", 1.638);
    }

    private String fromCurrency = "EUR";
    private String toCurrency = "USD";
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view;

        updateLabels();
        updateRate();

        EditText etAmount = view.findViewById(R.id.et_amount_from);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateConvertedAmount();
            }
        });

        view.findViewById(R.id.btn_currency_from).setOnClickListener(v ->
                showCurrencyPicker(true));
        view.findViewById(R.id.btn_currency_to).setOnClickListener(v ->
                showCurrencyPicker(false));
        view.findViewById(R.id.btn_swap).setOnClickListener(v -> {
            String tmp = fromCurrency;
            fromCurrency = toCurrency;
            toCurrency = tmp;
            updateLabels();
            updateRate();
            updateConvertedAmount();
        });
        view.findViewById(R.id.tv_rate_refresh).setOnClickListener(v ->
                Toast.makeText(getContext(), "Taux mis à jour", Toast.LENGTH_SHORT).show());
        view.findViewById(R.id.btn_confirm_exchange).setOnClickListener(v -> {
            EditText et = view.findViewById(R.id.et_amount_from);
            String text = et.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(getContext(), "Entrez un montant", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(text.replace(",", "."));
            if (amount <= 0) {
                Toast.makeText(getContext(), "Montant invalide", Toast.LENGTH_SHORT).show();
                return;
            }
            double converted = convert(amount);
            String sym = CurrencyHelper.symbolFor(toCurrency);
            Toast.makeText(getContext(), String.format(Locale.FRANCE,
                    "Échange : %.2f %s → %.2f %s", amount, fromCurrency, converted, toCurrency),
                    Toast.LENGTH_LONG).show();
            dismiss();
        });
    }

    private void updateLabels() {
        ((TextView) root.findViewById(R.id.tv_currency_from)).setText(fromCurrency);
        ((TextView) root.findViewById(R.id.tv_currency_to)).setText(toCurrency);
        CurrencyHelper.CurrencyInfo fromInfo = CurrencyHelper.getAll().get(fromCurrency);
        CurrencyHelper.CurrencyInfo toInfo = CurrencyHelper.getAll().get(toCurrency);
        root.findViewById(R.id.tv_balance_from).setVisibility(View.GONE);
        root.findViewById(R.id.tv_balance_to).setVisibility(View.GONE);
    }

    private void updateRate() {
        double rate = getRate(fromCurrency, toCurrency);
        String sym = CurrencyHelper.symbolFor(toCurrency);
        ((TextView) root.findViewById(R.id.tv_exchange_rate))
                .setText(String.format(Locale.FRANCE, "1 %s = %.4f %s (%s)",
                        fromCurrency, rate, toCurrency, "Taux en temps réel"));
        ((TextView) root.findViewById(R.id.tv_exchange_fee))
                .setText("Gratuit (plan Standard)");
    }

    private void updateConvertedAmount() {
        EditText et = root.findViewById(R.id.et_amount_from);
        TextView tvTo = root.findViewById(R.id.tv_amount_to);
        String text = et.getText().toString();
        if (text.isEmpty()) {
            tvTo.setText("");
            return;
        }
        try {
            double amount = Double.parseDouble(text.replace(",", "."));
            double converted = convert(amount);
            tvTo.setText(String.format(Locale.FRANCE, "%.2f", converted));
        } catch (NumberFormatException ignored) {}
    }

    private double convert(double amount) {
        double inEur = amount / getRate(fromCurrency, "EUR");
        return inEur * getRate("EUR", toCurrency);
    }

    private double getRate(String from, String to) {
        Double fromRate = RATES.get(from);
        Double toRate = RATES.get(to);
        if (fromRate == null || toRate == null) return 1.0;
        return toRate / fromRate;
    }

    private void showCurrencyPicker(boolean isFrom) {
        String[] labels = CurrencyHelper.getLabels();
        String[] codes = CurrencyHelper.getCodes();
        String current = isFrom ? fromCurrency : toCurrency;
        int checked = java.util.Arrays.asList(codes).indexOf(current);

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(isFrom ? "Devise d'origine" : "Devise de destination")
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    if (isFrom) fromCurrency = codes[which];
                    else toCurrency = codes[which];
                    updateLabels();
                    updateRate();
                    updateConvertedAmount();
                    dialog.dismiss();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
