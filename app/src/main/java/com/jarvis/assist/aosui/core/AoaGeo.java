package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaGeo {
    public static final float ARC_CY = 194.0f;
    public static final float ARC_H = 256.0f;
    public static final float ARC_K = 0.864f;
    public static final float ARC_W = 360.0f;
    public static final float DOCK_CY = -50.0f;
    public static final float DOCK_H = 128.0f;
    public static final float LAB_R = 186.0f;
    public static final float ORB_R = 61.0f;
    public static final float PTT_R = 104.0f;
    public static final float P_ARC_CY = 241.0f;
    public static final float P_DOCK_CY = 653.0f;
    public static final float P_H = 840.0f;
    public static final float P_IND = 16.0f;
    public static final float[] OUT = {150.0f, 176.0f};
    public static final float[] MID = {116.0f, 144.0f};
    public static final float[] IN = {72.0f, 108.0f};
    public static final float[] DOCK_OUT = {150.0f, 176.0f};
    public static final float[] DOCK_IN = {116.0f, 144.0f};

    private AoaGeo() {
    }

    public static float[] seg(int i, int i2, float f, float f2) {
        double d = f + 3.141592653589793d;
        double d2 = 3.141592653589793d - (f * 2.0f);
        double d3 = i2;
        double d4 = f2;
        float degrees = (float) Math.toDegrees(((i * d2) / d3) + d + d4);
        float degrees2 = (float) Math.toDegrees((d + (((i + 1) * d2) / d3)) - d4);
        return new float[]{degrees, degrees2, (degrees + degrees2) / 2.0f};
    }

    public static float inset(float f) {
        return f >= 150.0f ? 0.16f : 0.12f;
    }

    public static float[] tile(int i, float f, float f2, float f3, float f4, float f5) {
        float f6 = 2.0f * f5;
        float f7 = (f3 - f6) / 3.0f;
        float f8 = (f4 - f6) / 3.0f;
        float f9 = f + ((i % 3) * (f7 + f5));
        float f10 = f2 + ((i / 3) * (f5 + f8));
        return new float[]{f9, f10, f7 + f9, f8 + f10};
    }

    public static float[] portrait(float f, float f2, float f3) {
        float max = Math.max(0.0f, f3 - 14.0f);
        float f4 = f2 - max;
        float min = Math.min(f / 360.0f, f4 / 840.0f);
        return new float[]{min, (f - (360.0f * min)) / 2.0f, max + Math.max(0.0f, (f4 - (840.0f * min)) / 2.0f)};
    }

    public static float vis(float f, float f2) {
        return (float) Math.asin(Math.min(1.0f, (f2 + 3.0f) / f));
    }

    public static float[] dockSeg(int i, int i2, float f, float f2) {
        double vis = vis(f, f2) + 0.02d;
        double d = 3.141592653589793d - (2.0d * vis);
        double d2 = i2;
        float degrees = (float) Math.toDegrees(3.141592653589793d - ((((i * d) / d2) + vis) + 0.012d));
        float degrees2 = (float) Math.toDegrees(3.141592653589793d - ((vis + (((i + 1) * d) / d2)) - 0.012d));
        return new float[]{degrees2, degrees, (degrees2 + degrees) / 2.0f};
    }

    public static float[] land(float f, float f2) {
        float f3 = 0.5f * f;
        float f4 = f - f3;
        float f5 = f2 - 40.0f;
        float min = Math.min((f4 - 16.0f) / 360.0f, (f5 - 30.0f) / 270.0f);
        float max = Math.max(90.0f, 0.34f * f2);
        float f6 = (((f2 - max) - 8.0f) - 26.0f) - 6.0f;
        return new float[]{f3, min, ((f4 - (360.0f * min)) / 2.0f) + f3, (-20.0f) * min, 44.0f, Math.max(60.0f, ((f6 - 6.0f) - 16.0f) - 44.0f), f6, 26.0f + f6 + 6.0f, max, f5 - 4.0f, 40.0f};
    }
}
