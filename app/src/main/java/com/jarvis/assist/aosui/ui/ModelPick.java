package com.jarvis.assist.aosui.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.ai.ModelList;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class ModelPick {
    private ModelPick() {
    }

    public static TextView select(final Kit kit, final FrameLayout frameLayout, final String str, final String str2, final String[] strArr, final String str3, final boolean z, final BiConsumer<String, Consumer<String>> biConsumer, final TextView textView, final Consumer<String> consumer) {
        List<String[]> decode = ModelList.decode(str, Store.get(ModelList.cacheKey(str)));
        String[][] rows = rows(str, decode, strArr, str3, z);
        TextView select = kit.select(frameLayout, str2, rows[0], rows[1], str3, new Consumer() { // from class: com.jarvis.assist.aosui.ui.ModelPick$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ModelPick.lambda$select$0((String) obj);
            }
        });
        select.setText(ModelList.name(decode, str3));
        select.setContentDescription(str2 + ", " + ModelList.name(decode, str3));
        select.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.ModelPick$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ModelPick.open(kit, frameLayout, str, str2, strArr, str3, z, biConsumer, textView, consumer);
            }
        });
        return select;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$select$0(String str) {
    }

    static String[][] rows(String str, List<String[]> list, String[] strArr, String str2, boolean z) {
        return ModelList.rows(str, list, strArr, str2, z, Strings.t("ns.model.rec"), Strings.t("ns.model.custom"), Strings.t("ns.model.refresh"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void open(final Kit kit, final FrameLayout frameLayout, final String str, final String str2, final String[] strArr, final String str3, final boolean z, final BiConsumer<String, Consumer<String>> biConsumer, final TextView textView, final Consumer<String> consumer) {
        String[][] rows = rows(str, ModelList.decode(str, Store.get(ModelList.cacheKey(str))), strArr, str3, z);
        kit.pickSheet(frameLayout, str2, rows[0], rows[1], rows[2], str3, new Consumer() { // from class: com.jarvis.assist.aosui.ui.ModelPick$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ModelPick.lambda$open$3(textView, biConsumer, str, kit, frameLayout, str2, strArr, str3, z, consumer, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$open$2(TextView textView, Kit kit, FrameLayout frameLayout, String str, String str2, String[] strArr, String str3, boolean z, BiConsumer biConsumer, Consumer consumer, String str4) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str4);
        } catch (Exception e) {
            jSONObject = new JSONObject();
        }
        if (textView != null) {
            textView.setText(jSONObject.has("models") ? Strings.t("ns.model.n", "n", Integer.valueOf(jSONObject.optJSONArray("models").length())) : jSONObject.optString("error", Strings.t("ns.keyfail")).toUpperCase(Locale.getDefault()));
        }
        if (jSONObject.has("models")) {
            open(kit, frameLayout, str, str2, strArr, str3, z, biConsumer, textView, consumer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$open$3(final TextView textView, final BiConsumer biConsumer, final String str, final Kit kit, final FrameLayout frameLayout, final String str2, final String[] strArr, final String str3, final boolean z, final Consumer consumer, String str4) {
        if (ModelList.REFRESH.equals(str4)) {
            if (textView != null) {
                textView.setText(Strings.t("ns.checking"));
            }
            biConsumer.accept(str, new Consumer() { // from class: com.jarvis.assist.aosui.ui.ModelPick$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ModelPick.lambda$open$2(textView, kit, frameLayout, str, str2, strArr, str3, z, biConsumer, consumer, (String) obj);
                }
            });
        } else if (ModelList.CUSTOM.equals(str4)) {
            custom(kit, frameLayout, str, str2, str3, textView, consumer);
        } else {
            picked(frameLayout, str);
            consumer.accept(str4);
        }
    }

    static void picked(FrameLayout frameLayout, String str) {
        frameLayout.getContext().getSharedPreferences("home", 0).edit().putBoolean("ai.model." + str + ".picked", true).apply();
    }

    static void custom(Kit kit, final FrameLayout frameLayout, final String str, String str2, String str3, final TextView textView, final Consumer<String> consumer) {
        Sheet title = new Sheet(frameLayout.getContext(), kit.th, str2).title(Strings.t("ns.model.customh").toUpperCase(Locale.getDefault()));
        final EditText input = kit.input(str3 == null ? "" : str3, Strings.t("ns.model.customh"), false, true, 0);
        input.setContentDescription(Strings.t("ns.model.customh"));
        title.body().addView(input);
        title.buttons(new String[]{Strings.t("cancel"), Strings.t("confirm")}, new Runnable[]{null, new Runnable() { // from class: com.jarvis.assist.aosui.ui.ModelPick$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ModelPick.lambda$custom$4(str, input, textView, frameLayout, consumer);
            }
        }}, true);
        title.show(frameLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$custom$4(String str, EditText editText, TextView textView, FrameLayout frameLayout, Consumer consumer) {
        String custom = ModelList.custom(str, editText.getText().toString());
        if (custom != null) {
            picked(frameLayout, str);
            consumer.accept(custom);
        } else if (textView != null) {
            textView.setText(Strings.t("ns.model.bad"));
        }
    }
}
