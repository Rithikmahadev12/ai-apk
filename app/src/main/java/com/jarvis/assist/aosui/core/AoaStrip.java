package com.jarvis.assist.aosui.core;

import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaStrip {
    public static final String[] CARDS = {"agenda", "weather", "media", "health", "notifs", "battery", "alarms"};

    private AoaStrip() {
    }

    public static int wrap(int i, int i2) {
        if (i2 <= 0) {
            return 0;
        }
        return ((i % i2) + i2) % i2;
    }

    public static float wrapF(float f, int i) {
        if (i <= 0) {
            return 0.0f;
        }
        float f2 = i;
        float f3 = f % f2;
        if (f3 < 0.0f) {
            f3 += f2;
        }
        if (f3 >= f2) {
            return 0.0f;
        }
        return f3;
    }

    public static int nearest(float f, int i) {
        return wrap(Math.round(f), i);
    }

    public static String counter(int i, int i2, String str) {
        if (i2 <= 0) {
            return "";
        }
        return String.format(Locale.ROOT, "%02d/%02d · %s", Integer.valueOf(wrap(i, i2) + 1), Integer.valueOf(i2), str != null ? str.toUpperCase(Locale.ROOT) : "");
    }

    public static float thumbW(int i) {
        return 1.0f / Math.max(1, i);
    }

    public static float thumbX(float f, int i) {
        if (i <= 1) {
            return 0.0f;
        }
        return Math.min(wrapF(f, i), i - 1) / i;
    }

    public static int tapIndex(float f, int i) {
        if (i <= 0) {
            return 0;
        }
        return Math.max(0, Math.min(i - 1, (int) Math.floor(f * i)));
    }

    public static int settle(float f, float f2) {
        return f2 > 0.8f ? ((int) Math.floor(f)) + 1 : f2 < -0.8f ? ((int) Math.ceil(f)) - 1 : Math.round(f);
    }

    public static float toward(float f, int i, int i2) {
        if (i2 <= 0) {
            return f;
        }
        float wrapF = wrapF(i - f, i2);
        float f2 = i2;
        if (wrapF > f2 / 2.0f) {
            wrapF -= f2;
        }
        return f + wrapF;
    }
}
