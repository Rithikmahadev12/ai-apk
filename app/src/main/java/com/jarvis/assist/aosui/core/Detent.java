package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Detent {
    public static final long MIN_MS = 28;
    public static final float STEP = 5.5f;
    float last = Float.NaN;
    long lastAt = -1000;

    public void reset(float f) {
        this.last = f;
    }

    public boolean move(float f, long j) {
        if (Float.isNaN(this.last)) {
            this.last = f;
            return false;
        }
        if (Math.abs(f - this.last) < 5.5f) {
            return false;
        }
        this.last = f;
        if (j - this.lastAt < 28) {
            return false;
        }
        this.lastAt = j;
        return true;
    }
}
