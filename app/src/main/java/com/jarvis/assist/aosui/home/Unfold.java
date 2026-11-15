package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import com.jarvis.assist.aosui.core.UnfoldModel;

/* loaded from: /tmp/augmaos/classes.dex */
final class Unfold {
    private static Host host;
    private static boolean started;
    static final UnfoldModel M = new UnfoldModel();
    private static int sw = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Host {
        void unfold(String str);
    }

    Unfold() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void attach(Context context, Host host2) {
        host = host2;
        if (started) {
            return;
        }
        started = true;
        final DisplayManager displayManager = (DisplayManager) context.getApplicationContext().getSystemService(DisplayManager.class);
        if (displayManager == null) {
            return;
        }
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.home.Unfold$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Unfold.lambda$attach$0(displayManager);
            }
        };
        displayManager.registerDisplayListener(new DisplayManager.DisplayListener() { // from class: com.jarvis.assist.aosui.home.Unfold.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 250L);
            }
        }, handler);
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$attach$0(DisplayManager displayManager) {
        boolean z = false;
        Display display = displayManager.getDisplay(0);
        boolean z2 = false;
        for (Display display2 : displayManager.getDisplays()) {
            if (display2.getDisplayId() != 0 && (display2.getFlags() & 4) == 0 && display2.getState() == 2) {
                z2 = true;
            }
        }
        if (display != null && display.getState() == 2) {
            z = true;
        }
        if (!M.displays(z, z2) || host == null) {
            return;
        }
        host.unfold("flip");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void detach(Host host2) {
        if (host == host2) {
            host = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void config(int i) {
        int i2 = sw;
        sw = i;
        if (!UnfoldModel.sizeJump(i2, i) || host == null) {
            return;
        }
        host.unfold("book");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void simulate(String str) {
        if ("book".equals(str)) {
            int i = sw > 0 ? sw : 360;
            sw = i;
            config(Math.round(i * 1.6f));
            sw = i;
            return;
        }
        M.displays(false, true);
        if (!M.displays(true, false) || host == null) {
            return;
        }
        host.unfold("flip");
    }
}
