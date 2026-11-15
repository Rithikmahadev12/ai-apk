package com.jarvis.assist.aosui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Process;
import android.widget.Toast;
import com.jarvis.assist.aosui.core.CrashModel;
import java.lang.Thread;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Crash {
    static final String FILE = "aos.crash";

    private Crash() {
    }

    static SharedPreferences sp(Context context) {
        return context.getApplicationContext().getSharedPreferences(FILE, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void install(Context context) {
        final Context applicationContext = context.getApplicationContext();
        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: com.jarvis.assist.aosui.Crash$$ExternalSyntheticLambda0
            @Override // java.lang.Thread.UncaughtExceptionHandler
            public final void uncaughtException(Thread thread, Throwable th) {
                Crash.lambda$install$0(applicationContext, defaultUncaughtExceptionHandler, thread, th);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$install$0(Context context, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, Thread thread, Throwable th) {
        try {
            record(context, th);
        } catch (Throwable th2) {
        }
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        } else {
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
    }

    static void record(Context context, Throwable th) {
        SharedPreferences sp = sp(context);
        sp.edit().putLong("prev", sp.getLong("last", 0L)).putLong("last", System.currentTimeMillis()).putBoolean("before", sp.getBoolean("pending", false)).putString("trace", CrashModel.trace(th)).putBoolean("pending", false).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void starting(Context context) {
        sp(context).edit().putBoolean("pending", true).apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void drawn(Context context) {
        sp(context).edit().putBoolean("pending", false).apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean safe(Context context) {
        try {
            SharedPreferences sp = sp(context);
            return CrashModel.safeMode(sp.getLong("last", 0L), sp.getLong("prev", 0L), sp.getBoolean("before", false), sp.getLong("ack", 0L));
        } catch (Throwable th) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void ack(Context context) {
        SharedPreferences sp = sp(context);
        sp.edit().putLong("ack", sp.getLong("last", 0L)).putBoolean("before", false).commit();
    }

    public static String version(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return CrashModel.version(packageInfo.versionName, Build.VERSION.SDK_INT >= 28 ? packageInfo.getLongVersionCode() : packageInfo.versionCode);
        } catch (Throwable th) {
            return "aOS";
        }
    }

    public static String report(Context context, boolean z) {
        SharedPreferences sp = sp(context);
        return CrashModel.report(version(context), Build.MANUFACTURER + " " + Build.MODEL, Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")", z ? sp.getLong("last", 0L) : 0L, z ? sp.getString("trace", "") : "");
    }

    public static void share(Context context, String str, boolean z) {
        try {
            context.startActivity(Intent.createChooser(new Intent("android.intent.action.SEND").setType("text/plain").putExtra("android.intent.extra.SUBJECT", str).putExtra("android.intent.extra.TEXT", report(context, z)), str).addFlags(268435456));
        } catch (Exception e) {
            Toast.makeText(context, "No app to share with", 0).show();
        }
    }
}
