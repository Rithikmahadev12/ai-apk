package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeProvider;
import com.jarvis.assist.aosui.core.Detent;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.Palette;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Spin;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.Units;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.core.UnlockAnim;
import com.jarvis.assist.aosui.home.AoaStripView;
import com.jarvis.assist.aosui.home.HomeData;
import com.jarvis.assist.aosui.home.HomeView;
import com.jarvis.assist.aosui.home.VirtualA11y;
import com.jarvis.assist.aosui.home.WidgetPager;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

/* loaded from: /tmp/augmaos/classes.dex */
public final class HomeView extends ViewGroup {
    VirtualA11y a11y;
    final View act;
    Runnable afterLayout;
    final AoaLayer aoaL;
    final BackLayer back;
    final View bg;
    final Detent detent;
    final DockLayer dock;
    Spin dockM;
    float dockT0;
    boolean freeStart;
    RadialGradient glow;
    final Matrix gm;
    final HomeCtx h;
    final Host host;
    final Paint hp;
    final Path hz;
    final LandLayer land;
    float lastA;
    long lastT;
    long lastTap;
    boolean layersDropped;
    boolean longFired;
    final Runnable longPress;
    boolean onOrb;
    final OrbLayer orb;
    final Paint p;
    final WidgetPager pager;
    final Rect rb;
    boolean ringDown;
    Spin ringM;
    Spin ringMq;
    boolean ringOuter;
    boolean scrubbing;
    final int slop;
    String slotArea;
    boolean slotFired;
    final Runnable slotLong;
    final FrameClock.Handle spinLoop;
    boolean spinning;
    final AoaStripView strip;
    long t0;
    boolean tapped;
    final Units un;
    float vel;
    float x0;
    float y0;

    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Host {
        void doubleTap();

        void go(String str);

        void longOrb();

        void page(int i);

        void swipe(boolean z);

        default void longSlot(String str) {
        }

        default void pageSwipe(int i) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HomeView(Context context, final HomeCtx homeCtx, final Host host) {
        super(context);
        this.un = new Units();
        this.p = new Paint();
        this.gm = new Matrix();
        this.detent = new Detent();
        this.hz = new Path();
        this.hp = new Paint(1);
        this.slotLong = new Runnable() { // from class: com.jarvis.assist.aosui.home.HomeView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                HomeView.this.onSlotLong();
            }
        };
        this.longPress = new Runnable() { // from class: com.jarvis.assist.aosui.home.HomeView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                HomeView.this.onLong();
            }
        };
        this.rb = new Rect();
        this.h = homeCtx;
        this.host = host;
        this.slop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.bg = new View(context) { // from class: com.jarvis.assist.aosui.home.HomeView.1
            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                HomeView.this.drawBg(canvas);
            }
        };
        this.dock = new DockLayer(context, homeCtx);
        this.back = new BackLayer(context, homeCtx);
        this.orb = new OrbLayer(context, homeCtx);
        this.land = new LandLayer(context, homeCtx);
        this.land.setVisibility(8);
        this.back.setLayerType(2, null);
        this.land.setLayerType(2, null);
        this.pager = new WidgetPager(context, homeCtx, new WidgetPager.Listener() { // from class: com.jarvis.assist.aosui.home.HomeView.2
            @Override // com.jarvis.assist.aosui.home.WidgetPager.Listener
            public void page(int i) {
                Feedback.fx(HomeView.this, HomeView.this.scrubbing ? 15 : 8);
                HomeView.this.dock.setPages(HomeView.this.pager.count(), i);
                host.page(i);
                if (HomeView.this.a11y != null) {
                    HomeView.this.a11y.contentChanged();
                }
            }

            @Override // com.jarvis.assist.aosui.home.WidgetPager.Listener
            public void tap(String str) {
                host.go(str);
            }

            @Override // com.jarvis.assist.aosui.home.WidgetPager.Listener
            public void backdrop(Canvas canvas) {
                canvas.translate(-HomeView.this.pager.getLeft(), -HomeView.this.pager.getTop());
                HomeView.this.drawBg(canvas);
                if (homeCtx.g.land) {
                    HomeView.this.land.draw(canvas);
                } else {
                    HomeView.this.back.draw(canvas);
                }
            }
        });
        this.act = new View(context) { // from class: com.jarvis.assist.aosui.home.HomeView.3
            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                if (!homeCtx.brief || homeCtx.active < 0 || homeCtx.active >= homeCtx.g.nw) {
                    return;
                }
                int save = canvas.save();
                canvas.translate(homeCtx.g.ox, homeCtx.g.oy);
                canvas.scale(homeCtx.g.S, homeCtx.g.S);
                if (homeCtx.g.land) {
                    HomeView.this.land.wedge(canvas, homeCtx.active, true);
                } else {
                    HomeView.this.back.wedge(canvas, homeCtx.active, true);
                }
                canvas.restoreToCount(save);
            }
        };
        this.act.setImportantForAccessibility(2);
        this.strip = new AoaStripView(context, homeCtx, new AoaStripView.Tap() { // from class: com.jarvis.assist.aosui.home.HomeView$$ExternalSyntheticLambda3
            @Override // com.jarvis.assist.aosui.home.AoaStripView.Tap
            public final void go(String str) {
                HomeView.Host.this.go(str);
            }
        });
        this.strip.setVisibility(8);
        this.aoaL = new AoaLayer(context, homeCtx);
        this.aoaL.setVisibility(8);
        this.aoaL.setLayerType(2, null);
        this.aoaL.onAnim = new Consumer() { // from class: com.jarvis.assist.aosui.home.HomeView$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                HomeView.this.m273lambda$new$1$comaugmaosohomeHomeView(homeCtx, (Float) obj);
            }
        };
        addView(this.bg);
        addView(this.dock);
        addView(this.back);
        addView(this.land);
        addView(this.act);
        addView(this.aoaL);
        addView(this.strip);
        addView(this.orb);
        addView(this.pager);
        this.a11y = new VirtualA11y(this, new A11ySrc());
        this.spinLoop = FrameClock.I.add(new FrameClock.Sub() { // from class: com.jarvis.assist.aosui.home.HomeView$$ExternalSyntheticLambda5
            @Override // com.jarvis.assist.aosui.core.FrameClock.Sub
            public final boolean frame(long j, float f) {
                return HomeView.this.spinFrame(j, f);
            }
        }, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-augma-oso-home-HomeView, reason: not valid java name */
    public /* synthetic */ void m273lambda$new$1$comaugmaosohomeHomeView(HomeCtx homeCtx, Float f) {
        if (f.floatValue() < 0.0f) {
            this.orb.setScaleX(1.0f);
            this.orb.setScaleY(1.0f);
            this.orb.setAlpha(1.0f);
            this.strip.setTranslationX(0.0f);
            this.strip.setTranslationY(0.0f);
            this.strip.setAlpha(1.0f);
            return;
        }
        boolean z = this.aoaL.animLight;
        float orb = UnlockAnim.orb(f.floatValue(), z);
        float flash = (0.8f * orb) + 0.2f + (UnlockAnim.flash(f.floatValue(), z) * 0.08f);
        this.orb.setScaleX(flash);
        this.orb.setScaleY(flash);
        this.orb.setAlpha(Math.min(1.0f, orb * 1.4f));
        float left = (this.orb.getLeft() + this.orb.getRight()) / 2.0f;
        float top = (this.orb.getTop() + this.orb.getBottom()) / 2.0f;
        float item = UnlockAnim.item(((float) Math.hypot(r1 - left, r4 - top)) / homeCtx.g.S, this.aoaL.maxD, f.floatValue(), z);
        float f2 = 1.0f - item;
        this.strip.setTranslationX((left - ((this.strip.getLeft() + this.strip.getRight()) / 2.0f)) * f2);
        this.strip.setTranslationY(f2 * (top - ((this.strip.getTop() + this.strip.getBottom()) / 2.0f)));
        this.strip.setAlpha(item);
    }

    void detentAt(float f) {
        if (this.detent.move(f, SystemClock.uptimeMillis())) {
            Feedback.fx(this, 2);
        }
    }

    static boolean momentum() {
        return (Perf.k().calm || "LIGHT".equals(Perf.tier())) ? false : true;
    }

    float ang(float f, float f2, float f3, float f4) {
        return (float) Math.toDegrees(Math.atan2(f2 - f4, f - f3));
    }

    static float wrap(float f) {
        while (f > 180.0f) {
            f -= 360.0f;
        }
        while (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }

    boolean inRing(float f, float f2) {
        Geo geo = this.h.g;
        if (!geo.full) {
            return false;
        }
        float hypot = (float) Math.hypot(f - geo.cx, f2 - geo.cy);
        this.ringOuter = hypot > geo.RO && hypot <= geo.qsOut + 8.0f;
        return (hypot >= geo.RI && hypot <= geo.RO) || this.ringOuter;
    }

    void sample(float f, long j) {
        this.vel = (this.vel * 0.4f) + ((wrap(f - this.lastA) / ((float) Math.max(1L, j - this.lastT))) * 1000.0f * 0.6f);
        this.lastA = f;
        this.lastT = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean spinFrame(long j, float f) {
        boolean z;
        Geo geo = this.h.g;
        if (this.ringM == null) {
            z = false;
        } else {
            if (this.ringM.step(f) > 0) {
                Feedback.fx(this, 15);
            } else {
                detentAt(this.ringM.angle);
            }
            geo.spin = this.ringM.angle;
            boolean done = this.ringM.done();
            if (done) {
                geo.spin = ((geo.spin % 360.0f) + 360.0f) % 360.0f;
                this.ringM = null;
                this.a11y.contentChanged();
            }
            geo.ring(done);
            this.land.invalidate();
            z = (!done) | false;
        }
        if (this.ringMq != null) {
            if (this.ringMq.step(f) > 0) {
                Feedback.fx(this, 15);
            } else {
                detentAt(this.ringMq.angle);
            }
            geo.spinQ = this.ringMq.angle;
            boolean done2 = this.ringMq.done();
            if (done2) {
                geo.spinQ = ((geo.spinQ % 360.0f) + 360.0f) % 360.0f;
                this.ringMq = null;
                this.a11y.contentChanged();
            }
            geo.ringQ(done2);
            this.land.invalidate();
            z |= !done2;
        }
        if (this.dockM != null) {
            this.dockM.step(f);
            detentAt(this.dockT0 + this.dockM.angle);
            int scrub = this.dock.scrub(this.dockT0 + this.dockM.angle);
            if (scrub != this.pager.cur) {
                this.pager.go(scrub, true);
            }
            if (!this.dockM.done()) {
                return true;
            }
            this.dockM = null;
            this.scrubbing = false;
            this.dock.release(this.pager.cur);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void relayout() {
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) {
            return;
        }
        this.un.measure(getContext(), width, height);
        float f = getResources().getDisplayMetrics().density;
        Geo geo = this.h.g;
        geo.aoa = false;
        String homeMode = UnitsMath.homeMode(((width - this.un.il) - this.un.ir) / f, ((height - this.un.it) - this.un.ib) / f);
        boolean equals = "portrait".equals(homeMode);
        boolean z = !equals;
        Log.d("aOS", "home relayout " + width + "x" + height + " " + homeMode + (!equals ? " " + landMode() : ""));
        if ("compact".equals(homeMode)) {
            geo.layoutCompact(width, height, f, this.un.il, this.un.it, this.un.ir, this.un.ib, this.h.d.qs.size(), this.h.d.wheel.size(), this.h.d.dock.size());
            this.dock.configure(geo.cx, geo.cy, geo.RO, 160.0f, 20.0f, 0.0f, 3.1415927f, (geo.cx - geo.RO) - 52.0f, geo.cy, (geo.RO * 2.0f) + 104.0f, geo.RO + 52.0f);
        } else if (this.h.aoa) {
            this.aoaL.slots();
            geo.layoutAoa(width, height, f, this.un.il, this.un.it, this.un.ir, this.un.ib, this.h.d.qs.size(), this.h.d.wheel.size(), this.h.d.dock.size(), z, this.aoaL.inId, this.aoaL.inLab, this.aoaL.tId, this.aoaL.tLab);
        } else if (!equals) {
            geo.layoutLand(width, height, f, this.un.il, this.un.it, this.un.ir, this.un.ib, this.h.d.qs.size(), this.h.d.wheel.size(), this.h.d.dock.size(), landMode());
            if (geo.full) {
                this.dock.configure(geo.cx, geo.cy, geo.qsOut - 6.0f, 250.0f, 110.0f, 1.8849556f, 4.3982296f, (geo.cx - geo.qsOut) - 46.0f, 30.0f, geo.qsOut + 46.0f, geo.LH - 30.0f);
            } else if ("arc".equals(geo.mode)) {
                this.dock.configure(geo.bx, geo.by, geo.RO, 250.0f, 110.0f, 1.5707964f, 4.712389f, (geo.bx - geo.RO) - 52.0f, 0.0f, geo.RO + 52.0f, geo.LH);
            } else {
                this.dock.configure(geo.cx, geo.cy, geo.RO, 160.0f, 20.0f, 0.0f, 3.1415927f, (geo.cx - geo.RO) - 52.0f, geo.cy, (geo.RO * 2.0f) + 104.0f, geo.LH - geo.cy);
            }
        } else if (this.h.adv) {
            geo.layoutAdv(width, height, f, this.un.il, this.un.it, this.un.ir, this.un.ib, this.h.d.qs.size(), this.h.d.wheel.size(), this.h.d.dock.size(), BackLayer.advExtraIds(this.h.d), BackLayer.advExtraLabels(this.h.d), q2Labels());
            this.dock.portrait();
        } else {
            geo.layout(width, height, f, this.un.il, this.un.it, this.un.ir, this.un.ib, this.h.d.qs.size(), this.h.d.wheel.size(), this.h.d.dock.size());
            if (this.h.brief) {
                geo.pTop = geo.pt;
                geo.ph = Math.max(geo.ph, ((geo.by - geo.RO) - 50.0f) - geo.pt);
            }
            this.dock.portrait();
        }
        if (this.h.adv && !this.h.brief) {
            if (this.pager.greetIn != (!equals || this.h.d.widgetOrder().isEmpty())) {
                this.pager.setWidgets(this.h.d.widgetOrder());
            }
        }
        this.back.setVisibility(!equals ? 8 : 0);
        this.land.setVisibility(!equals ? 0 : 8);
        this.orb.setVisibility(geo.orb ? 0 : 8);
        this.pager.setVisibility((geo.ph <= 0.0f || geo.pw <= 0.0f) ? 8 : 0);
        this.strip.setVisibility(geo.aoa() ? 0 : 8);
        if (geo.aoa()) {
            this.back.setVisibility(8);
            this.land.setVisibility(8);
            this.dock.setVisibility(8);
            this.pager.setVisibility(8);
            this.aoaL.setVisibility(0);
        } else {
            this.dock.setVisibility(0);
            this.aoaL.setVisibility(8);
        }
        restyle();
        requestLayout();
        this.a11y.contentChanged();
        if (this.afterLayout != null) {
            this.afterLayout.run();
        }
    }

    String[] q2Labels() {
        int size = this.h.d.qs2.size();
        String[] strArr = new String[size];
        int i = 0;
        while (i < size) {
            strArr[i] = this.h.d.qs2.get(i).label + ((i >= this.h.d.qs2On.length || !this.h.d.qs2On[i]) ? "" : ", on");
            i++;
        }
        return strArr;
    }

    void restyle() {
        Theme theme = this.h.th;
        int width = (getWidth() - this.un.il) - this.un.ir;
        int height = (getHeight() - this.un.it) - this.un.ib;
        if (width > 0 && height > 0) {
            float f = width;
            float f2 = f * 0.6f;
            float f3 = height * 0.3f;
            this.glow = new RadialGradient(0.0f, 0.0f, f2, new int[]{theme.GLOW, Theme.a(theme.GLOW, 0.0f)}, new float[]{0.0f, 0.7f}, Shader.TileMode.CLAMP);
            this.gm.reset();
            this.gm.setScale(1.0f, f3 / f2);
            this.gm.postTranslate(this.un.il + (f * 0.5f), this.un.it + f3);
            this.glow.setLocalMatrix(this.gm);
        }
        this.h.styles();
        if (this.h.g.aoa()) {
            this.aoaL.prepare();
        } else if (this.h.g.land) {
            this.land.prepare();
        } else {
            this.back.prepare();
        }
        this.dock.setPages(this.pager.count(), this.pager.cur);
        this.dock.prepare();
        this.orb.prepare();
        this.pager.prepare();
        this.strip.prepare();
        this.bg.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        this.spinLoop.remove();
        this.dock.loop.remove();
        this.back.pulseH.remove();
        this.orb.loop.remove();
        this.pager.loop.remove();
        this.strip.loop.remove();
        dropLayers();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dropLayers() {
        this.layersDropped = true;
        this.aoaL.setLayerType(0, null);
        this.back.setLayerType(0, null);
        this.land.setLayerType(0, null);
        this.pager.dropLayers();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreLayers() {
        if (this.layersDropped) {
            this.layersDropped = false;
            this.aoaL.setLayerType(2, null);
            this.back.setLayerType(2, null);
            this.land.setLayerType(2, null);
            this.pager.restoreLayers();
            this.pager.backdropChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void redraw() {
        this.back.invalidate();
        this.land.invalidate();
        this.aoaL.invalidate();
        this.orb.prepare();
        this.pager.bindAll();
        if (this.strip.getVisibility() == 0) {
            this.strip.bind();
        }
        this.pager.backdropChanged();
        this.a11y.contentChanged();
    }

    static String landMode() {
        String str = Store.get("aos.land", "centre");
        return ("arc".equals(str) || "wheel".equals(str)) ? str : "centre";
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        post(new HomeView$$ExternalSyntheticLambda0(this));
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            int[] rect = rect(childAt);
            childAt.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, rect[2] - rect[0]), 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(0, rect[3] - rect[1]), 1073741824));
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            int[] rect = rect(childAt);
            childAt.layout(rect[0], rect[1], rect[2], rect[3]);
        }
    }

    int[] rect(View view) {
        Geo geo = this.h.g;
        int measuredWidth = getMeasuredWidth() > 0 ? getMeasuredWidth() : getWidth();
        int measuredHeight = getMeasuredHeight() > 0 ? getMeasuredHeight() : getHeight();
        if (view == this.back || view == this.bg || view == this.land || view == this.act || view == this.aoaL) {
            return new int[]{0, 0, measuredWidth, measuredHeight};
        }
        if (view == this.dock) {
            return new int[]{Math.round(geo.ox + (this.dock.ox * geo.S)), Math.round(geo.oy + (this.dock.top * geo.S)), Math.round(geo.ox + ((this.dock.ox + this.dock.ow) * geo.S)), Math.round(geo.oy + ((this.dock.top + this.dock.oh) * geo.S))};
        }
        if (view != this.orb) {
            return view == this.strip ? new int[]{Math.round(geo.ox + (geo.gRect.left * geo.S)), Math.round(geo.oy + (geo.gRect.top * geo.S)), Math.round(geo.ox + (geo.gRect.right * geo.S)), Math.round(geo.oy + ((geo.gRect.bottom + 4.0f + 12.0f) * geo.S))} : new int[]{Math.round(geo.ox + (geo.pLeft * geo.S)), Math.round(geo.oy + (geo.pTop * geo.S)), Math.round(geo.ox + ((geo.pLeft + geo.pw) * geo.S)), Math.round(geo.oy + ((geo.pTop + geo.ph) * geo.S))};
        }
        float orbHalf = this.h.orbHalf();
        return new int[]{Math.round(geo.ox + ((geo.cx - orbHalf) * geo.S)), Math.round(geo.oy + ((geo.cy - orbHalf) * geo.S)), Math.round(geo.ox + ((geo.cx + orbHalf) * geo.S)), Math.round(geo.oy + ((geo.cy + orbHalf) * geo.S))};
    }

    void drawBg(Canvas canvas) {
        Canvas canvas2;
        Theme theme = this.h.th;
        int width = getWidth();
        int height = getHeight();
        canvas.drawColor(theme.light ? Palette.LIGHT_BG : -16777216);
        float f = width;
        float f2 = height;
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(theme.BG);
        canvas.drawRect(0.0f, 0.0f, f, f2, this.p);
        float f3 = this.un.density * 27.0f;
        float max = Math.max(1.0f, this.un.density);
        this.p.setColor(theme.GRID);
        for (float f4 = 0.0f; f4 < f2; f4 += f3) {
            canvas.drawRect(0.0f, f4, f, Math.min(f2, f4 + max), this.p);
        }
        for (float f5 = 0.0f; f5 < f; f5 += f3) {
            canvas.drawRect(f5, 0.0f, Math.min(f, f5 + max), f2, this.p);
        }
        if (this.glow != null) {
            this.p.setShader(this.glow);
            canvas2 = canvas;
            canvas2.drawRect(0.0f, 0.0f, f, f2, this.p);
            this.p.setShader(null);
        } else {
            canvas2 = canvas;
        }
        if (this.h.adv && this.h.g.adv() && !Perf.k().calm) {
            haze(canvas2, width, height);
        }
    }

    void haze(Canvas canvas, int i, int i2) {
        Theme theme = this.h.th;
        this.hp.setStyle(Paint.Style.STROKE);
        this.hp.setStrokeCap(Paint.Cap.ROUND);
        float f = this.un.density;
        int i3 = 0;
        while (i3 < 2) {
            float f2 = i2;
            float f3 = (i3 == 0 ? 0.86f : 0.9f) * f2;
            float f4 = f2 * (i3 == 0 ? 0.04f : 0.03f);
            this.hz.rewind();
            this.hz.moveTo(-20.0f, f3 + f4);
            float f5 = i;
            this.hz.cubicTo(f5 * 0.3f, f3 - (1.6f * f4), f5 * 0.62f, f3 + (1.8f * f4), i + 20, f3 - (f4 * 0.4f));
            int i4 = i3 == 0 ? theme.RED : theme.BLUE;
            this.hp.setColor(Theme.a(i4, 0.08f));
            this.hp.setStrokeWidth(10.0f * f);
            canvas.drawPath(this.hz, this.hp);
            this.hp.setColor(Theme.a(i4, 0.22f));
            this.hp.setStrokeWidth(Math.max(1.0f, 1.2f * f));
            canvas.drawPath(this.hz, this.hp);
            i3++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unlockAnim() {
        if (this.h.g.aoa()) {
            this.aoaL.playUnlock("LIGHT".equals(Perf.tier()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSlotLong() {
        if (this.slotArea == null) {
            return;
        }
        this.slotFired = true;
        Feedback.fx(this, 3);
        this.host.longSlot(this.slotArea);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLong() {
        this.longFired = true;
        this.host.longOrb();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Geo geo = this.h.g;
        float lx = geo.lx(motionEvent.getX());
        float ly = geo.ly(motionEvent.getY());
        String str = null;
        switch (motionEvent.getActionMasked()) {
            case 0:
                if (this.aoaL.animating()) {
                    this.aoaL.skipUnlock();
                    return true;
                }
                this.x0 = motionEvent.getX();
                this.y0 = motionEvent.getY();
                this.t0 = SystemClock.uptimeMillis();
                this.longFired = false;
                this.ringM = null;
                this.ringMq = null;
                if (this.dockM != null) {
                    this.dockM = null;
                    this.dock.release(this.pager.cur);
                }
                this.scrubbing = Geo.hit(geo.scrubR, lx, ly);
                this.vel = 0.0f;
                this.lastT = this.t0;
                if (this.scrubbing) {
                    this.lastA = ang(lx, ly, geo.bx, geo.by);
                    this.detent.reset(this.lastA);
                    scrub(lx, ly);
                    return true;
                }
                this.ringDown = inRing(lx, ly);
                this.spinning = false;
                if (this.ringDown) {
                    this.lastA = ang(lx, ly, geo.cx, geo.cy);
                }
                if (geo.adv() || geo.aoa()) {
                    this.h.pressed = action(lx, ly);
                    this.back.invalidate();
                    this.aoaL.invalidate();
                }
                this.onOrb = inOrb(lx, ly);
                if (this.onOrb) {
                    postDelayed(this.longPress, 520L);
                } else {
                    String action = action(lx, ly);
                    if (action != null) {
                        if (action.startsWith("q")) {
                            str = "qs";
                        } else if (action.startsWith("k")) {
                            str = "qs2";
                        } else if (action.startsWith("w") || action.startsWith("x:tile:")) {
                            str = "wheel";
                        } else if (action.startsWith("d")) {
                            str = "dock";
                        }
                    }
                    this.slotArea = str;
                    if (this.slotArea != null && !this.ringDown) {
                        postDelayed(this.slotLong, 520L);
                    }
                }
                if (!this.onOrb && action(lx, ly) == null) {
                    r7 = true;
                }
                this.freeStart = r7;
                return true;
            case 1:
            case 3:
                if (this.h.pressed != null) {
                    this.h.pressed = null;
                    final BackLayer backLayer = this.back;
                    Objects.requireNonNull(backLayer);
                    postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.HomeView$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            BackLayer.this.invalidate();
                        }
                    }, 90L);
                    final AoaLayer aoaLayer = this.aoaL;
                    Objects.requireNonNull(aoaLayer);
                    postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.HomeView$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            AoaLayer.this.invalidate();
                        }
                    }, 90L);
                }
                removeCallbacks(this.longPress);
                removeCallbacks(this.slotLong);
                if (this.slotFired) {
                    this.slotFired = false;
                    return true;
                }
                if (this.spinning && this.ringOuter) {
                    this.spinning = false;
                    this.ringDown = false;
                    this.ringMq = new Spin(geo.spinQ, geo.segQ());
                    this.ringMq.fling(SystemClock.uptimeMillis() - this.lastT <= 120 ? this.vel : 0.0f, momentum());
                    this.spinLoop.kick();
                    return true;
                }
                if (this.spinning) {
                    this.spinning = false;
                    this.ringDown = false;
                    this.ringM = new Spin(geo.spin, geo.seg());
                    this.ringM.fling(SystemClock.uptimeMillis() - this.lastT <= 120 ? this.vel : 0.0f, momentum());
                    this.spinLoop.kick();
                    return true;
                }
                this.ringDown = false;
                if (this.scrubbing) {
                    if (momentum() && SystemClock.uptimeMillis() - this.lastT < 120 && Math.abs(this.vel) > 40.0f) {
                        this.dockT0 = this.dock.T0;
                        this.dockM = new Spin(this.dock.fa - this.dockT0, this.pager.count() > 1 ? Math.abs(this.dock.T1 - this.dock.T0) / (this.pager.count() - 1) : 1.0f, 0.0f, this.dock.T1 - this.dock.T0);
                        this.dockM.fling(this.vel, true);
                        this.spinLoop.kick();
                        return true;
                    }
                    this.scrubbing = false;
                    this.dock.release(this.pager.cur);
                    return true;
                }
                if (motionEvent.getActionMasked() == 3) {
                    return true;
                }
                float x = motionEvent.getX() - this.x0;
                float y = motionEvent.getY() - this.y0;
                float f = this.un.density;
                long uptimeMillis = SystemClock.uptimeMillis() - this.t0;
                if (this.freeStart && Math.abs(y) > f * 70.0f && Math.abs(y) > Math.abs(x) * 1.5f && uptimeMillis < 600) {
                    this.host.swipe(y < 0.0f);
                    return true;
                }
                this.tapped = false;
                if (geo.aoa() && !geo.land && Math.abs(x) > f * 70.0f && Math.abs(x) > Math.abs(y) * 1.5f && uptimeMillis < 700) {
                    this.host.go(x < 0.0f ? "x:page:music" : "x:page:clock");
                    return true;
                }
                if (!this.spinning && this.h.pages > 1 && Math.abs(x) > 70.0f * f && Math.abs(x) > Math.abs(y) * 1.5f && uptimeMillis < 700) {
                    this.host.pageSwipe(x < 0.0f ? 1 : -1);
                    return true;
                }
                boolean z = Math.hypot((double) x, (double) y) < ((double) (((float) this.slop) * 1.5f));
                this.tapped = z;
                if (!z) {
                    return true;
                }
                if (this.onOrb) {
                    if (!this.longFired) {
                        this.host.go("ai");
                    }
                    return true;
                }
                String action2 = action(lx, ly);
                if (action2 != null && action2.startsWith("aoa:")) {
                    this.aoaL.cycle(action2.endsWith("next") ? 1 : -1);
                    Feedback.fx(this, 0);
                    return true;
                }
                if (action2 != null) {
                    this.host.go(action2);
                    return true;
                }
                float f2 = f * 12.0f;
                if (Math.abs(y) < f2 && Math.abs(x) < f2 && uptimeMillis < 250) {
                    long uptimeMillis2 = SystemClock.uptimeMillis();
                    if (uptimeMillis2 - this.lastTap < 320) {
                        this.lastTap = 0L;
                        this.host.doubleTap();
                    } else {
                        this.lastTap = uptimeMillis2;
                    }
                }
                return true;
            case 2:
                if (this.scrubbing) {
                    float ang = ang(lx, ly, geo.bx, geo.by);
                    sample(ang, SystemClock.uptimeMillis());
                    detentAt(ang);
                    scrub(lx, ly);
                    return true;
                }
                if (this.ringDown) {
                    if (!this.spinning && Math.hypot(motionEvent.getX() - this.x0, motionEvent.getY() - this.y0) > this.slop) {
                        this.spinning = true;
                        removeCallbacks(this.longPress);
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (this.spinning && this.ringOuter) {
                        float ang2 = ang(lx, ly, geo.cx, geo.cy);
                        float wrap = wrap(ang2 - this.lastA);
                        int round = Math.round(geo.spinQ / geo.segQ());
                        sample(ang2, SystemClock.uptimeMillis());
                        geo.spinQ += wrap;
                        if (Math.round(geo.spinQ / geo.segQ()) != round) {
                            Feedback.fx(this, 15);
                        } else {
                            detentAt(geo.spinQ);
                        }
                        geo.ringQ(false);
                        this.land.invalidate();
                        return true;
                    }
                    if (this.spinning) {
                        float ang3 = ang(lx, ly, geo.cx, geo.cy);
                        float wrap2 = wrap(ang3 - this.lastA);
                        int round2 = Math.round(geo.spin / geo.seg());
                        sample(ang3, SystemClock.uptimeMillis());
                        geo.spin += wrap2;
                        if (Math.round(geo.spin / geo.seg()) != round2) {
                            Feedback.fx(this, 15);
                        } else {
                            detentAt(geo.spin);
                        }
                        geo.ring(false);
                        this.land.invalidate();
                        return true;
                    }
                }
                if (Math.hypot(motionEvent.getX() - this.x0, motionEvent.getY() - this.y0) > this.slop) {
                    removeCallbacks(this.longPress);
                    removeCallbacks(this.slotLong);
                }
                return true;
            default:
                return super.onTouchEvent(motionEvent);
        }
    }

    void scrub(float f, float f2) {
        Geo geo = this.h.g;
        int scrub = this.dock.scrub((((float) Math.toDegrees(Math.atan2(f2 - geo.by, f - geo.bx))) + 360.0f) % 360.0f);
        if (scrub != this.pager.cur) {
            this.pager.go(scrub, true);
        }
    }

    boolean inOrb(float f, float f2) {
        Geo geo = this.h.g;
        if (!geo.orb) {
            return false;
        }
        if (geo.aoa()) {
            return Math.hypot((double) (f - geo.cx), (double) (f2 - geo.cy)) < ((double) (geo.aIn[0] - 2.0f));
        }
        if (!geo.full || (Math.hypot(f - geo.cx, f2 - geo.cy) < geo.RI && !Geo.hit(geo.appsR, f, f2))) {
            return (Math.abs(f - geo.cx) <= 70.0f && Math.abs(f2 - geo.cy) <= 70.0f) || (Math.abs(f - geo.cx) <= 60.0f && f2 >= geo.cy + 20.0f && f2 <= geo.cy + 32.0f);
        }
        return false;
    }

    String action(float f, float f2) {
        Geo geo = this.h.g;
        for (int i = 0; i < geo.nq; i++) {
            if (Geo.hit(geo.qsR[i], f, f2)) {
                return "q" + i;
            }
        }
        for (int i2 = 0; i2 < geo.nw; i2++) {
            if (Geo.hit(geo.whR[i2], f, f2)) {
                return "w" + i2;
            }
        }
        for (int i3 = 0; i3 < geo.nd; i3++) {
            if (Geo.hit(geo.dkR[i3], f, f2)) {
                return "d" + i3;
            }
        }
        if (Geo.hit(geo.appsR, f, f2)) {
            return "apps";
        }
        if (geo.adv() && !geo.youChip.isEmpty() && geo.youChip.contains(f, f2)) {
            return "x:you";
        }
        for (int i4 = 0; i4 < geo.exR.size(); i4++) {
            if (Geo.hit(geo.exR.get(i4), f, f2)) {
                return geo.exA.get(i4);
            }
        }
        if (geo.tabsOn()) {
            for (int i5 = 0; i5 < 3; i5++) {
                if (Geo.hit(geo.tabR[i5], f, f2)) {
                    return "tab:" + Geo.MODES[i5];
                }
            }
            return null;
        }
        return null;
    }

    @Override // android.view.View
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        return this.a11y;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.a11y.hover(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    final class A11ySrc implements VirtualA11y.Src {
        A11ySrc() {
        }

        int[] k(int i) {
            int i2;
            Geo geo = HomeView.this.h.g;
            if (i < geo.nq) {
                return new int[]{0, i};
            }
            int i3 = i - geo.nq;
            if (i3 < geo.nw) {
                i2 = 1;
            } else {
                i3 -= geo.nw;
                if (i3 >= geo.nd) {
                    int i4 = i3 - geo.nd;
                    if (i4 == 0) {
                        return new int[]{3, 0};
                    }
                    int i5 = i4 - 1;
                    if (geo.orb) {
                        if (i5 == 0) {
                            return new int[]{4, 0};
                        }
                        i5--;
                    }
                    return i5 < geo.exR.size() ? new int[]{6, i5} : new int[]{5, i5 - geo.exR.size()};
                }
                i2 = 2;
            }
            return new int[]{i2, i3};
        }

        @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
        public int count() {
            Geo geo = HomeView.this.h.g;
            return geo.nq + geo.nw + geo.nd + 1 + (geo.orb ? 1 : 0) + geo.exR.size() + (geo.tabsOn() ? 3 : 0);
        }

        @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
        public void bounds(int i, Rect rect) {
            Geo geo = HomeView.this.h.g;
            int[] k = k(i);
            Region region = k[0] == 0 ? geo.qsR[k[1]] : k[0] == 1 ? geo.whR[k[1]] : k[0] == 2 ? geo.dkR[k[1]] : k[0] == 3 ? geo.appsR : k[0] == 5 ? geo.tabR[k[1]] : k[0] == 6 ? geo.exR.get(k[1]) : null;
            if (k[0] == 4) {
                rect.set(Math.round(geo.ox + ((geo.cx - 60.0f) * geo.S)), Math.round(geo.oy + ((geo.cy - 60.0f) * geo.S)), Math.round(geo.ox + ((geo.cx + 60.0f) * geo.S)), Math.round(geo.oy + ((geo.cy + 32.0f) * geo.S)));
            } else if (region == null) {
                rect.setEmpty();
            } else {
                region.getBounds(HomeView.this.rb);
                rect.set(Math.round(geo.ox + ((HomeView.this.rb.left / 8.0f) * geo.S)), Math.round(geo.oy + ((HomeView.this.rb.top / 8.0f) * geo.S)), Math.round(geo.ox + ((HomeView.this.rb.right / 8.0f) * geo.S)), Math.round(geo.oy + ((HomeView.this.rb.bottom / 8.0f) * geo.S)));
            }
        }

        @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
        public String label(int i) {
            String str;
            int[] k = k(i);
            HomeData homeData = HomeView.this.h.d;
            switch (k[0]) {
                case 0:
                    String str2 = homeData.qs.get(k[1]).label;
                    if (k[1] < homeData.qsOn.length && homeData.qsOn[k[1]]) {
                        str = ", on";
                    }
                    return str2 + str + nw(homeData.qsCnt[k[1]]);
                case 1:
                    HomeData.Entry entry = homeData.wheel.get(k[1]);
                    if (HomeView.this.h.g.adv()) {
                        return entry.label + nw(homeData.wheelCnt[k[1]]);
                    }
                    if (!HomeView.this.h.brief) {
                        return entry.label;
                    }
                    return entry.label + ", " + entry.sub + (k[1] == HomeView.this.h.active ? ", " + Strings.t("db.w.sel") : "");
                case 2:
                    HomeData.Entry entry2 = homeData.dock.get(k[1]);
                    return ((!HomeView.this.h.brief || entry2.sub.isEmpty()) ? entry2.label : entry2.label + " " + entry2.sub) + nw(homeData.dockCnt[k[1]]);
                case 3:
                    return HomeView.this.h.appsA11y;
                case 4:
                    return "Ask " + homeData.name;
                case 5:
                default:
                    return Geo.MODES[k[1]].toUpperCase(Locale.ROOT) + " layout" + (Geo.MODES[k[1]].equals(HomeView.this.h.g.mode) ? ", selected" : "");
                case 6:
                    return HomeView.this.h.g.exL.get(k[1]);
            }
        }

        String nw(int i) {
            return i > 0 ? ", " + i + " new" : "";
        }

        String act(int i) {
            int[] k = k(i);
            switch (k[0]) {
                case 0:
                    return "q" + k[1];
                case 1:
                    return "w" + k[1];
                case 2:
                    return "d" + k[1];
                case 3:
                    return "apps";
                case 4:
                    return "ai";
                case 5:
                default:
                    return "tab:" + Geo.MODES[k[1]];
                case 6:
                    return HomeView.this.h.g.exA.get(k[1]);
            }
        }

        @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
        public boolean click(int i) {
            HomeView.this.host.go(act(i));
            return true;
        }

        @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
        public boolean hasLong(int i) {
            return k(i)[0] == 4;
        }

        @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
        public boolean longClick(int i) {
            if (k(i)[0] != 4) {
                return false;
            }
            HomeView.this.host.longOrb();
            return true;
        }

        @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
        public View[] real() {
            WidgetView widgetView = HomeView.this.pager.cur < HomeView.this.pager.slides.size() ? HomeView.this.pager.slides.get(HomeView.this.pager.cur) : null;
            return widgetView == null ? new View[]{HomeView.this.pager} : new View[]{widgetView, widgetView.companion};
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int i = this.un.it;
        int i2 = this.un.ir;
        int i3 = this.un.ib;
        int i4 = this.un.il;
        int i5 = this.h.g.cutLpx;
        int i6 = this.h.g.cutRpx;
        this.un.insets(windowInsets);
        Geo geo = this.h.g;
        this.h.g.cutRpx = -1;
        geo.cutLpx = -1;
        if (Build.VERSION.SDK_INT >= 28 && windowInsets.getDisplayCutout() != null) {
            for (Rect rect : windowInsets.getDisplayCutout().getBoundingRects()) {
                if (rect.top <= 0 && rect.width() < getWidth() * 0.6f && rect.bottom < getHeight() * 0.1f) {
                    this.h.g.cutLpx = rect.left;
                    this.h.g.cutRpx = rect.right;
                }
            }
        }
        if (i != this.un.it || i2 != this.un.ir || i3 != this.un.ib || i4 != this.un.il || i5 != this.h.g.cutLpx || i6 != this.h.g.cutRpx) {
            post(new HomeView$$ExternalSyntheticLambda0(this));
        }
        return super.onApplyWindowInsets(windowInsets);
    }
}
