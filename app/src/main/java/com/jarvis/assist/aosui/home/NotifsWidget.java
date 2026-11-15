package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
final class NotifsWidget extends WidgetView {
    Txt dg;
    final ArrayList<Object[]> dgL;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NotifsWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.dgL = new ArrayList<>();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "notifs";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "NOTIFICATION SHADE";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void styles() {
        super.styles();
        this.dg = new Txt(Fonts.RAJ_600, this.h.zoom * 11.0f, 0.0f);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void bind() {
        this.dgL.clear();
        if (this.h.d.digest.isEmpty()) {
            return;
        }
        for (String str : this.h.d.digest.split("\n")) {
            GreetingWidget.wrap(this.dg, new String[]{str}, new int[]{0}, Math.max(40.0f, (this.W - this.padL) - this.padR), this.dgL, 3 - this.dgL.size());
        }
    }

    int shown() {
        JSONArray jSONArray = this.h.d.notifs;
        if (jSONArray == null) {
            return 0;
        }
        return Math.min(Math.max(1, this.h.d.digest.isEmpty() ? 3 : 2), jSONArray.length());
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        if (this.h.d.notifs == null) {
            return null;
        }
        return this.h.d.notifs.length() + " NEW";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        JSONArray jSONArray = this.h.d.notifs;
        int i = 1;
        int i2 = (!this.dgL.isEmpty() ? 1 : 0) + 1;
        if (jSONArray != null && jSONArray.length() != 0) {
            i = shown();
        }
        return i2 + i;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        if (i == 0) {
            return headH();
        }
        if (this.dgL.isEmpty() || i != 1) {
            return ((this.h.d.notifs == null || this.h.d.notifs.length() == 0) ? this.m9 : this.sb13).line();
        }
        return this.dgL.size() * this.dg.size * 1.3f;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        Canvas canvas2;
        HomeData homeData = this.h.d;
        JSONArray jSONArray = homeData.notifs;
        if (i == 0) {
            head(canvas, f, f2, f3, "bell", th().BLUE, Strings.t("w.notifs.h"), jSONArray != null ? String.valueOf(jSONArray.length()) : null);
            return;
        }
        if (!this.dgL.isEmpty() && i == 1) {
            Iterator<Object[]> it = this.dgL.iterator();
            float f5 = f2;
            while (it.hasNext()) {
                float f6 = f;
                for (Object obj : it.next()) {
                    String str = (String) ((Object[]) obj)[0];
                    this.dg.draw(canvas, str, f6, (((this.dg.size * 1.3f) - this.dg.line()) / 2.0f) + f5 + this.dg.asc, col(th().INK));
                    f6 += this.dg.w(str);
                }
                f5 += this.dg.size * 1.3f;
            }
            hit(f, f2, f + f3, f2 + f4, "x:digest", homeData.digest);
            return;
        }
        if (jSONArray == null) {
            msg(canvas, f, f2, f3, f4, Strings.t("w.notifs.perm"), "x:notifAccess", th().DIM);
            return;
        }
        if (jSONArray.length() == 0) {
            msg(canvas, f, f2, f3, f4, Strings.t("w.notifs.none"), null, th().DIM);
            return;
        }
        int i2 = (i - 1) - (!this.dgL.isEmpty() ? 1 : 0);
        JSONObject optJSONObject = jSONArray.optJSONObject(i2);
        if (optJSONObject == null) {
            return;
        }
        String str2 = homeData.ntag.containsKey(optJSONObject.optString("key")) ? homeData.ntag.get(optJSONObject.optString("key")) : "";
        boolean equals = "scam".equals(str2);
        float f7 = f2 + (f4 / 2.0f);
        String upperCase = optJSONObject.optString("app").toUpperCase(Locale.ROOT);
        if (upperCase.length() > 10) {
            upperCase = upperCase.substring(0, 10);
        }
        if (equals) {
            upperCase = Strings.t("w.notifs.scam");
        }
        String str3 = upperCase;
        Txt txt = this.m9;
        float f8 = f7 + (this.m9.cap / 2.0f);
        Theme th = th();
        txt.draw(canvas, str3, f, f8, col(equals ? th.RED : th.DIM));
        float w = f + this.m9.w(str3) + 8.0f;
        if (str2.isEmpty()) {
            canvas2 = canvas;
        } else {
            int i3 = (equals || "important".equals(str2)) ? th().RED : "noise".equals(str2) ? th().LINE2 : th().BLUE;
            this.p.setShader(null);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(col(i3));
            canvas2 = canvas;
            canvas2.drawCircle(w + 3.0f, f7, 3.0f, this.p);
            w += 14.0f;
        }
        float f9 = w;
        String optString = optJSONObject.optString("title");
        if (optString.isEmpty()) {
            optString = optJSONObject.optString("text");
        }
        String str4 = optString;
        String format = optJSONObject.optLong("at") > 0 ? new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(optJSONObject.optLong("at"))) : "";
        this.m9.align(Paint.Align.RIGHT);
        float f10 = f + f3;
        String str5 = format;
        this.m9.draw(canvas2, str5, f10, (this.m9.cap / 2.0f) + f7, col(th().DIM));
        this.m9.align(Paint.Align.LEFT);
        this.sb13.draw(canvas, fitText(this.sb13, str4, ((f10 - f9) - this.m9.w(str5)) - 6.0f), f9, f7 + (this.sb13.cap / 2.0f), col(th().INK));
        hit(f, f2, f10, f2 + f4, "n" + i2, optJSONObject.optString("app") + ": " + str4);
    }
}
