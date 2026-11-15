package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Strings;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
final class ScreenTimeWidget extends WidgetView {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ScreenTimeWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "screentime";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "USAGE STATS · TODAY";
    }

    boolean ok() {
        return this.h.d.screen != null && this.h.d.screen.optBoolean("ok");
    }

    JSONArray top() {
        JSONArray optJSONArray = ok() ? this.h.d.screen.optJSONArray("top") : null;
        return optJSONArray == null ? new JSONArray() : optJSONArray;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        if (ok()) {
            return 2 + Math.min(this.h.g.ph < 140.0f ? 2 : 3, top().length());
        }
        return 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        if (i == 0) {
            return headH();
        }
        return ((ok() && i == 1) ? this.hero : this.m9).line();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        if (i == 0) {
            head(canvas, f, f2, f3, "clock", th().BLUE, Strings.t("w.screentime.h"), null);
            return;
        }
        if (!ok()) {
            msg(canvas, f, f2, f3, f4, Strings.t("w.screentime.perm"), "x:usage", th().DIM);
            return;
        }
        long optLong = this.h.d.screen.optLong("total");
        if (i == 1) {
            String dur = HomeRules.dur(optLong);
            this.hero.draw(canvas, dur, f, f2 + this.hero.asc, col(th().INK));
            this.m9.draw(canvas, Strings.t("w.screentime.today"), 10.0f + f + this.hero.w(dur), f2 + this.hero.asc, col(th().DIM));
            return;
        }
        JSONObject optJSONObject = top().optJSONObject(i - 2);
        if (optJSONObject == null) {
            return;
        }
        float f5 = f2 + (f4 / 2.0f);
        float optLong2 = optLong > 0 ? ((float) optJSONObject.optLong("ms")) / ((float) optLong) : 0.0f;
        float f6 = f3 * 0.34f;
        String dur2 = HomeRules.dur(optJSONObject.optLong("ms"));
        this.m9.draw(canvas, fitText(this.m9, optJSONObject.optString("label").toUpperCase(Locale.ROOT), f6), f, f5 + (this.m9.cap / 2.0f), col(th().DIM));
        float f7 = f + f6 + 8.0f;
        float w = (f + f3) - this.m9.w(dur2);
        float f8 = w - 8.0f;
        this.m9.draw(canvas, dur2, w, f5 + (this.m9.cap / 2.0f), col(th().DIM));
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(col(th().LINE2));
        float f9 = f5 - 1.5f;
        float f10 = f5 + 1.5f;
        this.rf.set(f7, f9, f8, f10);
        canvas.drawRoundRect(this.rf, 2.0f, 2.0f, this.p);
        this.p.setColor(col(th().BLUE));
        this.rf.set(f7, f9, ((f8 - f7) * Math.min(1.0f, optLong2)) + f7, f10);
        canvas.drawRoundRect(this.rf, 2.0f, 2.0f, this.p);
    }
}
