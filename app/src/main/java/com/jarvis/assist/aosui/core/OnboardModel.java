package com.jarvis.assist.aosui.core;

import com.jarvis.assist.aosui.ai.Keys;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class OnboardModel {
    public static final int AI = 5;
    public static final int ESSENTIALS = 2;
    public static final int HOME = 6;
    public static final int LAST = 7;
    public static final int PHONE = 3;
    public boolean manual;
    public boolean run;
    public int st;
    public boolean viaCustom;
    public static final String[] STEPS = {"welcome", "yours", "essentials", "phone", "health", "ai", "home", "done"};
    public static final String[] ESS = {"calendar", "contacts", "location", "notifAccess", "health"};
    public static final String K_STEP = "aos.ob.step";
    public static final String K_PATH = "aos.ob.path";
    public static final String K_DONE = "aos.ob.done";
    public static final String[] KEYS = {K_STEP, K_PATH, K_DONE};
    public final Set<String> asked = new LinkedHashSet();
    public String phase = "";
    public String msg = "";
    public String busy = "";
    public String done = "";

    public static boolean allOk(JSONObject jSONObject) {
        for (String str : ESS) {
            if (!jSONObject.optBoolean(str)) {
                return false;
            }
        }
        return true;
    }

    public void next(boolean z) {
        this.run = false;
        this.st = (this.st != 2 || this.viaCustom) ? Math.min(7, this.st + 1) : z ? 6 : 5;
    }

    public void next() {
        next(false);
    }

    public void back(boolean z) {
        this.run = false;
        if (this.viaCustom || this.st == 7) {
            this.st = Math.max(0, this.st - 1);
            return;
        }
        int i = 2;
        if (this.st == 6) {
            if (!z) {
                i = 5;
            }
        } else if (this.st != 5) {
            this.st = Math.max(0, this.st - 1);
            return;
        }
        this.st = i;
    }

    public void back() {
        back(false);
    }

    public void customise() {
        this.run = false;
        this.viaCustom = true;
        this.st = 3;
    }

    public static boolean skipAiShown(boolean z) {
        return !z;
    }

    public void skipAi() {
        this.run = false;
        this.st = 6;
    }

    public boolean showBack() {
        return this.st > 0;
    }

    public String nextLabel(JSONObject jSONObject) {
        return this.st == 7 ? "ob.enter" : this.st == 0 ? "ob.start" : (this.st != 6 || jSONObject.optBoolean("home")) ? (this.st == 2 && this.phase.isEmpty() && !allOk(jSONObject)) ? "ob.skip" : "ob.next" : "ob.notnow";
    }

    public void start() {
        this.run = true;
        this.asked.clear();
        this.msg = "";
    }

    public String esNext(JSONObject jSONObject) {
        if (!this.run) {
            return null;
        }
        for (String str : ESS) {
            if (!jSONObject.optBoolean(str) && !this.asked.contains(str)) {
                this.asked.add(str);
                if (!"health".equals(str) || !jSONObject.has("healthGate") || "connect".equals(jSONObject.optString("healthGate"))) {
                    return str;
                }
            }
        }
        this.run = false;
        this.phase = "ai";
        return null;
    }

    public String essChip(JSONObject jSONObject, String str) {
        if (jSONObject.optBoolean(str)) {
            return "on";
        }
        if (!this.run || !this.asked.contains(str)) {
            return this.asked.contains(str) ? "skipped" : "";
        }
        Iterator<String> it = this.asked.iterator();
        String str2 = null;
        while (it.hasNext()) {
            str2 = it.next();
        }
        return str.equals(str2) ? "asking" : "skipped";
    }

    public boolean aiShown(JSONObject jSONObject) {
        return "ai".equals(this.phase) || allOk(jSONObject);
    }

    public String startLabel(JSONObject jSONObject) {
        if (aiShown(jSONObject)) {
            return null;
        }
        return this.run ? "ob.ess.asking" : this.asked.isEmpty() ? "ob.ess.start" : "ob.ess.again";
    }

    public static String healthGate(int i, boolean z, boolean z2) {
        if (i < 34 || !z) {
            return "unsupported";
        }
        return z2 ? "on" : "connect";
    }

    public Map<String, String> persist() {
        String str;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = this.asked.iterator();
        while (true) {
            str = ",";
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            if (sb.length() <= 0) {
                str = "";
            }
            sb.append(str).append(next);
        }
        if ("ai".equals(this.phase)) {
            sb.append(sb.length() <= 0 ? "" : ",").append("ai");
        }
        linkedHashMap.put(K_STEP, String.valueOf(this.st));
        linkedHashMap.put(K_PATH, this.viaCustom ? "custom" : "essentials");
        linkedHashMap.put(K_DONE, sb.toString());
        return linkedHashMap;
    }

    public void restore(String str, String str2, String str3) {
        String trim;
        this.run = false;
        this.asked.clear();
        this.phase = "";
        this.viaCustom = "custom".equals(str2);
        this.st = 0;
        if (str == null) {
            trim = "0";
        } else {
            try {
                trim = str.trim();
            } catch (NumberFormatException e) {
                this.st = 0;
            }
        }
        this.st = Math.max(0, Math.min(7, Integer.parseInt(trim)));
        if (str3 != null) {
            String[] split = str3.split(",");
            for (String str4 : split) {
                String trim2 = str4.trim();
                if ("ai".equals(trim2)) {
                    this.phase = "ai";
                } else {
                    for (String str5 : ESS) {
                        if (str5.equals(trim2)) {
                            this.asked.add(str5);
                        }
                    }
                }
            }
        }
    }

    public static String keyKind(String str) {
        return Keys.kind(str);
    }

    public static String bootMode(boolean z, String str, boolean z2) {
        if (z) {
            return null;
        }
        return "skip".equals(str) ? "skip" : ("short".equals(str) || z2) ? "short" : "full";
    }

    public static int bootTarget(boolean z, boolean z2, boolean z3, boolean z4) {
        return (z ? 20 : 0) + (z2 ? 30 : 0) + (z3 ? 25 : 0) + (z4 ? 25 : 0);
    }

    public static float bootStep(float f, int i, long j, boolean z) {
        if (j > (z ? 900 : 2200)) {
            i = 100;
        }
        float f2 = i;
        return Math.min(f + ((f2 - f) * 0.08f) + (f2 > f ? 0.15f : 0.0f), f2);
    }
}
