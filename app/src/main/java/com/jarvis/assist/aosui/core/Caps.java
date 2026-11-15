package com.jarvis.assist.aosui.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Caps {
    public static final int CHATS = 30;
    public static final int MSGS = 500;
    public static final int NOTIF = 200;
    public static final int RECENT = 12;
    public static final int ROUTINE_HOUR = 50;
    public static final int USAGE = 200;

    private Caps() {
    }

    public static <T> List<T> tail(List<T> list, int i) {
        return list.size() <= i ? list : new ArrayList(list.subList(list.size() - i, list.size()));
    }

    public static List<String> dropLowest(Map<String, Integer> map, int i) {
        ArrayList arrayList = new ArrayList();
        if (map.size() <= i) {
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList(map.entrySet());
        final ArrayList arrayList3 = new ArrayList(map.keySet());
        arrayList2.sort(new Comparator() { // from class: com.jarvis.assist.aosui.core.Caps$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Caps.lambda$dropLowest$0(arrayList3, (Map.Entry) obj, (Map.Entry) obj2);
            }
        });
        for (int i2 = 0; i2 < map.size() - i; i2++) {
            arrayList.add((String) ((Map.Entry) arrayList2.get(i2)).getKey());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$dropLowest$0(ArrayList arrayList, Map.Entry entry, Map.Entry entry2) {
        return ((Integer) entry.getValue()).equals(entry2.getValue()) ? arrayList.indexOf(entry2.getKey()) - arrayList.indexOf(entry.getKey()) : ((Integer) entry.getValue()).intValue() - ((Integer) entry2.getValue()).intValue();
    }

    public static List<String> dropOldest(Iterator<String> it, int i, int i2, String str) {
        ArrayList arrayList = new ArrayList();
        int i3 = i - i2;
        while (i3 > 0 && it.hasNext()) {
            String next = it.next();
            if (!next.equals(str)) {
                arrayList.add(next);
                i3--;
            }
        }
        return arrayList;
    }
}
