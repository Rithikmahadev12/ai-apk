package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class HomeBanner {
    public static final String KEY = "aos.homebanner";
    public static final long SNOOZE_MS = 259200000;

    private HomeBanner() {
    }

    public static boolean show(boolean z, long j, long j2) {
        if (z) {
            return false;
        }
        if (j <= 0) {
            return true;
        }
        return j2 - j >= SNOOZE_MS || j2 < j;
    }

    public static long parse(String str) {
        if (str != null) {
            try {
                if (str.isEmpty()) {
                    return 0L;
                }
                return Long.parseLong(str.trim());
            } catch (Exception e) {
                return 0L;
            }
        }
        return 0L;
    }
}
