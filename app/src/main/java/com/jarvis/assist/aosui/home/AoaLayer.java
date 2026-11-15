package com.jarvis.assist.aosui.home;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.os.SystemClock;
import android.view.View;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.AoaGeo;
import com.jarvis.assist.aosui.core.AoaTransit;
import com.jarvis.assist.aosui.core.AppGridModel;
import com.jarvis.assist.aosui.core.Corners;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Shapes;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnlockAnim;
import com.jarvis.assist.aosui.home.HomeData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaLayer extends View {
    static final int DIM = -9399652;
    static final int MID = -6902604;
    static final int O = -34278;
    static final int T = -11624262;
    static final int W = -1511182;
    long anim0;
    FrameClock.Handle animH;
    boolean animLight;
    float animT;
    final Paint bp;
    Txt chip;
    final HomeCtx h;
    boolean held;
    String[] inIc;
    String[] inId;
    String[] inLab;
    Txt lab;
    Bitmap logo;
    float maxD;
    Txt mono5;
    Txt mono6;
    Txt mono7;
    Txt monoB;
    Txt nameT;
    Consumer<Float> onAnim;
    final Paint p;
    final Path path;
    final float[] q;
    Txt raj13;
    Txt raj16;
    Txt raj9;
    int readout;
    boolean rev;
    final RectF rf;
    Txt seg;
    Txt segI;
    Txt segS;
    float speed;
    String[] tIc;
    String[] tId;
    String[] tLab;
    Txt tileN;
    Txt tileT;
    static final int FILL = Theme.rgba(10, 17, 20, 0.7f);
    static final String[][] IN_DEF = {new String[]{"gallery", "PHOTOS", "image"}, new String[]{"calendar", "CALENDAR", "cal"}, new String[]{"maps", "MAPS", "pin"}, new String[]{"camera", "CAMERA", "cam"}, new String[]{"music", "MUSIC", "music"}, new String[]{"clock", "CLOCK", "clock"}, new String[]{"email", "EMAIL", "mail"}};
    static final String[][] TILE_DEF = {new String[]{"email", "EMAIL", "mail"}, new String[]{"browser", "BROWSER", "globe"}, new String[]{"sms", "MESSAGES", "msg"}, new String[]{"music", "MUSIC", "play"}, new String[]{"camera", "CAMERA", "cam"}, new String[]{"clock", "CLOCK", "clock"}, new String[]{"notes", "NOTES", "list"}, new String[]{"maps", "MAPS", "pin"}, new String[]{"dial", "PHONE", "phone"}};
    static final String[] MODE_IC = {"moon", "bell", "heart", "list", "spark", "grid"};

    static int ln(float f) {
        return Theme.rgba(192, 214, 220, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AoaLayer(Context context, HomeCtx homeCtx) {
        super(context);
        this.p = new Paint(1);
        this.bp = new Paint(2);
        this.rf = new RectF();
        this.q = new float[2];
        this.path = new Path();
        this.inId = new String[0];
        this.inLab = new String[0];
        this.inIc = new String[0];
        this.tId = new String[0];
        this.tLab = new String[0];
        this.tIc = new String[0];
        this.animT = -1.0f;
        this.maxD = 1.0f;
        this.speed = 1.0f;
        this.h = homeCtx;
        this.logo = BackLayer.logo(context);
        this.bp.setColorFilter(new PorterDuffColorFilter(-34278, PorterDuff.Mode.SRC_IN));
        setImportantForAccessibility(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void slots() {
        HomeData homeData = this.h.d;
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        Iterator<HomeData.Entry> it = homeData.wheel.iterator();
        while (it.hasNext()) {
            hashSet.add(it.next().id);
        }
        HashSet hashSet2 = new HashSet();
        Iterator<HomeData.Entry> it2 = homeData.wheel.iterator();
        while (it2.hasNext()) {
            hashSet2.add(it2.next().label.toUpperCase(Locale.ROOT));
        }
        for (int i = 6; i < homeData.wheel.size() && arrayList.size() < 3; i++) {
            HomeData.Entry entry = homeData.wheel.get(i);
            arrayList.add(new String[]{"w" + i, entry.label, entry.icon});
        }
        for (String[] strArr : IN_DEF) {
            if (arrayList.size() >= 3) {
                break;
            }
            if (!hashSet.contains(strArr[0]) && !hashSet2.contains(strArr[1]) && (!strArr[0].equals("gallery") || !hashSet.contains("photos"))) {
                arrayList.add(new String[]{"x:tile:" + strArr[0], strArr[1], strArr[2]});
            }
        }
        this.inId = col(arrayList, 0);
        this.inLab = col(arrayList, 1);
        this.inIc = col(arrayList, 2);
        ArrayList arrayList2 = new ArrayList();
        List<String> parse = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
        PackageManager packageManager = getContext().getPackageManager();
        for (int i2 = 0; i2 < parse.size() && arrayList2.size() < 9; i2++) {
            String str = parse.get(i2);
            int indexOf = str.indexOf(47);
            String substring = str.substring(0, indexOf);
            try {
                substring = packageManager.getActivityInfo(new ComponentName(str.substring(0, indexOf), str.substring(indexOf + 1)), 0).loadLabel(packageManager).toString();
            } catch (Exception e) {
            }
            arrayList2.add(new String[]{"g" + i2, substring.toUpperCase(Locale.getDefault()), "grid"});
        }
        for (String[] strArr2 : TILE_DEF) {
            if (arrayList2.size() >= 9) {
                break;
            }
            if (!hashSet.contains(strArr2[0])) {
                arrayList2.add(new String[]{"x:tile:" + strArr2[0], strArr2[1], strArr2[2]});
            }
        }
        this.tId = col(arrayList2, 0);
        this.tLab = col(arrayList2, 1);
        this.tIc = col(arrayList2, 2);
    }

    static String[] col(List<String[]> list, int i) {
        int size = list.size();
        String[] strArr = new String[size];
        for (int i2 = 0; i2 < size; i2++) {
            strArr[i2] = list.get(i2)[i];
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepare() {
        float f = this.h.g.aS;
        this.seg = new Txt(Fonts.RAJ_600, 7.5f * f, 1.05f * f).align(Paint.Align.CENTER);
        this.segI = new Txt(Fonts.RAJ_600, 9.5f * f, 1.3f * f).align(Paint.Align.CENTER);
        float f2 = 0.8f * f;
        this.segS = new Txt(Fonts.RAJ_600, 6.2f * f, f2).align(Paint.Align.CENTER);
        float f3 = 0.3f * f;
        this.chip = new Txt(Fonts.MONO, 4.0f * f, f3).align(Paint.Align.CENTER);
        this.mono5 = new Txt(Fonts.MONO, 5.0f * f, 0.7f * f);
        this.mono6 = new Txt(Fonts.MONO, 6.0f * f, 0.9f * f);
        float f4 = 1.1f * f;
        this.mono7 = new Txt(Fonts.MONO, 7.0f * f, f4);
        this.monoB = new Txt(Fonts.MONO, 9.0f * f, f4);
        this.raj13 = new Txt(Fonts.RAJ_400, 13.0f * f, f3);
        this.raj16 = new Txt(Fonts.RAJ_400, 16.0f * f, 0.0f);
        this.raj9 = new Txt(Fonts.RAJ_600, 8.0f * f, 1.2f * f).align(Paint.Align.CENTER);
        this.tileT = new Txt(Fonts.MONO, 5.6f * f, f2).align(Paint.Align.CENTER);
        this.tileN = new Txt(Fonts.MONO, 5.5f * f, f3).align(Paint.Align.RIGHT);
        this.nameT = new Txt(Fonts.RAJ_600, 10.0f * f, 3.4f * f);
        this.lab = new Txt(Fonts.MONO, 5.8f * f, f * 1.8f).align(Paint.Align.CENTER);
        invalidate();
    }

    void badge(Canvas canvas, float f, float f2, int i) {
        float f3 = this.h.g.aS * 4.4f;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(-34278);
        canvas.drawCircle(f, f2, f3, this.p);
        this.chip.mid(canvas, HomeData.badge(i), f, f2, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean animating() {
        return this.animT >= 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void playUnlock(boolean z) {
        playUnlock(z, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void playUnlock(boolean z, float f) {
        this.rev = false;
        this.held = false;
        this.speed = f;
        start(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void playContract(boolean z) {
        this.rev = true;
        this.held = false;
        this.speed = 2.8f;
        start(z);
    }

    void start(boolean z) {
        this.animLight = z;
        this.anim0 = SystemClock.uptimeMillis();
        this.animT = this.rev ? UnlockAnim.total(z) : 0.0f;
        if (this.animH == null) {
            this.animH = FrameClock.I.add(new FrameClock.Sub() { // from class: com.jarvis.assist.aosui.home.AoaLayer$$ExternalSyntheticLambda1
                @Override // com.jarvis.assist.aosui.core.FrameClock.Sub
                public final boolean frame(long j, float f) {
                    return AoaLayer.this.m217lambda$start$0$comaugmaosohomeAoaLayer(j, f);
                }
            }, this);
        }
        this.animH.kick();
        if (this.onAnim != null) {
            this.onAnim.accept(Float.valueOf(0.0f));
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$start$0$com-augma-oso-home-AoaLayer, reason: not valid java name */
    public /* synthetic */ boolean m217lambda$start$0$comaugmaosohomeAoaLayer(long j, float f) {
        if (this.animT < 0.0f || this.held) {
            return false;
        }
        float uptimeMillis = (float) (SystemClock.uptimeMillis() - this.anim0);
        if (this.rev) {
            this.animT = AoaTransit.reversed(uptimeMillis, this.speed, UnlockAnim.total(this.animLight));
            if (this.animT <= 0.0f) {
                this.animT = 0.0f;
                this.held = true;
            }
        } else {
            this.animT = uptimeMillis * this.speed;
            if (UnlockAnim.done(this.animT, this.animLight)) {
                this.animT = -1.0f;
            }
        }
        if (this.onAnim != null) {
            this.onAnim.accept(Float.valueOf(this.animT));
        }
        invalidate();
        return this.animT >= 0.0f && !this.held;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void skipUnlock() {
        if (this.animT < 0.0f) {
            return;
        }
        this.animT = -1.0f;
        this.held = false;
        if (this.onAnim != null) {
            this.onAnim.accept(Float.valueOf(-1.0f));
        }
        invalidate();
    }

    float dist(float f, float f2) {
        return (float) Math.hypot(f - this.h.g.aCx, f2 - this.h.g.aCy);
    }

    int item(Canvas canvas, float f, float f2) {
        if (this.animT < 0.0f) {
            return -1;
        }
        float item = UnlockAnim.item(dist(f, f2), this.maxD, this.animT, this.animLight);
        int saveLayerAlpha = canvas.saveLayerAlpha(null, Math.round(255.0f * item));
        float f3 = 1.0f - item;
        canvas.translate((this.h.g.aCx - f) * f3, f3 * (this.h.g.aCy - f2));
        float f4 = (item * 0.4f) + 0.6f;
        canvas.scale(f4, f4, f, f2);
        return saveLayerAlpha;
    }

    int ring(Canvas canvas, int i) {
        if (this.animT < 0.0f) {
            return -1;
        }
        float ring = UnlockAnim.ring(i, this.animT, this.animLight);
        int saveLayerAlpha = canvas.saveLayerAlpha(null, Math.round(255.0f * ring));
        float f = this.h.g.aCx;
        float f2 = this.h.g.aCy;
        if (!this.animLight) {
            canvas.rotate((1.0f - ring) * (-12.0f), f, f2);
            float f3 = (ring * 0.75f) + 0.25f;
            canvas.scale(f3, f3, f, f2);
        }
        return saveLayerAlpha;
    }

    void end(Canvas canvas, int i) {
        if (i >= 0) {
            canvas.restoreToCount(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cycle(int i) {
        this.readout = (((this.readout + i) % 3) + 3) % 3;
        invalidate();
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

    void arc(Canvas canvas, float f, float f2, float f3, float f4, float f5, int i, float f6) {
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(f6);
        this.p.setColor(i);
        this.rf.set(f - f3, f2 - f3, f + f3, f2 + f3);
        canvas.drawArc(this.rf, f4, f5 - f4, false, this.p);
    }

    void labArc(Canvas canvas, float f, float f2, float f3, float f4, float f5, String[] strArr, float[] fArr, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < strArr.length; i3++) {
            float degrees = (float) Math.toDegrees(((this.lab.w(strArr[i3]) / 2.0f) + (this.h.g.aS * 4.0f)) / f3);
            arrayList.add(new float[]{fArr[i3] - degrees, fArr[i3] + degrees});
        }
        arrayList.sort(new Comparator() { // from class: com.jarvis.assist.aosui.home.AoaLayer$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = Float.compare(((float[]) obj)[0], ((float[]) obj2)[0]);
                return compare;
            }
        });
        Iterator it = arrayList.iterator();
        float f6 = f4;
        while (it.hasNext()) {
            float[] fArr2 = (float[]) it.next();
            if (fArr2[0] > f6) {
                arc(canvas, f, f2, f3, f6, fArr2[0], i, Math.max(0.6f, this.h.g.aS * 0.8f));
            }
            f6 = fArr2[1];
        }
        if (f5 > f6) {
            arc(canvas, f, f2, f3, f6, f5, i, Math.max(0.6f, this.h.g.aS * 0.8f));
        }
        for (int i4 = 0; i4 < strArr.length; i4++) {
            arcText(canvas, f, f2, f3, fArr[i4], strArr[i4], this.lab, i2);
        }
    }

    void wedge(Canvas canvas, Path path, int i, int i2) {
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(i);
        canvas.drawPath(path, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(Math.max(0.6f, this.h.g.aS * 0.8f));
        this.p.setColor(i2);
        canvas.drawPath(path, this.p);
    }

    void dial(Canvas canvas, float f, float f2, float f3, float f4, String str, int i) {
        Canvas canvas2;
        float f5 = this.h.g.aS;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(Math.max(0.6f, 0.7f * f5));
        this.p.setColor(ln(0.25f));
        canvas.drawCircle(f, f2, (3.0f * f5) + f3, this.p);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(Theme.rgba(3, 4, 5, 0.8f));
        canvas.drawCircle(f, f2, f3, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(ln(0.18f));
        canvas.drawCircle(f, f2, f3, this.p);
        this.p.setStrokeWidth(f5 * 1.6f);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(i);
        this.rf.set(f - f3, f2 - f3, f + f3, f3 + f2);
        if (f4 > 0.0f) {
            canvas2 = canvas;
            canvas2.drawArc(this.rf, -90.0f, Math.min(1.0f, f4) * 360.0f, false, this.p);
        } else {
            canvas2 = canvas;
        }
        this.p.setStrokeCap(Paint.Cap.BUTT);
        Txt txt = this.raj9;
        txt.draw(canvas2, str, f, f2 + (txt.cap / 2.0f), i);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        float f2;
        Canvas canvas2 = canvas;
        Geo geo = this.h.g;
        if (this.seg == null || !geo.aoa()) {
            return;
        }
        HomeData homeData = this.h.d;
        float f3 = geo.aS;
        int save = canvas2.save();
        canvas2.translate(geo.ox, geo.oy);
        canvas2.scale(geo.S, geo.S);
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(Math.max(0.6f, 0.7f * f3));
        this.p.setColor(ln(0.22f));
        float f4 = f3 * 71.0f;
        float f5 = f3 * 5.0f;
        float f6 = 3.0f * f4;
        float f7 = geo.aCx - f6;
        float f8 = geo.aCy - f6;
        while (f8 < geo.LH) {
            float f9 = f7;
            while (f9 < geo.LW) {
                if (f9 < 0.0f) {
                    f = f8;
                    f2 = f9;
                } else if (f8 < 0.0f) {
                    f = f8;
                    f2 = f9;
                } else {
                    canvas2.drawLine(f9 - f5, f8, f9 + f5, f8, this.p);
                    f = f8;
                    canvas2 = canvas;
                    f2 = f9;
                    canvas2.drawLine(f2, f - f5, f9, f + f5, this.p);
                }
                f9 = f2 + f4;
                f8 = f;
            }
            f8 += f4;
        }
        this.maxD = Math.max(Math.max(dist(0.0f, 0.0f), dist(geo.LW, 0.0f)), Math.max(dist(0.0f, geo.LH), dist(geo.LW, geo.LH)));
        if (!geo.land) {
            int item = item(canvas2, geo.aCx, Math.max(geo.statusY, 10.0f));
            status(canvas2, homeData, f3);
            end(canvas2, item);
        }
        int item2 = item(canvas2, (geo.aCornL.centerX() * 0.5f) + (geo.aCornR.centerX() * 0.5f), geo.aCornL.centerY());
        corners(canvas2, homeData, f3);
        end(canvas2, item2);
        control(canvas2, homeData, f3);
        int item3 = item(canvas2, geo.aCx, geo.aDialY);
        readouts(canvas2, homeData, f3);
        end(canvas2, item3);
        int item4 = item(canvas2, (geo.aMode[0].centerX() * 0.5f) + (geo.aMode[5].centerX() * 0.5f), geo.aMode[0].centerY());
        modes(canvas2, f3);
        end(canvas2, item4);
        int item5 = item(canvas2, geo.aTile[4].centerX(), geo.aTile[4].centerY());
        tiles(canvas2, homeData, f3);
        end(canvas2, item5);
        int item6 = item(canvas2, geo.dX, geo.land ? geo.aDockRow.centerY() : geo.dTop + (60.0f * f3));
        dock(canvas2, homeData, f3);
        end(canvas2, item6);
        canvas2.restoreToCount(save);
    }

    void status(Canvas canvas, HomeData homeData, float f) {
        Geo geo = this.h.g;
        float f2 = geo.aCx - (180.0f * f);
        float max = Math.max(geo.statusY, geo.aCy - (219.0f * f));
        if (this.logo != null) {
            float f3 = 18.0f * f;
            float f4 = (22.0f * f) + f2;
            float f5 = ((202.0f * f3) / 256.0f) / 2.0f;
            this.rf.set(f4, max - f5, f3 + f4, f5 + max);
            canvas.drawBitmap(this.logo, (Rect) null, this.rf, this.bp);
        }
        Txt txt = this.monoB;
        txt.align(Paint.Align.LEFT);
        txt.tracked(canvas, "OS", txt.tracked(canvas, "a", f2 + (46.0f * f), max + (txt.cap / 2.0f), W), max + (txt.cap / 2.0f), -34278);
        float f6 = f2 + (346.0f * f);
        float f7 = f * 12.0f;
        Txt align = new Txt(Fonts.RAJ_400, f7, 0.2f * f).align(Paint.Align.RIGHT);
        String str = homeData.btPct + "%";
        align.draw(canvas, str, f6, max + (align.cap / 2.0f), W);
        float w = f6 - (align.w(str) + (f * 5.0f));
        float f8 = f * 20.0f;
        float f9 = 9.0f * f;
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(Math.max(0.8f, f));
        this.p.setColor(W);
        float f10 = w - f8;
        float f11 = f9 / 2.0f;
        float f12 = max - f11;
        float f13 = f11 + max;
        this.rf.set(f10, f12, w, f13);
        canvas.drawRect(this.rf, this.p);
        float f14 = f9 / 5.0f;
        canvas.drawRect(w, max - f14, (1.5f * f) + w, f14 + max, this.p);
        this.p.setStyle(Paint.Style.FILL);
        float f15 = f * 2.0f;
        float f16 = f10 + f15;
        this.rf.set(f16, f12 + f15, ((f8 - (4.0f * f)) * Math.max(0.05f, homeData.btPct / 100.0f)) + f16, f13 - f15);
        canvas.drawRect(this.rf, this.p);
        float f17 = w - (f8 + (8.0f * f));
        String upperCase = "WIFI".equalsIgnoreCase(homeData.netType) || homeData.netType.toUpperCase(Locale.ROOT).contains("WI") ? "WIFI" : homeData.netType.isEmpty() ? "—" : homeData.netType.toUpperCase(Locale.ROOT);
        this.mono7.align(Paint.Align.CENTER);
        float w2 = this.mono7.w(upperCase) + (10.0f * f);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(Math.max(0.6f, f * 0.8f));
        this.p.setColor(ln(0.3f));
        float f18 = 7.0f * f;
        this.rf.set(f17 - w2, max - f18, f17, f18 + max);
        canvas.drawRect(this.rf, this.p);
        this.mono7.draw(canvas, upperCase, f17 - (w2 / 2.0f), (this.mono7.cap / 2.0f) + max, W);
        this.mono7.align(Paint.Align.LEFT);
        float f19 = f17 - (w2 + f7);
        if (homeData.alarmAt > 0) {
            Icons.draw(canvas, this.p, "clock", f19 - (6.0f * f), max, f * 11.0f, MID, 1.1f);
        }
    }

    void corners(Canvas canvas, HomeData homeData, float f) {
        Geo geo = this.h.g;
        RectF rectF = geo.aCornL;
        RectF rectF2 = geo.aCornR;
        Calendar calendar = Calendar.getInstance();
        Locale locale = Locale.getDefault();
        Txt txt = geo.land ? new Txt(Fonts.MONO, 13.0f, 1.6f) : new Txt(Fonts.MONO, 9.5f * f, 1.3f * f);
        txt.align(Paint.Align.LEFT);
        txt.draw(canvas, homeData.time, rectF.left, rectF.top + txt.cap, W);
        float f2 = 3.0f * f;
        float f3 = rectF.top + txt.cap + f2;
        String upperCase = String.format(locale, "%tA %<td %<tB", calendar).toUpperCase(locale);
        String str = "WEEK " + calendar.get(3) + " · DAY " + calendar.get(6) + " · " + calendar.get(1);
        Txt txt2 = geo.land ? new Txt(Fonts.MONO, 6.0f, 0.9f) : this.mono5;
        txt2.align(Paint.Align.LEFT);
        float f4 = f3 + txt2.cap + (1.0f * f);
        Txt txt3 = txt2;
        txt3.draw(canvas, upperCase, rectF.left, f4, MID);
        float line = f4 + txt3.line();
        txt3.draw(canvas, str, rectF.left, line, MID);
        float line2 = line + txt2.line() + f2;
        float f5 = f * 6.0f;
        Icons.draw(canvas, this.p, "clock", rectF.left + f2, line2 - (txt2.cap / 2.0f), f5, -11624262, 1.0f);
        txt2.draw(canvas, "CLOCK · ALARMS", rectF.left + (10.0f * f), line2, -11624262);
        String upperCase2 = homeData.name == null ? "" : homeData.name.toUpperCase(locale);
        Txt txt4 = geo.land ? new Txt(Fonts.RAJ_600, 14.0f, 4.0f) : this.nameT;
        txt4.align(Paint.Align.RIGHT);
        txt4.draw(canvas, WidgetView.fitText(txt4, upperCase2, rectF2.width()), rectF2.right + txt4.ls, rectF2.top + txt4.cap, W);
        float f6 = rectF2.top + txt4.cap + (4.0f * f);
        if (!geo.land) {
            txt2 = this.mono5;
        }
        txt2.align(Paint.Align.RIGHT);
        float f7 = f6 + txt2.cap;
        Txt txt5 = txt2;
        txt5.draw(canvas, "AGE OF", rectF2.right, f7, DIM);
        float line3 = f7 + txt5.line();
        txt5.draw(canvas, "AUGMENTATION", rectF2.right, line3, DIM);
        float line4 = line3 + txt5.line() + f2;
        int length = homeData.notifs == null ? 0 : homeData.notifs.length();
        String valueOf = length > 0 ? String.valueOf(length) : "CLEAR";
        txt5.draw(canvas, valueOf, rectF2.right, line4, -34278);
        float w = (rectF2.right - txt5.w(valueOf)) - (f * 2.0f);
        txt5.draw(canvas, "NOTIFICATIONS · ", w, line4, -11624262);
        txt5.align(Paint.Align.LEFT);
        Icons.draw(canvas, this.p, "bell", (w - txt5.w("NOTIFICATIONS · ")) - f5, line4 - (txt5.cap / 2.0f), f5, -11624262, 1.0f);
    }

    void control(Canvas canvas, HomeData homeData, float f) {
        float f2;
        int i;
        float f3;
        String str;
        int i2;
        char c;
        char c2;
        int i3;
        int i4;
        char c3;
        int i5;
        String str2;
        int i6;
        Geo geo = this.h.g;
        float f4 = geo.aCx;
        float f5 = geo.aCy;
        float f6 = f * 0.864f;
        int ring = ring(canvas, 2);
        float f7 = f * 0.8f;
        arc(canvas, f4, f5, 179.0f * f6, 180.0f, 360.0f, ln(0.22f), Math.max(0.6f, f7));
        float f8 = f * 2.0f;
        arc(canvas, f4, f5, (150.0f * f6) - f8, 180.0f, 360.0f, ln(0.08f), Math.max(0.6f, f7));
        float f9 = 0.6f;
        Canvas canvas2 = canvas;
        labArc(canvas2, f4, f5, geo.aQuickR, 180.0f, 360.0f, new String[]{"QUICK", "CONNECT", "DEVICE"}, new float[]{270.0f, 216.0f, 324.0f}, ln(0.22f), W);
        AoaLayer aoaLayer = this;
        float f10 = f4;
        float f11 = f5;
        int i7 = 0;
        while (true) {
            f2 = f9;
            i = 255;
            if (i7 >= geo.nq) {
                break;
            }
            HomeData.Entry entry = homeData.qs.get(i7);
            boolean z = i7 < homeData.qsOn.length && homeData.qsOn[i7];
            float f12 = geo.qsA[i7][2];
            boolean equals = ("q" + i7).equals(aoaLayer.h.pressed);
            Path path = geo.qs[i7];
            if (equals) {
                i4 = i7;
                c3 = 1;
                i5 = Theme.rgba(255, 122, 26, 0.3f);
            } else {
                i4 = i7;
                c3 = 1;
                i5 = FILL;
            }
            aoaLayer.wedge(canvas2, path, i5, equals ? -34278 : ln(0.22f));
            Shapes.pt(f10, f11, geo.aOut[c3] - (f6 * 6.0f), f12, aoaLayer.q);
            Paint paint = aoaLayer.p;
            String str3 = entry.icon;
            float f13 = aoaLayer.q[0];
            float f14 = aoaLayer.q[c3];
            char c4 = c3;
            float f15 = f10;
            int i8 = i4;
            float f16 = f11;
            Icons.draw(canvas, paint, str3, f13, f14, 7.0f * f, z ? -34278 : MID, 1.0f);
            canvas2 = canvas;
            arcText(canvas2, f15, f16, geo.aOut[0] + (11.0f * f6), f12, entry.label, aoaLayer.segS, W);
            aoaLayer = this;
            int i9 = i8 < homeData.qsCnt.length ? homeData.qsCnt[i8] : 0;
            if (i9 > 0) {
                str2 = String.valueOf(i9);
            } else if ("torch".equals(entry.id)) {
                if (z) {
                    str2 = "ON";
                }
                str2 = "OFF";
            } else {
                if (z) {
                    str2 = "SET";
                }
                str2 = "OFF";
            }
            Shapes.pt(f15, f16, geo.aOut[0] + (f6 * 4.0f), f12, aoaLayer.q);
            int save = canvas2.save();
            canvas2.rotate(90.0f + f12, aoaLayer.q[0], aoaLayer.q[c4]);
            aoaLayer.p.setStyle(Paint.Style.FILL);
            aoaLayer.p.setColor((z || i9 > 0) ? -34278 : Theme.a(-34278, 0.0f));
            float f17 = 9.0f * f;
            float f18 = 2.6f * f;
            int i10 = i9;
            aoaLayer.rf.set(aoaLayer.q[0] - f17, aoaLayer.q[c4] - f18, aoaLayer.q[0] + f17, aoaLayer.q[c4] + f18);
            canvas2.drawRect(aoaLayer.rf, aoaLayer.p);
            if (z || i10 > 0) {
                i6 = -34278;
            } else {
                aoaLayer.p.setStyle(Paint.Style.STROKE);
                aoaLayer.p.setStrokeWidth(Math.max(0.5f, f * f2));
                i6 = -34278;
                aoaLayer.p.setColor(Theme.a(-34278, 0.7f));
                canvas2.drawRect(aoaLayer.rf, aoaLayer.p);
            }
            aoaLayer.chip.mid(canvas2, str2, aoaLayer.q[0], aoaLayer.q[c4], (z || i10 > 0) ? W : i6);
            canvas2.restoreToCount(save);
            i7 = i8 + 1;
            f10 = f15;
            f11 = f16;
            f9 = f2;
        }
        float f19 = f10;
        float f20 = f11;
        int i11 = 0;
        aoaLayer.end(canvas2, ring);
        int ring2 = aoaLayer.ring(canvas2, 1);
        int i12 = 0;
        while (i12 < geo.nw) {
            HomeData.Entry entry2 = homeData.wheel.get(i12);
            float f21 = geo.whA[i12][2];
            boolean equals2 = ("w" + i12).equals(aoaLayer.h.pressed);
            Path path2 = geo.wh[i12];
            if (equals2) {
                i2 = i12;
                c = 26;
                c2 = 'z';
                i3 = Theme.rgba(i, 122, 26, 0.3f);
            } else {
                i2 = i12;
                c = 26;
                c2 = 'z';
                i3 = FILL;
            }
            aoaLayer.wedge(canvas2, path2, i3, equals2 ? -34278 : ln(0.22f));
            float f22 = f6 * 4.0f;
            float f23 = f19;
            float f24 = f20;
            int i13 = i2;
            int i14 = i11;
            arcText(canvas2, f23, f24, ((geo.aMidR[i11] + geo.aMidR[1]) / 2.0f) + f22, f21, entry2.label, aoaLayer.seg, W);
            aoaLayer = this;
            Shapes.pt(f23, f24, geo.aMidR[i14] + (f6 * 7.0f), f21, aoaLayer.q);
            Icons.draw(canvas, aoaLayer.p, entry2.icon, aoaLayer.q[i14], aoaLayer.q[1], f * 7.0f, MID, 1.0f);
            canvas2 = canvas;
            int i15 = i13 < homeData.wheelCnt.length ? homeData.wheelCnt[i13] : i14;
            if (i15 > 0) {
                Shapes.pt(f23, f24, geo.aMidR[1] - f22, geo.whA[i13][1] - 3.0f, aoaLayer.q);
                aoaLayer.badge(canvas2, aoaLayer.q[i14], aoaLayer.q[1], i15);
            }
            i12 = i13 + 1;
            f19 = f23;
            f20 = f24;
            i11 = i14;
            i = 255;
        }
        float f25 = f19;
        float f26 = f20;
        aoaLayer.end(canvas2, ring2);
        int ring3 = aoaLayer.ring(canvas2, i11);
        int i16 = 0;
        while (i16 < geo.nIn && i16 < aoaLayer.inLab.length) {
            float f27 = geo.inA[i16][2];
            aoaLayer.wedge(canvas2, geo.inP[i16], Theme.rgba(78, 160, 186, 0.14f), Theme.a(-11624262, 0.45f));
            arcText(canvas2, f25, f26, ((geo.aIn[0] + geo.aIn[1]) / 2.0f) + (f6 * 4.0f), f27, aoaLayer.inLab[i16], aoaLayer.segI, -11624262);
            aoaLayer = this;
            Shapes.pt(f25, f26, geo.aIn[0] + (f6 * 9.0f), f27, aoaLayer.q);
            Icons.draw(canvas, aoaLayer.p, aoaLayer.inIc[i16], aoaLayer.q[0], aoaLayer.q[1], f * 8.0f, -11624262, 1.0f);
            i16++;
            canvas2 = canvas;
        }
        float f28 = f * 78.0f;
        arc(canvas, f25, f26, f28, (float) Math.toDegrees(0.32d), (float) Math.toDegrees(1.05d), ln(0.3f), Math.max(f2, f7));
        Canvas canvas3 = canvas;
        arc(canvas3, f25, f26, f28, (float) Math.toDegrees(2.09d), (float) Math.toDegrees(2.82d), ln(0.3f), Math.max(f2, f7));
        float f29 = f28;
        int i17 = -1;
        while (true) {
            int i18 = i17;
            if (i18 > 1) {
                break;
            }
            float f30 = f25 + (i18 * geo.aBtnDx);
            float f31 = geo.aBtnY;
            float f32 = f * 7.0f;
            this.p.setStyle(Paint.Style.FILL);
            float f33 = 1.0f;
            float f34 = f29;
            this.p.setColor(Theme.rgba(3, 5, 6, 0.9f));
            canvas3.drawCircle(f30, f31, f32, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(0.6f, f7));
            this.p.setColor(ln(0.6f));
            canvas3.drawCircle(f30, f31, f32, this.p);
            float f35 = f * 2.6f;
            if (i18 >= 0) {
                f33 = -1.0f;
            }
            this.p.setColor(W);
            float f36 = f35 * f33;
            float f37 = f31 + f36;
            canvas.drawLine(f30, f31 - f36, f30, f37, this.p);
            float f38 = f31 + ((f35 - f8) * f33);
            canvas.drawLine(f30 - f8, f38, f30, f37, this.p);
            canvas.drawLine(f30 + f8, f38, f30, f37, this.p);
            i17 = i18 + 2;
            canvas3 = canvas;
            f29 = f34;
        }
        float f39 = f29;
        if (this.readout == 1) {
            f3 = homeData.btPct / 100.0f;
        } else {
            f3 = this.readout == 2 ? homeData.stTotal > 0 ? 1.0f - (((float) homeData.stFree) / ((float) homeData.stTotal)) : 0.0f : homeData.dayF;
        }
        if (this.readout == 1) {
            str = "BATTERY";
        } else {
            str = this.readout == 2 ? "STORAGE" : "DAY";
        }
        arcText(canvas, f25, f26, f39 - (f * 1.0f), 90.0f, str + " · " + Math.round(f3 * 100.0f) + "%", this.lab, W);
        end(canvas, ring3);
    }

    void readouts(Canvas canvas, HomeData homeData, float f) {
        Geo geo = this.h.g;
        float f2 = geo.aDialY;
        float f3 = f * 12.0f;
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(-34278);
        float f4 = 180.0f * f;
        float f5 = geo.aCx - f4;
        float f6 = geo.aCx + f4;
        if (!geo.land) {
            float f7 = f2 - (22.0f * f);
            float f8 = f * 8.0f;
            float f9 = f2 - (21.2f * f);
            canvas.drawRect(f5, f7, f5 + f8, f9, this.p);
            canvas.drawRect(f6 - f8, f7, f6, f9, this.p);
        }
        dial(canvas, geo.aDialLx, f2, f3, homeData.btPct / 100.0f, "B", -34278);
        float f10 = f2;
        Txt txt = this.raj13;
        txt.align(Paint.Align.LEFT);
        float f11 = 8.0f * f;
        float f12 = geo.aDialLx + f3 + f11;
        float f13 = 3.0f;
        float f14 = f10 - (f * 3.0f);
        txt.draw(canvas, homeData.btPct + "%", f12, f14, W);
        this.mono5.align(Paint.Align.LEFT);
        float f15 = f * 5.0f;
        float f16 = f10 + f15;
        this.mono5.draw(canvas, "BATTERY", f12, f16, DIM);
        float f17 = f10 + f3;
        this.mono5.draw(canvas, homeData.btPct + " / 100", f12, f17, DIM);
        long currentTimeMillis = System.currentTimeMillis();
        Iterator<HomeData.Ev> it = homeData.events.iterator();
        int i = 0;
        int i2 = 0;
        while (it.hasNext()) {
            HomeData.Ev next = it.next();
            int i3 = i;
            int i4 = i2;
            float f18 = f13;
            float f19 = f10;
            long j = currentTimeMillis;
            if (HomeRules.calDay(next.start, next.end, currentTimeMillis) == 0) {
                i = i3 + 1;
                i2 = next.end > j ? i4 + 1 : i4;
            } else {
                i2 = i4;
                i = i3;
            }
            f10 = f19;
            f13 = f18;
            currentTimeMillis = j;
        }
        long j2 = currentTimeMillis;
        float f20 = f10;
        float f21 = f13;
        int i5 = i;
        int i6 = i2;
        dial(canvas, geo.aDialRx, f20, f3, i5 > 0 ? i6 / i5 : 0.0f, "C", -11624262);
        txt.align(Paint.Align.RIGHT);
        float f22 = (geo.aDialRx - f3) - f11;
        txt.draw(canvas, homeData.calNeed ? "—" : String.valueOf(i6), f22, f14, W);
        this.mono5.align(Paint.Align.RIGHT);
        this.mono5.draw(canvas, "EVENTS LEFT", f22, f16, DIM);
        this.mono5.draw(canvas, i6 + " / " + i5, f22, f17, DIM);
        this.mono5.align(Paint.Align.LEFT);
        txt.align(Paint.Align.LEFT);
        if (geo.aMid.isEmpty()) {
            return;
        }
        RectF rectF = geo.aMid;
        float centerY = rectF.centerY();
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(Theme.rgba(10, 17, 20, 0.5f));
        float f23 = f * 2.0f;
        float f24 = 30.0f * f;
        this.rf.set(rectF.left, rectF.top + f23, rectF.left + f24, rectF.bottom);
        canvas.drawRect(this.rf, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(Math.max(0.6f, 0.7f * f));
        this.p.setColor(ln(0.12f));
        canvas.drawRect(this.rf, this.p);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(Theme.rgba(10, 17, 20, 0.5f));
        this.rf.set(rectF.right - f24, rectF.top + f23, rectF.right, rectF.bottom);
        canvas.drawRect(this.rf, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(ln(0.12f));
        canvas.drawRect(this.rf, this.p);
        float f25 = f * 40.0f;
        float f26 = rectF.left + f25;
        float f27 = f26 + f23;
        float f28 = f * 7.0f;
        this.mono5.draw(canvas, "WEATHER · NOW", f27, rectF.top + f28, DIM);
        float f29 = f * 1.0f;
        Icons.draw(canvas, this.p, homeData.wxIcon, f26 + f28, centerY + f29, f3, W, 1.1f);
        this.raj16.align(Paint.Align.LEFT);
        this.raj16.draw(canvas, homeData.wxTemp, f26 + (18.0f * f), (this.raj16.cap / 2.0f) + centerY + f29, W);
        String str = homeData.srcBy.get("weather");
        String substring = (str == null || !str.contains(" · ")) ? "" : str.substring(str.lastIndexOf(" · ") + 3);
        this.mono5.draw(canvas, WidgetView.fitText(this.mono5, (homeData.wxDesc + (substring.isEmpty() ? "" : " · " + substring)).toUpperCase(Locale.ROOT), (rectF.width() / f21) - f24), f27, rectF.bottom - f29, -11624262);
        boolean z = homeData.paMode;
        String upperCase = homeData.name != null ? homeData.name.toUpperCase(Locale.getDefault()) : "";
        String str2 = !z ? "ONLINE" : "OFFLINE";
        Txt txt2 = this.mono7;
        float w = txt2.w(upperCase);
        float centerX = (rectF.centerX() - ((((f11 + w) + f15) + this.mono6.w(str2)) / 2.0f)) + (10.0f * f);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(-34278);
        float f30 = 2.5f * f;
        canvas.drawCircle(centerX + f30, centerY, f30, this.p);
        txt2.align(Paint.Align.LEFT);
        float f31 = centerX + f11;
        txt2.draw(canvas, upperCase, f31, (txt2.cap / 2.0f) + centerY, W);
        this.mono6.align(Paint.Align.LEFT);
        this.mono6.draw(canvas, str2, f31 + w + f15, centerY + (this.mono6.cap / 2.0f), -34278);
        float f32 = rectF.right - f25;
        this.mono5.align(Paint.Align.RIGHT);
        this.mono5.draw(canvas, "NEXT ALARM", f32, rectF.top + f28, DIM);
        String format = homeData.alarmAt > 0 ? new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(homeData.alarmAt)) : "—";
        this.raj16.align(Paint.Align.RIGHT);
        this.raj16.draw(canvas, format, f32 - f23, centerY + (this.raj16.cap / 2.0f) + f29, W);
        this.raj16.align(Paint.Align.LEFT);
        this.mono5.draw(canvas, homeData.alarmAt > 0 ? "IN " + Corners.dur(homeData.alarmAt - j2) : "NONE SET", f32, rectF.bottom - f29, -34278);
        this.mono5.align(Paint.Align.LEFT);
    }

    void modes(Canvas canvas, float f) {
        Canvas canvas2 = canvas;
        Geo geo = this.h.g;
        String mode = Advanced.mode(filter(), ringer());
        if ("life".equals(mode)) {
            mode = "all";
        }
        String str = mode;
        float f2 = geo.land ? 1.0f : f;
        int i = 0;
        while (i < 6) {
            RectF rectF = geo.aMode[i];
            boolean equals = Geo.AOA_MODES[i].equals(str);
            this.p.setShader(null);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(FILL);
            canvas2.drawRect(rectF, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(0.6f, f2 * 0.8f));
            this.p.setColor(equals ? Theme.a(-11624262, 0.8f) : ln(0.16f));
            canvas2.drawRect(rectF, this.p);
            if (equals) {
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(-34278);
                canvas2.drawRect(rectF.left, rectF.bottom - (1.6f * f2), rectF.right, rectF.bottom, this.p);
            }
            Icons.draw(canvas, this.p, MODE_IC[i], rectF.centerX(), (rectF.height() * 0.36f) + rectF.top, f2 * 7.0f, equals ? W : MID, 1.0f);
            (geo.land ? new Txt(Fonts.MONO, 6.0f, 0.8f).align(Paint.Align.CENTER) : this.tileT).draw(canvas, Geo.AOA_MODES[i].toUpperCase(Locale.ROOT), rectF.centerX(), (rectF.height() * 0.8f) + rectF.top, equals ? W : MID);
            i++;
            canvas2 = canvas;
        }
    }

    int filter() {
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

    int ringer() {
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

    void tiles(Canvas canvas, HomeData homeData, float f) {
        float f2;
        float f3;
        int rgba;
        Canvas canvas2 = canvas;
        Geo geo = this.h.g;
        float min = geo.land ? Math.min(1.0f, geo.aTile[0].height() / 36.0f) : f;
        int i = 0;
        while (i < 9) {
            RectF rectF = geo.aTile[i];
            boolean z = i < this.tId.length && this.tId[i].equals(this.h.pressed);
            this.p.setShader(null);
            this.p.setStyle(Paint.Style.FILL);
            Paint paint = this.p;
            if (z) {
                f2 = 3.0f;
                f3 = 0.4f;
                rgba = Theme.rgba(255, 122, 26, 0.2f);
            } else {
                f2 = 3.0f;
                f3 = 0.4f;
                rgba = Theme.rgba(10, 17, 20, 0.6f);
            }
            paint.setColor(rgba);
            canvas2.drawRect(rectF, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(0.6f, 0.7f * min));
            this.p.setColor(ln(0.14f));
            canvas2.drawRect(rectF, this.p);
            int i2 = i + 1;
            float f4 = min * f2;
            this.tileN.draw(canvas2, String.format(Locale.ROOT, "%02d", Integer.valueOf(i2)), rectF.right - f4, rectF.top + f4 + this.tileN.cap, -34278);
            if (i < this.tLab.length) {
                Icons.draw(canvas, this.p, this.tIc[i], rectF.centerX(), (rectF.height() * f3) + rectF.top, min * 11.0f, W, 1.1f);
                this.tileT.draw(canvas, WidgetView.fitText(this.tileT, this.tLab[i], rectF.width() - (4.0f * min)), rectF.centerX(), (rectF.height() * 0.8f) + rectF.top, W);
            }
            canvas2 = canvas;
            i = i2;
        }
        RectF rectF2 = geo.aColL;
        RectF rectF3 = geo.aColR;
        float f5 = f * 6.0f;
        float f6 = rectF2.top + f5;
        Txt txt = new Txt(Fonts.MONO, 6.5f * min, min * 1.5f);
        txt.align(Paint.Align.LEFT);
        txt.draw(canvas, "LOCAL", rectF2.left, f6 + txt.cap, -11624262);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(ln(0.4f));
        canvas.drawRect(rectF2.left, (f * 3.0f) + txt.cap + f6, rectF2.right, (3.6f * f) + txt.cap + f6, this.p);
        String upperCase = homeData.netType.isEmpty() ? "OFFLINE" : homeData.netType.toUpperCase(Locale.ROOT);
        this.mono5.align(Paint.Align.LEFT);
        float f7 = f * 10.0f;
        this.mono5.draw(canvas, WidgetView.fitText(this.mono5, upperCase, rectF2.width()), rectF2.left, txt.cap + f6 + f7, MID);
        this.raj13.align(Paint.Align.LEFT);
        float f8 = f * 24.0f;
        this.raj13.draw(canvas, WidgetView.fitText(this.raj13, homeData.dataToday >= 0 ? (homeData.dataToday / 1048576) + " MB" : "—", rectF2.width()), rectF2.left, txt.cap + f6 + f8, W);
        this.p.setColor(-11624262);
        float f9 = 1.5f * f;
        float f10 = f * 31.0f;
        canvas.drawCircle(rectF2.left + f9, ((txt.cap + f6) + f10) - (this.mono5.cap / 2.0f), f9, this.p);
        this.mono5.draw(canvas, homeData.dataToday >= 0 ? "TODAY" : "IN USE", rectF2.left + f5, txt.cap + f6 + f10, -11624262);
        txt.align(Paint.Align.RIGHT);
        txt.draw(canvas, "STORAGE", rectF3.right, f6 + txt.cap, -11624262);
        this.mono5.align(Paint.Align.RIGHT);
        this.mono5.draw(canvas, homeData.stTotal > 0 ? StorageWidget.gb(homeData.stTotal) : "—", rectF3.right, txt.cap + f6 + f7, MID);
        this.raj13.align(Paint.Align.RIGHT);
        this.raj13.draw(canvas, homeData.stTotal > 0 ? Math.round((1.0f - (((float) homeData.stFree) / ((float) homeData.stTotal))) * 100.0f) + "%" : "—", rectF3.right, txt.cap + f6 + f8, W);
        String str = homeData.stTotal > 0 ? StorageWidget.gb(homeData.stFree) + " FREE" : "";
        this.mono5.draw(canvas, str, rectF3.right, txt.cap + f6 + f10, -11624262);
        this.p.setColor(-11624262);
        if (!str.isEmpty()) {
            canvas.drawCircle((rectF3.right - this.mono5.w(str)) - (f * 4.0f), ((f6 + txt.cap) + f10) - (this.mono5.cap / 2.0f), f9, this.p);
        }
        this.mono5.align(Paint.Align.LEFT);
        this.raj13.align(Paint.Align.LEFT);
    }

    String dockLab(int i) {
        HomeData homeData = this.h.d;
        if (i == 2) {
            return "APPS";
        }
        if (i == 4) {
            return homeData.name == null ? "" : homeData.name.toUpperCase(Locale.getDefault());
        }
        if (i >= 2) {
            i = 3;
        }
        if (i >= homeData.dock.size()) {
            i = homeData.dock.size() - 1;
        }
        return i < 0 ? "" : homeData.dock.get(i).label;
    }

    String dockIc(int i) {
        HomeData homeData = this.h.d;
        if (i == 2) {
            return "grid";
        }
        if (i == 4) {
            return "spark";
        }
        if (i >= 2) {
            i = 3;
        }
        if (i >= homeData.dock.size()) {
            i = homeData.dock.size() - 1;
        }
        return i < 0 ? "" : homeData.dock.get(i).icon;
    }

    void dock(Canvas canvas, HomeData homeData, float f) {
        float f2;
        int i;
        AoaLayer aoaLayer;
        float f3;
        float f4;
        int i2;
        Canvas canvas2 = canvas;
        Geo geo = this.h.g;
        float f5 = f * 0.864f;
        int i3 = -34278;
        int i4 = 2;
        if (!geo.land) {
            float f6 = geo.dX;
            float f7 = geo.dY;
            float f8 = geo.dTop;
            int save = canvas2.save();
            float f9 = f * 180.0f;
            canvas2.clipRect((geo.aCx - f9) - 1.0f, f8, geo.aCx + f9 + 1.0f, geo.LH);
            float f10 = 0.8f * f;
            arc(canvas2, f6, f7, 179.0f * f5, 0.0f, 180.0f, ln(0.22f), Math.max(0.6f, f10));
            arc(canvas, f6, f7, f5 * 108.0f, 0.0f, 180.0f, ln(0.12f), Math.max(0.6f, f10));
            float degrees = (float) Math.toDegrees(AoaGeo.vis(96.768005f, (f8 / f) - (f7 / f)));
            Canvas canvas3 = canvas;
            AoaLayer aoaLayer2 = this;
            int i5 = 2;
            aoaLayer2.labArc(canvas3, f6, f7, f5 * 112.0f, degrees, 180.0f - degrees, new String[]{"QUICK LAUNCH"}, new float[]{90.0f}, ln(0.22f), W);
            int i6 = 0;
            for (int i7 = 5; i6 < i7; i7 = 5) {
                if (geo.aDk[i6] == null) {
                    aoaLayer = aoaLayer2;
                    f3 = f6;
                    f4 = f7;
                    i2 = i5;
                } else {
                    float f11 = geo.dkA[i6][i5];
                    int i8 = i5;
                    boolean equals = (i6 < i8 ? "d" + i6 : i6 == i8 ? "apps" : i6 == 3 ? "d3" : "ai").equals(aoaLayer2.h.pressed);
                    aoaLayer2.wedge(canvas3, geo.aDk[i6], equals ? Theme.rgba(255, 122, 26, 0.3f) : Theme.rgba(16, 20, 26, 0.35f), equals ? -34278 : ln(0.22f));
                    float f12 = 4.0f * f5;
                    aoaLayer2.arcText(canvas3, f6, f7, ((geo.aDOut[0] + geo.aDOut[1]) / 2.0f) - f12, f11, aoaLayer2.dockLab(i6), aoaLayer2.raj9, W);
                    aoaLayer = aoaLayer2;
                    f3 = f6;
                    float f13 = f7;
                    Shapes.pt(f3, f13, geo.aDOut[1] - (7.0f * f5), f11, aoaLayer.q);
                    f4 = f13;
                    Icons.draw(canvas, aoaLayer.p, aoaLayer.dockIc(i6), aoaLayer.q[0], aoaLayer.q[1], 8.0f * f, DIM, 1.0f);
                    canvas3 = canvas;
                    i2 = 2;
                    int i9 = (i6 >= 2 || i6 >= homeData.dockCnt.length) ? 0 : homeData.dockCnt[i6];
                    if (i9 > 0) {
                        Shapes.pt(f3, f4, geo.aDOut[1] - f12, geo.dkA[i6][0] + 3.0f, aoaLayer.q);
                        aoaLayer.badge(canvas3, aoaLayer.q[0], aoaLayer.q[1], i9);
                    }
                }
                i6++;
                i5 = i2;
                f6 = f3;
                aoaLayer2 = aoaLayer;
                f7 = f4;
            }
            AoaLayer aoaLayer3 = aoaLayer2;
            float f14 = f7;
            if (geo.aSearch != null) {
                aoaLayer3.wedge(canvas3, geo.aSearch, Theme.rgba(16, 20, 26, 0.35f), ln(0.22f));
                i = 3;
                aoaLayer3.arcText(canvas3, f6, f14, ((geo.aDIn[0] + geo.aDIn[1]) / 2.0f) - (2.0f * f5), 90.0f, "SEARCH", aoaLayer3.raj9, W);
                f2 = f6;
                Shapes.pt(f2, f14, geo.aDIn[1] - (f5 * 7.0f), 90.0f, aoaLayer3.q);
                Icons.draw(canvas, aoaLayer3.p, "list", aoaLayer3.q[0], aoaLayer3.q[1], f * 7.0f, DIM, 1.0f);
            } else {
                f2 = f6;
                i = 3;
            }
            aoaLayer3.p.setStyle(Paint.Style.STROKE);
            aoaLayer3.p.setStrokeWidth(1.4f * f);
            aoaLayer3.p.setColor(-34278);
            aoaLayer3.rf.set(f2 - geo.aPtt, f14 - geo.aPtt, geo.aPtt + f2, geo.aPtt + f14);
            canvas.drawArc(aoaLayer3.rf, 0.0f, 180.0f, false, aoaLayer3.p);
            float f15 = f8 + (10.0f * f);
            aoaLayer3.p.setStyle(Paint.Style.FILL);
            aoaLayer3.p.setColor(Theme.rgba(i, 5, 6, 0.9f));
            float f16 = f * 9.0f;
            canvas.drawCircle(f2, f15, f16, aoaLayer3.p);
            aoaLayer3.p.setStyle(Paint.Style.STROKE);
            aoaLayer3.p.setStrokeWidth(Math.max(0.6f, f10));
            aoaLayer3.p.setColor(ln(0.6f));
            canvas.drawCircle(f2, f15, f16, aoaLayer3.p);
            aoaLayer3.p.setColor(W);
            float f17 = 2.2f * f;
            aoaLayer3.rf.set(f2 - f17, f15 - (5.0f * f), f2 + f17, (1.5f * f) + f15);
            canvas.drawRoundRect(aoaLayer3.rf, f17, f17, aoaLayer3.p);
            float f18 = 4.0f * f;
            float f19 = f15 + (3.5f * f);
            aoaLayer3.rf.set(f2 - f18, f15 - (f * 3.0f), f2 + f18, f19);
            canvas.drawArc(aoaLayer3.rf, 0.0f, 180.0f, false, aoaLayer3.p);
            float f20 = f2;
            canvas.drawLine(f20, f19, f2, f15 + (5.5f * f), aoaLayer3.p);
            aoaLayer3.lab.draw(canvas, "HOLD TO TALK", f20, f15 + (18.0f * f), MID);
            aoaLayer3.lab.draw(canvas, homeData.name == null ? "" : homeData.name.toUpperCase(Locale.getDefault()), f20, f15 + (25.0f * f), MID);
            canvas.restoreToCount(save);
            float f21 = homeData.stTotal > 0 ? 1.0f - (((float) homeData.stFree) / ((float) homeData.stTotal)) : 0.0f;
            float f22 = f * 11.0f;
            aoaLayer3.dial(canvas, geo.aDockDialLx, geo.aDockDialY, f22, f21, "S", -34278);
            dial(canvas, geo.aDockDialRx, geo.aDockDialY, f22, Math.min(1.0f, (homeData.notifs == null ? 0 : homeData.notifs.length()) / 20.0f), "U", -11624262);
            int length = homeData.notifs == null ? 0 : homeData.notifs.length();
            this.raj13.align(Paint.Align.RIGHT);
            float f23 = f * 16.0f;
            float f24 = geo.aDockDialRx - f23;
            float f25 = geo.aDockDialY - (24.0f * f);
            this.raj13.draw(canvas, String.valueOf(length), f24, f25, W);
            this.mono6.align(Paint.Align.RIGHT);
            float f26 = f25 + f16;
            this.mono6.draw(canvas, "UNREAD", f24, f26, DIM);
            String unreadApps = BackLayer.unreadApps(homeData);
            int indexOf = unreadApps.indexOf(" · ");
            if (indexOf > 0) {
                unreadApps = unreadApps.substring(0, indexOf);
            }
            this.mono6.draw(canvas, WidgetView.fitText(this.mono6, unreadApps, 56.0f * f), f24, f25 + (17.0f * f), DIM);
            this.mono6.align(Paint.Align.LEFT);
            this.raj13.align(Paint.Align.LEFT);
            this.raj13.draw(canvas, homeData.stTotal > 0 ? Math.round(f21 * 100.0f) + "%" : "—", geo.aDockDialLx + f23, f25, W);
            this.mono6.draw(canvas, "STORAGE", geo.aDockDialLx + f23, f26, DIM);
            return;
        }
        int i10 = 0;
        while (i10 < 5) {
            RectF rectF = geo.aDkR[i10];
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(FILL);
            canvas2.drawRect(rectF, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(0.8f);
            this.p.setColor(i10 == i4 ? i3 : ln(0.22f));
            canvas2.drawRect(rectF, this.p);
            Paint paint = this.p;
            int i11 = i3;
            String dockIc = dockIc(i10);
            float f27 = rectF.left + 12.0f;
            int i12 = i11;
            float centerY = rectF.centerY();
            if (i10 != i4) {
                i12 = W;
            }
            Icons.draw(canvas2, paint, dockIc, f27, centerY, 10.0f, i12, 1.1f);
            Txt txt = new Txt(Fonts.RAJ_600, 9.0f, 1.2f);
            txt.draw(canvas, WidgetView.fitText(txt, dockLab(i10), rectF.width() - 26.0f), 22.0f + rectF.left, (txt.cap / 2.0f) + rectF.centerY(), W);
            canvas2 = canvas;
            i10++;
            i4 = i4;
            i3 = -34278;
        }
    }
}
