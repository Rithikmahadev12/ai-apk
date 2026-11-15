package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.Caps;
import com.jarvis.assist.aosui.core.Corners;
import com.jarvis.assist.aosui.core.DayPart;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.HomeSlots;
import com.jarvis.assist.aosui.core.Profile;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.home.HomeData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class HomeData {
    static final HashSet<String> AIF_DEF;
    static final String[] DAY;
    static final String[] DEF_DOCK;
    static final String[] DEF_QS;
    static final String[] DEF_WHEEL;
    static final int[] HMC;
    static final String[][] HMS;
    static final HashMap<String, String> HSRC;
    static final String[] MON;
    static final HashMap<String, String> WXD;
    static final HashMap<String, String> WXN;
    public long alarmAt;
    final Backend be;
    public int btPct;
    final Context c;
    public boolean calNeed;
    public boolean charging;
    public boolean handoff;
    public boolean healthOff;
    public JSONArray notifs;
    public boolean paMode;
    public JSONObject screen;
    public long stFree;
    public long stTotal;
    public JSONObject starred;
    static final HashMap<String, String[]> WHEEL = new HashMap<>();
    static final HashMap<String, String[]> QS = new HashMap<>();
    static final HashMap<String, String[]> DOCK = new HashMap<>();
    static final HashMap<String, String[]> AOS = new HashMap<>();
    public final ArrayList<Entry> wheel = new ArrayList<>();
    public final ArrayList<Entry> qs = new ArrayList<>();
    public final ArrayList<Entry> qs2 = new ArrayList<>();
    public final ArrayList<Entry> dock = new ArrayList<>();
    public boolean[] qs2On = new boolean[0];
    public boolean[] qsOn = new boolean[6];
    public String you = "";
    public String time = "";
    public String date = "";
    public String greet = "HELLO";
    public String sub = "";
    public String nextPre = "";
    public String nextRed = "";
    public String nextPost = "";
    public String brief = "";
    public String sleep = "";
    public String name = "ASSISTANT";
    public String nameSet = "";
    public float dayF = 0.01f;
    public String btLabel = "BATTERY";
    public String btState = "norm";
    public String wxTemp = "—";
    public String wxDesc = "WEATHER";
    public String wxIcon = "cloud";
    public String wxState = "cloud";
    public final ArrayList<String[]> hourly = new ArrayList<>();
    public final ArrayList<Metric> hm = new ArrayList<>();
    public final ArrayList<Ev> events = new ArrayList<>();
    final ArrayList<Ev> evAll = new ArrayList<>();
    public final ArrayList<App> recent = new ArrayList<>();
    public String healthHint = "";
    public JSONObject health = new JSONObject();
    public final HashMap<String, String> srcBy = new HashMap<>();
    public final int[] wheelDot = new int[8];
    public final int[] dockDot = new int[5];
    public final int[] wheelCnt = new int[8];
    public final int[] dockCnt = new int[5];
    public final int[] qsCnt = new int[8];
    public final int[] qs2Cnt = new int[7];
    public String[] corner = {"time", "network", "alarm", "daylight"};
    public String netType = "";
    public long dataToday = -1;
    public JSONObject media = null;
    public boolean mediaAccess = true;
    public final HashMap<String, String> ntag = new HashMap<>();
    public String digest = "";
    public String lastReply = "";
    public final LinkedHashMap<String, String> awLabel = new LinkedHashMap<>();
    public final HashMap<String, Integer> pkgDot = new HashMap<>();

    static {
        char c;
        String[][] strArr = {new String[]{"health", "HEALTH", "VIT", "heart", "health"}, new String[]{"weather", "WEATHER", "ENV", "weather", "weather"}, new String[]{"gallery", "GALLERY", "MEDIA", "image", "gallery"}, new String[]{"calendar", "CALENDAR", "TIME", "cal", "calendar"}, new String[]{"settings", "SETTINGS", "SYS", "gear", "app:Settings"}, new String[]{"clock", "CLOCK", "TIME", "clock", "clock"}, new String[]{"music", "MUSIC", "PLAY", "music", "music"}, new String[]{"files", "FILES", "DOCS", "folder", "files"}, new String[]{"assistant", "ASSIST", "AI", "spark", "app:Assist"}, new String[]{"apps", "APPS", "ALL", "grid", "drawer"}, new String[]{"phone", "PHONE", "CALL", "phone", "dial"}, new String[]{"wifi", "WI-FI", "NET", "wifi", "wifi"}, new String[]{"vault", "VAULT", "PRIV", "lock", "app:Vault"}};
        int i = 0;
        int i2 = 0;
        while (true) {
            c = 3;
            if (i2 >= 13) {
                break;
            }
            String[] strArr2 = strArr[i2];
            WHEEL.put(strArr2[0], new String[]{strArr2[1], strArr2[2], strArr2[3], strArr2[4]});
            i2++;
        }
        String[][] strArr3 = {new String[]{"wifi", "WI-FI", "wifi", "wifi"}, new String[]{"bt", "BT", "bt", "bluetooth"}, new String[]{"torch", "TORCH", "torch", "torch"}, new String[]{"dnd", "DND", "moon", "dnd"}, new String[]{"rotate", "ROTATE", "rot", "display"}, new String[]{"location", "LOCATION", "pin", "location"}, new String[]{"battery", "BATTERY", "batt", "battery"}, new String[]{"sound", "SOUND", "bell", "sound"}, new String[]{"lock", "LOCK", "lock", "lock"}, new String[]{"hotspot", "HOTSPOT", "wifi", "hotspot"}, new String[]{"airplane", "AIRPLANE", "globe", "airplane"}, new String[]{"saver", "SAVER", "batt", "saver"}, new String[]{"nfc", "NFC", "spark", "nfc"}, new String[]{"dark", "DARK", "moon", "dark"}, new String[]{"bright", "BRIGHT", "sun", "display"}};
        String[][] strArr4 = {new String[]{"aos:ask", "ASK", "AI", "spark"}, new String[]{"aos:search", "SEARCH", "FIND", "list"}, new String[]{"aos:brief", "BRIEF", "DAY", "cal"}, new String[]{"aos:vault", "VAULT", "PRIV", "lock"}, new String[]{"aos:settings", "SETTINGS", "SYS", "gear"}, new String[]{"aos:drawer", "APPS", "ALL", "grid"}};
        int i3 = 0;
        while (i3 < 6) {
            String[] strArr5 = strArr4[i3];
            char c2 = c;
            AOS.put(strArr5[i], new String[]{strArr5[1], strArr5[2], strArr5[c2]});
            i3++;
            i = i;
            c = c2;
        }
        int i4 = i;
        char c3 = c;
        for (int i5 = i4; i5 < 15; i5++) {
            String[] strArr6 = strArr3[i5];
            QS.put(strArr6[i4], new String[]{strArr6[1], strArr6[2], strArr6[c3]});
        }
        String[][] strArr7 = {new String[]{"dial", "PHONE", "CALL", "phone"}, new String[]{"sms", "MSG", "SMS", "msg"}, new String[]{"browser", "BROWSER", "WEB", "globe"}, new String[]{"camera", "CAMERA", "CAM", "cam"}, new String[]{"email", "EMAIL", "MAIL", "mail"}, new String[]{"maps", "MAPS", "NAV", "pin"}, new String[]{"music", "MUSIC", "PLAY", "music"}, new String[]{"calendar", "CALENDAR", "TIME", "cal"}, new String[]{"clock", "CLOCK", "TIME", "clock"}, new String[]{"gallery", "GALLERY", "MEDIA", "image"}, new String[]{"contacts", "CONTACTS", "PPL", "phone"}, new String[]{"calculator", "CALC", "MATH", "grid"}, new String[]{"vault", "VAULT", "PRIV", "lock"}};
        for (int i6 = i4; i6 < 13; i6++) {
            String[] strArr8 = strArr7[i6];
            DOCK.put(strArr8[i4], new String[]{strArr8[1], strArr8[2], strArr8[c3]});
        }
        DEF_WHEEL = new String[]{"health", "weather", "gallery", "calendar", "settings"};
        DEF_QS = new String[]{"wifi", "bt", "torch", "sound", "rotate", "dnd"};
        DEF_DOCK = new String[]{"dial", "sms", "browser", "camera", "aos:search"};
        MON = new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        DAY = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        WXN = new HashMap<>();
        WXD = new HashMap<>();
        HSRC = new HashMap<>();
        WXN.put("openmeteo", "OPEN-METEO");
        WXN.put("met", "MET NORWAY");
        WXN.put("owm", "OPENWEATHERMAP");
        WXN.put("wapi", "WEATHERAPI.COM");
        String[] strArr9 = {"sun", "SUNNY", "moon", "CLEAR", "partly", "PARTLY", "cloud", "CLOUDY", "fog", "FOG", "rain", "RAIN", "snow", "SNOW", "storm", "STORM"};
        for (int i7 = i4; i7 < 16; i7 += 2) {
            WXD.put(strArr9[i7], strArr9[i7 + 1]);
        }
        HSRC.put("hc", "HEALTH CONNECT");
        HSRC.put("whoop", "WHOOP");
        HSRC.put("oura", "OURA");
        HSRC.put("uh", "ULTRAHUMAN");
        HSRC.put("apple", "APPLE HEALTH");
        HMS = new String[][]{new String[]{"heart", "HR"}, new String[]{"pulse", "HRV"}, new String[]{"rot", "RHR"}, new String[]{"flame", "STEPS"}};
        HMC = new int[]{-2078885, Theme.HM_HRV, -10835492, -1774607};
        AIF_DEF = new HashSet<>(Arrays.asList("chat", "brief", "notifs", "coach", "open", "share", "time", "people", "lock", "rewrite", "replies", "clip", "suggest", "whats", "receipt", "ocr", "hweek", "bedtime", "workout", "planday", "findtime", "pack", "shop", "web", "convert", "routine", "memory", "tone", "mute"));
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Entry {
        public final String icon;
        public final String id;
        public final String label;
        public final String sub;

        Entry(String str, String str2, String str3, String str4) {
            this.id = str;
            this.label = str2;
            this.sub = str3;
            this.icon = str4;
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Ev {
        public final boolean allDay;
        public final long end;
        public final long start;
        public final String title;

        Ev(String str, long j, long j2, boolean z) {
            this.title = str;
            this.start = j;
            this.end = j2;
            this.allDay = z;
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class App {
        public final String cls;
        public final String label;
        public final String pkg;

        App(String str, String str2, String str3) {
            this.pkg = str;
            this.cls = str2;
            this.label = str3;
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Metric {
        public final int color;
        public final float frac;
        public final String icon;
        public final String label;
        public final String value;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Metric(String str, String str2, String str3, int i, float f) {
            this.icon = str;
            this.label = str2;
            this.value = str3;
            this.color = i;
            this.frac = f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String greetName() {
        return Profile.greet(this.greet, this.you);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HomeData(Backend backend) {
        this.be = backend;
        this.c = backend.ctx();
        this.srcBy.put("recent", "AOS LAUNCH HISTORY");
        next(Strings.t("next.none"), "", "");
        emptyHealth();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String p2(int i) {
        return (i < 10 ? "0" : "") + i;
    }

    public static String hm(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return p2(calendar.get(11)) + ":" + p2(calendar.get(12));
    }

    void next(String str, String str2, String str3) {
        this.nextPre = str;
        this.nextRed = str2;
        this.nextPost = str3;
    }

    public JSONObject cfg() {
        return Store.obj("aos.cfg");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyCfg() {
        JSONObject cfg = cfg();
        this.wheel.clear();
        this.qs.clear();
        this.qs2.clear();
        this.dock.clear();
        Iterator<String> it = list(cfg, "wheel", DEF_WHEEL, 8).iterator();
        while (it.hasNext()) {
            this.wheel.add(entry("wheel", it.next()));
        }
        Iterator<String> it2 = list(cfg, "qs", DEF_QS, 8).iterator();
        while (it2.hasNext()) {
            this.qs.add(entry("qs", it2.next()));
        }
        Iterator<String> it3 = list(cfg, "qs2", HomeSlots.DEF_QS2, 7).iterator();
        while (it3.hasNext()) {
            this.qs2.add(entry("qs", it3.next()));
        }
        Iterator<String> it4 = list(cfg, "dock", DEF_DOCK, 5).iterator();
        while (it4.hasNext()) {
            this.dock.add(entry("dock", it4.next()));
        }
        if (this.qs2On.length != this.qs2.size()) {
            this.qs2On = new boolean[this.qs2.size()];
        }
        if (this.qsOn.length != this.qs.size()) {
            this.qsOn = new boolean[this.qs.size()];
        }
    }

    static ArrayList<String> list(JSONObject jSONObject, String str, String[] strArr, int i) {
        ArrayList<String> arrayList = new ArrayList<>();
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        int i2 = 0;
        if (optJSONArray != null) {
            while (i2 < optJSONArray.length() && arrayList.size() < i) {
                arrayList.add(optJSONArray.optString(i2));
                i2++;
            }
        } else {
            while (i2 < strArr.length && arrayList.size() < i) {
                arrayList.add(strArr[i2]);
                i2++;
            }
        }
        return arrayList;
    }

    static Entry entry(String str, String str2) {
        if (str2.startsWith("app:")) {
            String[] split = str2.split("\\|", 2);
            String upperCase = (split.length > 1 ? split[1] : "APP").toUpperCase(Locale.ROOT);
            if (upperCase.length() > 9) {
                upperCase = upperCase.substring(0, 9);
            }
            return "qs".equals(str) ? new Entry(str2, upperCase, "", "grid") : new Entry(str2, upperCase, "APP", "grid");
        }
        String[] strArr = AOS.get(str2);
        if (strArr != null) {
            return new Entry(str2, strArr[0], strArr[1], strArr[2]);
        }
        String[] strArr2 = ("wheel".equals(str) ? WHEEL : "qs".equals(str) ? QS : DOCK).get(str2);
        boolean equals = "qs".equals(str);
        if (strArr2 == null) {
            strArr2 = QS.get(str2);
            equals = strArr2 != null;
            if (strArr2 == null && (strArr2 = WHEEL.get(str2)) == null) {
                strArr2 = DOCK.get(str2);
            }
        }
        return strArr2 == null ? new Entry(str2, "—", "", "grid") : equals ? new Entry(str2, strArr2[0], "", strArr2[1]) : new Entry(str2, strArr2[0], strArr2[1], strArr2[2]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean tick() {
        Calendar calendar = Calendar.getInstance();
        String str = p2(calendar.get(11)) + ":" + p2(calendar.get(12));
        boolean z = !str.equals(this.time);
        this.time = str;
        this.date = DAY[calendar.get(7) - 1] + " " + calendar.get(5) + " " + MON[calendar.get(2)];
        this.dayF = Math.max(0.01f, ((calendar.get(11) * 60) + calendar.get(12)) / 1440.0f);
        int i = calendar.get(11);
        this.you = Profile.clean(Store.get(Profile.KEY, ""));
        this.greet = Strings.t(DayPart.greetKey((i * 60) + calendar.get(12), DayPart.eveFrom(Store.get("aos.brief"))));
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void battery() {
        Intent registerReceiver = this.c.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null) {
            return;
        }
        int intExtra = registerReceiver.getIntExtra("level", -1);
        int intExtra2 = registerReceiver.getIntExtra("scale", 100);
        int intExtra3 = registerReceiver.getIntExtra("status", -1);
        boolean z = intExtra3 == 2 || intExtra3 == 5;
        int max = Math.max(0, (intExtra * 100) / Math.max(1, intExtra2));
        this.btPct = max;
        this.btState = z ? "charge" : max <= 10 ? "crit" : max <= 20 ? "low" : "norm";
        this.btLabel = z ? "CHARGING" : "crit".equals(this.btState) ? "CRITICAL" : "low".equals(this.btState) ? "LOW" : "BATTERY";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void calendar() {
        JSONObject jSONObject;
        int i;
        Ev ev;
        try {
            jSONObject = new JSONObject(this.be.phoneCal(2));
        } catch (Exception e) {
            jSONObject = new JSONObject();
        }
        long currentTimeMillis = System.currentTimeMillis();
        this.calNeed = "permission".equals(jSONObject.optString("why"));
        this.events.clear();
        this.evAll.clear();
        if (!jSONObject.optBoolean("ok")) {
            next(this.calNeed ? "TAP CALENDAR TO ALLOW ACCESS" : Strings.t("next.none"), "", "");
            this.srcBy.put("calendar", "DEVICE CALENDAR");
            this.srcBy.put("greeting", "DEVICE CALENDAR");
            return;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray("events");
        HashSet hashSet = new HashSet();
        if (optJSONArray != null) {
            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                if (optJSONObject != null && optJSONObject.optLong("end") > currentTimeMillis) {
                    this.evAll.add(new Ev(optJSONObject.optString("title"), optJSONObject.optLong("start"), optJSONObject.optLong("end"), optJSONObject.optBoolean("allDay")));
                    String optString = optJSONObject.optString("calendar", "");
                    if (!optString.isEmpty()) {
                        hashSet.add(optString);
                    }
                }
            }
        }
        this.evAll.sort(new Comparator() { // from class: com.jarvis.assist.aosui.home.HomeData$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = Long.compare(((HomeData.Ev) obj).start, ((HomeData.Ev) obj2).start);
                return compare;
            }
        });
        for (int i3 = 0; i3 < Math.min(4, this.evAll.size()); i3++) {
            this.events.add(this.evAll.get(i3));
        }
        if (this.evAll.isEmpty()) {
            ev = null;
            i = 0;
        } else {
            i = 0;
            ev = this.evAll.get(0);
        }
        if (ev == null) {
            next(Strings.t("next.none"), "", "");
        } else if (ev.start <= currentTimeMillis) {
            next("NOW ", ev.title.toUpperCase(Locale.ROOT), "");
        } else {
            next("NEXT ", hm(ev.start), " " + ev.title.toUpperCase(Locale.ROOT));
        }
        Calendar calendar = Calendar.getInstance();
        Iterator<Ev> it = this.evAll.iterator();
        while (it.hasNext()) {
            Ev next = it.next();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(next.start);
            if (calendar2.get(1) == calendar.get(1) && calendar2.get(6) == calendar.get(6)) {
                i++;
            }
        }
        this.sub = i > 0 ? i + " EVENT" + (i > 1 ? "S" : "") + " TODAY" : "CLEAR DAY";
        int size = hashSet.size();
        String str = size > 0 ? "DEVICE CALENDAR · " + size + " ACCOUNT" + (size > 1 ? "S" : "") : "DEVICE CALENDAR";
        this.srcBy.put("calendar", str);
        this.srcBy.put("greeting", str);
    }

    void emptyHealth() {
        this.hm.clear();
        for (int i = 0; i < 4; i++) {
            this.hm.add(new Metric(HMS[i][0], HMS[i][1], "—", HMC[i], 0.0f));
        }
    }

    public JSONObject goals() {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(Store.home("health.goals"));
        } catch (Exception e) {
            jSONObject = new JSONObject();
        }
        try {
            return new JSONObject().put("steps", pos(jSONObject, "steps", 8000.0d)).put("sleep", pos(jSONObject, "sleep", 8.0d)).put("kcal", pos(jSONObject, "kcal", 500.0d)).put("hrv", pos(jSONObject, "hrv", 100.0d));
        } catch (Exception e2) {
            return new JSONObject();
        }
    }

    static double pos(JSONObject jSONObject, String str, double d) {
        double optDouble = jSONObject.optDouble(str, d);
        return optDouble > 0.0d ? optDouble : d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Integer num(JSONObject jSONObject, String str) {
        if (jSONObject.has(str) && !jSONObject.isNull(str) && (jSONObject.opt(str) instanceof Number)) {
            return Integer.valueOf((int) Math.round(jSONObject.optDouble(str)));
        }
        return null;
    }

    public static String fmtMin(Double d) {
        if (d == null) {
            return "—";
        }
        return ((int) Math.floor(d.doubleValue() / 60.0d)) + "H " + p2((int) Math.round(d.doubleValue() % 60.0d)) + "M";
    }

    static String age(JSONObject jSONObject, String str) {
        StringBuilder append;
        String str2;
        long optLong = jSONObject.optLong(str + "_at", 0L);
        if (optLong <= 0) {
            return "";
        }
        long currentTimeMillis = ((System.currentTimeMillis() / 1000) - optLong) / 60;
        if (currentTimeMillis < 120) {
            return "";
        }
        if (currentTimeMillis < 2880) {
            append = new StringBuilder().append(" · ").append(currentTimeMillis / 60);
            str2 = "H";
        } else {
            append = new StringBuilder().append(" · ").append(currentTimeMillis / 1440);
            str2 = "D";
        }
        return append.append(str2).toString();
    }

    /*  JADX ERROR: JadxRuntimeException in pass: ModVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r12v1 com.jarvis.assist.aosui.home.HomeData$Metric, still in use, count: 2, list:
          (r12v1 com.jarvis.assist.aosui.home.HomeData$Metric) from 0x00ed: MOVE (r25v2 com.jarvis.assist.aosui.home.HomeData$Metric) = (r12v1 com.jarvis.assist.aosui.home.HomeData$Metric)
          (r12v1 com.jarvis.assist.aosui.home.HomeData$Metric) from 0x00df: MOVE (r25v7 com.jarvis.assist.aosui.home.HomeData$Metric) = (r12v1 com.jarvis.assist.aosui.home.HomeData$Metric)
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
        	at jadx.core.utils.InsnRemover.unbindInsn(InsnRemover.java:80)
        	at jadx.core.utils.InsnRemover.addAndUnbind(InsnRemover.java:56)
        	at jadx.core.dex.visitors.ModVisitor.removeStep(ModVisitor.java:447)
        	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:96)
        */
    void applyHealth(java.lang.String r25) {
        /*
            Method dump skipped, instructions count: 846
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.HomeData.applyHealth(java.lang.String):void");
    }

    public boolean fahrenheit() {
        return "f".equals(Store.obj("aos.wxcfg").optString("units"));
    }

    public String deg(Object obj) {
        if (!(obj instanceof Number)) {
            return "—";
        }
        double doubleValue = ((Number) obj).doubleValue();
        if (fahrenheit()) {
            doubleValue = ((doubleValue * 9.0d) / 5.0d) + 32.0d;
        }
        return Math.round(doubleValue) + "°";
    }

    String wxSrc() {
        String optString = Store.obj("aos.wxcfg").optString("src", "auto");
        return (WXN.containsKey(optString) || "auto".equals(optString)) ? optString : "auto";
    }

    void applyWx(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("now");
        if (optJSONObject == null) {
            optJSONObject = new JSONObject();
        }
        String optString = optJSONObject.optString("code");
        if (!WXD.containsKey(optString)) {
            optString = "cloud";
        }
        this.wxTemp = deg(optJSONObject.opt("t"));
        this.wxDesc = optJSONObject.optString("desc", WXD.get(optString));
        this.wxIcon = "partly".equals(optString) ? "weather" : optString;
        this.wxState = optString;
        this.hourly.clear();
        JSONArray optJSONArray = jSONObject.optJSONArray("hourly");
        if (optJSONArray != null) {
            for (int i = 0; i < Math.min(6, optJSONArray.length()); i++) {
                JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                if (optJSONObject2 != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(optJSONObject2.optLong("ts"));
                    String optString2 = optJSONObject2.optString("code");
                    ArrayList<String[]> arrayList = this.hourly;
                    String p2 = p2(calendar.get(11));
                    String deg = deg(optJSONObject2.opt("t"));
                    if (!WXD.containsKey(optString2)) {
                        optString2 = "cloud";
                    }
                    arrayList.add(new String[]{p2, deg, optString2});
                }
            }
        }
        String optString3 = jSONObject.optString("city", "");
        this.srcBy.put("weather", (WXN.containsKey(jSONObject.optString("src")) ? WXN.get(jSONObject.optString("src")) : "WEATHER") + (optString3.isEmpty() ? "" : " · " + optString3.toUpperCase(Locale.ROOT)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void weather(boolean z, final Runnable runnable) {
        final String wxSrc = wxSrc();
        JSONObject jSONObject = null;
        try {
            String loc = this.be.loc();
            if (!loc.isEmpty()) {
                jSONObject = new JSONObject(loc);
            }
        } catch (Exception e) {
        }
        this.be.locRefresh(new Consumer() { // from class: com.jarvis.assist.aosui.home.HomeData$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                HomeData.lambda$weather$1((String) obj);
            }
        });
        if (jSONObject == null || !jSONObject.has("lat")) {
            this.srcBy.put("weather", (WXN.containsKey(wxSrc) ? WXN.get(wxSrc) : "OPEN-METEO") + " · ALLOW LOCATION");
            return;
        }
        JSONObject obj = Store.obj("aos.wx");
        if (obj.has("now")) {
            applyWx(obj);
        }
        if (z || !obj.has("now") || !wxSrc.equals(obj.optString("cfg")) || System.currentTimeMillis() - obj.optLong("at") >= 900000) {
            final String optString = jSONObject.optString("place", "");
            this.be.weatherFetch(wxSrc, jSONObject.optDouble("lat"), jSONObject.optDouble("lon"), new Consumer() { // from class: com.jarvis.assist.aosui.home.HomeData$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj2) {
                    HomeData.this.m271lambda$weather$3$comaugmaosohomeHomeData(wxSrc, runnable, optString, (String) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$weather$1(String str) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$weather$3$com-augma-oso-home-HomeData, reason: not valid java name */
    public /* synthetic */ void m271lambda$weather$3$comaugmaosohomeHomeData(final String str, final Runnable runnable, final String str2, final String str3) {
        NativeHome.MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.HomeData$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                HomeData.this.m270lambda$weather$2$comaugmaosohomeHomeData(str3, str, runnable, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$weather$2$com-augma-oso-home-HomeData, reason: not valid java name */
    public /* synthetic */ void m270lambda$weather$2$comaugmaosohomeHomeData(String str, String str2, Runnable runnable, String str3) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("now")) {
                if (jSONObject.has("error")) {
                    this.srcBy.put("weather", (WXN.containsKey(str2) ? WXN.get(str2) : "WEATHER") + " · " + jSONObject.optString("error").toUpperCase(Locale.ROOT));
                }
                runnable.run();
                return;
            }
            try {
                JSONObject put = jSONObject.put("at", System.currentTimeMillis()).put("cfg", str2);
                if (str3.isEmpty()) {
                    str3 = jSONObject.optString("place", "");
                }
                put.put("city", str3);
            } catch (Exception e) {
            }
            Store.set("aos.wx", jSONObject.toString());
            applyWx(jSONObject);
            runnable.run();
        } catch (Exception e2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recents() {
        this.recent.clear();
        JSONArray arr = Store.arr("aos.recent");
        for (int i = 0; i < arr.length() && this.recent.size() < 8; i++) {
            JSONObject optJSONObject = arr.optJSONObject(i);
            if (optJSONObject != null) {
                this.recent.add(new App(optJSONObject.optString("pkg"), optJSONObject.optString("cls"), optJSONObject.optString("label")));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remember(String str, String str2, String str3) {
        JSONArray arr = Store.arr("aos.recent");
        JSONArray jSONArray = new JSONArray();
        try {
            jSONArray.put(new JSONObject().put("pkg", str).put("cls", str2).put("label", str3).put("at", System.currentTimeMillis()));
            for (int i = 0; i < arr.length() && jSONArray.length() < 12; i++) {
                JSONObject optJSONObject = arr.optJSONObject(i);
                if (optJSONObject != null && !str.equals(optJSONObject.optString("pkg"))) {
                    jSONArray.put(optJSONObject);
                }
            }
        } catch (Exception e) {
        }
        Store.set("aos.recent", jSONArray.toString());
        recents();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bump(String str) {
        JSONObject obj = Store.obj("aos.usage");
        try {
            obj.put(str, obj.optInt(str) + 1);
            capCounts(obj, 200);
        } catch (Exception e) {
        }
        Store.set("aos.usage", obj.toString());
    }

    static void capCounts(JSONObject jSONObject, int i) {
        if (jSONObject.length() <= i) {
            return;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            linkedHashMap.put(next, Integer.valueOf(jSONObject.optInt(next)));
        }
        Iterator<String> it = Caps.dropLowest(linkedHashMap, i).iterator();
        while (it.hasNext()) {
            jSONObject.remove(it.next());
        }
    }

    JSONObject aif() {
        return Store.obj("aos.aif");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean aifOn(String str) {
        JSONObject aif = aif();
        if (aif.optBoolean("paused") && !"mute".equals(str) && !"redact".equals(str)) {
            return false;
        }
        JSONObject optJSONObject = aif.optJSONObject("on");
        return (optJSONObject == null || !optJSONObject.has(str) || optJSONObject.isNull(str)) ? AIF_DEF.contains(str) : optJSONObject.optBoolean(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String day() {
        Calendar calendar = Calendar.getInstance();
        return String.format(Locale.US, "%04d-%02d-%02d", Integer.valueOf(calendar.get(1)), Integer.valueOf(calendar.get(2) + 1), Integer.valueOf(calendar.get(5)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void name() {
        JSONObject jSONObject;
        String str;
        try {
            jSONObject = new JSONObject(this.be.aiHasKey());
        } catch (Exception e) {
            jSONObject = new JSONObject();
        }
        this.paMode = !jSONObject.optBoolean("hasKey") || aif().optBoolean("paused");
        try {
            str = new JSONObject(this.be.assistantInfo()).optString("pkg");
        } catch (Exception e2) {
            str = "";
        }
        this.handoff = HomeRules.handoff(this.paMode, this.be.get("ai.pa.fallback"), str);
        String trim = Store.home("ai.name").trim();
        this.nameSet = trim;
        this.name = trim.isEmpty() ? UnitsMath.assistantName(trim) : trim.toUpperCase(Locale.ROOT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void brief() {
        String t;
        String str = "";
        this.brief = "";
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(11);
        if (i >= 5) {
            if (i >= 12 && i < 17) {
                return;
            }
            String format = String.format(Locale.US, "%04d-%02d-%02d", Integer.valueOf(calendar.get(1)), Integer.valueOf(calendar.get(2) + 1), Integer.valueOf(calendar.get(5)));
            if (aifOn("greetai")) {
                JSONObject obj = Store.obj("aos.aif.greet");
                if (format.equals(obj.optString("d"))) {
                    if ((i < 12 ? "am" : "pm").equals(obj.optString("slot")) && !obj.optString("text").isEmpty()) {
                        this.brief = obj.optString("text");
                        return;
                    }
                }
            }
            if ("1".equals(Store.home("ai.briefhome"))) {
                ArrayList arrayList = new ArrayList();
                JSONObject jSONObject = this.health;
                if (i < 12) {
                    JSONObject obj2 = Store.obj("aos.wx");
                    JSONObject optJSONObject = obj2.optJSONObject("now");
                    if (optJSONObject != null && (optJSONObject.opt("t") instanceof Number)) {
                        JSONArray optJSONArray = obj2.optJSONArray("daily");
                        JSONObject optJSONObject2 = optJSONArray == null ? null : optJSONArray.optJSONObject(0);
                        String deg = deg(optJSONObject.opt("t"));
                        String str2 = optJSONObject.optString("desc").isEmpty() ? "" : " " + optJSONObject.optString("desc").toUpperCase(Locale.ROOT);
                        if (optJSONObject2 != null && (optJSONObject2.opt("hi") instanceof Number)) {
                            str = " · " + Strings.t("brief.hi", "t", deg(optJSONObject2.opt("hi")));
                        }
                        arrayList.add(deg + str2 + str);
                    }
                    JSONObject optJSONObject3 = jSONObject.optJSONObject("sleep");
                    if (optJSONObject3 != null && optJSONObject3.has("min") && !optJSONObject3.isNull("min")) {
                        arrayList.add(Strings.t("brief.slept", "t", fmtMin(Double.valueOf(optJSONObject3.optDouble("min")))));
                    }
                    Integer num = num(jSONObject, "recovery");
                    if (num == null) {
                        num = num(jSONObject, "readiness");
                    }
                    if (num != null) {
                        arrayList.add(Strings.t("brief.ready", "n", num));
                    }
                } else {
                    Calendar calendar2 = (Calendar) calendar.clone();
                    calendar2.add(5, 1);
                    calendar2.set(11, 0);
                    calendar2.set(12, 0);
                    calendar2.set(13, 0);
                    calendar2.set(14, 0);
                    long timeInMillis = calendar2.getTimeInMillis();
                    ArrayList arrayList2 = new ArrayList();
                    Iterator<Ev> it = this.evAll.iterator();
                    while (it.hasNext()) {
                        Ev next = it.next();
                        if (next.start >= timeInMillis && next.start < 86400000 + timeInMillis) {
                            arrayList2.add(next);
                        }
                    }
                    if (arrayList2.isEmpty()) {
                        t = Strings.t("brief.tmrfree");
                    } else {
                        t = Strings.t("brief.tmr", "n", Integer.valueOf(arrayList2.size()), "at", ((Ev) arrayList2.get(0)).allDay ? Strings.t("brief.allday") : hm(((Ev) arrayList2.get(0)).start));
                    }
                    arrayList.add(t);
                    try {
                        long optLong = new JSONObject(this.be.nextAlarm()).optLong("at");
                        if (optLong > 0 && optLong - System.currentTimeMillis() < 72000000) {
                            arrayList.add(Strings.t("brief.alarm", "at", hm(optLong)));
                        }
                    } catch (Exception e) {
                    }
                    Integer num2 = num(jSONObject, "steps");
                    if (num2 != null) {
                        arrayList.add(Strings.t("brief.steps", "n", num2.intValue() >= 1000 ? String.format(Locale.US, "%.1fK", Float.valueOf(num2.intValue() / 1000.0f)) : new StringBuilder().append(num2).toString(), "goal", ((int) (goals().optDouble("steps", 8000.0d) / 1000.0d)) + "K"));
                    }
                }
                this.brief = String.join(" · ", arrayList);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dots(int i, int i2, int i3) {
        Arrays.fill(this.wheelDot, 0);
        Arrays.fill(this.dockDot, 0);
        Arrays.fill(this.wheelCnt, 0);
        Arrays.fill(this.dockCnt, 0);
        Arrays.fill(this.qsCnt, 0);
        Arrays.fill(this.qs2Cnt, 0);
        if (this.notifs == null || this.notifs.length() == 0) {
            return;
        }
        for (int i4 = 0; i4 < Math.min(8, this.wheel.size()); i4++) {
            String slotPkg = slotPkg(this.wheel.get(i4).id);
            this.wheelDot[i4] = col(slotPkg, i, i2, i3);
            this.wheelCnt[i4] = count(slotPkg);
        }
        for (int i5 = 0; i5 < Math.min(5, this.dock.size()); i5++) {
            String slotPkg2 = slotPkg(this.dock.get(i5).id);
            this.dockDot[i5] = col(slotPkg2, i, i2, i3);
            this.dockCnt[i5] = count(slotPkg2);
        }
        for (int i6 = 0; i6 < Math.min(8, this.qs.size()); i6++) {
            this.qsCnt[i6] = count(slotPkg(this.qs.get(i6).id));
        }
        for (int i7 = 0; i7 < Math.min(7, this.qs2.size()); i7++) {
            this.qs2Cnt[i7] = count(slotPkg(this.qs2.get(i7).id));
        }
    }

    String slotPkg(String str) {
        if (str.startsWith("app:")) {
            return str.substring(4).split("/")[0];
        }
        if (!str.startsWith("aos:") && (!QS.containsKey(str) || WHEEL.containsKey(str) || DOCK.containsKey(str))) {
            try {
                String roleApp = this.be.roleApp(str);
                return roleApp == null ? "" : roleApp;
            } catch (Exception e) {
            }
        }
        return "";
    }

    int count(String str) {
        if (str == null || str.isEmpty() || this.notifs == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.notifs.length(); i2++) {
            JSONObject optJSONObject = this.notifs.optJSONObject(i2);
            if (optJSONObject != null && str.equals(optJSONObject.optString("pkg"))) {
                i++;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String badge(int i) {
        return i <= 0 ? "" : i > 9 ? "9+" : String.valueOf(i);
    }

    int col(String str, int i, int i2, int i3) {
        Integer num = (str == null || str.isEmpty()) ? null : this.pkgDot.get(str);
        if (num == null) {
            return 0;
        }
        return num.intValue() == 1 ? i : num.intValue() == 2 ? i2 : i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<String> widgetOrder() {
        JSONObject cfg = cfg();
        JSONArray optJSONArray = cfg.optJSONArray("widgets");
        ArrayList<String> arrayList = null;
        ArrayList<String> strs = optJSONArray == null ? null : strs(optJSONArray);
        ArrayList<String> strs2 = strs(cfg.optJSONArray("off"));
        boolean aifOn = aifOn("worder");
        JSONObject obj = aifOn ? Store.obj("aos.aif.worder") : null;
        if (obj != null && day().equals(obj.optString("d"))) {
            arrayList = strs(obj.optJSONArray("order"));
        }
        return new ArrayList<>(HomeRules.widgetOrder(strs, strs2, new Predicate() { // from class: com.jarvis.assist.aosui.home.HomeData$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj2) {
                return HomeData.this.m272lambda$widgetOrder$4$comaugmaosohomeHomeData((String) obj2);
            }
        }, aifOn, Calendar.getInstance().get(11), arrayList));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$widgetOrder$4$com-augma-oso-home-HomeData, reason: not valid java name */
    public /* synthetic */ boolean m272lambda$widgetOrder$4$comaugmaosohomeHomeData(String str) {
        return Widgets.has(str) || (str.startsWith("aw:") && this.awLabel.containsKey(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<String> strs(JSONArray jSONArray) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.optString(i));
            }
        }
        return arrayList;
    }

    Set<String> enabled() {
        JSONObject cfg = cfg();
        HashSet hashSet = new HashSet();
        ArrayList<String> strs = strs(cfg.optJSONArray("off"));
        JSONArray optJSONArray = cfg.optJSONArray("widgets");
        if (optJSONArray != null) {
            Iterator<String> it = strs(optJSONArray).iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (!strs.contains(next)) {
                    hashSet.add(next);
                }
            }
        }
        if (optJSONArray == null) {
            for (String str : SettingsModel.WIDGETS) {
                if (!Arrays.asList(SettingsModel.DEF_OFF).contains(str)) {
                    hashSet.add(str);
                }
            }
        }
        return hashSet;
    }

    boolean wants(String str) {
        JSONObject cfg = cfg();
        return strs(cfg.optJSONArray("widgets")).contains(str) && !strs(cfg.optJSONArray("off")).contains(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mediaNow() {
        JSONObject jSONObject;
        boolean z;
        String media;
        JSONObject jSONObject2 = null;
        try {
            media = this.be.media();
        } catch (Exception e) {
            jSONObject = null;
        }
        if (media != null && !media.isEmpty()) {
            jSONObject = new JSONObject(media);
            z = true;
            if (jSONObject != null && !jSONObject.optBoolean("access", true)) {
                z = false;
            }
            this.mediaAccess = z;
            if (jSONObject != null && !jSONObject.optString("title").isEmpty()) {
                jSONObject2 = jSONObject;
            }
            this.media = jSONObject2;
            this.srcBy.put("media", "ACTIVE MEDIA SESSION");
        }
        jSONObject = null;
        z = true;
        if (jSONObject != null) {
            z = false;
        }
        this.mediaAccess = z;
        if (jSONObject != null) {
            jSONObject2 = jSONObject;
        }
        this.media = jSONObject2;
        this.srcBy.put("media", "ACTIVE MEDIA SESSION");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void notifsNow() {
        /*
            Method dump skipped, instructions count: 418
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.HomeData.notifsNow():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$notifsNow$5$com-augma-oso-home-HomeData, reason: not valid java name */
    public /* synthetic */ int m269lambda$notifsNow$5$comaugmaosohomeHomeData(JSONObject jSONObject, JSONObject jSONObject2) {
        return Integer.compare(HomeRules.tagRank(this.ntag.get(jSONObject.optString("key"))), HomeRules.tagRank(this.ntag.get(jSONObject2.optString("key"))));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void extras() {
        mediaNow();
        notifsNow();
        this.srcBy.put("alarms", "SYSTEM CLOCK");
        this.srcBy.put("battery", "SYSTEM BATTERY");
        try {
            this.alarmAt = new JSONObject(this.be.nextAlarm()).optLong("at");
        } catch (Exception e) {
            this.alarmAt = 0L;
        }
        this.charging = "charge".equals(this.btState);
        try {
            JSONObject jSONObject = new JSONObject(this.be.storage());
            this.stFree = jSONObject.optLong("free");
            this.stTotal = jSONObject.optLong("total");
        } catch (Exception e2) {
            this.stTotal = 0L;
            this.stFree = 0L;
        }
        if (wants("people")) {
            try {
                this.starred = new JSONObject(this.be.starred());
            } catch (Exception e3) {
                this.starred = null;
            }
        }
        Set<String> enabled = enabled();
        this.corner = new String[4];
        for (int i = 0; i < 4; i++) {
            this.corner[i] = Corners.pick(Corners.CORNERS[i], Store.get(Corners.KEY, ""), enabled);
        }
        if (wants("screentime") || Arrays.asList(this.corner).contains("screentime")) {
            try {
                this.screen = new JSONObject(this.be.screenTime());
            } catch (Exception e4) {
                this.screen = null;
            }
        }
        if (Arrays.asList(this.corner).contains("data")) {
            this.dataToday = this.be.dataToday();
        }
        this.netType = this.be.net();
        this.lastReply = "";
        JSONArray arr = Store.arr("aos.chats");
        for (int i2 = 0; i2 < arr.length() && this.lastReply.isEmpty(); i2++) {
            JSONObject optJSONObject = arr.optJSONObject(i2);
            JSONArray optJSONArray = optJSONObject == null ? null : optJSONObject.optJSONArray("msgs");
            if (optJSONArray != null) {
                int length = optJSONArray.length() - 1;
                while (true) {
                    if (length < 0) {
                        break;
                    }
                    JSONObject optJSONObject2 = optJSONArray.optJSONObject(length);
                    if (optJSONObject2 != null && "a".equals(optJSONObject2.optString("r")) && !optJSONObject2.optBoolean("err")) {
                        this.lastReply = optJSONObject2.optString("t");
                        break;
                    }
                    length--;
                }
            }
        }
        this.awLabel.clear();
        try {
            JSONArray jSONArray = new JSONArray(this.be.appWidgets());
            for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                JSONObject optJSONObject3 = jSONArray.optJSONObject(i3);
                if (optJSONObject3 != null && optJSONObject3.has("id")) {
                    String str = "aw:" + optJSONObject3.optInt("id");
                    this.awLabel.put(str, optJSONObject3.optString("label").toUpperCase(Locale.ROOT));
                    this.srcBy.put(str, Strings.t("w.aw.src"));
                }
            }
        } catch (Exception e5) {
        }
        this.srcBy.put("people", "STARRED CONTACTS");
        this.srcBy.put("screentime", "USAGE STATS · TODAY");
        this.srcBy.put("worldclock", "THIS PHONE");
        this.srcBy.put("assistant", this.name);
        this.srcBy.put("storage", "INTERNAL STORAGE");
        this.srcBy.put("sleep", this.srcBy.containsKey("health") ? this.srcBy.get("health") : "HEALTH");
        this.srcBy.put("activity", this.srcBy.get("sleep"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<String> clocks() {
        ArrayList<String> strs = strs(Store.arr("aos.clocks"));
        if (strs.isEmpty()) {
            strs.add("Europe/London");
            strs.add("America/New_York");
            strs.add("Asia/Tokyo");
        }
        while (strs.size() > 3) {
            strs.remove(3);
        }
        return strs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bumpRoutine(String str) {
        if (aifOn("routine") || aifOn("suggest")) {
            JSONObject obj = Store.obj("aos.routine");
            Calendar calendar = Calendar.getInstance();
            String valueOf = String.valueOf(calendar.get(11));
            try {
                JSONObject optJSONObject = obj.optJSONObject("h");
                if (optJSONObject == null) {
                    optJSONObject = new JSONObject();
                    obj.put("h", optJSONObject);
                }
                JSONObject optJSONObject2 = optJSONObject.optJSONObject(valueOf);
                if (optJSONObject2 == null) {
                    optJSONObject2 = new JSONObject();
                    optJSONObject.put(valueOf, optJSONObject2);
                }
                optJSONObject2.put(str, optJSONObject2.optInt(str) + 1);
                capCounts(optJSONObject2, 50);
                if (!day().equals(obj.optString("last"))) {
                    obj.put("last", day());
                    JSONArray optJSONArray = obj.optJSONArray("wake");
                    JSONArray jSONArray = new JSONArray();
                    if (optJSONArray != null) {
                        for (int max = Math.max(0, optJSONArray.length() - 13); max < optJSONArray.length(); max++) {
                            jSONArray.put(optJSONArray.opt(max));
                        }
                    }
                    jSONArray.put((calendar.get(11) * 60) + calendar.get(12));
                    obj.put("wake", jSONArray);
                }
                ArrayList arrayList = new ArrayList();
                Iterator<String> keys = optJSONObject.keys();
                while (keys.hasNext()) {
                    arrayList.add(keys.next());
                }
                for (int i = 0; i < arrayList.size() - 24; i++) {
                    optJSONObject.remove((String) arrayList.get(i));
                }
            } catch (Exception e) {
            }
            Store.set("aos.routine", obj.toString());
        }
    }
}
