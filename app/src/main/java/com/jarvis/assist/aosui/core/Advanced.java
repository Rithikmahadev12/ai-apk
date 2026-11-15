package com.jarvis.assist.aosui.core;

import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Advanced {
    public static final String KEY = "aos.adv";
    public static final String[] PAGES = new String[0];

    private Advanced() {
    }

    public static boolean on(String str) {
        return "1".equals(str);
    }

    public static String set(boolean z) {
        return z ? "1" : "";
    }

    public static String dateLine(String str, int i, String str2, int i2) {
        return (str + " " + i + " " + str2 + " · WK " + i2).toUpperCase(Locale.ROOT);
    }

    public static String mode(int i, int i2) {
        return (i == 3 || i == 4) ? "bed" : i == 2 ? "focus" : i2 != 2 ? "silent" : "life";
    }

    public static String[] greeting(String str, String str2, int i, String str3, int i2, String str4, double d, String str5) {
        return greeting(str, str2, i, str3, i2, str4, d, str5, "");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static String[] greeting(String str, String str2, int i, String str3, int i2, String str4, double d, String str5, String str6) {
        char c;
        String str7;
        String str8;
        String clean = Profile.clean(str6);
        if (str == null) {
            str = "life";
        }
        String str9 = "INDEX 01 · " + str.toUpperCase(Locale.ROOT) + " MODE";
        String valueOf = d == Math.floor(d) ? String.valueOf((long) d) : String.valueOf(d);
        switch (str.hashCode()) {
            case -902327211:
                if (str.equals("silent")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 97409:
                if (str.equals("bed")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 3655441:
                if (str.equals("work")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 97604824:
                if (str.equals("focus")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        String str10 = "";
        switch (c) {
            case 0:
                if (i > 0) {
                    str7 = "WORK MODE · " + i + (i == 1 ? " MEETING LEFT" : " MEETINGS LEFT");
                } else {
                    str7 = "WORK MODE · CLEAR DAY";
                }
                if (str3 != null && !str3.isEmpty()) {
                    str8 = "NEXT " + str3;
                    break;
                } else {
                    str8 = "NOTHING ELSE TODAY";
                    break;
                }
            case 1:
                str8 = i2 + " WAITING";
                str7 = "FOCUS · NOTIFICATIONS HELD";
                break;
            case 2:
                str8 = i2 > 0 ? i2 + " WAITING" : "ALL QUIET";
                str7 = "SILENT · VIBRATE ONLY";
                break;
            case 3:
                String str11 = "WIND DOWN";
                if (!clean.isEmpty()) {
                    str7 = Profile.greet("WIND DOWN", clean);
                    if (str4 != null && !str4.isEmpty()) {
                        str10 = "ALARM " + str4 + " · ";
                    }
                    str8 = str10 + "SLEEP TARGET " + valueOf + "H";
                    break;
                } else {
                    if (str4 != null && !str4.isEmpty()) {
                        str11 = "WIND DOWN · ALARM " + str4;
                    }
                    str8 = "SLEEP TARGET " + valueOf + "H";
                    str7 = str11;
                    break;
                }
                break;
            default:
                if (str2 == null || str2.isEmpty()) {
                    str2 = "HELLO";
                }
                str7 = Profile.greet(str2, clean);
                if (str5 == null) {
                    str5 = "";
                }
                str8 = str5;
                break;
        }
        return new String[]{str9, str7.toUpperCase(Locale.ROOT), str8.toUpperCase(Locale.ROOT)};
    }

    public static String badge(int i) {
        return i <= 0 ? "" : i > 99 ? "99+" : String.valueOf(i);
    }
}
