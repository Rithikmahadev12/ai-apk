package com.jarvis.assist.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Store — persistent key/value storage for Jarvis config.
 * Mirrors AugmaOS Store pattern from DEX.
 */
public class Store {

    private static final String PREFS = "jarvis_store_v1";
    private static SharedPreferences sPrefs;

    // Keys
    public static final String KEY_SERVER_URL  = "server_url";
    public static final String KEY_USER_NAME   = "user_name";
    public static final String KEY_AI_URL      = "ai_base_url";
    public static final String KEY_AI_KEY      = "ai_api_key";
    public static final String KEY_AI_MODEL    = "ai_model";
    public static final String KEY_VAULT_IDS   = "_vault_ids";
    public static final String KEY_ONBOARDED   = "onboarded";
    public static final String KEY_THEME       = "aos.theme";
    public static final String KEY_SAVE_CHATS  = "save_chats";

    public static void init(Context ctx) {
        sPrefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static String get(String key) {
        return sPrefs.getString(key, null);
    }

    public static String get(String key, String def) {
        return sPrefs.getString(key, def);
    }

    public static boolean getBool(String key, boolean def) {
        return sPrefs.getBoolean(key, def);
    }

    public static void set(String key, String val) {
        sPrefs.edit().putString(key, val).apply();
    }

    public static void set(String key, boolean val) {
        sPrefs.edit().putBoolean(key, val).apply();
    }

    public static void remove(String key) {
        sPrefs.edit().remove(key).apply();
    }

    public static boolean has(String key) {
        return sPrefs.contains(key) && sPrefs.getString(key, null) != null
            && !sPrefs.getString(key, "").isEmpty();
    }

    public static void clearSession() {
        sPrefs.edit()
            .remove(KEY_SERVER_URL)
            .remove(KEY_USER_NAME)
            .apply();
    }

    public static void clearAll() {
        sPrefs.edit().clear().apply();
    }

    // ── Convenience getters ───────────────────────────────────────────────────

    public static String serverUrl()  { return get(KEY_SERVER_URL, ""); }
    public static String userName()   { return get(KEY_USER_NAME, ""); }
    public static String aiUrl()      { return get(KEY_AI_URL, ""); }
    public static String aiKey()      { return get(KEY_AI_KEY, ""); }
    public static String aiModel()    { return get(KEY_AI_MODEL, "gpt-4o-mini"); }
    public static boolean hasServer() { return has(KEY_SERVER_URL); }
    public static boolean hasUser()   { return has(KEY_USER_NAME); }
    public static boolean hasAiKey()  { return has(KEY_AI_KEY); }
    public static boolean onboarded() { return getBool(KEY_ONBOARDED, false); }
}
