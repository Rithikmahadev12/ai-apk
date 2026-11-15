package com.jarvis.assist.aosui.core;

import android.os.Build;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Theme {
    public static final int AOA_ORANGE = -34278;
    public static final int AOA_TEAL = -11624262;
    public static final int HM_HR = -2078885;
    public static final int HM_HRV = -6302731;
    public static final int HM_RHR = -10835492;
    public static final int HM_STEPS = -1774607;
    public static final int SC_CHARGE = -10835492;
    public static final int SC_CLOUD = -6641998;
    public static final int SC_CRIT = -3665874;
    public static final int SC_FOG = -8549994;
    public static final int SC_LOW = -2078885;
    public static final int SC_MOON = -5654273;
    public static final int SC_PARTLY = -6502426;
    public static final int SC_RAIN = -11623425;
    public static final int SC_SNOW = -6297601;
    public static final int SC_STORM = -6259457;
    public static final int SC_SUN = -34235;
    public int ACCENT_RAW;
    public int ACT;
    public int BG;
    public int BLUE;
    public int CARD;
    public int DIM;
    public int FRAME;
    public int GLOW;
    public int GRID;
    public int INK;
    public int INK_RAW;
    public int LINE;
    public int LINE2;
    public int RED;
    public int SEG;
    public boolean aoa;
    public float blurPx;
    public boolean light;
    public int tint;
    public int fWHITE = -1774607;
    public int fNAME = Flip7.NAME;
    public int fG1 = -6641998;
    public int fG2 = -8549994;
    public int fG3 = Flip7.G3;
    public int fG4 = Flip7.G4;
    public int fG5 = Flip7.G5;
    public int fG6 = Flip7.G6;
    public int fG7 = Flip7.G7;
    public int fRED = -3665874;
    public int fRED_HI = Flip7.RED_HI;
    public int fRED_DEEP = Flip7.RED_DEEP;
    public int fBLUE = -10835492;
    public int fBLUE_HI = Flip7.BLUE_HI;
    public int fBG = Flip7.BG;

    public static int a(int i, float f) {
        return (i & 16777215) | (Math.round(Math.max(0.0f, Math.min(1.0f, f)) * 255.0f) << 24);
    }

    public static int rgba(int i, int i2, int i3, float f) {
        return a((i << 16) | (-16777216) | (i2 << 8) | i3, f);
    }

    public static int hex(String str, int i) {
        try {
            String replace = str.trim().replace("#", "");
            if (replace.length() == 3) {
                char charAt = replace.charAt(0);
                char charAt2 = replace.charAt(0);
                char charAt3 = replace.charAt(1);
                char charAt4 = replace.charAt(1);
                replace = new StringBuilder().append(charAt).append(charAt2).append(charAt3).append(charAt4).append(replace.charAt(2)).append(replace.charAt(2)).toString();
            }
            return replace.length() != 6 ? i : Integer.parseInt(replace, 16) | (-16777216);
        } catch (Exception e) {
            return i;
        }
    }

    public static int mix(int i, int i2, float f) {
        float max = Math.max(0.0f, Math.min(1.0f, f));
        return (i & (-16777216)) | (Math.round(((i >> 16) & 255) + ((((i2 >> 16) & 255) - r0) * max)) << 16) | (Math.round(((i >> 8) & 255) + ((((i2 >> 8) & 255) - r1) * max)) << 8) | Math.round((i & 255) + (((i2 & 255) - r2) * max));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int sc(String str) {
        char c;
        if (str == null) {
            str = "";
        }
        switch (str.hashCode()) {
            case -1361632588:
                if (str.equals("charge")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -995409728:
                if (str.equals("partly")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 101566:
                if (str.equals("fog")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 107348:
                if (str.equals("low")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 114252:
                if (str.equals("sun")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 3062234:
                if (str.equals("crit")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3357441:
                if (str.equals("moon")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 3492756:
                if (str.equals("rain")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 3535235:
                if (str.equals("snow")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 94756405:
                if (str.equals("cloud")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 109770985:
                if (str.equals("storm")) {
                    c = '\n';
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
                return -10835492;
            case 1:
                return -2078885;
            case 2:
                return -3665874;
            case 3:
                return SC_SUN;
            case 4:
                return SC_MOON;
            case 5:
                return SC_PARTLY;
            case 6:
                return -6641998;
            case 7:
                return -8549994;
            case '\b':
                return SC_RAIN;
            case '\t':
                return SC_SNOW;
            case '\n':
                return SC_STORM;
            default:
                return this.INK;
        }
    }

    public int sc2(String str) {
        return "cloud".equals(str) ? this.DIM : sc(str);
    }

    public Theme load(String str, JSONObject jSONObject, JSONObject jSONObject2, float f, float f2) {
        boolean z = false;
        this.aoa = false;
        try {
            this.aoa = HomeTheme.aoa(Store.get(Advanced.KEY, ""));
        } catch (Exception e) {
            this.aoa = false;
        }
        if ("light".equals(str) && !this.aoa) {
            z = true;
        }
        this.light = z;
        int hex = hex(jSONObject.optString("accent", "#5aa9dc"), -10835492);
        int hex2 = hex(jSONObject.optString("alert", "#c8102e"), -3665874);
        int hex3 = hex(jSONObject.optString("ink", "#e4ebf1"), -1774607);
        this.ACCENT_RAW = hex;
        this.INK_RAW = hex3;
        if (this.light) {
            this.BG = Palette.LIGHT_BG;
            this.INK = Palette.LIGHT_INK;
            this.DIM = Palette.LIGHT_DIM;
            this.LINE = rgba(20, 30, 40, 0.14f);
            this.LINE2 = rgba(20, 30, 40, 0.3f);
            this.SEG = rgba(255, 255, 255, 0.85f);
            this.BLUE = mix(hex, -16777216, 0.4f);
            this.ACT = a(this.BLUE, 0.12f);
            this.GRID = rgba(20, 40, 60, 0.06f);
            this.GLOW = a(this.BLUE, 0.1f);
            this.FRAME = -3157033;
        } else {
            this.BG = -16579579;
            this.INK = hex3;
            this.DIM = -8549994;
            this.LINE = rgba(200, 214, 226, 0.16f);
            this.LINE2 = rgba(200, 214, 226, 0.32f);
            this.SEG = rgba(16, 20, 26, 0.9f);
            this.BLUE = hex;
            this.ACT = a(hex, 0.16f);
            this.GRID = rgba(160, 180, 200, 0.055f);
            this.GLOW = rgba(43, 93, 130, 0.22f);
            this.FRAME = -16053493;
        }
        this.RED = hex2;
        if (this.aoa) {
            this.BG = -16579322;
            this.INK = -1511182;
            this.DIM = -9399652;
            this.LINE = rgba(192, 214, 220, 0.14f);
            this.LINE2 = rgba(192, 214, 220, 0.26f);
            this.SEG = rgba(10, 17, 20, 0.9f);
            this.BLUE = AOA_TEAL;
            this.ACT = a(AOA_TEAL, 0.16f);
            this.GRID = rgba(160, 200, 212, 0.05f);
            this.GLOW = -15918561;
            this.FRAME = -16447221;
            this.RED = AOA_ORANGE;
            this.ACCENT_RAW = AOA_TEAL;
            this.INK_RAW = this.INK;
        }
        if (this.aoa) {
            this.fWHITE = -1511182;
            this.fNAME = -2299668;
            this.fG1 = -6902604;
            this.fG2 = -9401708;
            this.fG3 = -11374470;
            this.fG4 = -13876146;
            this.fG5 = -14798792;
            this.fG6 = -15457752;
            this.fG7 = -16117484;
            this.fRED = AOA_ORANGE;
            this.fRED_HI = -26038;
            this.fRED_DEEP = -14020090;
            this.fBLUE = AOA_TEAL;
            this.fBLUE_HI = -8601388;
            this.fBG = Flip7.BG;
        } else {
            this.fWHITE = -1774607;
            this.fNAME = Flip7.NAME;
            this.fG1 = -6641998;
            this.fG2 = -8549994;
            this.fG3 = Flip7.G3;
            this.fG4 = Flip7.G4;
            this.fG5 = Flip7.G5;
            this.fG6 = Flip7.G6;
            this.fG7 = Flip7.G7;
            this.fRED = -3665874;
            this.fRED_HI = Flip7.RED_HI;
            this.fRED_DEEP = Flip7.RED_DEEP;
            this.fBLUE = -10835492;
            this.fBLUE_HI = Flip7.BLUE_HI;
            this.fBG = Flip7.BG;
        }
        this.tint = jSONObject2.has("tint") ? jSONObject2.optInt("tint", 42) : 42;
        float round = Math.round((jSONObject2.has("blur") ? (float) jSONObject2.optDouble("blur", 12.0d) : 12.0f) * f);
        if (f <= 0.0f || Build.VERSION.SDK_INT < 31) {
            this.tint = Math.min(100, this.tint + 25);
        }
        this.blurPx = round * f2;
        this.CARD = this.light ? rgba(255, 255, 255, ((this.tint / 100.0f) * 0.9f) + 0.1f) : this.aoa ? rgba(10, 17, 20, Math.max(0.8f, this.tint / 100.0f)) : rgba(22, 28, 36, this.tint / 100.0f);
        return this;
    }
}
