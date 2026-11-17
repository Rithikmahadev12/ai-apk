package com.jarvis.assist.aosui.brief;

import java.util.Calendar;

public final class DayRing {
    static final long DAY = 86400000;
    public static final float MIN_SWEEP = 1.5f;

    private DayRing() { }

    public static float angle(float f) {
        return ((f / 1440.0f) * 360.0f) + 90.0f;
    }

    public static float angle(long j) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(j);
        return angle((cal.get(11) * 60) + cal.get(12) + (cal.get(13) / 60.0f));
    }

    public static float[] arc(long j, long j2, long j3, long j4) {
        if (j4 - j3 > DAY) { j4 = j3 + DAY; }
        long max = Math.max(j, j3);
        long min = Math.min(j2, j4);
        if (min <= max && (j != j2 || j < j3 || j >= j4)) { return null; }
        return new float[]{angle(max), Math.min(Math.max(1.5f, (((float)(min - max)) / 8.64E7f) * 360.0f), 359.9f)};
    }

    public static float[] sleep(long j, long j2, long j3) {
        if (j <= 0 || j2 <= j || j > j3) { return null; }
        return arc(j, Math.min(j2, j3), j3 - DAY, j3);
    }

    public static float[] daylight(long j, long j2) {
        if (j <= 0 || j2 <= j || j2 - j >= DAY) { return null; }
        return arc(j, j2, j, j2);
    }

    public static float[] elapsed(long j) {
        return new float[]{90.0f, Math.max(0.0f, (((float)(j - BriefModel.midnight(j))) / 8.64E7f) * 360.0f)};
    }

    public static float mid(float[] fArr) { return fArr[0] + (fArr[1] / 2.0f); }

    public static void point(float f, float f2, float f3, float f4, float[] fArr) {
        double radians = Math.toRadians(f4);
        double d = f3;
        fArr[0] = f + ((float)(Math.cos(radians) * d));
        fArr[1] = f2 + ((float)(d * Math.sin(radians)));
    }
}
