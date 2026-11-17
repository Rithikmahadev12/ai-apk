package com.jarvis.assist.aosui.settings;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.GoogleLink;
import com.jarvis.assist.aosui.ai.Keys;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.GoogleAuth;
import com.jarvis.assist.aosui.core.Memory;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.ui.Kit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class ScrData {
    static final String[][] HMET = {new String[]{"hr", "Heart rate"}, new String[]{"rhr", "Resting HR"}, new String[]{"hrv", "HRV"}, new String[]{"steps", "Steps"}, new String[]{"kcal", "Active kcal"}, new String[]{"sleep", "Sleep"}, new String[]{"spo2", "SpO2"}, new String[]{"temp", "Temperature"}, new String[]{"temp_dev", "temp Δ"}, new String[]{"recovery", "Recovery"}, new String[]{"strain", "Strain"}, new String[]{"readiness", "Readiness"}};

    private ScrData() {
    }

    static String t(String str, Object... objArr) {
        return NativeSettings.t(str, objArr);
    }

    static String up(String str) {
        return NativeSettings.up(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void google(final NativeSettings nativeSettings) {
        final Kit kit = nativeSettings.k;
        nativeSettings.add(nativeSettings.hd("Google", t("gg.sub", new Object[0])));
        if (!GoogleLink.configured()) {
            nativeSettings.add(kit.note("M10.5 2.5a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM8.3 7.7L2.5 13.5M4 12l1.5 1.5", kit.ALT, t("gg.notcfg", new Object[0]), t("gg.notcfg.p", new Object[0]), null, true));
            return;
        }
        boolean connected = GoogleLink.connected(nativeSettings.c);
        JSONObject J = NativeSettings.J(Store.get(GoogleAuth.CACHE, "{}"));
        if (!connected) {
            nativeSettings.add(kit.note("M10.5 2.5a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM8.3 7.7L2.5 13.5M4 12l1.5 1.5", kit.ACC, t("gg.off", new Object[0]), t("gg.p", new Object[0]), null, true));
            nativeSettings.add(kit.hrow(kit.btn(t("gg.connect", new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    GoogleLink.signIn(nativeSettings.c, new GoogleLink.Done() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda21
                        @Override // com.jarvis.assist.aosui.GoogleLink.Done
                        public final void done(boolean z, String str) {
                            ScrData.lambda$google$1(nativeSettings, kit, z, str);
                        }
                    });
                }
            })));
            nativeSettings.fine(t("gg.fine", new Object[0]));
            return;
        }
        JSONObject optJSONObject = J.optJSONObject("gmail");
        nativeSettings.sec("GMAIL");
        LinearLayout list = nativeSettings.list();
        kit.add(list, kit.row(null, 0, t("gg.unread", new Object[0]), (optJSONObject == null || optJSONObject.optInt("unread", -1) < 0) ? "—" : String.valueOf(optJSONObject.optInt("unread")), false, null, new View[0]));
        JSONArray optJSONArray = optJSONObject == null ? null : optJSONObject.optJSONArray("top");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM HH:mm", Locale.getDefault());
        String str = " · ";
        String str2 = "at";
        if (optJSONArray != null) {
            int i = 0;
            while (i < optJSONArray.length()) {
                JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                kit.add(list, kit.row(null, 0, optJSONObject2.optString("subject"), optJSONObject2.optString("from").toUpperCase(Locale.ROOT) + str + simpleDateFormat.format(new Date(optJSONObject2.optLong(str2))), false, null, new View[0]));
                i++;
                str = str;
                str2 = str2;
                optJSONArray = optJSONArray;
            }
        }
        String str3 = str;
        String str4 = str2;
        nativeSettings.sec(t("gg.drive", new Object[0]));
        LinearLayout list2 = nativeSettings.list();
        JSONArray optJSONArray2 = J.optJSONArray("drive");
        if (optJSONArray2 == null || optJSONArray2.length() == 0) {
            kit.add(list2, kit.row(null, 0, "—", null, false, null, new View[0]));
        } else {
            for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                JSONObject optJSONObject3 = optJSONArray2.optJSONObject(i2);
                kit.add(list2, kit.row(null, 0, optJSONObject3.optString("name"), optJSONObject3.optString(str4).replace('T', ' ').replaceAll("\\..*", "") + str3 + optJSONObject3.optString("mime").replaceAll(".*[./]", "").toUpperCase(Locale.ROOT), false, null, new View[0]));
            }
        }
        nativeSettings.add(kit.hrow(kit.btn(t("gg.refresh", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrData.lambda$google$3(nativeSettings);
                    }
                });
            }
        }), kit.btn(t("gg.disconnect", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                ScrData.lambda$google$5(nativeSettings);
            }
        })));
        nativeSettings.fine(t("gg.fine", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$google$0(Kit kit, NativeSettings nativeSettings, String str) {
        kit.toast(nativeSettings.root, "GOOGLE · " + str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$google$1(final NativeSettings nativeSettings, final Kit kit, boolean z, final String str) {
        if (z) {
            GoogleLink.refresh(nativeSettings.c);
        }
        nativeSettings.root.post(new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                ScrData.lambda$google$0(kit, nativeSettings, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$google$3(NativeSettings nativeSettings) {
        GoogleLink.refresh(nativeSettings.c);
        FrameLayout frameLayout = nativeSettings.root;
        Objects.requireNonNull(nativeSettings);
        frameLayout.post(new NativeSettings$$ExternalSyntheticLambda25(nativeSettings));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$google$5(NativeSettings nativeSettings) {
        GoogleLink.disconnect(nativeSettings.c);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static String srcName(String str) {
        char c;
        switch (str.hashCode()) {
            case 3323:
                if (str.equals("hc")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 3731:
                if (str.equals("uh")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 3422869:
                if (str.equals("oura")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 93029210:
                if (str.equals("apple")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 113107487:
                if (str.equals("whoop")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return "Health Connect";
            case 1:
                return "WHOOP";
            case 2:
                return "Oura";
            case 3:
                return "Ultrahuman";
            case 4:
                return "Apple Health";
            default:
                return str;
        }
    }

    static String when(JSONObject jSONObject) {
        long optLong = jSONObject == null ? 0L : jSONObject.optLong("sync");
        return optLong > 0 ? t("ns.hs.sync", "t", up(new SimpleDateFormat("d MMM HH:mm", Locale.getDefault()).format(new Date(optLong)))) : "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String hsum(NativeSettings nativeSettings) {
        String healthSources = nativeSettings.be.healthSources();
        JSONObject J = (healthSources == null || healthSources.isEmpty()) ? null : NativeSettings.J(healthSources);
        if (J == null || J.length() == 0) {
            return NativeSettings.J(nativeSettings.be.permState()).optBoolean("health") ? t("ns.hs.hcon", new Object[0]) : t("ns.hs.none", new Object[0]);
        }
        ArrayList arrayList = new ArrayList();
        for (String str : SettingsModel.HSRCS) {
            JSONObject optJSONObject = J.optJSONObject(str);
            if (optJSONObject != null && optJSONObject.optBoolean("connected")) {
                arrayList.add(up(srcName(str)));
            }
        }
        return arrayList.isEmpty() ? t("ns.hs.none", new Object[0]) : String.join(" · ", arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void weather(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        String[] wx = SettingsModel.wx(Store.get("aos.wxcfg"));
        JSONObject J = NativeSettings.J(nativeSettings.be.weatherStatus());
        nativeSettings.add(nativeSettings.hd(t("ns.wx.h", new Object[0]), t("ns.wx.sub", new Object[0])));
        nativeSettings.sec(t("ns.wx.src", new Object[0]));
        LinearLayout list = nativeSettings.list();
        String[] strArr = SettingsModel.WXS;
        int length = strArr.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                nativeSettings.sec(t("ns.wx.units", new Object[0]));
                nativeSettings.add(kit.seg(new String[]{"°C", "°F"}, new String[]{"c", "f"}, wx[1], t("ns.wx.units", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda16
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ScrData.lambda$weather$7(nativeSettings, (String) obj);
                    }
                }));
                nativeSettings.sec(t("ns.wx.keys", new Object[0]));
                wxKey(nativeSettings, J, "owm", "OpenWeatherMap", t("ns.wx.owm.from", new Object[0]));
                wxKey(nativeSettings, J, "wapi", "WeatherAPI.com", "weatherapi.com");
                nativeSettings.add(kit.note("M4 7.5h8v6.5H4zM5.5 7.5V5a2.5 2.5 0 0 1 5 0v2.5", kit.ACC, t("ns.wx.sent.h", new Object[0]), t("ns.wx.sent.p", new Object[0]), null, true));
                return;
            }
            final String str = strArr[i];
            if ((!"owm".equals(str) && !"wapi".equals(str)) || (J.optJSONObject(str) != null && J.optJSONObject(str).optBoolean("hasKey"))) {
                z = false;
            }
            boolean equals = str.equals(wx[0]);
            View add = kit.add(list, kit.row(null, 0, t("ns.wx.s." + str, new Object[0]), up(t("ns.wx.d." + str, new Object[0])) + (z ? " · " + t("ns.wx.addkey", new Object[0]) : ""), false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    ScrData.lambda$weather$6(nativeSettings, str);
                }
            }, equals ? kit.iconView(Kit.CHECK, kit.ACC, 14.0f) : null));
            if (equals) {
                add.setContentDescription(String.valueOf(add.getContentDescription()) + ", selected");
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$weather$6(NativeSettings nativeSettings, String str) {
        wxSet(nativeSettings, "src", str);
        Store.set("aos.wx", "");
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$weather$7(NativeSettings nativeSettings, String str) {
        wxSet(nativeSettings, "units", str);
        nativeSettings.paint();
    }

    static void wxSet(NativeSettings nativeSettings, String str, String str2) {
        nativeSettings.setBoth("aos.wxcfg", SettingsModel.wxSet(Store.get("aos.wxcfg"), str, str2));
    }

    static void wxKey(final NativeSettings nativeSettings, JSONObject jSONObject, final String str, String str2, String str3) {
        Kit kit = nativeSettings.k;
        JSONObject jSONObject2 = jSONObject.optJSONObject(str) == null ? new JSONObject() : jSONObject.optJSONObject(str);
        boolean optBoolean = jSONObject2.optBoolean("hasKey");
        LinearLayout card = kit.card(true, false);
        ScrAssist.cardHead(nativeSettings, card, str2, jSONObject2, t("ns.nokey", new Object[0]));
        TextView fine = kit.fine(t("ns.wx.free", "from", str3));
        ((LinearLayout.LayoutParams) fine.getLayoutParams()).topMargin = nativeSettings.px(4.0f);
        card.addView(fine);
        final TextView msg = kit.msg("");
        final String url = Keys.url(str);
        final BiConsumer biConsumer = new BiConsumer() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ScrData.lambda$wxKey$9(msg, nativeSettings, str, (String) obj, (TextView) obj2);
            }
        };
        TextView btn = kit.btn(t("ns.paste", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ScrData.lambda$wxKey$10(nativeSettings, biConsumer);
            }
        });
        btn.setContentDescription(t("ns.paste", new Object[0]) + " " + str2);
        View[] viewArr = new View[3];
        viewArr[0] = btn;
        viewArr[1] = !url.isEmpty() ? kit.btn(t("ns.getkey", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                nativeSettings.be.url(url);
            }
        }) : null;
        viewArr[2] = optBoolean ? kit.btn(t("ns.remove", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ScrData.lambda$wxKey$12(nativeSettings, str);
            }
        }) : null;
        card.addView(kit.hrow(viewArr));
        final EditText input = kit.input("", optBoolean ? t("ns.replace", new Object[0]) : t("ns.typekey", new Object[0]), true, true, 0);
        input.setContentDescription(str2 + " " + t("ns.key", new Object[0]));
        final TextView btn2 = kit.btn(t("ns.verify", new Object[0]), false, true, null);
        btn2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrData.lambda$wxKey$13(input, btn2, biConsumer, view);
            }
        });
        card.addView(kit.hrow(Kit.grow(input), btn2));
        card.addView(msg);
        nativeSettings.add(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$wxKey$8(TextView textView, NativeSettings nativeSettings, String str, TextView textView2, String str2) {
        JSONObject J = NativeSettings.J(str2);
        if (!J.optBoolean("ok")) {
            if (textView != null) {
                textView.setText(t("ns.verify", new Object[0]));
            }
            textView2.setText(up(J.optString("error", t("ns.keyfail", new Object[0]))));
        } else {
            Store.set("aos.wx", "");
            if (textView == null) {
                nativeSettings.toast(t("ns.wx.connected", "l4", Keys.last4(str)));
            }
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$wxKey$9(final TextView textView, final NativeSettings nativeSettings, String str, final String str2, final TextView textView2) {
        textView.setText(t("ns.checking.l4", "l4", Keys.last4(str2)));
        nativeSettings.be.weatherSetKey(str, str2, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrData.lambda$wxKey$8(textView2, nativeSettings, str2, textView, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$wxKey$10(NativeSettings nativeSettings, BiConsumer biConsumer) {
        String looks = Keys.looks(nativeSettings.be.clip("token"), "token");
        if (looks.isEmpty()) {
            nativeSettings.toast(t("ns.paste.none", new Object[0]));
        } else {
            biConsumer.accept(looks, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$wxKey$12(NativeSettings nativeSettings, String str) {
        nativeSettings.be.weatherClearKey(str);
        Store.set("aos.wx", "");
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$wxKey$13(EditText editText, TextView textView, BiConsumer biConsumer, View view) {
        String trim = editText.getText().toString().trim();
        if (trim.isEmpty()) {
            return;
        }
        textView.setText(t("ns.checking", new Object[0]));
        biConsumer.accept(trim, textView);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void health(final NativeSettings nativeSettings) {
        int i;
        String t;
        final Kit kit = nativeSettings.k;
        JSONObject J = NativeSettings.J(nativeSettings.be.healthSources());
        JSONObject jSONObject = J.optJSONObject("pref") == null ? new JSONObject() : J.optJSONObject("pref");
        double[] goals = SettingsModel.goals(nativeSettings.be.get("health.goals"));
        int i2 = 0;
        nativeSettings.add(nativeSettings.hd(t("ns.hs.h", new Object[0]), t("ns.hs.sub", new Object[0])));
        nativeSettings.add(kit.note("M4 7.5h8v6.5H4zM5.5 7.5V5a2.5 2.5 0 0 1 5 0v2.5", kit.ACC, t("ns.hs.stays.h", new Object[0]), t("ns.hs.stays.p", new Object[0]), null, true));
        nativeSettings.sec(t("ns.hs.srcs", new Object[0]));
        LinearLayout list = nativeSettings.list();
        String[] strArr = SettingsModel.HSRCS;
        int length = strArr.length;
        int i3 = 0;
        while (true) {
            i = 1;
            if (i3 >= length) {
                break;
            }
            final String str = strArr[i3];
            JSONObject jSONObject2 = J.optJSONObject(str) == null ? new JSONObject() : J.optJSONObject(str);
            boolean optBoolean = jSONObject2.optBoolean("connected");
            boolean z = optBoolean && !jSONObject2.optString("error").isEmpty();
            if (!optBoolean) {
                t = t("ns.hs.d." + str, new Object[0]);
            } else if (z) {
                t = up(jSONObject2.optString("error"));
            } else if (when(jSONObject2).isEmpty()) {
                t = t("ns.connected", new Object[0]) + (jSONObject2.optString("last4").isEmpty() ? "" : " · ••••" + jSONObject2.optString("last4"));
            } else {
                t = when(jSONObject2);
            }
            String str2 = t;
            int i4 = optBoolean ? kit.ACC : kit.INK;
            String srcName = srcName(str);
            Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    nativeSettings.guide(str);
                }
            };
            TextView chip = optBoolean ? kit.chip(z ? t("ns.hs.check", new Object[0]) : "ON", z ? 0 : kit.GOOD) : null;
            int i5 = i3;
            LinearLayout row = kit.row("M8 13.5S2 9.8 2 5.8A3 3 0 0 1 8 4.6a3 3 0 0 1 6 1.2c0 4-6 7.7-6 7.7z", i4, srcName, str2, true, runnable, chip);
            if (z) {
                View findViewWithTag = row.findViewWithTag("sub");
                if (findViewWithTag instanceof TextView) {
                    ((TextView) findViewWithTag).setTextColor(kit.ALT);
                }
            }
            kit.add(list, row);
            i3 = i5 + 1;
        }
        nativeSettings.sec(t("ns.hs.which", new Object[0]));
        LinearLayout list2 = nativeSettings.list();
        int length2 = SettingsModel.HSRCS.length + 1;
        String[] strArr2 = new String[length2];
        String[] strArr3 = new String[length2];
        strArr2[0] = t("ns.hs.auto", new Object[0]);
        strArr3[0] = "";
        int i6 = 0;
        while (i6 < SettingsModel.HSRCS.length) {
            int i7 = i6 + 1;
            strArr3[i7] = SettingsModel.HSRCS[i6];
            strArr2[i7] = srcName(SettingsModel.HSRCS[i6]);
            i6 = i7;
        }
        String[][] strArr4 = HMET;
        int length3 = strArr4.length;
        int i8 = 0;
        while (i8 < length3) {
            final String[] strArr5 = strArr4[i8];
            String t2 = t("ns.hm." + strArr5[i2], new Object[i2]);
            View[] viewArr = new View[1];
            int i9 = i2;
            viewArr[i9] = kit.select(nativeSettings.root, t2, strArr2, strArr3, jSONObject.optString(strArr5[i9], ""), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrData.lambda$health$15(nativeSettings, strArr5, (String) obj);
                }
            });
            kit.add(list2, kit.row(null, 0, t2, null, false, null, viewArr));
            i8++;
            i = 1;
            strArr2 = strArr2;
            i2 = i9;
            String str3 = str3;
            strArr3 = strArr3;
        }
        int i10 = i2;
        int i11 = i;
        nativeSettings.fine(t("ns.hs.auto.fine", new Object[i10]));
        nativeSettings.sec(t("ns.hs.goals", new Object[i10]));
        LinearLayout list3 = nativeSettings.list();
        Object[][] objArr = {new Object[]{"steps", t("ns.goal.steps", new Object[i10]), 1000, 50000}, new Object[]{"sleep", t("ns.goal.sleep", new Object[i10]), 4, 12}, new Object[]{"kcal", t("ns.goal.kcal", new Object[i10]), 100, 3000}};
        for (int i12 = 0; i12 < 3; i12++) {
            final String str4 = (String) objArr[i12][0];
            double d = goals[i12];
            final EditText input = kit.input(d == Math.rint(d) ? String.valueOf((long) d) : String.valueOf(d), (String) objArr[i12][i11], false, true, "sleep".equals(str4) ? 8194 : 2);
            input.setLayoutParams(new LinearLayout.LayoutParams(nativeSettings.px(110.0f), -2));
            input.setGravity(8388613);
            final Runnable runnable2 = new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    ScrData.lambda$health$16(input, nativeSettings, str4);
                }
            };
            input.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda13
                @Override // android.view.View.OnFocusChangeListener
                public final void onFocusChange(View view, boolean z2) {
                    ScrData.lambda$health$17(runnable2, kit, nativeSettings, view, z2);
                }
            });
            input.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda14
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView, int i13, KeyEvent keyEvent) {
                    return ScrData.lambda$health$18(runnable2, textView, i13, keyEvent);
                }
            });
            String str5 = (String) objArr[i12][i11];
            View[] viewArr2 = new View[i11];
            viewArr2[0] = input;
            kit.add(list3, kit.row(null, 0, str5, null, false, null, viewArr2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$health$15(NativeSettings nativeSettings, String[] strArr, String str) {
        JSONObject optJSONObject = NativeSettings.J(nativeSettings.be.healthSources()).optJSONObject("pref");
        if (optJSONObject == null) {
            optJSONObject = new JSONObject();
        }
        try {
            if (str.isEmpty()) {
                optJSONObject.remove(strArr[0]);
            } else {
                optJSONObject.put(strArr[0], str);
            }
        } catch (Exception e) {
        }
        nativeSettings.be.healthSetPref(optJSONObject.toString());
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$health$16(EditText editText, NativeSettings nativeSettings, String str) {
        try {
            String goalSet = SettingsModel.goalSet(nativeSettings.be.get("health.goals"), str, Double.parseDouble(editText.getText().toString().trim()));
            if (goalSet != null) {
                nativeSettings.be.set("health.goals", goalSet);
            }
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$health$17(Runnable runnable, Kit kit, NativeSettings nativeSettings, View view, boolean z) {
        if (!z) {
            runnable.run();
        }
        view.setBackground(Kit.pill(0, z ? kit.ACC : kit.LINE2, nativeSettings.dn, nativeSettings.dn * 6.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$health$18(Runnable runnable, TextView textView, int i, KeyEvent keyEvent) {
        runnable.run();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:38:0x05bf  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x05e5  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x05ed  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x05f5  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0614  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x064f  */
    /* JADX WARN: Removed duplicated region for block: B:64:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0617  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x05ff  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x05e0  */
    /* JADX WARN: Type inference failed for: r13v22 */
    /* JADX WARN: Type inference failed for: r13v23 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void guide(final com.jarvis.assist.aosui.settings.NativeSettings r29) {
        /*
            Method dump skipped, instructions count: 1718
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.settings.ScrData.guide(com.jarvis.assist.aosui.settings.NativeSettings):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$guide$24(Kit kit, TextView textView, TextView textView2, NativeSettings nativeSettings, String str) {
        JSONObject J = NativeSettings.J(str);
        if (J.has("progress")) {
            kit.enable(textView, false);
            textView.setText(t("ns.hg.apple.importing", new Object[0]));
            textView2.setText(t("ns.hg.apple.reading", "p", Integer.valueOf(J.optInt("progress"))));
        } else if (J.optBoolean("ok")) {
            nativeSettings.toast(t("ns.hg.apple.done", new Object[0]));
            nativeSettings.paint();
        } else {
            kit.enable(textView, true);
            textView.setText(t("ns.hg.apple.choose", new Object[0]));
            textView2.setText(up(J.optString("error", t("ns.hg.apple.fail", new Object[0]))));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$guide$27(NativeSettings nativeSettings, String str) {
        nativeSettings.be.healthDisconnect(str);
        nativeSettings.paint();
    }

    static LinearLayout card(NativeSettings nativeSettings, String str, int i, String str2, String str3) {
        Kit kit = nativeSettings.k;
        LinearLayout card = kit.card(true, false);
        LinearLayout linearLayout = new LinearLayout(nativeSettings.c);
        linearLayout.setGravity(16);
        linearLayout.addView(kit.iconView(str, i, 18.0f));
        TextView text = kit.text(str2, Fonts.RAJ_600, 15.0f, 0.0f, kit.INK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.leftMargin = nativeSettings.px(10.0f);
        linearLayout.addView(text, layoutParams);
        if (str3 != null) {
            linearLayout.addView(kit.mono(str3, 8.0f, kit.ACC));
        }
        card.addView(linearLayout);
        return card;
    }

    static TextView fineIn(NativeSettings nativeSettings, String str) {
        TextView fine = nativeSettings.k.fine(str);
        ((LinearLayout.LayoutParams) fine.getLayoutParams()).setMargins(0, nativeSettings.px(4.0f), 0, nativeSettings.px(4.0f));
        return fine;
    }

    static View code(final NativeSettings nativeSettings, final String str) {
        Kit kit = nativeSettings.k;
        LinearLayout linearLayout = new LinearLayout(nativeSettings.c);
        linearLayout.setGravity(16);
        linearLayout.setPadding(nativeSettings.px(10.0f), nativeSettings.px(6.0f), nativeSettings.px(6.0f), nativeSettings.px(6.0f));
        linearLayout.setBackground(Kit.pill(0, kit.LINE2, nativeSettings.dn, nativeSettings.dn * 6.0f));
        TextView text = kit.text(str, Fonts.MONO, 12.0f, 0.0f, kit.INK);
        text.setTextIsSelectable(true);
        linearLayout.addView(text, new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView btn = kit.btn(t("ns.copy", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                ScrData.lambda$code$28(nativeSettings, str);
            }
        });
        btn.setContentDescription(t("ns.copy", new Object[0]) + " " + str);
        linearLayout.addView(btn);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, nativeSettings.px(6.0f), 0, nativeSettings.px(6.0f));
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$code$28(NativeSettings nativeSettings, String str) {
        nativeSettings.be.copy(str);
        nativeSettings.toast(t("ns.copied", new Object[0]));
    }

    static void steps(NativeSettings nativeSettings, LinearLayout linearLayout, Object[] objArr) {
        Kit kit = nativeSettings.k;
        int i = 0;
        while (i < objArr.length) {
            LinearLayout linearLayout2 = new LinearLayout(nativeSettings.c);
            linearLayout2.setOrientation(0);
            int i2 = i + 1;
            TextView text = kit.text(i2 + ".", Fonts.RAJ_400, 13.0f, 0.0f, kit.DIM);
            text.setMinWidth(nativeSettings.px(20.0f));
            text.setImportantForAccessibility(2);
            linearLayout2.addView(text);
            LinearLayout linearLayout3 = new LinearLayout(nativeSettings.c);
            linearLayout3.setOrientation(1);
            for (Object obj : objArr[i] instanceof Object[] ? (Object[]) objArr[i] : new Object[]{objArr[i]}) {
                if (obj instanceof View) {
                    linearLayout3.addView((View) obj);
                } else {
                    TextView text2 = kit.text("", Fonts.RAJ_400, 13.0f, 0.0f, kit.DIM);
                    text2.setText(bold(nativeSettings, String.valueOf(obj)));
                    linearLayout3.addView(text2);
                }
            }
            linearLayout2.addView(linearLayout3, new LinearLayout.LayoutParams(0, -2, 1.0f));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(0, nativeSettings.px(3.0f), 0, nativeSettings.px(3.0f));
            linearLayout.addView(linearLayout2, layoutParams);
            i = i2;
        }
    }

    static CharSequence bold(NativeSettings nativeSettings, String str) {
        int indexOf;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int i = 0;
        while (true) {
            int indexOf2 = str.indexOf("<b>", i);
            if (indexOf2 >= 0 && (indexOf = str.indexOf("</b>", indexOf2)) >= 0) {
                spannableStringBuilder.append((CharSequence) str, i, indexOf2);
                int length = spannableStringBuilder.length();
                spannableStringBuilder.append((CharSequence) str, indexOf2 + 3, indexOf);
                spannableStringBuilder.setSpan(new StyleSpan(1), length, spannableStringBuilder.length(), 33);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(nativeSettings.k.INK), length, spannableStringBuilder.length(), 33);
                i = indexOf + 4;
            }
        }
        spannableStringBuilder.append((CharSequence) str.substring(i));
        return spannableStringBuilder;
    }

    static void field(final NativeSettings nativeSettings, LinearLayout linearLayout, final Map<String, EditText> map, final String str, final String str2, String str3, boolean z) {
        Kit.KeyField keyField = nativeSettings.k.keyField(str3, z, t("ns.paste", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrData.lambda$field$29(nativeSettings, str2, str, map, (Kit.KeyField) obj);
            }
        });
        if ("email".equals(str2)) {
            keyField.in.setInputType(33);
        }
        map.put(str2, keyField.in);
        linearLayout.addView(keyField.row);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$field$29(NativeSettings nativeSettings, String str, String str2, Map map, Kit.KeyField keyField) {
        String looks = Keys.looks(nativeSettings.be.clip("token"), "token");
        if (looks.isEmpty()) {
            nativeSettings.toast(t("ns.paste.none", new Object[0]));
            return;
        }
        keyField.in.setText(looks);
        if ("token".equals(str)) {
            connect(nativeSettings, str2, map, null, null);
        } else {
            nativeSettings.toast(t("ns.pasted", "l4", Keys.last4(looks)));
        }
    }

    static TextView connectBtn(final NativeSettings nativeSettings, final String str, String str2, final Map<String, EditText> map, final TextView textView) {
        final TextView btn = nativeSettings.k.btn(str2, false, false, null);
        btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda20
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrData.connect(nativeSettings, str, map, textView, btn);
            }
        });
        return btn;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void connect(final NativeSettings nativeSettings, String str, Map<String, EditText> map, final TextView textView, final TextView textView2) {
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry<String, EditText> entry : map.entrySet()) {
                String trim = entry.getValue().getText().toString().trim();
                if (!trim.isEmpty()) {
                    jSONObject.put(entry.getKey(), trim);
                }
            }
        } catch (Exception e) {
        }
        if (jSONObject.length() != 0) {
            final String charSequence = textView2 == null ? null : textView2.getText().toString();
            if (textView2 != null) {
                textView2.setText(t("ns.checking", new Object[0]));
            }
            nativeSettings.be.healthConnect(str, jSONObject.toString(), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrData.lambda$connect$31(textView2, charSequence, nativeSettings, textView, (String) obj);
                }
            });
            return;
        }
        nativeSettings.hgMsg = t("ns.hg.fill", new Object[0]);
        if (textView != null) {
            textView.setText(nativeSettings.hgMsg);
        } else {
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$connect$31(TextView textView, String str, NativeSettings nativeSettings, TextView textView2, String str2) {
        JSONObject J = NativeSettings.J(str2);
        if (!J.optString("auth").isEmpty()) {
            if (textView != null) {
                textView.setText(str);
            }
            nativeSettings.hgMsg = t("ns.hg.browser", new Object[0]);
            if (textView2 != null) {
                textView2.setText(nativeSettings.hgMsg);
                return;
            }
            return;
        }
        if (J.optBoolean("ok")) {
            nativeSettings.toast(t("ns.connected", new Object[0]));
            nativeSettings.hgMsg = "";
            nativeSettings.paint();
            return;
        }
        if (textView != null) {
            textView.setText(str);
        }
        nativeSettings.hgMsg = up(J.optString("error", t("ns.hg.didnt", new Object[0])));
        if (textView2 != null) {
            textView2.setText(nativeSettings.hgMsg);
        } else {
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void test(final NativeSettings nativeSettings) {
        nativeSettings.hgBusy = true;
        nativeSettings.hgTest = "";
        nativeSettings.paint();
        final String str = nativeSettings.hgSrc;
        if ("hc".equals(str)) {
            nativeSettings.be.healthTestHc(new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrData.lambda$test$32(nativeSettings, (String) obj);
                }
            });
        } else {
            nativeSettings.be.healthToday2(true, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrData$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrData.lambda$test$33(str, nativeSettings, (String) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$test$32(NativeSettings nativeSettings, String str) {
        nativeSettings.hgBusy = false;
        nativeSettings.hgTest = str;
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$test$33(String str, NativeSettings nativeSettings, String str2) {
        boolean z;
        String t;
        JSONObject J = NativeSettings.J(str2);
        JSONObject jSONObject = J.optJSONObject("by") == null ? new JSONObject() : J.optJSONObject("by");
        ArrayList arrayList = new ArrayList();
        for (String[] strArr : HMET) {
            if (str.equals(jSONObject.optString(strArr[0]))) {
                arrayList.add(up(t("ns.hm." + strArr[0], new Object[0])));
            }
        }
        JSONObject optJSONObject = J.optJSONObject("sources") == null ? null : J.optJSONObject("sources").optJSONObject(str);
        JSONObject optJSONObject2 = NativeSettings.J(nativeSettings.be.healthSources()).optJSONObject(str);
        nativeSettings.hgBusy = false;
        JSONArray optJSONArray = J.optJSONArray("slow");
        if (optJSONArray != null) {
            z = false;
            for (int i = 0; i < optJSONArray.length(); i++) {
                if (str.equals(optJSONArray.optString(i))) {
                    z = true;
                }
            }
        } else {
            z = false;
        }
        if (!arrayList.isEmpty()) {
            t = t("ns.hg.got", "list", String.join(" · ", arrayList)) + (z ? " · " + t("ns.hg.last", new Object[0]) : "");
        } else if (z) {
            t = t("ns.hg.slow", new Object[0]);
        } else if (optJSONObject != null) {
            t = t("ns.hg.nonew", new Object[0]);
        } else {
            t = t("ns.hg.noanswer", "e", (optJSONObject2 == null || optJSONObject2.optString("error").isEmpty()) ? t("ns.hg.tryagain", new Object[0]) : up(optJSONObject2.optString("error")));
        }
        nativeSettings.hgTest = t;
        nativeSettings.paint();
    }
}
