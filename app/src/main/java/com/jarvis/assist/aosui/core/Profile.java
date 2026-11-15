package com.jarvis.assist.aosui.core;

import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Profile {
    public static final String CHIP = "aos.youchip";
    public static final String KEY = "aos.you";
    public static final int MAX = 20;
    public static final long SNOOZE = 604800000;

    private Profile() {
    }

    public static String clean(String str) {
        String replaceAll = str == null ? "" : str.trim().replaceAll("\\s+", " ");
        if (replaceAll.length() <= 20) {
            return replaceAll;
        }
        String substring = replaceAll.substring(0, 20);
        int lastIndexOf = substring.lastIndexOf(32);
        if (lastIndexOf >= 10) {
            substring = substring.substring(0, lastIndexOf);
        }
        return substring.trim();
    }

    public static String greet(String str, String str2) {
        if (str == null) {
            str = "";
        }
        String clean = clean(str2);
        if (clean.isEmpty()) {
            return str;
        }
        return str + ", " + clean.toUpperCase(Locale.getDefault());
    }

    public static String[] migrate(String str, String str2) {
        String clean = clean(str2);
        if (clean(str).isEmpty() && !clean.isEmpty()) {
            return new String[]{clean, ""};
        }
        if (clean(str2).isEmpty()) {
            str2 = "";
        }
        return new String[]{null, str2};
    }

    public static boolean chip(String str, long j, long j2) {
        return clean(str).isEmpty() && (j <= 0 || j2 - j >= SNOOZE || j2 < j);
    }
}
