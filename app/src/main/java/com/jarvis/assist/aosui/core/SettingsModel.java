package com.jarvis.assist.aosui.core;

import com.jarvis.assist.aosui.ai.ModelList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class SettingsModel {
    public static final int MAXV = 200000;
    public static final String[] WHEEL = {"health", "weather", "gallery", "calendar", "settings", "clock", "music", "files", "assistant", "apps", "phone", "wifi", "vault"};
    public static final String[] QS = {"wifi", "bt", "torch", "dnd", "rotate", "location", "battery", "sound", "lock", "hotspot", "airplane", "saver", "nfc", "dark", "bright"};
    public static final String[] DOCK = {"dial", "sms", "browser", "camera", "email", "maps", "music", "calendar", "clock", "gallery", "contacts", "calculator", "vault"};
    public static final String[] WIDGETS = {"greeting", "health", "weather", "calendar", "recent", "media", "alarms", "notifs", "battery", "sleep", "activity", "people", "screentime", "worldclock", "assistant", "storage", AppGridModel.ID, "gmail"};
    public static final String[] NEWW = {"people", "screentime", "worldclock", "assistant", "storage", AppGridModel.ID, "gmail"};
    public static final String[] DEF_WHEEL = {"health", "weather", "gallery", "calendar", "settings"};
    public static final String[] DEF_QS = {"wifi", "bt", "torch", "sound", "rotate", "dnd"};
    public static final String[] DEF_DOCK = {"dial", "sms", "browser", "camera", "aos:search"};
    public static final String[] DEF_OFF = {"alarms", "notifs", "battery", "sleep", "activity", "people", "screentime", "worldclock", "assistant", "storage", AppGridModel.ID, "gmail"};
    public static final String[] LAND = {"centre", "wheel", "arc"};
    public static final String[][] PAL = {new String[]{"#5aa9dc", "#4ea3ff", "#7fd1c7", "#a07cff", "#3fb37f", "#ff7a45"}, new String[]{"#e4ebf1", "#ffffff", "#d9e3ff", "#e8e2f4", "#d6f5ea", "#f2e6df"}, new String[]{"#c8102e", "#e0475b", "#ff7a45", "#a07cff", "#ff5c8a", "#5aa9dc"}};
    public static final String[] COLOR_KEYS = {"accent", "ink", "alert"};
    public static final String[] COLOR_DEF = {"#5aa9dc", "#e4ebf1", "#c8102e"};
    public static final int[][] TP = {new int[]{8, 2}, new int[]{42, 12}, new int[]{70, 18}, new int[]{100, 0}};
    public static final String[] WXS = {"auto", "openmeteo", "met", "owm", "wapi"};
    public static final String[] ZDEF = {"Europe/London", "America/New_York", "Asia/Tokyo"};
    public static final String[] PROVIDERS = {"claude", "gemini", "openai"};
    public static final String[][] OAI = {new String[]{"openai", "OpenAI", "https://api.openai.com/v1", "gpt-5-nano"}, new String[]{"mistral", "Mistral", "https://api.mistral.ai/v1", "mistral-small-latest"}, new String[]{"openrouter", "OpenRouter", "https://openrouter.ai/api/v1", "openrouter/auto"}, new String[]{"groq", "Groq", "https://api.groq.com/openai/v1", "llama-3.1-8b-instant"}, new String[]{"custom", "Custom", "", ""}};
    public static final String[] MODELS_CLAUDE = {ModelList.CLAUDE_DEFAULT, "claude-sonnet-5-5", "claude-opus-5-5"};
    public static final String[] MODELS_GEMINI = {ModelList.GEMINI_DEFAULT, "gemini-flash-latest", "gemini-pro-latest"};
    public static final String[] SCREENS = {"root", "home", "widgets", AppGridModel.ID, "customcontrol", "customquick", "customgrid", "customdock", "custompages", "google", "clocks", "brief", "lang", "backup", "look", "text", "ai", "weather", "health", "aif", "aifleave", "aifmute", "privacy", "about", "fx"};
    public static final String[] HSRCS = {"hc", "whoop", "oura", "uh", "apple"};
    static final Pattern SCREEN = Pattern.compile("^[a-z]+(:[a-z]+)?$");
    public static final Pattern EXCL = Pattern.compile("^aos\\.(ic|wx|recent|usage|chats|onboarded|sort|aiu|ntag|nday|npend|nscam|routine|rates|homebanner|youchip|google|perf\\.force|aif\\..+)$");
    public static final Pattern SECRET = Pattern.compile("(?i).*(key|token|secret|pass).*");
    public static final List<String> NAT = Arrays.asList("aos.colors", "aos.theme", "aos.cfg", "aos.wxcfg", "aos.rm", "aos.lang", "ai.name", "ai.model.claude", "ai.model.gemini", "ai.speak", "ai.briefhome", "ai.voice.lang", "ai.voice.name", "ai.voice.rate", "ai.voice.pitch", "ai.voice.engine", "ai.voice.gem", "ai.voice.autosend", "ai.voice.conv", "ai.pa.voice", "ai.pa.fallback", "health.goals", "health.pref");

    private SettingsModel() {
    }

    public static String[] catalog(String str) {
        return ("qs".equals(str) || "qs2".equals(str)) ? QS : "dock".equals(str) ? DOCK : WHEEL;
    }

    public static int slots(String str) {
        if ("qs".equals(str)) {
            return 6;
        }
        if ("qs2".equals(str)) {
            return 7;
        }
        "dock".equals(str);
        return 5;
    }

    static JSONObject J(String str) {
        if (str != null) {
            try {
                if (str.isEmpty()) {
                    return null;
                }
                return new JSONObject(str);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    static JSONArray JA(String str) {
        if (str != null) {
            try {
                if (str.isEmpty()) {
                    return null;
                }
                return new JSONArray(str);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    static List<String> list(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.optString(i));
            }
        }
        return arrayList;
    }

    static List<String> list(String... strArr) {
        return new ArrayList(Arrays.asList(strArr));
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Cfg {
        public final LinkedHashMap<String, String> aw = new LinkedHashMap<>();
        public boolean dbl;
        public List<String> dock;
        public String land;
        public List<String> off;
        public List<String> qs;
        public List<String> qs2;
        public List<String> wheel;
        public List<String> widgets;

        public List<String> slots(String str) {
            return "qs".equals(str) ? this.qs : "qs2".equals(str) ? this.qs2 : "dock".equals(str) ? this.dock : this.wheel;
        }

        public int onCount() {
            Iterator<String> it = this.widgets.iterator();
            int i = 0;
            while (it.hasNext()) {
                if (!this.off.contains(it.next())) {
                    i++;
                }
            }
            return i;
        }
    }

    public static Cfg cfg(String str, String str2, String str3) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        Cfg cfg = new Cfg();
        JSONArray JA = JA(str3);
        if (JA != null) {
            for (int i = 0; i < JA.length(); i++) {
                JSONObject optJSONObject = JA.optJSONObject(i);
                if (optJSONObject != null) {
                    cfg.aw.put("aw:" + optJSONObject.optInt("id"), optJSONObject.optString("label", "Widget").isEmpty() ? "Widget" : optJSONObject.optString("label", "Widget"));
                }
            }
        }
        cfg.wheel = J.optJSONArray("wheel") != null ? list(J.optJSONArray("wheel")) : list(DEF_WHEEL);
        cfg.qs = J.optJSONArray("qs") != null ? list(J.optJSONArray("qs")) : list(DEF_QS);
        cfg.qs2 = J.optJSONArray("qs2") != null ? HomeSlots.clean("qs2", list(J.optJSONArray("qs2"))) : list(HomeSlots.DEF_QS2);
        cfg.dock = J.optJSONArray("dock") != null ? list(J.optJSONArray("dock")) : list(DEF_DOCK);
        JSONArray optJSONArray = J.optJSONArray("widgets");
        ArrayList arrayList = new ArrayList();
        for (String str4 : optJSONArray != null ? list(optJSONArray) : list(WIDGETS)) {
            if (ok(cfg, str4)) {
                arrayList.add(str4);
            }
        }
        cfg.widgets = new ArrayList(arrayList);
        for (String str5 : WIDGETS) {
            if (!arrayList.contains(str5)) {
                cfg.widgets.add(str5);
            }
        }
        for (String str6 : cfg.aw.keySet()) {
            if (!arrayList.contains(str6)) {
                cfg.widgets.add(str6);
            }
        }
        cfg.off = new ArrayList();
        for (String str7 : J.optJSONArray("off") != null ? list(J.optJSONArray("off")) : list(DEF_OFF)) {
            if (ok(cfg, str7)) {
                cfg.off.add(str7);
            }
        }
        if (optJSONArray != null) {
            List<String> list = list(optJSONArray);
            for (String str8 : WIDGETS) {
                if (!list.contains(str8)) {
                    cfg.off.add(str8);
                }
            }
        }
        if (str2 == null || str2.isEmpty()) {
            str2 = "centre";
        }
        cfg.land = str2;
        cfg.dbl = !Boolean.FALSE.equals(J.opt("dbl"));
        return cfg;
    }

    static boolean ok(Cfg cfg, String str) {
        return !String.valueOf(str).startsWith("aw:") || cfg.aw.containsKey(str);
    }

    public static String save(String str, Cfg cfg) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        try {
            J.put("wheel", new JSONArray((Collection) cfg.wheel));
            J.put("qs", new JSONArray((Collection) cfg.qs));
            if (cfg.qs2 != null) {
                J.put("qs2", new JSONArray((Collection) cfg.qs2));
            }
            J.put("dock", new JSONArray((Collection) cfg.dock));
            J.put("widgets", new JSONArray((Collection) cfg.widgets));
            J.put("off", new JSONArray((Collection) cfg.off));
            J.put("dbl", cfg.dbl);
        } catch (Exception e) {
        }
        return J.toString();
    }

    public static boolean move(List<String> list, int i, int i2) {
        int i3 = i2 + i;
        if (i < 0 || i3 < 0 || i >= list.size() || i3 >= list.size()) {
            return false;
        }
        String str = list.get(i);
        list.set(i, list.get(i3));
        list.set(i3, str);
        return true;
    }

    public static void toggleOff(Cfg cfg, String str) {
        boolean contains = cfg.off.contains(str);
        List<String> list = cfg.off;
        if (contains) {
            list.remove(str);
        } else {
            list.add(str);
        }
    }

    public static void resetSlots(Cfg cfg) {
        cfg.wheel = list(DEF_WHEEL);
        cfg.qs = list(DEF_QS);
        cfg.qs2 = list(HomeSlots.DEF_QS2);
        cfg.dock = list(DEF_DOCK);
    }

    public static void addWidget(Cfg cfg, String str) {
        if (!cfg.widgets.contains(str)) {
            cfg.widgets.add(str);
        }
        cfg.off.remove(str);
    }

    public static void removeWidget(Cfg cfg, String str) {
        cfg.widgets.remove(str);
        cfg.off.remove(str);
    }

    public static String appEntry(String str, String str2, String str3) {
        return "app:" + str + "/" + str2 + "|" + str3;
    }

    public static String[] slotLines(String str) {
        String trim = str == null ? "" : str.trim();
        int indexOf = trim.indexOf(" · ");
        if (indexOf <= 0) {
            return new String[]{trim, null};
        }
        String trim2 = trim.substring(0, indexOf).trim();
        int i = indexOf + 3;
        return new String[]{trim2, trim.substring(i).trim().isEmpty() ? null : trim.substring(i).trim()};
    }

    public static String appLabel(String str) {
        if (str == null || !str.startsWith("app:")) {
            return null;
        }
        int indexOf = str.indexOf(124);
        return (indexOf < 0 || indexOf == str.length() + (-1)) ? "App" : str.substring(indexOf + 1);
    }

    public static boolean colorApplies(String str, String str2) {
        return ("ink".equals(str) && "light".equals(str2)) ? false : true;
    }

    public static String color(String str, String str2) {
        JSONObject J = J(str);
        int indexOf = Arrays.asList(COLOR_KEYS).indexOf(str2);
        String str3 = indexOf < 0 ? "" : COLOR_DEF[indexOf];
        String optString = J != null ? J.optString(str2, "") : "";
        return optString.isEmpty() ? str3 : optString;
    }

    public static String colorSet(String str, String str2, String str3) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        try {
            J.put(str2, str3);
        } catch (Exception e) {
        }
        return J.toString();
    }

    public static boolean hexOk(String str) {
        return str != null && str.matches("^#[0-9a-fA-F]{6}$");
    }

    public static int[] ui(String str) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        int i = 42;
        if (J.has("tint") && !J.isNull("tint")) {
            i = J.optInt("tint", 42);
        }
        int i2 = 12;
        if (J.has("blur") && !J.isNull("blur")) {
            i2 = J.optInt("blur", 12);
        }
        return new int[]{i, i2, J.optInt("text", 0) != 0 ? J.optInt("text", 100) : 100};
    }

    public static String uiSet(String str, int i, int i2, int i3) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        try {
            J.put("tint", Math.max(0, Math.min(100, i)));
            J.put("blur", Math.max(0, Math.min(24, i2)));
            J.put("text", Math.max(85, Math.min(130, i3)));
        } catch (Exception e) {
        }
        return J.toString();
    }

    public static String wxConn(boolean z, boolean z2, boolean z3) {
        return z ? "key" : !z2 ? "noloc" : !z3 ? "default" : "ok";
    }

    public static String[] wx(String str) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        String optString = J.optString("src", "");
        if (optString.isEmpty()) {
            optString = "auto";
        }
        return new String[]{optString, "f".equals(J.optString("units")) ? "f" : "c"};
    }

    public static String wxSet(String str, String str2, String str3) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        String[] wx = wx(str);
        try {
            J.put("src", wx[0]);
            J.put("units", wx[1]);
            J.put(str2, str3);
        } catch (Exception e) {
        }
        return J.toString();
    }

    public static int[] aimem(String str) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        return new int[]{!Boolean.FALSE.equals(J.opt("on")) ? 1 : 0, (!J.has("turns") || J.isNull("turns")) ? 12 : J.optInt("turns", 0)};
    }

    public static String aimemSet(String str, Boolean bool, Integer num) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        int[] aimem = aimem(str);
        try {
            J.put("on", bool != null ? bool.booleanValue() : aimem[0] == 1);
            J.put("turns", num != null ? Math.max(0, Math.min(40, num.intValue())) : aimem[1]);
        } catch (Exception e) {
        }
        return J.toString();
    }

    public static List<String> notes(String str) {
        ArrayList arrayList = new ArrayList();
        JSONArray JA = JA(str);
        if (JA != null) {
            for (int i = 0; i < JA.length(); i++) {
                JSONObject optJSONObject = JA.optJSONObject(i);
                if (optJSONObject != null && !optJSONObject.optString("t").isEmpty()) {
                    arrayList.add(optJSONObject.optString("t"));
                }
            }
        }
        return arrayList;
    }

    public static String noteAdd(String str, String str2, long j) {
        String trim = str2 == null ? "" : str2.trim();
        if (trim.length() > 300) {
            trim = trim.substring(0, BootSeq.UN_HOLD_MS);
        }
        JSONArray jSONArray = new JSONArray();
        if (trim.isEmpty()) {
            return str;
        }
        try {
            jSONArray.put(new JSONObject().put("t", trim).put("at", j));
            JSONArray JA = JA(str);
            if (JA != null) {
                for (int i = 0; i < JA.length() && jSONArray.length() < 30; i++) {
                    JSONObject optJSONObject = JA.optJSONObject(i);
                    if (optJSONObject != null && !optJSONObject.optString("t").isEmpty() && !optJSONObject.optString("t").equals(trim)) {
                        jSONArray.put(optJSONObject);
                    }
                }
            }
        } catch (Exception e) {
        }
        return jSONArray.toString();
    }

    public static String noteDel(String str, int i) {
        JSONArray jSONArray = new JSONArray();
        JSONArray JA = JA(str);
        if (JA != null) {
            int i2 = 0;
            for (int i3 = 0; i3 < JA.length(); i3++) {
                JSONObject optJSONObject = JA.optJSONObject(i3);
                if (optJSONObject != null && !optJSONObject.optString("t").isEmpty()) {
                    int i4 = i2 + 1;
                    if (i2 != i) {
                        jSONArray.put(optJSONObject);
                    }
                    i2 = i4;
                }
            }
        }
        return jSONArray.toString();
    }

    public static List<String> clocks(String str) {
        JSONArray JA = JA(str);
        if (JA == null || JA.length() == 0) {
            return list(ZDEF);
        }
        List<String> list = list(JA);
        return new ArrayList(list.subList(0, Math.min(3, list.size())));
    }

    public static String clockSet(String str, int i, String str2) {
        List<String> clocks = clocks(str);
        while (clocks.size() <= i) {
            clocks.add("");
        }
        if (str2 == null) {
            str2 = "";
        }
        clocks.set(i, str2);
        ArrayList arrayList = new ArrayList();
        for (String str3 : clocks) {
            if (!str3.isEmpty()) {
                arrayList.add(str3);
            }
        }
        boolean isEmpty = arrayList.isEmpty();
        Collection collection = arrayList;
        if (isEmpty) {
            collection = list(ZDEF);
        }
        return new JSONArray(collection).toString();
    }

    public static String city(String str) {
        String valueOf = String.valueOf(str);
        return valueOf.substring(valueOf.lastIndexOf(47) + 1).replace('_', ' ');
    }

    public static double[] goals(String str) {
        JSONObject J = J(str);
        if (J == null) {
            J = new JSONObject();
        }
        double optDouble = J.optDouble("steps", 0.0d);
        double optDouble2 = J.optDouble("sleep", 0.0d);
        double optDouble3 = J.optDouble("kcal", 0.0d);
        if (optDouble <= 0.0d) {
            optDouble = 8000.0d;
        }
        if (optDouble2 <= 0.0d) {
            optDouble2 = 8.0d;
        }
        if (optDouble3 <= 0.0d) {
            optDouble3 = 500.0d;
        }
        return new double[]{optDouble, optDouble2, optDouble3};
    }

    public static String goalSet(String str, String str2, double d) {
        if (d <= 0.0d) {
            return null;
        }
        double[] goals = goals(str);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("steps", num(goals[0]));
            jSONObject.put("sleep", num(goals[1]));
            jSONObject.put("kcal", num(goals[2]));
            jSONObject.put(str2, num(d));
        } catch (Exception e) {
        }
        return jSONObject.toString();
    }

    static Object num(double d) {
        return d == Math.rint(d) ? Long.valueOf((long) d) : Double.valueOf(d);
    }

    public static List<String> order(String str) {
        JSONObject J = J(str);
        JSONArray optJSONArray = J == null ? null : J.optJSONArray("order");
        return optJSONArray == null ? list("claude", "gemini") : list(optJSONArray);
    }

    public static List<String> allProviders(List<String> list) {
        ArrayList arrayList = new ArrayList(list);
        for (String str : PROVIDERS) {
            if (!arrayList.contains(str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public static String orderMove(List<String> list, String str, int i) {
        ArrayList arrayList = new ArrayList(list);
        int indexOf = arrayList.indexOf(str);
        int i2 = i + indexOf;
        if (indexOf < 0 || i2 < 0 || i2 >= arrayList.size()) {
            return null;
        }
        arrayList.set(indexOf, (String) arrayList.get(i2));
        arrayList.set(i2, str);
        return String.join(",", arrayList);
    }

    public static String orderToggle(List<String> list, String str) {
        ArrayList arrayList = new ArrayList(list);
        if (arrayList.contains(str)) {
            arrayList.remove(str);
        } else {
            arrayList.add(str);
        }
        return String.join(",", arrayList);
    }

    public static String orderWithOpenai(String str) {
        JSONObject J = J(str);
        JSONArray optJSONArray = J == null ? null : J.optJSONArray("order");
        List arrayList = optJSONArray == null ? new ArrayList() : list(optJSONArray);
        if (arrayList.contains("openai")) {
            return null;
        }
        arrayList.add("openai");
        return String.join(",", arrayList);
    }

    public static int oaiPreset(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < OAI.length - 1; i++) {
            if (str.startsWith(OAI[i][2])) {
                return i;
            }
        }
        return OAI.length - 1;
    }

    public static boolean httpsOk(String str) {
        return str != null && str.startsWith("https://");
    }

    public static String screen(String str) {
        if (str == null || str.length() > 40 || !SCREEN.matcher(str).matches()) {
            return null;
        }
        return str;
    }

    public static String[] route(String str) {
        String screen = screen(str);
        if (screen == null) {
            return null;
        }
        String[] split = screen.split(":");
        if ("health".equals(split[0]) && split.length > 1) {
            if (Arrays.asList(HSRCS).contains(split[1])) {
                return new String[]{"hg", split[1]};
            }
            return null;
        }
        if (split.length > 1 || "root".equals(split[0]) || !Arrays.asList(SCREENS).contains(split[0])) {
            return null;
        }
        return new String[]{split[0], ""};
    }

    public static String res(String str) {
        if (str == null || str.length() > 4000) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new JSONObject().put("ok", jSONObject.optBoolean("ok")).put("error", jSONObject.optString("error", "").replaceAll("[^A-Za-z0-9 .,:_-]", "")).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean exportable(String str, boolean z) {
        return (str == null || !str.startsWith("aos.") || EXCL.matcher(str).matches() || SECRET.matcher(str).matches() || (!z && "aos.mem".equals(str))) ? false : true;
    }

    public static JSONObject bkBuild(Map<String, String> map, Function<String, String> function, List<String> list, boolean z, String str) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        JSONObject jSONObject3 = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() != null && exportable(entry.getKey(), z)) {
                    jSONObject2.put(entry.getKey(), entry.getValue());
                }
            }
            for (String str2 : NAT) {
                String apply = function.apply(str2);
                if (apply != null && !apply.isEmpty()) {
                    jSONObject3.put(str2, apply);
                }
            }
            jSONObject.put("app", "aOS").put("kind", "settings").put("v", 1).put("at", str).put("ls", jSONObject2).put("prefs", jSONObject3).put("ai", new JSONObject().put("order", list == null ? JSONObject.NULL : new JSONArray((Collection) list)));
        } catch (Exception e) {
        }
        return jSONObject;
    }

    public static String noAw(String str) {
        JSONObject J = J(str);
        if (J == null) {
            return str;
        }
        try {
            String[] strArr = {"widgets", "off"};
            for (int i = 0; i < 2; i++) {
                String str2 = strArr[i];
                JSONArray optJSONArray = J.optJSONArray(str2);
                if (optJSONArray != null) {
                    JSONArray jSONArray = new JSONArray();
                    for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                        if (!String.valueOf(optJSONArray.opt(i2)).startsWith("aw:")) {
                            jSONArray.put(optJSONArray.opt(i2));
                        }
                    }
                    J.put(str2, jSONArray);
                }
            }
        } catch (Exception e) {
        }
        return J.toString();
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Plan {
        public final String k;
        public final boolean nat;
        public final String v;
        public final String what;

        Plan(String str, String str2, boolean z, String str3) {
            this.k = str;
            this.v = str2;
            this.nat = z;
            this.what = str3;
        }
    }

    public static List<Plan> bkPlan(String str, BiFunction<String, Boolean, String> biFunction) {
        JSONObject J = J(str);
        if (J == null || !"aOS".equals(J.optString("app")) || !"settings".equals(J.optString("kind")) || J.optInt("v", 0) != 1 || !(J.opt("v") instanceof Number) || J.optJSONObject("ls") == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        JSONObject optJSONObject = J.optJSONObject("ls");
        JSONObject optJSONObject2 = J.optJSONObject("prefs");
        Iterator<String> keys = optJSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            Object opt = optJSONObject.opt(next);
            if (exportable(next, true) && (opt instanceof String)) {
                String str2 = (String) opt;
                if (str2.length() <= 200000) {
                    if ("aos.cfg".equals(next)) {
                        str2 = noAw(str2);
                    }
                    if (!str2.equals(biFunction.apply(next, false))) {
                        arrayList.add(new Plan(next, str2, false, null));
                    }
                }
            }
        }
        if (optJSONObject2 != null) {
            Iterator<String> keys2 = optJSONObject2.keys();
            while (keys2.hasNext()) {
                String next2 = keys2.next();
                Object opt2 = optJSONObject2.opt(next2);
                if (NAT.contains(next2) && (opt2 instanceof String)) {
                    String str3 = (String) opt2;
                    if (str3.length() <= 200000) {
                        if ("aos.cfg".equals(next2)) {
                            str3 = noAw(str3);
                        }
                        if (!optJSONObject.has(next2) || optJSONObject.isNull(next2) || next2.startsWith("ai.") || next2.startsWith("health.")) {
                            if (!str3.equals(biFunction.apply(next2, true))) {
                                arrayList.add(new Plan(next2, str3, true, null));
                            }
                        }
                    }
                }
            }
        }
        JSONObject optJSONObject3 = J.optJSONObject("ai");
        JSONArray optJSONArray = optJSONObject3 != null ? optJSONObject3.optJSONArray("order") : null;
        if (optJSONArray != null) {
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < optJSONArray.length(); i++) {
                if (Arrays.asList(PROVIDERS).contains(optJSONArray.optString(i))) {
                    arrayList2.add(optJSONArray.optString(i));
                }
            }
            if (!arrayList2.isEmpty()) {
                arrayList.add(new Plan("ai.order", String.join(",", arrayList2), false, String.join(" → ", arrayList2).toUpperCase(Locale.ROOT)));
            }
        }
        return arrayList;
    }

    public static boolean hasNotes(List<Plan> list) {
        if (list == null) {
            return false;
        }
        Iterator<Plan> it = list.iterator();
        while (it.hasNext()) {
            if ("aos.mem".equals(it.next().k)) {
                return true;
            }
        }
        return false;
    }
}
