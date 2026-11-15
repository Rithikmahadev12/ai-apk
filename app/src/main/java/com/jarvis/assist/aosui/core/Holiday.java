package com.jarvis.assist.aosui.core;

import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Holiday {
    public static final int ACCESS_READ = 200;

    private Holiday() {
    }

    public static boolean is(boolean z, String str, String str2, String str3, int i) {
        if (!z) {
            return false;
        }
        if (has(str) || has(str2) || has(str3)) {
            return true;
        }
        return i > 0 && i <= 200;
    }

    static boolean has(String str) {
        return str != null && str.toLowerCase(Locale.ROOT).contains("holiday");
    }
}
