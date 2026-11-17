package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.HomePages;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.PageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class PageView extends ViewGroup {
    final ArrayList<String> gridApps;
    final ArrayList<RectF> gridRects;
    final HomeCtx h;
    final Host host;
    final Runnable inval;
    final Txt lab;
    final HashMap<String, String> labels;
    boolean longFired;
    final Runnable longPress;
    final Paint p;
    final HomePages.Page page;
    final RectF rf;
    long t0;
    final ArrayList<RectF> wRect;
    final ArrayList<WidgetView> widgets;
    float x0;
    float y0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Host {
        void edit();

        void swipe(int i);

        void tap(String str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PageView(Context context, HomeCtx homeCtx, HomePages.Page page, final Host host) {
        super(context);
        this.widgets = new ArrayList<>();
        this.wRect = new ArrayList<>();
        this.gridRects = new ArrayList<>();
        this.gridApps = new ArrayList<>();
        this.p = new Paint(1);
        this.rf = new RectF();
        this.labels = new HashMap<>();
        this.inval = new Runnable() { // from class: com.jarvis.assist.aosui.home.PageView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PageView.this.invalidate();
            }
        };
        this.longPress = new Runnable() { // from class: com.jarvis.assist.aosui.home.PageView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PageView.this.onLong();
            }
        };
        this.h = homeCtx;
        this.page = page;
        this.host = host;
        setWillNotDraw(false);
        this.lab = new Txt(Fonts.MONO, homeCtx.L(7.0f, 9.0f), homeCtx.L(7.0f, 9.0f) * 0.12f).align(Paint.Align.CENTER);
        String appsCached = homeCtx.d.be.appsCached();
        if (appsCached != null) {
            try {
                JSONArray jSONArray = new JSONArray(appsCached);
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        this.labels.put(optJSONObject.optString("pkg") + "/" + optJSONObject.optString("cls"), optJSONObject.optString("label"));
                    }
                }
            } catch (Exception e) {
            }
        }
        Iterator<HomePages.Block> it = page.blocks.iterator();
        while (it.hasNext()) {
            HomePages.Block next = it.next();
            if (!"grid".equals(next.type)) {
                Iterator<String> it2 = next.items.iterator();
                while (it2.hasNext()) {
                    WidgetView make = Widgets.make(it2.next(), context, homeCtx);
                    if (make != null) {
                        Objects.requireNonNull(host);
                        make.go = new Consumer() { // from class: com.jarvis.assist.aosui.home.PageView$$ExternalSyntheticLambda2
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                host.tap((String) obj);
                            }
                        };
                        this.widgets.add(make);
                        addView(make);
                    }
                }
            }
        }
        setContentDescription(page.name);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        float f2;
        float f3;
        int i5;
        float f4;
        float max;
        float f5 = this.h.g.S;
        float f6 = (i3 - i) / f5;
        float f7 = (i4 - i2) / f5;
        this.gridRects.clear();
        this.gridApps.clear();
        this.wRect.clear();
        if (this.page.blocks.size() == 0) {
            return;
        }
        Iterator<HomePages.Block> it = this.page.blocks.iterator();
        int i6 = 0;
        float f8 = 0.0f;
        while (true) {
            f = 0.9f;
            f2 = 4.0f;
            f3 = 60.0f;
            i5 = 1;
            f4 = 8.0f;
            if (!it.hasNext()) {
                break;
            }
            if ("grid".equals(it.next().type)) {
                f8 += (HomePages.grid(it.next().items.size())[1] * Math.min(60.0f, (f6 / 4.0f) * 0.9f)) + 8.0f;
            } else {
                i6++;
            }
        }
        if (i6 == 0) {
            max = 0.0f;
        } else {
            float f9 = i6;
            max = Math.max(60.0f, ((f7 - f8) - (8.0f * f9)) / f9);
        }
        Iterator<HomePages.Block> it2 = this.page.blocks.iterator();
        float f10 = 0.0f;
        int i7 = 0;
        while (it2.hasNext()) {
            HomePages.Block next = it2.next();
            if ("grid".equals(next.type)) {
                int[] grid = HomePages.grid(next.items.size());
                float f11 = f6 / f2;
                float min = Math.min(f3, f11 * f);
                int i8 = 0;
                while (i8 < next.items.size()) {
                    float f12 = f4;
                    float f13 = f5;
                    this.gridRects.add(new RectF((i8 % 4) * f11, ((i8 / 4) * min) + f10, (r15 + 1) * f11, ((r1 + 1) * min) + f10));
                    this.gridApps.add(next.items.get(i8));
                    i8++;
                    f4 = f12;
                    i5 = i5;
                    f5 = f13;
                    f6 = f6;
                }
                f10 += (grid[i5] * min) + f4;
                f5 = f5;
                f = 0.9f;
                f2 = 4.0f;
                f3 = 60.0f;
            } else {
                float f14 = f5;
                float f15 = f6;
                int i9 = i5;
                float f16 = f4;
                "big".equals(next.type);
                int i10 = "half".equals(next.type) ? 2 : i9;
                int i11 = 0;
                while (i11 < next.items.size() && i11 < i10 && i7 < this.widgets.size()) {
                    float f17 = (f15 - ((i10 - 1) * f16)) / i10;
                    float f18 = i11 * (f17 + f16);
                    WidgetView widgetView = this.widgets.get(i7);
                    widgetView.styles();
                    widgetView.setSize(f17, max);
                    widgetView.bind();
                    widgetView.setCurrent(i9, SystemClock.uptimeMillis() - 2000);
                    float f19 = f17 + f18;
                    float f20 = f10 + max;
                    widgetView.layout(Math.round(f18 * f14), Math.round(f10 * f14), Math.round(f19 * f14), Math.round(f20 * f14));
                    this.wRect.add(new RectF(f18, f10, f19, f20));
                    i11++;
                    i7++;
                    i10 = i10;
                    i9 = 1;
                }
                f10 += max + f16;
                f4 = f16;
                f5 = f14;
                f6 = f15;
                f = 0.9f;
                f2 = 4.0f;
                f3 = 60.0f;
                i5 = 1;
            }
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        Canvas canvas2;
        float f;
        int i2;
        String substring;
        Canvas canvas3 = canvas;
        Theme theme = this.h.th;
        float f2 = this.h.g.S;
        canvas3.drawColor(theme.BG);
        int save = canvas3.save();
        canvas3.scale(f2, f2);
        Iterator<RectF> it = this.wRect.iterator();
        while (true) {
            i = 0;
            if (!it.hasNext()) {
                break;
            }
            RectF next = it.next();
            this.p.setShader(null);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(Theme.a(theme.CARD, 0.55f));
            canvas3.drawRect(next, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(1.2f);
            this.p.setColor(-2235414);
            while (i < 4) {
                int i3 = i % 2;
                float f3 = i3 == 0 ? next.left : next.right;
                float f4 = i < 2 ? next.top : next.bottom;
                float f5 = 8.0f;
                float f6 = i3 == 0 ? 8.0f : -8.0f;
                if (i >= 2) {
                    f5 = -8.0f;
                }
                float f7 = f3;
                float f8 = f4;
                canvas3.drawLine(f7, f8, f3 + f6, f4, this.p);
                canvas3 = canvas;
                canvas3.drawLine(f7, f8, f7, f8 + f5, this.p);
                i++;
            }
        }
        int i4 = 0;
        while (i4 < this.gridRects.size()) {
            RectF rectF = this.gridRects.get(i4);
            String str = this.gridApps.get(i4);
            int indexOf = str.indexOf(47);
            float min = Math.min(rectF.width(), rectF.height()) * 0.5f;
            float centerX = rectF.centerX();
            float height = rectF.top + (rectF.height() * 0.42f);
            Bitmap bitmap = IconCache.get(getContext()).get(str.substring(i, indexOf), str.substring(indexOf + 1), Math.round(min * f2), this.inval);
            if (bitmap != null) {
                float f9 = min / 2.0f;
                f = f2;
                this.rf.set(centerX - f9, height - f9, centerX + f9, height + f9);
                canvas3.drawBitmap(bitmap, (Rect) null, this.rf, this.p);
            } else {
                f = f2;
            }
            if (this.labels.containsKey(str)) {
                substring = this.labels.get(str);
                i2 = 0;
            } else {
                i2 = 0;
                substring = str.substring(0, indexOf);
            }
            this.lab.draw(canvas, WidgetView.fitText(this.lab, substring.toUpperCase(Locale.ROOT), rectF.width() - 4.0f), centerX, rectF.bottom - 4.0f, theme.INK);
            i4++;
            canvas3 = canvas;
            i = i2;
            f2 = f;
        }
        float f10 = f2;
        if (this.page.blocks.isEmpty()) {
            this.lab.draw(canvas, Strings.t("pg.empty"), (getWidth() / f10) / 2.0f, (getHeight() / f10) / 2.0f, theme.DIM);
            canvas2 = canvas;
        } else {
            canvas2 = canvas;
        }
        canvas2.restoreToCount(save);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLong() {
        this.longFired = true;
        this.host.edit();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.x0 = motionEvent.getX();
            this.y0 = motionEvent.getY();
            this.t0 = motionEvent.getEventTime();
        }
        return motionEvent.getActionMasked() == 2 && Math.abs(motionEvent.getX() - this.x0) > getResources().getDisplayMetrics().density * 24.0f && Math.abs(motionEvent.getX() - this.x0) > Math.abs(motionEvent.getY() - this.y0) * 1.5f;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f = this.h.g.S;
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.x0 = motionEvent.getX();
                this.y0 = motionEvent.getY();
                this.t0 = motionEvent.getEventTime();
                this.longFired = false;
                postDelayed(this.longPress, 560L);
                return true;
            case 1:
                removeCallbacks(this.longPress);
                if (this.longFired) {
                    return true;
                }
                float x = motionEvent.getX() - this.x0;
                float y = motionEvent.getY() - this.y0;
                if (Math.abs(x) > getResources().getDisplayMetrics().density * 60.0f && Math.abs(x) > Math.abs(y) * 1.5f) {
                    this.host.swipe(x < 0.0f ? 1 : -1);
                    return true;
                }
                if (Math.hypot(x, y) < 20.0d) {
                    float x2 = motionEvent.getX() / f;
                    float y2 = motionEvent.getY() / f;
                    for (int i = 0; i < this.gridRects.size(); i++) {
                        if (this.gridRects.get(i).contains(x2, y2)) {
                            this.host.tap("g:" + this.gridApps.get(i));
                            return true;
                        }
                    }
                    for (int i2 = 0; i2 < this.wRect.size() && i2 < this.widgets.size(); i2++) {
                        RectF rectF = this.wRect.get(i2);
                        if (rectF.contains(x2, y2)) {
                            String tap = this.widgets.get(i2).tap(x2 - rectF.left, y2 - rectF.top);
                            Host host = this.host;
                            if (tap == null) {
                                tap = "x:detail:" + this.widgets.get(i2).id();
                            }
                            host.tap(tap);
                            return true;
                        }
                    }
                }
                return true;
            case 2:
                if (Math.hypot(motionEvent.getX() - this.x0, motionEvent.getY() - this.y0) > 16.0d) {
                    removeCallbacks(this.longPress);
                }
                return true;
            case 3:
                removeCallbacks(this.longPress);
                return true;
            default:
                return super.onTouchEvent(motionEvent);
        }
    }
}
