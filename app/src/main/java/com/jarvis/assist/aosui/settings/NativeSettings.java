package com.jarvis.assist.aosui.settings;

import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.GoogleLink;
import com.jarvis.assist.aosui.ai.AndroidHost;
import com.jarvis.assist.aosui.ai.Features;
import com.jarvis.assist.aosui.core.AppGridModel;
import com.jarvis.assist.aosui.core.CrashModel;
import com.jarvis.assist.aosui.core.Drafts;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.HomeSlots;
import com.jarvis.assist.aosui.core.MemPolicy;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Profile;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.core.VaultLink;
import com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda16;
import com.jarvis.assist.aosui.home.YouSheet;
import com.jarvis.assist.aosui.onboard.Welcome;
import com.jarvis.assist.aosui.ui.Flow;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Sheet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class NativeSettings {
    static final String IC_CLOUD = "M4.5 12.5h7a2.8 2.8 0 0 0 .3-5.6A4 4 0 0 0 4.1 7.6a2.5 2.5 0 0 0 .4 4.9z";
    static final String IC_GLOBE = "M8 2a6 6 0 1 0 0 12A6 6 0 0 0 8 2zM2 8h12M8 2c2 2 2 10 0 12M8 2c-2 2-2 10 0 12";
    static final String IC_GRID = "M2.5 2.5h4v4h-4zM9.5 2.5h4v4h-4zM2.5 9.5h4v4h-4zM9.5 9.5h4v4h-4z";
    static final String IC_HEART = "M8 13.5S2 9.8 2 5.8A3 3 0 0 1 8 4.6a3 3 0 0 1 6 1.2c0 4-6 7.7-6 7.7z";
    static final String IC_HOME = "M2.5 8L8 3l5.5 5v5.5h-11zM6.5 13.5v-3h3v3";
    static final String IC_INFO = "M8 2a6 6 0 1 0 0 12A6 6 0 0 0 8 2zM8 7.5v4M8 5h0";
    static final String IC_KEY = "M10.5 2.5a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM8.3 7.7L2.5 13.5M4 12l1.5 1.5";
    static final String IC_LOCK = "M4 7.5h8v6.5H4zM5.5 7.5V5a2.5 2.5 0 0 1 5 0v2.5";
    static final String IC_PALETTE = "M8 2a6 6 0 1 0 0 12c1 0 1.2-.8.8-1.4-.6-.8 0-1.6.8-1.6H11a3 3 0 0 0 3-3A6 6 0 0 0 8 2z";
    static final String IC_SAVE = "M8 2.5v7M5 6.5l3 3 3-3M3 11v2.5h10V11";
    static final String IC_SPARK = "M8 1.5l1.6 4.9L14.5 8l-4.9 1.6L8 14.5l-1.6-4.9L1.5 8l4.9-1.6z";
    static final String IC_TYPE = "M3 4V2.5h10V4M8 2.5V13.5M6 13.5h4";
    static final String IC_WAVE = "M2 8h1.5M5 5v6M8 3v10M11 5.5v5M13.5 8H14";
    final Features F;
    final Backend be;
    boolean bkImpNotes;
    boolean bkNotes;
    List<SettingsModel.Plan> bkPlan;
    final LinearLayout body;
    final Context c;
    final float dn;
    boolean hgBusy;
    JSONObject hgRes;
    Kit k;
    final Runnable mirror;
    boolean perfTesting;
    boolean resumed;
    final FrameLayout root;
    final ScrollView sc;
    int secN;
    String ttsVoices;
    String voiceInfo;
    static final Handler MAIN = new Handler(Looper.getMainLooper());
    static final String[] PERMS = {"calendar", "contacts", "location", "mic", "notifAccess", "usage"};
    static final String[][] LANGS = {new String[]{"en", "English"}};
    final Kit.Grid[] bg0 = {null};
    final Theme th = new Theme();
    final ArrayList<String> stack = new ArrayList<>();
    String lastScreen = "";
    String hgSrc = "";
    String hgTest = "";
    String hgMsg = "";
    String bkMsg = "";
    final Runnable onStore = new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            NativeSettings.this.m357lambda$new$0$comaugmaososettingsNativeSettings();
        }
    };
    final Drafts drafts = new Drafts();
    final ArrayList<Integer> scrolls = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m357lambda$new$0$comaugmaososettingsNativeSettings() {
        if (this.resumed) {
            paint();
        }
    }

    public NativeSettings(Backend backend) {
        this.be = backend;
        this.c = backend.ctx();
        Store.init(this.c);
        Fonts.init(this.c);
        Strings.init(this.c);
        Perf.init(this.c, backend.deviceInfo());
        Objects.requireNonNull(backend);
        this.mirror = new NativeHome$$ExternalSyntheticLambda16(backend);
        Perf.onMirror(this.mirror);
        this.dn = this.c.getResources().getDisplayMetrics().density;
        this.F = new Features(AndroidHost.forSettings(backend));
        this.root = new FrameLayout(this.c);
        this.sc = new ScrollView(this.c);
        this.sc.setFillViewport(true);
        this.sc.setVerticalScrollBarEnabled(false);
        this.sc.setOverScrollMode(2);
        this.body = new LinearLayout(this.c);
        this.body.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(this.c);
        frameLayout.addView(this.body, new FrameLayout.LayoutParams(-1, -2, 1));
        frameLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                NativeSettings.this.m358lambda$new$1$comaugmaososettingsNativeSettings(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        this.sc.addView(frameLayout, new FrameLayout.LayoutParams(-1, -2));
        this.root.addView(this.sc, new FrameLayout.LayoutParams(-1, -1));
        this.root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda2
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return NativeSettings.this.m359lambda$new$2$comaugmaososettingsNativeSettings(view, windowInsets);
            }
        });
        this.stack.add("root");
        Store.on(this.onStore);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m358lambda$new$1$comaugmaososettingsNativeSettings(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int round = Math.round(this.dn * 640.0f);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.body.getLayoutParams();
        if (i3 - i <= round) {
            round = -1;
        }
        if (layoutParams.width != round) {
            layoutParams.width = round;
            this.body.setLayoutParams(layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ WindowInsets m359lambda$new$2$comaugmaososettingsNativeSettings(View view, WindowInsets windowInsets) {
        int systemWindowInsetTop;
        int systemWindowInsetBottom;
        int i;
        int i2;
        if (Build.VERSION.SDK_INT >= 30) {
            Insets insets = windowInsets.getInsets(WindowInsets.Type.displayCutout());
            Insets insets2 = windowInsets.getInsets(WindowInsets.Type.ime());
            Insets insets3 = windowInsets.getInsets(WindowInsets.Type.systemBars());
            systemWindowInsetTop = Math.max(insets.top, insets3.top);
            systemWindowInsetBottom = Math.max(insets2.bottom, insets3.bottom);
            i = Math.max(insets.left, insets3.left);
            i2 = Math.max(insets.right, insets3.right);
        } else {
            systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
            systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
            i = 0;
            i2 = 0;
        }
        this.body.setPadding(px(16.0f) + i, Math.max(px(14.0f), systemWindowInsetTop), px(16.0f) + i2, px(40.0f) + systemWindowInsetBottom);
        return windowInsets;
    }

    public View view() {
        return this.root;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int px(float f) {
        return Math.round(f * this.dn);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String t(String str, Object... objArr) {
        return objArr.length == 0 ? Strings.t(str) : Strings.t(str, objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String up(String str) {
        return str == null ? "" : str.toUpperCase(Locale.getDefault());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JSONObject J(String str) {
        JSONObject jSONObject;
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    jSONObject = new JSONObject(str);
                    return jSONObject;
                }
            } catch (Exception e) {
                return new JSONObject();
            }
        }
        jSONObject = new JSONObject();
        return jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JSONArray JA(String str) {
        JSONArray jSONArray;
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    jSONArray = new JSONArray(str);
                    return jSONArray;
                }
            } catch (Exception e) {
                return new JSONArray();
            }
        }
        jSONArray = new JSONArray();
        return jSONArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String pref(String str, String str2) {
        String str3 = this.be.get(str);
        return (str3 == null || str3.isEmpty()) ? str2 : str3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBoth(String str, String str2) {
        Store.set(str, str2);
        this.be.set(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void toast(String str) {
        this.k.toast(this.root, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EditText keep(EditText editText, final String str) {
        String str2 = this.drafts.get(str);
        if (str2 != null) {
            editText.setText(str2);
        }
        editText.addTextChangedListener(new TextWatcher() { // from class: com.jarvis.assist.aosui.settings.NativeSettings.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                NativeSettings.this.drafts.put(str, editable.toString());
            }
        });
        return editText;
    }

    public void open(String str, String str2) {
        String t;
        StringBuilder sb;
        String str3;
        String[] route = SettingsModel.route(str);
        if (route == null) {
            paint();
            return;
        }
        this.stack.clear();
        this.scrolls.clear();
        this.stack.add("root");
        if ("hg".equals(route[0])) {
            this.stack.add("health");
            this.stack.add("hg");
            this.hgSrc = route[1];
            this.hgRes = str2 == null ? null : J(SettingsModel.res(str2));
            this.hgTest = "";
            this.hgMsg = "";
        } else {
            this.stack.add(route[0]);
        }
        paint();
        this.sc.scrollTo(0, 0);
        if (this.hgRes == null || !"hg".equals(route[0])) {
            return;
        }
        String up = up(ScrData.srcName(this.hgSrc));
        if (this.hgRes.optBoolean("ok")) {
            t = t("ns.hs.connected", new Object[0]);
            sb = new StringBuilder();
            str3 = " ";
        } else {
            t = this.hgRes.optString("error").isEmpty() ? t("ns.hs.signfail", new Object[0]) : this.hgRes.optString("error");
            sb = new StringBuilder();
            str3 = " · ";
        }
        toast(up + sb.append(str3).append(t).toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: go, reason: merged with bridge method [inline-methods] */
    public void m356lambda$navRow$7$comaugmaososettingsNativeSettings(String str) {
        while (this.scrolls.size() < this.stack.size()) {
            this.scrolls.add(0);
        }
        if (!this.stack.isEmpty()) {
            this.scrolls.set(this.stack.size() - 1, Integer.valueOf(this.sc.getScrollY()));
        }
        this.stack.add(str);
        paint();
        this.sc.scrollTo(0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void guide(String str) {
        this.hgSrc = str;
        this.hgMsg = "";
        this.hgTest = "";
        this.hgBusy = false;
        this.hgRes = null;
        m356lambda$navRow$7$comaugmaososettingsNativeSettings("hg");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBack$3$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m360lambda$onBack$3$comaugmaososettingsNativeSettings(int i) {
        this.sc.scrollTo(0, i);
    }

    public boolean onBack() {
        if (Sheet.closeTop()) {
            return true;
        }
        if (this.stack.size() <= 1) {
            return false;
        }
        this.stack.remove(this.stack.size() - 1);
        paint();
        int size = this.stack.size() - 1;
        final int intValue = size < this.scrolls.size() ? this.scrolls.get(size).intValue() : 0;
        this.sc.post(new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m360lambda$onBack$3$comaugmaososettingsNativeSettings(intValue);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void back() {
        if (onBack()) {
            return;
        }
        this.be.finish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fix, reason: merged with bridge method [inline-methods] */
    public void m354lambda$fixRow$10$comaugmaososettingsNativeSettings(String str) {
        if (str.startsWith("perm:")) {
            this.be.askPerm(str.substring(5));
        } else {
            open(str, null);
        }
    }

    public void onResume() {
        this.resumed = true;
        paint();
    }

    public void onPause() {
        this.resumed = false;
    }

    public void onDestroy() {
        Store.off(this.onStore);
        Perf.offMirror(this.mirror);
        this.resumed = false;
        Sheet.closeTop();
        MAIN.removeCallbacksAndMessages(null);
    }

    public void trim(int i, int i2) {
        if (this.resumed || !MemPolicy.has(i2, 8)) {
            return;
        }
        this.voiceInfo = null;
        this.ttsVoices = null;
    }

    public void onEvent(String str) {
        if (str == null) {
            return;
        }
        if (str.contains("onPerm") || str.contains("onHealth") || str.contains("onResume")) {
            paint();
        }
    }

    void theme() {
        this.th.load(Store.get("aos.theme", "dark"), Store.obj("aos.colors"), Store.obj("aos.ui"), Perf.k().blur, this.dn);
        float f = SettingsModel.ui(Store.get("aos.ui"))[2] / 100.0f;
        float f2 = this.c.getResources().getConfiguration().fontScale;
        this.k = new Kit(this.c, this.th, UnitsMath.viewZoom(f2) * f, UnitsMath.textZoom(f2) * f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0041, code lost:
    
        if (r13.bg0[0].getTag() != (r13.k.light ? "l" : "d")) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void paint() {
        /*
            Method dump skipped, instructions count: 263
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.settings.NativeSettings.paint():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$paint$4$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m361lambda$paint$4$comaugmaososettingsNativeSettings(int i) {
        this.sc.scrollTo(0, i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    String title(String str) {
        char c;
        switch (str.hashCode()) {
            case -1762401549:
                if (str.equals("custompages")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1760880356:
                if (str.equals("customquick")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1581216476:
                if (str.equals("customdock")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1581124041:
                if (str.equals("customgrid")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1396673086:
                if (str.equals("backup")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case -1240244679:
                if (str.equals("google")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -1221262756:
                if (str.equals("health")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case -793375513:
                if (str.equals(AppGridModel.ID)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -314498168:
                if (str.equals("privacy")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 3112:
                if (str.equals("ai")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 3282:
                if (str.equals("fx")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case 3208415:
                if (str.equals("home")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3327647:
                if (str.equals("look")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 3506402:
                if (str.equals("root")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 3556653:
                if (str.equals("text")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 92611469:
                if (str.equals("about")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 94005370:
                if (str.equals("brief")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 304302284:
                if (str.equals("customcontrol")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1223440372:
                if (str.equals("weather")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 1340337839:
                if (str.equals("widgets")) {
                    c = 2;
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
                return t("set.title", new Object[0]);
            case 1:
                return t("ns.home.h", new Object[0]);
            case 2:
                return t("set.widgets", new Object[0]);
            case 3:
                return t("wid.appgrid", new Object[0]);
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
                return t("cz.title", new Object[0]);
            case '\t':
                return t("db.set.row", new Object[0]);
            case '\n':
                return t("ns.look.h", new Object[0]);
            case 11:
                return t("set.text", new Object[0]);
            case '\f':
                return "GOOGLE";
            case FxModel.BOOT /* 13 */:
                return t("set.ai", new Object[0]);
            case FxModel.LOCK /* 14 */:
                return t("set.weather", new Object[0]);
            case 15:
                return t("set.health", new Object[0]);
            case 16:
                return t("set.privacy", new Object[0]);
            case FxModel.N_EV /* 17 */:
                return t("set.fx", new Object[0]);
            case 18:
                return t("set.backup", new Object[0]);
            case 19:
                return t("set.about", new Object[0]);
            default:
                return t("set.title", new Object[0]);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    void build(String str) {
        char c;
        switch (str.hashCode()) {
            case -1762401549:
                if (str.equals("custompages")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1760880356:
                if (str.equals("customquick")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1581216476:
                if (str.equals("customdock")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1581124041:
                if (str.equals("customgrid")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1396673086:
                if (str.equals("backup")) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case -1357535707:
                if (str.equals("clocks")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1240244679:
                if (str.equals("google")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case -1221262756:
                if (str.equals("health")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case -1024463815:
                if (str.equals("aifleave")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case -1002832809:
                if (str.equals("aifmute")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case -793375513:
                if (str.equals(AppGridModel.ID)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -314498168:
                if (str.equals("privacy")) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case 3112:
                if (str.equals("ai")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 3282:
                if (str.equals("fx")) {
                    c = 24;
                    break;
                }
                c = 65535;
                break;
            case 3327:
                if (str.equals("hg")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 96574:
                if (str.equals("aif")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 3208415:
                if (str.equals("home")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 3314158:
                if (str.equals("lang")) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 3327647:
                if (str.equals("look")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 3556653:
                if (str.equals("text")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 92611469:
                if (str.equals("about")) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case 94005370:
                if (str.equals("brief")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 304302284:
                if (str.equals("customcontrol")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1223440372:
                if (str.equals("weather")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case 1340337839:
                if (str.equals("widgets")) {
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
                ScrLayout.home(this);
                return;
            case 1:
                ScrLayout.widgets(this);
                return;
            case 2:
                ScrLayout.appGrid(this);
                return;
            case 3:
            case 4:
            case 5:
            case 6:
                ScrLayout.custom(this, HomeSlots.areaOf(str));
                return;
            case 7:
                ScrLayout.custom(this, null);
                this.body.post(new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda30
                    @Override // java.lang.Runnable
                    public final void run() {
                        NativeSettings.this.m351lambda$build$6$comaugmaososettingsNativeSettings();
                    }
                });
                return;
            case '\b':
                ScrLayout.clocks(this);
                return;
            case '\t':
                ScrLayout.brief(this);
                return;
            case '\n':
                ScrLook.look(this);
                return;
            case 11:
                ScrLook.text(this);
                return;
            case '\f':
                ScrAssist.ai(this);
                return;
            case FxModel.BOOT /* 13 */:
                ScrAssist.aif(this);
                return;
            case FxModel.LOCK /* 14 */:
                ScrAssist.leave(this);
                return;
            case 15:
                ScrAssist.mute(this);
                return;
            case 16:
                ScrData.google(this);
                return;
            case FxModel.N_EV /* 17 */:
                ScrData.weather(this);
                return;
            case 18:
                ScrData.health(this);
                return;
            case 19:
                ScrData.guide(this);
                return;
            case 20:
                privacy();
                return;
            case 21:
                about();
                return;
            case 22:
                lang();
                return;
            case 23:
                backup();
                return;
            case 24:
                ScrFx.screen(this);
                return;
            default:
                rootScreen();
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$build$5$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m350lambda$build$5$comaugmaososettingsNativeSettings() {
        this.sc.fullScroll(130);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$build$6$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m351lambda$build$6$comaugmaososettingsNativeSettings() {
        this.sc.post(new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m350lambda$build$5$comaugmaososettingsNativeSettings();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View hd(String str, String str2) {
        return this.k.header(str, str2, this.stack.size() > 1 ? new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.back();
            }
        } : null, t("back", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(View view) {
        this.body.addView(view);
    }

    LinearLayout s0list() {
        return list();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sec(String str) {
        LinearLayout linearLayout = this.body;
        Kit kit = this.k;
        Locale locale = Locale.ROOT;
        String str2 = Kit.ADV ? "INDEX %02d · %s" : "%02d / %s";
        int i = this.secN + 1;
        this.secN = i;
        linearLayout.addView(kit.section(String.format(locale, str2, Integer.valueOf(i), str)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fine(String str) {
        this.body.addView(this.k.fine(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LinearLayout list() {
        LinearLayout list = this.k.list();
        this.body.addView(list);
        return list;
    }

    View navRow(LinearLayout linearLayout, final String str, String str2, String str3, String str4) {
        return this.k.add(linearLayout, this.k.row(str2, this.k.ACC, str3, str4, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m356lambda$navRow$7$comaugmaososettingsNativeSettings(str);
            }
        }, new View[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JSONObject ai() {
        return J(this.be.aiHasKey());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JSONObject paInfo() {
        return J(this.be.assistantInfo());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean paMode() {
        return !ai().optBoolean("hasKey") || this.F.paused();
    }

    String provName(String str) {
        if ("openai".equals(str)) {
            return "OpenAI-compatible";
        }
        if (str.isEmpty()) {
            return "";
        }
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String paLine() {
        JSONObject ai = ai();
        JSONObject paInfo = paInfo();
        if (paMode()) {
            return t(HomeRules.paLineKey(true, this.be.get("ai.pa.fallback"), paInfo.optString("pkg")), "name", paInfo.optString("label").isEmpty() ? paInfo.optString("pkg") : paInfo.optString("label"));
        }
        String optString = ai.optString("provider");
        JSONObject optJSONObject = ai.optJSONObject(optString);
        return t("set.pa.mode.ai", "name", provName(optString) + ((optJSONObject == null || optJSONObject.optString("last4").isEmpty()) ? "" : " ••••" + optJSONObject.optString("last4")));
    }

    boolean calm() {
        return "1".equals(Store.get("aos.rm", "")) || this.be.powerSave();
    }

    void rootScreen() {
        SettingsModel.Cfg cfg = cfg();
        JSONObject ai = ai();
        JSONObject J = J(this.be.permState());
        int i = 0;
        for (String str : PERMS) {
            if (J.optBoolean(str)) {
                i++;
            }
        }
        add(hd(t("set.title", new Object[0]), t("set.sub", new Object[0])));
        connections();
        sec(t("set.customise", new Object[0]));
        LinearLayout list = list();
        StringBuilder sb = new StringBuilder();
        for (String str2 : cfg.wheel) {
            if (sb.length() > 0) {
                sb.append(" · ");
            }
            sb.append(ScrLayout.label("wheel", str2));
        }
        navRow(list, "home", IC_HOME, t("set.home", new Object[0]), sb.toString());
        navRow(list, "widgets", IC_GRID, t("set.widgets", new Object[0]), t("set.widgets.sub", "n", Integer.valueOf(cfg.onCount())));
        navRow(list, "look", IC_PALETTE, t("set.look", new Object[0]), up(Store.get("aos.theme", "dark")) + " · " + up(SettingsModel.color(Store.get("aos.colors"), "accent")));
        navRow(list, "text", IC_TYPE, t("set.text", new Object[0]), SettingsModel.ui(Store.get("aos.ui"))[2] + "% · " + Perf.tier() + (calm() ? " · " + t("set.text.calm", new Object[0]) : ""));
        String pref = pref("ai.name", "Assistant");
        StringBuilder sb2 = new StringBuilder();
        for (String str3 : SettingsModel.order(this.be.aiHasKey())) {
            JSONObject optJSONObject = ai.optJSONObject(str3);
            if (optJSONObject != null && optJSONObject.optBoolean("hasKey")) {
                if (sb2.length() > 0) {
                    sb2.append(" → ");
                }
                sb2.append("openai".equals(str3) ? "OPENAI-COMPATIBLE" : up(str3));
            }
        }
        String t = t("set.ai", new Object[0]);
        String up = up(pref);
        int length = sb2.length();
        CharSequence charSequence = sb2;
        if (length <= 0) {
            charSequence = up(paLine());
        }
        navRow(list, "ai", IC_SPARK, t, up + " · " + String.valueOf(charSequence));
        String[] wx = SettingsModel.wx(Store.get("aos.wxcfg"));
        navRow(list, "weather", IC_CLOUD, t("set.weather", new Object[0]), t("ns.wx.s." + wx[0], new Object[0]) + " · °" + up(wx[1]));
        sec(t("set.data", new Object[0]));
        LinearLayout list2 = list();
        navRow(list2, "health", IC_HEART, t("set.health", new Object[0]), ScrData.hsum(this));
        navRow(list2, "privacy", IC_LOCK, t("set.privacy", new Object[0]), t("set.privacy.sub", "n", Integer.valueOf(i), "of", Integer.valueOf(PERMS.length)));
        sec(t("set.system", new Object[0]));
        LinearLayout list3 = list();
        String str4 = Store.get("aos.lang", "");
        String t2 = t("set.lang", new Object[0]);
        if (!str4.isEmpty() && LANGS[0][0].equals(str4)) {
            str4 = LANGS[0][1];
        } else if (str4.isEmpty()) {
            str4 = t("lang.system", new Object[0]);
        }
        navRow(list3, "lang", IC_GLOBE, t2, str4);
        String str5 = Store.get(Profile.KEY, "");
        this.k.add(s0list(), this.k.row(null, 0, t("you.row", new Object[0]), str5.isEmpty() ? t("you.none", new Object[0]) : str5.toUpperCase(Locale.getDefault()), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m364lambda$rootScreen$8$comaugmaososettingsNativeSettings();
            }
        }, new View[0]));
        navRow(list3, "fx", IC_WAVE, t("set.fx", new Object[0]), ScrFx.sum());
        navRow(list3, "backup", IC_SAVE, t("set.backup", new Object[0]), t("set.backup.sub", new Object[0]));
        navRow(list3, "about", IC_INFO, t("set.about", new Object[0]), t("set.about.sub", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$rootScreen$8$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m364lambda$rootScreen$8$comaugmaososettingsNativeSettings() {
        YouSheet.show(this.root, this.th, this.be, new NativeSettings$$ExternalSyntheticLambda25(this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettingsModel.Cfg cfg() {
        return SettingsModel.cfg(Store.get("aos.cfg"), Store.get("aos.land"), this.be.appWidgets());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveCfg(SettingsModel.Cfg cfg) {
        String save = SettingsModel.save(Store.get("aos.cfg"), cfg);
        Store.set("aos.cfg", save);
        this.be.set("aos.cfg", save);
    }

    void connections() {
        ArrayList arrayList;
        int i;
        String str;
        int i2;
        String str2;
        boolean z;
        String t;
        int i3;
        String t2;
        LinearLayout linearLayout;
        String str3;
        JSONObject ai = ai();
        JSONObject J = J(this.be.permState());
        JSONObject J2 = J(this.be.healthSources());
        JSONObject J3 = J(this.be.weatherStatus());
        String[] wx = SettingsModel.wx(Store.get("aos.wxcfg"));
        int i4 = 0;
        for (String str4 : SettingsModel.PROVIDERS) {
            JSONObject optJSONObject = ai.optJSONObject(str4);
            if (optJSONObject != null && optJSONObject.optBoolean("hasKey")) {
                i4++;
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (String str5 : SettingsModel.HSRCS) {
            JSONObject optJSONObject2 = J2.optJSONObject(str5);
            if (optJSONObject2 != null && optJSONObject2.optBoolean("connected")) {
                arrayList2.add(str5);
            }
        }
        int size = arrayList2.size() > 0 ? arrayList2.size() : J.optBoolean("health") ? 1 : 0;
        boolean z2 = ("owm".equals(wx[0]) || "wapi".equals(wx[0])) && (J3.optJSONObject(wx[0]) == null || !J3.optJSONObject(wx[0]).optBoolean("hasKey"));
        sec(t("ns.conn", new Object[0]));
        LinearLayout list = list();
        Flow flow = this.k.flow(12);
        flow.setPadding(0, px(12.0f), 0, px(12.0f));
        flow.addView(conn("AI", i4 > 0 ? "✓" : "—", i4 > 0));
        String wxConn = SettingsModel.wxConn(z2, J.optBoolean("location"), J.optBoolean("locFix"));
        String t3 = t("ns.conn.wx", new Object[0]);
        if ("key".equals(wxConn)) {
            arrayList = arrayList2;
            i = i4;
            str = t("ns.conn.key", new Object[0]);
        } else {
            arrayList = arrayList2;
            i = i4;
            if ("noloc".equals(wxConn)) {
                str = t("ns.conn.wx.noloc", new Object[0]);
            } else {
                str = "default".equals(wxConn) ? "· " + t("ns.conn.wx.default", new Object[0]) : "✓";
            }
        }
        flow.addView(conn(t3, str, "ok".equals(wxConn)));
        String t4 = t("ns.conn.health", new Object[0]);
        if (size > 0) {
            i2 = size;
            str2 = t(size > 1 ? "ns.conn.srcs" : "ns.conn.src", "n", Integer.valueOf(i2));
        } else {
            i2 = size;
            str2 = "—";
        }
        flow.addView(conn(t4, str2, i2 > 0));
        String t5 = t("ns.conn.voice", new Object[0]);
        if (J.optBoolean("mic")) {
            z = z2;
            t = "✓";
        } else {
            z = z2;
            t = t("ns.conn.mic", new Object[0]);
        }
        flow.addView(conn(t5, t, J.optBoolean("mic")));
        boolean connected = GoogleLink.connected(this.c);
        flow.addView(conn("GOOGLE", (GoogleLink.configured() && connected) ? "✓" : "—", connected));
        list.addView(flow);
        Kit kit = this.k;
        Kit kit2 = this.k;
        if (GoogleLink.configured()) {
            i3 = 0;
            t2 = connected ? t("gg.on", new Object[0]) : t("gg.off", new Object[0]);
        } else {
            i3 = 0;
            t2 = t("gg.notcfg", new Object[0]);
        }
        kit.add(list, kit2.row(null, 0, "Google", t2, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m352lambda$connections$9$comaugmaososettingsNativeSettings();
            }
        }, new View[i3]));
        if (i == 0) {
            linearLayout = list;
            str3 = "ns.conn.wx";
            fixRow(linearLayout, "ai", "AI", t("ns.fix.ai", new Object[0]), t("ns.setup", new Object[0]));
        } else {
            linearLayout = list;
            str3 = "ns.conn.wx";
        }
        if (z) {
            fixRow(linearLayout, "weather", t(str3, new Object[0]), t("ns.fix.wx", "src", t("ns.wx.s." + wx[0], new Object[0])), t("aif.fix", new Object[0]));
        } else if ("noloc".equals(wxConn)) {
            fixRow(linearLayout, "perm:location", t(str3, new Object[0]), t("ns.fix.wx.loc", new Object[0]), t("aif.fix", new Object[0]));
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str6 = (String) it.next();
            JSONObject optJSONObject3 = J2.optJSONObject(str6);
            if (!optJSONObject3.optString("error").isEmpty()) {
                fixRow(linearLayout, "health:" + str6, up(ScrData.srcName(str6)), up(optJSONObject3.optString("error")), t("aif.fix", new Object[0]));
            }
        }
        JSONObject optJSONObject4 = J2.optJSONObject("hc");
        if (optJSONObject4 != null && !optJSONObject4.optBoolean("connected") && arrayList.isEmpty()) {
            fixRow(linearLayout, "health:hc", t("ns.conn.health", new Object[0]), t("ns.fix.health", new Object[0]), t("ns.setup", new Object[0]));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$connections$9$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m352lambda$connections$9$comaugmaososettingsNativeSettings() {
        m356lambda$navRow$7$comaugmaososettingsNativeSettings("google");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextView conn(String str, String str2, boolean z) {
        TextView mono = this.k.mono(str + " " + str2, 9.0f, this.k.DIM);
        SpannableString spannableString = new SpannableString(str + " " + str2);
        Kit kit = this.k;
        spannableString.setSpan(new ForegroundColorSpan(z ? kit.GOOD : kit.ALT), str.length() + 1, spannableString.length(), 0);
        mono.setText(spannableString);
        if (z) {
            str2 = "ok";
        }
        mono.setContentDescription(str + " " + str2);
        return mono;
    }

    void fixRow(LinearLayout linearLayout, final String str, String str2, String str3, String str4) {
        this.k.add(linearLayout, this.k.rowWrap(str2, str3, this.k.btn(str4, false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m354lambda$fixRow$10$comaugmaososettingsNativeSettings(str);
            }
        })));
    }

    void lang() {
        char c;
        Kit.IconView iconView;
        String str = Store.get("aos.lang", "");
        char c2 = 0;
        add(hd(up(t("set.lang", new Object[0])), ""));
        LinearLayout list = list();
        char c3 = 1;
        int length = LANGS.length + 1;
        String[][] strArr = new String[length];
        strArr[0] = new String[]{"", t("lang.system", new Object[0])};
        System.arraycopy(LANGS, 0, strArr, 1, LANGS.length);
        int i = 0;
        while (i < length) {
            final String[] strArr2 = strArr[i];
            boolean equals = strArr2[c2].equals(str);
            Kit kit = this.k;
            Kit kit2 = this.k;
            String str2 = strArr2[c3];
            Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    NativeSettings.this.m355lambda$lang$11$comaugmaososettingsNativeSettings(strArr2);
                }
            };
            if (equals) {
                c = c3;
                iconView = this.k.iconView(Kit.CHECK, this.k.ACC, 14.0f);
            } else {
                c = c3;
                iconView = null;
            }
            View add = kit.add(list, kit2.row(null, 0, str2, null, false, runnable, iconView));
            if (equals) {
                add.setContentDescription(strArr2[c] + ", selected");
            }
            i++;
            c3 = c;
            c2 = 0;
        }
        fine(t("lang.fine", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$lang$11$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m355lambda$lang$11$comaugmaososettingsNativeSettings(String[] strArr) {
        setBoth("aos.lang", strArr[0]);
        Strings.init(this.c);
        paint();
    }

    void privacy() {
        JSONObject J = J(this.be.permState());
        add(hd(t("ns.priv.h", new Object[0]), t("ns.priv.sub", new Object[0])));
        add(this.k.note(IC_LOCK, this.k.ACC, t("ns.priv.leave.h", new Object[0]), t("ns.priv.leave.p", new Object[0]), null, true));
        this.k.add(list(), this.k.row(IC_LOCK, this.k.INK, t("ns.priv.vault", new Object[0]), t("ns.priv.vault.sub", new Object[0]), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m362lambda$privacy$12$comaugmaososettingsNativeSettings();
            }
        }, new View[0]));
        LinearLayout note = this.k.note(IC_HEART, this.k.ACC, t("ns.priv.health.h", new Object[0]), t("ns.priv.health.p", new Object[0]), null, true);
        note.setTag("st-hpriv");
        add(note);
        for (final String str : PERMS) {
            boolean optBoolean = J.optBoolean(str);
            Kit kit = this.k;
            TextView chip = optBoolean ? kit.chip("✓ ON", this.k.ACC) : kit.btn(t("ns.allow", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    NativeSettings.this.m363lambda$privacy$13$comaugmaososettingsNativeSettings(str);
                }
            });
            if (!optBoolean) {
                chip.setContentDescription(t("ns.allow", new Object[0]) + " " + t("perm." + str, new Object[0]));
            }
            add(this.k.note(null, 0, t("perm." + str, new Object[0]), t("perm." + str + ".why", new Object[0]), chip, false));
        }
        fine(t("ns.priv.fine", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$privacy$12$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m362lambda$privacy$12$comaugmaososettingsNativeSettings() {
        this.be.openApp(VaultLink.APP);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$privacy$13$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m363lambda$privacy$13$comaugmaososettingsNativeSettings(String str) {
        this.be.askPerm(str);
    }

    void about() {
        JSONObject J = J(this.be.appVersion());
        JSONObject J2 = J(this.be.permState());
        add(hd(t("ns.about.h", new Object[0]), ""));
        add(this.k.note(null, 0, CrashModel.version(J.optString("name"), J.optLong("code")), t("ns.about.p", new Object[0]), null, false));
        add(this.k.note(null, 0, t("ns.about.home", new Object[0]), J2.optBoolean("home") ? t("ns.about.home.yes", new Object[0]) : t("ns.about.home.no", new Object[0]), J2.optBoolean("home") ? this.k.chip(t("ns.about.default", new Object[0]), this.k.ACC) : this.k.btn(t("ns.set", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m338lambda$about$14$comaugmaososettingsNativeSettings();
            }
        }), false));
        LinearLayout list = list();
        this.k.add(list, this.k.row(null, 0, t("ns.about.lic", new Object[0]), t("ns.about.lic.sub", new Object[0]), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m339lambda$about$15$comaugmaososettingsNativeSettings();
            }
        }, new View[0]));
        this.k.add(list, this.k.row(null, 0, t("ns.about.pp", new Object[0]), t("ns.about.pp.sub", new Object[0]), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m340lambda$about$16$comaugmaososettingsNativeSettings();
            }
        }, new View[0]));
        this.k.add(list, this.k.row(null, 0, t("ns.about.fb", new Object[0]), t("ns.about.fb.sub", new Object[0]), true, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m341lambda$about$17$comaugmaososettingsNativeSettings();
            }
        }, new View[0]));
        add(this.k.bigBtn(t("ns.setup.again", new Object[0]), new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m342lambda$about$18$comaugmaososettingsNativeSettings();
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$about$14$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m338lambda$about$14$comaugmaososettingsNativeSettings() {
        this.be.askPerm("home");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$about$16$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m340lambda$about$16$comaugmaososettingsNativeSettings() {
        m356lambda$navRow$7$comaugmaososettingsNativeSettings("privacy");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$about$17$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m341lambda$about$17$comaugmaososettingsNativeSettings() {
        this.be.feedback();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$about$18$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m342lambda$about$18$comaugmaososettingsNativeSettings() {
        Store.set("aos.onboarded", "");
        Welcome.clearResume();
        this.be.finish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: licences, reason: merged with bridge method [inline-methods] */
    public void m339lambda$about$15$comaugmaososettingsNativeSettings() {
        new Sheet(this.c, this.th, t("ns.about.lic", new Object[0])).title(up(t("ns.about.lic", new Object[0]))).para(t("ns.about.lic.p", new Object[0]), 12.0f, 0.85f).buttons(new String[]{t("close", new Object[0])}, new Runnable[]{null}, false).show(this.root);
    }

    void backup() {
        add(hd(up(t("set.backup", new Object[0])), t("backup.sub", new Object[0])));
        add(this.k.note(IC_LOCK, this.k.ACC, t("backup.what", new Object[0]), t("backup.what.p", new Object[0]), null, true));
        this.k.add(list(), this.k.toggleRow(t("backup.notes", new Object[0]), null, this.bkNotes, new Consumer() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NativeSettings.this.m343lambda$backup$19$comaugmaososettingsNativeSettings((Boolean) obj);
            }
        }));
        final TextView msg = this.k.msg(this.bkMsg);
        add(this.k.hrow(this.k.btn(t("backup.export", new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m344lambda$backup$20$comaugmaososettingsNativeSettings(msg);
            }
        }), this.k.btn(t("backup.import", new Object[0]), true, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                NativeSettings.this.m347lambda$backup$23$comaugmaososettingsNativeSettings(msg);
            }
        })));
        add(msg);
        if (this.bkPlan == null) {
            return;
        }
        add(this.k.section(t("backup.preview", "n", Integer.valueOf(this.bkPlan.size()))));
        LinearLayout list = list();
        if (this.bkPlan.isEmpty()) {
            this.k.add(list, this.k.row(null, 0, t("backup.same", new Object[0]), null, false, null, new View[0]));
        }
        for (SettingsModel.Plan plan : this.bkPlan) {
            this.k.add(list, this.k.row(null, 0, kname(plan.k), plan.what != null ? plan.what : t("backup.change", new Object[0]), false, null, new View[0]));
        }
        if (SettingsModel.hasNotes(this.bkPlan)) {
            this.k.add(list(), this.k.toggleRow(t("backup.notes", new Object[0]), t("backup.notes.imp", new Object[0]), this.bkImpNotes, new Consumer() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda16
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    NativeSettings.this.m348lambda$backup$24$comaugmaososettingsNativeSettings((Boolean) obj);
                }
            }));
        }
        if (!this.bkPlan.isEmpty()) {
            add(this.k.hrow(this.k.btn(t("backup.apply", new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    NativeSettings.this.bkApply();
                }
            }), this.k.btn(t("cancel", new Object[0]), true, false, new Runnable() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    NativeSettings.this.m349lambda$backup$25$comaugmaososettingsNativeSettings();
                }
            })));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$19$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m343lambda$backup$19$comaugmaososettingsNativeSettings(Boolean bool) {
        this.bkNotes = bool.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$22$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m346lambda$backup$22$comaugmaososettingsNativeSettings(TextView textView, String str) {
        JSONObject J = J(str);
        if (!J.optBoolean("ok")) {
            this.bkMsg = up(J.optString("error", t("backup.fail", new Object[0])));
            textView.setText(this.bkMsg);
            return;
        }
        List<SettingsModel.Plan> bkPlan = SettingsModel.bkPlan(J.optString("text"), new BiFunction() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda4
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return NativeSettings.this.m345lambda$backup$21$comaugmaososettingsNativeSettings((String) obj, (Boolean) obj2);
            }
        });
        if (bkPlan == null) {
            this.bkPlan = null;
            this.bkMsg = t("backup.bad", new Object[0]);
            textView.setText(this.bkMsg);
        } else {
            this.bkPlan = bkPlan;
            this.bkImpNotes = false;
            this.bkMsg = "";
            paint();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$23$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m347lambda$backup$23$comaugmaososettingsNativeSettings(final TextView textView) {
        this.be.settingsImport(new Consumer() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda26
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NativeSettings.this.m346lambda$backup$22$comaugmaososettingsNativeSettings(textView, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$21$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ String m345lambda$backup$21$comaugmaososettingsNativeSettings(String str, Boolean bool) {
        return bool.booleanValue() ? this.be.get(str) : Store.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$24$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m348lambda$backup$24$comaugmaososettingsNativeSettings(Boolean bool) {
        this.bkImpNotes = bool.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$25$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m349lambda$backup$25$comaugmaososettingsNativeSettings() {
        this.bkPlan = null;
        paint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.jarvis.assist.aosui.settings.NativeSettings$2] */
    /* renamed from: exportNow, reason: merged with bridge method [inline-methods] */
    public void m344lambda$backup$20$comaugmaososettingsNativeSettings(final TextView textView) {
        String jSONObject;
        Map<String, String> all = Store.all();
        String format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US) { // from class: com.jarvis.assist.aosui.settings.NativeSettings.2
            {
                setTimeZone(TimeZone.getTimeZone("UTC"));
            }
        }.format(new Date());
        List<String> order = ai().optJSONArray("order") != null ? SettingsModel.order(this.be.aiHasKey()) : null;
        final Backend backend = this.be;
        Objects.requireNonNull(backend);
        JSONObject bkBuild = SettingsModel.bkBuild(all, new Function() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda27
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Backend.this.get((String) obj);
            }
        }, order, this.bkNotes, format);
        try {
            jSONObject = bkBuild.toString(1);
        } catch (Exception e) {
            jSONObject = bkBuild.toString();
        }
        this.be.settingsExport(jSONObject, new Consumer() { // from class: com.jarvis.assist.aosui.settings.NativeSettings$$ExternalSyntheticLambda28
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NativeSettings.this.m353lambda$exportNow$26$comaugmaososettingsNativeSettings(textView, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$exportNow$26$com-augma-oso-settings-NativeSettings, reason: not valid java name */
    public /* synthetic */ void m353lambda$exportNow$26$comaugmaososettingsNativeSettings(TextView textView, String str) {
        JSONObject J = J(str);
        this.bkMsg = J.optBoolean("ok") ? t("backup.saved", new Object[0]) : up(J.optString("error", t("backup.fail", new Object[0])));
        textView.setText(this.bkMsg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bkApply() {
        for (SettingsModel.Plan plan : this.bkPlan) {
            if (!"aos.mem".equals(plan.k) || this.bkImpNotes) {
                if ("ai.order".equals(plan.k)) {
                    this.be.aiSetOrder(plan.v);
                } else {
                    if (!plan.nat) {
                        Store.set(plan.k, plan.v);
                    }
                    if (plan.nat || SettingsModel.NAT.contains(plan.k)) {
                        this.be.set(plan.k, plan.v);
                    }
                }
            }
        }
        this.bkPlan = null;
        this.bkMsg = t("backup.done", new Object[0]);
        Strings.init(this.c);
        paint();
    }

    String kname(String str) {
        String t = Strings.t("ns.bk." + str);
        return t.equals(new StringBuilder().append("ns.bk.").append(str).toString()) ? str : t;
    }
}
