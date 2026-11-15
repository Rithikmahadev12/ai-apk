package com.jarvis.assist.ui;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.jarvis.assist.core.Fonts;
import com.jarvis.assist.core.Palette;

/**
 * OrbView — AugmaOS animated radar HUD.
 *
 * Reconstructed from:
 *   com.augma.oso.home.AoaOrb
 *   com.augma.oso.core.OrbPainter
 *   com.augma.oso.home.HubRing
 *   com.augma.oso.brief.DayRing
 *   com.augma.oso.core.RingMath
 *
 * Features:
 *   - Rotating sweep line + gradient trail (AugmaOS radar pattern)
 *   - Concentric ring grid  (#162030 on #020304 bg)
 *   - Radar blips that light up as sweep passes over them
 *   - 6 tab labels around outer ring (tappable)
 *   - Center orb glow pulse
 *   - Day-part greeting ring (DayRing)
 *   - 60fps via Choreographer-style Handler loop
 */
public class OrbView extends View {

    // ── Tab click callback ────────────────────────────────────────────────────

    public interface OnTabClick { void onTab(String tab); }
    private OnTabClick mTabCb;
    public void setOnTabClick(OnTabClick cb) { mTabCb = cb; }

    // ── Tab definitions ───────────────────────────────────────────────────────

    private static final String[] TABS = {
        "CHAT", "VAULT", "WALLET", "MEMORY", "REMIND", "HABITS"
    };
    private static final float[] TAB_DEG = {
        -90f, -30f, 30f, 90f, 150f, 210f
    };
    private static final int[] TAB_COLORS = {
        Palette.CYAN, Palette.PURPLE, Palette.GREEN,
        Palette.BLUE, Palette.TEAL, Palette.ORANGE
    };
    private String mActiveTab = "CHAT";

    // ── Blip data ─────────────────────────────────────────────────────────────

    private static final float[] BLIP_DEG = {
        25f, 72f, 130f, 185f, 245f, 310f, 48f, 160f, 290f
    };
    private static final float[] BLIP_R = {
        0.30f, 0.50f, 0.65f, 0.38f, 0.55f, 0.42f, 0.72f, 0.28f, 0.60f
    };
    private final float[] mBlipAlpha = new float[BLIP_DEG.length];

    // ── Paint pool ────────────────────────────────────────────────────────────

    private final Paint pRing   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pSweep  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pDot    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pGlow   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pText   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pLine   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pActive = new Paint(Paint.ANTI_ALIAS_FLAG);

    // ── State ─────────────────────────────────────────────────────────────────

    private float mSweep   = 0f;       // current sweep angle degrees
    private float mPulse   = 0f;       // orb pulse phase 0..2π
    private final RectF mOval = new RectF();
    private final Handler mH = new Handler(Looper.getMainLooper());
    private long mLastFrame = 0;

    // ── Greeting ──────────────────────────────────────────────────────────────

    private String mGreeting = "";
    private String mUserName = "";

    public void setGreeting(String g, String name) {
        mGreeting = g; mUserName = name; invalidate();
    }

    public void setActiveTab(String tab) {
        mActiveTab = tab; invalidate();
    }

    // ── Init ──────────────────────────────────────────────────────────────────

    public OrbView(Context ctx) {
        super(ctx);
        pRing.setStyle(Paint.Style.STROKE);
        pSweep.setStyle(Paint.Style.FILL);
        pDot.setStyle(Paint.Style.FILL);
        pGlow.setStyle(Paint.Style.FILL);
        pText.setTextAlign(Paint.Align.CENTER);
        pLine.setStyle(Paint.Style.STROKE);
        pActive.setStyle(Paint.Style.FILL);

        try {
            pGlow.setMaskFilter(new BlurMaskFilter(28f, BlurMaskFilter.Blur.NORMAL));
        } catch (Exception ignored) {}

        if (Fonts.MONO != null) pText.setTypeface(Fonts.MONO);
        if (Fonts.BRAND != null) {
            Paint tmp = new Paint(pText);
            tmp.setTypeface(Fonts.BRAND);
        }

        startLoop();
    }

