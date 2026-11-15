package com.jarvis.assist.aosui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import org.json.JSONObject;

import java.util.function.Consumer;

/**
 * JarvisBackend — drop-in Backend stub wired to AosUiActivity instead of
 * the AugmaOS Home launcher Activity. All network/health/widget calls
 * return safe empty JSON so the UI renders without crashing.
 * Wire real Jarvis data by overriding the relevant methods.
 */
public class JarvisBackend extends Backend {

    private final AosUiActivity mAct;

    public JarvisBackend(AosUiActivity act) {
        // Backend requires a Home instance — we pass null and override everything
        super(null);
        this.mAct = act;
    }

    @Override public Context ctx()      { return mAct; }
    @Override public Activity activity(){ return mAct; }

    @Override public String battery()   { return "{\"pct\":100,\"charging\":false,\"state\":\"norm\"}"; }
    @Override public String net()       { return ""; }
    @Override public String loc()       { return ""; }
    @Override public String apps()      { return "[]"; }
    @Override public String appsCached(){ return "[]"; }
    @Override public String nextAlarm() { return "{\"at\":0}"; }
    @Override public String phoneCal(int n) { return "[]"; }
    @Override public String notifList() { return "[]"; }
    @Override public String media()     { return "{}"; }
    @Override public String starred()   { return "{}"; }
    @Override public String screenTime(){ return "{}"; }
    @Override public long   dataToday() { return -1L; }
    @Override public String roleApp(String s) { return ""; }
    @Override public String get(String s)     { return ""; }
    @Override public String assistantInfo()   { return "{}"; }
    @Override public String aiHasKey()        { return "{}"; }
    @Override public String storage() {
        android.os.StatFs sf = new android.os.StatFs(android.os.Environment.getDataDirectory().getPath());
        long free  = sf.getAvailableBlocksLong() * sf.getBlockSizeLong();
        long total = sf.getBlockCountLong()      * sf.getBlockSizeLong();
        return "{\"free\":" + free + ",\"total\":" + total + "}";
    }

    @Override public void locRefresh(Consumer<String> cb) {}
    @Override public void weatherFetch(String src, double lat, double lon, Consumer<String> cb) {}
    @Override public void appsReady(Runnable r) { if (r != null) r.run(); }
    @Override public void askCal() {}
    @Override public void expandNotifs() {}
    @Override public void hapticFx(String type) {
        try {
            Vibrator v = (Vibrator) mAct.getSystemService(Context.VIBRATOR_SERVICE);
            if (v == null) return;
            if (Build.VERSION.SDK_INT >= 26) {
                v.vibrate(VibrationEffect.createOneShot(
                    "heavy".equals(type) ? 80 : "medium".equals(type) ? 40 : 20,
                    VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate("heavy".equals(type) ? 80 : 40);
            }
        } catch (Exception ignored) {}
    }

    @Override public void launch(String pkg, String cls) {
        try {
            Intent i = mAct.getPackageManager().getLaunchIntentForPackage(pkg);
            if (i != null) mAct.startActivity(i);
        } catch (Exception ignored) {}
    }

    @Override public void openApp(String pkg) { launch(pkg, ""); }

    @Override public void openSettings(String s) {
        mAct.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override public boolean torch() { return false; }
    @Override public boolean lockScreen() { return false; }
    @Override public String lockState()   { return "off"; }
    @Override public boolean mediaAct(String a, String b2) { return false; }
    @Override public boolean openAssistant(String s, boolean b2) { return false; }
    @Override public void webSearch(String q) {
        try {
            mAct.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=" + Uri.encode(q)))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception ignored) {}
    }

    // Health — return empty; real Jarvis health data can be wired here
    @Override public String healthToday(boolean b2) { return "{}"; }
}
