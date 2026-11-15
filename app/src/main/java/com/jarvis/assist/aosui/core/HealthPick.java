package com.jarvis.assist.aosui.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class HealthPick {
    public static final long HR_MAX_AGE = 604800;
    public static final String[] SHOWN = {"hr", "hrv", "rhr", "steps", "sleep"};

    private HealthPick() {
    }

    public static double[] latest(List<double[]> list, long j, long j2) {
        double[] dArr = null;
        if (list == null) {
            return null;
        }
        for (double[] dArr2 : list) {
            if (dArr2 != null && dArr2.length >= 2 && !Double.isNaN(dArr2[1]) && dArr2[1] > 0.0d && dArr2[0] <= 300 + j && j - dArr2[0] <= j2 && (dArr == null || dArr2[0] > dArr[0])) {
                dArr = dArr2;
            }
        }
        return dArr;
    }

    public static double[] rhr(List<double[]> list, List<double[]> list2, long j) {
        double[] latest = latest(list, j, 2592000L);
        if (latest != null) {
            return latest;
        }
        double[] dArr = null;
        if (list2 != null) {
            for (double[] dArr2 : list2) {
                if (dArr2 != null && dArr2[1] > 25.0d && j - dArr2[0] <= 86400.0d && dArr2[0] <= 300 + j && (dArr == null || dArr2[1] < dArr[1])) {
                    dArr = dArr2;
                }
            }
        }
        return dArr;
    }

    public static String syncAge(long j, long j2) {
        StringBuilder append;
        String str;
        if (j <= 0) {
            return "";
        }
        long max = Math.max(0L, j2 - j) / 60000;
        if (max < 2) {
            return "NOW";
        }
        if (max < 60) {
            append = new StringBuilder().append(max);
            str = "M";
        } else if (max < 2880) {
            append = new StringBuilder().append(max / 60);
            str = "H";
        } else {
            append = new StringBuilder().append(max / 1440);
            str = "D";
        }
        return append.append(str).toString();
    }

    public static String sourceLine(boolean z, List<String> list, List<String> list2, long j, long j2) {
        if (!z) {
            return "HEALTH · NOT CONNECTED";
        }
        if (list == null || list.isEmpty()) {
            return ((list2 == null || list2.isEmpty()) ? "HEALTH" : String.join(" · ", list2)) + " · NO DATA";
        }
        String syncAge = syncAge(j, j2);
        return String.join(" · ", list) + (syncAge.isEmpty() ? "" : " · " + syncAge);
    }

    public static String status(Collection<?> collection) {
        return collection == null ? "error" : collection.isEmpty() ? "none" : "ok";
    }

    public static List<String> noData(JSONObject jSONObject) {
        ArrayList arrayList = new ArrayList();
        if (jSONObject == null || !jSONObject.optBoolean("ok")) {
            return arrayList;
        }
        for (String str : SHOWN) {
            Object opt = jSONObject.opt(str);
            if (!("sleep".equals(str) ? (opt instanceof JSONObject) && (((JSONObject) opt).opt("min") instanceof Number) : opt instanceof Number)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public static int lastNight(List<long[]> list, long j) {
        int i = -1;
        if (list == null) {
            return -1;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            long[] jArr = list.get(i2);
            if (jArr != null && jArr[1] > jArr[0] && jArr[0] <= j && j - jArr[1] <= 129600) {
                boolean z = jArr[1] - jArr[0] >= 3600;
                boolean z2 = i >= 0 && list.get(i)[1] - list.get(i)[0] >= 3600;
                if (i < 0 || ((z && !z2) || (z == z2 && jArr[1] > list.get(i)[1]))) {
                    i = i2;
                }
            }
        }
        return i;
    }

    public static String testLine(JSONObject jSONObject) {
        if (jSONObject == null) {
            return "NO ANSWER · HEALTH CONNECT DID NOT REPLY IN 30 S · TRY AGAIN";
        }
        if (!jSONObject.optString("error").isEmpty()) {
            return "READ FAILED · " + jSONObject.optString("error").toUpperCase(Locale.ROOT);
        }
        int optInt = jSONObject.optInt("steps_days", -1);
        int optInt2 = jSONObject.optInt("sleep_nights", -1);
        int optInt3 = jSONObject.optInt("hr", -1);
        int optInt4 = jSONObject.optInt("hrv", -1);
        int optInt5 = jSONObject.optInt("rhr", -1);
        if (optInt < 0 && optInt2 < 0 && optInt3 < 0 && optInt4 < 0 && optInt5 < 0) {
            return "READ FAILED · NO PERMISSION OR HEALTH CONNECT ERROR";
        }
        String str = "Read OK · steps " + n(optInt) + " day" + (optInt == 1 ? "" : "s") + ", sleep " + n(optInt2) + " night" + (optInt2 == 1 ? "" : "s") + ", heart rate " + n(optInt3) + " sample" + (optInt3 != 1 ? "s" : "") + " (last 7 days)";
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String[][] strArr = {new String[]{"hr", "heart rate"}, new String[]{"hrv", "HRV"}, new String[]{"rhr", "resting heart rate"}, new String[]{"sleep_nights", "sleep"}, new String[]{"steps_days", "steps"}};
        for (int i = 0; i < 5; i++) {
            String[] strArr2 = strArr[i];
            int optInt6 = jSONObject.optInt(strArr2[0], -1);
            if (optInt6 == 0) {
                arrayList.add(strArr2[1]);
            } else if (optInt6 < 0) {
                arrayList2.add(strArr2[1]);
            }
        }
        if (!arrayList.isEmpty()) {
            str = str + " · no " + String.join(", ", arrayList) + " in Health Connect: turn on sync to Health Connect in your watch app";
        }
        if (arrayList2.isEmpty()) {
            return str;
        }
        return str + " · could not read " + String.join(", ", arrayList2) + " (permission?)";
    }

    static String n(int i) {
        return i < 0 ? "?" : String.valueOf(i);
    }

    public static float valueScale(float f, float f2, float f3, float f4) {
        float f5 = (f - f2) - f4;
        if (f3 <= 0.0f) {
            return 1.0f;
        }
        if (f5 <= 0.0f) {
            return 0.0f;
        }
        float f6 = f5 / f3;
        if (f6 >= 1.0f) {
            return 1.0f;
        }
        if (f6 >= 0.5f) {
            return f6;
        }
        return 0.0f;
    }
}
