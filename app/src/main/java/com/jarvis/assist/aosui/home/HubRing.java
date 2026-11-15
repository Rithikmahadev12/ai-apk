package com.jarvis.assist.aosui.home;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import com.jarvis.assist.aosui.core.Shapes;
import com.jarvis.assist.aosui.core.Theme;

/* loaded from: /tmp/augmaos/classes.dex */
final class HubRing {
    private HubRing() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void draw(Canvas canvas, Paint paint, RectF rectF, float[] fArr, HomeCtx homeCtx, float f, float f2, Shader shader, boolean z) {
        char c;
        Paint paint2;
        float f3;
        float f4;
        Theme theme = homeCtx.th;
        HomeData homeData = homeCtx.d;
        char c2 = 0;
        boolean z2 = homeCtx.arcs != null;
        ring(canvas, paint, f, f2, 84.0f, BackLayer.ma(theme.LINE, 0.8f), 1.0f);
        if (!z || z2) {
            c = 1;
        } else {
            c = 1;
            arc(canvas, paint, rectF, f, f2, 89.0f, 30.0f, 150.0f, theme.LINE2, 1.0f);
            arc(canvas, paint, rectF, f, f2, 89.0f, 62.0f, 118.0f, BackLayer.ma(theme.BLUE, 0.8f), 1.6f);
            arc(canvas, paint, rectF, f, f2, 79.0f, 150.0f, 200.0f, theme.LINE, 1.0f);
            arc(canvas, paint, rectF, f, f2, 79.0f, -20.0f, 30.0f, theme.LINE, 1.0f);
        }
        ring(canvas, paint, f, f2, 74.0f, theme.LINE, 1.0f);
        float f5 = z2 ? 90.0f : -90.0f;
        float f6 = z2 ? homeCtx.elapsed : 360.0f * homeData.dayF;
        if (z2 && homeCtx.dayArc != null) {
            arc(canvas, paint, rectF, f, f2, 84.0f, homeCtx.dayArc[0], homeCtx.dayArc[0] + homeCtx.dayArc[c], Theme.a(theme.BLUE, 0.45f), 1.4f);
        }
        if (!z2 || homeCtx.sleepArc == null) {
            paint2 = paint;
        } else {
            paint2 = paint;
            arc(canvas, paint2, rectF, f, f2, 79.0f, homeCtx.sleepArc[0], homeCtx.sleepArc[0] + homeCtx.sleepArc[c], Theme.a(theme.BLUE, 0.5f), 3.2f);
        }
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(1.8f);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setShader(shader);
        paint2.setColor(-16777216);
        float f7 = 74.0f;
        rectF.set(f - 74.0f, f2 - 74.0f, f + 74.0f, f2 + 74.0f);
        if (f6 > 0.0f) {
            Paint paint3 = paint2;
            canvas.drawArc(rectF, f5, f6, false, paint3);
            f4 = f5;
            f3 = f6;
            paint2 = paint3;
        } else {
            f3 = f6;
            f4 = f5;
        }
        paint2.setShader(null);
        paint2.setStrokeCap(Paint.Cap.BUTT);
        if (z2) {
            float[][] fArr2 = homeCtx.arcs;
            int length = fArr2.length;
            int i = 0;
            while (i < length) {
                float[] fArr3 = fArr2[i];
                float f8 = f7;
                arc(canvas, paint2, rectF, f, f2, 74.0f, fArr3[c2], fArr3[c] + fArr3[c2], fArr3[2] > 0.0f ? theme.BLUE : theme.INK, 4.2f);
                i++;
                f7 = f8;
                length = length;
                c2 = c2;
            }
        }
        char c3 = c2;
        Shapes.pt(f, f2, f7, f3 + f4, fArr);
        if (z2) {
            paint2.setStyle(Paint.Style.FILL);
            paint2.setColor(theme.BG);
            canvas.drawCircle(fArr[c3], fArr[c], 4.2f, paint2);
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeWidth(1.6f);
            paint2.setColor(theme.RED);
            canvas.drawCircle(fArr[c3], fArr[c], 4.2f, paint2);
            return;
        }
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(theme.RED);
        canvas.drawCircle(fArr[c3], fArr[c], 2.6f, paint2);
    }

    static void ring(Canvas canvas, Paint paint, float f, float f2, float f3, int i, float f4) {
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(f4);
        paint.setColor(i);
        canvas.drawCircle(f, f2, f3, paint);
    }

    static void arc(Canvas canvas, Paint paint, RectF rectF, float f, float f2, float f3, float f4, float f5, int i, float f6) {
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(f6);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(i);
        rectF.set(f - f3, f2 - f3, f + f3, f2 + f3);
        canvas.drawArc(rectF, f4, f5 - f4, false, paint);
        paint.setStrokeCap(Paint.Cap.BUTT);
    }
}
