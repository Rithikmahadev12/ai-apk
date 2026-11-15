package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.PathInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.AoaDrawer;
import com.jarvis.assist.aosui.core.DrawerModel;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.Icons;
import com.jarvis.assist.aosui.core.Palette;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.core.VaultLink;
import com.jarvis.assist.aosui.home.AppsDrawer;
import com.jarvis.assist.aosui.home.HomeData;
import com.jarvis.assist.aosui.home.VirtualA11y;
import com.jarvis.assist.aosui.ui.Kit;
import com.jarvis.assist.aosui.ui.Sheet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AppsDrawer extends FrameLayout {
    static final int AD = -9399652;
    static final int AO = -34278;
    static final int AT = -11624262;
    static final int AW = -1511182;
    static final String FOLDER = "\u0001folder:";
    TextView aClock;
    TextView aCount;
    TextView aDate;
    final int acc;
    final boolean aoa;
    final ArrayList<HomeRules.App> apps;
    final Backend be;
    final LinearLayout chips;
    final LinearLayout col;
    String cur;
    final HomeData d;
    DrawerModel.Cfg dc;
    final int dim;
    final float dn;
    final AppGrid grid;
    final HomeCtx h;
    final ArrayList<String> hidden;
    final Host host;
    final int ink;
    final int line;
    final AppList list;
    final TextView loading;
    final ArrayList<HomeRules.App> main;
    LinearLayout menu;
    final TextView pinHead;
    final AppGrid pins;
    final EditText q;
    final Rail rail;
    final int red;
    final Runnable reloadLater;
    final LinearLayout res;
    final ScrollView sc;
    View scrim;
    boolean showH;
    String sort;
    final AppGrid sug;
    LinearLayout sugBox;
    final TextView sugHead;
    final Theme th;
    HashMap<String, Integer> use;
    final float z;
    static final String[][] SETS = {new String[]{"brief", "app", "Brief"}, new String[]{"wifi", "settings", "wifi"}, new String[]{"bluetooth", "settings", "bluetooth"}, new String[]{"display", "settings", "display"}, new String[]{"battery", "settings", "battery"}, new String[]{"dnd", "settings", "dnd"}, new String[]{"location", "settings", "location"}, new String[]{"storage", "settings", "storage"}, new String[]{"aos", "app", "Settings"}, new String[]{"vault", "app", VaultLink.APP}, new String[]{"home", "settings", "home"}, new String[]{"date", "settings", "date"}};
    static final PathInterpolator EASE = new PathInterpolator(0.2f, 0.8f, 0.2f, 1.0f);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Host {
        void close();

        void dockChanged();

        void launch(String str, String str2, String str3);
    }

    static boolean isFolder(HomeRules.App app) {
        return app.pkg.startsWith(FOLDER);
    }

    static int folderIdx(HomeRules.App app) {
        try {
            return Integer.parseInt(app.pkg.substring(FOLDER.length()));
        } catch (Exception e) {
            return -1;
        }
    }

    HomeRules.App app(String str) {
        Iterator<HomeRules.App> it = this.apps.iterator();
        while (it.hasNext()) {
            HomeRules.App next = it.next();
            if (next.pkg.equals(str)) {
                return next;
            }
        }
        return null;
    }

    void saveDc() {
        Store.set(DrawerModel.KEY, DrawerModel.save(this.dc));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppsDrawer(Context context, Backend backend, HomeCtx homeCtx, HomeData homeData, final Host host) {
        super(context);
        Drawable gridBg;
        int i;
        float f;
        int i2;
        int i3;
        this.apps = new ArrayList<>();
        this.main = new ArrayList<>();
        this.hidden = new ArrayList<>();
        this.sort = "az";
        this.dc = new DrawerModel.Cfg();
        this.use = new HashMap<>();
        this.reloadLater = new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m257lambda$new$9$comaugmaosohomeAppsDrawer();
            }
        };
        this.cur = "";
        this.be = backend;
        this.h = homeCtx;
        this.d = homeData;
        this.host = host;
        this.th = homeCtx.th;
        this.dn = context.getResources().getDisplayMetrics().density;
        this.z = UnitsMath.viewZoom(context.getResources().getConfiguration().fontScale);
        this.ink = this.th.light ? Palette.LIGHT_INK : -1774607;
        this.dim = Theme.a(this.ink, 0.78f);
        this.line = this.th.light ? 1293164072 : 1388893922;
        this.acc = this.th.BLUE;
        this.aoa = homeCtx.aoa;
        this.red = this.aoa ? this.th.RED : -2078885;
        if (this.aoa) {
            gridBg = new AoaBg(this.dn);
        } else {
            if (this.th.light) {
                i = 246;
                f = 0.97f;
                i2 = 243;
                i3 = 244;
            } else {
                i = 5;
                f = 0.94f;
                i2 = 3;
                i3 = 4;
            }
            gridBg = new Kit.GridBg(Theme.rgba(i2, i3, i, f), Theme.a(this.line, 0.35f), this.dn);
        }
        setBackground(gridBg);
        setClickable(true);
        setFocusable(true);
        setImportantForAccessibility(1);
        VirtualA11y.pane(this, "Apps");
        this.col = new LinearLayout(context);
        this.col.setOrientation(1);
        addView(this.col, new FrameLayout.LayoutParams(-1, -1));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(16);
        linearLayout.setPadding(px(2.0f), px(8.0f), px(2.0f), px(14.0f));
        this.q = new EditText(context);
        this.q.setHint(Strings.t("drawer.search"));
        this.q.setSingleLine(true);
        this.q.setTypeface(Fonts.MONO);
        this.q.setTextSize(1, this.z * 12.0f);
        this.q.setLetterSpacing(0.14f);
        this.q.setTextColor(this.ink);
        this.q.setHintTextColor(this.dim);
        this.q.setBackground(pill(0, this.line));
        this.q.setPadding(px(16.0f), px(10.0f), px(16.0f), px(10.0f));
        this.q.setImeOptions(268435459);
        this.q.setContentDescription(Strings.t("dr.search.l"));
        this.q.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(this.q, new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView textView = new TextView(context);
        textView.setText("×");
        textView.setTextSize(1, this.z * 22.0f);
        textView.setTextColor(this.ink);
        textView.setGravity(17);
        textView.setBackground(pill(0, this.line));
        textView.setContentDescription(Strings.t("dr.close"));
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda21
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.this.m253lambda$new$0$comaugmaosohomeAppsDrawer(host, view);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px(40.0f), px(40.0f));
        layoutParams.setMarginStart(px(10.0f));
        linearLayout.addView(textView, layoutParams);
        if (this.aoa) {
            aoaTop(context, linearLayout, textView);
        } else {
            this.col.addView(linearLayout);
        }
        this.chips = new LinearLayout(context);
        this.chips.setPadding(px(2.0f), 0, px(2.0f), px(12.0f));
        this.col.addView(this.chips);
        this.sc = new ScrollView(context);
        this.sc.setVerticalScrollBarEnabled(false);
        this.sc.setOverScrollMode(1);
        this.sc.setClipToPadding(false);
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        this.sc.addView(linearLayout2);
        this.col.addView(this.sc, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.sugHead = sec(Strings.t("drawer.suggested"));
        this.sugHead.setPadding(px(6.0f), 0, px(6.0f), 0);
        this.sug = new AppGrid(context, true);
        if (this.aoa) {
            this.sugBox = new LinearLayout(context);
            this.sugBox.setOrientation(1);
            this.sugBox.setPadding(px(10.0f), px(8.0f), px(10.0f), px(4.0f));
            this.sugBox.setBackground(new Brk(this.dn, true, null));
            LinearLayout linearLayout3 = new LinearLayout(context);
            this.sugHead.setText("SUGGESTED");
            this.sugHead.setTextColor(-34278);
            this.sugHead.setPadding(0, 0, 0, px(6.0f));
            linearLayout3.addView(this.sugHead, new LinearLayout.LayoutParams(0, -2, 1.0f));
            TextView sec = sec("FROM YOUR USAGE");
            sec.setTextColor(AW);
            sec.setPadding(0, 0, 0, px(6.0f));
            linearLayout3.addView(sec);
            this.sugBox.addView(linearLayout3);
            this.sugBox.addView(this.sug, new LinearLayout.LayoutParams(-1, -2));
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.bottomMargin = px(12.0f);
            linearLayout2.addView(this.sugBox, layoutParams2);
        } else {
            linearLayout2.addView(this.sugHead);
            linearLayout2.addView(this.sug, new LinearLayout.LayoutParams(-1, -2));
        }
        this.pinHead = sec(Strings.t("dr.pinned"));
        this.pinHead.setPadding(px(6.0f), 0, px(6.0f), 0);
        linearLayout2.addView(this.pinHead);
        this.pins = new AppGrid(context, true);
        linearLayout2.addView(this.pins, new LinearLayout.LayoutParams(-1, -2));
        this.grid = new AppGrid(context, false);
        linearLayout2.addView(this.grid, new LinearLayout.LayoutParams(-1, -2));
        this.list = new AppList(context);
        linearLayout2.addView(this.list, new LinearLayout.LayoutParams(-1, -2));
        this.res = new LinearLayout(context);
        this.res.setOrientation(1);
        this.res.setPadding(0, px(6.0f), 0, px(6.0f));
        linearLayout2.addView(this.res);
        this.loading = sec(Strings.t("dr.loading"));
        this.loading.setPadding(px(6.0f), px(12.0f), px(6.0f), 0);
        this.loading.setVisibility(8);
        linearLayout2.addView(this.loading);
        this.rail = new Rail(context);
        addView(this.rail, new FrameLayout.LayoutParams(px(28.0f), -1, 8388613));
        this.sc.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda22
            @Override // android.view.View.OnScrollChangeListener
            public final void onScrollChange(View view, int i4, int i5, int i6, int i7) {
                AppsDrawer.this.m254lambda$new$1$comaugmaosohomeAppsDrawer(view, i4, i5, i6, i7);
            }
        });
        this.q.addTextChangedListener(new TextWatcher() { // from class: com.jarvis.assist.aosui.home.AppsDrawer.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                AppsDrawer.this.search(editable.toString());
            }
        });
        this.q.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda23
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView2, int i4, KeyEvent keyEvent) {
                return AppsDrawer.this.m255lambda$new$2$comaugmaosohomeAppsDrawer(textView2, i4, keyEvent);
            }
        });
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda24
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return AppsDrawer.this.m256lambda$new$3$comaugmaosohomeAppsDrawer(view, windowInsets);
            }
        });
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m253lambda$new$0$comaugmaosohomeAppsDrawer(Host host, View view) {
        Feedback.fx(view, 0);
        if (this.q.getText().length() > 0) {
            this.q.setText("");
        } else {
            host.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m254lambda$new$1$comaugmaosohomeAppsDrawer(View view, int i, int i2, int i3, int i4) {
        if (this.aoa) {
            this.rail.follow(i2);
        }
        this.grid.window();
        this.sug.window();
        this.pins.window();
        this.list.invalidate();
        if (Math.abs(i2 - i4) > 2 && this.q.hasFocus() && this.q.getText().length() == 0) {
            keyboard(false);
        }
        closeMenu();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ boolean m255lambda$new$2$comaugmaosohomeAppsDrawer(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 3 && (keyEvent == null || keyEvent.getKeyCode() != 66 || keyEvent.getAction() != 0)) {
            return false;
        }
        enter();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ WindowInsets m256lambda$new$3$comaugmaosohomeAppsDrawer(View view, WindowInsets windowInsets) {
        int systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
        int systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
        int systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
        int systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
        if (Build.VERSION.SDK_INT >= 30) {
            Insets insets = windowInsets.getInsets(WindowInsets.Type.ime() | WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            int i = insets.top;
            int i2 = insets.bottom;
            int i3 = insets.left;
            systemWindowInsetRight = insets.right;
            systemWindowInsetTop = i;
            systemWindowInsetBottom = i2;
            systemWindowInsetLeft = i3;
        }
        boolean z = Build.VERSION.SDK_INT >= 30 && windowInsets.isVisible(WindowInsets.Type.ime());
        boolean z2 = z && getHeight() > 0 && (getHeight() - systemWindowInsetTop) - systemWindowInsetBottom < px(360.0f);
        this.chips.setVisibility(z2 ? 8 : 0);
        this.col.setPadding(px(14.0f) + systemWindowInsetLeft, (z2 ? px(4.0f) : px(16.0f)) + systemWindowInsetTop, px(this.aoa ? 30.0f : 14.0f) + systemWindowInsetRight, (z ? px(4.0f) : px(24.0f)) + systemWindowInsetBottom);
        this.sc.setMinimumHeight(z2 ? px(96.0f) : 0);
        this.rail.setPadding(0, systemWindowInsetTop + px(110.0f), 0, systemWindowInsetBottom + px(24.0f));
        ((FrameLayout.LayoutParams) this.rail.getLayoutParams()).rightMargin = systemWindowInsetRight;
        this.rail.requestLayout();
        return windowInsets;
    }

    int px(float f) {
        return Math.round(f * this.dn);
    }

    Drawable pill(int i, int i2) {
        return new Kit.HudBg(i, i2, true, this.dn);
    }

    TextView sec(String str) {
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTypeface(Fonts.MONO);
        textView.setTextSize(1, this.z * 9.0f);
        textView.setLetterSpacing(0.14f);
        textView.setTextColor(this.dim);
        textView.setPadding(px(2.0f), px(8.0f), px(2.0f), px(4.0f));
        VirtualA11y.heading(textView);
        return textView;
    }

    TextView mono(String str, float f, float f2, int i) {
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTypeface(Fonts.MONO);
        textView.setTextSize(1, f * this.z);
        textView.setLetterSpacing(f2);
        textView.setTextColor(i);
        textView.setIncludeFontPadding(false);
        return textView;
    }

    void aoaTop(Context context, LinearLayout linearLayout, TextView textView) {
        float f;
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setGravity(16);
        linearLayout2.setPadding(px(2.0f), px(4.0f), px(2.0f), px(12.0f));
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setOrientation(1);
        TextView textView2 = new TextView(context);
        textView2.setText("APPS");
        textView2.setTypeface(Fonts.RAJ_600);
        textView2.setTextSize(1, this.z * 24.0f);
        textView2.setLetterSpacing(0.3f);
        textView2.setTextColor(AW);
        textView2.setIncludeFontPadding(false);
        VirtualA11y.heading(textView2);
        linearLayout3.addView(textView2);
        this.aCount = mono("", 7.5f, 0.14f, AD);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = px(4.0f);
        linearLayout3.addView(this.aCount, layoutParams);
        linearLayout2.addView(linearLayout3, new LinearLayout.LayoutParams(0, -2, 1.0f));
        LinearLayout linearLayout4 = new LinearLayout(context);
        linearLayout4.setOrientation(1);
        linearLayout4.setGravity(8388613);
        this.aClock = mono("", 13.0f, 0.14f, AW);
        this.aClock.setGravity(8388613);
        linearLayout4.addView(this.aClock);
        this.aDate = mono("", 6.5f, 0.14f, AD);
        this.aDate.setGravity(8388613);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.topMargin = px(4.0f);
        layoutParams2.gravity = 8388613;
        linearLayout4.addView(this.aDate, layoutParams2);
        linearLayout2.addView(linearLayout4, new LinearLayout.LayoutParams(-2, -2));
        ((ViewGroup) textView.getParent()).removeView(textView);
        textView.setBackground(new Brk(this.dn, true, null));
        textView.setTextColor(AW);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(px(40.0f), px(40.0f));
        layoutParams3.setMarginStart(px(12.0f));
        linearLayout2.addView(textView, layoutParams3);
        this.col.addView(linearLayout2);
        this.q.setHint("SEARCH");
        this.q.setHintTextColor(AD);
        this.q.setTextColor(AW);
        this.q.setBackground(new Brk(this.dn, true, this.q));
        this.q.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda36
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z) {
                AppsDrawer.this.m233lambda$aoaTop$4$comaugmaosohomeAppsDrawer(view, z);
            }
        });
        if (Build.VERSION.SDK_INT >= 29) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(-34278);
            f = 2.0f;
            gradientDrawable.setSize(px(2.0f), px(16.0f));
            this.q.setTextCursorDrawable(gradientDrawable);
        } else {
            f = 2.0f;
        }
        linearLayout.setPadding(px(f), 0, px(f), px(12.0f));
        this.col.addView(linearLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aoaTop$4$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m233lambda$aoaTop$4$comaugmaosohomeAppsDrawer(View view, boolean z) {
        this.q.getBackground().invalidateSelf();
    }

    void aoaHeader() {
        String str;
        if (this.aCount == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        Locale locale = Locale.getDefault();
        TextView textView = this.aCount;
        int size = this.apps.size();
        int size2 = this.dc.pins.size();
        if (this.hidden.isEmpty()) {
            str = "";
        } else {
            str = " · " + this.hidden.size() + " HIDDEN";
        }
        textView.setText(size + " INSTALLED · " + size2 + " PINNED" + str);
        this.aClock.setText(String.format(locale, "%tH:%<tM", calendar));
        this.aDate.setText(String.format(locale, "%ta %<td %<tb · WK %d", calendar, Integer.valueOf(calendar.get(3))).toUpperCase(locale));
    }

    void aoaChips() {
        this.chips.removeAllViews();
        String[][] strArr = {new String[]{"az", "A–Z", "list"}, new String[]{"use", "MOST USED", "flame"}, new String[]{"recent", "RECENT", "clock"}};
        for (int i = 0; i < 3; i++) {
            final String[] strArr2 = strArr[i];
            pill(strArr2[1], strArr2[2], this.sort.equals(strArr2[0]), new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda17
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AppsDrawer.this.m230lambda$aoaChips$5$comaugmaosohomeAppsDrawer(strArr2, view);
                }
            });
        }
        TextView pill = pill(this.dc.list() ? "VIEW · LIST" : "VIEW · GRID", this.dc.list() ? "list" : "grid", false, new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.this.m231lambda$aoaChips$6$comaugmaosohomeAppsDrawer(view);
            }
        });
        pill.setContentDescription(Strings.t(this.dc.list() ? "dr.view.grid.a11y" : "dr.view.list.a11y"));
        pill.setTextColor(-34278);
        if (this.hidden.isEmpty()) {
            return;
        }
        pill(Strings.t("dr.hidden", "n", Integer.valueOf(this.hidden.size())).toUpperCase(Locale.getDefault()), "lock", this.showH, new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda19
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.this.m232lambda$aoaChips$7$comaugmaosohomeAppsDrawer(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aoaChips$5$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m230lambda$aoaChips$5$comaugmaosohomeAppsDrawer(String[] strArr, View view) {
        Store.set("aos.sort", strArr[0]);
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aoaChips$6$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m231lambda$aoaChips$6$comaugmaosohomeAppsDrawer(View view) {
        this.dc.view = this.dc.list() ? "grid" : "list";
        saveDc();
        reload();
        this.sc.scrollTo(0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aoaChips$7$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m232lambda$aoaChips$7$comaugmaosohomeAppsDrawer(View view) {
        this.showH = !this.showH;
        reload();
    }

    TextView pill(String str, final String str2, final boolean z, final View.OnClickListener onClickListener) {
        TextView textView = new TextView(getContext()) { // from class: com.jarvis.assist.aosui.home.AppsDrawer.2
            final Paint ip = new Paint(1);

            @Override // android.widget.TextView, android.view.View
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                Icons.draw(canvas, this.ip, str2, (getWidth() / 2.0f) + getScrollX(), getScrollY() + AppsDrawer.this.px(12.0f), AppsDrawer.this.px(10.0f), z ? AppsDrawer.AW : -6902604, 1.1f * AppsDrawer.this.dn);
            }
        };
        textView.setText(str);
        textView.setTypeface(Fonts.MONO);
        textView.setTextSize(1, this.z * 7.0f);
        textView.setLetterSpacing(0.14f);
        textView.setTextColor(z ? AW : -6902604);
        textView.setSingleLine(true);
        textView.setGravity(81);
        textView.setPadding(px(2.0f), px(20.0f), px(2.0f), px(6.0f));
        textView.setMinHeight(px(40.0f));
        textView.setBackground(new PillBg(this.dn, z));
        textView.setSelected(z);
        textView.setContentDescription(str + (z ? ", selected" : ""));
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda35
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.lambda$pill$8(onClickListener, view);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        if (this.chips.getChildCount() > 0) {
            layoutParams.setMarginStart(px(5.0f));
        }
        this.chips.addView(textView, layoutParams);
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pill$8(View.OnClickListener onClickListener, View view) {
        Feedback.fx(view, 2);
        onClickListener.onClick(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class AoaBg extends Drawable {
        final float dn;
        final Paint f = new Paint();
        final Paint ln = new Paint();
        final Paint pl = new Paint(1);

        /* JADX INFO: Access modifiers changed from: package-private */
        public AoaBg(float f) {
            this.dn = f;
            this.f.setColor(-16579322);
            this.ln.setColor(Theme.rgba(160, 200, 212, 0.05f));
            this.pl.setColor(Theme.rgba(192, 214, 220, 0.22f));
            this.pl.setStrokeWidth(Math.max(1.0f, f * 0.8f));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            canvas.drawRect(bounds, this.f);
            float f = 27.0f * this.dn;
            float max = Math.max(1.0f, this.dn);
            for (float f2 = bounds.left; f2 < bounds.right; f2 += f) {
                canvas.drawRect(f2, bounds.top, f2 + max, bounds.bottom, this.ln);
            }
            for (float f3 = bounds.top; f3 < bounds.bottom; f3 += f) {
                canvas.drawRect(bounds.left, f3, bounds.right, f3 + max, this.ln);
            }
            float f4 = 71.0f * this.dn;
            float f5 = this.dn * 5.0f;
            float f6 = f4 / 2.0f;
            float f7 = bounds.top + f6;
            while (f7 < bounds.bottom) {
                float f8 = bounds.left + f6;
                while (f8 < bounds.right) {
                    canvas.drawLine(f8 - f5, f7, f8 + f5, f7, this.pl);
                    float f9 = f7;
                    float f10 = f8;
                    canvas.drawLine(f10, f9 - f5, f8, f9 + f5, this.pl);
                    f8 = f10 + f4;
                    f7 = f9;
                }
                f7 += f4;
            }
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Brk extends Drawable {
        final float dn;
        final View field;
        final Paint f = new Paint();
        final Paint s = new Paint();
        final Paint b = new Paint(1);
        final Paint u = new Paint();

        /* JADX INFO: Access modifiers changed from: package-private */
        public Brk(float f, boolean z, View view) {
            this.dn = f;
            this.field = view;
            this.f.setColor(Theme.rgba(10, 17, 20, 0.8f));
            this.s.setStyle(Paint.Style.STROKE);
            this.s.setStrokeWidth(Math.max(1.0f, f));
            this.s.setColor(Theme.rgba(192, 214, 220, 0.14f));
            this.b.setStyle(Paint.Style.STROKE);
            this.b.setStrokeWidth(Math.max(1.0f, f));
            this.b.setColor(z ? AppsDrawer.AW : 0);
            this.u.setColor(-34278);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            float strokeWidth = this.s.getStrokeWidth() / 2.0f;
            float f = this.dn * 7.0f;
            canvas.drawRect(bounds, this.f);
            canvas.drawRect(bounds.left + strokeWidth, bounds.top + strokeWidth, bounds.right - strokeWidth, bounds.bottom - strokeWidth, this.s);
            if (this.b.getColor() != 0) {
                float f2 = bounds.left + strokeWidth;
                float f3 = bounds.top + strokeWidth;
                float f4 = bounds.right - strokeWidth;
                float f5 = bounds.bottom - strokeWidth;
                float f6 = f2 + f;
                canvas.drawLine(f2, f3, f6, f3, this.b);
                float f7 = f3 + f;
                canvas.drawLine(f2, f3, f2, f7, this.b);
                float f8 = f4 - f;
                canvas.drawLine(f4, f3, f8, f3, this.b);
                canvas.drawLine(f4, f3, f4, f7, this.b);
                canvas.drawLine(f2, f5, f6, f5, this.b);
                float f9 = f5 - f;
                canvas.drawLine(f2, f5, f2, f9, this.b);
                canvas.drawLine(f4, f5, f8, f5, this.b);
                canvas.drawLine(f4, f5, f4, f9, this.b);
            }
            if (this.field == null || !this.field.hasFocus()) {
                return;
            }
            canvas.drawRect(bounds.left + (this.dn * 10.0f), bounds.bottom - (this.dn * 2.0f), bounds.right - (this.dn * 10.0f), bounds.bottom, this.u);
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class PillBg extends Drawable {
        final float dn;
        final boolean on;
        final Paint f = new Paint();
        final Paint s = new Paint();
        final Paint u = new Paint();

        /* JADX INFO: Access modifiers changed from: package-private */
        public PillBg(float f, boolean z) {
            this.dn = f;
            this.on = z;
            this.f.setColor(Theme.rgba(10, 17, 20, 0.7f));
            this.s.setStyle(Paint.Style.STROKE);
            this.s.setStrokeWidth(Math.max(1.0f, f));
            this.s.setColor(z ? Theme.a(-11624262, 0.8f) : Theme.rgba(192, 214, 220, 0.16f));
            this.u.setColor(-34278);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            canvas.drawRect(getBounds(), this.f);
            float strokeWidth = this.s.getStrokeWidth() / 2.0f;
            canvas.drawRect(r0.left + strokeWidth, r0.top + strokeWidth, r0.right - strokeWidth, r0.bottom - strokeWidth, this.s);
            if (this.on) {
                canvas.drawRect(r0.left, r0.bottom - (this.dn * 2.0f), r0.right, r0.bottom, this.u);
            }
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reload() {
        JSONArray jSONArray;
        JSONObject cfg = this.d.cfg();
        this.hidden.clear();
        this.hidden.addAll(HomeData.strs(cfg.optJSONArray("hidden")));
        this.sort = Store.get("aos.sort", "az");
        String appsCached = this.be.appsCached();
        this.loading.setVisibility(appsCached == null ? 0 : 8);
        if (appsCached == null) {
            this.be.appsReady(this.reloadLater);
            appsCached = "[]";
        }
        this.apps.clear();
        try {
            jSONArray = new JSONArray(appsCached);
        } catch (Exception e) {
            jSONArray = new JSONArray();
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject optJSONObject = jSONArray.optJSONObject(i);
            if (optJSONObject != null && (this.showH || !this.hidden.contains(optJSONObject.optString("pkg")))) {
                this.apps.add(new HomeRules.App(optJSONObject.optString("pkg"), optJSONObject.optString("cls"), optJSONObject.optString("label")));
            }
        }
        this.use = new HashMap<>();
        JSONObject obj = Store.obj("aos.usage");
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            this.use.put(next, Integer.valueOf(obj.optInt(next)));
        }
        ArrayList arrayList = new ArrayList();
        JSONArray arr = Store.arr("aos.recent");
        for (int i2 = 0; i2 < arr.length(); i2++) {
            JSONObject optJSONObject2 = arr.optJSONObject(i2);
            if (optJSONObject2 != null) {
                arrayList.add(optJSONObject2.optString("pkg"));
            }
        }
        HomeRules.sort(this.apps, this.sort, this.use, arrayList);
        this.dc = DrawerModel.cfg(Store.get(DrawerModel.KEY, ""));
        ArrayList arrayList2 = new ArrayList();
        Iterator<String> it = this.dc.pins.iterator();
        while (it.hasNext()) {
            HomeRules.App app = app(it.next());
            if (app != null) {
                arrayList2.add(app);
            }
        }
        this.pins.set(arrayList2);
        this.main.clear();
        for (int i3 = 0; i3 < this.dc.folders.size(); i3++) {
            Iterator<String> it2 = this.dc.folders.get(i3).apps.iterator();
            int i4 = 0;
            while (it2.hasNext()) {
                if (app(it2.next()) != null) {
                    i4++;
                }
            }
            if (i4 > 0 || this.dc.folders.get(i3).apps.isEmpty()) {
                this.main.add(new HomeRules.App(FOLDER + i3, "", this.dc.folders.get(i3).name));
            }
        }
        ArrayList arrayList3 = new ArrayList();
        Iterator<HomeRules.App> it3 = this.apps.iterator();
        while (it3.hasNext()) {
            arrayList3.add(it3.next().pkg);
        }
        HashSet hashSet = new HashSet(DrawerModel.mainPkgs(this.dc, arrayList3));
        Iterator<HomeRules.App> it4 = this.apps.iterator();
        while (it4.hasNext()) {
            HomeRules.App next2 = it4.next();
            if (hashSet.contains(next2.pkg)) {
                this.main.add(next2);
            }
        }
        chips();
        ArrayList arrayList4 = new ArrayList();
        if (this.d.aifOn("suggest")) {
            ArrayList arrayList5 = new ArrayList();
            Iterator<HomeRules.App> it5 = this.apps.iterator();
            while (it5.hasNext()) {
                arrayList5.add(it5.next().pkg);
            }
            HashMap hashMap = new HashMap();
            JSONObject optJSONObject3 = Store.obj("aos.routine").optJSONObject("h");
            if (optJSONObject3 != null) {
                Iterator<String> keys2 = optJSONObject3.keys();
                while (keys2.hasNext()) {
                    String next3 = keys2.next();
                    JSONObject optJSONObject4 = optJSONObject3.optJSONObject(next3);
                    if (optJSONObject4 != null) {
                        HashMap hashMap2 = new HashMap();
                        Iterator<String> keys3 = optJSONObject4.keys();
                        while (keys3.hasNext()) {
                            String next4 = keys3.next();
                            hashMap2.put(next4, Integer.valueOf(optJSONObject4.optInt(next4)));
                        }
                        hashMap.put(next3, hashMap2);
                    }
                }
            }
            ArrayList<String> arrayList6 = null;
            JSONObject obj2 = this.d.aifOn("suggestai") ? Store.obj("aos.aif.sug") : null;
            if (obj2 != null && HomeData.day().equals(obj2.optString("d"))) {
                arrayList6 = HomeData.strs(obj2.optJSONArray("pkgs"));
            }
            for (String str : HomeRules.suggest(hashMap, this.use, arrayList5, Calendar.getInstance().get(11), 4, arrayList6)) {
                Iterator<HomeRules.App> it6 = this.apps.iterator();
                while (true) {
                    if (it6.hasNext()) {
                        HomeRules.App next5 = it6.next();
                        if (next5.pkg.equals(str)) {
                            arrayList4.add(next5);
                            break;
                        }
                    }
                }
            }
        }
        this.sug.set(arrayList4);
        this.grid.set(this.main);
        this.list.set(this.main);
        search(this.q.getText().toString());
        dots(this.d.pkgDot);
        aoaHeader();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$9$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m257lambda$new$9$comaugmaosohomeAppsDrawer() {
        if (isAttachedToWindow()) {
            reload();
        }
    }

    void chips() {
        if (this.aoa) {
            aoaChips();
            return;
        }
        this.chips.removeAllViews();
        String[][] strArr = {new String[]{"az", Strings.t("drawer.az")}, new String[]{"use", Strings.t("drawer.used")}, new String[]{"recent", Strings.t("drawer.recent")}};
        for (int i = 0; i < 3; i++) {
            final String[] strArr2 = strArr[i];
            chip(strArr2[1], this.sort.equals(strArr2[0]), new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda32
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AppsDrawer.this.m234lambda$chips$10$comaugmaosohomeAppsDrawer(strArr2, view);
                }
            });
        }
        if (!this.hidden.isEmpty()) {
            chip(Strings.t("dr.hidden", "n", Integer.valueOf(this.hidden.size())), this.showH, new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda33
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AppsDrawer.this.m235lambda$chips$11$comaugmaosohomeAppsDrawer(view);
                }
            });
        }
        chip(Strings.t(this.dc.list() ? "dr.view.grid" : "dr.view.list"), false, new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda34
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.this.m236lambda$chips$12$comaugmaosohomeAppsDrawer(view);
            }
        }).setContentDescription(Strings.t(this.dc.list() ? "dr.view.grid.a11y" : "dr.view.list.a11y"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$chips$10$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m234lambda$chips$10$comaugmaosohomeAppsDrawer(String[] strArr, View view) {
        Store.set("aos.sort", strArr[0]);
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$chips$11$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m235lambda$chips$11$comaugmaosohomeAppsDrawer(View view) {
        this.showH = !this.showH;
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$chips$12$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m236lambda$chips$12$comaugmaosohomeAppsDrawer(View view) {
        this.dc.view = this.dc.list() ? "grid" : "list";
        saveDc();
        reload();
        this.sc.scrollTo(0, 0);
    }

    TextView chip(String str, boolean z, final View.OnClickListener onClickListener) {
        int i;
        int i2;
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTypeface(Fonts.MONO);
        textView.setTextSize(1, this.z * 9.5f);
        textView.setLetterSpacing(0.12f);
        textView.setTextColor(z ? -1 : this.ink);
        if (z) {
            i2 = -3665874;
            i = Theme.a(-3665874, 0.85f);
        } else {
            i = 0;
            i2 = this.line;
        }
        textView.setBackground(pill(i, i2));
        textView.setPadding(px(12.0f), px(6.0f), px(12.0f), px(6.0f));
        textView.setMinHeight(px(32.0f));
        textView.setGravity(17);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.lambda$chip$13(onClickListener, view);
            }
        });
        textView.setSelected(z);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMarginEnd(px(8.0f));
        this.chips.addView(textView, layoutParams);
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$chip$13(View.OnClickListener onClickListener, View view) {
        Feedback.fx(view, 2);
        onClickListener.onClick(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dots(HashMap<String, Integer> hashMap) {
        this.grid.dots = hashMap;
        this.sug.dots = hashMap;
        this.pins.dots = hashMap;
        this.list.dots = hashMap;
        this.grid.invalidate();
        this.sug.invalidate();
        this.pins.invalidate();
        this.list.invalidate();
    }

    void search(String str) {
        char c;
        JSONArray jSONArray;
        String lowerCase = str.trim().toLowerCase(Locale.ROOT);
        this.cur = lowerCase;
        boolean isEmpty = lowerCase.isEmpty();
        this.grid.set(!isEmpty ? HomeRules.search(this.apps, lowerCase) : this.main);
        if (this.aoa && this.dc.list()) {
            this.list.set(!isEmpty ? HomeRules.search(this.apps, lowerCase) : this.main);
        }
        int i = 8;
        this.sug.setVisibility((!isEmpty || this.sug.items.isEmpty()) ? 8 : 0);
        this.sugHead.setVisibility(this.sug.getVisibility());
        if (this.sugBox != null) {
            this.sugBox.setVisibility(this.sug.getVisibility());
        }
        this.pins.setVisibility((!isEmpty || this.pins.items.isEmpty()) ? 8 : 0);
        this.pinHead.setVisibility(this.pins.getVisibility());
        boolean z = this.dc.list() && (isEmpty || this.aoa);
        this.list.setVisibility(z ? 0 : 8);
        this.grid.setVisibility(z ? 8 : 0);
        Rail rail = this.rail;
        if ((z || this.aoa) && isEmpty && "az".equals(this.sort)) {
            i = 0;
        }
        rail.setVisibility(i);
        this.res.removeAllViews();
        if (isEmpty) {
            return;
        }
        final String trim = str.trim();
        for (String str2 : HomeRules.DRAWER_ORDER) {
            switch (str2.hashCode()) {
                case -567451565:
                    if (str2.equals("contacts")) {
                        c = 1;
                        break;
                    }
                    break;
                case 96773:
                    if (str2.equals("aos")) {
                        c = 0;
                        break;
                    }
                    break;
                case 117588:
                    if (str2.equals("web")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            c = 65535;
            switch (c) {
                case 0:
                    this.res.addView(sec(Strings.t("dr.aos")));
                    row(Strings.t("dr.ask", "name", HomeRules.askName(this.d.nameSet)), "\"" + trim + "\"", new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda25
                        @Override // java.lang.Runnable
                        public final void run() {
                            AppsDrawer.this.m261lambda$search$14$comaugmaosohomeAppsDrawer(trim);
                        }
                    });
                    String[] strArr = new String[SETS.length];
                    for (int i2 = 0; i2 < SETS.length; i2++) {
                        strArr[i2] = Strings.t("dr.set." + SETS[i2][0]);
                    }
                    Iterator<Integer> it = HomeRules.settingsMatch(strArr, lowerCase).iterator();
                    while (it.hasNext()) {
                        int intValue = it.next().intValue();
                        final String[] strArr2 = SETS[intValue];
                        row(strArr[intValue], null, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda26
                            @Override // java.lang.Runnable
                            public final void run() {
                                AppsDrawer.this.m262lambda$search$15$comaugmaosohomeAppsDrawer(strArr2);
                            }
                        });
                    }
                    break;
                case 1:
                    if (lowerCase.length() >= 2 && this.be.contactsOk()) {
                        try {
                            jSONArray = new JSONArray(this.be.contacts(lowerCase));
                        } catch (Exception e) {
                            jSONArray = new JSONArray();
                        }
                        int i3 = 0;
                        for (int i4 = 0; i4 < jSONArray.length() && i3 < 4; i4++) {
                            JSONObject optJSONObject = jSONArray.optJSONObject(i4);
                            if (optJSONObject != null && HomeRules.nameMatch(optJSONObject.optString("name"), lowerCase)) {
                                int i5 = i3 + 1;
                                if (i3 == 0) {
                                    this.res.addView(sec(Strings.t("dr.contacts")));
                                }
                                final String optString = optJSONObject.optString("num");
                                row(optJSONObject.optString("name"), optString, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda27
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        AppsDrawer.this.m263lambda$search$16$comaugmaosohomeAppsDrawer(optString);
                                    }
                                });
                                i3 = i5;
                            }
                        }
                    }
                    break;
                case 2:
                    this.res.addView(sec(Strings.t("dr.more")));
                    row(Strings.t("dr.web"), "\"" + trim + "\"", new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda28
                        @Override // java.lang.Runnable
                        public final void run() {
                            AppsDrawer.this.m264lambda$search$17$comaugmaosohomeAppsDrawer(trim);
                        }
                    });
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$search$14$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m261lambda$search$14$comaugmaosohomeAppsDrawer(String str) {
        ask(str);
        this.host.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$search$15$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m262lambda$search$15$comaugmaosohomeAppsDrawer(String[] strArr) {
        if ("app".equals(strArr[1])) {
            this.be.openApp(strArr[2]);
        } else {
            this.be.settings(strArr[2]);
        }
        this.host.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$search$16$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m263lambda$search$16$comaugmaosohomeAppsDrawer(String str) {
        this.be.url("tel:" + str.replaceAll("[^\\d+]", ""));
        this.host.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$search$17$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m264lambda$search$17$comaugmaosohomeAppsDrawer(String str) {
        this.be.webSearch(str);
        this.host.close();
    }

    void ask(String str) {
        if (this.d.handoff) {
            this.be.openAssistant(str, false);
        } else {
            this.be.openAsk(str);
        }
    }

    void row(String str, String str2, final Runnable runnable) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(16);
        linearLayout.setMinimumHeight(px(44.0f));
        linearLayout.setPadding(px(4.0f), 0, px(4.0f), 0);
        linearLayout.setClickable(true);
        linearLayout.setFocusable(true);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0);
        linearLayout.setBackground(new LayerDrawable(new Drawable[]{gradientDrawable}) { // from class: com.jarvis.assist.aosui.home.AppsDrawer.3
            final Paint lp = new Paint();

            {
                this.lp.setColor(Theme.a(AppsDrawer.this.line, 0.45f));
            }

            @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                super.draw(canvas);
                Rect bounds = getBounds();
                canvas.drawRect(bounds.left, bounds.bottom - Math.max(1.0f, AppsDrawer.this.dn), bounds.right, bounds.bottom, this.lp);
            }
        });
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTypeface(Fonts.RAJ_600);
        textView.setTextSize(1, this.z * 15.0f);
        textView.setTextColor(this.ink);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (str2 != null) {
            TextView textView2 = new TextView(getContext());
            textView2.setText(str2);
            textView2.setTypeface(Fonts.MONO);
            textView2.setTextSize(1, this.z * 10.0f);
            textView2.setTextColor(this.dim);
            textView2.setSingleLine(true);
            textView2.setEllipsize(TextUtils.TruncateAt.END);
            textView2.setMaxWidth((int) (getResources().getDisplayMetrics().widthPixels * 0.5f));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.setMarginStart(px(10.0f));
            linearLayout.addView(textView2, layoutParams);
        }
        linearLayout.setContentDescription(str + (str2 == null ? "" : " " + str2));
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda40
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.lambda$row$18(runnable, view);
            }
        });
        this.res.addView(linearLayout, new LinearLayout.LayoutParams(-1, -2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$row$18(Runnable runnable, View view) {
        Feedback.fx(view, 0);
        runnable.run();
    }

    void enter() {
        String trim = this.q.getText().toString().trim();
        if (trim.isEmpty()) {
            return;
        }
        if (!this.grid.items.isEmpty()) {
            launch(this.grid.items.get(0));
        } else {
            this.be.webSearch(trim);
            this.host.close();
        }
    }

    void launch(HomeRules.App app) {
        if (isFolder(app)) {
            openFolder(folderIdx(app));
        } else {
            keyboard(false);
            this.host.launch(app.pkg, app.cls, app.label);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void keyboard(boolean z) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        if (inputMethodManager == null) {
            return;
        }
        if (z) {
            this.q.requestFocus();
            inputMethodManager.showSoftInput(this.q, 0);
        } else {
            inputMethodManager.hideSoftInputFromWindow(this.q.getWindowToken(), 0);
            this.q.clearFocus();
        }
    }

    void menu(final HomeRules.App app, float f, float f2) {
        if (isFolder(app)) {
            folderMenu(folderIdx(app), f, f2);
            return;
        }
        closeMenu();
        Feedback.fx(this, 3);
        keyboard(false);
        boolean contains = this.hidden.contains(app.pkg);
        final LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setPadding(px(6.0f), px(6.0f), px(6.0f), px(6.0f));
        linearLayout.setClickable(true);
        linearLayout.setElevation(px(12.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(this.th.light ? -1 : -16118511);
        gradientDrawable.setCornerRadius(px(8.0f));
        gradientDrawable.setStroke(Math.max(1, Math.round(this.dn)), Theme.a(this.line, Math.min(1.0f, ((this.line >>> 24) / 255.0f) * 1.06f)));
        linearLayout.setBackground(gradientDrawable);
        TextView textView = new TextView(getContext());
        textView.setText(app.label);
        textView.setTypeface(Fonts.RAJ_600);
        textView.setTextSize(1, this.z * 14.0f);
        textView.setTextColor(this.ink);
        textView.setPadding(px(8.0f), px(6.0f), px(8.0f), px(8.0f));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(textView);
        item(linearLayout, Strings.t("dr.m.info"), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m244lambda$menu$19$comaugmaosohomeAppsDrawer(app);
            }
        });
        item(linearLayout, Strings.t("dr.m.dock"), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m245lambda$menu$20$comaugmaosohomeAppsDrawer(linearLayout, app);
            }
        });
        item(linearLayout, Strings.t(DrawerModel.pinned(this.dc, app.pkg) ? "dr.m.unpin" : "dr.m.pin"), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m246lambda$menu$21$comaugmaosohomeAppsDrawer(app);
            }
        });
        int folderOf = DrawerModel.folderOf(this.dc, app.pkg);
        if (folderOf >= 0) {
            item(linearLayout, Strings.t("dr.m.unfolder", "name", this.dc.folders.get(folderOf).name.toUpperCase(Locale.getDefault())), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.this.m247lambda$menu$22$comaugmaosohomeAppsDrawer(app);
                }
            });
        } else {
            item(linearLayout, Strings.t("dr.m.folder"), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.this.m248lambda$menu$23$comaugmaosohomeAppsDrawer(linearLayout, app);
                }
            });
        }
        item(linearLayout, Strings.t(contains ? "dr.m.unhide" : "dr.m.hide"), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m249lambda$menu$24$comaugmaosohomeAppsDrawer(app);
            }
        });
        item(linearLayout, Strings.t("dr.m.del"), this.red, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m250lambda$menu$25$comaugmaosohomeAppsDrawer(app);
            }
        });
        int width = getWidth();
        int height = getHeight();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(px(210.0f), -2);
        layoutParams.leftMargin = (int) Math.max(0.0f, Math.min(f, width - px(220.0f)));
        layoutParams.topMargin = (int) Math.max(0.0f, Math.min(f2, height - px(260.0f)));
        this.scrim = new View(getContext());
        this.scrim.setBackgroundColor(1711276032);
        this.scrim.setImportantForAccessibility(2);
        this.scrim.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.this.m251lambda$menu$26$comaugmaosohomeAppsDrawer(view);
            }
        });
        this.scrim.setElevation(px(11.0f));
        addView(this.scrim, new FrameLayout.LayoutParams(-1, -1));
        this.col.setImportantForAccessibility(4);
        linearLayout.setImportantForAccessibility(1);
        linearLayout.setContentDescription(app.label);
        this.menu = linearLayout;
        addView(linearLayout, layoutParams);
        VirtualA11y.pane(linearLayout, app.label);
        linearLayout.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.lambda$menu$27(linearLayout);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$menu$19$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m244lambda$menu$19$comaugmaosohomeAppsDrawer(HomeRules.App app) {
        this.be.info(app.pkg);
        closeMenu();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$menu$21$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m246lambda$menu$21$comaugmaosohomeAppsDrawer(HomeRules.App app) {
        if (!DrawerModel.togglePin(this.dc, app.pkg)) {
            this.be.toast(Strings.t("dr.pins.full"));
        }
        saveDc();
        closeMenu();
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$menu$22$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m247lambda$menu$22$comaugmaosohomeAppsDrawer(HomeRules.App app) {
        DrawerModel.removeFrom(this.dc, app.pkg);
        saveDc();
        closeMenu();
        Sheet.closeTop();
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$menu$24$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m249lambda$menu$24$comaugmaosohomeAppsDrawer(HomeRules.App app) {
        JSONObject cfg = this.d.cfg();
        ArrayList<String> strs = HomeData.strs(cfg.optJSONArray("hidden"));
        boolean contains = strs.contains(app.pkg);
        String str = app.pkg;
        if (contains) {
            strs.remove(str);
        } else {
            strs.add(str);
        }
        try {
            cfg.put("hidden", new JSONArray((Collection) strs));
        } catch (Exception e) {
        }
        Store.set("aos.cfg", cfg.toString());
        closeMenu();
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$menu$25$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m250lambda$menu$25$comaugmaosohomeAppsDrawer(HomeRules.App app) {
        this.be.uninstall(app.pkg);
        closeMenu();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$menu$26$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m251lambda$menu$26$comaugmaosohomeAppsDrawer(View view) {
        closeMenu();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$menu$27(LinearLayout linearLayout) {
        if (linearLayout.getChildCount() > 1) {
            linearLayout.getChildAt(1).sendAccessibilityEvent(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: dockSlots, reason: merged with bridge method [inline-methods] */
    public void m245lambda$menu$20$comaugmaosohomeAppsDrawer(LinearLayout linearLayout, final HomeRules.App app) {
        while (linearLayout.getChildCount() > 1) {
            linearLayout.removeViewAt(1);
        }
        final int i = 0;
        ((TextView) linearLayout.getChildAt(0)).setText("REPLACE DOCK SLOT");
        while (i < 4) {
            int i2 = i + 1;
            item(linearLayout, i2 + " · " + (i < this.d.dock.size() ? this.d.dock.get(i).label : "—"), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.this.m237lambda$dockSlots$28$comaugmaosohomeAppsDrawer(i, app);
                }
            });
            i = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$dockSlots$28$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m237lambda$dockSlots$28$comaugmaosohomeAppsDrawer(int i, HomeRules.App app) {
        JSONObject cfg = this.d.cfg();
        JSONArray optJSONArray = cfg.optJSONArray("dock");
        if (optJSONArray == null) {
            optJSONArray = new JSONArray();
            Iterator<HomeData.Entry> it = this.d.dock.iterator();
            while (it.hasNext()) {
                optJSONArray.put(it.next().id);
            }
        }
        while (optJSONArray.length() <= i) {
            try {
                optJSONArray.put("dial");
            } catch (Exception e) {
            }
        }
        optJSONArray.put(i, "app:" + app.pkg + "/" + app.cls + "|" + app.label);
        cfg.put("dock", optJSONArray);
        Store.set("aos.cfg", cfg.toString());
        closeMenu();
        this.host.dockChanged();
        this.host.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: folderPick, reason: merged with bridge method [inline-methods] */
    public void m248lambda$menu$23$comaugmaosohomeAppsDrawer(LinearLayout linearLayout, final HomeRules.App app) {
        while (linearLayout.getChildCount() > 1) {
            linearLayout.removeViewAt(1);
        }
        ((TextView) linearLayout.getChildAt(0)).setText(Strings.t("dr.m.folder.h"));
        for (final int i = 0; i < this.dc.folders.size(); i++) {
            item(linearLayout, this.dc.folders.get(i).name.toUpperCase(Locale.getDefault()), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.this.m241lambda$folderPick$29$comaugmaosohomeAppsDrawer(i, app);
                }
            });
        }
        if (this.dc.folders.size() < 24) {
            item(linearLayout, Strings.t("dr.f.new"), this.acc, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.this.m243lambda$folderPick$31$comaugmaosohomeAppsDrawer(app);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$folderPick$29$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m241lambda$folderPick$29$comaugmaosohomeAppsDrawer(int i, HomeRules.App app) {
        DrawerModel.addTo(this.dc, i, app.pkg);
        saveDc();
        closeMenu();
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$folderPick$30$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m242lambda$folderPick$30$comaugmaosohomeAppsDrawer(HomeRules.App app, String str) {
        DrawerModel.create(this.dc, str, app.pkg);
        saveDc();
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$folderPick$31$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m243lambda$folderPick$31$comaugmaosohomeAppsDrawer(final HomeRules.App app) {
        closeMenu();
        nameSheet(Strings.t("dr.f.new.h"), "", new Consumer() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AppsDrawer.this.m242lambda$folderPick$30$comaugmaosohomeAppsDrawer(app, (String) obj);
            }
        });
    }

    void folderMenu(final int i, float f, float f2) {
        if (i < 0 || i >= this.dc.folders.size()) {
            return;
        }
        closeMenu();
        Feedback.fx(this, 3);
        keyboard(false);
        String str = this.dc.folders.get(i).name;
        LinearLayout menuBox = menuBox(str);
        item(menuBox, Strings.t("dr.f.open"), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m238lambda$folderMenu$32$comaugmaosohomeAppsDrawer(i);
            }
        });
        item(menuBox, Strings.t("dr.f.rename"), this.ink, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m239lambda$folderMenu$33$comaugmaosohomeAppsDrawer(i);
            }
        });
        item(menuBox, Strings.t("dr.f.delete"), this.red, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m240lambda$folderMenu$34$comaugmaosohomeAppsDrawer(i);
            }
        });
        showMenu(menuBox, str, f, f2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$folderMenu$32$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m238lambda$folderMenu$32$comaugmaosohomeAppsDrawer(int i) {
        closeMenu();
        openFolder(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$folderMenu$33$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m239lambda$folderMenu$33$comaugmaosohomeAppsDrawer(int i) {
        closeMenu();
        m259lambda$openFolder$37$comaugmaosohomeAppsDrawer(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$folderMenu$34$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m240lambda$folderMenu$34$comaugmaosohomeAppsDrawer(int i) {
        DrawerModel.delete(this.dc, i);
        saveDc();
        closeMenu();
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$renameFolder$35$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m260lambda$renameFolder$35$comaugmaosohomeAppsDrawer(int i, String str) {
        DrawerModel.rename(this.dc, i, str);
        saveDc();
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: renameFolder, reason: merged with bridge method [inline-methods] */
    public void m259lambda$openFolder$37$comaugmaosohomeAppsDrawer(final int i) {
        nameSheet(Strings.t("dr.f.rename.h"), this.dc.folders.get(i).name, new Consumer() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AppsDrawer.this.m260lambda$renameFolder$35$comaugmaosohomeAppsDrawer(i, (String) obj);
            }
        });
    }

    void openFolder(final int i) {
        if (i < 0 || i >= this.dc.folders.size()) {
            return;
        }
        DrawerModel.Folder folder = this.dc.folders.get(i);
        boolean z = false;
        keyboard(false);
        final Sheet title = new Sheet(getContext(), this.th, folder.name).title(folder.name.toUpperCase(Locale.getDefault()));
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = folder.apps.iterator();
        while (it.hasNext()) {
            HomeRules.App app = app(it.next());
            if (app != null) {
                arrayList.add(app);
            }
        }
        HomeRules.sort(arrayList, "az", new HashMap(), new ArrayList());
        if (arrayList.isEmpty()) {
            title.para(Strings.t("dr.f.empty"), 13.0f, 0.7f);
        } else {
            AppGrid appGrid = new AppGrid(getContext(), z) { // from class: com.jarvis.assist.aosui.home.AppsDrawer.4
                @Override // com.jarvis.assist.aosui.home.AppsDrawer.AppGrid
                void open(HomeRules.App app2) {
                    title.close();
                    AppsDrawer.this.launch(app2);
                }
            };
            appGrid.set(arrayList);
            appGrid.dots = this.d.pkgDot;
            title.body().addView(appGrid, new LinearLayout.LayoutParams(-1, -2));
        }
        title.buttons(new String[]{Strings.t("dr.f.delete"), Strings.t("dr.f.rename"), Strings.t("dr.f.done")}, new Runnable[]{new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m258lambda$openFolder$36$comaugmaosohomeAppsDrawer(i);
            }
        }, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m259lambda$openFolder$37$comaugmaosohomeAppsDrawer(i);
            }
        }, null}, true);
        title.show(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$openFolder$36$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m258lambda$openFolder$36$comaugmaosohomeAppsDrawer(int i) {
        DrawerModel.delete(this.dc, i);
        saveDc();
        reload();
    }

    void nameSheet(String str, String str2, final Consumer<String> consumer) {
        Sheet title = new Sheet(getContext(), this.th, str).title(str);
        final EditText editText = new EditText(getContext());
        editText.setText(str2);
        editText.setHint(Strings.t("dr.f.name.ph"));
        editText.setSingleLine(true);
        editText.setTypeface(Fonts.RAJ_600);
        editText.setTextSize(1, this.z * 16.0f);
        editText.setTextColor(this.ink);
        editText.setHintTextColor(this.dim);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
        editText.setBackground(pill(0, this.line));
        editText.setPadding(px(16.0f), px(10.0f), px(16.0f), px(10.0f));
        editText.setContentDescription(Strings.t("dr.f.name.ph"));
        editText.setSelectAllOnFocus(true);
        title.body().addView(editText, new LinearLayout.LayoutParams(-1, -2));
        title.buttons(new String[]{Strings.t("dr.f.cancel"), Strings.t("dr.f.save")}, new Runnable[]{null, new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(editText.getText().toString());
            }
        }}, true);
        title.show(this);
        editText.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.this.m252lambda$nameSheet$39$comaugmaosohomeAppsDrawer(editText);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$nameSheet$39$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m252lambda$nameSheet$39$comaugmaosohomeAppsDrawer(EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, 0);
        }
    }

    LinearLayout menuBox(String str) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setPadding(px(6.0f), px(6.0f), px(6.0f), px(6.0f));
        linearLayout.setClickable(true);
        linearLayout.setElevation(px(12.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(this.th.light ? -1 : -16118511);
        gradientDrawable.setCornerRadius(px(8.0f));
        gradientDrawable.setStroke(Math.max(1, Math.round(this.dn)), this.line);
        linearLayout.setBackground(gradientDrawable);
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTypeface(Fonts.RAJ_600);
        textView.setTextSize(1, this.z * 14.0f);
        textView.setTextColor(this.ink);
        textView.setPadding(px(8.0f), px(6.0f), px(8.0f), px(8.0f));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(textView);
        return linearLayout;
    }

    void showMenu(final LinearLayout linearLayout, String str, float f, float f2) {
        int width = getWidth();
        int height = getHeight();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(px(210.0f), -2);
        layoutParams.leftMargin = (int) Math.max(0.0f, Math.min(f, width - px(220.0f)));
        layoutParams.topMargin = (int) Math.max(0.0f, Math.min(f2, height - px(260.0f)));
        this.scrim = new View(getContext());
        this.scrim.setBackgroundColor(1711276032);
        this.scrim.setImportantForAccessibility(2);
        this.scrim.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda30
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.this.m265lambda$showMenu$40$comaugmaosohomeAppsDrawer(view);
            }
        });
        this.scrim.setElevation(px(11.0f));
        addView(this.scrim, new FrameLayout.LayoutParams(-1, -1));
        this.col.setImportantForAccessibility(4);
        linearLayout.setImportantForAccessibility(1);
        linearLayout.setContentDescription(str);
        this.menu = linearLayout;
        addView(linearLayout, layoutParams);
        VirtualA11y.pane(linearLayout, str);
        linearLayout.post(new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                AppsDrawer.lambda$showMenu$41(linearLayout);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showMenu$40$com-augma-oso-home-AppsDrawer, reason: not valid java name */
    public /* synthetic */ void m265lambda$showMenu$40$comaugmaosohomeAppsDrawer(View view) {
        closeMenu();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$showMenu$41(LinearLayout linearLayout) {
        if (linearLayout.getChildCount() > 1) {
            linearLayout.getChildAt(1).sendAccessibilityEvent(8);
        }
    }

    void item(LinearLayout linearLayout, String str, int i, final Runnable runnable) {
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setTypeface(Fonts.MONO);
        textView.setTextSize(1, this.z * 10.0f);
        textView.setLetterSpacing(0.12f);
        textView.setTextColor(i);
        textView.setGravity(16);
        textView.setMinHeight(px(42.0f));
        textView.setPadding(px(8.0f), 0, px(8.0f), 0);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$$ExternalSyntheticLambda39
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppsDrawer.lambda$item$42(runnable, view);
            }
        });
        View view = new View(getContext());
        view.setBackgroundColor(Theme.a(this.line, 0.4f));
        linearLayout.addView(view, new LinearLayout.LayoutParams(-1, Math.max(1, Math.round(this.dn))));
        linearLayout.addView(textView, new LinearLayout.LayoutParams(-1, -2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$item$42(Runnable runnable, View view) {
        Feedback.fx(view, 0);
        runnable.run();
    }

    boolean closeMenu() {
        if (this.scrim != null) {
            removeView(this.scrim);
            this.scrim = null;
        }
        this.col.setImportantForAccessibility(0);
        if (this.menu == null) {
            return false;
        }
        removeView(this.menu);
        this.menu = null;
        return true;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            return true;
        }
        closeMenu();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onBack() {
        return closeMenu() || Sheet.closeTop();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestApplyInsets();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void open() {
        if (Perf.k().calm) {
            setAlpha(1.0f);
            return;
        }
        setAlpha(0.0f);
        setTranslationY(this.dn * 24.0f);
        animate().alpha(1.0f).translationY(0.0f).setDuration(280L).setInterpolator(EASE).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close(Runnable runnable) {
        keyboard(false);
        closeMenu();
        if (Perf.k().calm) {
            runnable.run();
        } else {
            animate().alpha(0.0f).setDuration(140L).withEndAction(runnable).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public class AppGrid extends View {
        final VirtualA11y a11y;
        Txt aHd;
        Txt aIdx;
        Txt aLab;
        int cols;
        float cw;
        HashMap<String, Integer> dots;
        int down;
        int drawnBot;
        int drawnTop;
        final String[] fit;
        final boolean fixed;
        float gapY;
        final ArrayList<String> hL;
        final ArrayList<Integer> hN;
        float[] hTop;
        final int iconPx;
        final Runnable inval;
        final ArrayList<HomeRules.App> items;
        float[] ix;
        float[] iy;
        final Txt lab;
        boolean longFired;
        final Runnable longPress;
        final Paint p;
        final RectF rf;
        float rowH;
        final int slop;
        float tH;
        float tw;
        final Rect vis;
        float x0;
        float y0;

        AppGrid(Context context, boolean z) {
            super(context);
            this.items = new ArrayList<>();
            this.p = new Paint(3);
            this.vis = new Rect();
            this.rf = new RectF();
            this.fit = new String[1024];
            this.cols = 4;
            this.drawnTop = -1;
            this.drawnBot = -1;
            this.inval = new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$AppGrid$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.AppGrid.this.invalidate();
                }
            };
            this.ix = new float[0];
            this.iy = new float[0];
            this.hTop = new float[0];
            this.hL = new ArrayList<>();
            this.hN = new ArrayList<>();
            this.down = -1;
            this.slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            this.longPress = new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$AppGrid$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.AppGrid.this.m266lambda$new$0$comaugmaosohomeAppsDrawer$AppGrid();
                }
            };
            this.fixed = z;
            this.lab = new Txt(Fonts.RAJ_600, AppsDrawer.this.dn * 12.0f, 0.0f).align(Paint.Align.CENTER);
            this.iconPx = AppsDrawer.this.px(48.0f);
            setPadding(z ? AppsDrawer.this.px(6.0f) : 0, 0, z ? AppsDrawer.this.px(6.0f) : 0, z ? AppsDrawer.this.px(10.0f) : 0);
            this.a11y = new VirtualA11y(this, new VirtualA11y.Src() { // from class: com.jarvis.assist.aosui.home.AppsDrawer.AppGrid.1
                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public int count() {
                    return AppGrid.this.items.size();
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public void bounds(int i, Rect rect) {
                    if (AppsDrawer.this.aoa && i < AppGrid.this.ix.length) {
                        rect.set((int) AppGrid.this.ix[i], (int) AppGrid.this.iy[i], (int) (AppGrid.this.ix[i] + AppGrid.this.tw), (int) (AppGrid.this.iy[i] + AppGrid.this.tH));
                        return;
                    }
                    int i2 = i / AppGrid.this.cols;
                    float paddingLeft = AppGrid.this.getPaddingLeft() + ((i % AppGrid.this.cols) * (AppGrid.this.cw + AppsDrawer.this.px(8.0f)));
                    float f = i2 * (AppGrid.this.rowH + AppGrid.this.gapY);
                    rect.set((int) paddingLeft, (int) f, (int) (paddingLeft + AppGrid.this.cw), (int) (f + AppGrid.this.rowH));
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public String label(int i) {
                    HomeRules.App app = AppGrid.this.items.get(i);
                    if (AppsDrawer.isFolder(app)) {
                        return Strings.t("dr.f.a11y", "name", app.label, "n", Integer.valueOf(AppsDrawer.this.folderApps(AppsDrawer.folderIdx(app)).size()));
                    }
                    String str = app.label;
                    String str2 = "";
                    String str3 = AppsDrawer.this.hidden.contains(app.pkg) ? ", hidden" : "";
                    if (AppGrid.this.dots != null && AppGrid.this.dots.containsKey(app.pkg)) {
                        str2 = ", has notifications";
                    }
                    return str + str3 + str2;
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public boolean click(int i) {
                    AppGrid.this.open(AppGrid.this.items.get(i));
                    return true;
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public boolean hasLong(int i) {
                    return true;
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public boolean longClick(int i) {
                    bounds(i, new Rect());
                    AppGrid.this.getLocationOnScreen(new int[2]);
                    AppsDrawer.this.getLocationOnScreen(new int[2]);
                    AppsDrawer.this.menu(AppGrid.this.items.get(i), (r2[0] - r1[0]) + r0.centerX(), (r2[1] - r1[1]) + r0.bottom);
                    return true;
                }
            });
        }

        void open(HomeRules.App app) {
            AppsDrawer.this.launch(app);
        }

        void set(List<HomeRules.App> list) {
            this.items.clear();
            this.items.addAll(list);
            Arrays.fill(this.fit, (Object) null);
            this.drawnBot = -1;
            this.drawnTop = -1;
            requestLayout();
            invalidate();
            this.a11y.contentChanged();
        }

        boolean sectioned() {
            return AppsDrawer.this.aoa && !this.fixed && "az".equals(AppsDrawer.this.sort) && AppsDrawer.this.cur.isEmpty();
        }

        void aoaMeasure(int i) {
            AppsDrawer appsDrawer;
            float f;
            float paddingLeft = (i - getPaddingLeft()) - getPaddingRight();
            float px = AppsDrawer.this.px(6.0f);
            float px2 = AppsDrawer.this.px(26.0f);
            this.cols = this.fixed ? 4 : 3;
            boolean z = true;
            this.tw = (paddingLeft - ((this.cols - 1) * px)) / this.cols;
            if (this.fixed) {
                appsDrawer = AppsDrawer.this;
                f = 70.0f;
            } else {
                appsDrawer = AppsDrawer.this;
                f = 78.0f;
            }
            this.tH = appsDrawer.px(f);
            if (this.aLab == null) {
                this.aLab = new Txt(Fonts.MONO, AppsDrawer.this.dn * 7.2f * AppsDrawer.this.z, AppsDrawer.this.dn * 1.1f * AppsDrawer.this.z).align(Paint.Align.CENTER);
                this.aIdx = new Txt(Fonts.MONO, AppsDrawer.this.dn * 6.5f, AppsDrawer.this.dn * 0.3f).align(Paint.Align.RIGHT);
                this.aHd = new Txt(Fonts.MONO, AppsDrawer.this.dn * 8.0f, AppsDrawer.this.dn * 1.4f);
            }
            int size = this.items.size();
            this.ix = new float[size];
            this.iy = new float[size];
            this.hL.clear();
            this.hN.clear();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            Iterator<HomeRules.App> it = this.items.iterator();
            while (true) {
                String str = "·";
                if (!it.hasNext()) {
                    break;
                }
                HomeRules.App next = it.next();
                if (!AppsDrawer.isFolder(next)) {
                    str = DrawerModel.letter(next.label);
                }
                arrayList2.add(str);
            }
            ArrayList arrayList3 = new ArrayList();
            List sections = sectioned() ? AoaDrawer.sections(arrayList2, arrayList3) : new ArrayList();
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            float f2 = 0.0f;
            while (i3 < size) {
                boolean z2 = z;
                if (i4 < sections.size() && ((int[]) sections.get(i4))[i2] == i3) {
                    String str2 = (String) arrayList3.get(i4);
                    if (!"·".equals(str2)) {
                        if (i5 != 0) {
                            f2 += this.tH + px;
                            i5 = i2;
                        }
                        arrayList.add(Float.valueOf(f2));
                        this.hL.add(str2);
                        this.hN.add(Integer.valueOf(((int[]) sections.get(i4))[z2 ? 1 : 0]));
                        f2 += px2;
                    }
                    i4++;
                }
                int i6 = size;
                this.ix[i3] = getPaddingLeft() + (i5 * (this.tw + px));
                this.iy[i3] = f2;
                i5++;
                if (i5 == this.cols && i3 < i6 - 1) {
                    f2 += this.tH + px;
                    i5 = 0;
                }
                i3++;
                z = z2 ? 1 : 0;
                size = i6;
                i2 = 0;
            }
            int i7 = size;
            this.hTop = new float[arrayList.size()];
            for (int i8 = 0; i8 < this.hTop.length; i8++) {
                this.hTop[i8] = ((Float) arrayList.get(i8)).floatValue();
            }
            float f3 = i7 == 0 ? 0.0f : f2 + this.tH;
            this.rowH = this.tH;
            this.gapY = px;
            setMeasuredDimension(i, ((int) Math.ceil(f3)) + getPaddingBottom());
        }

        float yOf(String str) {
            int jump = DrawerModel.jump(this.hL, str);
            if (jump < 0) {
                return 0.0f;
            }
            return this.hTop[jump];
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r21v0 */
        /* JADX WARN: Type inference failed for: r21v1 */
        /* JADX WARN: Type inference failed for: r21v2 */
        /* JADX WARN: Type inference failed for: r21v3 */
        /* JADX WARN: Type inference failed for: r21v4 */
        /* JADX WARN: Type inference failed for: r21v5 */
        void aoaDraw(Canvas canvas) {
            float f;
            char c;
            char c2;
            char c3;
            int i;
            char c4;
            float f2;
            HomeRules.App app;
            int i2;
            float f3;
            float f4;
            Integer num;
            int max = Math.max(1, AppsDrawer.this.sc.getHeight());
            int i3 = 0;
            if (!getLocalVisibleRect(this.vis)) {
                this.vis.set(0, 0, getWidth(), Math.min(getHeight(), max));
            }
            float f5 = this.vis.top - max;
            float f6 = this.vis.bottom + max;
            int px = AppsDrawer.this.px(28.0f);
            this.p.setShader(null);
            int i4 = 0;
            while (true) {
                f = 0.14f;
                c = 220;
                c2 = 0;
                if (i4 >= this.hTop.length) {
                    break;
                }
                float f7 = this.hTop[i4];
                if (f7 <= f6 && AppsDrawer.this.px(26.0f) + f7 >= f5) {
                    float px2 = f7 + AppsDrawer.this.px(13.0f);
                    this.aHd.align(Paint.Align.LEFT);
                    this.aHd.draw(canvas, this.hL.get(i4), getPaddingLeft(), px2 + (this.aHd.cap / 2.0f), -34278);
                    this.aHd.align(Paint.Align.RIGHT);
                    String valueOf = String.valueOf(this.hN.get(i4));
                    this.aHd.draw(canvas, valueOf, getWidth() - getPaddingRight(), px2 + (this.aHd.cap / 2.0f), AppsDrawer.AD);
                    this.aHd.align(Paint.Align.LEFT);
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(Theme.rgba(192, 214, 220, 0.14f));
                    canvas.drawRect(AppsDrawer.this.px(8.0f) + getPaddingLeft() + this.aHd.w(this.hL.get(i4)), px2, ((getWidth() - getPaddingRight()) - this.aHd.w(valueOf)) - AppsDrawer.this.px(8.0f), px2 + Math.max(1.0f, AppsDrawer.this.dn), this.p);
                }
                i4++;
            }
            Canvas canvas2 = canvas;
            boolean z = 192;
            while (i3 < this.items.size() && i3 < this.ix.length) {
                float f8 = this.ix[i3];
                float f9 = this.iy[i3];
                if (f9 > f6) {
                    c3 = c2;
                    i = i3;
                    c4 = c;
                    r21 = z;
                } else if (this.tH + f9 < f5) {
                    c3 = c2;
                    i = i3;
                    c4 = c;
                    r21 = z;
                } else {
                    HomeRules.App app2 = this.items.get(i3);
                    int i5 = AppsDrawer.this.hidden.contains(app2.pkg) ? 115 : 255;
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(Theme.rgba(10, 17, 20, 0.6f));
                    this.rf.set(f8, f9, this.tw + f8, this.tH + f9);
                    canvas2.drawRect(this.rf, this.p);
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(Math.max(1.0f, AppsDrawer.this.dn * 0.8f));
                    this.p.setColor(this.a11y.focused == i3 ? -11624262 : Theme.rgba(192, 214, 220, f));
                    canvas2.drawRect(this.rf, this.p);
                    this.p.setStyle(Paint.Style.FILL);
                    if (this.fixed) {
                        f2 = f9;
                        app = app2;
                        i2 = i5;
                        r21 = 192;
                    } else {
                        app = app2;
                        f2 = f9;
                        i2 = i5;
                        r21 = 192;
                        this.aIdx.draw(canvas2, AoaDrawer.idx(i3), (this.tw + f8) - AppsDrawer.this.px(4.0f), AppsDrawer.this.px(4.0f) + f9 + this.aIdx.cap, -34278);
                    }
                    float f10 = f8 + (this.tw / 2.0f);
                    float f11 = (this.tH * 0.4f) + f2;
                    if (AppsDrawer.isFolder(app)) {
                        f3 = f10;
                        f4 = f8;
                        c4 = 220;
                        i = i3;
                        canvas2 = canvas;
                        AppsDrawer.this.folderIcon(canvas2, this.p, this.rf, AppsDrawer.folderIdx(app), f3, f11, px, this.inval);
                        i2 = i2;
                        num = null;
                    } else {
                        canvas2 = canvas;
                        f3 = f10;
                        i = i3;
                        f4 = f8;
                        c4 = 220;
                        Bitmap bitmap = IconCache.get(getContext()).get(app.pkg, app.cls, px, this.inval);
                        if (bitmap != null) {
                            this.p.setAlpha(i2);
                            float f12 = px / 2.0f;
                            this.rf.set(f3 - f12, f11 - f12, f3 + f12, f12 + f11);
                            num = null;
                            canvas2.drawBitmap(bitmap, (Rect) null, this.rf, this.p);
                            this.p.setAlpha(255);
                        } else {
                            num = null;
                        }
                    }
                    c3 = 0;
                    this.aLab.draw(canvas2, WidgetView.fitText(this.aLab, app.label.toUpperCase(Locale.getDefault()), this.tw - AppsDrawer.this.px(8.0f)), f3, f2 + (this.tH * 0.82f), Theme.a(AppsDrawer.AW, i2 / 255.0f));
                    Integer num2 = this.dots == null ? num : this.dots.get(app.pkg);
                    if (num2 != null) {
                        float px3 = f4 + AppsDrawer.this.px(9.0f);
                        float px4 = AppsDrawer.this.px(9.0f) + f2;
                        this.p.setColor(num2.intValue() == 3 ? AppsDrawer.AD : -34278);
                        canvas2.drawCircle(px3, px4, AppsDrawer.this.px(4.5f), this.p);
                    }
                }
                i3 = i + 1;
                c2 = c3;
                z = r21;
                c = c4;
                f = 0.14f;
            }
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            AppsDrawer appsDrawer;
            float f;
            int size = View.MeasureSpec.getSize(i);
            int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
            float px = AppsDrawer.this.px(8.0f);
            if (AppsDrawer.this.aoa) {
                aoaMeasure(size);
                return;
            }
            this.cols = this.fixed ? 4 : Math.max(1, (int) Math.floor((paddingLeft + px) / (AppsDrawer.this.px(72.0f) + px)));
            this.cw = (paddingLeft - ((this.cols - 1) * px)) / this.cols;
            if (this.fixed) {
                appsDrawer = AppsDrawer.this;
                f = 4.0f;
            } else {
                appsDrawer = AppsDrawer.this;
                f = 16.0f;
            }
            this.gapY = appsDrawer.px(f);
            this.rowH = this.iconPx + AppsDrawer.this.px(6.0f) + this.lab.line();
            setMeasuredDimension(size, ((this.items.size() + this.cols) - 1) / this.cols == 0 ? 0 : ((int) Math.ceil((r7 * this.rowH) + ((r7 - 1) * this.gapY))) + getPaddingBottom());
        }

        void window() {
            if (!getLocalVisibleRect(this.vis)) {
                if (this.drawnTop != -1) {
                    this.drawnBot = -1;
                    this.drawnTop = -1;
                    return;
                }
                return;
            }
            int height = AppsDrawer.this.sc.getHeight();
            if (this.drawnTop >= 0) {
                int i = height / 3;
                if ((this.vis.top >= this.drawnTop + i || this.drawnTop <= 0) && (this.vis.bottom <= this.drawnBot - i || this.drawnBot >= getHeight())) {
                    return;
                }
            }
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            Integer num;
            float f2;
            boolean z;
            Canvas canvas2 = canvas;
            int max = Math.max(1, AppsDrawer.this.sc.getHeight());
            if (!getLocalVisibleRect(this.vis)) {
                this.vis.set(0, 0, getWidth(), Math.min(getHeight(), max));
            }
            this.drawnTop = Math.max(0, this.vis.top - max);
            this.drawnBot = this.vis.bottom + max;
            if (AppsDrawer.this.aoa) {
                aoaDraw(canvas);
                return;
            }
            float f3 = this.rowH + this.gapY;
            int max2 = Math.max(0, (int) (this.drawnTop / f3));
            int ceil = (int) Math.ceil(this.drawnBot / f3);
            int i = this.iconPx;
            float px = AppsDrawer.this.px(76.0f);
            int i2 = max2 * this.cols;
            while (i2 < Math.min(this.items.size(), (ceil + 1) * this.cols)) {
                HomeRules.App app = this.items.get(i2);
                int i3 = i2 / this.cols;
                float paddingLeft = getPaddingLeft() + ((i2 % this.cols) * (this.cw + AppsDrawer.this.px(8.0f)));
                float f4 = i3 * f3;
                float f5 = (this.cw / 2.0f) + paddingLeft;
                int i4 = AppsDrawer.this.hidden.contains(app.pkg) ? 115 : 255;
                this.p.setShader(null);
                this.p.setStyle(Paint.Style.FILL);
                float f6 = i4;
                this.p.setColor(Theme.a(-3615006, (0.06f * f6) / 255.0f));
                float f7 = i;
                float f8 = f7 / 2.0f;
                float f9 = f4 + f8;
                canvas2.drawCircle(f5, f9, f8, this.p);
                if (AppsDrawer.isFolder(app)) {
                    f = f3;
                    AppsDrawer.this.folderIcon(canvas2, this.p, this.rf, AppsDrawer.folderIdx(app), f5, f9, f7, this.inval);
                    canvas2 = canvas;
                    this.lab.draw(canvas2, WidgetView.fitText(this.lab, app.label, px), f5, this.lab.asc + f4 + f7 + AppsDrawer.this.px(6.0f), AppsDrawer.this.ink);
                    z = true;
                } else {
                    f = f3;
                    Bitmap bitmap = IconCache.get(getContext()).get(app.pkg, app.cls, i, this.inval);
                    if (bitmap != null) {
                        this.p.setAlpha(i4);
                        this.rf.set(f5 - f8, f4, f5 + f8, f4 + f7);
                        num = null;
                        canvas2.drawBitmap(bitmap, (Rect) null, this.rf, this.p);
                        this.p.setAlpha(255);
                    } else {
                        num = null;
                    }
                    if (i2 < this.fit.length) {
                        if (this.fit[i2] == null) {
                            this.fit[i2] = WidgetView.fitText(this.lab, app.label, px);
                        }
                        f2 = f5;
                        this.lab.draw(canvas2, this.fit[i2], f2, this.lab.asc + f4 + f7 + AppsDrawer.this.px(6.0f), Theme.a(AppsDrawer.this.ink, f6 / 255.0f));
                        canvas2 = canvas;
                    } else {
                        f2 = f5;
                        canvas2 = canvas;
                        this.lab.draw(canvas2, WidgetView.fitText(this.lab, app.label, px), f2, this.lab.asc + f4 + f7 + AppsDrawer.this.px(6.0f), Theme.a(AppsDrawer.this.ink, f6 / 255.0f));
                    }
                    Integer num2 = this.dots == null ? num : this.dots.get(app.pkg);
                    if (num2 != null) {
                        float f10 = f2 + (0.36f * f7);
                        float f11 = f4 + (f7 * 0.14f);
                        z = true;
                        this.p.setColor(num2.intValue() == 1 ? AppsDrawer.this.th.RED : num2.intValue() == 2 ? AppsDrawer.this.th.BLUE : AppsDrawer.this.th.DIM);
                        canvas2.drawCircle(f10, f11, AppsDrawer.this.px(5.0f), this.p);
                        this.p.setStyle(Paint.Style.STROKE);
                        this.p.setStrokeWidth(Math.max(1.0f, AppsDrawer.this.dn * 1.5f));
                        this.p.setColor(AppsDrawer.this.th.light ? Palette.LIGHT_BG : -16579579);
                        canvas2.drawCircle(f10, f11, AppsDrawer.this.px(5.0f), this.p);
                        this.p.setStyle(Paint.Style.FILL);
                    } else {
                        z = true;
                    }
                    if (this.a11y.focused == i2) {
                        this.p.setStyle(Paint.Style.STROKE);
                        this.p.setStrokeWidth(AppsDrawer.this.dn * 2.0f);
                        this.p.setColor(AppsDrawer.this.acc);
                        this.rf.set(paddingLeft, f4 - AppsDrawer.this.px(2.0f), paddingLeft + this.cw, this.rowH + f4);
                        canvas2.drawRoundRect(this.rf, AppsDrawer.this.px(8.0f), AppsDrawer.this.px(8.0f), this.p);
                        this.p.setStyle(Paint.Style.FILL);
                    }
                }
                i2++;
                f3 = f;
            }
        }

        int at(float f, float f2) {
            int paddingLeft;
            int i;
            if (!AppsDrawer.this.aoa) {
                float f3 = this.rowH + this.gapY;
                int i2 = (int) (f2 / f3);
                if (f2 - (i2 * f3) <= this.rowH && (paddingLeft = (int) ((f - getPaddingLeft()) / (this.cw + AppsDrawer.this.px(8.0f)))) >= 0 && paddingLeft < this.cols && (f - getPaddingLeft()) - (paddingLeft * (this.cw + AppsDrawer.this.px(8.0f))) <= this.cw && (i = (i2 * this.cols) + paddingLeft) < this.items.size()) {
                    return i;
                }
                return -1;
            }
            for (int i3 = 0; i3 < this.ix.length && i3 < this.items.size(); i3++) {
                if (f >= this.ix[i3] && f <= this.ix[i3] + this.tw && f2 >= this.iy[i3] && f2 <= this.iy[i3] + this.tH) {
                    return i3;
                }
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-augma-oso-home-AppsDrawer$AppGrid, reason: not valid java name */
        public /* synthetic */ void m266lambda$new$0$comaugmaosohomeAppsDrawer$AppGrid() {
            if (this.down >= 0) {
                this.longFired = true;
                getLocationOnScreen(new int[2]);
                AppsDrawer.this.getLocationOnScreen(new int[2]);
                AppsDrawer.this.menu(this.items.get(this.down), (r2[0] - r1[0]) + this.x0, (r2[1] - r1[1]) + this.y0);
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case 0:
                    AppsDrawer.this.closeMenu();
                    this.x0 = motionEvent.getX();
                    this.y0 = motionEvent.getY();
                    this.down = at(this.x0, this.y0);
                    this.longFired = false;
                    if (this.down >= 0) {
                        postDelayed(this.longPress, 480L);
                    }
                    return true;
                case 1:
                    removeCallbacks(this.longPress);
                    if (this.down >= 0 && !this.longFired && SystemClock.uptimeMillis() - motionEvent.getDownTime() < 600) {
                        Feedback.fx(this, 0);
                        open(this.items.get(this.down));
                    }
                    this.down = -1;
                    return true;
                case 2:
                    if (Math.hypot(motionEvent.getX() - this.x0, motionEvent.getY() - this.y0) > this.slop) {
                        removeCallbacks(this.longPress);
                        this.down = -1;
                    }
                    return true;
                case 3:
                    removeCallbacks(this.longPress);
                    this.down = -1;
                    return true;
                default:
                    return super.onTouchEvent(motionEvent);
            }
        }

        @Override // android.view.View
        public AccessibilityNodeProvider getAccessibilityNodeProvider() {
            return this.a11y;
        }

        @Override // android.view.View
        protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
            return this.a11y.hover(motionEvent) || super.dispatchHoverEvent(motionEvent);
        }
    }

    List<HomeRules.App> folderApps(int i) {
        ArrayList arrayList = new ArrayList();
        if (i >= 0 && i < this.dc.folders.size()) {
            Iterator<String> it = this.dc.folders.get(i).apps.iterator();
            while (it.hasNext()) {
                HomeRules.App app = app(it.next());
                if (app != null) {
                    arrayList.add(app);
                }
            }
        }
        return arrayList;
    }

    void folderIcon(Canvas canvas, Paint paint, RectF rectF, int i, float f, float f2, float f3, Runnable runnable) {
        Object obj;
        AppsDrawer appsDrawer = this;
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(1.0f, appsDrawer.dn));
        paint.setColor(appsDrawer.acc);
        float f4 = 2.0f;
        canvas.drawCircle(f, f2, (f3 / 2.0f) - appsDrawer.dn, paint);
        paint.setStyle(Paint.Style.FILL);
        List<HomeRules.App> folderApps = appsDrawer.folderApps(i);
        float f5 = 0.2f * f3;
        int round = Math.round(0.27f * f3);
        int i2 = 0;
        while (i2 < Math.min(4, folderApps.size())) {
            float f6 = (i2 % 2 == 0 ? -f5 : f5) + f;
            float f7 = (i2 < 2 ? -f5 : f5) + f2;
            float f8 = f4;
            Bitmap bitmap = IconCache.get(appsDrawer.getContext()).get(folderApps.get(i2).pkg, folderApps.get(i2).cls, round, runnable);
            if (bitmap != null) {
                paint.setAlpha(255);
                float f9 = round / f8;
                rectF.set(f6 - f9, f7 - f9, f6 + f9, f7 + f9);
                obj = null;
                canvas.drawBitmap(bitmap, (Rect) null, rectF, paint);
            } else {
                obj = null;
            }
            i2++;
            f4 = f8;
            appsDrawer = this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class AppList extends View {
        final VirtualA11y a11y;
        Txt aIdx;
        Txt aLab;
        Txt aSub;
        final HashMap<String, Integer> cnt;
        HashMap<String, Integer> dots;
        int down;
        final Txt head;
        final float headH;
        final Runnable inval;
        final Txt lab;
        boolean longFired;
        final Runnable longPress;
        final Paint p;
        final RectF rf;
        final ArrayList<HomeRules.App> rowApp;
        final float rowH;
        final ArrayList<String> rowLetter;
        final ArrayList<Float> rowTop;
        boolean sect;
        final int slop;
        float total;
        final Rect vis;
        float x0;
        float y0;

        AppList(Context context) {
            super(context);
            this.rowApp = new ArrayList<>();
            this.rowLetter = new ArrayList<>();
            this.rowTop = new ArrayList<>();
            this.p = new Paint(3);
            this.vis = new Rect();
            this.rf = new RectF();
            this.inval = new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$AppList$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.AppList.this.invalidate();
                }
            };
            this.rowH = AppsDrawer.this.px(52.0f);
            this.headH = AppsDrawer.this.px(28.0f);
            this.cnt = new HashMap<>();
            this.down = -1;
            this.slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            this.longPress = new Runnable() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$AppList$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AppsDrawer.AppList.this.onLongPress();
                }
            };
            this.lab = new Txt(Fonts.RAJ_600, AppsDrawer.this.dn * 15.0f * AppsDrawer.this.z, 0.0f);
            this.head = new Txt(Fonts.MONO, AppsDrawer.this.dn * 9.0f * AppsDrawer.this.z, AppsDrawer.this.dn * 9.0f * AppsDrawer.this.z * 0.14f);
            this.a11y = new VirtualA11y(this, new VirtualA11y.Src() { // from class: com.jarvis.assist.aosui.home.AppsDrawer.AppList.1
                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public int count() {
                    return AppList.this.rowApp.size();
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public void bounds(int i, Rect rect) {
                    float floatValue = AppList.this.rowTop.get(i).floatValue();
                    rect.set(0, (int) floatValue, AppList.this.getWidth(), (int) (floatValue + (AppList.this.rowApp.get(i) == null ? AppList.this.headH : AppList.this.rowH)));
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public String label(int i) {
                    HomeRules.App app = AppList.this.rowApp.get(i);
                    if (app == null) {
                        return AppList.this.rowLetter.get(i);
                    }
                    if (AppsDrawer.isFolder(app)) {
                        return Strings.t("dr.f.a11y", "name", app.label, "n", Integer.valueOf(AppsDrawer.this.folderApps(AppsDrawer.folderIdx(app)).size()));
                    }
                    return app.label + ((AppList.this.dots == null || !AppList.this.dots.containsKey(app.pkg)) ? "" : ", has notifications");
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public boolean click(int i) {
                    HomeRules.App app = AppList.this.rowApp.get(i);
                    if (app == null) {
                        return false;
                    }
                    AppsDrawer.this.launch(app);
                    return true;
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public boolean hasLong(int i) {
                    return AppList.this.rowApp.get(i) != null;
                }

                @Override // com.jarvis.assist.aosui.home.VirtualA11y.Src
                public boolean longClick(int i) {
                    HomeRules.App app = AppList.this.rowApp.get(i);
                    if (app == null) {
                        return false;
                    }
                    AppList.this.menuAt(app, i);
                    return true;
                }
            });
        }

        boolean sections() {
            return this.sect;
        }

        void set(List<HomeRules.App> list) {
            this.rowApp.clear();
            this.rowLetter.clear();
            this.rowTop.clear();
            this.sect = "az".equals(AppsDrawer.this.sort);
            this.cnt.clear();
            for (HomeRules.App app : list) {
                if (!AppsDrawer.isFolder(app)) {
                    this.cnt.merge(DrawerModel.letter(app.label), 1, new BiFunction() { // from class: com.jarvis.assist.aosui.home.AppsDrawer$AppList$$ExternalSyntheticLambda2
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            return Integer.valueOf(Integer.sum(((Integer) obj).intValue(), ((Integer) obj2).intValue()));
                        }
                    });
                }
            }
            float f = 0.0f;
            String str = null;
            for (HomeRules.App app2 : list) {
                String letter = AppsDrawer.isFolder(app2) ? "" : DrawerModel.letter(app2.label);
                if (this.sect && !AppsDrawer.isFolder(app2) && !letter.equals(str)) {
                    this.rowApp.add(null);
                    this.rowLetter.add(letter);
                    this.rowTop.add(Float.valueOf(f));
                    f += this.headH;
                    str = letter;
                }
                this.rowApp.add(app2);
                ArrayList<String> arrayList = this.rowLetter;
                if (AppsDrawer.isFolder(app2)) {
                    letter = "#";
                }
                arrayList.add(letter);
                this.rowTop.add(Float.valueOf(f));
                f += this.rowH;
            }
            this.total = f;
            requestLayout();
            invalidate();
            this.a11y.contentChanged();
        }

        float yOf(String str) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < this.rowApp.size(); i++) {
                if (this.rowApp.get(i) == null) {
                    arrayList.add(this.rowLetter.get(i));
                    arrayList2.add(Integer.valueOf(i));
                }
            }
            int jump = DrawerModel.jump(arrayList, str);
            if (jump < 0) {
                return 0.0f;
            }
            return this.rowTop.get(((Integer) arrayList2.get(jump)).intValue()).floatValue();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), (int) Math.ceil(this.total));
        }

        /* JADX WARN: Removed duplicated region for block: B:40:0x029f  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x0307  */
        /* JADX WARN: Removed duplicated region for block: B:48:0x0347 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:50:0x02ce  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void aoaDraw(android.graphics.Canvas r23, float r24, float r25, float r26) {
            /*
                Method dump skipped, instructions count: 847
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.AppsDrawer.AppList.aoaDraw(android.graphics.Canvas, float, float, float):void");
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            float f2;
            boolean z;
            float f3;
            float f4;
            Canvas canvas2 = canvas;
            boolean z2 = true;
            int max = Math.max(1, AppsDrawer.this.sc.getHeight());
            if (!getLocalVisibleRect(this.vis)) {
                this.vis.set(0, 0, getWidth(), Math.min(getHeight(), max));
            }
            float f5 = this.vis.top - max;
            float f6 = this.vis.bottom + max;
            float width = getWidth() - AppsDrawer.this.px(30.0f);
            if (AppsDrawer.this.aoa) {
                aoaDraw(canvas2, f5, f6, width);
                return;
            }
            int i = 0;
            while (i < this.rowApp.size()) {
                float floatValue = this.rowTop.get(i).floatValue();
                HomeRules.App app = this.rowApp.get(i);
                float f7 = app == null ? this.headH : this.rowH;
                float f8 = floatValue + f7;
                if (f8 < f5) {
                    f4 = width;
                    z = z2;
                } else {
                    if (floatValue > f6) {
                        return;
                    }
                    if (app == null) {
                        this.head.draw(canvas2, this.rowLetter.get(i), AppsDrawer.this.px(6.0f), (this.headH / 2.0f) + floatValue + (this.head.cap / 2.0f), AppsDrawer.this.acc);
                        this.p.setColor(Theme.a(AppsDrawer.this.line, 0.45f));
                        f4 = width;
                        canvas.drawRect(AppsDrawer.this.px(26.0f), floatValue + (this.headH / 2.0f), f4, floatValue + (this.headH / 2.0f) + Math.max(1.0f, AppsDrawer.this.dn), this.p);
                        canvas2 = canvas;
                        z = z2;
                    } else {
                        float f9 = width;
                        float px = AppsDrawer.this.px(36.0f);
                        float f10 = px / 2.0f;
                        float px2 = AppsDrawer.this.px(6.0f) + f10;
                        float f11 = floatValue + (f7 / 2.0f);
                        Integer num = null;
                        this.p.setShader(null);
                        this.p.setStyle(Paint.Style.FILL);
                        this.p.setColor(Theme.a(-3615006, 0.06f));
                        canvas2.drawCircle(px2, f11, f10, this.p);
                        if (AppsDrawer.isFolder(app)) {
                            f = f10;
                            f2 = px2;
                            AppsDrawer.this.folderIcon(canvas2, this.p, this.rf, AppsDrawer.folderIdx(app), f2, f11, px, this.inval);
                        } else {
                            f = f10;
                            f2 = px2;
                            Bitmap bitmap = IconCache.get(getContext()).get(app.pkg, app.cls, (int) px, this.inval);
                            if (bitmap != null) {
                                this.p.setAlpha(255);
                                this.rf.set(f2 - f, f11 - f, f2 + f, f11 + f);
                                canvas2.drawBitmap(bitmap, (Rect) null, this.rf, this.p);
                            }
                        }
                        float px3 = f2 + f + AppsDrawer.this.px(14.0f);
                        this.lab.draw(canvas2, WidgetView.fitText(this.lab, AppsDrawer.isFolder(app) ? app.label + "  ·  " + AppsDrawer.this.folderApps(AppsDrawer.folderIdx(app)).size() : app.label, f9 - px3), px3, (this.lab.cap / 2.0f) + f11, AppsDrawer.this.ink);
                        if (this.dots != null && !AppsDrawer.isFolder(app)) {
                            num = this.dots.get(app.pkg);
                        }
                        if (num != null) {
                            z = true;
                            this.p.setColor(num.intValue() == 1 ? AppsDrawer.this.th.RED : num.intValue() == 2 ? AppsDrawer.this.th.BLUE : AppsDrawer.this.th.DIM);
                            canvas2.drawCircle(f9 - AppsDrawer.this.px(6.0f), f11, AppsDrawer.this.px(4.0f), this.p);
                        } else {
                            z = true;
                        }
                        int i2 = i + 1;
                        if (i2 >= this.rowApp.size() || this.rowApp.get(i2) == null) {
                            f3 = f8;
                            f4 = f9;
                        } else {
                            this.p.setColor(Theme.a(AppsDrawer.this.line, 0.3f));
                            Canvas canvas3 = canvas2;
                            f3 = f8;
                            f4 = f9;
                            canvas3.drawRect(px3, f8 - Math.max(1.0f, AppsDrawer.this.dn), f4, f3, this.p);
                            canvas2 = canvas3;
                        }
                        if (this.a11y.focused == i) {
                            this.p.setStyle(Paint.Style.STROKE);
                            this.p.setStrokeWidth(AppsDrawer.this.dn * 2.0f);
                            this.p.setColor(AppsDrawer.this.acc);
                            this.rf.set(0.0f, floatValue + AppsDrawer.this.px(2.0f), f4, f3 - AppsDrawer.this.px(2.0f));
                            canvas2.drawRoundRect(this.rf, AppsDrawer.this.px(8.0f), AppsDrawer.this.px(8.0f), this.p);
                            this.p.setStyle(Paint.Style.FILL);
                        }
                    }
                }
                i++;
                width = f4;
                z2 = z;
            }
        }

        int at(float f) {
            int size = this.rowApp.size();
            do {
                size--;
                if (size < 0) {
                    return -1;
                }
            } while (f < this.rowTop.get(size).floatValue());
            if (this.rowApp.get(size) == null) {
                return -1;
            }
            return size;
        }

        void menuAt(HomeRules.App app, int i) {
            getLocationOnScreen(new int[2]);
            AppsDrawer.this.getLocationOnScreen(new int[2]);
            AppsDrawer.this.menu(app, AppsDrawer.this.px(40.0f), (r1[1] - r0[1]) + this.rowTop.get(i).floatValue() + this.rowH);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void onLongPress() {
            if (this.down >= 0) {
                this.longFired = true;
                menuAt(this.rowApp.get(this.down), this.down);
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case 0:
                    AppsDrawer.this.closeMenu();
                    this.x0 = motionEvent.getX();
                    this.y0 = motionEvent.getY();
                    this.down = at(this.y0);
                    this.longFired = false;
                    if (this.down >= 0) {
                        postDelayed(this.longPress, 480L);
                    }
                    return true;
                case 1:
                    removeCallbacks(this.longPress);
                    if (this.down >= 0 && !this.longFired && SystemClock.uptimeMillis() - motionEvent.getDownTime() < 600) {
                        Feedback.fx(this, 0);
                        AppsDrawer.this.launch(this.rowApp.get(this.down));
                    }
                    this.down = -1;
                    return true;
                case 2:
                    if (Math.hypot(motionEvent.getX() - this.x0, motionEvent.getY() - this.y0) > this.slop) {
                        removeCallbacks(this.longPress);
                        this.down = -1;
                    }
                    return true;
                case 3:
                    removeCallbacks(this.longPress);
                    this.down = -1;
                    return true;
                default:
                    return super.onTouchEvent(motionEvent);
            }
        }

        @Override // android.view.View
        public AccessibilityNodeProvider getAccessibilityNodeProvider() {
            return this.a11y;
        }

        @Override // android.view.View
        protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
            return this.a11y.hover(motionEvent) || super.dispatchHoverEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Rail extends View {
        int cur;
        final Paint p;
        int sel;
        final Txt t;

        void follow(int i) {
            if (getVisibility() != 0) {
                return;
            }
            int i2 = -1;
            if (AppsDrawer.this.grid.getVisibility() == 0) {
                int current = AoaDrawer.current(AppsDrawer.this.grid.hTop, (i - AppsDrawer.this.grid.getTop()) + AppsDrawer.this.px(4.0f));
                if (current >= 0) {
                    i2 = AoaDrawer.rail(AppsDrawer.this.grid.hL.get(current));
                }
            } else {
                int at = AppsDrawer.this.list.at(Math.max(0, (i - AppsDrawer.this.list.getTop()) + AppsDrawer.this.px(4.0f)));
                if (at >= 0) {
                    i2 = AoaDrawer.rail(AppsDrawer.this.list.rowLetter.get(at));
                }
            }
            if (i2 != this.cur) {
                this.cur = i2;
                invalidate();
            }
        }

        Rail(Context context) {
            super(context);
            this.p = new Paint(1);
            this.sel = -1;
            this.cur = -1;
            this.t = new Txt(Fonts.MONO, AppsDrawer.this.dn * 9.0f, 0.0f).align(Paint.Align.CENTER);
            setVisibility(8);
            setContentDescription(Strings.t("dr.rail.a11y"));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int length = DrawerModel.RAIL.length();
            float paddingTop = getPaddingTop();
            float height = ((getHeight() - paddingTop) - getPaddingBottom()) / length;
            float width = getWidth() / 2.0f;
            int i = 0;
            while (i < length) {
                this.t.draw(canvas, String.valueOf(DrawerModel.RAIL.charAt(i)), width, (this.t.cap / 2.0f) + (i * height) + paddingTop + (height / 2.0f), AppsDrawer.this.aoa ? (i == this.sel || (this.sel < 0 && i == this.cur)) ? -34278 : -11624262 : i == this.sel ? AppsDrawer.this.acc : AppsDrawer.this.dim);
                i++;
            }
            if (this.sel < 0 || AppsDrawer.this.aoa) {
                return;
            }
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(1.0f, AppsDrawer.this.dn));
            this.p.setColor(AppsDrawer.this.acc);
            canvas.drawCircle(width, paddingTop + (this.sel * height) + (height / 2.0f), AppsDrawer.this.px(9.0f), this.p);
        }

        void jumpTo(float f) {
            ScrollView scrollView;
            float top;
            float yOf;
            int length = DrawerModel.RAIL.length();
            float paddingTop = getPaddingTop();
            int railIndex = DrawerModel.railIndex(f - paddingTop, (getHeight() - paddingTop) - getPaddingBottom(), length);
            if (railIndex == this.sel) {
                return;
            }
            this.sel = railIndex;
            Feedback.fx(this, 2);
            String valueOf = String.valueOf(DrawerModel.RAIL.charAt(railIndex));
            if (AppsDrawer.this.aoa && AppsDrawer.this.grid.getVisibility() == 0) {
                scrollView = AppsDrawer.this.sc;
                top = AppsDrawer.this.grid.getTop();
                yOf = AppsDrawer.this.grid.yOf(valueOf);
            } else {
                scrollView = AppsDrawer.this.sc;
                top = AppsDrawer.this.list.getTop();
                yOf = AppsDrawer.this.list.yOf(valueOf);
            }
            scrollView.scrollTo(0, Math.round(top + yOf));
            invalidate();
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case 0:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    AppsDrawer.this.closeMenu();
                    jumpTo(motionEvent.getY());
                    return true;
                case 1:
                case 3:
                    this.sel = -1;
                    invalidate();
                    return true;
                case 2:
                    jumpTo(motionEvent.getY());
                    return true;
                default:
                    return super.onTouchEvent(motionEvent);
            }
        }
    }
}
