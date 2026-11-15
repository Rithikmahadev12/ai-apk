package com.jarvis.assist.aosui.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class HomePages {
    public static final int GRID_MAX = 24;
    public static final int GRID_MIN = 4;
    public static final String KEY = "aos.pages";
    public static final int MAX_BLOCKS = 6;
    public static final int MAX_PAGES = 5;
    public static final int NAME_MAX = 16;
    public static final String[] TYPES = {"grid", "widget", "half", "big"};

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Cfg {
        public int def;
        public final ArrayList<Page> pages = new ArrayList<>();
    }

    private HomePages() {
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Block {
        public final ArrayList<String> items = new ArrayList<>();
        public String type;

        public Block(String str) {
            this.type = str;
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Page {
        public final ArrayList<Block> blocks = new ArrayList<>();
        public String name;

        public Page(String str) {
            this.name = HomePages.name(str);
        }
    }

    public static String name(String str) {
        String replaceAll = str == null ? "" : str.trim().replaceAll("\\s+", " ");
        if (replaceAll.length() > 16) {
            replaceAll = replaceAll.substring(0, 16).trim();
        }
        return replaceAll.isEmpty() ? "PAGE" : replaceAll.toUpperCase(Locale.ROOT);
    }

    public static Cfg cfg(String str) {
        Cfg cfg = new Cfg();
        if (str == null || str.isEmpty()) {
            return cfg;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONArray optJSONArray = jSONObject.optJSONArray("pages");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length() && cfg.pages.size() < 5; i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        Page page = new Page(optJSONObject.optString("n"));
                        JSONArray optJSONArray2 = optJSONObject.optJSONArray("b");
                        if (optJSONArray2 != null) {
                            for (int i2 = 0; i2 < optJSONArray2.length() && page.blocks.size() < 6; i2++) {
                                JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i2);
                                if (optJSONObject2 != null) {
                                    String optString = optJSONObject2.optString("t");
                                    if (Arrays.asList(TYPES).contains(optString)) {
                                        Block block = new Block(optString);
                                        if ("grid".equals(optString)) {
                                            JSONArray optJSONArray3 = optJSONObject2.optJSONArray("a");
                                            if (optJSONArray3 != null) {
                                                for (int i3 = 0; i3 < optJSONArray3.length() && block.items.size() < 24; i3++) {
                                                    String optString2 = optJSONArray3.optString(i3);
                                                    if (optString2.indexOf(47) > 0 && !block.items.contains(optString2)) {
                                                        block.items.add(optString2);
                                                    }
                                                }
                                            }
                                        } else {
                                            Object opt = optJSONObject2.opt("w");
                                            if (opt instanceof JSONArray) {
                                                JSONArray jSONArray = (JSONArray) opt;
                                                for (int i4 = 0; i4 < jSONArray.length() && block.items.size() < 2; i4++) {
                                                    if (jSONArray.optString(i4).matches("[a-z0-9:]+")) {
                                                        block.items.add(jSONArray.optString(i4));
                                                    }
                                                }
                                            } else if ((opt instanceof String) && ((String) opt).matches("[a-z0-9:]+")) {
                                                block.items.add((String) opt);
                                            }
                                        }
                                        page.blocks.add(block);
                                    }
                                }
                            }
                        }
                        cfg.pages.add(page);
                    }
                }
            }
            cfg.def = Math.max(0, Math.min(cfg.pages.size(), jSONObject.optInt("def", 0)));
        } catch (Exception e) {
        }
        return cfg;
    }

    public static String save(Cfg cfg) {
        try {
            JSONArray jSONArray = new JSONArray();
            Iterator<Page> it = cfg.pages.iterator();
            while (it.hasNext()) {
                Page next = it.next();
                JSONArray jSONArray2 = new JSONArray();
                Iterator<Block> it2 = next.blocks.iterator();
                while (it2.hasNext()) {
                    Block next2 = it2.next();
                    JSONObject put = new JSONObject().put("t", next2.type);
                    if ("grid".equals(next2.type)) {
                        put.put("a", new JSONArray((Collection) next2.items));
                    } else if ("half".equals(next2.type)) {
                        put.put("w", new JSONArray((Collection) next2.items));
                    } else {
                        put.put("w", next2.items.isEmpty() ? "" : next2.items.get(0));
                    }
                    jSONArray2.put(put);
                }
                jSONArray.put(new JSONObject().put("n", next.name).put("b", jSONArray2));
            }
            return new JSONObject().put("def", cfg.def).put("pages", jSONArray).toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    public static int count(Cfg cfg) {
        return cfg.pages.size() + 1;
    }

    public static boolean add(Cfg cfg, String str) {
        if (cfg.pages.size() >= 5) {
            return false;
        }
        cfg.pages.add(new Page(str));
        return true;
    }

    public static boolean remove(Cfg cfg, int i) {
        if (i < 1 || i > cfg.pages.size()) {
            return false;
        }
        cfg.pages.remove(i - 1);
        if (cfg.def == i) {
            cfg.def = 0;
        } else if (cfg.def > i) {
            cfg.def--;
        }
        return true;
    }

    public static boolean move(Cfg cfg, int i, int i2) {
        int i3;
        if (i < 1 || i2 < 1 || i > cfg.pages.size() || i2 > cfg.pages.size() || i == i2) {
            return false;
        }
        cfg.pages.add(i2 - 1, cfg.pages.remove(i - 1));
        if (cfg.def == i) {
            cfg.def = i2;
        } else {
            if (i < cfg.def && i2 >= cfg.def) {
                i3 = cfg.def - 1;
            } else if (i > cfg.def && i2 <= cfg.def && cfg.def > 0) {
                i3 = cfg.def + 1;
            }
            cfg.def = i3;
        }
        return true;
    }

    public static boolean rename(Cfg cfg, int i, String str) {
        if (i < 1 || i > cfg.pages.size()) {
            return false;
        }
        cfg.pages.get(i - 1).name = name(str);
        return true;
    }

    public static boolean setDefault(Cfg cfg, int i) {
        if (i < 0 || i > cfg.pages.size()) {
            return false;
        }
        cfg.def = i;
        return true;
    }

    public static boolean addBlock(Page page, Block block) {
        if (page.blocks.size() >= 6) {
            return false;
        }
        page.blocks.add(block);
        return true;
    }

    public static int next(int i, int i2, int i3) {
        return Math.max(0, Math.min(i3 - 1, i + i2));
    }

    public static int[] grid(int i) {
        return new int[]{4, (Math.max(4, Math.min(24, i)) + 3) / 4};
    }
}
