package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import com.jarvis.assist.aosui.core.Converge;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.FrameClock;
import com.jarvis.assist.aosui.core.OrbPainter;
import com.jarvis.assist.aosui.core.Palette;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.PerfModel;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.core.UnitsMath;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class OrbLayer extends View {
    static final float NAME_W = 80.0f;
    final int[] cols;
    final HomeCtx h;
    long last;
    final FrameClock.Handle loop;
    Txt nameT;
    AoaOrb net;
    final OrbPainter orb;
    final RectF ov;
    final Paint p;
    float spin;
    float t;
    float turn;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OrbLayer(Context context, final HomeCtx homeCtx) {
        super(context);
        this.p = new Paint(1);
        this.orb = new OrbPainter();
        this.t = (float) (Math.random() * 9.0d);
        this.cols = new int[5];
        this.ov = new RectF();
        this.h = homeCtx;
        this.loop = FrameClock.I.add(new FrameClock.Sub() { // from class: com.jarvis.assist.aosui.home.OrbLayer$$ExternalSyntheticLambda0
            @Override // com.jarvis.assist.aosui.core.FrameClock.Sub
            public final boolean frame(long j, float f) {
                return OrbLayer.this.m293lambda$new$0$comaugmaosohomeOrbLayer(homeCtx, j, f);
            }
        }, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-augma-oso-home-OrbLayer, reason: not valid java name */
    public /* synthetic */ boolean m293lambda$new$0$comaugmaosohomeOrbLayer(HomeCtx homeCtx, long j, float f) {
        PerfModel.Knobs k = Perf.k();
        boolean z = k.orb;
        float min = Math.min(0.05f, f / 1000.0f);
        if (z) {
            this.t += 0.35f * min;
        }
        if (k.motion) {
            this.spin = (this.spin + (12.0f * min)) % 360.0f;
        }
        if (homeCtx.aoa && z && k.motion) {
            this.turn = (this.turn + (min * 0.21f)) % 6.2832f;
        }
        invalidate();
        return z || k.motion;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepare() {
        Theme theme = this.h.th;
        if (theme.light) {
            this.cols[0] = -15394272;
            this.cols[1] = Theme.mix(theme.ACCENT_RAW, -16777216, 0.4f);
            this.cols[2] = theme.RED;
            this.cols[3] = -13935230;
            this.cols[4] = -10853264;
        } else {
            this.cols[0] = theme.INK_RAW;
            this.cols[1] = theme.ACCENT_RAW;
            this.cols[2] = theme.RED;
            this.cols[3] = Theme.mix(theme.ACCENT_RAW, -16777216, 0.5f);
            this.cols[4] = theme.INK_RAW;
        }
        this.orb.colors(this.cols, theme.light, theme.BG);
        String str = this.h.d.name;
        float L = this.h.L(11.0f, 9.0f);
        Txt txt = new Txt(Fonts.RAJ_600, L, 0.42f * L);
        if (txt.w(str) > NAME_W) {
            txt = new Txt(Fonts.RAJ_600, L, L * 0.12f);
        }
        float w = txt.w(str);
        if (w > NAME_W) {
            float max = Math.max(7.0f, L * UnitsMath.fitScale(w, NAME_W, 0.0f));
            txt = new Txt(Fonts.RAJ_600, max, 0.12f * max);
            float w2 = txt.w(str);
            if (w2 > NAME_W) {
                txt.p.setTextScaleX(NAME_W / w2);
            }
        }
        this.nameT = txt.align(Paint.Align.CENTER);
        this.loop.kick();
        invalidate();
    }

    void advOrb(Canvas canvas, Theme theme) {
        float f = (!this.h.g.adv() || this.h.g.orbR <= 0.0f) ? 44.0f : this.h.g.orbR;
        this.p.setShader(new RadialGradient(70.0f, 70.0f, f, new int[]{-15723496, -16447734}, (float[]) null, Shader.TileMode.CLAMP));
        this.p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(70.0f, 70.0f, f, this.p);
        this.p.setShader(null);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(1.0f);
        this.p.setColor(theme.fWHITE);
        canvas.drawCircle(70.0f, 70.0f, f, this.p);
        this.p.setColor(theme.fRED_HI);
        canvas.drawCircle(71.2f, 70.8f, 2.4f + f, this.p);
        this.p.setColor(theme.fBLUE);
        canvas.drawCircle(68.8f, 69.4f, f - 2.0f, this.p);
        this.p.setStrokeWidth(0.7f);
        this.p.setColor(Theme.a(-1774607, 0.85f));
        float f2 = f * 0.42f;
        for (int i = 0; i < 6; i++) {
            int save = canvas.save();
            canvas.rotate((this.spin * 2.0f) + (i * 30), 70.0f, 70.0f);
            float f3 = 0.55f * f2;
            this.ov.set(70.0f - f2, 70.0f - f3, 70.0f + f2, f3 + 70.0f);
            canvas.drawOval(this.ov, this.p);
            canvas.restoreToCount(save);
        }
        this.p.setStyle(Paint.Style.FILL);
        this.p.setColor(-1774607);
        canvas.drawCircle(70.0f, 70.0f, 1.6f, this.p);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Geo geo = this.h.g;
        Theme theme = this.h.th;
        if (this.nameT == null) {
            return;
        }
        PerfModel.Knobs k = Perf.k();
        int save = canvas.save();
        canvas.scale(geo.S, geo.S);
        if (this.h.aoa) {
            if (this.net == null) {
                this.net = new AoaOrb(false);
            }
            float orbHalf = this.h.orbHalf();
            this.net.draw(canvas, orbHalf, orbHalf, orbHalf / 176.0f, this.turn);
            canvas.restoreToCount(save);
            return;
        }
        if (this.h.adv) {
            advOrb(canvas, theme);
            canvas.restoreToCount(save);
            return;
        }
        this.orb.draw(canvas, this.p, 70.0f, 70.0f, 44.8f, this.t, (((float) Math.sin((System.nanoTime() / 1000000.0d) / 1400.0d)) * 0.025f) + 0.09f, k.orbLayers, k.orbPts, k.orbGlow, 1.2727273f, 140.0f / (theme.light ? 40.0f : 26.0f));
        Converge.draw(canvas, this.p, 70.0f, 64.0f, 40.0f, this.spin, theme.light ? Theme.mix(theme.ACCENT_RAW, -16777216, 0.4f) : theme.ACCENT_RAW, theme.RED, theme.light ? Palette.LIGHT_INK : -1774607);
        this.nameT.draw(canvas, this.h.d.name, 70.0f, ((this.nameT.size - this.nameT.line()) / 2.0f) + 90.0f + this.nameT.asc, theme.INK);
        canvas.restoreToCount(save);
    }
}
