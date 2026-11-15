package com.jarvis.assist.aosui.onboard;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.R;
import com.jarvis.assist.aosui.ai.KeyCheck;
import com.jarvis.assist.aosui.ai.KeyFlow;
import com.jarvis.assist.aosui.ai.Keys;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.Converge;
import com.jarvis.assist.aosui.core.Drafts;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.OnboardModel;
import com.jarvis.assist.aosui.core.Palette;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Profile;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Svg;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.home.VoiceSheet;
import com.jarvis.assist.aosui.onboard.Welcome;
import com.jarvis.assist.aosui.ui.Flow;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.ModelPick;
import com.jarvis.assist.aosui.ui.Sheet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Welcome extends FrameLayout {
    static final String IC_APPLE = "M10.5 2.5c-.8.2-1.6 1-1.5 2 .9 0 1.6-.9 1.5-2zM8 5.5c-1-.6-3.5-.6-4 2-.5 2.5 1.2 6 2.6 6 .6 0 .9-.4 1.4-.4s.8.4 1.4.4c1.1 0 2.3-2.2 2.4-3-1.2-.5-1.6-2.4-.4-3.4-.8-1.2-2.5-1.2-3.4-1.6z";
    static final String IC_CHECK = "M3 8.5l3 3 7-7";
    static final String IC_HOME = "M2.5 8L8 3l5.5 5v5.5h-11zM6.5 13.5v-3h3v3";
    static final String IC_KEY = "M10.5 2.5a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM8.3 7.7L2.5 13.5M4 12l1.5 1.5M5.5 10.5L7 12";
    static final String IC_LOCK = "M4 7.5h8v6.5H4zM5.5 7.5V5a2.5 2.5 0 0 1 5 0v2.5";
    static final String IC_PHONE = "M3 2.5h3l1.2 3-1.8 1.2a8 8 0 0 0 3.9 3.9l1.2-1.8 3 1.2v3c0 .6-.5 1-1 1A11.5 11.5 0 0 1 2 3.5c0-.5.4-1 1-1z";
    static final String IC_SPARK = "M8 1.5l1.6 4.9L14.5 8l-4.9 1.6L8 14.5l-1.6-4.9L1.5 8l4.9-1.6z";
    static final String IC_WATCH = "M5 4h6v8H5zM6 4V1.5h4V4M6 12v2.5h4V12";
    final Backend be;
    final LinearLayout body;
    final Context c;
    boolean closed;
    final LinearLayout col;
    final float dn;
    final Drafts drafts;
    final Runnable esTick;
    final LinearLayout foot;
    Kit.Grid grid;
    final Host host;
    Kit k;
    int lastStep;
    final OnboardModel m;
    More more;
    EditText nameIn;
    JSONObject pa;
    int padB;
    int padL;
    int padR;
    int padT;
    final Progress prog;
    final ScrollView sc;
    final Map<String, EditText> slots;
    final boolean still;
    final Theme th;
    static final String IC_CAL = "M2.5 4h11v9.5h-11zM2.5 7h11M5.5 2.5v3M10.5 2.5v3";
    static final String IC_USER = "M8 2.5a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM2.5 14a5.5 5.5 0 0 1 11 0";
    static final String IC_PIN = "M8 14.5s-4.5-4.6-4.5-8a4.5 4.5 0 0 1 9 0c0 3.4-4.5 8-4.5 8zM8 4.8a1.7 1.7 0 1 0 0 3.4 1.7 1.7 0 0 0 0-3.4z";
    static final String IC_INBOX = "M2.5 9.5l2-6h7l2 6v4h-11zM2.5 9.5h3.5l.8 1.5h2.4l.8-1.5h3.5";
    static final String IC_HEART = "M8 13.5S2 9.8 2 5.8A3 3 0 0 1 8 4.6a3 3 0 0 1 6 1.2c0 4-6 7.7-6 7.7z";
    static final String[][] ESS = {new String[]{"calendar", IC_CAL, "ob.p.cal", "ob.ess.cal"}, new String[]{"contacts", IC_USER, "ob.p.contacts", "ob.ess.contacts"}, new String[]{"location", IC_PIN, "ob.p.loc", "ob.ess.loc"}, new String[]{"notifAccess", IC_INBOX, "ob.p.notif", "ob.ess.notif"}, new String[]{"health", IC_HEART, "ob.p.health", "ob.ess.health"}};
    static final String IC_MIC = "M8 2a2 2 0 0 0-2 2v4a2 2 0 0 0 4 0V4a2 2 0 0 0-2-2zM4 8a4 4 0 0 0 8 0M8 12v2.5";
    static final String IC_HOUR = "M4 2h8M4 14h8M5 2c0 4 6 4 6 6s-6 2-6 6M11 2c0 4-6 4-6 6";
    static final String[][] PERMS = {new String[]{"calendar", IC_CAL, "ob.p.cal", "ob.p.cal.p", "1"}, new String[]{"contacts", IC_USER, "ob.p.contacts", "ob.p.contacts.p", "1"}, new String[]{"location", IC_PIN, "ob.p.loc", "ob.p.loc.p", "1"}, new String[]{"mic", IC_MIC, "ob.p.mic", "ob.p.mic.p", ""}, new String[]{"notifAccess", IC_INBOX, "ob.p.notif", "ob.p.notif.p", ""}, new String[]{"usage", IC_HOUR, "ob.p.usage", "ob.p.usage.p", ""}};

    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Host {
        void done();
    }

    public Welcome(Backend backend, Host host) {
        super(backend.ctx());
        this.m = new OnboardModel();
        this.th = new Theme();
        this.lastStep = -1;
        this.esTick = new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                Welcome.this.esNext();
            }
        };
        this.drafts = new Drafts();
        this.slots = new HashMap();
        this.be = backend;
        this.host = host;
        this.c = backend.ctx();
        this.dn = this.c.getResources().getDisplayMetrics().density;
        this.still = Perf.k().calm || !Perf.k().motion || "1".equals(Store.get("aos.rm", "")) || animOff(this.c);
        setClickable(true);
        setFocusable(false);
        setImportantForAccessibility(1);
        Kit.pane(this, t("ob.a11y", new Object[0]));
        this.col = new LinearLayout(this.c);
        this.col.setOrientation(1);
        this.prog = new Progress(this.c);
        this.col.addView(this.prog, new LinearLayout.LayoutParams(-1, px(48.0f)));
        this.sc = new ScrollView(this.c);
        this.sc.setVerticalScrollBarEnabled(false);
        this.sc.setOverScrollMode(2);
        this.sc.setFillViewport(true);
        FrameLayout frameLayout = new FrameLayout(this.c);
        this.body = new LinearLayout(this.c);
        this.body.setOrientation(1);
        this.body.setPadding(px(16.0f), px(10.0f), px(16.0f), px(20.0f));
        frameLayout.addView(this.body, new FrameLayout.LayoutParams(-1, -2, 1));
        frameLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda27
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                Welcome.this.m313lambda$new$0$comaugmaosoonboardWelcome(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        this.sc.addView(frameLayout, new FrameLayout.LayoutParams(-1, -2));
        FrameLayout frameLayout2 = new FrameLayout(this.c);
        frameLayout2.addView(this.sc, new FrameLayout.LayoutParams(-1, -1));
        this.more = new More(this.c);
        frameLayout2.addView(this.more, new FrameLayout.LayoutParams(-1, px(44.0f), 80));
        this.col.addView(frameLayout2, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.sc.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda28
            @Override // android.view.View.OnScrollChangeListener
            public final void onScrollChange(View view, int i, int i2, int i3, int i4) {
                Welcome.this.m314lambda$new$1$comaugmaosoonboardWelcome(view, i, i2, i3, i4);
            }
        });
        this.sc.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda29
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                Welcome.this.m315lambda$new$2$comaugmaosoonboardWelcome(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        if (this.c.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            ((FrameLayout.LayoutParams) this.body.getLayoutParams()).gravity = 17;
        }
        this.foot = new LinearLayout(this.c);
        this.foot.setGravity(16);
        this.col.addView(this.foot, new LinearLayout.LayoutParams(-1, -2));
        addView(this.col, new FrameLayout.LayoutParams(-1, -1));
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda30
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return Welcome.this.m316lambda$new$3$comaugmaosoonboardWelcome(view, windowInsets);
            }
        });
        try {
            this.pa = new JSONObject(backend.assistantInfo());
        } catch (Exception e) {
            this.pa = new JSONObject();
        }
        this.m.restore(Store.get(OnboardModel.K_STEP), Store.get(OnboardModel.K_PATH), Store.get(OnboardModel.K_DONE));
        render();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m313lambda$new$0$comaugmaosoonboardWelcome(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int px = px(600.0f);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.body.getLayoutParams();
        if (i3 - i <= px) {
            px = -1;
        }
        if (layoutParams.width != px) {
            layoutParams.width = px;
            this.body.setLayoutParams(layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m314lambda$new$1$comaugmaosoonboardWelcome(View view, int i, int i2, int i3, int i4) {
        moreHint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m315lambda$new$2$comaugmaosoonboardWelcome(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        moreHint();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ WindowInsets m316lambda$new$3$comaugmaosoonboardWelcome(View view, WindowInsets windowInsets) {
        int systemWindowInsetTop;
        int systemWindowInsetBottom;
        int i;
        int i2;
        if (Build.VERSION.SDK_INT >= 30) {
            Insets insets = windowInsets.getInsets(WindowInsets.Type.displayCutout());
            Insets insets2 = windowInsets.getInsets(WindowInsets.Type.systemBars());
            Insets insets3 = windowInsets.getInsets(WindowInsets.Type.ime());
            systemWindowInsetTop = Math.max(insets.top, insets2.top);
            systemWindowInsetBottom = Math.max(insets3.bottom, insets2.bottom);
            i2 = Math.max(insets.left, insets2.left);
            i = Math.max(insets.right, insets2.right);
        } else {
            systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
            systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
            i = 0;
            i2 = 0;
        }
        this.padL = i2;
        this.padT = systemWindowInsetTop;
        this.padR = i;
        this.padB = systemWindowInsetBottom;
        this.col.setPadding(i2, 0, i, 0);
        this.prog.setPadding(px(14.0f), Math.max(px(14.0f), systemWindowInsetTop), px(14.0f), px(6.0f));
        ((LinearLayout.LayoutParams) this.prog.getLayoutParams()).height = Math.max(px(14.0f), systemWindowInsetTop) + px(34.0f);
        this.foot.setPadding(px(16.0f), px(10.0f), px(16.0f), Math.max(px(16.0f), systemWindowInsetBottom));
        return windowInsets;
    }

    public static boolean animOff(Context context) {
        try {
            return Settings.Global.getFloat(context.getContentResolver(), "animator_duration_scale", 1.0f) == 0.0f;
        } catch (Exception e) {
            return false;
        }
    }

    int px(float f) {
        return Math.round(f * this.dn);
    }

    static String t(String str, Object... objArr) {
        return objArr.length == 0 ? Strings.t(str) : Strings.t(str, objArr);
    }

    static String up(String str) {
        return str == null ? "" : str.toUpperCase(Locale.getDefault());
    }

    JSONObject perm() {
        try {
            return new JSONObject(this.be.permState());
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    JSONObject ai() {
        try {
            return new JSONObject(this.be.aiHasKey());
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    static JSONObject opt(JSONObject jSONObject, String str) {
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        return optJSONObject == null ? new JSONObject() : optJSONObject;
    }

    static JSONObject J(String str) {
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

    public void onPerm() {
        if (this.m.st >= 2 && this.m.st <= 6) {
            render();
        }
        if (this.m.run) {
            removeCallbacks(this.esTick);
            postDelayed(this.esTick, 350L);
        }
    }

    public void onResume() {
        if (!this.m.run) {
            render();
        } else {
            removeCallbacks(this.esTick);
            postDelayed(this.esTick, 350L);
        }
    }

    boolean keySaved() {
        try {
            return ai().optBoolean("hasKey");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean onBack() {
        if (!Sheet.closeTop() && this.m.showBack()) {
            Feedback.fx(this, 0);
            saveName();
            this.m.back(keySaved());
            render();
        }
        return true;
    }

    public void onConfig() {
        render();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.esTick);
        this.drafts.clear();
        this.slots.clear();
        super.onDetachedFromWindow();
    }

    void persist() {
        if (this.closed) {
            return;
        }
        for (Map.Entry<String, String> entry : this.m.persist().entrySet()) {
            if (!entry.getValue().equals(Store.get(entry.getKey()))) {
                Store.set(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void clearResume() {
        for (String str : OnboardModel.KEYS) {
            Store.remove(str);
        }
    }

    EditText keep(EditText editText, final String str) {
        String str2 = this.drafts.get(str);
        if (str2 != null) {
            editText.setText(str2);
        }
        editText.addTextChangedListener(new TextWatcher() { // from class: com.jarvis.assist.aosui.onboard.Welcome.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                Welcome.this.drafts.put(str, editable.toString());
            }
        });
        this.slots.put(str, editText);
        return editText;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void esStart() {
        this.m.start();
        esNext();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void esNext() {
        String esNext = this.m.esNext(perm());
        render();
        if (esNext != null) {
            this.be.askPerm(esNext);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void esPaste() {
        this.m.msg = "";
        String looks = Keys.looks(this.be.clip("ai"), "ai");
        if (!looks.isEmpty()) {
            esSave(looks);
            return;
        }
        this.m.msg = t("ob.gem.noclip", new Object[0]);
        this.m.manual = true;
        render();
    }

    void esSave(final String str) {
        if (!Keys.plausible(str)) {
            this.m.msg = t("ob.gem.noclip", new Object[0]);
            this.m.manual = true;
            render();
            return;
        }
        String provider = KeyFlow.provider(str, null);
        if (provider == null) {
            this.k.pickSheet(this, t("key.which", new Object[0]), KeyFlow.PICK_NAMES, KeyFlow.PICK, KeyFlow.preselect("gemini"), new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda25
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Welcome.this.m298lambda$esSave$4$comaugmaosoonboardWelcome(str, (String) obj);
                }
            });
        } else {
            m298lambda$esSave$4$comaugmaosoonboardWelcome(str, provider);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: esSave, reason: merged with bridge method [inline-methods] */
    public void m298lambda$esSave$4$comaugmaosoonboardWelcome(String str, final String str2) {
        this.m.busy = t("ns.checking.key", "name", up(Keys.name(str2)), "l4", Keys.last4(str));
        render();
        save(str2, str, new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m299lambda$esSave$5$comaugmaosoonboardWelcome(str2, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$esSave$5$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m299lambda$esSave$5$comaugmaosoonboardWelcome(String str, String str2) {
        JSONObject J = J(str2);
        this.m.busy = "";
        if (J.optBoolean("ok") || J.optBoolean("hasKey")) {
            this.m.done = str;
            this.m.msg = "";
            this.m.manual = false;
            this.drafts.clear("gem");
            provisional(J);
        } else {
            this.m.msg = up(J.optString("error", t("ns.keyfail", new Object[0])));
        }
        render();
    }

    void provisional(JSONObject jSONObject) {
        if (jSONObject.optBoolean("provisional")) {
            this.k.toast(this, jSONObject.optString("note"));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$save$6$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m326lambda$save$6$comaugmaosoonboardWelcome(String str, Consumer consumer, String str2) {
        String orderWithOpenai;
        if (!"claude".equals(str) && !"gemini".equals(str) && J(str2).optBoolean("ok") && (orderWithOpenai = SettingsModel.orderWithOpenai(str2)) != null) {
            this.be.aiSetOrder(orderWithOpenai);
        }
        consumer.accept(str2);
    }

    void save(final String str, String str2, final Consumer<String> consumer) {
        Consumer<String> consumer2 = new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m326lambda$save$6$comaugmaosoonboardWelcome(str, consumer, (String) obj);
            }
        };
        if ("claude".equals(str)) {
            this.be.aiSetKey(str2, consumer2);
        } else if ("gemini".equals(str)) {
            this.be.geminiSetKey(str2, consumer2);
        } else {
            String[] oai = Keys.oai(str);
            this.be.oaiSet(json("base", oai[0], "key", str2, "model", oai[1]).toString(), consumer2);
        }
    }

    void theme() {
        this.th.load(Store.get("aos.theme", "dark"), Store.obj("aos.colors"), new JSONObject(), Perf.k().blur, this.dn);
        Configuration configuration = this.c.getResources().getConfiguration();
        float tabletScale = UnitsMath.tabletScale(configuration.smallestScreenWidthDp);
        this.k = new Kit(this.c, this.th, UnitsMath.viewZoom(configuration.fontScale) * tabletScale, UnitsMath.textZoom(configuration.fontScale) * tabletScale);
    }

    void moreHint() {
        boolean canScrollVertically = this.sc.canScrollVertically(1);
        int i = canScrollVertically ? 0 : 8;
        if (this.more.getVisibility() != i) {
            this.more.setVisibility(i);
        }
        if (canScrollVertically) {
            this.more.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class More extends View {
        int bgKey;
        final Path ch;
        final Paint p;
        final Paint s;

        More(Context context) {
            super(context);
            this.p = new Paint(1);
            this.s = new Paint(1);
            this.ch = new Path();
            setVisibility(8);
            setContentDescription(Welcome.t("ob.more", new Object[0]));
            setClickable(true);
            setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$More$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    Welcome.More.this.m337lambda$new$0$comaugmaosoonboardWelcome$More(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-augma-oso-onboard-Welcome$More, reason: not valid java name */
        public /* synthetic */ void m337lambda$new$0$comaugmaosoonboardWelcome$More(View view) {
            Feedback.fx(view, 0);
            Welcome.this.sc.smoothScrollBy(0, (Welcome.this.sc.getHeight() * 2) / 3);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            if (Welcome.this.k == null || width == 0) {
                return;
            }
            if (this.bgKey != Welcome.this.k.BG) {
                this.bgKey = Welcome.this.k.BG;
                this.p.setShader(null);
            }
            if (this.p.getShader() == null) {
                this.p.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, height, Theme.a(Welcome.this.k.BG, 0.0f), Welcome.this.k.BG, Shader.TileMode.CLAMP));
            }
            this.p.setStyle(Paint.Style.FILL);
            float f = width;
            float f2 = height;
            canvas.drawRect(0.0f, 0.0f, f, f2, this.p);
            this.s.setStyle(Paint.Style.STROKE);
            this.s.setStrokeWidth(Welcome.this.dn * 1.6f);
            this.s.setStrokeCap(Paint.Cap.ROUND);
            this.s.setColor(Welcome.this.k.DIM);
            float f3 = f / 2.0f;
            float f4 = f2 * 0.62f;
            float f5 = Welcome.this.dn * 6.0f;
            this.ch.rewind();
            float f6 = f5 / 2.0f;
            float f7 = f4 - f6;
            this.ch.moveTo(f3 - f5, f7);
            this.ch.lineTo(f3, f4 + f6);
            this.ch.lineTo(f3 + f5, f7);
            canvas.drawPath(this.ch, this.s);
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            this.p.setShader(null);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0095, code lost:
    
        if (r14.grid.getTag() != (r14.k.light ? "l" : "d")) goto L24;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void render() {
        /*
            Method dump skipped, instructions count: 449
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.onboard.Welcome.render():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$render$7$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m324lambda$render$7$comaugmaosoonboardWelcome(int i) {
        this.sc.scrollTo(0, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$render$8$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m325lambda$render$8$comaugmaosoonboardWelcome() {
        View childAt = this.body.getChildAt(0);
        if (childAt != null) {
            childAt.sendAccessibilityEvent(8);
        }
    }

    void add(View view) {
        this.body.addView(view);
    }

    void footer() {
        this.foot.removeAllViews();
        JSONObject perm = perm();
        if (this.m.showBack()) {
            this.foot.addView(this.k.btn(t("ob.back", new Object[0]), true, false, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    Welcome.this.m304lambda$footer$9$comaugmaosoonboardWelcome();
                }
            }));
        } else {
            this.foot.addView(new View(this.c), new LinearLayout.LayoutParams(1, 1));
        }
        this.foot.addView(new View(this.c), new LinearLayout.LayoutParams(0, 1, 1.0f));
        TextView btn = this.k.btn(t(this.m.nextLabel(perm), new Object[0]), false, false, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                Welcome.this.m303lambda$footer$10$comaugmaosoonboardWelcome();
            }
        });
        if (this.m.st == 7) {
            ((Kit.FxText) btn).ev = 4;
        }
        this.foot.addView(btn);
        this.foot.setBackground(new Drawable() { // from class: com.jarvis.assist.aosui.onboard.Welcome.2
            final Paint lp = new Paint();

            {
                this.lp.setColor(Welcome.this.k.LINE);
                this.lp.setStrokeWidth(Math.max(1.0f, Welcome.this.dn));
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                canvas.drawLine(0.0f, 0.0f, getBounds().width(), 0.0f, this.lp);
            }

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -3;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$footer$9$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m304lambda$footer$9$comaugmaosoonboardWelcome() {
        saveName();
        this.m.back(keySaved());
        render();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$footer$10$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m303lambda$footer$10$comaugmaosoonboardWelcome() {
        saveName();
        if (this.m.st == 7) {
            finish();
        } else {
            this.m.next(keySaved());
            render();
        }
    }

    void finish() {
        saveName();
        Store.set("aos.onboarded", "1");
        this.closed = true;
        clearResume();
        this.drafts.clear();
        removeCallbacks(this.esTick);
        Sheet.closeTop();
        animate().alpha(0.0f).setDuration(this.still ? 0L : 220L).withEndAction(new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                Welcome.this.m302lambda$finish$11$comaugmaosoonboardWelcome();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$finish$11$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m302lambda$finish$11$comaugmaosoonboardWelcome() {
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).removeView(this);
        }
        this.host.done();
    }

    void saveName() {
        if (this.nameIn == null || !this.nameIn.isAttachedToWindow()) {
            return;
        }
        this.be.set("ai.name", this.nameIn.getText().toString().trim());
    }

    void step(JSONObject jSONObject) {
        switch (this.m.st) {
            case 0:
                hero(true, null, t("wl.welcome", new Object[0]), t("ob.welcome.p", new Object[0]));
                add(this.k.note(IC_LOCK, this.k.ACC, t("ob.standalone.h", new Object[0]), t("ob.standalone.p", new Object[0]), null, true));
                return;
            case 1:
                yours();
                return;
            case 2:
                essentials(jSONObject);
                return;
            case 3:
                h2(t("wl.phone", new Object[0]), t("ob.phone.p", new Object[0]));
                for (final String[] strArr : PERMS) {
                    boolean optBoolean = jSONObject.optBoolean(strArr[0]);
                    add(permCard(strArr[1], optBoolean, t(strArr[2], new Object[0]), "1".equals(strArr[4]), t(strArr[3], new Object[0]), optBoolean ? onChip() : this.k.btn(t("ob.allow", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda16
                        @Override // java.lang.Runnable
                        public final void run() {
                            Welcome.this.m327lambda$step$12$comaugmaosoonboardWelcome(strArr);
                        }
                    }), false));
                }
                return;
            case 4:
                health(jSONObject);
                return;
            case 5:
                aiStep();
                return;
            case 6:
                boolean optBoolean2 = jSONObject.optBoolean("home");
                hero(false, IC_HOME, t("ob.home.h", new Object[0]), t("ob.home.p", new Object[0]));
                add(this.k.note(IC_SPARK, this.k.ACC, t("ob.home.why.h", new Object[0]), t("ob.home.why.p", new Object[0]), null, false));
                add(this.k.note(IC_HOME, this.k.INK, t("ob.home.what.h", new Object[0]), t("ob.home.what.p", new Object[0]), null, false));
                add(this.k.note("M3 8.5l3 3 7-7", this.k.INK, t("ob.home.back.h", new Object[0]), t("ob.home.back.p", new Object[0]), null, false));
                add(optBoolean2 ? permCard("M3 8.5l3 3 7-7", true, t("ob.home.is", new Object[0]), false, null, this.k.chip("✓ " + t("ob.home.def", new Object[0]), this.k.ACC), false) : this.k.bigBtn(t("ob.home.set", new Object[0]), new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        Welcome.this.m328lambda$step$13$comaugmaosoonboardWelcome();
                    }
                }));
                return;
            default:
                String str = this.be.get("ai.name");
                String t = t("ob.done.h", new Object[0]);
                if (str == null || str.isEmpty()) {
                    str = t("ob.done.name", new Object[0]);
                }
                hero(true, null, t, t("ob.done.p", "name", str));
                add(this.k.note(IC_SPARK, this.k.ACC, t("ob.later.h", new Object[0]), t("ob.later.p", new Object[0]), null, true));
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$step$12$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m327lambda$step$12$comaugmaosoonboardWelcome(String[] strArr) {
        this.be.askPerm(strArr[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$step$13$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m328lambda$step$13$comaugmaosoonboardWelcome() {
        this.be.askPerm("home");
    }

    void h2(String str, String str2) {
        TextView text = this.k.text(str, Fonts.RAJ_600, 22.0f, 0.06f, this.k.INK);
        Kit.heading(text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, px(6.0f), 0, px(4.0f));
        this.body.addView(text, layoutParams);
        if (str2 != null) {
            TextView text2 = this.k.text(str2, Fonts.RAJ_400, 14.0f, 0.0f, this.k.DIM);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.bottomMargin = px(12.0f);
            this.body.addView(text2, layoutParams2);
        }
    }

    void hero(boolean z, String str, String str2, String str3) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, px(16.0f), 0, px(18.0f));
        linearLayout.setLayoutParams(layoutParams);
        boolean shortScreen = shortScreen();
        int i = shortScreen ? 76 : 120;
        if (shortScreen) {
            layoutParams.setMargins(0, px(4.0f), 0, px(10.0f));
        }
        if (z) {
            Mark mark = new Mark(this.c, i);
            float f = i;
            linearLayout.addView(mark, new LinearLayout.LayoutParams(px(f), px(f)));
        } else {
            linearLayout.addView(this.k.iconView(str, this.k.ACC, 56.0f));
        }
        if (z && this.m.st == 0 && !shortScreen) {
            ImageView imageView = new ImageView(this.c);
            imageView.setImageResource(R.drawable.aos_logo);
            imageView.setAdjustViewBounds(true);
            imageView.setImportantForAccessibility(2);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(px(44.0f), -2);
            layoutParams2.topMargin = -px(4.0f);
            linearLayout.addView(imageView, layoutParams2);
        }
        TextView text = this.k.text(str2, Fonts.RAJ_600, shortScreen ? 22.0f : 28.0f, 0.04f, this.k.INK);
        text.setGravity(17);
        Kit.heading(text);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams3.setMargins(0, px(8.0f), 0, px(6.0f));
        linearLayout.addView(text, layoutParams3);
        TextView text2 = this.k.text(str3, Fonts.RAJ_400, 14.0f, 0.0f, this.k.DIM);
        text2.setGravity(17);
        text2.setMaxWidth(px(420.0f));
        linearLayout.addView(text2);
        add(linearLayout);
    }

    boolean shortScreen() {
        return ((float) this.c.getResources().getConfiguration().screenHeightDp) / this.k.z < 620.0f;
    }

    TextView onChip() {
        return this.k.chip("✓ " + t("ob.on", new Object[0]), this.k.ACC);
    }

    LinearLayout permCard(String str, boolean z, String str2, boolean z2, String str3, View view, boolean z3) {
        LinearLayout note = this.k.note(str, z ? this.k.ACC : this.k.INK, str2, str3, view, false);
        if (z3) {
            note.setAlpha(0.6f);
        }
        if (z2) {
            TextView textView = (TextView) ((LinearLayout) note.getChildAt(1)).getChildAt(0);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str2 + "  ");
            int length = spannableStringBuilder.length();
            spannableStringBuilder.append((CharSequence) t("ob.rec", new Object[0]));
            spannableStringBuilder.setSpan(new ForegroundColorSpan(this.k.ACC), length, spannableStringBuilder.length(), 33);
            if (Build.VERSION.SDK_INT >= 28) {
                spannableStringBuilder.setSpan(new TypefaceSpan(Fonts.MONO), length, spannableStringBuilder.length(), 33);
            }
            spannableStringBuilder.setSpan(new AbsoluteSizeSpan(Math.round(this.k.z * 8.0f), true), length, spannableStringBuilder.length(), 33);
            textView.setText(spannableStringBuilder);
        }
        return note;
    }

    void yours() {
        h2(t("wl.yours", new Object[0]), t("ob.yours.p", new Object[0]));
        add(this.k.section(t("ob.name", new Object[0])));
        this.nameIn = keep(this.k.input(this.be.get("ai.name"), t("ob.name.ph", new Object[0]), false, false, 8193), "name");
        this.nameIn.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        this.nameIn.setContentDescription(t("ob.name", new Object[0]));
        this.nameIn.setImeOptions(6);
        this.nameIn.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda41
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return Welcome.this.m329lambda$yours$14$comaugmaosoonboardWelcome(textView, i, keyEvent);
            }
        });
        this.nameIn.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda42
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z) {
                Welcome.this.m330lambda$yours$15$comaugmaosoonboardWelcome(view, z);
            }
        });
        add(this.nameIn);
        add(this.k.section(t("you.h", new Object[0])));
        EditText input = this.k.input(Store.get(Profile.KEY, ""), t("you.ph", new Object[0]), false, false, 8193);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        input.setContentDescription(t("you.ph", new Object[0]));
        input.addTextChangedListener(new TextWatcher() { // from class: com.jarvis.assist.aosui.onboard.Welcome.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                String clean = Profile.clean(editable.toString());
                if (clean.isEmpty()) {
                    Store.remove(Profile.KEY);
                } else {
                    Store.set(Profile.KEY, clean);
                }
            }
        });
        add(input);
        add(this.k.section(t("vo.h", new Object[0])));
        add(this.k.btn(t("vo.choose", new Object[0]), true, false, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                Welcome.this.m331lambda$yours$16$comaugmaosoonboardWelcome();
            }
        }));
        String str = Store.get("aos.theme", "dark");
        add(this.k.section(t("ob.theme", new Object[0])));
        add(this.k.seg(new String[]{t("ob.dark", new Object[0]), t("ob.light", new Object[0])}, new String[]{"dark", "light"}, "light".equals(str) ? "light" : "dark", t("ob.theme", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda44
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m332lambda$yours$17$comaugmaosoonboardWelcome((String) obj);
            }
        }));
        add(this.k.section(t("adv.theme.h", new Object[0])));
        add(this.k.themePicker(Store.get(Advanced.KEY, ""), new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda45
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m333lambda$yours$18$comaugmaosoonboardWelcome((String) obj);
            }
        }));
        add(this.k.fine(t("ob.style.p", new Object[0])));
        String[][] strArr = {new String[]{"accent", "ob.col.accent", "ob.col.accent.p"}, new String[]{"ink", "ob.col.ink", "ob.col.ink.p"}, new String[]{"alert", "ob.col.alert", "ob.col.alert.p"}};
        for (int i = 0; i < 3; i++) {
            final String str2 = strArr[i][0];
            final String t = t(strArr[i][1], new Object[0]);
            final String color = SettingsModel.color(Store.get("aos.colors"), str2);
            add(this.k.section(t + "  · " + t(strArr[i][2], new Object[0])));
            add(this.k.swatches(SettingsModel.PAL[i], color, new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda46
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Welcome.this.m334lambda$yours$19$comaugmaosoonboardWelcome(str2, (String) obj);
                }
            }, t("ns.col.custom", new Object[0]) + ", " + t, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    Welcome.this.m336lambda$yours$21$comaugmaosoonboardWelcome(t, color, str2);
                }
            }));
        }
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        linearLayout.setPadding(px(12.0f), px(12.0f), px(12.0f), px(12.0f));
        linearLayout.setBackground(Kit.pill(0, this.k.LINE2, this.dn, this.dn * 8.0f));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(16.0f);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setImportantForAccessibility(4);
        linearLayout.addView(new Mark(this.c, 64), new LinearLayout.LayoutParams(px(64.0f), px(64.0f)));
        LinearLayout linearLayout2 = new LinearLayout(this.c);
        linearLayout2.setOrientation(1);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams2.leftMargin = px(14.0f);
        TextView mono = this.k.mono(t("ob.pv.app", new Object[0]), 9.0f, this.k.ACC);
        mono.setPadding(px(8.0f), px(3.0f), px(8.0f), px(3.0f));
        mono.setBackground(Kit.pill(0, this.k.ACC, this.dn, this.dn * 4.0f));
        LinearLayout linearLayout3 = new LinearLayout(this.c);
        linearLayout3.addView(mono);
        linearLayout2.addView(linearLayout3);
        TextView text = this.k.text(t("ob.pv.title", new Object[0]), Fonts.RAJ_400, 20.0f, 0.04f, this.k.INK);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams3.topMargin = px(4.0f);
        linearLayout2.addView(text, layoutParams3);
        TextView mono2 = this.k.mono(t("ob.pv.row", new Object[0]), 9.0f, this.k.ALT);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams4.topMargin = px(2.0f);
        linearLayout2.addView(mono2, layoutParams4);
        linearLayout.addView(linearLayout2, layoutParams2);
        add(linearLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$yours$14$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ boolean m329lambda$yours$14$comaugmaosoonboardWelcome(TextView textView, int i, KeyEvent keyEvent) {
        saveName();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$yours$15$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m330lambda$yours$15$comaugmaosoonboardWelcome(View view, boolean z) {
        if (!z) {
            saveName();
        }
        view.setBackground(Kit.pill(0, z ? this.k.ACC : this.k.LINE2, this.dn, this.dn * 6.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$yours$16$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m331lambda$yours$16$comaugmaosoonboardWelcome() {
        VoiceSheet.show(this, this.th, this.be, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$yours$17$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m332lambda$yours$17$comaugmaosoonboardWelcome(String str) {
        saveName();
        Store.set("aos.theme", str);
        this.be.set("aos.theme", str);
        render();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$yours$18$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m333lambda$yours$18$comaugmaosoonboardWelcome(String str) {
        saveName();
        Store.set(Advanced.KEY, str);
        render();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$yours$21$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m336lambda$yours$21$comaugmaosoonboardWelcome(String str, String str2, final String str3) {
        this.k.colorPicker(this, str, str2, new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m335lambda$yours$20$comaugmaosoonboardWelcome(str3, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: setColor, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void m335lambda$yours$20$comaugmaosoonboardWelcome(String str, String str2) {
        saveName();
        String colorSet = SettingsModel.colorSet(Store.get("aos.colors"), str, str2);
        Store.set("aos.colors", colorSet);
        this.be.set("aos.colors", colorSet);
        render();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v4 */
    /* JADX WARN: Type inference failed for: r16v5 */
    /* JADX WARN: Type inference failed for: r16v6 */
    void essentials(JSONObject jSONObject) {
        String str;
        boolean z;
        TextView chip;
        r16 != null ? r16 : null;
        String str2;
        boolean z2;
        final Welcome welcome = this;
        JSONObject ai = welcome.ai();
        ArrayList<String> arrayList = new ArrayList();
        for (String str3 : SettingsModel.PROVIDERS) {
            if (opt(ai, str3).optBoolean("hasKey") || ("claude".equals(str3) && ai.optBoolean("hasKey"))) {
                arrayList.add(str3);
            }
        }
        welcome.h2(t("wl.essentials", new Object[0]), t("ob.ess.p", new Object[0]));
        String startLabel = welcome.m.startLabel(jSONObject);
        boolean z3 = true;
        if (startLabel != null) {
            TextView bigBtn = welcome.k.bigBtn(t(startLabel, new Object[0]), welcome.m.run ? null : new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    Welcome.this.esStart();
                }
            });
            welcome.k.enable(bigBtn, !welcome.m.run);
            welcome.add(bigBtn);
        }
        String[][] strArr = ESS;
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            String[] strArr2 = strArr[i];
            String essChip = welcome.m.essChip(jSONObject, strArr2[0]);
            boolean equals = "on".equals(essChip);
            if (equals) {
                chip = welcome.onChip();
                r16 = z3;
            } else {
                Kit kit = welcome.k;
                if ("asking".equals(essChip)) {
                    z = z3;
                    str = t("ob.ess.asking", new Object[0]);
                } else {
                    boolean z4 = z3;
                    if ("skipped".equals(essChip)) {
                        str = t("ob.ess.skipped", new Object[0]);
                        z = z4;
                    } else {
                        str = "·";
                        z = z4;
                    }
                }
                chip = kit.chip(str, "asking".equals(essChip) ? welcome.k.INK : 0);
                r16 = z;
            }
            String t = (!"health".equals(strArr2[0]) || equals || "connect".equals(gate(jSONObject))) ? t(strArr2[3], new Object[0]) : t("ob.hc.old", new Object[0]);
            int i2 = i;
            String str4 = strArr2[r16];
            String t2 = t(strArr2[2], new Object[0]);
            if (equals || "asking".equals(essChip)) {
                str2 = t;
                z2 = false;
            } else {
                str2 = t;
                z2 = r16;
            }
            welcome = this;
            welcome.add(welcome.permCard(str4, equals, t2, false, str2, chip, z2));
            i = i2 + 1;
            z3 = r16;
        }
        boolean z5 = z3;
        if (welcome.m.aiShown(jSONObject)) {
            welcome.add(welcome.k.section(t("ob.yourai", new Object[0])));
            if (!arrayList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String str5 : arrayList) {
                    sb.append(sb.length() > 0 ? " + " : "").append("openai".equals(str5) ? "OPENAI-COMPATIBLE" : up(str5));
                }
                String optString = opt(ai, (String) arrayList.get(0)).optString("last4", "claude".equals(arrayList.get(0)) ? ai.optString("last4") : "");
                welcome.add(welcome.k.note("M3 8.5l3 3 7-7", welcome.k.ACC, t("ob.ai.connected", "list", sb), optString.isEmpty() ? t("ob.ai.connected.p", new Object[0]) : t("ob.ai.connected.key", "l4", optString), null, true));
            } else {
                welcome.add(welcome.gemCard());
                welcome.paCard();
                welcome.add(welcome.ghostBig(t("ob.skipai", new Object[0]), new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        Welcome.this.m300lambda$essentials$22$comaugmaosoonboardWelcome();
                    }
                }));
            }
        }
        TextView fine = welcome.k.fine("");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(t("ob.custom.pre", new Object[0]) + " ");
        int length2 = spannableStringBuilder.length();
        spannableStringBuilder.append((CharSequence) t("ob.custom.link", new Object[0]));
        spannableStringBuilder.setSpan(new ForegroundColorSpan(welcome.k.ACC), length2, spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(new UnderlineSpan(), length2, spannableStringBuilder.length(), 33);
        spannableStringBuilder.append((CharSequence) ": ").append((CharSequence) t("ob.custom.post", new Object[0]));
        fine.setText(spannableStringBuilder);
        fine.setClickable(z5);
        fine.setFocusable(z5);
        fine.setContentDescription(t("ob.custom.link", new Object[0]) + ". " + t("ob.custom.post", new Object[0]));
        fine.setMinHeight(welcome.px(44.0f));
        fine.setGravity(16);
        fine.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.jarvis.assist.aosui.onboard.Welcome.4
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName("android.widget.Button");
            }
        });
        fine.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Welcome.this.m301lambda$essentials$23$comaugmaosoonboardWelcome(view);
            }
        });
        welcome.add(fine);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$essentials$22$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m300lambda$essentials$22$comaugmaosoonboardWelcome() {
        saveName();
        this.m.skipAi();
        render();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$essentials$23$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m301lambda$essentials$23$comaugmaosoonboardWelcome(View view) {
        Feedback.fx(view, 0);
        saveName();
        this.m.customise();
        render();
    }

    TextView ghostBig(String str, Runnable runnable) {
        TextView bigBtn = this.k.bigBtn(str, runnable);
        bigBtn.setTextColor(this.k.DIM);
        bigBtn.setBackground(Kit.pill(0, this.k.LINE2, this.dn, this.dn * 999.0f));
        return bigBtn;
    }

    LinearLayout gemCard() {
        LinearLayout card = this.k.card(true, false);
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        linearLayout.addView(this.k.iconView(IC_SPARK, this.k.ACC, 18.0f));
        TextView text = this.k.text(t("ob.gem.h", new Object[0]), Fonts.RAJ_600, 15.0f, 0.0f, this.k.INK);
        Kit.heading(text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.leftMargin = px(10.0f);
        linearLayout.addView(text, layoutParams);
        linearLayout.addView(this.k.mono(t("ob.gem.time", new Object[0]), 8.0f, this.k.ACC));
        card.addView(linearLayout);
        String[] strArr = {t("ob.gem.s1", new Object[0]), t("ob.gem.s2", new Object[0]), t("ob.gem.s3", new Object[0])};
        int i = 0;
        while (i < 3) {
            LinearLayout linearLayout2 = new LinearLayout(this.c);
            linearLayout2.setGravity(48);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.topMargin = px(i == 0 ? 8.0f : 4.0f);
            int i2 = i + 1;
            TextView num = this.k.num(String.valueOf(i2));
            ((LinearLayout.LayoutParams) num.getLayoutParams()).rightMargin = px(8.0f);
            linearLayout2.addView(num);
            linearLayout2.addView(this.k.text(strArr[i], Fonts.RAJ_400, 13.0f, 0.0f, this.k.DIM), new LinearLayout.LayoutParams(0, -2, 1.0f));
            card.addView(linearLayout2, layoutParams2);
            i = i2;
        }
        View btn = this.k.btn(t("ob.gem.open", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Welcome.this.m305lambda$gemCard$24$comaugmaosoonboardWelcome();
            }
        });
        TextView btn2 = this.k.btn(this.m.busy.isEmpty() ? t("ob.gem.paste", new Object[0]) : t("ns.checking", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Welcome.this.esPaste();
            }
        });
        this.k.enable(btn2, this.m.busy.isEmpty());
        card.addView(wrapRow(btn, btn2));
        if (this.m.manual) {
            final EditText keep = keep(this.k.input("", t("ob.gem.type", new Object[0]), true, true, 0), "gem");
            keep.setContentDescription(t("ob.gem.type", new Object[0]));
            TextView btn3 = this.k.btn(t("ns.verify", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Welcome.this.m306lambda$gemCard$25$comaugmaosoonboardWelcome(keep);
                }
            });
            this.k.enable(btn3, this.m.busy.isEmpty());
            card.addView(this.k.hrow(Kit.grow(keep), btn3));
        }
        View msg = this.k.msg(this.m.busy.isEmpty() ? this.m.msg : this.m.busy);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams3.topMargin = px(6.0f);
        card.addView(msg, layoutParams3);
        TextView fine = this.k.fine(t("ob.gem.other", new Object[0]));
        ((LinearLayout.LayoutParams) fine.getLayoutParams()).topMargin = px(6.0f);
        card.addView(fine);
        return card;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$gemCard$24$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m305lambda$gemCard$24$comaugmaosoonboardWelcome() {
        this.be.url(Keys.url("gemini"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$gemCard$25$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m306lambda$gemCard$25$comaugmaosoonboardWelcome(EditText editText) {
        String trim = editText.getText().toString().trim();
        if (trim.isEmpty()) {
            return;
        }
        esSave(trim);
    }

    View wrapRow(View... viewArr) {
        Flow flow = this.k.flow(8);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(8.0f);
        flow.setLayoutParams(layoutParams);
        for (View view : viewArr) {
            if (view != null) {
                flow.addView(view);
            }
        }
        return flow;
    }

    void paCard() {
        if (this.pa.optString("pkg").isEmpty()) {
            return;
        }
        String optString = this.pa.optString("label").isEmpty() ? this.pa.optString("pkg") : this.pa.optString("label");
        LinearLayout card = this.k.card(true, false);
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        Bitmap dataImg = dataImg(this.pa.optString("icon"));
        if (dataImg != null) {
            ImageView imageView = new ImageView(this.c);
            imageView.setImageBitmap(dataImg);
            imageView.setImportantForAccessibility(2);
            imageView.setClipToOutline(true);
            imageView.setOutlineProvider(new ViewOutlineProvider() { // from class: com.jarvis.assist.aosui.onboard.Welcome.5
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
            linearLayout.addView(imageView, new LinearLayout.LayoutParams(px(18.0f), px(18.0f)));
        } else {
            linearLayout.addView(this.k.iconView(IC_PHONE, this.k.INK, 18.0f));
        }
        TextView text = this.k.text(t("wl.pa.title", "name", optString), Fonts.RAJ_600, 15.0f, 0.0f, this.k.INK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.leftMargin = px(10.0f);
        linearLayout.addView(text, layoutParams);
        card.addView(linearLayout);
        View text2 = this.k.text(t("wl.pa.p", "name", optString), Fonts.RAJ_400, 13.0f, 0.0f, this.k.DIM);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.topMargin = px(4.0f);
        card.addView(text2, layoutParams2);
        card.addView(wrapRow(this.k.btn(up(t("wl.pa.btn", "name", optString)), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                Welcome.this.m321lambda$paCard$26$comaugmaosoonboardWelcome();
            }
        })));
        add(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$paCard$26$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m321lambda$paCard$26$comaugmaosoonboardWelcome() {
        saveName();
        this.m.skipAi();
        render();
    }

    static Bitmap dataImg(String str) {
        int indexOf;
        if (str == null) {
            indexOf = -1;
        } else {
            try {
                indexOf = str.indexOf("base64,");
            } catch (Exception e) {
                return null;
            }
        }
        if (indexOf < 0) {
            return null;
        }
        byte[] decode = Base64.decode(str.substring(indexOf + 7), 0);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    static String gate(JSONObject jSONObject) {
        if (jSONObject.has("healthGate")) {
            return jSONObject.optString("healthGate");
        }
        return jSONObject.optBoolean("health") ? "on" : jSONObject.optBoolean("healthAvailable") ? "connect" : "unsupported";
    }

    void health(JSONObject jSONObject) {
        String gate = gate(jSONObject);
        boolean equals = "on".equals(gate);
        h2(t("wl.health", new Object[0]), t("ob.health.p", new Object[0]));
        if ("unsupported".equals(gate)) {
            add(permCard(IC_HEART, false, t("ob.p.health", new Object[0]), false, t("ob.hc.old", new Object[0]), this.k.btn(t("ob.hc.toset", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    Welcome.this.m307lambda$health$27$comaugmaosoonboardWelcome();
                }
            }), false));
        } else {
            add(permCard(IC_HEART, equals, t("ob.p.health", new Object[0]), true, t("ob.hc.p", new Object[0]), equals ? onChip() : this.k.btn(t("ob.hc.connect", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    Welcome.this.m308lambda$health$28$comaugmaosoonboardWelcome();
                }
            }), false));
        }
        if (!"unsupported".equals(gate)) {
            add(this.k.fine(t("ns.hg.hc.xiaomi.p", new Object[0])));
        }
        add(this.k.section(t("ob.hs.later", new Object[0])));
        String[][] strArr = {new String[]{IC_WATCH, "WHOOP", "ob.hs.whoop"}, new String[]{IC_HEART, "Oura", "ob.hs.oura"}, new String[]{IC_HEART, "Ultrahuman", "ob.hs.uh"}, new String[]{IC_APPLE, t("ob.hs.apple", new Object[0]), "ob.hs.apple.p"}};
        for (int i = 0; i < 4; i++) {
            String[] strArr2 = strArr[i];
            add(permCard(strArr2[0], false, strArr2[1], false, t(strArr2[2], new Object[0]), null, true));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$health$27$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m307lambda$health$27$comaugmaosoonboardWelcome() {
        this.be.openSettings("health");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$health$28$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m308lambda$health$28$comaugmaosoonboardWelcome() {
        this.be.askPerm("health");
    }

    void aiStep() {
        Iterator<String> it;
        String str;
        View spacer;
        View view;
        View spacer2;
        List<String> list;
        View view2;
        char c;
        String t;
        JSONObject ai = ai();
        String str2 = "claude";
        JSONObject optJSONObject = ai.optJSONObject("claude") != null ? ai.optJSONObject("claude") : json("hasKey", Boolean.valueOf(ai.optBoolean("hasKey")), "last4", ai.optString("last4"));
        JSONObject opt = opt(ai, "gemini");
        JSONObject opt2 = opt(ai, "openai");
        h2(t("wl.ai", new Object[0]), t("ob.ai.p", new Object[0]));
        add(this.k.note(IC_LOCK, this.k.ACC, t("ob.keys.h", new Object[0]), t("ob.keys.p", new Object[0]), null, true));
        keyCard("claude", "Anthropic Claude", t("ob.claude.p", new Object[0]), optJSONObject, SettingsModel.MODELS_CLAUDE);
        keyCard("gemini", "Google Gemini", t("ob.gemini.p", new Object[0]), opt, SettingsModel.MODELS_GEMINI);
        oaiCard(opt2);
        add(this.k.section(t("ob.order", new Object[0])));
        final List<String> order = SettingsModel.order(this.be.aiHasKey());
        LinearLayout list2 = this.k.list();
        Iterator<String> it2 = SettingsModel.allProviders(order).iterator();
        while (it2.hasNext()) {
            final String next = it2.next();
            boolean contains = order.contains(next);
            int indexOf = order.indexOf(next);
            JSONObject jSONObject = str2.equals(next) ? optJSONObject : "gemini".equals(next) ? opt : opt2;
            String str3 = str2.equals(next) ? "Anthropic Claude" : "gemini".equals(next) ? "Google Gemini" : "OpenAI-compatible";
            if (!contains || indexOf <= 0) {
                it = it2;
                str = str2;
                spacer = spacer();
            } else {
                it = it2;
                str = str2;
                spacer = this.k.circle(Kit.UP, t("ns.up", "name", str3), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda35
                    @Override // java.lang.Runnable
                    public final void run() {
                        Welcome.this.m294lambda$aiStep$29$comaugmaosoonboardWelcome(order, next);
                    }
                });
            }
            if (!contains || indexOf >= order.size() - 1) {
                view = spacer;
                spacer2 = spacer();
            } else {
                view = spacer;
                spacer2 = this.k.circle(Kit.DOWN, t("ns.down", "name", str3), true, 34.0f, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda36
                    @Override // java.lang.Runnable
                    public final void run() {
                        Welcome.this.m295lambda$aiStep$30$comaugmaosoonboardWelcome(order, next);
                    }
                });
            }
            Kit kit = this.k;
            if (jSONObject.optBoolean("hasKey")) {
                list = order;
                view2 = spacer2;
                c = 0;
                t = t("ns.ready", new Object[0]);
            } else {
                list = order;
                view2 = spacer2;
                c = 0;
                t = t("ns.notset", new Object[0]);
            }
            String str4 = t;
            View[] viewArr = new View[3];
            viewArr[c] = view;
            viewArr[1] = view2;
            viewArr[2] = this.k.toggle(contains, t("ns.use", "name", str3), new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda37
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Welcome.this.m296lambda$aiStep$31$comaugmaosoonboardWelcome(next, (Boolean) obj);
                }
            });
            LinearLayout row = kit.row(null, 0, str3, str4, false, null, viewArr);
            row.addView(this.k.num(contains ? String.valueOf(indexOf + 1) : "–"), 0);
            ((LinearLayout.LayoutParams) row.getChildAt(1).getLayoutParams()).leftMargin = px(12.0f);
            this.k.add(list2, row);
            it2 = it;
            str2 = str;
            order = list;
        }
        add(list2);
        add(this.k.fine(t("ob.ai.fine", new Object[0])));
        paCard();
        if (OnboardModel.skipAiShown(optJSONObject.optBoolean("hasKey") || opt.optBoolean("hasKey") || opt2.optBoolean("hasKey"))) {
            add(ghostBig(t("ob.skipai", new Object[0]), new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    Welcome.this.m297lambda$aiStep$32$comaugmaosoonboardWelcome();
                }
            }));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aiStep$29$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m294lambda$aiStep$29$comaugmaosoonboardWelcome(List list, String str) {
        String orderMove = SettingsModel.orderMove(list, str, -1);
        if (orderMove != null) {
            this.be.aiSetOrder(orderMove);
            render();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aiStep$30$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m295lambda$aiStep$30$comaugmaosoonboardWelcome(List list, String str) {
        String orderMove = SettingsModel.orderMove(list, str, 1);
        if (orderMove != null) {
            this.be.aiSetOrder(orderMove);
            render();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aiStep$31$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m296lambda$aiStep$31$comaugmaosoonboardWelcome(String str, Boolean bool) {
        this.be.aiSetOrder(SettingsModel.orderToggle(SettingsModel.order(this.be.aiHasKey()), str));
        render();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aiStep$32$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m297lambda$aiStep$32$comaugmaosoonboardWelcome() {
        saveName();
        this.m.skipAi();
        render();
    }

    View spacer() {
        View view = new View(this.c);
        view.setLayoutParams(new LinearLayout.LayoutParams(px(34.0f), px(34.0f)));
        view.setImportantForAccessibility(2);
        return view;
    }

    LinearLayout cardHead(LinearLayout linearLayout, String str, boolean z, String str2) {
        LinearLayout linearLayout2 = new LinearLayout(this.c);
        linearLayout2.setGravity(16);
        Kit kit = this.k;
        Kit kit2 = this.k;
        linearLayout2.addView(kit.iconView(IC_KEY, z ? kit2.ACC : kit2.INK, 18.0f));
        TextView text = this.k.text(str, Fonts.RAJ_600, 15.0f, 0.0f, this.k.INK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.leftMargin = px(10.0f);
        linearLayout2.addView(text, layoutParams);
        linearLayout2.addView(this.k.chip(str2, z ? this.k.ACC : 0));
        linearLayout.addView(linearLayout2);
        return linearLayout2;
    }

    void keyCard(final String str, String str2, String str3, JSONObject jSONObject, String[] strArr) {
        boolean optBoolean = jSONObject.optBoolean("hasKey");
        LinearLayout card = this.k.card(true, false);
        cardHead(card, str2, optBoolean, optBoolean ? "✓ " + t("ob.key.chip", "l4", jSONObject.optString("last4")) : t("ns.nokey", new Object[0]));
        View text = this.k.text(str3, Fonts.RAJ_400, 13.0f, 0.0f, this.k.DIM);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(4.0f);
        card.addView(text, layoutParams);
        final TextView msg = this.k.msg("");
        final EditText keep = keep(this.k.input("", t("ob.key.ph", new Object[0]), true, true, 0), "key." + str);
        keep.setContentDescription(str2 + " " + t("ns.key", new Object[0]));
        final TextView btn = this.k.btn(t("ns.verify", new Object[0]), false, true, null);
        btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda32
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Welcome.this.m310lambda$keyCard$34$comaugmaosoonboardWelcome(keep, btn, str, msg, view);
            }
        });
        TextView btn2 = this.k.btn(t("ns.paste", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                Welcome.this.m311lambda$keyCard$35$comaugmaosoonboardWelcome(msg, str);
            }
        });
        btn2.setContentDescription(t("ns.paste", new Object[0]) + " " + str2);
        card.addView(this.k.hrow(Kit.grow(keep), btn));
        card.addView(wrapRow(btn2));
        String str4 = this.be.get("ai.model." + str);
        if (str4 == null || str4.isEmpty()) {
            str4 = strArr[0];
        }
        Kit kit = this.k;
        String str5 = str4;
        Kit kit2 = this.k;
        String str6 = str2 + " " + t("ns.model", new Object[0]);
        Backend backend = this.be;
        Objects.requireNonNull(backend);
        card.addView(kit.hrow(this.k.mono(t("ns.model", new Object[0]), 9.0f, this.k.DIM), ModelPick.select(kit2, this, str, str6, strArr, str5, optBoolean, new Welcome$$ExternalSyntheticLambda9(backend), msg, new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m312lambda$keyCard$36$comaugmaosoonboardWelcome(str, (String) obj);
            }
        })));
        card.addView(msg);
        add(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$keyCard$34$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m310lambda$keyCard$34$comaugmaosoonboardWelcome(final EditText editText, final TextView textView, final String str, final TextView textView2, View view) {
        String trim = editText.getText().toString().trim();
        if (trim.isEmpty()) {
            return;
        }
        textView.setText(t("ns.checking", new Object[0]));
        this.k.enable(textView, false);
        Consumer<String> consumer = new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m309lambda$keyCard$33$comaugmaosoonboardWelcome(str, editText, textView, textView2, (String) obj);
            }
        };
        if ("claude".equals(str)) {
            this.be.aiSetKey(trim, consumer);
        } else {
            this.be.geminiSetKey(trim, consumer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$keyCard$33$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m309lambda$keyCard$33$comaugmaosoonboardWelcome(String str, EditText editText, TextView textView, TextView textView2, String str2) {
        JSONObject J = J(str2);
        if (KeyCheck.settle(this.drafts, "key." + str, J)) {
            editText.setText("");
            render();
            provisional(J);
        } else {
            textView.setText(t("ns.verify", new Object[0]));
            this.k.enable(textView, true);
            textView2.setText(up(J.optString("error", t("ns.keyfail", new Object[0]))));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$keyCard$36$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m312lambda$keyCard$36$comaugmaosoonboardWelcome(String str, String str2) {
        this.be.set("ai.model." + str, str2);
        render();
    }

    void pasteAny(TextView textView) {
        m311lambda$keyCard$35$comaugmaosoonboardWelcome(textView, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: pasteAny, reason: merged with bridge method [inline-methods] */
    public void m311lambda$keyCard$35$comaugmaosoonboardWelcome(final TextView textView, String str) {
        final String looks = Keys.looks(this.be.clip("ai"), "ai");
        if (looks.isEmpty()) {
            textView.setText(t("ns.paste.noai", new Object[0]));
            return;
        }
        String provider = KeyFlow.provider(looks, null);
        if (provider == null) {
            this.k.pickSheet(this, t("key.which", new Object[0]), KeyFlow.PICK_NAMES, KeyFlow.PICK, KeyFlow.preselect(str), new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda21
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Welcome.this.m322lambda$pasteAny$37$comaugmaosoonboardWelcome(textView, looks, (String) obj);
                }
            });
        } else {
            m322lambda$pasteAny$37$comaugmaosoonboardWelcome(textView, looks, provider);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: pasteSave, reason: merged with bridge method [inline-methods] */
    public void m322lambda$pasteAny$37$comaugmaosoonboardWelcome(final TextView textView, String str, final String str2) {
        final String last4 = Keys.last4(str);
        textView.setText(t("ns.checking.key", "name", up(Keys.name(str2)), "l4", last4));
        save(str2, str, new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m323lambda$pasteSave$38$comaugmaosoonboardWelcome(str2, last4, textView, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$pasteSave$38$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m323lambda$pasteSave$38$comaugmaosoonboardWelcome(String str, String str2, TextView textView, String str3) {
        JSONObject J = J(str3);
        if (!J.optBoolean("ok") && !J.optBoolean("hasKey")) {
            textView.setText(up(J.optString("error", t("ns.keyfail", new Object[0]))));
        } else {
            this.k.toast(this, J.optBoolean("provisional") ? J.optString("note") : t("ns.key.connected", "name", up(Keys.name(str)), "l4", str2));
            render();
        }
    }

    void oaiCard(JSONObject jSONObject) {
        String optString;
        final boolean optBoolean = jSONObject.optBoolean("hasKey");
        LinearLayout card = this.k.card(true, false);
        cardHead(card, "OpenAI-compatible", optBoolean, optBoolean ? "✓ " + t("ob.oai.ready", new Object[0]) : t("ob.oai.notset", new Object[0]));
        View text = this.k.text(t("ns.oai.p", new Object[0]), Fonts.RAJ_400, 13.0f, 0.0f, this.k.DIM);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(4.0f);
        card.addView(text, layoutParams);
        final TextView msg = this.k.msg("");
        int oaiPreset = SettingsModel.oaiPreset(jSONObject.optString("base"));
        final EditText keep = keep(this.k.input(jSONObject.optString("base").isEmpty() ? SettingsModel.OAI[0][2] : jSONObject.optString("base"), t("ns.oai.base", new Object[0]), false, true, 17), "oai.base");
        final EditText keep2 = keep(this.k.input("", optBoolean ? t("ns.oai.key.saved", new Object[0]) : t("ns.oai.key", new Object[0]), true, true, 0), "oai.key");
        if (this.drafts.get("oai.model") != null) {
            optString = this.drafts.get("oai.model");
        } else {
            optString = !jSONObject.optString("model").isEmpty() ? jSONObject.optString("model") : SettingsModel.OAI[0][3];
        }
        final String[] strArr = {optString};
        final TextView[] textViewArr = {null};
        final Consumer consumer = new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.this.m317lambda$oaiCard$39$comaugmaosoonboardWelcome(strArr, textViewArr, (String) obj);
            }
        };
        Kit kit = this.k;
        String t = t("ns.oai.model", new Object[0]);
        String[] strArr2 = {strArr[0]};
        String str = strArr[0];
        Backend backend = this.be;
        Objects.requireNonNull(backend);
        textViewArr[0] = ModelPick.select(kit, this, "openai", t, strArr2, str, optBoolean, new Welcome$$ExternalSyntheticLambda9(backend), msg, consumer);
        int length = SettingsModel.OAI.length;
        final String[] strArr3 = new String[length];
        final String[] strArr4 = new String[SettingsModel.OAI.length];
        for (int i = 0; i < length; i++) {
            strArr3[i] = SettingsModel.OAI[i][1];
            strArr4[i] = SettingsModel.OAI[i][0];
        }
        final String[] strArr5 = {strArr4[oaiPreset]};
        final TextView[] textViewArr2 = {null};
        final Consumer<String> consumer2 = new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Welcome.lambda$oaiCard$40(strArr5, keep, consumer, textViewArr2, (String) obj);
            }
        };
        textViewArr2[0] = this.k.select(this, t("ns.oai.preset", new Object[0]), strArr3, strArr4, strArr5[0], consumer2);
        textViewArr2[0].setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Welcome.this.m318lambda$oaiCard$41$comaugmaosoonboardWelcome(strArr3, strArr4, strArr5, consumer2, view);
            }
        });
        card.addView(this.k.hrow(this.k.mono(t("ns.oai.preset", new Object[0]), 9.0f, this.k.DIM), textViewArr2[0]));
        card.addView(this.k.hrow(keep));
        card.addView(this.k.hrow(keep2));
        final TextView btn = this.k.btn(t("ob.oai.test", new Object[0]), false, true, null);
        btn.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Welcome.this.m320lambda$oaiCard$43$comaugmaosoonboardWelcome(keep, keep2, strArr, msg, optBoolean, btn, view);
            }
        });
        card.addView(this.k.hrow(Kit.grow(textViewArr[0]), btn));
        card.addView(msg);
        add(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$oaiCard$39$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m317lambda$oaiCard$39$comaugmaosoonboardWelcome(String[] strArr, TextView[] textViewArr, String str) {
        strArr[0] = str;
        this.drafts.put("oai.model", str);
        if (textViewArr[0] != null) {
            textViewArr[0].setText(str);
            textViewArr[0].setContentDescription(t("ns.oai.model", new Object[0]) + ", " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$oaiCard$40(String[] strArr, EditText editText, Consumer consumer, TextView[] textViewArr, String str) {
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
    /* renamed from: lambda$oaiCard$41$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m318lambda$oaiCard$41$comaugmaosoonboardWelcome(String[] strArr, String[] strArr2, String[] strArr3, Consumer consumer, View view) {
        this.k.pickSheet(this, t("ns.oai.preset", new Object[0]), strArr, strArr2, strArr3[0], consumer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$oaiCard$43$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m320lambda$oaiCard$43$comaugmaosoonboardWelcome(EditText editText, final EditText editText2, String[] strArr, final TextView textView, boolean z, final TextView textView2, View view) {
        String t;
        String trim = editText.getText().toString().trim();
        String trim2 = editText2.getText().toString().trim();
        String trim3 = strArr[0].trim();
        if (!SettingsModel.httpsOk(trim)) {
            t = t("ns.oai.https", new Object[0]);
        } else if (trim2.isEmpty() && !z) {
            t = t("ns.oai.needkey", new Object[0]);
        } else {
            if (!trim3.isEmpty()) {
                textView2.setText(t("ns.oai.testing", new Object[0]));
                this.be.oaiSet(json("base", trim, "key", trim2, "model", trim3).toString(), new Consumer() { // from class: com.jarvis.assist.aosui.onboard.Welcome$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        Welcome.this.m319lambda$oaiCard$42$comaugmaosoonboardWelcome(editText2, textView2, textView, (String) obj);
                    }
                });
                return;
            }
            t = t("ns.oai.needmodel", new Object[0]);
        }
        textView.setText(t);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$oaiCard$42$com-augma-oso-onboard-Welcome, reason: not valid java name */
    public /* synthetic */ void m319lambda$oaiCard$42$comaugmaosoonboardWelcome(EditText editText, TextView textView, TextView textView2, String str) {
        JSONObject J = J(str);
        if (!J.optBoolean("ok")) {
            textView.setText(t("ob.oai.test", new Object[0]));
            textView2.setText(up(J.optString("error", t("ns.oai.fail", new Object[0]))));
            return;
        }
        editText.setText("");
        this.drafts.clear("oai.key");
        String orderWithOpenai = SettingsModel.orderWithOpenai(str);
        if (orderWithOpenai != null) {
            this.be.aiSetOrder(orderWithOpenai);
        }
        render();
        provisional(J);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Progress extends View {
        final Path chk;
        final Paint p;
        final Paint tx;

        Progress(Context context) {
            super(context);
            this.p = new Paint(1);
            this.tx = new Paint(1);
            this.chk = Svg.path("M3 8.5l3 3 7-7");
            setImportantForAccessibility(1);
            setFocusable(true);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            int length = OnboardModel.STEPS.length;
            float px = Welcome.this.px(11.0f);
            float paddingTop = getPaddingTop() + px;
            float paddingLeft = getPaddingLeft() + px;
            int i = length - 1;
            float width = (((getWidth() - getPaddingRight()) - px) - paddingLeft) / i;
            this.p.setStrokeWidth(Math.max(1.0f, Welcome.this.dn));
            this.tx.setTypeface(Fonts.MONO);
            this.tx.setTextAlign(Paint.Align.CENTER);
            int i2 = 0;
            while (i2 < i) {
                this.p.setStyle(Paint.Style.STROKE);
                this.p.setColor(Welcome.this.k.LINE2);
                float px2 = (i2 * width) + paddingLeft + px + Welcome.this.px(4.0f);
                int i3 = i2 + 1;
                float px3 = (((i3 * width) + paddingLeft) - px) - Welcome.this.px(4.0f);
                if (px3 > px2) {
                    canvas.drawLine(px2, paddingTop, px3, paddingTop, this.p);
                }
                i2 = i3;
            }
            int i4 = 0;
            while (i4 < length) {
                float f2 = (i4 * width) + paddingLeft;
                boolean z = i4 < Welcome.this.m.st;
                boolean z2 = i4 == Welcome.this.m.st;
                if (z2) {
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(Welcome.this.px(3.0f));
                    this.p.setColor(Theme.a(Welcome.this.k.ACC, 0.2f));
                    canvas.drawCircle(f2, paddingTop, Welcome.this.px(1.5f) + px, this.p);
                    this.p.setStrokeWidth(Math.max(1.0f, Welcome.this.dn));
                }
                this.p.setStyle(Paint.Style.FILL);
                Paint paint = this.p;
                Kit kit = Welcome.this.k;
                paint.setColor(z ? kit.ACC : kit.BG);
                canvas.drawCircle(f2, paddingTop, px, this.p);
                this.p.setStyle(Paint.Style.STROKE);
                this.p.setColor((z || z2) ? Welcome.this.k.ACC : Welcome.this.k.LINE2);
                canvas.drawCircle(f2, paddingTop, px, this.p);
                if (z) {
                    int save = canvas.save();
                    float px4 = Welcome.this.px(10.0f) / 16.0f;
                    float f3 = 8.0f * px4;
                    f = 2.0f;
                    canvas.translate(f2 - f3, paddingTop - f3);
                    canvas.scale(px4, px4);
                    this.p.setColor(Welcome.this.k.BG);
                    this.p.setStrokeWidth(1.6f);
                    this.p.setStrokeCap(Paint.Cap.ROUND);
                    canvas.drawPath(this.chk, this.p);
                    canvas.restoreToCount(save);
                    this.p.setStrokeWidth(Math.max(1.0f, Welcome.this.dn));
                } else {
                    f = 2.0f;
                    this.tx.setTextSize(Welcome.this.px(10.0f) * Welcome.this.k.lz);
                    Paint paint2 = this.tx;
                    Kit kit2 = Welcome.this.k;
                    paint2.setColor(z2 ? kit2.ACC : kit2.DIM);
                    canvas.drawText(String.valueOf(i4 + 1), f2, paddingTop - ((this.tx.ascent() + this.tx.descent()) / 2.0f), this.tx);
                }
                if (z2) {
                    this.tx.setTextSize(Math.max(Welcome.this.px(7.0f), Welcome.this.px(7.0f) * Welcome.this.k.lz));
                    this.tx.setLetterSpacing(0.1f);
                    this.tx.setColor(Welcome.this.k.ACC);
                    String t = Welcome.t("wl.step." + OnboardModel.STEPS[i4], new Object[0]);
                    float measureText = this.tx.measureText(t) / f;
                    canvas.drawText(t, Math.max((getPaddingLeft() / f) + measureText, Math.min((getWidth() - measureText) - (getPaddingRight() / f), f2)), paddingTop + px + Welcome.this.px(10.0f), this.tx);
                    this.tx.setLetterSpacing(0.0f);
                }
                i4++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Mark extends View {
        final Paint p;
        final long t0;

        Mark(Context context, int i) {
            super(context);
            this.p = new Paint(1);
            this.t0 = SystemClock.uptimeMillis();
            setImportantForAccessibility(2);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Converge.draw(canvas, this.p, getWidth() / 2.0f, getHeight() / 2.0f, Math.min(getWidth(), getHeight()), Welcome.this.still ? 0.0f : (((float) ((SystemClock.uptimeMillis() - this.t0) % 40000)) / 40000.0f) * 360.0f, Welcome.this.th.ACCENT_RAW, Welcome.this.th.RED, Welcome.this.th.light ? Palette.LIGHT_INK : Welcome.this.th.INK_RAW);
            if (Welcome.this.still || !isShown()) {
                return;
            }
            postInvalidateDelayed(33L);
        }
    }
}
