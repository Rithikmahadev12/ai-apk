package com.jarvis.assist.aosui.home;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.media.AudioManager;
import android.os.SystemClock;
import android.view.View;
import com.jarvis.assist.R;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.Corners;
import com.jarvis.assist.aosui.core.Flip7;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.HomeBanner;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.MemPolicy;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Profile;
import com.jarvis.assist.aosui.core.Shapes;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class BackLayer extends View {
    static final String[][] ADV_DEF = {new String[]{"email", "EMAIL", "mail"}, new String[]{"calendar", "CAL", "cal"}, new String[]{"sms", "MSG", "msg"}, new String[]{"notes", "NOTES", "list"}, new String[]{"health", "HEALTH", "heart"}, new String[]{"maps", "MAP", "pin"}, new String[]{"music", "MUSIC", "music"}, new String[]{"camera", "CAMERA", "cam"}};
    static Bitmap LOGO;
    Txt aBig;
    Txt aChip;
    Txt aDial;
    Txt aNum;
    Txt aStat;
    Txt aSub;
    Txt aTile;
    Txt advSm;
    Txt advTime;
    LinearGradient appsG;
    Path day;
    LinearGradient dayG;
    final Txt[] dkLT;
    Txt dkLab;
    final Txt[] dkST;
    Txt dkSub;
    Txt gBig;
    final HomeCtx h;
    Bitmap logo;
    SweepGradient opal;
    float opalX;
    float opalY;
    final Paint p;
    float pulseA;
    FrameClock.Handle pulseH;
    final float[] q;
    final Txt[] q2T;
    Txt qsLab;
    final Txt[] qsT;
    final RectF rf;
    Txt t65;
    Txt t7;
    Txt t75;
    Txt t9;
    Txt t9b;
    final Txt[] whLT;
    Txt whLab;
    final Txt[] whST;
    Txt whSub;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized Bitmap logo(Context context) {
        Bitmap bitmap;
        synchronized (BackLayer.class) {
            if (LOGO == null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = MemPolicy.sample(256, 202, Math.round(context.getResources().getDisplayMetrics().density * 36.0f));
                LOGO = BitmapFactory.decodeResource(context.getResources(), R.drawable.aos_logo, options);
            }
            bitmap = LOGO;
        }
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackLayer(Context context, final HomeCtx homeCtx) {
        super(context);
        this.p = new Paint(1);
        this.rf = new RectF();
        this.q = new float[2];
        this.pulseA = 1.0f;
        this.qsT = new Txt[8];
        this.whST = new Txt[8];
        this.whLT = new Txt[8];
        this.dkST = new Txt[5];
        this.dkLT = new Txt[5];
        this.q2T = new Txt[7];
        this.opalX = -1.0f;
        this.h = homeCtx;
        this.logo = logo(context);
        this.pulseH = FrameClock.I.add(new FrameClock.Sub() { // from class: com.jarvis.assist.aosui.home.BackLayer$$ExternalSyntheticLambda0
            @Override // com.jarvis.assist.aosui.core.FrameClock.Sub
            public final boolean frame(long j, float f) {
                return BackLayer.this.m268lambda$new$0$comaugmaosohomeBackLayer(homeCtx, j, f);
            }
        }, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-home-BackLayer, reason: not valid java name */
    public /* synthetic */ boolean m268lambda$new$0$comaugmaosohomeBackLayer(HomeCtx homeCtx, long j, float f) {
        if ("crit".equals(homeCtx.d.btState) && Perf.k().motion) {
            this.pulseA = (float) ((Math.cos((j / 1200.0d) * 2.0d * 3.141592653589793d) * 0.375d) + 0.625d);
            invalidate();
            return true;
        }
        if (this.pulseA == 1.0f) {
            return false;
        }
        this.pulseA = 1.0f;
        invalidate();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepare() {
        Geo geo = this.h.g;
        for (int i = 0; i < geo.nq; i++) {
            this.qsT[i] = fit(this.h.d.qs.get(i).label, this.h.L(6.0f, 7.0f), 171.0f, geo.qsA[i]);
        }
        for (int i2 = 0; i2 < geo.nw; i2++) {
            this.whST[i2] = fit(this.h.d.wheel.get(i2).sub, this.h.L(6.5f, 8.0f), 150.0f, geo.whA[i2]);
            this.whLT[i2] = fit(this.h.d.wheel.get(i2).label, this.h.L(11.0f, 10.0f), 137.5f, geo.whA[i2]);
        }
        for (int i3 = 0; i3 < geo.nq2 && i3 < this.h.d.qs2.size(); i3++) {
            this.q2T[i3] = fit(this.h.d.qs2.get(i3).label, this.h.L(5.5f, 7.0f), geo.q2Out - ((geo.q2Out - geo.q2In) * 0.55f), geo.q2A[i3]);
        }
        float f = geo.adv() ? 0.62f : 1.0f;
        for (int i4 = 0; i4 < geo.nd; i4++) {
            this.dkST[i4] = fit(this.h.d.dock.get(i4).sub, this.h.L(6.5f, 8.0f) * f, geo.RO - 8.0f, geo.dkA[i4]);
            this.dkLT[i4] = fit(this.h.d.dock.get(i4).label, this.h.L(12.5f, 10.0f) * f, geo.RO - 20.0f, geo.dkA[i4]);
        }
        this.t9 = this.h.monoL(9.0f, 2.2f, 9.0f);
        this.t75 = this.h.monoL(7.5f, 1.6f, 8.0f);
        this.t65 = this.h.monoL(6.5f, 1.6f, 8.0f);
        this.t7 = this.h.monoL(7.0f, 2.4f, 8.0f);
        float f2 = 9.0f * f;
        this.t9b = this.h.monoL(f2, f * 2.4f, f2);
        Theme theme = this.h.th;
        float f3 = geo.cx;
        float f4 = geo.cy;
        this.dayG = new LinearGradient(f3, f4 + 74.0f, f3, f4 - 74.0f, new int[]{theme.BLUE, Theme.a(theme.BLUE, 0.5f), theme.RED}, new float[]{0.0f, 0.55f, 1.0f}, Shader.TileMode.CLAMP);
        float f5 = geo.RA - 7.0f;
        this.appsG = new LinearGradient(geo.bx - f5, 0.0f, geo.bx + f5, 0.0f, new int[]{theme.BLUE, Theme.a(theme.BLUE, 0.5f), theme.RED}, new float[]{0.0f, 0.55f, 1.0f}, Shader.TileMode.CLAMP);
        invalidate();
    }

    Txt fit(String str, float f, float f2, float[] fArr) {
        Txt align = new Txt(Fonts.RAJ_600, f, 1.5f).align(Paint.Align.CENTER);
        float chord = Geo.chord(f2, fArr[0], fArr[1]);
        float w = align.w(str);
        if (w > chord && w > 0.0f) {
            float max = Math.max(0.72f, chord / w);
            align = new Txt(Fonts.RAJ_600, f * max, max * 1.5f).align(Paint.Align.CENTER);
            float w2 = align.w(str);
            if (w2 > chord) {
                align.p.setTextScaleX(chord / w2);
            }
        }
        return align;
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
        Shapes.pt(f, f2, f3, f4, this.q);
        float f5 = f4 + 90.0f;
        if (f4 > 0.0f && f4 < 180.0f) {
            f5 = f4 - 90.0f;
        }
        int save = canvas.save();
        canvas.rotate(f5, this.q[0], this.q[1]);
        txt.mid(canvas, str, this.q[0], this.q[1], i);
        canvas.restoreToCount(save);
    }

    void dot(Canvas canvas, Path path, int i, int i2) {
        if (i != 0 || i2 > 0) {
            path.computeBounds(this.rf, true);
            float width = this.rf.left + (this.rf.width() * 0.78f);
            float height = this.rf.top + (this.rf.height() * 0.22f);
            if (i2 > 0) {
                badge(canvas, width, height, i2);
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

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        HomeData homeData;
        Canvas canvas2;
        BackLayer backLayer;
        HomeData homeData2;
        Theme theme;
        float f;
        int i;
        int i2;
        float f2;
        float f3;
        int i3;
        float f4;
        Canvas canvas3 = canvas;
        Geo geo = this.h.g;
        if (this.t9 == null) {
            return;
        }
        Theme theme2 = this.h.th;
        HomeData homeData3 = this.h.d;
        int save = canvas3.save();
        canvas3.translate(geo.ox, geo.oy);
        canvas3.scale(geo.S, geo.S);
        float f5 = geo.cx;
        float f6 = geo.cy;
        float f7 = geo.LW;
        float f8 = geo.LH;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        float f9 = 1.0f;
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(Theme.a(theme2.LINE2, ((theme2.LINE2 >>> 24) / 255.0f) * 0.55f));
        float f10 = 81.0f;
        while (true) {
            int i4 = 1;
            if (f10 >= f8) {
                break;
            }
            float f11 = 27.0f;
            while (f11 < f7) {
                float f12 = f11 + (((int) (f10 / 135.0f)) % 2 == i4 ? 54 : 0);
                if (f12 > f7 - 10.0f) {
                    f4 = f10;
                    i3 = i4;
                } else {
                    i3 = i4;
                    canvas3.drawLine(f12 - 5.0f, f10, f12 + 5.0f, f10, this.p);
                    f4 = f10;
                    canvas3 = canvas;
                    canvas3.drawLine(f12, f4 - 5.0f, f12, f4 + 5.0f, this.p);
                }
                f11 += 108.0f;
                i4 = i3;
                f10 = f4;
            }
            f10 += 135.0f;
        }
        if (geo.adv()) {
            drawAdv(canvas3, theme2, homeData3, geo);
            canvas3.restoreToCount(save);
            return;
        }
        ring(canvas3, f5, f6, 214.0f, ma(theme2.LINE, 0.5f), 1.0f);
        ring(canvas, f5, f6, 246.0f, ma(theme2.LINE, 0.3f), 1.0f);
        BackLayer backLayer2 = this;
        backLayer2.ring(canvas, geo.bx, geo.by, 200.0f, ma(theme2.LINE, 0.35f), 1.0f);
        int i5 = 0;
        while (i5 < geo.nq) {
            boolean z = i5 < homeData3.qsOn.length && homeData3.qsOn[i5];
            float f13 = geo.qsA[i5][2];
            if (backLayer2.h.adv) {
                Theme theme3 = theme2;
                HomeData homeData4 = homeData3;
                float f14 = f5;
                backLayer2.advQs(canvas, theme3, homeData4, i5, z, f13, f14, f6);
                theme = theme3;
                homeData2 = homeData4;
                f2 = f14;
                f3 = f6;
                f = f9;
                i2 = i5;
            } else {
                float f15 = f5;
                homeData2 = homeData3;
                boolean z2 = z;
                theme = theme2;
                BackLayer backLayer3 = backLayer2;
                int i6 = i5;
                backLayer3.seg(canvas, geo.qs[i6], z2);
                Shapes.pt(f15, f6, 183.5f, f13, backLayer3.q);
                Paint paint = backLayer3.p;
                String str = homeData2.qs.get(i6).icon;
                float f16 = backLayer3.q[0];
                float f17 = backLayer3.q[1];
                if (z2) {
                    f = f9;
                    i = theme.BLUE;
                } else {
                    f = f9;
                    i = theme.INK;
                }
                int i7 = i;
                i2 = i6;
                Icons.draw(canvas, paint, str, f16, f17, 10.0f, i7);
                String str2 = homeData2.qs.get(i2).label;
                Txt txt = backLayer3.qsT[i2];
                int i8 = z2 ? theme.BLUE : theme.DIM;
                f2 = f15;
                f3 = f6;
                backLayer3.arcText(canvas, f2, f3, 171.0f, f13, str2, txt, i8);
            }
            i5 = i2 + 1;
            backLayer2 = this;
            f6 = f3;
            homeData3 = homeData2;
            theme2 = theme;
            f9 = f;
            f5 = f2;
        }
        Theme theme4 = theme2;
        float f18 = f5;
        float f19 = f6;
        float f20 = f9;
        HomeData homeData5 = homeData3;
        arc(canvas, f18, f19, 198.0f, 220.0f, 320.0f, theme4.LINE2, 1.0f);
        arc(canvas, f18, f19, 203.0f, 236.0f, 304.0f, theme4.LINE, 1.0f);
        arc(canvas, f18, f19, 198.0f, 220.0f, 226.0f, theme4.RED, 2.0f);
        arc(canvas, f18, f19, 198.0f, 314.0f, 320.0f, theme4.BLUE, 2.0f);
        arc(canvas, f18, f19, 98.0f, 200.0f, 340.0f, theme4.LINE, 1.0f);
        arc(canvas, f18, f19, 94.0f, 222.0f, 318.0f, theme4.LINE2, 1.0f);
        arc(canvas, f18, f19, 94.0f, 262.0f, 278.0f, theme4.BLUE, 2.0f);
        int i9 = 0;
        while (i9 < geo.nw) {
            wedge(canvas, i9, !this.h.brief && i9 == this.h.active);
            i9++;
        }
        HubRing.draw(canvas, this.p, this.rf, this.q, this.h, f18, f19, this.dayG, true);
        this.t9.drawFit(canvas, this.h.arcs != null ? this.h.ringLabel : "DAY · " + Math.round(homeData5.dayF * 100.0f) + "%", f18, f19 + 100.0f, theme4.DIM, 150.0f);
        if (this.h.adv) {
            float f21 = homeData5.stTotal > 0 ? f20 - (((float) homeData5.stFree) / ((float) homeData5.stTotal)) : 0.0f;
            int length = homeData5.notifs != null ? homeData5.notifs.length() : 0;
            homeData = homeData5;
            dial(canvas, 36.0f, homeData5.stTotal > 0 ? Math.round(100.0f * f21) + "%" : "—", homeData5.stTotal > 0 ? StorageWidget.gb(homeData5.stTotal) : "", Strings.t("adv.storage"), f21, "folder", theme4.INK, theme4.DIM, 1.0f);
            backLayer = this;
            canvas2 = canvas;
            backLayer.dial(canvas2, f7 - 36.0f, String.valueOf(length), Strings.t("adv.unread.sub"), Strings.t("adv.unread"), Math.min(f20, length / 20.0f), "bell", theme4.INK, theme4.DIM, 1.0f);
        } else {
            homeData = homeData5;
            float f22 = f20;
            int sc = theme4.sc(homeData.btState);
            String str3 = homeData.btPct + "%";
            String str4 = homeData.btLabel;
            float f23 = homeData.btPct / 100.0f;
            int i10 = "norm".equals(homeData.btState) ? theme4.DIM : sc;
            if ("crit".equals(homeData.btState)) {
                f22 = this.pulseA;
            }
            float f24 = f22;
            canvas2 = canvas;
            dial(canvas2, 36.0f, str3, str4, "BATTERY", f23, "batt", sc, i10, f24);
            backLayer = this;
            backLayer.dial(canvas2, f7 - 36.0f, homeData.wxTemp, homeData.wxDesc, "WEATHER", 0.55f, homeData.wxIcon, theme4.sc(homeData.wxState), theme4.DIM, 1.0f);
        }
        backLayer.drawDock(canvas2, theme4, homeData, geo);
        backLayer.topBar(canvas2, theme4, homeData, f7);
        canvas2.restoreToCount(save);
    }

    void drawDock(Canvas canvas, Theme theme, HomeData homeData, Geo geo) {
        BackLayer backLayer = this;
        Canvas canvas2 = canvas;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= geo.nd) {
                break;
            }
            float f = geo.dkA[i][2];
            HomeData.Entry entry = homeData.dock.get(i);
            backLayer.seg(canvas2, geo.dk[i], z);
            float f2 = geo.RO - geo.RI;
            boolean adv = geo.adv();
            int i2 = z;
            backLayer.arcText(canvas2, geo.bx, geo.by, geo.RO - (adv ? 0.14f * f2 : 8.0f), f, entry.sub, backLayer.dkST[i], theme.DIM);
            backLayer.arcText(canvas, geo.bx, geo.by, geo.RO - (adv ? 0.42f * f2 : 20.0f), f, entry.label, backLayer.dkLT[i], theme.INK);
            BackLayer backLayer2 = backLayer;
            float f3 = geo.bx;
            float f4 = geo.by;
            float f5 = geo.RI;
            Shapes.pt(f3, f4, adv ? f5 + (0.22f * f2) : f5 + 10.0f, f, backLayer2.q);
            Icons.draw(canvas, backLayer2.p, entry.icon, backLayer2.q[i2], backLayer2.q[1], adv ? Math.min(11.0f, 0.26f * f2) : 13.0f, theme.INK);
            canvas2 = canvas;
            if (!adv || i >= homeData.dockCnt.length || homeData.dockCnt[i] <= 0) {
                backLayer2.dot(canvas2, geo.dk[i], homeData.dockDot[i], i < homeData.dockCnt.length ? homeData.dockCnt[i] : i2);
            } else {
                Shapes.pt(geo.bx, geo.by, geo.RO - 5.0f, geo.dkA[i][1] - 4.0f, backLayer2.q);
                backLayer2.badge(canvas2, backLayer2.q[i2], backLayer2.q[1], homeData.dockCnt[i]);
            }
            i++;
            backLayer = backLayer2;
            z = i2;
        }
        BackLayer backLayer3 = backLayer;
        backLayer3.t7.drawFit(canvas2, backLayer3.h.dockHead, geo.bx, (geo.by - geo.RO) - 30.0f, theme.DIM, (geo.RO * 2.0f) - 40.0f);
        backLayer3.p.setStyle(Paint.Style.FILL);
        backLayer3.p.setColor(theme.ACT);
        canvas2.drawPath(geo.apps, backLayer3.p);
        backLayer3.p.setStyle(Paint.Style.STROKE);
        backLayer3.p.setStrokeWidth(1.0f);
        backLayer3.p.setColor(theme.BLUE);
        canvas2.drawPath(geo.apps, backLayer3.p);
        float f6 = geo.RA - 7.0f;
        backLayer3.p.setShader(backLayer3.appsG);
        backLayer3.p.setStrokeWidth(1.6f);
        backLayer3.p.setStrokeCap(Paint.Cap.ROUND);
        backLayer3.p.setColor(-16777216);
        backLayer3.rf.set(geo.bx - f6, geo.by - f6, geo.bx + f6, geo.by + f6);
        canvas.drawArc(backLayer3.rf, 186.0f, 168.0f, false, backLayer3.p);
        backLayer3.p.setShader(null);
        arc(canvas, geo.bx, geo.by, geo.RA - 13.0f, 210.0f, 330.0f, theme.LINE, 1.0f);
        if (geo.adv()) {
            Icons.draw(canvas, this.p, this.h.appsIcon, geo.bx, geo.by - (geo.RA * 0.6f), 13.0f, theme.INK);
            this.t9b.drawFit(canvas, this.h.appsLabel, geo.bx, geo.by - (geo.RA * 0.32f), theme.INK, 1.4f * geo.RA);
            return;
        }
        Icons.draw(canvas, this.p, this.h.appsIcon, geo.bx, geo.by - (geo.RA * 0.58f), 20.0f, theme.INK);
        this.t9b.drawFit(canvas, this.h.appsLabel, geo.bx, geo.by - (geo.RA * 0.3f), theme.INK, geo.RA * 1.6f);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(theme.LINE2);
        this.rf.set(geo.bx - 50.0f, geo.by - 10.0f, geo.bx + 50.0f, geo.by - 6.0f);
        canvas.drawRoundRect(this.rf, 2.0f, 2.0f, this.p);
    }

    void topBar(Canvas canvas, Theme theme, HomeData homeData, float f) {
        Canvas canvas2;
        float max = (Math.max(22.09375f, this.h.zoom * 11.0f) / 2.0f) + 14.0f;
        if (this.logo != null) {
            this.rf.set(20.0f, max - 11.046875f, 48.0f, 11.046875f + max);
            canvas2 = canvas;
            canvas.drawBitmap(this.logo, (Rect) null, this.rf, (Paint) null);
        } else {
            canvas2 = canvas;
        }
        Txt txt = this.h.brand;
        float f2 = max + (txt.cap / 2.0f);
        txt.tracked(canvas, "OS", txt.tracked(canvas2, "a", 55.0f, f2, theme.INK), f2, theme.RED);
        this.h.sbMono.draw(canvas, WidgetView.fitText(this.h.sbMono, homeData.time + " · " + homeData.date + " · " + homeData.btPct + "%", (this.h.g.cutRx < 0.0f || this.h.g.cutRx >= f - 40.0f) ? f : ((f - 20.0f) - this.h.g.cutRx) - 8.0f), (f - 20.0f) - (this.h.sbMono.ls / 2.0f), max + (this.h.sbMono.cap / 2.0f), theme.DIM);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wedge(Canvas canvas, int i, boolean z) {
        int i2;
        HomeData.Entry entry;
        Geo geo = this.h.g;
        Theme theme = this.h.th;
        HomeData homeData = this.h.d;
        float f = geo.cx;
        float f2 = geo.cy;
        float f3 = geo.whA[i][0];
        float f4 = geo.whA[i][1];
        float f5 = geo.whA[i][2];
        HomeData.Entry entry2 = homeData.wheel.get(i);
        seg(canvas, geo.wh[i], z);
        if (z) {
            i2 = 0;
            entry = entry2;
            arc(canvas, f, f2, 107.5f, f3 + 1.5f, f4 - 1.5f, theme.BLUE, 2.2f);
        } else {
            i2 = 0;
            entry = entry2;
        }
        arcText(canvas, f, f2, 150.0f, f5, entry.sub, this.whST[i], z ? theme.BLUE : theme.DIM);
        arcText(canvas, f, f2, 137.5f, f5, entry.label, this.whLT[i], z ? theme.BLUE : theme.INK);
        Shapes.pt(f, f2, 119.0f, f5, this.q);
        Icons.draw(canvas, this.p, entry.icon, this.q[i2], this.q[1], 13.0f, z ? theme.BLUE : theme.INK);
        dot(canvas, geo.wh[i], homeData.wheelDot[i], i < homeData.wheelCnt.length ? homeData.wheelCnt[i] : i2);
    }

    static List<String[]> advExtra(HomeData homeData) {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        Iterator<HomeData.Entry> it = homeData.wheel.iterator();
        while (it.hasNext()) {
            hashSet.add(it.next().id);
        }
        int min = 8 - Math.min(8, homeData.wheel.size());
        for (String[] strArr : ADV_DEF) {
            if (arrayList.size() >= min) {
                break;
            }
            if (!hashSet.contains(strArr[0])) {
                arrayList.add(strArr);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] advExtraIds(HomeData homeData) {
        List<String[]> advExtra = advExtra(homeData);
        int size = advExtra.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = advExtra.get(i)[0];
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] advExtraLabels(HomeData homeData) {
        List<String[]> advExtra = advExtra(homeData);
        int size = advExtra.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = advExtra.get(i)[1];
        }
        return strArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v69 */
    /* JADX WARN: Type inference failed for: r15v34 */
    /* JADX WARN: Type inference failed for: r15v39 */
    /* JADX WARN: Type inference failed for: r15v4 */
    void drawAdv(Canvas canvas, Theme theme, HomeData homeData, Geo geo) {
        float f;
        float f2;
        Txt txt;
        Txt txt2;
        int i;
        float f3;
        Txt txt3;
        String str;
        String str2;
        float f4;
        String str3;
        Theme theme2 = theme;
        HomeData homeData2 = homeData;
        float f5 = geo.LW;
        float f6 = geo.cx;
        float f7 = geo.cy;
        if (this.aTile == null) {
            this.aTile = this.h.monoL(7.0f, 1.6f, 7.5f);
            this.aNum = new Txt(Fonts.MONO, this.h.L(7.0f, 7.5f), 0.0f);
            this.aDial = new Txt(Fonts.RAJ_400, this.h.L(11.0f, 10.0f), this.h.L(11.0f, 10.0f) * 0.08f);
            this.aSub = new Txt(Fonts.MONO, this.h.L(6.5f, 7.5f), this.h.L(6.5f, 7.5f) * 0.14f);
            this.aChip = new Txt(Fonts.RAJ_600, this.h.L(11.0f, 9.0f), this.h.L(11.0f, 9.0f) * 0.16f).align(Paint.Align.CENTER);
            this.aBig = new Txt(Fonts.RAJ_400, this.h.L(13.0f, 11.0f), 0.0f);
        }
        if (this.aStat == null) {
            f = 10.0f;
            float L = this.h.L(10.0f, 9.0f);
            this.aStat = new Txt(Fonts.MONO, L, 0.18f * L);
        } else {
            f = 10.0f;
        }
        boolean equals = "time".equals(homeData2.corner[0]);
        boolean equals2 = "battery".equals(homeData2.corner[1]);
        float f8 = geo.uu * 18.0f;
        float f9 = f5 - (geo.uu * 18.0f);
        boolean z = geo.cutLx >= 0.0f && geo.statusY < 30.0f;
        this.aStat.align(Paint.Align.LEFT);
        if (equals) {
            f2 = 2.0f;
        } else {
            Txt txt4 = this.aStat;
            if (z) {
                f4 = 2.0f;
                str3 = WidgetView.fitText(this.aStat, homeData2.time, (geo.cutLx - 8.0f) - f8);
            } else {
                f4 = 2.0f;
                str3 = homeData2.time;
            }
            f2 = f4;
            txt4.draw(canvas, str3, f8, geo.statusY + (this.aStat.cap / f4), theme2.INK);
        }
        this.aStat.align(Paint.Align.RIGHT);
        boolean z2 = false;
        for (int i2 = 0; i2 < homeData2.qs.size() && i2 < homeData2.qsOn.length; i2++) {
            if ("wifi".equals(homeData2.qs.get(i2).id)) {
                z2 = homeData2.qsOn[i2];
            }
        }
        String str4 = (z2 ? "WIFI" : "5G") + (equals2 ? "" : " · " + homeData2.btPct + "%");
        Txt txt5 = this.aStat;
        if (z) {
            str4 = WidgetView.fitText(this.aStat, str4, (f9 - geo.cutRx) - 8.0f);
        }
        txt5.draw(canvas, str4, f9, geo.statusY + (this.aStat.cap / f2), theme2.INK);
        this.aStat.align(Paint.Align.LEFT);
        Txt txt6 = this.h.sbMono;
        greetPanel(canvas, theme, homeData, geo);
        float f10 = geo.acy;
        float f11 = geo.arcHalf;
        float f12 = (geo.F * 8.0f) + geo.qsOut;
        float f13 = geo.qsOut + (geo.F * 14.0f);
        float f14 = 270.0f - f11;
        float f15 = f11 + 270.0f;
        boolean z3 = false;
        float f16 = f6;
        arc(canvas, f16, f10, f12, f14, f15, Flip7.G4, 1.0f);
        float f17 = f2;
        arc(canvas, f16, f10, f13, f14 + 4.0f, 262.0f, Theme.a(Flip7.G5, 0.7f), 1.0f);
        BackLayer backLayer = this;
        backLayer.arc(canvas, f16, f10, f13, 278.0f, f15 - 4.0f, Theme.a(Flip7.G5, 0.7f), 1.0f);
        backLayer.arcText(canvas, f16, f10, geo.qsOut + (geo.F * 20.0f), 270.0f, Strings.t("adv.control"), backLayer.t65, -1774607);
        float f18 = f13 + 4.0f;
        float f19 = f11 * 0.62f;
        backLayer.arcText(canvas, f16, f10, f18, 270.0f - f19, Strings.t("adv.system"), backLayer.t65, -8549994);
        backLayer.arcText(canvas, f16, f10, f18, f19 + 270.0f, Strings.t("adv.signal"), backLayer.t65, -8549994);
        int i3 = 0;
        while (i3 < geo.nq) {
            float f20 = f7;
            float f21 = f10;
            float f22 = f16;
            int i4 = i3;
            backLayer.flipWedge(canvas, theme, geo.qs[i3], homeData2.qs.get(i3), backLayer.qsT[i3], (i3 >= homeData2.qsOn.length || !homeData2.qsOn[i3]) ? z3 : true, geo.qsA[i3][2], geo.qsIn, geo.qsOut, f22, f21, ("q" + i3).equals(backLayer.h.pressed));
            theme2 = theme;
            if (homeData2.qsCnt[i4] > 0) {
                Shapes.pt(f22, f21, geo.qsOut - 5.0f, geo.qsA[i4][1] - 3.0f, backLayer.q);
                backLayer.badge(canvas, backLayer.q[0], backLayer.q[1], homeData2.qsCnt[i4]);
            }
            i3 = i4 + 1;
            f16 = f22;
            f10 = f21;
            z3 = false;
            f7 = f20;
            f = 10.0f;
        }
        boolean z4 = z3;
        float f23 = f7;
        float f24 = f;
        boolean z5 = true;
        float f25 = f16;
        float f26 = f10;
        int i5 = -1;
        while (i5 <= 1) {
            float f27 = i5 < 0 ? geo.uu * 6.0f * 1.6f : f5 - ((geo.uu * 6.0f) * 1.6f);
            float f28 = 16.0f + f26;
            backLayer.ring(canvas, f27, f28, 5.0f, theme2.RED, 1.0f);
            BackLayer backLayer2 = backLayer;
            backLayer2.p.setStyle(Paint.Style.FILL);
            backLayer2.p.setColor(theme2.RED);
            canvas.drawCircle(f27, f28, 1.7f, backLayer2.p);
            i5 += 2;
            backLayer = backLayer2;
        }
        BackLayer backLayer3 = backLayer;
        int length = homeData2.notifs == null ? z4 ? 1 : 0 : homeData2.notifs.length();
        if (length > 0 && geo.nq > 0) {
            Shapes.pt(f25, f26, geo.qsOut + 3.0f, geo.qsA[geo.nq - 1][1] + f17, backLayer3.q);
            backLayer3.p.setStyle(Paint.Style.FILL);
            backLayer3.p.setColor(theme2.RED);
            canvas.drawCircle(backLayer3.q[z4 ? 1 : 0], backLayer3.q[1], 4.2f, backLayer3.p);
            backLayer3.aNum.align(Paint.Align.CENTER);
            backLayer3.aNum.draw(canvas, length > 9 ? "9+" : String.valueOf(length), backLayer3.q[z4 ? 1 : 0], backLayer3.q[1] + (backLayer3.aNum.cap / f17), -1);
            backLayer3.aNum.align(Paint.Align.LEFT);
        }
        float f29 = geo.q2Out + 5.0f;
        float f30 = f25;
        float f31 = f26;
        int i6 = length;
        BackLayer backLayer4 = this;
        backLayer4.arc(canvas, f30, f31, f29, 236.0f, 262.0f, Flip7.G4, 1.0f);
        backLayer4.arc(canvas, f30, f31, f29, 278.0f, 304.0f, Flip7.G4, 1.0f);
        backLayer4.arcText(canvas, f30, f31, f29, 270.0f, Strings.t("adv.quick"), backLayer4.t65, -8549994);
        int i7 = z4 ? 1 : 0;
        if (r15 == null) r15 = z4;
        while (i7 < geo.nq2 && i7 < homeData2.qs2.size()) {
            float f32 = f31;
            char c = r15;
            int i8 = i7;
            int i9 = i6;
            float f33 = f30;
            backLayer4.flipWedge(canvas, theme, geo.qs2[i7], homeData2.qs2.get(i7), backLayer4.q2T[i7], (i7 >= homeData2.qs2On.length || !homeData2.qs2On[i7]) ? r15 : z5, geo.q2A[i7][2], geo.q2In, geo.q2Out, f33, f32, ("k" + i7).equals(backLayer4.h.pressed));
            BackLayer backLayer5 = backLayer4;
            theme2 = theme;
            f31 = f32;
            if (homeData2.qs2Cnt[i8] > 0) {
                Shapes.pt(f33, f31, geo.q2Out - 5.0f, geo.q2A[i8][1] - 3.0f, backLayer5.q);
                backLayer5.badge(canvas, backLayer5.q[c], backLayer5.q[1], homeData2.qs2Cnt[i8]);
            }
            i7 = i8 + 1;
            backLayer4 = backLayer5;
            f30 = f33;
            z5 = true;
            i6 = i9;
            r15 = c;
        }
        BackLayer backLayer6 = backLayer4;
        int i10 = i6;
        int i11 = r15;
        float f34 = f30;
        boolean z6 = z5;
        float f35 = geo.outerR;
        float max = Math.max(2.5f, geo.uu * 5.0f * 1.4f);
        Shader shader = null;
        backLayer6.p.setShader(null);
        backLayer6.p.setStyle(Paint.Style.STROKE);
        backLayer6.p.setStrokeCap(Paint.Cap.ROUND);
        backLayer6.p.setStrokeWidth(max);
        backLayer6.rf.set(f34 - f35, f23 - f35, f34 + f35, f23 + f35);
        backLayer6.p.setColor(Flip7.G6);
        canvas.drawArc(backLayer6.rf, 140.0f, 260.0f, false, backLayer6.p);
        backLayer6.p.setColor(-1774607);
        canvas.drawArc(backLayer6.rf, 140.0f, Flip7.readySweep(homeData2.dayF), false, backLayer6.p);
        backLayer6.p.setStrokeCap(Paint.Cap.BUTT);
        backLayer6.arcText(canvas, f34, f23, f35 + max + 5.0f, 90.0f, "DAY · " + Math.round(homeData2.dayF * 100.0f) + "%", backLayer6.t65, -6641998);
        float f36 = geo.dialY;
        backLayer6.p.setStyle(Paint.Style.FILL);
        float f37 = f36 - 7.0f;
        float f38 = f36 + 7.0f;
        backLayer6.p.setShader(new LinearGradient(0.0f, f37, 0.0f, f38, new int[]{i11, Theme.a(theme2.INK, 0.07f), i11}, (float[]) null, Shader.TileMode.CLAMP));
        canvas.drawRect(0.0f, f37, f5, f38, backLayer6.p);
        boolean z7 = z6;
        backLayer6.p.setShader(new LinearGradient(0.0f, 0.0f, f5, 0.0f, new int[]{Theme.a(theme2.INK, 0.05f), Theme.a(theme2.INK, 0.22f), Theme.a(theme2.INK, 0.22f), Theme.a(theme2.INK, 0.05f)}, new float[]{0.0f, 0.3f, 0.7f, 1.0f}, Shader.TileMode.CLAMP));
        canvas.drawRect(0.0f, f36 - 0.5f, f5, f36 + 0.5f, backLayer6.p);
        backLayer6.p.setShader(null);
        float f39 = 1.0f;
        float f40 = homeData2.stTotal > 0 ? 1.0f - (((float) homeData2.stFree) / ((float) homeData2.stTotal)) : 0.0f;
        float f41 = geo.dialR;
        float f42 = geo.dialXL;
        float f43 = geo.dialXR;
        Theme theme3 = theme2;
        dialNum(canvas, theme3, f42, f36, f41, f40, homeData2.stTotal > 0 ? String.valueOf(Math.round(100.0f * f40)) : "—", theme2.RED);
        float f44 = f42 + f41 + 12.0f;
        float f45 = f36 - 3.0f;
        this.aDial.draw(canvas, Strings.t("adv.storage"), f44, f45, theme3.INK);
        float f46 = f36 + 4.0f;
        this.aSub.draw(canvas, homeData2.stTotal > 0 ? StorageWidget.gb(homeData2.stTotal) : "—", f44, f46 + this.aSub.cap, theme3.DIM);
        dialNum(canvas, theme3, f43, f36, f41, Math.min(1.0f, i10 / 20.0f), String.valueOf(i10), theme3.BLUE);
        this.aDial.align(Paint.Align.RIGHT);
        this.aSub.align(Paint.Align.RIGHT);
        float f47 = (f43 - f41) - 12.0f;
        this.aDial.draw(canvas, Strings.t("adv.unread"), f47, f45, theme3.INK);
        Canvas canvas2 = canvas;
        this.aSub.draw(canvas2, unreadApps(homeData2), f47, f46 + this.aSub.cap, theme3.DIM);
        this.aDial.align(Paint.Align.LEFT);
        this.aSub.align(Paint.Align.LEFT);
        String mode = Advanced.mode(phoneFilter(), phoneRinger());
        int i12 = 0;
        while (i12 < 5) {
            RectF rectF = geo.chip[i12];
            boolean equals3 = AdvWidgets.MODES[i12].equals(mode);
            this.p.setShader(null);
            if (equals3) {
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(Theme.a(theme3.RED, 0.3f));
                canvas2.drawRect(rectF, this.p);
            }
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(f39);
            this.p.setColor(equals3 ? theme3.RED : theme3.LINE2);
            canvas2.drawRect(rectF, this.p);
            this.aChip.drawFit(canvas2, Strings.t("adv.mode." + AdvWidgets.MODES[i12]), rectF.centerX(), rectF.centerY() + (this.aChip.cap / f17), equals3 ? -1 : theme3.INK, rectF.width() - 4.0f);
            i12++;
            f39 = 1.0f;
        }
        List<String[]> advExtra = advExtra(homeData2);
        int i13 = 0;
        while (i13 < 8) {
            RectF rectF2 = geo.tile[i13];
            if (i13 < geo.nw) {
                HomeData.Entry entry = homeData2.wheel.get(i13);
                str = entry.label;
                str2 = entry.icon;
            } else if (i13 - geo.nw < advExtra.size()) {
                str = advExtra.get(i13 - geo.nw)[z7 ? 1 : 0];
                str2 = advExtra.get(i13 - geo.nw)[2];
            } else {
                i13++;
                shader = null;
            }
            String str5 = str2;
            this.p.setShader(shader);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(Theme.a(theme3.BG, 0.55f));
            canvas2.drawRect(rectF2, this.p);
            if (this.h.pressed != null && (this.h.pressed.equals("w" + i13) || (i13 >= geo.nw && i13 - geo.nw < advExtra.size() && this.h.pressed.equals("x:tile:" + advExtra.get(i13 - geo.nw)[0])))) {
                this.p.setColor(Theme.a(-1774607, 0.12f));
                canvas2.drawRect(rectF2, this.p);
            }
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.0f);
            this.p.setColor(theme3.LINE2);
            canvas2.drawRect(rectF2, this.p);
            this.aNum.draw(canvas2, String.format(Locale.ROOT, "%02d", Integer.valueOf(i13 + 1)), rectF2.left + 5.0f, rectF2.top + 5.0f + this.aNum.cap, theme3.RED);
            Icons.draw(canvas, this.p, str5, rectF2.centerX(), rectF2.top + (rectF2.height() * 0.42f), Math.min(15.0f, rectF2.height() * 0.3f), -2235414, 1.1f);
            canvas2 = canvas;
            this.aTile.drawFit(canvas2, str.toUpperCase(Locale.ROOT), rectF2.centerX(), (rectF2.height() * 0.78f) + rectF2.top, -4668724, rectF2.width() - 6.0f);
            if (i13 < geo.nw && homeData2.wheelCnt[i13] > 0) {
                badge(canvas2, rectF2.right - 6.0f, rectF2.top + 6.0f, homeData2.wheelCnt[i13]);
            } else if (i13 < geo.nw && homeData2.wheelDot[i13] != 0) {
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(homeData2.wheelDot[i13]);
                canvas2.drawCircle(rectF2.right - 5.0f, rectF2.top + 5.0f, 2.4f, this.p);
            }
            i13++;
            shader = null;
        }
        float f48 = 1.6f;
        float f49 = geo.uu * 6.0f * 1.6f;
        float f50 = geo.acy + 4.0f;
        float f51 = (geo.LH - geo.RO) - 8.0f;
        float f52 = geo.qsOut + (geo.F * 8.0f);
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        int i14 = -1;
        while (i14 <= z7) {
            float f53 = i14 < 0 ? f49 : f5 - f49;
            this.p.setColor(Flip7.G5);
            Canvas canvas3 = canvas2;
            float f54 = f53;
            canvas3.drawLine(f54, f50, f53, f51, this.p);
            float f55 = f51;
            this.p.setColor(Flip7.G4);
            canvas.drawLine(f54, f50, f34 + (i14 * f52), f50, this.p);
            float f56 = f50;
            int i15 = 4;
            float[] fArr = {geo.dialY, geo.pTop, geo.pTop + geo.ph, geo.tileY};
            int i16 = 0;
            while (i16 < i15) {
                float f57 = fArr[i16];
                canvas.drawLine(f54, f57, (i14 * (-8)) + f54, f57, this.p);
                f54 = f54;
                i16++;
                i15 = i15;
            }
            float f58 = f54;
            int i17 = i15;
            canvas2 = canvas;
            float f59 = geo.dialY;
            float f60 = geo.pTop;
            float f61 = geo.pTop + geo.ph;
            float[] fArr2 = new float[5];
            fArr2[0] = f56;
            fArr2[1] = f59;
            fArr2[2] = f60;
            fArr2[3] = f61;
            fArr2[i17] = f55;
            for (int i18 = 0; i18 < 5; i18++) {
                float f62 = fArr2[i18];
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(Flip7.BG);
                canvas2.drawCircle(f58, f62, 2.2f, this.p);
                this.p.setStyle(Paint.Style.STROKE);
                this.p.setColor(f62 == geo.dialY ? i14 < 0 ? -3665874 : -10835492 : Flip7.G3);
                canvas2.drawCircle(f58, f62, 2.2f, this.p);
            }
            i14 += 2;
            f51 = f55;
            f50 = f56;
            z7 = true;
        }
        int i19 = 4;
        if (this.h.pages > 1) {
            float f63 = geo.chipY + geo.chipH + 5.0f;
            float f64 = f34 - (((this.h.pages - 1) * f24) / f17);
            int i20 = 0;
            while (i20 < this.h.pages) {
                float f65 = (i20 * f24) + f64;
                this.p.setShader(null);
                if (i20 == this.h.page) {
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(-10835492);
                    this.rf.set(f65 - 4.0f, f63 - 1.6f, f65 + 4.0f, f63 + 1.6f);
                    canvas2.drawRoundRect(this.rf, 1.6f, 1.6f, this.p);
                } else {
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(1.0f);
                    this.p.setColor(Flip7.G3);
                    canvas2.drawCircle(f65, f63, f17, this.p);
                }
                i20++;
                f17 = 2.0f;
            }
        }
        int i21 = 0;
        while (i21 < i19) {
            RectF rectF3 = geo.corner[i21];
            String[] cornerItem = cornerItem(homeData2, homeData2.corner[i21]);
            boolean z8 = i21 % 2 == 1;
            float f66 = z8 ? rectF3.right : rectF3.left;
            Txt txt7 = this.aSub;
            Txt txt8 = this.aDial;
            txt7.align(z8 ? Paint.Align.RIGHT : Paint.Align.LEFT);
            txt8.align(z8 ? Paint.Align.RIGHT : Paint.Align.LEFT);
            txt7.draw(canvas2, WidgetView.fitText(txt7, cornerItem[0], rectF3.width()), f66, rectF3.top + txt7.cap + 2.0f, -6641998);
            txt8.draw(canvas, WidgetView.fitText(txt8, cornerItem[1], rectF3.width()), f66, rectF3.top + txt7.line() + 4.0f + txt8.cap, -1774607);
            if (cornerItem[2].isEmpty()) {
                txt = txt7;
            } else {
                String fitText = WidgetView.fitText(txt7, cornerItem[2], rectF3.width());
                float line = rectF3.top + txt7.line() + txt8.line() + 7.0f + txt7.cap;
                txt = txt7;
                txt.draw(canvas, fitText, f66, line, -8549994);
            }
            if (cornerItem[3] != null) {
                i = i19;
                txt2 = txt8;
                f3 = f48;
                txt3 = txt;
                Hud.bar(canvas, this.p, this.rf, rectF3.left, rectF3.bottom - 1.0f, rectF3.width(), Float.parseFloat(cornerItem[3]), theme, 1.0f);
            } else {
                txt2 = txt8;
                i = i19;
                f3 = f48;
                txt3 = txt;
            }
            this.p.setShader(null);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.0f);
            this.p.setColor(Flip7.G5);
            float f67 = geo.uu * 6.0f * f3;
            if (z8) {
                f67 = f5 - f67;
            }
            canvas.drawLine(f67, rectF3.top + 3.0f, z8 ? rectF3.left - 4.0f : rectF3.right + 4.0f, rectF3.top + 3.0f, this.p);
            int indexOf = geo.exA.indexOf("x:corner:" + i21);
            if (indexOf >= 0) {
                geo.exL.set(indexOf, cornerItem[0] + ", " + cornerItem[1] + (cornerItem[2].isEmpty() ? "" : ", " + cornerItem[2]));
            }
            txt3.align(Paint.Align.LEFT);
            txt2.align(Paint.Align.LEFT);
            i21++;
            canvas2 = canvas;
            homeData2 = homeData;
            f48 = f3;
            i19 = i;
        }
        drawDock(canvas, theme, homeData, geo);
    }

    void greetPanel(Canvas canvas, Theme theme, HomeData homeData, Geo geo) {
        char c;
        String str;
        float f;
        String str2;
        Canvas canvas2 = canvas;
        RectF rectF = geo.gRect;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(Theme.a(theme.CARD, 0.55f));
        canvas2.drawRect(rectF, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.4f);
        this.p.setColor(-2235414);
        int i = 0;
        while (true) {
            c = 2;
            float f2 = 8.0f;
            if (i >= 4) {
                break;
            }
            int i2 = i % 2;
            float f3 = i2 == 0 ? rectF.left : rectF.right;
            float f4 = i < 2 ? rectF.top : rectF.bottom;
            float f5 = i2 == 0 ? 8.0f : -8.0f;
            if (i >= 2) {
                f2 = -8.0f;
            }
            float f6 = f4;
            Canvas canvas3 = canvas2;
            float f7 = f3;
            canvas3.drawLine(f7, f6, f5 + f3, f4, this.p);
            canvas.drawLine(f7, f6, f7, f6 + f2, this.p);
            i++;
            canvas2 = canvas;
        }
        long currentTimeMillis = System.currentTimeMillis();
        Iterator<HomeData.Ev> it = homeData.events.iterator();
        String str3 = "";
        String str4 = "";
        int i3 = 0;
        while (it.hasNext()) {
            HomeData.Ev next = it.next();
            char c2 = c;
            if (HomeRules.calDay(next.start, next.end, currentTimeMillis) == 0 && next.end > currentTimeMillis) {
                i3++;
                if (str4.isEmpty()) {
                    long j = next.start;
                    long j2 = next.end;
                    boolean z = next.allDay;
                    str2 = str3;
                    str4 = HomeRules.calTime(j, j2, z, currentTimeMillis, "ALL DAY") + " " + next.title;
                    str3 = str2;
                    c = c2;
                }
            }
            str2 = str3;
            str3 = str2;
            c = c2;
        }
        char c3 = c;
        String str5 = str3;
        String format = homeData.alarmAt > 0 ? new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(homeData.alarmAt)) : str5;
        String mode = Advanced.mode(phoneFilter(), phoneRinger());
        String str6 = homeData.greet;
        int length = homeData.notifs == null ? 0 : homeData.notifs.length();
        double optDouble = homeData.goals().optDouble("sleep", 8.0d);
        if (homeData.wxTemp == null || homeData.wxTemp.isEmpty()) {
            str = str5;
        } else {
            str = homeData.wxTemp + " " + (homeData.wxDesc == null ? str5 : homeData.wxDesc);
        }
        String[] greeting = Advanced.greeting(mode, str6, i3, str4, length, format, optDouble, str, homeData.you);
        float f8 = rectF.left + 10.0f;
        float width = rectF.width() - 20.0f;
        float min = Math.min(geo.gH * 0.36f, 20.0f);
        if (this.gBig == null || Math.abs(this.gBig.size - min) > 0.1f) {
            this.gBig = new Txt(Fonts.RAJ_400, min, 0.1f * min);
        }
        Txt txt = this.h.sbMono;
        txt.align(Paint.Align.LEFT);
        float f9 = txt.cap + rectF.top + 6.0f;
        txt.draw(canvas, "INDEX", f8, f9, theme.RED);
        txt.draw(canvas, greeting[0].substring(5), f8 + txt.w("INDEX"), f9, theme.DIM);
        txt.align(Paint.Align.RIGHT);
        txt.draw(canvas, homeData.btPct + "%", rectF.right - 10.0f, f9, theme.INK);
        txt.align(Paint.Align.LEFT);
        this.gBig.draw(canvas, WidgetView.fitText(this.gBig, greeting[1], width), f8, rectF.top + (geo.gH * 0.62f), theme.INK);
        String upperCase = String.format(Locale.getDefault(), "%ta %<tb %<td %<tY", Calendar.getInstance()).toUpperCase(Locale.ROOT);
        if (!greeting[c3].isEmpty()) {
            upperCase = upperCase + "  ·  " + greeting[c3];
        }
        String str7 = upperCase;
        boolean chip = Profile.chip(homeData.you, HomeBanner.parse(Store.get(Profile.CHIP, str5)), System.currentTimeMillis());
        if (chip) {
            String t = Strings.t("you.chip");
            f = this.aSub.w(t) + 12.0f;
            RectF rectF2 = geo.youChip;
            rectF2.set((rectF.right - 10.0f) - f, ((rectF.bottom - 6.0f) - this.aSub.cap) - 5.0f, rectF.right - 10.0f, rectF.bottom - 3.0f);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.0f);
            this.p.setColor(theme.RED);
            canvas.drawRect(rectF2, this.p);
            this.aSub.align(Paint.Align.CENTER);
            this.aSub.draw(canvas, t, rectF2.centerX(), rectF.bottom - 6.0f, theme.RED);
            this.aSub.align(Paint.Align.LEFT);
        } else {
            geo.youChip.setEmpty();
            f = 0.0f;
        }
        this.aSub.draw(canvas, WidgetView.fitText(this.aSub, str7, width - (chip ? f + 8.0f : 0.0f)), f8, rectF.bottom - 6.0f, theme.DIM);
    }

    void badge(Canvas canvas, float f, float f2, int i) {
        if (this.aNum == null) {
            this.aNum = new Txt(Fonts.MONO, this.h.L(7.0f, 7.5f), 0.0f);
        }
        badge(canvas, this.p, this.aNum, f, f2, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void badge(Canvas canvas, Paint paint, Txt txt, float f, float f2, int i) {
        if (i <= 0) {
            return;
        }
        paint.setShader(null);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(-3665874);
        canvas.drawCircle(f, f2, 5.2f, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.0f);
        paint.setColor(Flip7.BG);
        canvas.drawCircle(f, f2, 5.2f, paint);
        txt.align(Paint.Align.CENTER);
        txt.draw(canvas, HomeData.badge(i), f, f2 + (txt.cap / 2.0f), -1);
        txt.align(Paint.Align.LEFT);
    }

    void dialNum(Canvas canvas, Theme theme, float f, float f2, float f3, float f4, String str, int i) {
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(Flip7.G7);
        canvas.drawCircle(f, f2, f3, this.p);
        this.p.setShader(new SweepGradient(f, f2, new int[]{-3665874, -10835492, -3665874}, (float[]) null));
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        canvas.drawCircle(f, f2, f3, this.p);
        this.p.setShader(null);
        this.p.setStrokeWidth(2.0f);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(-1774607);
        this.rf.set((f - f3) + 3.0f, (f2 - f3) + 3.0f, (f + f3) - 3.0f, (f3 + f2) - 3.0f);
        if (f4 > 0.0f) {
            canvas.drawArc(this.rf, -90.0f, 360.0f * Math.min(1.0f, f4), false, this.p);
        }
        this.p.setStrokeCap(Paint.Cap.BUTT);
        this.aDial.align(Paint.Align.CENTER);
        this.aDial.draw(canvas, str, f, f2 + (this.aDial.cap / 2.0f), i);
        this.aDial.align(Paint.Align.LEFT);
    }

    void flipWedge(Canvas canvas, Theme theme, Path path, HomeData.Entry entry, Txt txt, boolean z, float f, float f2, float f3, float f4, float f5, boolean z2) {
        float f6;
        float f7;
        Canvas canvas2;
        Txt txt2;
        float f8;
        float f9 = f3 - f2;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(z ? Flip7.RED_DEEP : Flip7.G7);
        canvas.drawPath(path, this.p);
        if (z) {
            f6 = f4;
            f7 = f5;
            this.p.setShader(new RadialGradient(f6, f7, f3, new int[]{0, 0, Theme.a(-3665874, 0.45f)}, new float[]{0.0f, f2 / f3, 1.0f}, Shader.TileMode.CLAMP));
            canvas.drawPath(path, this.p);
            this.p.setShader(null);
        } else {
            f6 = f4;
            f7 = f5;
        }
        if (z2) {
            this.p.setColor(Theme.a(-1774607, 0.14f));
            canvas.drawPath(path, this.p);
        }
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(z ? -3665874 : Flip7.G5);
        canvas.drawPath(path, this.p);
        Shapes.pt(f6, f7, f3 - (0.24f * f9), f, this.q);
        Icons.draw(canvas, this.p, entry.icon, this.q[0], this.q[1], Math.min(9.0f, 0.22f * f9), z ? -45725 : -2234900);
        if (txt != null) {
            canvas2 = canvas;
            txt2 = txt;
            f8 = f;
            arcText(canvas2, f6, f7, f3 - (0.55f * f9), f8, entry.label, txt2, z ? -45725 : -2234900);
        } else {
            canvas2 = canvas;
            txt2 = txt;
            f8 = f;
        }
        if (0.28f * f9 < (txt2 != null ? txt2.cap / 2.0f : 0.0f) + 5.0f) {
            return;
        }
        Shapes.pt(f6, f7, f3 - (f9 * 0.83f), f8, this.q);
        String str = z ? "SET" : "OFF";
        float w = this.t65.w(str) + 4.0f;
        int save = canvas2.save();
        canvas2.rotate(f8 + 90.0f, this.q[0], this.q[1]);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(-3665874);
        float f10 = w / 2.0f;
        this.rf.set(this.q[0] - f10, this.q[1] - 3.0f, this.q[0] + f10, this.q[1] + 3.0f);
        canvas2.drawRect(this.rf, this.p);
        this.t65.mid(canvas2, str, this.q[0], this.q[1], -1);
        canvas2.restoreToCount(save);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String unreadApps(HomeData homeData) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        if (homeData.notifs != null) {
            for (int i = 0; i < homeData.notifs.length() && linkedHashSet.size() < 2; i++) {
                JSONObject optJSONObject = homeData.notifs.optJSONObject(i);
                if (optJSONObject != null && !optJSONObject.optString("app").isEmpty()) {
                    linkedHashSet.add(optJSONObject.optString("app").toUpperCase(Locale.ROOT));
                }
            }
        }
        return linkedHashSet.isEmpty() ? Strings.t("adv.unread.sub") : String.join(" · ", linkedHashSet);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    String[] cornerItem(HomeData homeData, String str) {
        char c;
        String str2;
        String str3;
        String str4;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        long currentTimeMillis = System.currentTimeMillis();
        switch (str.hashCode()) {
            case -1884274053:
                if (str.equals("storage")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -838362136:
                if (str.equals("uptime")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case -416416455:
                if (str.equals("screentime")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -331239923:
                if (str.equals("battery")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3076010:
                if (str.equals("data")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 3357441:
                if (str.equals("moon")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 3560141:
                if (str.equals("time")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 92895825:
                if (str.equals("alarm")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 96891546:
                if (str.equals("event")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 99151942:
                if (str.equals("heart")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 109522647:
                if (str.equals("sleep")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 109761319:
                if (str.equals("steps")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1223440372:
                if (str.equals("weather")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 1272354024:
                if (str.equals("notifications")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1843485230:
                if (str.equals("network")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1942983418:
                if (str.equals("daylight")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        str2 = "";
        str3 = "—";
        switch (c) {
            case 0:
                JSONObject jSONObject = homeData.screen;
                if (jSONObject == null || !jSONObject.optBoolean("ok")) {
                    return new String[]{"SCREEN TIME", "—", "ALLOW USAGE ACCESS", null};
                }
                JSONArray optJSONArray = jSONObject.optJSONArray("top");
                if (optJSONArray != null && optJSONArray.length() > 0) {
                    str2 = optJSONArray.optJSONObject(0).optString("label").toUpperCase(Locale.ROOT);
                }
                return new String[]{"SCREEN TIME", Corners.dur(jSONObject.optLong("total")), str2, null};
            case 1:
                Calendar calendar = Calendar.getInstance();
                return new String[]{"TIME", homeData.time, String.format(Locale.getDefault(), "%ta %<te %<tb · WK %d", calendar, Integer.valueOf(calendar.get(3))).toUpperCase(Locale.ROOT), null};
            case 2:
                return new String[]{"BATTERY", homeData.btPct + "%", homeData.btLabel, String.valueOf(homeData.btPct / 100.0f)};
            case 3:
                return new String[]{"NETWORK", homeData.netType.isEmpty() ? "—" : homeData.netType, homeData.dataToday >= 0 ? (homeData.dataToday / 1048576) + " MB TODAY" : "", null};
            case 4:
                return new String[]{"DATA TODAY", homeData.dataToday >= 0 ? (homeData.dataToday / 1048576) + " MB" : "—", homeData.dataToday >= 0 ? homeData.netType : "ALLOW USAGE ACCESS", null};
            case 5:
                return new String[]{"NOTIFICATIONS", homeData.notifs != null ? String.valueOf(homeData.notifs.length()) : "—", "", null};
            case 6:
                return new String[]{"NEXT ALARM", homeData.alarmAt > 0 ? simpleDateFormat.format(new Date(homeData.alarmAt)) : "—", homeData.alarmAt > 0 ? "IN " + Corners.dur(homeData.alarmAt - currentTimeMillis) : "NONE SET", null};
            case 7:
                HomeData.Ev ev = homeData.events.isEmpty() ? null : homeData.events.get(0);
                if (ev == null) {
                    str4 = null;
                } else {
                    str4 = null;
                    str3 = HomeRules.calTime(ev.start, ev.end, ev.allDay, currentTimeMillis, "ALL DAY");
                }
                return new String[]{"NEXT EVENT", str3, ev != null ? ev.title.toUpperCase(Locale.ROOT) : "", str4};
            case '\b':
                HomeData.Metric metric = homeData.hm.size() > 3 ? homeData.hm.get(3) : null;
                return new String[]{"STEPS TODAY", metric != null ? metric.value : "—", "", metric != null ? String.valueOf(metric.frac) : null};
            case '\t':
                HomeData.Metric metric2 = homeData.hm.isEmpty() ? null : homeData.hm.get(0);
                return new String[]{"HEART RATE", metric2 != null ? metric2.value + " BPM" : "—", "", null};
            case '\n':
                JSONObject optJSONObject = homeData.health == null ? null : homeData.health.optJSONObject("sleep");
                if (optJSONObject != null && optJSONObject.has("min")) {
                    str3 = HomeData.fmtMin(Double.valueOf(optJSONObject.optDouble("min")));
                }
                return new String[]{"SLEEP", str3, "", null};
            case 11:
                return new String[]{"WEATHER", homeData.wxTemp, homeData.wxDesc, null};
            case '\f':
                return new String[]{"STORAGE", homeData.stTotal > 0 ? StorageWidget.gb(homeData.stFree) + " FREE" : "—", "", homeData.stTotal > 0 ? String.valueOf(1.0f - (((float) homeData.stFree) / ((float) homeData.stTotal))) : null};
            case FxModel.BOOT /* 13 */:
                JSONArray optJSONArray2 = Store.obj("aos.wx").optJSONArray("daily");
                JSONObject optJSONObject2 = optJSONArray2 == null ? null : optJSONArray2.optJSONObject(0);
                if (optJSONObject2 == null || !optJSONObject2.has("set")) {
                    return new String[]{"DAYLIGHT", "—", "", null};
                }
                long optLong = optJSONObject2.optLong("rise");
                long optLong2 = optJSONObject2.optLong("set");
                if (currentTimeMillis >= optLong2 || currentTimeMillis <= optLong) {
                    return new String[]{"SUNRISE", simpleDateFormat.format(new Date(optLong + (currentTimeMillis > optLong2 ? 86400000L : 0L))), "SUNSET " + simpleDateFormat.format(new Date(optLong2)), null};
                }
                return new String[]{"DAYLIGHT LEFT", Corners.dur(optLong2 - currentTimeMillis), "SUNSET " + simpleDateFormat.format(new Date(optLong2)), String.valueOf(((float) (currentTimeMillis - optLong)) / ((float) Math.max(1L, optLong2 - optLong)))};
            case FxModel.LOCK /* 14 */:
                double moon = Corners.moon(currentTimeMillis);
                return new String[]{"MOON", Math.round(((1.0d - Math.cos((moon * 2.0d) * 3.141592653589793d)) / 2.0d) * 100.0d) + "%", Corners.moonName(moon), null};
            case 15:
                return new String[]{"UP SINCE BOOT", Corners.dur(SystemClock.elapsedRealtime()), "", null};
            default:
                return new String[]{str.toUpperCase(Locale.ROOT), "—", "", null};
        }
    }

    int phoneFilter() {
        try {
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NotificationManager.class);
            if (notificationManager == null) {
                return 1;
            }
            return notificationManager.getCurrentInterruptionFilter();
        } catch (Exception e) {
            return 1;
        }
    }

    int phoneRinger() {
        try {
            AudioManager audioManager = (AudioManager) getContext().getSystemService(AudioManager.class);
            if (audioManager == null) {
                return 2;
            }
            return audioManager.getRingerMode();
        } catch (Exception e) {
            return 2;
        }
    }

    void advTop(Canvas canvas, Theme theme, HomeData homeData, float f, float f2) {
        float f3;
        if (this.advTime == null) {
            this.advTime = new Txt(Fonts.RAJ_600, this.h.L(15.0f, 12.0f), this.h.L(15.0f, 12.0f) * 0.12f);
            this.advSm = new Txt(Fonts.MONO, this.h.L(6.5f, 8.0f), this.h.L(6.5f, 8.0f) * 0.14f);
        }
        float f4 = f - 20.0f;
        Txt txt = this.h.sbMono;
        txt.align(Paint.Align.LEFT);
        String str = homeData.btPct + "%";
        float w = f4 - txt.w(str);
        txt.draw(canvas, str, w, f2 + (txt.cap / 2.0f), theme.INK);
        float f5 = w - 6.0f;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(theme.INK);
        float f6 = f5 - 18.0f;
        float f7 = f2 - 4.5f;
        float f8 = f2 + 4.5f;
        this.rf.set(f6, f7, f5, f8);
        canvas.drawRoundRect(this.rf, 2.0f, 2.0f, this.p);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(theme.sc(homeData.btState) == theme.INK ? theme.BLUE : theme.sc(homeData.btState));
        float f9 = f6 + 2.0f;
        this.rf.set(f9, f7 + 2.0f, (Math.max(0.05f, homeData.btPct / 100.0f) * 14.0f) + f9, f8 - 2.0f);
        canvas.drawRect(this.rf, this.p);
        float f10 = f5 - 28.0f;
        boolean z = false;
        for (int i = 0; i < homeData.qs.size() && i < homeData.qsOn.length; i++) {
            if ("wifi".equals(homeData.qs.get(i).id)) {
                z = homeData.qsOn[i];
            }
        }
        if (z) {
            float w2 = txt.w("WIFI");
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setColor(theme.LINE2);
            float f11 = f10 - w2;
            f3 = 2.0f;
            this.rf.set(f11 - 12.0f, f2 - 8.0f, f10, f2 + 8.0f);
            canvas.drawRoundRect(this.rf, 3.0f, 3.0f, this.p);
            txt.draw(canvas, "WIFI", f11 - 6.0f, f2 + (txt.cap / 2.0f), theme.INK);
            f10 -= (w2 + 12.0f) + 8.0f;
        } else {
            f3 = 2.0f;
        }
        int length = homeData.notifs != null ? homeData.notifs.length() : 0;
        String badge = Advanced.badge(length);
        if (!badge.isEmpty()) {
            float w3 = txt.w(badge);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(theme.RED);
            float f12 = f10 - w3;
            this.rf.set(f12 - 14.0f, f2 - 8.0f, f10, f2 + 8.0f);
            canvas.drawRoundRect(this.rf, 8.0f, 8.0f, this.p);
            txt.draw(canvas, badge, f12 - 7.0f, f2 + (txt.cap / f3), -1);
        }
        txt.align(Paint.Align.RIGHT);
        Calendar calendar = Calendar.getInstance();
        Locale locale = Locale.getDefault();
        String dateLine = Advanced.dateLine(calendar.getDisplayName(7, 1, locale), calendar.get(5), calendar.getDisplayName(2, 1, locale), calendar.get(3));
        this.advTime.draw(canvas, homeData.time, 20.0f, 52.0f, theme.INK);
        this.advSm.draw(canvas, dateLine, 20.0f, this.advSm.line() + 56.0f, theme.DIM);
        String t = length > 0 ? Strings.t("adv.notifs", "n", Integer.valueOf(length)) : Strings.t("adv.notifs.none");
        this.advSm.align(Paint.Align.RIGHT);
        this.advSm.draw(canvas, t, f4, 44.0f, length > 0 ? theme.BLUE : theme.DIM);
        if (homeData.alarmAt > 0) {
            this.advSm.draw(canvas, Strings.t("adv.alarm", "t", new SimpleDateFormat("HH:mm", locale).format(new Date(homeData.alarmAt))), f4, 47.0f + this.advSm.line(), theme.DIM);
        }
        this.advSm.align(Paint.Align.LEFT);
    }

    void advQs(Canvas canvas, Theme theme, HomeData homeData, int i, boolean z, float f, float f2, float f3) {
        Geo geo = this.h.g;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(z ? Theme.a(theme.RED, 0.78f) : theme.SEG);
        canvas.drawPath(geo.qs[i], this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(z ? theme.RED : theme.LINE2);
        canvas.drawPath(geo.qs[i], this.p);
        Shapes.pt(f2, f3, 185.5f, f, this.q);
        Icons.draw(canvas, this.p, homeData.qs.get(i).icon, this.q[0], this.q[1], 9.0f, z ? -1 : theme.INK);
        arcText(canvas, f2, f3, 175.0f, f, homeData.qs.get(i).label, this.qsT[i], z ? -1 : theme.INK);
        arcText(canvas, f2, f3, 167.0f, f, z ? "SET" : "OFF", this.t65, z ? -1 : theme.RED);
    }

    void controlsRail(Canvas canvas, Theme theme, float f, float f2) {
        String t = Strings.t("adv.controls");
        arc(canvas, f, f2, 206.0f, 206.0f, 258.0f, ma(theme.LINE2, 0.9f), 1.0f);
        arc(canvas, f, f2, 206.0f, 282.0f, 334.0f, ma(theme.LINE2, 0.9f), 1.0f);
        arcText(canvas, f, f2, 206.0f, 270.0f, t, this.t65, theme.DIM);
        arcText(canvas, f, f2, 216.0f, 214.0f, Strings.t("adv.system"), this.t65, theme.DIM);
        arcText(canvas, f, f2, 216.0f, 326.0f, Strings.t("adv.signal"), this.t65, theme.DIM);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int ma(int i, float f) {
        return Theme.a(i, ((i >>> 24) / 255.0f) * f);
    }

    void opalDial(Canvas canvas, Theme theme, float f, float f2, float f3, float f4, String str, int i, float f5) {
        ring(canvas, f, f2, f3, ma(theme.LINE, 0.8f), 1.0f);
        SweepGradient sweepGradient = new SweepGradient(f, f2, new int[]{theme.RED, -1774607, theme.BLUE, theme.BLUE}, new float[]{0.0f, 0.45f, 0.9f, 1.0f});
        Matrix matrix = new Matrix();
        matrix.setRotate(-90.0f, f, f2);
        sweepGradient.setLocalMatrix(matrix);
        this.p.setShader(sweepGradient);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.8f);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setAlpha(Math.round(255.0f * f5));
        this.rf.set(f - f3, f2 - f3, f + f3, f2 + f3);
        if (f4 > 0.0f) {
            canvas.drawArc(this.rf, -90.0f, Math.min(1.0f, f4) * 360.0f, false, this.p);
        }
        this.p.setShader(null);
        this.p.setAlpha(255);
        this.p.setStrokeCap(Paint.Cap.BUTT);
        arc(canvas, f, f2, f3 + 6.0f, -60.0f, 240.0f, ma(theme.LINE2, 0.8f), 1.0f);
        Icons.draw(canvas, this.p, str, f, f2, 13.0f, i);
    }

    void dial(Canvas canvas, float f, String str, String str2, String str3, float f2, String str4, int i, int i2, float f3) {
        Canvas canvas2;
        Theme theme = this.h.th;
        if (this.h.adv) {
            opalDial(canvas, theme, f, 314.0f, 14.0f, f2, str4, i, f3);
            float min = (Math.min(f, this.h.g.LW - f) * 2.0f) - 4.0f;
            this.h.raj17.drawFit(canvas, str, f, 354.0f, theme.INK, min);
            this.t75.drawFit(canvas, str2, f, 358.0f + this.t75.line(), i2, min);
            this.t65.drawFit(canvas, str3, f, 288.0f, theme.DIM, min);
            return;
        }
        ring(canvas, f, 314.0f, 14.0f, theme.LINE, 1.0f);
        ring(canvas, f, 314.0f, 19.0f, ma(theme.LINE, 0.5f), 1.0f);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.6f);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(Theme.a(i, f3));
        this.rf.set(f - 14.0f, 300.0f, f + 14.0f, 328.0f);
        if (f2 > 0.0f) {
            canvas.drawArc(this.rf, -90.0f, Math.min(1.0f, f2) * 360.0f, false, this.p);
            canvas2 = canvas;
        } else {
            this.p.setStyle(Paint.Style.FILL);
            canvas2 = canvas;
            canvas2.drawCircle(f, 300.0f, 0.8f, this.p);
        }
        this.p.setStrokeCap(Paint.Cap.BUTT);
        Icons.draw(canvas2, this.p, str4, f, 314.0f, 13.0f, i);
        float min2 = (Math.min(f, this.h.g.LW - f) * 2.0f) - 4.0f;
        this.h.raj17.drawFit(canvas, str, f, 354.0f, theme.INK, min2);
        this.t75.drawFit(canvas, str2, f, 358.0f + this.t75.line(), i2, min2);
        this.t65.drawFit(canvas, str3, f, 288.0f, theme.DIM, min2);
    }
}
