package com.jarvis.assist.aosui.settings;

import android.content.Context;
import android.view.View;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Tiles;

public class NativeSettings {
    // Fields accessed by ScrLayout
    public Kit k;
    public android.content.Context c;
    public float dn;

    public NativeSettings(Backend b) {
        this.c = b.ctx();
    }

    public View view() { return null; }
    public void open(String screen, String res) { }
    public void onBack() { }
    public void onDestroy() { }
    public void onEvent(String event) { }
    public void onPause() { }
    public void onResume() { }
    public void trim(int i, int j) { }

    // Methods used by ScrLayout
    public static String t(String key, Object... args) { return key; }
    public SettingsModel.Cfg cfg() { return new SettingsModel.Cfg(); }
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
