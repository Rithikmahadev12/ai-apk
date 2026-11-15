package com.jarvis.assist.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jarvis.assist.core.Palette;
import com.jarvis.assist.core.Store;
import com.jarvis.assist.network.JarvisNet;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * AppBrief — daily brief screen.
 * Mirrors com.augma.oso.brief.BriefPage from DEX.
 */
public class AppBrief extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Palette.BG_DEEP);
        sv.setVerticalScrollBarEnabled(false);

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(dp(20), dp(60), dp(20), dp(40));

        Button back = Kit.smallBtn(this, "←", Palette.BG_DEEP, Palette.CYAN);
        back.setOnClickListener(v -> finish());
        col.addView(back, Kit.wrapWrap());

        col.addView(Kit.brandTv(this, "DAILY BRIEF", Palette.CYAN, 28f), Kit.matchWrap());
        col.addView(Kit.divider(this, Palette.BORDER, 1, 10, 16), Kit.matchWrap());

        ProgressBar pb = Kit.progress(this);
        pb.setVisibility(View.VISIBLE);
        col.addView(pb, Kit.matchWrap());

        sv.addView(col, new ScrollView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(sv);

        JarvisNet.fetchBrief(Store.userName(), new JarvisNet.Cb<JSONObject>() {
            @Override public void ok(JSONObject r) {
                col.removeView(pb);
                try {
                    Iterator<String> keys = r.keys();
                    while (keys.hasNext()) {
                        String k = keys.next();
                        col.addView(Kit.sectionHeader(AppBrief.this, k.toUpperCase()), Kit.matchWrap());
                        col.addView(Kit.bodyTv(AppBrief.this,
                            r.optString(k, "—"), Palette.TEXT_HI, 14f), Kit.matchWrap());
                        col.addView(Kit.vSpace(AppBrief.this, 12), Kit.matchWrap());
                    }
                } catch (Exception ignored) {}
            }
            @Override public void err(String e) {
                col.removeView(pb);
                col.addView(Kit.statusLabel(AppBrief.this, "// ERR: " + e, Palette.RED));
            }
        });
    }

    private int dp(float v) { return Kit.dp(this, v); }
}
