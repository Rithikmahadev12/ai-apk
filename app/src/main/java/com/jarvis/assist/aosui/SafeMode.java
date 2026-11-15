package com.jarvis.assist.aosui;
import android.app.Activity;
import android.view.View;

public final class SafeMode {
    public static View view(Activity activity) { return null; }
    public static void set(Activity activity, boolean safe) { }
    public static boolean isSafe(Activity activity) { return false; }
    public static void drawn(Activity activity) { }
}
