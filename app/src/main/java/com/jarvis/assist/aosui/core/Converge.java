package com.jarvis.assist.aosui.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Converge {
    static final Path[] LOOPS = new Path[9];

    static {
        for (int i = 0; i < 9; i++) {
            float f = i / 8.0f;
            float f2 = 0.44f - (0.26f * f);
            float f3 = 2.6f * f;
            float f4 = (1.0f - (f * 0.5f)) * 0.13f;
            Path path = new Path();
            for (int i2 = 0; i2 <= 120; i2++) {
                double d = ((i2 / 120.0d) * 3.141592653589793d * 2.0d) + f3;
                double cos = (float) (f2 * ((f4 * Math.cos(3.0d * d)) + 1.0d));
                float cos2 = (float) (Math.cos(d) * cos);
                float sin = (float) (cos * Math.sin(d));
                if (i2 == 0) {
                    path.moveTo(cos2, sin);
                } else {
                    path.lineTo(cos2, sin);
                }
            }
            path.close();
            LOOPS[i] = path;
        }
    }

    public static void draw(Canvas canvas, Paint paint, float f, float f2, float f3, float f4, int i, int i2, int i3) {
        int save = canvas.save();
        canvas.translate(f, f2);
        canvas.rotate(f4);
        canvas.scale(f3, f3);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStrokeJoin(Paint.Join.MITER);
        for (int i4 = 0; i4 < 9; i4++) {
            float f5 = i4 / 8.0f;
            paint.setStrokeWidth(0.016f - (0.005f * f5));
            paint.setColor(Theme.a(Theme.mix(i, i2, f5), 1.0f - (f5 * 0.45f)));
            canvas.drawPath(LOOPS[i4], paint);
        }
        paint.setStrokeWidth(0.018f);
        paint.setColor(i3);
        canvas.drawCircle(0.0f, 0.0f, 0.09f, paint);
        canvas.restoreToCount(save);
    }
}
