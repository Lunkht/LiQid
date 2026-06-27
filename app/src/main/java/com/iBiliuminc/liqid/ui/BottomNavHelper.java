package com.iBiliuminc.liqid.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.cards.CardsActivity;
import com.iBiliuminc.liqid.ui.crypto.CryptoActivity;
import com.iBiliuminc.liqid.ui.home.HomeActivity;
import com.iBiliuminc.liqid.ui.profile.SettingsActivity;
import com.iBiliuminc.liqid.ui.transfer.TransferActivity;

public class BottomNavHelper {

    public static void setup(Activity activity, int activeNavId) {
        // Resolve theme colors
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.liqid_accent, typedValue, true);
        int accentColor = typedValue.data;
        
        activity.getTheme().resolveAttribute(R.attr.liqid_text_secondary, typedValue, true);
        int inactiveColor = typedValue.data;

        View.OnClickListener navListener = v -> {
            int id = v.getId();
            if (id == activeNavId) return;
            Class<?> target;
            if (id == R.id.nav_home) target = HomeActivity.class;
            else if (id == R.id.nav_cards) target = CardsActivity.class;
            else if (id == R.id.nav_transfer) target = TransferActivity.class;
            else if (id == R.id.nav_crypto) target = CryptoActivity.class;
            else if (id == R.id.nav_settings) target = SettingsActivity.class;
            else return;

            Intent intent = new Intent(activity, target);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            if (!(activity instanceof HomeActivity)) {
                activity.finish();
            }
        };

        activity.findViewById(R.id.nav_home).setOnClickListener(navListener);
        activity.findViewById(R.id.nav_cards).setOnClickListener(navListener);
        activity.findViewById(R.id.nav_transfer).setOnClickListener(navListener);
        activity.findViewById(R.id.nav_crypto).setOnClickListener(navListener);
        activity.findViewById(R.id.nav_settings).setOnClickListener(navListener);

        int[][] navItems = {
                {R.id.nav_home, R.id.nav_icon_home, R.id.nav_label_home},
                {R.id.nav_cards, R.id.nav_icon_cards, R.id.nav_label_cards},
                {R.id.nav_transfer, R.id.nav_icon_transfer, R.id.nav_label_transfer},
                {R.id.nav_crypto, R.id.nav_icon_crypto, R.id.nav_label_crypto},
                {R.id.nav_settings, R.id.nav_icon_settings, R.id.nav_label_settings}
        };
        for (int[] item : navItems) {
            boolean active = item[0] == activeNavId;
            int color = active ? accentColor : inactiveColor;
            ImageView icon = activity.findViewById(item[1]);
            if (icon != null) icon.setColorFilter(color);
            TextView label = activity.findViewById(item[2]);
            if (label != null) label.setTextColor(color);
        }
    }
}
