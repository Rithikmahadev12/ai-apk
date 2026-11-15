package com.jarvis.assist.aosui.core;

import com.jarvis.assist.aosui.core.AoaPages;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaPages {
    static final String AD = "android.";
    public static final String[] ORDER = {"cal", "clock", "home", "music", "msg"};
    public static final Set<String> MSG_APPS = new HashSet(Arrays.asList("com.google.android.apps.messaging", "com.samsung.android.messaging", "com.android.mms", "jp.naver.line.android", "com.whatsapp", "com.whatsapp.w4b", "com.facebook.orca", "com.facebook.mlite", "org.telegram.messenger", "org.thoughtcrime.securesms", "com.discord", "com.Slack", "com.viber.voip", "com.kakao.talk", "com.tencent.mm", "com.instagram.android", "com.google.android.apps.dynamite", "com.microsoft.teams", "com.skype.raider", "com.snapchat.android", "com.augma.launcher"));
    static final Pattern PHONE = Pattern.compile("^[+()\\d][\\d\\s().-]{5,}$");
    static final Pattern PERSON = Pattern.compile("^\\p{L}[\\p{L}' -]{0,40}$");
    static final Pattern STATUS = Pattern.compile("(?i)listening|running|connected|syncing|\\bsync|charging|update|download|backup|battery|screenshot|usb|k\\.a\\.r\\.a|kara|is on\\b|active|is down|is up\\b|failed|error|alert|warning|check|offline|online|monitor|\\S+\\.\\S+");

    private AoaPages() {
    }

    public static String next(String str, int i) {
        int i2;
        int indexOf = Arrays.asList(ORDER).indexOf(str);
        if (indexOf >= 0 && (i2 = indexOf + i) >= 0 && i2 < ORDER.length) {
            return ORDER[i2];
        }
        return null;
    }

    public static int[] month(int i, int i2, int i3) {
        int[] iArr = new int[42];
        int i4 = (((i2 - i3) % 7) + 7) % 7;
        for (int i5 = 1; i5 <= i; i5++) {
            iArr[(i4 + i5) - 1] = i5;
        }
        return iArr;
    }

    public static int rows(int[] iArr) {
        int i = 0;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] > 0) {
                i = i2;
            }
        }
        return (i / 7) + 1;
    }

    public static float turn(float f, float f2) {
        float f3 = (f2 - f) % 360.0f;
        if (f3 > 180.0f) {
            f3 -= 360.0f;
        }
        return f3 <= -180.0f ? f3 + 360.0f : f3;
    }

    public static int clicks(float f, float f2) {
        return (int) (f / f2);
    }

    public static int volume(int i, int i2, int i3) {
        return Math.max(0, Math.min(i3, i + i2));
    }

    public static String zone(float f) {
        float f2 = ((f % 360.0f) + 360.0f) % 360.0f;
        return (f2 < 225.0f || f2 >= 315.0f) ? (f2 >= 315.0f || f2 < 45.0f) ? "next" : f2 < 135.0f ? "play" : "prev" : "menu";
    }

    public static String timer(long j) {
        long max = (Math.max(0L, j) + 999) / 1000;
        long j2 = max / 3600;
        long j3 = (max % 3600) / 60;
        long j4 = max % 60;
        return j2 > 0 ? String.format(Locale.ROOT, "%d:%02d:%02d", Long.valueOf(j2), Long.valueOf(j3), Long.valueOf(j4)) : String.format(Locale.ROOT, "%d:%02d", Long.valueOf(j3), Long.valueOf(j4));
    }

    public static String stopwatch(long j) {
        long max = Math.max(0L, j);
        long j2 = (max / 10) % 100;
        long j3 = max / 1000;
        long j4 = j3 / 3600;
        return (j4 > 0 ? j4 + ":" : "") + String.format(Locale.ROOT, "%02d:%02d.%02d", Long.valueOf((j3 % 3600) / 60), Long.valueOf(j3 % 60), Long.valueOf(j2));
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Conv {
        public final String app;
        public long at;
        public final List<String[]> items = new ArrayList();
        public final String pkg;
        public final String who;

        Conv(String str, String str2, String str3) {
            this.pkg = str;
            this.app = str2;
            this.who = str3;
        }
    }

    public static List<Conv> group(List<String[]> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (String[] strArr : list) {
            String trim = strArr[2].trim().isEmpty() ? strArr[1] : strArr[2].trim();
            String str = strArr[0] + "|" + trim.toLowerCase(Locale.ROOT);
            Conv conv = (Conv) linkedHashMap.get(str);
            if (conv == null) {
                conv = new Conv(strArr[0], strArr[1], trim);
                linkedHashMap.put(str, conv);
            }
            long parse = parse(strArr[4]);
            conv.items.add(new String[]{strArr[5], strArr[3], strArr[4]});
            conv.at = Math.max(conv.at, parse);
        }
        ArrayList arrayList = new ArrayList(linkedHashMap.values());
        arrayList.sort(new Comparator() { // from class: com.jarvis.assist.aosui.core.AoaPages$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = Long.compare(((AoaPages.Conv) obj2).at, ((AoaPages.Conv) obj).at);
                return compare;
            }
        });
        return arrayList;
    }

    static long parse(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0L;
        }
    }

    public static String initials(String str) {
        String[] split = (str == null ? "" : str.trim()).split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String str2 : split) {
            if (!str2.isEmpty()) {
                sb.appendCodePoint(str2.codePointAt(0));
                if (sb.length() >= 2) {
                    break;
                }
            }
        }
        return sb.toString().toUpperCase(Locale.ROOT);
    }

    public static boolean conversation(String str, String str2, boolean z, String str3, String str4, String str5) {
        String trim = str3 == null ? "" : str3.trim();
        if (!"com.augma.launcher".equals(str)) {
            if ("msg".equals(str2) || z || MSG_APPS.contains(str)) {
                return true;
            }
            return str != null && str.equals(str5);
        }
        if (!STATUS.matcher(trim).find()) {
            Pattern pattern = STATUS;
            if (str4 == null) {
                str4 = "";
            }
            if ((!pattern.matcher(str4).find() || PHONE.matcher(trim).matches()) && !"call".equals(str2) && !"service".equals(str2) && !"status".equals(str2) && !"progress".equals(str2) && !"err".equals(str2) && !"sys".equals(str2)) {
                if (PHONE.matcher(trim).matches()) {
                    return true;
                }
                return PERSON.matcher(trim).matches() && trim.split("\\s+").length <= 4;
            }
        }
        return false;
    }

    public static String when(long j, long j2) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar2.setTimeInMillis(j2);
        int i = (((calendar2.get(1) - calendar.get(1)) * 366) + calendar2.get(6)) - calendar.get(6);
        return i <= 0 ? String.format(Locale.ROOT, "%02d:%02d", Integer.valueOf(calendar.get(11)), Integer.valueOf(calendar.get(12))) : i == 1 ? "YDAY" : i < 7 ? calendar.getDisplayName(7, 1, Locale.ENGLISH).toUpperCase(Locale.ROOT) : String.format(Locale.ROOT, "%02d %s", Integer.valueOf(calendar.get(5)), calendar.getDisplayName(2, 1, Locale.ENGLISH).toUpperCase(Locale.ROOT));
    }
}
