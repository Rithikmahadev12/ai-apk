package com.jarvis.assist.aosui.core;

import android.graphics.Path;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Svg {
    static final Pattern TOK = Pattern.compile("[MmLlHhVvQqCcSsAaZz]|-?\\d*\\.?\\d+(?:e-?\\d+)?");

    private Svg() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Path path(String str) {
        char c;
        boolean isLowerCase;
        float f;
        float f2;
        float f3;
        boolean z;
        Path path = new Path();
        Matcher matcher = TOK.matcher(str);
        ArrayList arrayList = new ArrayList();
        while (matcher.find()) {
            arrayList.add(matcher.group());
        }
        int i = 0;
        char c2 = 'M';
        int i2 = 0;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        while (i2 < arrayList.size()) {
            String str2 = (String) arrayList.get(i2);
            try {
                if (Character.isLetter(str2.charAt(i))) {
                    c2 = str2.charAt(i);
                    i2++;
                    if (c2 == 'Z' || c2 == 'z') {
                        path.close();
                        f4 = f6;
                        f5 = f7;
                    }
                }
                switch (Character.toUpperCase(c)) {
                    case 'A':
                        int i3 = i2 + 1;
                        float f8 = f4;
                        float f9 = f(arrayList, i2);
                        int i4 = i3 + 1;
                        float f10 = f(arrayList, i3);
                        int i5 = i4 + 1;
                        float f11 = f(arrayList, i4);
                        int i6 = i5 + 1;
                        boolean z2 = true;
                        if (f(arrayList, i5) != 0.0f) {
                            f3 = f2;
                            z = 1;
                        } else {
                            f3 = f2;
                            z = i;
                        }
                        int i7 = i6 + 1;
                        if (f(arrayList, i6) == 0.0f) {
                            z2 = false;
                        }
                        int i8 = i7 + 1;
                        float f12 = f + f(arrayList, i7);
                        int i9 = i8 + 1;
                        float f13 = f(arrayList, i8) + f3;
                        arc(path, f8, f5, f9, f10, f11, z, z2, f12, f13);
                        i2 = i9;
                        f4 = f12;
                        f5 = f13;
                        c2 = c;
                        break;
                    case 'C':
                        int i10 = i2 + 1;
                        float f14 = f(arrayList, i2) + f;
                        int i11 = i10 + 1;
                        float f15 = f(arrayList, i10) + f2;
                        int i12 = i11 + 1;
                        float f16 = f(arrayList, i11) + f;
                        int i13 = i12 + 1;
                        float f17 = f(arrayList, i12) + f2;
                        int i14 = i13 + 1;
                        float f18 = f + f(arrayList, i13);
                        float f19 = f2 + f(arrayList, i14);
                        path.cubicTo(f14, f15, f16, f17, f18, f19);
                        f4 = f18;
                        f5 = f19;
                        i2 = i14 + 1;
                        c2 = c;
                        break;
                    case 'H':
                        f4 = f + f(arrayList, i2);
                        path.lineTo(f4, f5);
                        i2++;
                        c2 = c;
                        break;
                    case 'L':
                        int i15 = i2 + 1;
                        f4 = f + f(arrayList, i2);
                        i2 = i15 + 1;
                        float f20 = f2 + f(arrayList, i15);
                        path.lineTo(f4, f20);
                        f5 = f20;
                        c2 = c;
                        break;
                    case 'M':
                        int i16 = i2 + 1;
                        float f21 = f(arrayList, i2) + f;
                        int i17 = i16 + 1;
                        float f22 = f2 + f(arrayList, i16);
                        path.moveTo(f21, f22);
                        c2 = isLowerCase ? 'l' : 'L';
                        f4 = f21;
                        f6 = f4;
                        i2 = i17;
                        f5 = f22;
                        f7 = f5;
                        break;
                    case 'Q':
                        int i18 = i2 + 1;
                        float f23 = f(arrayList, i2) + f;
                        int i19 = i18 + 1;
                        float f24 = f(arrayList, i18) + f2;
                        int i20 = i19 + 1;
                        f4 = f(arrayList, i19) + f;
                        float f25 = f2 + f(arrayList, i20);
                        path.quadTo(f23, f24, f4, f25);
                        i2 = i20 + 1;
                        f5 = f25;
                        c2 = c;
                        break;
                    case 'S':
                        int i21 = i2 + 1;
                        float f26 = f(arrayList, i2) + f;
                        int i22 = i21 + 1;
                        float f27 = f(arrayList, i21) + f2;
                        int i23 = i22 + 1;
                        f4 = f(arrayList, i22) + f;
                        float f28 = f2 + f(arrayList, i23);
                        path.quadTo(f26, f27, f4, f28);
                        i2 = i23 + 1;
                        f5 = f28;
                        c2 = c;
                        break;
                    case 'V':
                        if (!isLowerCase) {
                            f5 = 0.0f;
                        }
                        f5 += f(arrayList, i2);
                        path.lineTo(f4, f5);
                        i2++;
                        c2 = c;
                        break;
                    default:
                        i2++;
                        c2 = c;
                        break;
                }
                i = 0;
            } catch (Exception e) {
            }
            c = c2;
            isLowerCase = Character.isLowerCase(c);
            f = isLowerCase ? f4 : 0.0f;
            f2 = isLowerCase ? f5 : 0.0f;
        }
        return path;
    }

    static float f(ArrayList<String> arrayList, int i) {
        return Float.parseFloat(arrayList.get(i));
    }

    static void arc(Path path, float f, float f2, float f3, float f4, float f5, boolean z, boolean z2, float f6, float f7) {
        if (f3 == 0.0f || f4 == 0.0f) {
            path.lineTo(f6, f7);
            return;
        }
        if (f == f6 && f2 == f7) {
            return;
        }
        double d = (f - f6) / 2.0d;
        double d2 = (f2 - f7) / 2.0d;
        double max = Math.max(f3, f4);
        double d3 = d * d;
        double d4 = d2 * d2;
        double d5 = d3 + d4;
        double d6 = d5 / (max * max);
        if (d6 > 1.0d) {
            max *= Math.sqrt(d6);
        }
        double sqrt = (z == z2 ? -1 : 1) * Math.sqrt(Math.max(0.0d, (((max * max) - d3) - d4) / d5));
        double d7 = (d2 * sqrt) + ((f + f6) / 2.0d);
        double d8 = ((-sqrt) * d) + ((f2 + f7) / 2.0d);
        double degrees = Math.toDegrees(Math.atan2(f2 - d8, f - d7));
        double degrees2 = Math.toDegrees(Math.atan2(f7 - d8, f6 - d7)) - degrees;
        if (z2 && degrees2 < 0.0d) {
            degrees2 += 360.0d;
        }
        if (!z2 && degrees2 > 0.0d) {
            degrees2 -= 360.0d;
        }
        path.arcTo(new RectF((float) (d7 - max), (float) (d8 - max), (float) (d7 + max), (float) (d8 + max)), (float) degrees, (float) degrees2);
    }
}
