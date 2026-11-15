package com.jarvis.assist.aosui.home;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import com.jarvis.assist.aosui.core.UnitsMath;

/* loaded from: /tmp/augmaos/classes.dex */
final class Txt {
    static final Rect R = new Rect();
    float asc;
    float cap;
    float desc;
    final float ls;
    final Paint p = new Paint(129);
    final float size;
    float xh;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Txt(Typeface typeface, float f, float f2) {
        this.size = f;
        this.ls = f2;
        this.p.setTypeface(typeface);
        this.p.setTextSize(f);
        if (f > 0.0f) {
            this.p.setLetterSpacing(f2 / f);
        }
        metrics();
    }

    void metrics() {
        Paint.FontMetrics fontMetrics = this.p.getFontMetrics();
        this.asc = -fontMetrics.ascent;
        this.desc = fontMetrics.descent;
        this.p.getTextBounds("x", 0, 1, R);
        this.xh = R.height();
        this.p.getTextBounds("H", 0, 1, R);
        this.cap = R.height();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float line() {
        return this.asc + this.desc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float w(String str) {
        return this.p.measureText(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Txt align(Paint.Align align) {
        this.p.setTextAlign(align);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void draw(Canvas canvas, String str, float f, float f2, int i) {
        this.p.setColor(i);
        canvas.drawText(str, f, f2, this.p);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float tracked(Canvas canvas, String str, float f, float f2, int i) {
        this.p.setColor(i);
        float letterSpacing = this.p.getLetterSpacing();
        this.p.setLetterSpacing(0.0f);
        float f3 = f;
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i2 + 1;
            Canvas canvas2 = canvas;
            String str2 = str;
            canvas2.drawText(str2, i2, i3, f3, f2, this.p);
            f3 += this.p.measureText(str2, i2, i3) + this.ls;
            canvas = canvas2;
            str = str2;
            i2 = i3;
        }
        this.p.setLetterSpacing(letterSpacing);
        return f3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void drawFit(Canvas canvas, String str, float f, float f2, int i, float f3) {
        float w = w(str);
        if (w <= f3 || f3 <= 0.0f) {
            draw(canvas, str, f, f2, i);
            return;
        }
        float textSize = this.p.getTextSize();
        this.p.setTextSize(UnitsMath.fitScale(w, f3, 0.72f) * textSize);
        float measureText = this.p.measureText(str);
        if (measureText > f3) {
            this.p.setTextScaleX(f3 / measureText);
        }
        draw(canvas, str, f, f2, i);
        this.p.setTextSize(textSize);
        this.p.setTextScaleX(1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mid(Canvas canvas, String str, float f, float f2, int i) {
        draw(canvas, str, f, f2 + (this.xh / 2.0f), i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float base(float f) {
        return f + this.asc;
    }
}
