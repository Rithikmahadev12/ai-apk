package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.GoogleAuth;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
final class GmailWidget extends WidgetView {
    /* JADX INFO: Access modifiers changed from: package-private */
    public GmailWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "gmail";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "GMAIL · READ-ONLY";
    }

    JSONObject g() {
        try {
            return new JSONObject(Store.get(GoogleAuth.CACHE, "{}")).optJSONObject("gmail");
        } catch (Exception e) {
            return null;
        }
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        JSONObject g = g();
        if (g == null || g.optInt("unread", -1) < 0) {
            return null;
        }
        return g.optInt("unread") + " UNREAD";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        JSONObject g = g();
        JSONArray optJSONArray = g == null ? null : g.optJSONArray("top");
        return 1 + ((optJSONArray == null || optJSONArray.length() == 0) ? 1 : Math.min(3, optJSONArray.length()));
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : this.sb13.line();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        if (i == 0) {
            head(canvas, f, f2, f3, "mail", th().RED, Strings.t("w.gmail.h"), null);
            return;
        }
        JSONObject g = g();
        JSONArray optJSONArray = g == null ? null : g.optJSONArray("top");
        if (optJSONArray == null || optJSONArray.length() == 0) {
            msg(canvas, f, f2, f3, f4, Strings.t(g == null ? "w.gmail.connect" : "w.gmail.none"), g == null ? "x:google" : null, th().DIM);
            return;
        }
        JSONObject optJSONObject = optJSONArray.optJSONObject(i - 1);
        if (optJSONObject == null) {
            return;
        }
        float f5 = f2 + (f4 / 2.0f);
        String upperCase = optJSONObject.optString("from").toUpperCase(Locale.ROOT);
        if (upperCase.length() > 12) {
            upperCase = upperCase.substring(0, 12);
        }
        String str = upperCase;
        String format = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(optJSONObject.optLong("at")));
        this.m9.draw(canvas, str, f, f5 + (this.m9.cap / 2.0f), col(th().DIM));
        float max = f + Math.max(70.0f, this.m9.w(str) + 8.0f);
        this.m9.align(Paint.Align.RIGHT);
        float f6 = f + f3;
        this.m9.draw(canvas, format, f6, f5 + (this.m9.cap / 2.0f), col(th().DIM));
        this.m9.align(Paint.Align.LEFT);
        this.sb13.draw(canvas, fitText(this.sb13, optJSONObject.optString("subject"), ((f6 - max) - this.m9.w(format)) - 6.0f), max, f5 + (this.sb13.cap / 2.0f), col(th().INK));
    }
}
