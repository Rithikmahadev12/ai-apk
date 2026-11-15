package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Shapes;
import com.jarvis.assist.aosui.core.Svg;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeData;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class LandLayer extends View {
    LinearGradient appsG;
    Txt bNum;
    LinearGradient dayG;
    Txt dkL;
    Txt dkL95;
    final Txt[] dkLT;
    Txt dkS;
    final Txt[] dkST;
    final HomeCtx h;
    Bitmap logo;
    Txt m9;
    Txt mono7;
    final Paint p;
    final float[] q;
    Txt raj13;
    Txt raj15;
    final RectF rf;
    Txt t65;
    Txt t7;
    Txt t75;
    Txt tab9;
    final Path tabP;
    final float[] tl;
    final float[] tr;
    final Txt[] whLT;
    final Txt[] whST;
    static final String[] MODEIC = {"M12 8.8a3.2 3.2 0 1 0 0 6.4 3.2 3.2 0 0 0 0-6.4zM5.5 5.5a9 9 0 0 0 0 13M18.5 5.5a9 9 0 0 1 0 13", "M3 7h7v2H3zM3 11h5v2H3zM3 15h6v2H3zM17 6a6 6 0 1 0 0 12 6 6 0 0 0 0-12z", "M2 4a11 11 0 0 1 0 16M22 4a11 11 0 0 0 0 16M12 10a2 2 0 1 0 0 4 2 2 0 0 0 0-4z"};
    static final Path[] MI = new Path[3];

    /* JADX INFO: Access modifiers changed from: package-private */
    public LandLayer(Context context, HomeCtx homeCtx) {
        super(context);
        this.p = new Paint(1);
        this.rf = new RectF();
        this.q = new float[2];
        this.tabP = new Path();
        this.dkST = new Txt[5];
        this.dkLT = new Txt[5];
        this.whST = new Txt[8];
        this.whLT = new Txt[8];
        this.tl = new float[3];
        this.tr = new float[3];
        this.h = homeCtx;
        this.logo = BackLayer.logo(context);
        for (int i = 0; i < 3; i++) {
            if (MI[i] == null) {
                MI[i] = Svg.path(MODEIC[i]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepare() {
        int i;
        int i2;
        float f;
        Geo geo = this.h.g;
        Theme theme = this.h.th;
        float f2 = this.h.zoom;
        float f3 = 6.5f;
        this.t65 = this.h.monoL(6.5f, 1.6f, 8.0f);
        this.t75 = this.h.monoL(7.5f, 2.0f, 8.0f);
        float f4 = 7.0f;
        this.t7 = this.h.monoL(7.0f, 1.4f, 8.0f);
        float L = this.h.L(9.0f, 9.0f);
        float f5 = 0.14f * L;
        this.m9 = new Txt(Fonts.MONO, L, f5);
        this.tab9 = new Txt(Fonts.MONO, L, f5);
        this.raj15 = new Txt(Fonts.RAJ_600, this.h.L(15.0f, 12.0f), 0.0f).align(Paint.Align.CENTER);
        float L2 = this.h.L(13.0f, 11.0f);
        this.raj13 = new Txt(Fonts.RAJ_600, L2, 0.06f * L2);
        this.mono7 = this.h.monoL(7.0f, 1.4f, 8.0f);
        Txt txt = null;
        this.dkL95 = null;
        this.dkS = null;
        this.dkL = null;
        boolean equals = "arc".equals(geo.mode);
        int i3 = 0;
        int i4 = 0;
        while (true) {
            i = 3;
            if (i4 >= geo.nw) {
                break;
            }
            HomeData.Entry entry = this.h.d.wheel.get(i4);
            if (equals) {
                i2 = i4;
                this.whLT[i2] = fit(entry.label, this.h.L(11.0f, 10.0f), 168.0f, new float[]{0.0f, 0.0f, 0.0f}, 1.5f, 64.0f);
                this.whST[i2] = txt;
                f = f4;
            } else {
                i2 = i4;
                f = f4;
                this.whST[i2] = fit(entry.sub, this.h.L(6.5f, 8.0f), geo.RO - 11.0f, geo.whA[i2], 1.4f, 0.0f);
                this.whLT[i2] = fit(entry.label, this.h.L(11.0f, 10.0f), geo.RO - 25.0f, geo.whA[i2], 1.4f, 0.0f);
            }
            i4 = i2 + 1;
            f4 = f;
            txt = null;
        }
        float f6 = f4;
        if (geo.full) {
            while (i3 < geo.nd) {
                HomeData.Entry entry2 = this.h.d.dock.get(i3);
                this.dkST[i3] = fit(entry2.sub, this.h.L(f3, 8.0f), geo.RO - 11.0f, geo.dkA[i3], 1.4f, 0.0f);
                this.dkLT[i3] = fit(entry2.label, this.h.L(11.0f, 10.0f), geo.RO - 25.0f, geo.dkA[i3], 1.4f, 0.0f);
                i3++;
                i = i;
                f3 = 6.5f;
            }
        }
        int i5 = i;
        float[] fArr = new float[i5];
        // fill-array-data instruction
        fArr[0] = 0.0f;
        fArr[1] = 0.55f;
        fArr[2] = 1.0f;
        this.dayG = new LinearGradient(geo.cx, geo.cy + 74.0f, geo.cx, geo.cy - 74.0f, new int[]{theme.BLUE, Theme.a(theme.BLUE, 0.5f), theme.RED}, fArr, Shader.TileMode.CLAMP);
        float f7 = geo.RA - f6;
        float[] fArr2 = new float[i5];
        // fill-array-data instruction
        fArr2[0] = 0.0f;
        fArr2[1] = 0.55f;
        fArr2[2] = 1.0f;
        this.appsG = new LinearGradient(geo.bx - f7, 0.0f, geo.bx + f7, 0.0f, new int[]{theme.BLUE, Theme.a(theme.BLUE, 0.5f), theme.RED}, fArr2, Shader.TileMode.CLAMP);
        measureTabs();
        invalidate();
    }

    Txt fit(String str, float f, float f2, float[] fArr, float f3, float f4) {
        Txt align = new Txt(Fonts.RAJ_600, f, f3).align(Paint.Align.CENTER);
        if (f4 <= 0.0f) {
            f4 = Geo.chord(f2, fArr[0], fArr[1]);
        }
        float w = align.w(str);
        if (w > f4 && w > 0.0f) {
            float max = Math.max(0.72f, f4 / w);
            align = new Txt(Fonts.RAJ_600, f * max, f3 * max).align(Paint.Align.CENTER);
            float w2 = align.w(str);
            if (w2 > f4) {
                align.p.setTextScaleX(f4 / w2);
            }
        }
        return align;
    }

    float brandW() {
        return this.h.brand.w("aOS") + 27.0f + this.h.brand.ls;
    }

    float tabW(int i) {
        return this.tab9.w(Geo.MODES[i].toUpperCase(Locale.ROOT)) + 29.0f + 12.0f;
    }

    String status() {
        return this.h.d.btPct + "%";
    }

    float statusW() {
        return this.m9.w(status()) + 38.0f + 7.0f + this.raj13.w(this.h.d.time) + 7.0f + this.m9.w(this.h.d.date);
    }

    void measureTabs() {
        Geo geo = this.h.g;
        if (!geo.tabsOn()) {
            return;
        }
        int i = 0;
        float f = 0.0f;
        int i2 = 0;
        while (i2 < 3) {
            f += tabW(i2) + (i2 > 0 ? 2 : 0);
            i2++;
        }
        float brandW = brandW() + 14.0f + (Math.max(0.0f, (((geo.LW - 28.0f) - brandW()) - f) - statusW()) / 2.0f);
        while (true) {
            float[] fArr = this.tl;
            if (i >= 3) {
                geo.tabs(fArr, this.tr);
                return;
            }
            fArr[i] = brandW;
            this.tr[i] = brandW + tabW(i);
            brandW = this.tr[i] + 2.0f;
            i++;
        }
    }

    Geo g() {
        return this.h.g;
    }

    void ring(Canvas canvas, float f, float f2, float f3, int i, float f4) {
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(f4);
        this.p.setColor(i);
        canvas.drawCircle(f, f2, f3, this.p);
    }

    void arc(Canvas canvas, float f, float f2, float f3, float f4, float f5, int i, float f6) {
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(f6);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(i);
        this.rf.set(f - f3, f2 - f3, f + f3, f2 + f3);
        canvas.drawArc(this.rf, f4, f5 - f4, false, this.p);
        this.p.setStrokeCap(Paint.Cap.BUTT);
    }

    void seg(Canvas canvas, Path path, boolean z) {
        Theme theme = this.h.th;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(z ? theme.ACT : theme.SEG);
        canvas.drawPath(path, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(z ? theme.BLUE : theme.LINE2);
        canvas.drawPath(path, this.p);
    }

    void arcText(Canvas canvas, float f, float f2, float f3, float f4, String str, Txt txt, int i) {
        float f5 = ((f4 % 360.0f) + 360.0f) % 360.0f;
        Shapes.pt(f, f2, f3, f5, this.q);
        float f6 = f5 + 90.0f;
        if (f5 > 0.0f && f5 < 180.0f) {
            f6 = f5 - 90.0f;
        }
        int save = canvas.save();
        canvas.rotate(f6, this.q[0], this.q[1]);
        txt.mid(canvas, str, this.q[0], this.q[1], i);
        canvas.restoreToCount(save);
    }

    void rotFit(Canvas canvas, float f, float f2, String str, Txt txt, int i, float f3, float f4) {
        int save = canvas.save();
        canvas.rotate(f3, f, f2);
        txt.drawFit(canvas, str, f, f2 + (txt.xh / 2.0f), i, f4);
        canvas.restoreToCount(save);
    }

    void rotText(Canvas canvas, float f, float f2, String str, Txt txt, int i, float f3) {
        int save = canvas.save();
        canvas.rotate(f3, f, f2);
        txt.mid(canvas, str, f, f2, i);
        canvas.restoreToCount(save);
    }

    void dot(Canvas canvas, Path path, int i, int i2) {
        if ((i != 0 || i2 > 0) && path != null) {
            path.computeBounds(this.rf, true);
            float width = this.rf.left + (this.rf.width() * 0.78f);
            float height = this.rf.top + (this.rf.height() * 0.22f);
            if (i2 > 0) {
                if (this.bNum == null) {
                    this.bNum = new Txt(Fonts.MONO, this.h.L(7.0f, 7.5f), 0.0f);
                }
                BackLayer.badge(canvas, this.p, this.bNum, width, height, i2);
                return;
            }
            this.p.setShader(null);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(i);
            canvas.drawCircle(width, height, 4.2f, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.5f);
            this.p.setColor(this.h.th.BG);
            canvas.drawCircle(width, height, 4.2f, this.p);
        }
    }

    static int ma(int i, float f) {
        return Theme.a(i, ((i >>> 24) / 255.0f) * f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        Canvas canvas2 = canvas;
        Geo geo = this.h.g;
        if (this.t65 == null || !geo.land) {
            return;
        }
        Theme theme = this.h.th;
        HomeData homeData = this.h.d;
        int save = canvas2.save();
        canvas2.translate(geo.ox, geo.oy);
        canvas2.scale(geo.S, geo.S);
        float f2 = geo.LW;
        float f3 = geo.LH;
        float f4 = geo.cx;
        float f5 = geo.cy;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(ma(theme.LINE2, 0.55f));
        float f6 = 40.0f;
        while (f6 < f3) {
            float f7 = 40.0f;
            while (f7 < f2) {
                float f8 = f7 + (((int) (f6 / 108.0f)) % 2 == 1 ? 67 : 0);
                if (f8 > f2 - 10.0f) {
                    f = f6;
                } else {
                    canvas2.drawLine(f8 - 5.0f, f6, f8 + 5.0f, f6, this.p);
                    f = f6;
                    canvas2 = canvas;
                    canvas2.drawLine(f8, f - 5.0f, f8, f + 5.0f, this.p);
                }
                f7 += 135.0f;
                f6 = f;
            }
            f6 += 108.0f;
        }
        if ("arc".equals(geo.mode)) {
            drawArcMode(canvas2, theme, homeData, geo);
        } else {
            drawWheelMode(canvas2, theme, homeData, geo);
        }
        topbar(canvas2, theme, homeData, geo);
        canvas2.restoreToCount(save);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v35 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6, types: [int] */
    void drawFull(Canvas canvas, Theme theme, HomeData homeData, Geo geo) {
        int i;
        float f;
        int i2;
        HomeData.Entry entry;
        float f2 = geo.cx;
        float f3 = geo.cy;
        float f4 = geo.RI;
        float f5 = geo.RO;
        float f6 = 10.0f;
        ring(canvas, f2, f3, geo.qsOut + 10.0f, ma(theme.LINE, 0.35f), 1.0f);
        LandLayer landLayer = this;
        Canvas canvas2 = canvas;
        float f7 = f2;
        float f8 = f3;
        int i3 = 0;
        int i4 = 0;
        while (i4 < geo.nq) {
            boolean z = (i4 >= homeData.qsOn.length || !homeData.qsOn[i4]) ? i3 : true;
            landLayer.seg(canvas2, geo.qs[i4], z);
            Shapes.pt(f7, f8, (geo.qsIn + geo.qsOut) / 2.0f, geo.qsA[i4][2], landLayer.q);
            Icons.draw(canvas2, landLayer.p, homeData.qs.get(i4).icon, landLayer.q[i3], landLayer.q[1], 11.0f, z ? theme.BLUE : theme.INK);
            i4++;
            i3 = 0;
            f6 = f6;
        }
        float f9 = f6;
        boolean z2 = i3;
        while (i3 < geo.nw) {
            landLayer.wedge(canvas2, i3, (landLayer.h.brief || i3 != landLayer.h.active) ? z2 : true);
            i3++;
        }
        ?? r1 = z2;
        while (r1 < geo.nd) {
            float f10 = geo.dkA[r1][2];
            HomeData.Entry entry2 = homeData.dock.get(r1);
            landLayer.seg(canvas2, geo.dk[r1], z2);
            if (landLayer.dkST[r1] != null) {
                float f11 = f7;
                int i5 = r1;
                Canvas canvas3 = canvas2;
                LandLayer landLayer2 = landLayer;
                float f12 = f8;
                i2 = i5;
                i = z2;
                entry = entry2;
                landLayer2.arcText(canvas3, f11, f12, f5 - 11.0f, f10, entry2.sub, landLayer.dkST[r1], theme.DIM);
                landLayer2.arcText(canvas, f11, f12, f5 - 25.0f, f10, entry.label, landLayer2.dkLT[i2], theme.INK);
                landLayer = landLayer2;
                f7 = f11;
                f = f12;
            } else {
                i = z2;
                f = f8;
                i2 = r1;
                entry = entry2;
            }
            Shapes.pt(f7, f, 9.0f + f4, f10, landLayer.q);
            float f13 = f;
            canvas2 = canvas;
            Icons.draw(canvas2, landLayer.p, entry.icon, landLayer.q[i], landLayer.q[1], 11.0f, theme.INK);
            landLayer.dot(canvas2, geo.dk[i2], homeData.dockDot[i2], i2 < homeData.dockCnt.length ? homeData.dockCnt[i2] : i);
            int i6 = i2 + 1;
            f8 = f13;
            z2 = i;
            r1 = i6;
        }
        float f14 = f8;
        float f15 = f7;
        HubRing.draw(canvas2, landLayer.p, landLayer.rf, landLayer.q, landLayer.h, f15, f14, landLayer.dayG, false);
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(theme.RED);
        canvas2.drawCircle(f15, (f14 - geo.qsOut) - f9, 2.4f, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(Theme.a(theme.RED, 0.5f));
        canvas2.drawCircle(f15, (f14 - geo.qsOut) - f9, 5.5f, this.p);
        Icons.draw(canvas2, this.p, this.h.appsIcon, geo.allApps.centerX(), (geo.allApps.bottom - 3.0f) - 7.0f, 14.0f, theme.INK);
        if (geo.msX >= 0.0f) {
            ministat(canvas, theme, homeData, geo.msX, geo.msY);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v0 */
    /* JADX WARN: Type inference failed for: r14v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r14v7 */
    void drawWheelMode(Canvas canvas, Theme theme, HomeData homeData, Geo geo) {
        char c;
        if (geo.full) {
            drawFull(canvas, theme, homeData, geo);
            return;
        }
        float f = geo.cx;
        float f2 = geo.cy;
        float f3 = geo.RI;
        float f4 = geo.RO;
        ring(canvas, f, f2, f4 + 40.0f, ma(theme.LINE, 0.4f), 1.0f);
        ring(canvas, f, f2, f4 + 70.0f, ma(theme.LINE, 0.22f), 1.0f);
        ?? r14 = 0;
        int i = 0;
        while (true) {
            c = 1;
            boolean z = true;
            if (i >= geo.nw) {
                break;
            }
            if (this.h.brief || i != this.h.active) {
                z = false;
            }
            wedge(canvas, i, z);
            i++;
        }
        float f5 = 8.0f + f4;
        arc(canvas, f, f2, f5, 186.0f, 354.0f, theme.LINE2, 1.0f);
        Canvas canvas2 = canvas;
        arc(canvas2, f, f2, f4 + 14.0f, 214.0f, 326.0f, theme.LINE, 1.0f);
        arc(canvas2, f, f2, f5, 186.0f, 194.0f, theme.RED, 2.0f);
        arc(canvas2, f, f2, f5, 346.0f, 354.0f, theme.BLUE, 2.0f);
        LandLayer landLayer = this;
        float f6 = f;
        float f7 = f2;
        if (landLayer.whLT.length > 0 && landLayer.whLT[0] != null) {
            Txt txt = landLayer.whLT[0];
        }
        int i2 = 0;
        LandLayer landLayer2 = landLayer;
        while (i2 < geo.nd) {
            float f8 = geo.dkA[i2][2];
            HomeData.Entry entry = homeData.dock.get(i2);
            landLayer2.seg(canvas2, geo.dk[i2], r14);
            Shapes.pt(f6, f7, 13.0f + f3, f8, landLayer2.q);
            int i3 = i2;
            int i4 = r14;
            float f9 = f3;
            char c2 = c;
            float f10 = f4;
            float f11 = f7;
            Icons.draw(canvas, landLayer2.p, entry.icon, landLayer2.q[r14], landLayer2.q[c], 13.0f, theme.INK);
            float f12 = f10 - 25.0f;
            float chord = Geo.chord(f12, geo.dkA[i3][i4], geo.dkA[i3][c2 == true ? 1 : 0]);
            Shapes.pt(f6, f11, f12, f8, landLayer2.q);
            float f13 = f8 - 90.0f;
            LandLayer landLayer3 = landLayer2;
            float f14 = f6;
            landLayer3.rotFit(canvas, landLayer2.q[i4], landLayer2.q[c2 == true ? 1 : 0], entry.label, landLayer2.dockLab(), theme.INK, f13, chord);
            Shapes.pt(f14, f11, f10 - 11.0f, f8, landLayer3.q);
            canvas2 = canvas;
            landLayer3.rotFit(canvas2, landLayer3.q[i4], landLayer3.q[c2 == true ? 1 : 0], entry.sub, landLayer3.dockSub(), theme.DIM, f13, chord);
            landLayer3.dot(canvas2, geo.dk[i3], homeData.dockDot[i3], i3 < homeData.dockCnt.length ? homeData.dockCnt[i3] : i4);
            i2 = i3 + 1;
            landLayer2 = landLayer3;
            f7 = f11;
            f6 = f14;
            r14 = i4;
            c = c2 == true ? 1 : 0;
            f3 = f9;
            f4 = f10;
        }
        float f15 = f7;
        float f16 = f6;
        float f17 = f3;
        float f18 = f4;
        LandLayer landLayer4 = landLayer2;
        landLayer4.p.setStyle(Paint.Style.STROKE);
        landLayer4.p.setStrokeWidth(1.0f);
        landLayer4.p.setColor(theme.LINE2);
        canvas2.drawLine((f16 - f18) - 4.0f, f15, (f16 - f17) + 4.0f, f15, landLayer4.p);
        canvas.drawLine((f16 + f17) - 4.0f, f15, f16 + f18 + 4.0f, f15, landLayer4.p);
        HubRing.draw(canvas, landLayer4.p, landLayer4.rf, landLayer4.q, landLayer4.h, f16, f15, landLayer4.dayG, false);
        Icons.draw(canvas, landLayer4.p, landLayer4.h.appsIcon, geo.allApps.centerX(), (geo.allApps.bottom - 3.0f) - 7.0f, 14.0f, theme.INK);
        if (geo.ringX >= 0.0f) {
            landLayer4.rings(canvas, theme, homeData, geo.ringX, geo.ringY);
        }
        if (geo.msX >= 0.0f) {
            ministat(canvas, theme, homeData, geo.msX, geo.msY);
        }
        qsRow(canvas, theme, homeData, geo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wedge(Canvas canvas, int i, boolean z) {
        float f;
        float f2;
        LandLayer landLayer;
        float f3;
        float f4;
        int i2;
        char c;
        float f5;
        HomeData.Entry entry;
        Geo geo = this.h.g;
        Theme theme = this.h.th;
        HomeData homeData = this.h.d;
        float f6 = geo.whA[i][0];
        float f7 = geo.whA[i][1];
        float f8 = geo.whA[i][2];
        float f9 = geo.cy;
        HomeData.Entry entry2 = homeData.wheel.get(i);
        seg(canvas, geo.wh[i], z);
        if (!"arc".equals(geo.mode)) {
            float f10 = geo.cx;
            float f11 = geo.RI;
            float f12 = geo.RO;
            if (z) {
                f = f12;
                f2 = f9;
                arc(canvas, f10, f2, f11 + 1.5f, f6 + 1.5f, f7 - 1.5f, theme.BLUE, 2.2f);
            } else {
                f = f12;
                f2 = f9;
            }
            arcText(canvas, f10, f2, f - 11.0f, f8, entry2.sub, this.whST[i], z ? theme.BLUE : theme.DIM);
            arcText(canvas, f10, f2, f - 25.0f, f8, entry2.label, this.whLT[i], z ? theme.BLUE : theme.INK);
            Shapes.pt(f10, f2, geo.full ? f11 + 9.0f : f11 + 13.0f, f8, this.q);
            Icons.draw(canvas, this.p, entry2.icon, this.q[0], this.q[1], geo.full ? 11.0f : 13.0f, z ? theme.BLUE : theme.INK);
            dot(canvas, geo.wh[i], homeData.wheelDot[i], i < homeData.wheelCnt.length ? homeData.wheelCnt[i] : 0);
            return;
        }
        float f13 = geo.wx;
        if (z) {
            i2 = 0;
            f5 = f8;
            c = 1;
            entry = entry2;
            arc(canvas, f13, f9, 113.5f, f6 + 1.2f, f7 - 1.2f, theme.BLUE, 2.2f);
            landLayer = this;
            f3 = f13;
            f4 = f9;
        } else {
            landLayer = this;
            f3 = f13;
            f4 = f9;
            i2 = 0;
            c = 1;
            f5 = f8;
            entry = entry2;
        }
        Shapes.pt(f3, f4, 126.0f, f5, landLayer.q);
        Icons.draw(canvas, landLayer.p, entry.icon, landLayer.q[i2], landLayer.q[c], 13.0f, z ? theme.BLUE : theme.INK);
        Shapes.pt(f3, f4, 168.0f, f5, landLayer.q);
        LandLayer landLayer2 = landLayer;
        landLayer2.rotText(canvas, landLayer.q[i2], landLayer.q[c], entry.label, landLayer.whLT[i], z ? theme.BLUE : theme.INK, f5);
        landLayer2.dot(canvas, geo.wh[i], homeData.wheelDot[i], i < homeData.wheelCnt.length ? homeData.wheelCnt[i] : i2);
    }

    Txt dockLab() {
        if (this.dkL == null) {
            this.dkL = new Txt(Fonts.RAJ_600, this.h.L(11.0f, 10.0f), 1.4f).align(Paint.Align.CENTER);
        }
        return this.dkL;
    }

    Txt dockSub() {
        if (this.dkS == null) {
            this.dkS = new Txt(Fonts.RAJ_600, this.h.L(6.5f, 8.0f), 1.4f).align(Paint.Align.CENTER);
        }
        return this.dkS;
    }

    Txt dockLab95() {
        if (this.dkL95 == null) {
            this.dkL95 = new Txt(Fonts.RAJ_600, this.h.L(9.5f, 10.0f), 1.5f).align(Paint.Align.CENTER);
        }
        return this.dkL95;
    }

    void drawArcMode(Canvas canvas, Theme theme, HomeData homeData, Geo geo) {
        char c;
        float f = geo.wx;
        float f2 = geo.cy;
        ring(canvas, f, f2, 250.0f, ma(theme.LINE, 0.4f), 1.0f);
        Canvas canvas2 = canvas;
        ring(canvas2, f, f2, 282.0f, ma(theme.LINE, 0.25f), 1.0f);
        boolean z = false;
        int i = 0;
        while (true) {
            c = 2;
            if (i >= geo.nq) {
                break;
            }
            boolean z2 = i < homeData.qsOn.length && homeData.qsOn[i];
            seg(canvas2, geo.qs[i], z2);
            Shapes.pt(f, f2, 221.0f, geo.qsA[i][2], this.q);
            Icons.draw(canvas, this.p, homeData.qs.get(i).icon, this.q[0], this.q[1], 12.0f, z2 ? theme.BLUE : theme.INK);
            i++;
            canvas2 = canvas;
        }
        int i2 = 1;
        arc(canvas, f, f2, 243.0f, -38.0f, 38.0f, theme.LINE2, 1.0f);
        arc(canvas, f, f2, 248.0f, -22.0f, 22.0f, theme.LINE, 1.0f);
        arc(canvas, f, f2, 243.0f, -38.0f, -32.0f, theme.RED, 2.0f);
        arc(canvas, f, f2, 243.0f, 32.0f, 38.0f, theme.BLUE, 2.0f);
        int i3 = 0;
        while (i3 < geo.nw) {
            wedge(canvas, i3, !this.h.brief && i3 == this.h.active);
            i3++;
        }
        arc(canvas, f, f2, 104.0f, -50.0f, 50.0f, theme.LINE, 1.0f);
        Canvas canvas3 = canvas;
        arc(canvas3, f, f2, 100.0f, -12.0f, 12.0f, theme.BLUE, 2.0f);
        LandLayer landLayer = this;
        float f3 = geo.bx;
        float f4 = geo.by;
        float f5 = geo.RO;
        float f6 = geo.RI;
        float f7 = geo.RA;
        int i4 = 0;
        while (i4 < geo.nd) {
            float f8 = geo.dkA[i4][c];
            HomeData.Entry entry = homeData.dock.get(i4);
            int i5 = i2;
            landLayer.seg(canvas3, geo.dk[i4], z);
            int i6 = z;
            Shapes.pt(f3, f4, f6 + 12.0f, f8, landLayer.q);
            float f9 = f6;
            float f10 = f7;
            int i7 = i4;
            float f11 = f5;
            Icons.draw(canvas, landLayer.p, entry.icon, landLayer.q[i6], landLayer.q[i5], 13.0f, theme.INK);
            Shapes.pt(f3, f4, f11 - 21.0f, f8, landLayer.q);
            float f12 = f4;
            LandLayer landLayer2 = landLayer;
            canvas3 = canvas;
            landLayer2.rotFit(canvas3, landLayer.q[i6], landLayer.q[i5], entry.label, landLayer.dockLab95(), theme.INK, f8 - 180.0f, 50.0f);
            landLayer = landLayer2;
            landLayer.dot(canvas3, geo.dk[i7], homeData.dockDot[i7], i7 < homeData.dockCnt.length ? homeData.dockCnt[i7] : i6);
            i4 = i7 + 1;
            f4 = f12;
            i2 = i5;
            z = i6;
            f5 = f11;
            f6 = f9;
            f7 = f10;
            c = 2;
        }
        float f13 = f7;
        boolean z3 = z;
        float f14 = f4;
        landLayer.p.setStyle(Paint.Style.FILL);
        landLayer.p.setColor(theme.ACT);
        canvas3.drawPath(geo.apps, landLayer.p);
        landLayer.p.setStyle(Paint.Style.STROKE);
        landLayer.p.setStrokeWidth(1.0f);
        landLayer.p.setColor(theme.BLUE);
        canvas3.drawPath(geo.apps, landLayer.p);
        float f15 = f13 - 7.0f;
        landLayer.p.setShader(landLayer.appsG);
        landLayer.p.setStrokeWidth(1.6f);
        landLayer.p.setStrokeCap(Paint.Cap.ROUND);
        landLayer.p.setColor(-16777216);
        landLayer.rf.set(f3 - f15, f14 - f15, f3 + f15, f15 + f14);
        canvas.drawArc(landLayer.rf, 96.0f, 168.0f, false, landLayer.p);
        landLayer.p.setShader(null);
        landLayer.p.setStrokeCap(Paint.Cap.BUTT);
        arc(canvas, f3, f14, f13 - 13.0f, 120.0f, 240.0f, theme.LINE, 1.0f);
        float f16 = f3 - (0.52f * f13);
        Icons.draw(canvas, this.p, this.h.appsIcon, f16, f14 - 9.0f, 17.0f, theme.INK);
        String[] split = this.h.appsLabel.split(" ", 2);
        this.t75.mid(canvas, split[z3 ? 1 : 0], f16, f14 + 12.0f, theme.INK);
        if (split.length > i2) {
            this.t75.mid(canvas, split[i2], f16, f14 + 22.0f, theme.INK);
        }
        ministat(canvas, theme, homeData, geo.msX, geo.msY);
    }

    void qsRow(Canvas canvas, Theme theme, HomeData homeData, Geo geo) {
        Canvas canvas2;
        int i = 0;
        while (i < geo.nq) {
            RectF rectF = geo.qsDot[i];
            if (rectF.isEmpty()) {
                canvas2 = canvas;
            } else {
                boolean z = i < homeData.qsOn.length && homeData.qsOn[i];
                float centerX = rectF.centerX();
                float centerY = rectF.centerY();
                if (z) {
                    this.p.setShader(null);
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(theme.ACT);
                    canvas.drawCircle(centerX, centerY, 13.0f, this.p);
                }
                Canvas canvas3 = canvas;
                ring(canvas3, centerX, centerY, 12.5f, z ? theme.BLUE : theme.LINE2, 1.0f);
                canvas2 = canvas3;
                Icons.draw(canvas2, this.p, homeData.qs.get(i).icon, centerX, centerY, 12.0f, z ? theme.BLUE : theme.INK);
            }
            i++;
            canvas = canvas2;
        }
    }

    void rings(Canvas canvas, Theme theme, HomeData homeData, float f, float f2) {
        int sc = theme.sc(homeData.btState);
        int sc2 = theme.sc(homeData.wxState);
        dialAt(canvas, theme, f + 30.0f, f2, homeData.btPct + "%", homeData.btLabel, homeData.btPct / 100.0f, "batt", sc, "crit".equals(homeData.btState));
        dialAt(canvas, theme, (f + g().ringW) - 30.0f, f2, homeData.wxTemp, homeData.wxDesc, 0.55f, homeData.wxIcon, sc2, false);
    }

    void dialAt(Canvas canvas, Theme theme, float f, float f2, String str, String str2, float f3, String str3, int i, boolean z) {
        float f4 = f2 + 18.0f;
        ring(canvas, f, f4, 13.0f, theme.LINE, 1.0f);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.6f);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(i);
        this.rf.set(f - 13.0f, f4 - 13.0f, f + 13.0f, 13.0f + f4);
        if (f3 > 0.0f) {
            canvas.drawArc(this.rf, -90.0f, Math.min(1.0f, f3) * 360.0f, false, this.p);
        }
        this.p.setStrokeCap(Paint.Cap.BUTT);
        Icons.draw(canvas, this.p, str3, f, f4, 12.0f, i);
        float f5 = g().ringW - 64.0f;
        float f6 = f2 + 48.0f;
        this.raj15.drawFit(canvas, str, f, f6, theme.INK, f5);
        this.mono7.drawFit(canvas, str2, f, f6 + 3.0f + this.mono7.line(), i == theme.INK ? theme.DIM : i, f5);
    }

    void ministat(Canvas canvas, Theme theme, HomeData homeData, float f, float f2) {
        float f3 = f2 + 8.0f;
        int sc = theme.sc(homeData.btState);
        int sc2 = theme.sc(homeData.wxState);
        Icons.draw(canvas, this.p, "batt", f + 6.5f, f3, 12.0f, sc);
        float f4 = f + 17.0f;
        String str = homeData.btPct + "%";
        Txt txt = this.m9;
        float f5 = f3 + (this.m9.cap / 2.0f);
        if ("norm".equals(homeData.btState)) {
            sc = theme.INK;
        }
        txt.draw(canvas, str, f4, f5, sc);
        float w = this.m9.w(str) + 10.0f + f4;
        Icons.draw(canvas, this.p, homeData.wxIcon, w + 6.5f, f3, 12.0f, sc2);
        Txt txt2 = this.m9;
        txt2.draw(canvas, homeData.wxTemp + " " + homeData.wxDesc, w + 17.0f, f3 + (this.m9.cap / 2.0f), sc2);
    }

    void topbar(Canvas canvas, Theme theme, HomeData homeData, Geo geo) {
        Canvas canvas2;
        float f;
        float f2;
        float f3;
        float f4;
        int i;
        float f5 = geo.LW;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(Theme.a(theme.BG, 0.18f));
        canvas.drawRect(0.0f, 0.0f, f5, 30.0f, this.p);
        this.p.setColor(theme.LINE);
        canvas.drawRect(0.0f, 29.0f, f5, 30.0f, this.p);
        float f6 = 30.0f;
        float f7 = 14.0f;
        if (this.logo != null) {
            this.rf.set(14.0f, 7.109375f, 34.0f, 22.890625f);
            canvas.drawBitmap(this.logo, (Rect) null, this.rf, (Paint) null);
        }
        Txt txt = this.h.brand;
        float f8 = 2.0f;
        float f9 = 15.0f + (txt.cap / 2.0f);
        txt.tracked(canvas, "OS", txt.tracked(canvas, "a", 41.0f, f9, theme.INK), f9, theme.RED);
        if (geo.tabsOn()) {
            int i2 = 0;
            while (i2 < 3) {
                boolean equals = Geo.MODES[i2].equals(geo.mode);
                float f10 = this.tl[i2];
                float f11 = this.tr[i2];
                float f12 = equals ? 5.0f : 6.0f;
                int i3 = equals ? theme.BLUE : theme.DIM;
                if (equals) {
                    this.tabP.rewind();
                    float f13 = f10 + 0.5f;
                    this.tabP.moveTo(f13, f6);
                    float f14 = f12 + 6.0f;
                    this.tabP.lineTo(f13, f14);
                    float f15 = f12 + 0.5f;
                    this.tabP.quadTo(f13, f15, f10 + 6.0f, f15);
                    this.tabP.lineTo(f11 - 6.0f, f15);
                    float f16 = f11 - 0.5f;
                    this.tabP.quadTo(f16, f15, f16, f14);
                    this.tabP.lineTo(f16, f6);
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(theme.BG);
                    float f17 = f12 + 1.0f;
                    float f18 = f6;
                    i = i3;
                    f3 = f7;
                    f4 = 0.5f;
                    f = f8;
                    canvas2 = canvas;
                    canvas2.drawRect(f10 + 1.0f, f17, f11 - 1.0f, f18, this.p);
                    f2 = f18;
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(1.0f);
                    this.p.setColor(theme.LINE2);
                    canvas2.drawPath(this.tabP, this.p);
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(theme.BLUE);
                    canvas2.drawRect(f10 + 3.0f, f17, f11 - 3.0f, f12 + 3.0f, this.p);
                } else {
                    canvas2 = canvas;
                    f = f8;
                    f2 = f6;
                    f3 = f7;
                    f4 = 0.5f;
                    i = i3;
                }
                float f19 = f12 + ((f2 - f12) / f);
                int save = canvas2.save();
                float f20 = f10 + 12.0f;
                canvas2.translate(f20, f19 - 6.0f);
                canvas2.scale(f4, f4);
                this.p.setStyle(Paint.Style.STROKE);
                this.p.setStrokeWidth(1.8f);
                this.p.setStrokeCap(Paint.Cap.ROUND);
                this.p.setColor(i);
                canvas2.drawPath(MI[i2], this.p);
                this.p.setStrokeCap(Paint.Cap.BUTT);
                canvas2.restoreToCount(save);
                this.tab9.draw(canvas, Geo.MODES[i2].toUpperCase(Locale.ROOT), f20 + 12.0f + 5.0f, f19 + (this.tab9.cap / f), i);
                i2++;
                f8 = f;
                f7 = f3;
                f6 = f2;
            }
        }
        float f21 = f8;
        float statusW = (f5 - f7) - statusW();
        int sc = theme.sc(homeData.btState);
        Icons.draw(canvas, this.p, "wifi", statusW + 5.5f, 15.0f, 11.0f, theme.INK);
        float f22 = statusW + 18.0f;
        Icons.draw(canvas, this.p, "batt", f22 + 6.5f, 15.0f, 13.0f, sc);
        float f23 = f22 + 20.0f;
        this.m9.draw(canvas, status(), f23, 15.0f + (this.m9.cap / f21), "norm".equals(homeData.btState) ? theme.INK : sc);
        float w = f23 + this.m9.w(status()) + 7.0f;
        this.raj13.draw(canvas, homeData.time, w, 15.0f + (this.raj13.cap / f21), theme.INK);
        this.m9.draw(canvas, homeData.date, w + this.raj13.w(homeData.time) + 7.0f, 15.0f + (this.m9.cap / f21), theme.DIM);
    }
}
