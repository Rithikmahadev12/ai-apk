package com.jarvis.assist.aosui.home;

import android.text.InputFilter;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.Profile;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Sheet;

/* loaded from: /tmp/augmaos/classes.dex */
public final class YouSheet {
    private YouSheet() {
    }

    public static void show(FrameLayout frameLayout, Theme theme, Backend backend, final Runnable runnable) {
        Kit kit = new Kit(frameLayout.getContext(), theme, 1.0f);
        Sheet title = new Sheet(frameLayout.getContext(), theme, Strings.t("you.h")).title(Strings.t("you.h"));
        title.para(Strings.t("you.p"), 13.0f, 0.75f);
        final EditText input = kit.input(Store.get(Profile.KEY, ""), Strings.t("you.ph"), false, false, 8193);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        input.setContentDescription(Strings.t("you.ph"));
        title.body().addView(input);
        title.buttons(new String[]{Strings.t("you.later"), Strings.t("you.save")}, new Runnable[]{new Runnable() { // from class: com.jarvis.assist.aosui.home.YouSheet$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                YouSheet.lambda$show$0(runnable);
            }
        }, new Runnable() { // from class: com.jarvis.assist.aosui.home.YouSheet$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                YouSheet.lambda$show$1(input, runnable);
            }
        }}, true);
        title.show(frameLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$show$0(Runnable runnable) {
        Store.set(Profile.CHIP, String.valueOf(System.currentTimeMillis()));
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$show$1(EditText editText, Runnable runnable) {
        String clean = Profile.clean(editText.getText().toString());
        if (clean.isEmpty()) {
            Store.remove(Profile.KEY);
        } else {
            Store.set(Profile.KEY, clean);
        }
        runnable.run();
    }
}
