package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class PagerMath {
    private PagerMath() {
    }

    public static float knob(int i, int i2, float f, float f2) {
        float f3;
        if (i2 > 1) {
            f3 = Math.max(0, Math.min(r2, i)) / (i2 - 1);
        } else {
            f3 = 0.0f;
        }
        return f + (f3 * (f2 - f));
    }

    public static float clamp(float f, float f2, float f3) {
        return Math.max(Math.min(f2, f3), Math.min(Math.max(f2, f3), f));
    }

    public static int page(float f, int i, float f2, float f3) {
        if (i <= 1 || f3 == f2) {
            return 0;
        }
        int i2 = i - 1;
        return Math.max(0, Math.min(i2, Math.round(((clamp(f, f2, f3) - f2) / (f3 - f2)) * i2)));
    }
}
