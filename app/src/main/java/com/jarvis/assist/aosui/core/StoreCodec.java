package com.jarvis.assist.aosui.core;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: /tmp/augmaos/classes.dex */
public final class StoreCodec {
    public static final int MAX_VALUE = 524288;

    private StoreCodec() {
    }

    public static boolean keep(String str, String str2) {
        return (str == null || str2 == null || !str.startsWith("aos.") || str.length() > 120 || "aos.ic".equals(str) || "aos.native".equals(str) || str2.length() > 524288) ? false : true;
    }

    public static Map<String, String> parse(String str) {
        String trim;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (str == null) {
            return linkedHashMap;
        }
        try {
            int[] iArr = {skip(str, 0)};
            if (iArr[0] < str.length() && str.charAt(iArr[0]) == '{') {
                iArr[0] = skip(str, iArr[0] + 1);
                if (iArr[0] < str.length() && str.charAt(iArr[0]) == '}') {
                    return linkedHashMap;
                }
                while (iArr[0] < str.length()) {
                    String str2 = str(str, iArr);
                    iArr[0] = skip(str, iArr[0]);
                    if (str.charAt(iArr[0]) != ':') {
                        return new LinkedHashMap();
                    }
                    iArr[0] = skip(str, iArr[0] + 1);
                    if (str.charAt(iArr[0]) == '\"') {
                        trim = str(str, iArr);
                    } else {
                        int i = iArr[0];
                        iArr[0] = value(str, iArr[0]);
                        trim = str.substring(i, iArr[0]).trim();
                        if ("null".equals(trim)) {
                            trim = null;
                        }
                    }
                    if (keep(str2, trim)) {
                        linkedHashMap.put(str2, trim);
                    }
                    iArr[0] = skip(str, iArr[0]);
                    char charAt = str.charAt(iArr[0]);
                    if (charAt != ',') {
                        return charAt == '}' ? linkedHashMap : new LinkedHashMap();
                    }
                    iArr[0] = skip(str, iArr[0] + 1);
                }
                return new LinkedHashMap();
            }
            return linkedHashMap;
        } catch (RuntimeException e) {
            return new LinkedHashMap();
        }
    }

    static int skip(String str, int i) {
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
            i++;
        }
        return i;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:10:0x0032. Please report as an issue. */
    static String str(String str, int[] iArr) {
        char c;
        char c2;
        if (str.charAt(iArr[0]) != '\"') {
            throw new IllegalArgumentException("string expected");
        }
        StringBuilder sb = new StringBuilder();
        int i = iArr[0] + 1;
        while (true) {
            int i2 = i + 1;
            char charAt = str.charAt(i);
            if (charAt != '\"') {
                if (charAt == '\\') {
                    i = i2 + 1;
                    char charAt2 = str.charAt(i2);
                    switch (charAt2) {
                        case 'b':
                            c = '\b';
                            sb.append(c);
                            break;
                        case 'f':
                            c = '\f';
                            sb.append(c);
                            break;
                        case 'n':
                            c2 = '\n';
                            sb.append(c2);
                            break;
                        case 'r':
                            c2 = '\r';
                            sb.append(c2);
                            break;
                        case 't':
                            c2 = '\t';
                            sb.append(c2);
                            break;
                        case 'u':
                            int i3 = i + 4;
                            sb.append((char) Integer.parseInt(str.substring(i, i3), 16));
                            i = i3;
                            break;
                        default:
                            sb.append(charAt2);
                            break;
                    }
                } else {
                    sb.append(charAt);
                    i = i2;
                }
            } else {
                iArr[0] = i2;
                return sb.toString();
            }
        }
    }

    static int value(String str, int i) {
        boolean z = false;
        int i2 = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (z) {
                if (charAt == '\\') {
                    i++;
                } else if (charAt == '\"') {
                    z = false;
                }
            } else if (charAt == '\"') {
                z = true;
            } else if (charAt == '{' || charAt == '[') {
                i2++;
            } else if (charAt == '}' || charAt == ']') {
                if (i2 == 0) {
                    return i;
                }
                i2--;
            } else if (charAt == ',' && i2 == 0) {
                return i;
            }
            i++;
        }
        return i;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x001c. Please report as an issue. */
    public static String quote(String str) {
        String str2;
        String str3;
        String str4;
        String str5;
        StringBuilder append = new StringBuilder(str.length() + 8).append('\"');
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case '\t':
                    str2 = "\\t";
                    append.append(str2);
                    break;
                case '\n':
                    str2 = "\\n";
                    append.append(str2);
                    break;
                case FxModel.BOOT /* 13 */:
                    str2 = "\\r";
                    append.append(str2);
                    break;
                case '\"':
                    str3 = "\\\"";
                    append.append(str3);
                    break;
                case '<':
                    str4 = "\\u003c";
                    append.append(str4);
                    break;
                case '>':
                    str4 = "\\u003e";
                    append.append(str4);
                    break;
                case '\\':
                    str3 = "\\\\";
                    append.append(str3);
                    break;
                case 8232:
                    str5 = "\\u2028";
                    append.append(str5);
                    break;
                case 8233:
                    str5 = "\\u2029";
                    append.append(str5);
                    break;
                default:
                    if (charAt < ' ') {
                        append.append(String.format("\\u%04x", Integer.valueOf(charAt)));
                        break;
                    } else {
                        append.append(charAt);
                        break;
                    }
            }
        }
        return append.append('\"').toString();
    }

    public static String dump(Map<String, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean z = true;
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if ((entry.getValue() instanceof String) && keep(entry.getKey(), (String) entry.getValue())) {
                if (!z) {
                    sb.append(',');
                }
                sb.append(quote(entry.getKey())).append(':').append(quote((String) entry.getValue()));
                z = false;
            }
        }
        return sb.append('}').toString();
    }
}
