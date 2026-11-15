package com.jarvis.assist.aosui;

import android.content.Context;
import android.net.Uri;

public class HealthSources {
    public HealthSources() { }
    public static java.lang.String today(Context ctx, boolean z) { return null; }
    public static java.lang.String week(Context ctx, boolean z) { return null; }
    public static java.lang.String importApple(Context ctx, Uri uri, Progress p) { return null; }

    public interface Progress {
        void on(int i);
    }
}
