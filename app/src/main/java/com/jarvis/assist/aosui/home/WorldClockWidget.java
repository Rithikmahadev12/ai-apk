package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Strings;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: /tmp/augmaos/classes.dex */
final class WorldClockWidget extends WidgetView {
    Txt m7;
    final ArrayList<String[]> z;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WorldClockWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.z = new ArrayList<>();
        this.flexFirst = true;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "worldclock";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "THIS PHONE";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void styles() {
        super.styles();
        this.m7 = new Txt(Fonts.MONO, Math.max(8.0f, this.h.zoom * 7.0f), this.h.zoom * 7.0f * 0.14f).align(Paint.Align.CENTER);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void bind() {
        this.z.clear();
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        int i = (calendar.get(1) * 400) + calendar.get(6);
        this.z.add(new String[]{this.h.d.time, Strings.t("w.worldclock.here"), Strings.t("w.worldclock.today")});
        Iterator<String> it = this.h.d.clocks().iterator();
        int i2 = 0;
        while (it.hasNext()) {
            String next = it.next();
            i2++;
            if (i2 > 2) {
                return;
            }
            TimeZone timeZone = TimeZone.getTimeZone(next);
            if (timeZone.getID().equals(next) || "GMT".equals(next)) {
                Calendar calendar2 = Calendar.getInstance(timeZone);
                calendar2.setTimeInMillis(currentTimeMillis);
                int i3 = (calendar2.get(1) * 400) + calendar2.get(6);
                String upperCase = next.substring(next.lastIndexOf(47) + 1).replace('_', ' ').toUpperCase(Locale.ROOT);
                if (upperCase.length() > 12) {
                    upperCase = upperCase.substring(0, 12);
                }
                this.z.add(new String[]{HomeData.p2(calendar2.get(11)) + ":" + HomeData.p2(calendar2.get(12)), upperCase, Strings.t(i3 > i ? "w.worldclock.tomorrow" : i3 < i ? "w.worldclock.yesterday" : "w.worldclock.today")});
            }
        }
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        return this.h.d.time;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : this.sv15.line() + 2.0f + this.m9.line() + 2.0f + this.m7.line();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        if (i == 0) {
            head(canvas, f, f2, f3, "globe", th().BLUE, Strings.t("w.worldclock.h"), null);
            return;
        }
        int size = this.z.size();
        if (size == 0) {
            return;
        }
        float line = f2 + ((f4 - ((((this.sv15.line() + 2.0f) + this.m9.line()) + 2.0f) + this.m7.line())) / 2.0f);
        float f5 = f3 / size;
        this.m9.align(Paint.Align.CENTER);
        for (int i2 = 0; i2 < size; i2++) {
            String[] strArr = this.z.get(i2);
            float f6 = f + (i2 * f5) + (f5 / 2.0f);
            this.sv15.draw(canvas, strArr[0], f6, this.sv15.asc + line, col(th().INK));
            this.m9.draw(canvas, strArr[1], f6, this.sv15.line() + line + 2.0f + this.m9.asc, col(th().DIM));
            this.m7.draw(canvas, strArr[2], f6, this.sv15.line() + line + 2.0f + this.m9.line() + 2.0f + this.m7.asc, col(th().DIM));
        }
        this.m9.align(Paint.Align.LEFT);
    }
}
