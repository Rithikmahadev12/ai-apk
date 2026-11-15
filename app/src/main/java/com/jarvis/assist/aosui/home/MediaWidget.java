package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.SystemClock;
import com.jarvis.assist.aosui.core.Flip7;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
final class MediaWidget extends WidgetView {
    final Path tri;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.tri = new Path();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "media";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "ACTIVE MEDIA SESSION";
    }

    float app() {
        return Math.max(24.0f, Math.min(30.0f, this.h.g.pw * 0.09f));
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        return this.h.d.media == null ? "IDLE" : this.h.d.media.optBoolean("playing") ? "PLAYING" : "PAUSED";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return (this.h.d.media == null || this.h.adv) ? 2 : 4;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        Txt txt;
        if (this.h.adv && this.h.d.media != null) {
            return i == 0 ? headH() : (((this.H - this.padT) - this.padB) - headH()) - 6.0f;
        }
        if (i == 0) {
            return headH();
        }
        if (this.h.d.media != null) {
            if (i == 1) {
                txt = this.sb13;
                return txt.line();
            }
            if (i != 2) {
                return app();
            }
        }
        txt = this.m9;
        return txt.line();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        String t;
        int i2;
        String str;
        HomeData homeData = this.h.d;
        String upperCase = homeData.media == null ? "" : homeData.media.optString("app").toUpperCase(Locale.ROOT);
        if (i == 0) {
            head(canvas, f, f2, f3, "music", th().BLUE, Strings.t("w.media.h"), upperCase);
            return;
        }
        if (homeData.media == null) {
            if (homeData.mediaAccess) {
                t = Strings.t("w.media.none");
                i2 = th().DIM;
                str = null;
            } else {
                t = Strings.t("w.media.perm");
                i2 = th().DIM;
                str = "x:notifAccess";
            }
            msg(canvas, f, f2, f3, f4, t, str, i2);
            return;
        }
        if (this.h.adv) {
            advPlayer(canvas, f, f2, f3, f4);
            return;
        }
        MediaWidget mediaWidget = this;
        if (i == 1) {
            mediaWidget.sb13.draw(canvas, fitText(mediaWidget.sb13, homeData.media.optString("title"), f3), f, (f4 / 2.0f) + f2 + (mediaWidget.sb13.cap / 2.0f), mediaWidget.col(mediaWidget.th().INK));
            return;
        }
        if (i == 2) {
            mediaWidget.m9.draw(canvas, fitText(mediaWidget.m9, homeData.media.optString("artist").toUpperCase(Locale.ROOT), f3), f, mediaWidget.m9.base(f2), mediaWidget.col(mediaWidget.th().DIM));
            return;
        }
        Canvas canvas2 = canvas;
        float app = mediaWidget.app();
        float f5 = f + (f3 / 2.0f);
        float f6 = f2 + (f4 / 2.0f);
        boolean optBoolean = homeData.media.optBoolean("playing");
        int i3 = -1;
        while (i3 <= 1) {
            float f7 = i3;
            float f8 = f5 + ((22.0f + app) * f7);
            boolean z = i3 == 0;
            Theme th = mediaWidget.th();
            int col = mediaWidget.col(z ? th.BLUE : th.LINE2);
            Theme th2 = mediaWidget.th();
            int col2 = mediaWidget.col(z ? th2.BLUE : th2.INK);
            mediaWidget.p.setShader(null);
            mediaWidget.p.setStyle(Paint.Style.STROKE);
            mediaWidget.p.setStrokeWidth(1.0f);
            mediaWidget.p.setColor(col);
            float f9 = app / 2.0f;
            canvas2.drawCircle(f8, f6, f9 - 0.5f, mediaWidget.p);
            mediaWidget.p.setStyle(Paint.Style.FILL);
            mediaWidget.p.setColor(col2);
            float f10 = 0.13f * app;
            if (z && optBoolean) {
                float f11 = f10 * 1.1f;
                float f12 = 1.3f * f10;
                float f13 = f6 - f12;
                float f14 = f10 * 0.35f;
                float f15 = f12 + f6;
                canvas.drawRect(f8 - f11, f13, f8 - f14, f15, mediaWidget.p);
                canvas.drawRect(f8 + f14, f13, f8 + f11, f15, mediaWidget.p);
            } else if (z) {
                mediaWidget.tri(f8 - (0.8f * f10), f6, f10 * 1.3f, 1.0f);
                canvas2.drawPath(mediaWidget.tri, mediaWidget.p);
            } else {
                mediaWidget.tri(f8 - ((f7 * f10) * 1.2f), f6, f10, f7);
                canvas2.drawPath(mediaWidget.tri, mediaWidget.p);
                mediaWidget.tri(f8, f6, f10, f7);
                canvas2.drawPath(mediaWidget.tri, mediaWidget.p);
            }
            mediaWidget.hit((f8 - f9) - 8.0f, f6 - f9, 8.0f + f8 + f9, f6 + f9, i3 < 0 ? "mprev" : i3 > 0 ? "mnext" : "mplay", i3 < 0 ? "Previous" : i3 > 0 ? "Next" : "Play or pause");
            i3++;
            mediaWidget = this;
            canvas2 = canvas;
        }
    }

    void advPlayer(Canvas canvas, float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        HomeData homeData = this.h.d;
        boolean optBoolean = homeData.media.optBoolean("playing");
        float f7 = f4 / 2.0f;
        float min = Math.min(f7 - 8.0f, 0.2f * f3);
        float f8 = f + min + 8.0f;
        float f9 = f2 + f7;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(col(Flip7.G7));
        canvas.drawCircle(f8, f9, min, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(col(Flip7.G4));
        canvas.drawCircle(f8, f9, min, this.p);
        long optLong = homeData.media.optLong("dur", 0L);
        float min2 = optLong > 0 ? Math.min(1.0f, ((float) homeData.media.optLong("pos", 0L)) / ((float) optLong)) : 0.0f;
        this.p.setStrokeWidth(2.0f);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(col(-1774607));
        float f10 = f8 + min;
        this.rf.set((f8 - min) + 4.0f, (f9 - min) + 4.0f, f10 - 4.0f, (f9 + min) - 4.0f);
        if (min2 > 0.0f) {
            canvas.drawArc(this.rf, -90.0f, min2 * 360.0f, false, this.p);
        }
        this.p.setStrokeCap(Paint.Cap.BUTT);
        boolean z = optBoolean && !Perf.k().calm;
        long j = 90;
        long uptimeMillis = z ? SystemClock.uptimeMillis() / 90 : 0L;
        String str = "title";
        int hashCode = homeData.media.optString("title").hashCode();
        int i = 0;
        while (true) {
            f5 = 0.8f;
            if (i >= 48) {
                break;
            }
            double d = i;
            double d2 = (6.283185307179586d * d) / 48.0d;
            float f11 = f8;
            double d3 = uptimeMillis;
            float abs = ((optBoolean ? 7.0f : 2.5f) * Math.abs((float) ((Math.sin((hashCode * 0.001d) + (1.7d * d) + (0.9d * d3)) * 0.5d * Math.cos((d * 0.53d) + (d3 * 0.37d))) + 0.5d))) + 2.0f;
            this.p.setColor(col(i % 2 == 0 ? Theme.a(Flip7.BLUE_HI, 0.8f) : Theme.a(Flip7.RED_HI, 0.7f)));
            float cos = (float) Math.cos(d2);
            float sin = (float) Math.sin(d2);
            float f12 = 3.0f + min;
            float f13 = f11 + (cos * f12);
            float f14 = (sin * f12) + f9;
            float f15 = f12 + abs;
            canvas.drawLine(f13, f14, f11 + (cos * f15), (sin * f15) + f9, this.p);
            i++;
            str = str;
            j = 90;
            f9 = f9;
            hashCode = hashCode;
            f8 = f11;
        }
        long j2 = j;
        Icons.draw(canvas, this.p, "music", f8, f9, min * 0.5f, col(-1774607));
        float f16 = f10 + 16.0f;
        float f17 = (f + f3) - f16;
        float max = Math.max(22.0f, Math.min(28.0f, 0.32f * f4));
        Txt txt = this.sb13;
        String fitText = fitText(this.sb13, homeData.media.optString(str), f17);
        float f18 = f2 + (0.28f * f4);
        txt.draw(canvas, fitText, f16, f18, col(th().INK));
        this.m9.draw(canvas, fitText(this.m9, homeData.media.optString("artist").toUpperCase(Locale.ROOT), f17), f16, f18 + this.m9.line() + 2.0f, col(th().DIM));
        Canvas canvas2 = canvas;
        float f19 = f2 + (0.72f * f4);
        float f20 = (f17 / 2.0f) + f16;
        int i2 = -1;
        while (i2 <= 1) {
            float f21 = i2;
            float f22 = ((18.0f + max) * f21) + f20;
            boolean z2 = i2 == 0;
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.0f);
            Paint paint = this.p;
            Theme th = th();
            paint.setColor(col(z2 ? th.RED : th.LINE2));
            float f23 = max / 2.0f;
            canvas2.drawCircle(f22, f19, f23, this.p);
            float f24 = f5;
            this.p.setStyle(Paint.Style.FILL);
            Paint paint2 = this.p;
            Theme th2 = th();
            paint2.setColor(col(z2 ? th2.RED : th2.INK));
            float f25 = 0.13f * max;
            if (z2 && optBoolean) {
                float f26 = f25 * 1.1f;
                float f27 = f22 - f26;
                float f28 = 1.3f * f25;
                float f29 = f19 - f28;
                float f30 = 0.35f * f25;
                float f31 = f28 + f19;
                f6 = f23;
                canvas2.drawRect(f27, f29, f22 - f30, f31, this.p);
                canvas.drawRect(f22 + f30, f29, f22 + f26, f31, this.p);
                f22 = f22;
            } else {
                f6 = f23;
                if (z2) {
                    tri(f22 - (f25 * f24), f19, f25 * 1.3f, 1.0f);
                } else {
                    tri(f22 - ((f21 * f25) * 1.2f), f19, f25, f21);
                    canvas2.drawPath(this.tri, this.p);
                    tri(f22, f19, f25, f21);
                }
                canvas2.drawPath(this.tri, this.p);
            }
            hit((f22 - f6) - 6.0f, f19 - f6, f22 + f6 + 6.0f, f19 + f6, i2 < 0 ? "mprev" : i2 > 0 ? "mnext" : "mplay", i2 < 0 ? "Previous" : i2 > 0 ? "Next" : "Play or pause");
            i2++;
            canvas2 = canvas;
            f5 = f24;
        }
        if (z && this.current) {
            postInvalidateDelayed(j2);
        }
    }

    void tri(float f, float f2, float f3, float f4) {
        this.tri.rewind();
        this.tri.moveTo(f, f2 - f3);
        this.tri.lineTo((f4 * f3 * 1.5f) + f, f2);
        this.tri.lineTo(f, f2 + f3);
        this.tri.close();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String spoken() {
        HomeData homeData = this.h.d;
        StringBuilder append = new StringBuilder(Strings.t("w.media.h")).append(". ");
        if (homeData.media == null) {
            return append.append(Strings.t(homeData.mediaAccess ? "w.media.none" : "w.media.perm")).toString();
        }
        append.append(homeData.media.optBoolean("playing") ? "Playing " : "Paused ").append(homeData.media.optString("title"));
        if (!homeData.media.optString("artist").isEmpty()) {
            append.append(" by ").append(homeData.media.optString("artist"));
        }
        if (!homeData.media.optString("app").isEmpty()) {
            append.append(", in ").append(homeData.media.optString("app"));
        }
        return append.append(".").toString();
    }
}
