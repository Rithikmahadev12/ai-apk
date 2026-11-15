package com.jarvis.assist.aosui.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: /tmp/augmaos/classes.dex */
public final class HomeSlots {
    public static final String[] AREAS = {"qs", "qs2", "wheel", "dock"};
    public static final String[] SHORTCUTS = {"aos:ask", "aos:search", "aos:brief", "aos:vault", "aos:settings", "aos:drawer"};
    public static final String[] DEF_QS2 = {"location", "hotspot", "airplane", "saver", "nfc", "dark", "bright"};

    private HomeSlots() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int min(String str) {
        char c;
        switch (str.hashCode()) {
            case 3618:
                if (str.equals("qs")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 112208:
                if (str.equals("qs2")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 113097563:
                if (str.equals("wheel")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 6;
            case 1:
                return 5;
            case 2:
                return 4;
            default:
                return 3;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int max(String str) {
        char c;
        switch (str.hashCode()) {
            case 3618:
                if (str.equals("qs")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 112208:
                if (str.equals("qs2")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 113097563:
                if (str.equals("wheel")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 8;
            case 1:
                return 7;
            case 2:
                return 8;
            default:
                return 5;
        }
    }

    public static int step(String str) {
        return "wheel".equals(str) ? 4 : 1;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static String route(String str) {
        char c;
        switch (str.hashCode()) {
            case 3618:
                if (str.equals("qs")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 112208:
                if (str.equals("qs2")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 113097563:
                if (str.equals("wheel")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return "customcontrol";
            case 1:
                return "customquick";
            case 2:
                return "customgrid";
            default:
                return "customdock";
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static String areaOf(String str) {
        char c;
        if (str == null) {
            str = "";
        }
        switch (str.hashCode()) {
            case -1760880356:
                if (str.equals("customquick")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1581216476:
                if (str.equals("customdock")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1581124041:
                if (str.equals("customgrid")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 304302284:
                if (str.equals("customcontrol")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return "qs";
            case 1:
                return "qs2";
            case 2:
                return "wheel";
            case 3:
                return "dock";
            default:
                return null;
        }
    }

    public static String kind(String str) {
        if (str == null) {
            return "id";
        }
        return str.startsWith("app:") ? "app" : str.startsWith("aos:") ? "aos" : "id";
    }

    public static boolean valid(String str) {
        if (str == null || str.isEmpty() || str.length() > 300) {
            return false;
        }
        return str.startsWith("app:") ? str.indexOf(47) > 4 : str.startsWith("aos:") ? Arrays.asList(SHORTCUTS).contains(str) : str.matches("[a-z0-9]+");
    }

    public static List<String> clean(String str, List<String> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            for (String str2 : list) {
                if (valid(str2) && arrayList.size() < max(str)) {
                    arrayList.add(str2);
                }
            }
        }
        return arrayList;
    }

    public static boolean canAdd(String str, List<String> list) {
        if (list.size() + step(str) > max(str)) {
            return step(str) == 1 && list.size() < max(str);
        }
        return true;
    }

    public static boolean canRemove(String str, List<String> list) {
        return list.size() > min(str);
    }

    public static boolean add(String str, List<String> list, String str2) {
        if (!valid(str2) || list.size() >= max(str)) {
            return false;
        }
        list.add(str2);
        return true;
    }

    public static boolean remove(String str, List<String> list, int i) {
        if (i < 0 || i >= list.size() || !canRemove(str, list)) {
            return false;
        }
        list.remove(i);
        return true;
    }

    public static boolean move(List<String> list, int i, int i2) {
        if (i < 0 || i >= list.size() || i2 < 0 || i2 >= list.size() || i == i2) {
            return false;
        }
        list.add(i2, list.remove(i));
        return true;
    }

    public static int gridCells(int i) {
        return i <= 4 ? 4 : 8;
    }
}
