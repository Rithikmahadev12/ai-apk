package com.jarvis.assist.aosui.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import java.lang.reflect.Array;
import java.util.Arrays;

/* loaded from: /tmp/augmaos/classes.dex */
public final class OrbPainter {
    int bg;
    RadialGradient coreG;
    float gR;
    float gx;
    float gy;
    boolean light;
    static final float[] LR = {1.0f, 0.97f, 1.02f, 0.99f, 0.93f};
    static final float[] PH = {0.0f, 1.7f, 3.1f, 2.3f, 5.2f};
    static final float[] SP = {0.7f, 0.9f, 0.6f, 0.8f, 1.3f};
    static final PorterDuffXfermode ADD = new PorterDuffXfermode(PorterDuff.Mode.ADD);
    final int[] col = new int[5];
    public boolean fill = true;
    public boolean drift = true;
    public boolean additive = true;
    public float coreStop = 0.6f;
    public float coreA0 = 0.95f;
    public float coreA1 = 0.6f;
    final Path path = new Path();
    final Paint core = new Paint(1);
    final RadialGradient[][] gc = (RadialGradient[][]) Array.newInstance((Class<?>) RadialGradient.class, 5, 64);

    public void colors(int[] iArr, boolean z, int i) {
        System.arraycopy(iArr, 0, this.col, 0, 5);
        this.light = z;
        this.bg = i;
        drop();
    }

    void drop() {
        for (RadialGradient[] radialGradientArr : this.gc) {
            Arrays.fill(radialGradientArr, (Object) null);
        }
        this.coreG = null;
    }

    public void draw(Canvas canvas, Paint paint, float f, float f2, float f3, float f4, float f5, int i, int i2, boolean z, float f6, float f7) {
        float f8;
        float f9;
        float f10;
        float f11;
        int i3;
        Shader shader;
        float f12 = f3;
        float f13 = f4;
        if (f != this.gx || f2 != this.gy || f12 != this.gR) {
            this.gx = f;
            this.gy = f2;
            this.gR = f12;
            drop();
        }
        float f14 = 400.0f;
        int round = Math.round(f5 * 400.0f);
        paint.setShader(null);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStrokeJoin(Paint.Join.MITER);
        if (this.additive && !this.light) {
            paint.setXfermode(ADD);
        }
        int i4 = 0;
        while (true) {
            float f15 = f14;
            if (i4 >= Math.min(5, i)) {
                break;
            }
            float f16 = LR[i4];
            float f17 = PH[i4];
            float f18 = SP[i4];
            Path path = this.path;
            path.rewind();
            float cos = this.drift ? ((float) Math.cos((f13 * 0.3d) + f17)) * f12 * f5 * 0.6f : 0.0f;
            if (this.drift) {
                f10 = cos;
                f11 = ((float) Math.sin((f13 * 0.25d) + f17)) * f12 * f5 * 0.6f;
            } else {
                f10 = cos;
                f11 = 0.0f;
            }
            int i5 = 0;
            while (i5 <= i2) {
                int i6 = i4;
                float f19 = f11;
                int i7 = i5;
                double d = (i5 / i2) * 3.141592653589793d * 2.0d;
                double d2 = f13 * f18;
                double d3 = (2.0d * d) + d2;
                double d4 = f17;
                float f20 = f17;
                float sin = (float) (f12 * f16 * ((((Math.sin(d3 + d4) * 0.55d) + (Math.sin(((3.0d * d) - (0.7d * d2)) + (d4 * 1.3d)) * 0.3d) + (Math.sin((5.0d * d) + (d2 * 1.6d)) * 0.15d)) * f5) + 1.0d));
                float cos2 = f + (((float) Math.cos(d)) * sin) + f10;
                float sin2 = f2 + (((float) Math.sin(d)) * sin) + f19;
                if (i7 == 0) {
                    path.moveTo(cos2, sin2);
                } else {
                    path.lineTo(cos2, sin2);
                }
                i5 = i7 + 1;
                i4 = i6;
                f11 = f19;
                f17 = f20;
            }
            int i8 = i4;
            path.close();
            int i9 = this.col[i8];
            if (this.fill) {
                int max = Math.max(0, Math.min(63, round));
                RadialGradient radialGradient = this.gc[i8][max];
                if (radialGradient != null) {
                    i3 = i9;
                    shader = null;
                } else {
                    float f21 = f12 * f16 * ((round / f15) + 1.0f);
                    float f22 = 0.35f * f12;
                    float f23 = f22 / f21;
                    float f24 = (f22 + ((f21 - f22) * 0.72f)) / f21;
                    RadialGradient[] radialGradientArr = this.gc[i8];
                    i3 = i9;
                    shader = null;
                    RadialGradient radialGradient2 = new RadialGradient(f, f2, f21, new int[]{Theme.a(i9, 0.0f), Theme.a(i9, 0.0f), Theme.a(i9, 0.03f), Theme.a(i9, this.light ? 0.08f : 0.18f)}, new float[]{0.0f, f23, f24, 1.0f}, Shader.TileMode.CLAMP);
                    radialGradientArr[max] = radialGradient2;
                    radialGradient = radialGradient2;
                }
                paint.setStyle(Paint.Style.FILL);
                paint.setShader(radialGradient);
                canvas.drawPath(path, paint);
                paint.setShader(shader);
            } else {
                i3 = i9;
                shader = null;
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(f6);
            paint.setColor(Theme.a(i3, this.light ? 0.7f : 0.85f));
            canvas.drawPath(path, paint);
            if (z) {
                paint.setStrokeWidth(f7);
                paint.setColor(Theme.a(i3, this.light ? 0.07f : 0.16f));
                canvas.drawPath(path, paint);
            }
            i4 = i8 + 1;
            f12 = f3;
            f14 = f15;
            f13 = f4;
        }
        paint.setXfermode(null);
        if (this.coreG == null) {
            f8 = f;
            f9 = f2;
            this.coreG = new RadialGradient(f, f2, f3 * 0.8f, new int[]{Theme.a(this.bg, this.coreA0), Theme.a(this.bg, this.coreA1), Theme.a(this.bg, 0.0f)}, new float[]{0.0f, this.coreStop, 1.0f}, Shader.TileMode.CLAMP);
        } else {
            f8 = f;
            f9 = f2;
        }
        this.core.setShader(this.coreG);
        canvas.drawCircle(f8, f9, f3 * 0.8f, this.core);
    }
}
