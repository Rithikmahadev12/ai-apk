package com.jarvis.assist.aosui.core;

import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.WindowInsets;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Units {
    public float hDp;
    public int ib;
    public int il;
    public int ir;
    public int it;
    public float wDp;
    public float density = 1.0f;
    public float fontScale = 1.0f;
    public float u = 1.0f;

    public void measure(Context context, int i, int i2) {
        this.density = context.getResources().getDisplayMetrics().density;
        this.fontScale = context.getResources().getConfiguration().fontScale;
        this.wDp = ((i - this.il) - this.ir) / this.density;
        this.hDp = ((i2 - this.it) - this.ib) / this.density;
        this.u = UnitsMath.u(this.wDp, this.hDp);
    }

    public float dp(float f) {
        return f * this.density;
    }

    public void insets(WindowInsets windowInsets) {
        if (windowInsets == null) {
            this.il = 0;
            this.ib = 0;
            this.ir = 0;
            this.it = 0;
            return;
        }
        if (Build.VERSION.SDK_INT >= 30) {
            Insets insets = windowInsets.getInsets(WindowInsets.Type.displayCutout() | WindowInsets.Type.systemBars());
            this.it = insets.top;
            this.ir = insets.right;
            this.ib = insets.bottom;
            this.il = insets.left;
            return;
        }
        if (Build.VERSION.SDK_INT >= 28 && windowInsets.getDisplayCutout() != null) {
            DisplayCutout displayCutout = windowInsets.getDisplayCutout();
            this.it = Math.max(displayCutout.getSafeInsetTop(), windowInsets.getSystemWindowInsetTop());
            this.ib = Math.max(displayCutout.getSafeInsetBottom(), windowInsets.getSystemWindowInsetBottom());
            this.il = displayCutout.getSafeInsetLeft();
            this.ir = displayCutout.getSafeInsetRight();
            return;
        }
        this.it = windowInsets.getSystemWindowInsetTop();
        this.ib = windowInsets.getSystemWindowInsetBottom();
        this.il = windowInsets.getSystemWindowInsetLeft();
        this.ir = windowInsets.getSystemWindowInsetRight();
    }
}
