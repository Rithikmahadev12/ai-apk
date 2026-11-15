package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import com.jarvis.assist.aosui.core.ChargeFx;
import com.jarvis.assist.aosui.core.Flip7;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Theme;

/* loaded from: /tmp/augmaos/classes.dex */
final class ChargeView extends View {
    final boolean fast;
    final float level;
    final Paint p;
    final RectF rf;
    final long t0;
    final Theme0 th;

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class Theme0 {
        int blue;
        int red;

        Theme0() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChargeView(Context context, float f, boolean z, int i, int i2) {
        super(context);
        this.t0 = SystemClock.uptimeMillis();
        this.p = new Paint(1);
        this.rf = new RectF();
        this.level = f;
        this.fast = z;
        this.th = new Theme0();
        this.th.red = i;
        this.th.blue = i2;
        setImportantForAccessibility(1);
        setContentDescription((z ? "Charging fast, " : "Charging, ") + Math.round(f * 100.0f) + " percent");
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        long uptimeMillis = SystemClock.uptimeMillis() - this.t0;
        float max = uptimeMillis < 250 ? ((float) uptimeMillis) / 250.0f : uptimeMillis > 2200 ? Math.max(0.0f, ((float) (ChargeFx.MS - uptimeMillis)) / 400.0f) : 1.0f;
        if (uptimeMillis >= ChargeFx.MS) {
            if (getParent() instanceof ViewGroup) {
                ((ViewGroup) getParent()).removeView(this);
                return;
            }
            return;
        }
        float f = getResources().getDisplayMetrics().density;
        float width = getWidth() / 2.0f;
        float height = getHeight() * 0.42f;
        float min = Math.min(getWidth(), getHeight()) * 0.22f;
        canvas.drawColor(Theme.a(Flip7.BG, 0.82f * max));
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(f);
        this.p.setColor(Theme.a(Flip7.G5, max));
        canvas.drawCircle(width, height, min, this.p);
        canvas.drawCircle(width, height, (10.0f * f) + min, this.p);
        SweepGradient sweepGradient = new SweepGradient(width, height, new int[]{this.th.red, -1774607, this.th.blue, this.th.blue}, new float[]{0.0f, 0.45f, 0.9f, 1.0f});
        Matrix matrix = new Matrix();
        matrix.setRotate(-90.0f, width, height);
        sweepGradient.setLocalMatrix(matrix);
        this.p.setShader(sweepGradient);
        this.p.setAlpha(Math.round(255.0f * max));
        this.p.setStrokeWidth(4.0f * f);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        float f2 = height + min;
        this.rf.set(width - min, height - min, width + min, f2);
        float fill = ChargeFx.fill(uptimeMillis, this.level);
        if (fill > 0.0f) {
            canvas.drawArc(this.rf, -90.0f, fill * 360.0f, false, this.p);
        }
        this.p.setShader(null);
        this.p.setStrokeCap(Paint.Cap.BUTT);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setTypeface(Fonts.RAJ_400);
        this.p.setTextAlign(Paint.Align.CENTER);
        this.p.setTextSize(0.55f * min);
        this.p.setColor(Theme.a(-1774607, max));
        canvas.drawText(Math.round(fill * 100.0f) + "%", width, height + (min * 0.2f), this.p);
        this.p.setTypeface(Fonts.MONO);
        this.p.setTextSize(12.0f * f);
        this.p.setLetterSpacing(0.3f);
        this.p.setColor(Theme.a(this.fast ? this.th.red : -6641998, max));
        canvas.drawText(this.fast ? "CHARGING · FAST" : "CHARGING", width, f2 + (f * 34.0f), this.p);
        this.p.setLetterSpacing(0.0f);
        postInvalidateOnAnimation();
    }
}
