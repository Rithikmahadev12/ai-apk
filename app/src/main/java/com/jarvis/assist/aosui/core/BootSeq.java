package com.jarvis.assist.aosui.core;

import com.jarvis.assist.aosui.ai.KeyCheck;

/* loaded from: /tmp/augmaos/classes.dex */
public final class BootSeq {
    public static final int FADE_MS = 400;
    public static final int HOLD_MS = 420;
    public static final String KEY = "aos.bootfx";
    public static final int N = 6;
    public static final int SHORT_DIV = 4;
    public static final int STEP_MS = 600;
    public static final int UN = 2;
    public static final int UN_FADE_MS = 350;
    public static final int UN_HOLD_MS = 300;
    public static final int UN_STEP_MS = 600;
    public static final String[] STEP_KEYS = {"boot.s1", "boot.s2", "boot.s3", "boot.s4", "boot.s5", "boot.s6"};
    public static final String[] UNFOLD_KEYS = {"boot.u1", "boot.u2"};

    private BootSeq() {
    }

    public static int n(boolean z) {
        return z ? 2 : 6;
    }

    public static int stepAt(long j, boolean z, boolean z2) {
        return !z2 ? stepAt(j, z) : (int) Math.max(0L, Math.min(1L, j / 600));
    }

    public static float progress(long j, boolean z, boolean z2) {
        return !z2 ? progress(j, z) : Math.max(0.0f, Math.min(1.0f, ((float) j) / 1200.0f));
    }

    public static long leaveAt(boolean z, boolean z2) {
        return z2 ? KeyCheck.VERIFY_RETRY_MS : leaveAt(z);
    }

    public static long totalMs(boolean z, boolean z2) {
        return z2 ? leaveAt(false, true) + 350 : totalMs(z);
    }

    public static float fade(long j, long j2, boolean z, boolean z2) {
        if (!z2) {
            return fade(j, j2, z);
        }
        if (j2 < 0 || j < j2) {
            return 0.0f;
        }
        return Math.min(1.0f, ((float) (j - j2)) / 350.0f);
    }

    public static boolean on(String str) {
        return !"0".equals(str);
    }

    public static boolean bootOn(String str, boolean z, boolean z2, String str2) {
        return on(str) && z && !z2 && !"BATTERY".equals(str2);
    }

    static int step(boolean z) {
        if (z) {
            return FxModel.TEST_GAP_MS;
        }
        return 600;
    }

    public static int stepAt(long j, boolean z) {
        return (int) Math.max(0L, Math.min(5L, j / step(z)));
    }

    public static float progress(long j, boolean z) {
        return Math.max(0.0f, Math.min(1.0f, ((float) j) / (step(z) * 6)));
    }

    public static long leaveAt(boolean z) {
        return (step(z) * 6) + (z ? 105 : HOLD_MS);
    }

    public static long totalMs(boolean z) {
        return leaveAt(z) + (z ? 200 : 400);
    }

    public static float fade(long j, long j2, boolean z) {
        if (j2 < 0 || j < j2) {
            return 0.0f;
        }
        return Math.min(1.0f, ((float) (j - j2)) / (z ? 200 : 400));
    }

    public static int points(String str) {
        return ("LIGHT".equals(str) || "BATTERY".equals(str)) ? 40 : 72;
    }
}
