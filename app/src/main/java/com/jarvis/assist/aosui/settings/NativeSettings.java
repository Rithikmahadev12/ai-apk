package com.jarvis.assist.aosui.settings;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Tiles;
import org.json.JSONArray;
import org.json.JSONObject;

public class NativeSettings {
    // Fields accessed directly by ScrLayout
    public Kit k;
    public android.content.Context c;
    public float dn;
    public Backend be;
    public FrameLayout root;
    public Kit.Theme th;

    // Static fields
    public static final Handler MAIN = new Handler(Looper.getMainLooper());

    public NativeSettings(Backend b) {
        this.be = b;
        this.c = b.ctx();
    }

    // Static utility methods
    public static String t(String key, Object... args) { return key; }
    public static JSONObject J(String str) {
        try { return str != null ? new JSONObject(str) : new JSONObject(); }
        catch (Exception e) { return new JSONObject(); }
    }
    public static JSONArray JA(String str) {
        try { return str != null ? new JSONArray(str) : new JSONArray(); }
        catch (Exception e) { return new JSONArray(); }
    }

    // Instance methods used by ScrLayout
    public View view() { return null; }
    public void open(String screen, String res) { }
    public void onBack() { }
    public void onDestroy() { }
    public void onEvent(String event) { }
    public void onPause() { }
    public void onResume() { }
    public void trim(int i, int j) { }
    public SettingsModel.Cfg cfg() { return new SettingsModel.Cfg(); }
    public void saveCfg(SettingsModel.Cfg cfg) { }
    public void paint() { }
    public void add(View view) { }
    public void add(Tiles tiles) { }
    public View hd(String title, String sub) { return null; }
    public void sec(String title) { }
    public int px(float dp) { return (int)(dp * dn); }
    public void fine(String text) { }
    public View list() { return null; }

    // Lambda dispatch used by ScrLayout
    public void m356lambda$navRow$7$comaugmaososettingsNativeSettings(String screen) { }
}
