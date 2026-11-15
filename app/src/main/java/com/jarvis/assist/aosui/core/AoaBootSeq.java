package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaBootSeq {
    public static final int FADE = 400;

    private AoaBootSeq() {
    }

    public static float k(boolean z, boolean z2) {
        if (z) {
            return 0.55f;
        }
        return z2 ? 0.6f : 1.0f;
    }

    public static long total(boolean z, boolean z2) {
        if (z) {
            return 1850L;
        }
        return Math.round(k(false, z2) * 2700.0f);
    }

    public static long leave(boolean z, boolean z2) {
        return total(z, z2) - 400;
    }

    static float span(float f, float f2, float f3) {
        return UnlockAnim.ease((f - f2) / (f3 - f2));
    }

    static float lin(float f, float f2, float f3) {
        return Math.max(0.0f, Math.min(1.0f, (f - f2) / (f3 - f2)));
    }

    public static float grid(long j, boolean z, boolean z2) {
        return lin((float) j, 0.0f, k(z, z2) * 300.0f);
    }

    public static float asm(long j, boolean z, boolean z2) {
        float k = k(z, z2);
        return lin((float) j, 150.0f * k, k * 1350.0f);
    }

    public static float point(float f, int i) {
        return UnlockAnim.ease((f - ((((float) ((i * 2654435761L) & 65535)) / 65535.0f) * 0.35f)) / 0.65f);
    }

    public static float ignite(long j, boolean z, boolean z2) {
        float k = k(z, z2);
        return span((float) j, 900.0f * k, k * 1200.0f);
    }

    public static float rim(long j, boolean z, boolean z2) {
        float k = k(z, z2);
        return span((float) j, 1100.0f * k, k * 1600.0f);
    }

    public static float fill(long j, boolean z, boolean z2) {
        float k = k(z, z2);
        return span((float) j, 1300.0f * k, k * 1700.0f);
    }

    public static float readouts(long j, boolean z, boolean z2) {
        float k = k(z, z2);
        long leave = leave(z, z2);
        float f = (float) j;
        return j < leave ? span(f, 200.0f * k, k * 500.0f) : 1.0f - lin(f, (float) leave, (float) (leave + 300));
    }

    public static float progress(long j, boolean z, boolean z2) {
        return lin((float) j, 0.0f, (float) leave(z, z2));
    }

    public static int step(long j, boolean z, boolean z2, int i) {
        return Math.max(0, Math.min(i - 1, (int) (progress(j, z, z2) * i)));
    }

    public static float fade(long j, long j2) {
        if (j2 < 0 || j < j2) {
            return 0.0f;
        }
        return Math.min(1.0f, ((float) (j - j2)) / 400.0f);
    }
}
