package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.PathInterpolator;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.VirtualA11y;
import java.util.Locale;
import java.util.function.Consumer;

/* loaded from: /tmp/augmaos/classes.dex */
public abstract class WidgetView extends View {
    static final int MAXH = 16;
    protected float H;
    protected float W;
    final VirtualA11y a11y;
    protected float alpha;
    protected boolean animRows;
    protected Txt big;
    long changedAt;
    View companion;
    boolean current;
    protected boolean flexFirst;
    Consumer<String> go;
    protected final HomeCtx h;
    final String[] ha;
    final float[] hb;
    protected Txt hero;
    final String[] hl;
    String lastSpoken;
    protected Txt m9;
    int nh;
    int num;
    protected final Paint p;
    protected float padB;
    protected float padL;
    protected float padR;
    protected float padT;
    protected final RectF rf;
    final float[] rowHs;
    final float[] rowY;
    protected Txt sb13;
    protected Txt sv15;
    static final PathInterpolator EASE = new PathInterpolator(0.2f, 0.8f, 0.2f, 1.0f);
    static final PathInterpolator CSS_EASE = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);

    protected abstract void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4);

    public abstract String id();

    protected abstract float rowH(int i);

    protected abstract int rows();

    public abstract String source();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void slot(float[] fArr) {
        fArr[0] = this.padL;
        fArr[1] = this.padT + headH() + 6.0f;
        fArr[2] = this.W - this.padR;
        fArr[3] = (this.H - this.padB) - 2.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WidgetView(Context context, final HomeCtx homeCtx) {
        super(context);
        this.p = new Paint(1);
        this.rf = new RectF();
        this.alpha = 1.0f;
        this.animRows = true;
        this.padT = 12.0f;
        this.padR = 14.0f;
        this.padB = 16.0f;
        this.padL = 14.0f;
        this.lastSpoken = "";
        this.hb = new float[64];
        this.ha = new String[16];
        this.hl = new String[16];
        this.rowY = new float[8];
        this.rowHs = new float[8];
        this.h = homeCtx;
        setImportantForAccessibility(1);
        this.a11y = new VirtualA11y(this, new VirtualA11y.Src() { // from class: com.jarvis.assist.aosui.home.WidgetView.1
            @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
            public int count() {
                if (WidgetView.this.current) {
                    return WidgetView.this.nh + 1;
                }
                return 0;
            }

            @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
            public void bounds(int i, Rect rect) {
                float f = homeCtx.g.S;
                if (i == 0) {
                    rect.set(0, 0, WidgetView.this.getWidth(), WidgetView.this.getHeight());
                } else {
                    int i2 = (i - 1) * 4;
                    rect.set(Math.round(WidgetView.this.hb[i2] * f), Math.round(WidgetView.this.hb[i2 + 1] * f), Math.round(WidgetView.this.hb[i2 + 2] * f), Math.round(WidgetView.this.hb[i2 + 3] * f));
                }
            }

            @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
            public String label(int i) {
                return i == 0 ? WidgetView.this.spoken() : WidgetView.this.hl[i - 1];
            }

            @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
            public boolean click(int i) {
                String str;
                if (i == 0) {
                    str = WidgetView.this.nh > 0 ? WidgetView.this.ha[0] : null;
                } else {
                    str = WidgetView.this.ha[i - 1];
                }
                if (str == null || WidgetView.this.go == null) {
                    return false;
                }
                WidgetView.this.go.accept(str);
                return true;
            }
        });
    }

    @Override // android.view.View
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        return this.a11y;
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.a11y.hover(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    public String spoken() {
        StringBuilder append = new StringBuilder(id().toUpperCase(Locale.ROOT)).append(". ").append(source()).append(". ");
        for (int i = 0; i < this.nh; i++) {
            append.append(this.hl[i]).append(". ");
        }
        return append.toString().trim();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a11yChanged() {
        String spoken = spoken();
        if (spoken.equals(this.lastSpoken)) {
            return;
        }
        this.lastSpoken = spoken;
        this.a11y.contentChanged();
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void bind() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trim() {
    }

    protected String readout() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String longTap(float f, float f2) {
        return null;
    }

    public String tap(float f, float f2) {
        for (int i = this.nh - 1; i >= 0; i--) {
            int i2 = i * 4;
            if (f >= this.hb[i2] && f <= this.hb[i2 + 2] && f2 >= this.hb[i2 + 1] && f2 <= this.hb[i2 + 3]) {
                return this.ha[i];
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void hit(float f, float f2, float f3, float f4, String str, String str2) {
        if (this.nh >= 16 || this.alpha < 0.5f) {
            return;
        }
        float f5 = (f2 + f4) / 2.0f;
        float max = Math.max(f4 - f2, 44.0f) / 2.0f;
        int i = this.nh * 4;
        this.hb[i] = f;
        this.hb[i + 1] = Math.max(0.0f, f5 - max);
        this.hb[i + 2] = f3;
        this.hb[i + 3] = Math.min(this.H, f5 + max);
        this.ha[this.nh] = str;
        this.hl[this.nh] = str2;
        this.nh++;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void msg(Canvas canvas, float f, float f2, float f3, float f4, String str, String str2, int i) {
        this.m9.draw(canvas, fitText(this.m9, str, f3), f, (this.m9.cap / 2.0f) + (f4 / 2.0f) + f2, col(i));
        if (str2 != null) {
            hit(f, f2, f3 + f, f4 + f2, str2, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void heroRing(Canvas canvas, float f, float f2, float f3, float f4, int i, String str, String str2, String str3, int i2) {
        float ringSz = ringSz();
        float f5 = f2 + (f3 / 2.0f);
        ringI(canvas, f + (ringSz / 2.0f), f5, ringSz, f4, i, str);
        float f6 = f + ringSz + 12.0f;
        float line = f5 - ((((this.hero.line() * 0.82f) + 2.0f) + this.m9.line()) / 2.0f);
        this.hero.draw(canvas, str2, f6, this.hero.cap + line + (((this.hero.line() * 0.82f) - this.hero.cap) / 2.0f), col(th().INK));
        this.m9.draw(canvas, str3, f6, line + (this.hero.line() * 0.82f) + 2.0f + this.m9.asc, col(i2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float heroRingH() {
        return Math.max(ringSz(), (this.hero.line() * 0.82f) + 2.0f + this.m9.line());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void styles() {
        float f = this.h.zoom;
        float f2 = 17.0f * f;
        this.big = new Txt(Fonts.RAJ_400, f2, 0.06f * f2).align(Paint.Align.RIGHT);
        this.m9 = new Txt(Fonts.MONO, this.h.L(9.0f, 10.0f), this.h.L(9.0f, 10.0f) * 0.14f);
        this.sv15 = new Txt(Fonts.RAJ_600, 15.0f * f, 0.0f).align(Paint.Align.CENTER);
        this.hero = new Txt(Fonts.RAJ_600, Math.max(20.0f, Math.min(28.0f, this.h.g.ph * 0.11f)) * f, 0.0f);
        this.sb13 = new Txt(Fonts.RAJ_400, f * 13.0f, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSize(float f, float f2) {
        this.W = f;
        this.H = f2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurrent(boolean z, long j) {
        int i = z ? 1 : 4;
        if (getImportantForAccessibility() != i) {
            setImportantForAccessibility(i);
        }
        if (z != this.current) {
            this.current = z;
            this.changedAt = j;
            this.a11y.contentChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean animating(long j) {
        return this.animRows && j - this.changedAt < (((long) rows()) * 60) + 520;
    }

    void layoutRows() {
        int min = Math.min(8, rows());
        float f = this.padT;
        float f2 = (this.H - this.padT) - this.padB;
        float f3 = 0.0f;
        for (int i = 0; i < min; i++) {
            this.rowHs[i] = rowH(i);
            f3 += this.rowHs[i];
        }
        if (this.flexFirst) {
            if (min >= 2) {
                this.rowY[0] = f;
                float f4 = f + this.rowHs[0] + 6.0f;
                float f5 = (f2 - this.rowHs[0]) - 6.0f;
                for (int i2 = 2; i2 < min; i2++) {
                    f5 -= this.rowHs[i2] + 6.0f;
                }
                this.rowHs[1] = Math.max(this.rowHs[1], f5);
                for (int i3 = 1; i3 < min; i3++) {
                    this.rowY[i3] = f4;
                    f4 += this.rowHs[i3] + 6.0f;
                }
                return;
            }
        }
        float f6 = min > 1 ? (f2 - f3) / (min - 1) : 0.0f;
        for (int i4 = 0; i4 < min; i4++) {
            this.rowY[i4] = f;
            f += this.rowHs[i4] + f6;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        Canvas canvas2;
        if (this.m9 == null) {
            return;
        }
        float f2 = this.h.g.S;
        int save = canvas.save();
        canvas.scale(f2, f2);
        layoutRows();
        this.nh = 0;
        long uptimeMillis = SystemClock.uptimeMillis();
        int i = 0;
        while (true) {
            float f3 = 1.0f;
            if (i < Math.min(8, rows())) {
                if (!this.animRows) {
                    f = 0.0f;
                } else {
                    float f4 = (float) ((uptimeMillis - this.changedAt) - (this.current ? i * 60 : 0L));
                    float max = Math.max(0.0f, Math.min(1.0f, f4 / 400.0f));
                    float max2 = Math.max(0.0f, Math.min(1.0f, f4 / 500.0f));
                    if (this.current) {
                        float interpolation = CSS_EASE.getInterpolation(max);
                        f = (1.0f - EASE.getInterpolation(max2)) * 8.0f;
                        f3 = interpolation;
                    } else {
                        f3 = 1.0f - CSS_EASE.getInterpolation(max);
                        f = EASE.getInterpolation(max2) * 8.0f;
                    }
                    if (f3 <= 0.003f) {
                        canvas2 = canvas;
                        i++;
                        canvas = canvas2;
                    }
                }
                this.alpha = f3;
                int save2 = canvas.save();
                canvas.translate(0.0f, f);
                canvas2 = canvas;
                drawRow(canvas2, i, this.padL, this.rowY[i], (this.W - this.padL) - this.padR, this.rowHs[i]);
                canvas2.restoreToCount(save2);
                i++;
                canvas = canvas2;
            } else {
                this.alpha = 1.0f;
                canvas.restoreToCount(save);
                a11yChanged();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int col(int i) {
        return this.alpha >= 1.0f ? i : Theme.a(i, ((i >>> 24) / 255.0f) * this.alpha);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Theme th() {
        return this.h.th;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float headH() {
        return this.m9.line();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00d0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void head(android.graphics.Canvas r19, float r20, float r21, float r22, java.lang.String r23, int r24, java.lang.String r25, java.lang.String r26) {
        /*
            Method dump skipped, instructions count: 266
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.WidgetView.head(android.graphics.Canvas, float, float, float, java.lang.String, int, java.lang.String, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void ringI(Canvas canvas, float f, float f2, float f3, float f4, int i, String str) {
        float f5 = (f3 / 2.0f) - 4.0f;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(col(th().LINE));
        canvas.drawCircle(f, f2, f5, this.p);
        float f6 = f - f5;
        float f7 = f2 - f5;
        float f8 = f + f5;
        float f9 = f5 + f2;
        this.rf.set(f6 - 3.5f, f7 - 3.5f, f8 + 3.5f, 3.5f + f9);
        this.p.setColor(col(th().LINE2));
        canvas.drawArc(this.rf, -60.0f, 300.0f, false, this.p);
        if (f4 > 0.0f) {
            this.p.setShader(Hud.opal(f, f2, th()));
            this.p.setAlpha(Math.round(this.alpha * 255.0f));
            this.p.setStrokeWidth(1.8f);
            this.p.setStrokeCap(Paint.Cap.ROUND);
            this.rf.set(f6, f7, f8, f9);
            canvas.drawArc(this.rf, -90.0f, Math.min(1.0f, f4) * 360.0f, false, this.p);
            this.p.setShader(null);
            this.p.setAlpha(255);
            this.p.setStrokeCap(Paint.Cap.BUTT);
        }
        Icons.draw(canvas, this.p, str, f, f2, f3 * 0.34f, col(i));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void metric(Canvas canvas, String str, float f, String str2, float f2, float f3, float f4, float f5) {
        float f6 = f3 + (f5 / 2.0f);
        float min = Math.min(0.3f * f4, 52.0f);
        float max = Math.max(28.0f, this.m9.w(str2) + 6.0f);
        this.m9.draw(canvas, fitText(this.m9, str, min - 4.0f), f2, f6 + (this.m9.cap / 2.0f), col(th().DIM));
        this.m9.align(Paint.Align.RIGHT);
        this.m9.draw(canvas, str2, f2 + f4, f6 + (this.m9.cap / 2.0f), col(th().INK));
        this.m9.align(Paint.Align.LEFT);
        Hud.bar(canvas, this.p, this.rf, f2 + min, f6, (f4 - min) - max, f, th(), this.alpha);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void chart(Canvas canvas, float[] fArr, float f, float f2, float f3, float f4, int i) {
        Hud.chart(canvas, this.p, fArr, f, f2, f3, f4, i, th(), this.alpha);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String fitText(Txt txt, String str, float f) {
        if (txt.w(str) <= f) {
            return str;
        }
        int length = str.length();
        while (length > 1) {
            if (txt.w(str.substring(0, length) + "…") <= f) {
                break;
            }
            length--;
        }
        return str.substring(0, Math.max(1, length)) + "…";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float ringSz() {
        return Math.max(24.0f, Math.min(72.0f, Math.min(this.h.g.pw * 0.13f, this.h.g.ph * 0.26f)));
    }
}
