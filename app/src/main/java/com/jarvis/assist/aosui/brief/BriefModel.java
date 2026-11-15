package com.jarvis.assist.aosui.brief;

import com.jarvis.assist.aosui.core.Caps;
import com.jarvis.assist.aosui.core.DayPart;
import com.jarvis.assist.aosui.core.HomeRules;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class BriefModel {
    static final long DAY = 86400000;
    public static final int FREE_H0 = 8;
    public static final int FREE_H1 = 20;
    static final long HOUR = 3600000;
    static final long MIN = 60000;
    public static final String[] SECTIONS = {"weather", "day", "health", "notifs", "phone", "lists"};
    static final String[] MON = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    static final String[] WD = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    static final Map<String, String> HSRC = new LinkedHashMap();
    static final Map<String, String> WXD = new LinkedHashMap();
    static final Map<String, String> WXN = new LinkedHashMap();

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Cfg {
        public boolean speak;
        public int at = DayPart.EVENING;
        public final Set<String> off = new HashSet();
        public String name = "";
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Ev {
        public boolean allDay;
        public int day;
        public long end;
        public boolean hol;
        public boolean next;
        public boolean now;
        public boolean rem;
        public long start;
        public String title = "";
        public String where = "";
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class In {
        public long alarmAt;
        public JSONObject cal;
        public boolean charging;
        public boolean fahrenheit;
        public long free;
        public JSONObject health;
        public JSONArray notifs;
        public boolean notifsRead;
        public long now;
        public JSONObject screen;
        public long total;
        public String wake;
        public JSONObject week;
        public JSONObject wx;
        public boolean wxNeedLoc;
        public int switchMin = DayPart.EVENING;
        public String mode = "auto";
        public String name = "";
        public String aiLine = "";
        public String wxErr = "";
        public double stepsGoal = 8000.0d;
        public double kcalGoal = 500.0d;
        public double sleepGoalH = 8.0d;
        public String aiNotifs = "nokey";
        public int batt = -1;
        public List<String> shop = new ArrayList();
        public Set<String> shopDone = new HashSet();
        public Set<String> off = new HashSet();
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Tr {
        String t(String str);
    }

    private BriefModel() {
    }

    public static String f(Tr tr, String str, Object... objArr) {
        String t = tr.t(str);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i2 >= objArr.length) {
                return t;
            }
            t = t.replace("{" + String.valueOf(objArr[i]) + "}", String.valueOf(objArr[i2]));
            i += 2;
        }
    }

    static {
        HSRC.put("hc", "HEALTH CONNECT");
        HSRC.put("whoop", "WHOOP");
        HSRC.put("oura", "OURA");
        HSRC.put("uh", "ULTRAHUMAN");
        HSRC.put("apple", "APPLE HEALTH");
        String[] strArr = {"sun", "SUNNY", "moon", "CLEAR", "partly", "PARTLY", "cloud", "CLOUDY", "fog", "FOG", "rain", "RAIN", "snow", "SNOW", "storm", "STORM"};
        for (int i = 0; i < 16; i += 2) {
            WXD.put(strArr[i], strArr[i + 1]);
        }
        WXN.put("openmeteo", "OPEN-METEO");
        WXN.put("met", "MET NORWAY");
        WXN.put("owm", "OPENWEATHERMAP");
        WXN.put("wapi", "WEATHERAPI.COM");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.jarvis.assist.aosui.brief.BriefModel.Cfg cfg(java.lang.String r6) {
        /*
            com.jarvis.assist.aosui.brief.BriefModel$Cfg r0 = new com.jarvis.assist.aosui.brief.BriefModel$Cfg
            r0.<init>()
            if (r6 == 0) goto L14
            boolean r1 = r6.isEmpty()     // Catch: java.lang.Exception -> L1a
            if (r1 == 0) goto Le
            goto L14
        Le:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch: java.lang.Exception -> L1a
            r1.<init>(r6)     // Catch: java.lang.Exception -> L1a
            goto L20
        L14:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch: java.lang.Exception -> L1a
            r1.<init>()     // Catch: java.lang.Exception -> L1a
            goto L20
        L1a:
            r6 = move-exception
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>()
        L20:
            r6 = 1020(0x3fc, float:1.43E-42)
            java.lang.String r6 = hm(r6)
            java.lang.String r2 = "at"
            java.lang.String r6 = r1.optString(r2, r6)
            int r6 = minutes(r6)
            if (r6 < 0) goto L34
            r0.at = r6
        L34:
            java.lang.String r6 = "off"
            org.json.JSONArray r6 = r1.optJSONArray(r6)
            r2 = 0
            if (r6 == 0) goto L50
            r3 = r2
        L3e:
            int r4 = r6.length()
            if (r3 >= r4) goto L50
            java.util.Set<java.lang.String> r4 = r0.off
            java.lang.String r5 = r6.optString(r3)
            r4.add(r5)
            int r3 = r3 + 1
            goto L3e
        L50:
            java.lang.String r6 = "speak"
            boolean r6 = r1.optBoolean(r6)
            r0.speak = r6
            java.lang.String r6 = "name"
            java.lang.String r3 = ""
            java.lang.String r6 = r1.optString(r6, r3)
            java.lang.String r6 = r6.trim()
            r0.name = r6
            java.lang.String r6 = r0.name
            int r6 = r6.length()
            r1 = 40
            if (r6 <= r1) goto L78
            java.lang.String r6 = r0.name
            java.lang.String r6 = r6.substring(r2, r1)
            r0.name = r6
        L78:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.brief.BriefModel.cfg(java.lang.String):com.jarvis.assist.aosui.brief.BriefModel$Cfg");
    }

    public static String save(Cfg cfg) {
        try {
            JSONArray jSONArray = new JSONArray();
            for (String str : SECTIONS) {
                if (cfg.off.contains(str)) {
                    jSONArray.put(str);
                }
            }
            return new JSONObject().put("at", hm(cfg.at)).put("off", jSONArray).put("speak", cfg.speak).put("name", cfg.name).toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Card {
        public String fix;
        public final String id;
        public boolean show;
        public String src = "";

        Card(String str) {
            this.id = str;
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Out {
        public String alarmSuggest;
        public boolean calFix;
        public boolean charging;
        public float[] dayArc;
        public boolean evening;
        public long free;
        public int nCount;
        public boolean readyRec;
        public float[] sleepArc;
        public boolean stFix;
        public float stepsFrac;
        public long total;
        public String time = "";
        public String date = "";
        public String greet = "";
        public String summary = "";
        public String summarySrc = "";
        public final Card weather = new Card("weather");
        public final Card day = new Card("day");
        public final Card health = new Card("health");
        public final Card notifs = new Card("notifs");
        public final Card phone = new Card("phone");
        public final Card lists = new Card("lists");
        public String wxHi = "";
        public String wxLo = "";
        public String wxTemp = "";
        public String wxDesc = "";
        public String wxCode = "cloud";
        public String wxHiLo = "";
        public String wxTmr = "";
        public String wxTmrCode = "cloud";
        public final List<String[]> hours = new ArrayList();
        public final List<Ev> events = new ArrayList();
        public String alarm = "";
        public String freeSlots = "";
        public String sleep = "";
        public String sleepQ = "";
        public String sleepBed = "";
        public String ready = "";
        public String kcal = "";
        public int steps = -1;
        public int stepsGoal = 8000;
        public int sleepScore = -1;
        public int readyN = -1;
        public int kcalN = -1;
        public int kcalGoal = Caps.MSGS;
        public int sleepMin = -1;
        public float sleepFrac = Float.NaN;
        public float[] sleep7 = new float[7];
        public float[] steps7 = new float[7];
        public final LinkedHashMap<String, String> hsrc = new LinkedHashMap<>();
        public final Set<String> healthNoData = new LinkedHashSet();
        public String healthHint = "";
        public final List<String[]> byApp = new ArrayList();
        public final List<String[]> top = new ArrayList();
        public String aiNotifs = "nokey";
        public int batt = -1;
        public long screenMs = -1;
        public final List<String[]> topApps = new ArrayList();
        public final List<Ev> reminders = new ArrayList();
        public final List<String> shop = new ArrayList();
        public final Set<String> shopDone = new HashSet();
        public final List<float[]> arcs = new ArrayList();
        public String sleepShort = "";
        public String ringNext = "";
        public int nextIn = -1;
        public String firstTmr = "";

        public List<Card> cards() {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.weather);
            arrayList.add(this.day);
            arrayList.add(this.health);
            arrayList.add(this.notifs);
            arrayList.add(this.phone);
            arrayList.add(this.lists);
            return arrayList;
        }
    }

    static String p2(int i) {
        return (i < 10 ? "0" : "") + i;
    }

    public static String hm(int i) {
        int i2 = ((i % 1440) + 1440) % 1440;
        return p2(i2 / 60) + ":" + p2(i2 % 60);
    }

    public static String hm(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return p2(calendar.get(11)) + ":" + p2(calendar.get(12));
    }

    public static int minutes(String str) {
        if (str == null) {
            return -1;
        }
        Matcher matcher = Pattern.compile("^\\s*(\\d{1,2})[:.](\\d{2})").matcher(str);
        if (!matcher.find()) {
            return -1;
        }
        int parseInt = Integer.parseInt(matcher.group(1));
        int parseInt2 = Integer.parseInt(matcher.group(2));
        if (parseInt > 23 || parseInt2 > 59) {
            return -1;
        }
        return (parseInt * 60) + parseInt2;
    }

    static String ymd(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return calendar.get(1) + "-" + p2(calendar.get(2) + 1) + "-" + p2(calendar.get(5));
    }

    public static int minuteOfDay(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return (calendar.get(11) * 60) + calendar.get(12);
    }

    public static long midnight(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    static long addDays(long j, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar.add(5, i);
        return calendar.getTimeInMillis();
    }

    static String deg(Object obj, boolean z) {
        if (!(obj instanceof Number)) {
            return "—";
        }
        double doubleValue = ((Number) obj).doubleValue();
        if (z) {
            doubleValue = ((doubleValue * 9.0d) / 5.0d) + 32.0d;
        }
        return Math.round(doubleValue) + "°";
    }

    static String code(String str) {
        return WXD.containsKey(str) ? str : "cloud";
    }

    static String up(String str) {
        return str == null ? "" : str.toUpperCase(Locale.ROOT);
    }

    static String srcName(String str) {
        String str2 = HSRC.get(str);
        return str2 == null ? "" : str2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long ms(long j) {
        return (j <= 0 || j >= 100000000000L) ? j : j * 1000;
    }

    public static float frac(double d, double d2) {
        if (d < 0.0d || d2 <= 0.0d) {
            return Float.NaN;
        }
        return (float) Math.max(0.0d, Math.min(1.0d, d / d2));
    }

    public static String bytes(long j) {
        double d = j / 1.0E9d;
        if (d < 100.0d) {
            return String.format(Locale.US, "%.1f GB", Double.valueOf(d));
        }
        return Math.round(d) + " GB";
    }

    public static String dur(long j) {
        return HomeRules.dur(j);
    }

    public static String steps(int i) {
        return i >= 1000 ? String.format(Locale.US, "%.1fK", Float.valueOf(i / 1000.0f)) : new StringBuilder().append(i).toString();
    }

    static Integer num(JSONObject jSONObject, String str) {
        if (jSONObject == null || !jSONObject.has(str) || jSONObject.isNull(str) || !(jSONObject.opt(str) instanceof Number)) {
            return null;
        }
        return Integer.valueOf((int) Math.round(jSONObject.optDouble(str)));
    }

    static String fmtMin(double d) {
        return ((int) Math.floor(d / 60.0d)) + "H " + p2((int) Math.round(d % 60.0d)) + "M";
    }

    public static boolean evening(long j, int i, String str) {
        if ("today".equals(str)) {
            return false;
        }
        if ("tonight".equals(str)) {
            return true;
        }
        return DayPart.evening(minuteOfDay(j), i);
    }

    /* JADX WARN: Removed duplicated region for block: B:161:0x0455  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0876  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x089a  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x09d9  */
    /* JADX WARN: Removed duplicated region for block: B:408:0x0a20  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x0a80  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x0a91  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x0bd6  */
    /* JADX WARN: Removed duplicated region for block: B:452:0x0c67  */
    /* JADX WARN: Removed duplicated region for block: B:456:0x0c8d  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x0cfb  */
    /* JADX WARN: Removed duplicated region for block: B:491:0x0d48  */
    /* JADX WARN: Removed duplicated region for block: B:513:0x0c6a  */
    /* JADX WARN: Removed duplicated region for block: B:519:0x0c42  */
    /* JADX WARN: Removed duplicated region for block: B:520:0x0aa2  */
    /* JADX WARN: Removed duplicated region for block: B:548:0x0a83  */
    /* JADX WARN: Removed duplicated region for block: B:549:0x0a67  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.jarvis.assist.aosui.brief.BriefModel.Out build(com.jarvis.assist.aosui.brief.BriefModel.In r52, com.jarvis.assist.aosui.brief.BriefModel.Tr r53) {
        /*
            Method dump skipped, instructions count: 3449
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.brief.BriefModel.build(com.jarvis.assist.aosui.brief.BriefModel$In, com.jarvis.assist.aosui.brief.BriefModel$Tr):com.jarvis.assist.aosui.brief.BriefModel$Out");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$build$1(LinkedHashMap linkedHashMap, String str, String str2) {
        return ((Integer) linkedHashMap.get(str2)).intValue() - ((Integer) linkedHashMap.get(str)).intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$build$2(Object[] objArr, Object[] objArr2) {
        return ((Integer) objArr[0]).intValue() != ((Integer) objArr2[0]).intValue() ? Integer.compare(((Integer) objArr[0]).intValue(), ((Integer) objArr2[0]).intValue()) : Long.compare(((Long) objArr2[1]).longValue(), ((Long) objArr[1]).longValue());
    }

    static String hh(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return p2(calendar.get(11));
    }

    static long ceil15(long j) {
        return (((j + 900000) - 1) / 900000) * 900000;
    }

    static void put(Out out, JSONObject jSONObject, String str) {
        String srcName = jSONObject == null ? "" : srcName(jSONObject.optString(str));
        if (srcName.isEmpty()) {
            return;
        }
        out.hsrc.put(str, srcName);
    }

    public static List<long[]> freeTime(List<JSONObject> list, long j, long j2, long j3, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        long ceil15 = ceil15(j);
        for (long midnight = midnight(j); midnight < j2; midnight = addDays(midnight, 1)) {
            arrayList.addAll(freeSlots(list, Math.max(ceil15, (i * HOUR) + midnight), Math.min(j2, (i2 * HOUR) + midnight), j3));
        }
        return arrayList;
    }

    public static List<long[]> freeSlots(List<JSONObject> list, long j, long j2, long j3) {
        long j4 = j;
        ArrayList<long[]> arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (j2 <= j4) {
            return arrayList2;
        }
        for (JSONObject jSONObject : list) {
            if (!jSONObject.optBoolean("allDay") && !jSONObject.optBoolean("rem")) {
                long max = Math.max(j4, jSONObject.optLong("start"));
                long min = Math.min(j2, jSONObject.optLong("end"));
                if (min > max) {
                    arrayList.add(new long[]{max, min});
                }
            }
        }
        arrayList.sort(new Comparator() { // from class: com.jarvis.assist.aosui.brief.BriefModel$$ExternalSyntheticLambda3
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = Long.compare(((long[]) obj)[0], ((long[]) obj2)[0]);
                return compare;
            }
        });
        for (long[] jArr : arrayList) {
            if (jArr[0] - j4 >= j3) {
                arrayList2.add(new long[]{j4, jArr[0]});
            }
            j4 = Math.max(j4, jArr[1]);
        }
        if (j2 - j4 >= j3) {
            arrayList2.add(new long[]{j4, j2});
        }
        return arrayList2;
    }

    static String line(Out out, Tr tr, In in, long j, long j2) {
        String str;
        ArrayList arrayList = new ArrayList();
        if (!out.evening) {
            if (!out.wxTemp.isEmpty()) {
                arrayList.add(out.wxTemp + " " + out.wxDesc + (out.wxHi.isEmpty() ? "" : " · " + f(tr, "brief.hi", "t", out.wxHi)));
            }
            if (!out.sleep.isEmpty()) {
                arrayList.add(f(tr, "brief.slept", "t", out.sleep));
            }
            if (!out.ready.isEmpty()) {
                arrayList.add(out.ready);
            }
        } else {
            int i = 0;
            for (Ev ev : out.events) {
                if (ev.day == 1 && !ev.hol) {
                    if (i == 0) {
                        str = ev.allDay ? tr.t("brief.allday") : hm(ev.start);
                    }
                    i++;
                }
            }
            arrayList.add(i == 0 ? tr.t("brief.tmrfree") : f(tr, "brief.tmr", "n", Integer.valueOf(i), "at", str));
            if (in.alarmAt > in.now && in.alarmAt - in.now < 72000000) {
                arrayList.add(f(tr, "brief.alarm", "at", hm(in.alarmAt)));
            }
            if (out.steps >= 0) {
                arrayList.add(f(tr, "brief.steps", "n", steps(out.steps), "goal", (out.stepsGoal / 1000) + "K"));
            }
        }
        return String.join(" · ", arrayList);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001f  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x001e A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String suggestAlarm(int r1, java.lang.String r2, int r3) {
        /*
            if (r1 < 0) goto L9
            int r1 = r1 + (-90)
            int r1 = r1 / 5
            int r1 = r1 * 5
            goto La
        L9:
            r1 = -1
        La:
            int r2 = minutes(r2)
            if (r1 < 0) goto L13
            if (r2 < 0) goto L13
            goto L17
        L13:
            if (r1 < 0) goto L1b
            r2 = 540(0x21c, float:7.57E-43)
        L17:
            int r2 = java.lang.Math.min(r1, r2)
        L1b:
            r1 = 0
            if (r2 >= 0) goto L1f
            return r1
        L1f:
            r0 = 240(0xf0, float:3.36E-43)
            int r2 = java.lang.Math.max(r2, r0)
            if (r3 < 0) goto L2a
            if (r3 > r2) goto L2a
            return r1
        L2a:
            java.lang.String r1 = hm(r2)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.brief.BriefModel.suggestAlarm(int, java.lang.String, int):java.lang.String");
    }

    public static String plain(Out out, In in, Tr tr) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(in.now);
        calendar.get(11);
        String t = tr.t(DayPart.greetKey(minuteOfDay(in.now), in.switchMin));
        if (!t.isEmpty()) {
            t = t.charAt(0) + t.substring(1).toLowerCase(Locale.ROOT);
        }
        arrayList.add(t + ((in.name == null || in.name.isEmpty()) ? "" : ", " + in.name) + ".");
        if (out.weather.show && !out.wxTemp.isEmpty()) {
            arrayList.add((out.wxHi.isEmpty() ? f(tr, "db.say.wx0", "t", out.wxTemp, "desc", out.wxDesc.toLowerCase(Locale.ROOT)) : f(tr, "db.say.wx", "t", out.wxTemp, "desc", out.wxDesc.toLowerCase(Locale.ROOT), "hi", out.wxHi, "lo", out.wxLo)) + ".");
        }
        if (out.day.show && !out.calFix) {
            if (out.evening) {
                int i = 0;
                Ev ev = null;
                for (Ev ev2 : out.events) {
                    if (ev2.day == 0) {
                        if (ev == null) {
                            ev = ev2;
                        }
                        i++;
                    }
                }
                if (i > 0) {
                    arrayList.add(f(tr, ev.allDay ? "db.say.tonight.ad" : "db.say.tonight", "n", Integer.valueOf(i), "first", ev.title, "at", hm(ev.start)) + ".");
                }
            }
            boolean z = out.evening;
            int i2 = 0;
            Ev ev3 = null;
            for (Ev ev4 : out.events) {
                if (ev4.day == z && !ev4.hol) {
                    if (ev3 == null) {
                        ev3 = ev4;
                    }
                    i2++;
                }
            }
            for (Ev ev5 : out.events) {
                if (ev5.day == z && ev5.hol && !arrayList2.contains(ev5.title)) {
                    arrayList2.add(ev5.title);
                }
            }
            boolean z2 = out.evening;
            arrayList.add((i2 == 0 ? tr.t(z2 ? "lc.brief.tmr.none" : "lc.brief.today.none") : f(tr, (z2 ? "lc.brief.tmr" : "lc.brief.today") + (ev3.allDay ? ".ad" : ""), "n", Integer.valueOf(i2), "first", ev3.title, "at", hm(ev3.start))) + ".");
            if (!out.freeSlots.isEmpty() && !out.freeSlots.equals(tr.t("db.cal.nofree"))) {
                arrayList.add(out.freeSlots.substring(0, 1) + out.freeSlots.substring(1).toLowerCase(Locale.ROOT).replace("–", " " + tr.t("db.say.to") + " ").replace(" · ", ", ") + ".");
            }
            if (in.alarmAt > in.now && in.alarmAt - in.now < DAY) {
                arrayList.add(f(tr, "lc.brief.alarm", "at", hm(in.alarmAt)) + ".");
            }
        }
        if (out.health.show && out.health.fix == null) {
            if (!out.sleep.isEmpty() && in.health != null && in.health.optJSONObject("sleep") != null) {
                arrayList.add(f(tr, "lc.brief.sleep", "h", String.format(Locale.US, "%.1f", Double.valueOf(in.health.optJSONObject("sleep").optDouble("min") / 60.0d))) + ".");
            }
            if (out.steps >= 0) {
                arrayList.add(f(tr, "lc.brief.steps", "n", Integer.valueOf(out.steps)) + ".");
            }
        }
        if (out.notifs.show && out.nCount > 0) {
            arrayList.add(f(tr, "lc.brief.notifs", "n", Integer.valueOf(out.nCount)) + ".");
        }
        if (out.phone.show && out.batt >= 0) {
            arrayList.add(f(tr, "lc.brief.batt", "pct", Integer.valueOf(out.batt)) + ".");
        }
        if (arrayList.size() == 1 && arrayList2.isEmpty()) {
            arrayList.add(tr.t("lc.brief.empty"));
        }
        if (!arrayList2.isEmpty()) {
            arrayList.add(f(tr, "lc.brief.also", "list", String.join(", ", arrayList2)) + ".");
        }
        return String.join(" ", arrayList);
    }
}
