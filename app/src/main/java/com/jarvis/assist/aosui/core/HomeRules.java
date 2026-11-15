package com.jarvis.assist.aosui.core;

import com.jarvis.assist.aosui.HealthSources$$ExternalSyntheticLambda5;
import com.jarvis.assist.aosui.core.HomeRules;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;

/* loaded from: /tmp/augmaos/classes.dex */
public final class HomeRules {
    static final Set<String> PKG_NOISE;
    public static final String[] DRAWER_ORDER = {"apps", "aos", "contacts", "web"};
    static final Pattern IMP = Pattern.compile("\\b(urgent|asap|now|otp|code|verification|verify|security|alert|fraud|failed|overdue|due today|cancelled|canceled|delayed|boarding|gate|emergency|doctor|hospital|mum|mom|dad|wife|husband|boss)\\b|\\?$", 2);
    static final Pattern NOISE = Pattern.compile("\\b(sale|offer|discount|% off|deal|promo|coupon|free|win|reward|points|newsletter|unsubscribe|recommended for you|you might like|trending|new video|went live|liked your|followed you|reacted|streak|weekly summary|rate (the|this|our) app)\\b", 2);
    static final Pattern NOISE_PKG = Pattern.compile("\\b(promo|marketing|offers?)\\b", 2);
    static final Pattern SCAM = Pattern.compile("(verify|confirm|update|suspend|locked|unusual|urgent|prize|won|refund|parcel|package|delivery fee|customs|tax|unpaid|pay now|click|link|login|password|account)", 2);
    static final Pattern SHORTURL = Pattern.compile("\\b(bit\\.ly|tinyurl|t\\.co|goo\\.gl|cutt\\.ly|rb\\.gy|is\\.gd|[a-z0-9-]+\\.(xyz|top|icu|club|buzz|tk|ml|ga|cf|gq))\\b", 2);
    static final Pattern URL = Pattern.compile("https?://|www\\.", 2);
    static final Pattern SMS = Pattern.compile("sms|messag|mms|whatsapp|telegram|line\\b", 2);
    static final Pattern SIX = Pattern.compile("\\b\\d{6}\\b");
    static final Pattern OTP = Pattern.compile("code|otp", 2);
    public static final String[] DEF_WIDGETS = {"greeting", "health", "weather", "calendar", "recent", "media"};
    static final Collator COL = Collator.getInstance();

    private HomeRules() {
    }

    static {
        COL.setStrength(1);
        PKG_NOISE = new HashSet(Arrays.asList("com", "org", "net", "io", "co", "de", "uk", "android", "google", "apps", "app", "sec", "samsung", "mobile", "client", "lite", "release", "prod", "free", "pro"));
    }

    public static boolean nameMatch(String str, String str2) {
        String trim = str == null ? "" : str.toLowerCase(Locale.ROOT).trim();
        String trim2 = str2 != null ? str2.toLowerCase(Locale.ROOT).trim() : "";
        if (trim2.isEmpty() || trim.isEmpty()) {
            return false;
        }
        if (trim.startsWith(trim2)) {
            return true;
        }
        for (String str3 : trim.split("[\\s\\-.,'()]+")) {
            if (!str3.isEmpty() && str3.startsWith(trim2)) {
                return true;
            }
        }
        return false;
    }

