package com.jarvis.assist.aosui.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AppGridModel {
    public static final String ID = "appgrid";
    public static final String KEY = "aos.appgrid";
    public static final int MAX = 8;
    public static final int MIN = 4;

    private AppGridModel() {
    }

    public static List<String> parse(String str) {
        ArrayList arrayList = new ArrayList();
        if (str == null || str.isEmpty()) {
            return arrayList;
        }
        try {
            JSONArray jSONArray = new JSONArray(str);
            for (int i = 0; i < jSONArray.length() && arrayList.size() < 8; i++) {
                String optString = jSONArray.optString(i, "");
                if (optString.indexOf(47) > 0 && !arrayList.contains(optString)) {
                    arrayList.add(optString);
                }
            }
        } catch (Exception e) {
        }
        return arrayList;
    }

    public static String save(List<String> list) {
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < list.size() && i < 8; i++) {
            jSONArray.put(list.get(i));
        }
        return jSONArray.toString();
    }

    public static int[] grid(int i) {
        return new int[]{Math.max(2, Math.min(4, (Math.max(4, Math.min(8, i)) + 1) / 2)), 2};
    }

    public static int cells(int i) {
        int[] grid = grid(i);
        return grid[0] * grid[1];
    }

    public static boolean add(List<String> list, String str) {
        if (str == null || str.indexOf(47) <= 0 || list.contains(str) || list.size() >= 8) {
            return false;
        }
        list.add(str);
        return true;
    }

    public static boolean remove(List<String> list, String str) {
        return list.remove(str);
    }

    public static boolean move(List<String> list, int i, int i2) {
        return SettingsModel.move(list, i, i2);
    }

    public static List<String> alive(List<String> list, Collection<String> collection) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (collection.contains(str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }
}
