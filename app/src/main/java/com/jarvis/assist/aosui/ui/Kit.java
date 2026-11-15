package com.jarvis.assist.aosui.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.jarvis.assist.aosui.ai.ModelList;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.ChargeFx;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.HomeTheme;
import com.jarvis.assist.aosui.core.Palette;
import com.jarvis.assist.aosui.core.SegModel;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Svg;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnitsMath;
import com.jarvis.assist.aosui.ui.Kit;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Kit {
    public static volatile boolean ADV = false;
    public static final String CHECK = "M3 8.5l3 3 7-7";
    public static final String CHEV = "M6 3l5 5-5 5";
    public static final String DOWN = "M4 6l4 4 4-4";
    public static final String LEFT = "M10 3L5 8l5 5";
    public static int TICK = -3665874;
    static int TICK2 = -1774607;
    public static final String UP = "M4 10l4-4 4 4";
    public static final String X = "M4 4l8 8M12 4l-8 8";
    public final int ACC;
    public final int ALT;
    public final int BG;
    public final int CARD;
    public final int DIM;
    public final int GOOD;
    public final int INK;
    public final int LINE;
    public final int LINE2;
    public final Context c;
    public final float dn;
    public final boolean light;
    public final float lz;
    public final Theme th;
    public final float z;

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class KeyField {
        public EditText in;
        public TextView msg;
        public TextView paste;
        public LinearLayout row;
    }

    public Flow flow(int i) {
        return new Flow(this.c, px(i), false);
    }

    public Kit(Context context, Theme theme, float f) {
        this(context, theme, f, f);
    }

    public Kit(Context context, Theme theme, float f, float f2) {
        this.c = context;
        this.th = theme;
        this.dn = context.getResources().getDisplayMetrics().density;
        this.z = f;
        this.lz = f2;
        this.light = theme.light;
        this.BG = this.light ? Palette.LIGHT_BG : -16579579;
        this.INK = theme.INK;
        this.DIM = this.light ? Palette.LIGHT_DIM : -7629149;
        this.GOOD = this.light ? Palette.GOOD_LIGHT : Palette.GOOD_DARK;
        this.LINE = this.light ? Theme.rgba(20, 30, 40, 0.12f) : Theme.rgba(200, 214, 226, 0.16f);
        this.LINE2 = this.light ? Theme.rgba(20, 30, 40, 0.28f) : Theme.rgba(200, 214, 226, 0.34f);
        this.ACC = theme.BLUE;
        this.ALT = theme.RED;
        this.CARD = theme.CARD;
        ADV = HomeTheme.hud(Store.get(Advanced.KEY, ""));
        TICK = theme.aoa ? Theme.AOA_ORANGE : -3665874;
    }

    public int px(float f) {
        return Math.round(f * this.dn);
    }

    public static void pane(View view, String str) {
        if (Build.VERSION.SDK_INT >= 28) {
            view.setAccessibilityPaneTitle(str);
        }
    }

    public static void heading(View view) {
        if (Build.VERSION.SDK_INT >= 28) {
            view.setAccessibilityHeading(true);
        }
    }

    public static Path icon(String str) {
        return Svg.path(str);
    }

    public static GradientDrawable pill(int i, int i2, float f, float f2) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(i);
        gradientDrawable.setCornerRadius(Math.min(f2, Math.max(2.0f, f * 2.0f)));
        if (f > 0.0f) {
            gradientDrawable.setStroke(Math.max(1, Math.round(f)), i2);
        }
        return gradientDrawable;
    }

    public TextView text(String str, Typeface typeface, float f, float f2, int i) {
        return style(new TextView(this.c), str, typeface, f, f2, i);
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static class FxText extends TextView {
        public int ev;

        public FxText(Context context) {
            super(context);
            this.ev = 0;
        }

        @Override // android.view.View
        public boolean performClick() {
            if (isEnabled()) {
                Feedback.fx(this, this.ev);
            }
            return super.performClick();
        }
    }

    TextView style(TextView textView, String str, Typeface typeface, float f, float f2, int i) {
        textView.setText(str);
        textView.setTypeface(typeface);
        textView.setTextSize(1, f * this.z);
        textView.setLetterSpacing(f2);
        textView.setTextColor(i);
        textView.setIncludeFontPadding(false);
        textView.setLineSpacing(0.0f, 1.15f);
        return textView;
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class FitText extends TextView {
        float base;

        public FitText(Context context, float f) {
            super(context);
            this.base = f;
            setSingleLine();
            setTextSize(0, f);
        }

        @Override // android.widget.TextView, android.view.View
        protected void onMeasure(int i, int i2) {
            if (View.MeasureSpec.getMode(i) != 0 && getText() != null) {
                Paint paint = new Paint(getPaint());
                paint.setTextSize(this.base);
                float fitScale = this.base * UnitsMath.fitScale(paint.measureText(getText().toString()), (View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight(), 0.6f);
                if (Math.abs(getTextSize() - fitScale) > 0.5f) {
                    super.setTextSize(0, fitScale);
                }
            }
            super.onMeasure(i, i2);
        }
    }

    public TextView fitText(String str, Typeface typeface, float f, float f2, int i) {
        FitText fitText = new FitText(this.c, f * this.lz * this.dn);
        fitText.setText(str);
        fitText.setTypeface(typeface);
        fitText.setLetterSpacing(f2);
        fitText.setTextColor(i);
        fitText.setIncludeFontPadding(false);
        return fitText;
    }

    public TextView clamp(TextView textView) {
        textView.setSingleLine(false);
        textView.setMaxLines(UnitsMath.lines(this.z));
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }

    public TextView mono(String str, float f, int i) {
        return text(str, Fonts.MONO, f, 0.12f, i);
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class HudBg extends Drawable {
        final boolean brackets;
        final float dn;
        final Paint f = new Paint(1);
        final Paint s = new Paint(1);
        final RectF r = new RectF();

        public HudBg(int i, int i2, boolean z, float f) {
            this.dn = f;
            this.brackets = z;
            this.f.setColor(i);
            this.s.setStyle(Paint.Style.STROKE);
            this.s.setColor(i2);
            this.s.setStrokeWidth(Math.max(1.0f, f));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            float strokeWidth = this.s.getStrokeWidth() / 2.0f;
            this.r.set(bounds.left + strokeWidth, bounds.top + strokeWidth, bounds.right - strokeWidth, bounds.bottom - strokeWidth);
            canvas.drawRect(this.r, this.f);
            int alpha = this.s.getAlpha();
            this.s.setAlpha(alpha / 2);
            canvas.drawRect(this.r, this.s);
            this.s.setAlpha(alpha);
            if (this.brackets) {
                float f = this.dn * 6.0f;
                this.s.setStrokeWidth(Math.max(1.0f, this.dn * 1.4f));
                canvas.drawLine(this.r.left, this.r.top, this.r.left + f, this.r.top, this.s);
                canvas.drawLine(this.r.left, this.r.top, this.r.left, this.r.top + f, this.s);
                canvas.drawLine(this.r.right, this.r.top, this.r.right - f, this.r.top, this.s);
                canvas.drawLine(this.r.right, this.r.top, this.r.right, this.r.top + f, this.s);
                canvas.drawLine(this.r.left, this.r.bottom, this.r.left + f, this.r.bottom, this.s);
                canvas.drawLine(this.r.left, this.r.bottom, this.r.left, this.r.bottom - f, this.s);
                canvas.drawLine(this.r.right, this.r.bottom, this.r.right - f, this.r.bottom, this.s);
                canvas.drawLine(this.r.right, this.r.bottom, this.r.right, this.r.bottom - f, this.s);
                this.s.setStrokeWidth(Math.max(1.0f, this.dn));
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

    public HudBg hud(boolean z) {
        return z ? new HudBg(Theme.a(TICK, 0.85f), TICK, true, this.dn) : new HudBg(0, this.LINE2, true, this.dn);
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class GridBg extends Drawable {
        final int bg;
        final float dn;
        final Paint p = new Paint();

        public GridBg(int i, int i2, float f) {
            this.bg = i;
            this.dn = f;
            this.p.setColor(i2);
            this.p.setStrokeWidth(Math.max(1.0f, f));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            canvas.drawColor(this.bg);
            float f = this.dn * 54.0f;
            float f2 = this.dn * 4.0f;
            float f3 = f / 2.0f;
            for (float f4 = bounds.top + f3; f4 < bounds.bottom; f4 += f) {
                float f5 = bounds.left + f3;
                while (f5 < bounds.right) {
                    canvas.drawLine(f5 - f2, f4, f5 + f2, f4, this.p);
                    float f6 = f5;
                    canvas.drawLine(f6, f4 - f2, f5, f4 + f2, this.p);
                    f5 = f6 + f;
                }
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

    /* loaded from: /tmp/augmaos/classes.dex */
    static final class HudHead extends TextView {
        final float dn;
        final Paint lp;

        HudHead(Context context, float f, int i) {
            super(context);
            this.lp = new Paint(1);
            this.dn = f;
            this.lp.setColor(i);
            this.lp.setStrokeWidth(Math.max(1.0f, f));
            this.lp.setStyle(Paint.Style.STROKE);
            setWillNotDraw(false);
        }

        @Override // android.widget.TextView, android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float height = getHeight() / 2.0f;
            float paddingLeft = getPaddingLeft() + getPaint().measureText(getText().toString()) + (this.dn * 10.0f);
            float width = getWidth() - (this.dn * 4.0f);
            int color = this.lp.getColor();
            this.lp.setColor(Kit.TICK);
            canvas.drawCircle(this.dn * 4.0f, height, this.dn * 2.6f, this.lp);
            this.lp.setColor(color);
            if (width - paddingLeft > this.dn * 20.0f) {
                canvas.drawLine(paddingLeft, height, width - (this.dn * 3.0f), height, this.lp);
                canvas.drawCircle(width, height, this.dn * 2.6f, this.lp);
            }
        }
    }

    public TextView section(String str) {
        TextView style = style(new HudHead(this.c, this.dn, this.LINE2), str, Fonts.MONO, 9.0f, 0.22f, this.DIM);
        style.setPadding(px(14.0f), 0, 0, 0);
        style.setSingleLine();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(px(2.0f), px(14.0f), px(2.0f), px(6.0f));
        style.setLayoutParams(layoutParams);
        heading(style);
        return style;
    }

    public TextView fine(String str) {
        TextView text = text(str, Fonts.RAJ_400, 12.0f, 0.0f, this.DIM);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(12.0f);
        text.setLayoutParams(layoutParams);
        return text;
    }

    public TextView msg(String str) {
        if (str == null) {
            str = "";
        }
        TextView text = text(str, Fonts.MONO, 9.0f, 0.1f, this.ALT);
        text.setMinHeight(px(12.0f));
        text.setAccessibilityLiveRegion(1);
        return text;
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public final class IconView extends View {
        int col;
        final Path p;
        final Paint pt;
        final float size;

        IconView(Path path, int i, float f) {
            super(Kit.this.c);
            this.pt = new Paint(1);
            this.p = path;
            this.col = i;
            this.size = f;
            this.pt.setStyle(Paint.Style.STROKE);
            this.pt.setStrokeCap(Paint.Cap.ROUND);
            this.pt.setStrokeJoin(Paint.Join.ROUND);
            setImportantForAccessibility(2);
        }

        public void color(int i) {
            this.col = i;
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.p == null) {
                return;
            }
            float min = Math.min(getWidth(), getHeight()) / 16.0f;
            this.pt.setColor(this.col);
            this.pt.setStrokeWidth(1.4f);
            canvas.save();
            float f = 16.0f * min;
            canvas.translate((getWidth() - f) / 2.0f, (getHeight() - f) / 2.0f);
            canvas.scale(min, min);
            canvas.drawPath(this.p, this.pt);
            canvas.restore();
        }
    }

    public IconView iconView(String str, int i, float f) {
        IconView iconView = new IconView(str == null ? null : icon(str), i, f);
        iconView.setLayoutParams(new LinearLayout.LayoutParams(px(f), px(f)));
        return iconView;
    }

    public View circle(String str, String str2, boolean z, float f, final Runnable runnable) {
        FrameLayout frameLayout = new FrameLayout(this.c);
        frameLayout.setBackground(z ? pill(0, this.LINE2, this.dn, this.dn * 999.0f) : null);
        frameLayout.addView(new IconView(icon(str), this.INK, 14.0f), new FrameLayout.LayoutParams(px(14.0f), px(14.0f), 17));
        frameLayout.setContentDescription(str2);
        frameLayout.setClickable(true);
        frameLayout.setFocusable(true);
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Kit.lambda$circle$0(runnable, view);
            }
        });
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(px(f), px(f)));
        frameLayout.setMinimumWidth(px(Math.max(f, 34.0f)));
        return frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$circle$0(Runnable runnable, View view) {
        Feedback.fx(view, 0);
        runnable.run();
    }

    public View header(String str, String str2, Runnable runnable, String str3) {
        Kit kit;
        String str4;
        Typeface typeface;
        float f;
        int i;
        float f2;
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        linearLayout.setPadding(0, px(6.0f), 0, px(10.0f));
        if (runnable != null) {
            kit = this;
            View circle = kit.circle(LEFT, str3, true, 36.0f, runnable);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px(36.0f), px(36.0f));
            layoutParams.rightMargin = px(12.0f);
            linearLayout.addView(circle, layoutParams);
        } else {
            kit = this;
        }
        LinearLayout linearLayout2 = new LinearLayout(kit.c);
        linearLayout2.setOrientation(1);
        if (ADV) {
            linearLayout2.addView(kit.text("aOS · " + str.toUpperCase(Locale.getDefault()), Fonts.MONO, 9.0f, 0.3f, kit.DIM));
        }
        if (ADV) {
            typeface = Fonts.RAJ_400;
            f = 0.16f;
            i = kit.INK;
            f2 = 24.0f;
            str4 = str;
        } else {
            str4 = str;
            typeface = Fonts.RAJ_600;
            f = 0.08f;
            i = kit.INK;
            f2 = 19.0f;
        }
        TextView text = kit.text(str4, typeface, f2, f, i);
        heading(text);
        linearLayout2.addView(text);
        if (str2 != null && !str2.isEmpty()) {
            TextView text2 = kit.text(str2, Fonts.MONO, 9.0f, 0.14f, kit.DIM);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.topMargin = px(4.0f);
            linearLayout2.addView(text2, layoutParams2);
        }
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(0, -2, 1.0f));
        return linearLayout;
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class CardBg extends Drawable {
        final float dn;
        final Paint f = new Paint(1);
        final Paint s = new Paint(1);
        final Paint br = new Paint(1);
        final RectF r = new RectF();
        final Path p = new Path();

        public CardBg(int i, int i2, int i3, float f) {
            this.dn = f;
            this.f.setColor(i);
            this.s.setStyle(Paint.Style.STROKE);
            this.s.setColor(i2);
            this.s.setStrokeWidth(Math.max(1.0f, f));
            this.br.setStyle(Paint.Style.STROKE);
            this.br.setColor(i3);
            this.br.setStrokeWidth(Math.max(1.0f, f));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            float strokeWidth = this.s.getStrokeWidth() / 2.0f;
            this.r.set(bounds.left + strokeWidth, bounds.top + strokeWidth, bounds.right - strokeWidth, bounds.bottom - strokeWidth);
            float f = this.dn * 2.0f;
            canvas.drawRoundRect(this.r, f, f, this.f);
            canvas.drawRoundRect(this.r, f, f, this.s);
            float f2 = this.dn * 6.0f;
            if (Kit.ADV) {
                float f3 = this.dn * 10.0f;
                this.br.setStrokeWidth(Math.max(1.0f, this.dn * 1.4f));
                this.p.reset();
                this.p.moveTo(bounds.left, bounds.top + f3);
                this.p.lineTo(bounds.left, bounds.top);
                this.p.lineTo(bounds.left + f3, bounds.top);
                this.p.moveTo(bounds.right - f3, bounds.top);
                this.p.lineTo(bounds.right, bounds.top);
                this.p.lineTo(bounds.right, bounds.top + f3);
                this.p.moveTo(bounds.left, bounds.bottom - f3);
                this.p.lineTo(bounds.left, bounds.bottom);
                this.p.lineTo(bounds.left + f3, bounds.bottom);
                this.p.moveTo(bounds.right - f3, bounds.bottom);
                this.p.lineTo(bounds.right, bounds.bottom);
                this.p.lineTo(bounds.right, bounds.bottom - f3);
                int color = this.br.getColor();
                this.br.setColor(Kit.TICK2);
                canvas.drawPath(this.p, this.br);
                this.br.setColor(color);
                return;
            }
            this.br.setStrokeWidth(Math.max(2.0f, this.dn * 2.0f));
            int color2 = this.br.getColor();
            this.br.setColor(Kit.TICK);
            canvas.drawLine(bounds.left + strokeWidth, bounds.top + strokeWidth, bounds.left + (this.dn * 12.0f), bounds.top + strokeWidth, this.br);
            this.br.setColor(color2);
            this.br.setStrokeWidth(Math.max(1.0f, this.dn));
            canvas.drawLine((bounds.right - f2) - (this.dn * 6.0f), bounds.top + f2, bounds.right - f2, bounds.top + f2, this.br);
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

    public LinearLayout card(boolean z, boolean z2) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(z ? 1 : 0);
        linearLayout.setBackground(new CardBg(this.CARD, z2 ? Theme.a(this.ACC, 0.5f) : this.LINE2, this.LINE2, this.dn));
        linearLayout.setPadding(px(12.0f), px(12.0f), px(12.0f), px(12.0f));
        if (!z) {
            linearLayout.setGravity(16);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, px(8.0f), 0, px(8.0f));
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    public LinearLayout list() {
        LinearLayout card = card(true, false);
        card.setPadding(px(12.0f), px(2.0f), px(12.0f), px(2.0f));
        return card;
    }

    public <V extends View> V add(LinearLayout linearLayout, V v) {
        if (linearLayout.getChildCount() > 0) {
            View view = new View(this.c);
            view.setBackgroundColor(this.LINE);
            linearLayout.addView(view, new LinearLayout.LayoutParams(-1, Math.max(1, Math.round(this.dn))));
        }
        linearLayout.addView(v);
        return v;
    }

    public LinearLayout note(String str, int i, String str2, String str3, View view, boolean z) {
        LinearLayout card = card(false, z);
        card.setGravity(48);
        if (str != null) {
            View iconView = iconView(str, i, 18.0f);
            ((LinearLayout.LayoutParams) iconView.getLayoutParams()).setMargins(0, px(2.0f), px(12.0f), 0);
            card.addView(iconView);
        }
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.addView(text(str2, Fonts.RAJ_600, 15.0f, 0.0f, this.INK));
        if (str3 != null && !str3.isEmpty()) {
            TextView text = text(str3, Fonts.RAJ_400, 13.0f, 0.0f, this.DIM);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.topMargin = px(2.0f);
            linearLayout.addView(text, layoutParams);
        }
        card.addView(linearLayout, new LinearLayout.LayoutParams(0, -2, 1.0f));
        if (view != null) {
            card.setGravity(16);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.leftMargin = px(8.0f);
            card.addView(view, layoutParams2);
        }
        return card;
    }

    public LinearLayout row(String str, int i, String str2, String str3, boolean z, final Runnable runnable, View... viewArr) {
        String str4;
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        linearLayout.setMinimumHeight(px(52.0f));
        linearLayout.setPadding(0, px(6.0f), 0, px(6.0f));
        if (str != null) {
            View iconView = iconView(str, i, 18.0f);
            ((LinearLayout.LayoutParams) iconView.getLayoutParams()).rightMargin = px(12.0f);
            linearLayout.addView(iconView);
        }
        LinearLayout linearLayout2 = new LinearLayout(this.c);
        linearLayout2.setOrientation(1);
        linearLayout2.addView(text(str2, Fonts.RAJ_600, 15.0f, 0.0f, this.INK));
        if (str3 == null || str3.isEmpty()) {
            str4 = str3;
        } else {
            str4 = str3;
            TextView clamp = clamp(text(str4, Fonts.MONO, 8.5f, 0.12f, this.DIM));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.topMargin = px(3.0f);
            linearLayout2.addView(clamp, layoutParams);
            clamp.setTag("sub");
        }
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(0, -2, 1.0f));
        for (View view : viewArr) {
            if (view != null) {
                LinearLayout.LayoutParams layoutParams2 = view.getLayoutParams() instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) view.getLayoutParams() : new LinearLayout.LayoutParams(-2, -2);
                layoutParams2.leftMargin = px(8.0f);
                linearLayout.addView(view, layoutParams2);
            }
        }
        if (z) {
            View iconView2 = iconView(CHEV, this.INK, 12.0f);
            ((LinearLayout.LayoutParams) iconView2.getLayoutParams()).leftMargin = px(12.0f);
            linearLayout.addView(iconView2);
        }
        if (runnable != null) {
            linearLayout.setClickable(true);
            linearLayout.setFocusable(true);
            linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    Kit.lambda$row$1(runnable, view2);
                }
            });
            linearLayout.setContentDescription(str2 + ((str4 == null || str4.isEmpty()) ? "" : ", " + str4));
            linearLayout.setBackground(ripple());
        }
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$row$1(Runnable runnable, View view) {
        Feedback.fx(view, 0);
        runnable.run();
    }

    public LinearLayout rowWrap(String str, String str2, View... viewArr) {
        LinearLayout row = row(null, 0, str, str2, false, null, viewArr);
        View findViewWithTag = row.findViewWithTag("sub");
        if (findViewWithTag instanceof TextView) {
            TextView textView = (TextView) findViewWithTag;
            textView.setSingleLine(false);
            textView.setEllipsize(null);
        }
        return row;
    }

    Drawable ripple() {
        return new RippleDrawable(ColorStateList.valueOf(Theme.a(this.ACC, 0.18f)), null, new ColorDrawable(-1));
    }

    public TextView num(String str) {
        TextView text = text(str, Fonts.MONO, 9.0f, 0.0f, this.DIM);
        text.setGravity(17);
        text.setBackground(pill(0, this.LINE2, this.dn, this.dn * 999.0f));
        int round = Math.round(px(20.0f) * Math.max(1.0f, this.z));
        text.setLayoutParams(new LinearLayout.LayoutParams(round, round));
        text.setImportantForAccessibility(2);
        return text;
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Toggle extends View {
        Consumer<Boolean> cb;
        boolean enabled;
        float k;
        boolean on;
        final Paint p;
        final RectF r;

        Toggle(boolean z, String str, Consumer<Boolean> consumer) {
            super(Kit.this.c);
            this.enabled = true;
            this.p = new Paint(1);
            this.r = new RectF();
            this.on = z;
            this.cb = consumer;
            this.k = z ? 1.0f : 0.0f;
            setContentDescription(str);
            setClickable(true);
            setFocusable(true);
            setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$Toggle$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    Kit.Toggle.this.m368lambda$new$0$comaugmaosouiKit$Toggle(view);
                }
            });
            setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.jarvis.assist.aosui.ui.Kit.Toggle.1
                @Override // android.view.View.AccessibilityDelegate
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                    accessibilityNodeInfo.setClassName("android.widget.Switch");
                    accessibilityNodeInfo.setCheckable(true);
                    accessibilityNodeInfo.setChecked(Toggle.this.on);
                    accessibilityNodeInfo.setEnabled(Toggle.this.enabled);
                }
            });
            setLayoutParams(new LinearLayout.LayoutParams(Kit.this.px(48.0f), Kit.this.px(44.0f)));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-augma-oso-ui-Kit$Toggle, reason: not valid java name */
        public /* synthetic */ void m368lambda$new$0$comaugmaosouiKit$Toggle(View view) {
            if (!this.enabled) {
                Feedback.fx(view, 5);
                return;
            }
            Feedback.fx(view, 15);
            set(!this.on);
            if (this.cb != null) {
                this.cb.accept(Boolean.valueOf(this.on));
            }
        }

        public void set(boolean z) {
            this.on = z;
            this.k = z ? 1.0f : 0.0f;
            invalidate();
            sendAccessibilityEvent(4);
        }

        public boolean on() {
            return this.on;
        }

        public void enable(boolean z) {
            this.enabled = z;
            setAlpha(z ? 1.0f : 0.4f);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float px = Kit.this.px(40.0f);
            float px2 = Kit.this.px(22.0f);
            float width = getWidth() - px;
            float height = (getHeight() - px2) / 2.0f;
            this.r.set(width + 0.5f, height + 0.5f, (px + width) - 0.5f, (height + px2) - 0.5f);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(this.on ? Theme.a(Kit.this.ACC, 0.18f) : 0);
            float f = px2 / 2.0f;
            canvas.drawRoundRect(this.r, f, f, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(1.0f, Kit.this.dn));
            this.p.setColor(this.on ? Kit.this.ACC : Kit.this.LINE2);
            canvas.drawRoundRect(this.r, f, f, this.p);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(this.on ? Kit.this.ACC : Kit.this.DIM);
            canvas.drawCircle(width + Kit.this.px(3.0f) + Kit.this.px(7.0f) + (this.k * Kit.this.px(18.0f)), height + f, Kit.this.px(7.0f), this.p);
        }
    }

    public Toggle toggle(boolean z, String str, Consumer<Boolean> consumer) {
        return new Toggle(z, str, consumer);
    }

    public LinearLayout toggleRow(String str, String str2, boolean z, Consumer<Boolean> consumer) {
        Toggle toggle = toggle(z, str, consumer);
        return str2 == null ? row(null, 0, str, null, false, null, toggle) : rowWrap(str, str2, toggle);
    }

    public LinearLayout seg(String[] strArr, String[] strArr2, String str, String str2, final Consumer<String> consumer) {
        char c;
        char c2;
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setContentDescription(str2);
        Paint paint = new Paint();
        paint.setTypeface(Fonts.MONO);
        paint.setTextSize(this.z * 9.5f * this.dn);
        paint.setLetterSpacing(0.1f);
        float px = px(6.0f);
        for (String str3 : strArr) {
            px += paint.measureText(str3) + px(24.0f);
        }
        boolean z = px > ((float) (this.c.getResources().getDisplayMetrics().widthPixels - px(72.0f)));
        Flow flow = z ? new Flow(this.c, px(6.0f), false) : null;
        int i = -1;
        if (!z) {
            linearLayout.setBackground(new HudBg(0, this.LINE2, false, this.dn));
            linearLayout.setPadding(px(3.0f), px(3.0f), px(3.0f), px(3.0f));
        } else {
            linearLayout.addView(flow, new LinearLayout.LayoutParams(-1, -2));
        }
        boolean[] selected = SegModel.selected(strArr2, str);
        int i2 = 0;
        while (i2 < strArr.length) {
            final String str4 = strArr2[i2];
            final boolean z2 = selected[i2];
            int i3 = i2;
            String str5 = strArr[i3];
            Typeface typeface = Fonts.MONO;
            if (!z2) {
                i = this.DIM;
            }
            TextView text = text(str5, typeface, 9.5f, 0.1f, i);
            text.setGravity(17);
            text.setPadding(px(4.0f), px(9.0f), px(4.0f), px(9.0f));
            text.setMinHeight(px(44.0f));
            if (z2) {
                text.setBackground(hud(true));
            }
            text.setClickable(true);
            text.setFocusable(true);
            text.setContentDescription(strArr[i3] + (str2 == null ? "" : ", " + str2));
            text.setSelected(z2);
            final String t = Strings.t(SegModel.stateKey(z2));
            text.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.jarvis.assist.aosui.ui.Kit.1
                @Override // android.view.View.AccessibilityDelegate
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                    accessibilityNodeInfo.setClassName("android.widget.RadioButton");
                    accessibilityNodeInfo.setCheckable(true);
                    accessibilityNodeInfo.setChecked(z2);
                    accessibilityNodeInfo.setSelected(z2);
                    if (Build.VERSION.SDK_INT >= 30) {
                        accessibilityNodeInfo.setStateDescription(t);
                    }
                }
            });
            text.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    Kit.lambda$seg$2(consumer, str4, view);
                }
            });
            if (z) {
                text.setPadding(px(14.0f), px(9.0f), px(14.0f), px(9.0f));
                if (!z2) {
                    text.setBackground(hud(false));
                }
                flow.addView(text);
                c2 = 0;
                c = 65534;
            } else {
                c = 65534;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
                if (i3 > 0) {
                    c2 = 0;
                    layoutParams.leftMargin = px(3.0f);
                } else {
                    c2 = 0;
                }
                linearLayout.addView(text, layoutParams);
            }
            i2 = i3 + 1;
            i = -1;
        }
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$seg$2(Consumer consumer, String str, View view) {
        Feedback.fx(view, 15);
        consumer.accept(str);
    }

    public LinearLayout slider(String str, final int i, int i2, final int i3, int i4, String str2, final IntFunction<String> intFunction, final Consumer<Integer> consumer) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(px(2.0f), px(12.0f), px(2.0f), px(12.0f));
        linearLayout.setLayoutParams(layoutParams);
        TextView text = text(str, Fonts.RAJ_400, 14.0f, 0.0f, this.INK);
        text.setMinWidth(px(52.0f));
        text.setImportantForAccessibility(2);
        linearLayout.addView(text);
        SeekBar seekBar = new SeekBar(this.c);
        seekBar.setMax((i2 - i) / i3);
        seekBar.setProgress((i4 - i) / i3);
        seekBar.setContentDescription(str2 == null ? str : str2);
        seekBar.setProgressTintList(ColorStateList.valueOf(this.ACC));
        seekBar.setThumbTintList(ColorStateList.valueOf(this.ACC));
        seekBar.setProgressBackgroundTintList(ColorStateList.valueOf(this.LINE2));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, px(44.0f), 1.0f);
        layoutParams2.setMargins(px(6.0f), 0, px(6.0f), 0);
        linearLayout.addView(seekBar, layoutParams2);
        final TextView text2 = text(intFunction == null ? "" : intFunction.apply(i4), Fonts.MONO, 9.0f, 0.0f, this.DIM);
        text2.setMinWidth(px(40.0f));
        text2.setGravity(8388613);
        if (intFunction != null) {
            linearLayout.addView(text2);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.jarvis.assist.aosui.ui.Kit.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i5, boolean z) {
                if (z) {
                    Feedback.fx(seekBar2, 2);
                }
                if (intFunction != null) {
                    text2.setText((CharSequence) intFunction.apply(i + (i3 * i5)));
                }
                if (!z || seekBar2.isPressed()) {
                    return;
                }
                consumer.accept(Integer.valueOf(i + (i5 * i3)));
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
                consumer.accept(Integer.valueOf(i + (seekBar2.getProgress() * i3)));
            }
        });
        return linearLayout;
    }

    public LinearLayout sliderAa(int i, int i2, int i3, int i4, String str, Consumer<Integer> consumer) {
        LinearLayout slider = slider("Aa", i, i2, i3, i4, str, null, consumer);
        ((TextView) slider.getChildAt(0)).setTextSize(1, this.z * 12.0f);
        ((TextView) slider.getChildAt(0)).setMinWidth(0);
        TextView text = text("Aa", Fonts.RAJ_400, 20.0f, 0.0f, this.INK);
        text.setImportantForAccessibility(2);
        slider.addView(text);
        return slider;
    }

    public LinearLayout swatches(String[] strArr, final String str, final Consumer<String> consumer, String str2, final Runnable runnable) {
        String str3;
        Flow flow = new Flow(this.c, px(10.0f), false);
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.addView(flow, new LinearLayout.LayoutParams(-1, -2));
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            final String str4 = strArr[i];
            final boolean equalsIgnoreCase = str4.equalsIgnoreCase(str);
            View view = new View(this.c) { // from class: com.jarvis.assist.aosui.ui.Kit.3
                final Paint p = new Paint(1);

                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    float width = getWidth() / 2.0f;
                    float height = getHeight() / 2.0f;
                    if (equalsIgnoreCase) {
                        this.p.setStyle(Paint.Style.STROKE);
                        this.p.setStrokeWidth(Kit.this.px(2.0f));
                        this.p.setColor(Kit.this.INK);
                        canvas.drawCircle(width, height, Kit.this.px(17.0f) + Kit.this.px(3.0f), this.p);
                    }
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(Theme.hex(str4, -7829368));
                    canvas.drawCircle(width, height, Kit.this.px(17.0f), this.p);
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(Math.max(1.0f, Kit.this.dn));
                    this.p.setColor(Kit.this.LINE2);
                    canvas.drawCircle(width, height, Kit.this.px(17.0f) - (Math.max(1.0f, Kit.this.dn) / 2.0f), this.p);
                }
            };
            if (equalsIgnoreCase) {
                str3 = ", selected";
            }
            view.setContentDescription(str4 + str3);
            view.setClickable(true);
            view.setFocusable(true);
            view.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda19
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    Kit.lambda$swatches$3(consumer, str4, view2);
                }
            });
            flow.addView(view, new ViewGroup.LayoutParams(px(44.0f), px(44.0f)));
            i++;
        }
        boolean z = false;
        for (String str5 : strArr) {
            if (str5.equalsIgnoreCase(str)) {
                z = true;
            }
        }
        final boolean z2 = !z;
        View view2 = new View(this.c) { // from class: com.jarvis.assist.aosui.ui.Kit.4
            final Paint p = new Paint(1);
            final Paint g = new Paint(1);

            @Override // android.view.View
            protected void onSizeChanged(int i2, int i3, int i4, int i5) {
                this.g.setShader(new SweepGradient(i2 / 2.0f, i3 / 2.0f, new int[]{-45747, -11699, -11665528, -11676929, -7713281, -45747}, (float[]) null));
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                float width = getWidth() / 2.0f;
                float height = getHeight() / 2.0f;
                if (!z2) {
                    this.g.setAlpha(FxModel.TEST_GAP_MS);
                    canvas.drawCircle(width, height, Kit.this.px(17.0f), this.g);
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(Math.max(1.0f, Kit.this.dn));
                    this.p.setColor(Kit.this.LINE2);
                    canvas.drawCircle(width, height, Kit.this.px(17.0f), this.p);
                    return;
                }
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(Theme.hex(str, -7829368));
                canvas.drawCircle(width, height, Kit.this.px(17.0f), this.p);
                this.p.setStyle(Paint.Style.STROKE);
                this.p.setStrokeWidth(Kit.this.px(2.0f));
                this.p.setColor(Kit.this.INK);
                canvas.drawCircle(width, height, Kit.this.px(20.0f), this.p);
            }
        };
        view2.setContentDescription(str2 + (z ? "" : ", " + str));
        view2.setClickable(true);
        view2.setFocusable(true);
        view2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda20
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                Kit.lambda$swatches$4(runnable, view3);
            }
        });
        flow.addView(view2, new ViewGroup.LayoutParams(px(44.0f), px(44.0f)));
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$swatches$3(Consumer consumer, String str, View view) {
        Feedback.fx(view, 0);
        consumer.accept(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$swatches$4(Runnable runnable, View view) {
        Feedback.fx(view, 0);
        runnable.run();
    }

    public TextView btn(String str, boolean z, boolean z2, final Runnable runnable) {
        TextView style = style(new FxText(this.c), str, Fonts.MONO, z2 ? 10.0f : 11.0f, 0.16f, z ? this.DIM : this.ACC);
        style.setGravity(17);
        style.setSingleLine();
        style.setPadding(px(z2 ? 12.0f : 18.0f), px(6.0f), px(z2 ? 12.0f : 18.0f), px(6.0f));
        style.setMinHeight(px(44.0f));
        style.setMinimumHeight(px(44.0f));
        style.setBackground(new HudBg(z ? 0 : Theme.a(this.ACC, 0.12f), z ? this.LINE2 : this.ACC, true, this.dn));
        style.setClickable(true);
        style.setFocusable(true);
        style.setContentDescription(str);
        style.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.jarvis.assist.aosui.ui.Kit.5
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName("android.widget.Button");
            }
        });
        if (runnable != null) {
            style.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    runnable.run();
                }
            });
        }
        return style;
    }

    public TextView bigBtn(String str, Runnable runnable) {
        TextView btn = btn(str, false, false, runnable);
        btn.setTextSize(1, this.z * 12.0f);
        btn.setMinHeight(px(52.0f));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(6.0f);
        btn.setLayoutParams(layoutParams);
        return btn;
    }

    public void enable(TextView textView, boolean z) {
        textView.setEnabled(z);
        textView.setAlpha(z ? 1.0f : 0.5f);
    }

    public TextView chip(String str, int i) {
        TextView style = style(new FxText(this.c), str, Fonts.MONO, 9.0f, 0.12f, i == 0 ? this.DIM : i);
        style.setSingleLine();
        style.setPadding(px(9.0f), px(5.0f), px(9.0f), px(5.0f));
        if (i == 0) {
            i = this.LINE2;
        }
        style.setBackground(pill(0, i, this.dn, this.dn * 999.0f));
        style.setGravity(17);
        return style;
    }

    public LinearLayout hrow(View... viewArr) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setGravity(16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = px(6.0f);
        linearLayout.setLayoutParams(layoutParams);
        for (View view : viewArr) {
            if (view != null) {
                LinearLayout.LayoutParams layoutParams2 = view.getLayoutParams() instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) view.getLayoutParams() : new LinearLayout.LayoutParams(-2, -2);
                layoutParams2.topMargin = 0;
                if (linearLayout.getChildCount() > 0) {
                    layoutParams2.leftMargin = px(8.0f);
                }
                linearLayout.addView(view, layoutParams2);
            }
        }
        return linearLayout;
    }

    public static View grow(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        return view;
    }

    public EditText input(String str, String str2, boolean z, boolean z2, int i) {
        EditText editText = new EditText(this.c);
        if (str == null) {
            str = "";
        }
        editText.setText(str);
        editText.setHint(str2);
        editText.setTypeface(Fonts.RAJ_400);
        editText.setTextSize(1, (z2 ? 14 : 16) * this.z);
        editText.setTextColor(this.INK);
        editText.setHintTextColor(this.DIM);
        editText.setPadding(px(z2 ? 12.0f : 14.0f), px(z2 ? 9.0f : 12.0f), px(z2 ? 12.0f : 14.0f), px(z2 ? 9.0f : 12.0f));
        editText.setBackground(pill(0, this.LINE2, this.dn, this.dn * 6.0f));
        editText.setSingleLine();
        if (this.z > 1.15f) {
            editText.setHorizontallyScrolling(false);
            editText.setMaxLines(3);
        }
        editText.setContentDescription(str2);
        if (z) {
            i = 524417;
        } else if (i == 0) {
            i = 524289;
        }
        editText.setInputType(i);
        if (z) {
            editText.setImportantForAutofill(2);
        }
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda8
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z3) {
                Kit.this.m366lambda$input$6$comaugmaosouiKit(view, z3);
            }
        });
        editText.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        return editText;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$input$6$com-augma-oso-ui-Kit, reason: not valid java name */
    public /* synthetic */ void m366lambda$input$6$comaugmaosouiKit(View view, boolean z) {
        view.setBackground(pill(0, z ? this.ACC : this.LINE2, this.dn, this.dn * 6.0f));
    }

    public KeyField keyField(String str, boolean z, String str2, final Consumer<KeyField> consumer) {
        final KeyField keyField = new KeyField();
        keyField.in = input("", str, z, true, 0);
        keyField.msg = msg("");
        keyField.paste = btn(str2, true, true, null);
        keyField.paste.setContentDescription(str2 + " " + str);
        keyField.paste.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda21
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                consumer.accept(keyField);
            }
        });
        keyField.row = hrow(grow(keyField.in), keyField.paste);
        return keyField;
    }

    public TextView select(final FrameLayout frameLayout, final String str, final String[] strArr, final String[] strArr2, final String str2, final Consumer<String> consumer) {
        String str3;
        String str4;
        int i = -1;
        for (int i2 = 0; i2 < strArr2.length; i2++) {
            if (strArr2[i2].equals(str2)) {
                i = i2;
            }
        }
        if (i >= 0) {
            str4 = strArr[i];
        } else {
            if (str2 != null) {
                str3 = str2;
                TextView style = style(new FxText(this.c), str3, Fonts.MONO, 12.0f, 0.0f, this.INK);
                style.setSingleLine();
                style.setEllipsize(TextUtils.TruncateAt.END);
                style.setPadding(px(10.0f), px(9.0f), px(10.0f), px(9.0f));
                style.setMinHeight(px(40.0f));
                style.setGravity(16);
                style.setBackground(pill(this.BG, this.LINE2, this.dn, this.dn * 6.0f));
                style.setContentDescription(str + ", " + str3);
                style.setClickable(true);
                style.setFocusable(true);
                style.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        Kit.this.m367lambda$select$8$comaugmaosouiKit(frameLayout, str, strArr, strArr2, str2, consumer, view);
                    }
                });
                style.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                return style;
            }
            str4 = "";
        }
        str3 = str4;
        TextView style2 = style(new FxText(this.c), str3, Fonts.MONO, 12.0f, 0.0f, this.INK);
        style2.setSingleLine();
        style2.setEllipsize(TextUtils.TruncateAt.END);
        style2.setPadding(px(10.0f), px(9.0f), px(10.0f), px(9.0f));
        style2.setMinHeight(px(40.0f));
        style2.setGravity(16);
        style2.setBackground(pill(this.BG, this.LINE2, this.dn, this.dn * 6.0f));
        style2.setContentDescription(str + ", " + str3);
        style2.setClickable(true);
        style2.setFocusable(true);
        style2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Kit.this.m367lambda$select$8$comaugmaosouiKit(frameLayout, str, strArr, strArr2, str2, consumer, view);
            }
        });
        style2.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        return style2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$select$8$com-augma-oso-ui-Kit, reason: not valid java name */
    public /* synthetic */ void m367lambda$select$8$comaugmaosouiKit(FrameLayout frameLayout, String str, String[] strArr, String[] strArr2, String str2, Consumer consumer, View view) {
        pickSheet(frameLayout, str, strArr, strArr2, str2, consumer);
    }

    public void pickSheet(FrameLayout frameLayout, String str, String[] strArr, String[] strArr2, String str2, Consumer<String> consumer) {
        pickSheet(frameLayout, str, strArr, strArr2, null, str2, consumer);
    }

    public void pickSheet(FrameLayout frameLayout, String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, final Consumer<String> consumer) {
        boolean z;
        final Sheet title = new Sheet(this.c, this.th, str).title(str.toUpperCase(Locale.getDefault()));
        LinearLayout body = title.body();
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.setImportantForAccessibility(1);
        final int length = strArr.length;
        linearLayout.setContentDescription(str);
        if (Build.VERSION.SDK_INT >= 28) {
            linearLayout.setAccessibilityPaneTitle(str);
        }
        linearLayout.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.jarvis.assist.aosui.ui.Kit.6
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName(ListView.class.getName());
                accessibilityNodeInfo.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(length, 1, false));
            }
        });
        for (final int i = 0; i < length; i++) {
            final String str3 = strArr2[i];
            final boolean equals = str3.equals(str2);
            String str4 = (strArr3 == null || strArr3[i] == null || strArr3[i].isEmpty()) ? null : strArr3[i];
            String str5 = str4;
            LinearLayout row = row(null, 0, strArr[i], str5, false, new Runnable() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    Kit.lambda$pickSheet$9(Sheet.this, consumer, str3);
                }
            }, equals ? iconView(CHECK, this.ACC, 14.0f) : null);
            row.setContentDescription(ModelList.a11y(strArr[i], str5, i, length, equals));
            row.setImportantForAccessibility(1);
            row.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.jarvis.assist.aosui.ui.Kit.7
                @Override // android.view.View.AccessibilityDelegate
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                    accessibilityNodeInfo.setClassName(Button.class.getName());
                    accessibilityNodeInfo.setSelected(equals);
                    accessibilityNodeInfo.setClickable(true);
                    accessibilityNodeInfo.setCollectionItemInfo(AccessibilityNodeInfo.CollectionItemInfo.obtain(i, 1, 0, 1, false, equals));
                }
            });
            if (linearLayout.getChildCount() > 0) {
                View view = new View(this.c);
                view.setBackgroundColor(this.LINE);
                view.setImportantForAccessibility(2);
                z = true;
                linearLayout.addView(view, new LinearLayout.LayoutParams(-1, 1));
            } else {
                z = true;
            }
            linearLayout.addView(row);
        }
        body.addView(linearLayout, new LinearLayout.LayoutParams(-1, -2));
        title.show(frameLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$pickSheet$9(Sheet sheet, Consumer consumer, String str) {
        sheet.close();
        consumer.accept(str);
    }

    public void colorPicker(FrameLayout frameLayout, String str, String str2, final Consumer<String> consumer) {
        final float[] fArr = new float[3];
        Color.colorToHSV(Theme.hex(str2, -10835492), fArr);
        Sheet title = new Sheet(this.c, this.th, str).title(str);
        LinearLayout body = title.body();
        final View view = new View(this.c);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, px(44.0f));
        layoutParams.bottomMargin = px(6.0f);
        body.addView(view, layoutParams);
        final EditText input = input(str2, Strings.t("ns.col.hex"), false, true, 528385);
        final Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                Kit.this.m365lambda$colorPicker$10$comaugmaosouiKit(fArr, view);
            }
        };
        final boolean[] zArr = {false};
        final Runnable runnable2 = new Runnable() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                Kit.lambda$colorPicker$11(runnable, zArr, input, fArr);
            }
        };
        body.addView(slider(Strings.t("ns.col.hue"), 0, 360, 1, Math.round(fArr[0]), Strings.t("ns.col.hue"), new IntFunction() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda12
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return Kit.lambda$colorPicker$12(i);
            }
        }, new Consumer() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Kit.lambda$colorPicker$13(fArr, runnable2, (Integer) obj);
            }
        }));
        body.addView(slider(Strings.t("ns.col.sat"), 0, 100, 1, Math.round(fArr[1] * 100.0f), Strings.t("ns.col.sat"), new IntFunction() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda14
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return Kit.lambda$colorPicker$14(i);
            }
        }, new Consumer() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Kit.lambda$colorPicker$15(fArr, runnable2, (Integer) obj);
            }
        }));
        body.addView(slider(Strings.t("ns.col.val"), 0, 100, 1, Math.round(fArr[2] * 100.0f), Strings.t("ns.col.val"), new IntFunction() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda16
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return Kit.lambda$colorPicker$16(i);
            }
        }, new Consumer() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Kit.lambda$colorPicker$17(fArr, runnable2, (Integer) obj);
            }
        }));
        input.addTextChangedListener(new TextWatcher() { // from class: com.jarvis.assist.aosui.ui.Kit.8
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (zArr[0]) {
                    return;
                }
                String trim = editable.toString().trim();
                if (!trim.startsWith("#")) {
                    trim = "#" + trim;
                }
                if (trim.matches("^#[0-9a-fA-F]{6}$")) {
                    Color.colorToHSV(Theme.hex(trim, 0), fArr);
                    runnable.run();
                }
            }
        });
        body.addView(input);
        runnable.run();
        title.buttons(new String[]{Strings.t("cancel"), Strings.t("ns.apply")}, new Runnable[]{null, new Runnable() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(String.format("#%06x", Integer.valueOf(Color.HSVToColor(fArr) & 16777215)));
            }
        }}, true).show(frameLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$colorPicker$10$com-augma-oso-ui-Kit, reason: not valid java name */
    public /* synthetic */ void m365lambda$colorPicker$10$comaugmaosouiKit(float[] fArr, View view) {
        int HSVToColor = Color.HSVToColor(fArr);
        view.setBackground(pill(HSVToColor, this.LINE2, this.dn, this.dn * 8.0f));
        view.setContentDescription(String.format("#%06x", Integer.valueOf(HSVToColor & 16777215)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$colorPicker$11(Runnable runnable, boolean[] zArr, EditText editText, float[] fArr) {
        runnable.run();
        zArr[0] = true;
        editText.setText(String.format("#%06x", Integer.valueOf(Color.HSVToColor(fArr) & 16777215)));
        zArr[0] = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String lambda$colorPicker$12(int i) {
        return i + "°";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$colorPicker$13(float[] fArr, Runnable runnable, Integer num) {
        fArr[0] = num.intValue();
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String lambda$colorPicker$14(int i) {
        return i + "%";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$colorPicker$15(float[] fArr, Runnable runnable, Integer num) {
        fArr[1] = num.intValue() / 100.0f;
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String lambda$colorPicker$16(int i) {
        return i + "%";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$colorPicker$17(float[] fArr, Runnable runnable, Integer num) {
        fArr[2] = num.intValue() / 100.0f;
        runnable.run();
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Grid extends View {
        int bg;
        final float dn;
        final Paint hz;
        int lastW;
        boolean light;
        int line;
        final Paint ln;

        public Grid(Context context, int i, int i2, boolean z) {
            super(context);
            this.ln = new Paint();
            this.hz = new Paint(1);
            this.dn = context.getResources().getDisplayMetrics().density;
            this.bg = i;
            this.line = i2;
            this.light = z;
            this.ln.setColor(i2);
            this.ln.setStrokeWidth(Math.max(1.0f, this.dn));
            setImportantForAccessibility(2);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            int width = getWidth();
            int height = getHeight();
            canvas.drawColor(this.bg);
            float f2 = this.dn * 27.0f;
            float f3 = 0.0f;
            while (true) {
                f = width;
                if (f3 >= f) {
                    break;
                }
                canvas.drawLine(f3, 0.0f, f3, height, this.ln);
                f3 += f2;
            }
            for (float f4 = 0.0f; f4 < height; f4 += f2) {
                canvas.drawLine(0.0f, f4, f, f4, this.ln);
            }
            if (this.light) {
                return;
            }
            if (this.lastW != width) {
                this.lastW = width;
                this.hz.setShader(new RadialGradient(f / 2.0f, 0.0f, Math.max(1.0f, f * 0.7f), Theme.rgba(43, 93, 130, 0.22f), 0, Shader.TileMode.CLAMP));
            }
            canvas.save();
            canvas.scale(1.0f, 0.37f);
            canvas.drawRect(0.0f, 0.0f, f, f * 0.7f, this.hz);
            canvas.restore();
        }
    }

    public void toast(final FrameLayout frameLayout, String str) {
        final TextView text = text(str, Fonts.MONO, 10.0f, 0.12f, this.INK);
        text.setPadding(px(16.0f), px(10.0f), px(16.0f), px(10.0f));
        text.setBackground(pill(this.light ? -218103809 : -234156268, this.LINE2, this.dn, this.dn * 999.0f));
        text.setGravity(17);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2, 81);
        layoutParams.setMargins(px(16.0f), 0, px(16.0f), px(40.0f));
        frameLayout.addView(text, layoutParams);
        text.announceForAccessibility(str);
        text.setAlpha(0.0f);
        text.animate().alpha(1.0f).setDuration(150L).start();
        text.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                r0.animate().alpha(0.0f).setDuration(200L).withEndAction(new Runnable() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        r1.removeView(r2);
                    }
                }).start();
            }
        }, ChargeFx.MS);
    }

    public LinearLayout themePicker(String str, final Consumer<String> consumer) {
        LinearLayout linearLayout = new LinearLayout(this.c);
        String value = HomeTheme.value(HomeTheme.parse(str));
        final String[] strArr = {"", "1", HomeTheme.AOA_V};
        String[] strArr2 = {"adv.theme.simple", "adv.theme.adv", "adv.theme.aoa"};
        final int i = 0;
        while (i < 3) {
            final boolean z = i > 0;
            boolean equals = strArr[i].equals(value);
            LinearLayout linearLayout2 = new LinearLayout(this.c);
            linearLayout2.setOrientation(1);
            linearLayout2.setPadding(px(8.0f), px(10.0f), px(8.0f), px(10.0f));
            linearLayout2.setBackground(new HudBg(equals ? Theme.a(TICK, 0.12f) : 0, equals ? TICK : this.LINE2, true, this.dn));
            View view = new View(this.c) { // from class: com.jarvis.assist.aosui.ui.Kit.9
                final Paint p = new Paint(1);
                final RectF r = new RectF();

                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    float width = getWidth();
                    float height = getHeight();
                    float f = width / 2.0f;
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeWidth(Math.max(1.0f, Kit.this.dn));
                    this.p.setColor(Kit.this.LINE2);
                    if (i == 2) {
                        Kit.this.aoaPreview(canvas, this.p, this.r, width, height);
                        return;
                    }
                    if (z) {
                        this.p.setColor(Kit.this.INK);
                        float f2 = width * 0.08f;
                        canvas.drawRect(f2, height * 0.04f, width * 0.92f, height * 0.16f, this.p);
                        this.p.setColor(Kit.this.LINE2);
                        float f3 = width * 0.34f;
                        float f4 = 0.2f * height;
                        this.r.set(f - f3, f4, f + f3, (width * 0.68f) + f4);
                        canvas.drawArc(this.r, 200.0f, 140.0f, false, this.p);
                        this.p.setStyle(Paint.Style.FILL);
                        this.p.setColor(Theme.a(Kit.TICK, 0.8f));
                        Canvas canvas2 = canvas;
                        canvas2.drawRect(f - f2, f4, f + f2, height * 0.25f, this.p);
                        this.p.setStyle(Paint.Style.STROKE);
                        this.p.setColor(Kit.this.ACC);
                        canvas2.drawCircle(f, f4 + f3, width * 0.09f, this.p);
                        this.p.setColor(Kit.this.LINE2);
                        int i2 = 0;
                        while (i2 < 5) {
                            float f5 = f2 + (i2 * width * 0.17f);
                            if (i2 == 2) {
                                this.p.setStyle(Paint.Style.FILL);
                                this.p.setColor(Theme.a(Kit.TICK, 0.8f));
                            }
                            canvas2.drawRect(f5, 0.56f * height, f5 + (width * 0.15f), 0.62f * height, this.p);
                            this.p.setStyle(Paint.Style.STROKE);
                            this.p.setColor(Kit.this.LINE2);
                            i2++;
                            canvas2 = canvas;
                        }
                        for (int i3 = 0; i3 < 8; i3++) {
                            float f6 = f2 + ((i3 % 4) * width * 0.215f);
                            float f7 = (height * 0.66f) + ((i3 / 4) * height * 0.1f);
                            canvas.drawRect(f6, f7, f6 + (width * 0.19f), f7 + (height * 0.08f), this.p);
                        }
                        Paint paint = new Paint(1);
                        float f8 = width * 0.1f;
                        float f9 = width * 0.6f;
                        paint.setShader(new LinearGradient(f8, 0.0f, f9, 0.0f, new int[]{Kit.TICK, -1774607, Kit.this.ACC}, (float[]) null, Shader.TileMode.CLAMP));
                        float f10 = height * 0.9f;
                        canvas.drawRect(f8, f10, f9, f10 + Math.max(1.0f, Kit.this.dn * 1.5f), paint);
                        return;
                    }
                    float f11 = width * 0.36f;
                    float f12 = height * 0.08f;
                    this.r.set(f - f11, f12, f + f11, (width * 0.72f) + f12);
                    canvas.drawArc(this.r, 200.0f, 140.0f, false, this.p);
                    float f13 = width * 0.08f;
                    this.r.inset(f13, f13);
                    canvas.drawArc(this.r, 200.0f, 140.0f, false, this.p);
                    this.p.setColor(Kit.this.ACC);
                    float f14 = width * 0.1f;
                    canvas.drawCircle(f, f12 + f11, f14, this.p);
                    this.p.setColor(Kit.this.LINE2);
                    float f15 = 0.52f * height;
                    canvas.drawRect(f14, f15, width * 0.9f, height * 0.72f, this.p);
                    this.p.setColor(Kit.TICK);
                    canvas.drawLine(f14, f15, width * 0.2f, f15, this.p);
                    this.p.setColor(Kit.this.LINE2);
                    float f16 = width * 0.4f;
                    float f17 = height * 0.8f;
                    this.r.set(f - f16, f17, f + f16, (width * 0.8f) + f17);
                    canvas.drawArc(this.r, 180.0f, 180.0f, false, this.p);
                }
            };
            view.setImportantForAccessibility(2);
            linearLayout2.addView(view, new LinearLayout.LayoutParams(-1, px(150.0f)));
            TextView text = text(Strings.t(strArr2[i]).toUpperCase(Locale.getDefault()), Fonts.MONO, 10.0f, 0.18f, equals ? this.INK : this.DIM);
            text.setPadding(0, px(8.0f), 0, px(2.0f));
            linearLayout2.addView(text);
            linearLayout2.addView(text(Strings.t(strArr2[i] + ".p"), Fonts.RAJ_400, 12.0f, 0.0f, this.DIM));
            linearLayout2.setClickable(true);
            linearLayout2.setFocusable(true);
            linearLayout2.setContentDescription(Strings.t(strArr2[i]) + ", " + Strings.t(strArr2[i] + ".p") + (equals ? ", selected" : ""));
            linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.ui.Kit$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    Kit.lambda$themePicker$21(consumer, strArr, i, view2);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
            if (i > 0) {
                layoutParams.leftMargin = px(6.0f);
            }
            linearLayout.addView(linearLayout2, layoutParams);
            i++;
        }
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$themePicker$21(Consumer consumer, String[] strArr, int i, View view) {
        Feedback.fx(view, 15);
        consumer.accept(strArr[i]);
    }

    void aoaPreview(Canvas canvas, Paint paint, RectF rectF, float f, float f2) {
        int rgba = Theme.rgba(192, 214, 220, 0.26f);
        float f3 = f / 2.0f;
        float max = Math.max(1.0f, this.dn);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(-16579322);
        canvas.drawRect(0.0f, 0.0f, f, f2, paint);
        paint.setColor(-16117484);
        float f4 = f * 0.08f;
        float f5 = f2 * 0.04f;
        float f6 = f * 0.92f;
        float f7 = f2 * 0.17f;
        canvas.drawRect(f4, f5, f6, f7, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(max);
        paint.setColor(-1511182);
        float f8 = f * 0.06f;
        float f9 = f4 + f8;
        canvas.drawLine(f4, f5, f9, f5, paint);
        float f10 = f5 + f8;
        canvas.drawLine(f4, f5, f4, f10, paint);
        float f11 = f6 - f8;
        canvas.drawLine(f6, f5, f11, f5, paint);
        canvas.drawLine(f6, f5, f6, f10, paint);
        canvas.drawLine(f4, f7, f9, f7, paint);
        float f12 = f7 - f8;
        canvas.drawLine(f4, f7, f4, f12, paint);
        canvas.drawLine(f6, f7, f11, f7, paint);
        canvas.drawLine(f6, f7, f6, f12, paint);
        paint.setColor(rgba);
        float f13 = f2 * 0.2f;
        canvas.drawLine(f4, f13, f6, f13, paint);
        paint.setColor(Theme.AOA_ORANGE);
        paint.setStrokeWidth(2.0f * max);
        canvas.drawLine(f4, f13, f * 0.2f, f13, paint);
        paint.setStrokeWidth(max);
        paint.setColor(rgba);
        float f14 = f * 0.36f;
        float f15 = 0.26f * f2;
        rectF.set(f3 - f14, f15, f3 + f14, (0.72f * f) + f15);
        canvas.drawArc(rectF, 200.0f, 140.0f, false, paint);
        Canvas canvas2 = canvas;
        float f16 = f15 + f14;
        float f17 = f * 0.17f;
        paint.setStyle(Paint.Style.FILL);
        float f18 = 0.9f;
        paint.setShader(new RadialGradient(f3, f16, f17, new int[]{Theme.a(Theme.AOA_ORANGE, 0.9f), Theme.a(Theme.AOA_ORANGE, 0.0f)}, (float[]) null, Shader.TileMode.CLAMP));
        canvas2.drawCircle(f3, f16, f17, paint);
        paint.setShader(null);
        float f19 = 0.8f;
        paint.setColor(Theme.a(-20368, 0.8f));
        int i = 0;
        while (i < 18) {
            double d = i * 2.4d;
            float f20 = f19;
            float f21 = (((i % 5) * 0.14f) + 0.4f) * f17;
            float f22 = f18;
            canvas2.drawCircle((((float) Math.cos(d)) * f21) + f3, f16 + (((float) Math.sin(d)) * f21 * f20), max * f22, paint);
            i++;
            f19 = f20;
            rgba = rgba;
            f18 = f22;
        }
        float f23 = f18;
        int i2 = rgba;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Theme.a(Theme.AOA_ORANGE, 0.7f));
        canvas2.drawCircle(f3, f16, f17 * 0.62f, paint);
        paint.setColor(i2);
        int i3 = 0;
        while (i3 < 5) {
            float f24 = f4 + (i3 * f * 0.17f);
            if (i3 == 2) {
                paint.setColor(Theme.AOA_ORANGE);
            }
            canvas2.drawRect(f24, f2 * 0.6f, f24 + (0.15f * f), f2 * 0.65f, paint);
            paint.setColor(i2);
            i3++;
            canvas2 = canvas;
        }
        for (int i4 = 0; i4 < 8; i4++) {
            float f25 = f4 + ((i4 % 4) * f * 0.215f);
            float f26 = ((i4 / 4) * f2 * 0.09f) + (0.69f * f2);
            canvas.drawRect(f25, f26, f25 + (0.19f * f), f26 + (0.07f * f2), paint);
        }
        paint.setColor(Theme.AOA_ORANGE);
        float f27 = f * 0.3f;
        float f28 = f2 * f23;
        rectF.set(f3 - f27, f28, f3 + f27, (f * 0.6f) + f28);
        canvas.drawArc(rectF, 200.0f, 140.0f, false, paint);
        paint.setColor(Theme.AOA_TEAL);
        float f29 = f2 * 0.97f;
        canvas.drawLine(f * 0.1f, f29, f27, f29, paint);
    }
}
