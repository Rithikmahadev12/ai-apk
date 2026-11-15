package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class VaultLink {
    public static final String APP = "Vault";
    public static final String GET_URL = "https://augmaimaginarium.com/dev/project/?id=vault";
    public static final String PKG = "com.augma.aos.vault";

    /* loaded from: /tmp/augmaos/classes.dex */
    public enum Route {
        LAUNCH,
        GET,
        NONE
    }

    private VaultLink() {
    }

    public static String target(String str) {
        if (str == null || !("vault".equals(str) || "app:Vault".equals(str) || APP.equals(str))) {
            return null;
        }
        return APP;
    }

    public static Route route(String str, boolean z) {
        return target(str) == null ? Route.NONE : z ? Route.LAUNCH : Route.GET;
    }
}
