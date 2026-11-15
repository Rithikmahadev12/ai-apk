package com.jarvis.assist.aosui.home;

import android.graphics.Paint;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnitsMath;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class HomeCtx {
    boolean adv;
    Txt ainame;
    boolean aoa;
    float[][] arcs;
    Txt brand;
    boolean brief;
    final HomeData d;
    float[] dayArc;
    float elapsed;
    Txt monoT8;
    int page;
    String pressed;
    Txt raj17;
    Txt sbMono;
    float[] sleepArc;
    final Theme th = new Theme();
    final Geo g = new Geo();
    float zoom = 1.0f;
    int pages = 1;
    int active = 2;
    String appsLabel = "ALL APPS";
    String appsIcon = "drawer";
    String appsA11y = "All apps";
    String dockHead = "QUICK LAUNCH";
    String ringLabel = "";

    /* JADX INFO: Access modifiers changed from: package-private */
    public HomeCtx(HomeData homeData) {
        this.d = homeData;
        styles();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float orbHalf() {
        if (this.aoa && this.g.aoa()) {
            return this.g.aS * 88.0f;
        }
        return 70.0f;
    }

    float orbR() {
        if (this.aoa && this.g.aoa()) {
            return this.g.orbR;
        }
        return 56.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float L(float f, float f2) {
        return UnitsMath.labelSize(f, this.zoom, this.g.k, f2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Txt monoL(float f, float f2, float f3) {
        float L = L(f, f3);
        return mono(L, (f2 * L) / f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void styles() {
        this.raj17 = new Txt(Fonts.RAJ_600, L(17.0f, 12.0f), 0.0f).align(Paint.Align.CENTER);
        float L = L(11.0f, 10.0f);
        float L2 = L(9.0f, 9.0f);
        this.brand = new Txt(Fonts.RAJ_600, L, 0.38f * L);
        this.sbMono = new Txt(Fonts.MONO, L2, 0.14f * L2).align(Paint.Align.RIGHT);
    }

    static Txt mono(float f, float f2) {
        return new Txt(Fonts.MONO, f, f2).align(Paint.Align.CENTER);
    }
}
