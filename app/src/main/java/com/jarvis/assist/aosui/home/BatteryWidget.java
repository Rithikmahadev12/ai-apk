package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;

/* loaded from: /tmp/augmaos/classes.dex */
final class BatteryWidget extends WidgetView {
    /* JADX INFO: Access modifiers changed from: package-private */
    public BatteryWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "battery";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "SYSTEM BATTERY";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        return this.h.d.btPct + "%";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 3;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : i == 1 ? heroRingH() : this.m9.line() + 4.0f;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        int i2;
        HomeData homeData = this.h.d;
        boolean equals = "charge".equals(homeData.btState);
        int i3 = homeData.btPct;
        if (equals) {
            i2 = th().BLUE;
        } else {
            Theme th = th();
            i2 = i3 <= 20 ? th.RED : th.INK;
        }
        int i4 = i2;
        if (i == 0) {
            head(canvas, f, f2, f3, "batt", i4, Strings.t("w.battery.h"), null);
            return;
        }
        String str = equals ? "CHARGING" : i3 <= 20 ? "LOW · CHARGE SOON" : "ON BATTERY";
        if (i == 2) {
            metric(canvas, "LEVEL", i3 / 100.0f, i3 + "%", f, f2, f3, f4);
        } else {
            heroRing(canvas, f, f2, f4, i3 / 100.0f, i4, "batt", i3 + "%", str, th().DIM);
            hit(f, f2, f + f3, f2 + f4, "x:battery", "Battery " + i3 + "% " + str);
        }
    }
}
