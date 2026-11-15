package com.jarvis.assist.aosui.home;

import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.RenderEffect;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.SizeF;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.AwSize;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.MemPolicy;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.WidgetPager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class WidgetPager extends ViewGroup {
    static final int NEAR = 2;
    boolean anim;
    long animAt;
    int animMs;
    final HashMap<Integer, int[]> awSized;
    AwViews awViews;
    boolean blurDirty;
    Object blurNode;
    final int[] box;
    final Path br;
    final Card card;
    boolean claimed;
    int cur;
    String dbg;
    float[] dotW;
    boolean down;
    long downAt;
    float dx;
    float from;
    boolean greetIn;
    final HomeCtx h;
    boolean longFired;
    final Runnable longPress;
    final FrameClock.Handle loop;
    final Listener ls;
    final Paint p;
    float pos;
    final RectF rf;
    final Runnable shownNow;
    final ArrayList<WidgetView> slides;
    final int slop;
    final float[] slot;
    Txt src;
    String srcFit;
    String srcKey;
    float srcRoom;
    float to;
    VelocityTracker vt;
    float x0;
    float y0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Listener {
        void backdrop(Canvas canvas);

        void page(int i);

        void tap(String str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WidgetPager(Context context, final HomeCtx homeCtx, Listener listener) {
        super(context);
        this.slides = new ArrayList<>();
        this.p = new Paint(1);
        this.rf = new RectF();
        this.br = new Path();
        this.animMs = 450;
        this.dotW = new float[0];
        this.blurDirty = true;
        this.greetIn = true;
        this.slot = new float[4];
        this.awSized = new HashMap<>();
        this.box = new int[4];
        this.shownNow = new Runnable() { // from class: com.jarvis.assist.aosui.home.WidgetPager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                WidgetPager.this.shownNow();
            }
        };
        this.longPress = new Runnable() { // from class: com.jarvis.assist.aosui.home.WidgetPager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                WidgetPager.this.onLongPress();
            }
        };
        this.srcFit = "";
        this.srcRoom = -1.0f;
        this.h = homeCtx;
        this.ls = listener;
        setWillNotDraw(true);
        this.slop = ViewConfiguration.get(context).getScaledTouchSlop();
        setClipChildren(true);
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.jarvis.assist.aosui.home.WidgetPager.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), homeCtx.adv ? 0.0f : homeCtx.g.S * 2.0f);
            }
        });
        this.card = new Card(context);
        addView(this.card);
        this.loop = FrameClock.I.add(new FrameClock.Sub() { // from class: com.jarvis.assist.aosui.home.WidgetPager$$ExternalSyntheticLambda3
            @Override // com.jarvis.assist.aosui.core.FrameClock.Sub
            public final boolean frame(long j, float f) {
                return WidgetPager.this.frame(j, f);
            }
        }, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWidgets(ArrayList<String> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        this.greetIn = !this.h.adv || arrayList.isEmpty() || this.h.g.land;
        if (this.greetIn) {
            arrayList2.add("greeting");
        }
        arrayList2.addAll(arrayList);
        boolean z = arrayList2.size() == this.slides.size();
        for (int i2 = 0; z && i2 < arrayList2.size(); i2++) {
            z = this.slides.get(i2).id().equals(arrayList2.get(i2));
        }
        if (!z) {
            removeAllViews();
            addView(this.card);
            this.slides.clear();
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                WidgetView make = Widgets.make((String) it.next(), getContext(), this.h);
                if (make != null) {
                    final Listener listener = this.ls;
                    Objects.requireNonNull(listener);
                    make.go = new Consumer() { // from class: com.jarvis.assist.aosui.home.WidgetPager$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WidgetPager.Listener.this.tap((String) obj);
                        }
                    };
                    this.slides.add(make);
                    addView(make);
                    if (MemPolicy.keepPage(this.slides.size() - 1, this.cur, 2)) {
                        companion(make);
                    }
                }
            }
        }
        while (i < this.slides.size()) {
            WidgetView widgetView = this.slides.get(i);
            i++;
            widgetView.num = i;
        }
        this.dotW = new float[this.slides.size()];
        this.cur = Math.min(this.cur, this.slides.size() - 1);
        this.pos = this.cur;
        prepare();
    }

    void companion(WidgetView widgetView) {
        if (!(widgetView instanceof PhoneWidget)) {
            if (widgetView.companion != null) {
                View view = widgetView.companion;
                if (view.getParent() != this) {
                    if (view.getParent() instanceof ViewGroup) {
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
                    addView(view);
                    return;
                }
                return;
            }
            return;
        }
        if (this.awViews == null) {
            return;
        }
        PhoneWidget phoneWidget = (PhoneWidget) widgetView;
        View view2 = this.awViews.view(phoneWidget.awId);
        if (view2 == null) {
            return;
        }
        if (view2.getParent() instanceof ViewGroup) {
            ((ViewGroup) view2.getParent()).removeView(view2);
        }
        phoneWidget.companion = view2;
        if (phoneWidget.W > 0.0f) {
            slotPx(phoneWidget, this.box);
            sizeAw(phoneWidget, view2, this.box[2] - this.box[0], this.box[3] - this.box[1]);
        }
        addView(view2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void near(int i) {
        boolean z = false;
        for (int i2 = 0; i2 < this.slides.size(); i2++) {
            if (this.slides.get(i2) instanceof PhoneWidget) {
                PhoneWidget phoneWidget = (PhoneWidget) this.slides.get(i2);
                if (MemPolicy.keepPage(i2, this.cur, i)) {
                    if (phoneWidget.companion == null) {
                        companion(phoneWidget);
                        z = phoneWidget.companion != null;
                    }
                } else if (phoneWidget.companion != null) {
                    removeView(phoneWidget.companion);
                    phoneWidget.companion = null;
                    this.awSized.remove(Integer.valueOf(phoneWidget.awId));
                    if (this.awViews != null) {
                        this.awViews.drop(phoneWidget.awId);
                    }
                    z = true;
                }
            }
        }
        if (z) {
            requestLayout();
            place();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface AwViews {
        View view(int i);

        default void drop(int i) {
        }

        default void shown(int i) {
        }
    }

    void sizeAw(PhoneWidget phoneWidget, View view, int i, int i2) {
        if (!(view instanceof AppWidgetHostView) || !AwSize.usable(i, i2)) {
            return;
        }
        int[] dp = AwSize.dp(i, i2, getResources().getDisplayMetrics().density);
        if (AwSize.changed(this.awSized.get(Integer.valueOf(phoneWidget.awId)), dp)) {
            this.awSized.put(Integer.valueOf(phoneWidget.awId), dp);
            AppWidgetHostView appWidgetHostView = (AppWidgetHostView) view;
            Bundle bundle = new Bundle();
            bundle.putInt("appWidgetMinWidth", dp[0]);
            bundle.putInt("appWidgetMinHeight", dp[1]);
            bundle.putInt("appWidgetMaxWidth", dp[2]);
            bundle.putInt("appWidgetMaxHeight", dp[3]);
            try {
                if (Build.VERSION.SDK_INT >= 31) {
                    appWidgetHostView.updateAppWidgetSize(bundle, Collections.singletonList(new SizeF(dp[0], dp[1])));
                } else {
                    appWidgetHostView.updateAppWidgetSize(bundle, dp[0], dp[1], dp[2], dp[3]);
                }
            } catch (Throwable th) {
            }
            if (Backend.debug()) {
                Log.d("aOS", "aw " + phoneWidget.awId + " size " + dp[0] + "x" + dp[1] + "dp (" + i + "x" + i2 + "px)");
            }
        }
    }

    void slotPx(WidgetView widgetView, int[] iArr) {
        widgetView.slot(this.slot);
        float f = this.h.g.S;
        iArr[0] = Math.round(this.slot[0] * f);
        iArr[1] = Math.round(this.slot[1] * f);
        iArr[2] = Math.round(this.slot[2] * f);
        iArr[3] = Math.round(this.slot[3] * f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepare() {
        this.dbg = Backend.debug() ? Store.get("aos.dbg.pager") : null;
        this.src = new Txt(Fonts.MONO, Math.max(8.0f, this.h.zoom * 7.0f), this.h.zoom * 7.0f * 0.1f);
        this.srcKey = null;
        long uptimeMillis = SystemClock.uptimeMillis();
        int i = 0;
        while (i < this.slides.size()) {
            WidgetView widgetView = this.slides.get(i);
            widgetView.styles();
            widgetView.setSize(this.h.g.pw, this.h.g.ph);
            widgetView.bind();
            widgetView.setCurrent(i == this.cur, uptimeMillis - 2000);
            widgetView.invalidate();
            this.dotW[i] = i == this.cur ? 12.0f : 5.0f;
            if ((widgetView instanceof PhoneWidget) && widgetView.companion != null) {
                slotPx(widgetView, this.box);
                sizeAw((PhoneWidget) widgetView, widgetView.companion, this.box[2] - this.box[0], this.box[3] - this.box[1]);
            }
            i++;
        }
        requestLayout();
        place();
        backdropChanged();
        invalidateOutline();
        invalidate();
        this.loop.kick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trimSlides() {
        Iterator<WidgetView> it = this.slides.iterator();
        while (it.hasNext()) {
            it.next().trim();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bindAll() {
        Iterator<WidgetView> it = this.slides.iterator();
        while (it.hasNext()) {
            WidgetView next = it.next();
            next.bind();
            next.invalidate();
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int count() {
        return this.slides.size();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
        this.card.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            if (getChildAt(i3) instanceof WidgetView) {
                getChildAt(i3).measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = i4 - i2;
        this.card.layout(0, 0, i5, i6);
        for (int i7 = 0; i7 < getChildCount(); i7++) {
            View childAt = getChildAt(i7);
            if (childAt instanceof WidgetView) {
                childAt.layout(0, 0, i5, i6);
            }
        }
        float f = this.h.g.S;
        Iterator<WidgetView> it = this.slides.iterator();
        while (it.hasNext()) {
            WidgetView next = it.next();
            if (next.companion != null) {
                View view = next.companion;
                next.slot(this.slot);
                int round = Math.round(this.slot[0] * f);
                int round2 = Math.round(this.slot[1] * f);
                int round3 = Math.round(this.slot[2] * f);
                int round4 = Math.round(this.slot[3] * f);
                boolean z2 = next instanceof PhoneWidget;
                if (z2) {
                    sizeAw((PhoneWidget) next, view, round3 - round, round4 - round2);
                }
                if (!z2 || view.getWidth() != round3 - round || view.getHeight() != round4 - round2 || view.isLayoutRequested()) {
                    view.measure(View.MeasureSpec.makeMeasureSpec(round3 - round, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(0, round4 - round2), 1073741824));
                }
                view.layout(round, round2, round3, round4);
            }
        }
        place();
    }

    void place() {
        float width = getWidth();
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int i = 0; i < this.slides.size(); i++) {
            WidgetView widgetView = this.slides.get(i);
            widgetView.setTranslationX((i - this.pos) * width);
            float f = (float) (uptimeMillis - widgetView.changedAt);
            float min = Math.min(1.0f, f / 450.0f);
            float interpolation = WidgetView.EASE.getInterpolation(Math.min(1.0f, f / 600.0f));
            float interpolation2 = widgetView.current ? (WidgetView.CSS_EASE.getInterpolation(min) * 0.75f) + 0.25f : 1.0f - (WidgetView.CSS_EASE.getInterpolation(min) * 0.75f);
            float f2 = interpolation * 0.04f;
            float f3 = widgetView.current ? f2 + 0.96f : 1.0f - f2;
            if (Perf.k().calm) {
                float f4 = widgetView.current ? 1.0f : 0.25f;
                f3 = widgetView.current ? 1.0f : 0.96f;
                interpolation2 = f4;
            }
            boolean slideDrawn = MemPolicy.slideDrawn(i, this.pos);
            widgetView.setAlpha(slideDrawn ? interpolation2 : 0.0f);
            widgetView.setScaleX(f3);
            widgetView.setScaleY(f3);
            int i2 = slideDrawn ? 0 : 4;
            if (widgetView.getVisibility() != i2) {
                widgetView.setVisibility(i2);
            }
            if (widgetView.companion != null) {
                View view = widgetView.companion;
                view.setTranslationX(widgetView.getTranslationX());
                view.setAlpha(interpolation2);
                view.setScaleX(f3);
                view.setScaleY(f3);
                view.setPivotX((width / 2.0f) - view.getLeft());
                view.setPivotY((getHeight() / 2.0f) - view.getTop());
                view.setVisibility(slideDrawn ? 0 : 4);
            }
            if (!slideDrawn && widgetView.getLayerType() != 0) {
                widgetView.setLayerType(0, null);
            }
        }
    }

    void setSlides(List<WidgetView> list) {
        removeAllViews();
        addView(this.card);
        this.slides.clear();
        for (WidgetView widgetView : list) {
            this.slides.add(widgetView);
            addView(widgetView);
            companion(widgetView);
        }
        this.dotW = new float[this.slides.size()];
        this.cur = Math.max(0, Math.min(this.cur, this.slides.size() - 1));
        this.pos = this.cur;
        prepare();
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void go(int i, boolean z) {
        int max = Math.max(0, Math.min(this.slides.size() - 1, i));
        boolean z2 = max != this.cur;
        this.cur = max;
        long uptimeMillis = SystemClock.uptimeMillis();
        int i2 = 0;
        while (i2 < this.slides.size()) {
            this.slides.get(i2).setCurrent(i2 == this.cur, uptimeMillis);
            i2++;
        }
        int i3 = Perf.k().calm ? 0 : Perf.k().carMs;
        if (!z || i3 == 0) {
            this.pos = this.cur;
            this.anim = false;
        } else {
            this.from = this.pos;
            this.to = this.cur;
            this.animAt = uptimeMillis;
            this.animMs = i3;
            this.anim = true;
        }
        place();
        invalidate();
        this.loop.kick();
        if (z2) {
            this.ls.page(this.cur);
            near(2);
            shownLater();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void shownLater() {
        removeCallbacks(this.shownNow);
        postDelayed(this.shownNow, 700L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void shownNow() {
        if (this.cur >= this.slides.size() || !(this.slides.get(this.cur) instanceof PhoneWidget) || this.slides.get(this.cur).companion == null || this.awViews == null) {
            return;
        }
        this.awViews.shown(((PhoneWidget) this.slides.get(this.cur)).awId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean frame(long j, float f) {
        boolean z;
        long uptimeMillis = SystemClock.uptimeMillis();
        if (!this.anim || this.down) {
            z = false;
        } else {
            float min = Math.min(1.0f, ((float) (uptimeMillis - this.animAt)) / this.animMs);
            this.pos = this.from + ((this.to - this.from) * WidgetView.EASE.getInterpolation(min));
            if (min >= 1.0f) {
                this.anim = false;
                this.pos = this.to;
            }
            z = true;
        }
        Iterator<WidgetView> it = this.slides.iterator();
        while (it.hasNext()) {
            WidgetView next = it.next();
            if (next.animating(uptimeMillis)) {
                next.invalidate();
                z = true;
            }
            if (uptimeMillis - next.changedAt < 650) {
                z = true;
            }
        }
        int i = 0;
        while (i < this.dotW.length) {
            float f2 = i == this.cur ? 12.0f : 5.0f;
            if (Math.abs(this.dotW[i] - f2) > 0.05f) {
                float[] fArr = this.dotW;
                fArr[i] = fArr[i] + ((f2 - this.dotW[i]) * Math.min(1.0f, f / 90.0f));
                z = true;
            } else {
                this.dotW[i] = f2;
            }
            i++;
        }
        place();
        invalidate();
        return z || this.down;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 0:
                onTouchEvent(motionEvent);
                return false;
            case 1:
            case 3:
                this.down = false;
                if (this.vt != null) {
                    this.vt.recycle();
                    this.vt = null;
                }
                return false;
            case 2:
                if (!this.down) {
                    return false;
                }
                if (this.vt != null) {
                    this.vt.addMovement(motionEvent);
                }
                float x = motionEvent.getX() - this.x0;
                return Math.abs(x) > this.h.g.S * 8.0f && Math.abs(x) > Math.abs(motionEvent.getY() - this.y0) * 1.5f;
            default:
                return false;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float max = Math.max(1, getWidth());
        int i = 0;
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.down = true;
                this.claimed = false;
                this.longFired = false;
                this.x0 = motionEvent.getX();
                this.y0 = motionEvent.getY();
                this.dx = 0.0f;
                this.downAt = motionEvent.getEventTime();
                if (this.vt != null) {
                    this.vt.recycle();
                }
                this.vt = VelocityTracker.obtain();
                this.vt.addMovement(motionEvent);
                removeCallbacks(this.longPress);
                postDelayed(this.longPress, 520L);
                return true;
            case 1:
            case 3:
                removeCallbacks(this.longPress);
                if (!this.down) {
                    return false;
                }
                this.down = false;
                if (this.longFired) {
                    if (this.vt != null) {
                        this.vt.recycle();
                        this.vt = null;
                    }
                    return true;
                }
                this.vt.addMovement(motionEvent);
                this.vt.computeCurrentVelocity(1000);
                float xVelocity = this.vt.getXVelocity() / this.h.g.S;
                this.vt.recycle();
                this.vt = null;
                if (!this.claimed) {
                    if (motionEvent.getActionMasked() == 1 && Math.hypot(motionEvent.getX() - this.x0, motionEvent.getY() - this.y0) < this.slop * 1.5f && motionEvent.getEventTime() - this.downAt < 600) {
                        tapAt(motionEvent.getX(), motionEvent.getY());
                    }
                    return true;
                }
                if (this.dx < this.h.g.S * (-36.0f) || xVelocity < -500.0f) {
                    i = 1;
                } else if (this.dx > this.h.g.S * 36.0f || xVelocity > 500.0f) {
                    i = -1;
                }
                go(this.cur + i, true);
                return true;
            case 2:
                if (!this.down) {
                    return false;
                }
                this.vt.addMovement(motionEvent);
                float x = motionEvent.getX() - this.x0;
                float y = motionEvent.getY() - this.y0;
                if (Math.hypot(x, y) > this.slop) {
                    removeCallbacks(this.longPress);
                }
                if (!this.claimed) {
                    if (Math.abs(x) <= this.h.g.S * 8.0f || Math.abs(x) <= Math.abs(y) * 1.5f) {
                        return true;
                    }
                    this.claimed = true;
                    this.anim = false;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                this.dx = x;
                if ((this.cur == 0 && this.dx > 0.0f) || (this.cur == this.slides.size() - 1 && this.dx < 0.0f)) {
                    i = 1;
                }
                float f = this.cur;
                float f2 = this.dx;
                if (i != 0) {
                    f2 *= 0.3f;
                }
                this.pos = f - (f2 / max);
                place();
                this.loop.kick();
                return true;
            default:
                return super.onTouchEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLongPress() {
        String longTap;
        if (!this.down || this.claimed || this.cur >= this.slides.size() || (longTap = this.slides.get(this.cur).longTap(this.x0 / this.h.g.S, this.y0 / this.h.g.S)) == null) {
            return;
        }
        this.longFired = true;
        Feedback.fx(this, 3);
        this.ls.tap(longTap);
    }

    void tapAt(float f, float f2) {
        float f3 = this.h.g.S;
        float f4 = f / f3;
        float f5 = f2 / f3;
        float f6 = this.h.g.pw;
        float f7 = this.h.g.ph;
        float f8 = f6 - 12.0f;
        for (int length = this.dotW.length - 1; length >= 0; length--) {
            float f9 = f8 - this.dotW[length];
            if (f4 >= f9 - 3.0f && f4 <= f8 + 3.0f && f5 >= ((f7 - 5.0f) - 5.0f) - 19.0f && f5 <= f7) {
                go(length, true);
                return;
            }
            f8 = f9 - 5.0f;
        }
        if (this.cur < this.slides.size()) {
            String tap = this.slides.get(this.cur).tap(f4, f5);
            Listener listener = this.ls;
            if (tap == null) {
                tap = "x:detail:" + this.slides.get(this.cur).id();
            }
            listener.tap(tap);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Card extends View {
        Card(Context context) {
            super(context);
            setLayerType(2, null);
            setImportantForAccessibility(2);
        }

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Theme theme = WidgetPager.this.h.th;
            float f = WidgetPager.this.h.g.S;
            float width = getWidth();
            float height = getHeight();
            if (WidgetPager.this.h.adv) {
                WidgetPager.this.advCard(canvas, theme, f, width, height);
                return;
            }
            boolean equals = "solid".equals(WidgetPager.this.dbg);
            boolean equals2 = "nobackdrop".equals(WidgetPager.this.dbg);
            if (Build.VERSION.SDK_INT >= 31 && theme.blurPx > 0.0f && canvas.isHardwareAccelerated() && !equals && !equals2) {
                WidgetPager.this.drawBlur(canvas, width, height);
            }
            WidgetPager.this.p.setShader(null);
            WidgetPager.this.p.setStyle(Paint.Style.FILL);
            WidgetPager.this.p.setColor(equals ? theme.light ? -1 : -16777216 : equals2 ? 0 : theme.CARD);
            WidgetPager.this.rf.set(0.0f, 0.0f, width, height);
            float f2 = f * 2.0f;
            canvas.drawRoundRect(WidgetPager.this.rf, f2, f2, WidgetPager.this.p);
            WidgetPager.this.p.setStyle(Paint.Style.STROKE);
            WidgetPager.this.p.setStrokeWidth(f);
            WidgetPager.this.p.setColor(theme.LINE);
            float f3 = f / 2.0f;
            WidgetPager.this.rf.set(f3, f3, width - f3, height - f3);
            float f4 = 1.5f * f;
            canvas.drawRoundRect(WidgetPager.this.rf, f4, f4, WidgetPager.this.p);
            WidgetPager.this.p.setStrokeWidth(f2);
            WidgetPager.this.p.setColor(theme.RED);
            canvas.drawLine(f3, f, f * 14.0f, f, WidgetPager.this.p);
            WidgetPager.this.p.setStrokeWidth(f);
            WidgetPager.this.p.setColor(theme.LINE2);
            float f5 = f * 8.0f;
            canvas.drawLine(width - (16.0f * f), f5, width - (f * 9.0f), f5, WidgetPager.this.p);
        }
    }

    void advCard(Canvas canvas, Theme theme, float f, float f2, float f3) {
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(Theme.a(theme.CARD, 0.55f));
        canvas.drawRect(0.0f, 0.0f, f2, f3, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.4f * f);
        this.p.setColor(-2235414);
        float f4 = 8.0f * f;
        float f5 = f * 0.7f;
        float f6 = f5 + f4;
        canvas.drawLine(f5, f5, f6, f5, this.p);
        canvas.drawLine(f5, f5, f5, f6, this.p);
        float f7 = f2 - f5;
        float f8 = f7 - f4;
        canvas.drawLine(f7, f5, f8, f5, this.p);
        canvas.drawLine(f7, f5, f7, f6, this.p);
        float f9 = f3 - f5;
        canvas.drawLine(f5, f9, f6, f9, this.p);
        float f10 = f9 - f4;
        canvas.drawLine(f5, f9, f5, f10, this.p);
        canvas.drawLine(f7, f9, f8, f9, this.p);
        canvas.drawLine(f7, f9, f7, f10, this.p);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dropLayers() {
        if (Build.VERSION.SDK_INT >= 31 && this.blurNode != null) {
            ((RenderNode) this.blurNode).discardDisplayList();
        }
        this.blurNode = null;
        this.blurDirty = true;
        this.card.setLayerType(0, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreLayers() {
        if (this.card.getLayerType() != 2) {
            this.card.setLayerType(2, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void backdropChanged() {
        this.blurDirty = true;
        this.card.invalidate();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.src == null) {
            return;
        }
        Theme theme = this.h.th;
        float f = this.h.g.S;
        int save = canvas.save();
        canvas.scale(f, f);
        float f2 = this.h.g.pw;
        float f3 = this.h.g.ph;
        float f4 = 12.0f;
        float f5 = f2 - 12.0f;
        float f6 = 5.0f;
        float f7 = (f3 - 5.0f) - 2.5f;
        this.p.setShader(null);
        int length = this.dotW.length - 1;
        while (length >= 0) {
            float f8 = this.dotW[length];
            float f9 = f4;
            float f10 = f6;
            this.rf.set((f5 - f8) + 0.5f, f7 - 2.0f, f5 - 0.5f, f7 + 2.0f);
            boolean z = length == this.cur;
            float max = Math.max(0.0f, Math.min(1.0f, (f8 - f10) / 7.0f));
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(Theme.a(theme.BLUE, max));
            canvas.drawRoundRect(this.rf, 2.5f, 2.5f, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.0f);
            this.p.setColor((z || max > 0.5f) ? theme.BLUE : theme.DIM);
            canvas.drawRoundRect(this.rf, 2.5f, 2.5f, this.p);
            f5 -= f8 + f10;
            length--;
            f4 = f9;
            f6 = f10;
        }
        float f11 = f4;
        String source = this.cur < this.slides.size() ? this.slides.get(this.cur).source() : "";
        float f12 = (f5 - 14.0f) - 3.0f;
        if (!source.equals(this.srcKey) || Math.abs(f12 - this.srcRoom) > 0.5f) {
            this.srcKey = source;
            this.srcRoom = f12;
            this.srcFit = f12 > f11 ? WidgetView.fitText(this.src, "FROM " + source, f12) : "";
        }
        if (!this.srcFit.isEmpty()) {
            this.src.draw(canvas, this.srcFit, 14.0f, (f3 - 4.0f) - this.src.desc, theme.DIM);
        }
        canvas.restoreToCount(save);
    }

    void bracket(Canvas canvas, float f, float f2, boolean z) {
        this.br.rewind();
        if (z) {
            float f3 = f + 11.5f;
            this.br.moveTo(f3, f2);
            this.br.lineTo(f3, f2 + 6.5f);
            float f4 = f2 + 11.5f;
            this.br.quadTo(f3, f4, 6.5f + f, f4);
            this.br.lineTo(f, f4);
        } else {
            float f5 = f + 0.5f;
            this.br.moveTo(f5, f2 + 12.0f);
            this.br.lineTo(f5, f2 + 5.5f);
            float f6 = f2 + 0.5f;
            this.br.quadTo(f5, f6, 5.5f + f, f6);
            this.br.lineTo(f + 12.0f, f6);
        }
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(this.h.th.LINE2);
        canvas.drawPath(this.br, this.p);
    }

    void drawBlur(Canvas canvas, float f, float f2) {
        RenderNode renderNode = (RenderNode) this.blurNode;
        if (renderNode == null) {
            renderNode = new RenderNode("aos-panel-backdrop");
            this.blurNode = renderNode;
            this.blurDirty = true;
        }
        if (this.blurDirty || renderNode.getWidth() != ((int) f) || renderNode.getHeight() != ((int) f2)) {
            renderNode.setPosition(0, 0, (int) f, (int) f2);
            float max = Math.max(1.0f, (this.h.th.blurPx - 0.5f) / 0.57735f);
            renderNode.setRenderEffect(RenderEffect.createBlurEffect(max, max, Shader.TileMode.CLAMP));
            try {
                this.ls.backdrop(renderNode.beginRecording());
                renderNode.endRecording();
                this.blurDirty = false;
            } catch (Throwable th) {
                renderNode.endRecording();
                throw th;
            }
        }
        canvas.drawRenderNode(renderNode);
    }
}
