package com.jarvis.assist.aosui.settings;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.BootSeq;
import com.jarvis.assist.aosui.core.ChargeFx;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.HomeTheme;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.UnfoldModel;
import com.jarvis.assist.aosui.core.UnlockAnim;
import com.jarvis.assist.aosui.ui.Kit;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class ScrFx {
    private ScrFx() {
    }

    static String t(String str, Object... objArr) {
        return NativeSettings.t(str, objArr);
    }

    static FxModel.Cfg cfg() {
        return FxModel.cfg(Store.get(FxModel.KEY, ""));
    }

    static void save(NativeSettings nativeSettings, FxModel.Cfg cfg) {
        Store.set(FxModel.KEY, FxModel.save(cfg));
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String sum() {
        FxModel.Cfg cfg = cfg();
        return t("set.fx.sub", "snd", !cfg.sounds ? t("fx.off", new Object[0]) : cfg.follow ? t("fx.follow.short", new Object[0]) : t("fx.on", new Object[0]), "hap", FxModel.hapticScale(cfg) > 0.0f ? t("fx.str." + cfg.strength, new Object[0]) : t("fx.off", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void screen(final NativeSettings nativeSettings) {
        final Kit kit = nativeSettings.k;
        final FxModel.Cfg cfg = cfg();
        nativeSettings.add(nativeSettings.hd(t("fx.h", new Object[0]), t("fx.sub", new Object[0])));
        nativeSettings.sec(t("fx.snd", new Object[0]).toUpperCase(Locale.getDefault()));
        LinearLayout list = nativeSettings.list();
        kit.add(list, kit.toggleRow(t("fx.snd", new Object[0]), t("fx.snd.sub", new Object[0]), cfg.sounds, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrFx.lambda$screen$0(FxModel.Cfg.this, nativeSettings, (Boolean) obj);
            }
        }));
        kit.add(list, kit.toggleRow(t("fx.follow", new Object[0]), t("fx.follow.sub", new Object[0]), cfg.follow, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrFx.lambda$screen$1(FxModel.Cfg.this, nativeSettings, (Boolean) obj);
            }
        }));
        LinearLayout slider = kit.slider(t("fx.vol", new Object[0]), 0, 100, 5, cfg.volume, t("fx.vol", new Object[0]), new IntFunction() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda6
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return ScrFx.lambda$screen$2(i);
            }
        }, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrFx.lambda$screen$3(FxModel.Cfg.this, (Integer) obj);
            }
        });
        if (!cfg.sounds) {
            slider.setAlpha(0.5f);
        }
        kit.add(list, slider);
        nativeSettings.sec(t("fx.motion", new Object[0]));
        LinearLayout list2 = nativeSettings.list();
        boolean on = BootSeq.on(Store.get(BootSeq.KEY, ""));
        kit.add(list2, kit.toggleRow(t("fx.boot", new Object[0]), t("fx.boot.sub", new Object[0]), on, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrFx.lambda$screen$4(NativeSettings.this, (Boolean) obj);
            }
        }));
        if (HomeTheme.aoa(Store.get(Advanced.KEY, ""))) {
            kit.add(list2, kit.toggleRow(t("fx.unlock", new Object[0]), t("fx.unlock.sub", new Object[0]), UnlockAnim.on(Store.get(UnlockAnim.KEY, "")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrFx.lambda$screen$5(NativeSettings.this, (Boolean) obj);
                }
            }));
        }
        LinearLayout linearLayout = kit.toggleRow(t("fx.boot.unfold", new Object[0]), t("fx.boot.unfold.sub", new Object[0]), UnfoldModel.on(Store.get(UnfoldModel.KEY, "")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Store.set(UnfoldModel.KEY, r1.booleanValue() ? "" : "0");
            }
        });
        if (!on) {
            linearLayout.setAlpha(0.5f);
            linearLayout.setEnabled(false);
        }
        kit.add(list2, linearLayout);
        kit.add(list2, kit.toggleRow(t("fx.charge", new Object[0]), t("fx.charge.sub", new Object[0]), ChargeFx.on(Store.get(ChargeFx.KEY, "")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Store.set(ChargeFx.KEY, r1.booleanValue() ? "" : "0");
            }
        }));
        nativeSettings.sec(t("fx.style", new Object[0]));
        int length = FxModel.STYLES.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            strArr[i] = t("fx.style." + FxModel.STYLES[i], new Object[0]);
        }
        nativeSettings.add(kit.seg(strArr, FxModel.STYLES, cfg.style, t("fx.style", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrFx.lambda$screen$8(FxModel.Cfg.this, nativeSettings, (String) obj);
            }
        }));
        if (!Feedback.phone().touchSounds) {
            nativeSettings.fine(cfg.follow ? t("fx.sys.off.follow", new Object[0]) : t("fx.sys.off", new Object[0]));
        }
        TextView msg = kit.msg(t("fx.why." + FxModel.why(cfg, Feedback.phone()), new Object[0]));
        if ("ok".equals(FxModel.why(cfg, Feedback.phone()))) {
            msg.setTextColor(kit.GOOD);
        }
        nativeSettings.add(msg);
        nativeSettings.sec(t("fx.hap", new Object[0]).toUpperCase(Locale.getDefault()));
        kit.add(nativeSettings.list(), kit.toggleRow(t("fx.hap", new Object[0]), t("fx.hap.sub", new Object[0]), cfg.haptics, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrFx.lambda$screen$9(FxModel.Cfg.this, nativeSettings, (Boolean) obj);
            }
        }));
        nativeSettings.sec(t("fx.str", new Object[0]));
        int length2 = FxModel.STRENGTHS.length;
        String[] strArr2 = new String[length2];
        for (int i2 = 0; i2 < length2; i2++) {
            strArr2[i2] = t("fx.str." + FxModel.STRENGTHS[i2], new Object[0]);
        }
        nativeSettings.add(kit.seg(strArr2, FxModel.STRENGTHS, cfg.strength, t("fx.str", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrFx.lambda$screen$10(FxModel.Cfg.this, nativeSettings, (String) obj);
            }
        }));
        nativeSettings.sec(t("fx.test", new Object[0]));
        TextView btn = kit.btn(t("fx.test.snd", new Object[0]), false, true, null);
        TextView btn2 = kit.btn(t("fx.test.hap", new Object[0]), false, true, null);
        TextView btn3 = kit.btn(t("fx.preview", new Object[0]), true, true, null);
        ((Kit.FxText) btn).ev = -1;
        ((Kit.FxText) btn2).ev = -1;
        ((Kit.FxText) btn3).ev = -1;
        btn3.setContentDescription(t("fx.preview.a11y", new Object[0]));
        final TextView msg2 = kit.msg("");
        btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrFx.lambda$screen$11(msg2, kit, view);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrFx.lambda$screen$12(msg2, kit, view);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrFx$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrFx.lambda$screen$13(msg2, kit, view);
            }
        });
        nativeSettings.add(kit.hrow(btn, btn2));
        nativeSettings.add(kit.hrow(btn3));
        nativeSettings.add(msg2);
        nativeSettings.fine(t("fx.fine", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$0(FxModel.Cfg cfg, NativeSettings nativeSettings, Boolean bool) {
        cfg.sounds = bool.booleanValue();
        save(nativeSettings, cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$1(FxModel.Cfg cfg, NativeSettings nativeSettings, Boolean bool) {
        cfg.follow = bool.booleanValue();
        save(nativeSettings, cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String lambda$screen$2(int i) {
        return i + "%";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$3(FxModel.Cfg cfg, Integer num) {
        if (num.intValue() == cfg.volume) {
            return;
        }
        cfg.volume = num.intValue();
        Store.set(FxModel.KEY, FxModel.save(cfg));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$4(NativeSettings nativeSettings, Boolean bool) {
        Store.set(BootSeq.KEY, bool.booleanValue() ? "" : "0");
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$5(NativeSettings nativeSettings, Boolean bool) {
        Store.set(UnlockAnim.KEY, bool.booleanValue() ? "" : "0");
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$8(FxModel.Cfg cfg, NativeSettings nativeSettings, String str) {
        cfg.style = str;
        Store.set(FxModel.KEY, FxModel.save(cfg));
        Feedback.release();
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$9(FxModel.Cfg cfg, NativeSettings nativeSettings, Boolean bool) {
        cfg.haptics = bool.booleanValue();
        save(nativeSettings, cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$10(FxModel.Cfg cfg, NativeSettings nativeSettings, String str) {
        cfg.strength = str;
        if (!"off".equals(str)) {
            cfg.haptics = true;
        }
        save(nativeSettings, cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$11(TextView textView, Kit kit, View view) {
        String str;
        String testSound = Feedback.testSound(view, false);
        textView.setTextColor("ok".equals(testSound) ? kit.GOOD : kit.ALT);
        if ("ok".equals(testSound)) {
            str = t("fx.test.snd.ok", new Object[0]);
        } else {
            str = t("fx.why." + testSound, new Object[0]) + " · " + t("fx.test.snd.try", new Object[0]);
        }
        textView.setText(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$12(TextView textView, Kit kit, View view) {
        Feedback.testSound(view, true);
        textView.setTextColor(kit.ALT);
        textView.setText(t("fx.preview.a11y", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$screen$13(TextView textView, Kit kit, View view) {
        int testHaptic = Feedback.testHaptic(view);
        FxModel.Cfg cfg = cfg();
        textView.setTextColor(kit.ALT);
        textView.setText(testHaptic == 0 ? t("fx.test.hap.off", new Object[0]) : FxModel.testHapticHint(cfg, Feedback.phone()) ? t("fx.test.hap.sys", new Object[0]) : t("fx.test.hap.ok", new Object[0]));
    }
}
