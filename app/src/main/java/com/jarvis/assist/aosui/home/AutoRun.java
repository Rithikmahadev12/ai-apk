package com.jarvis.assist.aosui.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.ai.AndroidHost;
import com.jarvis.assist.aosui.ai.AutoAI;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.home.HomeData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AutoRun {
    static final String PREFS = "aos.auto";
    final AutoAI ai;
    final Backend be;
    Map<String, Map<String, Integer>> hrs0;
    final ExecutorService io;
    long lastCheck;
    volatile boolean running;
    Map<String, Integer> use0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoRun(Backend backend, ExecutorService executorService) {
        this.be = backend;
        this.io = executorService;
        this.ai = new AutoAI(AndroidHost.forSettings(backend));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void kick(HomeData homeData, boolean z) {
        if (!z || this.running) {
            return;
        }
        this.lastCheck = SystemClock.uptimeMillis();
        schedule();
        final AutoAI.In input = input(homeData);
        this.running = true;
        this.io.execute(new Runnable() { // from class: com.jarvis.assist.aosui.home.AutoRun$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AutoRun.this.m267lambda$kick$0$comaugmaosohomeAutoRun(input);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$kick$0$com-augma-oso-home-AutoRun, reason: not valid java name */
    public /* synthetic */ void m267lambda$kick$0$comaugmaosohomeAutoRun(AutoAI.In in) {
        try {
            if (!this.ai.due(in).isEmpty()) {
                tops(in);
                Log.i("aOS", "auto AI ran " + String.valueOf(this.ai.run(in)));
            }
        } catch (Throwable th) {
        }
        this.running = false;
        due(this.be.ctx(), false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void tick(HomeData homeData, boolean z) {
        if (SystemClock.uptimeMillis() - this.lastCheck >= 60000 || due(this.be.ctx())) {
            kick(homeData, z);
        }
    }

    AutoAI.In input(HomeData homeData) {
        long j;
        String str;
        String str2;
        AutoAI.In in = new AutoAI.In();
        Calendar calendar = Calendar.getInstance();
        in.hour = calendar.get(11);
        in.minute = calendar.get(12);
        in.now = System.currentTimeMillis();
        try {
            JSONObject jSONObject = homeData.health;
            JSONObject optJSONObject = Store.obj("aos.wx").optJSONObject("now");
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < Math.min(3, homeData.evAll.size()); i++) {
                HomeData.Ev ev = homeData.evAll.get(i);
                jSONArray.put((ev.allDay ? "all day" : HomeData.hm(ev.start)) + " " + ev.title);
            }
            try {
                j = new JSONObject(this.be.nextAlarm()).optLong("at");
            } catch (Exception e) {
                j = 0;
            }
            JSONObject optJSONObject2 = jSONObject.optJSONObject("sleep");
            JSONObject put = in.facts.put("now", HomeData.hm(in.now));
            if (optJSONObject == null || !(optJSONObject.opt("t") instanceof Number)) {
                str = "";
                str2 = str;
            } else {
                str = "";
                str2 = Math.round(optJSONObject.optDouble("t")) + "C " + optJSONObject.optString("desc");
            }
            put.put("weather", str2).put("events", jSONArray).put("next_alarm", j > 0 ? HomeData.hm(j) : str).put("sleep_min", optJSONObject2 == null ? JSONObject.NULL : optJSONObject2.opt("min")).put("steps", jSONObject.opt("steps") == null ? JSONObject.NULL : jSONObject.opt("steps")).put("recovery", jSONObject.has("recovery") ? jSONObject.opt("recovery") : jSONObject.has("readiness") ? jSONObject.opt("readiness") : JSONObject.NULL);
        } catch (Exception e2) {
        }
        JSONObject cfg = homeData.cfg();
        ArrayList<String> strs = HomeData.strs(cfg.optJSONArray("off"));
        Iterator<String> it = HomeData.strs(cfg.optJSONArray("widgets")).iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (!"greeting".equals(next) && !strs.contains(next) && !in.widgets.contains(next)) {
                in.widgets.add(next);
            }
        }
        HashMap hashMap = new HashMap();
        JSONObject optJSONObject3 = Store.obj("aos.routine").optJSONObject("h");
        if (optJSONObject3 != null) {
            Iterator<String> keys = optJSONObject3.keys();
            while (keys.hasNext()) {
                String next2 = keys.next();
                JSONObject optJSONObject4 = optJSONObject3.optJSONObject(next2);
                if (optJSONObject4 != null) {
                    HashMap hashMap2 = new HashMap();
                    Iterator<String> keys2 = optJSONObject4.keys();
                    while (keys2.hasNext()) {
                        String next3 = keys2.next();
                        hashMap2.put(next3, Integer.valueOf(optJSONObject4.optInt(next3)));
                    }
                    hashMap.put(next2, hashMap2);
                }
            }
        }
        final Map map = (Map) hashMap.get(String.valueOf(in.hour));
        if (map != null) {
            ArrayList arrayList = new ArrayList(map.keySet());
            arrayList.sort(new Comparator() { // from class: com.jarvis.assist.aosui.home.AutoRun$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return AutoRun.lambda$input$1(map, (String) obj, (String) obj2);
                }
            });
            for (int i2 = 0; i2 < Math.min(5, arrayList.size()); i2++) {
                in.topNow.put(arrayList.get(i2));
            }
        }
        HashMap hashMap3 = new HashMap();
        JSONObject obj = Store.obj("aos.usage");
        Iterator<String> keys3 = obj.keys();
        while (keys3.hasNext()) {
            String next4 = keys3.next();
            hashMap3.put(next4, Integer.valueOf(obj.optInt(next4)));
        }
        this.hrs0 = hashMap;
        this.use0 = hashMap3;
        return in;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$input$1(Map map, String str, String str2) {
        return ((Integer) map.get(str2)).intValue() - ((Integer) map.get(str)).intValue();
    }

    void tops(AutoAI.In in) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            JSONArray jSONArray = new JSONArray(this.be.apps());
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject optJSONObject = jSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    linkedHashMap.put(optJSONObject.optString("pkg"), optJSONObject.optString("label"));
                }
            }
        } catch (Exception e) {
        }
        try {
            for (String str : HomeRules.suggest(this.hrs0, this.use0, linkedHashMap.keySet(), in.hour, 8, null)) {
                in.top.put(new JSONObject().put("pkg", str).put("name", linkedHashMap.get(str)));
            }
        } catch (Exception e2) {
        }
    }

    void schedule() {
        Context ctx = this.be.ctx();
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService("alarm");
        if (alarmManager == null) {
            return;
        }
        PendingIntent broadcast = PendingIntent.getBroadcast(ctx, 7, new Intent(ctx, (Class<?>) Due.class), 201326592);
        boolean z = false;
        for (String str : AutoAI.IDS) {
            if (this.ai.F.on(str)) {
                z = true;
            }
        }
        if (z && !ctx.getSharedPreferences(PREFS, 0).getBoolean("alarm", false)) {
            alarmManager.setInexactRepeating(3, SystemClock.elapsedRealtime() + 900000, 900000L, broadcast);
            ctx.getSharedPreferences(PREFS, 0).edit().putBoolean("alarm", true).apply();
        } else {
            if (z || !ctx.getSharedPreferences(PREFS, 0).getBoolean("alarm", false)) {
                return;
            }
            alarmManager.cancel(broadcast);
            ctx.getSharedPreferences(PREFS, 0).edit().putBoolean("alarm", false).apply();
        }
    }

    static boolean due(Context context) {
        return context.getSharedPreferences(PREFS, 0).getBoolean("due", false);
    }

    static void due(Context context, boolean z) {
        context.getSharedPreferences(PREFS, 0).edit().putBoolean("due", z).apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void note(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optString("key").isEmpty()) {
                return;
            }
            this.ai.noteNotif(jSONObject);
        } catch (Throwable th) {
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Due extends BroadcastReceiver {
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            AutoRun.due(context, true);
        }
    }
}
