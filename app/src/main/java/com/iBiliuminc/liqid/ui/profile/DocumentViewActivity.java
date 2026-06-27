package com.iBiliuminc.liqid.ui.profile;

import android.os.Bundle;
import android.widget.TextView;

import com.iBiliuminc.liqid.R;
import com.iBiliuminc.liqid.ui.BaseActivity;

public class DocumentViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);

        String content = getIntent().getStringExtra("document_content");
        if (content == null) content = "";

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.tv_document_content)).setText(content);
    }
}
