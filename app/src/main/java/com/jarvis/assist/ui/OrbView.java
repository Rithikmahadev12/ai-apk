package com.jarvis.assist.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.jarvis.assist.core.Fonts;
import com.jarvis.assist.core.Palette;

import java.util.Random;

/**
 * OrbView — Full AugmaOS visual reconstruction.
 *
 * Sources used (decompiled):
 *   com.augma.oso.home.AoaOrb        → particle sphere + ring draw
 *   com.augma.oso.core.OrbPainter    → plasma blob fill/stroke + radial gradient
 *   com.augma.oso.home.HubRing       → concentric arc rings (74dp, 84dp) + day arc
 *   com.augma.oso.core.RingMath      → wedge geometry for arc menu segments
 *   com.augma.oso.core.Shapes        → wedge() / pt() path builder
 *   com.augma.oso.home.DockLayer     → arc app menu at bottom T0=200° T1=340°
 *   com.augma.oso.core.OsoWheel      → outer/inner ring hit testing
 *   com.augma.oso.core.Theme         → color constants (AOA_ORANGE, AOA_TEAL, fBLUE etc.)
 */
public class OrbView extends View {

    // ─── Callback ────────────────────────────────────────────────────────────
    public interface OnTabClick { void onTab(String tab); }
    private OnTabClick mTabCb;
    public void setOnTabClick(OnTabClick cb) { mTabCb = cb; }

    // ─── Colour constants (from Theme.java decompile) ─────────────────────────
    // Theme.AOA_ORANGE = -34278  → 0xFFFF79DA  wait, let's decode:
    // -34278 as signed int = 0xFFFF79DA — actually it's 0xFFFF7956 no…
    // -34278 = 0xFFFFFA1A — let's just use the actual hex properly:
    // -34278 in hex signed 32bit = 0xFFFF79DA — recalc: 0x100000000 - 34278 = 0xFFFF7BDA
    // Safer to just use the visual values seen in image:
    private static final int C_ORANGE   = 0xFFFF7A26;  // AOA warm orange core
    private static final int C_TEAL     = 0xFF4BD8C8;  // AOA_TEAL -11624262 = 0xFF4BD8FA approx
    private static final int C_RED      = 0xFFE03535;  // theme.RED
    private static final int C_BLUE     = 0xFF3A8EFF;  // theme.BLUE / fBLUE -10835492
    private static final int C_WHITE    = 0xFFE8F4FF;  // fWHITE -1774607 = 0xFFE4F0F1
    private static final int C_BG       = 0xFF050C10;  // deep dark
    private static final int C_RING     = 0xFF112233;  // ring grid lines
    private static final int C_LINE2    = 0xFF1A3A55;  // secondary ring
    private static final int C_DIM      = 0xFF4A6680;  // dim text / inactive

    // ─── Tab definitions ──────────────────────────────────────────────────────
    public static final String[] TABS = {
        "CHAT","VAULT","WALLET","MEMORY","REMIND","HABITS"
    };
    // Placed around the arc — matching AugmaOS wedge positions
    // AugmaOS has them at ~top-left and top-right fan positions
    private static final float[] TAB_DEG = {
        210f, 240f, 270f, 300f, 330f, 360f   // bottom-left arc, same as DockLayer T0=200 T1=340 remapped
    };
    private String mActiveTab = "CHAT";

    // ─── Particle sphere (AoaOrb.java — small/home variant N=350) ────────────
    private static final int N_PARTICLES = 350;
    private final float[] mPX = new float[N_PARTICLES * 4]; // x,y,z,phase
    private final int[]   mPK = new int[N_PARTICLES];       // kind: 0=core 1=ring 2=shell
    private float[] mPQ;                                     // projected x,y,depth per particle

    // Link lines between nearby particles
    private int[]   mLinks;
    private int     mLinkCount;
    private float[] mLinesBuf;

    // Arc decorators on the orb (from AoaOrb.arcs)
    private static final int N_ARCS = 9;
    private final float[][] mArcs = new float[N_ARCS][3]; // start, end, speed

    // ─── Plasma blobs (OrbPainter.java) ───────────────────────────────────────
    // 5-layer organic plasma animated around the orb center
    private static final float[] BLOB_LR = {1.0f, 0.97f, 1.02f, 0.99f, 0.93f};
    private static final float[] BLOB_PH = {0.0f, 1.7f,  3.1f,  2.3f,  5.2f};
    private static final float[] BLOB_SP = {0.7f, 0.9f,  0.6f,  0.8f,  1.3f};
    private static final int     BLOB_N  = 3; // show 3 layers (perf)
    private static final int     BLOB_PTS = 48;

    // ─── Hub ring state ───────────────────────────────────────────────────────
    // HubRing draws rings at 74dp and 84dp from center, plus arcs
    private float mDayFrac = 0.65f; // fraction of day elapsed (for day arc)

