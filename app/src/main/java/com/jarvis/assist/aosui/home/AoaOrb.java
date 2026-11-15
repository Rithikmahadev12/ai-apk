package com.jarvis.assist.aosui.home;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import com.jarvis.assist.aosui.core.AoaBootSeq;
import com.jarvis.assist.aosui.core.BootSeq;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Theme;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaOrb {
    static final float[][] LB = {new float[]{0.05f, -0.25f, 0.2f, 1.0f}, new float[]{0.12f, -0.48f, 0.1f, 1.0f}, new float[]{0.9f, -0.3f, 0.1f, 0.0f}, new float[]{-0.75f, 0.15f, 0.3f, 0.0f}, new float[]{-0.8f, 0.45f, 0.2f, 1.0f}};
    static final float[][] RH = {new float[]{0.12f, 0.1f}, new float[]{0.88f, 0.22f}, new float[]{0.18f, 0.82f}, new float[]{0.62f, 0.94f}};
    int[] L;
    final int N;
    final float[] P;
    final float R;
    final float S;
    final Paint add;
    final float[][] arcs;
    float[] arr;
    public float asm;
    final boolean big;
    RadialGradient core;
    RadialGradient ember;
    public float fill;
    float gcx;
    float gcy;
    RadialGradient glass;
    float gu;
    RadialGradient halo;
    RadialGradient hi;
    public float ign;
    final int[] kind;
    float[] lines;
    final float[][] mk;
    final Matrix mx;
    int nl;
    final Paint p;
    final Path path;
    final float[] q;
    final RectF rf;
    public float rim;
    public float spanX;
    public float spanY;
    float[] sx;
    float[] sy;
    final float[] w3;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object, com.jarvis.assist.aosui.home.AoaOrb] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.jarvis.assist.aosui.home.AoaOrb] */
    public AoaOrb(boolean z) {
        float f;
        AoaOrb obj = new Object();
        obj.L = new int[0];
        int i = 1;
        obj.p = new Paint(1);
        obj.add = new Paint(1);
        obj.rf = new RectF();
        obj.path = new Path();
        obj.mx = new Matrix();
        obj.w3 = new float[3];
        obj.asm = -1.0f;
        float f2 = 1.0f;
        obj.ign = 1.0f;
        obj.rim = 1.0f;
        obj.fill = 1.0f;
        obj.spanX = 600.0f;
        obj.spanY = 1200.0f;
        obj.gu = -1.0f;
        obj.lines = new float[0];
        obj.big = z;
        Random random = new Random(z ? 290L : 176L);
        obj.add.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        obj.p.setStrokeCap(Paint.Cap.BUTT);
        float f3 = 0.95f;
        float f4 = 0.05f;
        float f5 = 0.25f;
        int i2 = 2;
        if (!z) {
            obj.S = 176.0f;
            obj.R = obj.S * 0.84f;
            obj.N = BootSeq.HOLD_MS;
            obj.P = new float[obj.N * 4];
            obj.kind = new int[obj.N];
            int i3 = 0;
            while (i3 < obj.N) {
                float nextFloat = (random.nextFloat() * 2.0f) - f2;
                float nextFloat2 = random.nextFloat() * 6.283f;
                int i4 = i;
                if (i3 < 170) {
                    f = (random.nextFloat() * 0.2f) + 0.32f;
                } else {
                    float nextFloat3 = random.nextFloat();
                    f = i3 < 220 ? nextFloat3 * f5 : (nextFloat3 * f4) + f3;
                }
                float f6 = f2;
                float f7 = f3;
                float f8 = f4;
                float sqrt = (float) Math.sqrt(f6 - (nextFloat * nextFloat));
                int i5 = i3 * 4;
                double d = nextFloat2;
                float f9 = f5;
                obj.P[i5] = ((float) Math.cos(d)) * sqrt * f;
                obj.P[i5 + 1] = nextFloat * f * (i3 < 170 ? 0.55f : f6);
                obj.P[i5 + 2] = sqrt * ((float) Math.sin(d)) * f;
                obj.P[i5 + 3] = random.nextFloat();
                obj.kind[i3] = i3 < 220 ? 0 : i4;
                i3++;
                i = i4;
                f3 = f7;
                f2 = f6;
                f4 = f8;
                f5 = f9;
            }
            int i6 = i;
            ArrayList arrayList = new ArrayList();
            for (int i7 = 0; i7 < obj.N; i7++) {
                int i8 = 0;
                for (int i9 = 0; i9 < obj.N && i8 < 2; i9++) {
                    if (i9 != i7) {
                        int i10 = i7 * 4;
                        int i11 = i9 * 4;
                        float f10 = obj.P[i10] - obj.P[i11];
                        float f11 = obj.P[i10 + 1] - obj.P[i11 + 1];
                        float f12 = obj.P[i10 + 2] - obj.P[i11 + 2];
                        if ((f10 * f10) + (f11 * f11) + (f12 * f12) < 0.03f) {
                            arrayList.add(Integer.valueOf(i7));
                            arrayList.add(Integer.valueOf(i9));
                            i8++;
                        }
                    }
                }
            }
            obj.setLinks(arrayList);
            obj.arcs = new float[9];
            int i12 = 0;
            for (int i13 = 9; i12 < i13; i13 = 9) {
                float[][] fArr = obj.arcs;
                float nextFloat4 = random.nextFloat() * 6.283f;
                float nextFloat5 = random.nextFloat() * 6.283f;
                float nextFloat6 = (random.nextFloat() * 1.2f) + 0.4f;
                float[] fArr2 = new float[3];
                fArr2[0] = nextFloat4;
                fArr2[i6] = nextFloat5;
                fArr2[2] = nextFloat6;
                fArr[i12] = fArr2;
                i12++;
            }
            obj.mk = new float[0];
        } else {
            obj.S = 290.0f;
            obj.R = obj.S * 0.9f;
            obj.N = 1080;
            obj.P = new float[obj.N * 4];
            obj.kind = new int[obj.N];
            int i14 = 0;
            int i15 = 0;
            AoaOrb aoaOrb = obj;
            while (i15 < 140) {
                float nextFloat7 = (random.nextFloat() * 2.0f) - 1.0f;
                float nextFloat8 = random.nextFloat() * 6.283f;
                float pow = ((float) Math.pow(random.nextFloat(), 1.6d)) * 0.22f;
                float sqrt2 = (float) Math.sqrt(1.0f - (nextFloat7 * nextFloat7));
                double d2 = nextFloat8;
                aoaOrb.set(i14, ((float) Math.cos(d2)) * sqrt2 * pow, nextFloat7 * pow, sqrt2 * ((float) Math.sin(d2)) * pow, 0);
                i15++;
                i14++;
                aoaOrb = this;
                i2 = i2;
            }
            int i16 = i2;
            int i17 = 0;
            while (i17 < 320) {
                float f13 = i17 / 320.0f;
                float nextFloat9 = (f13 * 6.283f * 1.6f) + (random.nextFloat() * 0.25f);
                float nextFloat10 = (f13 * 0.28f) + 0.3f + (random.nextFloat() * 0.05f);
                double d3 = nextFloat9;
                set(i14, ((float) Math.cos(d3)) * nextFloat10, (random.nextFloat() - 0.5f) * 0.12f, ((float) Math.sin(d3)) * nextFloat10, 1);
                i17++;
                i14++;
            }
            int i18 = 0;
            while (i18 < 620) {
                float nextFloat11 = (random.nextFloat() * 2.0f) - 1.0f;
                float nextFloat12 = random.nextFloat() * 6.283f;
                float nextFloat13 = (random.nextFloat() * 0.04f) + 0.97f;
                float sqrt3 = (float) Math.sqrt(1.0f - (nextFloat11 * nextFloat11));
                double d4 = nextFloat12;
                set(i14, ((float) Math.cos(d4)) * sqrt3 * nextFloat13, nextFloat11 * nextFloat13, sqrt3 * ((float) Math.sin(d4)) * nextFloat13, 2);
                i18++;
                i14++;
            }
            obj = this;
            ArrayList arrayList2 = new ArrayList();
            int i19 = 0;
            while (i19 < obj.N) {
                float f14 = obj.kind[i19] == i16 ? 0.02f : 0.012f;
                int i20 = i19 + 1;
                int i21 = 0;
                for (int i22 = i20; i22 < obj.N && i21 < i16; i22++) {
                    int i23 = i19 * 4;
                    int i24 = i22 * 4;
                    float f15 = obj.P[i23] - obj.P[i24];
                    float f16 = obj.P[i23 + 1] - obj.P[i24 + 1];
                    float f17 = obj.P[i23 + i16] - obj.P[i24 + i16];
                    if ((f15 * f15) + (f16 * f16) + (f17 * f17) < f14) {
                        arrayList2.add(Integer.valueOf(i19));
                        arrayList2.add(Integer.valueOf(i22));
                        i21++;
                    }
                }
                i19 = i20;
            }
            obj.setLinks(arrayList2);
            obj.arcs = new float[16];
            for (int i25 = 0; i25 < 16; i25++) {
                float[][] fArr3 = obj.arcs;
                float nextFloat14 = random.nextFloat() * 3.14f;
                float nextFloat15 = random.nextFloat() * 6.283f;
                float nextFloat16 = random.nextFloat() * 6.283f;
                float nextFloat17 = (random.nextFloat() * 2.0f) + 0.8f;
                float nextFloat18 = (random.nextFloat() * 0.25f) + 0.95f;
                float[] fArr4 = new float[5];
                fArr4[0] = nextFloat14;
                fArr4[1] = nextFloat15;
                fArr4[i16] = nextFloat16;
                fArr4[3] = nextFloat17;
                fArr4[4] = nextFloat18;
                fArr3[i25] = fArr4;
            }
            obj.mk = new float[9];
            for (int i26 = 0; i26 < 9; i26++) {
                float nextFloat19 = (random.nextFloat() * 2.0f) - 1.0f;
                float nextFloat20 = random.nextFloat() * 6.283f;
                float sqrt4 = (float) Math.sqrt(1.0f - (nextFloat19 * nextFloat19));
                float[][] fArr5 = obj.mk;
                double d5 = nextFloat20;
                float cos = ((float) Math.cos(d5)) * sqrt4 * 1.12f;
                float sin = sqrt4 * ((float) Math.sin(d5)) * 1.12f;
                float[] fArr6 = new float[3];
                fArr6[0] = cos;
                fArr6[1] = nextFloat19 * 1.12f;
                fArr6[i16] = sin;
                fArr5[i26] = fArr6;
            }
        }
        obj.q = new float[obj.N * 3];
    }

    void set(int i, float f, float f2, float f3, int i2) {
        int i3 = i * 4;
        this.P[i3] = f;
        this.P[i3 + 1] = f2;
        this.P[i3 + 2] = f3;
        this.kind[i] = i2;
    }

    void setLinks(List<Integer> list) {
        this.L = new int[list.size()];
        for (int i = 0; i < this.L.length; i++) {
            this.L[i] = list.get(i).intValue();
        }
        this.nl = this.L.length / 2;
        this.lines = new float[this.nl * 4];
    }

    void pr(float f, float f2, float f3, float f4, float f5, float[] fArr) {
        float f6 = (f * f4) - (f3 * f5);
        float f7 = (f * f5) + (f3 * f4);
        if (this.big) {
            float f8 = (f2 * 0.94f) - (f7 * 0.34f);
            float f9 = (f7 * 0.94f) + (f2 * 0.34f);
            float f10 = 1.6f / (2.4f - f9);
            fArr[0] = this.S + (f6 * this.R * f10 * 1.12f);
            fArr[1] = this.S + (f8 * this.R * f10 * 1.12f);
            fArr[2] = f9;
            return;
        }
        float f11 = (f2 * 0.96f) - (f7 * 0.18f);
        float f12 = (f7 * 0.96f) + (f2 * 0.18f);
        float f13 = 1.0f / (1.9f - (0.6f * f12));
        fArr[0] = this.S + (f6 * this.R * f13 * 1.2f);
        fArr[1] = this.S + (f11 * this.R * f13 * 1.2f);
        fArr[2] = f12;
    }

    static int step(String str) {
        if ("HIGH".equals(str)) {
            return 0;
        }
        return "BALANCED".equals(str) ? 4 : 2;
    }

    static boolean keep(int i, int i2) {
        return i2 == 0 || (i2 != 4 ? i % 2 == 0 : i % 4 != 3);
    }

    public void draw(Canvas canvas, float f, float f2, float f3, float f4) {
        int step = step(Perf.tier());
        double d = f4;
        float cos = (float) Math.cos(d);
        float sin = (float) Math.sin(d);
        int save = canvas.save();
        canvas.translate(f - (this.S * f3), f2 - (this.S * f3));
        canvas.scale(f3, f3);
        if (this.gu != f3) {
            this.gu = f3;
            grads();
        }
        for (int i = 0; i < this.N; i++) {
            int i2 = i * 4;
            pr(this.P[i2], this.P[i2 + 1], this.P[i2 + 2], cos, sin, this.w3);
            int i3 = i * 3;
            this.q[i3] = this.w3[0];
            this.q[i3 + 1] = this.w3[1];
            this.q[i3 + 2] = this.w3[2];
        }
        if (this.asm >= 0.0f) {
            if (this.sx == null) {
                Random random = new Random(5L);
                this.sx = new float[this.N];
                this.sy = new float[this.N];
                this.arr = new float[this.N];
                for (int i4 = 0; i4 < this.N; i4++) {
                    this.sx[i4] = this.S + (((random.nextFloat() * 2.0f) - 1.0f) * this.spanX);
                    this.sy[i4] = this.S + (((random.nextFloat() * 2.0f) - 1.0f) * this.spanY);
                }
            }
            for (int i5 = 0; i5 < this.N; i5++) {
                float point = AoaBootSeq.point(this.asm, i5);
                this.arr[i5] = point;
                int i6 = i5 * 3;
                this.q[i6] = this.sx[i5] + ((this.q[i6] - this.sx[i5]) * point);
                int i7 = i6 + 1;
                this.q[i7] = this.sy[i5] + ((this.q[i7] - this.sy[i5]) * point);
            }
        }
        this.p.setXfermode(null);
        this.p.setShader(null);
        this.add.setShader(null);
        if (!this.big) {
            home(canvas, f4, step);
        } else {
            page(canvas, f4, step);
        }
        canvas.restoreToCount(save);
    }

    void grads() {
        if (!this.big) {
            float f = this.S * 0.36f;
            this.core = new RadialGradient(this.S, this.S, this.S * 0.42f, new int[]{Theme.rgba(255, 120, 40, 1.0f), Theme.rgba(255, 170, 40, 0.62f), Theme.rgba(120, 30, 0, 0.0f)}, new float[]{0.0f, 0.4f, 1.0f}, Shader.TileMode.CLAMP);
            this.halo = new RadialGradient(this.S, this.S, 0.95f * this.S, new int[]{Theme.rgba(90, 30, 10, 0.25f), Theme.rgba(90, 30, 10, 0.25f), Theme.rgba(40, 12, 4, 0.0f)}, new float[]{0.0f, 0.21052632f, 1.0f}, Shader.TileMode.CLAMP);
            this.glass = new RadialGradient(this.S - (0.12f * f), this.S - (0.15f * f), f * 1.04f, new int[]{Theme.rgba(255, FxModel.TEST_GAP_MS, 70, 0.42f), Theme.rgba(255, FxModel.TEST_GAP_MS, 70, 0.42f), Theme.rgba(255, 120, 40, 0.46f), Theme.rgba(255, 130, 50, 0.7f), Theme.rgba(255, 170, 100, 0.9f)}, new float[]{0.0f, 0.1f, 0.72f, 0.93f, 1.0f}, Shader.TileMode.CLAMP);
            float f2 = f * 0.45f;
            this.hi = new RadialGradient(this.S - f2, this.S - (f * 0.5f), f2, Theme.rgba(255, 230, 200, 0.14f), Theme.rgba(255, 230, 200, 0.0f), Shader.TileMode.CLAMP);
            return;
        }
        this.ember = new RadialGradient(this.S, this.S, this.R * 0.76f, new int[]{Theme.rgba(120, 40, 20, 0.55f), Theme.rgba(60, 20, 12, 0.45f), Theme.rgba(30, 10, 8, 0.2f)}, new float[]{0.0f, 0.7f, 1.0f}, Shader.TileMode.CLAMP);
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x02f6 A[LOOP:3: B:59:0x02f3->B:61:0x02f6, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x033d  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x039e A[LOOP:5: B:74:0x0399->B:76:0x039e, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void home(android.graphics.Canvas r28, float r29, int r30) {
        /*
            Method dump skipped, instructions count: 993
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.AoaOrb.home(android.graphics.Canvas, float, int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x028e  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x02ac  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0297  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void page(android.graphics.Canvas r31, float r32, int r33) {
        /*
            Method dump skipped, instructions count: 920
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.AoaOrb.page(android.graphics.Canvas, float, int):void");
    }
}
