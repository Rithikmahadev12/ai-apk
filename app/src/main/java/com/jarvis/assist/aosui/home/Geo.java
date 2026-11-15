package com.jarvis.assist.aosui.home;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import com.jarvis.assist.aosui.core.AoaGeo;
import com.jarvis.assist.aosui.core.HomeTheme;
import com.jarvis.assist.aosui.core.RingMath;
import com.jarvis.assist.aosui.core.Shapes;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.UnitsMath;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class Geo {
    static final float PH_MAX = 190.0f;
    static final float TB = 30.0f;
    float F;
    float RA;
    float RI;
    float RO;
    float aBtnDx;
    float aBtnY;
    final RectF aColL;
    final RectF aColR;
    final RectF aCornL;
    final RectF aCornR;
    float aCx;
    float aCy;
    float[] aDIn;
    float[] aDOut;
    float aDayY;
    float aDialLx;
    float aDialRx;
    float aDialY;
    final Path[] aDk;
    final RectF[] aDkR;
    float aDockDialLx;
    float aDockDialRx;
    float aDockDialY;
    final RectF aDockRow;
    float[] aIn;
    float aIndY;
    final RectF aMid;
    float[] aMidR;
    final RectF[] aMode;
    float[] aOut;
    float aPtt;
    float aQuickR;
    float aS;
    Path aSearch;
    final RectF[] aTile;
    float acy;
    boolean aoa;
    Path apps;
    Region appsR;
    float arcHalf;
    float barY;
    float bx;
    float by;
    float chipY;
    int cutLpx;
    float cutLx;
    int cutRpx;
    float cutRx;
    float dS;
    float dTop;
    float dX;
    float dY;
    float dialR;
    float dialXL;
    float dialXR;
    float dialY;
    float dkHalf;
    boolean full;
    float gH;
    final RectF gRect;
    float gTop;
    float hrY;
    final float[][] inA;
    final Path[] inP;
    boolean land;
    float msX;
    float msY;
    int nIn;
    int nTile;
    int nd;
    int nq;
    int nq2;
    int nr;
    int nw;
    float orbR;
    float outerR;
    float ox;
    float oy;
    float pLeft;
    float pTop;
    float pb;
    float pt;
    float pw;
    float q2In;
    String[] q2Labels;
    float q2Out;
    float qsIn;
    float qsOut;
    float qsX;
    float qsY;
    float railR;
    float ringX;
    float ringY;
    Region scrubR;
    float spin;
    float spinQ;
    float statusY;
    float sy;
    float tileY;
    float uu;
    float wx;
    final RectF youChip;
    static final String[] MODES = {"centre", "wheel", "arc"};
    static final String[] AOA_MODES = {"bed", "silent", "life", "work", "focus", "all"};
    float k = 1.0f;
    float S = 1.0f;
    float LW = 324.0f;
    float LH = 740.0f;
    float cx = 162.0f;
    float cy = 242.0f;
    float ph = 146.0f;
    final Path[] qs = new Path[8];
    final Path[] wh = new Path[8];
    final Path[] dk = new Path[5];
    final Path[] qs2 = new Path[7];
    final Region[] qsR = new Region[8];
    final Region[] whR = new Region[8];
    final Region[] dkR = new Region[5];
    final float[][] qsA = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 8, 3);
    final float[][] whA = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 8, 3);
    final float[][] dkA = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 5, 3);
    final float[][] q2A = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 7, 3);
    boolean orb = true;
    String mode = "portrait";
    float RIl = 92.0f;
    float ROl = 150.0f;
    final RectF[] qsDot = new RectF[8];
    final RectF[] tab = new RectF[3];
    final Region[] tabR = new Region[3];
    final RectF allApps = new RectF();
    float ringW = 150.0f;
    float chipH = 22.0f;
    float tileH = 40.0f;
    final RectF[] tile = new RectF[8];
    final ArrayList<Region> exR = new ArrayList<>();
    final ArrayList<String> exA = new ArrayList<>();
    final ArrayList<String> exL = new ArrayList<>();
    final RectF[] chip = new RectF[5];
    final RectF[] corner = new RectF[4];

    /* JADX INFO: Access modifiers changed from: package-private */
    public Geo() {
        for (int i = 0; i < 8; i++) {
            this.tile[i] = new RectF();
        }
        for (int i2 = 0; i2 < 5; i2++) {
            this.chip[i2] = new RectF();
        }
        for (int i3 = 0; i3 < 4; i3++) {
            this.corner[i3] = new RectF();
        }
        this.F = 1.0f;
        this.cutLpx = -1;
        this.cutRpx = -1;
        this.cutLx = -1.0f;
        this.cutRx = -1.0f;
        this.gRect = new RectF();
        this.youChip = new RectF();
        this.q2Labels = new String[0];
        this.aS = 1.0f;
        this.dS = 1.0f;
        this.inA = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 3, 3);
        this.inP = new Path[3];
        this.aTile = new RectF[9];
        this.aMode = new RectF[6];
        this.aCornL = new RectF();
        this.aCornR = new RectF();
        this.aMid = new RectF();
        this.aColL = new RectF();
        this.aColR = new RectF();
        this.aDockRow = new RectF();
        this.aOut = new float[2];
        this.aMidR = new float[2];
        this.aIn = new float[2];
        this.aDOut = new float[2];
        this.aDIn = new float[2];
        this.aDk = new Path[5];
        this.aDkR = new RectF[5];
        for (int i4 = 0; i4 < 9; i4++) {
            this.aTile[i4] = new RectF();
        }
        for (int i5 = 0; i5 < 6; i5++) {
            this.aMode[i5] = new RectF();
        }
        for (int i6 = 0; i6 < 5; i6++) {
            this.aDkR[i6] = new RectF();
        }
        for (int i7 = 0; i7 < 8; i7++) {
            this.qsDot[i7] = new RectF();
        }
        for (int i8 = 0; i8 < 3; i8++) {
            this.tab[i8] = new RectF();
        }
    }

    void layoutAdv(float f, float f2, float f3, int i, int i2, int i3, int i4, int i5, int i6, int i7, String[] strArr, String[] strArr2) {
        layoutAdv(f, f2, f3, i, i2, i3, i4, i5, i6, i7, strArr, strArr2, this.q2Labels);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void layoutAdv(float f, float f2, float f3, int i, int i2, int i3, int i4, int i5, int i6, int i7, String[] strArr, String[] strArr2, String[] strArr3) {
        this.q2Labels = strArr3;
        this.land = false;
        this.full = false;
        char c = 1;
        this.orb = true;
        this.mode = "adv";
        for (int i8 = 0; i8 < 3; i8++) {
            this.tabR[i8] = null;
        }
        float f4 = i;
        float[] stage = UnitsMath.stage(((f - f4) - i3) / f3, (f2 - i4) / f3, 324.0f, 740.0f);
        this.k = stage[0];
        this.LW = stage[1];
        this.LH = stage[2];
        this.S = this.k * f3;
        this.ox = f4;
        this.oy = 0.0f;
        this.cx = this.LW / 2.0f;
        this.bx = this.cx;
        this.by = this.LH;
        Region region = new Region(-10000, -10000, 10000, 10000);
        this.exR.clear();
        this.exA.clear();
        this.exL.clear();
        float f5 = this.LW / 676.0f;
        this.uu = f5;
        float f6 = 44.0f / this.k;
        float f7 = i2 / this.S;
        this.cutLx = this.cutLpx < 0 ? -1.0f : (this.cutLpx - this.ox) / this.S;
        this.cutRx = this.cutRpx >= 0 ? (this.cutRpx - this.ox) / this.S : -1.0f;
        this.statusY = f7 > 16.0f ? f7 / 2.0f : 13.0f;
        this.gTop = f7 > 16.0f ? f7 + 4.0f : 24.0f;
        this.gH = Math.max(150.0f * f5, 46.0f);
        float f8 = this.gTop + this.gH + 6.0f;
        this.ph = Math.max(118.0f, this.LH * 0.19f);
        float f9 = this.LW / 800.0f;
        float f10 = 50.0f * f5;
        float max = Math.max(0.5f, Math.min(1.0f, ((((((((this.LH - f8) - this.ph) - f10) - ((176.0f * f5) + (f5 * 9.0f))) - 10.0f) - 10.0f) - 10.0f) - 24.0f) / (858.0f * f9)));
        this.F = f9 * max;
        this.sy = max;
        float f11 = f5 * max;
        this.cy = f8 + (this.F * 354.0f);
        this.acy = this.cy;
        this.arcHalf = 90.0f;
        this.railR = this.F * 330.0f;
        this.qsOut = this.F * 330.0f;
        this.qsIn = this.F * 250.0f;
        this.q2Out = this.F * 240.0f;
        this.q2In = this.F * 175.0f;
        this.outerR = this.F * 92.0f;
        this.orbR = this.F * 70.0f;
        float f12 = f5 * 20.0f;
        this.gRect.set(f12, this.gTop, this.LW - f12, this.gTop + this.gH);
        Path path = new Path();
        path.addRect(this.gRect, Path.Direction.CW);
        this.exR.add(region(path, region));
        this.exA.add("brief");
        this.exL.add(Strings.t("db.shortcut"));
        this.nq = Math.min(8, i5);
        float f13 = 180.0f;
        float max2 = 180.0f / Math.max(1, this.nq);
        int i9 = 0;
        while (i9 < this.nq) {
            float f14 = (i9 * max2) + f13 + 1.0f;
            float f15 = (f14 + max2) - 2.0f;
            this.qsA[i9][0] = f14;
            this.qsA[i9][c] = f15;
            this.qsA[i9][2] = (f14 + f15) / 2.0f;
            this.qs[i9] = Shapes.wedge(this.cx, this.cy, this.qsIn, this.qsOut, f14, f15, 2.0f);
            this.qsR[i9] = region(this.qs[i9], region);
            this.qsDot[i9].setEmpty();
            i9++;
            f13 = f13;
            c = 1;
        }
        float f16 = f13;
        this.nq2 = Math.min(7, strArr3.length);
        float max3 = f16 / Math.max(1, this.nq2);
        for (int i10 = 0; i10 < this.nq2; i10++) {
            float f17 = (i10 * max3) + f16 + 1.0f;
            float f18 = (f17 + max3) - 2.0f;
            this.q2A[i10][0] = f17;
            this.q2A[i10][1] = f18;
            this.q2A[i10][2] = (f17 + f18) / 2.0f;
            this.qs2[i10] = Shapes.wedge(this.cx, this.cy, this.q2In, this.q2Out, f17, f18, 2.0f);
            this.exR.add(region(this.qs2[i10], region));
            this.exA.add("k" + i10);
            this.exL.add(strArr3[i10]);
        }
        float f19 = this.LW * 0.25f;
        this.corner[0].set(12.0f, (this.cy - this.qsOut) - (this.F * 16.0f), f19 + 12.0f, ((this.cy - this.qsOut) - (this.F * 16.0f)) + Math.max(34.0f, this.qsOut * 0.3f));
        this.corner[1].set((this.LW - 12.0f) - f19, this.corner[0].top, this.LW - 12.0f, this.corner[0].bottom);
        this.dialR = this.F * 40.0f;
        this.dialY = this.cy + (this.F * 120.0f);
        this.dialXL = this.dialR + f12;
        this.dialXR = (this.LW - f12) - this.dialR;
        this.chipY = this.dialY + this.dialR + 12.0f;
        this.chipH = f10 * Math.max(0.8f, max);
        this.pLeft = f12;
        this.pw = this.LW - (40.0f * f5);
        this.pTop = this.chipY + this.chipH + 10.0f;
        this.RO = this.F * 330.0f;
        this.RI = this.RO - Math.max(this.F * 80.0f, f6);
        this.RA = this.RI - 8.0f;
        float f20 = 88.0f * f11;
        float f21 = f11 * 9.0f;
        this.ph += Math.max(0.0f, ((this.LH - this.RO) - 46.0f) - ((((this.pTop + this.ph) + 10.0f) + (f20 * 2.0f)) + f21));
        this.tileY = this.pTop + this.ph + 10.0f;
        this.tileH = f20;
        this.barY = -1000.0f;
        this.hrY = -1000.0f;
        float f22 = f5 * 10.0f;
        float f23 = 632.0f * f5;
        float f24 = (f23 - (f22 * 4.0f)) / 5.0f;
        int i11 = 0;
        while (i11 < 5) {
            float f25 = (i11 * (f24 + f22)) + f12;
            float f26 = f25 + f24;
            float f27 = f23;
            this.chip[i11].set(f25, this.chipY, f26, this.chipY + this.chipH);
            Path path2 = new Path();
            float max4 = Math.max(0.0f, (f6 - this.chipH) / 2.0f);
            path2.addRect(f25, this.chipY - max4, f26, this.chipY + this.chipH + max4, Path.Direction.CW);
            this.exR.add(region(path2, region));
            this.exA.add("x:mode:" + AdvWidgets.MODES[i11]);
            this.exL.add(Strings.t("adv.mode." + AdvWidgets.MODES[i11]));
            i11++;
            f23 = f27;
        }
        this.nw = Math.min(8, i6);
        float f28 = f5 * 11.0f;
        float f29 = (f23 - (3.0f * f28)) / 4.0f;
        for (int i12 = 0; i12 < 8; i12++) {
            float f30 = ((i12 % 4) * (f29 + f28)) + f12;
            float f31 = this.tileY + ((i12 / 4) * (this.tileH + f21));
            float f32 = f30 + f29;
            this.tile[i12].set(f30, f31, f32, this.tileH + f31);
            Path path3 = new Path();
            float max5 = Math.max(0.0f, (f6 - this.tileH) / 2.0f);
            path3.addRect(f30, f31 - max5, f32, f31 + this.tileH + max5, Path.Direction.CW);
            if (i12 < this.nw) {
                float[] fArr = this.whA[i12];
                float[] fArr2 = this.whA[i12];
                this.whA[i12][2] = 0.0f;
                fArr2[1] = 0.0f;
                fArr[0] = 0.0f;
                this.wh[i12] = path3;
                this.whR[i12] = region(path3, region);
            } else if (i12 - this.nw < strArr.length) {
                this.exR.add(region(path3, region));
                this.exA.add("x:tile:" + strArr[i12 - this.nw]);
                this.exL.add(strArr2[i12 - this.nw]);
            }
        }
        this.nd = Math.min(5, i7);
        float max6 = f16 / Math.max(1, this.nd);
        this.dkHalf = 90.0f;
        for (int i13 = 0; i13 < this.nd; i13++) {
            float f33 = (i13 * max6) + f16 + 1.0f;
            float f34 = (f33 + max6) - 2.0f;
            this.dkA[i13][0] = f33;
            this.dkA[i13][1] = f34;
            this.dkA[i13][2] = (f33 + f34) / 2.0f;
            this.dk[i13] = Shapes.wedge(this.bx, this.by, this.RI, this.RO, f33, f34, 4.0f);
            this.dkR[i13] = region(this.dk[i13], region);
        }
        this.apps = new Path();
        this.apps.moveTo(this.bx - this.RA, this.by);
        this.apps.arcTo(new RectF(this.bx - this.RA, this.by - this.RA, this.bx + this.RA, this.by + this.RA), f16, f16, false);
        this.apps.close();
        this.appsR = region(this.apps, region);
        this.scrubR = region(Shapes.wedge(this.bx, this.by, this.RO + 2.0f, this.RO + 48.0f, 196.0f, 344.0f, 0.0f), region);
        float f35 = this.LW * 0.22f;
        float max7 = Math.max(34.0f, this.RO * 0.28f) / 2.0f;
        this.corner[2].set(12.0f, (this.by - (this.RO * 0.78f)) - max7, f35 + 12.0f, (this.by - (this.RO * 0.78f)) + max7);
        this.corner[3].set((this.LW - 12.0f) - f35, this.corner[2].top, this.LW - 12.0f, this.corner[2].bottom);
        int i14 = 0;
        while (i14 < 4) {
            Path path4 = new Path();
            path4.addRect(this.corner[i14], Path.Direction.CW);
            this.exR.add(region(path4, region));
            this.exA.add("x:corner:" + i14);
            i14++;
            this.exL.add("CORNER " + i14);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean adv() {
        return "adv".equals(this.mode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean aoa() {
        return HomeTheme.AOA_V.equals(this.mode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void layoutAoa(float f, float f2, float f3, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, String[] strArr, String[] strArr2, String[] strArr3, String[] strArr4) {
        int i8;
        float f4;
        float f5;
        int i9;
        int i10;
        float f6;
        int i11;
        int i12;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        this.land = z;
        this.full = false;
        this.orb = true;
        this.mode = HomeTheme.AOA_V;
        this.aoa = true;
        for (int i13 = 0; i13 < 3; i13++) {
            this.tabR[i13] = null;
        }
        Region region = new Region(-10000, -10000, 10000, 10000);
        this.exR.clear();
        this.exA.clear();
        this.exL.clear();
        float f16 = i;
        float f17 = ((f - f16) - i3) / f3;
        float f18 = ((f2 - i4) - (z ? i2 : 0)) / f3;
        if (z) {
            i8 = 1;
            this.k = Math.min(f17 / 800.0f, f18 / 360.0f);
            this.LW = f17 / this.k;
        } else {
            i8 = 1;
            this.k = f17 / 360.0f;
            this.LW = 360.0f;
        }
        this.LH = f18 / this.k;
        this.S = this.k * f3;
        this.ox = f16;
        this.oy = z ? i2 : 0.0f;
        float f19 = 44.0f / this.k;
        float f20 = z ? 0.0f : i2 / this.S;
        this.statusY = f20 > 16.0f ? f20 / 2.0f : 12.0f;
        if (this.cutLpx < 0) {
            f4 = 16.0f;
            f5 = -1.0f;
        } else {
            f4 = 16.0f;
            f5 = (this.cutLpx - this.ox) / this.S;
        }
        this.cutLx = f5;
        this.cutRx = this.cutRpx >= 0 ? (this.cutRpx - this.ox) / this.S : -1.0f;
        int i14 = 4;
        if (!z) {
            i9 = 0;
            i12 = 2;
            float[] portrait = AoaGeo.portrait(this.LW, this.LH, f20);
            this.aS = portrait[0];
            float f21 = portrait[i8];
            float f22 = portrait[2];
            float f23 = this.aS;
            i10 = 3;
            this.aCx = f21 + (180.0f * f23);
            this.aCy = (241.0f * f23) + f22;
            float f24 = f23 * 360.0f;
            i11 = 6;
            float f25 = f21 + (14.0f * f23);
            float f26 = f22 + (82.0f * f23);
            this.aCornL.set(f25, f22 + (f23 * 50.0f), f21 + (120.0f * f23), f26);
            float f27 = (48.0f * f23) + f22;
            float f28 = f21 + (346.0f * f23);
            this.aCornR.set((240.0f * f23) + f21, f27, f28, f26);
            this.aDialY = f22 + (291.0f * f23);
            this.aDialLx = (29.0f * f23) + f21;
            this.aDialRx = (331.0f * f23) + f21;
            this.aDayY = f22 + (318.0f * f23);
            this.aMid.set(f25, f22 + (322.0f * f23), f28, f22 + (356.0f * f23));
            f8 = f22 + (372.0f * f23);
            f9 = 87.0f * f23;
            this.aIndY = f22 + (479.0f * f23);
            f10 = f22 + (501.0f * f23);
            f11 = 25.0f * f23;
            f12 = f22 + (545.0f * f23);
            float f29 = f23 * 119.0f;
            f6 = f19;
            float f30 = f12 + f29;
            this.aColL.set(f25, f12, f21 + (52.0f * f23), f30);
            this.aColR.set(f21 + (300.0f * f23), f12, f28, f30);
            int i15 = 0;
            while (i15 < 9) {
                int i16 = i15;
                float f31 = f12;
                float[] tile = AoaGeo.tile(i16, f21 + (63.0f * f23), f31, f23 * 234.0f, f29, f23 * 4.5f);
                this.aTile[i16].set(tile[0], tile[i8], tile[2], tile[3]);
                i15 = i16 + 1;
                f12 = f31;
            }
            this.dTop = f22 + (696.0f * f23);
            this.dX = this.aCx;
            this.dY = f22 + (653.0f * f23);
            this.dS = f23;
            this.aDockDialY = f22 + (708.0f * f23);
            this.aDockDialLx = f21 + (22.0f * f23);
            this.aDockDialRx = f21 + (f23 * 336.0f);
            this.aDockRow.setEmpty();
            f14 = f21;
            f13 = f29;
            f7 = f24;
        } else {
            i9 = 0;
            i10 = 3;
            f6 = f19;
            i11 = 6;
            i12 = 2;
            float[] land = AoaGeo.land(this.LW, this.LH);
            this.aS = land[i8];
            this.aCx = land[2] + (this.aS * 180.0f);
            this.aCy = land[3] + (this.aS * 241.0f);
            f7 = land[0] - 24.0f;
            f8 = land[4];
            f9 = land[5];
            this.aIndY = f8 + f9 + 4.0f + 6.0f;
            f10 = land[6];
            float f32 = land[7];
            float f33 = land[8];
            float f34 = (0.5f * f7) + 12.0f;
            this.aCornL.set(12.0f, 6.0f, f34, 40.0f);
            float f35 = f7 + 12.0f;
            this.aCornR.set(f34, 6.0f, f35, 40.0f);
            this.aDialY = this.aCy + (this.aS * 50.0f);
            this.aDialLx = land[2] + (this.aS * 29.0f);
            this.aDialRx = land[2] + (this.aS * 331.0f);
            this.aDayY = this.aCy + (this.aS * 77.0f);
            this.aMid.setEmpty();
            float f36 = f32 + f33;
            this.aColL.set(12.0f, f32, 64.0f, f36);
            this.aColR.set(f35 - 52.0f, f32, f35, f36);
            int i17 = 0;
            while (i17 < 9) {
                float f37 = f32;
                int i18 = i17;
                float[] tile2 = AoaGeo.tile(i18, 72.0f, f37, (f7 - 104.0f) - f4, f33, 4.0f);
                this.aTile[i18].set(tile2[0], tile2[i8], tile2[2], tile2[3]);
                i17 = i18 + 1;
                f32 = f37;
            }
            this.aDockRow.set(land[0] + f4, land[9], this.LW - f4, land[9] + land[10]);
            this.dTop = -1000.0f;
            this.dS = 0.0f;
            this.dX = this.aCx;
            this.dY = 0.0f;
            this.aDockDialY = -1000.0f;
            f11 = 26.0f;
            f12 = f32;
            f13 = f33;
            f14 = 12.0f;
        }
        float f38 = this.aS * 0.864f;
        this.cx = this.aCx;
        this.cy = this.aCy;
        this.orbR = this.aS * 47.0f;
        this.bx = this.dX;
        this.by = this.dY;
        this.aBtnDx = this.aS * 71.0f;
        this.aBtnY = this.aCy + (this.aS * 24.0f);
        this.aQuickR = 186.0f * f38;
        float f39 = 150.0f * f38;
        this.aOut[i9] = f39;
        float f40 = 176.0f * f38;
        this.aOut[i8] = f40;
        float f41 = 116.0f * f38;
        this.aMidR[i9] = f41;
        float f42 = 144.0f * f38;
        this.aMidR[i8] = f42;
        this.aIn[i9] = 72.0f * f38;
        this.aIn[i8] = 108.0f * f38;
        this.qsIn = this.aOut[i9];
        this.qsOut = this.aOut[i8];
        float f43 = f14;
        this.nq = Math.min(8, i5);
        int i19 = i9;
        while (i19 < this.nq) {
            float f44 = f38;
            float[] seg = AoaGeo.seg(i19, this.nq, 0.16f, 0.012f);
            this.qsA[i19][i9] = seg[i9];
            this.qsA[i19][i8] = seg[i8];
            this.qsA[i19][i12] = seg[i12];
            int i20 = i19;
            this.qs[i20] = Shapes.wedge(this.aCx, this.aCy, this.aOut[i9], this.aOut[i8], seg[i9], seg[i8], 1.0f);
            this.qsR[i20] = region(this.qs[i20], region);
            this.qsDot[i20].setEmpty();
            i19 = i20 + 1;
            f38 = f44;
            f7 = f7;
        }
        float f45 = f38;
        float f46 = f7;
        this.nw = Math.min(i11, i6);
        int i21 = i9;
        while (i21 < this.nw) {
            float[] seg2 = AoaGeo.seg(i21, this.nw, 0.12f, 0.025f);
            this.whA[i21][i9] = seg2[i9];
            this.whA[i21][i8] = seg2[i8];
            this.whA[i21][i12] = seg2[i12];
            int i22 = i21;
            this.wh[i22] = Shapes.wedge(this.aCx, this.aCy, this.aMidR[i9], this.aMidR[i8], seg2[i9], seg2[i8], 1.0f);
            this.whR[i22] = region(this.wh[i22], region);
            i21 = i22 + 1;
        }
        this.nIn = Math.min(i10, strArr.length);
        for (int i23 = i9; i23 < this.nIn; i23++) {
            float[] seg3 = AoaGeo.seg(i23, this.nIn, 0.12f, 0.025f);
            this.inA[i23][i9] = seg3[i9];
            this.inA[i23][i8] = seg3[i8];
            this.inA[i23][i12] = seg3[i12];
            this.inP[i23] = Shapes.wedge(this.aCx, this.aCy, this.aIn[i9], this.aIn[i8], seg3[i9], seg3[i8], 1.0f);
            this.exR.add(region(this.inP[i23], region));
            this.exA.add(strArr[i23]);
            this.exL.add(strArr2[i23]);
        }
        int i24 = i9;
        while (i24 < i12) {
            float f47 = this.aCx + ((i24 == 0 ? -1 : i8) * this.aBtnDx);
            float max = Math.max(this.aS * 9.0f, f6 / 2.0f);
            Path path = new Path();
            int i25 = i24;
            path.addCircle(f47, this.aBtnY, max, Path.Direction.CW);
            this.exR.add(region(path, region));
            this.exA.add(i25 == 0 ? "aoa:prev" : "aoa:next");
            this.exL.add(i25 == 0 ? "Previous readout" : "Next readout");
            i24 = i25 + 1;
            i12 = 2;
        }
        rect(this.aCornL, "x:clock", "Clock", region);
        rect(this.aCornR, "x:digest", "Notifications", region);
        if (this.aMid.isEmpty()) {
            f15 = f39;
        } else {
            float width = this.aMid.width() / 3.0f;
            f15 = f39;
            rect(new RectF(this.aMid.left + (this.aS * 50.0f), this.aMid.top, this.aMid.left + width, this.aMid.bottom), "x:detail:weather", "Weather", region);
            float f48 = width * 2.0f;
            rect(new RectF(this.aMid.left + width, this.aMid.top, this.aMid.left + f48, this.aMid.bottom), "ai", "Assistant", region);
            rect(new RectF(this.aMid.left + f48, this.aMid.top, this.aMid.right - (this.aS * 50.0f), this.aMid.bottom), "x:detail:alarms", "Next alarm", region);
        }
        float f49 = this.aS * 18.0f;
        float f50 = f49 * 4.0f;
        rect(new RectF(this.aDialLx - f49, this.aDialY - f49, this.aDialLx + f50, this.aDialY + f49), "x:battery", "Battery", region);
        rect(new RectF(this.aDialRx - f50, this.aDialY - f49, this.aDialRx + f49, this.aDialY + f49), "x:detail:calendar", "Today", region);
        this.gTop = f8;
        this.gH = f9 + 4.0f + 12.0f;
        this.gRect.set(z ? f43 : f43 + (this.aS * 22.0f), f8, z ? f43 + f46 : f43 + (this.aS * 338.0f), f9 + f8);
        this.youChip.setEmpty();
        float f51 = z ? f43 : f43 + (this.aS * 14.0f);
        float f52 = z ? f46 : this.aS * 332.0f;
        float f53 = z ? 4.0f : 4.0f * this.aS;
        float f54 = 5.0f;
        float f55 = (f52 - (f53 * 5.0f)) / 6.0f;
        int i26 = i9;
        while (i26 < 6) {
            float f56 = (i26 * (f55 + f53)) + f51;
            float f57 = f56 + f55;
            float f58 = f54;
            float f59 = f10 + f11;
            this.aMode[i26].set(f56, f10, f57, f59);
            Path path2 = new Path();
            float max2 = Math.max(0.0f, (f6 - f11) / 2.0f);
            path2.addRect(f56, f10 - max2, f57, f59 + max2, Path.Direction.CW);
            this.exR.add(region(path2, region));
            this.exA.add("x:mode:" + AOA_MODES[i26]);
            this.exL.add(AOA_MODES[i26].toUpperCase(Locale.ROOT));
            i26++;
            f54 = f58;
        }
        float f60 = f54;
        this.nTile = Math.min(9, strArr3.length);
        for (int i27 = i9; i27 < this.nTile; i27++) {
            rect(this.aTile[i27], strArr3[i27], strArr4[i27], region);
        }
        rect(this.aColR, "x:storage", "Storage", region);
        this.nd = i9;
        int i28 = 0;
        for (int i29 = 5; i28 < i29; i29 = 5) {
            this.dk[i28] = null;
            this.aDk[i28] = null;
            i28++;
        }
        this.appsR = null;
        this.apps = null;
        this.scrubR = null;
        this.RO = 0.0f;
        this.RI = 0.0f;
        this.RA = 0.0f;
        this.aSearch = null;
        String[] strArr5 = {"d0", "d1", "apps", "d3", "ai"};
        if (!z) {
            this.aDOut[0] = f15;
            this.aDOut[i8] = f40;
            this.aDIn[0] = f41;
            this.aDIn[i8] = f42;
            this.aPtt = f45 * 104.0f;
            int i30 = 0;
            while (i30 < 5) {
                float[] dockSeg = AoaGeo.dockSeg(i30, 5, this.aDOut[0] / this.aS, 43.0f);
                this.dkA[i30][0] = dockSeg[0];
                this.dkA[i30][i8] = dockSeg[i8];
                this.dkA[i30][2] = dockSeg[2];
                Path wedge = Shapes.wedge(this.dX, this.dY, this.aDOut[0], this.aDOut[i8], dockSeg[0], dockSeg[i8], 1.0f);
                this.aDk[i30] = wedge;
                if (i30 < 2 && i30 < i7) {
                    this.dk[i30] = wedge;
                    this.dkR[i30] = region(wedge, region);
                    this.nd = i30 + 1;
                } else if (i30 == 2) {
                    this.apps = wedge;
                    this.appsR = region(wedge, region);
                } else {
                    this.exR.add(region(wedge, region));
                    this.exA.add(strArr5[i30]);
                    this.exL.add(i30 == i14 ? "Assistant" : "Dock");
                }
                i30++;
                i14 = 4;
            }
            int i31 = i8;
            float[] dockSeg2 = AoaGeo.dockSeg(0, i31, this.aDIn[0] / this.aS, 43.0f);
            this.aSearch = Shapes.wedge(this.dX, this.dY, this.aDIn[0], this.aDIn[i31], dockSeg2[0], dockSeg2[i31], 1.0f);
            this.exR.add(region(this.aSearch, region));
            this.exA.add("x:search");
            this.exL.add("Search");
            Path path3 = new Path();
            path3.addCircle(this.dX, this.dY, this.aPtt, Path.Direction.CW);
            Path path4 = new Path();
            path4.addRect(this.dX - this.aPtt, this.dTop, this.dX + this.aPtt, this.dY + this.aPtt, Path.Direction.CW);
            path3.op(path4, Path.Op.INTERSECT);
            this.exR.add(region(path3, region));
            this.exA.add("x:talk");
            this.exL.add("Hold to talk");
            float f61 = this.aS * f4;
            rect(new RectF(this.aDockDialLx - f61, this.aDockDialY - f61, this.aDockDialLx + f61, this.aDockDialY + f61), "x:storage", "Storage", region);
            rect(new RectF(this.aDockDialRx - f61, this.aDockDialY - f61, this.aDockDialRx + f61, this.aDockDialY + f61), "x:digest", "Unread", region);
        } else {
            int i32 = 0;
            float width2 = (this.aDockRow.width() - 20.0f) / f60;
            while (i32 < 5) {
                RectF rectF = this.aDkR[i32];
                float f62 = i32 * (width2 + f60);
                rectF.set(this.aDockRow.left + f62, this.aDockRow.top, this.aDockRow.left + f62 + width2, this.aDockRow.bottom);
                Path path5 = new Path();
                path5.addRect(rectF, Path.Direction.CW);
                this.aDk[i32] = path5;
                if (i32 < 2 && i32 < i7) {
                    this.dk[i32] = path5;
                    this.dkR[i32] = region(path5, region);
                    this.nd = i32 + 1;
                } else if (i32 == 2) {
                    this.apps = path5;
                    this.appsR = region(path5, region);
                } else {
                    this.exR.add(region(path5, region));
                    this.exA.add(strArr5[i32]);
                    this.exL.add(i32 == 4 ? "Assistant" : "Dock");
                    i32++;
                }
                i32++;
            }
        }
        this.pTop = this.gRect.top;
        this.ph = 0.0f;
        this.pw = 0.0f;
        this.pLeft = this.gRect.left;
        this.tileY = f12;
        this.tileH = f13;
    }

    void rect(RectF rectF, String str, String str2, Region region) {
        Path path = new Path();
        path.addRect(rectF, Path.Direction.CW);
        this.exR.add(region(path, region));
        this.exA.add(str);
        this.exL.add(str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void layout(float f, float f2, float f3, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8;
        portraitMode();
        float f4 = i;
        float f5 = ((f - f4) - i3) / f3;
        float f6 = i2;
        float f7 = i4;
        float[] stage = UnitsMath.stage(f5, ((f2 - f6) - f7) / f3, 324.0f, 740.0f);
        if (i2 > 0) {
            i8 = Math.max(0, Math.round(f6 - ((stage[0] * f3) * 26.0f)));
            if (i8 != i2) {
                stage = UnitsMath.stage(f5, ((f2 - i8) - f7) / f3, 324.0f, 740.0f);
            }
        } else {
            i8 = i2;
        }
        this.k = stage[0];
        char c = 1;
        this.LW = stage[1];
        this.LH = stage[2];
        this.S = this.k * f3;
        this.ox = f4;
        this.oy = i8;
        float f8 = 2.0f;
        this.cx = this.LW / 2.0f;
        this.cy = 242.0f;
        this.bx = this.cx;
        this.by = this.LH;
        float f9 = -1.0f;
        this.cutLx = (this.cutLpx < 0 || i8 == i2) ? -1.0f : (this.cutLpx - this.ox) / this.S;
        if (this.cutRpx >= 0 && i8 != i2) {
            f9 = (this.cutRpx - this.ox) / this.S;
        }
        this.cutRx = f9;
        this.RO = Math.min(156.0f, this.cx - 6.0f);
        this.RI = this.RO - 44.0f;
        this.RA = this.RI - 8.0f;
        this.ph = 146.0f;
        this.pt = 376.0f;
        this.pb = (this.by - this.RO) - 40.0f;
        this.pTop = Math.max(this.pt, this.pt + (((this.pb - this.pt) - this.ph) / 2.0f));
        if (this.pTop + this.ph > this.pb) {
            this.ph = Math.max(96.0f, this.pb - this.pTop);
        }
        this.pw = Math.min(this.LW - 32.0f, 360.0f);
        this.pLeft = (this.LW - this.pw) / 2.0f;
        Region region = new Region(-10000, -10000, 10000, 10000);
        this.nq = Math.min(6, i5);
        float max = 106.0f / Math.max(1, this.nq);
        int i9 = 0;
        while (i9 < this.nq) {
            float f10 = (i9 * max) + 217.0f + 0.8f;
            int i10 = i9 + 1;
            float f11 = (217.0f + (i10 * max)) - 0.8f;
            this.qsA[i9][0] = f10;
            this.qsA[i9][1] = f11;
            this.qsA[i9][2] = (f10 + f11) / 2.0f;
            this.qs[i9] = Shapes.wedge(this.cx, this.cy, 164.0f, 192.0f, f10, f11, 4.0f);
            this.qsR[i9] = region(this.qs[i9], region);
            i9 = i10;
        }
        this.nw = Math.min(6, i6);
        float max2 = 152.0f / Math.max(1, this.nw);
        int i11 = 0;
        while (i11 < this.nw) {
            float f12 = (i11 * max2) + 194.0f + 1.0f;
            int i12 = i11 + 1;
            float f13 = (194.0f + (i12 * max2)) - 1.0f;
            this.whA[i11][0] = f12;
            this.whA[i11][1] = f13;
            this.whA[i11][2] = (f12 + f13) / 2.0f;
            this.wh[i11] = Shapes.wedge(this.cx, this.cy, 106.0f, 158.0f, f12, f13, 4.0f);
            this.whR[i11] = region(this.wh[i11], region);
            i11 = i12;
        }
        this.nd = Math.min(5, i7);
        float max3 = 180.0f / Math.max(1, this.nd);
        int i13 = 0;
        while (i13 < this.nd) {
            float f14 = (i13 * max3) + 180.0f + 1.0f;
            int i14 = i13 + 1;
            float f15 = ((i14 * max3) + 180.0f) - 1.0f;
            this.dkA[i13][0] = f14;
            this.dkA[i13][c] = f15;
            this.dkA[i13][2] = (f14 + f15) / f8;
            this.dk[i13] = Shapes.wedge(this.bx, this.by, this.RI, this.RO, f14, f15, 4.0f);
            this.dkR[i13] = region(this.dk[i13], region);
            i13 = i14;
            f8 = f8;
            c = 1;
        }
        this.apps = new Path();
        this.apps.moveTo(this.bx - this.RA, this.by);
        this.apps.arcTo(new RectF(this.bx - this.RA, this.by - this.RA, this.bx + this.RA, this.by + this.RA), 180.0f, 180.0f, false);
        this.apps.close();
        this.appsR = region(this.apps, region);
        this.scrubR = region(Shapes.wedge(this.bx, this.by, this.RO + f8, this.RO + 48.0f, 196.0f, 344.0f, 0.0f), region);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void layoutLand(float f, float f2, float f3, int i, int i2, int i3, int i4, int i5, int i6, int i7, String str) {
        this.land = true;
        this.full = false;
        this.mode = "wheel";
        for (int i8 = 0; i8 < 3; i8++) {
            this.tabR[i8] = null;
            this.tab[i8].setEmpty();
        }
        float f4 = i;
        float f5 = ((f - f4) - i3) / f3;
        float f6 = i2;
        float f7 = ((f2 - f6) - i4) / f3;
        this.k = Math.min(f5 / 800.0f, f7 / 360.0f);
        this.LW = f5 / this.k;
        this.LH = f7 / this.k;
        this.S = this.k * f3;
        this.ox = f4;
        this.oy = f6;
        Region region = new Region(-10000, -10000, 10000, 10000);
        this.cy = ((this.LH - TB) / 2.0f) + TB;
        this.nq = Math.min(6, i5);
        this.nw = Math.min(6, i6);
        this.nd = Math.min(4, i7);
        this.orb = true;
        this.full = true;
        this.ringW = 150.0f;
        float[] radii = RingMath.radii(this.LH - TB, 150.0f, 80.0f);
        this.RI = radii[0];
        this.RO = radii[1];
        this.qsIn = radii[2];
        this.qsOut = radii[3];
        this.RA = 0.0f;
        this.cx = this.LW - Math.max(176.0f, this.qsOut + 20.0f);
        this.bx = this.cx;
        this.by = this.cy;
        this.nr = this.nw + this.nd;
        ring(true);
        for (RectF rectF : this.qsDot) {
            rectF.setEmpty();
        }
        this.scrubR = region(Shapes.wedge(this.cx, this.cy, this.qsOut + 2.0f, this.qsOut + 46.0f, 110.0f, 250.0f, 0.0f), region);
        this.allApps.set(this.cx - 24.0f, (this.cy + this.RI) - 28.0f, this.cx + 24.0f, (this.cy + this.RI) - 4.0f);
        this.apps = new Path();
        this.apps.addRect(this.allApps, Path.Direction.CW);
        this.appsR = region(this.apps, region);
        float f8 = ((this.cx - this.qsOut) - 60.0f) - 24.0f;
        this.pw = Math.min(f8, 400.0f);
        this.pLeft = ((f8 - this.pw) / 2.0f) + 24.0f;
        float f9 = ((this.LH - TB) - 40.0f) - 16.0f;
        this.ph = Math.min(f9, PH_MAX);
        this.pTop = ((f9 - this.ph) / 2.0f) + 70.0f;
        this.msX = this.pLeft + 2.0f;
        this.msY = 42.0f;
        this.ringX = -1.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void layoutCompact(float f, float f2, float f3, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        Path path;
        float f4;
        this.land = true;
        int i8 = 0;
        this.full = false;
        this.mode = "compact";
        this.orb = true;
        int i9 = 0;
        while (true) {
            path = null;
            if (i9 >= 3) {
                break;
            }
            this.tabR[i9] = null;
            this.tab[i9].setEmpty();
            i9++;
        }
        float f5 = i;
        float f6 = i2;
        float[] stage = UnitsMath.stage(((f - f5) - i3) / f3, ((f2 - f6) - i4) / f3, 360.0f, 470.0f);
        this.k = stage[0];
        this.LW = stage[1];
        char c = 2;
        this.LH = stage[2];
        this.S = this.k * f3;
        this.ox = f5;
        this.oy = f6;
        Region region = new Region(-10000, -10000, 10000, 10000);
        this.nq = Math.min(6, i5);
        this.nw = Math.min(6, i6);
        this.nd = Math.min(4, i7);
        this.RI = this.RIl;
        this.RO = this.ROl;
        this.RA = 0.0f;
        float f7 = 2.0f;
        this.cx = this.LW / 2.0f;
        this.cy = this.RO + 48.0f;
        this.bx = this.cx;
        this.by = this.cy;
        float f8 = 180.0f;
        float max = 180.0f / Math.max(1, this.nw);
        int i10 = 0;
        while (i10 < this.nw) {
            float f9 = (i10 * max) + f8 + 0.8f;
            int i11 = i8;
            int i12 = i10 + 1;
            Path path2 = path;
            float f10 = ((i12 * max) + f8) - 0.8f;
            this.whA[i10][i11] = f9;
            this.whA[i10][1] = f10;
            this.whA[i10][c] = (f9 + f10) / f7;
            this.wh[i10] = Shapes.wedge(this.cx, this.cy, this.RI, this.RO, f9, f10, 4.0f);
            this.whR[i10] = region(this.wh[i10], region);
            f7 = f7;
            i10 = i12;
            i8 = i11;
            path = path2;
            c = c;
            f8 = 180.0f;
        }
        int i13 = i8;
        Path path3 = path;
        char c2 = c;
        float f11 = f7;
        while (i8 < this.nd) {
            int i14 = i8 + 1;
            float f12 = (180 - (i14 * 45)) + 1;
            float f13 = 43.0f + f12;
            this.dkA[i8][i13] = f12;
            this.dkA[i8][1] = f13;
            this.dkA[i8][c2] = (f12 + f13) / f11;
            this.dk[i8] = Shapes.wedge(this.cx, this.cy, this.RI, this.RO, f12, f13, 4.0f);
            this.dkR[i8] = region(this.dk[i8], region);
            i8 = i14;
        }
        this.scrubR = region(Shapes.wedge(this.cx, this.cy, this.RO + f11, this.RO + 48.0f, 16.0f, 164.0f, 0.0f), region);
        this.allApps.set(this.cx - 24.0f, (this.cy + this.RI) - TB, this.cx + 24.0f, (this.cy + this.RI) - 6.0f);
        this.apps = new Path();
        this.apps.addRect(this.allApps, Path.Direction.CW);
        this.appsR = region(this.apps, region);
        float f14 = this.cy + this.RO + 50.0f;
        float f15 = ((this.LH - f14) - 66.0f) - 8.0f;
        this.msX = -1.0f;
        this.pTop = f14;
        if (f15 >= 120.0f) {
            this.ph = Math.min(PH_MAX, f15);
            this.pw = Math.min(this.LW - 32.0f, 360.0f);
            f4 = (this.LW - this.pw) / f11;
        } else {
            this.ph = 0.0f;
            this.pw = 0.0f;
            f4 = this.cx;
        }
        this.pLeft = f4;
        this.ringW = 130.0f;
        float f16 = (this.LH - 66.0f) - 4.0f;
        float max2 = Math.max(34.0f, Math.min(48.0f, (((this.LW - 12.0f) - this.ringW) - 8.0f) / Math.max(1, this.nq)));
        float max3 = Math.max(4.0f, (this.LW - ((this.ringW + 8.0f) + (this.nq * max2))) / f11);
        this.ringX = max3;
        this.ringY = f16;
        this.qsX = max3 + this.ringW + 8.0f + ((max2 - 26.0f) / f11);
        this.qsY = f16 + 18.0f;
        for (int i15 = i13; i15 < 6; i15++) {
            if (i15 < this.nq) {
                float f17 = this.qsX + (i15 * max2);
                this.qsDot[i15].set(f17, this.qsY, f17 + 26.0f, this.qsY + 26.0f);
                Path path4 = new Path();
                float f18 = f17 + 13.0f;
                float f19 = max2 / f11;
                path4.addRect(f18 - f19, this.qsY - 9.0f, f18 + f19, this.qsY + 35.0f, Path.Direction.CW);
                this.qs[i15] = path3;
                this.qsR[i15] = region(path4, region);
            } else {
                this.qsDot[i15].setEmpty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ring(boolean z) {
        if (this.full) {
            Region region = new Region(-10000, -10000, 10000, 10000);
            float[][] even = RingMath.even(this.nr, -90.0f, 2.0f, this.spin);
            int i = 0;
            while (i < this.nr) {
                boolean z2 = i < this.nw;
                int i2 = z2 ? i : i - this.nw;
                float[] fArr = z2 ? this.whA[i2] : this.dkA[i2];
                fArr[0] = even[i][0];
                fArr[1] = even[i][1];
                fArr[2] = even[i][2];
                Path wedge = Shapes.wedge(this.cx, this.cy, this.RI, this.RO, fArr[0], fArr[1], 6.0f);
                if (z2) {
                    this.wh[i2] = wedge;
                    if (z) {
                        this.whR[i2] = region(wedge, region);
                    }
                } else {
                    this.dk[i2] = wedge;
                    if (z) {
                        this.dkR[i2] = region(wedge, region);
                    }
                }
                i++;
            }
            ringQ(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ringQ(boolean z) {
        if (this.full) {
            Region region = new Region(-10000, -10000, 10000, 10000);
            float[][] even = RingMath.even(this.nq, -90.0f, 3.0f, this.spinQ);
            for (int i = 0; i < this.nq; i++) {
                this.qsA[i][0] = even[i][0];
                this.qsA[i][1] = even[i][1];
                this.qsA[i][2] = even[i][2];
                this.qs[i] = Shapes.wedge(this.cx, this.cy, this.qsIn, this.qsOut, even[i][0], even[i][1], 4.0f);
                if (z) {
                    this.qsR[i] = region(this.qs[i], region);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float seg() {
        if (this.nr > 0) {
            return 360.0f / this.nr;
        }
        return 360.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float segQ() {
        if (this.nq > 0) {
            return 360.0f / this.nq;
        }
        return 360.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean tabsOn() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void tabs(float[] fArr, float[] fArr2) {
        Region region = new Region(-10000, -10000, 10000, 10000);
        for (int i = 0; i < 3; i++) {
            this.tab[i].set(fArr[i], 0.0f, fArr2[i], TB);
            Path path = new Path();
            path.addRect(fArr[i], 0.0f, fArr2[i], 44.0f, Path.Direction.CW);
            this.tabR[i] = region(path, region);
        }
    }

    void portraitMode() {
        this.full = false;
        this.land = false;
        this.exR.clear();
        this.exA.clear();
        this.exL.clear();
        this.orb = true;
        this.mode = "portrait";
        for (int i = 0; i < 3; i++) {
            this.tabR[i] = null;
        }
    }

    static Region region(Path path, Region region) {
        Path path2 = new Path(path);
        Matrix matrix = new Matrix();
        matrix.setScale(8.0f, 8.0f);
        path2.transform(matrix);
        Region region2 = new Region();
        region2.setPath(path2, new Region(-80000, -80000, 80000, 80000));
        return region2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hit(Region region, float f, float f2) {
        return region != null && region.contains(Math.round(f * 8.0f), Math.round(f2 * 8.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float lx(float f) {
        return (f - this.ox) / this.S;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float ly(float f) {
        return (f - this.oy) / this.S;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float chord(float f, float f2, float f3) {
        return Math.max(8.0f, ((f * 2.0f) * ((float) Math.sin(((f3 - f2) * 3.141592653589793d) / 360.0d))) - 6.0f);
    }
}
