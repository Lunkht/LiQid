package com.iBiliuminc.liqid.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.domain.model.Country;
import com.iBiliuminc.liqid.ui.BaseActivity;
import com.iBiliuminc.liqid.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PhoneEntryActivity extends BaseActivity {

    private TextView tvCountryCode;
    private TextView tvFlagEmoji;
    private EditText etPhoneNumber;
    private MaterialButton btnContinue;
    private List<Country> countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_entry);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        tvCountryCode = findViewById(R.id.tv_country_code);
        tvFlagEmoji = findViewById(R.id.tv_flag_emoji);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnContinue = findViewById(R.id.btn_continue);

        initCountries();
        detectCountry();

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnContinue.setEnabled(s.length() >= 8);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnContinue.setOnClickListener(v -> {
            String fullNumber = tvCountryCode.getText().toString() + " " + etPhoneNumber.getText().toString();
            Intent intent = new Intent(this, OtpVerificationActivity.class);
            intent.putExtra("phone_number", fullNumber);
            startActivity(intent);
        });

        findViewById(R.id.btn_country_picker).setOnClickListener(v -> showCountryPicker());

        findViewById(R.id.btn_skip).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }

    private void initCountries() {
        countries = new ArrayList<>();
        // West Africa
        countries.add(new Country("Bénin", "+229", "BJ", "🇧🇯"));
        countries.add(new Country("Burkina Faso", "+226", "BF", "🇧🇫"));
        countries.add(new Country("Cap-Vert", "+238", "CV", "🇨🇻"));
        countries.add(new Country("Côte d'Ivoire", "+225", "CI", "🇨🇮"));
        countries.add(new Country("Gambie", "+220", "GM", "🇬🇲"));
        countries.add(new Country("Ghana", "+233", "GH", "🇬🇭"));
        countries.add(new Country("Guinée", "+224", "GN", "🇬🇳"));
        countries.add(new Country("Guinée-Bissau", "+245", "GW", "🇬🇼"));
        countries.add(new Country("Libéria", "+231", "LR", "🇱🇷"));
        countries.add(new Country("Mali", "+223", "ML", "🇲🇱"));
        countries.add(new Country("Mauritanie", "+222", "MR", "🇲🇷"));
        countries.add(new Country("Niger", "+227", "NE", "🇳🇪"));
        countries.add(new Country("Nigéria", "+234", "NG", "🇳🇬"));
        countries.add(new Country("Sénégal", "+221", "SN", "🇸🇳"));
        countries.add(new Country("Sierra Leone", "+232", "SL", "🇸🇱"));
        countries.add(new Country("Togo", "+228", "TG", "🇹🇬"));

        // North Africa
        countries.add(new Country("Algérie", "+213", "DZ", "🇩🇿"));
        countries.add(new Country("Égypte", "+20", "EG", "🇪🇬"));
        countries.add(new Country("Libye", "+218", "LY", "🇱🇾"));
        countries.add(new Country("Maroc", "+212", "MA", "🇲🇦"));
        countries.add(new Country("Soudan", "+249", "SD", "🇸🇩"));
        countries.add(new Country("Tunisie", "+216", "TN", "🇹🇳"));
    }

    private void detectCountry() {
        String currentIso = Locale.getDefault().getCountry();
        for (Country c : countries) {
            if (c.getIsoCode().equalsIgnoreCase(currentIso)) {
                updateSelectedCountry(c);
                return;
            }
        }
        // Default to Guinea if not found in list
        updateSelectedCountry(countries.get(6)); // Guinea is at index 6
    }

    private void updateSelectedCountry(Country country) {
        tvCountryCode.setText(country.getCode());
        tvFlagEmoji.setText(country.getFlagEmoji());
    }

    private void showCountryPicker() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Banking);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_country_picker, null);
        RecyclerView rv = view.findViewById(R.id.rv_countries);
        
        CountryAdapter adapter = new CountryAdapter(countries, country -> {
            updateSelectedCountry(country);
            dialog.dismiss();
        });
        rv.setAdapter(adapter);
        
        dialog.setContentView(view);
        dialog.show();
    }

    private static class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
        private final List<Country> countries;
        private final OnCountrySelectedListener listener;

        CountryAdapter(List<Country> countries, OnCountrySelectedListener listener) {
            this.countries = countries;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Country country = countries.get(position);
            holder.tvFlag.setText(country.getFlagEmoji());
            holder.tvName.setText(country.getName());
            holder.tvCode.setText(country.getCode());
            holder.itemView.setOnClickListener(v -> listener.onCountrySelected(country));
        }

        @Override
        public int getItemCount() { return countries.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvFlag, tvName, tvCode;
            ViewHolder(View itemView) {
                super(itemView);
                tvFlag = itemView.findViewById(R.id.tv_flag_emoji);
                tvName = itemView.findViewById(R.id.tv_country_name);
                tvCode = itemView.findViewById(R.id.tv_country_code);
            }
        }

        interface OnCountrySelectedListener {
            void onCountrySelected(Country country);
        }
    }
}
