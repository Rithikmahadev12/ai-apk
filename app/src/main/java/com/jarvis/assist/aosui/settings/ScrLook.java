package com.jarvis.assist.aosui.settings;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.PerfModel;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.ui.Kit;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class ScrLook {
    private ScrLook() {
    }

    static String t(String str, Object... objArr) {
        return NativeSettings.t(str, objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void look(final NativeSettings nativeSettings) {
        String[] strArr;
        String str;
        int i;
        Kit kit = nativeSettings.k;
        String str2 = Store.get("aos.theme", "dark");
        String str3 = Store.get("aos.colors");
        int[] ui = SettingsModel.ui(Store.get("aos.ui"));
        int i2 = 0;
        nativeSettings.add(nativeSettings.hd(t("ns.look.h", new Object[0]), t("ns.look.sub", new Object[0])));
        nativeSettings.sec(t("adv.theme.h", new Object[0]));
        String str4 = "";
        nativeSettings.add(kit.themePicker(Store.get(Advanced.KEY, ""), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLook.lambda$look$0(nativeSettings, (String) obj);
            }
        }));
        nativeSettings.sec(t("ns.theme", new Object[0]));
        String[] strArr2 = {t("ns.theme.dark", new Object[0]), t("ns.theme.light", new Object[0])};
        String[] strArr3 = {"dark", "light"};
        if ("light".equals(str2)) {
            strArr = strArr3;
            str = "light";
        } else {
            strArr = strArr3;
            str = "dark";
        }
        nativeSettings.add(kit.seg(strArr2, strArr, str, t("ns.theme", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLook.lambda$look$1(nativeSettings, (String) obj);
            }
        }));
        String[] strArr4 = {t("ns.col.accent", new Object[0]), t("ns.col.ink", new Object[0]), t("ns.col.alert", new Object[0])};
        int i3 = 0;
        while (i3 < 3) {
            final String str5 = SettingsModel.COLOR_KEYS[i3];
            final String str6 = strArr4[i3];
            nativeSettings.sec(str6);
            final String color = SettingsModel.color(str3, str5);
            kit = kit;
            LinearLayout swatches = kit.swatches(SettingsModel.PAL[i3], color, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrLook.setColor(nativeSettings, str5, (String) obj);
                }
            }, t("ns.col.custom", new Object[i2]) + ", " + str6, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLook.picker(r0, str6, color, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda16
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ScrLook.setColor(nativeSettings, r2, (String) obj);
                        }
                    });
                }
            });
            if (SettingsModel.colorApplies(str5, "light".equals(str2) ? "light" : "dark")) {
                nativeSettings.add(swatches);
                i = 0;
            } else {
                swatches.setAlpha(0.45f);
                nativeSettings.add(swatches);
                i = 0;
                nativeSettings.fine(t("ns.col.ink.light", new Object[0]));
            }
            i3++;
            i2 = i;
        }
        int i4 = i2;
        preview(nativeSettings, ui[i4]);
        nativeSettings.sec(t("ns.tp", new Object[i4]));
        for (int[] iArr : SettingsModel.TP) {
            if (iArr[0] == ui[0] && iArr[1] == ui[1]) {
                str4 = iArr[0] + "," + iArr[1];
            }
        }
        nativeSettings.add(kit.seg(new String[]{t("ns.tp.clear", new Object[0]), t("ns.tp.glass", new Object[0]), t("ns.tp.frosted", new Object[0]), t("ns.tp.solid", new Object[0])}, new String[]{"8,2", "42,12", "70,18", "100,0"}, str4, t("ns.tp", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLook.lambda$look$5(nativeSettings, (String) obj);
            }
        }));
        nativeSettings.add(kit.slider(t("ns.tp.panels", new Object[0]), 0, 100, 1, ui[0], t("ns.tp.panels", new Object[0]), new IntFunction() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda11
            @Override // java.util.function.IntFunction
            public final Object apply(int i5) {
                return ScrLook.lambda$look$6(i5);
            }
        }, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLook.setUi(nativeSettings, ((Integer) obj).intValue(), -1, -1);
            }
        }));
        nativeSettings.add(kit.slider(t("ns.tp.blur", new Object[0]), 0, 24, 1, ui[1], t("ns.tp.blur", new Object[0]), new IntFunction() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda13
            @Override // java.util.function.IntFunction
            public final Object apply(int i5) {
                return ScrLook.lambda$look$8(i5);
            }
        }, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLook.setUi(nativeSettings, -1, ((Integer) obj).intValue(), -1);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$look$0(NativeSettings nativeSettings, String str) {
        Store.set(Advanced.KEY, str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$look$1(NativeSettings nativeSettings, String str) {
        nativeSettings.setBoth("aos.theme", str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$look$5(NativeSettings nativeSettings, String str) {
        String[] split = str.split(",");
        setUi(nativeSettings, Integer.parseInt(split[0]), Integer.parseInt(split[1]), -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String lambda$look$6(int i) {
        return i + "%";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String lambda$look$8(int i) {
        return i + " px";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setColor(NativeSettings nativeSettings, String str, String str2) {
        nativeSettings.setBoth("aos.colors", SettingsModel.colorSet(Store.get("aos.colors"), str, str2.toLowerCase(Locale.ROOT)));
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setUi(NativeSettings nativeSettings, int i, int i2, int i3) {
        int[] ui = SettingsModel.ui(Store.get("aos.ui"));
        String str = Store.get("aos.ui");
        if (i < 0) {
            i = ui[0];
        }
        if (i2 < 0) {
            i2 = ui[1];
        }
        if (i3 < 0) {
            i3 = ui[2];
        }
        Store.set("aos.ui", SettingsModel.uiSet(str, i, i2, i3));
        nativeSettings.paint();
    }

    static void preview(NativeSettings nativeSettings, int i) {
        Kit kit = nativeSettings.k;
        nativeSettings.sec(t("ns.preview", new Object[0]));
        LinearLayout card = kit.card(true, false);
        LinearLayout linearLayout = new LinearLayout(nativeSettings.c);
        linearLayout.setGravity(16);
        linearLayout.addView(kit.iconView("M8 1.5l1.6 4.9L14.5 8l-4.9 1.6L8 14.5l-1.6-4.9L1.5 8l4.9-1.6z", kit.ACC, 18.0f));
        TextView text = kit.text(t("ns.preview.t", new Object[0]), Fonts.RAJ_600, 15.0f, 0.0f, kit.INK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.leftMargin = nativeSettings.px(10.0f);
        linearLayout.addView(text, layoutParams);
        linearLayout.addView(kit.chip(t("ns.preview.chip", new Object[0]), kit.ACC));
        TextView chip = kit.chip(t("ns.preview.alert", new Object[0]), kit.ALT);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = nativeSettings.px(6.0f);
        linearLayout.addView(chip, layoutParams2);
        card.addView(linearLayout);
        View text2 = kit.text(t("ns.preview.p", new Object[0]), Fonts.RAJ_400, 13.0f, 0.0f, kit.DIM);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams3.topMargin = nativeSettings.px(6.0f);
        card.addView(text2, layoutParams3);
        card.setContentDescription(t("ns.preview", new Object[0]) + ": " + t("ns.preview.p", new Object[0]));
        card.setFocusable(true);
        nativeSettings.add(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void picker(NativeSettings nativeSettings, String str, String str2, Consumer<String> consumer) {
        nativeSettings.k.colorPicker(nativeSettings.root, str, str2, consumer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void text(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        int[] ui = SettingsModel.ui(Store.get("aos.ui"));
        nativeSettings.add(nativeSettings.hd(NativeSettings.up(t("set.text", new Object[0])), ""));
        perf(nativeSettings);
        nativeSettings.sec(t("motion.h", new Object[0]));
        kit.add(nativeSettings.list(), kit.toggleRow(t("motion.reduce", new Object[0]), nativeSettings.be.powerSave() ? t("motion.saver.on", new Object[0]) : t("motion.saver.off", new Object[0]), "1".equals(Store.get("aos.rm", "")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.setBoth("aos.rm", r2.booleanValue() ? "1" : "");
            }
        }));
        nativeSettings.sec(t("ns.textsize", new Object[0]));
        nativeSettings.add(kit.sliderAa(85, 130, 5, ui[2], t("ns.textsize", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLook.setUi(nativeSettings, -1, -1, ((Integer) obj).intValue());
            }
        }));
        nativeSettings.fine(t("ns.textsize.fine", "n", Integer.valueOf(ui[2])));
        preview(nativeSettings, ui[0]);
    }

    static void perf(final NativeSettings nativeSettings) {
        String t;
        Kit kit = nativeSettings.k;
        JSONObject state = Perf.state();
        PerfModel.Knobs k = Perf.k();
        JSONObject optJSONObject = state.optJSONObject("measured");
        if (Perf.thermalStatus() >= 3) {
            t = t("perf.forced.heat", new Object[0]);
        } else {
            t = nativeSettings.be.powerSave() ? t("perf.forced.saver", new Object[0]) : "";
        }
        nativeSettings.sec(t("perf.h", new Object[0]));
        TextView fine = kit.fine(t("perf.now", "tier", Perf.tier(), "fps", Integer.valueOf(k.fps)) + (t.isEmpty() ? "" : " · " + t));
        ((LinearLayout.LayoutParams) fine.getLayoutParams()).topMargin = 0;
        nativeSettings.add(fine);
        nativeSettings.sec(NativeSettings.up(t("perf.mode", new Object[0])));
        int length = PerfModel.MODES.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            strArr[i] = t("perf.m." + PerfModel.MODES[i], new Object[0]);
        }
        nativeSettings.add(kit.seg(strArr, PerfModel.MODES, state.optString("mode", "auto"), t("perf.mode", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLook.lambda$perf$12(nativeSettings, (String) obj);
            }
        }));
        nativeSettings.fine(t("perf.mode.sub", new Object[0]));
        nativeSettings.sec(NativeSettings.up(t("perf.fps", new Object[0])));
        List<String> rates = Perf.rates();
        int size = rates.size();
        String[] strArr2 = new String[size];
        String[] strArr3 = (String[]) rates.toArray(new String[0]);
        for (int i2 = 0; i2 < size; i2++) {
            strArr2[i2] = "auto".equals(strArr3[i2]) ? t("perf.fps.auto", new Object[0]) : strArr3[i2];
        }
        nativeSettings.add(kit.seg(strArr2, strArr3, state.optString("fps", "auto"), t("perf.fps", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLook.lambda$perf$13(nativeSettings, (String) obj);
            }
        }));
        LinearLayout list = nativeSettings.list();
        String[] strArr4 = {"blur", "orb", "dock", "motion", "boot", "meter"};
        int i3 = 0;
        while (true) {
            boolean z = true;
            if (i3 >= 6) {
                break;
            }
            final String str = strArr4[i3];
            String t2 = t("perf.sw." + str, new Object[0]);
            if (state.optInt(str, !"meter".equals(str) ? 1 : 0) == 0) {
                z = false;
            }
            kit.add(list, kit.toggleRow(t2, null, z, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Perf.set(str, Integer.valueOf(r1.booleanValue() ? 1 : 0));
                }
            }));
            i3++;
        }
        TextView btn = kit.btn(t("perf.retest", new Object[0]), false, true, null);
        kit.enable(btn, !nativeSettings.perfTesting);
        btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrLook.lambda$perf$16(nativeSettings, view);
            }
        });
        nativeSettings.add(kit.hrow(btn));
        TextView fine2 = kit.fine(nativeSettings.perfTesting ? t("perf.testing", new Object[0]) : optJSONObject != null ? t("perf.result", "tier", state.optString("auto"), "fps", Integer.valueOf(optJSONObject.optInt("fps")), "ms", Double.valueOf(optJSONObject.optDouble("ms"))) : t("perf.none", new Object[0]));
        fine2.setAccessibilityLiveRegion(1);
        nativeSettings.add(fine2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$perf$12(NativeSettings nativeSettings, String str) {
        Perf.set("mode", str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$perf$13(NativeSettings nativeSettings, String str) {
        boolean equals = "auto".equals(str);
        Object obj = str;
        if (!equals) {
            obj = Integer.valueOf(Integer.parseInt(str));
        }
        Perf.set("fps", obj);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$perf$15(NativeSettings nativeSettings) {
        nativeSettings.perfTesting = false;
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$perf$16(final NativeSettings nativeSettings, View view) {
        if (nativeSettings.perfTesting) {
            return;
        }
        nativeSettings.perfTesting = true;
        nativeSettings.paint();
        Perf.retest(new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLook$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                ScrLook.lambda$perf$15(nativeSettings);
            }
        });
    }
}
