package com.jarvis.assist.aosui.core;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Corners {
    public static final String KEY = "aos.corners";
    public static final String[] CORNERS = {"tl", "tr", "bl", "br"};
    public static final String[] ITEMS = {"screentime", "time", "battery", "network", "data", "notifications", "alarm", "event", "steps", "heart", "sleep", "weather", "storage", "daylight", "moon", "uptime"};
    static final String[][] PREF = {new String[]{"screentime", "time"}, new String[]{"network", "data", "battery"}, new String[]{"alarm", "event", "uptime"}, new String[]{"daylight", "moon", "uptime"}};

    private Corners() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static List<String> shownBy(String str) {
        char c;
        switch (str.hashCode()) {
            case -1884274053:
                if (str.equals("storage")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -416416455:
                if (str.equals("screentime")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -331239923:
                if (str.equals("battery")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 3560141:
                if (str.equals("time")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 92895825:
                if (str.equals("alarm")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 96891546:
                if (str.equals("event")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 99151942:
                if (str.equals("heart")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 109522647:
                if (str.equals("sleep")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 109761319:
                if (str.equals("steps")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1223440372:
                if (str.equals("weather")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1272354024:
                if (str.equals("notifications")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1942983418:
                if (str.equals("daylight")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return Arrays.asList("weather");
            case 1:
                return Arrays.asList("weather");
            case 2:
                return Arrays.asList("health", "activity");
            case 3:
                return Arrays.asList("health");
            case 4:
                return Arrays.asList("sleep", "health");
            case 5:
                return Arrays.asList("battery");
            case 6:
                return Arrays.asList("dials", "storage");
            case 7:
                return Arrays.asList("dials", "notifs");
            case '\b':
                return Arrays.asList("calendar");
            case '\t':
                return Arrays.asList("alarms", "worldclock");
            case '\n':
                return Arrays.asList("screentime");
            case 11:
                return Arrays.asList("greeting");
            default:
                return Arrays.asList(new String[0]);
        }
    }

    public static boolean inWidget(String str, Set<String> set) {
        for (String str2 : shownBy(str)) {
            if ("dials".equals(str2) || set.contains(str2)) {
                return true;
            }
        }
        return false;
    }

    public static String pick(String str, String str2, Set<String> set) {
        String user = user(str, str2);
        if (user != null) {
            return user;
        }
        int indexOf = Arrays.asList(CORNERS).indexOf(str);
        if (indexOf < 0) {
            return "time";
        }
        for (String str3 : PREF[indexOf]) {
            if (!inWidget(str3, set)) {
                return str3;
            }
        }
        return PREF[indexOf][0];
    }

    public static String user(String str, String str2) {
        String str3 = "";
        if (str2 != null) {
            try {
                if (!str2.isEmpty()) {
                    str3 = new JSONObject(str2).optString(str, "");
                }
            } catch (Exception e) {
                return null;
            }
        }
        if (Arrays.asList(ITEMS).contains(str3)) {
            return str3;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001a A[Catch: Exception -> 0x0022, TryCatch #0 {Exception -> 0x0022, blocks: (B:13:0x0002, B:16:0x0009, B:4:0x0016, B:5:0x001d, B:11:0x001a, B:2:0x000f), top: B:12:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:4:0x0016 A[Catch: Exception -> 0x0022, TryCatch #0 {Exception -> 0x0022, blocks: (B:13:0x0002, B:16:0x0009, B:4:0x0016, B:5:0x001d, B:11:0x001a, B:2:0x000f), top: B:12:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String set(java.lang.String r1, java.lang.String r2, java.lang.String r3) {
        /*
            if (r1 == 0) goto Lf
            boolean r0 = r1.isEmpty()     // Catch: java.lang.Exception -> L22
            if (r0 == 0) goto L9
            goto Lf
        L9:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch: java.lang.Exception -> L22
            r0.<init>(r1)     // Catch: java.lang.Exception -> L22
            goto L14
        Lf:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch: java.lang.Exception -> L22
            r0.<init>()     // Catch: java.lang.Exception -> L22
        L14:
            if (r3 != 0) goto L1a
            r0.remove(r2)     // Catch: java.lang.Exception -> L22
            goto L1d
        L1a:
            r0.put(r2, r3)     // Catch: java.lang.Exception -> L22
        L1d:
            java.lang.String r1 = r0.toString()     // Catch: java.lang.Exception -> L22
            return r1
        L22:
            r1 = move-exception
            java.lang.String r1 = "{}"
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.core.Corners.set(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    public static double moon(long j) {
        double d = ((j - 947182440000L) / 8.64E7d) / 29.530588853d;
        return d - Math.floor(d);
    }

    public static String moonName(double d) {
        return new String[]{"NEW", "WAXING CRESCENT", "FIRST QUARTER", "WAXING GIBBOUS", "FULL", "WANING GIBBOUS", "LAST QUARTER", "WANING CRESCENT"}[((int) Math.floor((d * 8.0d) + 0.5d)) % 8];
    }

    public static String dur(long j) {
        StringBuilder append;
        long max = Math.max(0L, j / 60000);
        if (max >= 60) {
            long j2 = max / 60;
            append = new StringBuilder().append(j2).append("H ").append(String.format(Locale.ROOT, "%02dM", Long.valueOf(max % 60)));
        } else {
            append = new StringBuilder().append(max).append("M");
        }
        return append.toString();
    }
}
