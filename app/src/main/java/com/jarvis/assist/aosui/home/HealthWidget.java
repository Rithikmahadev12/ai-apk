package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
final class HealthWidget extends WidgetView {
    /* JADX INFO: Access modifiers changed from: package-private */
    public HealthWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.flexFirst = true;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "health";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return this.h.d.srcBy.containsKey("health") ? this.h.d.srcBy.get("health") : "HEALTH · NOT CONNECTED";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        HomeData.Metric metric = this.h.d.hm.isEmpty() ? null : this.h.d.hm.get(0);
        if (metric == null || metric.value.equals("—") || metric.value.equals(Strings.t("w.health.nodata"))) {
            return this.h.d.healthOff ? "OFFLINE" : "—";
        }
        return metric.value + " BPM";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return (this.h.d.healthOff || !this.h.d.healthHint.isEmpty()) ? 3 : 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : i == 1 ? ringSz() + 2.0f + this.sv15.line() + 2.0f + this.m9.line() : this.m9.line();
    }

    List<HomeData.Metric> four() {
        ArrayList arrayList = new ArrayList();
        ArrayList<HomeData.Metric> arrayList2 = this.h.d.hm;
        if (arrayList2.size() < 4) {
            return arrayList2;
        }
        JSONObject optJSONObject = this.h.d.health == null ? null : this.h.d.health.optJSONObject("sleep");
        double optDouble = this.h.d.goals().optDouble("sleep", 8.0d);
        boolean z = (optJSONObject == null || !optJSONObject.has("min") || optJSONObject.isNull("min")) ? false : true;
        arrayList.add(arrayList2.get(0));
        arrayList.add(arrayList2.get(3));
        arrayList.add(new HomeData.Metric("moon", "SLEEP", z ? HomeData.fmtMin(Double.valueOf(optJSONObject.optDouble("min"))) : "—", Theme.HM_HRV, z ? (float) Math.min(1.0d, optJSONObject.optDouble("min") / (optDouble * 60.0d)) : 0.0f));
        arrayList.add(arrayList2.get(1));
        return arrayList;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        if (i == 0) {
            head(canvas, f, f2, f3, "heart", th().RED, Strings.t("w.health.h"), null);
            return;
        }
        if (i == 2) {
            String t = this.h.d.healthOff ? Strings.t("w.health.connect") : this.h.d.healthHint;
            this.m9.draw(canvas, fitText(this.m9, t, f3), f, this.m9.base(f2), col(this.h.d.healthOff ? th().BLUE : th().DIM));
            hit(f, f2, f + f3, f2 + f4, "x:health", t);
            return;
        }
        HealthWidget healthWidget = this;
        float ringSz = healthWidget.ringSz();
        float f5 = 2.0f;
        float line = f2 + ((f4 - ((((ringSz + 2.0f) + healthWidget.sv15.line()) + 2.0f) + healthWidget.m9.line())) / 2.0f);
        List<HomeData.Metric> four = healthWidget.four();
        int size = four.size();
        float[] fArr = new float[size];
        float f6 = 0.0f;
        for (int i2 = 0; i2 < size; i2++) {
            HomeData.Metric metric = four.get(i2);
            fArr[i2] = Math.max(ringSz, Math.max((metric.value.equals(Strings.t("w.health.nodata")) ? healthWidget.m9 : healthWidget.sv15).w(metric.value), healthWidget.m9.w(metric.label)));
            f6 += fArr[i2];
        }
        if (f6 > f3 && size > 0) {
            for (int i3 = 0; i3 < size; i3++) {
                fArr[i3] = f3 / size;
            }
            f6 = f3;
        }
        float f7 = size > 1 ? (f3 - f6) / (size - 1) : 0.0f;
        float f8 = f;
        int i4 = 0;
        while (i4 < size) {
            HomeData.Metric metric2 = four.get(i4);
            float f9 = (fArr[i4] / f5) + f8;
            float f10 = f5;
            int i5 = i4;
            healthWidget.ringI(canvas, f9, (ringSz / f5) + line, ringSz, metric2.frac, metric2.color, metric2.icon);
            HealthWidget healthWidget2 = healthWidget;
            float f11 = ringSz;
            boolean equals = metric2.value.equals(Strings.t("w.health.nodata"));
            Txt txt = equals ? healthWidget2.m9 : healthWidget2.sv15;
            float f12 = line + f11 + f10;
            float f13 = f12 + healthWidget2.sv15.asc;
            txt.align(Paint.Align.CENTER);
            squeezed(canvas, txt, metric2.value, f9, f13, fArr[i5] - f10, healthWidget2.col(equals ? healthWidget2.th().DIM : healthWidget2.th().INK));
            txt.align(Paint.Align.LEFT);
            healthWidget2.m9.align(Paint.Align.CENTER);
            squeezed(canvas, healthWidget2.m9, metric2.label, f9, f12 + healthWidget2.sv15.line() + f10 + healthWidget2.m9.asc, fArr[i5] - f10, healthWidget2.col(healthWidget2.th().DIM));
            healthWidget2.m9.align(Paint.Align.LEFT);
            f8 += fArr[i5] + f7;
            i4 = i5 + 1;
            f5 = f10;
            healthWidget = healthWidget2;
            ringSz = f11;
        }
    }

    static void squeezed(Canvas canvas, Txt txt, String str, float f, float f2, float f3, int i) {
        float w = txt.w(str);
        boolean z = w > f3 && f3 > 0.0f;
        if (z) {
            canvas.save();
            canvas.scale(f3 / w, 1.0f, f, f2);
        }
        txt.draw(canvas, str, f, f2, i);
        if (z) {
            canvas.restore();
        }
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String spoken() {
        String str;
        StringBuilder append = new StringBuilder(Strings.t("w.health.h")).append(". ");
        Iterator<HomeData.Metric> it = this.h.d.hm.iterator();
        while (it.hasNext()) {
            HomeData.Metric next = it.next();
            append.append(next.label).append(" ").append(next.value.equals("—") ? this.h.d.healthOff ? "not connected" : Strings.t("w.health.nodata") : next.value).append(". ");
        }
        if (!this.h.d.healthOff) {
            if (!this.h.d.healthHint.isEmpty()) {
                str = this.h.d.healthHint;
            }
            return append.toString().trim();
        }
        str = Strings.t("w.health.connect");
        append.append(str).append(".");
        return append.toString().trim();
    }
}
