package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class SegModel {
    private SegModel() {
    }

    public static boolean[] selected(String[] strArr, String str) {
        boolean[] zArr = new boolean[strArr.length];
        if (str != null) {
            int i = 0;
            while (true) {
                if (i >= strArr.length) {
                    break;
                }
                if (str.equals(strArr[i])) {
                    zArr[i] = true;
                    break;
                }
                i++;
            }
        }
        return zArr;
    }

    public static String stateKey(boolean z) {
        return z ? "kit.seg.on" : "kit.seg.off";
    }
}
