package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeData;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
final class CalendarWidget extends WidgetView {
    /* JADX INFO: Access modifiers changed from: package-private */
    public CalendarWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "calendar";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return this.h.d.srcBy.containsKey("calendar") ? this.h.d.srcBy.get("calendar") : "DEVICE CALENDAR";
    }

    int shown() {
        return Math.min(this.h.g.ph >= 150.0f ? 4 : this.h.g.ph >= 128.0f ? 3 : 2, this.h.d.events.size());
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected String readout() {
        long currentTimeMillis = System.currentTimeMillis();
        Iterator<HomeData.Ev> it = this.h.d.events.iterator();
        int i = 0;
        while (it.hasNext()) {
            HomeData.Ev next = it.next();
            if (HomeRules.calDay(next.start, next.end, currentTimeMillis) == 0) {
                i++;
            }
        }
        return i + " TODAY";
    }

    int shown2() {
        return Math.max(1, Math.min(shown(), this.h.g.ph >= 150.0f ? 3 : 2));
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return shown2() + 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : i == 1 ? (this.m9.line() * 2.0f) + 4.0f : this.h.d.events.isEmpty() ? this.m9.line() : Math.max(this.sb13.line(), 7.0f);
    }

    void week(Canvas canvas, float f, float f2, float f3, float f4) {
        int i;
        Calendar calendar = Calendar.getInstance();
        int i2 = 6;
        int i3 = calendar.get(6);
        calendar.add(6, -((calendar.get(7) + 5) % 7));
        float f5 = f3 / 7.0f;
        this.m9.align(Paint.Align.CENTER);
        int i4 = 0;
        for (int i5 = 7; i4 < i5; i5 = 7) {
            float f6 = f + (i4 * f5) + 1.0f;
            float f7 = (f6 + f5) - 2.0f;
            boolean z = calendar.get(i2) == i3;
            this.p.setShader(null);
            if (z) {
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(col(Theme.a(th().RED, 0.85f)));
                i = 1;
                canvas.drawRect(f6, f2, f7, f2 + f4, this.p);
            } else {
                i = 1;
            }
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.0f);
            Paint paint = this.p;
            Theme th = th();
            paint.setColor(col(z ? th.RED : th.LINE));
            float f8 = f2 + f4;
            canvas.drawRect(f6, f2, f7, f8, this.p);
            String upperCase = calendar.getDisplayName(i5, i, Locale.getDefault()).toUpperCase(Locale.ROOT);
            if (upperCase.length() > 2) {
                upperCase = upperCase.substring(0, 2);
            }
            float f9 = (f6 + f7) / 2.0f;
            this.m9.draw(canvas, upperCase, f9, f2 + 2.0f + this.m9.asc, col(z ? -1 : th().DIM));
            this.m9.draw(canvas, String.valueOf(calendar.get(5)), f9, (f8 - 2.0f) - this.m9.desc, col(z ? -1 : th().INK));
            Iterator<HomeData.Ev> it = this.h.d.events.iterator();
            int i6 = 0;
            while (it.hasNext()) {
                HomeData.Ev next = it.next();
                Calendar calendar2 = Calendar.getInstance();
                float f10 = f5;
                calendar2.setTimeInMillis(next.start);
                if (calendar2.get(6) == calendar.get(6)) {
                    i6 = i;
                }
                f5 = f10;
            }
            float f11 = f5;
            if (i6 != 0 && !z) {
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(col(th().BLUE));
                canvas.drawCircle(f7 - 3.0f, f2 + 3.0f, 1.6f, this.p);
            }
            calendar.add(6, i);
            i4++;
            f5 = f11;
            i2 = 6;
        }
        this.m9.align(Paint.Align.LEFT);
    }

    int[] days() {
        long currentTimeMillis = System.currentTimeMillis();
        int shown = shown();
        int[] iArr = new int[shown];
        for (int i = 0; i < shown; i++) {
            HomeData.Ev ev = this.h.d.events.get(i);
            iArr[i] = HomeRules.calDay(ev.start, ev.end, currentTimeMillis);
        }
        return iArr;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String spoken() {
        if (this.h.d.events.isEmpty()) {
            return Strings.t("w.calendar.h") + ". " + Strings.t("w.calendar.none");
        }
        long currentTimeMillis = System.currentTimeMillis();
        int[] days = days();
        StringBuilder append = new StringBuilder(Strings.t("w.calendar.h")).append(". ");
        for (int i = 0; i < days.length; i++) {
            HomeData.Ev ev = this.h.d.events.get(i);
            append.append(days[i] == 1 ? Strings.t("w.calendar.tmr") + ", " : "").append(HomeRules.calTime(ev.start, ev.end, ev.allDay, currentTimeMillis, Strings.t("w.calendar.allday"))).append(", ").append(ev.title).append(". ");
        }
        return append.toString().trim();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        HomeData homeData = this.h.d;
        long currentTimeMillis = System.currentTimeMillis();
        if (i == 0) {
            head(canvas, f, f2, f3, "cal", th().BLUE, Strings.t("w.calendar.h") + " · " + (HomeRules.onlyTomorrow(days()) ? Strings.t("w.calendar.tmr") : homeData.date), null);
            return;
        }
        if (i == 1) {
            week(canvas, f, f2, f3, f4);
            return;
        }
        if (homeData.events.isEmpty()) {
            this.m9.draw(canvas, Strings.t("w.calendar.none"), f, this.m9.base(f2), col(th().DIM));
            return;
        }
        HomeData.Ev ev = homeData.events.get(i - 2);
        String calTime = HomeRules.calTime(ev.start, ev.end, ev.allDay, currentTimeMillis, Strings.t("w.calendar.allday"));
        boolean equals = "NOW".equals(calTime);
        boolean z = HomeRules.calDay(ev.start, ev.end, currentTimeMillis) == 1;
        float f5 = f2 + (f4 / 2.0f);
        float max = Math.max(36.0f, this.m9.w(Strings.t("w.calendar.allday")));
        Txt txt = this.m9;
        float f6 = f5 + (this.m9.cap / 2.0f);
        Theme th = th();
        txt.draw(canvas, calTime, f, f6, col(equals ? th.RED : th.DIM));
        float f7 = f + max + 8.0f;
        float f8 = f7 + 3.5f;
        this.p.setShader(null);
        if (equals) {
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(col(th().RED));
            canvas.drawCircle(f8, f5, 3.5f, this.p);
        } else {
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.5f);
            Paint paint = this.p;
            Theme th2 = th();
            paint.setColor(col(z ? th2.DIM : th2.BLUE));
            canvas.drawCircle(f8, f5, 2.75f, this.p);
        }
        float f9 = f7 + 9.0f + 8.0f;
        if (z) {
            String str = Strings.t("w.calendar.tmr") + " · ";
            this.m9.draw(canvas, str, f9, (this.m9.cap / 2.0f) + f5, col(th().DIM));
            f9 += this.m9.w(str);
        }
        this.sb13.draw(canvas, fitText(this.sb13, ev.title, (f + f3) - f9), f9, f5 + (this.sb13.cap / 2.0f), col(th().INK));
    }
}
