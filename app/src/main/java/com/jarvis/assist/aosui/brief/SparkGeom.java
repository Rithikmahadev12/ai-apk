package com.jarvis.assist.aosui.brief;

/* loaded from: /tmp/augmaos/classes.dex */
public final class SparkGeom {
    private SparkGeom() {
    }

    public static float[] area(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = f4 + (f3 / 2.0f);
        float f8 = 1.0f + f7;
        return new float[]{f7, f7, Math.max(f8, (f - f7) - (f5 > 0.0f ? f5 + f6 : 0.0f)), Math.max(f8, f2 - f7)};
    }

    public static float labelY(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3 - 1.0f, f + (0.35f * f2)));
    }
}
