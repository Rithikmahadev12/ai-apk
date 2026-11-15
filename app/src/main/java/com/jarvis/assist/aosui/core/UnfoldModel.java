package com.jarvis.assist.aosui.core;

/* loaded from: /tmp/augmaos/classes.dex */
public final class UnfoldModel {
    public static final float JUMP = 1.3f;
    public static final String KEY = "aos.bootfx.unfold";
    public static final long PENDING_MS = 2000;
    public static final long RATE_MS = 20000;
    boolean lastFolded;
    boolean mainWasOn = true;
    long lastPlay = -4611686018427387904L;
    long pendingAt = -1;

    public static boolean on(String str) {
        return !"0".equals(str);
    }

    public boolean displays(boolean z, boolean z2) {
        boolean z3 = z && !this.mainWasOn && this.lastFolded;
        if (z) {
            this.lastFolded = false;
        } else if (z2) {
            this.lastFolded = true;
        }
        this.mainWasOn = z;
        return z3;
    }

    public boolean folded() {
        return this.lastFolded;
    }

    public static boolean sizeJump(int i, int i2) {
        return i > 0 && ((float) i2) >= ((float) i) * 1.3f;
    }

    public boolean allow(long j) {
        if (j - this.lastPlay < RATE_MS) {
            return false;
        }
        this.lastPlay = j;
        return true;
    }

    public void played(long j) {
        this.lastPlay = j;
    }

    public void pend(long j) {
        this.pendingAt = j;
    }

    public boolean takePending(long j) {
        boolean z = this.pendingAt >= 0 && j - this.pendingAt <= 2000;
        this.pendingAt = -1L;
        return z;
    }
}
