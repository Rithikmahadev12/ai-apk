package com.jarvis.assist.aosui;

import android.content.Context;
import android.net.Uri;

public class HealthSources {
    public HealthSources() { }
    public static java.lang.String today(Context ctx, boolean z) { return null; }
    public static java.lang.String week(Context ctx, boolean z) { return null; }
    public static java.lang.String importApple(Context ctx, Uri uri, Progress p) { return null; }
    public static java.lang.String finish(Context ctx, Uri uri) { return null; }

    public interface Progress {
        void on(int i);
    }

    public static java.lang.String connect(android.content.Context ctx, String src, Progress p) { return null; }
    public static java.lang.String status(android.content.Context ctx) { return null; }
    public static void setPref(android.content.Context ctx, String pref) { }
    public static void wipe(android.content.Context ctx) { }
    public static void disconnect(android.content.Context ctx, String src) { }
    public static void today2(android.content.Context ctx, PhoneAI.Done done) { }
}
