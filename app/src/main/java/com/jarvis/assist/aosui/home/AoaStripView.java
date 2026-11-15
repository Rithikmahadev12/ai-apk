package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.AoaStrip;
import com.jarvis.assist.aosui.core.Corners;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaStripView extends View {
    static final float IND = 12.0f;
    Txt bigT;
    final ArrayList<Card> cards;
    Txt colT;
    Txt colV;
    boolean drag;
    boolean gliding;
    final HomeCtx h;
    String lastId;
    long lastT;
    float lastX;
    final FrameClock.Handle loop;
    final Paint p;
    float p0;
    float pos;
    final RectF rf;
    Txt rowT;
    final int slop;
    Txt smallT;
    Txt tagT;
    final Tap tap;
    float target;
    float trackX0;
    float trackX1;
    float vx;
    float x0;
    float y0;

    /* loaded from: /tmp/augmaos/classes.dex */
    interface Tap {
        void go(String str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Card {
        String go;
        String id;
        String tag;
        String info = "";
        String big = "";
        String small = "";
        float bar = -1.0f;
        final ArrayList<String[]> rows = new ArrayList<>();
        final ArrayList<String[]> cols = new ArrayList<>();

        Card() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AoaStripView(Context context, HomeCtx homeCtx, Tap tap) {
        super(context);
        this.p = new Paint(1);
        this.rf = new RectF();
        this.cards = new ArrayList<>();
        this.h = homeCtx;
        this.tap = tap;
        this.slop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.loop = FrameClock.I.add(new FrameClock.Sub() { // from class: com.jarvis.assist.aosui.home.AoaStripView$$ExternalSyntheticLambda0
            @Override // com.jarvis.assist.aosui.core.FrameClock.Sub
            public final boolean frame(long j, float f) {
                return AoaStripView.this.m229lambda$new$0$comaugmaosohomeAoaStripView(j, f);
            }
        }, this);
        setFocusable(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-home-AoaStripView, reason: not valid java name */
    public /* synthetic */ boolean m229lambda$new$0$comaugmaosohomeAoaStripView(long j, float f) {
        if (!this.gliding) {
            return false;
        }
        this.pos += (this.target - this.pos) * Math.min(1.0f, (f / 1000.0f) * 14.0f);
        if (Math.abs(this.target - this.pos) < 0.002f) {
            this.pos = this.target;
            this.gliding = false;
            normalise();
        }
        invalidate();
        return this.gliding;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepare() {
        this.tagT = new Txt(Fonts.MONO, this.h.L(7.5f, 7.5f), this.h.L(7.5f, 7.5f) * 0.14f);
        this.smallT = new Txt(Fonts.MONO, this.h.L(8.0f, 8.0f), this.h.L(8.0f, 8.0f) * 0.14f);
        this.rowT = new Txt(Fonts.MONO, this.h.L(8.0f, 8.0f), this.h.L(8.0f, 8.0f) * 0.1f);
        this.colT = new Txt(Fonts.MONO, this.h.L(7.0f, 7.0f), this.h.L(7.0f, 7.0f) * 0.1f).align(Paint.Align.CENTER);
        this.colV = new Txt(Fonts.RAJ_600, this.h.L(13.0f, 11.0f), 0.0f).align(Paint.Align.CENTER);
        bind();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bind() {
        String str;
        String str2;
        long j;
        int i;
        long j2;
        String str3;
        long j3;
        HomeData.Metric metric;
        HomeData homeData = this.h.d;
        this.cards.clear();
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Card card = card("agenda", "x:page:cal");
        Iterator<HomeData.Ev> it = homeData.events.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            HomeData.Ev next = it.next();
            if (HomeRules.calDay(next.start, next.end, currentTimeMillis) == 0 && next.end > currentTimeMillis) {
                i2++;
            }
        }
        long j4 = 0;
        card.big = Advanced.greeting(Advanced.mode(1, 2), homeData.greet, i2, "", 0, homeData.alarmAt > 0 ? simpleDateFormat.format(new Date(homeData.alarmAt)) : "", 8.0d, "", homeData.you)[1];
        card.info = "TODAY · " + i2 + " LEFT";
        Iterator<HomeData.Ev> it2 = homeData.events.iterator();
        int i3 = 0;
        while (true) {
            str = "NOW";
            if (!it2.hasNext()) {
                break;
            }
            HomeData.Ev next2 = it2.next();
            if (next2.end <= currentTimeMillis || i3 >= 3) {
                j4 = 0;
                i3 = i3;
                it2 = it2;
            } else {
                Iterator<HomeData.Ev> it3 = it2;
                int i4 = i3;
                card.rows.add(new String[]{(HomeRules.calDay(next2.start, next2.end, currentTimeMillis) == 0 ? "" : "TOMORROW ") + (next2.allDay ? "ALL DAY" : simpleDateFormat.format(new Date(next2.start))) + " · " + next2.title.toUpperCase(Locale.getDefault()), next2.start > currentTimeMillis ? Corners.dur(next2.start - currentTimeMillis) : "NOW", i4 == 0 ? "o" : ""});
                j4 = 0;
                it2 = it3;
                i3 = i4 + 1;
            }
        }
        int i5 = i3;
        long j5 = j4;
        if (homeData.calNeed) {
            card.rows.add(new String[]{"CALENDAR · ALLOW ACCESS", "", ""});
        } else if (i5 == 0) {
            card.rows.add(new String[]{"NOTHING ELSE TODAY", "", ""});
        }
        if (!"—".equals(homeData.wxTemp) && !homeData.wxTemp.isEmpty()) {
            Card card2 = card("weather", "x:detail:weather");
            card2.big = homeData.wxTemp;
            card2.small = homeData.wxDesc;
            String str4 = homeData.srcBy.get("weather");
            if (str4 != null && str4.contains(" · ")) {
                str = str4.substring(str4.lastIndexOf(" · ") + 3);
            }
            card2.info = str;
            for (int i6 = 0; i6 < homeData.hourly.size() && i6 < 5; i6++) {
                card2.cols.add(new String[]{homeData.hourly.get(i6)[0] + ":00", homeData.hourly.get(i6)[1]});
            }
        }
        if (homeData.media != null && !homeData.media.optString("title").isEmpty()) {
            Card card3 = card("media", "x:detail:media");
            card3.big = homeData.media.optString("title").toUpperCase(Locale.getDefault());
            card3.small = homeData.media.optString("artist").toUpperCase(Locale.getDefault());
            String optString = homeData.media.optString("app");
            card3.info = optString.isEmpty() ? homeData.media.optBoolean("playing") ? "PLAYING" : "PAUSED" : optString.toUpperCase(Locale.getDefault());
            str2 = "app";
            long optLong = homeData.media.optLong("dur", j5);
            j = currentTimeMillis;
            long optLong2 = homeData.media.optLong("pos", j5);
            if (optLong > j5) {
                card3.bar = Math.min(1.0f, ((float) optLong2) / ((float) optLong));
                card3.rows.add(new String[]{mmss(optLong2), homeData.media.optBoolean("playing") ? "PLAYING" : "PAUSED", "", mmss(optLong)});
            } else {
                card3.rows.add(new String[]{homeData.media.optBoolean("playing") ? "PLAYING" : "PAUSED", "", ""});
            }
        } else {
            str2 = "app";
            j = currentTimeMillis;
        }
        if (!homeData.healthOff && !homeData.hm.isEmpty()) {
            Card card4 = card("health", "x:detail:health");
            if (homeData.hm.size() > 3) {
                metric = homeData.hm.get(3);
                i = 0;
            } else {
                i = 0;
                metric = homeData.hm.get(0);
            }
            card4.big = metric.value;
            card4.small = metric.label;
            card4.info = "TODAY";
            if (metric.frac > 0.0f) {
                card4.bar = Math.min(1.0f, metric.frac);
            }
            for (int i7 = i; i7 < homeData.hm.size() && card4.cols.size() < 4; i7++) {
                if (homeData.hm.get(i7) != metric) {
                    card4.cols.add(new String[]{homeData.hm.get(i7).label, homeData.hm.get(i7).value});
                }
            }
        } else {
            i = 0;
        }
        if (homeData.notifs != null) {
            Card card5 = card("notifs", "x:detail:notifs");
            int length = homeData.notifs.length();
            card5.info = length + " UNREAD";
            if (length == 0) {
                card5.big = "ALL CLEAR";
            }
            int i8 = i;
            for (int i9 = 4; i8 < length && card5.rows.size() < i9; i9 = 4) {
                JSONObject optJSONObject = homeData.notifs.optJSONObject(i8);
                if (optJSONObject == null) {
                    j3 = j5;
                    str3 = str2;
                } else {
                    String optString2 = optJSONObject.optString("title");
                    if (optString2.isEmpty()) {
                        optString2 = optJSONObject.optString("text");
                    }
                    long optLong3 = optJSONObject.optLong("at");
                    str3 = str2;
                    j3 = j5;
                    card5.rows.add(new String[]{optJSONObject.optString(str3).toUpperCase(Locale.getDefault()) + " · " + optString2.toUpperCase(Locale.getDefault()), optLong3 > j3 ? Corners.dur(j - optLong3) : "", card5.rows.isEmpty() ? "o" : ""});
                }
                i8++;
                str2 = str3;
                j5 = j3;
            }
            j2 = j5;
        } else {
            j2 = j5;
        }
        Card card6 = card("battery", "x:detail:battery");
        card6.big = homeData.btPct + "%";
        card6.small = homeData.btLabel;
        card6.info = "PHONE";
        card6.bar = homeData.btPct / 100.0f;
        if (homeData.stTotal > j2) {
            card6.rows.add(new String[]{"STORAGE · " + StorageWidget.gb(homeData.stTotal), StorageWidget.gb(homeData.stFree) + " FREE", ""});
        }
        if (homeData.alarmAt > j2) {
            Card card7 = card("alarms", "x:detail:alarms");
            card7.big = simpleDateFormat.format(new Date(homeData.alarmAt));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(homeData.alarmAt);
            card7.small = calendar.getDisplayName(7, 1, Locale.getDefault()).toUpperCase(Locale.getDefault()) + " · IN " + Corners.dur(homeData.alarmAt - j);
            card7.info = "NEXT";
        }
        int i10 = 0;
        for (int i11 = 0; i11 < this.cards.size(); i11++) {
            if (this.cards.get(i11).id.equals(this.lastId)) {
                i10 = i11;
            }
        }
        if (!this.gliding) {
            float f = i10;
            this.pos = f;
            this.target = f;
        }
        if (!this.cards.isEmpty()) {
            this.lastId = this.cards.get(AoaStrip.nearest(this.pos, this.cards.size())).id;
        }
        setContentDescription(describe());
        invalidate();
    }

    Card card(String str, String str2) {
        Card card = new Card();
        card.id = str;
        card.go = str2;
        card.tag = Strings.t("aoa.c." + str);
        this.cards.add(card);
        return card;
    }

    static String mmss(long j) {
        long j2 = j / 1000;
        return (j2 / 60) + ":" + String.format(Locale.ROOT, "%02d", Long.valueOf(j2 % 60));
    }

    String describe() {
        String str = "";
        if (this.cards.isEmpty()) {
            return "";
        }
        Card card = this.cards.get(AoaStrip.nearest(this.pos, this.cards.size()));
        String counter = AoaStrip.counter(AoaStrip.nearest(this.pos, this.cards.size()), this.cards.size(), card.tag);
        String str2 = card.big;
        if (!card.small.isEmpty()) {
            str = " " + card.small;
        }
        return counter + ". " + str2 + str;
    }

    void normalise() {
        int size = this.cards.size();
        if (size == 0) {
            return;
        }
        float wrapF = AoaStrip.wrapF(this.pos, size);
        this.target += wrapF - this.pos;
        this.pos = wrapF;
        String str = this.cards.get(AoaStrip.nearest(this.pos, size)).id;
        if (str.equals(this.lastId)) {
            return;
        }
        this.lastId = str;
        Feedback.fx(this, 8);
        setContentDescription(describe());
    }

    void glideTo(float f) {
        this.target = f;
        if (!Perf.k().calm && Perf.k().motion) {
            this.gliding = true;
            this.loop.kick();
        } else {
            this.pos = f;
            this.gliding = false;
            normalise();
            invalidate();
        }
    }

    float H() {
        return getHeight() / Math.max(0.01f, this.h.g.S);
    }

    float W() {
        return getWidth() / Math.max(0.01f, this.h.g.S);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Canvas canvas2;
        AoaStripView aoaStripView = this;
        Canvas canvas3 = canvas;
        if (aoaStripView.tagT == null) {
            return;
        }
        Theme theme = aoaStripView.h.th;
        int size = aoaStripView.cards.size();
        float f = aoaStripView.h.g.S;
        float W = aoaStripView.W();
        float H = (aoaStripView.H() - IND) - 4.0f;
        int save = canvas3.save();
        canvas3.scale(f, f);
        int save2 = canvas3.save();
        canvas3.clipRect(0.0f, 0.0f, W, H);
        float floor = (float) Math.floor(aoaStripView.pos);
        int i = 0;
        int i2 = 0;
        while (i2 < 2 && size > 0) {
            float f2 = ((i2 + floor) - aoaStripView.pos) * W;
            if (f2 < W && f2 > (-W)) {
                aoaStripView.drawCard(canvas3, theme, aoaStripView.cards.get(AoaStrip.wrap(((int) floor) + i2, size)), f2, W, H);
            }
            i2++;
            canvas3 = canvas3;
            aoaStripView = aoaStripView;
            theme = theme;
            W = W;
            H = H;
        }
        Theme theme2 = theme;
        float f3 = W;
        float f4 = H;
        AoaStripView aoaStripView2 = aoaStripView;
        Canvas canvas4 = canvas3;
        canvas4.restoreToCount(save2);
        aoaStripView2.p.setShader(null);
        aoaStripView2.p.setStyle(Paint.Style.STROKE);
        aoaStripView2.p.setStrokeWidth(1.2f);
        aoaStripView2.p.setColor(theme2.fWHITE);
        while (i < 4) {
            int i3 = i % 2;
            float f5 = i3 == 0 ? 0.6f : f3 - 0.6f;
            float f6 = i >= 2 ? f4 - 0.6f : 0.6f;
            float f7 = i3 == 0 ? 7.0f : -7.0f;
            float f8 = i < 2 ? 7.0f : -7.0f;
            float f9 = f5 + f7;
            float f10 = f5;
            canvas4.drawLine(f10, f6, f9, f6, aoaStripView2.p);
            canvas4 = canvas;
            canvas4.drawLine(f10, f6, f10, f6 + f8, aoaStripView2.p);
            i++;
        }
        float f11 = f4 + 4.0f + 6.0f;
        String t = Strings.t("aoa.widgets");
        String counter = size == 0 ? "" : AoaStrip.counter(AoaStrip.nearest(aoaStripView2.pos, size), size, aoaStripView2.cards.get(AoaStrip.nearest(aoaStripView2.pos, size)).tag);
        aoaStripView2.tagT.align(Paint.Align.LEFT);
        aoaStripView2.tagT.draw(canvas, t, 0.0f, f11 + (aoaStripView2.tagT.cap / 2.0f), theme2.DIM);
        float w = aoaStripView2.tagT.w(t) + 8.0f;
        aoaStripView2.tagT.align(Paint.Align.RIGHT);
        float max = Math.max(aoaStripView2.tagT.w("00/00 · NOW PLAYING"), aoaStripView2.tagT.w(counter));
        aoaStripView2.tagT.draw(canvas, counter, f3, f11 + (aoaStripView2.tagT.cap / 2.0f), theme2.INK);
        aoaStripView2.tagT.align(Paint.Align.LEFT);
        float f12 = (f3 - max) - 8.0f;
        aoaStripView2.trackX0 = w;
        aoaStripView2.trackX1 = f12;
        aoaStripView2.p.setStyle(Paint.Style.FILL);
        aoaStripView2.p.setColor(theme2.LINE2);
        canvas.drawRect(w, f11 - 0.5f, f12, f11 + 0.5f, aoaStripView2.p);
        if (size > 0) {
            float f13 = f12 - w;
            float thumbW = AoaStrip.thumbW(size) * f13;
            float thumbX = w + (f13 * AoaStrip.thumbX(aoaStripView2.pos, size));
            aoaStripView2.p.setColor(theme2.RED);
            canvas2 = canvas;
            canvas2.drawRect(thumbX, f11 - 1.5f, thumbW + thumbX, f11 + 1.5f, aoaStripView2.p);
        } else {
            canvas2 = canvas;
        }
        canvas2.restoreToCount(save);
    }

    void drawCard(Canvas canvas, Theme theme, Card card, float f, float f2, float f3) {
        float f4;
        float f5;
        float f6;
        float f7 = f + 10.0f;
        float f8 = f + f2;
        float f9 = f8 - 10.0f;
        float f10 = f9 - f7;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(theme.CARD);
        canvas.drawRect(f, 0.0f, f8, f3, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        float f11 = 1.0f;
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(Theme.a(-4139300, 0.1f));
        canvas.drawRect(f + 0.5f, 0.5f, f8 - 0.5f, f3 - 0.5f, this.p);
        float f12 = this.tagT.cap + 8.0f;
        this.tagT.align(Paint.Align.LEFT);
        this.tagT.draw(canvas, card.tag, f7, f12, theme.RED);
        this.tagT.align(Paint.Align.RIGHT);
        this.tagT.draw(canvas, WidgetView.fitText(this.tagT, card.info, (f10 - this.tagT.w(card.tag)) - 10.0f), f9, f12, theme.INK);
        this.tagT.align(Paint.Align.LEFT);
        char c = 2;
        int size = card.rows.size() + (card.cols.isEmpty() ? 0 : 2);
        float f13 = card.bar;
        float max = Math.max(13.0f, Math.min(24.0f, (size + 0 > 2 ? 0.2f : 0.26f) * f3));
        if (this.bigT == null || Math.abs(this.bigT.size - max) > 0.1f) {
            this.bigT = new Txt(Fonts.RAJ_400, max, 0.04f * max);
        }
        float f14 = f12 + this.bigT.cap + 6.0f;
        float f15 = 0.0f;
        if (!card.big.isEmpty()) {
            String upperCase = card.small.isEmpty() ? "" : card.small.toUpperCase(Locale.getDefault());
            String fitText = WidgetView.fitText(this.bigT, card.big, f10 - (upperCase.isEmpty() ? 0.0f : Math.min(0.45f * f10, this.smallT.w(upperCase)) + 6.0f));
            String str = upperCase;
            f4 = 8.0f;
            f5 = 0.1f;
            this.bigT.draw(canvas, fitText, f7, f14, theme.INK);
            if (!str.isEmpty()) {
                this.smallT.draw(canvas, WidgetView.fitText(this.smallT, str, (f10 - this.bigT.w(fitText)) - 6.0f), this.bigT.w(fitText) + f7 + 6.0f, f14, theme.DIM);
            }
        } else {
            f4 = 8.0f;
            f5 = 0.1f;
            f14 -= this.bigT.cap;
        }
        if (card.bar >= 0.0f) {
            float f16 = f14 + 6.0f;
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(Theme.a(-4139300, 0.14f));
            float f17 = f16 + 2.0f;
            canvas.drawRect(f7, f16, f9, f17, this.p);
            this.p.setColor(theme.RED);
            canvas.drawRect(f7, f16, (Math.max(0.0f, Math.min(1.0f, card.bar)) * f10) + f7, f17, this.p);
            f7 = f7;
            f14 = f17;
        }
        if (card.cols.isEmpty()) {
            f6 = 1.0f;
        } else {
            float size2 = f10 / card.cols.size();
            float f18 = f14 + this.colT.cap + 4.0f;
            int i = 0;
            while (i < card.cols.size()) {
                float f19 = f7 + ((i + 0.5f) * size2);
                float f20 = size2 - 4.0f;
                this.colT.draw(canvas, WidgetView.fitText(this.colT, card.cols.get(i)[0].toUpperCase(Locale.getDefault()), f20), f19, f18, theme.DIM);
                float f21 = f18;
                this.colV.draw(canvas, WidgetView.fitText(this.colV, card.cols.get(i)[1], f20), f19, f21 + 3.0f + this.colV.cap, theme.INK);
                i++;
                f18 = f21;
                f11 = f11;
            }
            f6 = f11;
            f14 = f18 + this.colV.cap + 3.0f;
        }
        float f22 = this.rowT.cap + f4;
        this.p.setStyle(Paint.Style.FILL);
        Iterator<String[]> it = card.rows.iterator();
        while (it.hasNext()) {
            String[] next = it.next();
            float f23 = f14 + 4.0f;
            float f24 = f23 + f22;
            if (f24 > f3 - 2.0f) {
                return;
            }
            this.p.setColor(Theme.a(-4139300, f5));
            float f25 = f7;
            canvas.drawRect(f25, f23, f9, f23 + f6, this.p);
            f7 = f25;
            float f26 = f23 + 4.0f + this.rowT.cap;
            float f27 = f15;
            String str2 = next[1];
            this.rowT.align(Paint.Align.RIGHT);
            float w = str2.isEmpty() ? f27 : this.rowT.w(str2) + f4;
            char c2 = c;
            this.rowT.draw(canvas, str2, f9, f26, "o".equals(next[c2]) ? theme.RED : theme.INK);
            this.rowT.align(Paint.Align.LEFT);
            if (next.length > 3) {
                this.rowT.draw(canvas, next[0], f7, f26, theme.DIM);
                this.rowT.align(Paint.Align.CENTER);
                this.rowT.draw(canvas, next[1], (f7 + f9) / 2.0f, f26, theme.INK);
                this.rowT.align(Paint.Align.RIGHT);
                this.rowT.draw(canvas, next[3], f9, f26, theme.DIM);
                this.rowT.align(Paint.Align.LEFT);
            } else {
                this.rowT.draw(canvas, WidgetView.fitText(this.rowT, next[0], f10 - w), f7, f26, theme.DIM);
            }
            f14 = f24;
            c = c2;
            f15 = f27;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int size = this.cards.size();
        float max = Math.max(1, getWidth());
        switch (motionEvent.getActionMasked()) {
            case 0:
                float x = motionEvent.getX();
                this.lastX = x;
                this.x0 = x;
                this.y0 = motionEvent.getY();
                this.p0 = this.pos;
                this.drag = false;
                this.vx = 0.0f;
                this.lastT = motionEvent.getEventTime();
                this.gliding = false;
                return size > 0;
            case 1:
                if (this.drag) {
                    glideTo(AoaStrip.settle(this.pos, motionEvent.getEventTime() - this.lastT <= 120 ? this.vx : 0.0f));
                    return true;
                }
                if (Math.hypot(motionEvent.getX() - this.x0, motionEvent.getY() - this.y0) > this.slop * 1.5f || size == 0) {
                    glideTo(Math.round(this.pos));
                    return true;
                }
                float y = motionEvent.getY() / this.h.g.S;
                float x2 = motionEvent.getX() / this.h.g.S;
                if (y > (H() - IND) - 4.0f) {
                    if (this.trackX1 > this.trackX0) {
                        glideTo(AoaStrip.toward(this.pos, AoaStrip.tapIndex((x2 - this.trackX0) / (this.trackX1 - this.trackX0), size), size));
                    }
                    return true;
                }
                String str = this.cards.get(AoaStrip.nearest(this.pos, size)).go;
                if (str != null) {
                    this.tap.go(str);
                }
                return true;
            case 2:
                float x3 = motionEvent.getX() - this.x0;
                float y2 = motionEvent.getY() - this.y0;
                if (!this.drag && Math.abs(x3) > this.slop && Math.abs(x3) > Math.abs(y2)) {
                    this.drag = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (this.drag) {
                    long eventTime = motionEvent.getEventTime();
                    this.vx = (this.vx * 0.4f) + ((((-(motionEvent.getX() - this.lastX)) / max) / ((float) Math.max(1L, eventTime - this.lastT))) * 1000.0f * 0.6f);
                    this.lastX = motionEvent.getX();
                    this.lastT = eventTime;
                    this.pos = this.p0 - (x3 / max);
                    invalidate();
                }
                return true;
            case 3:
                glideTo(Math.round(this.pos));
                return true;
            default:
                return super.onTouchEvent(motionEvent);
        }
    }
}
