package com.jarvis.assist.aosui.home;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.AppGridModel;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.AdvWidgets;
import com.jarvis.assist.aosui.home.HomeData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AdvWidgets {
    static final String[][] TILE_DEF = {new String[]{"email", "EMAIL", "mail"}, new String[]{"calendar", "CAL", "cal"}, new String[]{"sms", "MSG", "msg"}, new String[]{"notes", "NOTES", "list"}, new String[]{"health", "HEALTH", "heart"}, new String[]{"maps", "MAP", "pin"}, new String[]{"music", "MUSIC", "music"}, new String[]{"camera", "CAMERA", "cam"}};
    static final String[] MODES = {"bed", "silent", "life", "work", "focus"};

    private AdvWidgets() {
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class Head extends WidgetView {
        Txt thin;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Head(Context context, HomeCtx homeCtx) {
            super(context, homeCtx);
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public String id() {
            return "advhead";
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public String source() {
            return "aOS · " + Strings.t("adv.theme.adv").toUpperCase(Locale.ROOT);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.jarvis.assist.aosui.home.WidgetView
        public void styles() {
            super.styles();
            float min = Math.min(this.h.g.ph * 0.36f, this.h.g.pw * 0.085f) * this.h.zoom;
            this.thin = new Txt(Fonts.RAJ_400, min, 0.1f * min);
            this.padT = 14.0f;
            this.padL = 14.0f;
            this.padR = 14.0f;
            this.padB = 12.0f;
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected int rows() {
            return 3;
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected float rowH(int i) {
            return ((i != 0 && i == 1) ? this.thin : this.m9).line();
        }

        String[] g3() {
            String str;
            HomeData homeData = this.h.d;
            long currentTimeMillis = System.currentTimeMillis();
            Iterator<HomeData.Ev> it = homeData.events.iterator();
            int i = 0;
            String str2 = "";
            while (it.hasNext()) {
                HomeData.Ev next = it.next();
                if (HomeRules.calDay(next.start, next.end, currentTimeMillis) == 0 && next.end > currentTimeMillis) {
                    i++;
                    if (str2.isEmpty()) {
                        long j = currentTimeMillis;
                        currentTimeMillis = j;
                        str2 = HomeRules.calTime(next.start, next.end, next.allDay, j, "ALL DAY") + " " + next.title;
                    }
                }
            }
            String format = homeData.alarmAt > 0 ? new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(homeData.alarmAt)) : "";
            String mode = Advanced.mode(Day.filter(getContext()), Day.ringer(getContext()));
            String str3 = homeData.greet;
            int length = homeData.notifs != null ? homeData.notifs.length() : 0;
            double optDouble = homeData.goals().optDouble("sleep", 8.0d);
            if (homeData.wxTemp != null && !homeData.wxTemp.isEmpty()) {
                str = homeData.wxTemp + " " + (homeData.wxDesc != null ? homeData.wxDesc : "");
            }
            return Advanced.greeting(mode, str3, i, str2, length, format, optDouble, str);
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public String spoken() {
            String[] g3 = g3();
            return g3[0] + ". " + g3[1] + ". " + g3[2];
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
            HomeData homeData = this.h.d;
            float f5 = f2 + (f4 / 2.0f);
            String[] g3 = g3();
            if (i == 0) {
                this.m9.draw(canvas, "INDEX", f, f5 + (this.m9.cap / 2.0f), col(th().RED));
                this.m9.draw(canvas, g3[0].substring(5), f + this.m9.w("INDEX"), f5 + (this.m9.cap / 2.0f), col(th().DIM));
                this.m9.align(Paint.Align.RIGHT);
                this.m9.draw(canvas, homeData.btPct + "%", f + f3, f5 + (this.m9.cap / 2.0f), col(th().INK));
                this.m9.align(Paint.Align.LEFT);
                return;
            }
            if (i == 1) {
                this.thin.draw(canvas, fitText(this.thin, g3[1], f3), f, f5 + (this.thin.cap / 2.0f), col(th().INK));
                hit(f, f2, f + f3, f2 + f4, "brief", g3[1]);
                return;
            }
            String upperCase = String.format(Locale.getDefault(), "%ta %<tb %<td %<tY", Calendar.getInstance()).toUpperCase(Locale.ROOT);
            if (!g3[2].isEmpty()) {
                upperCase = upperCase + "  ·  " + g3[2];
            }
            this.m9.draw(canvas, fitText(this.m9, upperCase, f3), f, f5 + (this.m9.cap / 2.0f), col(th().DIM));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Tiles extends WidgetView {
        List<String> apps;
        final Runnable inval;
        Txt lab;
        final HashMap<String, String> labels;
        Txt num;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Tiles(Context context, HomeCtx homeCtx) {
            super(context, homeCtx);
            this.apps = new ArrayList();
            this.labels = new HashMap<>();
            this.inval = new Runnable() { // from class: com.jarvis.assist.aosui.home.AdvWidgets$Tiles$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AdvWidgets.Tiles.this.invalidate();
                }
            };
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public String id() {
            return "advtiles";
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public String source() {
            return Strings.t(this.apps.isEmpty() ? "adv.tiles.src" : "w.appgrid.src");
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.jarvis.assist.aosui.home.WidgetView
        public void styles() {
            super.styles();
            this.num = new Txt(Fonts.MONO, this.h.zoom * 9.0f, 0.0f);
            this.lab = new Txt(Fonts.MONO, this.h.zoom * 7.5f, this.h.zoom * 7.5f * 0.2f).align(Paint.Align.CENTER);
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public void bind() {
            this.apps = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
            this.labels.clear();
            String appsCached = this.h.d.be.appsCached();
            if (appsCached != null) {
                try {
                    JSONArray jSONArray = new JSONArray(appsCached);
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject optJSONObject = jSONArray.optJSONObject(i);
                        if (optJSONObject != null) {
                            this.labels.put(optJSONObject.optString("pkg") + "/" + optJSONObject.optString("cls"), optJSONObject.optString("label"));
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected int rows() {
            return 1;
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected float rowH(int i) {
            return (this.H - this.padT) - this.padB;
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
            float f5;
            int i2;
            String str;
            String str2;
            Tiles tiles = this;
            float f6 = 4.0f;
            float f7 = (f3 - 12.0f) / 4.0f;
            float f8 = 2.0f;
            float f9 = (f4 - 4.0f) / 2.0f;
            int i3 = 0;
            int i4 = 0;
            while (i4 < 8) {
                float f10 = f + ((i4 % 4) * (f7 + f6));
                float f11 = f2 + ((i4 / 4) * (f9 + f6));
                tiles.p.setShader(null);
                tiles.p.setStyle(Paint.Style.STROKE);
                tiles.p.setStrokeWidth(1.0f);
                tiles.p.setColor(tiles.col(tiles.th().LINE2));
                float f12 = f10 + f7;
                float f13 = f11 + f9;
                canvas.drawRect(f10, f11, f12, f13, tiles.p);
                int i5 = i4 + 1;
                tiles.num.draw(canvas, String.format(Locale.ROOT, "%02d", Integer.valueOf(i5)), f10 + f6, f11 + f6 + tiles.num.cap, tiles.col(tiles.th().RED));
                float f14 = f10 + (f7 / f8);
                float f15 = f11 + (0.46f * f9);
                float min = Math.min(f7, f9) * 0.32f;
                if (tiles.apps.isEmpty()) {
                    f5 = f8;
                    i2 = i3;
                    String[] strArr = AdvWidgets.TILE_DEF[i4];
                    String str3 = strArr[1];
                    String str4 = "x:tile:" + strArr[i2];
                    Icons.draw(canvas, tiles.p, strArr[2], f14, f15, min, tiles.col(tiles.th().INK));
                    str = str3;
                    str2 = str4;
                } else if (i4 >= tiles.apps.size()) {
                    f5 = f8;
                    i2 = i3;
                    tiles = this;
                    f8 = f5;
                    i3 = i2;
                    i4 = i5;
                    f6 = 4.0f;
                } else {
                    String str5 = tiles.apps.get(i4);
                    int indexOf = str5.indexOf(47);
                    String str6 = tiles.labels.get(str5);
                    if (str6 == null) {
                        str6 = str5.substring(i3, indexOf);
                    }
                    String upperCase = str6.toUpperCase(Locale.ROOT);
                    String str7 = "g" + i4;
                    f5 = f8;
                    Bitmap bitmap = IconCache.get(tiles.getContext()).get(str5.substring(i3, indexOf), str5.substring(indexOf + 1), Math.round(tiles.h.g.S * min), tiles.inval);
                    if (bitmap != null) {
                        float f16 = min / f5;
                        i2 = i3;
                        tiles.rf.set(f14 - f16, f15 - f16, f14 + f16, f15 + f16);
                        tiles.p.setAlpha(Math.round(tiles.alpha * 255.0f));
                        canvas.drawBitmap(bitmap, (Rect) null, tiles.rf, tiles.p);
                        tiles.p.setAlpha(255);
                    } else {
                        i2 = i3;
                    }
                    str = upperCase;
                    str2 = str7;
                }
                tiles.lab.draw(canvas, fitText(tiles.lab, str, f7 - 6.0f), f14, (f13 - 5.0f) - tiles.lab.desc, tiles.col(tiles.th().INK));
                tiles.hit(f10, f11, f12, f13, str2, str);
                tiles = this;
                f8 = f5;
                i3 = i2;
                i4 = i5;
                f6 = 4.0f;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Day extends WidgetView {
        Txt chip;
        String mode;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Day(Context context, HomeCtx homeCtx) {
            super(context, homeCtx);
            this.mode = "life";
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public String id() {
            return "advday";
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public String source() {
            return Strings.t("adv.day.src");
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.jarvis.assist.aosui.home.WidgetView
        public void styles() {
            super.styles();
            this.chip = new Txt(Fonts.MONO, this.h.zoom * 8.5f, this.h.zoom * 8.5f * 0.2f).align(Paint.Align.CENTER);
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        public void bind() {
            this.mode = Advanced.mode(phoneFilter(), phoneRinger());
        }

        static int filter(Context context) {
            try {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
                if (notificationManager == null) {
                    return 1;
                }
                return notificationManager.getCurrentInterruptionFilter();
            } catch (Exception e) {
                return 1;
            }
        }

        static int ringer(Context context) {
            try {
                AudioManager audioManager = (AudioManager) context.getSystemService(AudioManager.class);
                if (audioManager == null) {
                    return 2;
                }
                return audioManager.getRingerMode();
            } catch (Exception e) {
                return 2;
            }
        }

        int phoneFilter() {
            try {
                NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NotificationManager.class);
                if (notificationManager == null) {
                    return 1;
                }
                return notificationManager.getCurrentInterruptionFilter();
            } catch (Exception e) {
                return 1;
            }
        }

        int phoneRinger() {
            try {
                AudioManager audioManager = (AudioManager) getContext().getSystemService(AudioManager.class);
                if (audioManager == null) {
                    return 2;
                }
                return audioManager.getRingerMode();
            } catch (Exception e) {
                return 2;
            }
        }

        float[] hr() {
            JSONArray optJSONArray = this.h.d.health == null ? null : this.h.d.health.optJSONArray("hr_series");
            if (optJSONArray == null || optJSONArray.length() < 4) {
                return null;
            }
            int min = Math.min(48, optJSONArray.length());
            float[] fArr = new float[min];
            for (int i = 0; i < min; i++) {
                JSONArray optJSONArray2 = optJSONArray.optJSONArray((optJSONArray.length() - min) + i);
                fArr[i] = optJSONArray2 == null ? Float.NaN : (float) optJSONArray2.optDouble(1, Double.NaN);
            }
            return fArr;
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected int rows() {
            return hr() == null ? 3 : 4;
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected float rowH(int i) {
            if (i == 0) {
                return headH();
            }
            if (i == 1) {
                return 26.0f;
            }
            if (i != 2 || hr() == null) {
                return this.m9.line() + 4.0f;
            }
            return 30.0f;
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected String readout() {
            HomeData.Metric metric = this.h.d.hm.isEmpty() ? null : this.h.d.hm.get(0);
            if (hr() == null || metric == null || metric.value.equals("—")) {
                return this.h.d.time;
            }
            return metric.value + " BPM";
        }

        @Override // com.jarvis.assist.aosui.home.WidgetView
        protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
            if (i == 0) {
                head(canvas, f, f2, f3, "spark", th().RED, Strings.t("adv.day.h"), null);
                return;
            }
            Day day = this;
            if (i != 1) {
                float[] hr = day.hr();
                if (i == 2 && hr != null) {
                    day.m9.draw(canvas, "HR", f, f2 + (f4 / 2.0f) + (day.m9.cap / 2.0f), day.col(day.th().DIM));
                    day.chart(canvas, hr, f + 22.0f, f2, f3 - 22.0f, f4, day.th().RED);
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                float f5 = ((calendar.get(11) * 60) + calendar.get(12)) / 1440.0f;
                float f6 = f2 + (f4 / 2.0f);
                day.m9.draw(canvas, "00", f, f6 + (day.m9.cap / 2.0f), day.col(day.th().DIM));
                day.m9.align(Paint.Align.RIGHT);
                day.m9.draw(canvas, "24", f + f3, f6 + (day.m9.cap / 2.0f), day.col(day.th().DIM));
                day.m9.align(Paint.Align.LEFT);
                float w = f + day.m9.w("00") + 6.0f;
                float w2 = f3 - ((day.m9.w("00") + 6.0f) * 2.0f);
                Hud.bar(canvas, day.p, day.rf, w, f6, w2, f5, day.th(), day.alpha);
                day.p.setShader(null);
                day.p.setStyle(Paint.Style.FILL);
                day.p.setColor(day.col(-1774607));
                float f7 = w + (w2 * f5);
                canvas.drawRect(f7 - 0.75f, f6 - 5.0f, f7 + 0.75f, f6 + 5.0f, day.p);
                return;
            }
            float f8 = (f3 - 16.0f) / 5.0f;
            int i2 = 0;
            while (i2 < 5) {
                float f9 = f + (i2 * (f8 + 4.0f));
                boolean equals = AdvWidgets.MODES[i2].equals(day.mode);
                day.p.setShader(null);
                if (equals) {
                    day.p.setStyle(Paint.Style.FILL);
                    day.p.setColor(day.col(Theme.a(day.th().RED, 0.85f)));
                    canvas.drawRect(f9, f2, f9 + f8, f2 + f4, day.p);
                }
                day.p.setStyle(Paint.Style.STROKE);
                day.p.setStrokeWidth(1.0f);
                Paint paint = day.p;
                Theme th = day.th();
                paint.setColor(day.col(equals ? th.RED : th.LINE2));
                float f10 = f9 + f8;
                float f11 = f2 + f4;
                canvas.drawRect(f9, f2, f10, f11, day.p);
                String t = Strings.t("adv.mode." + AdvWidgets.MODES[i2]);
                day.chip.draw(canvas, fitText(day.chip, t, f8 - 4.0f), (f8 / 2.0f) + f9, f2 + (f4 / 2.0f) + (day.chip.cap / 2.0f), day.col(equals ? -1 : day.th().INK));
                String str = "x:mode:" + AdvWidgets.MODES[i2];
                String str2 = t + (equals ? ", on" : "");
                Day day2 = day;
                day2.hit(f9, f2, f10, f11, str, str2);
                i2++;
                day = day2;
            }
        }
    }
}
