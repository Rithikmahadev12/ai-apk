package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class UnitsMath {
    public static final float CH = 470.0f;
    public static final float CW = 360.0f;
    public static final float LABEL_CAP = 1.3f;

    private UnitsMath() {
    }

    public static float u(float f, float f2) {
        return Math.max(0.82f, Math.min(1.6f, Math.min(f, f2) / 390.0f));
    }

    public static float[] stage(float f, float f2, float f3, float f4) {
        if (f <= 0.0f || f2 <= 0.0f) {
            return new float[]{1.0f, f3, f4};
        }
        float min = Math.min(f / f3, f2 / f4);
        return new float[]{min, f / min, f2 / min};
    }

    public static boolean landscape(float f, float f2) {
        return f > f2 * 1.15f;
    }

    public static float textZoom(float f) {
        return Math.max(0.85f, Math.min(1.3f, f));
    }

    public static int lines(float f) {
        return f > 1.15f ? 3 : 1;
    }

    public static float viewZoom(float f) {
        if (f <= 0.0f) {
            f = 1.0f;
        }
        return Math.max(0.85f, f);
    }

    public static String homeMode(float f, float f2) {
        return landscape(f, f2) ? "land" : (f <= 0.0f || f2 / f >= 1.3f) ? "portrait" : "compact";
    }

    public static float labelSize(float f, float f2, float f3, float f4) {
        float f5 = f * f2;
        return Math.max(f5, f3 > 0.0f ? (f4 * f2) / f3 : f5);
    }

    public static float fitScale(float f, float f2, float f3) {
        if (f <= f2 || f <= 0.0f) {
            return 1.0f;
        }
        return Math.max(f3, f2 / f);
    }

    public static float tabletScale(int i) {
        return i >= 600 ? 1.2f : 1.0f;
    }

    public static float landStageH(float f, float f2) {
        if (Math.min(f, f2) >= 600.0f) {
            return Math.max(360.0f, Math.min(460.0f, (f2 * 800.0f) / f));
        }
        return 360.0f;
    }

    public static double luminance(int i) {
        return (lin((i >> 16) & 255) * 0.2126d) + (lin((i >> 8) & 255) * 0.7152d) + (lin(i & 255) * 0.0722d);
    }

    static double lin(int i) {
        double d = i / 255.0d;
        return d <= 0.03928d ? d / 12.92d : Math.pow((d + 0.055d) / 1.055d, 2.4d);
    }

    public static double contrast(int i, int i2) {
        double luminance = luminance(i);
        double luminance2 = luminance(i2);
        return (Math.max(luminance, luminance2) + 0.05d) / (Math.min(luminance, luminance2) + 0.05d);
    }

    public static int over(int i, int i2) {
        float f = (i >>> 24) / 255.0f;
        float f2 = 1.0f - f;
        return Math.round(((i & 255) * f) + ((i2 & 255) * f2)) | (-16777216) | (Math.round((((i >> 16) & 255) * f) + (((i2 >> 16) & 255) * f2)) << 16) | (Math.round((((i >> 8) & 255) * f) + (((i2 >> 8) & 255) * f2)) << 8);
    }

    public static String assistantName(String str) {
        return (str == null || str.trim().isEmpty()) ? "aOS" : str.trim();
    }
}
