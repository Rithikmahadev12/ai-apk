package com.jarvis.assist.aosui.core;

import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: /tmp/augmaos/classes.dex */
public final class FrameClock implements Choreographer.FrameCallback {
    public static final FrameClock I = new FrameClock();
    public int drawn;
    public float jsMs;
    long lastDraw;
    long lastVsync;
    boolean posted;
    public float vsyncMs;
    final ArrayList<Entry> subs = new ArrayList<>();
    boolean active = true;

    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Sub {
        boolean frame(long j, float f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Entry {
        boolean on;
        final Sub s;
        final View v;

        Entry(Sub sub, View view) {
            this.s = sub;
            this.v = view;
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Handle {
        final Entry e;

        Handle(Entry entry) {
            this.e = entry;
        }

        public void kick() {
            this.e.on = true;
            FrameClock.this.post();
        }

        public void remove() {
            FrameClock.this.subs.remove(this.e);
        }

        public void stop() {
            this.e.on = false;
        }
    }

    public Handle add(Sub sub, View view) {
        Entry entry = new Entry(sub, view);
        entry.on = true;
        this.subs.add(entry);
        post();
        return new Handle(entry);
    }

    public void setActive(boolean z) {
        this.active = z;
        if (z) {
            this.lastDraw = 0L;
            Iterator<Entry> it = this.subs.iterator();
            while (it.hasNext()) {
                it.next().on = true;
            }
            post();
        }
    }

    void post() {
        if (!this.posted && this.active && want()) {
            this.posted = true;
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    boolean want() {
        for (int i = 0; i < this.subs.size(); i++) {
            Entry entry = this.subs.get(i);
            if (entry.on && (entry.v == null || entry.v.isShown())) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.Choreographer.FrameCallback
    public void doFrame(long j) {
        boolean z;
        this.posted = false;
        if (!this.active) {
            this.lastVsync = 0L;
            this.lastDraw = 0L;
            return;
        }
        long j2 = j / 1000000;
        float max = 1000.0f / Math.max(1, Perf.k().fps);
        if (this.lastVsync != 0) {
            long j3 = j2 - this.lastVsync;
            if (j3 > 4 && j3 < 60) {
                this.vsyncMs = this.vsyncMs == 0.0f ? (float) j3 : (this.vsyncMs * 0.9f) + (((float) j3) * 0.1f);
            }
        }
        this.lastVsync = j2;
        if (this.lastDraw != 0 && ((float) (j2 - this.lastDraw)) < max - 2.0f) {
            this.posted = true;
            Choreographer.getInstance().postFrameCallback(this);
            return;
        }
        if (this.lastDraw != 0) {
            max = (float) (j2 - this.lastDraw);
        }
        this.lastDraw = j2;
        long nanoTime = System.nanoTime();
        boolean z2 = false;
        for (int i = 0; i < this.subs.size(); i++) {
            Entry entry = this.subs.get(i);
            if (entry.on && (entry.v == null || entry.v.isShown())) {
                try {
                    z = entry.s.frame(j2, max);
                } catch (RuntimeException e) {
                    Log.w("aOS", "frame", e);
                    z = false;
                }
                entry.on = z;
                if (z) {
                    z2 = true;
                }
            }
        }
        this.jsMs = (this.jsMs * 0.95f) + ((((float) (System.nanoTime() - nanoTime)) / 1000000.0f) * 0.05f);
        this.drawn++;
        if (z2) {
            Perf.frame(max, this.vsyncMs);
            post();
        } else {
            this.lastVsync = 0L;
            this.lastDraw = 0L;
        }
    }
}