    public static int dayOf(long j, long j2) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTimeInMillis(j2);
        calendar2.setTimeInMillis(j);
        Calendar[] calendarArr = {calendar, calendar2};
        for (int i = 0; i < 2; i++) {
            Calendar calendar3 = calendarArr[i];
            calendar3.set(11, 12);
            calendar3.set(12, 0);
            calendar3.set(13, 0);
            calendar3.set(14, 0);
        }
        return (int) Math.round((calendar2.getTimeInMillis() - calendar.getTimeInMillis()) / 8.64E7d);
    }

    public static int calDay(long j, long j2, long j3) {
        if (j > j3 || j2 <= j3) {
            return Math.max(0, dayOf(j, j3));
        }
        return 0;
    }

    public static String calTime(long j, long j2, boolean z, long j3, String str) {
        if (z) {
            return str;
        }
        if (j <= j3 && j2 > j3) {
            return "NOW";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i = calendar.get(11);
        int i2 = calendar.get(12);
        return (i < 10 ? "0" : "") + i + ":" + (i2 >= 10 ? "" : "0") + i2;
    }

    public static boolean onlyTomorrow(int[] iArr) {
        if (iArr.length == 0) {
            return false;
        }
        for (int i : iArr) {
            if (i == 0) {
                return false;
            }
        }
        return true;
    }

    public static String classify(String str, String str2, String str3) {
        String str4 = nz(str) + " " + nz(str2);
        if (IMP.matcher(str4).find()) {
            return "important";
        }
        if (NOISE.matcher(str4).find() || NOISE_PKG.matcher(nz(str3)).find()) {
            return "noise";
        }
        return "normal";
    }

    public static boolean scam(String str, String str2, String str3) {
        String str4 = nz(str) + " " + nz(str2);
        int i = 0;
        while (SCAM.matcher(str4).find()) {
            i++;
        }
        boolean find = URL.matcher(str4).find();
        boolean find2 = SMS.matcher(nz(str3)).find();
        boolean z = SIX.matcher(str4).find() && OTP.matcher(str4).find();
        if (!find2 || z) {
            return false;
        }
        return (find && i >= 1) || i >= 3 || (find && SHORTURL.matcher(str4).find());
    }

    public static String tag(String str, String str2, String str3, boolean z, boolean z2) {
        return (z && scam(str, str2, str3)) ? "scam" : z2 ? classify(str, str2, str3) : "";
    }

    public static int tagRank(String str) {
        if ("scam".equals(str) || "important".equals(str)) {
            return 0;
        }
        return "noise".equals(str) ? 2 : 1;
    }

    public static List<String> suggest(Map<String, Map<String, Integer>> map, Map<String, Integer> map2, Collection<String> collection, int i, int i2, List<String> list) {
        LinkedHashSet linkedHashSet = new LinkedHashSet(collection);
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            for (String str : list) {
                if (linkedHashSet.contains(str) && !arrayList.contains(str) && arrayList.size() < i2) {
                    arrayList.add(str);
                }
            }
            if (arrayList.size() >= Math.min(i2, 2)) {
                return arrayList;
            }
            arrayList.clear();
        }
        final HashMap hashMap = new HashMap();
        int[] iArr = {i, (i + 23) % 24, (i + 1) % 24};
        int i3 = 0;
        while (i3 < 3) {
            Map<String, Integer> map3 = map == null ? null : map.get(String.valueOf(iArr[i3]));
            if (map3 != null) {
                Iterator<Map.Entry<String, Integer>> it = map3.entrySet().iterator();
                while (it.hasNext()) {
                    hashMap.merge(it.next().getKey(), Double.valueOf(r3.getValue().intValue() * (i3 == 0 ? 2.0d : 1.0d)), new HealthSources$$ExternalSyntheticLambda5());
                }
            }
            i3++;
        }
        if (map2 != null) {
            Iterator<Map.Entry<String, Integer>> it2 = map2.entrySet().iterator();
            while (it2.hasNext()) {
                hashMap.merge(it2.next().getKey(), Double.valueOf(r10.getValue().intValue() * 0.2d), new HealthSources$$ExternalSyntheticLambda5());
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (String str2 : hashMap.keySet()) {
            if (linkedHashSet.contains(str2)) {
                arrayList2.add(str2);
            }
        }
        arrayList2.sort(new Comparator() { // from class: com.jarvis.assist.aosui.core.HomeRules$$ExternalSyntheticLambda4
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return HomeRules.lambda$suggest$0(hashMap, (String) obj, (String) obj2);
            }
        });
        return new ArrayList(arrayList2.subList(0, Math.min(i2, arrayList2.size())));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$suggest$0(HashMap hashMap, String str, String str2) {
        int compare = Double.compare(((Double) hashMap.get(str2)).doubleValue(), ((Double) hashMap.get(str)).doubleValue());
        return compare != 0 ? compare : str.compareTo(str2);
    }

    public static List<String> localOrder(List<String> list, int i, List<String> list2) {
        String[] strArr = i < 11 ? new String[]{"weather", "calendar", "health", "alarms", "notifs"} : i < 17 ? new String[]{"calendar", "notifs", "media", "weather", "recent"} : new String[]{"sleep", "alarms", "calendar", "media", "battery"};
        final ArrayList arrayList = new ArrayList();
        if (list2 != null) {
            arrayList.addAll(list2);
        }
        for (String str : strArr) {
            arrayList.add(str);
        }
        ArrayList arrayList2 = new ArrayList(list);
        arrayList2.sort(Comparator.comparingInt(new ToIntFunction() { // from class: com.jarvis.assist.aosui.core.HomeRules$$ExternalSyntheticLambda5
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return HomeRules.lambda$localOrder$1(arrayList, (String) obj);
            }
        }));
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$localOrder$1(ArrayList arrayList, String str) {
        int indexOf = arrayList.indexOf(str);
        if (indexOf < 0) {
            return 99;
        }
        return indexOf;
    }

    public static List<String> widgetOrder(List<String> list, Collection<String> collection, Predicate<String> predicate, boolean z, int i, List<String> list2) {
        ArrayList arrayList = new ArrayList();
        if (list == null) {
            list = Arrays.asList(DEF_WIDGETS);
        }
        for (String str : list) {
            if (str != null && !"greeting".equals(str) && (collection == null || !collection.contains(str))) {
                if (!arrayList.contains(str) && predicate.test(str)) {
                    arrayList.add(str);
                }
            }
        }
        return z ? localOrder(arrayList, i, list2) : arrayList;
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class App {
        public final String cls;
        public final String label;
        public final String low;
        public final String pkg;

        public App(String str, String str2, String str3) {
            this.pkg = str;
            this.cls = str2;
            this.label = str3 == null ? "" : str3;
            this.low = this.label.toLowerCase(Locale.ROOT);
        }
    }

    public static void sort(List<App> list, String str, final Map<String, Integer> map, final List<String> list2) {
        final Comparator<? super App> comparator = new Comparator() { // from class: com.jarvis.assist.aosui.core.HomeRules$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = HomeRules.COL.compare(((HomeRules.App) obj).label, ((HomeRules.App) obj2).label);
                return compare;
            }
        };
        if (!"use".equals(str)) {
            if (!"recent".equals(str)) {
                list.sort(comparator);
                return;
            } else {
                list.sort(new Comparator() { // from class: com.jarvis.assist.aosui.core.HomeRules$$ExternalSyntheticLambda3
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        return HomeRules.lambda$sort$4(list2, comparator, (HomeRules.App) obj, (HomeRules.App) obj2);
                    }
                });
                return;
            }
        }
        list.sort(new Comparator() { // from class: com.jarvis.assist.aosui.core.HomeRules$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return HomeRules.lambda$sort$3(map, comparator, (HomeRules.App) obj, (HomeRules.App) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$sort$3(Map map, Comparator comparator, App app, App app2) {
        int compare = Integer.compare(get(map, app2.pkg), get(map, app.pkg));
        return compare != 0 ? compare : comparator.compare(app, app2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$sort$4(List list, Comparator comparator, App app, App app2) {
        int indexOf = list == null ? -1 : list.indexOf(app.pkg);
        int indexOf2 = list != null ? list.indexOf(app2.pkg) : -1;
        if (indexOf < 0) {
            indexOf = Integer.MAX_VALUE;
        }
        if (indexOf2 < 0) {
            indexOf2 = Integer.MAX_VALUE;
        }
        int compare = Integer.compare(indexOf, indexOf2);
        return compare != 0 ? compare : comparator.compare(app, app2);
    }

    public static int score(String str, String str2) {
        boolean z;
        if (str2.isEmpty() || str.startsWith(str2)) {
            return 0;
        }
        int indexOf = str.indexOf(str2);
        while (true) {
            if (indexOf <= 0) {
                z = false;
                break;
            }
            if (!Character.isLetterOrDigit(str.charAt(indexOf - 1))) {
                z = true;
                break;
            }
            indexOf = str.indexOf(str2, indexOf + 1);
        }
        if (z) {
            return 1;
        }
        if (str2.length() > 1 && str2.indexOf(32) < 0) {
            StringBuilder sb = new StringBuilder();
            boolean z2 = true;
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if (Character.isLetterOrDigit(charAt)) {
                    if (z2) {
                        sb.append(charAt);
                    }
                    z2 = false;
                } else {
                    z2 = true;
                }
            }
            if (sb.length() > 1 && sb.toString().startsWith(str2)) {
                return 2;
            }
        }
        return str.contains(str2) ? 3 : -1;
    }

    public static List<App> search(List<App> list, String str) {
        String lowerCase = nz(str).trim().toLowerCase(Locale.ROOT);
        if (lowerCase.isEmpty()) {
            return new ArrayList(list);
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            int score = score(list.get(i).low, lowerCase);
            if (score >= 0) {
                arrayList.add(new int[]{score, i});
            }
        }
        arrayList.sort(new Comparator() { // from class: com.jarvis.assist.aosui.core.HomeRules$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return HomeRules.lambda$search$5((int[]) obj, (int[]) obj2);
            }
        });
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(list.get(((int[]) it.next())[1]));
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$search$5(int[] iArr, int[] iArr2) {
        int i;
        int i2;
        if (iArr[0] != iArr2[0]) {
            i = iArr[0];
            i2 = iArr2[0];
        } else {
            i = iArr[1];
            i2 = iArr2[1];
        }
        return Integer.compare(i, i2);
    }

    public static List<Integer> settingsMatch(String[] strArr, String str) {
        String lowerCase = nz(str).trim().toLowerCase(Locale.ROOT);
        ArrayList arrayList = new ArrayList();
        if (lowerCase.isEmpty()) {
            return arrayList;
        }
        for (int i = 0; i < strArr.length && arrayList.size() < 3; i++) {
            if (strArr[i].toLowerCase(Locale.ROOT).contains(lowerCase)) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        return arrayList;
    }

    public static boolean handoff(boolean z, String str, String str2) {
        return (!z || "0".equals(str) || str2 == null || str2.isEmpty()) ? false : true;
    }

    public static String paLineKey(boolean z, String str, String str2) {
        if (!z) {
            return "set.pa.mode.ai";
        }
        if (str2 == null || str2.isEmpty()) {
            return "set.pa.mode.none";
        }
        return "0".equals(str) ? "set.pa.mode.off" : "set.pa.mode.phone";
    }

    public static String askName(String str) {
        return UnitsMath.assistantName(str);
    }

    public static String initials(String str) {
        StringBuilder sb = new StringBuilder();
        for (String str2 : nz(str).trim().split("\\s+")) {
            if (!str2.isEmpty() && sb.length() < 2) {
                sb.append(str2.charAt(0));
            }
        }
        String upperCase = sb.toString().toUpperCase(Locale.ROOT);
        return upperCase.isEmpty() ? "?" : upperCase;
    }

    public static String appLabel(String str, String str2) {
        String str3 = "";
        String trim = str2 == null ? "" : str2.trim();
        String trim2 = str == null ? "" : str.trim();
        if (!trim.isEmpty() && !trim.equals(trim2) && !trim.matches("^[a-z0-9_]+(\\.[a-z0-9_]+)+$")) {
            return trim;
        }
        if (!trim2.isEmpty()) {
            trim = trim2;
        }
        if (trim.isEmpty()) {
            return "";
        }
        String[] split = trim.split("\\.");
        int length = split.length - 1;
        while (true) {
            if (length >= 0) {
                if (!split[length].isEmpty() && !PKG_NOISE.contains(split[length].toLowerCase(Locale.ROOT))) {
                    str3 = split[length];
                    break;
                }
                length--;
            } else {
                break;
            }
        }
        if (str3.isEmpty()) {
            str3 = split[split.length - 1];
        }
        String replace = str3.replace('_', ' ');
        if (replace.isEmpty()) {
            return trim;
        }
        return Character.toUpperCase(replace.charAt(0)) + replace.substring(1);
    }

    public static String dur(long j) {
        StringBuilder append;
        long round = Math.round(j / 60000.0d);
        if (round < 60) {
            append = new StringBuilder();
        } else {
            long j2 = round / 60;
            round %= 60;
            append = new StringBuilder().append(j2).append("H ").append(round < 10 ? "0" : "");
        }
        return append.append(round).append("M").toString();
    }

    static int get(Map<String, Integer> map, String str) {
        Integer num = map == null ? null : map.get(str);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    static String nz(String str) {
        return str == null ? "" : str;
    }
}
