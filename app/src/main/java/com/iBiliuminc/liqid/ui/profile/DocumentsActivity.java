package com.iBiliuminc.liqid.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DocumentsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        setMenuItem(R.id.menu_statement_current, R.drawable.ic_download,
                "Relevé en cours (Juin 2026)", "PDF · 245 Ko",
                v -> generateStatement("Relevé Juin 2026",
                        "Solde au 27/06/2026 : 0,00 €\n" +
                        "Opérations : 5\n" +
                        "Débits : 128,38 €\n" +
                        "Crédits : 500,00 €"));

        setMenuItem(R.id.menu_statement_previous, R.drawable.ic_download,
                "Relevé précédent (Mai 2026)", "PDF · 231 Ko",
                v -> generateStatement("Relevé Mai 2026",
                        "Solde au 31/05/2026 : 0,00 €\n" +
                        "Opérations : 3\n" +
                        "Débits : 45,00 €\n" +
                        "Crédits : 1 200,00 €"));

        setMenuItem(R.id.menu_statement_archive, R.drawable.ic_download,
                "Archive annuelle 2025", "PDF · 2.8 Mo",
                v -> generateStatement("Archive 2025",
                        "Total crédits 2025 : 12 450,00 €\n" +
                        "Total débits 2025 : 8 320,00 €\n" +
                        "Solde final : 4 130,00 €"));

        setMenuItem(R.id.menu_terms, R.drawable.ic_note,
                "Conditions générales", "PDF · 180 Ko",
                v -> openAsset("CGV Liqid Banking\n\n" +
                        "1. Objet\nLes présentes CGV régissent l'utilisation du service Liqid.\n\n" +
                        "2. Compte\nL'ouverture d'un compte est gratuite et sans engagement.\n\n" +
                        "3. Cartes\nLes cartes STANDARD, PREMIUM et METAL sont disponibles.\n\n" +
                        "4. Frais\nLes virements SEPA sont gratuits. Les changes devises appliquent un spread de 0.5%.\n\n" +
                        "5. Responsabilité\nLe client est responsable de la sécurité de son code PIN."));

        setMenuItem(R.id.menu_fee_schedule, R.drawable.ic_note,
                "Tarifs et commissions", "PDF · 95 Ko",
                v -> openAsset("Grille Tarifaire\n\n" +
                        "STANDARD : Gratuit\n" +
                        "  - Virements SEPA : Gratuits\n" +
                        "  - Retraits DAB : 1 €\n" +
                        "  - Change devises : Spread 1%\n\n" +
                        "PREMIUM : 9,99 €/mois\n" +
                        "  - Virements SEPA : Gratuits\n" +
                        "  - Retraits DAB : Gratuits\n" +
                        "  - Change devises : Spread 0.5%\n\n" +
                        "METAL : 24,99 €/mois\n" +
                        "  - Tout illimité\n" +
                        "  - Change devises : Spread 0.2%\n" +
                        "  - Assurance voyage incluse"));
    }

    private void generateStatement(String title, String content) {
        String fileName = title.replaceAll("\\s+", "_") + ".txt";
        File dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (dir != null) {
            dir.mkdirs();
            File file = new File(dir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(("=== " + title + " ===\n\n").getBytes());
                fos.write(("Généré le : " + new SimpleDateFormat("dd/MM/yyyy HH:mm",
                        Locale.FRANCE).format(new Date()) + "\n\n").getBytes());
                fos.write(content.getBytes());
                fos.close();
                Toast.makeText(this, "Téléchargé : " + fileName, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openAsset(String content) {
        Intent intent = new Intent(this, DocumentViewActivity.class);
        intent.putExtra("document_content", content);
        startActivity(intent);
    }

    private void setMenuItem(int id, int icon, String label, String sublabel,
                             android.view.View.OnClickListener listener) {
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
