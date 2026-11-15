package com.jarvis.assist.aosui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.AoaPages;
import com.jarvis.assist.aosui.core.AoaTransit;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.AoaPage;
import com.jarvis.assist.aosui.home.AppsDrawer;
import com.jarvis.assist.aosui.ui.Kit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public abstract class AoaPage extends FrameLayout {
    static final int DIM = -9399652;
    static final Handler MAIN = new Handler(Looper.getMainLooper());
    static final int MID = -6902604;
    static final int O = -34278;
    static final int T = -11624262;
    static final int W = -1511182;
    static Hooks hooks;
    final Backend be;
    final FrameLayout body;
    boolean closing;
    final LinearLayout col;
    final HomeData d;
    final float dn;
    final HomeCtx h;
    final TextView hDate;
    final TextView hTime;
    final TextView hWeek;
    final String kind;
    final TextView sub;
    boolean swiping;
    float sx;
    float sy;
    String tab;
    final LinearLayout tabs;
    final View title;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Hooks {
        void contract();

        void expand();

        float[] orb();
    }

    abstract void showTab(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int ln(float f) {
        return Theme.rgba(192, 214, 220, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AoaPage(Context context, Backend backend, HomeCtx homeCtx, HomeData homeData, String str, String str2) {
        super(context);
        this.tab = "";
        this.be = backend;
        this.h = homeCtx;
        this.d = homeData;
        this.kind = str;
        this.dn = context.getResources().getDisplayMetrics().density;
        setBackground(new AppsDrawer.AoaBg(this.dn));
        setClickable(true);
        setFocusable(true);
        VirtualA11y.pane(this, str2);
        this.col = new LinearLayout(context);
        this.col.setOrientation(1);
        addView(this.col, new FrameLayout.LayoutParams(-1, -1));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(px(4.0f), px(6.0f), px(4.0f), px(10.0f));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        this.hTime = mono("", 13.0f, 0.14f, W);
        linearLayout2.addView(this.hTime);
        this.hDate = mono("", 6.5f, 0.14f, MID);
        linearLayout2.addView(this.hDate, lp(-2, -2, 3.0f));
        this.hWeek = mono("", 6.5f, 0.14f, MID);
        linearLayout2.addView(this.hWeek, lp(-2, -2, 1.0f));
        TextView mono = mono("‹ HOME", 7.5f, 0.16f, -11624262);
        mono.setPadding(0, px(6.0f), px(16.0f), px(6.0f));
        mono.setClickable(true);
        mono.setContentDescription("Back to home");
        mono.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AoaPage.this.m226lambda$new$0$comaugmaosohomeAoaPage(view);
            }
        });
        linearLayout2.addView(mono);
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(0, -2, 1.0f));
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setOrientation(1);
        linearLayout3.setGravity(8388613);
        this.title = new FitTitle(context, str2);
        this.title.setContentDescription(str2);
        VirtualA11y.heading(this.title);
        linearLayout3.addView(this.title, new LinearLayout.LayoutParams(-1, px(24.0f)));
        this.sub = mono("", 6.5f, 0.14f, DIM);
        this.sub.setGravity(8388613);
        this.sub.setSingleLine(true);
        LinearLayout.LayoutParams lp = lp(-2, -2, 4.0f);
        lp.gravity = 8388613;
        linearLayout3.addView(this.sub, lp);
        linearLayout.addView(linearLayout3, new LinearLayout.LayoutParams(0, -2, 1.0f));
        this.col.addView(linearLayout);
        this.tabs = new LinearLayout(context);
        this.tabs.setPadding(0, 0, 0, px(10.0f));
        this.col.addView(this.tabs);
        this.body = new FrameLayout(context);
        this.col.addView(this.body, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda2
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return AoaPage.this.m227lambda$new$1$comaugmaosohomeAoaPage(view, windowInsets);
            }
        });
        header();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-home-AoaPage, reason: not valid java name */
    public /* synthetic */ void m226lambda$new$0$comaugmaosohomeAoaPage(View view) {
        Feedback.fx(view, 0);
        close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-augma-oso-home-AoaPage, reason: not valid java name */
    public /* synthetic */ WindowInsets m227lambda$new$1$comaugmaosohomeAoaPage(View view, WindowInsets windowInsets) {
        int systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
        int systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
        if (Build.VERSION.SDK_INT >= 30) {
            Insets insets = windowInsets.getInsets(WindowInsets.Type.ime() | WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            int i = insets.top;
            systemWindowInsetBottom = insets.bottom;
            systemWindowInsetTop = i;
        }
        this.col.setPadding(px(14.0f), systemWindowInsetTop + px(8.0f), px(14.0f), systemWindowInsetBottom + px(12.0f));
        return windowInsets;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int px(float f) {
        return Math.round(f * this.dn);
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    final class FitTitle extends View {
        final Paint p;
        final String s;

        FitTitle(Context context, String str) {
            super(context);
            this.p = new Paint(1);
            this.s = str;
            this.p.setTypeface(Fonts.RAJ_600);
            this.p.setColor(AoaPage.W);
            this.p.setTextAlign(Paint.Align.RIGHT);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float width = getWidth();
            float f = AoaPage.this.dn * 18.0f;
            this.p.setTextSize(f);
            this.p.setLetterSpacing(0.32f);
            if (this.p.measureText(this.s) > width) {
                this.p.setLetterSpacing(0.12f);
            }
            float measureText = this.p.measureText(this.s);
            if (measureText > width && measureText > 0.0f) {
                this.p.setTextSize(((f * width) / measureText) * 0.98f);
            }
            canvas.drawText(this.s, width - ((this.p.getLetterSpacing() * this.p.getTextSize()) * 0.0f), -this.p.getFontMetrics().ascent, this.p);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LinearLayout.LayoutParams lp(int i, int i2, float f) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, i2);
        layoutParams.topMargin = px(f);
        return layoutParams;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextView mono(String str, float f, float f2, int i) {
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTypeface(Fonts.MONO);
        textView.setTextSize(1, f);
        textView.setLetterSpacing(f2);
        textView.setTextColor(i);
        textView.setIncludeFontPadding(false);
        return textView;
    }

    TextView raj(String str, float f, int i) {
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTypeface(Fonts.RAJ_400);
        textView.setTextSize(1, f);
        textView.setTextColor(i);
        textView.setIncludeFontPadding(false);
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void header() {
        Calendar calendar = Calendar.getInstance();
        Locale locale = Locale.getDefault();
        this.hTime.setText(String.format(locale, "%tH:%<tM", calendar));
        this.hDate.setText(String.format(locale, "%tA %<td %<tB", calendar).toUpperCase(locale));
        this.hWeek.setText("WEEK " + calendar.get(3) + " · DAY " + calendar.get(6) + " · " + calendar.get(1));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTabs(final String[][] strArr, String str) {
        this.tab = str;
        this.tabs.removeAllViews();
        for (final String[] strArr2 : strArr) {
            boolean equals = strArr2[0].equals(str);
            TextView mono = mono(strArr2[1], 7.0f, 0.16f, equals ? W : MID);
            mono.setGravity(17);
            mono.setSingleLine(true);
            mono.setPadding(px(2.0f), px(9.0f), px(2.0f), px(8.0f));
            mono.setBackground(new AppsDrawer.PillBg(this.dn, equals));
            mono.setClickable(true);
            mono.setContentDescription(strArr2[1] + (equals ? ", selected" : ""));
            mono.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AoaPage.this.m228lambda$setTabs$2$comaugmaosohomeAoaPage(strArr, strArr2, view);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
            if (this.tabs.getChildCount() > 0) {
                layoutParams.setMarginStart(px(5.0f));
            }
            this.tabs.addView(mono, layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setTabs$2$com-augma-oso-home-AoaPage, reason: not valid java name */
    public /* synthetic */ void m228lambda$setTabs$2$comaugmaosohomeAoaPage(String[][] strArr, String[] strArr2, View view) {
        Feedback.fx(view, 2);
        setTabs(strArr, strArr2[0]);
        showTab(strArr2[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refresh() {
        header();
    }

    void stop() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FrameLayout panel(View view, int i) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setBackground(new AppsDrawer.Brk(this.dn, true, null));
        float f = i;
        frameLayout.setPadding(px(f), px(f), px(f), px(f));
        frameLayout.addView(view, new FrameLayout.LayoutParams(-1, -2));
        return frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LinearLayout row(String str, String str2, String str3, boolean z, final Runnable runnable) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(16);
        linearLayout.setMinimumHeight(px(42.0f));
        linearLayout.setPadding(px(2.0f), 0, px(2.0f), 0);
        linearLayout.setBackground(new Drawable() { // from class: com.jarvis.assist.aosui.home.AoaPage.1
            final Paint p = new Paint();

            {
                this.p.setColor(AoaPage.ln(0.1f));
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                Rect bounds = getBounds();
                canvas.drawRect(bounds.left, bounds.bottom - Math.max(1.0f, AoaPage.this.dn), bounds.right, bounds.bottom, this.p);
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
        if (str != null) {
            TextView mono = mono(str, 8.0f, 0.12f, DIM);
            mono.setMinWidth(px(52.0f));
            linearLayout.addView(mono);
        }
        int i = W;
        TextView mono2 = mono(str2, 9.0f, 0.1f, W);
        mono2.setSingleLine(true);
        mono2.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(mono2, new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (str3 != null) {
            if (z) {
                i = -34278;
            }
            TextView mono3 = mono(str3, 8.5f, 0.1f, i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.setMarginStart(px(8.0f));
            linearLayout.addView(mono3, layoutParams);
        }
        if (runnable != null) {
            linearLayout.setClickable(true);
            linearLayout.setFocusable(true);
            linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AoaPage.lambda$row$3(runnable, view);
                }
            });
        }
        linearLayout.setContentDescription((str == null ? "" : str + " ") + str2 + (str3 != null ? ", " + str3 : ""));
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$row$3(Runnable runnable, View view) {
        Feedback.fx(view, 0);
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextView button(String str, boolean z, final Runnable runnable) {
        TextView mono = mono(str, 8.0f, 0.16f, z ? -34278 : W);
        mono.setGravity(17);
        mono.setPadding(px(12.0f), px(10.0f), px(12.0f), px(10.0f));
        mono.setBackground(Kit.pill(Theme.a(z ? -34278 : -16117484, z ? 0.08f : 0.7f), z ? -34278 : ln(0.22f), this.dn, 0.0f));
        mono.setClickable(true);
        mono.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AoaPage.lambda$button$4(runnable, view);
            }
        });
        return mono;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$button$4(Runnable runnable, View view) {
        Feedback.fx(view, 0);
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScrollView scroll(View view) {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setOverScrollMode(1);
        scrollView.addView(view);
        return scrollView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start(Intent intent) {
        try {
            getContext().startActivity(intent.addFlags(268435456));
        } catch (Exception e) {
            this.be.toast("No app for that");
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.sx = motionEvent.getX();
                this.sy = motionEvent.getY();
                this.swiping = false;
                return false;
            case 1:
            default:
                return false;
            case 2:
                float x = motionEvent.getX() - this.sx;
                float y = motionEvent.getY() - this.sy;
                if (Math.abs(x) <= px(24.0f) || Math.abs(x) <= Math.abs(y) * 2.0f) {
                    return false;
                }
                this.swiping = true;
                return true;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.sx = motionEvent.getX();
            this.sy = motionEvent.getY();
            this.swiping = false;
            return true;
        }
        if (motionEvent.getActionMasked() == 2 && !this.swiping) {
            float x = motionEvent.getX() - this.sx;
            float y = motionEvent.getY() - this.sy;
            if (Math.abs(x) > px(24.0f) && Math.abs(x) > Math.abs(y) * 2.0f) {
                this.swiping = true;
            }
            return true;
        }
        if (motionEvent.getActionMasked() == 1 && this.swiping) {
            float x2 = motionEvent.getX() - this.sx;
            if (Math.abs(x2) > px(60.0f)) {
                go(AoaPages.next(this.kind, x2 < 0.0f ? 1 : -1));
            }
            this.swiping = false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void go(String str) {
        ViewGroup viewGroup;
        if (str == null || (viewGroup = (ViewGroup) getParent()) == null) {
            return;
        }
        Feedback.fx(this, 8);
        if ("home".equals(str)) {
            close();
        } else {
            show(viewGroup, this.be, this.h, this.d, str, -1.0f, -1.0f, Arrays.asList(AoaPages.ORDER).indexOf(str) > Arrays.asList(AoaPages.ORDER).indexOf(this.kind) ? 1 : -1);
        }
    }

    static boolean light() {
        return "LIGHT".equals(Perf.tier());
    }

    static boolean plain() {
        return Perf.k().calm || !Perf.k().motion;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reveal(float f, float f2) {
        if (plain()) {
            setAlpha(0.0f);
            animate().alpha(1.0f).setDuration(160L).start();
            return;
        }
        boolean light = light();
        float width = getWidth();
        float height = getHeight();
        try {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this, (int) f, (int) f2, 0.0f, AoaTransit.radius(f, f2, width, height));
            createCircularReveal.setStartDelay(AoaTransit.revealDelay(light));
            createCircularReveal.setDuration(AoaTransit.revealMs(light));
            createCircularReveal.setInterpolator(new DecelerateInterpolator());
            setVisibility(4);
            createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.jarvis.assist.aosui.home.AoaPage.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    AoaPage.this.setVisibility(0);
                }
            });
            createCircularReveal.start();
        } catch (Exception e) {
            setVisibility(0);
        }
        float radius = AoaTransit.radius(f, f2, width, height);
        for (int i = 0; i < this.col.getChildCount(); i++) {
            View childAt = this.col.getChildAt(i);
            if (childAt.getVisibility() == 0) {
                float hypot = (float) Math.hypot(r5 - f, r6 - f2);
                childAt.setTranslationX((f - (childAt.getLeft() + (childAt.getWidth() / 2.0f))) * 0.35f);
                childAt.setTranslationY((f2 - (childAt.getTop() + (childAt.getHeight() / 2.0f))) * 0.35f);
                childAt.setScaleX(0.92f);
                childAt.setScaleY(0.92f);
                childAt.setAlpha(0.0f);
                childAt.animate().translationX(0.0f).translationY(0.0f).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setStartDelay(AoaTransit.blockDelay(hypot, radius, light)).setDuration(AoaTransit.blockMs(light)).setInterpolator(new DecelerateInterpolator()).start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void slideIn(int i) {
        if (plain()) {
            setAlpha(0.0f);
            animate().alpha(1.0f).setDuration(160L).start();
            return;
        }
        float f = getResources().getDisplayMetrics().widthPixels;
        long j = light() ? 200L : 300L;
        setTranslationX(i * f);
        animate().translationX(0.0f).setDuration(j).setInterpolator(new DecelerateInterpolator()).start();
        View childAt = this.col.getChildAt(0);
        childAt.setTranslationX((-i) * f * 0.39999998f);
        childAt.animate().translationX(0.0f).setDuration(80 + j).setInterpolator(new DecelerateInterpolator()).start();
        this.title.setScaleX(1.1f);
        this.title.setScaleY(1.1f);
        this.title.animate().scaleX(1.0f).scaleY(1.0f).setStartDelay(j).setDuration(220L).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        if (this.closing) {
            return;
        }
        this.closing = true;
        stop();
        final ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup == null) {
            return;
        }
        if (plain()) {
            animate().alpha(0.0f).setDuration(140L).withEndAction(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    AoaPage.this.m225lambda$close$5$comaugmaosohomeAoaPage(viewGroup);
                }
            }).start();
            return;
        }
        float[] orb = hooks == null ? null : hooks.orb();
        float width = orb == null ? getWidth() / 2.0f : orb[0];
        float height = orb == null ? getHeight() / 3.0f : orb[1];
        boolean light = light();
        try {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this, (int) width, (int) height, AoaTransit.radius(width, height, getWidth(), getHeight()), 0.0f);
            createCircularReveal.setDuration(AoaTransit.collapseMs(light));
            createCircularReveal.setInterpolator(new AccelerateInterpolator());
            createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.jarvis.assist.aosui.home.AoaPage.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    viewGroup.removeView(AoaPage.this);
                }
            });
            createCircularReveal.start();
        } catch (Exception e) {
            viewGroup.removeView(this);
        }
        if (hooks != null) {
            final Hooks hooks2 = hooks;
            Objects.requireNonNull(hooks2);
            postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AoaPage.Hooks.this.expand();
                }
            }, AoaTransit.collapseMs(light) / 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$close$5$com-augma-oso-home-AoaPage, reason: not valid java name */
    public /* synthetic */ void m225lambda$close$5$comaugmaosohomeAoaPage(ViewGroup viewGroup) {
        viewGroup.removeView(this);
        if (hooks != null) {
            hooks.expand();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void show(ViewGroup viewGroup, Backend backend, HomeCtx homeCtx, HomeData homeData, String str) {
        show(viewGroup, backend, homeCtx, homeData, str, -1.0f, -1.0f, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void show(ViewGroup viewGroup, Backend backend, HomeCtx homeCtx, HomeData homeData, String str, final float f, final float f2, final int i) {
        AoaPage find = find(viewGroup);
        if (find != null) {
            find.stop();
            viewGroup.removeView(find);
        }
        boolean z = find == null && i == 0;
        Context context = viewGroup.getContext();
        final AoaPage aoaCal = "cal".equals(str) ? new AoaCal(context, backend, homeCtx, homeData) : "clock".equals(str) ? new AoaClock(context, backend, homeCtx, homeData) : "music".equals(str) ? new AoaMusic(context, backend, homeCtx, homeData) : new AoaMsg(context, backend, homeCtx, homeData);
        viewGroup.addView(aoaCal, new FrameLayout.LayoutParams(-1, -1));
        aoaCal.requestApplyInsets();
        if (i != 0) {
            aoaCal.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    AoaPage.this.slideIn(i);
                }
            });
            return;
        }
        if (z && hooks != null && !plain()) {
            hooks.contract();
        }
        float[] orb = hooks == null ? null : hooks.orb();
        if (f < 0.0f) {
            f = orb != null ? orb[0] : -1.0f;
        }
        if (f2 < 0.0f) {
            f2 = orb != null ? orb[1] : -1.0f;
        }
        if (f < 0.0f) {
            f = viewGroup.getWidth() / 2.0f;
            f2 = viewGroup.getHeight() / 3.0f;
        }
        aoaCal.setVisibility(4);
        aoaCal.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaPage$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                AoaPage.this.reveal(f, f2);
            }
        });
    }

    static AoaPage find(ViewGroup viewGroup) {
        for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
            if (viewGroup.getChildAt(childCount) instanceof AoaPage) {
                return (AoaPage) viewGroup.getChildAt(childCount);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean closeTop(ViewGroup viewGroup) {
        AoaPage find = find(viewGroup);
        if (find == null) {
            return false;
        }
        find.close();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void changed(ViewGroup viewGroup) {
        AoaPage find = find(viewGroup);
        if (find != null) {
            find.refresh();
        }
    }
}
