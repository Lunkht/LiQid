package com.iBiliuminc.liqid.ui.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.iBiliuminc.liqid.R;

public class ReceiveBottomSheet extends BottomSheetDialogFragment {

    private static final String IBAN = "GN00 IBIL 0001 2345 6789 01";
    private static final String BIC = "IBILGNGA";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receive_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tv_iban_display).setVisibility(View.VISIBLE);
        ((android.widget.TextView) view.findViewById(R.id.tv_iban_display)).setText(IBAN);

        view.findViewById(R.id.btn_copy_iban).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("IBAN", IBAN));
            Toast.makeText(getContext(), "IBAN copié", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btn_share_iban).setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "IBAN : " + IBAN + "\nBIC : " + BIC);
            startActivity(Intent.createChooser(share, "Partager mes coordonnées bancaires"));
        });
    }
}
