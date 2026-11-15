package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.home.HomeData;
import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
final class RecentWidget extends WidgetView {
    Txt ini;
    Txt m7;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecentWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "recent";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "AOS LAUNCH HISTORY";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void styles() {
        super.styles();
        float f = this.h.zoom;
        this.ini = new Txt(Fonts.RAJ_600, 10.5f * f, 0.0f).align(Paint.Align.CENTER);
        float f2 = f * 7.0f;
        this.m7 = new Txt(Fonts.MONO, Math.max(8.0f, f2), f2 * 0.14f).align(Paint.Align.CENTER);
    }

    int cols() {
        return Math.max(1, (int) Math.floor(((this.W - this.padL) - this.padR) / 52.0f));
    }

    int shown() {
        return Math.min(Math.min(this.h.g.ph < 150.0f ? 4 : cols(), cols()), this.h.d.recent.size());
    }

    float app() {
        return Math.max(24.0f, Math.min(30.0f, this.h.g.pw * 0.09f));
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : ((this.H - this.padT) - this.padB) - headH();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        if (i == 0) {
            head(canvas, f, f2, f3, "drawer", th().BLUE, Strings.t("w.recent.h"), null);
            return;
        }
        RecentWidget recentWidget = this;
        float f5 = 2.0f;
        if (recentWidget.h.d.recent.isEmpty()) {
            recentWidget.m9.draw(canvas, Strings.t("w.recent.none"), f, f2 + (f4 / 2.0f) + (recentWidget.m9.cap / 2.0f), recentWidget.col(recentWidget.th().DIM));
            return;
        }
        float cols = f3 / recentWidget.cols();
        float app = recentWidget.app();
        float line = app + 2.0f + recentWidget.m7.line();
        float f6 = f2 + ((f4 - line) / 2.0f);
        int i2 = 0;
        while (i2 < recentWidget.shown()) {
            HomeData.App app2 = recentWidget.h.d.recent.get(i2);
            float f7 = cols / f5;
            float f8 = f + (i2 * cols) + f7;
            String upperCase = app2.label.toUpperCase(Locale.ROOT);
            recentWidget.p.setShader(null);
            recentWidget.p.setStyle(Paint.Style.STROKE);
            recentWidget.p.setStrokeWidth(1.0f);
            recentWidget.p.setColor(recentWidget.col(recentWidget.th().LINE2));
            float f9 = app / f5;
            float f10 = f6 + f9;
            canvas.drawCircle(f8, f10, f9 - 0.5f, recentWidget.p);
            float f11 = f5;
            recentWidget.ini.draw(canvas, upperCase.length() > 2 ? upperCase.substring(0, 2) : upperCase, f8, f10 + (recentWidget.ini.cap / f11), recentWidget.col(recentWidget.th().INK));
            recentWidget.m7.draw(canvas, upperCase.length() > 9 ? upperCase.substring(0, 9) : upperCase, f8, recentWidget.m7.asc + f6 + app + f11, recentWidget.col(recentWidget.th().DIM));
            recentWidget.hit(f8 - f7, f6, f8 + f7, f6 + line, "r" + i2, app2.label);
            i2++;
            recentWidget = this;
            f5 = f11;
        }
    }
}
