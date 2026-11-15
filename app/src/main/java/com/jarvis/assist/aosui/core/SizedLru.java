package com.jarvis.assist.aosui.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/* loaded from: /tmp/augmaos/classes.dex */
public final class SizedLru<K, V> {
    public int evictions;
    final LinkedHashMap<K, V> m = new LinkedHashMap<>(16, 0.75f, true);
    int max;
    int size;
    final Sizer<V> sz;

    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Sizer<V> {
        int size(V v);
    }

    public SizedLru(int i, Sizer<V> sizer) {
        this.max = Math.max(1, i);
        this.sz = sizer;
    }

    public synchronized V get(K k) {
        return this.m.get(k);
    }

    public synchronized void put(K k, V v) {
        if (k != null && v != null) {
            int size = this.sz.size(v);
            if (size <= this.max) {
                V put = this.m.put(k, v);
                if (put != null) {
                    this.size -= this.sz.size(put);
                }
                this.size += size;
                trim(this.max);
            }
        }
    }

    public synchronized V remove(K k) {
        V remove;
        remove = this.m.remove(k);
        if (remove != null) {
            this.size -= this.sz.size(remove);
        }
        return remove;
    }

    public synchronized void removeIf(Predicate<K> predicate) {
        Iterator<Map.Entry<K, V>> it = this.m.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> next = it.next();
            if (predicate.test(next.getKey())) {
                this.size -= this.sz.size(next.getValue());
                it.remove();
            }
        }
    }

    public synchronized void trim(int i) {
        Iterator<Map.Entry<K, V>> it = this.m.entrySet().iterator();
        while (this.size > i && it.hasNext()) {
            this.size -= this.sz.size(it.next().getValue());
            it.remove();
            this.evictions++;
        }
    }

    public synchronized void clear() {
        this.m.clear();
        this.size = 0;
    }

    public synchronized int size() {
        return this.size;
    }

    public synchronized int count() {
        return this.m.size();
    }

    public int max() {
        return this.max;
    }
}
