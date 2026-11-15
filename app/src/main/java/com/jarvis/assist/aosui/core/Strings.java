package com.jarvis.assist.aosui.core;

import android.content.Context;
import java.util.HashMap;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Strings {
    static Context app;
    static final HashMap<String, String> cache = new HashMap<>();

    private Strings() {
    }

    public static void init(Context context) {
        app = context.getApplicationContext();
    }

    public static String name(String str) {
        return "i18n_" + str.replaceAll("[^A-Za-z0-9_]", "_");
    }

    public static synchronized String t(String str) {
        synchronized (Strings.class) {
            String str2 = cache.get(str);
            if (str2 != null) {
                return str2;
            }
            int identifier = app == null ? 0 : app.getResources().getIdentifier(name(str), "string", app.getPackageName());
            String string = identifier == 0 ? str : app.getString(identifier);
            cache.put(str, string);
            return string;
        }
    }

    public static String t(String str, Object... objArr) {
        String t = t(str);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i2 >= objArr.length) {
                return t;
            }
            t = t.replace("{" + String.valueOf(objArr[i]) + "}", String.valueOf(objArr[i2]));
            i += 2;
        }
    }
}
