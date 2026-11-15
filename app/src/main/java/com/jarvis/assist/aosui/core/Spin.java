package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Spin {
    public static final float FRICTION = 3.2f;
    public static final float MAX_VEL = 2400.0f;
    public static final float MIN_VEL = 40.0f;
    public static final float SNAP_MS = 180.0f;
    public float angle;
    final boolean bounded;
    final float max;
    final float min;
    public final float seg;
    float snapFrom;
    float snapT;
    float snapTo;
    boolean snapping;
    public float vel;

    public Spin(float f, float f2) {
        this(f, f2, Float.NaN, Float.NaN);
    }

    public Spin(float f, float f2, float f3, float f4) {
        this.angle = f;
        this.seg = Math.max(0.001f, f2);
        this.min = f3;
        this.max = f4;
        this.bounded = (Float.isNaN(f3) || Float.isNaN(f4)) ? false : true;
    }

    public void fling(float f, boolean z) {
        this.vel = z ? Math.max(-2400.0f, Math.min(2400.0f, f)) : 0.0f;
        if (!z || Math.abs(this.vel) < 40.0f) {
            startSnap();
        }
    }

    void startSnap() {
        this.vel = 0.0f;
        this.snapping = true;
        this.snapFrom = this.angle;
        this.snapTo = clamp(Math.round(this.angle / this.seg) * this.seg);
        this.snapT = 0.0f;
    }

    float clamp(float f) {
        return this.bounded ? Math.max(Math.min(this.min, this.max), Math.min(Math.max(this.min, this.max), f)) : f;
    }

    public boolean done() {
        return !this.snapping && this.vel == 0.0f;
    }

    public int index() {
        return Math.round(this.angle / this.seg);
    }

    public int step(float f) {
        if (done()) {
            return 0;
        }
        int index = index();
        float max = Math.max(0.0f, Math.min(64.0f, f)) / 1000.0f;
        if (this.snapping) {
            this.snapT = Math.min(1.0f, this.snapT + (f / 180.0f));
            this.angle = this.snapFrom + ((this.snapTo - this.snapFrom) * (1.0f - (((1.0f - this.snapT) * (1.0f - this.snapT)) * (1.0f - this.snapT))));
            if (this.snapT >= 1.0f) {
                this.angle = this.snapTo;
                this.snapping = false;
            }
        } else {
            this.angle += this.vel * max;
            this.vel *= (float) Math.exp(max * (-3.2f));
            if (this.bounded && this.angle != clamp(this.angle)) {
                this.angle = clamp(this.angle);
                startSnap();
            } else if (Math.abs(this.vel) < 40.0f) {
                startSnap();
            }
        }
        return Math.abs(index() - index);
    }
}
