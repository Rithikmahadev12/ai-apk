package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import com.jarvis.assist.aosui.core.Strings;
import java.util.Calendar;

/* loaded from: /tmp/augmaos/classes.dex */
final class AlarmsWidget extends WidgetView {
    static final String[] DAYL = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

    /* JADX INFO: Access modifiers changed from: package-private */
    public AlarmsWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "alarms";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "SYSTEM CLOCK";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        if (i == 0) {
            return headH();
        }
        return (this.h.d.alarmAt > 0 ? this.hero : this.m9).line();
    }

    static String when(long j) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j);
        Calendar[] calendarArr = {(Calendar) calendar2.clone(), (Calendar) calendar.clone()};
        for (int i = 0; i < 2; i++) {
            Calendar calendar3 = calendarArr[i];
            calendar3.set(11, 0);
            calendar3.set(12, 0);
            calendar3.set(13, 0);
            calendar3.set(14, 0);
        }
        long round = Math.round((r6.getTimeInMillis() - r7.getTimeInMillis()) / 8.64E7d);
        return round == 0 ? "TODAY" : round == 1 ? "TOMORROW" : DAYL[calendar2.get(7) - 1];
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        long j = this.h.d.alarmAt;
        if (i == 0) {
            head(canvas, f, f2, f3, "clock", th().BLUE, Strings.t("w.alarms.h"), null);
            return;
        }
        if (j <= 0) {
            msg(canvas, f, f2, f3, f4, Strings.t("w.alarms.none"), "x:clock", th().DIM);
            return;
        }
        String hm = HomeData.hm(j);
        float f5 = f2 + this.hero.asc;
        this.hero.draw(canvas, hm, f, f5, col(th().INK));
        this.m9.draw(canvas, when(j), 10.0f + this.hero.w(hm) + f, f5, col(th().DIM));
        hit(f, f2, f + f3, f2 + f4, "x:clock", "Next alarm " + hm + " " + when(j));
    }
}
