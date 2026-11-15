package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AwSize {
    private AwSize() {
    }

    public static int[] dp(int i, int i2, float f) {
        if (f <= 0.0f) {
            f = 1.0f;
        }
        int max = Math.max(1, Math.round(i / f));
        int max2 = Math.max(1, Math.round(i2 / f));
        return new int[]{max, max2, max, max2};
    }

    public static boolean changed(int[] iArr, int[] iArr2) {
        if (iArr == null) {
            return true;
        }
        for (int i = 0; i < 4; i++) {
            if (Math.abs(iArr[i] - iArr2[i]) >= 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean usable(int i, int i2) {
        return i > 0 && i2 > 0;
    }
}
