package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.HealthPick;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeData;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: /tmp/augmaos/classes.dex */
final class GreetingWidget extends WidgetView {
    static final float LABEL_GAP = 8.0f;
    float askH;
    float askY;
    final ArrayList<Object[]> brL;
    Txt greet;
    Txt greetL;
    Txt hlB;
    Txt hlB2;
    Txt nx;
    final ArrayList<Object[]> nxL;
    Txt nxRed;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GreetingWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.nxL = new ArrayList<>();
        this.brL = new ArrayList<>();
        this.animRows = false;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "greeting";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        String str = this.h.d.srcBy.get("greeting");
        return str == null ? "DEVICE CALENDAR" : str;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 0;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return 0.0f;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void styles() {
        super.styles();
        float f = this.h.zoom;
        float f2 = 20.0f * f;
        this.greet = new Txt(Fonts.RAJ_400, f2, f2 * 0.06f);
        this.nx = new Txt(Fonts.RAJ_600, 14.0f * f, 0.0f);
        this.hlB = new Txt(Fonts.RAJ_600, 15.0f * f, 0.0f).align(Paint.Align.RIGHT);
        float f3 = ("arc".equals(this.h.g.mode) ? 17.0f : 18.0f) * f;
        this.greetL = new Txt(Fonts.RAJ_400, f3, 0.06f * f3);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void bind() {
        HomeData homeData = this.h.d;
        this.nxL.clear();
        this.brL.clear();
        float max = this.h.g.land ? Math.max(60.0f, this.W - 28.0f) : 172.0f;
        boolean z = this.h.zoom > 1.1f;
        wrap(this.nx, new String[]{homeData.nextPre, homeData.nextRed, homeData.nextPost}, new int[]{0, 1, 0}, max, this.nxL, (this.h.g.land || z) ? 2 : 3);
        String str = homeData.brief.isEmpty() ? homeData.sleep : homeData.brief;
        if (!str.isEmpty()) {
            wrap(this.m9, new String[]{str}, new int[]{0}, max, this.brL, z ? 1 : this.h.g.land ? 2 : 3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void wrap(Txt txt, String[] strArr, int[] iArr, float f, ArrayList<Object[]> arrayList, int i) {
        ArrayList arrayList2 = new ArrayList();
        float f2 = 0.0f;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (str != null && !str.isEmpty()) {
                for (String str2 : str.split("(?<= )")) {
                    float w = txt.w(str2);
                    if (f2 > 0.0f && txt.w(str2.trim()) + f2 > f) {
                        arrayList.add(arrayList2.toArray());
                        if (arrayList.size() >= i) {
                            return;
                        }
                        arrayList2 = new ArrayList();
                        str2 = str2.replaceFirst("^ +", "");
                        w = txt.w(str2);
                        f2 = 0.0f;
                    }
                    arrayList2.add(new Object[]{str2, Integer.valueOf(iArr[i2])});
                    f2 += w;
                }
            }
        }
        if (!arrayList2.isEmpty()) {
            arrayList.add(arrayList2.toArray());
        }
    }

    void drawLand(Canvas canvas, HomeData homeData, Theme theme) {
        Theme theme2 = theme;
        float f = this.W - 28.0f;
        this.m9.draw(canvas, fitText(this.m9, homeData.sub, f), 14.0f, this.m9.base(6.0f), theme2.DIM);
        float line = this.m9.line() + 6.0f;
        this.greetL.draw(canvas, fitText(this.greetL, homeData.greetName(), f), 14.0f, this.greetL.base(line), theme2.INK);
        float line2 = line + this.greetL.line() + 10.0f;
        Iterator<Object[]> it = this.nxL.iterator();
        while (it.hasNext()) {
            float f2 = 14.0f;
            for (Object obj : it.next()) {
                Object[] objArr = (Object[]) obj;
                String str = (String) objArr[0];
                this.nx.draw(canvas, str, f2, this.nx.base(line2), ((Integer) objArr[1]).intValue() == 1 ? theme2.RED : theme2.INK);
                f2 += this.nx.w(str);
            }
            line2 += this.nx.line();
        }
        float f3 = line2 + 4.0f;
        Iterator<Object[]> it2 = this.brL.iterator();
        while (it2.hasNext()) {
            float f4 = 14.0f;
            for (Object obj2 : it2.next()) {
                String str2 = (String) ((Object[]) obj2)[0];
                this.m9.draw(canvas, str2, f4, this.m9.base(f3), theme2.DIM);
                f4 += this.m9.w(str2);
            }
            f3 += this.m9.line();
        }
        if ("arc".equals(this.h.g.mode)) {
            float f5 = f3 + 10.0f;
            float f6 = (f - 12.0f) / 2.0f;
            float line3 = this.hlB.line() + 4.0f;
            Iterator<HomeData.Metric> it3 = homeData.hm.iterator();
            float f7 = 0.0f;
            while (it3.hasNext()) {
                f7 = Math.max(f7, this.m9.w(it3.next().label));
            }
            float min = Math.min(f7, 0.55f * f6);
            int i = 0;
            while (i < homeData.hm.size()) {
                HomeData.Metric metric = homeData.hm.get(i);
                float f8 = 14.0f + ((i % 2) * (f6 + 12.0f));
                float f9 = f5 + ((i / 2) * (line3 + 1.0f));
                float f10 = f9 + 2.0f + this.hlB.asc;
                Icons.draw(canvas, this.p, metric.icon, f8 + 5.5f, f10 - (this.m9.cap / 2.0f), 10.0f, metric.color);
                float f11 = f8 + 15.0f;
                this.m9.draw(canvas, fitText(this.m9, metric.label, min), f11, f10, theme2.DIM);
                cellValue(canvas, metric, f11 + min + LABEL_GAP, f8 + f6, f10, theme2);
                theme2 = theme;
                Hud.bar(canvas, this.p, this.rf, f8, f9 + line3 + 0.5f, f6, metric.frac, theme2, this.alpha);
                i++;
                min = min;
            }
        }
    }

    void cellValue(Canvas canvas, HomeData.Metric metric, float f, float f2, float f3, Theme theme) {
        boolean equals = metric.value.equals(Strings.t("w.health.nodata"));
        Txt txt = equals ? this.m9 : this.hlB;
        String str = metric.value;
        float f4 = f2 - f;
        float valueScale = HealthPick.valueScale(f4, 0.0f, txt.w(str), 4.0f);
        if (valueScale == 0.0f) {
            txt = this.hlB;
            str = "—";
            valueScale = HealthPick.valueScale(f4, 0.0f, txt.w("—"), 4.0f);
            if (valueScale == 0.0f) {
                return;
            }
        }
        String str2 = str;
        Txt txt2 = txt;
        float w = txt2 == this.hlB ? f2 : f2 - txt2.w(str2);
        int i = (equals && txt2 == this.m9) ? theme.DIM : theme.INK;
        if (valueScale >= 1.0f) {
            txt2.draw(canvas, str2, w, f3, i);
            return;
        }
        canvas.save();
        canvas.scale(valueScale, 1.0f, f2, f3);
        txt2.draw(canvas, str2, w, f3, i);
        canvas.restore();
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x0370  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x03f7 A[LOOP:5: B:54:0x03f1->B:56:0x03f7, LOOP_END] */
    @Override // com.jarvis.assist.aosui.home.WidgetView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onDraw(android.graphics.Canvas r27) {
        /*
            Method dump skipped, instructions count: 1134
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.GreetingWidget.onDraw(android.graphics.Canvas):void");
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String spoken() {
        HomeData homeData = this.h.d;
        StringBuilder sb = new StringBuilder();
        String[] strArr = {homeData.sub, homeData.greet, (homeData.nextPre + homeData.nextRed + homeData.nextPost).trim(), homeData.brief.isEmpty() ? homeData.sleep : homeData.brief};
        for (int i = 0; i < 4; i++) {
            String str = strArr[i];
            if (str != null && !str.trim().isEmpty()) {
                sb.append(str.trim()).append(". ");
            }
        }
        sb.append("Health: ");
        Iterator<HomeData.Metric> it = homeData.hm.iterator();
        while (it.hasNext()) {
            HomeData.Metric next = it.next();
            sb.append(next.label).append(" ").append(next.value.equals("—") ? this.h.d.healthOff ? "not connected" : Strings.t("w.health.nodata") : next.value).append(", ");
        }
        return sb.toString().replaceAll(", $", ".").trim();
    }

    static String askLabel(Txt txt, String str, float f) {
        String trim = str == null ? "" : str.trim();
        String str2 = "ASK " + trim;
        if (txt.w(str2) <= f) {
            return str2;
        }
        String[] split = trim.split("\\s+");
        if (split.length > 1) {
            String[] strArr = {split[split.length - 1], split[0]};
            for (int i = 0; i < 2; i++) {
                String str3 = "ASK " + strArr[i];
                if (txt.w(str3) <= f) {
                    return str3;
                }
            }
        }
        return fitText(txt, str2, f);
    }
}
