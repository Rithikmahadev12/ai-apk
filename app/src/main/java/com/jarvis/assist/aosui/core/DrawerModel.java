package com.jarvis.assist.aosui.core;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class DrawerModel {
    public static final String KEY = "aos.drawer";
    public static final int MAX_FOLDERS = 24;
    public static final int MAX_PINS = 12;
    public static final int NAME_MAX = 24;
    public static final String RAIL = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private DrawerModel() {
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Folder {
        public final ArrayList<String> apps = new ArrayList<>();
        public String name;

        public Folder(String str) {
            this.name = str;
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Cfg {
        public String view = "grid";
        public final ArrayList<String> pins = new ArrayList<>();
        public final ArrayList<Folder> folders = new ArrayList<>();

        public boolean list() {
            return "list".equals(this.view);
        }
    }

    public static Cfg cfg(String str) {
        String str2 = "list";
        Cfg cfg = new Cfg();
        if (str == null || str.isEmpty()) {
            return cfg;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!"list".equals(jSONObject.optString("view"))) {
                str2 = "grid";
            }
            cfg.view = str2;
            JSONArray optJSONArray = jSONObject.optJSONArray("pins");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length() && cfg.pins.size() < 12; i++) {
                    String optString = optJSONArray.optString(i, "");
                    if (!optString.isEmpty() && !cfg.pins.contains(optString)) {
                        cfg.pins.add(optString);
                    }
                }
            }
            JSONArray optJSONArray2 = jSONObject.optJSONArray("folders");
            HashSet hashSet = new HashSet();
            if (optJSONArray2 != null) {
                for (int i2 = 0; i2 < optJSONArray2.length() && cfg.folders.size() < 24; i2++) {
                    JSONObject optJSONObject = optJSONArray2.optJSONObject(i2);
                    if (optJSONObject != null) {
                        Folder folder = new Folder(name(optJSONObject.optString("n")));
                        JSONArray optJSONArray3 = optJSONObject.optJSONArray("a");
                        if (optJSONArray3 != null) {
                            for (int i3 = 0; i3 < optJSONArray3.length(); i3++) {
                                String optString2 = optJSONArray3.optString(i3, "");
                                if (!optString2.isEmpty() && hashSet.add(optString2)) {
                                    folder.apps.add(optString2);
                                }
                            }
                        }
                        cfg.folders.add(folder);
                    }
                }
            }
        } catch (Exception e) {
        }
        return cfg;
    }

    public static String save(Cfg cfg) {
        try {
            JSONObject put = new JSONObject().put("view", cfg.list() ? "list" : "grid").put("pins", new JSONArray((Collection) cfg.pins));
            JSONArray jSONArray = new JSONArray();
            Iterator<Folder> it = cfg.folders.iterator();
            while (it.hasNext()) {
                Folder next = it.next();
                jSONArray.put(new JSONObject().put("n", next.name).put("a", new JSONArray((Collection) next.apps)));
            }
            return put.put("folders", jSONArray).toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    public static String name(String str) {
        String replaceAll = str == null ? "" : str.trim().replaceAll("\\s+", " ");
        if (replaceAll.length() > 24) {
            replaceAll = replaceAll.substring(0, 24).trim();
        }
        return replaceAll.isEmpty() ? "Folder" : replaceAll;
    }

    public static boolean pinned(Cfg cfg, String str) {
        return cfg.pins.contains(str);
    }

    public static boolean togglePin(Cfg cfg, String str) {
        if (cfg.pins.remove(str)) {
            return true;
        }
        if (cfg.pins.size() >= 12) {
            return false;
        }
        cfg.pins.add(str);
        return true;
    }

    public static int folderOf(Cfg cfg, String str) {
        for (int i = 0; i < cfg.folders.size(); i++) {
            if (cfg.folders.get(i).apps.contains(str)) {
                return i;
            }
        }
        return -1;
    }

    public static int create(Cfg cfg, String str, String str2) {
        if (cfg.folders.size() >= 24) {
            return -1;
        }
        cfg.folders.add(new Folder(name(str)));
        if (str2 != null) {
            addTo(cfg, cfg.folders.size() - 1, str2);
        }
        return cfg.folders.size() - 1;
    }

    public static boolean addTo(Cfg cfg, int i, String str) {
        if (i < 0 || i >= cfg.folders.size() || str == null || str.isEmpty()) {
            return false;
        }
        removeFrom(cfg, str);
        cfg.folders.get(i).apps.add(str);
        return true;
    }

    public static boolean removeFrom(Cfg cfg, String str) {
        Iterator<Folder> it = cfg.folders.iterator();
        boolean z = false;
        while (it.hasNext()) {
            z |= it.next().apps.remove(str);
        }
        return z;
    }

    public static boolean rename(Cfg cfg, int i, String str) {
        if (i < 0 || i >= cfg.folders.size()) {
            return false;
        }
        cfg.folders.get(i).name = name(str);
        return true;
    }

    public static boolean delete(Cfg cfg, int i) {
        if (i < 0 || i >= cfg.folders.size()) {
            return false;
        }
        cfg.folders.remove(i);
        return true;
    }

    public static List<String> mainPkgs(Cfg cfg, List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (!cfg.pins.contains(str) && folderOf(cfg, str) < 0) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public static String letter(String str) {
        String trim = str == null ? "" : str.trim();
        if (trim.isEmpty()) {
            return "#";
        }
        String upperCase = Normalizer.normalize(trim.substring(0, Math.min(2, trim.length())), Normalizer.Form.NFD).replaceAll("\\p{M}", "").toUpperCase(Locale.ROOT);
        char charAt = upperCase.isEmpty() ? '#' : upperCase.charAt(0);
        return (charAt < 'A' || charAt > 'Z') ? "#" : String.valueOf(charAt);
    }

    public static int railIndex(float f, float f2, int i) {
        if (i <= 0 || f2 <= 0.0f) {
            return 0;
        }
        return Math.max(0, Math.min(i - 1, (int) ((f / f2) * i)));
    }

    public static int jump(List<String> list, String str) {
        int i = -1;
        if (list.isEmpty()) {
            return -1;
        }
        int indexOf = RAIL.indexOf(str);
        int i2 = Integer.MAX_VALUE;
        for (int i3 = 0; i3 < list.size(); i3++) {
            int indexOf2 = RAIL.indexOf(list.get(i3));
            if (indexOf2 == indexOf) {
                return i3;
            }
            if (indexOf2 > indexOf && indexOf2 < i2) {
                i = i3;
                i2 = indexOf2;
            }
        }
        return i >= 0 ? i : list.size() - 1;
    }
}
