package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.HomeBanner;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.ui.Kit;

public final class SetHomeBanner extends LinearLayout {

    public interface Host { void gone(); }

    public SetHomeBanner(Context context, Theme theme, final Backend backend, float f, final Host host) {
        super(context);
        float f2 = context.getResources().getDisplayMetrics().density;
        setOrientation(0);
        setGravity(16);
        setBackground(new Kit.CardBg(Theme.a(theme.light ? -1 : -16118511, 0.92f), Theme.a(theme.BLUE, 0.5f), theme.LINE2, f2));
        float f3 = 6.0f * f2;
        setPadding(Math.round(12.0f * f2), Math.round(f3), Math.round(4.0f * f2), Math.round(f3));
        float f4 = 44.0f * f2;
        setMinimumHeight(Math.round(f4));
        TextView textView = new TextView(context);
        textView.setText(Strings.t("hb.text"));
        textView.setTypeface(Fonts.MONO);
        textView.setTextSize(1, 9.5f * f);
        textView.setLetterSpacing(0.1f);
        textView.setTextColor(theme.INK);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        addView(textView, new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView btn = btn(context, Strings.t("hb.go"), theme.BLUE, f, f2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Feedback.fx(view, 0);
                backend.askPerm("home");
            }
        });
        addView(btn);
        TextView btn2 = btn(context, "×", theme.DIM, f, f2);
        btn2.setTextSize(1, f * 18.0f);
        btn2.setContentDescription(Strings.t("hb.dismiss"));
        btn2.setBackground(null);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Feedback.fx(view, 7);
                Store.set(HomeBanner.KEY, String.valueOf(System.currentTimeMillis()));
                host.gone();
            }
        });
        addView(btn2, new LinearLayout.LayoutParams(Math.round(f4), Math.round(f4)));
        setContentDescription(Strings.t("hb.text"));
    }

    static TextView btn(Context context, String str, int i, float f, float f2) {
        TextView tv = new TextView(context);
        tv.setText(str);
        tv.setTypeface(Fonts.MONO);
        tv.setTextSize(1, f * 9.5f);
        tv.setLetterSpacing(0.12f);
        tv.setTextColor(i);
        tv.setGravity(17);
        tv.setMinHeight(Math.round(36.0f * f2));
        float p = 12.0f * f2;
        tv.setPadding(Math.round(p), 0, Math.round(p), 0);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0);
        gd.setCornerRadius(999.0f * f2);
        gd.setStroke(Math.max(1, Math.round(f2)), i);
        tv.setBackground(gd);
        tv.setClickable(true);
        tv.setFocusable(true);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
        lp.setMarginStart(Math.round(f2 * 8.0f));
        tv.setLayoutParams(lp);
        return tv;
    }
}
