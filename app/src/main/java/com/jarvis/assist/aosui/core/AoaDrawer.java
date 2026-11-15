package com.jarvis.assist.aosui.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaDrawer {
    private AoaDrawer() {
    }

    public static String idx(int i) {
        return String.format(Locale.ROOT, "%03d", Integer.valueOf(i + 1));
    }

    public static List<int[]> sections(List<String> list, List<String> list2) {
        ArrayList arrayList = new ArrayList();
        list2.clear();
        Object obj = null;
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (!str.equals(obj)) {
                arrayList.add(new int[]{i, 0});
                list2.add(str);
                obj = str;
            }
            int[] iArr = (int[]) arrayList.get(arrayList.size() - 1);
            iArr[1] = iArr[1] + 1;
        }
        return arrayList;
    }

    public static int current(float[] fArr, float f) {
        int i;
        int i2 = -1;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            i = i2;
            i2 = i4;
            if (i2 >= fArr.length || fArr[i2] > f) {
                break;
            }
            i3 = i2 + 1;
        }
        return i;
    }

    public static int rail(String str) {
        return Math.max(0, (str == null || str.isEmpty()) ? -1 : DrawerModel.RAIL.indexOf(str.charAt(0)));
    }
}
