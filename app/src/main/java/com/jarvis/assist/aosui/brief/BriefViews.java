package com.jarvis.assist.aosui.brief;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Theme;
import java.util.List;

/* loaded from: /tmp/augmaos/classes.dex */
final class BriefViews {
    private BriefViews() {
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class Panel extends Drawable {
        final float dn;
        final Paint f = new Paint(1);
        final Paint s = new Paint(1);
        final Paint b = new Paint();
        final RectF r = new RectF();

        Panel(int i, int i2, int i3, float f) {
            this.dn = f;
            this.f.setColor(i);
            this.s.setStyle(Paint.Style.STROKE);
            this.s.setColor(i2);
            this.s.setStrokeWidth(Math.max(1.0f, f));
            this.b.setColor(i3);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            float strokeWidth = this.s.getStrokeWidth() / 2.0f;
            float f = this.dn * 6.0f;
            float f2 = this.dn * 10.0f;
            float max = Math.max(1.0f, this.dn);
            this.r.set(bounds.left + strokeWidth, bounds.top + strokeWidth, bounds.right - strokeWidth, bounds.bottom - strokeWidth);
            canvas.drawRoundRect(this.r, f, f, this.f);
            canvas.drawRoundRect(this.r, f, f, this.s);
            float f3 = bounds.left;
            float f4 = bounds.top;
            float f5 = bounds.right;
            float f6 = bounds.bottom;
            float f7 = f3 + f2;
            float f8 = f4 + max;
            canvas.drawRect(f3, f4, f7, f8, this.b);
            float f9 = f3 + max;
            float f10 = f4 + f2;
            canvas.drawRect(f3, f4, f9, f10, this.b);
            float f11 = f5 - f2;
            canvas.drawRect(f11, f4, f5, f8, this.b);
            float f12 = f5 - max;
            canvas.drawRect(f12, f4, f5, f10, this.b);
            float f13 = f6 - max;
            canvas.drawRect(f3, f13, f7, f6, this.b);
            float f14 = f6 - f2;
            canvas.drawRect(f3, f14, f9, f6, this.b);
            canvas.drawRect(f11, f13, f5, f6, this.b);
            canvas.drawRect(f12, f14, f5, f6, this.b);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class ArcRule extends View {
        float a0;
        final Paint ac;
        final float dn;
        final Paint ln;
        final RectF o;
        float sw;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ArcRule(Context context, int i, int i2, float f) {
            super(context);
            this.ln = new Paint(1);
            this.ac = new Paint(1);
            this.o = new RectF();
            this.dn = f;
            Paint[] paintArr = {this.ln, this.ac};
            for (int i3 = 0; i3 < 2; i3++) {
                Paint paint = paintArr[i3];
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
            }
            this.ln.setStrokeWidth(Math.max(1.0f, f));
            this.ln.setColor(i);
            this.ac.setStrokeWidth(f * 1.5f);
            this.ac.setColor(i2);
            setImportantForAccessibility(2);
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            if (i == 0) {
                return;
            }
            float max = Math.max(this.dn * 2.0f, i2 - (this.dn * 2.0f));
            float f = (((i * i) / 4.0f) + (max * max)) / (max * 2.0f);
            float f2 = i / 2.0f;
            float f3 = (this.dn * 1.0f) + f;
            this.o.set(f2 - f, f3 - f, f2 + f, f3 + f);
            float degrees = (float) Math.toDegrees(Math.asin(Math.min(1.0f, (f2 - this.dn) / f)));
            this.a0 = 270.0f - degrees;
            this.sw = degrees * 2.0f;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.sw <= 0.0f) {
                return;
            }
            canvas.drawArc(this.o, this.a0, this.sw, false, this.ln);
            canvas.drawArc(this.o, this.a0, Math.min(this.sw, this.sw * 0.18f), false, this.ac);
            float width = this.o.width() / 2.0f;
            float centerX = this.o.centerX();
            float centerY = this.o.centerY();
            float[] fArr = {this.a0, this.a0 + this.sw};
            for (int i = 0; i < 2; i++) {
                double radians = Math.toRadians(fArr[i]);
                canvas.drawCircle((((float) Math.cos(radians)) * width) + centerX, (((float) Math.sin(radians)) * width) + centerY, this.dn * 2.2f, this.ln);
            }
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class MiniRing extends View {
        int acc;
        final Paint arc;
        final float dn;
        float frac;
        float k;
        int line;
        final RectF r;
        final RectF rail;
        final Paint tr;
        float ty;
        String v;
        final Paint val;

        /* JADX INFO: Access modifiers changed from: package-private */
        public MiniRing(Context context, int i, int i2, int i3, float f, float f2) {
            super(context);
            this.tr = new Paint(1);
            this.arc = new Paint(1);
            this.val = new Paint(1);
            this.r = new RectF();
            this.frac = Float.NaN;
            this.k = 1.0f;
            this.v = "";
            this.rail = new RectF();
            this.dn = f;
            Paint[] paintArr = {this.tr, this.arc};
            for (int i4 = 0; i4 < 2; i4++) {
                Paint paint = paintArr[i4];
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
            }
            this.tr.setStrokeWidth(Math.max(1.0f, f));
            this.tr.setColor(i2);
            this.arc.setStrokeWidth(2.0f * f);
            this.arc.setColor(i);
            this.line = i2;
            this.acc = i;
            this.val.setTypeface(Fonts.RAJ_600);
            this.val.setColor(i3);
            this.val.setTextSize(f * 13.0f * f2);
            this.val.setTextAlign(Paint.Align.CENTER);
            setImportantForAccessibility(2);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public MiniRing set(float f, String str) {
            if (!Float.isNaN(f)) {
                f = Math.max(0.0f, Math.min(1.0f, f));
            }
            this.frac = f;
            if (str == null) {
                str = "";
            }
            this.v = str;
            invalidate();
            return this;
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            float min = Math.min(i, i2);
            float strokeWidth = this.arc.getStrokeWidth();
            float f = i;
            float f2 = i2;
            this.r.set(((f - min) / 2.0f) + strokeWidth, ((f2 - min) / 2.0f) + strokeWidth, ((f + min) / 2.0f) - strokeWidth, ((min + f2) / 2.0f) - strokeWidth);
            Paint.FontMetrics fontMetrics = this.val.getFontMetrics();
            this.ty = (f2 / 2.0f) - ((fontMetrics.ascent + fontMetrics.descent) / 2.0f);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            canvas.drawOval(this.r, this.tr);
            float f = this.dn * 2.5f;
            this.rail.set(this.r.left + f, this.r.top + f, this.r.right - f, this.r.bottom - f);
            canvas.drawArc(this.rail, -60.0f, 300.0f, false, this.tr);
            if (!Float.isNaN(this.frac) && this.frac > 0.0f) {
                SweepGradient sweepGradient = new SweepGradient(this.r.centerX(), this.r.centerY(), new int[]{-3665874, -1774607, this.acc, this.acc}, new float[]{0.0f, 0.45f, 0.9f, 1.0f});
                Matrix matrix = new Matrix();
                matrix.setRotate(-90.0f, this.r.centerX(), this.r.centerY());
                sweepGradient.setLocalMatrix(matrix);
                this.arc.setShader(sweepGradient);
                canvas.drawArc(this.r, -90.0f, this.frac * 360.0f * this.k, false, this.arc);
                this.arc.setShader(null);
            }
            float width = ((this.r.width() - this.arc.getStrokeWidth()) * 0.78f) - (this.dn * 4.0f);
            float measureText = this.val.measureText(this.v);
            if (measureText > width && measureText > 0.0f) {
                canvas.save();
                float f2 = width / measureText;
                canvas.scale(f2, f2, getWidth() / 2.0f, getHeight() / 2.0f);
            }
            canvas.drawText(this.v, getWidth() / 2.0f, this.ty, this.val);
            if (measureText <= width || measureText <= 0.0f) {
                return;
            }
            canvas.restore();
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class DayMini extends View {
        float R;
        int acc;
        float[][] arcs;
        float[] dayA;
        final float dn;
        float elapsed;
        final Paint ev;
        int ink;
        float k;
        float nowDeg;
        final float[] pt;
        final RectF ri;
        final RectF ro;
        float[] sleepA;
        final Paint sun;
        final Paint sunF;
        float sx;
        float sy;
        final Paint thin;
        final Paint track;

        /* JADX INFO: Access modifiers changed from: package-private */
        public DayMini(Context context, float f) {
            super(context);
            this.track = new Paint(1);
            this.ev = new Paint(1);
            this.thin = new Paint(1);
            this.sun = new Paint(1);
            this.sunF = new Paint(1);
            this.ro = new RectF();
            this.ri = new RectF();
            this.pt = new float[2];
            this.arcs = new float[0][];
            this.nowDeg = 90.0f;
            this.k = 1.0f;
            this.dn = f;
            setImportantForAccessibility(2);
            Paint[] paintArr = {this.track, this.ev, this.thin, this.sun};
            for (int i = 0; i < 4; i++) {
                paintArr[i].setStyle(Paint.Style.STROKE);
            }
            this.ev.setStrokeCap(Paint.Cap.ROUND);
            this.thin.setStrokeCap(Paint.Cap.ROUND);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void colors(int i, int i2, int i3, int i4) {
            this.ink = i;
            this.acc = i2;
            this.track.setColor(i3);
            this.track.setStrokeWidth(Math.max(1.0f, this.dn));
            this.sun.setColor(i);
            this.sun.setStrokeWidth(this.dn * 1.5f);
            this.sunF.setColor(i4);
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void set(List<float[]> list, float[] fArr, float[] fArr2) {
            this.arcs = list.toArray(new float[0][]);
            this.sleepA = fArr;
            this.dayA = fArr2;
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void now(float f, float f2) {
            if (f == this.nowDeg && f2 == this.elapsed) {
                return;
            }
            this.nowDeg = f;
            this.elapsed = f2;
            layoutRing();
            invalidate();
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            layoutRing();
        }

        void layoutRing() {
            int width = getWidth();
            int height = getHeight();
            if (width == 0) {
                return;
            }
            float f = width / 2.0f;
            float f2 = height / 2.0f;
            this.R = (Math.min(width, height) / 2.0f) - (this.dn * 6.0f);
            float f3 = this.R - (this.dn * 7.0f);
            this.ro.set(f - this.R, f2 - this.R, this.R + f, this.R + f2);
            this.ri.set(f - f3, f2 - f3, f + f3, f3 + f2);
            DayRing.point(f, f2, this.R, this.nowDeg, this.pt);
            this.sx = this.pt[0];
            this.sy = this.pt[1];
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.R <= 0.0f) {
                return;
            }
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, this.R, this.track);
            if (this.dayA != null) {
                this.thin.setStrokeWidth(Math.max(1.0f, this.dn));
                this.thin.setColor(Theme.a(this.acc, 0.35f));
                canvas.drawArc(this.ri, this.dayA[0], this.dayA[1] * this.k, false, this.thin);
            }
            if (this.sleepA != null) {
                this.ev.setStrokeWidth(this.dn * 3.5f);
                this.ev.setColor(Theme.a(this.acc, 0.55f));
                canvas.drawArc(this.ri, this.sleepA[0], this.k * this.sleepA[1], false, this.ev);
            }
            this.ev.setStrokeWidth(this.dn * 4.0f);
            boolean z = false;
            for (float[] fArr : this.arcs) {
                if (fArr[2] > 1.0f) {
                    z = true;
                } else {
                    this.ev.setColor(fArr[2] > 0.0f ? this.acc : this.ink);
                    canvas.drawArc(this.ro, fArr[0], this.k * fArr[1], false, this.ev);
                }
            }
            this.thin.setStrokeWidth(Math.max(1.0f, this.dn));
            this.thin.setColor(Theme.a(this.acc, 0.6f));
            canvas.drawArc(this.ro, 90.0f, this.elapsed * this.k, false, this.thin);
            float f = this.dn * 3.5f;
            if (!z) {
                canvas.drawCircle(this.sx, this.sy, f, this.sunF);
            }
            if (z) {
                this.ev.setStrokeWidth(this.dn * 5.0f);
                this.ev.setColor(this.acc);
                for (float[] fArr2 : this.arcs) {
                    if (fArr2[2] > 1.0f) {
                        canvas.drawArc(this.ro, fArr2[0], fArr2[1] * this.k, false, this.ev);
                    }
                }
            }
            canvas.drawCircle(this.sx, this.sy, f, this.sun);
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class Icon extends View {
        int col;
        String k;
        final Paint p;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Icon(Context context, String str, int i) {
            super(context);
            this.p = new Paint(1);
            this.k = str;
            this.col = i;
            setImportantForAccessibility(2);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float min = Math.min(getWidth(), getHeight());
            Icons.draw(canvas, this.p, this.k, getWidth() / 2.0f, getHeight() / 2.0f, min, this.col, (1.3f * min) / 16.0f);
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class Spark extends View {
        final Paint base;
        final Paint dot;
        final Path fill;
        final Paint fp;
        final Paint glow;
        String label;
        final Paint ln;
        final Paint lt;
        float lx;
        float ly;
        final Path path;
        final Paint pd;
        float[] px;
        float[] py;
        float[] v;
        boolean zero;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Spark(Context context, int i, int i2, float f) {
            super(context);
            this.ln = new Paint(1);
            this.dot = new Paint(1);
            this.base = new Paint(1);
            this.pd = new Paint(1);
            this.path = new Path();
            this.v = new float[0];
            this.px = new float[0];
            this.py = new float[0];
            this.lx = -1.0f;
            this.label = "";
            this.lt = new Paint(1);
            this.fill = new Path();
            this.fp = new Paint(1);
            this.glow = new Paint(1);
            this.ln.setStyle(Paint.Style.STROKE);
            this.ln.setStrokeWidth(1.6f * f);
            this.ln.setStrokeCap(Paint.Cap.ROUND);
            this.ln.setStrokeJoin(Paint.Join.ROUND);
            this.ln.setColor(i);
            this.dot.setColor(i);
            this.pd.setColor(0);
            this.base.setColor(i2);
            this.base.setStrokeWidth(Math.max(1.0f, f));
            setImportantForAccessibility(2);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void set(float[] fArr) {
            this.v = fArr == null ? new float[0] : (float[]) fArr.clone();
            this.px = new float[this.v.length];
            this.py = new float[this.v.length];
            build();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Spark dots(int i) {
            this.pd.setColor(i);
            return this;
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            build();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Spark label(String str, float f) {
            if (str == null) {
                str = "";
            }
            this.label = str;
            this.lt.setTypeface(Fonts.MONO);
            this.lt.setTextSize(f);
            this.lt.setColor(this.dot.getColor());
            build();
            invalidate();
            return this;
        }

        void build() {
            this.path.reset();
            this.lx = -1.0f;
            int width = getWidth();
            int height = getHeight();
            if (width == 0 || this.v.length < 2) {
                return;
            }
            float f = -3.4028235E38f;
            float f2 = Float.MAX_VALUE;
            for (float f3 : this.v) {
                if (!Float.isNaN(f3)) {
                    f2 = Math.min(f2, f3);
                    f = Math.max(f, f3);
                }
            }
            if (f2 == Float.MAX_VALUE) {
                return;
            }
            if (this.zero) {
                f2 = Math.min(0.0f, f2);
                if (f <= 0.0f) {
                    f = 1.0f;
                }
            }
            if (f - f2 < 0.001f) {
                f2 -= 1.0f;
                f += 1.0f;
            }
            float strokeWidth = this.ln.getStrokeWidth();
            float[] area = SparkGeom.area(width, height, strokeWidth, strokeWidth * 1.7f, this.label.isEmpty() ? 0.0f : this.lt.measureText(this.label), strokeWidth * 3.0f);
            float length = (area[2] - area[0]) / (this.v.length - 1);
            boolean z = false;
            for (int i = 0; i < this.v.length; i++) {
                if (Float.isNaN(this.v[i])) {
                    z = false;
                } else {
                    float f4 = area[0] + (i * length);
                    float f5 = area[3] - (((this.v[i] - f2) / (f - f2)) * (area[3] - area[1]));
                    if (z) {
                        this.path.lineTo(f4, f5);
                    } else {
                        this.path.moveTo(f4, f5);
                    }
                    this.lx = f4;
                    this.ly = f5;
                    this.px[i] = f4;
                    this.py[i] = f5;
                    z = true;
                }
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            float height = getHeight() - 0.5f;
            float f2 = getResources().getDisplayMetrics().density * 1.6f;
            this.base.setStyle(Paint.Style.STROKE);
            float f3 = f2 * 2.0f;
            canvas.drawLine(f3, height, getWidth() - f3, height, this.base);
            float f4 = height - f2;
            canvas.drawCircle(f2, f4, f2, this.base);
            canvas.drawCircle(getWidth() - f2, f4, f2, this.base);
            if (this.lx >= 0.0f && !this.path.isEmpty()) {
                this.fill.set(this.path);
                int i = 0;
                while (true) {
                    if (i >= this.v.length) {
                        f = -1.0f;
                        break;
                    } else {
                        if (!Float.isNaN(this.v[i])) {
                            f = this.px[i];
                            break;
                        }
                        i++;
                    }
                }
                this.fill.lineTo(this.lx, height);
                this.fill.lineTo(f, height);
                this.fill.close();
                this.fp.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, getHeight(), new int[]{Theme.a(this.ln.getColor(), 0.35f), Theme.a(this.ln.getColor(), 0.0f)}, (float[]) null, Shader.TileMode.CLAMP));
                canvas.drawPath(this.fill, this.fp);
                this.glow.setColor(Theme.a(this.ln.getColor(), 0.25f));
                canvas.drawCircle(this.lx, this.ly, this.ln.getStrokeWidth() * 3.4f, this.glow);
            }
            canvas.drawPath(this.path, this.ln);
            if (this.pd.getColor() != 0) {
                for (int i2 = 0; i2 < this.v.length; i2++) {
                    if (!Float.isNaN(this.v[i2]) && i2 < this.px.length) {
                        canvas.drawCircle(this.px[i2], this.py[i2], this.ln.getStrokeWidth() * 0.9f, this.pd);
                    }
                }
            }
            if (this.lx >= 0.0f) {
                canvas.drawCircle(this.lx, this.ly, this.ln.getStrokeWidth() * 1.7f, this.dot);
                if (this.label.isEmpty()) {
                    return;
                }
                canvas.drawText(this.label, this.lx + (this.ln.getStrokeWidth() * 4.7f), SparkGeom.labelY(this.ly, this.lt.getTextSize(), getHeight()), this.lt);
            }
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class Hours extends View {
        int[] col;
        final float dn;
        String[][] h;
        final Paint ic;
        final Paint mono;
        final Theme th;
        final Paint val;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Hours(Context context, Theme theme, int i, int i2, float f, float f2) {
            super(context);
            this.mono = new Paint(1);
            this.val = new Paint(1);
            this.ic = new Paint(1);
            this.h = new String[0][];
            this.col = new int[0];
            this.dn = f;
            this.th = theme;
            this.mono.setTypeface(Fonts.MONO);
            this.mono.setColor(i);
            this.mono.setTextSize(8.5f * f * f2);
            this.mono.setTextAlign(Paint.Align.CENTER);
            this.val.setTypeface(Fonts.RAJ_600);
            this.val.setColor(i2);
            this.val.setTextSize(f * 14.0f * f2);
            this.val.setTextAlign(Paint.Align.CENTER);
            setImportantForAccessibility(2);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void set(List<String[]> list) {
            this.h = list.toArray(new String[0][]);
            this.col = new int[this.h.length];
            for (int i = 0; i < this.h.length; i++) {
                this.col[i] = this.th.sc2(this.h[i][2]);
            }
            requestLayout();
            invalidate();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), Math.round((this.mono.getTextSize() * 1.4f) + (this.dn * 20.0f) + (this.val.getTextSize() * 1.5f)));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.h.length == 0) {
                return;
            }
            float width = getWidth() / Math.max(6, this.h.length);
            float textSize = this.mono.getTextSize();
            int i = 0;
            while (i < this.h.length) {
                float f = (i * width) + (width / 2.0f);
                canvas.drawText(this.h[i][0], f, textSize, this.mono);
                Canvas canvas2 = canvas;
                Icons.draw(canvas2, this.ic, "partly".equals(this.h[i][2]) ? "weather" : this.h[i][2], f, (this.dn * 6.0f) + textSize + (this.dn * 7.0f), this.dn * 14.0f, this.col[i]);
                canvas2.drawText(this.h[i][1], f, (this.dn * 20.0f) + textSize + this.val.getTextSize(), this.val);
                i++;
                canvas = canvas2;
            }
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class PullScroll extends ScrollView {
        boolean busy;
        final float dn;
        boolean drag;
        TextView hint;
        L l;
        float pull;
        String sBusy;
        String sPull;
        String sRel;
        float y0;

        /* loaded from: /tmp/augmaos/classes.dex */
        interface L {
            void refresh();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PullScroll(Context context) {
            super(context);
            this.sPull = "";
            this.sRel = "";
            this.sBusy = "";
            this.dn = context.getResources().getDisplayMetrics().density;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void busy(boolean z) {
            this.busy = z;
            if (this.hint != null) {
                this.hint.setText(this.sBusy);
                this.hint.setAlpha(z ? 1.0f : 0.0f);
            }
            if (z) {
                return;
            }
            settle();
        }

        void settle() {
            View childAt = getChildAt(0);
            if (childAt != null) {
                childAt.animate().translationY(0.0f).setDuration(180L).start();
            }
            if (this.hint == null || this.busy) {
                return;
            }
            this.hint.animate().alpha(0.0f).setDuration(180L).start();
        }

        @Override // android.widget.ScrollView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() == 0) {
                this.y0 = motionEvent.getY();
                this.drag = false;
            }
            if (motionEvent.getActionMasked() != 2 || getScrollY() != 0 || this.busy || motionEvent.getY() - this.y0 <= this.dn * 12.0f) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            this.drag = true;
            return true;
        }

        @Override // android.widget.ScrollView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (this.drag) {
                int actionMasked = motionEvent.getActionMasked();
                boolean z = false;
                View childAt = getChildAt(0);
                if (actionMasked == 2) {
                    this.pull = Math.max(0.0f, (motionEvent.getY() - this.y0) * 0.45f);
                    if (childAt != null) {
                        childAt.setTranslationY(Math.min(this.pull, this.dn * 110.0f));
                    }
                    if (this.hint != null) {
                        this.hint.setAlpha(Math.min(1.0f, this.pull / (this.dn * 40.0f)));
                        this.hint.setText(this.pull > this.dn * 70.0f ? this.sRel : this.sPull);
                    }
                    return true;
                }
                if (actionMasked == 1 || actionMasked == 3) {
                    this.drag = false;
                    if (actionMasked == 1 && this.pull > this.dn * 70.0f) {
                        z = true;
                    }
                    this.pull = 0.0f;
                    if (!z || this.l == null) {
                        settle();
                    } else {
                        Feedback.fx(this, 2);
                        busy(true);
                        if (childAt != null) {
                            childAt.animate().translationY(this.dn * 34.0f).setDuration(120L).start();
                        }
                        this.l.refresh();
                    }
                    return true;
                }
            }
            return super.onTouchEvent(motionEvent);
        }
    }
}
