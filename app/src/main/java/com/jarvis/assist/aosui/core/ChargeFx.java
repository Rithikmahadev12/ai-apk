package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class ChargeFx {
    public static final String KEY = "aos.chargefx";
    public static final long MS = 2600;

    private ChargeFx() {
    }

    public static boolean on(String str) {
        return !"0".equals(str);
    }

    public static boolean play(String str, boolean z, boolean z2, boolean z3) {
        return on(str) && !z && z2 && z3;
    }

    public static boolean fast(long j) {
        return Math.abs(j) > 1500000;
    }

    public static float fill(long j, float f) {
        float max = 1.0f - Math.max(0.0f, Math.min(1.0f, ((float) j) / 1400.0f));
        return f * (1.0f - ((max * max) * max));
    }
}
