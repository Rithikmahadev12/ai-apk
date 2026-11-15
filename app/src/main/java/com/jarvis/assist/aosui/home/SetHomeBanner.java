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
import com.jarvis.assist.aosui.home.SetHomeBanner;
import com.jarvis.assist.aosui.ui.Kit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class SetHomeBanner extends LinearLayout {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Host {
        void gone();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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
        btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.SetHomeBanner$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SetHomeBanner.lambda$new$0(Backend.this, view);
            }
        });
        addView(btn);
        TextView btn2 = btn(context, "×", theme.DIM, f, f2);
        btn2.setTextSize(1, f * 18.0f);
        btn2.setContentDescription(Strings.t("hb.dismiss"));
        btn2.setBackground(null);
        btn2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.SetHomeBanner$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SetHomeBanner.lambda$new$1(SetHomeBanner.Host.this, view);
            }
        });
        addView(btn2, new LinearLayout.LayoutParams(Math.round(f4), Math.round(f4)));
        setContentDescription(Strings.t("hb.text"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$0(Backend backend, View view) {
        Feedback.fx(view, 0);
        backend.askPerm("home");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$1(Host host, View view) {
        Feedback.fx(view, 7);
        Store.set(HomeBanner.KEY, String.valueOf(System.currentTimeMillis()));
        host.gone();
    }

    static TextView btn(Context context, String str, int i, float f, float f2) {
        TextView textView = new TextView(context);
        textView.setText(str);
        textView.setTypeface(Fonts.MONO);
        textView.setTextSize(1, f * 9.5f);
        textView.setLetterSpacing(0.12f);
        textView.setTextColor(i);
        textView.setGravity(17);
        textView.setMinHeight(Math.round(36.0f * f2));
        float f3 = 12.0f * f2;
        textView.setPadding(Math.round(f3), 0, Math.round(f3), 0);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0);
        gradientDrawable.setCornerRadius(999.0f * f2);
        gradientDrawable.setStroke(Math.max(1, Math.round(f2)), i);
        textView.setBackground(gradientDrawable);
        textView.setClickable(true);
        textView.setFocusable(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMarginStart(Math.round(f2 * 8.0f));
        textView.setLayoutParams(layoutParams);
        return textView;
    }
}
