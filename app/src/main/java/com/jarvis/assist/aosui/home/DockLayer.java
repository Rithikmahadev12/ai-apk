package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.PagerMath;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.PerfModel;
import com.jarvis.assist.aosui.core.Shapes;
import com.jarvis.assist.aosui.core.Theme;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class DockLayer extends View {
    float R;
    float T0;
    float T1;
    float bx;
    float by;
    final int[] cnt;
    boolean custom;
    float[] da;
    float[] df;
    final Paint dp;
    float[] dr;
    float[] dx;
    float[] dy;
    float f0;
    float f1;
    float fa;
    int frameN;
    final HomeCtx h;
    boolean hold;
    float hv;
    int idx;
    float ka;
    int[] key;
    float lk;
    final FrameClock.Handle loop;
    int n;
    int nd;
    final int[] off;
    float oh;
    int[] ord;
    float ow;
    float ox;
    final Paint p;
    final int[] pal;
    int palInk;
    boolean palLight;
    float[] pts;
    final float[] q;
    final RectF rf;
    float[] sz;
    long t0;
    float tS;
    float target;
    float top;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void configure(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        this.custom = true;
        this.bx = f;
        this.by = f2;
        this.R = f3;
        this.T0 = f4;
        this.T1 = f5;
        this.f0 = f6;
        this.f1 = f7;
        this.ox = f8;
        this.top = f9;
        this.ow = f10;
        this.oh = f11;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void portrait() {
        this.custom = false;
        Geo geo = this.h.g;
        this.bx = geo.bx;
        this.by = geo.by;
        this.R = geo.RO;
        this.T0 = 200.0f;
        this.T1 = 340.0f;
        this.f0 = 3.1415927f;
        this.f1 = 6.2831855f;
        this.ox = 0.0f;
        this.top = (geo.by - geo.RO) - 52.0f;
        this.ow = geo.LW;
        this.oh = geo.LH - this.top;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DockLayer(Context context, HomeCtx homeCtx) {
        super(context);
        this.p = new Paint(1);
        this.dp = new Paint();
        this.rf = new RectF();
        this.q = new float[2];
        this.T0 = 200.0f;
        this.T1 = 340.0f;
        this.n = 1;
        this.lk = -999.0f;
        this.dx = new float[0];
        this.dy = new float[0];
        this.da = new float[0];
        this.df = new float[0];
        this.dr = new float[0];
        this.pts = new float[0];
        this.pal = new int[289];
        this.palInk = -1;
        this.key = new int[0];
        this.ord = new int[0];
        this.sz = new float[0];
        this.cnt = new int[289];
        this.off = new int[289];
        this.f0 = 3.1415927f;
        this.f1 = 6.2831855f;
        this.t0 = System.currentTimeMillis();
        this.h = homeCtx;
        this.dp.setStrokeCap(Paint.Cap.SQUARE);
        this.dp.setAntiAlias(false);
        this.loop = FrameClock.I.add(new FrameClock.Sub() { // from class: com.jarvis.assist.aosui.home.DockLayer$$ExternalSyntheticLambda0
            @Override // com.jarvis.assist.aosui.core.FrameClock.Sub
            public final boolean frame(long j, float f) {
                return DockLayer.this.frame(j, f);
            }
        }, this);
    }

    float angFor(int i) {
        return PagerMath.knob(i, this.n, this.T0, this.T1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPages(int i, int i2) {
        this.n = Math.max(1, i);
        this.idx = i2;
        if (!this.hold) {
            this.target = angFor(i2);
        }
        this.loop.kick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepare() {
        float f;
        float f2;
        Geo geo = this.h.g;
        if (!this.custom) {
            portrait();
        }
        float f3 = Perf.k().dockSp;
        float f4 = this.R + 16.0f;
        float f5 = this.R + 44.0f;
        float f6 = f4;
        int i = 0;
        while (true) {
            double d = 5.2d;
            f = 5.0f;
            if (f6 > f5) {
                break;
            }
            double d2 = this.f0;
            while (d2 <= this.f1) {
                i++;
                d2 += (f3 * d) / f6;
                d = d;
            }
            f6 += 5.0f * f3;
        }
        this.dx = new float[i];
        this.dy = new float[i];
        this.da = new float[i];
        this.df = new float[i];
        this.dr = new float[i];
        this.nd = 0;
        float f7 = f4;
        while (f7 <= f5) {
            float f8 = 1.0f - ((f7 - f4) / (f5 - f4));
            double d3 = this.f0;
            while (d3 <= this.f1) {
                float f9 = f;
                double d4 = f7;
                float cos = (float) (this.bx + (Math.cos(d3) * d4));
                float sin = (float) (this.by + (Math.sin(d3) * d4));
                if (cos < 0.0f || cos > geo.LW || sin < 0.0f) {
                    f2 = f9;
                } else if (sin > geo.LH) {
                    f2 = f9;
                } else {
                    f2 = f9;
                    this.dx[this.nd] = cos - this.ox;
                    this.dy[this.nd] = sin - this.top;
                    this.da[this.nd] = (float) d3;
                    this.df[this.nd] = f8;
                    this.dr[this.nd] = f7;
                    this.nd++;
                }
                d3 += (f3 * 5.2d) / d4;
                f = f2;
            }
            float f10 = f;
            f7 += f3 * f10;
            f = f10;
        }
        this.key = new int[this.nd];
        this.ord = new int[this.nd];
        this.sz = new float[this.nd];
        this.pts = new float[this.nd * 2];
        palette();
        float angFor = angFor(this.idx);
        this.target = angFor;
        this.ka = angFor;
        this.lk = -999.0f;
        this.loop.kick();
        invalidate();
    }

    void palette() {
        Theme theme = this.h.th;
        this.palLight = theme.light;
        this.palInk = theme.INK;
        char c = 2;
        int[] iArr = theme.light ? new int[]{60, 70, 80} : new int[]{228, 235, 241};
        int[] iArr2 = theme.light ? new int[]{43, 109, 156} : new int[]{90, 169, 220};
        int[] iArr3 = {255, 77, 99};
        int i = 0;
        while (i <= 16) {
            float f = i / 16.0f;
            int[] mix = f < 0.5f ? mix(iArr, iArr2, f * 2.0f) : mix(iArr2, iArr3, (f - 0.5f) * 2.0f);
            int i2 = 0;
            while (i2 <= 16) {
                this.pal[(i * 17) + i2] = Theme.rgba(mix[0], mix[1], mix[c], i2 / 20.0f);
                i2++;
                c = 2;
            }
            i++;
            c = 2;
        }
    }

    static int[] mix(int[] iArr, int[] iArr2, float f) {
        float max = Math.max(0.0f, Math.min(1.0f, f));
        return new int[]{Math.round(iArr[0] + ((iArr2[0] - iArr[0]) * max)), Math.round(iArr[1] + ((iArr2[1] - iArr[1]) * max)), Math.round(iArr[2] + ((iArr2[2] - iArr[2]) * max))};
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int scrub(float f) {
        this.hold = true;
        this.fa = PagerMath.clamp(f, this.T0, this.T1);
        this.loop.kick();
        return PagerMath.page(this.fa, this.n, this.T0, this.T1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release(int i) {
        this.hold = false;
        this.ka = this.fa;
        this.idx = i;
        this.target = angFor(i);
        this.loop.kick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean frame(long j, float f) {
        PerfModel.Knobs k = Perf.k();
        boolean z = k.dock;
        if (!z && !this.hold) {
            this.ka = this.target;
            this.hv = 0.0f;
        }
        boolean objArr = this.hold || this.hv > 0.02f || Math.abs(this.target - this.ka) > 0.05f;
        this.frameN++;
        if (!this.hold) {
            this.ka += (this.target - this.ka) * 0.16f;
        }
        this.hv += ((this.hold ? 1.0f : 0.0f) - this.hv) * 0.15f;
        float f2 = this.hold ? this.fa : this.ka;
        if (this.lk == f2 && !objArr && z && this.frameN % k.dockDiv != 0) {
            return true;
        }
        this.lk = f2;
        this.tS = z ? ((float) (j - this.t0)) / 1000.0f : 0.0f;
        invalidate();
        return objArr || z;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        Canvas canvas2;
        int i2;
        int i3;
        Canvas canvas3 = canvas;
        Geo geo = this.h.g;
        Theme theme = this.h.th;
        if (this.nd == 0 && this.dx.length == 0) {
            return;
        }
        if (this.palLight != theme.light || this.palInk != theme.INK) {
            palette();
        }
        int save = canvas3.save();
        canvas3.scale(geo.S, geo.S);
        float f = this.hold ? this.fa : this.ka;
        float f2 = this.by - this.top;
        float f3 = this.bx - this.ox;
        float f4 = this.R + 8.0f;
        float radians = (float) Math.toRadians(f);
        float f5 = this.tS;
        float f6 = this.hv;
        int i4 = 0;
        Arrays.fill(this.cnt, 0);
        int i5 = 0;
        while (i5 < this.nd) {
            float f7 = this.da[i5];
            float f8 = f6;
            int i6 = i5;
            float max = (float) Math.max(0.0d, 1.0d - (Math.abs((((f7 - radians) + 9.42477796076938d) % 6.283185307179586d) - 3.141592653589793d) / 0.9d));
            float sin = (float) ((Math.sin(((f5 * 2.2d) + (f7 * 7.0f)) - (this.dr[i6] * 0.35d)) * 0.5d) + 0.5d);
            float f9 = max * max;
            float min = Math.min(0.8f, (((0.22f * sin * sin) + 0.1f) * this.df[i6]) + (((0.55f * f8) + 0.18f) * f9));
            float f10 = f9 * ((0.8f * f8) + 0.4f);
            int i7 = (int) ((min * 20.0f) + 0.5f);
            if (i7 == 0) {
                this.key[i6] = -1;
            } else {
                int min2 = (((int) (((f10 <= 1.0f ? f10 : 1.0f) * 16.0f) + 0.5f)) * 17) + Math.min(16, i7);
                this.key[i6] = min2;
                int[] iArr = this.cnt;
                iArr[min2] = iArr[min2] + 1;
                this.sz[i6] = (max * f8 * 0.7f) + 0.9f;
            }
            i5 = i6 + 1;
            f6 = f8;
        }
        float f11 = f6;
        int i8 = 0;
        int i9 = 0;
        while (true) {
            if (i8 >= 289) {
                break;
            }
            this.off[i8] = i9;
            i9 += this.cnt[i8];
            i8++;
        }
        for (int i10 = 0; i10 < this.nd; i10++) {
            int i11 = this.key[i10];
            if (i11 >= 0) {
                int[] iArr2 = this.off;
                int i12 = iArr2[i11];
                iArr2[i11] = i12 + 1;
                this.ord[i12] = i10;
            }
        }
        boolean z = f11 < 0.01f;
        this.dp.setStyle(Paint.Style.FILL);
        this.dp.setStrokeWidth(1.8f);
        this.dp.setStrokeCap(Paint.Cap.SQUARE);
        int i13 = 0;
        int i14 = 0;
        for (i = 289; i13 < i; i = 289) {
            int i15 = this.cnt[i13];
            if (i15 == 0) {
                i2 = i4;
                i3 = i13;
            } else {
                this.dp.setColor(this.pal[i13]);
                if (z) {
                    int i16 = i4;
                    int i17 = i14;
                    while (i17 < i14 + i15) {
                        int i18 = this.ord[i17];
                        int i19 = i16 + 1;
                        this.pts[i16] = this.dx[i18];
                        this.pts[i19] = this.dy[i18];
                        i17++;
                        i16 = i19 + 1;
                    }
                    canvas3.drawPoints(this.pts, i4, i16, this.dp);
                    i2 = i4;
                    i3 = i13;
                } else {
                    int i20 = i14;
                    while (i20 < i14 + i15) {
                        int i21 = this.ord[i20];
                        float f12 = this.sz[i21];
                        canvas.drawRect(this.dx[i21] - f12, this.dy[i21] - f12, this.dx[i21] + f12, this.dy[i21] + f12, this.dp);
                        i20++;
                        i13 = i13;
                        i4 = i4;
                    }
                    i2 = i4;
                    i3 = i13;
                }
                i14 += i15;
            }
            i13 = i3 + 1;
            canvas3 = canvas;
            i4 = i2;
        }
        int i22 = i4;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(theme.LINE2);
        this.rf.set(f3 - f4, f2 - f4, f3 + f4, f2 + f4);
        canvas.drawArc(this.rf, Math.min(this.T0, this.T1), Math.abs(this.T1 - this.T0), false, this.p);
        float f13 = this.T1 > this.T0 ? 1.0f : -1.0f;
        float f14 = f - (14.0f * f13);
        float max2 = f13 > 0.0f ? Math.max(Math.min(this.T0, this.T1), f14) : Math.min(Math.max(this.T0, this.T1), f14);
        float min3 = Math.min(f, max2);
        float max3 = Math.max(f, max2);
        this.p.setStrokeWidth(2.0f);
        this.p.setColor(theme.RED);
        if (max3 > min3) {
            canvas2 = canvas;
            canvas2.drawArc(this.rf, min3, max3 - min3, false, this.p);
        } else {
            canvas2 = canvas;
        }
        Shapes.pt(f3, f2, f4, f, this.q);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(theme.BG);
        canvas2.drawCircle(this.q[i22], this.q[1], 5.0f, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.6f);
        this.p.setColor(theme.RED);
        canvas2.drawCircle(this.q[i22], this.q[1], 5.0f, this.p);
        this.p.setStrokeCap(Paint.Cap.BUTT);
        canvas2.restoreToCount(save);
    }
}
