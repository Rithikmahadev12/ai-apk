package com.jarvis.assist.aosui.core;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import com.jarvis.assist.aosui.core.SizedLru;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Memory {
    static final Handler MAIN;
    static final Runnable RECLAIM;
    static final Runnable RECLAIM2;
    public static int lastLevel;
    static int memClass;
    static final ArrayList<Trim> subs = new ArrayList<>();
    public static final ThreadPoolExecutor IO = new ThreadPoolExecutor(4, 4, 20, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() { // from class: com.jarvis.assist.aosui.core.Memory$$ExternalSyntheticLambda4
        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            return Memory.lambda$static$1(runnable);
        }
    });

    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Trim {
        void trim(int i, int i2);
    }

    private Memory() {
    }

    static {
        IO.allowCoreThreadTimeOut(true);
        MAIN = new Handler(Looper.getMainLooper());
        RECLAIM = new Runnable() { // from class: com.jarvis.assist.aosui.core.Memory$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.core.Memory$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Memory.lambda$static$2();
                    }
                });
            }
        };
        RECLAIM2 = new Runnable() { // from class: com.jarvis.assist.aosui.core.Memory$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.core.Memory$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Memory.lambda$static$4();
                    }
                });
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$0(Runnable runnable) {
        Process.setThreadPriority(10);
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Thread lambda$static$1(final Runnable runnable) {
        Thread thread = new Thread(new Runnable() { // from class: com.jarvis.assist.aosui.core.Memory$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                Memory.lambda$static$0(runnable);
            }
        }, "aos-io");
        thread.setDaemon(true);
        return thread;
    }

    public static void io(Runnable runnable) {
        IO.execute(runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$2() {
        Runtime.getRuntime().gc();
        System.runFinalization();
        Runtime.getRuntime().gc();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$4() {
        Runtime.getRuntime().gc();
        System.runFinalization();
        Runtime.getRuntime().gc();
    }

    public static void reclaimSoon() {
        MAIN.removeCallbacks(RECLAIM);
        MAIN.removeCallbacks(RECLAIM2);
        MAIN.postDelayed(RECLAIM, 600L);
        MAIN.postDelayed(RECLAIM2, 5000L);
    }

    public static synchronized void init(Context context) {
        synchronized (Memory.class) {
            if (memClass <= 0) {
                try {
                    ActivityManager activityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
                    memClass = activityManager == null ? 128 : activityManager.getMemoryClass();
                } catch (Exception e) {
                    memClass = MemPolicy.TRIM_CHAT;
                }
            }
        }
    }

    public static int memClass() {
        return memClass > 0 ? memClass : MemPolicy.TRIM_CHAT;
    }

    public static SizedLru<String, Bitmap> bitmaps(int i) {
        return new SizedLru<>(MemPolicy.budget(memClass(), i), new SizedLru.Sizer() { // from class: com.jarvis.assist.aosui.core.Memory$$ExternalSyntheticLambda0
            @Override // com.jarvis.assist.aosui.core.SizedLru.Sizer
            public final int size(Object obj) {
                return Memory.lambda$bitmaps$6((Bitmap) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$bitmaps$6(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        return bitmap.getAllocationByteCount();
    }

    public static void on(Trim trim) {
        synchronized (subs) {
            if (!subs.contains(trim)) {
                subs.add(trim);
            }
        }
    }

    public static void off(Trim trim) {
        synchronized (subs) {
            subs.remove(trim);
        }
    }

    public static void trim(int i) {
        ArrayList arrayList;
        lastLevel = i;
        int actions = MemPolicy.actions(i);
        if (actions == 0) {
            return;
        }
        synchronized (subs) {
            arrayList = new ArrayList(subs);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                ((Trim) it.next()).trim(i, actions);
            } catch (RuntimeException e) {
                Log.w("aOS", "trim", e);
            }
        }
        Log.i("aOS.mem", "trim " + i + " → actions " + Integer.toBinaryString(actions) + " (" + arrayList.size() + " listeners)");
    }
}
