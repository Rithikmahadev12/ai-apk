package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.jarvis.assist.aosui.core.AoaBootSeq;
import com.jarvis.assist.aosui.core.BootSeq;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Flip7;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.onboard.Welcome;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaBoot extends View {
    static final int MID = -6902604;
    static final int O = -34278;
    static final int T = -11624262;
    static final int W = -1511182;
    final Path arc;
    final float dn;
    long el;
    boolean ended;
    final Host host;
    long lastFrame;
    boolean left;
    long leftAt;
    final boolean light;
    final AoaOrb orb;
    final Paint p;
    final Txt pct;
    final boolean plain;
    final RectF rf;
    final Txt stepT;
    final String[] steps;
    final Txt titleT;
    final Paint tp;
    final boolean unfold;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Host {
        void gone();

        void leave();

        float[] orbTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AoaBoot(Context context, boolean z, boolean z2, Host host) {
        super(context);
        this.orb = new AoaOrb(false);
        this.p = new Paint(1);
        this.tp = new Paint(1);
        this.rf = new RectF();
        this.arc = new Path();
        this.lastFrame = -1L;
        this.leftAt = -1L;
        this.host = host;
        this.unfold = z2;
        this.dn = context.getResources().getDisplayMetrics().density;
        this.light = "LIGHT".equals(Perf.tier());
        this.plain = z && !z2 && Welcome.animOff(context);
        String[] strArr = z2 ? BootSeq.UNFOLD_KEYS : BootSeq.STEP_KEYS;
        this.steps = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            this.steps[i] = Strings.t(strArr[i]).toUpperCase(Locale.getDefault());
        }
        this.pct = new Txt(Fonts.MONO, this.dn * 22.0f, this.dn * 2.0f).align(Paint.Align.CENTER);
        this.stepT = new Txt(Fonts.MONO, this.dn * 8.0f, this.dn * 2.4f).align(Paint.Align.CENTER);
        this.titleT = new Txt(Fonts.MONO, this.dn * 8.5f, this.dn * 2.6f).align(Paint.Align.CENTER);
        setClickable(true);
        setImportantForAccessibility(1);
        setContentDescription(Strings.t("boot.loading") + ". " + Strings.t("boot.skip"));
        Feedback.warm();
        Feedback.fx(this, 13);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 1 && this.leftAt < 0 && this.lastFrame > 0) {
            leave();
            Feedback.fx(this, 0);
        }
        return true;
    }

    void leave() {
        if (this.left) {
            return;
        }
        this.left = true;
        this.leftAt = this.el;
        this.host.leave();
    }

    long lv() {
        if (this.plain) {
            return 300L;
        }
        return AoaBootSeq.leave(this.unfold, this.light);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        float grid;
        Canvas canvas2;
        int i;
        Canvas canvas3 = canvas;
        if (this.ended) {
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        this.el += this.lastFrame < 0 ? 0L : Math.min(64L, uptimeMillis - this.lastFrame);
        this.lastFrame = uptimeMillis;
        if (this.leftAt < 0 && this.el >= lv()) {
            leave();
        }
        float fade = AoaBootSeq.fade(this.el, this.leftAt);
        if (fade >= 1.0f) {
            end();
            return;
        }
        float f2 = 1.0f - fade;
        int round = Math.round(255.0f * f2);
        float width = getWidth();
        float height = getHeight();
        float[] orbTarget = this.host.orbTarget();
        float f3 = orbTarget != null ? orbTarget[0] : width / 2.0f;
        float f4 = orbTarget != null ? orbTarget[1] : 0.3f * height;
        float f5 = orbTarget != null ? orbTarget[2] : 88.0f * this.dn;
        int saveLayerAlpha = canvas3.saveLayerAlpha(null, round);
        canvas3.drawColor(Flip7.BG);
        if (this.plain) {
            f = 2.0f;
            grid = 1.0f;
        } else {
            f = 2.0f;
            grid = AoaBootSeq.grid(this.el, this.unfold, this.light);
        }
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(Theme.rgba(160, 200, 212, 0.05f * grid));
        float f6 = this.dn * 27.0f;
        int i2 = saveLayerAlpha;
        float f7 = 0.0f;
        while (f7 < width) {
            canvas3.drawRect(f7, 0.0f, Math.max(1.0f, this.dn) + f7, height, this.p);
            f7 += f6;
            canvas3 = canvas;
            i2 = i2;
        }
        int i3 = i2;
        for (float f8 = 0.0f; f8 < height; f8 += f6) {
            canvas.drawRect(0.0f, f8, width, f8 + Math.max(1.0f, this.dn), this.p);
        }
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(Math.max(1.0f, this.dn * 0.8f));
        float f9 = 0.22f;
        char c = 192;
        int i4 = 214;
        int i5 = 220;
        this.p.setColor(Theme.rgba(192, 214, 220, grid * 0.22f));
        float f10 = this.dn * 71.0f;
        float f11 = this.dn * 5.0f;
        float f12 = f4 % f10;
        while (f12 < height) {
            float f13 = f3 % f10;
            while (f13 < width) {
                int i6 = i4;
                float f14 = f12;
                canvas.drawLine(f13 - f11, f14, f13 + f11, f12, this.p);
                float f15 = f13;
                canvas.drawLine(f15, f14 - f11, f13, f14 + f11, this.p);
                f13 = f15 + f10;
                i5 = i5;
                i4 = i6;
                f12 = f14;
                f9 = f9;
                c = 192;
            }
            f12 += f10;
            f9 = f9;
            c = 192;
        }
        int i7 = i4;
        float f16 = f9;
        int i8 = i5;
        float f17 = f5 / 176.0f;
        if (!this.plain) {
            this.orb.asm = AoaBootSeq.asm(this.el, this.unfold, this.light);
            this.orb.ign = AoaBootSeq.ignite(this.el, this.unfold, this.light);
            this.orb.rim = AoaBootSeq.rim(this.el, this.unfold, this.light);
            this.orb.fill = AoaBootSeq.fill(this.el, this.unfold, this.light);
            this.orb.spanX = width / f17;
            this.orb.spanY = height / f17;
        } else {
            this.orb.asm = -1.0f;
            AoaOrb aoaOrb = this.orb;
            AoaOrb aoaOrb2 = this.orb;
            this.orb.fill = 1.0f;
            aoaOrb2.rim = 1.0f;
            aoaOrb.ign = 1.0f;
        }
        this.orb.draw(canvas, f3, f4, f17, (((float) this.el) / 1000.0f) * 0.21f);
        if (!this.plain) {
            f2 = AoaBootSeq.readouts(this.el, this.unfold, this.light);
        }
        float progress = this.plain ? 1.0f : AoaBootSeq.progress(this.el, this.unfold, this.light);
        float f18 = f17 * 63.36f;
        float f19 = 1.06f * f5;
        if (f2 > 0.0f) {
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(1.0f, this.dn));
            this.p.setColor(Theme.rgba(192, i7, i8, f2 * f16));
            float f20 = f3 - f19;
            float f21 = f4 - f19;
            float f22 = f3 + f19;
            float f23 = f19 + f4;
            this.rf.set(f20, f21, f22, f23);
            canvas.drawArc(this.rf, 205.0f, 130.0f, false, this.p);
            this.p.setColor(Theme.a(-34278, f2));
            this.p.setStrokeWidth(this.dn * f);
            this.p.setStrokeCap(Paint.Cap.ROUND);
            this.rf.set((f3 - f18) - (this.dn * 6.0f), (f4 - f18) - (this.dn * 6.0f), f3 + f18 + (this.dn * 6.0f), f18 + f4 + (this.dn * 6.0f));
            if (progress > 0.0f) {
                i = -34278;
                canvas.drawArc(this.rf, -90.0f, progress * 360.0f, false, this.p);
            } else {
                i = -34278;
            }
            this.p.setStrokeCap(Paint.Cap.BUTT);
            this.tp.setTypeface(Fonts.MONO);
            this.tp.setTextSize(this.dn * 7.5f);
            this.tp.setLetterSpacing(0.34f);
            this.tp.setColor(Theme.a(W, f2));
            this.arc.rewind();
            this.rf.set(f20 + (this.dn * 8.0f), f21 + (this.dn * 8.0f), f22 - (this.dn * 8.0f), f23 - (this.dn * 8.0f));
            this.arc.addArc(this.rf, 205.0f, 130.0f);
            String t = Strings.t("aoa.core");
            canvas.drawTextOnPath(t, this.arc, Math.max(0.0f, (new PathMeasure(this.arc, false).getLength() - this.tp.measureText(t)) / f), 0.0f, this.tp);
            float f24 = f4 + f5 + (this.dn * 24.0f);
            this.pct.draw(canvas, Math.round(progress * 100.0f) + "%", f3, f24, Theme.a(W, f2));
            float f25 = f24 + (this.dn * 20.0f);
            this.stepT.draw(canvas, this.steps[this.plain ? this.steps.length - 1 : AoaBootSeq.step(this.el, this.unfold, this.light, this.steps.length)], f3, f25, Theme.a(MID, f2));
            float f26 = f25 + (this.dn * 18.0f);
            float w = this.titleT.w("aOS · AGE OF AUGMENTATION");
            this.titleT.align(Paint.Align.LEFT);
            this.titleT.tracked(canvas, " · AGE OF AUGMENTATION", this.titleT.tracked(canvas, "OS", this.titleT.tracked(canvas, "a", f3 - (w / f), f26, Theme.a(W, f2)), f26, Theme.a(i, f2)), f26, Theme.a(MID, f2));
            canvas2 = canvas;
            this.titleT.align(Paint.Align.CENTER);
        } else {
            canvas2 = canvas;
        }
        canvas2.restoreToCount(i3);
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
