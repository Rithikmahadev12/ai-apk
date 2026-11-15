package com.jarvis.assist.aosui.settings;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.brief.BriefModel;
import com.jarvis.assist.aosui.core.AppGridModel;
import com.jarvis.assist.aosui.core.Corners;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.HomePages;
import com.jarvis.assist.aosui.core.HomeSlots;
import com.jarvis.assist.aosui.core.Profile;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.LockSheet;
import com.jarvis.assist.aosui.home.YouSheet;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Sheet;
import com.jarvis.assist.aosui.ui.Tiles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class ScrLayout {
    private ScrLayout() {
    }

    static String t(String str, Object... objArr) {
        return NativeSettings.t(str, objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String label(String str, String str2) {
        String appLabel = SettingsModel.appLabel(str2);
        if (appLabel != null) {
            return appLabel;
        }
        String t = Strings.t("ns.cat." + str + "." + str2);
        return t.startsWith("ns.cat.") ? str2 : t;
    }

    static String wname(NativeSettings nativeSettings, String str, SettingsModel.Cfg cfg) {
        if (str.startsWith("aw:")) {
            return t("wid.aw", "label", cfg.aw.containsKey(str) ? cfg.aw.get(str) : "");
        }
        return t("wid." + str, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void home(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        nativeSettings.add(nativeSettings.hd(t("ns.home.h", new Object[0]), t("ns.home.sub", new Object[0])));
        String[] strArr = {"wheel", "qs", "dock"};
        int i = 0;
        while (i < 3) {
            final String str = strArr[i];
            int slots = SettingsModel.slots(str);
            String str2 = "ns.slots.";
            String str3 = "n";
            nativeSettings.sec(t("ns.slots." + str, "n", Integer.valueOf(slots)));
            Tiles tiles = new Tiles(nativeSettings.c, nativeSettings.px(Math.max(1.0f, kit.z) * 130.0f), nativeSettings.px(8.0f));
            List<String> slots2 = cfg.slots(str);
            int i2 = 0;
            while (i2 < Math.min(slots, slots2.size())) {
                LinearLayout linearLayout = new LinearLayout(nativeSettings.c);
                linearLayout.setGravity(16);
                linearLayout.setPadding(nativeSettings.px(12.0f), 0, nativeSettings.px(12.0f), 0);
                linearLayout.setMinimumHeight(nativeSettings.px(46.0f));
                Tiles tiles2 = tiles;
                String str4 = str2;
                linearLayout.setBackground(new Kit.CardBg(kit.CARD, kit.LINE2, 0, nativeSettings.dn));
                int i3 = i2 + 1;
                linearLayout.addView(kit.num(String.valueOf(i3)));
                String[] slotLines = SettingsModel.slotLines(label(str, slots2.get(i2)));
                LinearLayout linearLayout2 = new LinearLayout(nativeSettings.c);
                linearLayout2.setOrientation(1);
                String str5 = str3;
                final int i4 = i2;
                SettingsModel.Cfg cfg2 = cfg;
                int i5 = slots;
                String[] strArr2 = strArr;
                int i6 = i;
                TextView text = kit.text(slotLines[0], Fonts.RAJ_600, 14.0f, 0.0f, kit.INK);
                text.setSingleLine(false);
                text.setEllipsize(null);
                linearLayout2.addView(text);
                if (slotLines[1] != null) {
                    TextView text2 = kit.text(slotLines[1], Fonts.MONO, 9.0f, 0.08f, kit.DIM);
                    text2.setSingleLine(false);
                    text2.setMaxLines(2);
                    linearLayout2.addView(text2);
                }
                linearLayout2.setPadding(0, nativeSettings.px(6.0f), 0, nativeSettings.px(6.0f));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
                layoutParams.leftMargin = nativeSettings.px(8.0f);
                linearLayout.addView(linearLayout2, layoutParams);
                linearLayout.setClickable(true);
                linearLayout.setFocusable(true);
                linearLayout.setContentDescription(t("ns.slot.a11y", "kind", t(str4 + str, str5, Integer.valueOf(i5)), "n", Integer.valueOf(i3), "label", label(str, slots2.get(i4))));
                linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda42
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ScrLayout.lambda$home$0(NativeSettings.this, str, i4, view);
                    }
                });
                tiles2.addView(linearLayout);
                tiles = tiles2;
                str2 = str4;
                str3 = str5;
                i = i6;
                i2 = i3;
                slots = i5;
                cfg = cfg2;
                strArr = strArr2;
            }
            nativeSettings.add(tiles);
            i++;
            strArr = strArr;
        }
        SettingsModel.Cfg cfg3 = cfg;
        nativeSettings.fine(t("ns.home.fine", new Object[0]));
        kit.add(nativeSettings.list(), kit.row(null, 0, t("cz.title", new Object[0]), t("cz.row.sub", new Object[0]), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m356lambda$navRow$7$comaugmaososettingsNativeSettings("customcontrol");
            }
        }, new View[0]));
        kit.add(nativeSettings.list(), kit.row(null, 0, t("db.set.row", new Object[0]), t("db.set.sub", new Object[0]), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m356lambda$navRow$7$comaugmaososettingsNativeSettings("brief");
            }
        }, new View[0]));
        nativeSettings.sec(t("ns.gestures", new Object[0]));
        LinearLayout list = nativeSettings.list();
        kit.add(list, kit.toggleRow(t("ns.dbl", new Object[0]), null, cfg3.dbl, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda45
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$home$3(NativeSettings.this, (Boolean) obj);
            }
        }));
        lockRow(nativeSettings, list);
        String t = t("ns.lockwall", new Object[0]);
        String t2 = t("ns.lockwall.sub", new Object[0]);
        final Backend backend = nativeSettings.be;
        Objects.requireNonNull(backend);
        kit.add(list, kit.row(null, 0, t, t2, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                Backend.this.openLockWallpaper();
            }
        }, new View[0]));
        TextView btn = kit.btn(t("ns.reset", new Object[0]), true, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$home$4(NativeSettings.this);
            }
        });
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.topMargin = nativeSettings.px(16.0f);
        btn.setLayoutParams(layoutParams2);
        nativeSettings.add(btn);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$home$0(NativeSettings nativeSettings, String str, int i, View view) {
        Feedback.fx(view, 0);
        picker(nativeSettings, str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$home$3(NativeSettings nativeSettings, Boolean bool) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        cfg.dbl = bool.booleanValue();
        nativeSettings.saveCfg(cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$home$4(NativeSettings nativeSettings) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        SettingsModel.resetSlots(cfg);
        nativeSettings.saveCfg(cfg);
        nativeSettings.paint();
    }

    static void lockRow(final NativeSettings nativeSettings, LinearLayout linearLayout) {
        Kit kit = nativeSettings.k;
        final JSONObject J = NativeSettings.J(nativeSettings.be.lockState());
        String optString = J.optString("via");
        String t = "service".equals(optString) ? t("lock.st.svc", new Object[0]) : "admin".equals(optString) ? t("lock.st.admin", new Object[0]) : t("lock.st.off", new Object[0]);
        boolean equals = Boolean.FALSE.equals(J.opt("canService"));
        final Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$lockRow$5(NativeSettings.this);
            }
        };
        ArrayList arrayList = new ArrayList();
        if (!"service".equals(optString) && !equals) {
            arrayList.add(kit.btn(t("lock.setup", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    LockSheet.ask(r0.root, r0.th, NativeSettings.this.be, false, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda28
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            r1.run();
                        }
                    });
                }
            }));
        }
        if (!equals) {
            final boolean equals2 = "admin".equals(nativeSettings.be.get("aos.lockvia"));
            arrayList.add(kit.btn(equals2 ? t("lock.use.svc", new Object[0]) : t("lock.use.admin", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.lambda$lockRow$9(equals2, nativeSettings, J, runnable);
                }
            }));
        } else if (!J.optBoolean("admin")) {
            arrayList.add(kit.btn(t("lock.setup", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    LockSheet.ask(r0.root, r0.th, NativeSettings.this.be, true, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda18
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            r1.run();
                        }
                    });
                }
            }));
        }
        kit.add(linearLayout, kit.rowWrap(J.optBoolean("ready") ? t("lock.ready", new Object[0]) : t("lock.need", new Object[0]), t, (View[]) arrayList.toArray(new View[0])));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$lockRow$5(NativeSettings nativeSettings) {
        Handler handler = NativeSettings.MAIN;
        Objects.requireNonNull(nativeSettings);
        handler.postDelayed(new NativeSettings$$ExternalSyntheticLambda25(nativeSettings), 400L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$lockRow$9(boolean z, NativeSettings nativeSettings, JSONObject jSONObject, final Runnable runnable) {
        String str = z ? "" : "admin";
        nativeSettings.be.set("aos.lockvia", str);
        if ("admin".equals(str) && !jSONObject.optBoolean("admin")) {
            LockSheet.ask(nativeSettings.root, nativeSettings.th, nativeSettings.be, true, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda58
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    runnable.run();
                }
            });
        }
        nativeSettings.paint();
    }

    static void picker(final NativeSettings nativeSettings, final String str, int i) {
        Kit kit;
        String[] strArr;
        Kit.IconView iconView;
        final int i2 = i;
        Kit kit2 = nativeSettings.k;
        List<String> slots = nativeSettings.cfg().slots(str);
        int i3 = i2 + 1;
        final Sheet title = new Sheet(nativeSettings.c, nativeSettings.th, t("ns.slot", "n", Integer.valueOf(i3))).title(t("ns.slot", "n", Integer.valueOf(i3)));
        LinearLayout body = title.body();
        final Consumer consumer = new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda59
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$picker$12(NativeSettings.this, str, i2, title, (String) obj);
            }
        };
        body.addView(kit2.section("AOS"));
        LinearLayout list = kit2.list();
        body.addView(list);
        String[] catalog = SettingsModel.catalog(str);
        int length = catalog.length;
        boolean z = false;
        int i4 = 0;
        while (i4 < length) {
            final String str2 = catalog[i4];
            boolean z2 = (i2 >= slots.size() || !str2.equals(slots.get(i2))) ? z : true;
            String label = label(str, str2);
            Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    consumer.accept(str2);
                }
            };
            if (z2) {
                strArr = catalog;
                iconView = kit2.iconView(Kit.CHECK, kit2.ACC, 14.0f);
            } else {
                strArr = catalog;
                iconView = null;
            }
            View[] viewArr = {iconView};
            int i5 = length;
            int i6 = i4;
            View add = kit2.add(list, kit2.row(null, 0, label, null, false, runnable, viewArr));
            if (z2) {
                add.setContentDescription(label(str, str2) + ", selected");
            }
            i4 = i6 + 1;
            i2 = i;
            length = i5;
            catalog = strArr;
            z = false;
        }
        if (!"qs".equals(str)) {
            body.addView(kit2.section(t("ns.installed", new Object[0])));
            LinearLayout list2 = kit2.list();
            body.addView(list2);
            String appsCached = nativeSettings.be.appsCached();
            if (appsCached == null) {
                kit = kit2;
                kit.add(list2, kit.row(null, 0, t("dr.loading", new Object[0]), null, false, null, new View[0]));
            } else {
                kit = kit2;
            }
            if (appsCached == null) {
                appsCached = "[]";
            }
            JSONArray JA = NativeSettings.JA(appsCached);
            ArrayList arrayList = new ArrayList();
            for (int i7 = 0; i7 < JA.length(); i7++) {
                if (JA.optJSONObject(i7) != null) {
                    arrayList.add(JA.optJSONObject(i7));
                }
            }
            Collections.sort(arrayList, new Comparator() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda61
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((JSONObject) obj).optString("label").compareToIgnoreCase(((JSONObject) obj2).optString("label"));
                    return compareToIgnoreCase;
                }
            });
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                JSONObject jSONObject = (JSONObject) it.next();
                final String appEntry = SettingsModel.appEntry(jSONObject.optString("pkg"), jSONObject.optString("cls"), jSONObject.optString("label"));
                kit.add(list2, kit.row(null, 0, jSONObject.optString("label"), null, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda62
                    @Override // java.lang.Runnable
                    public final void run() {
                        consumer.accept(appEntry);
                    }
                }, new View[0]));
            }
        }
        title.show(nativeSettings.root);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$picker$12(NativeSettings nativeSettings, String str, int i, Sheet sheet, String str2) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        List<String> slots = cfg.slots(str);
        while (slots.size() <= i) {
            slots.add(str2);
        }
        slots.set(i, str2);
        nativeSettings.saveCfg(cfg);
        sheet.close();
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void custom(final NativeSettings nativeSettings, String str) {
        int i;
        String str2;
        String str3;
        Kit kit;
        LinearLayout linearLayout;
        SettingsModel.Cfg cfg;
        int i2;
        ArrayList arrayList;
        int i3;
        Object obj;
        View spacer;
        Kit kit2 = nativeSettings.k;
        SettingsModel.Cfg cfg2 = nativeSettings.cfg();
        int i4 = 0;
        nativeSettings.add(nativeSettings.hd(NativeSettings.up(t("cz.title", new Object[0])), t("cz.sub", new Object[0])));
        ArrayList<String> arrayList2 = new ArrayList(Arrays.asList(HomeSlots.AREAS));
        if (str != null) {
            arrayList2.remove(str);
            arrayList2.add(0, str);
        }
        for (final String str4 : arrayList2) {
            List<String> slots = cfg2.slots(str4);
            int min = HomeSlots.min(str4);
            int max = HomeSlots.max(str4);
            nativeSettings.sec(t("cz.area." + str4, new Object[i4]) + " · " + slots.size() + " / " + max);
            LinearLayout list = nativeSettings.list();
            final int i5 = i4;
            while (i5 < slots.size()) {
                String str5 = slots.get(i5);
                String slotName = slotName(str5);
                ArrayList arrayList3 = new ArrayList();
                if (slots.size() > min) {
                    String t = t("wid.remove", new Object[i4]);
                    kit = kit2;
                    linearLayout = list;
                    str3 = slotName;
                    arrayList = arrayList3;
                    cfg = cfg2;
                    i2 = 1;
                    arrayList.add(kit.circle(Kit.X, t + " " + slotName, true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda53
                        @Override // java.lang.Runnable
                        public final void run() {
                            ScrLayout.lambda$custom$16(NativeSettings.this, str4, i5);
                        }
                    }));
                } else {
                    str3 = slotName;
                    kit = kit2;
                    linearLayout = list;
                    cfg = cfg2;
                    i2 = 1;
                    arrayList = arrayList3;
                }
                if (i5 > 0) {
                    i3 = i2;
                    obj = "name";
                    spacer = kit.circle(Kit.UP, t("ns.up", "name", str3), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda54
                        @Override // java.lang.Runnable
                        public final void run() {
                            ScrLayout.czMove(NativeSettings.this, str4, r2, i5 - 1);
                        }
                    });
                } else {
                    i3 = i2;
                    obj = "name";
                    spacer = spacer(nativeSettings);
                }
                arrayList.add(spacer);
                arrayList.add(i5 < slots.size() + (-1) ? kit.circle(Kit.DOWN, t("ns.down", obj, str3), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda55
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrLayout.czMove(NativeSettings.this, str4, r2, i5 + 1);
                    }
                }) : spacer(nativeSettings));
                LinearLayout linearLayout2 = linearLayout;
                LinearLayout row = kit.row(null, 0, str3, t("cz.kind." + HomeSlots.kind(str5), new Object[0]), false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda56
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrLayout.czPick(NativeSettings.this, str4, i5);
                    }
                }, (View[]) arrayList.toArray(new View[0]));
                i5++;
                row.addView(kit.num(String.format(Locale.ROOT, "%02d", Integer.valueOf(i5))), 0);
                ((LinearLayout.LayoutParams) row.getChildAt(i3).getLayoutParams()).leftMargin = nativeSettings.px(12.0f);
                kit.add(linearLayout2, row);
                kit2 = kit;
                list = linearLayout2;
                cfg2 = cfg;
                i4 = 0;
            }
            Kit kit3 = kit2;
            SettingsModel.Cfg cfg3 = cfg2;
            if (slots.size() < max) {
                nativeSettings.add(kit3.hrow(kit3.btn(t("cz.add", new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda57
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrLayout.czPick(NativeSettings.this, str4, -1);
                    }
                })));
            }
            String t2 = t("cz.limits", "min", Integer.valueOf(min), "max", Integer.valueOf(max));
            if ("wheel".equals(str4)) {
                i = 0;
                str2 = " " + t("cz.grid.fine", new Object[0]);
            } else {
                i = 0;
                str2 = "";
            }
            nativeSettings.fine(t2 + str2);
            kit2 = kit3;
            i4 = i;
            cfg2 = cfg3;
        }
        corners(nativeSettings);
        pages(nativeSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$custom$16(NativeSettings nativeSettings, String str, int i) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        if (HomeSlots.remove(str, cfg.slots(str), i)) {
            nativeSettings.saveCfg(cfg);
            nativeSettings.paint();
        }
    }

    static void corners(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        String str = Store.get(Corners.KEY, "");
        Set<String> enabledWidgets = enabledWidgets(nativeSettings);
        nativeSettings.sec(t("cn.h", new Object[0]));
        LinearLayout list = nativeSettings.list();
        for (int i = 0; i < 4; i++) {
            final String str2 = Corners.CORNERS[i];
            String user = Corners.user(str2, str);
            String pick = Corners.pick(str2, str, enabledWidgets);
            kit.add(list, kit.row(null, 0, t("cn." + str2, new Object[0]), (user == null ? t("cn.auto", new Object[0]) + " · " : "") + t("cn.i." + pick, new Object[0]), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.cornerPick(NativeSettings.this, str2);
                }
            }, new View[0]));
        }
        nativeSettings.fine(t("cn.fine", new Object[0]));
    }

    static Set<String> enabledWidgets(NativeSettings nativeSettings) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        HashSet hashSet = new HashSet();
        for (String str : cfg.widgets) {
            if (!cfg.off.contains(str)) {
                hashSet.add(str);
            }
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void cornerPick(final NativeSettings nativeSettings, final String str) {
        Kit kit = nativeSettings.k;
        final Sheet title = new Sheet(nativeSettings.c, nativeSettings.th, t("cn." + str, new Object[0])).title(t("cn." + str, new Object[0]));
        LinearLayout list = kit.list();
        title.body().addView(list);
        Set<String> enabledWidgets = enabledWidgets(nativeSettings);
        kit.add(list, kit.row(null, 0, t("cn.auto", new Object[0]), t("cn.auto.sub", new Object[0]), false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$cornerPick$22(str, title, nativeSettings);
            }
        }, new View[0]));
        for (final String str2 : Corners.ITEMS) {
            kit.add(list, kit.row(null, 0, t("cn.i." + str2, new Object[0]), Corners.inWidget(str2, enabledWidgets) ? t("cn.inwidget", new Object[0]) : null, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.lambda$cornerPick$23(str, str2, title, nativeSettings);
                }
            }, new View[0]));
        }
        title.show(nativeSettings.root);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$cornerPick$22(String str, Sheet sheet, NativeSettings nativeSettings) {
        Store.set(Corners.KEY, Corners.set(Store.get(Corners.KEY, ""), str, null));
        sheet.close();
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$cornerPick$23(String str, String str2, Sheet sheet, NativeSettings nativeSettings) {
        Store.set(Corners.KEY, Corners.set(Store.get(Corners.KEY, ""), str, str2));
        sheet.close();
        nativeSettings.paint();
    }

    static HomePages.Cfg pc() {
        return HomePages.cfg(Store.get(HomePages.KEY, ""));
    }

    static void pcSave(NativeSettings nativeSettings, HomePages.Cfg cfg) {
        Store.set(HomePages.KEY, HomePages.save(cfg));
        nativeSettings.paint();
    }

    static void pages(final NativeSettings nativeSettings) {
        Object obj;
        View spacer;
        Kit kit = nativeSettings.k;
        HomePages.Cfg pc = pc();
        nativeSettings.sec(t("pg.h", new Object[0]) + " · " + HomePages.count(pc));
        LinearLayout list = nativeSettings.list();
        kit.add(list, kit.row(null, 0, t("pg.main", new Object[0]), pc.def == 0 ? t("pg.default", new Object[0]) : null, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$pages$24(NativeSettings.this);
            }
        }, new View[0]));
        int i = 1;
        final int i2 = 1;
        while (i2 <= pc.pages.size()) {
            HomePages.Page page = pc.pages.get(i2 - 1);
            ArrayList arrayList = new ArrayList();
            arrayList.add(kit.circle(Kit.X, t("wid.remove", new Object[0]) + " " + page.name, true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.lambda$pages$25(i2, nativeSettings);
                }
            }));
            if (i2 > i) {
                obj = "name";
                spacer = kit.circle(Kit.UP, t("ns.up", "name", page.name), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrLayout.lambda$pages$26(i2, nativeSettings);
                    }
                });
            } else {
                obj = "name";
                spacer = spacer(nativeSettings);
            }
            arrayList.add(spacer);
            arrayList.add(i2 < pc.pages.size() ? kit.circle(Kit.DOWN, t("ns.down", obj, page.name), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.lambda$pages$27(i2, nativeSettings);
                }
            }) : spacer(nativeSettings));
            StringBuilder sb = new StringBuilder();
            Iterator<HomePages.Block> it = page.blocks.iterator();
            while (it.hasNext()) {
                HomePages.Block next = it.next();
                if (sb.length() > 0) {
                    sb.append(" · ");
                }
                sb.append(t("pg.t." + next.type, new Object[0]));
            }
            int i3 = i2 + 1;
            kit.add(list, kit.row(null, 0, String.format(Locale.ROOT, "%02d  %s", Integer.valueOf(i3), page.name) + (pc.def == i2 ? "  · " + t("pg.default", new Object[0]) : ""), sb.length() == 0 ? t("pg.empty", new Object[0]) : sb.toString(), false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.pageEdit(NativeSettings.this, i2);
                }
            }, (View[]) arrayList.toArray(new View[0])));
            i2 = i3;
            i = 1;
        }
        if (pc.pages.size() < 5) {
            nativeSettings.add(kit.hrow(kit.btn(t("pg.add", new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.lambda$pages$29(NativeSettings.this);
                }
            })));
        }
        nativeSettings.fine(t("pg.fine", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pages$24(NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        HomePages.setDefault(pc, 0);
        pcSave(nativeSettings, pc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pages$25(int i, NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        HomePages.remove(pc, i);
        pcSave(nativeSettings, pc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pages$26(int i, NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        HomePages.move(pc, i, i - 1);
        pcSave(nativeSettings, pc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pages$27(int i, NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        HomePages.move(pc, i, i + 1);
        pcSave(nativeSettings, pc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pages$29(NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        HomePages.add(pc, "PAGE " + (pc.pages.size() + 2));
        pcSave(nativeSettings, pc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void pageEdit(final NativeSettings nativeSettings, final int i) {
        Kit kit = nativeSettings.k;
        HomePages.Cfg pc = pc();
        if (i < 1 || i > pc.pages.size()) {
            return;
        }
        HomePages.Page page = pc.pages.get(i - 1);
        final Sheet title = new Sheet(nativeSettings.c, nativeSettings.th, page.name).title(page.name);
        LinearLayout body = title.body();
        final EditText input = kit.input(page.name, t("pg.rename", new Object[0]), false, false, 4097);
        body.addView(input);
        LinearLayout list = kit.list();
        body.addView(list);
        for (final int i2 = 0; i2 < page.blocks.size(); i2++) {
            HomePages.Block block = page.blocks.get(i2);
            kit.add(list, kit.row(null, 0, t("pg.t." + block.type, new Object[0]), "grid".equals(block.type) ? block.items.size() + " APPS" : String.join(" · ", block.items).toUpperCase(Locale.ROOT), false, null, kit.circle(Kit.X, t("wid.remove", new Object[0]), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.lambda$pageEdit$30(i, i2, title, nativeSettings);
                }
            })));
        }
        title.buttons(new String[]{t("pg.default", new Object[0]), t("pg.block.add", new Object[0]), t("cz.done", new Object[0])}, new Runnable[]{new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$pageEdit$31(i, input, nativeSettings);
            }
        }, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$pageEdit$32(i, input, nativeSettings);
            }
        }, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$pageEdit$33(i, input, nativeSettings);
            }
        }}, true);
        title.show(nativeSettings.root);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pageEdit$30(int i, int i2, Sheet sheet, NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        pc.pages.get(i - 1).blocks.remove(i2);
        Store.set(HomePages.KEY, HomePages.save(pc));
        sheet.close();
        pageEdit(nativeSettings, i);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pageEdit$31(int i, EditText editText, NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        HomePages.rename(pc, i, editText.getText().toString());
        HomePages.setDefault(pc, i);
        pcSave(nativeSettings, pc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pageEdit$32(int i, EditText editText, NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        HomePages.rename(pc, i, editText.getText().toString());
        Store.set(HomePages.KEY, HomePages.save(pc));
        blockAdd(nativeSettings, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pageEdit$33(int i, EditText editText, NativeSettings nativeSettings) {
        HomePages.Cfg pc = pc();
        HomePages.rename(pc, i, editText.getText().toString());
        pcSave(nativeSettings, pc);
    }

    static void blockAdd(final NativeSettings nativeSettings, final int i) {
        final Kit kit = nativeSettings.k;
        final Sheet title = new Sheet(nativeSettings.c, nativeSettings.th, t("pg.block.add", new Object[0])).title(t("pg.block.add", new Object[0]));
        LinearLayout body = title.body();
        final LinearLayout list = kit.list();
        final String[] strArr = {"grid"};
        final HomePages.Block[] blockArr = {null};
        final Runnable[] runnableArr = {new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$blockAdd$38(list, i, blockArr, strArr, title, nativeSettings, kit);
            }
        }};
        body.addView(kit.seg(new String[]{t("pg.t.grid", new Object[0]), t("pg.t.widget", new Object[0]), t("pg.t.half", new Object[0]), t("pg.t.big", new Object[0])}, HomePages.TYPES, strArr[0], t("pg.block.add", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$blockAdd$39(strArr, blockArr, runnableArr, (String) obj);
            }
        }));
        body.addView(list);
        runnableArr[0].run();
        Objects.requireNonNull(nativeSettings);
        title.onDismiss = new NativeSettings$$ExternalSyntheticLambda25(nativeSettings);
        title.show(nativeSettings.root);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$blockAdd$38(LinearLayout linearLayout, final int i, final HomePages.Block[] blockArr, final String[] strArr, final Sheet sheet, final NativeSettings nativeSettings, final Kit kit) {
        linearLayout.removeAllViews();
        final Consumer consumer = new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda69
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$blockAdd$34(i, blockArr, strArr, sheet, nativeSettings, kit, (String) obj);
            }
        };
        if ("grid".equals(strArr[0])) {
            String appsCached = nativeSettings.be.appsCached();
            if (appsCached == null) {
                appsCached = "[]";
            }
            JSONArray JA = NativeSettings.JA(appsCached);
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < JA.length(); i2++) {
                if (JA.optJSONObject(i2) != null) {
                    arrayList.add(JA.optJSONObject(i2));
                }
            }
            Collections.sort(arrayList, new Comparator() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda70
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((JSONObject) obj).optString("label").compareToIgnoreCase(((JSONObject) obj2).optString("label"));
                    return compareToIgnoreCase;
                }
            });
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                JSONObject jSONObject = (JSONObject) it.next();
                final String str = jSONObject.optString("pkg") + "/" + jSONObject.optString("cls");
                kit.add(linearLayout, kit.row(null, 0, jSONObject.optString("label"), null, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda71
                    @Override // java.lang.Runnable
                    public final void run() {
                        consumer.accept(str);
                    }
                }, new View[0]));
            }
            return;
        }
        for (final String str2 : SettingsModel.WIDGETS) {
            kit.add(linearLayout, kit.row(null, 0, t("wid." + str2, new Object[0]), null, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda72
                @Override // java.lang.Runnable
                public final void run() {
                    consumer.accept(str2);
                }
            }, new View[0]));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$blockAdd$34(int i, HomePages.Block[] blockArr, String[] strArr, Sheet sheet, NativeSettings nativeSettings, Kit kit, String str) {
        HomePages.Cfg pc = pc();
        int i2 = 1;
        HomePages.Page page = pc.pages.get(i - 1);
        if (blockArr[0] == null) {
            blockArr[0] = new HomePages.Block(strArr[0]);
            if (!HomePages.addBlock(page, blockArr[0])) {
                sheet.close();
                return;
            }
        } else {
            blockArr[0] = page.blocks.get(page.blocks.size() - 1);
        }
        blockArr[0].items.add(str);
        Store.set(HomePages.KEY, HomePages.save(pc));
        if ("grid".equals(strArr[0])) {
            i2 = 24;
        } else if ("half".equals(strArr[0])) {
            i2 = 2;
        }
        if (blockArr[0].items.size() < i2) {
            kit.toast(nativeSettings.root, str);
        } else {
            sheet.close();
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$blockAdd$39(String[] strArr, HomePages.Block[] blockArr, Runnable[] runnableArr, String str) {
        strArr[0] = str;
        blockArr[0] = null;
        runnableArr[0].run();
    }

    static String slotName(String str) {
        String appLabel = SettingsModel.appLabel(str);
        if (appLabel != null) {
            return appLabel;
        }
        if (str.startsWith("aos:")) {
            return t("cz.sc." + str.substring(4), new Object[0]);
        }
        String[] strArr = {"qs", "wheel", "dock"};
        for (int i = 0; i < 3; i++) {
            String t = Strings.t("ns.cat." + strArr[i] + "." + str);
            if (!t.startsWith("ns.cat.")) {
                return t;
            }
        }
        return str.toUpperCase(Locale.ROOT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void czMove(NativeSettings nativeSettings, String str, int i, int i2) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        if (HomeSlots.move(cfg.slots(str), i, i2)) {
            nativeSettings.saveCfg(cfg);
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void czPick(final NativeSettings nativeSettings, final String str, final int i) {
        final Kit kit = nativeSettings.k;
        final Sheet title = new Sheet(nativeSettings.c, nativeSettings.th, t("cz.pick", new Object[0])).title(t("cz.pick", new Object[0]));
        LinearLayout body = title.body();
        final LinearLayout list = kit.list();
        final Consumer consumer = new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$czPick$40(NativeSettings.this, str, i, title, (String) obj);
            }
        };
        final String[] strArr = {("qs".equals(str) || "qs2".equals(str)) ? "controls" : "apps"};
        final Runnable[] runnableArr = {new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$czPick$46(list, strArr, kit, consumer, nativeSettings);
            }
        }};
        body.addView(kit.seg(new String[]{t("cz.tab.apps", new Object[0]), t("cz.tab.controls", new Object[0]), t("cz.tab.shortcuts", new Object[0])}, new String[]{"apps", "controls", "shortcuts"}, strArr[0], t("cz.pick", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$czPick$47(strArr, runnableArr, (String) obj);
            }
        }));
        body.addView(list);
        runnableArr[0].run();
        title.show(nativeSettings.root);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$czPick$40(NativeSettings nativeSettings, String str, int i, Sheet sheet, String str2) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        List<String> slots = cfg.slots(str);
        if (i < 0) {
            HomeSlots.add(str, slots, str2);
        } else if (i < slots.size()) {
            slots.set(i, str2);
        }
        nativeSettings.saveCfg(cfg);
        sheet.close();
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$czPick$46(LinearLayout linearLayout, String[] strArr, Kit kit, final Consumer consumer, NativeSettings nativeSettings) {
        linearLayout.removeAllViews();
        if ("controls".equals(strArr[0])) {
            String[] strArr2 = SettingsModel.QS;
            int length = strArr2.length;
            int i = 0;
            while (i < length) {
                final String str = strArr2[i];
                Kit kit2 = kit;
                kit2.add(linearLayout, kit2.row(null, 0, slotName(str), null, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda23
                    @Override // java.lang.Runnable
                    public final void run() {
                        consumer.accept(str);
                    }
                }, new View[0]));
                i++;
                kit = kit2;
            }
            return;
        }
        if ("shortcuts".equals(strArr[0])) {
            for (final String str2 : HomeSlots.SHORTCUTS) {
                kit.add(linearLayout, kit.row(null, 0, slotName(str2), t("cz.kind.aos", new Object[0]), false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda24
                    @Override // java.lang.Runnable
                    public final void run() {
                        consumer.accept(str2);
                    }
                }, new View[0]));
            }
            LinkedHashSet linkedHashSet = new LinkedHashSet(Arrays.asList(SettingsModel.WHEEL));
            linkedHashSet.addAll(Arrays.asList(SettingsModel.DOCK));
            Iterator it = linkedHashSet.iterator();
            while (it.hasNext()) {
                final String str3 = (String) it.next();
                kit.add(linearLayout, kit.row(null, 0, slotName(str3), t("cz.kind.id", new Object[0]), false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda25
                    @Override // java.lang.Runnable
                    public final void run() {
                        consumer.accept(str3);
                    }
                }, new View[0]));
            }
            return;
        }
        String appsCached = nativeSettings.be.appsCached();
        if (appsCached == null) {
            appsCached = "[]";
        }
        JSONArray JA = NativeSettings.JA(appsCached);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < JA.length(); i2++) {
            if (JA.optJSONObject(i2) != null) {
                arrayList.add(JA.optJSONObject(i2));
            }
        }
        Collections.sort(arrayList, new Comparator() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda26
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareToIgnoreCase;
                compareToIgnoreCase = ((JSONObject) obj).optString("label").compareToIgnoreCase(((JSONObject) obj2).optString("label"));
                return compareToIgnoreCase;
            }
        });
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            JSONObject jSONObject = (JSONObject) it2.next();
            final String appEntry = SettingsModel.appEntry(jSONObject.optString("pkg"), jSONObject.optString("cls"), jSONObject.optString("label"));
            kit.add(linearLayout, kit.row(null, 0, jSONObject.optString("label"), null, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    consumer.accept(appEntry);
                }
            }, new View[0]));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$czPick$47(String[] strArr, Runnable[] runnableArr, String str) {
        strArr[0] = str;
        runnableArr[0].run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void widgets(final NativeSettings nativeSettings) {
        int i;
        Object obj;
        View spacer;
        KeyEvent.Callback callback;
        Kit kit = nativeSettings.k;
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        nativeSettings.add(nativeSettings.hd(NativeSettings.up(t("set.widgets", new Object[0])), t("wid.sub", new Object[0])));
        LinearLayout list = nativeSettings.list();
        final int i2 = 0;
        while (i2 < cfg.widgets.size()) {
            final String str = cfg.widgets.get(i2);
            boolean z = !cfg.off.contains(str);
            boolean startsWith = str.startsWith("aw:");
            String wname = wname(nativeSettings, str, cfg);
            ArrayList arrayList = new ArrayList();
            if (startsWith) {
                arrayList.add(kit.circle(Kit.X, t("wid.remove", new Object[0]) + " " + wname, true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrLayout.lambda$widgets$48(NativeSettings.this, str);
                    }
                }));
            }
            if (i2 > 0) {
                i = 1;
                obj = "name";
                spacer = kit.circle(Kit.UP, t("ns.up", "name", wname), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrLayout.move(NativeSettings.this, i2, -1);
                    }
                });
            } else {
                i = 1;
                obj = "name";
                spacer = spacer(nativeSettings);
            }
            arrayList.add(spacer);
            arrayList.add(i2 < cfg.widgets.size() + (-1) ? kit.circle(Kit.DOWN, t("ns.down", obj, wname), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.move(NativeSettings.this, i2, 1);
                }
            }) : spacer(nativeSettings));
            if (!"greeting".equals(str)) {
                callback = kit.toggle(z, wname, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda14
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj2) {
                        ScrLayout.lambda$widgets$51(NativeSettings.this, str, (Boolean) obj2);
                    }
                });
            } else {
                callback = kit.chip(t("wid.always", new Object[0]), kit.ACC);
            }
            arrayList.add(callback);
            LinearLayout row = kit.row(null, 0, wname, null, false, null, (View[]) arrayList.toArray(new View[0]));
            i2++;
            row.addView(kit.num(String.valueOf(i2)), 0);
            ((LinearLayout.LayoutParams) row.getChildAt(i).getLayoutParams()).leftMargin = nativeSettings.px(12.0f);
            kit.add(list, row);
        }
        nativeSettings.add(kit.hrow(kit.btn(t("wid.add", new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.addWidget(NativeSettings.this);
            }
        })));
        nativeSettings.fine(t("wid.add.fine", new Object[0]));
        StringBuilder sb = new StringBuilder();
        for (String str2 : SettingsModel.clocks(Store.get("aos.clocks"))) {
            if (sb.length() > 0) {
                sb.append(" · ");
            }
            sb.append(SettingsModel.city(str2));
        }
        LinearLayout list2 = nativeSettings.list();
        kit.add(list2, kit.row(null, 0, t("clocks.title", new Object[0]), sb.toString(), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m356lambda$navRow$7$comaugmaososettingsNativeSettings("clocks");
            }
        }, new View[0]));
        int size = AppGridModel.parse(Store.get(AppGridModel.KEY, "")).size();
        kit.add(list2, kit.row(null, 0, t("wid.appgrid", new Object[0]), size == 0 ? t("ag.none", new Object[0]) : t("ag.n", "n", Integer.valueOf(size)), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m356lambda$navRow$7$comaugmaososettingsNativeSettings(AppGridModel.ID);
            }
        }, new View[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$widgets$48(NativeSettings nativeSettings, String str) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        try {
            nativeSettings.be.widgetRemove(Integer.parseInt(str.substring(3)));
        } catch (Exception e) {
        }
        SettingsModel.removeWidget(cfg, str);
        nativeSettings.saveCfg(cfg);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$widgets$51(NativeSettings nativeSettings, String str, Boolean bool) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        SettingsModel.toggleOff(cfg, str);
        nativeSettings.saveCfg(cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void appGrid(final NativeSettings nativeSettings) {
        int i;
        Object obj;
        View spacer;
        Kit kit = nativeSettings.k;
        List<String> parse = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        nativeSettings.add(nativeSettings.hd(NativeSettings.up(t("wid.appgrid", new Object[0])), t("ag.sub", new Object[0])));
        int i2 = 1;
        kit.add(nativeSettings.list(), kit.toggleRow(t("ag.show", new Object[0]), t("ag.show.sub", new Object[0]), !cfg.off.contains(AppGridModel.ID), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda64
            @Override // java.util.function.Consumer
            public final void accept(Object obj2) {
                ScrLayout.lambda$appGrid$55(NativeSettings.this, (Boolean) obj2);
            }
        }));
        nativeSettings.sec(t("ag.apps", "n", Integer.valueOf(parse.size()), "max", 8));
        LinearLayout list = nativeSettings.list();
        HashMap hashMap = new HashMap();
        JSONArray JA = NativeSettings.JA(nativeSettings.be.appsCached() == null ? "[]" : nativeSettings.be.appsCached());
        for (int i3 = 0; i3 < JA.length(); i3++) {
            JSONObject optJSONObject = JA.optJSONObject(i3);
            if (optJSONObject != null) {
                hashMap.put(optJSONObject.optString("pkg") + "/" + optJSONObject.optString("cls"), optJSONObject.optString("label"));
            }
        }
        if (parse.isEmpty()) {
            kit.add(list, kit.row(null, 0, t("ag.none", new Object[0]), t("ag.none.sub", new Object[0]), false, null, new View[0]));
        }
        final int i4 = 0;
        while (i4 < parse.size()) {
            final String str = parse.get(i4);
            String str2 = hashMap.containsKey(str) ? (String) hashMap.get(str) : str;
            ArrayList arrayList = new ArrayList();
            arrayList.add(kit.circle(Kit.X, t("wid.remove", new Object[0]) + " " + str2, true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda65
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.lambda$appGrid$56(str, nativeSettings);
                }
            }));
            if (i4 > 0) {
                i = i2;
                obj = "name";
                spacer = kit.circle(Kit.UP, t("ns.up", "name", str2), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda66
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrLayout.agMove(NativeSettings.this, i4, -1);
                    }
                });
            } else {
                i = i2;
                obj = "name";
                spacer = spacer(nativeSettings);
            }
            arrayList.add(spacer);
            arrayList.add(i4 < parse.size() + (-1) ? kit.circle(Kit.DOWN, t("ns.down", obj, str2), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda67
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.agMove(NativeSettings.this, i4, 1);
                }
            }) : spacer(nativeSettings));
            LinearLayout row = kit.row(null, 0, str2, null, false, null, (View[]) arrayList.toArray(new View[0]));
            i4++;
            row.addView(kit.num(String.valueOf(i4)), 0);
            ((LinearLayout.LayoutParams) row.getChildAt(i).getLayoutParams()).leftMargin = nativeSettings.px(12.0f);
            kit.add(list, row);
            i2 = 1;
        }
        if (parse.size() < 8) {
            nativeSettings.add(kit.hrow(kit.btn(t("ag.add", new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda68
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.agPick(NativeSettings.this);
                }
            })));
        }
        nativeSettings.fine(t("ag.fine", "min", 4, "max", 8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$appGrid$55(NativeSettings nativeSettings, Boolean bool) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        if (!cfg.widgets.contains(AppGridModel.ID)) {
            SettingsModel.addWidget(cfg, AppGridModel.ID);
        } else if (bool.booleanValue() == cfg.off.contains(AppGridModel.ID)) {
            SettingsModel.toggleOff(cfg, AppGridModel.ID);
        }
        nativeSettings.saveCfg(cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$appGrid$56(String str, NativeSettings nativeSettings) {
        List<String> parse = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
        AppGridModel.remove(parse, str);
        Store.set(AppGridModel.KEY, AppGridModel.save(parse));
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void agMove(NativeSettings nativeSettings, int i, int i2) {
        List<String> parse = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
        if (AppGridModel.move(parse, i, i2)) {
            Store.set(AppGridModel.KEY, AppGridModel.save(parse));
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void agPick(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        final Sheet title = new Sheet(nativeSettings.c, nativeSettings.th, t("ag.add", new Object[0])).title(t("ag.add", new Object[0]));
        LinearLayout body = title.body();
        LinearLayout list = kit.list();
        body.addView(list);
        List<String> parse = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
        String appsCached = nativeSettings.be.appsCached();
        if (appsCached == null) {
            kit.add(list, kit.row(null, 0, t("dr.loading", new Object[0]), null, false, null, new View[0]));
        }
        if (appsCached == null) {
            appsCached = "[]";
        }
        JSONArray JA = NativeSettings.JA(appsCached);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < JA.length(); i++) {
            if (JA.optJSONObject(i) != null) {
                arrayList.add(JA.optJSONObject(i));
            }
        }
        Collections.sort(arrayList, new Comparator() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda3
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareToIgnoreCase;
                compareToIgnoreCase = ((JSONObject) obj).optString("label").compareToIgnoreCase(((JSONObject) obj2).optString("label"));
                return compareToIgnoreCase;
            }
        });
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            JSONObject jSONObject = (JSONObject) it.next();
            final String str = jSONObject.optString("pkg") + "/" + jSONObject.optString("cls");
            if (!parse.contains(str)) {
                kit.add(list, kit.row(null, 0, jSONObject.optString("label"), null, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrLayout.lambda$agPick$61(str, nativeSettings, title);
                    }
                }, new View[0]));
            }
        }
        title.show(nativeSettings.root);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$agPick$61(String str, NativeSettings nativeSettings, Sheet sheet) {
        List<String> parse = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
        AppGridModel.add(parse, str);
        Store.set(AppGridModel.KEY, AppGridModel.save(parse));
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        if (!cfg.widgets.contains(AppGridModel.ID) || cfg.off.contains(AppGridModel.ID)) {
            SettingsModel.addWidget(cfg, AppGridModel.ID);
            nativeSettings.saveCfg(cfg);
        }
        sheet.close();
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static View spacer(NativeSettings nativeSettings) {
        View view = new View(nativeSettings.c);
        view.setLayoutParams(new LinearLayout.LayoutParams(nativeSettings.px(34.0f), nativeSettings.px(34.0f)));
        view.setImportantForAccessibility(2);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void move(NativeSettings nativeSettings, int i, int i2) {
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        if (SettingsModel.move(cfg.widgets, i, i2)) {
            nativeSettings.saveCfg(cfg);
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addWidget(final NativeSettings nativeSettings) {
        final Kit kit = nativeSettings.k;
        final Sheet title = new Sheet(nativeSettings.c, nativeSettings.th, t("wid.add", new Object[0])).title(t("wid.add", new Object[0]));
        LinearLayout body = title.body();
        final TextView msg = kit.msg(t("wid.loading", new Object[0]));
        body.addView(msg);
        final LinearLayout list = kit.list();
        body.addView(list);
        title.show(nativeSettings.root);
        nativeSettings.be.widgetProviders(new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda63
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$addWidget$65(msg, nativeSettings, kit, title, list, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$addWidget$65(final TextView textView, final NativeSettings nativeSettings, Kit kit, final Sheet sheet, LinearLayout linearLayout, String str) {
        JSONArray JA = NativeSettings.JA(str);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < JA.length(); i++) {
            if (JA.optJSONObject(i) != null) {
                arrayList.add(JA.optJSONObject(i));
            }
        }
        Collections.sort(arrayList, new Comparator() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda40
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareTo;
                compareTo = (r1.optString("app") + ((JSONObject) obj).optString("label")).compareTo(r2.optString("app") + ((JSONObject) obj2).optString("label"));
                return compareTo;
            }
        });
        textView.setText(arrayList.isEmpty() ? t("wid.none", new Object[0]) : "");
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            final JSONObject jSONObject = (JSONObject) it.next();
            ImageView imageView = new ImageView(nativeSettings.c);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImportantForAccessibility(2);
            Bitmap dataImg = dataImg(jSONObject.optString("img"));
            if (dataImg != null) {
                imageView.setImageBitmap(dataImg);
            }
            imageView.setLayoutParams(new LinearLayout.LayoutParams(nativeSettings.px(56.0f), nativeSettings.px(40.0f)));
            LinearLayout row = kit.row(null, 0, jSONObject.optString("label"), NativeSettings.up(jSONObject.optString("app")) + (jSONObject.optInt("w") > 0 ? " · " + jSONObject.optInt("w") + "×" + jSONObject.optInt("h") + " DP" : ""), false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda41
                @Override // java.lang.Runnable
                public final void run() {
                    ScrLayout.lambda$addWidget$64(textView, nativeSettings, jSONObject, sheet);
                }
            }, new View[0]);
            row.addView(imageView, 0);
            ((LinearLayout.LayoutParams) row.getChildAt(1).getLayoutParams()).leftMargin = nativeSettings.px(12.0f);
            kit.add(linearLayout, row);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$addWidget$64(final TextView textView, final NativeSettings nativeSettings, JSONObject jSONObject, final Sheet sheet) {
        textView.setText(t("wid.adding", new Object[0]));
        nativeSettings.be.widgetAdd(jSONObject.optString("cn"), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda73
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$addWidget$63(textView, nativeSettings, sheet, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$addWidget$63(TextView textView, NativeSettings nativeSettings, Sheet sheet, String str) {
        JSONObject J = NativeSettings.J(str);
        if (!J.optBoolean("ok")) {
            textView.setText(t("wid.addfail", new Object[0]));
            return;
        }
        SettingsModel.Cfg cfg = nativeSettings.cfg();
        SettingsModel.addWidget(cfg, "aw:" + J.optInt("id"));
        nativeSettings.saveCfg(cfg);
        sheet.close();
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bitmap dataImg(String str) {
        if (str != null && str.startsWith("data:image")) {
            try {
                byte[] decode = Base64.decode(str.substring(str.indexOf(44) + 1), 0);
                return BitmapFactory.decodeByteArray(decode, 0, decode.length);
            } catch (Exception e) {
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void brief(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        BriefModel.Cfg cfg = BriefModel.cfg(Store.get("aos.brief"));
        final Consumer consumer = new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda29
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Store.set("aos.brief", BriefModel.save((BriefModel.Cfg) obj));
            }
        };
        nativeSettings.add(nativeSettings.hd(t("db.set.h", new Object[0]), t("db.set.sub", new Object[0])));
        nativeSettings.sec(t("db.set.switch", new Object[0]));
        ArrayList arrayList = new ArrayList();
        for (int i = 600; i <= 1320; i += 30) {
            arrayList.add(BriefModel.hm(i));
        }
        String[] strArr = (String[]) arrayList.toArray(new String[0]);
        LinearLayout hrow = kit.hrow(kit.select(nativeSettings.root, t("db.set.switch", new Object[0]), strArr, strArr, BriefModel.hm(cfg.at), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$brief$67(consumer, (String) obj);
            }
        }));
        ((LinearLayout.LayoutParams) hrow.getLayoutParams()).topMargin = 0;
        nativeSettings.add(hrow);
        nativeSettings.fine(t("db.set.switch.fine", new Object[0]));
        nativeSettings.sec(t("db.set.sections", new Object[0]));
        LinearLayout list = nativeSettings.list();
        for (final String str : BriefModel.SECTIONS) {
            kit.add(list, kit.toggleRow(t("db.sec." + str, new Object[0]), null, !cfg.off.contains(str), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda31
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrLayout.lambda$brief$68(str, consumer, (Boolean) obj);
                }
            }));
        }
        kit.add(nativeSettings.list(), kit.toggleRow(t("db.set.speak", new Object[0]), null, cfg.speak, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda32
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrLayout.lambda$brief$69(consumer, (Boolean) obj);
            }
        }));
        String str2 = Store.get(Profile.KEY, "");
        kit.add(nativeSettings.list(), kit.row(null, 0, t("you.row", new Object[0]), str2.isEmpty() ? t("you.none", new Object[0]) : str2.toUpperCase(Locale.getDefault()), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                ScrLayout.lambda$brief$70(NativeSettings.this);
            }
        }, new View[0]));
        nativeSettings.fine(t("db.set.fine", new Object[0]));
        nativeSettings.add(kit.hrow(kit.btn(t("db.title", new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.be.openApp("Brief");
            }
        })));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$brief$67(Consumer consumer, String str) {
        BriefModel.Cfg cfg = BriefModel.cfg(Store.get("aos.brief"));
        cfg.at = BriefModel.minutes(str);
        consumer.accept(cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$brief$68(String str, Consumer consumer, Boolean bool) {
        BriefModel.Cfg cfg = BriefModel.cfg(Store.get("aos.brief"));
        if (bool.booleanValue()) {
            cfg.off.remove(str);
        } else {
            cfg.off.add(str);
        }
        consumer.accept(cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$brief$69(Consumer consumer, Boolean bool) {
        BriefModel.Cfg cfg = BriefModel.cfg(Store.get("aos.brief"));
        cfg.speak = bool.booleanValue();
        consumer.accept(cfg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$brief$70(NativeSettings nativeSettings) {
        FrameLayout frameLayout = nativeSettings.root;
        Theme theme = nativeSettings.th;
        Backend backend = nativeSettings.be;
        Objects.requireNonNull(nativeSettings);
        YouSheet.show(frameLayout, theme, backend, new NativeSettings$$ExternalSyntheticLambda25(nativeSettings));
    }

    static String[] zones() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("UTC");
        for (String str : TimeZone.getAvailableIDs()) {
            if (str.matches("^(Africa|America|Antarctica|Asia|Atlantic|Australia|Europe|Indian|Pacific)/[A-Za-z_\\-/]+$")) {
                arrayList.add(str);
            }
        }
        Collections.sort(arrayList.subList(1, arrayList.size()));
        return (String[]) arrayList.toArray(new String[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void clocks(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        List<String> clocks = SettingsModel.clocks(Store.get("aos.clocks"));
        String[] zones = zones();
        String[] strArr = new String[zones.length + 1];
        String[] strArr2 = new String[zones.length + 1];
        strArr[0] = "";
        strArr2[0] = "—";
        int i = 0;
        while (i < zones.length) {
            int i2 = i + 1;
            strArr[i2] = zones[i];
            strArr2[i2] = zones[i].replace('_', ' ');
            i = i2;
        }
        nativeSettings.add(nativeSettings.hd(NativeSettings.up(t("clocks.title", new Object[0])), t("clocks.sub", new Object[0])));
        final int i3 = 0;
        while (i3 < 3) {
            int i4 = i3 + 1;
            nativeSettings.sec(t("clocks.n", "n", Integer.valueOf(i4)));
            LinearLayout hrow = kit.hrow(kit.select(nativeSettings.root, t("clocks.n", "n", Integer.valueOf(i4)), strArr2, strArr, i3 < clocks.size() ? clocks.get(i3) : "", new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrLayout$$ExternalSyntheticLambda52
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrLayout.lambda$clocks$72(i3, nativeSettings, (String) obj);
                }
            }));
            ((LinearLayout.LayoutParams) hrow.getLayoutParams()).topMargin = 0;
            nativeSettings.add(hrow);
            i3 = i4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$clocks$72(int i, NativeSettings nativeSettings, String str) {
        Store.set("aos.clocks", SettingsModel.clockSet(Store.get("aos.clocks"), i, str));
        nativeSettings.paint();
    }

    static List<String> list(String... strArr) {
        return new ArrayList(Arrays.asList(strArr));
    }

    static String lc(String str) {
        return str.toLowerCase(Locale.ROOT);
    }
}
