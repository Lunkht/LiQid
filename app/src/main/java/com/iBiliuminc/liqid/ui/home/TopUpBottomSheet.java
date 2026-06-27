package com.iBiliuminc.liqid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.iBiliuminc.liqid.R;

public class TopUpBottomSheet extends BottomSheetDialogFragment {

    private boolean isCardSelected = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topup_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateRadio();

        view.findViewById(R.id.method_card).setOnClickListener(v -> {
            isCardSelected = true;
            updateRadio();
        });
        view.findViewById(R.id.method_bank_transfer).setOnClickListener(v -> {
            isCardSelected = false;
            updateRadio();
        });

        int[] quickAmounts = {20, 50, 100, 500};
        int[] btnIds = {R.id.btn_amount_20, R.id.btn_amount_50, R.id.btn_amount_100, R.id.btn_amount_500};
        for (int i = 0; i < btnIds.length; i++) {
            int amount = quickAmounts[i];
            view.findViewById(btnIds[i]).setOnClickListener(v -> {
                EditText et = view.findViewById(R.id.et_topup_amount);
                et.setText(String.valueOf(amount));
            });
        }

        view.findViewById(R.id.btn_confirm_topup).setOnClickListener(v -> {
            EditText et = view.findViewById(R.id.et_topup_amount);
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
            String method = isCardSelected ? "Carte bancaire" : "Virement bancaire";
            Toast.makeText(getContext(), String.format("Recharge de %.2f € par %s", amount, method),
                    Toast.LENGTH_LONG).show();
            dismiss();
        });
    }

    private void updateRadio() {
        ImageView radioCard = requireView().findViewById(R.id.radio_card);
        ImageView radioBank = requireView().findViewById(R.id.radio_bank);
        if (isCardSelected) {
            radioCard.setImageResource(R.drawable.ic_radio_selected);
            radioBank.setImageResource(R.drawable.ic_radio_unselected);
        } else {
            radioCard.setImageResource(R.drawable.ic_radio_unselected);
            radioBank.setImageResource(R.drawable.ic_radio_selected);
        }
    }
}
