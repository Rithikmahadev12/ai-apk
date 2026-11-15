package com.jarvis.assist.aosui.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.jarvis.assist.aosui.ai.Tools;
import com.jarvis.assist.aosui.core.Store;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntConsumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Store {
    public static final String FILE = "aos.store";
    public static final String MIGRATED = "_migrated";
    static SharedPreferences home;
    static boolean pending;
    static SharedPreferences sp;
    static final ArrayList<Runnable> subs = new ArrayList<>();
    static final Handler main = new Handler(Looper.getMainLooper());
    static final SharedPreferences.OnSharedPreferenceChangeListener L = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.jarvis.assist.aosui.core.Store$$ExternalSyntheticLambda1
        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            Store.lambda$static$0(sharedPreferences, str);
        }
    };

    private Store() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$0(SharedPreferences sharedPreferences, String str) {
        if (str == null || str.startsWith("aos.")) {
            fire();
        }
    }

    static void fire() {
        if (pending) {
            return;
        }
        pending = true;
        main.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.core.Store$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Store.lambda$fire$1();
            }
        }, 120L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$fire$1() {
        pending = false;
        Iterator it = new ArrayList(subs).iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
    }

    public static synchronized void init(Context context) {
        synchronized (Store.class) {
            if (sp != null) {
                return;
            }
            Context applicationContext = context.getApplicationContext();
            sp = applicationContext.getSharedPreferences(FILE, 0);
            home = applicationContext.getSharedPreferences("home", 0);
            sp.registerOnSharedPreferenceChangeListener(L);
        }
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

    public static String get(String str) {
        return sp.getString(str, null);
    }

    public static String get(String str, String str2) {
        String string = sp.getString(str, null);
        return string == null ? str2 : string;
    }

    public static void set(String str, String str2) {
        if (str2 == null) {
            remove(str);
            return;
        }
        sp.edit().putString(str, str2).apply();
        if ("aos.colors".equals(str) || "aos.theme".equals(str) || "aos.rm".equals(str)) {
            home.edit().putString(str, str2).apply();
        }
    }

    public static Map<String, String> all() {
        TreeMap treeMap = new TreeMap();
        for (Map.Entry<String, ?> entry : sp.getAll().entrySet()) {
            if (entry.getKey().startsWith("aos.") && (entry.getValue() instanceof String)) {
                treeMap.put(entry.getKey(), (String) entry.getValue());
            }
        }
        return treeMap;
    }

    public static void remove(String str) {
        sp.edit().remove(str).apply();
    }

    public static JSONObject obj(String str) {
        try {
            String str2 = get(str);
            JSONObject jSONObject = str2 == null ? null : new JSONObject(str2);
            return jSONObject == null ? new JSONObject() : jSONObject;
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static JSONArray arr(String str) {
        try {
            String str2 = get(str);
            return str2 == null ? new JSONArray() : new JSONArray(str2);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public static String home(String str) {
        return home.getString(str, "");
    }

    public static void setHome(String str, String str2) {
        home.edit().putString(str, str2).apply();
    }

    public static boolean migrated() {
        return sp.getBoolean(MIGRATED, false);
    }

    public static boolean preNative(Context context) {
        return new File(context.getApplicationInfo().dataDir, "app_webview").isDirectory();
    }

    public static void migrateOnce(Context context, final IntConsumer intConsumer) {
        if (migrated()) {
            return;
        }
        if (preNative(context)) {
            migrate(context, new IntConsumer() { // from class: com.jarvis.assist.aosui.core.Store$$ExternalSyntheticLambda4
                @Override // java.util.function.IntConsumer
                public final void accept(int i) {
                    Store.lambda$migrateOnce$2(intConsumer, i);
                }
            });
        } else {
            sp.edit().putBoolean(MIGRATED, true).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$migrateOnce$2(IntConsumer intConsumer, int i) {
        if (i < 0) {
            sp.edit().putBoolean(MIGRATED, true).apply();
        }
        intConsumer.accept(i);
    }

    public static int importJson(String str) {
        Map<String, String> parse = StoreCodec.parse(str);
        SharedPreferences.Editor edit = sp.edit();
        for (Map.Entry<String, String> entry : parse.entrySet()) {
            edit.putString(entry.getKey(), entry.getValue());
        }
        edit.putBoolean(MIGRATED, true).apply();
        return parse.size();
    }

    public static void migrate(Context context, final IntConsumer intConsumer) {
        if (migrated()) {
            intConsumer.accept(0);
            return;
        }
        final WebView[] webViewArr = {null};
        final boolean[] zArr = {false};
        final Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.core.Store$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                Store.lambda$migrate$3(webViewArr);
            }
        };
        try {
            WebView webView = new WebView(context);
            webViewArr[0] = webView;
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowContentAccess(false);
            webView.addJavascriptInterface(new AnonymousClass1(zArr, runnable, intConsumer), "Mig");
            webView.loadUrl("file:///android_asset/migrate.html");
            main.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.core.Store$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    Store.lambda$migrate$4(zArr, runnable, intConsumer);
                }
            }, Tools.DATA_WAIT_MS);
        } catch (Throwable th) {
            runnable.run();
            intConsumer.accept(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$migrate$3(WebView[] webViewArr) {
        WebView webView = webViewArr[0];
        webViewArr[0] = null;
        if (webView != null) {
            try {
                webView.removeJavascriptInterface("Mig");
                webView.destroy();
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.jarvis.assist.aosui.core.Store$1, reason: invalid class name */
    /* loaded from: /tmp/augmaos/classes.dex */
    public class AnonymousClass1 {
        final /* synthetic */ IntConsumer val$done;
        final /* synthetic */ Runnable val$end;
        final /* synthetic */ boolean[] val$fin;

        AnonymousClass1(boolean[] zArr, Runnable runnable, IntConsumer intConsumer) {
            this.val$fin = zArr;
            this.val$end = runnable;
            this.val$done = intConsumer;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$done$0(boolean[] zArr, String str, Runnable runnable, IntConsumer intConsumer) {
            int i;
            if (zArr[0]) {
                return;
            }
            zArr[0] = true;
            try {
                i = Store.importJson(str);
            } catch (Exception e) {
                i = -1;
            }
            runnable.run();
            intConsumer.accept(i);
        }

        @JavascriptInterface
        public void done(final String str) {
            Handler handler = Store.main;
            final boolean[] zArr = this.val$fin;
            final Runnable runnable = this.val$end;
            final IntConsumer intConsumer = this.val$done;
            handler.post(new Runnable() { // from class: com.jarvis.assist.aosui.core.Store$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Store.AnonymousClass1.lambda$done$0(zArr, str, runnable, intConsumer);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$migrate$4(boolean[] zArr, Runnable runnable, IntConsumer intConsumer) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        runnable.run();
        intConsumer.accept(-1);
    }
}
