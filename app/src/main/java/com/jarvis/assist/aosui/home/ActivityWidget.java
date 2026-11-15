package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeData;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
final class ActivityWidget extends WidgetView {
    final ArrayList<HomeData.Metric> acts;
    String right;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.acts = new ArrayList<>();
        this.flexFirst = true;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "activity";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        String str = this.h.d.srcBy.get("health");
        return str == null ? "HEALTH · NOT CONNECTED" : str;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void bind() {
        String format;
        double d;
        double d2;
        float f;
        String str;
        JSONObject jSONObject = this.h.d.health;
        JSONObject goals = this.h.d.goals();
        this.acts.clear();
        Integer num = HomeData.num(jSONObject, "steps");
        Integer num2 = HomeData.num(jSONObject, "kcal");
        Integer num3 = HomeData.num(jSONObject, "recovery");
        Integer num4 = HomeData.num(jSONObject, "readiness");
        if (num3 != null) {
            num4 = num3;
        }
        double optDouble = goals.optDouble("steps", 8000.0d);
        double optDouble2 = goals.optDouble("kcal", 500.0d);
        ArrayList<HomeData.Metric> arrayList = this.acts;
        if (num == null) {
            format = "—";
        } else {
            format = num.intValue() >= 1000 ? String.format(Locale.US, "%.1fK", Float.valueOf(num.intValue() / 1000.0f)) : new StringBuilder().append(num).toString();
        }
        int i = th().RED;
        if (num == null || num.intValue() == 0) {
            d = optDouble2;
            d2 = 1.0d;
            f = 0.0f;
        } else {
            d = optDouble2;
            d2 = 1.0d;
            f = (float) Math.min(1.0d, num.intValue() / optDouble);
        }
        arrayList.add(new HomeData.Metric("flame", "STEPS", format, i, f));
        this.acts.add(new HomeData.Metric("spark", "ACTIVE KCAL", num2 != null ? new StringBuilder().append(num2).toString() : "—", -1774607, (num2 == null || num2.intValue() == 0) ? 0.0f : (float) Math.min(d2, num2.intValue() / d)));
        if (num4 != null) {
            this.acts.add(new HomeData.Metric("pulse", num3 != null ? "RECOVERY" : "READINESS", num4 + "%", Theme.HM_HRV, num4.intValue() / 100.0f));
        }
        if (jSONObject.opt("strain") instanceof Number) {
            double optDouble3 = jSONObject.optDouble("strain");
            this.acts.add(new HomeData.Metric("rot", "STRAIN", String.format(Locale.US, "%.1f", Double.valueOf(optDouble3)), th().BLUE, (float) Math.min(d2, optDouble3 / 21.0d)));
        }
        if (num != null) {
            str = "GOAL " + (optDouble >= 1000.0d ? SleepWidget.fmtGoal(optDouble / 1000.0d) + "K" : SleepWidget.fmtGoal(optDouble));
        } else {
            str = null;
        }
        this.right = str;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        if (this.acts.isEmpty() || this.acts.get(0).value.equals("—")) {
            return null;
        }
        return this.acts.get(0).value + " STEPS";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : ringSz() + 2.0f + this.sv15.line() + 2.0f + this.m9.line();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        if (i == 0) {
            head(canvas, f, f2, f3, "flame", th().RED, Strings.t("w.activity.h"), this.right);
        } else {
            rings(canvas, this.acts, f, f2, f3, f4, true);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0078 A[LOOP:1: B:15:0x0076->B:16:0x0078, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void rings(android.graphics.Canvas r24, java.util.ArrayList<com.jarvis.assist.aosui.home.HomeData.Metric> r25, float r26, float r27, float r28, float r29, boolean r30) {
        /*
            Method dump skipped, instructions count: 237
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.ActivityWidget.rings(android.graphics.Canvas, java.util.ArrayList, float, float, float, float, boolean):void");
    }
}
