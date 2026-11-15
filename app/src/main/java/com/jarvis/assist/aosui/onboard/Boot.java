package com.jarvis.assist.aosui.onboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.BootSeq;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.OnboardModel;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Svg;
import com.jarvis.assist.aosui.core.Theme;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Boot extends View {
    static boolean played;
    float S;
    final boolean adv;
    final Path arcP;
    RadialGradient core;
    float coreR;
    float cx;
    float cy;
    final float dn;
    long el;
    boolean ended;
    final Host host;
    final String label;
    boolean land;
    long lastFrame;
    int lastStep;
    long leftAt;
    final Path loop;
    final Matrix mx;
    final Path[] nodeIc;
    final RectF oval;
    final Paint p;
    final int pts;
    final String[] ring;
    final boolean shortMode;
    float shown;
    final String[] steps;
    final float[] stopPos;
    final int[] stops;
    final String sub;
    SweepGradient sweepG;
    final Theme th;
    final String title;
    final Paint tp;
    float tx;
    float ty;
    final boolean unfold;

    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Host {
        void gone();

        float[] orbTarget();
    }

    public static String mode(String str, boolean z) {
        String bootMode = OnboardModel.bootMode(played, str, z);
        played = true;
        if ("skip".equals(bootMode)) {
            return null;
        }
        return bootMode;
    }

    public Boot(Context context, Theme theme, String str, boolean z, Host host) {
        this(context, theme, str, z, false, host);
    }

    public Boot(Context context, Theme theme, String str, boolean z, boolean z2, Host host) {
        super(context);
        this.p = new Paint(1);
        this.tp = new Paint(1);
        this.loop = new Path();
        this.arcP = new Path();
        this.oval = new RectF();
        this.mx = new Matrix();
        this.nodeIc = new Path[]{Svg.path("M-4 -4L4 4M-1 4H4V-1"), Svg.path("M4 0H-4M-1-3L-4 0-1 3"), Svg.path("M-4 4L4-4M-1-4H4V1")};
        this.leftAt = -1L;
        this.lastFrame = -1L;
        this.lastStep = -1;
        this.stopPos = new float[]{0.0f, 0.4f, 0.72f, 1.0f};
        this.ring = new String[3];
        this.th = theme;
        this.host = host;
        this.shortMode = z && !z2;
        this.unfold = z2;
        this.dn = context.getResources().getDisplayMetrics().density;
        this.adv = Advanced.on(Store.get(Advanced.KEY, ""));
        this.pts = BootSeq.points(Perf.tier());
        int i = theme.INK_RAW;
        int i2 = theme.ACCENT_RAW;
        this.stops = new int[]{i, i2, Theme.mix(i2, -16777216, 0.45f), theme.RED};
        String[] strArr = z2 ? BootSeq.UNFOLD_KEYS : BootSeq.STEP_KEYS;
        this.steps = new String[strArr.length];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            this.steps[i3] = Strings.t(strArr[i3]);
        }
        this.ring[0] = Strings.t("boot.r1");
        this.ring[1] = Strings.t("boot.r2");
        this.ring[2] = Strings.t("boot.r3");
        this.label = Strings.t("boot.label");
        this.title = Strings.t("boot.title");
        this.sub = Strings.t(this.adv ? "boot.sub.adv" : "boot.sub");
        setClickable(true);
        setImportantForAccessibility(1);
        setAccessibilityLiveRegion(1);
        setContentDescription(Strings.t("boot.loading") + ". " + Strings.t("boot.skip"));
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setStrokeJoin(Paint.Join.ROUND);
        Feedback.warm();
        Feedback.fx(this, 13);
    }

    public void mark(String str) {
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 1 && this.leftAt < 0 && this.lastFrame > 0) {
            this.leftAt = this.el;
            Feedback.fx(this, 0);
        }
        return true;
    }

    void lay() {
        float min;
        float f;
        float width = getWidth();
        float height = getHeight();
        this.land = width > height;
        if (this.land) {
            min = Math.min(0.76f * height, 0.46f * width);
            f = 440.0f;
        } else {
            min = Math.min(0.86f * width, 0.62f * height);
            f = 480.0f;
        }
        this.S = Math.min(min, this.dn * f);
        float f2 = (this.S * 0.11f) + (this.dn * 10.0f) + (this.S * 0.06f);
        if (this.land) {
            float f3 = 0.05f * width;
            float f4 = this.S * 0.9f;
            this.cx = ((width - ((this.S + f3) + f4)) / 2.0f) + (this.S / 2.0f);
            float f5 = height / 2.0f;
            this.cy = f5;
            this.tx = this.cx + (this.S / 2.0f) + f3 + (f4 / 2.0f);
            this.ty = f5 - (f2 / 2.0f);
        } else {
            float f6 = this.S * 0.06f;
            float f7 = this.S + f6 + f2 + (this.S * 0.09f);
            float f8 = width / 2.0f;
            this.cx = f8;
            this.cy = ((height - f7) / 2.0f) + (this.S / 2.0f);
            this.tx = f8;
            this.ty = this.cy + (this.S / 2.0f) + f6;
        }
        this.core = null;
        this.sweepG = new SweepGradient(this.cx, this.cy, this.stops, this.stopPos);
        this.mx.setRotate(-90.0f, this.cx, this.cy);
        this.sweepG.setLocalMatrix(this.mx);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        lay();
    }

    int mixC(float f) {
        float max = Math.max(0.0f, Math.min(1.0f, f));
        int i = 0;
        while (i < this.stopPos.length - 1) {
            int i2 = i + 1;
            if (max <= this.stopPos[i2]) {
                return Theme.mix(this.stops[i], this.stops[i2], (max - this.stopPos[i]) / (this.stopPos[i2] - this.stopPos[i]));
            }
            i = i2;
        }
        return this.stops[this.stops.length - 1];
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        Canvas canvas2 = canvas;
        if (this.ended) {
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        long min = this.lastFrame < 0 ? 0L : Math.min(64L, uptimeMillis - this.lastFrame);
        this.lastFrame = uptimeMillis;
        this.el += min;
        if (this.S == 0.0f) {
            lay();
        }
        if (this.leftAt < 0 && this.el >= BootSeq.leaveAt(this.shortMode, this.unfold)) {
            this.leftAt = this.el;
        }
        float fade = BootSeq.fade(this.el, this.leftAt, this.shortMode, this.unfold);
        float f2 = 1.0f;
        float f3 = 1.0f - fade;
        if (fade >= 1.0f) {
            end();
            return;
        }
        float f4 = (((float) this.el) / 1000.0f) * (this.shortMode ? 1.6f : 1.0f);
        int i = this.th.INK_RAW;
        int i2 = this.th.ACCENT_RAW;
        int i3 = this.th.RED;
        int i4 = this.stops[2];
        canvas2.drawColor(Theme.a(-16579579, 0.94f * f3));
        float progress = BootSeq.progress(this.el, this.shortMode, this.unfold);
        float f5 = 9.0f;
        this.shown += (progress - this.shown) * Math.min(1.0f, (((float) min) / 1000.0f) * 9.0f);
        if (progress >= 1.0f && this.shown > 0.995f) {
            this.shown = 1.0f;
        }
        int stepAt = BootSeq.stepAt(this.el, this.shortMode, this.unfold);
        if (stepAt != this.lastStep) {
            this.lastStep = stepAt;
            setContentDescription(Strings.t("boot.a11y", "n", Integer.valueOf(Math.round(this.shown * 100.0f))) + ". " + this.steps[stepAt]);
        }
        float f6 = this.S * 0.24f;
        float f7 = 0.04f;
        float sin = (((float) Math.sin(3.0f * f4)) * 0.04f) + 0.1f;
        int[] iArr = {i, i2, i3, i3, i4};
        this.p.setXfermode(null);
        this.p.setStyle(Paint.Style.STROKE);
        int i5 = 0;
        while (true) {
            float f8 = f2;
            if (i5 >= 5) {
                break;
            }
            float f9 = i5;
            float f10 = f5;
            float f11 = f9 * 1.3f;
            float f12 = (0.25f * f9) + 0.8f;
            float f13 = f7;
            float f14 = (i5 % 2 != 1 ? -1 : 1) * 0.8f * f4;
            this.loop.rewind();
            int i6 = 0;
            while (i6 <= this.pts) {
                int i7 = i;
                float f15 = f4;
                float f16 = f9;
                double d = (i6 / this.pts) * 3.141592653589793d * 2.0d;
                float f17 = f15 * f12;
                double d2 = f11;
                float sin2 = ((0.015f * f16) + 0.96f) * f6 * ((((float) ((Math.sin((d * 2.0d) + (f17 * 3.0f) + d2) * 0.6d) + (Math.sin(((3.0d * d) - (f17 * 2.0f)) + d2) * 0.4d))) * sin) + f8);
                double d3 = d + f14;
                float cos = this.cx + (((float) Math.cos(d3)) * sin2);
                float sin3 = this.cy + (((float) Math.sin(d3)) * sin2);
                Path path = this.loop;
                if (i6 == 0) {
                    path.moveTo(cos, sin3);
                } else {
                    path.lineTo(cos, sin3);
                }
                i6++;
                i = i7;
                f4 = f15;
                f9 = f16;
            }
            this.loop.close();
            this.p.setStrokeWidth(this.dn * 4.5f);
            this.p.setColor(Theme.a(iArr[i5], 0.16f * f3));
            canvas2.drawPath(this.loop, this.p);
            this.p.setStrokeWidth(this.dn);
            this.p.setColor(Theme.a(iArr[i5], f3 * 0.85f));
            canvas2.drawPath(this.loop, this.p);
            i5++;
            f2 = f8;
            f5 = f10;
            f7 = f13;
            f4 = f4;
        }
        int i8 = i;
        float f18 = f5;
        float f19 = f7;
        int i9 = 3;
        if (this.core == null || this.coreR != f6) {
            this.coreR = f6;
            this.core = new RadialGradient(this.cx, this.cy, f6 * 0.85f, new int[]{Theme.rgba(3, 4, 5, 0.95f), Theme.rgba(3, 4, 5, 0.0f)}, (float[]) null, Shader.TileMode.CLAMP);
        }
        this.p.setStyle(Paint.Style.FILL);
        this.p.setShader(this.core);
        float f20 = 255.0f * f3;
        this.p.setAlpha(Math.round(f20));
        canvas2.drawCircle(this.cx, this.cy, f6 * 0.85f, this.p);
        this.p.setShader(null);
        this.p.setAlpha(255);
        float f21 = this.S * 0.355f;
        float f22 = this.dn * 5.0f;
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeCap(Paint.Cap.BUTT);
        this.p.setStrokeWidth(this.dn);
        this.p.setColor(Theme.a(i8, 0.12f * f3));
        canvas2.drawCircle(this.cx, this.cy, f21, this.p);
        float f23 = 360.0f * this.shown;
        float f24 = 0.35f;
        if (f23 > 0.5f) {
            this.p.setShader(this.sweepG);
            this.p.setStrokeWidth(f22);
            this.p.setAlpha(Math.round(f20));
            this.oval.set(this.cx - f21, this.cy - f21, this.cx + f21, this.cy + f21);
            canvas2.drawArc(this.oval, -90.0f, f23, false, this.p);
            this.p.setShader(null);
            this.p.setAlpha(255);
            int mixC = mixC(this.shown);
            double radians = Math.toRadians(f23 - 90.0f);
            float cos2 = this.cx + (((float) Math.cos(radians)) * f21);
            float sin4 = this.cy + (((float) Math.sin(radians)) * f21);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(Theme.a(this.stops[0], f3));
            float f25 = f22 / 2.0f;
            canvas2.drawCircle(this.cx, this.cy - f21, f25, this.p);
            this.p.setColor(Theme.a(mixC, f3 * 0.35f));
            canvas2.drawCircle(cos2, sin4, 1.4f * f22, this.p);
            this.p.setColor(Theme.a(mixC, f3));
            canvas2.drawCircle(cos2, sin4, f25, this.p);
        }
        float f26 = this.S * 0.44f;
        double[] dArr = {-0.5235987755982989d, 1.5707963267948966d, 3.6651914291880923d};
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(this.dn * 0.8f);
        this.p.setColor(Theme.a(i8, 0.45f * f3));
        this.oval.set(this.cx - f26, this.cy - f26, this.cx + f26, this.cy + f26);
        int i10 = 0;
        while (true) {
            double d4 = 0.0d;
            f = 0.2f;
            if (i10 >= 3) {
                break;
            }
            double d5 = 0.2f;
            double d6 = dArr[i10] + d5;
            int i11 = i10 + 1;
            double d7 = dArr[i11 % 3];
            if (i10 == 2) {
                d4 = 6.283185307179586d;
            }
            canvas2.drawArc(this.oval, (float) Math.toDegrees(d6), (float) Math.toDegrees(((d7 + d4) - d5) - d6), false, this.p);
            i10 = i11;
        }
        float f27 = this.S * 0.022f;
        this.tp.setTypeface(Fonts.MONO);
        this.tp.setLetterSpacing(0.0f);
        this.tp.setTextAlign(Paint.Align.CENTER);
        this.tp.setTextSize(this.S * 0.03f);
        int i12 = 0;
        while (i12 < i9) {
            float cos3 = this.cx + (((float) Math.cos(dArr[i12])) * f26);
            float f28 = f26;
            float sin5 = this.cy + (((float) Math.sin(dArr[i12])) * f28);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(Theme.a(-16579579, f3));
            canvas2.drawCircle(cos3, sin5, f27, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(this.dn * 0.8f);
            this.p.setColor(Theme.a(i8, f3));
            canvas2.drawCircle(cos3, sin5, f27, this.p);
            int save = canvas2.save();
            canvas2.translate(cos3, sin5);
            float f29 = f27 / 6.0f;
            canvas2.scale(f29, f29);
            this.p.setStrokeWidth((1.3f / f29) * this.dn);
            canvas2.drawPath(this.nodeIc[i12], this.p);
            canvas2.restoreToCount(save);
            double d8 = f;
            double d9 = dArr[i12] + d8;
            int i13 = i12 + 1;
            double d10 = (dArr[i13 % 3] + (i12 == 2 ? 6.283185307179586d : 0.0d)) - d8;
            double d11 = ((((d9 + d10) / 2.0d) % 6.283185307179586d) + 6.283185307179586d) % 6.283185307179586d;
            boolean z = d11 > 0.0d && d11 < 3.141592653589793d;
            float f30 = f28 + ((z ? -1 : 1) * this.S * 0.03f);
            this.arcP.rewind();
            float f31 = f24;
            this.oval.set(this.cx - f30, this.cy - f30, this.cx + f30, this.cy + f30);
            float degrees = (float) Math.toDegrees(d10 - d9);
            if (z) {
                this.arcP.addArc(this.oval, (float) Math.toDegrees(d10), -degrees);
            } else {
                this.arcP.addArc(this.oval, (float) Math.toDegrees(d9), degrees);
            }
            this.tp.setColor(Theme.a(i8, f3));
            this.tp.setTextAlign(Paint.Align.LEFT);
            this.tp.setLetterSpacing(0.18f);
            canvas2 = canvas;
            canvas2.drawTextOnPath(this.ring[i12], this.arcP, Math.max(0.0f, (((float) (((f30 * 3.141592653589793d) * degrees) / 180.0d)) - this.tp.measureText(this.ring[i12])) / 2.0f), this.tp.getTextSize() * f31, this.tp);
            this.tp.setTextAlign(Paint.Align.CENTER);
            this.tp.setLetterSpacing(0.0f);
            f27 = f27;
            f26 = f28;
            i12 = i13;
            f24 = f31;
            i9 = 3;
            f = 0.2f;
        }
        float f32 = f26;
        this.oval.set(this.cx - f32, this.cy - f32, this.cx + f32, this.cy + f32);
        this.tp.setTypeface(Fonts.RAJ_400);
        this.tp.setTextSize(this.S * 0.13f);
        this.tp.setLetterSpacing(0.02f);
        this.tp.setColor(Theme.a(i8, f3));
        canvas2.drawText(Math.round(this.shown * 100.0f) + "%", this.cx, this.cy + (this.S * f19), this.tp);
        this.tp.setTypeface(Fonts.MONO);
        this.tp.setTextSize(this.S * 0.042f);
        this.tp.setLetterSpacing(0.6f);
        this.tp.setColor(Theme.a(-6641998, f3));
        canvas2.drawText(this.label, this.cx + (this.tp.getTextSize() * 0.3f), this.cy + (this.S * 0.1f), this.tp);
        this.tp.setTypeface(Fonts.RAJ_600);
        this.tp.setTextSize(this.S * 0.11f);
        this.tp.setLetterSpacing(0.3f);
        this.tp.setColor(Theme.a(i8, f3));
        float ascent = this.ty - this.tp.ascent();
        canvas2.drawText(this.title, this.tx + (this.tp.getTextSize() * 0.15f), ascent, this.tp);
        this.tp.setTypeface(Fonts.MONO);
        this.tp.setTextSize(Math.max(this.dn * f18, this.S * 0.036f));
        this.tp.setLetterSpacing(0.32f);
        this.tp.setColor(Theme.a(-6641998, f3));
        float ascent2 = (ascent + (this.dn * 10.0f)) - this.tp.ascent();
        canvas2.drawText(this.sub, this.tx + (this.tp.getTextSize() * 0.16f), ascent2, this.tp);
        this.tp.setTypeface(Fonts.RAJ_400);
        this.tp.setTextSize(Math.max(this.dn * 12.0f, this.S * 0.05f));
        this.tp.setLetterSpacing(0.06f);
        this.tp.setColor(Theme.a(-9734784, f3));
        canvas2.drawText(this.steps[stepAt], this.tx, ascent2 + (this.tp.getTextSize() * 0.9f) + (this.dn * 8.0f), this.tp);
        postInvalidateOnAnimation();
    }

    void end() {
        if (this.ended) {
            return;
        }
        this.ended = true;
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).removeView(this);
        }
        this.host.gone();
    }
}
