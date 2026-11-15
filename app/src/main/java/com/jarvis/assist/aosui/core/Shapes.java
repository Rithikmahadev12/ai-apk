package com.jarvis.assist.aosui.core;

import android.graphics.Path;
import android.graphics.RectF;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Shapes {
    private Shapes() {
    }

    public static float[] pt(float f, float f2, float f3, float f4, float[] fArr) {
        double radians = Math.toRadians(f4);
        double d = f3;
        fArr[0] = (float) (f + (Math.cos(radians) * d));
        fArr[1] = (float) (f2 + (d * Math.sin(radians)));
        return fArr;
    }

    public static float[] pt(float f, float f2, float f3, float f4) {
        return pt(f, f2, f3, f4, new float[2]);
    }

    public static Path wedge(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        Path path = new Path();
        if (f7 <= 0.0f) {
            RectF rectF = new RectF(f - f4, f2 - f4, f + f4, f4 + f2);
            RectF rectF2 = new RectF(f - f3, f2 - f3, f + f3, f2 + f3);
            float f8 = f6 - f5;
            path.arcTo(rectF, f5, f8, true);
            path.arcTo(rectF2, f6, -f8);
            path.close();
            return path;
        }
        float degrees = (float) Math.toDegrees(f7 / f4);
        float degrees2 = (float) Math.toDegrees(f7 / f3);
        float f9 = f5 + degrees;
        float f10 = f6 - f5;
        path.arcTo(new RectF(f - f4, f2 - f4, f + f4, f2 + f4), f9, f10 - (degrees * 2.0f), true);
        float[] pt = pt(f, f2, f4, f6);
        float f11 = f4 - f7;
        float[] pt2 = pt(f, f2, f11, f6);
        path.quadTo(pt[0], pt[1], pt2[0], pt2[1]);
        float f12 = f3 + f7;
        float[] pt3 = pt(f, f2, f12, f6);
        path.lineTo(pt3[0], pt3[1]);
        float[] pt4 = pt(f, f2, f3, f6);
        float f13 = f6 - degrees2;
        float[] pt5 = pt(f, f2, f3, f13);
        path.quadTo(pt4[0], pt4[1], pt5[0], pt5[1]);
        path.arcTo(new RectF(f - f3, f2 - f3, f + f3, f2 + f3), f13, -(f10 - (degrees2 * 2.0f)));
        float[] pt6 = pt(f, f2, f3, f5);
        float[] pt7 = pt(f, f2, f12, f5);
        path.quadTo(pt6[0], pt6[1], pt7[0], pt7[1]);
        float[] pt8 = pt(f, f2, f11, f5);
        path.lineTo(pt8[0], pt8[1]);
        float[] pt9 = pt(f, f2, f4, f5);
        float[] pt10 = pt(f, f2, f4, f9);
        path.quadTo(pt9[0], pt9[1], pt10[0], pt10[1]);
        path.close();
        return path;
    }

    public static Path arc(float f, float f2, float f3, float f4, float f5) {
        Path path = new Path();
        path.arcTo(new RectF(f - f3, f2 - f3, f + f3, f2 + f3), f4, f5 - f4, true);
        return path;
    }
}
