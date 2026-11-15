package com.jarvis.assist.aosui.home;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Sheet;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class VoiceSheet {
    private VoiceSheet() {
    }

    public static void show(final FrameLayout frameLayout, Theme theme, final Backend backend, final Runnable runnable) {
        final Kit kit = new Kit(frameLayout.getContext(), theme, 1.0f);
        Sheet title = new Sheet(frameLayout.getContext(), theme, Strings.t("vo.h")).title(Strings.t("vo.h"));
        title.para(Strings.t("vo.p"), 13.0f, 0.75f);
        final LinearLayout list = kit.list();
        title.body().addView(list);
        list.addView(kit.msg(Strings.t("set.v.checking")));
        backend.ttsVoices(new Consumer() { // from class: com.jarvis.assist.aosui.home.VoiceSheet$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                frameLayout.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.VoiceSheet$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoiceSheet.lambda$show$0(r1, r2, r3, r4);
                    }
                });
            }
        });
        title.buttons(new String[]{Strings.t("cz.done")}, new Runnable[]{new Runnable() { // from class: com.jarvis.assist.aosui.home.VoiceSheet$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                VoiceSheet.lambda$show$2(Backend.this, runnable);
            }
        }}, true);
        title.show(frameLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$show$0(LinearLayout linearLayout, String str, Backend backend, Kit kit) {
        JSONObject jSONObject;
        linearLayout.removeAllViews();
        try {
            jSONObject = new JSONObject(str == null ? "{}" : str);
        } catch (Exception e) {
            jSONObject = new JSONObject();
        }
        String str2 = backend.get("ai.voice.name");
        String str3 = str2 == null ? "" : str2;
        row(kit, linearLayout, backend, "", Strings.t("set.v.auto") + (jSONObject.optString("auto").isEmpty() ? "" : " · " + jSONObject.optString("auto")), Strings.t("vo.auto.sub"), str3.isEmpty());
        JSONArray optJSONArray = jSONObject.optJSONArray("voices");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    int optInt = optJSONObject.optInt("quality");
                    row(kit, linearLayout, backend, optJSONObject.optString("name"), optJSONObject.optString("name"), Strings.t(optInt >= 500 ? "set.v.q.vhigh" : optInt >= 400 ? "set.v.q.high" : "set.v.q.norm") + " · " + Strings.t(optJSONObject.optBoolean("net") ? "set.v.net" : "set.v.offline"), optJSONObject.optString("name").equals(str3));
                }
            }
        }
        kit.add(linearLayout, kit.row(null, 0, Strings.t("vo.kokoro"), Strings.t("vo.kokoro.sub"), false, null, kit.chip(Strings.t("vo.soon"), 0)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$show$2(Backend backend, Runnable runnable) {
        backend.sayStop();
        if (runnable != null) {
            runnable.run();
        }
    }

    static void row(Kit kit, LinearLayout linearLayout, final Backend backend, final String str, String str2, String str3, boolean z) {
        final Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.home.VoiceSheet$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Backend.this.set("ai.voice.name", str);
            }
        };
        kit.add(linearLayout, kit.row(null, 0, str2 + (z ? "  ✓" : ""), str3, false, runnable, kit.btn(Strings.t("vo.preview"), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.home.VoiceSheet$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                VoiceSheet.lambda$row$4(runnable, backend);
            }
        })));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$row$4(Runnable runnable, Backend backend) {
        runnable.run();
        backend.sayStop();
        backend.speak(new JSONArray().put(Strings.t("vo.sample")).toString());
    }
}
