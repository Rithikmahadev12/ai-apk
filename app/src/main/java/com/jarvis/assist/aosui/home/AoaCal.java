package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.CalendarContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.AoaPages;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Memory;
import com.jarvis.assist.aosui.core.Theme;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaCal extends AoaPage {
    final ArrayList<Ev> evs;
    Grid grid;
    LinearLayout list;
    final Calendar month;
    boolean noAccess;
    final Calendar sel;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Ev {
        boolean all;
        long end;
        long start;
        String title;
        String where;

        Ev() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AoaCal(Context context, Backend backend, HomeCtx homeCtx, HomeData homeData) {
        super(context, backend, homeCtx, homeData, "cal", "CALENDAR");
        this.evs = new ArrayList<>();
        this.month = Calendar.getInstance();
        this.sel = Calendar.getInstance();
        this.month.set(5, 1);
        setTabs(new String[][]{new String[]{"month", "MONTH"}, new String[]{"week", "WEEK"}, new String[]{"agenda", "AGENDA"}}, "month");
        showTab("month");
        load();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$load$0$com-augma-oso-home-AoaCal, reason: not valid java name */
    public /* synthetic */ void m202lambda$load$0$comaugmaosohomeAoaCal(String str) {
        this.evs.clear();
        this.noAccess = false;
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.optBoolean("ok")) {
                this.noAccess = "permission".equals(jSONObject.optString("why"));
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("events");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        Ev ev = new Ev();
                        ev.title = optJSONObject.optString("title");
                        ev.where = optJSONObject.optString("where");
                        ev.start = optJSONObject.optLong("start");
                        ev.end = optJSONObject.optLong("end");
                        ev.all = optJSONObject.optBoolean("allDay");
                        this.evs.add(ev);
                    }
                }
            }
        } catch (Exception e) {
        }
        this.sub.setText(this.noAccess ? "NO ACCESS" : this.evs.size() + " EVENTS · 62 DAYS");
        showTab(this.tab);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$load$1$com-augma-oso-home-AoaCal, reason: not valid java name */
    public /* synthetic */ void m203lambda$load$1$comaugmaosohomeAoaCal() {
        final String phoneCal = this.be.phoneCal(62);
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaCal$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                AoaCal.this.m202lambda$load$0$comaugmaosohomeAoaCal(phoneCal);
            }
        });
    }

    void load() {
        Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaCal$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AoaCal.this.m203lambda$load$1$comaugmaosohomeAoaCal();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.AoaPage
    public void refresh() {
        super.refresh();
    }

    static boolean same(Calendar calendar, long j) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j);
        return calendar.get(1) == calendar2.get(1) && calendar.get(6) == calendar2.get(6);
    }

    boolean hasEv(Calendar calendar) {
        long startOf = startOf(calendar);
        long j = 86400000 + startOf;
        Iterator<Ev> it = this.evs.iterator();
        while (it.hasNext()) {
            Ev next = it.next();
            if (next.start < j && next.end > startOf) {
                return true;
            }
        }
        return false;
    }

    static long startOf(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.set(11, 0);
        calendar2.set(12, 0);
        calendar2.set(13, 0);
        calendar2.set(14, 0);
        return calendar2.getTimeInMillis();
    }

    @Override // com.jarvis.assist.aosui.home.AoaPage
    void showTab(String str) {
        this.body.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        LinearLayout linearLayout2 = new LinearLayout(getContext());
        linearLayout2.setGravity(16);
        linearLayout2.setPadding(0, 0, 0, px(8.0f));
        linearLayout2.addView(mono("week".equals(str) ? "THIS WEEK" : "agenda".equals(str) ? "NEXT 30 DAYS" : "SELECTED DAY", 7.5f, 0.16f, Theme.AOA_ORANGE), new LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout2.addView(button("+ NEW", true, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaCal$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AoaCal.this.m204lambda$showTab$2$comaugmaosohomeAoaCal();
            }
        }));
        if (!"agenda".equals(str)) {
            this.grid = new Grid(getContext(), "week".equals(str));
            linearLayout.addView(panel(this.grid, 10), new LinearLayout.LayoutParams(-1, -2));
            linearLayout.addView(new View(getContext()), new LinearLayout.LayoutParams(-1, px(12.0f)));
        }
        linearLayout.addView(linearLayout2);
        this.list = new LinearLayout(getContext());
        this.list.setOrientation(1);
        linearLayout.addView(this.list);
        agenda();
        this.body.addView(scroll(linearLayout));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showTab$2$com-augma-oso-home-AoaCal, reason: not valid java name */
    public /* synthetic */ void m204lambda$showTab$2$comaugmaosohomeAoaCal() {
        Intent data = new Intent("android.intent.action.INSERT").setData(CalendarContract.Events.CONTENT_URI);
        Calendar calendar = (Calendar) this.sel.clone();
        calendar.set(11, Math.max(9, Calendar.getInstance().get(11) + 1));
        calendar.set(12, 0);
        data.putExtra("beginTime", calendar.getTimeInMillis());
        data.putExtra("endTime", calendar.getTimeInMillis() + 3600000);
        start(data);
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0151  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x012f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void agenda() {
        /*
            Method dump skipped, instructions count: 513
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.AoaCal.agenda():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$agenda$3$com-augma-oso-home-AoaCal, reason: not valid java name */
    public /* synthetic */ void m201lambda$agenda$3$comaugmaosohomeAoaCal(long j) {
        start(new Intent("android.intent.action.VIEW", Uri.parse("content://com.android.calendar/time/" + j)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Grid extends View {
        int[] cells;
        float cw;
        final Txt dw;
        final Txt hd;
        final Txt num;
        final Paint p;
        float rh;
        int rows;
        float top;
        final boolean week;
        float x0;
        float y0;

        Grid(Context context, boolean z) {
            super(context);
            this.p = new Paint(1);
            this.week = z;
            this.hd = new Txt(Fonts.MONO, AoaCal.this.dn * 9.0f, AoaCal.this.dn * 1.6f);
            this.dw = new Txt(Fonts.MONO, AoaCal.this.dn * 7.0f, AoaCal.this.dn * 0.8f).align(Paint.Align.CENTER);
            this.num = new Txt(Fonts.RAJ_400, AoaCal.this.dn * 13.0f, 0.0f).align(Paint.Align.CENTER);
            build();
        }

        void build() {
            this.cells = AoaPages.month(AoaCal.this.month.getActualMaximum(5), AoaCal.this.month.get(7), AoaCal.this.month.getFirstDayOfWeek());
            this.rows = AoaPages.rows(this.cells);
            requestLayout();
            invalidate();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            this.cw = size / 7.0f;
            this.rh = AoaCal.this.px(34.0f);
            this.top = AoaCal.this.px(44.0f);
            setMeasuredDimension(size, (int) (this.top + ((this.week ? 1 : this.rows) * this.rh)));
        }

        int weekRow() {
            int i = 1;
            if (AoaCal.this.sel.get(2) == AoaCal.this.month.get(2) && AoaCal.this.sel.get(1) == AoaCal.this.month.get(1)) {
                i = AoaCal.this.sel.get(5);
            }
            for (int i2 = 0; i2 < 42; i2++) {
                if (this.cells[i2] == i) {
                    return i2 / 7;
                }
            }
            return 0;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            Calendar calendar;
            float f2;
            int i;
            Locale locale = Locale.getDefault();
            String upperCase = String.format(locale, "%tB %<tY", AoaCal.this.month).toUpperCase(locale);
            this.hd.align(Paint.Align.CENTER);
            this.hd.draw(canvas, upperCase, getWidth() / 2.0f, AoaCal.this.px(12.0f) + this.hd.cap, -1511182);
            this.hd.align(Paint.Align.LEFT);
            this.hd.draw(canvas, "‹", AoaCal.this.px(4.0f), AoaCal.this.px(12.0f) + this.hd.cap, Theme.AOA_TEAL);
            this.hd.align(Paint.Align.RIGHT);
            this.hd.draw(canvas, "›", getWidth() - AoaCal.this.px(4.0f), AoaCal.this.px(12.0f) + this.hd.cap, Theme.AOA_TEAL);
            Calendar calendar2 = (Calendar) AoaCal.this.month.clone();
            int firstDayOfWeek = AoaCal.this.month.getFirstDayOfWeek();
            int i2 = 0;
            while (true) {
                f = 0.5f;
                if (i2 >= 7) {
                    break;
                }
                calendar2.set(7, (((firstDayOfWeek - 1) + i2) % 7) + 1);
                this.dw.draw(canvas, calendar2.getDisplayName(7, 1, locale).substring(0, 1).toUpperCase(locale), this.cw * (i2 + 0.5f), this.top - AoaCal.this.px(8.0f), -9399652);
                i2++;
            }
            int weekRow = this.week ? weekRow() : 0;
            int i3 = this.week ? weekRow + 1 : this.rows;
            Calendar calendar3 = Calendar.getInstance();
            int i4 = weekRow * 7;
            while (i4 < i3 * 7) {
                int i5 = this.cells[i4];
                if (i5 != 0) {
                    float f3 = this.cw * ((i4 % 7) + f);
                    float f4 = this.top + (((i4 / 7) - weekRow) * this.rh) + (this.rh / 2.0f);
                    Calendar calendar4 = (Calendar) AoaCal.this.month.clone();
                    calendar4.set(5, i5);
                    boolean same = AoaCal.same(calendar3, calendar4.getTimeInMillis());
                    boolean same2 = AoaCal.same(AoaCal.this.sel, calendar4.getTimeInMillis());
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(Math.max(1.0f, AoaCal.this.dn));
                    if (same2) {
                        this.p.setColor(Theme.a(Theme.AOA_TEAL, 0.8f));
                        f2 = 3.0f;
                        calendar = calendar4;
                        canvas.drawRect((f3 - (this.cw / 2.0f)) + AoaCal.this.px(3.0f), (f4 - (this.rh / 2.0f)) + AoaCal.this.px(2.0f), ((this.cw / 2.0f) + f3) - AoaCal.this.px(3.0f), (f4 + (this.rh / 2.0f)) - AoaCal.this.px(2.0f), this.p);
                    } else {
                        calendar = calendar4;
                        f2 = 3.0f;
                    }
                    if (same) {
                        this.p.setStyle(Paint.Style.FILL);
                        this.p.setColor(Theme.AOA_ORANGE);
                        i = -34278;
                        canvas.drawRect((f3 - (this.cw / 2.0f)) + AoaCal.this.px(f2), (f4 + (this.rh / 2.0f)) - AoaCal.this.px(4.0f), ((this.cw / 2.0f) + f3) - AoaCal.this.px(f2), (f4 + (this.rh / 2.0f)) - AoaCal.this.px(2.0f), this.p);
                    } else {
                        i = -34278;
                    }
                    this.num.draw(canvas, String.valueOf(i5), f3, (f4 + (this.num.cap / 2.0f)) - AoaCal.this.px(2.0f), same ? i : -1511182);
                    if (AoaCal.this.hasEv(calendar)) {
                        this.p.setStyle(Paint.Style.FILL);
                        this.p.setColor(same ? i : Theme.AOA_TEAL);
                        canvas.drawCircle(f3, f4 + AoaCal.this.px(10.0f), AoaCal.this.px(2.0f), this.p);
                    }
                }
                i4++;
                f = 0.5f;
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case 0:
                    this.x0 = motionEvent.getX();
                    this.y0 = motionEvent.getY();
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                case 1:
                    float x = motionEvent.getX() - this.x0;
                    float y = motionEvent.getY() - this.y0;
                    if (Math.abs(x) > AoaCal.this.px(50.0f) && Math.abs(x) > Math.abs(y)) {
                        flip(x < 0.0f ? 1 : -1);
                        return true;
                    }
                    if (this.y0 < this.top - AoaCal.this.px(16.0f)) {
                        if (this.x0 < AoaCal.this.px(40.0f)) {
                            flip(-1);
                        } else if (this.x0 > getWidth() - AoaCal.this.px(40.0f)) {
                            flip(1);
                        }
                        return true;
                    }
                    int weekRow = ((((int) ((this.y0 - this.top) / this.rh)) + (this.week ? weekRow() : 0)) * 7) + ((int) (this.x0 / this.cw));
                    if (weekRow >= 0 && weekRow < 42 && this.cells[weekRow] > 0) {
                        AoaCal.this.sel.setTimeInMillis(AoaCal.this.month.getTimeInMillis());
                        AoaCal.this.sel.set(5, this.cells[weekRow]);
                        Feedback.fx(this, 2);
                        invalidate();
                        AoaCal.this.agenda();
                    }
                    return true;
                default:
                    return true;
            }
        }

        void flip(int i) {
            AoaCal.this.month.add(2, i);
            Feedback.fx(this, 8);
            build();
        }
    }
}
