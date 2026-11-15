package com.jarvis.assist.aosui.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class CrashModel {
    public static final int FRAMES = 8;
    public static final String PKG = "com.jarvis.assist.aosui";
    public static final long WINDOW = 60000;

    private CrashModel() {
    }

    public static boolean safeMode(long j, long j2, boolean z, long j3) {
        if (j <= 0 || j <= j3) {
            return false;
        }
        if (z) {
            return true;
        }
        return j2 > 0 && j2 > j3 && j >= j2 && j - j2 <= 60000;
    }

    public static String trace(Throwable th) {
        if (th == null) {
            return "";
        }
        Throwable th2 = th;
        for (int i = 0; i < 10 && th2.getCause() != null && th2.getCause() != th2; i++) {
            th2 = th2.getCause();
        }
        StringBuilder sb = new StringBuilder(th.getClass().getName());
        if (th2 != th) {
            sb.append(" (cause ").append(th2.getClass().getName()).append(')');
        }
        int frames = frames(sb, th2.getStackTrace(), 0);
        if (th2 != th) {
            frames(sb, th.getStackTrace(), frames);
        }
        return sb.toString();
    }

    static int frames(StringBuilder sb, StackTraceElement[] stackTraceElementArr, int i) {
        if (stackTraceElementArr == null) {
            return i;
        }
        for (StackTraceElement stackTraceElement : stackTraceElementArr) {
            if (i >= 8) {
                break;
            }
            String className = stackTraceElement.getClassName();
            if (className != null && className.startsWith("com.jarvis.assist.aosui.")) {
                sb.append("\n  at ").append(className).append('.').append(stackTraceElement.getMethodName()).append('(').append(stackTraceElement.getFileName() == null ? "?" : stackTraceElement.getFileName()).append(':').append(stackTraceElement.getLineNumber()).append(')');
                i++;
            }
        }
        return i;
    }

    public static String report(String str, String str2, String str3, long j, String str4) {
        StringBuilder append = new StringBuilder(str).append("\nDevice: ").append(str2).append("\nAndroid: ").append(str3);
        if (j > 0 && str4 != null && !str4.isEmpty()) {
            append.append("\nLast crash: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT).format(new Date(j))).append('\n').append(str4);
        }
        return append.toString();
    }

    public static String version(String str, long j) {
        return "aOS " + str + " (" + j + ")";
    }
}
