package com.jarvis.assist.aosui.home;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.ui.Sheet;
import java.util.function.Consumer;

/* loaded from: /tmp/augmaos/classes.dex */
public final class LockSheet {
    private LockSheet() {
    }

    static boolean consented() {
        return "1".equals(Store.get("aos.lockconsent")) || "1".equals(Store.home("aos.lockconsent"));
    }

    public static void ask(FrameLayout frameLayout, Theme theme, Backend backend, boolean z) {
        ask(frameLayout, theme, backend, z, null);
    }

    public static void ask(FrameLayout frameLayout, Theme theme, final Backend backend, final boolean z, final Consumer<Boolean> consumer) {
        if (!z && consented()) {
            setup(backend, "service");
            if (consumer != null) {
                consumer.accept(true);
                return;
            }
            return;
        }
        Sheet title = new Sheet(frameLayout.getContext(), theme, Strings.t("lock.h")).title(Strings.t("lock.h"));
        if (z) {
            title.para(Strings.t("lock.p.admin"), 14.0f, 1.0f);
        } else {
            title.para(Strings.t("lock.d.p"), 14.0f, 1.0f).head(Strings.t("lock.d.does.h")).bullets(Strings.t("lock.d.does")).head(Strings.t("lock.d.not.h")).bullets(Strings.t("lock.d.not")).para(Strings.t("lock.d.off"), 13.0f, 0.85f);
        }
        final boolean[] zArr = {false};
        title.onDismiss = new Runnable() { // from class: com.jarvis.assist.aosui.home.LockSheet$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LockSheet.lambda$ask$0(zArr, consumer);
            }
        };
        title.buttons(new String[]{Strings.t("lock.no"), Strings.t("lock.agree")}, new Runnable[]{new Runnable() { // from class: com.jarvis.assist.aosui.home.LockSheet$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                LockSheet.lambda$ask$1(zArr, consumer);
            }
        }, new Runnable() { // from class: com.jarvis.assist.aosui.home.LockSheet$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                LockSheet.lambda$ask$2(zArr, z, backend, consumer);
            }
        }}, true).show(frameLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ask$0(boolean[] zArr, Consumer consumer) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        if (consumer != null) {
            consumer.accept(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ask$1(boolean[] zArr, Consumer consumer) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        if (consumer != null) {
            consumer.accept(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$ask$2(boolean[] zArr, boolean z, Backend backend, Consumer consumer) {
        zArr[0] = true;
        if (!z) {
            Store.set("aos.lockconsent", "1");
            Store.setHome("aos.lockconsent", "1");
        }
        setup(backend, z ? "admin" : "service");
        if (consumer != null) {
            consumer.accept(true);
        }
    }

    static void setup(Backend backend, String str) {
        Activity activity = backend.activity();
        if (!"admin".equals(str) && Build.VERSION.SDK_INT >= 28) {
            try {
                activity.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS").addFlags(268435456));
                return;
            } catch (ActivityNotFoundException | SecurityException e) {
                toast(activity, "No accessibility settings here: using device admin instead");
            }
        }
        try {
            activity.startActivity(new Intent("android.app.action.ADD_DEVICE_ADMIN").putExtra("android.app.extra.DEVICE_ADMIN", new ComponentName(activity, "com.jarvis.assist.aosui.LockAdmin")).putExtra("android.app.extra.ADD_EXPLANATION", "Lets aOS's lock action turn the screen off and lock the phone. Only the lock permission is used. Your PIN is needed the next time you unlock."));
        } catch (Exception e2) {
            toast(activity, "Could not open the lock settings");
        }
    }

    static void toast(Context context, String str) {
        try {
            Toast.makeText(context, str, 0).show();
        } catch (Exception e) {
        }
    }
}
