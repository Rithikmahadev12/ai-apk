package com.jarvis.assist.aosui.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/* loaded from: /tmp/augmaos/classes.dex */
public final class NetSphere {
    public final float ct;
    public final byte[] kind;
    public int[] links = new int[0];
    public final int n;
    public int nl;
    public final float[] p;
    public final float[] ph;
    public final float st;

    NetSphere(int i, float f) {
        this.n = i;
        this.p = new float[i * 3];
        this.kind = new byte[i];
        this.ph = new float[i];
        double d = f;
        this.ct = (float) Math.cos(d);
        this.st = (float) Math.sin(d);
    }

    public static int count(String str, float f) {
        return Math.round(("HIGH".equals(str) ? 400 : "BALANCED".equals(str) ? 260 : 140) * f);
    }

    public static NetSphere home(int i, long j) {
        float f;
        float f2;
        int i2;
        NetSphere netSphere = new NetSphere(i, 0.19f);
        Random random = new Random(j);
        float f3 = i;
        int round = Math.round(0.4f * f3);
        int round2 = round + Math.round(0.15f * f3);
        int round3 = Math.round(f3 * 0.08f) + round2;
        for (int i3 = 0; i3 < i; i3++) {
            if (i3 < round) {
                f = (random.nextFloat() * 0.2f) + 0.32f;
                f2 = 0.55f;
                i2 = 1;
            } else if (i3 < round2) {
                netSphere.spiral(i3, random, (i3 - round) / Math.max(1, round2 - round));
            } else {
                float nextFloat = random.nextFloat();
                if (i3 < round3) {
                    f = nextFloat * 0.22f;
                    f2 = 1.0f;
                    i2 = 0;
                } else {
                    f = (nextFloat * 0.05f) + 0.95f;
                    f2 = 1.0f;
                    i2 = 3;
                }
            }
            netSphere.ball(i3, random, f, f2, i2);
        }
        netSphere.link(0.03f, 0.03f);
        return netSphere;
    }

    public static NetSphere big(int i, long j) {
        float nextFloat;
        float f;
        int i2;
        NetSphere netSphere = new NetSphere(i, 0.35f);
        Random random = new Random(j);
        float f2 = i;
        int round = Math.round(0.13f * f2);
        int round2 = Math.round(f2 * 0.3f) + round;
        for (int i3 = 0; i3 < i; i3++) {
            if (i3 < round) {
                nextFloat = ((float) Math.pow(random.nextFloat(), 1.6d)) * 0.22f;
                f = 1.0f;
                i2 = 0;
            } else if (i3 < round2) {
                netSphere.spiral(i3, random, (i3 - round) / Math.max(1, round2 - round));
            } else {
                nextFloat = (random.nextFloat() * 0.04f) + 0.97f;
                f = 1.0f;
                i2 = 3;
            }
            netSphere.ball(i3, random, nextFloat, f, i2);
        }
        netSphere.link(0.012f, 0.02f);
        return netSphere;
    }

    void ball(int i, Random random, float f, float f2, int i2) {
        float nextFloat = (random.nextFloat() * 2.0f) - 1.0f;
        float nextFloat2 = random.nextFloat() * 6.2832f;
        float sqrt = (float) Math.sqrt(1.0f - (nextFloat * nextFloat));
        int i3 = i * 3;
        double d = nextFloat2;
        this.p[i3] = ((float) Math.cos(d)) * sqrt * f;
        this.p[i3 + 1] = nextFloat * f * f2;
        this.p[i3 + 2] = sqrt * ((float) Math.sin(d)) * f;
        this.kind[i] = (byte) i2;
        this.ph[i] = random.nextFloat();
    }

    void spiral(int i, Random random, float f) {
        float nextFloat = (6.2832f * f * 1.6f) + (random.nextFloat() * 0.25f);
        float nextFloat2 = (f * 0.28f) + 0.3f + (random.nextFloat() * 0.05f);
        int i2 = i * 3;
        double d = nextFloat;
        this.p[i2] = ((float) Math.cos(d)) * nextFloat2;
        this.p[i2 + 1] = (random.nextFloat() - 0.5f) * 0.12f;
        this.p[i2 + 2] = ((float) Math.sin(d)) * nextFloat2;
        this.kind[i] = 2;
        this.ph[i] = random.nextFloat();
    }

    void link(float f, float f2) {
        int[] iArr = new int[this.n * 4];
        HashSet hashSet = new HashSet();
        int i = 0;
        for (int i2 = 0; i2 < this.n; i2++) {
            float f3 = this.kind[i2] == 3 ? f2 : f;
            int i3 = -1;
            int i4 = -1;
            float f4 = f3;
            for (int i5 = 0; i5 < this.n; i5++) {
                if (i5 != i2) {
                    int i6 = i2 * 3;
                    int i7 = i5 * 3;
                    float f5 = this.p[i6] - this.p[i7];
                    float f6 = this.p[i6 + 1] - this.p[i7 + 1];
                    float f7 = this.p[i6 + 2] - this.p[i7 + 2];
                    float f8 = (f5 * f5) + (f6 * f6) + (f7 * f7);
                    if (f8 < f3) {
                        f4 = f3;
                        i4 = i3;
                        i3 = i5;
                        f3 = f8;
                    } else if (f8 < f4) {
                        i4 = i5;
                        f4 = f8;
                    }
                }
            }
            int[] iArr2 = {i3, i4};
            for (int i8 = 0; i8 < 2; i8++) {
                int i9 = iArr2[i8];
                if (i9 >= 0 && hashSet.add(Long.valueOf((Math.min(i2, i9) * this.n) + Math.max(i2, i9)))) {
                    int i10 = i + 1;
                    iArr[i] = i2;
                    i = i10 + 1;
                    iArr[i10] = i9;
                }
            }
        }
        this.links = Arrays.copyOf(iArr, i);
        this.nl = i / 2;
    }

    public void rotate(float f, float[] fArr) {
        double d = f;
        float cos = (float) Math.cos(d);
        float sin = (float) Math.sin(d);
        for (int i = 0; i < this.n; i++) {
            int i2 = i * 3;
            float f2 = this.p[i2];
            int i3 = i2 + 1;
            float f3 = this.p[i3];
            int i4 = i2 + 2;
            float f4 = this.p[i4];
            float f5 = (f2 * cos) - (f4 * sin);
            float f6 = (f2 * sin) + (f4 * cos);
            fArr[i2] = f5;
            fArr[i3] = (this.ct * f3) - (this.st * f6);
            fArr[i4] = (f6 * this.ct) + (f3 * this.st);
        }
    }

    public static float persp(float f) {
        return 1.0f / (1.0f - (f * 0.15f));
    }

    public float twinkle(int i, float f) {
        return (((float) Math.sin((f * 3.0f) + (this.ph[i] * 20.0f))) * 0.4f) + 0.6f;
    }
}
