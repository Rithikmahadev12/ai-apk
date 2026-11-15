package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class HomeTheme {
    public static final int ADVANCED = 1;
    public static final int AOA = 2;
    public static final String AOA_V = "aoa";
    public static final int SIMPLE = 0;

    private HomeTheme() {
    }

    public static int parse(String str) {
        if ("1".equals(str)) {
            return 1;
        }
        return AOA_V.equals(str) ? 2 : 0;
    }

    public static String value(int i) {
        return i == 1 ? "1" : i == 2 ? AOA_V : "";
    }

    public static boolean aoa(String str) {
        return parse(str) == 2;
    }

    public static boolean hud(String str) {
        return parse(str) != 0;
    }
}
