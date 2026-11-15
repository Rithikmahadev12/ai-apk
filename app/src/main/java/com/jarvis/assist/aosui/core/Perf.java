package com.jarvis.assist.aosui.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.provider.Settings;
import android.view.Choreographer;
import com.jarvis.assist.aosui.ai.KeyCheck;
import com.jarvis.assist.aosui.core.PerfModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Perf {
    static Context app;
    static PerfModel.Gov gov;
    static boolean listening;
    static Runnable mirror;
    static boolean saver;
    static int thermal;
    static PerfModel.Knobs K = PerfModel.knobs("BALANCED");
    static String tier = "BALANCED";
    static JSONObject st = new JSONObject();
    static int maxHz = 60;
    static final ArrayList<Runnable> subs = new ArrayList<>();
    public static final ArrayList<String> steps = new ArrayList<>();
    static int[] panel = new int[0];
    static JSONObject devInfo = new JSONObject();
    static String lastRm = "";
    static String lastForce = "";

    private Perf() {
    }

    public static PerfModel.Knobs k() {
        return K;
    }

    public static JSONObject state() {
        try {
            return new JSONObject(st.toString());
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static int thermalStatus() {
        return thermal;
    }

    public static List<String> rates() {
        return PerfModel.rates(maxHz, panel);
    }

    public static void set(String str, Object obj) {
        try {
            st.put(str, obj);
            if ("mode".equals(str)) {
                st.put("tier", "auto".equals(obj) ? st.optString("auto", "BALANCED") : st.optString("tier", "BALANCED"));
            }
        } catch (Exception e) {
        }
        gov = null;
        changed(true);
    }

    public static void retest(final Runnable runnable) {
        final Bitmap createBitmap = Bitmap.createBitmap(280, 280, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(createBitmap);
        final Paint paint = new Paint(1);
        final Path path = new Path();
        final long[] jArr = {0};
        final int[] iArr = {0};
        final double[] dArr = {0.0d};
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() { // from class: com.jarvis.assist.aosui.core.Perf.1
            @Override // android.view.Choreographer.FrameCallback
            public void doFrame(long j) {
                long j2 = j / 1000000;
                int i = 0;
                if (jArr[0] == 0) {
                    jArr[0] = j2;
                }
                long nanoTime = System.nanoTime();
                double d = 1000.0d;
                double d2 = j2 / 1000.0d;
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                int i2 = 0;
                while (i2 < 5) {
                    path.reset();
                    int i3 = i;
                    while (i3 <= 72) {
                        double d3 = (i3 / 72.0d) * 6.2832d;
                        int i4 = i;
                        long j3 = nanoTime;
                        double d4 = d;
                        double d5 = i2;
                        double sin = 84.0f * ((((Math.sin((2.0d * d3) + d2 + d5) * 0.55d) + (Math.sin(((3.0d * d3) - (0.7d * d2)) + d5) * 0.3d) + (Math.sin((5.0d * d3) + (1.6d * d2)) * 0.15d)) * 0.09d) + 1.0d);
                        float cos = (float) ((Math.cos(d3) * sin) + 140.0d);
                        float sin2 = (float) ((Math.sin(d3) * sin) + 140.0d);
                        Path path2 = path;
                        if (i3 == 0) {
                            path2.moveTo(cos, sin2);
                        } else {
                            path2.lineTo(cos, sin2);
                        }
                        i3++;
                        i = i4;
                        nanoTime = j3;
                        d = d4;
                    }
                    path.close();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setShader(new RadialGradient(140.0f, 140.0f, 92.4f, 5941724, 777693660, Shader.TileMode.CLAMP));
                    canvas.drawPath(path, paint);
                    paint.setShader(null);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(2.5f);
                    paint.setColor(-648369700);
                    canvas.drawPath(path, paint);
                    paint.setStrokeWidth(10.0f);
                    paint.setColor(693807580);
                    canvas.drawPath(path, paint);
                    i2++;
                    i = i;
                    nanoTime = nanoTime;
                }
                int i5 = i;
                double d6 = d;
                double[] dArr2 = dArr;
                dArr2[i5] = dArr2[i5] + ((System.nanoTime() - nanoTime) / 1000000.0d);
                int[] iArr2 = iArr;
                iArr2[i5] = iArr2[i5] + 1;
                if (j2 - jArr[i5] < KeyCheck.VERIFY_RETRY_MS) {
                    Choreographer.getInstance().postFrameCallback(this);
                    return;
                }
                double round = Math.round((iArr[i5] * d6) / Math.max(1L, j2 - jArr[i5]));
                double round2 = Math.round((dArr[i5] / iArr[i5]) * 100.0d) / 100.0d;
                createBitmap.recycle();
                try {
                    Perf.st.put("measured", new JSONObject().put("fps", (int) round).put("ms", round2).put("at", System.currentTimeMillis()));
                    String pick = PerfModel.pick(Perf.devInfo.optBoolean("lowRam"), Perf.devInfo.optInt("memClass"), Perf.devInfo.optLong("totalMem"), Perf.devInfo.optInt("cores"), Perf.devInfo.optInt("sdk"), round, round2);
                    Perf.st.put("auto", pick);
                    Perf.st.put("tier", pick);
                } catch (Exception e) {
                }
                Perf.gov = null;
                Perf.steps.clear();
                Perf.changed(true);
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    public static String tier() {
        return tier;
    }

    public static void on(Runnable runnable) {
        if (subs.contains(runnable)) {
            return;
        }
        subs.add(runnable);
    }

    public static void off(Runnable runnable) {
        subs.remove(runnable);
    }

    public static void onMirror(Runnable runnable) {
        mirror = runnable;
    }

    public static void offMirror(Runnable runnable) {
        if (mirror == runnable) {
            mirror = null;
        }
    }

    static void step(String str) {
        steps.add(str);
        while (steps.size() > 40) {
            steps.remove(0);
        }
    }

    public static void init(Context context, String str) {
        JSONObject jSONObject;
        app = context.getApplicationContext();
        try {
            jSONObject = new JSONObject(str);
        } catch (Exception e) {
            jSONObject = new JSONObject();
        }
        thermal = jSONObject.optInt("thermal", 0);
        saver = jSONObject.optBoolean("powerSave", false);
        JSONArray optJSONArray = jSONObject.optJSONArray("rates");
        int optInt = jSONObject.optInt("rate", 60);
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                optInt = Math.max(optInt, optJSONArray.optInt(i));
            }
        }
        panel = new int[optJSONArray == null ? 0 : optJSONArray.length()];
        for (int i2 = 0; i2 < panel.length; i2++) {
            panel[i2] = (int) Math.round(optJSONArray.optDouble(i2));
        }
        devInfo = jSONObject;
        maxHz = Math.max(30, Math.min(120, optInt));
        st = Store.obj("aos.perf");
        try {
            String[] strArr = {"mode", "fps"};
            for (int i3 = 0; i3 < 2; i3++) {
                String str2 = strArr[i3];
                if (!st.has(str2)) {
                    st.put(str2, "auto");
                }
            }
            String[] strArr2 = {"blur", "orb", "dock", "motion", "boot"};
            for (int i4 = 0; i4 < 5; i4++) {
                String str3 = strArr2[i4];
                if (!st.has(str3)) {
                    st.put(str3, 1);
                }
            }
            if (!st.has("meter")) {
                st.put("meter", 0);
            }
            if (!st.has("auto")) {
                String pick = PerfModel.pick(jSONObject.optBoolean("lowRam"), jSONObject.optInt("memClass"), jSONObject.optLong("totalMem"), jSONObject.optInt("cores"), jSONObject.optInt("sdk"), 0.0d, 0.0d);
                st.put("auto", pick);
                if (!st.has("tier")) {
                    st.put("tier", pick);
                }
            }
        } catch (Exception e2) {
        }
        compute();
        save();
        if (listening) {
            return;
        }
        listening = true;
        Store.on(new Runnable() { // from class: com.jarvis.assist.aosui.core.Perf$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Perf.lambda$init$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$init$0() {
        JSONObject obj = Store.obj("aos.perf");
        if (obj.length() > 0 && !obj.toString().equals(st.toString())) {
            st = obj;
            gov = null;
        } else if (!rmChanged() && forced().equals(lastForce)) {
            return;
        }
        changed(false);
    }

    static boolean rmChanged() {
        String str = Store.get("aos.rm", "");
        if (str.equals(lastRm)) {
            return false;
        }
        lastRm = str;
        return true;
    }

    static boolean reduceMotion() {
        if ("1".equals(Store.get("aos.rm", ""))) {
            return true;
        }
        try {
            if (app != null) {
                if (Settings.Global.getFloat(app.getContentResolver(), "animator_duration_scale", 1.0f) == 0.0f) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String forced() {
        if (app == null || (app.getApplicationInfo().flags & 2) == 0) {
            return "";
        }
        String str = Store.get("aos.perf.force", "");
        return (PerfModel.idx(str) >= 0 || "BATTERY".equals(str)) ? str : "";
    }

    static void compute() {
        String[] strArr = new String[1];
        String optString = st.optString("tier", st.optString("auto", "BALANCED"));
        String forced = forced();
        lastForce = forced;
        if (forced.isEmpty()) {
            forced = st.optString("mode", "auto");
        }
        K = PerfModel.compute(forced, optString, st.optString("fps", "auto"), maxHz, thermal >= 3, saver, reduceMotion(), st.optInt("blur", 1) != 0, st.optInt("orb", 1) != 0, st.optInt("dock", 1) != 0, st.optInt("motion", 1) != 0, st.optInt("boot", 1) != 0, strArr);
        tier = strArr[0];
    }

    static void save() {
        Store.set("aos.perf", st.toString());
        try {
            boolean z = true;
            JSONObject put = new JSONObject().put("tier", tier).put("lockFps", K.lockFps).put("motion", st.optInt("motion", 1)).put("fps", K.fps);
            if (!"HIGH".equals(tier) || K.fps <= 60) {
                z = false;
            }
            Store.setHome("aos.perf", put.put("high", z).toString());
        } catch (Exception e) {
        }
        if (mirror != null) {
            mirror.run();
        }
    }

    static void changed(boolean z) {
        compute();
        if (z) {
            save();
        } else if (mirror != null) {
            mirror.run();
        }
        Iterator it = new ArrayList(subs).iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
    }

    public static void thermal(int i) {
        thermal = i;
        changed(true);
    }

    public static void powerSave(boolean z) {
        saver = z;
        changed(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void frame(float f, float f2) {
        if (!"auto".equals(st.optString("mode", "auto")) || thermal >= 3 || saver || !lastForce.isEmpty()) {
            return;
        }
        float max = Math.max(1000.0f / K.fps, f2);
        if (gov == null) {
            gov = new PerfModel.Gov(max);
        }
        gov.target = max;
        gov.canUp = PerfModel.idx(tier) > PerfModel.idx(st.optString("auto", "HIGH"));
        String push = gov.push(f);
        if (push == null) {
            return;
        }
        try {
            if ("down".equals(push) && !"LIGHT".equals(tier)) {
                st.put("tier", PerfModel.down(tier));
                step("down:" + st.optString("tier"));
                changed(true);
            } else if ("up".equals(push)) {
                st.put("tier", PerfModel.up(tier));
                step("up:" + st.optString("tier"));
                changed(true);
            }
        } catch (Exception e) {
        }
    }
}
