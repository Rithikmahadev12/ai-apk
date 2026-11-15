package com.jarvis.assist.aosui.home;

import android.widget.FrameLayout;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.ui.Sheet;

/* loaded from: /tmp/augmaos/classes.dex */
final class PersonSheet {
    private PersonSheet() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void show(FrameLayout frameLayout, Theme theme, final Backend backend, String str, String str2) {
        final String replaceAll = str2 != null ? str2.replaceAll("[^\\d+]", "") : "";
        new Sheet(frameLayout.getContext(), theme, str).title(str).para(str2, 13.0f, 0.7f).buttons(new String[]{Strings.t("close"), Strings.t("w.people.sms"), Strings.t("w.people.call")}, new Runnable[]{null, new Runnable() { // from class: com.jarvis.assist.aosui.home.PersonSheet$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Backend.this.url("smsto:" + replaceAll);
            }
        }, new Runnable() { // from class: com.jarvis.assist.aosui.home.PersonSheet$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Backend.this.url("tel:" + replaceAll);
            }
        }}, true).show(frameLayout);
    }
}