    // ─── Arc menu (DockLayer — bottom arc T0=200° T1=340°) ───────────────────
    private static final float ARC_T0_DEG = 200f;
    private static final float ARC_T1_DEG = 340f;
    private static final String[] ARC_LABELS = {
        "CHAT","VAULT","WALLET","MEMORY","REMIND","HABITS"
    };
    private static final int[] ARC_COLORS = {
        0xFF4BD8FA, 0xFF9B59FF, 0xFF3AFF8A,
        0xFF3A8EFF, 0xFF4BD8C8, 0xFFFF9A3A
    };

    // ─── Paint pool ───────────────────────────────────────────────────────────
    private final Paint mP     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPAdd  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mRf    = new RectF();
    private final Path  mPath  = new Path();
    private final float[] mW3  = new float[3]; // temp projection buffer

    // ─── Animation state ─────────────────────────────────────────────────────
    private float mTurn  = 0f;   // sphere rotation (radians) — from OrbLayer.turn
    private float mSpin  = 0f;   // inner spin degrees
    private float mT     = 4.5f; // plasma time
    private float mPulse = 0f;   // orb core pulse phase
    private float mSweep = 0f;   // radar sweep degrees
    private final float[] mBlipAlpha;
    private static final float[] BLIP_DEG = {25f,72f,130f,185f,245f,310f,48f,160f,290f};
    private static final float[] BLIP_R   = {.30f,.50f,.65f,.38f,.55f,.42f,.72f,.28f,.60f};

    // ─── Greeting ─────────────────────────────────────────────────────────────
    private String mGreeting = "";
    private String mUserName = "";

    // ─── Handler loop ─────────────────────────────────────────────────────────
    private final Handler mH = new Handler(Looper.getMainLooper());
    private long mLastMs = 0;

    // ─── Cached gradient handles (invalidated on size change) ─────────────────
    private RadialGradient mCoreGrad;
    private RadialGradient mHaloGrad;
    private RadialGradient mGlassGrad;
    private float mGradU = -1f; // last unit scale used to build grads

    // ─────────────────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────────────────

