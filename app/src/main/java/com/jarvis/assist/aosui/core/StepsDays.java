package com.jarvis.assist.aosui.core;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class StepsDays {
    public static final int N = 7;

    private StepsDays() {
    }

    public static long midnight(long j, TimeZone timeZone, int i) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(j);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(5, -i);
        return calendar.getTimeInMillis();
    }

    public static float[] fromRecords(List<long[]> list, long j, TimeZone timeZone) {
        boolean z;
        long[] jArr = new long[8];
        int i = 0;
        for (int i2 = 0; i2 < 7; i2++) {
            jArr[i2] = midnight(j, timeZone, 6 - i2);
        }
        jArr[7] = Math.max(j, jArr[6]) + 1;
        double[] dArr = new double[7];
        if (list != null) {
            z = false;
            for (long[] jArr2 : list) {
                if (jArr2 != null && jArr2.length >= 3) {
                    char c = 2;
                    if (jArr2[2] > 0) {
                        long j2 = jArr2[i];
                        long max = Math.max(jArr2[i], jArr2[1]);
                        if ((max > jArr[i] || j2 >= jArr[i]) && j2 < jArr[7]) {
                            if (max == j2) {
                                for (int i3 = i; i3 < 7; i3++) {
                                    if (j2 >= jArr[i3] && j2 < jArr[i3 + 1]) {
                                        dArr[i3] = dArr[i3] + jArr2[2];
                                        z = true;
                                    }
                                }
                                i = 0;
                            } else {
                                int i4 = 0;
                                while (i4 < 7) {
                                    long max2 = Math.max(j2, jArr[i4]);
                                    int i5 = i4 + 1;
                                    char c2 = c;
                                    long j3 = j2;
                                    if (Math.min(max, jArr[i5]) > max2) {
                                        dArr[i4] = dArr[i4] + ((jArr2[c2] * (r7 - max2)) / (max - j3));
                                        z = true;
                                    }
                                    c = c2;
                                    j2 = j3;
                                    i4 = i5;
                                }
                                i = 0;
                            }
                        }
                    }
                }
                i = 0;
            }
        } else {
            z = false;
        }
        float[] fArr = new float[7];
        for (int i6 = 0; i6 < 7; i6++) {
            fArr[i6] = z ? (float) Math.round(dArr[i6]) : Float.NaN;
        }
        return fArr;
    }

    public static float[] fromDays(JSONArray jSONArray, long j, TimeZone timeZone) {
        boolean z;
        float[] fArr = new float[7];
        Arrays.fill(fArr, Float.NaN);
        if (jSONArray != null) {
            z = false;
            for (int i = 0; i < 7; i++) {
                String ymd = ymd(midnight(j, timeZone, 6 - i), timeZone);
                int i2 = 0;
                while (true) {
                    if (i2 >= jSONArray.length()) {
                        break;
                    }
                    JSONObject optJSONObject = jSONArray.optJSONObject(i2);
                    if (optJSONObject == null || !ymd.equals(optJSONObject.optString("date"))) {
                        i2++;
                    } else if (optJSONObject.opt("steps") instanceof Number) {
                        fArr[i] = (float) optJSONObject.optDouble("steps");
                        z = true;
                    }
                }
            }
        } else {
            z = false;
        }
        if (z) {
            for (int i3 = 0; i3 < 7; i3++) {
                if (Float.isNaN(fArr[i3])) {
                    fArr[i3] = 0.0f;
                }
            }
        }
        return fArr;
    }

    public static int today(float[] fArr) {
        if (fArr == null || fArr.length == 0 || Float.isNaN(fArr[fArr.length - 1])) {
            return -1;
        }
        return Math.round(fArr[fArr.length - 1]);
    }

    public static int ring(int i, float[] fArr) {
        return Math.max(i, today(fArr));
    }

    public static String ymd(long j, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(j);
        return String.format(Locale.US, "%04d-%02d-%02d", Integer.valueOf(calendar.get(1)), Integer.valueOf(calendar.get(2) + 1), Integer.valueOf(calendar.get(5)));
    }
}
