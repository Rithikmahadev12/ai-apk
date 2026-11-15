package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaTransit {
    public static final float CONTRACT_SPEED = 2.8f;
    public static final float EXPAND_SPEED = 1.8f;
    public static final float HEADER_SHARE = 0.6f;

    private AoaTransit() {
    }

    public static long revealDelay(boolean z) {
        return z ? 30L : 60L;
    }

    public static long revealMs(boolean z) {
        return z ? 220L : 340L;
    }

    public static long blockDelay(float f, float f2, boolean z) {
        return (z || f2 <= 0.0f) ? revealDelay(z) : revealDelay(false) + Math.round(Math.max(0.0f, Math.min(1.0f, f / f2)) * 160.0f);
    }

    public static long blockMs(boolean z) {
        return z ? 180L : 220L;
    }

    public static long openMs(boolean z) {
        return z ? revealDelay(true) + revealMs(true) : Math.max(revealDelay(false) + revealMs(false), blockDelay(1.0f, 1.0f, false) + blockMs(false));
    }

    public static long collapseMs(boolean z) {
        return z ? 180L : 260L;
    }

    public static float radius(float f, float f2, float f3, float f4) {
        double d = f;
        double d2 = f2;
        double d3 = f3 - f;
        double d4 = f4 - f2;
        return (float) Math.max(Math.max(Math.hypot(d, d2), Math.hypot(d3, d2)), Math.max(Math.hypot(d, d4), Math.hypot(d3, d4)));
    }

    public static float reversed(float f, float f2, int i) {
        return Math.max(0.0f, i - (f * f2));
    }
}
