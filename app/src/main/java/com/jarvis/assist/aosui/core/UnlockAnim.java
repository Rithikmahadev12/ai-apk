package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class UnlockAnim {
    public static final String KEY = "aos.unlockfx";
    public static final long WINDOW = 2000;

    private UnlockAnim() {
    }

    public static boolean on(String str) {
        return !"0".equals(str);
    }

    public static boolean should(String str, boolean z, long j, long j2) {
        return on(str) && !z && j > 0 && j2 >= j && j2 - j <= 2000;
    }

    public static int total(boolean z) {
        return z ? 380 : 840;
    }

    public static float ease(float f) {
        float max = 1.0f - Math.max(0.0f, Math.min(1.0f, f));
        return 1.0f - ((max * max) * max);
    }

    static float span(float f, float f2, float f3) {
        return ease((f - f2) / f3);
    }

    public static float orb(float f, boolean z) {
        return span(f, 0.0f, z ? 220.0f : 260.0f);
    }

    public static float flash(float f, boolean z) {
        float f2 = f / (z ? 220.0f : 260.0f);
        if (f2 <= 0.0f || f2 >= 1.0f) {
            return 0.0f;
        }
        return f2 < 0.4f ? f2 / 0.4f : (1.0f - f2) / 0.6f;
    }

    public static float ring(int i, float f, boolean z) {
        float f2;
        float f3;
        if (z) {
            f2 = 120.0f;
            f3 = 260.0f;
        } else {
            f2 = (i * 90) + 140;
            f3 = 360.0f;
        }
        return span(f, f2, f3);
    }

    public static float item(float f, float f2, float f3, boolean z) {
        if (z) {
            return span(f3, 120.0f, 260.0f);
        }
        return span(f3, ((f2 > 0.0f ? Math.max(0.0f, Math.min(1.0f, f / f2)) : 0.0f) * 240.0f) + 220.0f, 380.0f);
    }

    public static boolean done(float f, boolean z) {
        return f >= ((float) total(z));
    }
}
