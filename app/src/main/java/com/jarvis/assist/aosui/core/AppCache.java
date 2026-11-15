package com.jarvis.assist.aosui.core;

import java.util.HashMap;
import java.util.Iterator;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AppCache {
    int gen;
    String value;
    int valueGen = -1;
    final HashMap<String, String> labels = new HashMap<>();

    public synchronized String get() {
        return this.value;
    }

    public synchronized int gen() {
        return this.gen;
    }

    public synchronized boolean fresh() {
        boolean z;
        if (this.value != null) {
            z = this.valueGen == this.gen;
        }
        return z;
    }

    public synchronized boolean put(int i, String str) {
        if (this.value == null || i >= this.valueGen) {
            this.value = str;
            this.valueGen = i;
        }
        return i == this.gen;
    }

    public synchronized void invalidate(String str) {
        this.gen++;
        if (str == null) {
            this.labels.clear();
            return;
        }
        Iterator<String> it = this.labels.keySet().iterator();
        while (it.hasNext()) {
            if (it.next().startsWith(str + "/")) {
                it.remove();
            }
        }
    }

    public synchronized String label(String str) {
        return this.labels.get(str);
    }

    public synchronized void label(String str, String str2) {
        if (this.labels.size() < 2000) {
            this.labels.put(str, str2);
        }
    }

    public synchronized int labels() {
        return this.labels.size();
    }
}
