package com.jarvis.assist.aosui.home;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import com.jarvis.assist.aosui.core.Theme;

/* loaded from: /tmp/augmaos/classes.dex */
final class Hud {
    static final int ICE = -1774607;
    static final Path line = new Path();
    static final Path area = new Path();

    private Hud() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SweepGradient opal(float f, float f2, Theme theme) {
        SweepGradient sweepGradient = new SweepGradient(f, f2, new int[]{theme.RED, -1774607, theme.BLUE, theme.BLUE}, new float[]{0.0f, 0.45f, 0.9f, 1.0f});
        Matrix matrix = new Matrix();
        matrix.setRotate(-90.0f, f, f2);
        sweepGradient.setLocalMatrix(matrix);
        return sweepGradient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void bar(Canvas canvas, Paint paint, RectF rectF, float f, float f2, float f3, float f4, Theme theme, float f5) {
        if (f3 <= 4.0f) {
            return;
        }
        paint.setShader(null);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Theme.a(theme.LINE2, f5));
        canvas.drawRect(f, f2 - 0.5f, f + f3, f2 + 0.5f, paint);
        float max = f + (Math.max(0.0f, Math.min(1.0f, f4)) * f3);
        if (f4 <= 0.0f) {
            return;
        }
        paint.setShader(new LinearGradient(f, 0.0f, max, 0.0f, new int[]{theme.RED, -1774607, theme.BLUE}, new float[]{0.0f, 0.55f, 1.0f}, Shader.TileMode.CLAMP));
        paint.setAlpha(Math.round(255.0f * f5));
        rectF.set(f, f2 - 1.2f, max, f2 + 1.2f);
        canvas.drawRoundRect(rectF, 1.2f, 1.2f, paint);
        paint.setShader(null);
        paint.setAlpha(255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void chart(Canvas canvas, Paint paint, float[] fArr, float f, float f2, float f3, float f4, int i, Theme theme, float f5) {
        float f6;
        float f7;
        float f8 = Float.MAX_VALUE;
        float f9 = -3.4028235E38f;
        int i2 = 0;
        for (float f10 : fArr) {
            if (!Float.isNaN(f10)) {
                float min = Math.min(f8, f10);
                i2++;
                f9 = Math.max(f9, f10);
                f8 = min;
            }
        }
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        float f11 = 1.0f;
        paint.setStrokeWidth(1.0f);
        paint.setColor(Theme.a(theme.LINE2, f5));
        float f12 = f2 + f4;
        float f13 = f + f3;
        canvas.drawLine(f + 3.0f, f12, f13 - 3.0f, f12, paint);
        canvas.drawCircle(f + 1.5f, f12, 1.5f, paint);
        canvas.drawCircle(f13 - 1.5f, f12, 1.5f, paint);
        if (i2 < 2) {
            return;
        }
        float max = Math.max(0.001f, f9 - f8);
        float f14 = f2 + (0.12f * f4);
        float f15 = 0.8f * f4;
        line.rewind();
        area.rewind();
        float f16 = f;
        float f17 = f12;
        float f18 = 0.0f;
        float f19 = 0.0f;
        boolean z = true;
        int i3 = 0;
        while (true) {
            float f20 = f11;
            if (i3 < fArr.length) {
                if (!Float.isNaN(fArr[i3])) {
                    float max2 = f + 4.0f + (((f3 - 8.0f) * i3) / Math.max(1, fArr.length - 1));
                    float f21 = f14 + ((f20 - ((fArr[i3] - f8) / max)) * f15);
                    if (z) {
                        line.moveTo(max2, f21);
                        area.moveTo(max2, f12);
                        area.lineTo(max2, f21);
                        f6 = max2;
                        f7 = f21;
                        z = false;
                    } else {
                        float f22 = (f18 + max2) / 2.0f;
                        f6 = max2;
                        f7 = f21;
                        line.cubicTo(f22, f19, f22, f7, f6, f21);
                        area.cubicTo(f22, f19, f22, f7, f6, f7);
                    }
                    f17 = f7;
                    f19 = f17;
                    f16 = f6;
                    f18 = f16;
                }
                i3++;
                f11 = f20;
            } else {
                area.lineTo(f16, f12);
                area.close();
                int round = Math.round(255.0f * f5);
                paint.setStyle(Paint.Style.FILL);
                paint.setShader(new LinearGradient(0.0f, f14, 0.0f, f12, new int[]{Theme.a(i, 0.38f), Theme.a(i, 0.0f)}, (float[]) null, Shader.TileMode.CLAMP));
                paint.setAlpha(round);
                canvas.drawPath(area, paint);
                paint.setShader(null);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(1.4f);
                paint.setColor(Theme.a(i, f5));
                canvas.drawPath(line, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Theme.a(i, 0.25f * f5));
                canvas.drawCircle(f16, f17, 5.0f, paint);
                paint.setColor(Theme.a(-1774607, f5));
                canvas.drawCircle(f16, f17, 2.0f, paint);
                return;
            }
        }
    }
}
