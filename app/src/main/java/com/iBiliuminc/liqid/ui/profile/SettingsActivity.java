package com.iBiliuminc.liqid.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.BottomNavHelper;
import com.iBiliuminc.liqid.ui.auth.RegisterActivity;

public class SettingsActivity extends BaseActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);

        BottomNavHelper.setup(this, R.id.nav_settings);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        setupMenuItems();
        setupToggleSettings();
        setupAboutSettings();
    }

    private void setupMenuItems() {
        setNavigationItem(R.id.menu_profile_link, R.drawable.ic_profile, "Mon profil", v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
        setNavigationItem(R.id.menu_personal_info, R.drawable.ic_profile, "Informations personnelles", v ->
                startActivity(new Intent(this, PersonalInfoActivity.class)));
        setNavigationItem(R.id.menu_iban, R.drawable.ic_bank, "Mes IBAN", v ->
                startActivity(new Intent(this, IbanActivity.class)));
        setNavigationItem(R.id.menu_documents, R.drawable.ic_download, "Documents", v ->
                startActivity(new Intent(this, DocumentsActivity.class)));
        setNavigationItem(R.id.menu_pin, R.drawable.ic_eye, "Modifier le code PIN", v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("change_pin_only", true);
            startActivity(intent);
        });
        setNavigationItem(R.id.menu_biometric, R.drawable.ic_fingerprint, "Authentification biométrique", v -> {
            boolean current = prefs.getBoolean("biometric_enabled", true);
            prefs.edit().putBoolean("biometric_enabled", !current).apply();
            Toast.makeText(this, !current ? "Biométrie activée" : "Biométrie désactivée", Toast.LENGTH_SHORT).show();
        });
        setNavigationItem(R.id.menu_devices, R.drawable.ic_location, "Appareils connectés", v ->
                Toast.makeText(this, "Cet appareil est le seul connecté", Toast.LENGTH_SHORT).show());
        setNavigationItem(R.id.menu_notifications, R.drawable.ic_bell, "Notifications", v ->
                Toast.makeText(this, "Paramètres de notification", Toast.LENGTH_SHORT).show());
        setNavigationItem(R.id.menu_language, R.drawable.ic_flag, "Langue", v -> {
            String[] languages = {"Français", "English", "Español"};
            new AlertDialog.Builder(this)
                    .setTitle("Choisir la langue")
                    .setItems(languages, (dialog, which) ->
                            Toast.makeText(this, "Langue changée : " + languages[which], Toast.LENGTH_SHORT).show())
                    .show();
        });
    }

    private void setupToggleSettings() {
        setupThemeSelector();

        setToggleItem(R.id.setting_hide_balance, R.drawable.ic_eye, "Masquer le solde",
                "Cacher les montants au démarrage", "hide_balance_enabled", false);

        setToggleItem(R.id.setting_incognito, R.drawable.ic_fingerprint, "Mode Incognito",
                "Sécuriser l'accès à l'application", "incognito_enabled", false);
    }

    private void setupAboutSettings() {
        setNavigationItem(R.id.menu_tos, R.drawable.ic_note, "Conditions d'utilisation", null);
        setNavigationItem(R.id.menu_privacy, R.drawable.ic_check_circle, "Politique de confidentialité", null);
    }

    private void setupThemeSelector() {
        View layout = findViewById(R.id.setting_theme_selector);
        if (layout == null) return;

        ((ImageView) layout.findViewById(R.id.iv_menu_icon)).setImageResource(R.drawable.ic_star);
        TextView tvLabel = layout.findViewById(R.id.tv_menu_label);
        tvLabel.setText("Thème de l'application");

        TextView tvSub = layout.findViewById(R.id.tv_menu_sublabel);
        tvSub.setVisibility(View.VISIBLE);

        String currentTheme = prefs.getString("app_theme", "dark");
        String themeLabel;
        switch (currentTheme) {
            case "light": themeLabel = "Clair"; break;
            case "neon": themeLabel = "Neon"; break;
            default: themeLabel = "Sombre";
        }
        tvSub.setText(themeLabel);

        layout.setOnClickListener(v -> {
            String[] themes = {"Clair", "Sombre", "Neon"};
            String[] themeValues = {"light", "dark", "neon"};

            new AlertDialog.Builder(this)
                    .setTitle("Choisir un thème")
                    .setItems(themes, (dialog, which) -> {
                        String selected = themeValues[which];
                        if (!selected.equals(currentTheme)) {
                            prefs.edit().putString("app_theme", selected).apply();
                            Intent intent = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        });
    }

    private void setNavigationItem(int layoutId, int iconRes, String label, View.OnClickListener listener) {
        View layout = findViewById(layoutId);
        if (layout == null) return;

        ((ImageView) layout.findViewById(R.id.iv_menu_icon)).setImageResource(iconRes);
        ((TextView) layout.findViewById(R.id.tv_menu_label)).setText(label);

        if (listener != null) {
            layout.setOnClickListener(listener);
        }
    }

    private void setToggleItem(int layoutId, int iconRes, String label, String sublabel,
                               String prefKey, boolean defaultValue) {
        View layout = findViewById(layoutId);
        if (layout == null) return;

        ((ImageView) layout.findViewById(R.id.iv_menu_icon)).setImageResource(iconRes);
        ((TextView) layout.findViewById(R.id.tv_menu_label)).setText(label);

        View chevron = layout.findViewById(R.id.iv_menu_chevron);
        if (chevron != null) chevron.setVisibility(View.GONE);

        MaterialSwitch sw = layout.findViewById(R.id.switch_menu_toggle);
        if (sw != null) {
            sw.setVisibility(View.VISIBLE);
            boolean current = prefs.getBoolean(prefKey, defaultValue);
            sw.setChecked(current);
        }

        TextView tvSub = layout.findViewById(R.id.tv_menu_sublabel);
        if (tvSub != null) {
            tvSub.setVisibility(View.VISIBLE);
            tvSub.setText(sublabel);
        }

        layout.setOnClickListener(v -> {
            if (sw != null) {
                boolean newState = !sw.isChecked();
                sw.setChecked(newState);
                prefs.edit().putBoolean(prefKey, newState).apply();
                Toast.makeText(this, label + " : " + (newState ? "Activé" : "Désactivé"), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
