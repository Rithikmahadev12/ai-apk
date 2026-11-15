package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import com.jarvis.assist.aosui.core.Strings;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class StorageWidget extends WidgetView {
    /* JADX INFO: Access modifiers changed from: package-private */
    public StorageWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "storage";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "INTERNAL STORAGE";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String gb(long j) {
        double d = j;
        return String.format(Locale.US, d < 1.0E10d ? "%.1f GB" : "%.0f GB", Double.valueOf(d / 1.0E9d));
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : this.h.d.stTotal > 0 ? heroRingH() : this.m9.line();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        HomeData homeData = this.h.d;
        if (i == 0) {
            head(canvas, f, f2, f3, "folder", th().BLUE, Strings.t("w.storage.h"), null);
            return;
        }
        if (homeData.stTotal <= 0) {
            msg(canvas, f, f2, f3, f4, Strings.t("w.storage.none"), null, th().DIM);
            return;
        }
        float f5 = ((float) homeData.stFree) / ((float) homeData.stTotal);
        String t = Strings.t("w.storage.free", "total", gb(homeData.stTotal));
        heroRing(canvas, f, f2, f4, 1.0f - f5, f5 < 0.1f ? th().RED : th().BLUE, "folder", gb(homeData.stFree), t, th().DIM);
        hit(f, f2, f + f3, f2 + f4, "x:storage", gb(homeData.stFree) + " " + t);
    }
}
