package com.jarvis.assist.aosui.ai;

import org.json.JSONObject;

public class KeyCheck {
    public KeyCheck() { }

    public static void check(Object p0, Object p1) { }
    public static String status() { return null; }

    /**
     * settle — called by ScrAssist after a key-check response arrives.
     * @param drafts  whether NativeSettings.drafts is set (pending save)
     * @param storeKey  the prefs key for this provider's draft ("key.claude" etc.)
     * @param resp    the parsed JSON response from the check
     * @return true if the check is complete (no further action needed),
     *         false if the caller should show an error/note
     */
    public static boolean settle(boolean drafts, String storeKey, JSONObject resp) {
        return true;
    }

    public static class Resp {
        public Resp() { }
        public boolean ok;
        public String msg;
    }
}
