package com.jarvis.assist.aosui.core;

import java.lang.reflect.Array;

/* loaded from: /tmp/augmaos/classes.dex */
public final class RingMath {
    private RingMath() {
    }

    public static float[][] even(int i, float f, float f2, float f3) {
        float[][] fArr = (float[][]) Array.newInstance((Class<?>) Float.TYPE, Math.max(0, i), 3);
        if (i <= 0) {
            return fArr;
        }
        float f4 = 360.0f / i;
        float min = Math.min(f2, 0.4f * f4);
        for (int i2 = 0; i2 < i; i2++) {
            float f5 = f4 / 2.0f;
            float f6 = ((f + f3) + (i2 * f4)) - f5;
            float f7 = min / 2.0f;
            fArr[i2][0] = f6 + f7;
            fArr[i2][1] = (f6 + f4) - f7;
            fArr[i2][2] = f6 + f5;
        }
        return fArr;
    }

    public static float[] radii(float f, float f2, float f3) {
        float max = Math.max(30.0f + f3, Math.min(f2, (((f / 2.0f) - 6.0f) - 26.0f) - 6.0f));
        float f4 = 6.0f + max;
        return new float[]{Math.max(f3, max - 50.0f), max, f4, 26.0f + f4};
    }

    public static int at(float f, int i, float f2, float f3) {
        if (i <= 0) {
            return -1;
        }
        float f4 = 360.0f / i;
        return Math.min(i - 1, (int) (((((((f - f2) - f3) + (f4 / 2.0f)) % 360.0f) + 720.0f) % 360.0f) / f4));
    }
}