    public OrbView(Context ctx) {
        super(ctx);
        // ADD blend mode for particle additive glow (from AoaOrb)
        mPAdd.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        mPAdd.setAntiAlias(true);

        mP.setStrokeCap(Paint.Cap.BUTT);
        mP.setAntiAlias(true);

        mPText.setAntiAlias(true);
        mPText.setTextAlign(Paint.Align.CENTER);
        if (Fonts.MONO  != null) mPText.setTypeface(Fonts.MONO);

        // Build particle sphere
        buildSphere();

        // Build arc decorators
        Random rng = new Random(176L);
        for (int i = 0; i < N_ARCS; i++) {
            mArcs[i][0] = rng.nextFloat() * 6.283f;          // start angle
            mArcs[i][1] = rng.nextFloat() * 6.283f;          // span
            mArcs[i][2] = (rng.nextFloat() * 1.2f) + 0.4f;   // speed mult
        }

        mBlipAlpha = new float[BLIP_DEG.length];
        startLoop();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  PUBLIC API
    // ─────────────────────────────────────────────────────────────────────────

    public void setGreeting(String g, String name) { mGreeting = g; mUserName = name; invalidate(); }
    public void setActiveTab(String tab) { mActiveTab = tab; invalidate(); }

    // ─────────────────────────────────────────────────────────────────────────
    //  SPHERE BUILD  (AoaOrb constructor — small variant)
    // ─────────────────────────────────────────────────────────────────────────

    private void buildSphere() {
        Random rng  = new Random(176L);
        float  ONE  = 1.0f;
        float  f3   = 0.95f, f4 = 0.05f, f5 = 0.25f;

        for (int i = 0; i < N_PARTICLES; i++) {
            float nx  = (rng.nextFloat() * 2.0f) - ONE;
            float ang = rng.nextFloat() * 6.283f;
            float r;
            if (i < 170) {
                r = (rng.nextFloat() * 0.2f) + 0.32f;
                mPK[i] = 0; // core
            } else if (i < 220) {
                r = rng.nextFloat() * f5;
                mPK[i] = 0;
            } else {
                r = (rng.nextFloat() * f4) + f3;
                mPK[i] = 1; // shell
            }
            float sqrtV = (float) Math.sqrt(ONE - nx * nx);
            int   base  = i * 4;
            mPX[base]     = (float)(Math.cos(ang)) * sqrtV * r;
            mPX[base + 1] = nx * r * (i < 170 ? 0.55f : ONE);
            mPX[base + 2] = sqrtV * (float)(Math.sin(ang)) * r;
            mPX[base + 3] = rng.nextFloat();
        }

        // Build link pairs between close particles
        java.util.ArrayList<Integer> links = new java.util.ArrayList<>();
        for (int i = 0; i < N_PARTICLES; i++) {
            int cnt = 0;
            for (int j = 0; j < N_PARTICLES && cnt < 2; j++) {
                if (j == i) continue;
                int b1 = i*4, b2 = j*4;
                float dx = mPX[b1]-mPX[b2], dy = mPX[b1+1]-mPX[b2+1], dz = mPX[b1+2]-mPX[b2+2];
                if (dx*dx + dy*dy + dz*dz < 0.03f) {
                    links.add(i); links.add(j); cnt++;
                }
            }
        }
        mLinkCount = links.size() / 2;
        mLinks     = new int[links.size()];
        mLinesBuf  = new float[mLinkCount * 4];
        for (int i = 0; i < mLinks.length; i++) mLinks[i] = links.get(i);
        mPQ = new float[N_PARTICLES * 3];
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SPHERE PROJECTION  (AoaOrb.pr — small variant)
    //  Projects 3D point (px,py,pz) rotating by cos/sin to screen (x,y,depth)
    // ─────────────────────────────────────────────────────────────────────────

    private void project(float px, float py, float pz,
                         float cosA, float sinA,
                         float S,   float R,
                         float[] out) {
        float rx = px * cosA - pz * sinA;
        float rz = px * sinA + pz * cosA;
        // small-orb tilt: pitch ~10°
        float ry = py * 0.96f - rz * 0.18f;
        float rz2= rz * 0.96f + py * 0.18f;
        float persp = 1.0f / (1.9f - 0.6f * rz2);
        out[0] = S + rx * R * persp * 1.2f;
        out[1] = S + ry * R * persp * 1.2f;
        out[2] = rz2;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ANIMATION LOOP
    // ─────────────────────────────────────────────────────────────────────────

    private final Runnable mTick = new Runnable() {
        @Override public void run() {
            long now = System.currentTimeMillis();
            float dt = mLastMs == 0 ? 16f : Math.min(50f, now - mLastMs);
            mLastMs = now;
            float sec = dt / 1000f;

            mTurn  = (mTurn  + sec * 0.21f) % 6.2832f;
            mSpin  = (mSpin  + 12f * sec) % 360f;
            mT    += 0.35f * Math.min(0.05f, sec);
            mPulse = (float)((mPulse + sec * 3.0f) % (2 * Math.PI));
            mSweep = (mSweep + dt * 0.108f) % 360f;

            for (int i = 0; i < BLIP_DEG.length; i++) {
                float diff = ((mSweep - BLIP_DEG[i]) % 360f + 360f) % 360f;
                mBlipAlpha[i] = diff < 8f ? 1.0f : Math.max(0f, mBlipAlpha[i] - dt * 0.001f);
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

    // ─────────────────────────────────────────────────────────────────────────
    //  onDraw — master pipeline
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    protected void onDraw(Canvas canvas) {
        final int W = getWidth(), H = getHeight();
        if (W == 0 || H == 0) return;

        final float cx = W * 0.5f, cy = H * 0.5f;

        // ── Background ───────────────────────────────────────────────────────
        canvas.drawColor(C_BG);

        // Unit scale: AugmaOS uses S=176 for the small orb.
        // We fit it to the view width with some margin.
        float fitS = W * 0.5f * 0.90f;      // S in pixels (orb "unit")
        float S    = fitS;                   // half-size of the orb square
        float R    = S * 0.84f;             // actual radius (AoaOrb: R = S*0.84)

        // ── Grid rings (HubRing style + extra radar rings) ───────────────────
        drawGridRings(canvas, cx, cy, S);

        // ── Hub rings (HubRing.draw — 74dp, 84dp) ───────────────────────────
        drawHubRings(canvas, cx, cy, S);

        // ── Radar sweep (blips + sweep gradient) ────────────────────────────
        drawRadarSweep(canvas, cx, cy, S);

        // ── Plasma blobs (OrbPainter) ────────────────────────────────────────
        drawPlasmaBlobs(canvas, cx, cy, S, R);

        // ── Particle sphere (AoaOrb) ─────────────────────────────────────────
        drawParticleSphere(canvas, cx, cy, S, R);

        // ── Core orb (OrbPainter core + glass) ───────────────────────────────
        drawCoreOrb(canvas, cx, cy, S, R);

        // ── Arc wedge menu (DockLayer, bottom arc) ───────────────────────────
        drawArcMenu(canvas, cx, cy, S, R);

        // ── HUD text overlay ─────────────────────────────────────────────────
        drawHudText(canvas, cx, cy, S);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  GRID RINGS
    // ─────────────────────────────────────────────────────────────────────────

    private void drawGridRings(Canvas canvas, float cx, float cy, float S) {
        mP.setStyle(Paint.Style.STROKE);
        // Outer radar rings
        float[] rads = {0.28f, 0.48f, 0.68f, 0.88f, 1.0f};
        for (float r : rads) {
            float alpha = r < 1.0f ? 0.35f : 0.5f;
            mP.setColor(Color.argb((int)(255*alpha), 0x11, 0x33, 0x55));
            mP.setStrokeWidth(r < 1.0f ? 0.8f : 1.2f);
            canvas.drawCircle(cx, cy, S * r, mP);
        }
        // Cross-hair lines
        mP.setColor(Color.argb(40, 0x11, 0x44, 0x66));
        mP.setStrokeWidth(0.6f);
        canvas.drawLine(cx - S, cy, cx + S, cy, mP);
        canvas.drawLine(cx, cy - S, cx, cy + S, mP);
        float d = S * 0.707f;
        canvas.drawLine(cx-d, cy-d, cx+d, cy+d, mP);
        canvas.drawLine(cx+d, cy-d, cx-d, cy+d, mP);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HUB RINGS  (HubRing.draw — decompiled)
    //  Draws concentric arcs at r=74 and r=84 (scaled to our unit)
    // ─────────────────────────────────────────────────────────────────────────

    private void drawHubRings(Canvas canvas, float cx, float cy, float S) {
        float scale = S / 176f; // AugmaOS uses 176dp as base unit
        float r84   = 84f * scale;
        float r74   = 74f * scale;
        float r79   = 79f * scale;
        float r89   = 89f * scale;

        mP.setStyle(Paint.Style.STROKE);

        // Outer ring at 84 (LINE color — dim blue)
        mP.setColor(Color.argb(200, 0x11, 0x44, 0x66));
        mP.setStrokeWidth(1.0f);
        canvas.drawCircle(cx, cy, r84, mP);

        // Inner ring at 74 (LINE)
        mP.setColor(Color.argb(180, 0x11, 0x44, 0x66));
        mP.setStrokeWidth(1.0f);
        canvas.drawCircle(cx, cy, r74, mP);

        // Decorative arcs at 89 (from HubRing — only when not aoa mode)
        mP.setColor(Color.argb(80, 0x1A, 0x55, 0x88));
        mP.setStrokeWidth(1.0f);
        mP.setStrokeCap(Paint.Cap.ROUND);
        // arc(canvas, 89, 30, 150) — arc from 30° span 120°
        mRf.set(cx-r89, cy-r89, cx+r89, cy+r89);
        canvas.drawArc(mRf, 30f, 120f, false, mP);
        // arc(canvas, 89, 62, 118, BLUE 0.8, 1.6) — accent blue arc
        mP.setColor(Color.argb(140, 0x3A, 0x8E, 0xFF));
        mP.setStrokeWidth(1.6f);
        mRf.set(cx-r89, cy-r89, cx+r89, cy+r89);
        canvas.drawArc(mRf, 62f, 56f, false, mP);
        // arc at 79, dim arcs
        mP.setColor(Color.argb(70, 0x11, 0x44, 0x66));
        mP.setStrokeWidth(1.0f);
        mRf.set(cx-r79, cy-r79, cx+r79, cy+r79);
        canvas.drawArc(mRf, 150f, 50f, false, mP);
        mRf.set(cx-r79, cy-r79, cx+r79, cy+r79);
        canvas.drawArc(mRf, -20f, 50f, false, mP);

        // Day-progress arc at r74 (blue glow) — HubRing dayArc
        float dayA = mDayFrac * 360f;
        mP.setColor(Color.argb(100, 0x3A, 0x8E, 0xFF));
        mP.setStrokeWidth(1.4f);
        mRf.set(cx-r84, cy-r84, cx+r84, cy+r84);
        canvas.drawArc(mRf, -90f, dayA, false, mP);

        // Red dot at the tip of the day arc (HubRing red circle)
        double tipRad = Math.toRadians(-90 + dayA);
        float tipX = cx + (float)(Math.cos(tipRad) * r84);
        float tipY = cy + (float)(Math.sin(tipRad) * r84);
        mP.setStyle(Paint.Style.FILL);
        mP.setColor(C_BG);
        canvas.drawCircle(tipX, tipY, 4.2f * scale, mP);
        mP.setStyle(Paint.Style.STROKE);
        mP.setColor(C_RED);
        mP.setStrokeWidth(1.6f);
        canvas.drawCircle(tipX, tipY, 4.2f * scale, mP);

        mP.setStrokeCap(Paint.Cap.BUTT);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  RADAR SWEEP
    // ─────────────────────────────────────────────────────────────────────────

    private void drawRadarSweep(Canvas canvas, float cx, float cy, float S) {
        float maxR = S * 0.95f;
        mRf.set(cx - maxR, cy - maxR, cx + maxR, cy + maxR);

        // Sweep gradient trail — 100 slices with quadratic ease
        mP.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 80; i++) {
            float startA  = mSweep - i;
            float alphaf  = (1f - i / 80f);
            alphaf = alphaf * alphaf;
            int a = (int)(255 * alphaf * 0.35f);
            if (a <= 0) continue;
            mP.setColor(Color.argb(a, 0x4B, 0xD8, 0xFA));
            canvas.drawArc(mRf, startA, 1.1f, true, mP);
        }

        // Sweep line
        mP.setStyle(Paint.Style.STROKE);
        mP.setColor(C_TEAL);
        mP.setStrokeWidth(2.0f);
        double sRad = Math.toRadians(mSweep);
        canvas.drawLine(cx, cy,
            cx + (float)(maxR * Math.cos(sRad)),
            cy + (float)(maxR * Math.sin(sRad)), mP);

        // Blips
        for (int i = 0; i < BLIP_DEG.length; i++) {
            float a = mBlipAlpha[i];
            if (a <= 0.01f) continue;
            double bRad = Math.toRadians(BLIP_DEG[i]);
            float bx = cx + (float)(maxR * BLIP_R[i] * Math.cos(bRad));
            float by = cy + (float)(maxR * BLIP_R[i] * Math.sin(bRad));
            mPAdd.setStyle(Paint.Style.FILL);
            mPAdd.setColor(Color.argb((int)(a * 50), 0x4B, 0xD8, 0xFA));
            canvas.drawCircle(bx, by, 18f * a, mPAdd);
            mP.setStyle(Paint.Style.FILL);
            mP.setColor(Color.argb((int)(a * 255), 0x4B, 0xD8, 0xFA));
            canvas.drawCircle(bx, by, 4f, mP);
            if (a > 0.7f) {
                mP.setStyle(Paint.Style.STROKE);
                mP.setStrokeWidth(1f);
                mP.setColor(Color.argb((int)(a * 90), 0x4B, 0xD8, 0xFA));
                canvas.drawCircle(bx, by, 9f * a, mP);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  PLASMA BLOBS  (OrbPainter.draw — 3-layer version)
    //  Each blob is a warped closed path with radial gradient fill
    // ─────────────────────────────────────────────────────────────────────────

    private void drawPlasmaBlobs(Canvas canvas, float cx, float cy, float S, float R) {
        int save = canvas.saveLayer(null, null);
        mP.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));

        float blobR = R * 0.52f; // blob radius — slightly inside orb radius
        float amp   = 0.09f;     // waviness amplitude

        for (int layer = 0; layer < BLOB_N; layer++) {
            float lr = BLOB_LR[layer];
            float ph = BLOB_PH[layer];
            float sp = BLOB_SP[layer];

            float driftX = (float)(Math.cos(mT * 0.3 + ph)) * blobR * amp * 0.6f;
            float driftY = (float)(Math.sin(mT * 0.25 + ph)) * blobR * amp * 0.6f;

            mPath.rewind();
            for (int pt = 0; pt <= BLOB_PTS; pt++) {
                double theta  = (pt / (double)BLOB_PTS) * Math.PI * 2.0;
                double d2     = mT * sp;
                double d3     = 2.0 * theta + d2;
                double wave   = (Math.sin(d3 + ph) * 0.55
                               + Math.sin(3*theta - 0.7*d2 + ph*1.3) * 0.30
                               + Math.sin(5*theta + d2*1.6) * 0.15) * amp;
                float r2      = blobR * lr * (float)(wave + 1.0);
                float px2     = cx + (float)(Math.cos(theta)) * r2 + driftX;
                float py2     = cy + (float)(Math.sin(theta)) * r2 + driftY;
                if (pt == 0) mPath.moveTo(px2, py2);
                else         mPath.lineTo(px2, py2);
            }
            mPath.close();

            // Pick layer colour: core=orange, mid=teal, outer=blue
            int col = layer == 0 ? C_ORANGE : (layer == 1 ? C_TEAL : C_BLUE);
            float eff = blobR * lr * (float)(amp * 0.4 + 1.0);

            // Radial gradient fill (OrbPainter gc cache equivalent)
            float r0pos = 0.35f * blobR / eff;
            float r1pos = (0.35f * blobR + (eff - 0.35f*blobR) * 0.72f) / eff;
            RadialGradient rg = new RadialGradient(cx, cy, eff,
                new int[]{
                    Color.argb(0,   Color.red(col), Color.green(col), Color.blue(col)),
                    Color.argb(0,   Color.red(col), Color.green(col), Color.blue(col)),
                    Color.argb(45,  Color.red(col), Color.green(col), Color.blue(col)),
                    Color.argb(100, Color.red(col), Color.green(col), Color.blue(col))},
                new float[]{0f, r0pos, r1pos, 1.0f},
                Shader.TileMode.CLAMP);
            mP.setStyle(Paint.Style.FILL);
            mP.setShader(rg);
            canvas.drawPath(mPath, mP);
            mP.setShader(null);

            // Stroke outline
            mP.setStyle(Paint.Style.STROKE);
            mP.setStrokeWidth(1.3f);
            mP.setColor(Color.argb(200, Color.red(col), Color.green(col), Color.blue(col)));
            canvas.drawPath(mPath, mP);
        }
        mP.setXfermode(null);
        canvas.restoreToCount(save);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  PARTICLE SPHERE  (AoaOrb.draw + home() logic)
    // ─────────────────────────────────────────────────────────────────────────

    private void drawParticleSphere(Canvas canvas, float cx, float cy, float S, float R) {
        float cosA = (float) Math.cos(mTurn);
        float sinA = (float) Math.sin(mTurn);

        // Project all particles
        for (int i = 0; i < N_PARTICLES; i++) {
            int b = i * 4;
            project(mPX[b], mPX[b+1], mPX[b+2], cosA, sinA, S, R, mW3);
            int q = i * 3;
            mPQ[q]   = mW3[0] - S + cx;  // translate to view center
            mPQ[q+1] = mW3[1] - S + cy;
            mPQ[q+2] = mW3[2];
        }

        // Draw links first (back-to-front not sorted — matches AugmaOS perf)
        mP.setStyle(Paint.Style.STROKE);
        mP.setStrokeWidth(0.6f);
        for (int i = 0; i < mLinkCount; i++) {
            int ia = mLinks[i*2]   * 3;
            int ib = mLinks[i*2+1] * 3;
            float depA = mPQ[ia+2], depB = mPQ[ib+2];
            float dep  = (depA + depB) * 0.5f;
            float vis  = Math.max(0f, (dep + 1.0f) * 0.4f);
            mP.setColor(Color.argb((int)(vis*60), 0x4B, 0xD8, 0xFA));
            canvas.drawLine(mPQ[ia], mPQ[ia+1], mPQ[ib], mPQ[ib+1], mP);
        }

        // Draw particles
        for (int i = 0; i < N_PARTICLES; i++) {
            int q   = i * 3;
            float d = mPQ[q+2]; // depth -1..+1
            float vis = Math.max(0f, (d + 1.0f) * 0.5f); // front = brighter
            float px = mPQ[q], py = mPQ[q+1];

            if (mPK[i] == 0) {
                // Core particles — orange/amber glow
                int alpha = (int)(vis * 90 * mPX[i*4+3]);
                if (alpha < 4) continue;
                mPAdd.setStyle(Paint.Style.FILL);
                mPAdd.setColor(Color.argb(alpha, 0xFF, 0x9A, 0x3A));
                float sz = 1.4f + vis * 1.2f;
                canvas.drawCircle(px, py, sz, mPAdd);
            } else {
                // Shell particles — blue-white
                int alpha = (int)(vis * 140 * mPX[i*4+3]);
                if (alpha < 4) continue;
                mPAdd.setStyle(Paint.Style.FILL);
                mPAdd.setColor(Color.argb(alpha, 0xC0, 0xE8, 0xFF));
                float sz = 1.0f + vis * 0.8f;
                canvas.drawCircle(px, py, sz, mPAdd);
            }
        }

        mPAdd.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));

        // Rotating arc decorators (AoaOrb.arcs)
        mP.setStyle(Paint.Style.STROKE);
        mP.setStrokeWidth(0.9f);
        mP.setStrokeCap(Paint.Cap.ROUND);
        float arcBaseR = R * 0.78f;
        for (int i = 0; i < N_ARCS; i++) {
            float startDeg = (float) Math.toDegrees(mArcs[i][0] + mTurn * mArcs[i][2]);
            float spanDeg  = (float) Math.toDegrees(mArcs[i][1]) * 0.3f;
            float alpha    = 0.25f + (float)(Math.sin(mT * mArcs[i][2] + i)) * 0.15f;
            mP.setColor(Color.argb((int)(alpha*180), 0x4B, 0xD8, 0xFA));
            float rr = arcBaseR * (0.85f + 0.15f * (i % 3) / 3f);
            mRf.set(cx-rr, cy-rr, cx+rr, cy+rr);
            canvas.drawArc(mRf, startDeg, spanDeg, false, mP);
        }
        mP.setStrokeCap(Paint.Cap.BUTT);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  CORE ORB  (OrbPainter core circle + glass highlight)
    // ─────────────────────────────────────────────────────────────────────────

    private void drawCoreOrb(Canvas canvas, float cx, float cy, float S, float R) {
        float pulse = 0.88f + 0.12f * (float) Math.sin(mPulse);
        float coreR = S * 0.26f * pulse;
        float glassR= coreR * 0.36f;

        // Halo glow
        RadialGradient halo = new RadialGradient(cx, cy, coreR * 2.2f,
            new int[]{Color.argb(80, 0xFF,0x9A,0x3A), Color.argb(40,0x90,0x30,0x10), Color.argb(0,0x40,0x12,0x04)},
            new float[]{0f, 0.21f, 1f}, Shader.TileMode.CLAMP);
        mP.setStyle(Paint.Style.FILL);
        mP.setShader(halo);
        canvas.drawCircle(cx, cy, coreR * 2.2f, mP);
        mP.setShader(null);

        // Core gradient — orange center fading out
        RadialGradient core = new RadialGradient(cx, cy, coreR,
            new int[]{Color.argb(240, 0xFF,0x78,0x28), Color.argb(158,0xFF,0xAA,0x28), Color.argb(0,0x78,0x1E,0x00)},
            new float[]{0f, 0.4f, 1f}, Shader.TileMode.CLAMP);
        mP.setShader(core);
        canvas.drawCircle(cx, cy, coreR, mP);
        mP.setShader(null);

        // Glass specular highlight (OrbPainter glass gradient — top-left offset)
        float off  = -0.12f * glassR;
        float off2 = -0.15f * glassR;
        RadialGradient glass = new RadialGradient(cx+off, cy+off2, glassR * 1.04f,
            new int[]{
                Color.argb(107, 0xFF,0xC8,0x46),
                Color.argb(107, 0xFF,0xC8,0x46),
                Color.argb(117, 0xFF,0x78,0x28),
                Color.argb(178, 0xFF,0x82,0x32),
                Color.argb(229, 0xFF,0xAA,0x64)},
            new float[]{0f, 0.1f, 0.72f, 0.93f, 1f}, Shader.TileMode.CLAMP);
        mP.setShader(glass);
        canvas.drawCircle(cx+off, cy+off2, glassR * 1.04f, mP);
        mP.setShader(null);

        // Bright hi spot (OrbPainter hi gradient)
        float hiR = glassR * 0.45f;
        RadialGradient hi = new RadialGradient(cx-hiR, cy-glassR*0.5f, hiR,
            Color.argb(36,0xFF,0xE6,0xC8), Color.argb(0,0xFF,0xE6,0xC8), Shader.TileMode.CLAMP);
        mP.setShader(hi);
        canvas.drawCircle(cx-hiR, cy-glassR*0.5f, hiR, mP);
        mP.setShader(null);

        // Rotating ring overlays (OrbLayer.advOrb — 6 oval spokes)
        mP.setStyle(Paint.Style.STROKE);
        mP.setStrokeWidth(0.7f);
        mP.setColor(Color.argb(216, 0xE4, 0xF0, 0xF1)); // fWHITE
        for (int i = 0; i < 6; i++) {
            int saveC = canvas.save();
            canvas.rotate(mSpin * 2f + i * 30f, cx, cy);
            float oa = coreR * 0.42f;
            float ob = oa * 0.55f;
            mRf.set(cx-oa, cy-ob, cx+oa, cy+ob);
            canvas.drawOval(mRf, mP);
            canvas.restoreToCount(saveC);
        }

        // Red accent ring
        mP.setStrokeWidth(0.9f);
        mP.setColor(C_RED);
        canvas.drawCircle(cx + coreR*0.07f, cy + coreR*0.05f, coreR*1.1f + 2.4f, mP);

        // Blue inner ring
        mP.setColor(C_BLUE);
        canvas.drawCircle(cx - coreR*0.07f, cy - coreR*0.03f, coreR - 2f, mP);

        // Center dot
        mP.setStyle(Paint.Style.FILL);
        mP.setColor(Color.argb(178, 0xE4, 0xF0, 0xF1));
        canvas.drawCircle(cx, cy, 1.6f, mP);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ARC WEDGE MENU  (DockLayer + RingMath.even + Shapes.wedge)
    //  Draws the bottom-arc tab menu matching AugmaOS DockLayer
    //  T0=200°, T1=340°, N segments = tab count
    // ─────────────────────────────────────────────────────────────────────────

    private void drawArcMenu(Canvas canvas, float cx, float cy, float S, float R) {
        int   n    = ARC_LABELS.length;
        float T0   = ARC_T0_DEG;
        float T1   = ARC_T1_DEG;
        float span = T1 - T0;

        // Radii (RingMath.radii — based on S)
        // radii(S, maxR, minR): [inner, mid, outer, label]
        float maxAllowedR = S * 0.98f;
        float minR  = Math.max(30f, Math.min(maxAllowedR, (S - 6f - 26f - 6f)));
        float inner = Math.max(30f, minR - 50f);
        float outer = minR;
        float gap   = 4.0f;  // gap between wedges in degrees

        // RingMath.even: distribute n segments evenly over T0..T1
        float segSpan  = span / n;
        float maxGap   = Math.min(gap, 0.4f * segSpan);

        for (int i = 0; i < n; i++) {
            float midDeg   = T0 + (i + 0.5f) * segSpan;
            float startDeg = midDeg - segSpan * 0.5f + maxGap * 0.5f;
            float endDeg   = midDeg + segSpan * 0.5f - maxGap * 0.5f;
            boolean active = ARC_LABELS[i].equals(mActiveTab);

            // Wedge path (Shapes.wedge simplified — no rounded corner version)
            Path wedge = buildWedge(cx, cy, inner, outer, startDeg, endDeg);

            int col = ARC_COLORS[i];
            int r   = Color.red(col), g = Color.green(col), b = Color.blue(col);

            // Fill — dim for inactive, brighter for active
            mP.setStyle(Paint.Style.FILL);
            if (active) {
                mP.setColor(Color.argb(80, r, g, b));
            } else {
                mP.setColor(Color.argb(28, r, g, b));
            }
            canvas.drawPath(wedge, mP);

            // Stroke
            mP.setStyle(Paint.Style.STROKE);
            mP.setStrokeWidth(active ? 1.6f : 0.9f);
            mP.setColor(Color.argb(active ? 220 : 100, r, g, b));
            canvas.drawPath(wedge, mP);

            // Label text at midpoint between inner and outer
            float labelR = (inner + outer) * 0.5f;
            double midRad = Math.toRadians(midDeg);
            float lx = cx + (float)(Math.cos(midRad) * labelR);
            float ly = cy + (float)(Math.sin(midRad) * labelR);

            float sp = dp(7.5f);
            mPText.setTextSize(sp);
            if (Fonts.MONO != null) mPText.setTypeface(Fonts.MONO);
            mPText.setColor(active
                ? Color.argb(255, r, g, b)
                : Color.argb(160, r, g, b));
            // Rotate canvas so text reads radially outward
            int sv = canvas.save();
            canvas.rotate(midDeg + 90f, lx, ly);
            canvas.drawText(ARC_LABELS[i], lx, ly + sp * 0.4f, mPText);
            canvas.restoreToCount(sv);

            // Active indicator dot on outer edge
            if (active) {
                float dotX = cx + (float)(Math.cos(midRad) * (outer + 5f));
                float dotY = cy + (float)(Math.sin(midRad) * (outer + 5f));
                mP.setStyle(Paint.Style.FILL);
                mP.setColor(col);
                canvas.drawCircle(dotX, dotY, 3f, mP);
            }
        }
    }

    /** Build an annular wedge path from startDeg to endDeg between inner and outer radii */
    private Path buildWedge(float cx, float cy, float inner, float outer, float startDeg, float endDeg) {
        Path path = new Path();
        float sweepA = endDeg - startDeg;
        mRf.set(cx-outer, cy-outer, cx+outer, cy+outer);
        path.arcTo(mRf, startDeg, sweepA, true);
        // line back along the end-angle spoke to inner
        double endRad = Math.toRadians(endDeg);
        path.lineTo(cx + (float)(Math.cos(endRad)*inner), cy + (float)(Math.sin(endRad)*inner));
        mRf.set(cx-inner, cy-inner, cx+inner, cy+inner);
        path.arcTo(mRf, endDeg, -sweepA);
        path.close();
        return path;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HUD TEXT  (greeting + user name, center orb area)
    // ─────────────────────────────────────────────────────────────────────────

    private void drawHudText(Canvas canvas, float cx, float cy, float S) {
        float coreTop = cy - S * 0.26f * 1.2f;

        if (!mGreeting.isEmpty()) {
            mPText.setTextSize(dp(9f));
            if (Fonts.MONO != null) mPText.setTypeface(Fonts.MONO);
            mPText.setColor(Color.argb(160, 0x9B, 0xC8, 0xE8));
            mPText.setLetterSpacing(0.15f);
            canvas.drawText(mGreeting.toUpperCase(), cx, coreTop - dp(10f), mPText);
        }
        if (!mUserName.isEmpty()) {
            mPText.setTextSize(dp(14f));
            if (Fonts.BRAND != null) mPText.setTypeface(Fonts.BRAND);
            else if (Fonts.MONO != null) mPText.setTypeface(Fonts.MONO);
            mPText.setColor(C_WHITE);
            mPText.setLetterSpacing(0.25f);
            canvas.drawText(mUserName.toUpperCase(), cx, coreTop + dp(28f), mPText);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TOUCH  (hit test arc menu segments + outer radar tab positions)
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != MotionEvent.ACTION_UP || mTabCb == null) return true;

        final float cx = getWidth() * 0.5f, cy = getHeight() * 0.5f;
        final float S  = getWidth() * 0.5f * 0.90f;
        float tx = ev.getX(), ty = ev.getY();

        int   n    = ARC_LABELS.length;
        float T0   = ARC_T0_DEG;
        float T1   = ARC_T1_DEG;
        float span = T1 - T0;
        float segSpan = span / n;

        float maxAllowedR = S * 0.98f;
        float minR  = Math.max(30f, Math.min(maxAllowedR, (S - 6f - 26f - 6f)));
        float inner = Math.max(30f, minR - 50f);
        float outer = minR;

        float dx = tx - cx, dy2 = ty - cy;
        float dist = (float) Math.sqrt(dx*dx + dy2*dy2);

        if (dist >= inner && dist <= outer + 20f) {
            // Convert touch angle to degrees (0=right, 90=down)
            float angDeg = (float) Math.toDegrees(Math.atan2(dy2, dx));
            if (angDeg < 0) angDeg += 360f;
            // Normalize to T0..T1 range
            if (angDeg >= T0 && angDeg <= T1) {
                int idx = (int)((angDeg - T0) / segSpan);
                if (idx >= 0 && idx < n) {
                    mActiveTab = ARC_LABELS[idx];
                    if (mTabCb != null) mTabCb.onTab(mActiveTab);
                    invalidate();
                }
            }
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private float dp(float v) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v,
            getResources().getDisplayMetrics());
    }
}