    private final Runnable mTick = new Runnable() {
        @Override public void run() {
            long now = System.currentTimeMillis();
            float dt = mLastFrame == 0 ? 16f : Math.min(50f, now - mLastFrame);
            mLastFrame = now;

            // Advance sweep ~1.8°/frame at 60fps = ~108°/s
            mSweep = (mSweep + dt * 0.108f) % 360f;
            mPulse = (float)((mPulse + dt * 0.003f) % (2 * Math.PI));

            // Update blip alphas
            for (int i = 0; i < BLIP_DEG.length; i++) {
                float diff = ((mSweep - BLIP_DEG[i]) % 360f + 360f) % 360f;
                if (diff < 8f) {
                    mBlipAlpha[i] = 1.0f;
                } else {
                    mBlipAlpha[i] = Math.max(0f, mBlipAlpha[i] - dt * 0.001f);
                }
            }

            invalidate();
            mH.postDelayed(this, 16);
        }
    };

    private void startLoop() { mH.post(mTick); }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mH.removeCallbacksAndMessages(null);
    }

    // ── Draw ──────────────────────────────────────────────────────────────────

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int w = getWidth(), h = getHeight();
        final float cx = w * 0.5f, cy = h * 0.5f;
        final float maxR = Math.min(cx, cy) * 0.82f;

        // ── Background ────────────────────────────────────────────────────────
        canvas.drawColor(Palette.BG_DEEP);

        // ── Grid rings ───────────────────────────────────────────────────────
        pRing.setColor(Palette.BORDER);
        pRing.setStrokeWidth(1.0f);
        float[] ringR = {0.22f, 0.42f, 0.62f, 0.82f, 1.0f};
        for (float r : ringR) {
            canvas.drawCircle(cx, cy, maxR * r, pRing);
        }

        // ── Crosshairs ────────────────────────────────────────────────────────
        pLine.setColor(Palette.BORDER);
        pLine.setStrokeWidth(0.8f);
        canvas.drawLine(cx - maxR, cy, cx + maxR, cy, pLine);
        canvas.drawLine(cx, cy - maxR, cx, cy + maxR, pLine);

        // Diagonal thin lines
        float dOff = maxR * 0.707f;
        canvas.drawLine(cx - dOff, cy - dOff, cx + dOff, cy + dOff, pLine);
        canvas.drawLine(cx + dOff, cy - dOff, cx - dOff, cy + dOff, pLine);

        // ── Sweep gradient trail ──────────────────────────────────────────────
        mOval.set(cx - maxR, cy - maxR, cx + maxR, cy + maxR);
        for (int i = 0; i < 100; i++) {
            float startA = mSweep - i;
            float alpha = (1f - i / 100f);
            alpha = alpha * alpha; // ease
            int a = (int)(255 * alpha * 0.45f);
            if (a <= 0) continue;
            pSweep.setColor(Color.argb(a, 0x5a, 0xa9, 0xdc));
            canvas.drawArc(mOval, startA, 1.1f, true, pSweep);
        }

        // ── Sweep line ────────────────────────────────────────────────────────
        pLine.setColor(Palette.CYAN);
        pLine.setStrokeWidth(2.2f);
        double sweepRad = Math.toRadians(mSweep);
        canvas.drawLine(cx, cy,
            cx + (float)(maxR * Math.cos(sweepRad)),
            cy + (float)(maxR * Math.sin(sweepRad)),
            pLine);

        // ── Radar blips ───────────────────────────────────────────────────────
        for (int i = 0; i < BLIP_DEG.length; i++) {
            float a = mBlipAlpha[i];
            if (a <= 0.01f) continue;
            double bRad = Math.toRadians(BLIP_DEG[i]);
            float bx = cx + (float)(maxR * BLIP_R[i] * Math.cos(bRad));
            float by = cy + (float)(maxR * BLIP_R[i] * Math.sin(bRad));

            // Outer glow
            pGlow.setColor(Color.argb((int)(a * 60), 0x5a, 0xa9, 0xdc));
            canvas.drawCircle(bx, by, 22f * a, pGlow);

            // Core dot
            pDot.setColor(Color.argb((int)(a * 255), 0x5a, 0xa9, 0xdc));
            canvas.drawCircle(bx, by, 4.5f, pDot);

            // Ring around bright blips
            if (a > 0.7f) {
                pRing.setColor(Color.argb((int)(a * 100), 0x5a, 0xa9, 0xdc));
                pRing.setStrokeWidth(1f);
                canvas.drawCircle(bx, by, 10f * a, pRing);
            }
        }

        // ── Center orb ────────────────────────────────────────────────────────
        float pulse = 0.85f + 0.15f * (float)Math.sin(mPulse);

        // Outer glow
        pGlow.setColor(0x225aa9dc);
        canvas.drawCircle(cx, cy, 48f * pulse, pGlow);

        // Mid ring
        pRing.setColor(0x405aa9dc);
        pRing.setStrokeWidth(1.5f);
        canvas.drawCircle(cx, cy, 28f * pulse, pRing);

        // Inner fill
        pDot.setColor(Palette.BG_CARD);
        canvas.drawCircle(cx, cy, 18f, pDot);

        // Core dot
        pDot.setColor(Palette.CYAN);
        canvas.drawCircle(cx, cy, 6f, pDot);

        pDot.setColor(Palette.TEXT_HI);
        canvas.drawCircle(cx, cy, 2.5f, pDot);

        // ── Tab labels around outer ring ──────────────────────────────────────
        float labelR = maxR * 1.10f;
        float tabTextSp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            9.5f, getResources().getDisplayMetrics());
        pText.setTextSize(tabTextSp);

        for (int i = 0; i < TABS.length; i++) {
            boolean active = TABS[i].equals(mActiveTab);
            double tRad = Math.toRadians(TAB_DEG[i]);
            float tx = cx + (float)(labelR * Math.cos(tRad));
            float ty = cy + (float)(labelR * Math.sin(tRad)) + pText.getTextSize() * 0.35f;

            if (active) {
                // Active tab: filled bg pill + bright text
                float tw = pText.measureText(TABS[i]);
                float pad = 10f;
                pActive.setColor(TAB_COLORS[i]);
                RectF pillR = new RectF(tx - tw/2 - pad, ty - pText.getTextSize() - 2,
                    tx + tw/2 + pad, ty + 4);
                canvas.drawRoundRect(pillR, 6f, 6f, pActive);
                pText.setColor(Palette.BG_DEEP);
            } else {
                pText.setColor(Palette.TEXT_DIM);
            }
            canvas.drawText(TABS[i], tx, ty, pText);
        }

        // ── Greeting text (center orb area) ───────────────────────────────────
        if (!mGreeting.isEmpty()) {
            float greetSp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                10f, getResources().getDisplayMetrics());
            pText.setTextSize(greetSp);
            pText.setColor(Palette.TEXT_DIM);
            if (Fonts.MONO != null) pText.setTypeface(Fonts.MONO);
            canvas.drawText(mGreeting.toUpperCase(), cx, cy - 36f, pText);
        }
        if (!mUserName.isEmpty()) {
            float nameSp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                13f, getResources().getDisplayMetrics());
            pText.setTextSize(nameSp);
            pText.setColor(Palette.TEXT_HI);
            if (Fonts.BRAND != null) pText.setTypeface(Fonts.BRAND);
            canvas.drawText(mUserName.toUpperCase(), cx, cy + 48f, pText);
        }
    }

    // ── Touch ─────────────────────────────────────────────────────────────────

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != MotionEvent.ACTION_UP) return true;
        if (mTabCb == null) return true;

        final float cx = getWidth() * 0.5f, cy = getHeight() * 0.5f;
        final float maxR = Math.min(cx, cy) * 0.82f;
        float labelR = maxR * 1.10f;
        float tx = ev.getX(), ty = ev.getY();

        for (int i = 0; i < TABS.length; i++) {
            double tRad = Math.toRadians(TAB_DEG[i]);
            float lx = cx + (float)(labelR * Math.cos(tRad));
            float ly = cy + (float)(labelR * Math.sin(tRad));
            float dist = (float)Math.sqrt(Math.pow(tx - lx, 2) + Math.pow(ty - ly, 2));
            if (dist < dp(getContext(), 36)) {
                mActiveTab = TABS[i];
                mTabCb.onTab(TABS[i]);
                return true;
            }
        }
        return true;
    }

    private int dp(Context ctx, float v) {
        return Math.round(TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, v, ctx.getResources().getDisplayMetrics()));
    }
}
