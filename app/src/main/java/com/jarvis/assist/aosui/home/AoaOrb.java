package com.jarvis.assist.aosui.home;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import com.jarvis.assist.aosui.core.AoaBootSeq;
import com.jarvis.assist.aosui.core.BootSeq;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Theme;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class AoaOrb {
    static final float[][] LB = {new float[]{0.05f,-0.25f,0.2f,1.0f},new float[]{0.12f,-0.48f,0.1f,1.0f},new float[]{0.9f,-0.3f,0.1f,0.0f},new float[]{-0.75f,0.15f,0.3f,0.0f},new float[]{-0.8f,0.45f,0.2f,1.0f}};
    static final float[][] RH = {new float[]{0.12f,0.1f},new float[]{0.88f,0.22f},new float[]{0.18f,0.82f},new float[]{0.62f,0.94f}};
    int[] L;
    int N;
    float[] P;
    float R;
    float S;
    Paint add;
    float[][] arcs;
    float[] arr;
    public float asm;
    boolean big;
    RadialGradient core;
    RadialGradient ember;
    public float fill;
    float gcx;
    float gcy;
    RadialGradient glass;
    float gu;
    RadialGradient halo;
    RadialGradient hi;
    public float ign;
    int[] kind;
    float[] lines;
    float[][] mk;
    Matrix mx;
    int nl;
    Paint p;
    Path path;
    float[] q;
    RectF rf;
    public float rim;
    public float spanX;
    public float spanY;
    float[] sx;
    float[] sy;
    float[] w3;

    public AoaOrb(boolean z) {
        L = new int[0];
        p = new Paint(1);
        add = new Paint(1);
        rf = new RectF();
        path = new Path();
        mx = new Matrix();
        w3 = new float[3];
        asm = -1.0f;
        ign = 1.0f;
        rim = 1.0f;
        fill = 1.0f;
        spanX = 600.0f;
        spanY = 1200.0f;
        gu = -1.0f;
        lines = new float[0];
        big = z;
        Random random = new Random(z ? 290L : 176L);
        add.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        p.setStrokeCap(Paint.Cap.BUTT);
        if (!z) {
            S = 176.0f;
            R = S * 0.84f;
            N = BootSeq.HOLD_MS;
            P = new float[N * 4];
            kind = new int[N];
            for (int i = 0; i < N; i++) {
                float nx = (random.nextFloat() * 2.0f) - 1.0f;
                float a = random.nextFloat() * 6.283f;
                float r;
                if (i < 170) r = (random.nextFloat() * 0.2f) + 0.32f;
                else if (i < 220) r = random.nextFloat() * 0.25f;
                else r = (random.nextFloat() * 0.05f) + 0.95f;
                float sq = (float) Math.sqrt(1.0f - nx * nx);
                int idx = i * 4;
                P[idx]   = ((float) Math.cos(a)) * sq * r;
                P[idx+1] = nx * r * (i < 170 ? 0.55f : 1.0f);
                P[idx+2] = sq * ((float) Math.sin(a)) * r;
                P[idx+3] = random.nextFloat();
                kind[i] = i < 220 ? 0 : 1;
            }
            ArrayList<Integer> links = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                int c = 0;
                for (int j = 0; j < N && c < 2; j++) {
                    if (j != i) {
                        int ai = i * 4, aj = j * 4;
                        float dx = P[ai]-P[aj], dy = P[ai+1]-P[aj+1], dz = P[ai+2]-P[aj+2];
                        if (dx*dx + dy*dy + dz*dz < 0.03f) { links.add(i); links.add(j); c++; }
                    }
                }
            }
            setLinks(links);
            arcs = new float[9][];
            for (int i = 0; i < 9; i++) {
                arcs[i] = new float[]{random.nextFloat()*6.283f, random.nextFloat()*6.283f, (random.nextFloat()*1.2f)+0.4f};
            }
            mk = new float[0][];
        } else {
            S = 290.0f;
            R = S * 0.9f;
            N = 1080;
            P = new float[N * 4];
            kind = new int[N];
            int idx = 0;
            for (int i = 0; i < 140; i++) {
                float nx = (random.nextFloat()*2.0f)-1.0f;
                float a = random.nextFloat()*6.283f;
                float r = ((float)Math.pow(random.nextFloat(),1.6d))*0.22f;
                float sq = (float)Math.sqrt(1.0f - nx*nx);
                set(idx++, ((float)Math.cos(a))*sq*r, nx*r, sq*((float)Math.sin(a))*r, 0);
            }
            for (int i = 0; i < 320; i++) {
                float t = i/320.0f;
                float a = (t*6.283f*1.6f)+(random.nextFloat()*0.25f);
                float r = (t*0.28f)+0.3f+(random.nextFloat()*0.05f);
                set(idx++, ((float)Math.cos(a))*r, (random.nextFloat()-0.5f)*0.12f, ((float)Math.sin(a))*r, 1);
            }
            for (int i = 0; i < 620; i++) {
                float nx = (random.nextFloat()*2.0f)-1.0f;
                float a = random.nextFloat()*6.283f;
                float r = (random.nextFloat()*0.04f)+0.97f;
                float sq = (float)Math.sqrt(1.0f - nx*nx);
                set(idx++, ((float)Math.cos(a))*sq*r, nx*r, sq*((float)Math.sin(a))*r, 2);
            }
            ArrayList<Integer> links2 = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                float th = kind[i] == 2 ? 0.02f : 0.012f;
                int c = 0;
                for (int j = i+1; j < N && c < 2; j++) {
                    int ai=i*4, aj=j*4;
                    float dx=P[ai]-P[aj], dy=P[ai+1]-P[aj+1], dz=P[ai+2]-P[aj+2];
                    if (dx*dx+dy*dy+dz*dz < th) { links2.add(i); links2.add(j); c++; }
                }
            }
            setLinks(links2);
            arcs = new float[16][];
            for (int i = 0; i < 16; i++) {
                arcs[i] = new float[]{random.nextFloat()*3.14f, random.nextFloat()*6.283f, random.nextFloat()*6.283f, (random.nextFloat()*2.0f)+0.8f, (random.nextFloat()*0.25f)+0.95f};
            }
            mk = new float[9][];
            for (int i = 0; i < 9; i++) {
                float nx = (random.nextFloat()*2.0f)-1.0f;
                float a = random.nextFloat()*6.283f;
                float sq = (float)Math.sqrt(1.0f - nx*nx);
                mk[i] = new float[]{((float)Math.cos(a))*sq*1.12f, nx*1.12f, sq*((float)Math.sin(a))*1.12f};
            }
        }
        q = new float[N * 3];
    }

    void set(int i, float f, float f2, float f3, int i2) {
        int i3 = i * 4;
        P[i3] = f; P[i3+1] = f2; P[i3+2] = f3; kind[i] = i2;
    }

    void setLinks(ArrayList<Integer> list) {
        L = new int[list.size()];
        for (int i = 0; i < list.size(); i++) L[i] = list.get(i);
    }

    public void draw(Canvas canvas, Theme theme, float f) { }
    public void update(long j, float f) { }
}
