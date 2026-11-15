package com.jarvis.assist.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.jarvis.assist.core.Palette;
import com.jarvis.assist.core.Store;

/**
 * AppSettings — standalone settings screen.
 * Mirrors com.augma.oso.Apps$AppSettings from DEX.
 */
public class AppSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Palette.BG_DEEP);
        sv.setVerticalScrollBarEnabled(false);

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(dp(24), dp(60), dp(24), dp(40));

        col.addView(Kit.brandTv(this, "SETTINGS", Palette.CYAN, 28f), Kit.matchWrap());
        col.addView(Kit.divider(this, Palette.BORDER, 1, 12, 16), Kit.matchWrap());

        col.addView(Kit.sectionHeader(this, "CONNECTION"), Kit.matchWrap());

        EditText etUrl = Kit.input(this, "Server URL");
        etUrl.setText(Store.serverUrl());
        col.addView(etUrl, Kit.matchWrap());

        EditText etUser = Kit.input(this, "Username");
        etUser.setText(Store.userName());
        col.addView(etUser, Kit.matchWrap());

        col.addView(Kit.sectionHeader(this, "AI PROVIDER"), Kit.matchWrap());

        EditText etAiUrl = Kit.input(this, "Base URL (e.g. https://api.openai.com/v1)");
        etAiUrl.setText(Store.aiUrl());
        col.addView(etAiUrl, Kit.matchWrap());

        EditText etKey = Kit.input(this, "API Key");
        etKey.setText(Store.aiKey());
        etKey.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        col.addView(etKey, Kit.matchWrap());

        EditText etModel = Kit.input(this, "Model");
        etModel.setText(Store.aiModel());
        col.addView(etModel, Kit.matchWrap());

        col.addView(Kit.vSpace(this, 16), Kit.matchWrap());

        Button save = Kit.btn(this, "SAVE ALL", Palette.CYAN, Palette.BG_DEEP);
        save.setOnClickListener(v -> {
            Store.set(Store.KEY_SERVER_URL, etUrl.getText().toString().trim());
            Store.set(Store.KEY_USER_NAME,  etUser.getText().toString().trim());
            Store.set(Store.KEY_AI_URL,     etAiUrl.getText().toString().trim());
            Store.set(Store.KEY_AI_KEY,     etKey.getText().toString().trim());
            Store.set(Store.KEY_AI_MODEL,   etModel.getText().toString().trim());
            Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
            finish();
        });
        col.addView(save, Kit.matchWrap());

        col.addView(Kit.vSpace(this, 10), Kit.matchWrap());

        Button back = Kit.btn(this, "← BACK", Palette.BG_CARD, Palette.TEXT_DIM);
        back.setOnClickListener(v -> finish());
        col.addView(back, Kit.matchWrap());

        sv.addView(col, new ScrollView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(sv);
    }

    private int dp(float v) { return Kit.dp(this, v); }
}
