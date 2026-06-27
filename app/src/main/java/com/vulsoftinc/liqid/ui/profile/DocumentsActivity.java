package com.vulsoftinc.liqid.ui.profile;

import android.os.Bundle;
import android.widget.Toast;

import com.vulsoftinc.liqid.R;
import com.vulsoftinc.liqid.ui.BaseActivity;

public class DocumentsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        setMenuItem(R.id.menu_statement_current, R.drawable.ic_download, "Relevé en cours (Juin 2026)", "PDF · 245 Ko", v ->
                Toast.makeText(this, "Téléchargement du relevé en cours...", Toast.LENGTH_SHORT).show());

        setMenuItem(R.id.menu_statement_previous, R.drawable.ic_download, "Relevé précédent (Mai 2026)", "PDF · 231 Ko", v ->
                Toast.makeText(this, "Téléchargement du relevé précédent...", Toast.LENGTH_SHORT).show());

        setMenuItem(R.id.menu_statement_archive, R.drawable.ic_download, "Archive annuelle 2025", "PDF · 2.8 Mo", v ->
                Toast.makeText(this, "Téléchargement de l'archive...", Toast.LENGTH_SHORT).show());

        setMenuItem(R.id.menu_terms, R.drawable.ic_note, "Conditions générales", "PDF · 180 Ko", v ->
                Toast.makeText(this, "Ouverture des CGV...", Toast.LENGTH_SHORT).show());

        setMenuItem(R.id.menu_fee_schedule, R.drawable.ic_note, "Tarifs et commissions", "PDF · 95 Ko", v ->
                Toast.makeText(this, "Ouverture de la grille tarifaire...", Toast.LENGTH_SHORT).show());
    }

    private void setMenuItem(int id, int icon, String label, String sublabel, android.view.View.OnClickListener listener) {
        android.view.View layout = findViewById(id);
        if (layout == null) return;

        ((android.widget.ImageView) layout.findViewById(R.id.iv_menu_icon)).setImageResource(icon);
        ((android.widget.TextView) layout.findViewById(R.id.tv_menu_label)).setText(label);

        android.widget.TextView tvSub = layout.findViewById(R.id.tv_menu_sublabel);
        if (tvSub != null) {
            tvSub.setVisibility(android.view.View.VISIBLE);
            tvSub.setText(sublabel);
        }

        layout.setOnClickListener(listener);
    }
}
