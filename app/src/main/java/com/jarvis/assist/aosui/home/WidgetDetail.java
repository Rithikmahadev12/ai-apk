package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.Corners;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.GoogleAuth;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeData;
import com.jarvis.assist.aosui.ui.Kit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.function.Function;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class WidgetDetail {
    static final String TAG = "aos-widget-detail";

    private WidgetDetail() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean close(FrameLayout frameLayout) {
        View findViewWithTag = frameLayout.findViewWithTag(TAG);
        if (findViewWithTag == null) {
            return false;
        }
        frameLayout.removeView(findViewWithTag);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v29 */
    /* JADX WARN: Type inference failed for: r11v30, types: [boolean] */
    /* JADX WARN: Type inference failed for: r11v31 */
    public static void show(final FrameLayout frameLayout, final Backend backend, HomeCtx homeCtx, HomeData homeData, String str) {
        char c;
        String str2;
        Context context;
        final String str3;
        String str4;
        JSONArray jSONArray;
        int i;
        String str5;
        String str6;
        String str7;
        final FrameLayout frameLayout2;
        close(frameLayout);
        Context context2 = frameLayout.getContext();
        Kit kit = new Kit(context2, homeCtx.th, homeCtx.zoom);
        float f = context2.getResources().getDisplayMetrics().density;
        ScrollView scrollView = new ScrollView(context2);
        scrollView.setTag(TAG);
        scrollView.setBackground(new Kit.GridBg(kit.BG, Theme.a(kit.LINE2, 0.5f), f));
        scrollView.setClickable(true);
        LinearLayout linearLayout = new LinearLayout(context2);
        linearLayout.setOrientation(1);
        int round = Math.round(16.0f * f);
        linearLayout.setPadding(round, Math.round(36.0f * f), round, round);
        scrollView.addView(linearLayout);
        int[] iArr = {0};
        String t = Strings.t("wid." + str);
        if (t.startsWith("wid.")) {
            t = str;
        }
        String str8 = " · ";
        linearLayout.addView(kit.header(t.toUpperCase(Locale.ROOT), homeData.time + " · " + homeData.date, new Runnable() { // from class: com.jarvis.assist.aosui.home.WidgetDetail$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WidgetDetail.close(frameLayout);
            }
        }, Strings.t("back")));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEE d", Locale.getDefault());
        switch (str.hashCode()) {
            case -1884274053:
                if (str.equals("storage")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -1655966961:
                if (str.equals("activity")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1415196606:
                if (str.equals("alarms")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1221262756:
                if (str.equals("health")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1039689917:
                if (str.equals("notifs")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -934918565:
                if (str.equals("recent")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -331239923:
                if (str.equals("battery")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -178324674:
                if (str.equals("calendar")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 98466462:
                if (str.equals("gmail")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 103772132:
                if (str.equals("media")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 109522647:
                if (str.equals("sleep")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 461450172:
                if (str.equals("worldclock")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 1223440372:
                if (str.equals("weather")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        String str9 = "";
        str2 = "—";
        switch (c) {
            case 0:
                context = context2;
                JSONObject obj = Store.obj("aos.wx");
                LinearLayout card = card(kit, linearLayout, iArr, "NOW", homeData.wxTemp + " " + homeData.wxDesc);
                JSONArray optJSONArray = obj.optJSONArray("hourly");
                if (optJSONArray != null) {
                    int i2 = 0;
                    while (i2 < optJSONArray.length()) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                        if (optJSONObject != null) {
                            jSONArray = optJSONArray;
                            i = i2;
                            row(kit, card, simpleDateFormat.format(new Date(optJSONObject.optLong("ts"))), deg(optJSONObject.opt("t")));
                        } else {
                            jSONArray = optJSONArray;
                            i = i2;
                        }
                        i2 = i + 1;
                        optJSONArray = jSONArray;
                    }
                }
                LinearLayout card2 = card(kit, linearLayout, iArr, "7 DAYS", "");
                JSONArray optJSONArray2 = obj.optJSONArray("daily");
                if (optJSONArray2 != null) {
                    int i3 = 0;
                    while (i3 < optJSONArray2.length()) {
                        JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i3);
                        if (optJSONObject2 == null) {
                            str4 = str2;
                        } else {
                            str4 = str2;
                            row(kit, card2, simpleDateFormat2.format(new Date(optJSONObject2.optLong("ts"))).toUpperCase(Locale.ROOT), optJSONObject2.optInt("hi") + "° / " + optJSONObject2.optInt("lo") + "°" + (optJSONObject2.has("pop") ? "  ·  RAIN " + optJSONObject2.optInt("pop") + "%" : ""));
                        }
                        i3++;
                        str2 = str4;
                    }
                }
                String str10 = str2;
                JSONObject optJSONObject3 = optJSONArray2 != null ? optJSONArray2.optJSONObject(0) : null;
                if (optJSONObject3 != null && optJSONObject3.has("rise")) {
                    LinearLayout card3 = card(kit, linearLayout, iArr, "SUN", "");
                    row(kit, card3, "SUNRISE", simpleDateFormat.format(new Date(optJSONObject3.optLong("rise"))));
                    row(kit, card3, "SUNSET", simpleDateFormat.format(new Date(optJSONObject3.optLong("set"))));
                }
                kit.add(card(kit, linearLayout, iArr, "WIND · UV", str10), kit.fine(Strings.t("wd.wx.more")));
                str3 = "weather";
                break;
            case 1:
                context = context2;
                LinearLayout card4 = card(kit, linearLayout, iArr, "MONTH", new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date()).toUpperCase(Locale.ROOT));
                Calendar calendar = Calendar.getInstance();
                int i4 = calendar.get(5);
                calendar.set(5, 1);
                int i5 = (calendar.get(7) + 5) % 7;
                int actualMaximum = calendar.getActualMaximum(5);
                StringBuilder sb = new StringBuilder("MO TU WE TH FR SA SU\n");
                for (int i6 = 0; i6 < i5; i6++) {
                    sb.append("   ");
                }
                int i7 = 1;
                while (i7 <= actualMaximum) {
                    Locale locale = Locale.ROOT;
                    Object[] objArr = {Integer.valueOf(i7)};
                    sb.append(i7 == i4 ? String.format(locale, "[%d]", objArr).substring(0, Math.min(3, String.format(Locale.ROOT, "[%d]", Integer.valueOf(i7)).length())) : String.format(locale, "%2d ", objArr));
                    if ((i5 + i7) % 7 == 0) {
                        sb.append('\n');
                    }
                    i7++;
                }
                TextView text = kit.text(sb.toString(), Fonts.MONO, 12.0f, 0.1f, kit.INK);
                text.setSingleLine(false);
                card4.addView(text);
                LinearLayout card5 = card(kit, linearLayout, iArr, "COMING UP", new StringBuilder().append(homeData.events.size()).toString());
                long currentTimeMillis = System.currentTimeMillis();
                Iterator<HomeData.Ev> it = homeData.events.iterator();
                while (it.hasNext()) {
                    HomeData.Ev next = it.next();
                    row(kit, card5, HomeRules.calTime(next.start, next.end, next.allDay, currentTimeMillis, "ALL DAY"), next.title);
                }
                if (homeData.events.isEmpty()) {
                    row(kit, card5, "—", Strings.t("w.calendar.none"));
                }
                str3 = "calendar";
                break;
            case 2:
            case 3:
            case 4:
                context = context2;
                LinearLayout card6 = card(kit, linearLayout, iArr, "TODAY", homeCtx.d.healthOff ? "OFFLINE" : "");
                Iterator<HomeData.Metric> it2 = homeData.hm.iterator();
                boolean z = false;
                while (it2.hasNext()) {
                    HomeData.Metric next2 = it2.next();
                    bar(kit, card6, next2.label, next2.value, next2.frac);
                    z |= (next2.value.equals("—") || next2.value.equals(Strings.t("w.health.nodata"))) ? false : true;
                }
                if (!z) {
                    note(kit, card6, Strings.t("wd.health.none"));
                }
                JSONObject optJSONObject4 = homeData.health != null ? homeData.health.optJSONObject("sleep") : null;
                if (optJSONObject4 != null && optJSONObject4.has("min")) {
                    str2 = HomeData.fmtMin(Double.valueOf(optJSONObject4.optDouble("min")));
                }
                LinearLayout card7 = card(kit, linearLayout, iArr, "SLEEP", str2);
                if (optJSONObject4 != null && optJSONObject4.optLong("start") > 0) {
                    row(kit, card7, "BED", HomeData.hm(optJSONObject4.optLong("start") * 1000));
                    row(kit, card7, "WAKE", HomeData.hm(optJSONObject4.optLong("end") * 1000));
                }
                if (optJSONObject4 != null && optJSONObject4.has("q")) {
                    row(kit, card7, "QUALITY", optJSONObject4.optInt("q") + "%");
                }
                if (optJSONObject4 == null || !optJSONObject4.has("min")) {
                    note(kit, card7, Strings.t("wd.sleep.none"));
                }
                src(kit, card7, homeCtx.d.healthOff ? "HEALTH · NOT CONNECTED" : "HEALTH CONNECT");
                str3 = "health";
                break;
            case 5:
                context = context2;
                LinearLayout card8 = card(kit, linearLayout, iArr, "BY APP", homeData.notifs == null ? "NO ACCESS" : homeData.notifs.length() + " NEW");
                TreeMap treeMap = new TreeMap();
                if (homeData.notifs != null) {
                    int i8 = 0;
                    while (i8 < homeData.notifs.length()) {
                        JSONObject optJSONObject5 = homeData.notifs.optJSONObject(i8);
                        if (optJSONObject5 == null) {
                            str6 = str9;
                        } else {
                            List list = (List) treeMap.computeIfAbsent(optJSONObject5.optString("app").toUpperCase(Locale.ROOT), new Function() { // from class: com.jarvis.assist.aosui.home.WidgetDetail$$ExternalSyntheticLambda1
                                @Override // java.util.function.Function
                                public final Object apply(Object obj2) {
                                    return WidgetDetail.lambda$show$1((String) obj2);
                                }
                            });
                            if (optJSONObject5.optLong("at") > 0) {
                                str6 = str9;
                                str7 = simpleDateFormat.format(new Date(optJSONObject5.optLong("at"))) + "  ";
                            } else {
                                str6 = str9;
                                str7 = str6;
                            }
                            list.add(str7 + (optJSONObject5.optString("title").isEmpty() ? optJSONObject5.optString("text") : optJSONObject5.optString("title")));
                        }
                        i8++;
                        str9 = str6;
                    }
                }
                for (java.util.Map.Entry<String, java.util.List> entry : ((java.util.TreeMap<String, java.util.List>) treeMap).entrySet()) {
                    card8.addView(kit.section(((String) entry.getKey()) + str8 + ((List) entry.getValue()).size()));
                    Iterator it3 = ((List) entry.getValue()).iterator();
                    while (it3.hasNext()) {
                        TextView text2 = kit.text((String) it3.next(), Fonts.RAJ_400, 14.0f, 0.0f, kit.INK);
                        text2.setPadding(0, 0, 0, Math.round(6.0f * f));
                        card8.addView(text2);
                        str8 = str8;
                    }
                }
                if (homeData.notifs != null) {
                    str5 = treeMap.isEmpty() ? "wd.notifs.none" : "wd.notifs.perm";
                    src(kit, card8, "NOTIFICATION SHADE");
                    str3 = null;
                    break;
                }
                note(kit, card8, Strings.t(str5));
                src(kit, card8, "NOTIFICATION SHADE");
                str3 = null;
            case 6:
                context = context2;
                LinearLayout card9 = card(kit, linearLayout, iArr, "WORLD", homeData.time);
                Iterator<String> it4 = homeData.clocks().iterator();
                while (it4.hasNext()) {
                    String next3 = it4.next();
                    Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone(next3));
                    row(kit, card9, next3.substring(next3.lastIndexOf(47) + 1).replace('_', ' ').toUpperCase(Locale.ROOT), HomeData.p2(calendar2.get(11)) + ":" + HomeData.p2(calendar2.get(12)));
                }
                LinearLayout card10 = card(kit, linearLayout, iArr, "ALARMS", "");
                row(kit, card10, "NEXT", homeData.alarmAt > 0 ? simpleDateFormat.format(new Date(homeData.alarmAt)) : "—");
                if (homeData.alarmAt <= 0) {
                    note(kit, card10, Strings.t("wd.alarm.none"));
                }
                str3 = "clock";
                break;
            case 7:
                context = context2;
                LinearLayout card11 = card(kit, linearLayout, iArr, "NEXT ALARM", homeData.alarmAt > 0 ? simpleDateFormat.format(new Date(homeData.alarmAt)) : "—");
                if (homeData.alarmAt > 0) {
                    row(kit, card11, "DAY", simpleDateFormat2.format(new Date(homeData.alarmAt)).toUpperCase(Locale.ROOT));
                    row(kit, card11, "IN", Corners.dur(homeData.alarmAt - System.currentTimeMillis()));
                } else {
                    note(kit, card11, Strings.t("wd.alarm.none"));
                }
                src(kit, card11, "SYSTEM CLOCK");
                str3 = "clock";
                break;
            case '\b':
                context = context2;
                LinearLayout card12 = card(kit, linearLayout, iArr, "RECENT APPS", homeData.recent.isEmpty() ? "—" : String.valueOf(homeData.recent.size()));
                int i9 = 0;
                while (i9 < homeData.recent.size()) {
                    int i10 = i9 + 1;
                    row(kit, card12, String.format(Locale.ROOT, "%02d", Integer.valueOf(i10)), homeData.recent.get(i9).label);
                    i9 = i10;
                }
                if (homeData.recent.isEmpty()) {
                    note(kit, card12, Strings.t("wd.recent.none"));
                }
                src(kit, card12, "AOS LAUNCH HISTORY");
                str3 = null;
                break;
            case '\t':
                context = context2;
                LinearLayout card13 = card(kit, linearLayout, iArr, "STORAGE", homeData.stTotal > 0 ? StorageWidget.gb(homeData.stFree) + " FREE" : "—");
                if (homeData.stTotal > 0) {
                    bar(kit, card13, "USED", Math.round((1.0f - (((float) homeData.stFree) / ((float) homeData.stTotal))) * 100.0f) + "%", 1.0f - (((float) homeData.stFree) / ((float) homeData.stTotal)));
                    row(kit, card13, "TOTAL", StorageWidget.gb(homeData.stTotal));
                } else {
                    note(kit, card13, Strings.t("w.storage.none"));
                }
                src(kit, card13, "THIS PHONE");
                str3 = null;
                break;
            case '\n':
                context = context2;
                LinearLayout card14 = card(kit, linearLayout, iArr, "PHONE", homeData.btPct + "%");
                bar(kit, card14, "LEVEL", homeData.btPct + "%", homeData.btPct / 100.0f);
                row(kit, card14, "STATE", homeData.btLabel);
                if (homeData.stTotal > 0) {
                    bar(kit, card(kit, linearLayout, iArr, "STORAGE", StorageWidget.gb(homeData.stFree) + " FREE"), "USED", Math.round((1.0f - (((float) homeData.stFree) / ((float) homeData.stTotal))) * 100.0f) + "%", 1.0f - (((float) homeData.stFree) / ((float) homeData.stTotal)));
                }
                str3 = null;
                break;
            case 11:
                JSONObject optJSONObject6 = Store.obj(GoogleAuth.CACHE).optJSONObject("gmail");
                LinearLayout card15 = card(kit, linearLayout, iArr, "INBOX", optJSONObject6 == null ? "—" : optJSONObject6.optInt("unread") + " UNREAD");
                JSONArray optJSONArray3 = optJSONObject6 != null ? optJSONObject6.optJSONArray("top") : null;
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("d MMM HH:mm", Locale.getDefault());
                if (optJSONArray3 != null) {
                    int i11 = 0;
                    while (i11 < optJSONArray3.length()) {
                        JSONObject optJSONObject7 = optJSONArray3.optJSONObject(i11);
                        row(kit, card15, optJSONObject7.optString("from").toUpperCase(Locale.ROOT) + " · " + simpleDateFormat3.format(new Date(optJSONObject7.optLong("at"))), optJSONObject7.optString("subject"));
                        i11++;
                        optJSONArray3 = optJSONArray3;
                        context2 = context2;
                    }
                }
                context = context2;
                JSONArray optJSONArray4 = Store.obj(GoogleAuth.CACHE).optJSONArray("drive");
                LinearLayout card16 = card(kit, linearLayout, iArr, "RECENT DRIVE FILES", optJSONArray4 != null ? String.valueOf(optJSONArray4.length()) : "—");
                if (optJSONArray4 != null) {
                    for (int i12 = 0; i12 < optJSONArray4.length(); i12++) {
                        JSONObject optJSONObject8 = optJSONArray4.optJSONObject(i12);
                        row(kit, card16, optJSONObject8.optString("at").replace('T', ' ').replaceAll("\\..*", ""), optJSONObject8.optString("name"));
                    }
                }
                if (optJSONObject6 == null && optJSONArray4 == null) {
                    note(kit, card15, Strings.t("wd.gmail.none"));
                }
                src(kit, card16, "GOOGLE · READ-ONLY");
                str3 = "email";
                break;
            case '\f':
                JSONObject jSONObject = homeData.media;
                LinearLayout card17 = card(kit, linearLayout, iArr, "NOW PLAYING", jSONObject == null ? "IDLE" : jSONObject.optBoolean("playing") ? "PLAYING" : "PAUSED");
                if (jSONObject != null) {
                    row(kit, card17, "TITLE", jSONObject.optString("title"));
                    row(kit, card17, "ARTIST", jSONObject.optString("artist"));
                    row(kit, card17, "APP", jSONObject.optString("app"));
                } else {
                    note(kit, card17, Strings.t(homeData.mediaAccess ? "wd.media.none" : "wd.media.perm"));
                }
                src(kit, card17, "ACTIVE MEDIA SESSION");
                str3 = "music";
                context = context2;
                break;
            default:
                context = context2;
                note(kit, card(kit, linearLayout, iArr, "DETAILS", ""), Strings.t("wd.none"));
                str3 = null;
                break;
        }
        LinearLayout linearLayout2 = new LinearLayout(context);
        if (str3 != null) {
            final FrameLayout frameLayout3 = frameLayout;
            boolean r11 = false;
            linearLayout2.addView(kit.btn(Strings.t("wd.open"), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.home.WidgetDetail$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    WidgetDetail.lambda$show$2(frameLayout3, backend, str3);
                }
            }));
            frameLayout2 = frameLayout3;
        } else {
            r11 = false;
            frameLayout2 = frameLayout;
        }
        linearLayout2.addView(kit.btn(Strings.t("wd.close"), true, r11, new Runnable() { // from class: com.jarvis.assist.aosui.home.WidgetDetail$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                WidgetDetail.close(frameLayout2);
            }
        }));
        View[] viewArr = new View[1];
        viewArr[0] = linearLayout2;
        linearLayout.addView(kit.hrow(viewArr));
        frameLayout2.addView(scrollView, new FrameLayout.LayoutParams(-1, -1));
        scrollView.setAlpha(0.0f);
        scrollView.animate().alpha(1.0f).setDuration(180L).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ List lambda$show$1(String str) {
        return new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$show$2(FrameLayout frameLayout, Backend backend, String str) {
        close(frameLayout);
        backend.launchRole(str);
    }

    static String deg(Object obj) {
        if (!(obj instanceof Number)) {
            return "—";
        }
        return Math.round(((Number) obj).doubleValue()) + "°";
    }

    static LinearLayout card(Kit kit, LinearLayout linearLayout, int[] iArr, String str, String str2) {
        LinearLayout card = kit.card(true, false);
        LinearLayout linearLayout2 = new LinearLayout(linearLayout.getContext());
        linearLayout2.setGravity(16);
        Locale locale = Locale.ROOT;
        String str3 = Kit.ADV ? "INDEX: %02d · %s" : "%02d / %s";
        int i = iArr[0] + 1;
        iArr[0] = i;
        linearLayout2.addView(kit.text(String.format(locale, str3, Integer.valueOf(i), str), Fonts.MONO, 10.0f, 0.2f, kit.INK), new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (str2 != null && !str2.isEmpty()) {
            linearLayout2.addView(kit.text(str2, Fonts.RAJ_400, 20.0f, 0.04f, kit.INK));
        }
        card.addView(linearLayout2);
        linearLayout.addView(card);
        return card;
    }

    static void note(Kit kit, LinearLayout linearLayout, String str) {
        TextView text = kit.text(str, Fonts.RAJ_400, 14.0f, 0.0f, kit.DIM);
        text.setPadding(0, Math.round(kit.dn * 8.0f), 0, Math.round(kit.dn * 4.0f));
        linearLayout.addView(text);
    }

    static void src(Kit kit, LinearLayout linearLayout, String str) {
        TextView text = kit.text(Strings.t("wd.src", "src", str.toUpperCase(Locale.ROOT)), Fonts.MONO, 8.0f, 0.14f, kit.DIM);
        text.setPadding(0, Math.round(kit.dn * 10.0f), 0, 0);
        linearLayout.addView(text);
    }

    static void row(Kit kit, LinearLayout linearLayout, String str, String str2) {
        LinearLayout linearLayout2 = new LinearLayout(linearLayout.getContext());
        linearLayout2.setPadding(0, 6, 0, 6);
        linearLayout2.addView(kit.text(str, Fonts.MONO, 10.0f, 0.14f, kit.DIM), new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (str2 == null || str2.isEmpty()) {
            str2 = "—";
        }
        linearLayout2.addView(kit.text(str2, Fonts.RAJ_600, 15.0f, 0.0f, kit.INK));
        linearLayout.addView(linearLayout2);
    }

    static void bar(final Kit kit, LinearLayout linearLayout, String str, String str2, final float f) {
        row(kit, linearLayout, str, str2);
        linearLayout.addView(new View(linearLayout.getContext()) { // from class: com.jarvis.assist.aosui.home.WidgetDetail.1
            final Paint pp = new Paint(1);
            final RectF rf = new RectF();

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                Hud.bar(canvas, this.pp, this.rf, 0.0f, getHeight() / 2.0f, getWidth(), f, kit.th, 1.0f);
            }
        }, new LinearLayout.LayoutParams(-1, Math.round(linearLayout.getResources().getDisplayMetrics().density * 8.0f)));
    }
}
