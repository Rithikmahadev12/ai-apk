package com.jarvis.assist.aosui.core;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.Memory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.IntConsumer;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Feedback {
    static Context app;
    public static volatile boolean debug;
    static long lastSound;
    static boolean loading;
    static SoundPool pool;
    static FxModel.Cfg cfg = new FxModel.Cfg();
    static String cfgSrc = "\u0000";
    static String poolStyle = "aos";
    static final int[] ids = new int[12];
    static final boolean[] ready = new boolean[12];
    static final Memory.Trim trim = new Memory.Trim() { // from class: com.jarvis.assist.aosui.core.Feedback$$ExternalSyntheticLambda3
        @Override // com.jarvis.assist.aosui.core.Memory.Trim
        public final void trim(int i, int i2) {
            Feedback.lambda$static$0(i, i2);
        }
    };
    static int lastSnd = -1;

    private Feedback() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$0(int i, int i2) {
        if (MemPolicy.has(i2, 8)) {
            release();
        }
    }

    public static synchronized void init(Context context) {
        synchronized (Feedback.class) {
            if (app == null) {
                app = context.getApplicationContext();
                debug = (app.getApplicationInfo().flags & 2) != 0;
                Store.init(app);
                Memory.on(trim);
            }
        }
    }

    static FxModel.Cfg cfg() {
        String str = Store.get(FxModel.KEY, "");
        if (!str.equals(cfgSrc)) {
            cfg = FxModel.cfg(str);
            cfgSrc = str;
        }
        return cfg;
    }

    public static FxModel.Phone phone() {
        FxModel.Phone phone = new FxModel.Phone();
        if (app == null) {
            return phone;
        }
        boolean z = false;
        try {
            phone.touchSounds = Settings.System.getInt(app.getContentResolver(), "sound_effects_enabled", 1) != 0;
        } catch (Exception e) {
        }
        try {
            phone.hapticSetting = Settings.System.getInt(app.getContentResolver(), "haptic_feedback_enabled", 1) != 0;
        } catch (Exception e2) {
        }
        try {
            AudioManager audioManager = (AudioManager) app.getSystemService(AudioManager.class);
            if (audioManager != null) {
                phone.ringer = audioManager.getRingerMode();
            }
        } catch (Exception e3) {
        }
        try {
            NotificationManager notificationManager = (NotificationManager) app.getSystemService(NotificationManager.class);
            int currentInterruptionFilter = notificationManager == null ? 1 : notificationManager.getCurrentInterruptionFilter();
            if (currentInterruptionFilter != 1 && currentInterruptionFilter != 0) {
                z = true;
            }
            phone.dnd = z;
        } catch (Exception e4) {
        }
        phone.tier = Perf.tier();
        return phone;
    }

    public static void fx(View view, int i) {
        if (app == null) {
            if (view == null) {
                return;
            } else {
                init(view.getContext());
            }
        }
        try {
            FxModel.Cfg cfg2 = cfg();
            FxModel.Phone phone = phone();
            int[] decide = FxModel.decide(i, cfg2, phone);
            if (debug) {
                int i2 = decide[0];
                String str = decide[1] < 0 ? "-" : FxModel.SND_NAMES[decide[1]];
                Log.d("aOSFx", "ev=" + i + " haptic=" + i2 + " sound=" + str + " str=" + cfg2.strength + " tier=" + phone.tier + " dnd=" + phone.dnd + " ringer=" + phone.ringer + " touchSnd=" + phone.touchSounds);
            }
            if (decide[0] != 0) {
                haptic(view, decide[0], FxModel.hapticScale(cfg2), cfg2.strength, false, !phone.hapticSetting);
            }
            if (decide[1] != -1) {
                sound(decide[1], FxModel.gain(cfg2));
            }
        } catch (Exception e) {
            Log.w("aOSFx", "fx " + i, e);
        }
    }

    public static void legacy(View view, String str) {
        fx(view, "heavy".equals(str) ? 3 : "tick".equals(str) ? 2 : "double".equals(str) ? 4 : 0);
    }

    public static int testHaptic(final View view) {
        if (app == null) {
            init(view.getContext());
        }
        final FxModel.Cfg cfg2 = cfg();
        final FxModel.Phone phone = phone();
        final int testHapticPath = FxModel.testHapticPath(cfg2, phone);
        seq(view, new IntConsumer() { // from class: com.jarvis.assist.aosui.core.Feedback$$ExternalSyntheticLambda4
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                Feedback.lambda$testHaptic$1(testHapticPath, cfg2, phone, view, i);
            }
        });
        return testHapticPath;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$testHaptic$1(int i, FxModel.Cfg cfg2, FxModel.Phone phone, View view, int i2) {
        int i3 = FxModel.map(FxModel.TEST_SEQ[i2])[0];
        if (debug) {
            Log.d("aOSFx", "test ev=" + FxModel.TEST_SEQ[i2] + " haptic=" + (i == 0 ? 0 : i3) + " path=" + (i == 1 ? "view" : i == 2 ? "vibrator" : "none") + " str=" + cfg2.strength + " sysHaptic=" + phone.hapticSetting);
        }
        if (i != 0) {
            haptic(view, i3, FxModel.hapticScale(cfg2), cfg2.strength, i == 2, !phone.hapticSetting);
        }
    }

    public static String testSound(final View view, final boolean z) {
        if (app == null) {
            init(view.getContext());
        }
        final FxModel.Cfg cfg2 = cfg();
        final FxModel.Phone phone = phone();
        load();
        final float gain = FxModel.gain(cfg2);
        final boolean z2 = z || FxModel.soundOn(cfg2, phone);
        view.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.core.Feedback$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                Feedback.seq(view, new IntConsumer() { // from class: com.jarvis.assist.aosui.core.Feedback$$ExternalSyntheticLambda6
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i) {
                        Feedback.lambda$testSound$2(r1, r2, r3, r4, r5, i);
                    }
                });
            }
        }, pool == null ? 250L : 0L);
        return FxModel.why(cfg2, phone);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$testSound$2(boolean z, boolean z2, FxModel.Cfg cfg2, FxModel.Phone phone, float f, int i) {
        int i2 = FxModel.map(FxModel.TEST_SEQ[i])[1];
        if (debug) {
            Log.d("aOSFx", (z ? "preview" : "test") + " ev=" + FxModel.TEST_SEQ[i] + " haptic=0 sound=" + (z2 ? FxModel.SND_NAMES[i2] : "-") + " vol=" + cfg2.volume + " why=" + FxModel.why(cfg2, phone));
        }
        if (z2) {
            sound(i2, f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void seq(View view, final IntConsumer intConsumer) {
        for (final int i = 0; i < FxModel.TEST_SEQ.length; i++) {
            view.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.core.Feedback$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    intConsumer.accept(i);
                }
            }, i * 150);
        }
    }

    static int constant(int i) {
        int i2 = Build.VERSION.SDK_INT;
        switch (i) {
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
            case 10:
                return 0;
            case 5:
                return i2 >= 30 ? 16 : 1;
            case 6:
                return i2 >= 30 ? 17 : 0;
            case 7:
                return i2 >= 34 ? 26 : 4;
            case 8:
                return i2 >= 34 ? 12 : 6;
            case 9:
                return i2 >= 34 ? 13 : 4;
            default:
                return 1;
        }
    }

    static void haptic(View view, int i, float f, String str, boolean z, boolean z2) {
        if (((z || !"normal".equals(str) || view == null || i == 3 || i == 7) && vibrate(i, f, z2)) || view == null) {
            return;
        }
        view.performHapticFeedback(constant(i));
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0050 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00ee  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x010f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean vibrate(int r12, float r13, boolean r14) {
        /*
            Method dump skipped, instructions count: 295
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.core.Feedback.vibrate(int, float, boolean):boolean");
    }

    static void sound(int i, float f) {
        if (pool != null && !poolStyle.equals(cfg().style)) {
            release();
        }
        if (pool == null) {
            load();
            return;
        }
        if (ready[i]) {
            long uptimeMillis = SystemClock.uptimeMillis();
            if (i != lastSnd || uptimeMillis - lastSound >= 35) {
                lastSnd = i;
                lastSound = uptimeMillis;
                float max = Math.max(0.0f, Math.min(1.0f, f));
                pool.play(ids[i], max, max, 1, 0, 1.0f);
            }
        }
    }

    static synchronized void load() {
        synchronized (Feedback.class) {
            if (pool != null && !poolStyle.equals(cfg().style)) {
                release();
            }
            if (pool == null && !loading && app != null) {
                loading = true;
                final String str = cfg().style;
                Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.core.Feedback$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Feedback.lambda$load$6(str);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$load$6(String str) {
        try {
            File file = new File(app.getCacheDir(), "fx");
            if (!file.isDirectory() && !file.mkdirs()) {
                throw new IOException("no cache dir");
            }
            String[] strArr = new String[12];
            for (int i = 0; i < 12; i++) {
                File file2 = new File(file, FxModel.SND_NAMES[i] + ("aos".equals(str) ? "" : "-" + str) + "-v2.wav");
                strArr[i] = file2.getPath();
                if (!file2.isFile() || file2.length() < 44) {
                    byte[] wav = FxModel.wav(FxModel.pcm(str, i));
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    try {
                        fileOutputStream.write(wav);
                        fileOutputStream.close();
                    } finally {
                    }
                }
            }
            SoundPool build = new SoundPool.Builder().setMaxStreams(3).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setContentType(4).build()).build();
            final int[] iArr = new int[12];
            Arrays.fill(ready, false);
            build.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: com.jarvis.assist.aosui.core.Feedback$$ExternalSyntheticLambda0
                @Override // android.media.SoundPool.OnLoadCompleteListener
                public final void onLoadComplete(SoundPool soundPool, int i2, int i3) {
                    Feedback.lambda$load$5(iArr, soundPool, i2, i3);
                }
            });
            for (int i2 = 0; i2 < 12; i2++) {
                iArr[i2] = build.load(strArr[i2], 1);
            }
            synchronized (Feedback.class) {
                System.arraycopy(iArr, 0, ids, 0, 12);
                pool = build;
                poolStyle = str;
                loading = false;
            }
        } catch (Exception e) {
            Log.w("aOSFx", "sounds unavailable", e);
            synchronized (Feedback.class) {
                loading = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$load$5(int[] iArr, SoundPool soundPool, int i, int i2) {
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (iArr[i3] == i) {
                ready[i3] = i2 == 0;
                if (i2 != 0) {
                    Log.w("aOSFx", "load " + FxModel.SND_NAMES[i3] + " status " + i2);
                } else if (debug) {
                    Log.d("aOSFx", "loaded " + FxModel.SND_NAMES[i3]);
                }
            }
        }
    }

    public static synchronized void release() {
        synchronized (Feedback.class) {
            if (pool != null) {
                pool.release();
                pool = null;
            }
            Arrays.fill(ready, false);
        }
    }

    public static void warm() {
        if (app == null || !FxModel.soundOn(cfg(), phone())) {
            return;
        }
        load();
    }
}
