package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
final class WeatherWidget extends WidgetView {
    /* JADX INFO: Access modifiers changed from: package-private */
    public WeatherWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "weather";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return this.h.d.srcBy.containsKey("weather") ? this.h.d.srcBy.get("weather") : "WEATHER";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        return this.h.d.wxTemp;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 3;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        float line;
        float f;
        if (i == 0) {
            return headH();
        }
        if (i == 1) {
            line = ringSz();
            f = this.sb13.line() + (this.m9.line() * 2.0f) + 8.0f;
        } else {
            if (this.h.d.hourly.isEmpty()) {
                return this.m9.line();
            }
            line = (this.m9.line() * 2.0f) + 7.0f + 4.0f;
            f = 40.0f;
        }
        return Math.max(line, f);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        String str;
        HomeData homeData = this.h.d;
        int sc2 = th().sc2(homeData.wxState);
        if (i == 0) {
            head(canvas, f, f2, f3, homeData.wxIcon, sc2, Strings.t("w.weather.h"), homeData.wxDesc);
            return;
        }
        if (i == 1) {
            float ringSz = ringSz();
            Icons.draw(canvas, this.p, homeData.wxIcon, f + (ringSz / 2.0f), f2 + (f4 / 2.0f), ringSz, col(sc2), (1.3f * ringSz) / 30.0f);
            float f5 = f + ringSz + 10.0f;
            float f6 = (f3 - ringSz) - 10.0f;
            str = "";
            this.sb13.draw(canvas, fitText(this.sb13, homeData.wxDesc == null ? "" : homeData.wxDesc, f6), f5, this.sb13.cap + f2 + 1.0f, col(th().INK));
            JSONArray optJSONArray = Store.obj("aos.wx").optJSONArray("daily");
            JSONObject optJSONObject = optJSONArray == null ? null : optJSONArray.optJSONObject(0);
            if (optJSONObject != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String str2 = "H " + optJSONObject.optInt("hi") + "°  L " + optJSONObject.optInt("lo") + "°" + (optJSONObject.has("pop") ? "  ·  RAIN " + optJSONObject.optInt("pop") + "%" : str);
                str = optJSONObject.has("rise") ? "SUN " + simpleDateFormat.format(new Date(optJSONObject.optLong("rise"))) + " → " + simpleDateFormat.format(new Date(optJSONObject.optLong("set"))) : "";
                this.m9.draw(canvas, fitText(this.m9, str2, f6), f5, this.m9.cap + this.sb13.line() + f2 + 3.0f, col(th().INK));
                if (!str.isEmpty()) {
                    this.m9.draw(canvas, fitText(this.m9, str, f6), f5, this.sb13.line() + f2 + 6.0f + this.m9.line() + this.m9.cap, col(th().DIM));
                    return;
                }
                return;
            }
            return;
        }
        if (homeData.hourly.isEmpty()) {
            this.m9.draw(canvas, Strings.t("w.weather.loc"), f, this.m9.base(f2), col(th().DIM));
            return;
        }
        int size = homeData.hourly.size();
        float[] fArr = new float[size];
        for (int i2 = 0; i2 < size; i2++) {
            try {
                fArr[i2] = Float.parseFloat(homeData.hourly.get(i2)[1].replaceAll("[^0-9.-]", ""));
            } catch (Exception e) {
                fArr[i2] = Float.NaN;
            }
        }
        chart(canvas, fArr, f, f2, f3, (f4 - this.m9.line()) - 3.0f, th().BLUE);
        this.m9.align(Paint.Align.CENTER);
        int i3 = 0;
        while (true) {
            Txt txt = this.m9;
            if (i3 >= size) {
                txt.align(Paint.Align.LEFT);
                return;
            } else {
                txt.draw(canvas, homeData.hourly.get(i3)[0], (((f3 - 8.0f) * i3) / Math.max(1, size - 1)) + f + 4.0f, (f2 + f4) - this.m9.desc, col(th().DIM));
                i3++;
            }
        }
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String spoken() {
        HomeData homeData = this.h.d;
        StringBuilder append = new StringBuilder(Strings.t("w.weather.h")).append(". ");
        if (homeData.wxTemp != null && !homeData.wxTemp.isEmpty()) {
            append.append(homeData.wxTemp).append(", ");
        }
        if (homeData.wxDesc != null && !homeData.wxDesc.isEmpty()) {
            append.append(homeData.wxDesc).append(". ");
        }
        if (homeData.hourly.isEmpty()) {
            append.append(Strings.t("w.weather.loc")).append(". ");
        } else {
            append.append("Next hours: ");
            int i = 0;
            while (i < homeData.hourly.size()) {
                append.append(i > 0 ? ", " : "").append(homeData.hourly.get(i)[0]).append(":00 ").append(homeData.hourly.get(i)[1]);
                i++;
            }
            append.append(". ");
        }
        return append.append("From ").append(source()).toString().trim();
    }
}
