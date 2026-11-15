package com.jarvis.assist.aosui.core;

import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class OsoWheel {
    public static final float CENTRE = 56.0f;
    public static final int CENTRE_HIT = 0;
    public static final float DETENT = 15.0f;
    public static final float INNER = 96.0f;
    public static final int NONE = -1;
    public static final float OUTER = 175.0f;
    public static final float VB = 340.0f;
    public static final String[] OUT = {"MENU", "LIKE", "NEXT", "OUTPUT", "PLAY", "EQ", "PREV", "QUEUE"};
    public static final String[] IN = {"VOL", "SEEK", "SHUF", "REP"};

    private OsoWheel() {
    }

    public static float angle(float f, float f2) {
        float degrees = (float) Math.toDegrees(Math.atan2(f, -f2));
        return degrees < 0.0f ? degrees + 360.0f : degrees;
    }

    public static float wrap(float f) {
        float f2 = f % 360.0f;
        if (f2 > 180.0f) {
            f2 -= 360.0f;
        }
        return f2 <= -180.0f ? f2 + 360.0f : f2;
    }

    public static int detent(float f) {
        return Math.round(f / 15.0f);
    }

    public static int outerIndex(float f) {
        return ((int) (((((f + 22.5f) % 360.0f) + 360.0f) % 360.0f) / 45.0f)) % 8;
    }

    public static int innerIndex(float f) {
        return ((int) (((((f + 45.0f) % 360.0f) + 360.0f) % 360.0f) / 90.0f)) % 4;
    }

    public static int hit(float f, float f2) {
        float hypot = (float) Math.hypot(f, f2);
        float angle = angle(f, f2);
        if (hypot < 56.0f) {
            return 0;
        }
        if (hypot < 96.0f) {
            return innerIndex(angle) + 10;
        }
        if (hypot <= 175.0f) {
            return outerIndex(angle) + 20;
        }
        return -1;
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Dial {
        int det;
        public float rot;

        public int turn(float f) {
            this.rot += f;
            int detent = OsoWheel.detent(this.rot);
            int i = detent - this.det;
            this.det = detent;
            return i;
        }
    }

    public static long seek(long j, int i, long j2) {
        return Math.max(0L, Math.min(j2, j + (i * 5000)));
    }

    public static int vol(int i, int i2, int i3) {
        return Math.max(0, Math.min(i3, i + i2));
    }

    public static String fmt(long j) {
        long max = Math.max(0L, j / 1000);
        return String.format(Locale.ROOT, "%02d:%02d", Long.valueOf(max / 60), Long.valueOf(max % 60));
    }

    public static String pad(int i) {
        return String.format(Locale.ROOT, "%02d", Integer.valueOf(i));
    }

    public static int upNextFrom(long[] jArr, long j) {
        int i = 0;
        while (i < jArr.length) {
            long j2 = jArr[i];
            i++;
            if (j2 == j) {
                return i;
            }
        }
        return 0;
    }

    public static int queueWindow(int i, int i2, boolean z) {
        if (z) {
            return Math.max(0, Math.min(i - 1, Math.max(0, i2 - 3)));
        }
        return 0;
    }

    public static float bar(int i, int i2) {
        double abs = i + Math.abs(i2 % 97);
        return ((float) Math.abs((Math.sin(0.37d * abs) * 14.0d) + (Math.sin(0.11d * abs) * 9.0d) + (Math.sin(abs * 1.3d) * 4.0d))) + 6.0f;
    }

    public static float marker(float f, int i, float f2) {
        return ((i * Math.max(0.0f, Math.min(1.0f, f)) * 3.0f) + 3.0f) * (f2 / 330.0f);
    }

    public static long pos(long j, long j2, boolean z, long j3, long j4) {
        long j5 = j + ((!z || j2 <= 0) ? 0L : j4 - j2);
        return j3 > 0 ? Math.min(j3, j5) : j5;
    }
}
