package com.jarvis.assist.aosui.brief;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.ai.AndroidHost;
import com.jarvis.assist.aosui.ai.Features;
import com.jarvis.assist.aosui.ai.NativeAssist$$ExternalSyntheticLambda58;
import com.jarvis.assist.aosui.ai.Speech;
import com.jarvis.assist.aosui.ai.Tools;
import com.jarvis.assist.aosui.brief.BriefModel;
import com.jarvis.assist.aosui.brief.BriefViews;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.MemPolicy;
import com.jarvis.assist.aosui.core.Memory;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.PerfModel;
import com.jarvis.assist.aosui.core.Profile;
import com.jarvis.assist.aosui.core.SettingsModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnfoldModel;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.ui.Flow;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Sheet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class BriefPage {
    static final Handler MAIN = new Handler(Looper.getMainLooper());
    final Features F;
    final Backend be;
    final LinearLayout body;
    final Context c;
    int cardN;
    LinearLayout colL;
    LinearLayout colR;
    boolean dead;
    final float dn;
    int gen;
    Kit.Grid grid;
    final TextView hint;
    int insB;
    int insL;
    int insR;
    int insT;
    Kit k;
    final FrameClock.Handle loop;
    BriefModel.Out out;
    int pending;
    boolean preparing;
    boolean resumed;
    final BriefViews.DayMini ring;
    final FrameLayout root;
    final BriefViews.PullScroll sc;
    boolean speaking;
    boolean spoke;
    int ticks;
    TextView timeV;
    float wL;
    float wR;
    final Theme th = new Theme();
    final BriefModel.In in = new BriefModel.In();
    BriefModel.Cfg cfg = new BriefModel.Cfg();
    final BriefModel.Tr tr = new BriefModel.Tr() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda18
        @Override // com.jarvis.assist.aosui.brief.BriefModel.Tr
        public final String t(String str) {
            return Strings.t(str);
        }
    };
    String mode = "auto";
    boolean stale = true;
    String nSum = "";
    String nState = "";
    final HashSet<String> shopDone = new HashSet<>();
    final ArrayList<BriefViews.MiniRing> minis = new ArrayList<>();
    float drawK = 1.0f;
    final Memory.Trim trim = new Memory.Trim() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda19
        @Override // com.jarvis.assist.aosui.core.Memory.Trim
        public final void trim(int i, int i2) {
            BriefPage.this.trim(i, i2);
        }
    };
    final Runnable tick = new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage.1
        @Override // java.lang.Runnable
        public void run() {
            if (BriefPage.this.resumed) {
                BriefPage.this.in.now = BriefPage.this.now();
                BriefModel.Out out = BriefPage.this.out;
                if (BriefPage.this.timeV != null && out != null) {
                    BriefPage.this.timeV.setText(BriefModel.hm(BriefPage.this.in.now));
                }
                BriefPage.this.ringNow();
                BriefPage briefPage = BriefPage.this;
                int i = briefPage.ticks + 1;
                briefPage.ticks = i;
                if (i % 15 == 0) {
                    BriefPage.this.rebuild();
                }
                BriefPage.MAIN.postDelayed(this, UnfoldModel.RATE_MS);
            }
        }
    };
    final Runnable autoSpeak = new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda20
        @Override // java.lang.Runnable
        public final void run() {
            BriefPage.this.m190lambda$new$10$comaugmaosobriefBriefPage();
        }
    };

    public BriefPage(Backend backend) {
        this.be = backend;
        this.c = backend.ctx();
        Memory.init(this.c);
        Store.init(this.c);
        Fonts.init(this.c);
        Strings.init(this.c);
        Perf.init(this.c, backend.deviceInfo());
        this.dn = this.c.getResources().getDisplayMetrics().density;
        this.F = new Features(AndroidHost.forSettings(backend));
        Memory.on(this.trim);
        this.root = new FrameLayout(this.c);
        this.sc = new BriefViews.PullScroll(this.c);
        this.sc.setFillViewport(true);
        this.sc.setVerticalScrollBarEnabled(false);
        this.sc.setOverScrollMode(2);
        this.body = new LinearLayout(this.c);
        this.body.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(this.c);
        frameLayout.addView(this.body, new FrameLayout.LayoutParams(-1, -2, 1));
        frameLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda21
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                BriefPage.this.m188lambda$new$0$comaugmaosobriefBriefPage(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        this.sc.addView(frameLayout, new FrameLayout.LayoutParams(-1, -2));
        this.root.addView(this.sc, new FrameLayout.LayoutParams(-1, -1));
        this.hint = new TextView(this.c);
        this.hint.setTypeface(Fonts.MONO);
        this.hint.setTextSize(1, 9.0f);
        this.hint.setLetterSpacing(0.14f);
        this.hint.setGravity(17);
        this.hint.setAlpha(0.0f);
        this.hint.setImportantForAccessibility(2);
        this.root.addView(this.hint, new FrameLayout.LayoutParams(-1, -2, 48));
        this.sc.hint = this.hint;
        this.sc.l = new BriefViews.PullScroll.L() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda22
            @Override // com.jarvis.assist.aosui.brief.BriefViews.PullScroll.L
            public final void refresh() {
                BriefPage.this.m189lambda$new$1$comaugmaosobriefBriefPage();
            }
        };
        this.sc.sPull = t("db.refresh.pull", new Object[0]);
        this.sc.sRel = t("db.refresh.release", new Object[0]);
        this.sc.sBusy = t("db.refresh.busy", new Object[0]);
        this.ring = new BriefViews.DayMini(this.c, this.dn);
        this.ring.setFocusable(true);
        this.ring.setImportantForAccessibility(1);
        this.loop = FrameClock.I.add(new FrameClock.Sub() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda23
            @Override // com.jarvis.assist.aosui.core.FrameClock.Sub
            public final boolean frame(long j, float f) {
                return BriefPage.this.frame(j, f);
            }
        }, this.ring);
        this.root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda24
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return BriefPage.this.m191lambda$new$2$comaugmaosobriefBriefPage(view, windowInsets);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m188lambda$new$0$comaugmaosobriefBriefPage(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int round = Math.round((land() ? 1100 : 640) * this.dn);
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
    /* renamed from: lambda$new$1$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m189lambda$new$1$comaugmaosobriefBriefPage() {
        load(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ WindowInsets m191lambda$new$2$comaugmaosobriefBriefPage(View view, WindowInsets windowInsets) {
        int systemWindowInsetTop;
        int systemWindowInsetBottom;
        int i;
        int i2;
        if (Build.VERSION.SDK_INT >= 30) {
            Insets insets = windowInsets.getInsets(WindowInsets.Type.displayCutout());
            Insets insets2 = windowInsets.getInsets(WindowInsets.Type.systemBars());
            systemWindowInsetTop = Math.max(insets.top, insets2.top);
            systemWindowInsetBottom = insets2.bottom;
            i = Math.max(insets.left, insets2.left);
            i2 = Math.max(insets.right, insets2.right);
        } else {
            systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
            systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
            i = 0;
            i2 = 0;
        }
        this.insT = systemWindowInsetTop;
        this.insB = systemWindowInsetBottom;
        this.insL = i;
        this.insR = i2;
        pad();
        ((FrameLayout.LayoutParams) this.hint.getLayoutParams()).topMargin = Math.max(px(14.0f), systemWindowInsetTop) + px(4.0f);
        return windowInsets;
    }

    public View view() {
        return this.root;
    }

    int px(float f) {
        return Math.round(f * this.dn);
    }

    static String t(String str, Object... objArr) {
        return objArr.length == 0 ? Strings.t(str) : Strings.t(str, objArr);
    }

    static JSONObject J(String str) {
        if (str != null) {
            try {
                if (!str.isEmpty() && !"null".equals(str)) {
                    return new JSONObject(str);
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    boolean land() {
        return this.c.getResources().getConfiguration().orientation == 2;
    }

    void pad() {
        this.body.setPadding(px(16.0f) + this.insL, Math.max(px(14.0f), this.insT), px(16.0f) + this.insR, px(40.0f) + this.insB);
    }

    public void debug(String str) {
        if ("scroll".equals(str)) {
            this.sc.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    BriefPage.this.m178lambda$debug$3$comaugmaosobriefBriefPage();
                }
            }, 600L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$debug$3$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m178lambda$debug$3$comaugmaosobriefBriefPage() {
        this.sc.scrollTo(0, (this.sc.getHeight() * 4) / 5);
    }

    public void open() {
        this.mode = "auto";
        this.spoke = false;
        this.nSum = "";
        this.nState = "";
        this.sc.scrollTo(0, 0);
        drawIn();
    }

    public void onResume() {
        this.resumed = true;
        FrameClock.I.setActive(true);
        load(false);
        MAIN.removeCallbacks(this.tick);
        MAIN.postDelayed(this.tick, UnfoldModel.RATE_MS);
        if (this.drawK < 1.0f) {
            this.loop.kick();
        }
    }

    public void onPause() {
        this.resumed = false;
        if (this.drawK < 1.0f) {
            drawDone();
        }
        MAIN.removeCallbacks(this.tick);
        MAIN.removeCallbacks(this.autoSpeak);
        commitShop();
        this.speaking = false;
        this.preparing = false;
    }

    public void onDestroy() {
        this.dead = true;
        Memory.off(this.trim);
        MAIN.removeCallbacks(this.tick);
        MAIN.removeCallbacks(this.autoSpeak);
        this.loop.remove();
        Sheet.closeTop();
    }

    public boolean onBack() {
        return Sheet.closeTop();
    }

    public void onConfig() {
        pad();
        render();
    }

    public void onPerm() {
        if (this.resumed) {
            load(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trim(int i, int i2) {
        if (!this.resumed && MemPolicy.has(i2, 2)) {
            this.ring.setLayerType(0, null);
        }
        if (this.resumed || !MemPolicy.has(i2, 8)) {
            return;
        }
        this.body.removeAllViews();
        this.minis.clear();
        this.colR = null;
        this.colL = null;
        this.timeV = null;
        this.in.week = null;
        this.stale = true;
    }

    public void onVoiceEvent(String str, String str2) {
        JSONObject J;
        if ("onSpeak".equals(str) && (J = J(str2)) != null) {
            boolean z = this.speaking;
            if (J.has("start")) {
                this.speaking = true;
            }
            if (J.has("end") || J.has("error")) {
                this.speaking = false;
            }
            if (z != this.speaking) {
                render();
            }
        }
    }

    long now() {
        int minutes;
        long currentTimeMillis = System.currentTimeMillis();
        if (!Backend.debug() || (minutes = BriefModel.minutes(Store.get("aos.dbg.now", ""))) < 0) {
            return currentTimeMillis;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, minutes / 60);
        calendar.set(12, minutes % 60);
        return calendar.getTimeInMillis();
    }

    static String dbg(String str) {
        if (!Backend.debug()) {
            return null;
        }
        String str2 = Store.get(str, "");
        if (str2.isEmpty()) {
            return null;
        }
        return str2;
    }

    JSONObject wx(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        long round = Math.round((now() - System.currentTimeMillis()) / 3600000.0d) * 3600000;
        if (round == 0) {
            return jSONObject;
        }
        try {
            JSONObject jSONObject2 = new JSONObject(jSONObject.toString());
            JSONArray optJSONArray = jSONObject2.optJSONArray("hourly");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        optJSONObject.put("ts", BriefModel.ms(optJSONObject.optLong("ts")) + round);
                    }
                }
            }
            return jSONObject2;
        } catch (Exception e) {
            return jSONObject;
        }
    }

    void load(final boolean z) {
        final int i = this.gen + 1;
        this.gen = i;
        this.cfg = BriefModel.cfg(Store.get("aos.brief"));
        this.in.now = now();
        this.in.switchMin = this.cfg.at;
        this.in.name = Store.get(Profile.KEY, "");
        this.in.off.clear();
        this.in.off.addAll(this.cfg.off);
        this.in.fahrenheit = "f".equals(Store.obj("aos.wxcfg").optString("units"));
        this.in.wx = Store.obj("aos.wx").has("now") ? wx(Store.obj("aos.wx")) : null;
        double[] goals = goals();
        this.in.stepsGoal = goals[0];
        this.in.sleepGoalH = goals[1];
        this.in.kcalGoal = goals[2];
        this.in.aiLine = aiLine();
        this.in.aiNotifs = aiState("notifs");
        this.in.wake = this.F.wake();
        this.in.shop = this.F.shop();
        this.in.shopDone.clear();
        this.in.shopDone.addAll(this.shopDone);
        rebuild();
        this.pending = 3;
        Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m183lambda$load$5$comaugmaosobriefBriefPage(i, z);
            }
        });
        Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m185lambda$load$7$comaugmaosobriefBriefPage(z, i);
            }
        });
        Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m187lambda$load$9$comaugmaosobriefBriefPage(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$load$5$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m183lambda$load$5$comaugmaosobriefBriefPage(final int i, final boolean z) {
        final String phoneCal = this.be.phoneCal(2);
        final String nextAlarm = this.be.nextAlarm();
        final String notifList = this.be.notifList();
        final String battery = this.be.battery();
        final String storage = this.be.storage();
        final String screenTime = this.be.screenTime();
        final String loc = this.be.loc();
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m182lambda$load$4$comaugmaosobriefBriefPage(i, phoneCal, nextAlarm, notifList, battery, storage, screenTime, loc, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$load$4$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m182lambda$load$4$comaugmaosobriefBriefPage(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, boolean z) {
        if (this.dead || i != this.gen) {
            return;
        }
        this.in.cal = J(str);
        JSONObject J = J(str2);
        this.in.alarmAt = J == null ? 0L : J.optLong("at");
        JSONArray jSONArray = null;
        if (str3 != null) {
            try {
                if (!"null".equals(str3) && !str3.isEmpty()) {
                    jSONArray = this.F.filterNotifs(new JSONArray(str3));
                }
            } catch (Exception e) {
            }
        }
        this.in.notifs = jSONArray;
        boolean z2 = true;
        this.in.notifsRead = true;
        JSONObject J2 = J(str4);
        this.in.batt = J2 != null ? J2.optInt("pct", -1) : -1;
        this.in.charging = J2 != null && J2.optBoolean("charging");
        JSONObject J3 = J(str5);
        this.in.free = J3 == null ? 0L : J3.optLong("free");
        this.in.total = J3 != null ? J3.optLong("total") : 0L;
        this.in.screen = J(str6);
        JSONObject J4 = J(str7);
        BriefModel.In in = this.in;
        if (this.in.wx != null || (J4 != null && J4.has("lat"))) {
            z2 = false;
        }
        in.wxNeedLoc = z2;
        done(i);
        weather(z, J4, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$load$6$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m184lambda$load$6$comaugmaosobriefBriefPage(int i, String str) {
        if (this.dead || i != this.gen) {
            return;
        }
        this.in.health = J(str);
        done(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$load$7$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m185lambda$load$7$comaugmaosobriefBriefPage(boolean z, final int i) {
        String dbg = dbg("aos.dbg.health");
        if (dbg == null) {
            dbg = this.be.healthToday(z);
        }
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m184lambda$load$6$comaugmaosobriefBriefPage(i, dbg);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$load$8$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m186lambda$load$8$comaugmaosobriefBriefPage(int i, String str) {
        if (this.dead || i != this.gen) {
            return;
        }
        this.in.week = J(str);
        done(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$load$9$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m187lambda$load$9$comaugmaosobriefBriefPage(final int i) {
        String dbg = dbg("aos.dbg.week");
        if (dbg == null) {
            dbg = this.be.healthWeek();
        }
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m186lambda$load$8$comaugmaosobriefBriefPage(i, dbg);
            }
        });
    }

    void done(int i) {
        rebuild();
        int i2 = this.pending - 1;
        this.pending = i2;
        if (i2 <= 0) {
            this.sc.busy(false);
            if (!this.cfg.speak || this.spoke) {
                return;
            }
            MAIN.removeCallbacks(this.autoSpeak);
            MAIN.postDelayed(this.autoSpeak, 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$10$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m190lambda$new$10$comaugmaosobriefBriefPage() {
        if (!this.resumed || this.spoke || this.speaking || this.preparing) {
            return;
        }
        this.spoke = true;
        read();
    }

    void weather(boolean z, JSONObject jSONObject, final int i) {
        if (jSONObject == null || !jSONObject.has("lat")) {
            return;
        }
        JSONObject obj = Store.obj("aos.wx");
        final String optString = Store.obj("aos.wxcfg").optString("src", "auto");
        if (z || !obj.has("now") || !optString.equals(obj.optString("cfg")) || System.currentTimeMillis() - obj.optLong("at") >= 900000) {
            final String optString2 = jSONObject.optString("place", "");
            this.be.weatherFetch(optString, jSONObject.optDouble("lat"), jSONObject.optDouble("lon"), new Consumer() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(Object obj2) {
                    BriefPage.this.m200lambda$weather$12$comaugmaosobriefBriefPage(optString, optString2, i, (String) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$weather$11$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m199lambda$weather$11$comaugmaosobriefBriefPage(String str, String str2, String str3, int i) {
        JSONObject J;
        if (this.dead || (J = J(str)) == null || !J.has("now")) {
            return;
        }
        try {
            JSONObject put = J.put("at", System.currentTimeMillis()).put("cfg", str2);
            if (str3.isEmpty()) {
                str3 = J.optString("place", "");
            }
            put.put("city", str3);
        } catch (Exception e) {
        }
        Store.set("aos.wx", J.toString());
        if (i != this.gen) {
            return;
        }
        this.in.wx = wx(J);
        this.in.wxNeedLoc = false;
        rebuild();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$weather$12$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m200lambda$weather$12$comaugmaosobriefBriefPage(final String str, final String str2, final int i, final String str3) {
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m199lambda$weather$11$comaugmaosobriefBriefPage(str3, str, str2, i);
            }
        });
    }

    double[] goals() {
        return SettingsModel.goals(Store.home("health.goals"));
    }

    boolean hasKey() {
        JSONObject J = J(this.be.aiHasKey());
        return J != null && J.optBoolean("hasKey");
    }

    String aiLine() {
        if (!this.F.on("greetai")) {
            return "";
        }
        JSONObject obj = Store.obj("aos.aif.greet");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.in.now);
        if (this.F.day().equals(obj.optString("d"))) {
            return (calendar.get(11) < 12 ? "am" : "pm").equals(obj.optString("slot")) ? obj.optString("text", "") : "";
        }
        return "";
    }

    String aiState(String str) {
        return (this.F.paused() || !this.F.on(str)) ? "off" : hasKey() ? "ok" : "nokey";
    }

    static boolean still() {
        PerfModel.Knobs k = Perf.k();
        return k.calm || !k.motion || "LIGHT".equals(Perf.tier());
    }

    void drawDone() {
        this.drawK = 1.0f;
        this.ring.k = 1.0f;
        this.ring.invalidate();
        Iterator<BriefViews.MiniRing> it = this.minis.iterator();
        while (it.hasNext()) {
            BriefViews.MiniRing next = it.next();
            next.k = 1.0f;
            next.invalidate();
        }
    }

    void drawIn() {
        if (still()) {
            drawDone();
            return;
        }
        this.drawK = 0.0f;
        this.ring.k = 0.0f;
        this.ring.setLayerType(0, null);
        this.ring.invalidate();
        this.loop.kick();
    }

    static float ease(float f) {
        float f2 = 1.0f - f;
        return 1.0f - ((f2 * f2) * f2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean frame(long j, float f) {
        if (this.drawK >= 1.0f) {
            return false;
        }
        this.drawK = Math.min(1.0f, this.drawK + (f / 900.0f));
        float ease = ease(this.drawK);
        this.ring.k = ease;
        this.ring.invalidate();
        Iterator<BriefViews.MiniRing> it = this.minis.iterator();
        while (it.hasNext()) {
            BriefViews.MiniRing next = it.next();
            next.k = ease;
            next.invalidate();
        }
        if (this.drawK < 1.0f) {
            return true;
        }
        this.ring.setLayerType(2, null);
        return false;
    }

    void ringNow() {
        long now = this.in.now == 0 ? now() : this.in.now;
        this.ring.now(DayRing.angle(now), DayRing.elapsed(now)[1]);
    }

    void rebuild() {
        this.in.mode = this.mode;
        this.in.now = this.in.now == 0 ? now() : this.in.now;
        this.out = BriefModel.build(this.in, this.tr);
        render();
    }

    void theme() {
        this.th.load(Store.get("aos.theme", "dark"), Store.obj("aos.colors"), Store.obj("aos.ui"), Perf.k().blur, this.dn);
        float f = SettingsModel.ui(Store.get("aos.ui"))[2] / 100.0f;
        Configuration configuration = this.c.getResources().getConfiguration();
        this.k = new Kit(this.c, this.th, UnitsMath.viewZoom(configuration.fontScale) * f * UnitsMath.tabletScale(configuration.smallestScreenWidthDp), UnitsMath.textZoom(configuration.fontScale) * f * UnitsMath.tabletScale(configuration.smallestScreenWidthDp));
    }

    void render() {
        int i;
        float f;
        int i2;
        int i3;
        if (this.out == null || this.dead) {
            return;
        }
        theme();
        final int scrollY = this.sc.getScrollY();
        this.stale = false;
        boolean land = land();
        String str = this.k.light ? "l" : "d";
        if (this.grid == null || this.grid.getTag() != str) {
            if (this.grid != null) {
                this.root.removeView(this.grid);
            }
            Context context = this.c;
            int i4 = this.k.BG;
            if (this.k.light) {
                i = 60;
                f = 0.06f;
                i2 = 20;
                i3 = 40;
            } else {
                i = 200;
                f = 0.055f;
                i2 = 160;
                i3 = 180;
            }
            this.grid = new Kit.Grid(context, i4, Theme.rgba(i2, i3, i, f), this.k.light);
            this.grid.setTag(str);
            this.root.addView(this.grid, 0, new FrameLayout.LayoutParams(-1, -1));
        }
        this.root.setBackground(new Kit.GridBg(this.k.BG, Theme.a(this.k.LINE2, 0.5f), this.dn));
        this.cardN = 0;
        this.hint.setTextColor(this.k.DIM);
        this.body.removeAllViews();
        this.minis.clear();
        Kit.pane(this.body, t("db.a11y", new Object[0]));
        pad();
        this.body.addView(topBar());
        header();
        this.colR = null;
        this.colL = null;
        this.wR = 0.0f;
        this.wL = 0.0f;
        if (land) {
            LinearLayout linearLayout = new LinearLayout(this.c);
            linearLayout.setGravity(48);
            this.colL = vcol();
            this.colR = vcol();
            linearLayout.addView(this.colL, new LinearLayout.LayoutParams(0, -2, 1.0f));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
            layoutParams.leftMargin = px(10.0f);
            linearLayout.addView(this.colR, layoutParams);
            this.body.addView(linearLayout, new LinearLayout.LayoutParams(-1, -2));
        }
        if (this.out.weather.show) {
            weather();
        }
        if (this.out.day.show) {
            day();
        }
        if (this.out.health.show) {
            health();
        }
        if (this.out.notifs.show) {
            notifs();
        }
        if (this.out.phone.show) {
            phone();
        }
        if (this.out.lists.show) {
            lists();
        }
        this.colR = null;
        this.colL = null;
        actions();
        if (scrollY > 0) {
            this.sc.post(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    BriefPage.this.m195lambda$render$13$comaugmaosobriefBriefPage(scrollY);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$render$13$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m195lambda$render$13$comaugmaosobriefBriefPage(int i) {
        this.sc.scrollTo(0, i);
    }

    LinearLayout vcol() {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        return linearLayout;
    }

    View topBar() {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        Kit kit = this.k;
        String t = t("back", new Object[0]);
        Backend backend = this.be;
        Objects.requireNonNull(backend);
        View circle = kit.circle(Kit.LEFT, t, true, 36.0f, new NativeAssist$$ExternalSyntheticLambda58(backend));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px(36.0f), px(36.0f));
        layoutParams.rightMargin = px(10.0f);
        linearLayout.addView(circle, layoutParams);
        LinearLayout seg = this.k.seg(new String[]{t("db.today", new Object[0]), t("db.tonight", new Object[0]) + " + " + t("db.tomorrow", new Object[0])}, new String[]{"today", "tonight"}, this.out.evening ? "tonight" : "today", t("db.mode", new Object[0]), new Consumer() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda32
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BriefPage.this.m198lambda$topBar$14$comaugmaosobriefBriefPage((String) obj);
            }
        });
        seg.setBackground(null);
        seg.setPadding(0, 0, 0, 0);
        for (int i = 0; i < seg.getChildCount(); i++) {
            View childAt = seg.getChildAt(i);
            if (childAt instanceof TextView) {
                if (childAt.getBackground() == null) {
                    childAt.setBackground(this.k.hud(false));
                }
                if (i > 0) {
                    ((LinearLayout.LayoutParams) childAt.getLayoutParams()).leftMargin = px(8.0f);
                }
            }
        }
        linearLayout.addView(seg, new LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout.setPadding(0, 0, 0, px(2.0f));
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$topBar$14$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m198lambda$topBar$14$comaugmaosobriefBriefPage(String str) {
        this.mode = str;
        rebuild();
    }

    LinearLayout card(String str, View view, String str2, String str3, float f) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.setBackground(new Kit.CardBg(this.k.CARD, this.k.LINE, this.k.LINE2, this.dn));
        linearLayout.setPadding(px(14.0f), px(11.0f), px(14.0f), px(12.0f));
        linearLayout.setTag(str2);
        LinearLayout linearLayout2 = new LinearLayout(this.c);
        linearLayout2.setGravity(16);
        Locale locale = Locale.ROOT;
        String str4 = Kit.ADV ? "INDEX: %02d · %s" : "%02d / %s";
        int i = this.cardN + 1;
        this.cardN = i;
        TextView mono = mono(String.format(locale, str4, Integer.valueOf(i), str), 9.5f, this.k.INK);
        Kit.heading(mono);
        linearLayout2.addView(mono, new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (view != null) {
            linearLayout2.addView(view);
        }
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, px(7.0f));
        layoutParams.topMargin = px(3.0f);
        layoutParams.bottomMargin = px(2.0f);
        linearLayout.addView(new BriefViews.ArcRule(this.c, this.k.LINE2, this.k.ACC, this.dn), layoutParams);
        linearLayout.setContentDescription(str + ". " + str3);
        if (Build.VERSION.SDK_INT >= 28) {
            linearLayout.setScreenReaderFocusable(true);
        }
        LinearLayout linearLayout3 = this.body;
        if (this.colL != null) {
            if (this.wL <= this.wR) {
                linearLayout3 = this.colL;
                this.wL += f;
            } else {
                linearLayout3 = this.colR;
                this.wR += f;
            }
        }
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.topMargin = px(10.0f);
        linearLayout3.addView(linearLayout, layoutParams2);
        return linearLayout;
    }

    TextView big(String str) {
        TextView text = this.k.text(str, Fonts.RAJ_400, 20.0f, 0.04f, this.k.INK);
        text.setSingleLine();
        text.setEllipsize(TextUtils.TruncateAt.END);
        text.setPadding(px(8.0f), 0, 0, 0);
        return text;
    }

    void foot(LinearLayout linearLayout) {
        String str = (String) linearLayout.getTag();
        if (str == null || str.isEmpty()) {
            return;
        }
        TextView clamp = this.k.clamp(mono("FROM " + str, 8.0f, this.k.DIM));
        clamp.setAlpha(0.8f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(10.0f);
        linearLayout.addView(clamp, layoutParams);
    }

    TextView tv(String str, Typeface typeface, float f, int i) {
        return this.k.text(str, typeface, f, 0.0f, i);
    }

    TextView mono(String str, float f, int i) {
        return this.k.text(str, Fonts.MONO, f, 0.14f, i);
    }

    LinearLayout.LayoutParams top(int i) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(i);
        return layoutParams;
    }

    static LinearLayout.LayoutParams lpm(LinearLayout.LayoutParams layoutParams, int i) {
        layoutParams.leftMargin = Math.round(i * Resources.getSystem().getDisplayMetrics().density);
        return layoutParams;
    }

    void fixRow(LinearLayout linearLayout, String str, final String str2) {
        TextView btn = this.k.btn(t("db.fix", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m180lambda$fixRow$15$comaugmaosobriefBriefPage(str2);
            }
        });
        btn.setContentDescription(t("db.fix", new Object[0]) + ": " + str);
        LinearLayout hrow = this.k.hrow(Kit.grow(tv(str, Fonts.RAJ_400, 14.0f, this.k.INK)), btn);
        ((LinearLayout.LayoutParams) hrow.getLayoutParams()).topMargin = px(8.0f);
        linearLayout.addView(hrow);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* renamed from: fix, reason: merged with bridge method [inline-methods] */
    public void m180lambda$fixRow$15$comaugmaosobriefBriefPage(String str) {
        char c;
        switch (str.hashCode()) {
            case -1221262756:
                if (str.equals("health")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -178324674:
                if (str.equals("calendar")) {
                    c = 0;
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
                this.be.askCal();
                return;
            case 1:
                this.be.openSettings("health");
                return;
            default:
                this.be.askPerm(str);
                return;
        }
    }

    View metric(String str, float f, String str2, int i) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        BriefViews.MiniRing miniRing = new BriefViews.MiniRing(this.c, i, this.k.LINE2, this.k.INK, this.dn, this.k.lz).set(f, str2);
        miniRing.k = ease(this.drawK);
        this.minis.add(miniRing);
        int round = Math.round(px(50.0f) * Math.max(1.0f, this.k.lz));
        linearLayout.addView(miniRing, new LinearLayout.LayoutParams(round, round));
        TextView fitText = this.k.fitText(str, Fonts.MONO, 8.0f, 0.14f, this.k.DIM);
        fitText.setGravity(17);
        linearLayout.addView(fitText, top(5));
        linearLayout.setImportantForAccessibility(4);
        return linearLayout;
    }

    LinearLayout metrics(View... viewArr) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(48);
        for (View view : viewArr) {
            linearLayout.addView(view, new LinearLayout.LayoutParams(0, -2, 1.0f));
        }
        return linearLayout;
    }

    void header() {
        BriefModel.Out out = this.out;
        LinearLayout card = card(out.evening ? t("db.tonight", new Object[0]) + " · " + t("db.tomorrow", new Object[0]) : t("db.today", new Object[0]), null, out.summarySrc, out.greet + ". " + BriefModel.hm(this.in.now) + ", " + out.date + ". " + out.summary, 0.0f);
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        LinearLayout linearLayout2 = new LinearLayout(this.c);
        linearLayout2.setOrientation(1);
        boolean z = this.k.z > 1.15f;
        if (z) {
            linearLayout.setOrientation(1);
            linearLayout.setGravity(8388611);
        }
        LinearLayout linearLayout3 = new LinearLayout(this.c);
        linearLayout3.setGravity(80);
        if (z) {
            linearLayout3.setOrientation(1);
        }
        if (z) {
            Kit.FitText fitText = new Kit.FitText(this.c, this.k.z * 44.0f * this.dn);
            fitText.setText(BriefModel.hm(this.in.now));
            fitText.setTypeface(Fonts.RAJ_600);
            fitText.setTextColor(this.k.INK);
            this.timeV = fitText;
        } else {
            this.timeV = tv(BriefModel.hm(this.in.now), Fonts.RAJ_600, 44.0f, this.k.INK);
        }
        this.timeV.setIncludeFontPadding(false);
        linearLayout3.addView(this.timeV, new LinearLayout.LayoutParams(z ? -1 : -2, -2));
        TextView mono = mono(out.date, 10.0f, this.k.DIM);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        if (!z) {
            layoutParams.leftMargin = px(10.0f);
            layoutParams.bottomMargin = px(7.0f);
        } else {
            layoutParams.topMargin = px(4.0f);
        }
        linearLayout3.addView(mono, layoutParams);
        linearLayout2.addView(linearLayout3, top(2));
        TextView text = this.k.text(out.greet, Fonts.RAJ_600, 20.0f, 0.06f, this.k.INK);
        text.setMaxLines(2);
        linearLayout2.addView(text, top(2));
        if (!out.summary.isEmpty()) {
            linearLayout2.addView(tv(out.summary, Fonts.RAJ_400, 15.0f, this.k.DIM), top(6));
        }
        if (!out.ringNext.isEmpty() || !out.sleepShort.isEmpty()) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            if (!out.ringNext.isEmpty()) {
                spannableStringBuilder.append((CharSequence) out.ringNext);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(this.k.ACC), 0, spannableStringBuilder.length(), 33);
            }
            if (!out.sleepShort.isEmpty()) {
                if (spannableStringBuilder.length() > 0) {
                    spannableStringBuilder.append((CharSequence) "  ·  ");
                }
                spannableStringBuilder.append((CharSequence) out.sleepShort);
            }
            TextView mono2 = mono("", 8.5f, this.k.DIM);
            mono2.setText(spannableStringBuilder);
            linearLayout2.addView(mono2, top(6));
        }
        linearLayout.addView(linearLayout2, z ? new LinearLayout.LayoutParams(-1, -2) : new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (this.ring.getParent() != null) {
            ((ViewGroup) this.ring.getParent()).removeView(this.ring);
        }
        this.ring.colors(this.k.INK, this.k.ACC, this.k.LINE2, this.k.BG);
        this.ring.set(out.arcs, out.sleepArc, out.dayArc);
        ringNow();
        this.ring.setLayerType(this.drawK >= 1.0f ? 2 : 0, null);
        this.ring.setContentDescription(t("db.ring.a11y", "n", Integer.valueOf(out.arcs.size()), "sleep", (out.sleepArc == null || out.sleep.isEmpty()) ? "" : t("db.h.sleep", new Object[0]) + " " + out.sleep + ".", "at", BriefModel.hm(this.in.now)) + (out.ringNext.isEmpty() ? "" : " " + out.ringNext + "."));
        int round = Math.round(px(92.0f) * Math.max(1.0f, this.k.lz));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(round, round);
        int px = px(10.0f);
        if (z) {
            layoutParams2.topMargin = px;
        } else {
            layoutParams2.leftMargin = px;
        }
        linearLayout.addView(this.ring, layoutParams2);
        card.addView(linearLayout, top(2));
        for (int i = 0; i < linearLayout2.getChildCount(); i++) {
            linearLayout2.getChildAt(i).setImportantForAccessibility(4);
        }
        foot(card);
    }

    void weather() {
        BriefModel.Out out = this.out;
        if (out.weather.fix != null) {
            LinearLayout card = card(t("db.wx", new Object[0]), null, out.weather.src, t("db.wx.fix", new Object[0]), 1.0f);
            fixRow(card, t("db.wx.fix", new Object[0]), out.weather.fix);
            foot(card);
            return;
        }
        LinearLayout card2 = card(t("db.wx", new Object[0]), big(out.wxDesc), out.weather.src, out.wxTemp + " " + out.wxDesc + ". " + out.wxHiLo + ". " + out.wxTmr, 3.0f);
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        linearLayout.addView(new BriefViews.Icon(this.c, "partly".equals(out.wxCode) ? "weather" : out.wxCode, this.th.sc2(out.wxCode)), new LinearLayout.LayoutParams(px(30.0f), px(30.0f)));
        TextView tv = tv(out.wxTemp, Fonts.RAJ_600, 34.0f, this.k.INK);
        tv.setIncludeFontPadding(false);
        linearLayout.addView(tv, lpm(new LinearLayout.LayoutParams(-2, -2), 12));
        LinearLayout linearLayout2 = new LinearLayout(this.c);
        linearLayout2.setOrientation(1);
        linearLayout2.addView(mono(out.wxDesc, 10.0f, this.k.INK));
        if (!out.wxHiLo.isEmpty()) {
            linearLayout2.addView(mono(out.wxHiLo, 9.0f, this.k.DIM), top(4));
        }
        linearLayout.addView(linearLayout2, lpm(new LinearLayout.LayoutParams(0, -2, 1.0f), 14));
        card2.addView(linearLayout, top(6));
        if (!out.hours.isEmpty()) {
            card2.addView(mono(t("db.wx.next", new Object[0]), 8.0f, this.k.DIM), top(12));
            BriefViews.Hours hours = new BriefViews.Hours(this.c, this.th, this.k.DIM, this.k.INK, this.dn, this.k.lz);
            hours.set(out.hours);
            card2.addView(hours, top(6));
        }
        if (!out.wxTmr.isEmpty()) {
            LinearLayout linearLayout3 = new LinearLayout(this.c);
            linearLayout3.setGravity(16);
            linearLayout3.addView(new BriefViews.Icon(this.c, "partly".equals(out.wxTmrCode) ? "weather" : out.wxTmrCode, this.th.sc2(out.wxTmrCode)), new LinearLayout.LayoutParams(px(16.0f), px(16.0f)));
            linearLayout3.addView(mono(out.wxTmr, 9.0f, this.k.INK), lpm(new LinearLayout.LayoutParams(-2, -2), 8));
            card2.addView(linearLayout3, top(10));
        }
        foot(card2);
    }

    void day() {
        String str;
        String str2;
        String t;
        StringBuilder sb;
        BriefModel.Out out = this.out;
        StringBuilder sb2 = new StringBuilder();
        for (BriefModel.Ev ev : out.events) {
            StringBuilder append = sb2.append(ev.allDay ? t("db.allday", new Object[0]) : BriefModel.hm(ev.start)).append(" ").append(ev.title);
            if (ev.now) {
                t = t("db.now", new Object[0]);
                sb = new StringBuilder();
            } else if (ev.next) {
                t = t("db.next", new Object[0]);
                sb = new StringBuilder();
            } else {
                str2 = "";
                append.append(str2).append(". ");
            }
            str2 = sb.append(", ").append(t).toString();
            append.append(str2).append(". ");
        }
        sb2.append(out.alarm.isEmpty() ? t("db.noalarm", new Object[0]) : out.alarm).append(". ").append(out.freeSlots);
        String t2 = out.evening ? t("db.cal.tonight", new Object[0]) + " · " + t("db.cal.tmr", new Object[0]) : t("db.cal.today", new Object[0]);
        if (out.calFix) {
            str = "—";
        } else if (out.events.isEmpty()) {
            str = "CLEAR";
        } else {
            str = out.events.size() + (out.evening ? " AHEAD" : " TODAY");
        }
        LinearLayout card = card(t2, big(str), out.day.src, out.calFix ? t("db.cal.fix", new Object[0]) : sb2.toString(), (out.events.size() * 0.6f) + 1.5f);
        if (out.calFix) {
            fixRow(card, t("db.cal.fix", new Object[0]), "calendar");
        } else {
            Iterator<BriefModel.Ev> it = out.events.iterator();
            int i = -1;
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BriefModel.Ev next = it.next();
                if (out.evening && next.day != i) {
                    card.addView(mono(next.day == 0 ? t("db.cal.tonight", new Object[0]) : t("db.cal.tmr", new Object[0]), 8.0f, this.k.ACC), top(i < 0 ? 6 : 12));
                    i = next.day;
                }
                card.addView(event(next));
            }
            if (out.evening && i < 1) {
                card.addView(mono(t("db.cal.tmr", new Object[0]), 8.0f, this.k.ACC), top(12));
                card.addView(tv(t("db.cal.clear", new Object[0]), Fonts.RAJ_400, 14.0f, this.k.DIM), top(4));
            } else if (out.events.isEmpty()) {
                card.addView(tv(t("db.cal.clear", new Object[0]), Fonts.RAJ_400, 14.0f, this.k.DIM), top(6));
            }
            if (!out.freeSlots.isEmpty()) {
                card.addView(mono(out.freeSlots, 9.0f, this.k.GOOD), top(10));
            }
        }
        LinearLayout row = this.k.row(null, 0, out.alarm.isEmpty() ? t("db.noalarm", new Object[0]) : out.alarm, null, false, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m177lambda$day$16$comaugmaosobriefBriefPage();
            }
        }, new View[0]);
        row.addView(new BriefViews.Icon(this.c, "clock", this.k.DIM), 0, new LinearLayout.LayoutParams(px(16.0f), px(16.0f)));
        ((LinearLayout.LayoutParams) row.getChildAt(1).getLayoutParams()).leftMargin = px(10.0f);
        row.setMinimumHeight(px(44.0f));
        card.addView(row, top(6));
        foot(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$day$16$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m177lambda$day$16$comaugmaosobriefBriefPage() {
        this.be.launchRole("clock");
    }

    View event(final BriefModel.Ev ev) {
        TextView chip;
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        linearLayout.setMinimumHeight(px(44.0f));
        linearLayout.setPadding(0, px(5.0f), 0, px(5.0f));
        TextView mono = mono(ev.allDay ? t("db.allday", new Object[0]) : BriefModel.hm(ev.start), 10.0f, ev.now ? this.k.ALT : this.k.INK);
        mono.setLetterSpacing(0.04f);
        mono.setMinWidth(px(52.0f));
        linearLayout.addView(mono);
        LinearLayout linearLayout2 = new LinearLayout(this.c);
        linearLayout2.setOrientation(1);
        TextView tv = tv(ev.title, Fonts.RAJ_600, 15.0f, this.k.INK);
        tv.setMaxLines(2);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout2.addView(tv);
        if (!ev.where.isEmpty()) {
            linearLayout2.addView(this.k.clamp(mono(ev.where.toUpperCase(Locale.getDefault()), 8.5f, this.k.DIM)), top(3));
        }
        linearLayout.addView(linearLayout2, lpm(new LinearLayout.LayoutParams(0, -2, 1.0f), 8));
        linearLayout.setGravity(48);
        mono.setPadding(0, Math.max(0, (tv.getLineHeight() - mono.getLineHeight()) / 2), 0, 0);
        TextView textView = null;
        if (ev.now || ev.next) {
            chip = this.k.chip(ev.now ? t("db.now", new Object[0]) : (this.out.nextIn <= 0 || this.out.nextIn >= 180) ? t("db.next", new Object[0]) : t("db.next.in", "n", Integer.valueOf(this.out.nextIn)), ev.now ? this.k.ALT : this.k.ACC);
            chip.setImportantForAccessibility(2);
        } else {
            chip = null;
        }
        if (!ev.where.isEmpty()) {
            textView = this.k.btn(t("db.go", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    BriefPage.this.m179lambda$event$17$comaugmaosobriefBriefPage(ev);
                }
            });
            textView.setContentDescription(t("db.go.a11y", "where", ev.where));
        }
        if (chip != null || textView != null) {
            LinearLayout linearLayout3 = new LinearLayout(this.c);
            linearLayout3.setGravity(16);
            if (chip != null) {
                linearLayout3.addView(chip, new LinearLayout.LayoutParams(-2, -2));
            }
            if (textView != null) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                if (chip != null) {
                    layoutParams.leftMargin = px(10.0f);
                }
                linearLayout3.addView(textView, layoutParams);
            }
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.topMargin = px(chip != null ? 4.0f : 6.0f);
            linearLayout2.addView(linearLayout3, layoutParams2);
        }
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$event$17$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m179lambda$event$17$comaugmaosobriefBriefPage(BriefModel.Ev ev) {
        this.be.url("geo:0,0?q=" + Uri.encode(ev.where));
    }

    void health() {
        View view;
        String str;
        String str2;
        String str3;
        String str4;
        BriefModel.Out out = this.out;
        if (out.health.fix != null) {
            LinearLayout card = card(t("db.h", new Object[0]), null, out.health.src, t("db.h.fix", new Object[0]), 1.0f);
            fixRow(card, t("db.h.fix", new Object[0]), "health");
            foot(card);
            return;
        }
        String t = t("w.health.nodata", new Object[0]);
        String steps = out.steps >= 0 ? BriefModel.steps(out.steps) : out.healthNoData.contains("steps") ? t : "—";
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(8388613);
        linearLayout.addView(big(out.sleep.isEmpty() ? steps + " STEPS" : out.sleep));
        if (!out.sleepBed.isEmpty()) {
            linearLayout.addView(mono(t("db.bed", "t", out.sleepBed), 8.0f, this.k.DIM));
        }
        String t2 = t("db.h", new Object[0]);
        String str5 = out.health.src;
        String str6 = "";
        if (out.sleep.isEmpty()) {
            view = linearLayout;
            str = t2;
            str2 = str5;
            str3 = t;
            str4 = "";
        } else {
            view = linearLayout;
            str = t2;
            str2 = str5;
            str3 = t;
            str4 = t("db.h.sleep", new Object[0]) + " " + out.sleep + src("sleep") + " " + out.sleepQ + " " + out.sleepBed + ". ";
        }
        if (!out.ready.isEmpty()) {
            str6 = out.ready + src(out.readyRec ? "recovery" : "readiness") + ". ";
        }
        LinearLayout card2 = card(str, view, str2, str4 + str6 + t("db.h.steps", new Object[0]) + " " + steps + src("steps") + " " + t("db.h.goal", "goal", BriefModel.steps(out.stepsGoal)) + ". " + out.kcal + src("kcal"), 3.5f);
        card2.addView(metrics(metric(t("db.h.sleep", new Object[0]), out.sleepFrac, out.sleepMin >= 0 ? (out.sleepMin / 60) + ":" + String.format(Locale.US, "%02d", Integer.valueOf(out.sleepMin % 60)) : out.healthNoData.contains("sleep") ? str3 : "—", this.k.ACC), metric(t(out.readyRec ? "db.h.recl" : "db.h.readyl", new Object[0]), out.readyN >= 0 ? out.readyN / 100.0f : Float.NaN, out.readyN >= 0 ? new StringBuilder().append(out.readyN).toString() : out.healthNoData.contains("ready") ? str3 : "—", this.k.INK), metric(t("db.h.steps", new Object[0]), out.steps >= 0 ? out.stepsFrac : Float.NaN, steps, this.k.ACC), metric(t("db.h.active", new Object[0]), BriefModel.frac(out.kcalN, out.kcalGoal), out.kcalN >= 0 ? new StringBuilder().append(out.kcalN).toString() : out.healthNoData.contains("kcal") ? str3 : "—", this.k.INK)), top(8));
        if (!out.healthHint.isEmpty()) {
            card2.addView(mono(out.healthHint, 8.5f, this.k.DIM), top(10));
        }
        StringBuilder sb = new StringBuilder();
        if (!out.sleepQ.isEmpty()) {
            sb.append(out.sleepQ);
        }
        if (out.sleepScore >= 0 && out.sleepQ.isEmpty()) {
            sb.append(t("db.h.score", "n", Integer.valueOf(out.sleepScore)));
        }
        if (out.steps >= 0) {
            if (sb.length() > 0) {
                sb.append(" · ");
            }
            sb.append(t("db.h.goal", "goal", BriefModel.steps(out.stepsGoal)));
        }
        if (sb.length() > 0) {
            card2.addView(mono(sb.toString(), 8.5f, this.k.DIM), top(10));
        }
        if (has(out.sleep7) || has(out.steps7)) {
            LinearLayout linearLayout2 = new LinearLayout(this.c);
            linearLayout2.addView(trend(t("db.h.t.sleep", new Object[0]), out.sleep7, false), new LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout2.addView(new View(this.c), new LinearLayout.LayoutParams(px(14.0f), 1));
            linearLayout2.addView(trend(t("db.h.t.steps", new Object[0]), out.steps7, true), new LinearLayout.LayoutParams(0, -2, 1.0f));
            card2.addView(linearLayout2, top(12));
        }
        foot(card2);
    }

    String src(String str) {
        String str2 = this.out.hsrc.get(str);
        return str2 == null ? "" : " · " + str2;
    }

    static boolean has(float[] fArr) {
        for (float f : fArr) {
            if (!Float.isNaN(f)) {
                return true;
            }
        }
        return false;
    }

    View trend(String str, float[] fArr, boolean z) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.addView(mono(str, 8.0f, this.k.DIM));
        if (!has(fArr)) {
            TextView mono = mono(t(z ? "db.h.t.nosteps" : "db.h.t.nosleep", new Object[0]), 9.0f, this.k.DIM);
            mono.setMinHeight(px(32.0f));
            mono.setGravity(16);
            linearLayout.addView(mono, top(5));
            return linearLayout;
        }
        BriefViews.Spark dots = new BriefViews.Spark(this.c, this.k.ACC, this.k.LINE, this.dn).dots(this.k.INK);
        dots.zero = z;
        dots.set(fArr);
        if (z) {
            float f = Float.NaN;
            for (float f2 : fArr) {
                if (!Float.isNaN(f2)) {
                    f = f2;
                }
            }
            if (!Float.isNaN(f)) {
                dots.label(BriefModel.steps(Math.round(f)), this.dn * 8.0f * this.k.lz);
            }
        }
        linearLayout.addView(dots, new LinearLayout.LayoutParams(-1, px(32.0f)));
        ((LinearLayout.LayoutParams) dots.getLayoutParams()).topMargin = px(5.0f);
        return linearLayout;
    }

    void notifs() {
        BriefModel.Out out = this.out;
        if (out.notifs.fix != null) {
            LinearLayout card = card(t("db.n", new Object[0]), null, out.notifs.src, t("db.n.fix", new Object[0]), 1.0f);
            fixRow(card, t("db.n.fix", new Object[0]), "notifAccess");
            foot(card);
            return;
        }
        StringBuilder append = new StringBuilder(t("db.n.count", "n", Integer.valueOf(out.nCount))).append(". ");
        for (String[] strArr : out.byApp) {
            append.append(strArr[0]).append(" ").append(strArr[1]).append(", ");
        }
        LinearLayout card2 = card(t("db.n", new Object[0]), big(t("db.n.count", "n", Integer.valueOf(out.nCount))), out.notifs.src, append.toString(), (out.top.size() * 0.6f) + 2.0f);
        if (!out.byApp.isEmpty()) {
            Flow flow = this.k.flow(10);
            for (String[] strArr2 : out.byApp) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(strArr2[0].toUpperCase(Locale.getDefault()) + " ");
                int length = spannableStringBuilder.length();
                spannableStringBuilder.append((CharSequence) strArr2[1]);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(this.k.ACC), length, spannableStringBuilder.length(), 33);
                TextView mono = mono("", 9.5f, this.k.INK);
                mono.setText(spannableStringBuilder);
                mono.setImportantForAccessibility(2);
                flow.addView(mono);
            }
            card2.addView(flow, top(6));
        }
        if (!out.top.isEmpty()) {
            card2.addView(mono(t("db.n.top", new Object[0]), 8.0f, this.k.DIM), top(12));
            for (final String[] strArr3 : out.top) {
                LinearLayout row = this.k.row(null, 0, strArr3[1].isEmpty() ? strArr3[0] : strArr3[1], strArr3[0].toUpperCase(Locale.getDefault()) + (strArr3[2].isEmpty() ? "" : " · " + strArr3[2]), true, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        BriefPage.this.m192lambda$notifs$18$comaugmaosobriefBriefPage(strArr3);
                    }
                }, new View[0]);
                if ("scam".equals(strArr3[4]) || "important".equals(strArr3[4])) {
                    row.addView(this.k.chip(strArr3[4].toUpperCase(Locale.ROOT), this.k.ALT), 1);
                }
                card2.addView(row);
            }
        }
        TextView btn = this.k.btn(this.nState.equals("busy") ? t("db.n.busy", new Object[0]) : t("db.n.sum", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.summarise();
            }
        });
        this.k.enable(btn, "ok".equals(out.aiNotifs) && !this.nState.equals("busy"));
        LinearLayout hrow = this.k.hrow(btn);
        ((LinearLayout.LayoutParams) hrow.getLayoutParams()).topMargin = px(10.0f);
        if (!"ok".equals(out.aiNotifs)) {
            String t = "nokey".equals(out.aiNotifs) ? t("db.n.nokey", new Object[0]) : t("db.n.off", new Object[0]);
            btn.setContentDescription(t("db.n.sum", new Object[0]) + ", " + t);
            hrow.addView(Kit.grow(mono(t, 8.5f, this.k.DIM)), new LinearLayout.LayoutParams(0, -2, 1.0f));
            ((LinearLayout.LayoutParams) hrow.getChildAt(1).getLayoutParams()).leftMargin = px(10.0f);
        }
        card2.addView(hrow);
        if (!this.nSum.isEmpty()) {
            TextView tv = tv(this.nSum, Fonts.RAJ_400, 14.0f, this.nState.equals("err") ? this.k.ALT : this.k.INK);
            tv.setAccessibilityLiveRegion(1);
            card2.addView(tv, top(8));
        }
        foot(card2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$notifs$18$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m192lambda$notifs$18$comaugmaosobriefBriefPage(String[] strArr) {
        this.be.notifOpen(strArr[3]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void summarise() {
        if (!"ok".equals(aiState("notifs")) || "busy".equals(this.nState) || this.in.notifs == null) {
            return;
        }
        this.nState = "busy";
        this.nSum = "";
        render();
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < Math.min(30, this.in.notifs.length()); i++) {
            JSONObject optJSONObject = this.in.notifs.optJSONObject(i);
            if (optJSONObject != null) {
                jSONArray.put(Tools.o("app", optJSONObject.optString("app"), "title", Features.redactAlways(optJSONObject.optString("title")), "text", Features.redactAlways(optJSONObject.optString("text"))));
            }
        }
        final String str = "Summarise the user's waiting phone notifications in 2 to 4 short lines: what matters first, group the rest. Plain text.\n\n" + Tools.fence("notifications", jSONArray);
        Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m197lambda$summarise$20$comaugmaosobriefBriefPage(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$summarise$19$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m196lambda$summarise$19$comaugmaosobriefBriefPage(String str) {
        if (this.dead) {
            return;
        }
        this.nState = (str == null || str.isEmpty()) ? "err" : "done";
        this.nSum = (str == null || str.isEmpty()) ? t("db.n.err", new Object[0]) : str.trim();
        render();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$summarise$20$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m197lambda$summarise$20$comaugmaosobriefBriefPage(String str) {
        final String ask = ask("notifs", str);
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m196lambda$summarise$19$comaugmaosobriefBriefPage(ask);
            }
        });
    }

    String ask(String str, String str2) {
        try {
            JSONObject put = new JSONObject().put("sys", this.F.sys()).put("feature", str);
            Backend backend = this.be;
            JSONObject jSONObject = new JSONObject();
            if (str2.length() > 12000) {
                str2 = str2.substring(0, 12000);
            }
            JSONObject aiAsk = backend.aiAsk(jSONObject.put("text", str2).put("ctx", put).put("auto", true));
            if (aiAsk != null && aiAsk.has("text")) {
                this.F.bump(Features.tokens(aiAsk));
                return aiAsk.optString("text", "");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    void phone() {
        float f;
        int i;
        long j;
        BriefModel.Out out = this.out;
        String str = out.batt >= 0 ? out.batt + "%" : "—";
        String bytes = out.total > 0 ? BriefModel.bytes(out.free) : "—";
        String dur = out.screenMs >= 0 ? BriefModel.dur(out.screenMs) : "—";
        LinearLayout card = card(t("db.p", new Object[0]), big(str), out.phone.src, t("db.p.batt", new Object[0]) + " " + str + (out.charging ? " " + t("db.p.charging", new Object[0]) : "") + ". " + t("db.p.free", "n", bytes) + ". " + t("db.p.st", new Object[0]) + " " + dur, (out.topApps.size() * 0.4f) + 2.5f);
        int round = out.total > 0 ? (int) Math.round(((out.total - out.free) * 100.0d) / out.total) : -1;
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        TextView tv = tv(dur, Fonts.RAJ_600, 18.0f, this.k.INK);
        tv.setGravity(17);
        linearLayout.addView(tv, new LinearLayout.LayoutParams(-1, -2));
        TextView mono = mono(t("db.p.st", new Object[0]), 8.0f, this.k.DIM);
        mono.setGravity(17);
        mono.setSingleLine();
        linearLayout.addView(mono, top(4));
        linearLayout.setPadding(0, px(12.0f), 0, 0);
        linearLayout.setImportantForAccessibility(4);
        View[] viewArr = new View[3];
        String t = t(out.charging ? "db.p.charging" : "db.p.batt", new Object[0]);
        float f2 = out.batt >= 0 ? out.batt / 100.0f : Float.NaN;
        if (out.charging) {
            i = -10835492;
            f = 100.0f;
        } else {
            if (out.batt >= 0) {
                f = 100.0f;
                if (out.batt <= 20) {
                    i = -2078885;
                }
            } else {
                f = 100.0f;
            }
            i = this.k.ACC;
        }
        viewArr[0] = metric(t, f2, str, i);
        viewArr[1] = metric(t("db.p.storage", new Object[0]), round >= 0 ? round / f : Float.NaN, round >= 0 ? round + "%" : "—", this.k.INK);
        viewArr[2] = linearLayout;
        card.addView(metrics(viewArr), top(8));
        if (out.total > 0) {
            card.addView(mono(t("db.p.free", "n", bytes), 8.5f, this.k.DIM), top(8));
        }
        for (String[] strArr : out.topApps) {
            LinearLayout linearLayout2 = new LinearLayout(this.c);
            linearLayout2.setGravity(16);
            linearLayout2.addView(Kit.grow(tv(strArr[0], Fonts.RAJ_600, 14.0f, this.k.INK)));
            try {
                j = Long.parseLong(strArr[1]);
            } catch (Exception e) {
                j = 0;
            }
            linearLayout2.addView(mono(BriefModel.dur(j), 9.0f, this.k.DIM));
            card.addView(linearLayout2, top(6));
        }
        if (out.stFix) {
            fixRow(card, t("db.p.st.fix", new Object[0]), "usage");
        }
        foot(card);
    }

    void lists() {
        BriefModel.Out out = this.out;
        StringBuilder sb = new StringBuilder();
        for (BriefModel.Ev ev : out.reminders) {
            sb.append(BriefModel.hm(ev.start)).append(" ").append(ev.title).append(". ");
        }
        Iterator<String> it = out.shop.iterator();
        while (it.hasNext()) {
            sb.append(it.next()).append(", ");
        }
        int size = out.reminders.size() + out.shop.size();
        LinearLayout card = card(t("db.l", new Object[0]), big(size == 0 ? "—" : size == 1 ? "1 ITEM" : size + " ITEMS"), out.lists.src, sb.toString(), (size * 0.5f) + 1.0f);
        float f = 15.0f;
        if (!out.reminders.isEmpty()) {
            card.addView(mono(t("db.l.rem", new Object[0]), 8.0f, this.k.ACC), top(6));
            for (BriefModel.Ev ev2 : out.reminders) {
                LinearLayout linearLayout = new LinearLayout(this.c);
                linearLayout.setGravity(16);
                linearLayout.setMinimumHeight(px(36.0f));
                TextView mono = mono(ev2.allDay ? t("db.allday", new Object[0]) : BriefModel.hm(ev2.start), 10.0f, this.k.INK);
                mono.setMinWidth(px(52.0f));
                linearLayout.addView(mono);
                linearLayout.addView(Kit.grow(tv(ev2.title, Fonts.RAJ_600, 15.0f, this.k.INK)));
                card.addView(linearLayout);
            }
        }
        if (!out.shop.isEmpty()) {
            card.addView(mono(t("db.l.shop", new Object[0]), 8.0f, this.k.ACC), top(10));
            for (final String str : out.shop) {
                final boolean contains = this.shopDone.contains(str);
                Typeface typeface = Fonts.RAJ_600;
                Kit kit = this.k;
                TextView tv = tv(str, typeface, f, contains ? kit.DIM : kit.INK);
                if (contains) {
                    tv.setPaintFlags(tv.getPaintFlags() | 16);
                }
                LinearLayout linearLayout2 = new LinearLayout(this.c);
                linearLayout2.setGravity(16);
                linearLayout2.setMinimumHeight(px(44.0f));
                Kit.IconView iconView = this.k.iconView(contains ? Kit.CHECK : null, this.k.ACC, 16.0f);
                Kit kit2 = this.k;
                iconView.setBackground(Kit.pill(0, contains ? kit2.ACC : kit2.LINE2, this.dn, this.dn * 999.0f));
                linearLayout2.addView(iconView);
                linearLayout2.addView(tv, lpm(new LinearLayout.LayoutParams(0, -2, 1.0f), 12));
                linearLayout2.setClickable(true);
                linearLayout2.setFocusable(true);
                linearLayout2.setContentDescription(contains ? t("db.l.done", "item", str) : t("db.l.check", "item", str));
                linearLayout2.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.jarvis.assist.aosui.brief.BriefPage.2
                    @Override // android.view.View.AccessibilityDelegate
                    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                        accessibilityNodeInfo.setClassName("android.widget.CheckBox");
                        accessibilityNodeInfo.setCheckable(true);
                        accessibilityNodeInfo.setChecked(contains);
                    }
                });
                linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda8
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        BriefPage.this.m181lambda$lists$21$comaugmaosobriefBriefPage(str, view);
                    }
                });
                card.addView(linearLayout2);
                f = 15.0f;
            }
        }
        foot(card);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$lists$21$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m181lambda$lists$21$comaugmaosobriefBriefPage(String str, View view) {
        Feedback.fx(view, 15);
        if (!this.shopDone.remove(str)) {
            this.shopDone.add(str);
        }
        this.in.shopDone.clear();
        this.in.shopDone.addAll(this.shopDone);
        render();
    }

    void commitShop() {
        if (this.shopDone.isEmpty()) {
            return;
        }
        List<String> shop = this.F.shop();
        shop.removeAll(this.shopDone);
        this.F.shopSet(shop);
        this.shopDone.clear();
    }

    void actions() {
        final BriefModel.Out out = this.out;
        LinearLayout card = card(t("db.a", new Object[0]), null, "", t("db.a", new Object[0]), 0.0f);
        if (Build.VERSION.SDK_INT >= 28) {
            card.setScreenReaderFocusable(false);
        }
        card.setContentDescription(null);
        Flow flow = this.k.flow(8);
        flow.addView(this.k.btn(this.preparing ? t("db.a.prep", new Object[0]) : this.speaking ? t("db.a.stop", new Object[0]) : t("db.a.read", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.read();
            }
        }));
        flow.addView(this.k.btn(t("db.a.ask", new Object[0]), false, true, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m174lambda$actions$22$comaugmaosobriefBriefPage(out);
            }
        }));
        if (out.evening && out.alarmSuggest != null) {
            flow.addView(this.k.btn(t("db.a.alarm", "at", out.alarmSuggest), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    BriefPage.this.alarmSheet();
                }
            }));
        }
        if (out.evening) {
            flow.addView(this.k.btn(t("db.a.dnd", new Object[0]), true, true, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    BriefPage.this.m175lambda$actions$23$comaugmaosobriefBriefPage();
                }
            }));
        }
        card.addView(flow, top(6));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$actions$22$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m174lambda$actions$22$comaugmaosobriefBriefPage(BriefModel.Out out) {
        this.be.openAsk(out.evening ? t("db.ask.q.eve", new Object[0]) : t("db.ask.q", new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$actions$23$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m175lambda$actions$23$comaugmaosobriefBriefPage() {
        this.be.settings("dnd");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void read() {
        if (this.speaking || this.preparing) {
            this.be.sayStop();
            this.speaking = false;
            this.preparing = false;
            render();
            return;
        }
        final String plain = BriefModel.plain(this.out, this.in, this.tr);
        if ("ok".equals(aiState("brief"))) {
            this.preparing = true;
            render();
            final String str = "Rephrase this phone brief as a short, warm spoken summary for text-to-speech: keep every fact and time, plain sentences, no lists or symbols.\n\n" + Tools.fence("day_brief", plain);
            Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    BriefPage.this.m194lambda$read$25$comaugmaosobriefBriefPage(str, plain);
                }
            });
            return;
        }
        say(plain);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$read$24$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m193lambda$read$24$comaugmaosobriefBriefPage(String str, String str2) {
        if (this.dead || !this.preparing) {
            return;
        }
        this.preparing = false;
        if (str == null || str.trim().isEmpty()) {
            str = str2;
        }
        say(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$read$25$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m194lambda$read$25$comaugmaosobriefBriefPage(String str, final String str2) {
        final String ask = ask("brief", str);
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m193lambda$read$24$comaugmaosobriefBriefPage(ask, str2);
            }
        });
    }

    void say(String str) {
        JSONArray jSONArray = new JSONArray((Collection) Speech.sentences(str));
        if (jSONArray.length() == 0) {
            render();
            return;
        }
        this.speaking = true;
        this.be.speakLocal(jSONArray.toString());
        render();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void alarmSheet() {
        String str = this.out.alarmSuggest;
        if (str == null) {
            return;
        }
        final int minutes = BriefModel.minutes(str);
        Sheet title = new Sheet(this.c, this.th, t("db.a.alarm.t", "at", str)).title(t("db.a.alarm.t", "at", str));
        if (!this.out.firstTmr.isEmpty()) {
            title.para(t("db.a.alarm.ev", "ev", this.out.firstTmr), 14.0f, 0.85f);
        }
        if (this.in.wake != null) {
            title.para(t("db.a.alarm.wake", "at", this.in.wake), 14.0f, 0.85f);
        }
        title.para(t("db.a.alarm.p", new Object[0]), 13.0f, 0.7f);
        title.buttons(new String[]{t("cancel", new Object[0]), t("db.a.alarm.ok", new Object[0])}, new Runnable[]{null, new Runnable() { // from class: com.jarvis.assist.aosui.brief.BriefPage$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                BriefPage.this.m176lambda$alarmSheet$26$comaugmaosobriefBriefPage(minutes);
            }
        }}, true).show(this.root);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$alarmSheet$26$com-augma-oso-brief-BriefPage, reason: not valid java name */
    public /* synthetic */ void m176lambda$alarmSheet$26$comaugmaosobriefBriefPage(int i) {
        this.k.toast(this.root, this.be.setAlarm(i / 60, i % 60, "aOS") ? t("db.toast.alarm", new Object[0]) : t("db.toast.noalarm", new Object[0]));
    }
}
