package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class MemPolicy {
    public static final int BACKGROUND = 40;
    public static final int CLEAR_BITMAPS = 8;
    public static final int CLEAR_ICONS = 64;
    public static final int COMPLETE = 80;
    public static final int DROP_FAR_WIDGETS = 32;
    public static final int DROP_LAYERS = 2;
    public static final int HALF_ICONS = 4;
    public static final int MODERATE = 60;
    public static final int RELEASE_VOICE = 16;
    public static final int RUNNING_CRITICAL = 15;
    public static final int RUNNING_LOW = 10;
    public static final int RUNNING_MODERATE = 5;
    public static final int STOP_CLOCK = 1;
    public static final int TRIM_CHAT = 128;
    public static final int UI_HIDDEN = 20;

    private MemPolicy() {
    }

    public static int actions(int i) {
        int i2 = i >= 20 ? 7 : 0;
        if (i >= 40) {
            i2 |= 184;
        }
        if (i >= 80) {
            i2 |= 64;
        }
        if (i == 10) {
            i2 |= 4;
        }
        return i == 15 ? i2 | 172 : i2;
    }

    public static boolean has(int i, int i2) {
        return (i & i2) != 0;
    }

    public static int budget(int i, int i2) {
        return (int) Math.max(1048576L, Math.min(67108864L, ((Math.max(16, i) * 1024) * 1024) / Math.max(1, i2)));
    }

    public static int iconBudget(int i) {
        return budget(i, 16);
    }

    public static int bitmapBudget(int i) {
        return budget(i, 32);
    }

    public static boolean keepPage(int i, int i2, int i3) {
        return Math.abs(i - i2) <= i3;
    }

    public static boolean slideDrawn(int i, float f) {
        return Math.abs(((float) i) - f) < 1.0f;
    }

    public static int sample(int i, int i2, int i3) {
        int max = Math.max(i, i2);
        int i4 = 1;
        if (i3 <= 0) {
            return 1;
        }
        while (true) {
            int i5 = i4 * 2;
            if (max / i5 < i3) {
                return i4;
            }
            i4 = i5;
        }
    }
}
