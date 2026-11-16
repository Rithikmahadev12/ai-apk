package com.jarvis.assist.aosui.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Palette;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.ui.Kit;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Sheet extends FrameLayout {
    static WeakReference<Sheet> top = new WeakReference<>(null);
    final int acc;
    final int bg;
    final LinearLayout col;
    final float dn;
    final ArrayList<View> hid;
    final int ink;
    final int line;
    public Runnable onDismiss;
    final float z;

    public Sheet(Context context, Theme theme, String str) {
        super(context);
        this.hid = new ArrayList<>();
        this.dn = context.getResources().getDisplayMetrics().density;
        this.z = UnitsMath.viewZoom(context.getResources().getConfiguration().fontScale);
        boolean z = theme.light;
        this.bg = z ? Palette.LIGHT_BG : -16052460;
        this.ink = z ? Palette.LIGHT_INK : -1511691;
        this.line = z ? 1293164072 : 872415231;
        this.acc = theme.BLUE;
        setBackgroundColor(-1728053248);
        setClickable(true);
        setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Sheet$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Sheet.this.m370lambda$new$0$comaugmaosouiSheet(view);
            }
        });
        setImportantForAccessibility(2);
        Kit.pane(this, str);
        ScrollView scrollView = new ScrollView(context);
        scrollView.setClickable(true);
        scrollView.setFillViewport(false);
        scrollView.setBackground(new Kit.CardBg(this.bg, this.line, this.line, this.dn));
        this.col = new LinearLayout(context);
        this.col.setOrientation(1);
        this.col.setPadding(px(16.0f), px(18.0f), px(16.0f), px(18.0f));
        scrollView.addView(this.col);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2, 81);
        layoutParams.setMargins(px(12.0f), 0, px(12.0f), px(16.0f));
        addView(scrollView, layoutParams);
        scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.jarvis.assist.aosui.ui.Sheet$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                Sheet.this.m371lambda$new$1$comaugmaosouiSheet(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-ui-Sheet, reason: not valid java name */
    public /* synthetic */ void m370lambda$new$0$comaugmaosouiSheet(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-augma-oso-ui-Sheet, reason: not valid java name */
    public /* synthetic */ void m371lambda$new$1$comaugmaosouiSheet(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int px = px(520.0f);
        int height = (int) (getHeight() * 0.85f);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int i9 = i3 - i;
        if (i9 > px || i4 - i2 > height) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
            layoutParams2.width = Math.min(i9, px);
            layoutParams2.height = Math.min(i4 - i2, height);
            view.setLayoutParams(layoutParams2);
        }
    }

    public LinearLayout body() {
        return this.col;
    }

    public int acc() {
        return this.acc;
    }

    public int ink() {
        return this.ink;
    }

    public int line() {
        return this.line;
    }

    public int px(float f) {
        return Math.round(f * this.dn);
    }

    public TextView text(String str, float f, boolean z, float f2) {
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTextColor(this.ink);
        textView.setTypeface(z ? Fonts.RAJ_600 : Fonts.RAJ_400);
        textView.setTextSize(1, f * this.z);
        textView.setLetterSpacing(f2);
        textView.setLineSpacing(0.0f, 1.15f);
        this.col.addView(textView);
        return textView;
    }

    public Sheet title(String str) {
        TextView text = text(str.toUpperCase(Locale.getDefault()), 15.0f, false, 0.2f);
        text.setTypeface(Fonts.RAJ_400);
        text.setPadding(0, 0, 0, px(8.0f));
        Kit.heading(text);
        return this;
    }

    public Sheet para(String str, float f, float f2) {
        TextView text = text(str, f, false, 0.0f);
        text.setAlpha(f2);
        text.setPadding(0, 0, 0, px(6.0f));
        return this;
    }

    public Sheet head(String str) {
        text(str, 13.0f, true, 0.12f).setPadding(0, px(2.0f), 0, px(2.0f));
        return this;
    }

    public Sheet bullets(String str) {
        for (String str2 : str.split("\\|")) {
            text("•  " + str2, 14.0f, false, 0.0f).setPadding(px(6.0f), 0, 0, px(2.0f));
        }
        this.col.addView(new View(getContext()), new LinearLayout.LayoutParams(1, px(8.0f)));
        return this;
    }

    public Sheet buttons(String[] strArr, final Runnable[] runnableArr, boolean z) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(8388613);
        linearLayout.setPadding(0, px(8.0f), 0, 0);
        for (int i = 0; i < strArr.length; i++) {
            TextView textView = new TextView(getContext());
            boolean z2 = z && i == strArr.length - 1;
            textView.setText(strArr[i]);
            textView.setTypeface(Fonts.RAJ_600);
            textView.setTextSize(1, this.z * 13.0f);
            textView.setLetterSpacing(0.12f);
            textView.setTextColor(z2 ? this.acc : this.ink);
            textView.setGravity(17);
            textView.setMinHeight(px(44.0f));
            textView.setPadding(px(16.0f), 0, px(16.0f), 0);
            if (z2) {
                textView.setTextColor(-1);
            }
            textView.setBackground(new Kit.HudBg(z2 ? Theme.a(Kit.TICK, 0.85f) : 0, z2 ? Kit.TICK : this.line, true, this.dn));
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Sheet$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    Sheet.this.m369lambda$buttons$2$comaugmaosouiSheet(runnableArr, i, view);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.setMarginStart(px(8.0f));
            linearLayout.addView(textView, layoutParams);
            i++;
        }
        this.col.addView(linearLayout);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$buttons$2$com-augma-oso-ui-Sheet, reason: not valid java name */
    public /* synthetic */ void m369lambda$buttons$2$comaugmaosouiSheet(Runnable[] runnableArr, int i, View view) {
        Feedback.fx(view, 0);
        close();
        if (runnableArr[i] != null) {
            runnableArr[i].run();
        }
    }

    public void show(FrameLayout frameLayout) {
        closeTop();
        Feedback.fx(frameLayout, 6);
        top = new WeakReference<>(this);
        for (int i = 0; i < frameLayout.getChildCount(); i++) {
            View childAt = frameLayout.getChildAt(i);
            if (childAt.getImportantForAccessibility() != 4) {
                childAt.setImportantForAccessibility(4);
                this.hid.add(childAt);
            }
        }
        frameLayout.addView(this, new FrameLayout.LayoutParams(-1, -1));
        setAlpha(0.0f);
        animate().alpha(1.0f).setDuration(160L).start();
        post(new Runnable() { // from class: com.jarvis.assist.aosui.ui.Sheet$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                Sheet.this.m372lambda$show$3$comaugmaosouiSheet();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$show$3$com-augma-oso-ui-Sheet, reason: not valid java name */
    public /* synthetic */ void m372lambda$show$3$comaugmaosouiSheet() {
        View childAt = this.col.getChildAt(this.col.getChildCount() - 1);
        if (childAt instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) childAt;
            if (viewGroup.getChildCount() > 0) {
                viewGroup.getChildAt(viewGroup.getChildCount() - 1).requestFocus();
            }
        }
        sendAccessibilityEvent(32);
    }

    public void dismiss() {
        Feedback.fx(this, 7);
        close();
        if (this.onDismiss != null) {
            this.onDismiss.run();
        }
    }

    public void close() {
        Iterator<View> it = this.hid.iterator();
        while (it.hasNext()) {
            it.next().setImportantForAccessibility(0);
        }
        this.hid.clear();
        if (top.get() == this) {
            top.clear();
        }
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    public static boolean closeTop() {
        Sheet sheet = top.get();
        if (sheet == null) {
            return false;
        }
        sheet.dismiss();
        return true;
    }
}
