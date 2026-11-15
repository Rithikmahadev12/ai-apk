package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Strings;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
final class SleepWidget extends WidgetView {
    static final int MOON = -6302731;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SleepWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "sleep";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        String str = this.h.d.srcBy.get("health");
        return str == null ? "HEALTH · NOT CONNECTED" : str;
    }

    JSONObject sl() {
        JSONObject optJSONObject = this.h.d.health.optJSONObject("sleep");
        if (optJSONObject == null || !optJSONObject.has("min") || optJSONObject.isNull("min")) {
            return null;
        }
        return optJSONObject;
    }

    boolean bar() {
        JSONObject sl = sl();
        return sl != null && sl.optLong("start") > 0 && sl.optLong("end") > 0;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        JSONObject sl = sl();
        if (sl == null) {
            return null;
        }
        return HomeData.fmtMin(Double.valueOf(sl.optDouble("min")));
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return (sl() != null && bar()) ? 3 : 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : (sl() != null && i == 1) ? heroRingH() : this.m9.line();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        JSONObject sl = sl();
        double optDouble = this.h.d.goals().optDouble("sleep", 8.0d);
        if (i == 0) {
            head(canvas, f, f2, f3, "moon", -6302731, Strings.t("w.sleep.h"), (sl == null || !sl.has("q") || sl.isNull("q")) ? null : sl.optInt("q") + "%");
            return;
        }
        if (sl == null) {
            msg(canvas, f, f2, f3, f4, Strings.t((this.h.d.healthOff || this.h.d.health == null || !this.h.d.health.optBoolean("ok")) ? "w.sleep.none" : "w.health.nosleep"), "x:health", th().DIM);
            return;
        }
        float min = (float) Math.min(1.0d, sl.optDouble("min") / (60.0d * optDouble));
        if (i == 1) {
            heroRing(canvas, f, f2, f4, min, -6302731, "moon", HomeData.fmtMin(Double.valueOf(sl.optDouble("min"))), Math.round(100.0f * min) + "% OF " + fmtGoal(optDouble) + "H GOAL", th().DIM);
            return;
        }
        float f5 = f2 + (f4 / 2.0f);
        String hm = HomeData.hm(sl.optLong("start") * 1000);
        String hm2 = HomeData.hm(sl.optLong("end") * 1000);
        Icons.draw(canvas, this.p, "moon", f + 4.5f, f5, 9.0f, col(th().DIM));
        float f6 = f + 12.0f;
        this.m9.draw(canvas, hm, f6, f5 + (this.m9.cap / 2.0f), col(th().DIM));
        float w = this.m9.w(hm2);
        float w2 = f6 + this.m9.w(hm) + 8.0f;
        float f7 = f + f3;
        float f8 = f7 - w;
        Icons.draw(canvas, this.p, "sun", f8 - 7.5f, f5, 9.0f, col(th().DIM));
        this.m9.draw(canvas, hm2, f8, f5 + (this.m9.cap / 2.0f), col(th().DIM));
        Hud.bar(canvas, this.p, this.rf, w2, f5, (((f7 - 12.0f) - w) - 8.0f) - w2, min, th(), this.alpha);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String fmtGoal(double d) {
        return d == Math.floor(d) ? String.valueOf((long) d) : String.valueOf(d);
    }
}
