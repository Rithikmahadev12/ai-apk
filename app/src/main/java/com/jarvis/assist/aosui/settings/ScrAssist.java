package com.jarvis.assist.aosui.settings;

import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.ai.Features;
import com.jarvis.assist.aosui.ai.KeyCheck;
import com.jarvis.assist.aosui.ai.KeyFlow;
import com.jarvis.assist.aosui.ai.Keys;
import com.jarvis.assist.aosui.ai.ModelList;
import com.jarvis.assist.aosui.core.BootSeq;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.VoiceSheet;
import com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda9;
import com.jarvis.assist.aosui.ui.Flow;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.ModelPick;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class ScrAssist {
    static final String[][] VLANGS = {new String[]{"en-US", "English (US)"}, new String[]{"en-GB", "English (UK)"}, new String[]{"en-AU", "English (Australia)"}, new String[]{"en-IN", "English (India)"}, new String[]{"th-TH", "ไทย"}, new String[]{"de-DE", "Deutsch"}, new String[]{"fr-FR", "Français"}, new String[]{"es-ES", "Español"}, new String[]{"it-IT", "Italiano"}, new String[]{"pt-BR", "Português (Brasil)"}, new String[]{"nl-NL", "Nederlands"}, new String[]{"ja-JP", "日本語"}, new String[]{"ko-KR", "한국어"}, new String[]{"zh-CN", "中文 (简体)"}, new String[]{"hi-IN", "हिन्दी"}, new String[]{"id-ID", "Bahasa Indonesia"}, new String[]{"vi-VN", "Tiếng Việt"}};
    static final String[] GROUPS = {"ask", "write", "home", "notif", "photo", "health", "plan", "know", "me", "privacy"};

    private ScrAssist() {
    }

    static String t(String str, Object... objArr) {
        return NativeSettings.t(str, objArr);
    }

    static String up(String str) {
        return NativeSettings.up(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void ai(final NativeSettings nativeSettings) {
        int i;
        String optString;
        boolean z;
        String t;
        int i2;
        String str;
        int i3;
        JSONObject jSONObject;
        String str2;
        JSONObject jSONObject2;
        LinearLayout linearLayout;
        final String str3;
        boolean z2;
        int i4;
        JSONObject jSONObject3;
        View spacer;
        char c;
        String t2;
        final Kit kit = nativeSettings.k;
        JSONObject ai = nativeSettings.ai();
        String str4 = "claude";
        JSONObject optJSONObject = ai.optJSONObject("claude") != null ? ai.optJSONObject("claude") : json("hasKey", Boolean.valueOf(ai.optBoolean("hasKey")), "last4", ai.optString("last4"));
        String str5 = "gemini";
        JSONObject opt = opt(ai, "gemini");
        JSONObject opt2 = opt(ai, "openai");
        nativeSettings.add(nativeSettings.hd(t("ns.ai.h", new Object[0]), up(nativeSettings.paLine())));
        Iterator<Features.F> it = Features.LIST.iterator();
        int i5 = 0;
        while (it.hasNext()) {
            if (nativeSettings.F.on(it.next().id)) {
                i5++;
            }
        }
        kit.add(nativeSettings.list(), kit.row("M8 1.5l1.6 4.9L14.5 8l-4.9 1.6L8 14.5l-1.6-4.9L1.5 8l4.9-1.6z", kit.ACC, t("aif.title", new Object[0]), nativeSettings.F.paused() ? t("aif.paused", new Object[0]) : t("aif.sub", "n", Integer.valueOf(i5), "of", Integer.valueOf(Features.LIST.size()), "c", Integer.valueOf(nativeSettings.F.usage().optInt("today"))), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                nativeSettings.m356lambda$navRow$7$comaugmaososettingsNativeSettings("aif");
            }
        }, new View[0]));
        boolean paMode = nativeSettings.paMode();
        JSONObject paInfo = nativeSettings.paInfo();
        if (paInfo.optString("label").isEmpty()) {
            i = 0;
            optString = t("as.pa.generic", new Object[0]);
        } else {
            i = 0;
            optString = paInfo.optString("label");
        }
        nativeSettings.sec(t("set.pa.h", new Object[i]));
        LinearLayout list = nativeSettings.list();
        String paLine = nativeSettings.paLine();
        if (!paMode) {
            z = paMode;
            t = t("set.pa.fallback.sub", "name", optString);
        } else if (paInfo.optString("pkg").isEmpty()) {
            z = paMode;
            t = t("set.pa.cmds", new Object[0]);
        } else {
            z = paMode;
            t = t("set.pa.mode.sub", "name", optString);
        }
        String str6 = "hasKey";
        LinearLayout rowWrap = kit.rowWrap(paLine, t, new View[0]);
        Bitmap dataImg = z ? dataImg(paInfo.optString("icon")) : null;
        if (dataImg != null) {
            ImageView imageView = new ImageView(nativeSettings.c);
            imageView.setImageBitmap(dataImg);
            imageView.setImportantForAccessibility(2);
            imageView.setClipToOutline(true);
            imageView.setOutlineProvider(new ViewOutlineProvider() { // from class: com.jarvis.assist.aosui.settings.ScrAssist.1
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(nativeSettings.px(22.0f), nativeSettings.px(22.0f));
            layoutParams.rightMargin = nativeSettings.px(12.0f);
            i2 = 0;
            rowWrap.addView(imageView, 0, layoutParams);
        } else {
            Kit.IconView iconView = kit.iconView("M8 1.5l1.6 4.9L14.5 8l-4.9 1.6L8 14.5l-1.6-4.9L1.5 8l4.9-1.6z", z ? kit.INK : kit.ACC, 18.0f);
            ((LinearLayout.LayoutParams) iconView.getLayoutParams()).rightMargin = nativeSettings.px(12.0f);
            i2 = 0;
            rowWrap.addView(iconView, 0);
        }
        kit.add(list, rowWrap);
        kit.add(list, kit.toggleRow(t("set.pa.voice", new Object[i2]), t("set.pa.voice.sub", "name", optString), "1".equals(nativeSettings.be.get("ai.pa.voice")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda48
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.be.set("ai.pa.voice", ((Boolean)obj).booleanValue() ? "1" : "");
            }
        }));
        kit.add(list, kit.toggleRow(t("set.pa.fallback", new Object[0]), null, !"0".equals(nativeSettings.be.get("ai.pa.fallback")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda49
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.be.set("ai.pa.fallback", ((Boolean)obj).booleanValue() ? "" : "0");
            }
        }));
        nativeSettings.fine(t("set.pa.cmds", new Object[0]));
        nativeSettings.sec(t("ns.ai.name", new Object[0]));
        int i6 = 2;
        final EditText input = kit.input(nativeSettings.be.get("ai.name"), t("ns.ai.name.ph", new Object[0]), false, false, 8193);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        input.setContentDescription(t("ns.ai.name.ph", new Object[0]));
        input.setImeOptions(6);
        final Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                nativeSettings.be.set("ai.name", input.getText().toString().trim());
            }
        };
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda51
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i7, KeyEvent keyEvent) {
                return ScrAssist.lambda$ai$4(runnable, textView, i7, keyEvent);
            }
        });
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda52
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z3) {
                ScrAssist.lambda$ai$5(runnable, kit, nativeSettings, view, z3);
            }
        });
        nativeSettings.add(input);
        nativeSettings.add(kit.note("M4 7.5h8v6.5H4zM5.5 7.5V5a2.5 2.5 0 0 1 5 0v2.5", kit.ACC, t("ns.ai.keys.h", new Object[0]), t("ns.ai.keys.p", new Object[0]), null, true));
        keyCard(nativeSettings, "claude", "Anthropic Claude", optJSONObject, SettingsModel.MODELS_CLAUDE);
        keyCard(nativeSettings, "gemini", "Google Gemini", opt, SettingsModel.MODELS_GEMINI);
        oaiCard(nativeSettings, opt2);
        nativeSettings.sec(t("ns.ai.order", new Object[0]));
        final List<String> order = SettingsModel.order(nativeSettings.be.aiHasKey());
        LinearLayout list2 = nativeSettings.list();
        for (final String str7 : SettingsModel.allProviders(order)) {
            boolean contains = order.contains(str7);
            int indexOf = order.indexOf(str7);
            JSONObject jSONObject4 = str4.equals(str7) ? optJSONObject : str5.equals(str7) ? opt : opt2;
            String pname = pname(str7);
            if (!contains || indexOf <= 0) {
                str = str4;
                i3 = i6;
                jSONObject = optJSONObject;
                str2 = str5;
                jSONObject2 = opt2;
                linearLayout = list2;
                str3 = str7;
                z2 = contains;
                i4 = indexOf;
                jSONObject3 = jSONObject4;
                spacer = ScrLayout.spacer(nativeSettings);
            } else {
                i3 = i6;
                // kit = kit; // JADX artifact removed
                str = str4;
                str3 = str7;
                JSONObject jSONObject5 = opt2;
                jSONObject3 = jSONObject4;
                linearLayout = list2;
                jSONObject = optJSONObject;
                z2 = contains;
                str2 = str5;
                i4 = indexOf;
                jSONObject2 = jSONObject5;
                spacer = kit.circle(Kit.UP, t("ns.up", "name", pname), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda53
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrAssist.lambda$ai$6(order, str7, nativeSettings);
                    }
                });
            }
            View view = spacer;
            View spacer2 = (!z2 || i4 >= order.size() + (-1)) ? ScrLayout.spacer(nativeSettings) : kit.circle(Kit.DOWN, t("ns.down", "name", pname), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    ScrAssist.lambda$ai$7(order, str3, nativeSettings);
                }
            });
            String str8 = str6;
            if (jSONObject3.optBoolean(str8)) {
                c = 0;
                t2 = t("ns.ready", new Object[0]);
            } else {
                c = 0;
                t2 = t("ns.notset", new Object[0]);
            }
            String str9 = t2;
            View[] viewArr = new View[3];
            viewArr[c] = view;
            viewArr[1] = spacer2;
            viewArr[i3] = kit.toggle(z2, t("ns.use", "name", pname), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda55
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.lambda$ai$8(nativeSettings, str3, (Boolean) obj);
                }
            });
            List<String> list3 = order;
            LinearLayout row = kit.row(null, 0, pname, str9, false, null, viewArr);
            row.addView(kit.num(z2 ? String.valueOf(i4 + 1) : "–"), 0);
            ((LinearLayout.LayoutParams) row.getChildAt(1).getLayoutParams()).leftMargin = nativeSettings.px(12.0f);
            kit.add(linearLayout, row);
            // order = list3; // replaced:
            final java.util.List<String> order2 = list3;
            list2 = linearLayout;
            i6 = i3;
            str4 = str;
            optJSONObject = jSONObject;
            str5 = str2;
            opt2 = jSONObject2;
            str6 = str8;
        }
        int i7 = i6;
        String str10 = str6;
        nativeSettings.fine(t("ns.ai.order.fine", new Object[0]));
        int[] aimem = SettingsModel.aimem(Store.get("aos.aimem"));
        nativeSettings.sec(t("ns.mem", new Object[0]));
        LinearLayout list4 = nativeSettings.list();
        kit.add(list4, kit.toggleRow(t("ns.mem.on", new Object[0]), null, aimem[0] == 1, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda56
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Store.set("aos.aimem", SettingsModel.aimemSet(Store.get("aos.aimem"), (Boolean) obj, null));
            }
        }));
        final EditText input2 = kit.input(String.valueOf(aimem[1]), t("ns.mem.turns.a11y", new Object[0]), false, true, 2);
        input2.setLayoutParams(new LinearLayout.LayoutParams(nativeSettings.px(80.0f), -2));
        input2.setImeOptions(6);
        final Runnable runnable2 = new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                ScrAssist.lambda$ai$10(input2);
            }
        };
        input2.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda42
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i8, KeyEvent keyEvent) {
                return ScrAssist.lambda$ai$11(runnable2, textView, i8, keyEvent);
            }
        });
        input2.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda43
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view2, boolean z3) {
                ScrAssist.lambda$ai$12(runnable2, view2, z3);
            }
        });
        kit.add(list4, kit.row(null, 0, t("ns.mem.turns", new Object[0]), null, false, null, input2));
        int length = NativeSettings.JA(Store.get("aos.chats")).length();
        final boolean[] zArr = {false};
        final LinearLayout row2 = kit.row(null, 0, t("ns.mem.clear", new Object[0]), null, false, null, kit.mono(t("ns.mem.saved", "n", Integer.valueOf(length)), 8.5f, kit.DIM));
        final TextView textView = (TextView) row2.getChildAt(row2.getChildCount() - 1);
        row2.setClickable(true);
        row2.setFocusable(true);
        row2.setContentDescription(t("ns.mem.clear", new Object[0]) + ", " + t("ns.mem.saved", "n", Integer.valueOf(length)));
        row2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda44
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ScrAssist.lambda$ai$13(zArr, nativeSettings, textView, row2, view2);
            }
        });
        kit.add(list4, row2);
        nativeSettings.sec(t("ns.notes", new Object[0]));
        LinearLayout list5 = nativeSettings.list();
        List<String> notes = SettingsModel.notes(Store.get("aos.mem"));
        if (notes.isEmpty()) {
            ((LinearLayout) kit.add(list5, kit.row(null, 0, "", null, false, null, new View[0]))).addView(kit.mono(t("ns.notes.none", new Object[0]), 8.5f, kit.DIM), 0);
        }
        for (int i8 = 0; i8 < notes.size(); i8++) {
            LinearLayout rowWrap2 = kit.rowWrap(notes.get(i8), null, kit.circle(Kit.X, t("ns.notes.del", new Object[0]) + ": " + notes.get(i8), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    ScrAssist.lambda$ai$14(i8, nativeSettings);
                }
            }));
            ((TextView) ((LinearLayout) rowWrap2.getChildAt(0)).getChildAt(0)).setTypeface(Fonts.RAJ_400);
            kit.add(list5, rowWrap2);
        }
        final EditText input3 = kit.input("", t("ns.notes.ph", new Object[0]), false, true, 16385);
        input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(BootSeq.UN_HOLD_MS)});
        TextView btn = kit.btn(t("ns.notes.pin", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                ScrAssist.lambda$ai$15(input3, nativeSettings);
            }
        });
        View[] viewArr2 = new View[i7];
        viewArr2[0] = Kit.grow(input3);
        viewArr2[1] = btn;
        LinearLayout hrow = kit.hrow(viewArr2);
        hrow.setPadding(0, nativeSettings.px(6.0f), 0, nativeSettings.px(8.0f));
        kit.add(list5, hrow);
        nativeSettings.fine(t("ns.notes.fine", new Object[0]));
        voice(nativeSettings, opt.optBoolean(str10));
        kit.add(nativeSettings.list(), kit.toggleRow(t("set.ai.briefhome", new Object[0]), null, "1".equals(nativeSettings.be.get("ai.briefhome")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda47
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.be.set("ai.briefhome", ((Boolean)obj).booleanValue() ? "1" : "");
            }
        }));
        nativeSettings.fine(t("set.ai.briefhome.fine", new Object[0]));
        nativeSettings.fine(t("set.ai.sent", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$ai$4(Runnable runnable, TextView textView, int i, KeyEvent keyEvent) {
        runnable.run();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$5(Runnable runnable, Kit kit, NativeSettings nativeSettings, View view, boolean z) {
        if (!z) {
            runnable.run();
        }
        view.setBackground(Kit.pill(0, z ? kit.ACC : kit.LINE2, nativeSettings.dn, nativeSettings.dn * 6.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$6(List list, String str, NativeSettings nativeSettings) {
        String orderMove = SettingsModel.orderMove(list, str, -1);
        if (orderMove != null) {
            nativeSettings.be.aiSetOrder(orderMove);
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$7(List list, String str, NativeSettings nativeSettings) {
        String orderMove = SettingsModel.orderMove(list, str, 1);
        if (orderMove != null) {
            nativeSettings.be.aiSetOrder(orderMove);
            nativeSettings.paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$8(NativeSettings nativeSettings, String str, Boolean bool) {
        nativeSettings.be.aiSetOrder(SettingsModel.orderToggle(SettingsModel.order(nativeSettings.be.aiHasKey()), str));
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$10(EditText editText) {
        int i;
        try {
            i = Integer.parseInt(editText.getText().toString().trim());
        } catch (Exception e) {
            i = 0;
        }
        int max = Math.max(0, Math.min(40, i));
        Store.set("aos.aimem", SettingsModel.aimemSet(Store.get("aos.aimem"), null, Integer.valueOf(max)));
        if (editText.getText().toString().equals(String.valueOf(max))) {
            return;
        }
        editText.setText(String.valueOf(max));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$ai$11(Runnable runnable, TextView textView, int i, KeyEvent keyEvent) {
        runnable.run();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$12(Runnable runnable, View view, boolean z) {
        if (z) {
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$13(boolean[] zArr, NativeSettings nativeSettings, TextView textView, LinearLayout linearLayout, View view) {
        if (zArr[0]) {
            Store.remove("aos.chats");
            nativeSettings.paint();
        } else {
            zArr[0] = true;
            textView.setText(t("ns.mem.sure", new Object[0]));
            linearLayout.setContentDescription(t("ns.mem.sure", new Object[0]));
            linearLayout.announceForAccessibility(t("ns.mem.sure", new Object[0]));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$14(int i, NativeSettings nativeSettings) {
        Store.set("aos.mem", SettingsModel.noteDel(Store.get("aos.mem"), i));
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ai$15(EditText editText, NativeSettings nativeSettings) {
        String trim = editText.getText().toString().trim();
        if (trim.isEmpty()) {
            return;
        }
        Store.set("aos.mem", SettingsModel.noteAdd(Store.get("aos.mem"), trim, System.currentTimeMillis()));
        nativeSettings.paint();
    }

    static JSONObject opt(JSONObject jSONObject, String str) {
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        return optJSONObject == null ? new JSONObject() : optJSONObject;
    }

    static JSONObject json(Object... objArr) {
        JSONObject jSONObject = new JSONObject();
        int i = 0;
        while (true) {
            int i2 = i + 1;
            try {
                if (i2 >= objArr.length) {
                    break;
                }
                jSONObject.put((String) objArr[i], objArr[i2]);
                i += 2;
            } catch (Exception e) {
            }
        }
        return jSONObject;
    }

    static String pname(String str) {
        return "claude".equals(str) ? "Anthropic Claude" : "gemini".equals(str) ? "Google Gemini" : "OpenAI-compatible";
    }

    static Bitmap dataImg(String str) {
        return ScrLayout.dataImg(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LinearLayout cardHead(NativeSettings nativeSettings, LinearLayout linearLayout, String str, JSONObject jSONObject, String str2) {
        TextView chip;
        Kit kit = nativeSettings.k;
        boolean optBoolean = jSONObject.optBoolean("hasKey");
        LinearLayout linearLayout2 = new LinearLayout(nativeSettings.c);
        linearLayout2.setGravity(16);
        linearLayout2.addView(kit.iconView("M10.5 2.5a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM8.3 7.7L2.5 13.5M4 12l1.5 1.5", optBoolean ? kit.ACC : kit.INK, 18.0f));
        TextView text = kit.text(str, Fonts.RAJ_600, 15.0f, 0.0f, kit.INK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.leftMargin = nativeSettings.px(10.0f);
        linearLayout2.addView(text, layoutParams);
        if (optBoolean) {
            chip = kit.chip("✓ " + t("ns.connected", new Object[0]) + (jSONObject.optString("last4").isEmpty() ? "" : " · ••••" + jSONObject.optString("last4")), kit.GOOD);
        } else {
            chip = kit.chip(str2, 0);
        }
        linearLayout2.addView(chip);
        linearLayout.addView(linearLayout2);
        return linearLayout2;
    }

    static void keyCard(final NativeSettings nativeSettings, final String str, String str2, JSONObject jSONObject, String[] strArr) {
        Kit kit = nativeSettings.k;
        boolean optBoolean = jSONObject.optBoolean("hasKey");
        LinearLayout card = kit.card(true, false);
        cardHead(nativeSettings, card, str2, jSONObject, t("ns.nokey", new Object[0]));
        if (!optBoolean) {
            TextView fine = kit.fine(t("ns.ai.howto", new Object[0]));
            ((LinearLayout.LayoutParams) fine.getLayoutParams()).topMargin = nativeSettings.px(4.0f);
            card.addView(fine);
        }
        final TextView msg = kit.msg("");
        final String url = Keys.url(str);
        TextView btn = kit.btn(t("ns.paste", new Object[0]), true, true, null);
        btn.setContentDescription(t("ns.paste", new Object[0]) + " " + str2);
        btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrAssist.pasteAI(nativeSettings, msg, str);
            }
        });
        View[] viewArr = new View[3];
        viewArr[0] = btn;
        viewArr[1] = (optBoolean || url.isEmpty()) ? null : kit.btn(t("ns.getkey", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                nativeSettings.be.url(url);
            }
        });
        viewArr[2] = optBoolean ? kit.btn(t("ns.remove", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ScrAssist.lambda$keyCard$19(str, nativeSettings);
            }
        }) : null;
        card.addView(kit.hrow(viewArr));
        final EditText keep = nativeSettings.keep(kit.input("", optBoolean ? t("ns.replace", new Object[0]) : t("ns.typekey", new Object[0]), true, true, 0), "key." + str);
        keep.setContentDescription(str2 + " " + t("ns.key", new Object[0]));
        final TextView btn2 = kit.btn(t("ns.verify", new Object[0]), false, true, null);
        btn2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrAssist.lambda$keyCard$21(keep, btn2, nativeSettings, str, msg, view);
            }
        });
        card.addView(kit.hrow(Kit.grow(keep), btn2));
        String pref = nativeSettings.pref("ai.model." + str, strArr[0]);
        TextView mono = kit.mono(t("ns.model", new Object[0]), 9.0f, kit.DIM);
        FrameLayout frameLayout = nativeSettings.root;
        String str3 = str2 + " " + t("ns.model", new Object[0]);
        Backend backend = nativeSettings.be;
        Objects.requireNonNull(backend);
        card.addView(kit.hrow(mono, ModelPick.select(kit, frameLayout, str, str3, strArr, pref, optBoolean, new Welcome$$ExternalSyntheticLambda9(backend), msg, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$keyCard$22(nativeSettings, str, (String) obj);
            }
        })));
        card.addView(msg);
        nativeSettings.add(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$keyCard$19(String str, NativeSettings nativeSettings) {
        if ("claude".equals(str)) {
            nativeSettings.be.aiClearKey();
        } else {
            nativeSettings.be.geminiClearKey();
        }
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$keyCard$21(EditText editText, final TextView textView, final NativeSettings nativeSettings, final String str, final TextView textView2, View view) {
        String trim = editText.getText().toString().trim();
        if (trim.isEmpty()) {
            return;
        }
        textView.setText(t("ns.checking", new Object[0]));
        Consumer<String> consumer = new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$keyCard$20(nativeSettings, str, textView, textView2, (String) obj);
            }
        };
        if ("claude".equals(str)) {
            nativeSettings.be.aiSetKey(trim, consumer);
        } else {
            nativeSettings.be.geminiSetKey(trim, consumer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$keyCard$20(NativeSettings nativeSettings, String str, TextView textView, TextView textView2, String str2) {
        JSONObject J = NativeSettings.J(str2);
        if (!KeyCheck.settle(nativeSettings.drafts, "key." + str, J)) {
            textView.setText(t("ns.verify", new Object[0]));
            textView2.setText(up(J.optString("error", t("ns.keyfail", new Object[0]))));
        } else {
            nativeSettings.paint();
            if (J.optBoolean("provisional")) {
                nativeSettings.toast(J.optString("note"));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$keyCard$22(NativeSettings nativeSettings, String str, String str2) {
        nativeSettings.be.set("ai.model." + str, str2);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void pasteAI(NativeSettings nativeSettings, TextView textView) {
        pasteAI(nativeSettings, textView, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void pasteAI(final NativeSettings nativeSettings, final TextView textView, String str) {
        final String looks = Keys.looks(nativeSettings.be.clip("ai"), "ai");
        if (looks.isEmpty()) {
            nativeSettings.toast(t("ns.paste.noai", new Object[0]));
            return;
        }
        String provider = KeyFlow.provider(looks, null);
        if (provider == null) {
            nativeSettings.k.pickSheet(nativeSettings.root, t("key.which", new Object[0]), KeyFlow.PICK_NAMES, KeyFlow.PICK, KeyFlow.preselect(str), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.saveAI(nativeSettings, textView, looks, (String) obj);
                }
            });
        } else {
            saveAI(nativeSettings, textView, looks, provider);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void saveAI(final NativeSettings nativeSettings, final TextView textView, String str, final String str2) {
        final String last4 = Keys.last4(str);
        textView.setText(t("ns.checking.key", "name", up(Keys.name(str2)), "l4", last4));
        Consumer<String> consumer = new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda58
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$saveAI$24(str2, nativeSettings, last4, textView, (String) obj);
            }
        };
        if ("claude".equals(str2)) {
            nativeSettings.be.aiSetKey(str, consumer);
        } else if ("gemini".equals(str2)) {
            nativeSettings.be.geminiSetKey(str, consumer);
        } else {
            String[] oai = Keys.oai(str2);
            nativeSettings.be.oaiSet(json("base", oai[0], "key", str, "model", oai[1]).toString(), consumer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$saveAI$24(String str, NativeSettings nativeSettings, String str2, TextView textView, String str3) {
        String orderWithOpenai;
        JSONObject J = NativeSettings.J(str3);
        if (J.optBoolean("ok") || J.optBoolean("hasKey")) {
            if (!"claude".equals(str) && !"gemini".equals(str) && (orderWithOpenai = SettingsModel.orderWithOpenai(str3)) != null) {
                nativeSettings.be.aiSetOrder(orderWithOpenai);
            }
            nativeSettings.toast(J.optBoolean("provisional") ? J.optString("note") : t("ns.key.connected", "name", up(Keys.name(str)), "l4", str2));
            nativeSettings.paint();
            return;
        }
        textView.setText(up(J.optString("error", t("ns.keyfail", new Object[0]))));
    }

    static void oaiCard(final NativeSettings nativeSettings, final JSONObject jSONObject) {
        char c;
        TextView textView;
        final Kit kit = nativeSettings.k;
        LinearLayout card = kit.card(true, false);
        cardHead(nativeSettings, card, "OpenAI-compatible", jSONObject, t("ns.notset.chip", new Object[0]));
        String str = "";
        final TextView msg = kit.msg("");
        TextView btn = kit.btn(t("ns.paste", new Object[0]), true, true, null);
        btn.setContentDescription(t("ns.paste", new Object[0]) + " OpenAI-compatible");
        btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrAssist.pasteAI(nativeSettings, msg);
            }
        });
        card.addView(kit.hrow(btn, Kit.grow(kit.text(t("ns.oai.auto", new Object[0]), Fonts.RAJ_400, 12.0f, 0.0f, kit.DIM))));
        TextView fine = kit.fine(t("ns.oai.p", new Object[0]));
        ((LinearLayout.LayoutParams) fine.getLayoutParams()).topMargin = nativeSettings.px(4.0f);
        card.addView(fine);
        int oaiPreset = jSONObject.optString("base").isEmpty() ? 0 : SettingsModel.oaiPreset(jSONObject.optString("base"));
        final EditText input = kit.input(jSONObject.optString("base").isEmpty() ? SettingsModel.OAI[0][2] : jSONObject.optString("base"), t("ns.oai.base", new Object[0]), false, true, 17);
        final EditText input2 = kit.input("", (jSONObject.optBoolean("key") || jSONObject.optBoolean("hasKey")) ? t("ns.oai.key.saved", new Object[0]) : t("ns.oai.key", new Object[0]), true, true, 0);
        if (jSONObject.optString("model").isEmpty()) {
            if (jSONObject.optString("base").isEmpty()) {
                str = SettingsModel.OAI[0][3];
            }
        } else {
            str = jSONObject.optString("model");
        }
        final String[] strArr = {str};
        final TextView[] textViewArr = {null};
        final Consumer consumer = new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$oaiCard$26(strArr, textViewArr, (String) obj);
            }
        };
        FrameLayout frameLayout = nativeSettings.root;
        String t = t("ns.oai.model", new Object[0]);
        String[] strArr2 = {strArr[0]};
        String str2 = strArr[0];
        boolean optBoolean = jSONObject.optBoolean("hasKey");
        Backend backend = nativeSettings.be;
        Objects.requireNonNull(backend);
        textViewArr[0] = ModelPick.select(kit, frameLayout, "openai", t, strArr2, str2, optBoolean, new Welcome$$ExternalSyntheticLambda9(backend), msg, consumer);
        int length = SettingsModel.OAI.length;
        final String[] strArr3 = new String[length];
        final String[] strArr4 = new String[SettingsModel.OAI.length];
        for (int i = 0; i < length; i++) {
            strArr3[i] = SettingsModel.OAI[i][1];
            strArr4[i] = SettingsModel.OAI[i][0];
        }
        final String[] strArr5 = {strArr4[oaiPreset]};
        final TextView[] textViewArr2 = {null};
        textViewArr2[0] = kit.select(nativeSettings.root, t("ns.oai.preset", new Object[0]), strArr3, strArr4, strArr5[0], new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$oaiCard$27(strArr5, input, consumer, textViewArr2, (String) obj);
            }
        });
        textViewArr2[0].setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                kit.pickSheet(nativeSettings.root, ScrAssist.t("ns.oai.preset", new Object[0]), strArr3, strArr4, r4[0], new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        /* lambda$oaiCard$28 r-vars elided */
                    }
                });
            }
        });
        card.addView(kit.hrow(kit.mono(t("ns.oai.preset", new Object[0]), 9.0f, kit.DIM), textViewArr2[0]));
        card.addView(kit.hrow(input));
        card.addView(kit.hrow(input2));
        final TextView btn2 = kit.btn(t("ns.oai.fetch", new Object[0]), true, true, null);
        final String[][] strArr6 = {null};
        btn2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrAssist.lambda$oaiCard$31(input, msg, btn2, nativeSettings, input2, strArr6, strArr, consumer, view);
            }
        });
        card.addView(kit.hrow(Kit.grow(textViewArr[0]), btn2));
        final TextView btn3 = kit.btn(t("ns.oai.save", new Object[0]), false, true, null);
        btn3.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrAssist.lambda$oaiCard$33(input, input2, strArr, msg, jSONObject, btn3, nativeSettings, view);
            }
        });
        View[] viewArr = new View[2];
        viewArr[0] = btn3;
        if (jSONObject.optBoolean("hasKey")) {
            c = 1;
            textView = kit.btn(t("ns.remove", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    ScrAssist.lambda$oaiCard$34(nativeSettings);
                }
            });
        } else {
            c = 1;
            textView = null;
        }
        viewArr[c] = textView;
        card.addView(kit.hrow(viewArr));
        card.addView(msg);
        TextView fine2 = kit.fine(t("ns.oai.clear", new Object[0]));
        ((LinearLayout.LayoutParams) fine2.getLayoutParams()).topMargin = nativeSettings.px(4.0f);
        card.addView(fine2);
        nativeSettings.add(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$26(String[] strArr, TextView[] textViewArr, String str) {
        strArr[0] = str;
        if (textViewArr[0] != null) {
            textViewArr[0].setText(str);
            textViewArr[0].setContentDescription(t("ns.oai.model", new Object[0]) + ", " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$27(String[] strArr, EditText editText, Consumer consumer, TextView[] textViewArr, String str) {
        for (String[] strArr2 : SettingsModel.OAI) {
            if (strArr2[0].equals(str)) {
                strArr[0] = str;
                if (!strArr2[2].isEmpty()) {
                    editText.setText(strArr2[2]);
                }
                consumer.accept(strArr2[3]);
                textViewArr[0].setText(strArr2[1]);
                textViewArr[0].setContentDescription(t("ns.oai.preset", new Object[0]) + ", " + strArr2[1]);
                if (strArr2[2].isEmpty()) {
                    editText.requestFocus();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$28(String[] strArr, EditText editText, Consumer consumer, TextView[] textViewArr, String str) {
        for (String[] strArr2 : SettingsModel.OAI) {
            if (strArr2[0].equals(str)) {
                strArr[0] = str;
                if (!strArr2[2].isEmpty()) {
                    editText.setText(strArr2[2]);
                }
                consumer.accept(strArr2[3]);
                textViewArr[0].setText(strArr2[1]);
                if (strArr2[2].isEmpty()) {
                    editText.requestFocus();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$31(EditText editText, final TextView textView, final TextView textView2, final NativeSettings nativeSettings, EditText editText2, final String[][] strArr, final String[] strArr2, final Consumer consumer, View view) {
        String trim = editText.getText().toString().trim();
        if (!SettingsModel.httpsOk(trim)) {
            textView.setText(t("ns.oai.https", new Object[0]));
        } else {
            textView2.setText(t("ns.oai.fetching", new Object[0]));
            nativeSettings.be.oaiModels(json("base", trim, "key", editText2.getText().toString().trim()).toString(), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda57
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.lambda$oaiCard$30(textView2, textView, strArr, strArr2, nativeSettings, consumer, (String) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$30(TextView textView, TextView textView2, String[][] strArr, String[] strArr2, NativeSettings nativeSettings, Consumer consumer, String str) {
        textView.setText(t("ns.oai.fetch", new Object[0]));
        JSONObject J = NativeSettings.J(str);
        JSONArray optJSONArray = J.optJSONArray("models");
        if (optJSONArray == null) {
            textView2.setText(up(J.optString("error", t("ns.oai.nolist", new Object[0]))));
            return;
        }
        strArr[0] = new String[optJSONArray.length()];
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < optJSONArray.length(); i++) {
            strArr[0][i] = optJSONArray.optString(i);
            arrayList.add(new String[]{optJSONArray.optString(i), optJSONArray.optString(i)});
        }
        Store.set(ModelList.cacheKey("openai"), ModelList.encode(arrayList));
        textView2.setText(t("ns.oai.models", "n", Integer.valueOf(optJSONArray.length())));
        String smallest = ModelList.smallest(arrayList, null);
        if (smallest != null) {
            if (strArr2[0].trim().isEmpty() || !nativeSettings.root.getContext().getSharedPreferences("home", 0).getBoolean("ai.model.openai.picked", false)) {
                consumer.accept(smallest);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$33(EditText editText, EditText editText2, String[] strArr, final TextView textView, JSONObject jSONObject, final TextView textView2, final NativeSettings nativeSettings, View view) {
        String t;
        String trim = editText.getText().toString().trim();
        String trim2 = editText2.getText().toString().trim();
        String trim3 = strArr[0].trim();
        if (!SettingsModel.httpsOk(trim)) {
            t = t("ns.oai.https", new Object[0]);
        } else if (trim2.isEmpty() && !jSONObject.optBoolean("key") && !jSONObject.optBoolean("hasKey")) {
            t = t("ns.oai.needkey", new Object[0]);
        } else {
            if (!trim3.isEmpty()) {
                textView2.setText(t("ns.oai.testing", new Object[0]));
                nativeSettings.be.oaiSet(json("base", trim, "key", trim2, "model", trim3).toString(), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda21
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ScrAssist.lambda$oaiCard$32(nativeSettings, textView2, textView, (String) obj);
                    }
                });
                return;
            }
            t = t("ns.oai.needmodel", new Object[0]);
        }
        textView.setText(t);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$32(NativeSettings nativeSettings, TextView textView, TextView textView2, String str) {
        JSONObject J = NativeSettings.J(str);
        if (!J.optBoolean("ok")) {
            textView.setText(t("ns.oai.save", new Object[0]));
            textView2.setText(up(J.optString("error", t("ns.oai.fail", new Object[0]))));
            return;
        }
        String orderWithOpenai = SettingsModel.orderWithOpenai(str);
        if (orderWithOpenai != null) {
            nativeSettings.be.aiSetOrder(orderWithOpenai);
        }
        nativeSettings.paint();
        if (J.optBoolean("provisional")) {
            nativeSettings.toast(J.optString("note"));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$34(NativeSettings nativeSettings) {
        nativeSettings.be.oaiClear();
        nativeSettings.paint();
    }

    static void voice(final NativeSettings nativeSettings, boolean z) {
        int i;
        String t;
        String str;
        String[] strArr;
        int i2;
        Kit kit = nativeSettings.k;
        String pref = nativeSettings.pref("ai.voice.lang", "");
        String pref2 = nativeSettings.pref("ai.voice.engine", "system");
        String pref3 = nativeSettings.pref("ai.voice.gem", "Kore");
        if (nativeSettings.voiceInfo == null) {
            nativeSettings.voiceInfo = "";
            nativeSettings.be.voiceInfo(new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda22
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.lambda$voice$35(nativeSettings, (String) obj);
                }
            });
        }
        if (nativeSettings.ttsVoices == null) {
            nativeSettings.ttsVoices = "";
            nativeSettings.be.ttsVoices(new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda31
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.lambda$voice$36(nativeSettings, (String) obj);
                }
            });
        }
        JSONObject J = NativeSettings.J(nativeSettings.voiceInfo);
        JSONObject J2 = NativeSettings.J(nativeSettings.ttsVoices);
        nativeSettings.sec(t("set.v.h", new Object[0]));
        LinearLayout list = nativeSettings.list();
        kit.add(list, kit.row(null, 0, t("vo.h", new Object[0]), nativeSettings.pref("ai.voice.name", "").isEmpty() ? t("set.v.auto", new Object[0]) : nativeSettings.pref("ai.voice.name", ""), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                ScrAssist.lambda$voice$37(nativeSettings);
            }
        }, new View[0]));
        boolean z2 = true;
        kit.add(list, kit.toggleRow(t("set.v.speak", new Object[0]), null, !"0".equals(nativeSettings.pref("ai.speak", "1")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda33
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.be.set("ai.speak", ((Boolean)obj).booleanValue() ? "1" : "0");
            }
        }));
        kit.add(list, kit.toggleRow(t("set.v.autosend", new Object[0]), null, !"0".equals(nativeSettings.pref("ai.voice.autosend", "1")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.be.set("ai.voice.autosend", ((Boolean)obj).booleanValue() ? "1" : "0");
            }
        }));
        kit.add(list, kit.toggleRow(t("set.v.conv", new Object[0]), null, "1".equals(nativeSettings.pref("ai.voice.conv", "")), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda35
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.be.set("ai.voice.conv", ((Boolean)obj).booleanValue() ? "1" : "");
            }
        }));
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.add("");
        arrayList2.add(t("set.v.system", new Object[0]));
        HashSet hashSet = new HashSet();
        hashSet.add("");
        String[][] strArr2 = VLANGS;
        int length = strArr2.length;
        int i3 = 0;
        while (i3 < length) {
            String[] strArr3 = strArr2[i3];
            boolean z3 = z2;
            arrayList.add(strArr3[0]);
            arrayList2.add(strArr3[z3 ? 1 : 0]);
            hashSet.add(strArr3[0]);
            i3++;
            z2 = z3;
        }
        boolean z4 = z2;
        if (!pref.isEmpty() && !hashSet.contains(pref)) {
            arrayList.add(pref);
            arrayList2.add(pref);
            hashSet.add(pref);
        }
        String[] strArr4 = {"installed", "supported"};
        int i4 = 0;
        while (i4 < 2) {
            JSONArray optJSONArray = J.optJSONArray(strArr4[i4]);
            if (optJSONArray != null) {
                strArr = strArr4;
                int i5 = 0;
                while (true) {
                    i2 = i4;
                    if (i5 < optJSONArray.length()) {
                        if (hashSet.add(optJSONArray.optString(i5))) {
                            arrayList.add(optJSONArray.optString(i5));
                            arrayList2.add(optJSONArray.optString(i5));
                        }
                        i5++;
                        i4 = i2;
                    }
                }
            } else {
                strArr = strArr4;
                i2 = i4;
            }
            i4 = i2 + 1;
            strArr4 = strArr;
        }
        View[] viewArr = new View[2];
        viewArr[0] = kit.mono(t("set.v.lang", new Object[0]), 9.0f, kit.DIM);
        String str2 = "installed";
        viewArr[z4 ? 1 : 0] = kit.select(nativeSettings.root, t("set.v.lang", new Object[0]), (String[]) arrayList2.toArray(new String[0]), (String[]) arrayList.toArray(new String[0]), pref, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda36
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$voice$41(nativeSettings, (String) obj);
            }
        });
        ((LinearLayout) kit.add(list, kit.hrow(viewArr))).setPadding(0, nativeSettings.px(8.0f), 0, nativeSettings.px(8.0f));
        kit.add(list, kit.rowWrap(t("set.v.rec", new Object[0]), recLine(nativeSettings, J), new View[0]));
        View[] viewArr2 = new View[2];
        viewArr2[0] = kit.btn(t("set.v.download", new Object[0]), false, z4, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                nativeSettings.be.voiceSettings("download");
            }
        });
        viewArr2[z4 ? 1 : 0] = kit.btn(t("set.v.inputset", new Object[0]), z4, z4, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                nativeSettings.be.voiceSettings("input");
            }
        });
        ((LinearLayout) kit.add(list, kit.hrow(viewArr2))).setPadding(0, nativeSettings.px(4.0f), 0, nativeSettings.px(10.0f));
        nativeSettings.sec(t("set.v.out", new Object[0]));
        LinearLayout list2 = nativeSettings.list();
        View[] viewArr3 = new View[2];
        viewArr3[0] = kit.mono(t("set.v.engine", new Object[0]), 9.0f, kit.DIM);
        viewArr3[1] = kit.select(nativeSettings.root, t("set.v.engine", new Object[0]), new String[]{t("set.v.eng.sys", new Object[0]), t("set.v.eng.gem", new Object[0])}, new String[]{"system", "gemini"}, "gemini".equals(pref2) ? "gemini" : "system", new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda39
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$voice$44(nativeSettings, (String) obj);
            }
        });
        ((LinearLayout) kit.add(list2, kit.hrow(viewArr3))).setPadding(0, nativeSettings.px(8.0f), 0, nativeSettings.px(8.0f));
        if ("gemini".equals(pref2)) {
            JSONArray JA = NativeSettings.JA(nativeSettings.be.gemVoices());
            ArrayList arrayList3 = new ArrayList();
            for (int i6 = 0; i6 < JA.length(); i6++) {
                arrayList3.add(JA.optString(i6));
            }
            if (arrayList3.isEmpty()) {
                Collections.addAll(arrayList3, "Kore", "Puck", "Charon", "Aoede", "Leda", "Orus", "Zephyr", "Fenrir");
            }
            String[] strArr5 = (String[]) arrayList3.toArray(new String[0]);
            ((LinearLayout) kit.add(list2, kit.hrow(kit.mono(t("set.v.gvoice", new Object[0]), 9.0f, kit.DIM), kit.select(nativeSettings.root, t("set.v.gvoice", new Object[0]), strArr5, strArr5, pref3, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda23
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.lambda$voice$45(nativeSettings, (String) obj);
                }
            })))).setPadding(0, nativeSettings.px(8.0f), 0, nativeSettings.px(8.0f));
            TextView fine = kit.fine(z ? t("set.v.gem.fine", new Object[0]) : t("set.v.gem.nokey", new Object[0]));
            ((LinearLayout.LayoutParams) fine.getLayoutParams()).setMargins(0, nativeSettings.px(4.0f), 0, nativeSettings.px(4.0f));
            kit.add(list2, fine);
        }
        JSONArray optJSONArray2 = J2.optJSONArray("voices");
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        arrayList4.add("");
        arrayList5.add(t("set.v.auto", new Object[0]) + (J2.optString("auto").isEmpty() ? "" : " · " + J2.optString("auto")));
        if (optJSONArray2 != null) {
            int i7 = 0;
            while (i7 < optJSONArray2.length()) {
                JSONObject optJSONObject = optJSONArray2.optJSONObject(i7);
                if (optJSONObject == null) {
                    str = str2;
                } else {
                    int optInt = optJSONObject.optInt("quality");
                    arrayList4.add(optJSONObject.optString("name"));
                    String optString = optJSONObject.optString("name");
                    if (optInt >= 500) {
                        i = 0;
                        t = t("set.v.q.vhigh", new Object[0]);
                    } else {
                        i = 0;
                        t = optInt >= 400 ? t("set.v.q.high", new Object[0]) : t("set.v.q.norm", new Object[0]);
                    }
                    String sb = (optJSONObject.optBoolean("net") ? new StringBuilder().append(" · ").append(t("set.v.net", new Object[i])) : new StringBuilder().append(" · ").append(t("set.v.offline", new Object[i]))).toString();
                    str = str2;
                    arrayList5.add(optString + " · " + t + sb + (optJSONObject.optBoolean(str) ? "" : " · " + t("set.v.notinst", new Object[0])));
                }
                i7++;
                str2 = str;
            }
        }
        String pref4 = nativeSettings.pref("ai.voice.name", "");
        if (!pref4.isEmpty() && !arrayList4.contains(pref4)) {
            arrayList4.add(pref4);
            arrayList5.add(pref4);
        }
        ((LinearLayout) kit.add(list2, kit.hrow(kit.mono(t("set.v.voice", new Object[0]), 9.0f, kit.DIM), kit.select(nativeSettings.root, t("set.v.voice", new Object[0]), (String[]) arrayList5.toArray(new String[0]), (String[]) arrayList4.toArray(new String[0]), pref4, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$voice$46(nativeSettings, (String) obj);
            }
        })))).setPadding(0, nativeSettings.px(8.0f), 0, nativeSettings.px(8.0f));
        int parse = parse(nativeSettings.pref("ai.voice.rate", "100"), 100);
        int parse2 = parse(nativeSettings.pref("ai.voice.pitch", "100"), 100);
        kit.add(list2, kit.slider(t("set.v.rate", new Object[0]), 50, 200, 5, parse, t("set.v.rate", new Object[0]), new IntFunction() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda25
            @Override // java.util.function.IntFunction
            public final Object apply(int i8) {
                String format;
                format = String.format(Locale.US, "%.2f×", Float.valueOf(i8 / 100.0f));
                return format;
            }
        }, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda26
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.be.set("ai.voice.rate", String.valueOf((Integer) obj));
            }
        }));
        kit.add(list2, kit.slider(t("set.v.pitch", new Object[0]), 50, 200, 5, parse2, t("set.v.pitch", new Object[0]), new IntFunction() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda27
            @Override // java.util.function.IntFunction
            public final Object apply(int i8) {
                String format;
                format = String.format(Locale.US, "%.2f×", Float.valueOf(i8 / 100.0f));
                return format;
            }
        }, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda28
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                nativeSettings.be.set("ai.voice.pitch", String.valueOf((Integer) obj));
            }
        }));
        ((LinearLayout) kit.add(list2, kit.hrow(kit.btn(t("set.v.test", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                ScrAssist.lambda$voice$51(nativeSettings);
            }
        }), kit.btn(t("set.v.ttsset", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                nativeSettings.be.voiceSettings("tts");
            }
        })))).setPadding(0, nativeSettings.px(4.0f), 0, nativeSettings.px(10.0f));
        nativeSettings.fine(t("set.v.fine", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$voice$35(NativeSettings nativeSettings, String str) {
        if (str == null) {
            str = "{}";
        }
        nativeSettings.voiceInfo = str;
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$voice$36(NativeSettings nativeSettings, String str) {
        if (str == null) {
            str = "{}";
        }
        nativeSettings.ttsVoices = str;
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$voice$37(NativeSettings nativeSettings) {
        FrameLayout frameLayout = nativeSettings.root;
        Theme theme = nativeSettings.th;
        Backend backend = nativeSettings.be;
        Objects.requireNonNull(nativeSettings);
        VoiceSheet.show(frameLayout, theme, backend, new NativeSettings$$ExternalSyntheticLambda25(nativeSettings));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$voice$41(NativeSettings nativeSettings, String str) {
        nativeSettings.be.set("ai.voice.lang", str);
        nativeSettings.be.set("ai.voice.name", "");
        nativeSettings.voiceInfo = null;
        nativeSettings.ttsVoices = null;
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$voice$44(NativeSettings nativeSettings, String str) {
        nativeSettings.be.set("ai.voice.engine", str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$voice$45(NativeSettings nativeSettings, String str) {
        nativeSettings.be.set("ai.voice.gem", str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$voice$46(NativeSettings nativeSettings, String str) {
        nativeSettings.be.set("ai.voice.name", str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$voice$51(NativeSettings nativeSettings) {
        String trim = nativeSettings.pref("ai.name", "Assistant").trim();
        nativeSettings.be.sayStop();
        nativeSettings.be.speak(new JSONArray().put(t("set.v.sample", "name", trim)).put(t("set.v.sample2", new Object[0])).toString());
    }

    static int parse(String str, int i) {
        try {
            int parseInt = Integer.parseInt(str.trim());
            return parseInt > 0 ? parseInt : i;
        } catch (Exception e) {
            return i;
        }
    }

    static String recLine(NativeSettings nativeSettings, JSONObject jSONObject) {
        boolean z;
        if (nativeSettings.voiceInfo == null || nativeSettings.voiceInfo.isEmpty()) {
            return t("set.v.checking", new Object[0]);
        }
        String optString = jSONObject.optString("lang");
        String str = optString.split("-")[0];
        JSONArray optJSONArray = jSONObject.optJSONArray("installed");
        if (optJSONArray != null) {
            z = false;
            for (int i = 0; i < optJSONArray.length(); i++) {
                if (optJSONArray.optString(i).split("-")[0].equals(str)) {
                    z = true;
                }
            }
        } else {
            z = false;
        }
        if (!jSONObject.optBoolean("available")) {
            return t("set.v.rec.none", new Object[0]);
        }
        if (!jSONObject.optBoolean("onDevice")) {
            return t("set.v.rec.sys", new Object[0]);
        }
        if (optJSONArray != null && !z) {
            return t("set.v.rec.nolang", "l", optString);
        }
        return t("set.v.rec.ondev", "l", optString);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void aif(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        final Features features = nativeSettings.F;
        JSONObject st = features.st();
        JSONObject usage = features.usage();
        int estimate = features.estimate();
        boolean paused = features.paused();
        int i = 0;
        nativeSettings.add(nativeSettings.hd(up(t("aif.title", new Object[0])), t("aif.hd", new Object[0])));
        boolean keyMissing = features.keyMissing();
        if (keyMissing) {
            nativeSettings.add(kit.note("M10.5 2.5a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM8.3 7.7L2.5 13.5M4 12l1.5 1.5", kit.ACC, t("aif.nokey", new Object[0]), null, kit.btn(t("aif.nokey.go", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda59
                @Override // java.lang.Runnable
                public final void run() {
                    nativeSettings.m354lambda$fixRow$10$comaugmaososettingsNativeSettings("ai");
                }
            }), true));
        }
        kit.add(nativeSettings.list(), kit.toggleRow(t("aif.pause", new Object[0]), t("aif.pause.sub", new Object[0]), paused, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda60
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$aif$54(nativeSettings.F, nativeSettings, (Boolean) obj);
            }
        }));
        nativeSettings.sec(t("aif.usage", new Object[0]));
        LinearLayout list = nativeSettings.list();
        Flow flow = kit.flow(12);
        flow.setPadding(0, nativeSettings.px(12.0f), 0, nativeSettings.px(12.0f));
        flow.addView(nativeSettings.conn(t("aif.usage.today", new Object[0]), String.valueOf(usage.optInt("today")), true));
        flow.addView(nativeSettings.conn(t("aif.usage.week", new Object[0]), String.valueOf(usage.optInt("wk")), true));
        int optInt = usage.optInt("tokens");
        if (optInt > 0) {
            flow.addView(nativeSettings.conn(t("aif.usage.tokens", new Object[0]), optInt >= 1000 ? (Math.round(optInt / 100.0d) / 10.0d) + "K" : String.valueOf(optInt), true));
        }
        flow.addView(nativeSettings.conn(t("aif.usage.auto", new Object[0]), String.valueOf(estimate), true));
        list.addView(flow);
        nativeSettings.fine(t("aif.usage.fine", "m", usage.optString("m")));
        String[] strArr = GROUPS;
        int length = strArr.length;
        int i2 = 0;
        while (i2 < length) {
            String str = strArr[i2];
            ArrayList arrayList = new ArrayList();
            for (Features.F f : Features.LIST) {
                if (f.group.equals(str)) {
                    arrayList.add(f);
                }
            }
            if (!arrayList.isEmpty()) {
                nativeSettings.sec(up(t("aif.g." + str, new Object[i])));
                LinearLayout list2 = nativeSettings.list();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    kit.add(list2, frow(nativeSettings, (Features.F) it.next(), st, paused, keyMissing));
                }
                if ("privacy".equals(str)) {
                    kit.add(list2, kit.row(null, 0, t("aif.leave", new Object[i]), t("aif.leave.sub", new Object[i]), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda61
                        @Override // java.lang.Runnable
                        public final void run() {
                            nativeSettings.m356lambda$navRow$7$comaugmaososettingsNativeSettings("aifleave");
                        }
                    }, new View[i]));
                }
            }
            i2++;
            i = 0;
        }
        nativeSettings.fine(t("aif.fine", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$aif$54(Features features, NativeSettings nativeSettings, Boolean bool) {
        features.setPref("paused", bool != null && bool.booleanValue() ? "true" : "false");
        nativeSettings.paint();
    }

    static LinearLayout frow(final NativeSettings nativeSettings, final Features.F f, JSONObject jSONObject, boolean z, boolean z2) {
        String str;
        String str2;
        boolean z3;
        Kit kit;
        String str3;
        final String str4;
        Kit kit2 = nativeSettings.k;
        boolean on = nativeSettings.F.on(f.id);
        String t = t("aif.f." + f.id + ".title", new Object[0]);
        JSONObject optJSONObject = jSONObject.optJSONObject("on");
        Object opt = optJSONObject == null ? null : optJSONObject.opt(f.id);
        final boolean booleanValue = opt instanceof Boolean ? ((Boolean) opt).booleanValue() : f.def;
        Kit.Toggle toggle = kit2.toggle(on, t, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScrAssist.lambda$frow$56(nativeSettings, f, booleanValue, (Boolean) obj);
            }
        });
        if (z && !"mute".equals(f.id) && !"redact".equals(f.id)) {
            toggle.enable(false);
        }
        LinearLayout rowWrap = kit2.rowWrap(t, t("aif.f." + f.id + ".blurb", new Object[0]), toggle);
        rowWrap.setGravity(48);
        LinearLayout linearLayout = (LinearLayout) rowWrap.getChildAt(0);
        String str5 = "n";
        if (f.auto) {
            TextView textView = (TextView) linearLayout.getChildAt(0);
            textView.setText(t + "  ");
            LinearLayout linearLayout2 = new LinearLayout(nativeSettings.c);
            linearLayout2.setGravity(16);
            linearLayout.removeView(textView);
            linearLayout2.addView(textView);
            linearLayout2.addView(kit2.chip(t("aif.auto", "n", Integer.valueOf(f.calls)), 0));
            linearLayout.addView(linearLayout2, 0);
        }
        List<String> needs = nativeSettings.F.needs(f.id);
        List<String> chips = Features.chips(needs, z2);
        if (z2 && needs.contains("ai")) {
            rowWrap.setAlpha(0.45f);
            toggle.enable(false);
        }
        if (chips.isEmpty()) {
            str = t;
            str2 = "n";
            z3 = on;
        } else {
            Flow flow = kit2.flow(4);
            Iterator<String> it = chips.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if ("ai".equals(next)) {
                    str4 = "ai";
                } else {
                    str4 = "health";
                    if (!"health".equals(next)) {
                        str4 = "camera".equals(next) ? "" : "perm:" + next;
                    }
                }
                String str6 = t;
                String str7 = str5;
                boolean z4 = on;
                Iterator<String> it2 = it;
                TextView chip = kit2.chip(t("aif.need." + next, new Object[0]) + " · " + t("aif.fix", new Object[0]), kit2.ALT);
                chip.setClickable(true);
                chip.setFocusable(true);
                chip.setContentDescription(t("aif.need." + next, new Object[0]) + ", " + t("aif.fix", new Object[0]));
                if (!str4.isEmpty()) {
                    chip.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda15
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            nativeSettings.m354lambda$fixRow$10$comaugmaososettingsNativeSettings(str4);
                        }
                    });
                }
                flow.addView(chip);
                t = str6;
                str5 = str7;
                on = z4;
                it = it2;
            }
            str = t;
            str2 = str5;
            z3 = on;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.topMargin = nativeSettings.px(4.0f);
            linearLayout.addView(flow, layoutParams);
        }
        if ("tone".equals(f.id) && z3) {
            String[] strArr = {"concise", "friendly", "formal"};
            kit = kit2;
            String[] strArr2 = new String[3];
            for (int i = 0; i < 3; i++) {
                strArr2[i] = t("aif.tone." + strArr[i], new Object[0]);
            }
            String str8 = str;
            str3 = str2;
            LinearLayout seg = kit.seg(strArr2, strArr, String.valueOf(nativeSettings.F.pref("tone", "concise")), str8, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda16
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.lambda$frow$58(nativeSettings, (String) obj);
                }
            });
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.topMargin = nativeSettings.px(6.0f);
            linearLayout.addView(seg, layoutParams2);
            String[] strArr3 = {"short", "normal", "long"};
            String[] strArr4 = new String[3];
            int i2 = 0;
            for (int i3 = 3; i2 < i3; i3 = 3) {
                strArr4[i2] = t("aif.len." + strArr3[i2], new Object[0]);
                i2++;
            }
            LinearLayout seg2 = kit.seg(strArr4, strArr3, String.valueOf(nativeSettings.F.pref("len", "short")), str8, new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda17
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.lambda$frow$59(nativeSettings, (String) obj);
                }
            });
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams3.topMargin = nativeSettings.px(4.0f);
            linearLayout.addView(seg2, layoutParams3);
        } else {
            kit = kit2;
            str3 = str2;
        }
        if ("digest".equals(f.id) && z3) {
            final String valueOf = String.valueOf(nativeSettings.F.pref("digestAt", "18:00"));
            TextView btn = kit.btn(valueOf, true, true, null);
            btn.setContentDescription(t("aif.digest.at", new Object[0]) + " " + valueOf);
            btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda18
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ScrAssist.lambda$frow$61(valueOf, nativeSettings, view);
                }
            });
            linearLayout.addView(kit.hrow(kit.mono(t("aif.digest.at", new Object[0]), 9.0f, kit.DIM), btn));
        }
        if ("mute".equals(f.id) && z3) {
            JSONArray optJSONArray = jSONObject.optJSONArray("mute");
            View[] viewArr = new View[1];
            viewArr[0] = kit.btn(t("aif.mute.pick", str3, Integer.valueOf(optJSONArray == null ? 0 : optJSONArray.length())), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    nativeSettings.m356lambda$navRow$7$comaugmaososettingsNativeSettings("aifmute");
                }
            });
            linearLayout.addView(kit.hrow(viewArr));
        }
        return rowWrap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$frow$56(NativeSettings nativeSettings, Features.F f, boolean z, Boolean bool) {
        nativeSettings.F.set(f.id, !z);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$frow$58(NativeSettings nativeSettings, String str) {
        nativeSettings.F.setPref("tone", str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$frow$59(NativeSettings nativeSettings, String str) {
        nativeSettings.F.setPref("len", str);
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$frow$61(String str, final NativeSettings nativeSettings, View view) {
        int i = 18;
        int i2 = 0;
        try {
            i = Integer.parseInt(str.substring(0, 2));
            i2 = Integer.parseInt(str.substring(3, 5));
        } catch (Exception e) {
        }
        new TimePickerDialog(nativeSettings.c, new TimePickerDialog.OnTimeSetListener() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda64
            @Override // android.app.TimePickerDialog.OnTimeSetListener
            public final void onTimeSet(TimePicker timePicker, int i3, int i4) {
                ScrAssist.lambda$frow$60(nativeSettings, timePicker, i3, i4);
            }
        }, i, i2, true).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$frow$60(NativeSettings nativeSettings, TimePicker timePicker, int i, int i2) {
        nativeSettings.F.setPref("digestAt", String.format(Locale.US, "%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2)));
        nativeSettings.paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void leave(NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        nativeSettings.add(nativeSettings.hd(up(t("aif.leave", new Object[0])), t("aif.leave.hd", new Object[0])));
        nativeSettings.add(kit.note("M4 7.5h8v6.5H4zM5.5 7.5V5a2.5 2.5 0 0 1 5 0v2.5", kit.ACC, t("aif.leave.h", new Object[0]), t("aif.leave.p", new Object[0]), null, true));
        for (String str : GROUPS) {
            ArrayList arrayList = new ArrayList();
            for (Features.F f : Features.LIST) {
                if (f.group.equals(str)) {
                    arrayList.add(f);
                }
            }
            if (!arrayList.isEmpty()) {
                nativeSettings.sec(up(t("aif.g." + str, new Object[0])));
                LinearLayout list = nativeSettings.list();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Features.F f2 = (Features.F) it.next();
                    boolean on = nativeSettings.F.on(f2.id);
                    String t = t("aif.f." + f2.id + ".title", new Object[0]);
                    String t2 = t("aif.f." + f2.id + ".sends", new Object[0]);
                    View[] viewArr = new View[1];
                    viewArr[0] = kit.chip(on ? "ON" : "OFF", on ? kit.ACC : 0);
                    kit.add(list, kit.rowWrap(t, t2, viewArr));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void mute(final NativeSettings nativeSettings) {
        Kit kit = nativeSettings.k;
        JSONArray optJSONArray = nativeSettings.F.st().optJSONArray("mute");
        ArrayList arrayList = new ArrayList();
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        nativeSettings.add(nativeSettings.hd(up(t("aif.mute.title", new Object[0])), t("aif.mute.hd", new Object[0])));
        LinearLayout list = nativeSettings.list();
        String appsCached = nativeSettings.be.appsCached();
        if (appsCached == null) {
            kit.add(list, kit.row(null, 0, t("dr.loading", new Object[0]), null, false, null, new View[0]));
            Backend backend = nativeSettings.be;
            Objects.requireNonNull(nativeSettings);
            backend.appsReady(new NativeSettings$$ExternalSyntheticLambda25(nativeSettings));
            return;
        }
        JSONArray JA = NativeSettings.JA(appsCached);
        ArrayList arrayList2 = new ArrayList();
        HashSet hashSet = new HashSet();
        for (int i2 = 0; i2 < JA.length(); i2++) {
            JSONObject optJSONObject = JA.optJSONObject(i2);
            if (optJSONObject != null && hashSet.add(optJSONObject.optString("pkg"))) {
                arrayList2.add(optJSONObject);
            }
        }
        Collections.sort(arrayList2, new Comparator() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda62
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareToIgnoreCase;
                compareToIgnoreCase = ((JSONObject) obj).optString("label").compareToIgnoreCase(((JSONObject) obj2).optString("label"));
                return compareToIgnoreCase;
            }
        });
        if (arrayList2.isEmpty()) {
            kit.add(list, kit.row(null, 0, t("needsapp", new Object[0]), null, false, null, new View[0]));
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            JSONObject jSONObject = (JSONObject) it.next();
            final String optString = jSONObject.optString("pkg");
            kit.add(list, kit.toggleRow(jSONObject.optString("label"), null, arrayList.contains(optString), new Consumer() { // from class: com.jarvis.assist.aosui.settings.ScrAssist$$ExternalSyntheticLambda63
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrAssist.lambda$mute$64(nativeSettings, optString, (Boolean) obj);
                }
            }));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$mute$64(NativeSettings nativeSettings, String str, Boolean bool) {
        JSONObject st = nativeSettings.F.st();
        JSONArray optJSONArray = st.optJSONArray("mute");
        ArrayList arrayList = new ArrayList();
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        if (arrayList.contains(str)) {
            arrayList.remove(str);
        } else {
            arrayList.add(str);
        }
        try {
            st.put("mute", new JSONArray((Collection) arrayList));
        } catch (Exception e) {
        }
        nativeSettings.F.save(st);
    }
}
