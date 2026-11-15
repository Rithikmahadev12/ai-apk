package com.jarvis.assist.aosui.core;

import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Flip7 {
    public static final int BG = -16645372;
    public static final int BLUE = -10835492;
    public static final int BLUE_DEEP = -15982542;
    public static final int BLUE_HI = -8600336;
    public static final int BLUE_MID = -13669742;
    public static final int G1 = -6641998;
    public static final int G2 = -8549994;
    public static final int G3 = -10787214;
    public static final int G4 = -12958385;
    public static final int G5 = -14274502;
    public static final int G6 = -15064791;
    public static final int G7 = -16052202;
    public static final int NAME = -2234900;
    public static final int PANEL = -1945498351;
    public static final float READY_START = 140.0f;
    public static final float READY_SWEEP = 260.0f;
    public static final int RED = -3665874;
    public static final int RED_DEEP = -14022128;
    public static final int RED_HI = -45725;
    public static final int WHITE = -1774607;
    public static final int WHITE2 = -854279;

    private Flip7() {
    }

    public static float readySweep(float f) {
        return Math.max(0.0f, Math.min(1.0f, f)) * 260.0f;
    }

    public static String caption(int i, String str) {
        return String.format(Locale.ROOT, "INDEX: %02d · %s", Integer.valueOf(i), str == null ? "" : str.toUpperCase(Locale.ROOT));
    }
}
