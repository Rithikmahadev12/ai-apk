package com.jarvis.assist.aosui.home;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.FrameLayout;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.VaultLink;
import com.jarvis.assist.aosui.ui.Sheet;

/* loaded from: /tmp/augmaos/classes.dex */
public final class GetVault {
    private GetVault() {
    }

    public static void open(Activity activity) {
        Intent intent;
        try {
            intent = activity.getPackageManager().getLaunchIntentForPackage(VaultLink.PKG);
        } catch (Exception e) {
            intent = null;
        }
        if (VaultLink.route(VaultLink.APP, intent != null) == VaultLink.Route.LAUNCH) {
            try {
                activity.startActivity(intent.addFlags(268435456));
                return;
            } catch (Exception e2) {
            }
        }
        sheet(activity);
    }

    static void sheet(final Activity activity) {
        if (activity.findViewById(R.id.content) instanceof FrameLayout) {
            FrameLayout frameLayout = (FrameLayout) activity.findViewById(R.id.content);
            Theme theme = new Theme();
            theme.load(Store.get("aos.theme", "dark"), Store.obj("aos.colors"), Store.obj("aos.ui"), Perf.k().blur, activity.getResources().getDisplayMetrics().density);
            new Sheet(activity, theme, "Get Vault").title("GET VAULT").para("Vault is a separate app for private notes and files, encrypted on this phone.", 15.0f, 0.9f).para("It keeps its data only in its own app storage — nothing goes to aOS, a server or a backup.", 13.0f, 0.7f).buttons(new String[]{"NOT NOW", "GET VAULT"}, new Runnable[]{null, new Runnable() { // from class: com.jarvis.assist.aosui.home.GetVault$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(VaultLink.GET_URL)).addFlags(268435456));
                }
            }}, true).show(frameLayout);
        }
    }
}
