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

public final class VoiceSheet {
    private VoiceSheet() { }

    public static void show(final FrameLayout frameLayout, Theme theme, final Backend backend, final Runnable runnable) {
        final Kit kit = new Kit(frameLayout.getContext(), theme, 1.0f);
        Sheet title = new Sheet(frameLayout.getContext(), theme, Strings.t("vo.h")).title(Strings.t("vo.h"));
        title.para(Strings.t("vo.p"), 13.0f, 0.75f);
        final LinearLayout list = kit.list();
        title.body().addView(list);
        list.addView(kit.msg(Strings.t("set.v.checking")));
        backend.ttsVoices(new Consumer<String>() {
            @Override public void accept(final String str) {
                frameLayout.post(new Runnable() {
                    @Override public void run() {
                        lambda$show$0(list, str, backend, kit);
                    }
                });
            }
        });
        title.buttons(new String[]{Strings.t("cz.done")}, new Runnable[]{new Runnable() {
            @Override public void run() {
                lambda$show$2(backend, runnable);
            }
        }}, true);
        title.show(frameLayout);
    }

    static void lambda$show$0(LinearLayout linearLayout, String str, Backend backend, Kit kit) {
        JSONObject jSONObject;
        linearLayout.removeAllViews();
        try { jSONObject = new JSONObject(str == null ? "{}" : str); }
        catch (Exception e) { jSONObject = new JSONObject(); }
        String cur = backend.get("ai.voice.name");
        String str3 = cur == null ? "" : cur;
        row(kit, linearLayout, backend, "", Strings.t("set.v.auto") + (jSONObject.optString("auto").isEmpty() ? "" : " · " + jSONObject.optString("auto")), Strings.t("vo.auto.sub"), str3.isEmpty());
        JSONArray arr = jSONObject.optJSONArray("voices");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.optJSONObject(i);
                if (o != null) {
                    int q = o.optInt("quality");
                    row(kit, linearLayout, backend, o.optString("name"), o.optString("name"),
                        Strings.t(q >= 500 ? "set.v.q.vhigh" : q >= 400 ? "set.v.q.high" : "set.v.q.norm") + " · " + Strings.t(o.optBoolean("net") ? "set.v.net" : "set.v.offline"),
                        o.optString("name").equals(str3));
                }
            }
        }
        kit.add(linearLayout, kit.row(null, 0, Strings.t("vo.kokoro"), Strings.t("vo.kokoro.sub"), false, null, kit.chip(Strings.t("vo.soon"), 0)));
    }

    static void lambda$show$2(Backend backend, Runnable runnable) {
        backend.sayStop();
        if (runnable != null) runnable.run();
    }

    static void row(Kit kit, LinearLayout linearLayout, final Backend backend, final String name, String label, String sub, boolean sel) {
        final Runnable pick = new Runnable() {
            @Override public void run() { backend.set("ai.voice.name", name); }
        };
        kit.add(linearLayout, kit.row(null, 0, label + (sel ? "  \u2713" : ""), sub, false, pick,
            kit.btn(Strings.t("vo.preview"), true, true, new Runnable() {
                @Override public void run() { pick.run(); backend.sayStop(); backend.speak(new JSONArray().put(Strings.t("vo.sample")).toString()); }
            })));
    }
}
