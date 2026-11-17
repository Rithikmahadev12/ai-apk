package com.jarvis.assist.aosui.settings;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.ai.Features;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Tiles;
import org.json.JSONArray;
import org.json.JSONObject;

public class NativeSettings {

    // ── Core fields accessed by all Scr* classes ──────────────────────
    public Kit k;
    public Context c;
    public float dn;
    public Backend be;
    public FrameLayout root;
    public Theme th;

    // ── F is properly typed as Features (ScrAssist calls F.on/set/etc) ─
    public Features F = null;

    // ── ScrData health-gateway state ──────────────────────────────────
    public String hgMsg = "";
    public String hgTest = "";
    public String hgSrc = "";
    public boolean hgBusy = false;

    // ── ScrAssist state flags ─────────────────────────────────────────
    public boolean perfTesting = false;
    public boolean drafts = false;

    // ── Static handler ─────────────────────────────────────────────────
    public static final Handler MAIN = new Handler(Looper.getMainLooper());

    public NativeSettings(Backend b) {
        this.be = b;
        this.c = b.ctx();
    }

    // ── Static utility ─────────────────────────────────────────────────
    public static String t(String key, Object... args) { return key; }
    public static String up(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
    public static JSONObject J(String str) {
        try { return str != null ? new JSONObject(str) : new JSONObject(); }
        catch (Exception e) { return new JSONObject(); }
    }
    public static JSONArray JA(String str) {
        try { return str != null ? new JSONArray(str) : new JSONArray(); }
        catch (Exception e) { return new JSONArray(); }
    }

    // ── Lifecycle / nav ────────────────────────────────────────────────
    public View view() { return null; }
    public void open(String screen, String res) { }
    public void onBack() { }
    public void onDestroy() { }
    public void onEvent(String event) { }
    public void onPause() { }
    public void onResume() { }
    public void trim(int i, int j) { }

    // ── Settings model helpers ─────────────────────────────────────────
    public SettingsModel.Cfg cfg() { return new SettingsModel.Cfg(); }
    public void saveCfg(SettingsModel.Cfg cfg) { }
    public void setBoth(String key, String value) { }
    public void paint() { }
    public void toast(String msg) { }
    public void guide(String str) { }

    // ── View building helpers ──────────────────────────────────────────
    public void add(View view) { }
    public void add(Tiles tiles) { }
    public View hd(String title, String sub) { return null; }
    public void sec(String title) { }
    public int px(float dp) { return (int)(dp * dn); }
    public void fine(String text) { }
    public LinearLayout list() { return new LinearLayout(c); }

    // ── AI / PA methods used by ScrAssist ─────────────────────────────

    /** Current AI configuration JSON (claude / gemini / openai sub-objects) */
    public JSONObject ai() { return new JSONObject(); }

    /** One-line string describing the current Personal Assistant mode */
    public String paLine() { return ""; }

    /** Is Personal Assistant mode active? */
    public boolean paMode() { return false; }

    /** Personal Assistant info (label, pkg, etc.) */
    public JSONObject paInfo() { return new JSONObject(); }

    /**
     * keep — binds an EditText to a prefs key so its content survives redraws.
     * Returns the same EditText for chaining.
     */
    public EditText keep(EditText editText, String prefsKey) { return editText; }

    /**
     * pref — reads a prefs string with a default.
     */
    public String pref(String key, String defVal) {
        try {
            String v = be != null ? be.get(key) : null;
            return (v != null && !v.isEmpty()) ? v : defVal;
        } catch (Exception e) { return defVal; }
    }

    /**
     * conn — creates a small info-row view (label + value chip).
     * @param label   left label text
     * @param value   right value text
     * @param enabled dimmed when false
     */
    public View conn(String label, String value, boolean enabled) {
        return new View(c);
    }

    // ── Lambda dispatch used by Scr* classes ───────────────────────────
    public void m356lambda$navRow$7$comaugmaososettingsNativeSettings(String screen) { }

    // voiceInfo and ttsVoices — raw JSON strings cached by ScrAssist
    public String voiceInfo = null;
    public String ttsVoices = null;

    // lambda for screen navigation — called by ScrAssist
    public void m354lambda$fixRow$10$comaugmaososettingsNativeSettings(String screen) {
        open(screen, null);
    }

    // setPref overload accepting Boolean (ScrAssist calls setPref("paused", bool))
    public void setPref(String key, Boolean value) {
        if (F != null) F.setPref(key, value != null ? value.toString() : "false");
    }
}
