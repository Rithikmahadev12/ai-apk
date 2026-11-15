package com.jarvis.assist.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarvis.assist.core.Fonts;
import com.jarvis.assist.core.Palette;

/**
 * Kit — AugmaOS UI component factory.
 * Mirrors com.augma.oso.ui.Kit from DEX.
 */
public final class Kit {

    private Kit() {}

    // ── Layout params ─────────────────────────────────────────────────────────

    public static LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static LinearLayout.LayoutParams wrapWrap() {
        return new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams fMatchMatch() {
        return new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static FrameLayout.LayoutParams fMatchWrap() {
        return new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    // ── dp helper ─────────────────────────────────────────────────────────────

    public static int dp(Context ctx, float v) {
        return Math.round(TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, v, ctx.getResources().getDisplayMetrics()));
    }

    // ── Drawables ─────────────────────────────────────────────────────────────

    public static GradientDrawable circle(int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setColor(color);
        return gd;
    }

    public static GradientDrawable rect(int fill, int stroke, int strokeColor, int cornerDp, Context ctx) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(fill);
        if (stroke > 0) gd.setStroke(dp(ctx, stroke), strokeColor);
        gd.setCornerRadius(dp(ctx, cornerDp));
        return gd;
    }

    // ── Spacers ───────────────────────────────────────────────────────────────

    public static View vSpace(Context ctx, int heightDp) {
        View v = new View(ctx);
        v.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(ctx, heightDp)));
        return v;
    }

    public static View hSpace(Context ctx, int widthDp) {
        View v = new View(ctx);
        v.setLayoutParams(new LinearLayout.LayoutParams(dp(ctx, widthDp), 1));
        return v;
    }

    public static View divider(Context ctx, int color, int heightDp, int topDp, int botDp) {
        View v = new View(ctx);
        v.setBackgroundColor(color);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(ctx, heightDp));
        lp.topMargin = dp(ctx, topDp);
        lp.bottomMargin = dp(ctx, botDp);
        v.setLayoutParams(lp);
        return v;
    }

    // ── Text ──────────────────────────────────────────────────────────────────

    public static TextView monoTv(Context ctx, String text, int color, float spSz) {
        TextView tv = new TextView(ctx);
        tv.setText(text);
        tv.setTypeface(Fonts.MONO);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSz);
        tv.setTextColor(color);
        return tv;
    }

    public static TextView brandTv(Context ctx, String text, int color, float spSz) {
        TextView tv = new TextView(ctx);
        tv.setText(text);
        tv.setTypeface(Fonts.BRAND);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSz);
        tv.setTextColor(color);
        return tv;
    }

    public static TextView bodyTv(Context ctx, String text, int color, float spSz) {
        TextView tv = new TextView(ctx);
        tv.setText(text);
        tv.setTypeface(Fonts.BODY);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSz);
        tv.setTextColor(color);
        return tv;
    }

    // ── Input ─────────────────────────────────────────────────────────────────

    public static EditText input(Context ctx, String hint) {
        EditText et = new EditText(ctx);
        et.setHint(hint);
        et.setHintTextColor(Palette.TEXT_DIM);
        et.setTextColor(Palette.TEXT_HI);
        et.setTypeface(Fonts.MONO);
        et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        et.setBackground(rect(Palette.BG_CARD, 1, Palette.BORDER, 4, ctx));
        et.setPadding(dp(ctx, 14), dp(ctx, 12), dp(ctx, 14), dp(ctx, 12));
        LinearLayout.LayoutParams lp = matchWrap();
        lp.bottomMargin = dp(ctx, 6);
        et.setLayoutParams(lp);
        return et;
    }

    // ── Buttons ───────────────────────────────────────────────────────────────

    public static Button btn(Context ctx, String label, int bgColor, int textColor) {
        Button btn = new Button(ctx);
        btn.setText(label);
        btn.setTypeface(Fonts.MONO);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        btn.setTextColor(textColor);
        btn.setBackground(rect(bgColor, 0, 0, 3, ctx));
        btn.setAllCaps(true);
        btn.setLetterSpacing(0.12f);
        btn.setPadding(dp(ctx, 16), dp(ctx, 12), dp(ctx, 16), dp(ctx, 12));
        LinearLayout.LayoutParams lp = matchWrap();
        lp.topMargin = dp(ctx, 4);
        lp.bottomMargin = dp(ctx, 4);
        btn.setLayoutParams(lp);
        return btn;
    }

    public static Button smallBtn(Context ctx, String label, int bgColor, int textColor) {
        Button btn = new Button(ctx);
        btn.setText(label);
        btn.setTypeface(Fonts.MONO);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f);
        btn.setTextColor(textColor);
        btn.setBackground(rect(bgColor, 0, 0, 2, ctx));
        btn.setAllCaps(true);
        btn.setPadding(dp(ctx, 10), dp(ctx, 5), dp(ctx, 10), dp(ctx, 5));
        btn.setLayoutParams(wrapWrap());
        return btn;
    }

    // ── Pill / chip ───────────────────────────────────────────────────────────

    public static TextView pill(Context ctx, String label, int bgColor, int textColor) {
        TextView tv = new TextView(ctx);
        tv.setText(label.toUpperCase());
        tv.setTypeface(Fonts.MONO);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f);
        tv.setTextColor(textColor);
        tv.setBackground(rect(bgColor, 0, 0, 20, ctx));
        tv.setPadding(dp(ctx, 10), dp(ctx, 4), dp(ctx, 10), dp(ctx, 4));
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(wrapWrap());
        return tv;
    }

    // ── Section header ────────────────────────────────────────────────────────

    public static TextView sectionHeader(Context ctx, String title) {
        TextView tv = new TextView(ctx);
        tv.setText(title);
        tv.setTypeface(Fonts.BRAND);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        tv.setTextColor(Palette.CYAN);
        tv.setLetterSpacing(0.15f);
        LinearLayout.LayoutParams lp = matchWrap();
        lp.topMargin = dp(ctx, 4);
        lp.bottomMargin = dp(ctx, 10);
        tv.setLayoutParams(lp);
        return tv;
    }

    // ── Status label ─────────────────────────────────────────────────────────

    public static TextView statusLabel(Context ctx, String text, int color) {
        TextView tv = new TextView(ctx);
        tv.setText(text);
        tv.setTypeface(Fonts.MONO);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        tv.setTextColor(color);
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = matchWrap();
        lp.topMargin = dp(ctx, 24);
        lp.bottomMargin = dp(ctx, 24);
        tv.setLayoutParams(lp);
        return tv;
    }

    // ── Progress bar ──────────────────────────────────────────────────────────

    public static ProgressBar progress(Context ctx) {
        ProgressBar pb = new ProgressBar(ctx);
        pb.setIndeterminate(true);
        pb.setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = matchWrap();
        lp.topMargin = dp(ctx, 8);
        lp.bottomMargin = dp(ctx, 8);
        pb.setLayoutParams(lp);
        return pb;
    }

    // ── Data row card ─────────────────────────────────────────────────────────

    public static LinearLayout dataRow(Context ctx, String prefix, String value) {
        LinearLayout row = new LinearLayout(ctx);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBackground(rect(Palette.BG_CARD, 0, 0, 4, ctx));
        row.setPadding(dp(ctx, 14), dp(ctx, 11), dp(ctx, 14), dp(ctx, 11));
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView pre = monoTv(ctx, prefix, Palette.CYAN, 11f);
        pre.setLayoutParams(wrapWrap());
        row.addView(pre);
        row.addView(hSpace(ctx, 10));

        TextView val = bodyTv(ctx, value, Palette.TEXT_HI, 13f);
        LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        val.setLayoutParams(vlp);
        row.addView(val);

        LinearLayout.LayoutParams lp = matchWrap();
        lp.bottomMargin = dp(ctx, 6);
        row.setLayoutParams(lp);
        return row;
    }
}
