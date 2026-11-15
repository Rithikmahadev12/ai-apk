package com.jarvis.assist.aosui.core;

import java.util.HashMap;
import java.util.Map;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Drafts {
    public String focus;
    private final Map<String, String> m = new HashMap();
    public int selEnd;
    public int selStart;

    public String get(String str) {
        return this.m.get(str);
    }

    public void put(String str, String str2) {
        if (str2 == null || str2.isEmpty()) {
            this.m.remove(str);
        } else {
            this.m.put(str, str2);
        }
    }

    public void clear(String str) {
        this.m.remove(str);
        if (str.equals(this.focus)) {
            this.focus = null;
        }
    }

    public void clear() {
        this.m.clear();
        this.focus = null;
        this.selEnd = 0;
        this.selStart = 0;
    }

    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    public String toString() {
        return "Drafts(" + this.m.size() + ")";
    }
}
