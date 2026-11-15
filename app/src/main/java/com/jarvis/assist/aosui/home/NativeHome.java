package com.jarvis.assist.aosui.home;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.location.LocationManager;
import android.media.AudioManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.GoogleLink;
import com.jarvis.assist.aosui.ai.Conn;
import com.jarvis.assist.aosui.ai.KeyCheck;
import com.jarvis.assist.aosui.brief.BriefModel;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.AppGridModel;
import com.jarvis.assist.aosui.core.BootSeq;
import com.jarvis.assist.aosui.core.ChargeFx;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.GoogleAuth;
import com.jarvis.assist.aosui.core.HomeBanner;
import com.jarvis.assist.aosui.core.HomePages;
import com.jarvis.assist.aosui.core.HomeSlots;
import com.jarvis.assist.aosui.core.HomeTheme;
import com.jarvis.assist.aosui.core.MemPolicy;
import com.jarvis.assist.aosui.core.Memory;
import com.jarvis.assist.aosui.core.OnboardModel;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Profile;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.UnfoldModel;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.core.UnlockAnim;
import com.jarvis.assist.aosui.core.VaultLink;
import com.jarvis.assist.aosui.home.AoaBoot;
import com.jarvis.assist.aosui.home.AoaPage;
import com.jarvis.assist.aosui.home.AppsDrawer;
import com.jarvis.assist.aosui.home.HomeData;
import com.jarvis.assist.aosui.home.HomeView;
import com.jarvis.assist.aosui.home.NativeHome;
import com.jarvis.assist.aosui.home.PageView;
import com.jarvis.assist.aosui.home.SetHomeBanner;
import com.jarvis.assist.aosui.home.Unfold;
import com.jarvis.assist.aosui.home.WidgetPager;
import com.jarvis.assist.aosui.onboard.Boot;
import com.jarvis.assist.aosui.onboard.Welcome;
import com.jarvis.assist.aosui.ui.Sheet;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class NativeHome {
    static final Handler MAIN = new Handler(Looper.getMainLooper());
    static WeakReference<NativeHome> live = new WeakReference<>(null);
    final AutoRun auto;
    SetHomeBanner banner;
    final Backend be;
    View boot;
    final HomeData d;
    boolean dead;
    AppsDrawer drawer;
    final HomeCtx h;
    long mAt;
    int mDrawn;
    TextView meterV;
    final Runnable mirror;
    long presentAt;
    PageView pv;
    boolean resumed;
    final FrameLayout root;
    boolean torchOn;
    final HomeView view;
    Welcome wel;
    final ExecutorService io = Memory.IO;
    final Runnable ticker = new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome.1
        @Override // java.lang.Runnable
        public void run() {
            if (NativeHome.this.d.tick()) {
                NativeHome.this.d.battery();
                NativeHome.this.d.calendar();
                NativeHome.this.d.brief();
                NativeHome.this.d.extras();
                NativeHome.this.d.dots(NativeHome.this.h.th.RED, NativeHome.this.h.th.BLUE, NativeHome.this.h.th.DIM);
                NativeHome.this.view.redraw();
            }
            NativeHome.this.auto.tick(NativeHome.this.d, NativeHome.this.resumed && !NativeHome.this.covered());
            NativeHome.MAIN.postDelayed(this, Conn.STALE_MS);
        }
    };
    final Runnable storeChanged = new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            NativeHome.this.onStore();
        }
    };
    final Runnable perfChanged = new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda9
        @Override // java.lang.Runnable
        public final void run() {
            NativeHome.this.onPerf();
        }
    };
    final Runnable meterTick = new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome.2
        @Override // java.lang.Runnable
        public void run() {
            if (NativeHome.this.meterV == null) {
                return;
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            int i = FrameClock.I.drawn;
            float max = NativeHome.this.mAt == 0 ? 0.0f : ((i - NativeHome.this.mDrawn) * 1000.0f) / ((float) Math.max(1L, uptimeMillis - NativeHome.this.mAt));
            NativeHome.this.mDrawn = i;
            NativeHome.this.mAt = uptimeMillis;
            NativeHome.this.meterV.setText(String.format(Locale.US, "%d FPS · %.1f MS · %s", Integer.valueOf(Math.round(max)), Float.valueOf(FrameClock.I.jsMs), Perf.tier().substring(0, 1)));
            NativeHome.MAIN.postDelayed(this, 1000L);
        }
    };
    final Unfold.Host unfoldHost = new Unfold.Host() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda10
        @Override // com.jarvis.assist.aosui.home.Unfold.Host
        public final void unfold(String str) {
            NativeHome.this.m283lambda$new$4$comaugmaosohomeNativeHome(str);
        }
    };
    HomePages.Cfg pagesCfg = new HomePages.Cfg();
    final View.OnLayoutChangeListener bannerPush = new View.OnLayoutChangeListener() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda11
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            NativeHome.this.onBannerLayout(view, i, i2, i3, i4, i5, i6, i7, i8);
        }
    };
    final BroadcastReceiver present = new BroadcastReceiver() { // from class: com.jarvis.assist.aosui.home.NativeHome.11
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NativeHome.this.presentAt = SystemClock.uptimeMillis();
            if (NativeHome.this.resumed) {
                NativeHome.MAIN.post(new NativeHome$$ExternalSyntheticLambda23(NativeHome.this));
            }
        }
    };
    final Runnable autoKick = new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda12
        @Override // java.lang.Runnable
        public final void run() {
            NativeHome.this.autoKick();
        }
    };
    final Runnable notifNow = new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda13
        @Override // java.lang.Runnable
        public final void run() {
            NativeHome.this.onNotifNow();
        }
    };
    final Runnable mediaNow = new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda14
        @Override // java.lang.Runnable
        public final void run() {
            NativeHome.this.onMediaNow();
        }
    };
    final Memory.Trim trim = new Memory.Trim() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda15
        @Override // com.jarvis.assist.aosui.core.Memory.Trim
        public final void trim(int i, int i2) {
            NativeHome.this.trim(i, i2);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPerf() {
        theme();
        this.view.relayout();
        meter();
    }

    void meter() {
        boolean z = Perf.state().optInt("meter", 0) != 0 && this.resumed;
        if (z && this.meterV == null) {
            this.meterV = new TextView(this.be.ctx());
            this.meterV.setTypeface(Fonts.MONO);
            this.meterV.setTextSize(1, 9.0f);
            this.meterV.setTextColor(-1774607);
            this.meterV.setBackgroundColor(-1728053248);
            float f = this.be.ctx().getResources().getDisplayMetrics().density;
            float f2 = 6.0f * f;
            float f3 = 3.0f * f;
            this.meterV.setPadding(Math.round(f2), Math.round(f3), Math.round(f2), Math.round(f3));
            this.meterV.setImportantForAccessibility(2);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2, 8388659);
            layoutParams.setMargins(Math.round(8.0f * f), Math.round(f * 28.0f), 0, 0);
            this.root.addView(this.meterV, layoutParams);
            this.mAt = 0L;
            MAIN.post(this.meterTick);
            return;
        }
        if (z || this.meterV == null) {
            return;
        }
        MAIN.removeCallbacks(this.meterTick);
        this.root.removeView(this.meterV);
        this.meterV = null;
    }

    public NativeHome(final Backend backend) {
        this.be = backend;
        Objects.requireNonNull(backend);
        this.mirror = new NativeHome$$ExternalSyntheticLambda16(backend);
        Context ctx = backend.ctx();
        Memory.init(ctx);
        Memory.on(this.trim);
        Store.init(ctx);
        Fonts.init(ctx);
        Strings.init(ctx);
        Perf.init(ctx, backend.deviceInfo());
        Perf.onMirror(this.mirror);
        backend.applyPerf();
        this.d = new HomeData(backend);
        this.h = new HomeCtx(this.d);
        this.auto = new AutoRun(backend, this.io);
        live = new WeakReference<>(this);
        theme();
        refresh();
        this.view = new HomeView(ctx, this.h, new HomeView.Host() { // from class: com.jarvis.assist.aosui.home.NativeHome.3
            @Override // com.jarvis.assist.aosui.home.HomeView.Host
            public void go(String str) {
                NativeHome.this.go(str);
            }

            @Override // com.jarvis.assist.aosui.home.HomeView.Host
            public void longOrb() {
                Feedback.fx(NativeHome.this.view, 3);
                backend.aiWarm();
                if (NativeHome.this.d.handoff) {
                    backend.openAssistant("", true);
                } else {
                    backend.openTalk();
                }
            }

            @Override // com.jarvis.assist.aosui.home.HomeView.Host
            public void swipe(boolean z) {
                if (z) {
                    NativeHome.this.drawer(true);
                } else {
                    backend.expandNotifs();
                }
            }

            @Override // com.jarvis.assist.aosui.home.HomeView.Host
            public void doubleTap() {
                if (NativeHome.this.d.cfg().optBoolean("dbl", true)) {
                    Feedback.fx(NativeHome.this.view, 14);
                    NativeHome.this.lockNow();
                }
            }

            @Override // com.jarvis.assist.aosui.home.HomeView.Host
            public void page(int i) {
            }

            @Override // com.jarvis.assist.aosui.home.HomeView.Host
            public void longSlot(String str) {
                backend.openSettings(HomeSlots.route(str));
            }

            @Override // com.jarvis.assist.aosui.home.HomeView.Host
            public void pageSwipe(int i) {
                NativeHome.this.showPage(HomePages.next(NativeHome.this.h.page, i, NativeHome.this.h.pages));
            }
        });
        this.view.pager.awViews = new WidgetPager.AwViews() { // from class: com.jarvis.assist.aosui.home.NativeHome.4
            @Override // com.jarvis.assist.aosui.home.WidgetPager.AwViews
            public void drop(int i) {
                backend.dropAppWidgetView(i);
            }

            @Override // com.jarvis.assist.aosui.home.WidgetPager.AwViews
            public void shown(int i) {
                backend.appWidgetShown(i);
            }

            @Override // com.jarvis.assist.aosui.home.WidgetPager.AwViews
            public View view(int i) {
                return backend.appWidgetView(i);
            }
        };
        this.view.pager.setWidgets(this.d.widgetOrder());
        this.root = new FrameLayout(ctx) { // from class: com.jarvis.assist.aosui.home.NativeHome.5
            @Override // android.view.ViewGroup, android.view.View
            public void addChildrenForAccessibility(ArrayList<View> arrayList) {
                super.addChildrenForAccessibility(arrayList);
                if (NativeHome.this.covered()) {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        View view = arrayList.get(size);
                        while (view != null && view != NativeHome.this.view) {
                            view = view.getParent() instanceof View ? (View) view.getParent() : null;
                        }
                        if (view == NativeHome.this.view) {
                            arrayList.remove(size);
                        }
                    }
                }
            }
        };
        this.root.addView(this.view, new FrameLayout.LayoutParams(-1, -1));
        Store.on(this.storeChanged);
        Perf.on(this.perfChanged);
        Store.migrateOnce(ctx, new IntConsumer() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda17
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                NativeHome.this.m281lambda$new$0$comaugmaosohomeNativeHome(i);
            }
        });
        welcome();
        String mode = Boot.mode(bootOn() ? "skip".equals(Perf.k().boot) ? "short" : Perf.k().boot : "skip", Welcome.animOff(ctx));
        if (mode != null) {
            playBoot("short".equals(mode), false);
            Unfold.M.played(SystemClock.uptimeMillis());
        }
        try {
            ctx.registerReceiver(this.present, new IntentFilter("android.intent.action.USER_PRESENT"));
        } catch (Exception e) {
        }
        Unfold.attach(ctx, this.unfoldHost);
        Unfold.config(ctx.getResources().getConfiguration().smallestScreenWidthDp);
        pagesLoad();
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NativeHome.this.m282lambda$new$1$comaugmaosohomeNativeHome();
            }
        });
        BriefModel.Cfg cfg = BriefModel.cfg(Store.get("aos.brief"));
        String[] migrate = Profile.migrate(Store.get(Profile.KEY, ""), cfg.name);
        if (migrate[0] != null) {
            Store.set(Profile.KEY, migrate[0]);
            cfg.name = "";
            Store.set("aos.brief", BriefModel.save(cfg));
        }
        health();
        this.d.weather(false, new NativeHome$$ExternalSyntheticLambda8(this));
        Feedback.warm();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m281lambda$new$0$comaugmaosohomeNativeHome(int i) {
        Log.i("aOS", "native store: migrated " + i + " keys from the pre-native WebView");
        if (i >= 0) {
            onStore();
        }
        welcome();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m282lambda$new$1$comaugmaosohomeNativeHome() {
        showPage(this.pagesCfg.def);
    }

    public View view() {
        return this.root;
    }

    void theme() {
        Configuration configuration = this.be.ctx().getResources().getConfiguration();
        this.h.zoom = UnitsMath.textZoom(configuration.fontScale);
        this.h.adv = Advanced.on(Store.get(Advanced.KEY, ""));
        this.h.aoa = HomeTheme.aoa(Store.get(Advanced.KEY, ""));
        this.h.th.load(Store.get("aos.theme", "dark"), Store.obj("aos.colors"), Store.obj("aos.ui"), Perf.k().blur, this.be.ctx().getResources().getDisplayMetrics().density);
    }

    void refresh() {
        this.d.applyCfg();
        this.d.tick();
        this.d.battery();
        this.d.calendar();
        this.d.recents();
        this.d.name();
        this.d.brief();
        Arrays.fill(this.d.qsOn, false);
        int i = -1;
        for (int i2 = 0; i2 < this.d.qs.size(); i2++) {
            if ("wifi".equals(this.d.qs.get(i2).id)) {
                i = i2;
            }
        }
        if (i >= 0) {
            this.d.qsOn[i] = "WIFI".equals(this.be.net());
        }
        if (this.torchOn) {
            for (int i3 = 0; i3 < this.d.qs.size(); i3++) {
                if ("torch".equals(this.d.qs.get(i3).id)) {
                    this.d.qsOn[i3] = true;
                }
            }
        }
        for (int i4 = 0; i4 < this.d.qs2.size(); i4++) {
            this.d.qs2On[i4] = ctlOn(this.d.qs2.get(i4).id);
        }
        this.d.extras();
        this.d.dots(this.h.th.RED, this.h.th.BLUE, this.h.th.DIM);
    }

    void health() {
        this.io.execute(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                NativeHome.this.m279lambda$health$3$comaugmaosohomeNativeHome();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$health$2$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m278lambda$health$2$comaugmaosohomeNativeHome(String str) {
        if (this.dead) {
            return;
        }
        this.d.applyHealth(str);
        this.d.brief();
        redraw();
        if (this.boot instanceof Boot) {
            ((Boot) this.boot).mark("health");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$health$3$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m279lambda$health$3$comaugmaosohomeNativeHome() {
        final String healthToday = this.be.healthToday(false);
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NativeHome.this.m278lambda$health$2$comaugmaosohomeNativeHome(healthToday);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void weatherDone() {
        redraw();
        if (this.boot instanceof Boot) {
            ((Boot) this.boot).mark("weather");
        }
    }

    boolean bootOn() {
        return BootSeq.bootOn(Store.get(BootSeq.KEY, ""), Perf.state().optInt("boot", 1) != 0, Perf.k().calm, Perf.tier());
    }

    void playBoot(boolean z, boolean z2) {
        if (this.boot != null) {
            return;
        }
        Context ctx = this.be.ctx();
        if (this.h.aoa) {
            this.boot = new AoaBoot(ctx, z, z2, new AoaBoot.Host() { // from class: com.jarvis.assist.aosui.home.NativeHome.6
                @Override // com.jarvis.assist.aosui.home.AoaBoot.Host
                public float[] orbTarget() {
                    if (!NativeHome.this.h.g.aoa() || NativeHome.this.view.orb.getWidth() == 0) {
                        return null;
                    }
                    return new float[]{NativeHome.this.view.orb.getLeft() + (NativeHome.this.view.orb.getWidth() / 2.0f), NativeHome.this.view.orb.getTop() + (NativeHome.this.view.orb.getHeight() / 2.0f), NativeHome.this.view.orb.getWidth() / 2.0f};
                }

                @Override // com.jarvis.assist.aosui.home.AoaBoot.Host
                public void leave() {
                    if (NativeHome.this.wel == null && NativeHome.this.drawer == null) {
                        NativeHome.this.view.unlockAnim();
                    }
                }

                @Override // com.jarvis.assist.aosui.home.AoaBoot.Host
                public void gone() {
                    NativeHome.this.boot = null;
                }
            });
            this.root.addView(this.boot, new FrameLayout.LayoutParams(-1, -1));
        } else {
            this.boot = new Boot(ctx, this.h.th, this.d.name, z, z2, new Boot.Host() { // from class: com.jarvis.assist.aosui.home.NativeHome.7
                @Override // com.jarvis.assist.aosui.onboard.Boot.Host
                public float[] orbTarget() {
                    if (NativeHome.this.wel != null || NativeHome.this.drawer != null || !NativeHome.this.h.g.orb || NativeHome.this.view.orb.getVisibility() != 0 || NativeHome.this.view.orb.getWidth() == 0) {
                        return null;
                    }
                    return new float[]{NativeHome.this.view.orb.getLeft() + (NativeHome.this.view.orb.getWidth() / 2.0f), NativeHome.this.view.orb.getTop() + (NativeHome.this.view.orb.getHeight() / 2.0f), NativeHome.this.view.orb.getWidth() * 0.32f};
                }

                @Override // com.jarvis.assist.aosui.onboard.Boot.Host
                public void gone() {
                    NativeHome.this.boot = null;
                }
            });
            this.root.addView(this.boot, new FrameLayout.LayoutParams(-1, -1));
            ((Boot) this.boot).mark("fonts");
            ((Boot) this.boot).mark("data");
        }
    }

    boolean unfoldOk() {
        return bootOn() && UnfoldModel.on(Store.get(UnfoldModel.KEY, "")) && !Welcome.animOff(this.be.ctx()) && this.wel == null && this.boot == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m283lambda$new$4$comaugmaosohomeNativeHome(String str) {
        if (this.dead || !unfoldOk()) {
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        if (!this.resumed) {
            Unfold.M.pend(uptimeMillis);
            return;
        }
        if (Unfold.M.allow(uptimeMillis)) {
            Log.i("aOS", "boot on unfold (" + str + ")");
            Sheet.closeTop();
            if (this.drawer != null) {
                drawer(false);
            }
            playBoot(false, true);
        }
    }

    boolean covered() {
        return (this.drawer == null && this.wel == null) ? false : true;
    }

    void welcome() {
        if (this.wel != null || !Store.migrated() || !Store.get("aos.onboarded", "").isEmpty()) {
            return;
        }
        Sheet.closeTop();
        if (this.drawer != null) {
            drawer(false);
        }
        bannerGone();
        this.wel = new Welcome(this.be, new Welcome.Host() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda0
            @Override // com.jarvis.assist.aosui.onboard.Welcome.Host
            public final void done() {
                NativeHome.this.m287lambda$welcome$5$comaugmaosohomeNativeHome();
            }
        });
        this.view.setVisibility(4);
        this.view.dropLayers();
        this.view.setImportantForAccessibility(4);
        int indexOfChild = this.boot != null ? this.root.indexOfChild(this.boot) : -1;
        FrameLayout frameLayout = this.root;
        Welcome welcome = this.wel;
        if (indexOfChild < 0) {
            indexOfChild = this.root.getChildCount();
        }
        frameLayout.addView(welcome, indexOfChild, new FrameLayout.LayoutParams(-1, -1));
        FrameClock.I.setActive(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$welcome$5$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m287lambda$welcome$5$comaugmaosohomeNativeHome() {
        this.wel = null;
        MAIN.post(new NativeHome$$ExternalSyntheticLambda4(this));
        boolean z = false;
        this.view.setImportantForAccessibility(0);
        this.view.setVisibility(0);
        this.view.restoreLayers();
        FrameClock frameClock = FrameClock.I;
        if (!covered() && this.resumed) {
            z = true;
        }
        frameClock.setActive(z);
        onStore();
    }

    void redraw() {
        if (this.view != null) {
            this.view.redraw();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStore() {
        theme();
        refresh();
        this.view.pager.setWidgets(this.d.widgetOrder());
        this.view.relayout();
        pagesLoad();
        if (this.h.page > 0) {
            showPage(this.h.page);
        }
    }

    void go(String str) {
        JSONObject optJSONObject;
        String aoaPage;
        if (!str.equals("apps")) {
            Feedback.fx(this.view, 0);
        }
        if (this.h.aoa && (aoaPage = aoaPage(str)) != null) {
            aoaHooks();
            r2 = !str.startsWith("x:page:") || this.view.tapped;
            AoaPage.show(this.root, this.be, this.h, this.d, aoaPage, r2 ? this.view.x0 : -1.0f, r2 ? this.view.y0 : -1.0f, 0);
            return;
        }
        if (str.startsWith("x:")) {
            xTap(str.substring(2));
            return;
        }
        if (str.startsWith("tab:")) {
            Store.set("aos.land", str.substring(4));
            this.view.relayout();
            return;
        }
        char charAt = str.charAt(0);
        if (str.equals("apps")) {
            drawer(true);
            return;
        }
        if (str.equals("ai")) {
            this.be.openApp("Assist");
            return;
        }
        if (str.equals("brief")) {
            this.be.openApp("Brief");
            return;
        }
        if (str.equals("mplay") || str.equals("mprev") || str.equals("mnext")) {
            if (this.d.media != null && this.d.media.optBoolean("playing")) {
                r2 = true;
            }
            this.be.mediaAct(str.equals("mplay") ? r2 ? "pause" : "play" : str.equals("mprev") ? "prev" : "next", "");
            MAIN.postDelayed(this.mediaNow, 400L);
            return;
        }
        try {
            int parseInt = Integer.parseInt(str.substring(1));
            if (charAt != 'w' || parseInt >= this.d.wheel.size()) {
                if (charAt != 'q' || parseInt >= this.d.qs.size()) {
                    if (charAt != 'k' || parseInt >= this.d.qs2.size()) {
                        if (charAt != 'd' || parseInt >= this.d.dock.size()) {
                            if (charAt == 'g') {
                                List<String> parse = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
                                if (parseInt < parse.size()) {
                                    String str2 = parse.get(parseInt);
                                    int indexOf = str2.indexOf(47);
                                    int i = indexOf + 1;
                                    this.d.remember(str2.substring(0, indexOf), str2.substring(i), "");
                                    this.be.launch(str2.substring(0, indexOf), str2.substring(i));
                                    return;
                                }
                                return;
                            }
                            if (charAt != 'r' || parseInt >= this.d.recent.size()) {
                                if (charAt != 'n' || this.d.notifs == null || parseInt >= this.d.notifs.length() || (optJSONObject = this.d.notifs.optJSONObject(parseInt)) == null) {
                                    return;
                                }
                                this.be.notifOpen(optJSONObject.optString("key"));
                                MAIN.postDelayed(this.notifNow, 600L);
                                return;
                            }
                            HomeData.App app = this.d.recent.get(parseInt);
                            this.d.remember(app.pkg, app.cls, app.label);
                            this.be.launch(app.pkg, app.cls);
                            return;
                        }
                        openEntry("dock", this.d.dock.get(parseInt).id);
                        return;
                    }
                    openEntry("qs", this.d.qs2.get(parseInt).id);
                    return;
                }
                openEntry("qs", this.d.qs.get(parseInt).id);
                return;
            }
            openEntry("wheel", this.d.wheel.get(parseInt).id);
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.jarvis.assist.aosui.home.NativeHome$8, reason: invalid class name */
    /* loaded from: /tmp/augmaos/classes.dex */
    public class AnonymousClass8 implements AoaPage.Hooks {
        AnonymousClass8() {
        }

        @Override // com.jarvis.assist.aosui.home.AoaPage.Hooks
        public float[] orb() {
            if (!NativeHome.this.h.g.aoa() || NativeHome.this.view.orb.getWidth() == 0) {
                return null;
            }
            return new float[]{NativeHome.this.view.orb.getLeft() + (NativeHome.this.view.orb.getWidth() / 2.0f), NativeHome.this.view.orb.getTop() + (NativeHome.this.view.orb.getHeight() / 2.0f)};
        }

        @Override // com.jarvis.assist.aosui.home.AoaPage.Hooks
        public void contract() {
            if (NativeHome.this.view.aoaL.getVisibility() == 0) {
                NativeHome.this.view.aoaL.playContract("LIGHT".equals(Perf.tier()));
            }
            NativeHome.this.view.orb.animate().scaleX(1.18f).scaleY(1.18f).setDuration(140L).withEndAction(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$8$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NativeHome.AnonymousClass8.this.m292lambda$contract$0$comaugmaosohomeNativeHome$8();
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$contract$0$com-augma-oso-home-NativeHome$8, reason: not valid java name */
        public /* synthetic */ void m292lambda$contract$0$comaugmaosohomeNativeHome$8() {
            NativeHome.this.view.orb.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200L).start();
        }

        @Override // com.jarvis.assist.aosui.home.AoaPage.Hooks
        public void expand() {
            if (NativeHome.this.view.aoaL.getVisibility() == 0) {
                NativeHome.this.view.aoaL.playUnlock("LIGHT".equals(Perf.tier()), 1.8f);
            }
        }
    }

    void aoaHooks() {
        AoaPage.hooks = new AnonymousClass8();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x007b, code lost:
    
        if (r7.equals("music") != false) goto L61;
     */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00ea A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00eb A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00ee A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00ef A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00f0 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00e6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    java.lang.String aoaPage(java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.NativeHome.aoaPage(java.lang.String):java.lang.String");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    boolean ctlOn(String str) {
        char c;
        Context ctx = this.be.ctx();
        try {
            switch (str.hashCode()) {
                case -677011630:
                    if (str.equals("airplane")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 99610:
                    if (str.equals("dnd")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 108971:
                    if (str.equals("nfc")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 3075958:
                    if (str.equals("dark")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 3649301:
                    if (str.equals("wifi")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 109211285:
                    if (str.equals("saver")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 110547964:
                    if (str.equals("torch")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 1901043637:
                    if (str.equals("location")) {
                        c = 5;
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
                    return "WIFI".equals(this.be.net());
                case 1:
                    return this.torchOn;
                case 2:
                    return Settings.Global.getInt(ctx.getContentResolver(), "airplane_mode_on", 0) != 0;
                case 3:
                    PowerManager powerManager = (PowerManager) ctx.getSystemService(PowerManager.class);
                    return powerManager != null && powerManager.isPowerSaveMode();
                case 4:
                    return (ctx.getResources().getConfiguration().uiMode & 48) == 32;
                case 5:
                    LocationManager locationManager = (LocationManager) ctx.getSystemService(LocationManager.class);
                    return locationManager != null && Build.VERSION.SDK_INT >= 28 && locationManager.isLocationEnabled();
                case 6:
                    NfcManager nfcManager = (NfcManager) ctx.getSystemService(NfcManager.class);
                    return (nfcManager == null || nfcManager.getDefaultAdapter() == null || !nfcManager.getDefaultAdapter().isEnabled()) ? false : true;
                case 7:
                    NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NotificationManager.class);
                    return notificationManager != null && notificationManager.getCurrentInterruptionFilter() > 1;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$openEntry$6$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m286lambda$openEntry$6$comaugmaosohomeNativeHome() {
        if (this.drawer != null) {
            this.drawer.keyboard(true);
        }
    }

    void openEntry(String str, String str2) {
        Backend backend;
        String str3;
        int i = -1;
        if (str2.startsWith("aos:")) {
            switch (str2.hashCode()) {
                case -820590716:
                    if (str2.equals("aos:ask")) {
                        i = 0;
                        break;
                    }
                    break;
                case 595254776:
                    if (str2.equals("aos:settings")) {
                        i = 4;
                        break;
                    }
                    break;
                case 1238038717:
                    if (str2.equals("aos:search")) {
                        i = 1;
                        break;
                    }
                    break;
                case 1687199429:
                    if (str2.equals("aos:brief")) {
                        i = 2;
                        break;
                    }
                    break;
                case 1705175165:
                    if (str2.equals("aos:vault")) {
                        i = 3;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    this.be.openApp("Assist");
                    return;
                case 1:
                    drawer(true);
                    MAIN.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            NativeHome.this.m286lambda$openEntry$6$comaugmaosohomeNativeHome();
                        }
                    }, 300L);
                    return;
                case 2:
                    backend = this.be;
                    str3 = "Brief";
                    break;
                case 3:
                    this.be.openApp(VaultLink.APP);
                    return;
                case 4:
                    backend = this.be;
                    str3 = "Settings";
                    break;
                default:
                    drawer(true);
                    return;
            }
            backend.openApp(str3);
            return;
        }
        HashMap<String, String[]> hashMap = "qs".equals(str) ? HomeData.QS : "wheel".equals(str) ? HomeData.WHEEL : HomeData.DOCK;
        if (!str2.startsWith("app:") && !hashMap.containsKey(str2)) {
            if (HomeData.QS.containsKey(str2)) {
                str = "qs";
            } else {
                str = HomeData.WHEEL.containsKey(str2) ? "wheel" : "dock";
            }
        }
        if (str2.startsWith("app:")) {
            String[] split = str2.substring(4).split("\\|")[0].split("/");
            String substring = str2.contains("|") ? str2.substring(str2.indexOf(124) + 1) : "";
            if (split.length > 1) {
                this.d.remember(split[0], split[1], substring);
                this.be.launch(split[0], split[1]);
                return;
            }
            return;
        }
        if (this.d.calNeed && "calendar".equals(str2)) {
            this.be.askCal();
            return;
        }
        if ("wheel".equals(str)) {
            String[] strArr = HomeData.WHEEL.get(str2);
            if (strArr == null) {
                return;
            }
            if (!"drawer".equals(strArr[3])) {
                if (strArr[3].startsWith("app:")) {
                    this.be.openApp(strArr[3].substring(4));
                    return;
                } else {
                    this.be.launchRole(strArr[3]);
                    return;
                }
            }
            drawer(true);
            return;
        }
        if ("dock".equals(str)) {
            if ("vault".equals(str2)) {
                this.be.openApp(VaultLink.APP);
                return;
            } else {
                this.be.launchRole(str2);
                return;
            }
        }
        String[] strArr2 = HomeData.QS.get(str2);
        if (strArr2 == null) {
            return;
        }
        if ("lock".equals(strArr2[2])) {
            lockNow();
            return;
        }
        if (!"torch".equals(strArr2[2])) {
            this.be.settings(strArr2[2]);
            return;
        }
        for (int i2 = 0; i2 < this.d.qs.size(); i2++) {
            if (str2.equals(this.d.qs.get(i2).id)) {
                i = i2;
            }
        }
        this.torchOn = this.be.torch();
        if (i >= 0) {
            this.d.qsOn[i] = this.torchOn;
        }
        for (int i3 = 0; i3 < this.d.qs2.size(); i3++) {
            if (str2.equals(this.d.qs2.get(i3).id)) {
                this.d.qs2On[i3] = this.torchOn;
            }
        }
        this.view.back.invalidate();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    void xTap(String str) {
        char c;
        if (str.matches("p\\d+")) {
            JSONArray optJSONArray = this.d.starred == null ? null : this.d.starred.optJSONArray("people");
            JSONObject optJSONObject = optJSONArray != null ? optJSONArray.optJSONObject(Integer.parseInt(str.substring(1))) : null;
            if (optJSONObject != null) {
                PersonSheet.show(this.root, this.h.th, this.be, optJSONObject.optString("name"), optJSONObject.optString("num"));
                return;
            }
            return;
        }
        char c2 = 65535;
        switch (str.hashCode()) {
            case -1884274053:
                if (str.equals("storage")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1331913276:
                if (str.equals("digest")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1240244679:
                if (str.equals("google")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -1221262756:
                if (str.equals("health")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -906336856:
                if (str.equals("search")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -793375513:
                if (str.equals(AppGridModel.ID)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -567451565:
                if (str.equals("contacts")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -331239923:
                if (str.equals("battery")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 3552428:
                if (str.equals("talk")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 94755854:
                if (str.equals("clock")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 111574433:
                if (str.equals("usage")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 952290484:
                if (str.equals("notifAccess")) {
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
            case 1:
            case 2:
                this.be.askPerm(str);
                return;
            case 3:
                this.be.settings("storage");
                return;
            case 4:
                drawer(true);
                MAIN.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda27
                    @Override // java.lang.Runnable
                    public final void run() {
                        NativeHome.this.m290lambda$xTap$7$comaugmaosohomeNativeHome();
                    }
                }, 300L);
                return;
            case 5:
                this.view.host.longOrb();
                return;
            case 6:
                this.be.settings("battery");
                return;
            case 7:
                this.be.launchRole("clock");
                return;
            case '\b':
                this.be.expandNotifs();
                return;
            case '\t':
                this.be.openSettings("health");
                return;
            case '\n':
                this.be.openSettings(AppGridModel.ID);
                return;
            case 11:
                this.be.openSettings("google");
                return;
            default:
                if (str.startsWith("tile:")) {
                    String substring = str.substring(5);
                    if ("health".equals(substring)) {
                        this.be.openSettings("health");
                        return;
                    } else {
                        this.be.launchRole(substring);
                        return;
                    }
                }
                if (str.startsWith("mode:")) {
                    mode(str.substring(5));
                    return;
                }
                if (str.equals("you")) {
                    YouSheet.show(this.root, this.h.th, this.be, new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda28
                        @Override // java.lang.Runnable
                        public final void run() {
                            NativeHome.this.m291lambda$xTap$8$comaugmaosohomeNativeHome();
                        }
                    });
                    return;
                }
                if (str.startsWith("corner:")) {
                    String cornerDetail = cornerDetail(this.d.corner[Math.max(0, Math.min(3, str.charAt(7) - '0'))]);
                    if (cornerDetail.startsWith("set:")) {
                        this.be.settings(cornerDetail.substring(4));
                        return;
                    } else {
                        WidgetDetail.show(this.root, this.be, this.h, this.d, cornerDetail);
                        return;
                    }
                }
                if (str.startsWith("detail:")) {
                    WidgetDetail.show(this.root, this.be, this.h, this.d, str.substring(7));
                    return;
                }
                switch (str.hashCode()) {
                    case 3058:
                        if (str.equals("a3")) {
                            c2 = 0;
                            break;
                        }
                        break;
                }
                switch (c2) {
                    case 0:
                        this.be.openApp("Brief");
                        return;
                    default:
                        if (str.matches("a\\d")) {
                            this.be.openAsk(Strings.t("w.assistant.q" + str.substring(1)));
                            return;
                        }
                        return;
                }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$xTap$7$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m290lambda$xTap$7$comaugmaosohomeNativeHome() {
        if (this.drawer != null) {
            this.drawer.keyboard(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$xTap$8$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m291lambda$xTap$8$comaugmaosohomeNativeHome() {
        refresh();
        this.view.redraw();
        this.view.back.invalidate();
    }

    public void debug(String str) {
        if (str == null) {
            return;
        }
        if (str.equals("drawer") || str.startsWith("drawer:search=")) {
            if (this.wel != null) {
                return;
            }
            drawer(true);
            if (!str.startsWith("drawer:search=") || this.drawer == null) {
                return;
            }
            this.drawer.q.setText(str.substring(14));
            this.drawer.q.setSelection(this.drawer.q.length());
            return;
        }
        if (str.equals("aoa:unlock")) {
            this.view.unlockAnim();
            return;
        }
        if (str.startsWith("aoa:")) {
            aoaHooks();
            AoaPage.show(this.root, this.be, this.h, this.d, str.substring(4));
            return;
        }
        if (str.startsWith("unfold")) {
            Unfold.simulate(str.endsWith("book") ? "book" : "flip");
            return;
        }
        if (str.equals("locksheet")) {
            Sheet.closeTop();
            if (this.drawer != null) {
                drawer(false);
            }
            Store.remove("aos.lockconsent");
            Store.setHome("aos.lockconsent", "");
            LockSheet.ask(this.root, this.h.th, this.be, false);
            return;
        }
        if (str.startsWith("onboarding:")) {
            try {
                int parseInt = Integer.parseInt(str.substring(11).trim());
                if (this.wel != null) {
                    this.root.removeView(this.wel);
                    this.wel = null;
                }
                Store.set(OnboardModel.K_STEP, String.valueOf(Math.max(0, parseInt - 1)));
                Store.set(OnboardModel.K_PATH, "custom");
                Store.set("aos.onboarded", "");
                welcome();
            } catch (Exception e) {
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    void mode(String str) {
        AudioManager audioManager = (AudioManager) this.be.ctx().getSystemService(AudioManager.class);
        NotificationManager notificationManager = (NotificationManager) this.be.ctx().getSystemService(NotificationManager.class);
        char c = 0;
        boolean z = (notificationManager == null || notificationManager.getCurrentInterruptionFilter() == 1) ? false : true;
        try {
            switch (str.hashCode()) {
                case -902327211:
                    if (str.equals("silent")) {
                        break;
                    }
                    c = 65535;
                    break;
                case 96673:
                    if (str.equals("all")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 3321596:
                    if (str.equals("life")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 3655441:
                    if (str.equals("work")) {
                        c = 3;
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
                    if (audioManager != null && !z) {
                        audioManager.setRingerMode(1);
                        break;
                    } else {
                        this.be.settings("sound");
                        break;
                    }
                case 1:
                case 2:
                    if (audioManager != null && !z) {
                        audioManager.setRingerMode(2);
                        break;
                    } else {
                        this.be.settings("dnd");
                        break;
                    }
                case 3:
                    this.be.settings("dnd");
                    this.be.toast(Strings.t("adv.mode.work.hint"));
                    break;
                default:
                    this.be.settings("dnd");
                    break;
            }
        } catch (Exception e) {
            this.be.settings("dnd");
        }
        MAIN.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                NativeHome.this.m280lambda$mode$9$comaugmaosohomeNativeHome();
            }
        }, 600L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$mode$9$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m280lambda$mode$9$comaugmaosohomeNativeHome() {
        this.view.pager.bindAll();
        this.view.back.invalidate();
        this.view.aoaL.invalidate();
    }

    void pagesLoad() {
        this.pagesCfg = HomePages.cfg(Store.get(HomePages.KEY, ""));
        this.h.pages = HomePages.count(this.pagesCfg);
        if (this.h.page >= this.h.pages) {
            showPage(0);
        }
    }

    void showPage(int i) {
        if (this.pv != null) {
            this.root.removeView(this.pv);
            this.pv = null;
        }
        this.h.page = Math.max(0, Math.min(this.h.pages - 1, i));
        this.view.back.invalidate();
        if (this.h.page == 0) {
            return;
        }
        Geo geo = this.h.g;
        geo.adv();
        float f = geo.pTop;
        float f2 = geo.adv() ? geo.tile[7].bottom : geo.pTop + geo.ph;
        this.pv = new PageView(this.be.ctx(), this.h, this.pagesCfg.pages.get(this.h.page - 1), new PageView.Host() { // from class: com.jarvis.assist.aosui.home.NativeHome.9
            @Override // com.jarvis.assist.aosui.home.PageView.Host
            public void swipe(int i2) {
                NativeHome.this.showPage(HomePages.next(NativeHome.this.h.page, i2, NativeHome.this.h.pages));
                Feedback.fx(NativeHome.this.view, 8);
            }

            @Override // com.jarvis.assist.aosui.home.PageView.Host
            public void edit() {
                Feedback.fx(NativeHome.this.view, 3);
                NativeHome.this.be.openSettings("custompages");
            }

            @Override // com.jarvis.assist.aosui.home.PageView.Host
            public void tap(String str) {
                if (!str.startsWith("g:")) {
                    NativeHome.this.go(str);
                    return;
                }
                String substring = str.substring(2);
                int indexOf = substring.indexOf(47);
                NativeHome.this.be.launch(substring.substring(0, indexOf), substring.substring(indexOf + 1));
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(Math.round(geo.pw * geo.S), Math.round((f2 - f) * geo.S));
        layoutParams.leftMargin = Math.round(geo.ox + (geo.pLeft * geo.S));
        layoutParams.topMargin = Math.round(geo.oy + (f * geo.S));
        this.root.addView(this.pv, this.root.indexOfChild(this.view) + 1, layoutParams);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    static String cornerDetail(String str) {
        char c;
        switch (str.hashCode()) {
            case -1884274053:
                if (str.equals("storage")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -838362136:
                if (str.equals("uptime")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -331239923:
                if (str.equals("battery")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 3076010:
                if (str.equals("data")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 3357441:
                if (str.equals("moon")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3560141:
                if (str.equals("time")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 92895825:
                if (str.equals("alarm")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 96891546:
                if (str.equals("event")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 99151942:
                if (str.equals("heart")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 109522647:
                if (str.equals("sleep")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 109761319:
                if (str.equals("steps")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1223440372:
                if (str.equals("weather")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1272354024:
                if (str.equals("notifications")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1843485230:
                if (str.equals("network")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 1942983418:
                if (str.equals("daylight")) {
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
            case 1:
            case 2:
                return "weather";
            case 3:
                return "calendar";
            case 4:
            case 5:
            case 6:
                return "health";
            case 7:
            case '\b':
            case '\t':
                return "battery";
            case '\n':
                return "notifs";
            case 11:
            case '\f':
                return "worldclock";
            case FxModel.BOOT /* 13 */:
            case FxModel.LOCK /* 14 */:
                return "set:internet";
            default:
                return "worldclock";
        }
    }

    public void onPowerConnected() {
        PowerManager powerManager = (PowerManager) this.be.ctx().getSystemService(PowerManager.class);
        if (ChargeFxOk(powerManager != null && powerManager.isInteractive())) {
            BatteryManager batteryManager = (BatteryManager) this.be.ctx().getSystemService(BatteryManager.class);
            this.root.addView(new ChargeView(this.be.ctx(), Math.max(0, Math.min(100, batteryManager == null ? this.d.btPct : batteryManager.getIntProperty(4))) / 100.0f, ChargeFx.fast(batteryManager == null ? 0L : batteryManager.getLongProperty(2)), this.h.th.RED, this.h.th.BLUE), new FrameLayout.LayoutParams(-1, -1));
        }
    }

    boolean ChargeFxOk(boolean z) {
        return ChargeFx.play(Store.get(ChargeFx.KEY, ""), Perf.k().calm, this.resumed && !covered(), z);
    }

    void lockNow() {
        boolean z;
        if (this.be.lockScreen()) {
            return;
        }
        try {
            z = new JSONObject(this.be.lockState()).optBoolean("canService", true);
        } catch (Exception e) {
            z = true;
        }
        LockSheet.ask(this.root, this.h.th, this.be, true ^ z);
    }

    void drawer(boolean z) {
        if (!z) {
            ime(false);
            if (this.drawer != null) {
                Feedback.fx(this.view, 7);
                final AppsDrawer appsDrawer = this.drawer;
                this.drawer = null;
                appsDrawer.close(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda18
                    @Override // java.lang.Runnable
                    public final void run() {
                        NativeHome.this.m275lambda$drawer$10$comaugmaosohomeNativeHome(appsDrawer);
                    }
                });
                homeCovered(false);
                return;
            }
            return;
        }
        if (this.drawer != null) {
            this.drawer.reload();
            return;
        }
        this.drawer = new AppsDrawer(this.be.ctx(), this.be, this.h, this.d, new AppsDrawer.Host() { // from class: com.jarvis.assist.aosui.home.NativeHome.10
            @Override // com.jarvis.assist.aosui.home.AppsDrawer.Host
            public void launch(String str, String str2, String str3) {
                NativeHome.this.d.remember(str, str2, str3);
                NativeHome.this.d.bump(str);
                NativeHome.this.d.bumpRoutine(str);
                NativeHome.this.be.launch(str, str2);
                NativeHome.this.drawer(false);
            }

            @Override // com.jarvis.assist.aosui.home.AppsDrawer.Host
            public void close() {
                NativeHome.this.drawer(false);
            }

            @Override // com.jarvis.assist.aosui.home.AppsDrawer.Host
            public void dockChanged() {
                NativeHome.this.onStore();
            }
        });
        Feedback.fx(this.view, 6);
        this.root.addView(this.drawer, new FrameLayout.LayoutParams(-1, -1));
        homeCovered(true);
        this.drawer.open();
        ime(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$drawer$10$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m275lambda$drawer$10$comaugmaosohomeNativeHome(AppsDrawer appsDrawer) {
        this.root.removeView(appsDrawer);
    }

    void ime(boolean z) {
        try {
            this.be.activity().getWindow().setSoftInputMode(z ? 16 : 32);
            if (this.drawer != null) {
                this.drawer.requestApplyInsets();
            }
        } catch (Exception e) {
        }
    }

    void homeCovered(boolean z) {
        if (Build.VERSION.SDK_INT >= 31) {
            float max = (!z || Perf.k().blur <= 0.0f) ? 0.0f : Math.max(1.0f, (((Perf.k().blur * 18.0f) * this.be.ctx().getResources().getDisplayMetrics().density) - 0.5f) / 0.57735f);
            this.view.setRenderEffect(max > 0.0f ? RenderEffect.createBlurEffect(max, max, Shader.TileMode.CLAMP) : null);
        }
        FrameClock.I.setActive(!covered() && this.resumed);
        if (this.wel == null) {
            this.view.setImportantForAccessibility(z ? 4 : 0);
        }
        this.root.notifySubtreeAccessibilityStateChanged(this.root, this.root, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void banner() {
        boolean z = this.wel == null && Store.migrated() && bannerShow();
        if (z && this.banner == null) {
            this.banner = new SetHomeBanner(this.be.ctx(), this.h.th, this.be, this.h.zoom, new SetHomeBanner.Host() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda21
                @Override // com.jarvis.assist.aosui.home.SetHomeBanner.Host
                public final void gone() {
                    NativeHome.this.bannerGone();
                }
            });
            float f = this.be.ctx().getResources().getDisplayMetrics().density;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2, 49);
            int round = Math.round(8.0f * f);
            layoutParams.setMargins(round, Math.round(f * 4.0f) + this.view.un.it, round, 0);
            this.root.addView(this.banner, Math.min(this.root.getChildCount(), this.root.indexOfChild(this.view) + 1), layoutParams);
            this.banner.addOnLayoutChangeListener(this.bannerPush);
            return;
        }
        if (z || this.banner == null) {
            return;
        }
        bannerGone();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBannerLayout(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int round = i4 + Math.round(this.be.ctx().getResources().getDisplayMetrics().density * 4.0f);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.view.getLayoutParams();
        if (layoutParams.topMargin != round) {
            layoutParams.topMargin = round;
            this.view.setLayoutParams(layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bannerGone() {
        if (this.banner != null) {
            this.banner.removeOnLayoutChangeListener(this.bannerPush);
            this.root.removeView(this.banner);
            this.banner = null;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.view.getLayoutParams();
        if (layoutParams.topMargin != 0) {
            layoutParams.topMargin = 0;
            this.view.setLayoutParams(layoutParams);
        }
    }

    boolean bannerShow() {
        return HomeBanner.show(this.be.isHome(), HomeBanner.parse(Store.get(HomeBanner.KEY, "")), System.currentTimeMillis());
    }

    void googleRefresh() {
        if (GoogleLink.connected(this.be.ctx())) {
            if (System.currentTimeMillis() - Store.obj(GoogleAuth.CACHE).optLong("at") < 900000) {
                return;
            }
            this.io.execute(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    NativeHome.this.m277lambda$googleRefresh$12$comaugmaosohomeNativeHome();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$googleRefresh$11$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m276lambda$googleRefresh$11$comaugmaosohomeNativeHome() {
        if (this.dead) {
            return;
        }
        this.view.pager.bindAll();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$googleRefresh$12$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m277lambda$googleRefresh$12$comaugmaosohomeNativeHome() {
        GoogleLink.refresh(this.be.ctx());
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                NativeHome.this.m276lambda$googleRefresh$11$comaugmaosohomeNativeHome();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void maybeUnlock() {
        if (this.dead || !this.h.aoa || this.wel != null || this.drawer != null || this.boot != null) {
            this.presentAt = 0L;
            return;
        }
        if (UnlockAnim.should(Store.get(UnlockAnim.KEY, ""), Perf.k().calm || !Perf.k().motion, this.presentAt, SystemClock.uptimeMillis())) {
            this.presentAt = 0L;
            AoaPage.closeTop(this.root);
            this.view.unlockAnim();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onResume$13$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m285lambda$onResume$13$comaugmaosohomeNativeHome() {
        playBoot(false, true);
    }

    public void onResume() {
        this.resumed = true;
        MAIN.post(new NativeHome$$ExternalSyntheticLambda23(this));
        if (Unfold.M.takePending(SystemClock.uptimeMillis()) && unfoldOk() && Unfold.M.allow(SystemClock.uptimeMillis())) {
            MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    NativeHome.this.m285lambda$onResume$13$comaugmaosohomeNativeHome();
                }
            });
        }
        meter();
        googleRefresh();
        MAIN.post(new NativeHome$$ExternalSyntheticLambda4(this));
        Store.on(this.storeChanged);
        refresh();
        theme();
        this.view.relayout();
        health();
        this.d.weather(false, new NativeHome$$ExternalSyntheticLambda8(this));
        welcome();
        if (this.wel != null) {
            this.wel.onResume();
        }
        FrameClock.I.setActive(true ^ covered());
        if (this.drawer != null) {
            this.drawer.reload();
        }
        this.view.pager.shownLater();
        MAIN.removeCallbacks(this.ticker);
        MAIN.postDelayed(this.ticker, Conn.STALE_MS);
        MAIN.postDelayed(this.autoKick, KeyCheck.VERIFY_RETRY_MS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void autoKick() {
        this.auto.kick(this.d, this.resumed && !covered());
    }

    public void onPause() {
        this.resumed = false;
        MAIN.removeCallbacks(this.ticker);
        MAIN.removeCallbacks(this.autoKick);
        meter();
    }

    public void onStart() {
        if (this.wel == null) {
            this.view.restoreLayers();
        }
        FrameClock.I.setActive(!covered());
    }

    public void onStop() {
        FrameClock.I.setActive(false);
        this.view.dropLayers();
    }

    public boolean onBack() {
        if (this.wel != null) {
            return this.wel.onBack();
        }
        if (AoaPage.closeTop(this.root) || WidgetDetail.close(this.root) || Sheet.closeTop()) {
            return true;
        }
        if (this.drawer == null) {
            return false;
        }
        if (this.drawer.onBack()) {
            return true;
        }
        drawer(false);
        return true;
    }

    public void onHomeKey() {
        if (this.wel != null) {
            return;
        }
        AoaPage.closeTop(this.root);
        Sheet.closeTop();
        drawer(false);
        this.view.pager.go(0, true);
        WidgetDetail.close(this.root);
        showPage(this.pagesCfg.def);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onConfig$14$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m284lambda$onConfig$14$comaugmaosohomeNativeHome() {
        showPage(this.h.page);
    }

    public void onConfig() {
        Unfold.config(this.be.ctx().getResources().getConfiguration().smallestScreenWidthDp);
        theme();
        this.view.relayout();
        if (this.h.page > 0) {
            MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    NativeHome.this.m284lambda$onConfig$14$comaugmaosohomeNativeHome();
                }
            });
        }
        if (this.wel != null) {
            this.wel.onConfig();
        }
        bannerGone();
        MAIN.post(new NativeHome$$ExternalSyntheticLambda4(this));
    }

    public void onApps() {
        if (this.drawer != null) {
            this.drawer.reload();
        }
    }

    public void onPerm() {
        this.d.calendar();
        this.d.brief();
        redraw();
        if (this.wel != null) {
            this.wel.onPerm();
        }
        wxOnPerm();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$wxOnPerm$15$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m288lambda$wxOnPerm$15$comaugmaosohomeNativeHome(boolean[] zArr) {
        if (this.dead || zArr[0] || !hasLoc()) {
            return;
        }
        zArr[0] = true;
        this.d.weather(true, new NativeHome$$ExternalSyntheticLambda8(this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$wxOnPerm$16$com-augma-oso-home-NativeHome, reason: not valid java name */
    public /* synthetic */ void m289lambda$wxOnPerm$16$comaugmaosohomeNativeHome(final boolean[] zArr, String str) {
        MAIN.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                NativeHome.this.m288lambda$wxOnPerm$15$comaugmaosohomeNativeHome(zArr);
            }
        });
    }

    void wxOnPerm() {
        if (hasLoc()) {
            return;
        }
        final boolean[] zArr = {false};
        this.be.locRefresh(new Consumer() { // from class: com.jarvis.assist.aosui.home.NativeHome$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NativeHome.this.m289lambda$wxOnPerm$16$comaugmaosohomeNativeHome(zArr, (String) obj);
            }
        });
    }

    boolean hasLoc() {
        try {
            String loc = this.be.loc();
            if (loc.isEmpty()) {
                return false;
            }
            return new JSONObject(loc).has("lat");
        } catch (Exception e) {
            return false;
        }
    }

    public static void noteNotif(String str) {
        NativeHome nativeHome = live.get();
        if (nativeHome == null || str == null || str.length() <= 2) {
            return;
        }
        nativeHome.auto.note(str);
    }

    public void onNotif() {
        MAIN.removeCallbacks(this.notifNow);
        MAIN.postDelayed(this.notifNow, 120L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNotifNow() {
        this.d.notifsNow();
        this.d.dots(this.h.th.RED, this.h.th.BLUE, this.h.th.DIM);
        this.view.back.invalidate();
        this.view.pager.bindAll();
        if (this.drawer != null) {
            this.drawer.dots(this.d.pkgDot);
        }
        AoaPage.changed(this.root);
    }

    public void onMedia() {
        MAIN.removeCallbacks(this.mediaNow);
        MAIN.post(this.mediaNow);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMediaNow() {
        this.d.mediaNow();
        this.view.pager.bindAll();
        AoaPage.changed(this.root);
    }

    public void onPowerSave(boolean z) {
        Perf.powerSave(z);
    }

    public void onThermal(int i) {
        Perf.thermal(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trim(int i, int i2) {
        if (!this.resumed || i < 20) {
            if (MemPolicy.has(i2, 1) && !this.resumed) {
                FrameClock.I.setActive(false);
            }
            if (MemPolicy.has(i2, 2) && !this.resumed) {
                this.view.dropLayers();
            }
            if (MemPolicy.has(i2, 32)) {
                this.view.pager.near(this.resumed ? 2 : 0);
            }
            if (!MemPolicy.has(i2, 8) || this.resumed) {
                return;
            }
            if (this.drawer != null) {
                AppsDrawer appsDrawer = this.drawer;
                this.drawer = null;
                this.root.removeView(appsDrawer);
                homeCovered(false);
            }
            Sheet.closeTop();
            this.view.pager.trimSlides();
        }
    }

    public void onDestroy() {
        this.dead = true;
        try {
            this.be.ctx().unregisterReceiver(this.present);
        } catch (Exception e) {
        }
        Unfold.detach(this.unfoldHost);
        if (live.get() == this) {
            live.clear();
        }
        Memory.off(this.trim);
        Perf.offMirror(this.mirror);
        Sheet.closeTop();
        MAIN.removeCallbacks(this.autoKick);
        MAIN.removeCallbacks(this.ticker);
        MAIN.removeCallbacks(this.notifNow);
        MAIN.removeCallbacks(this.mediaNow);
        MAIN.removeCallbacks(this.meterTick);
        Store.off(this.storeChanged);
        Perf.off(this.perfChanged);
        this.view.release();
        FrameClock.I.setActive(false);
    }
}
