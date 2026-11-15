package com.jarvis.assist.aosui.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class FxModel {
    public static final int BOOT = 13;
    public static final int CLOSE = 7;
    public static final int CONFIRM = 4;
    public static final int ERROR = 12;
    public static final int H_CONFIRM = 5;
    public static final int H_GEND = 9;
    public static final int H_GSTART = 8;
    public static final int H_HEAVY = 10;
    public static final int H_KEY = 2;
    public static final int H_LONG = 4;
    public static final int H_NONE = 0;
    public static final int H_REJECT = 6;
    public static final int H_SEG = 7;
    public static final int H_TICK = 3;
    public static final int H_VKEY = 1;
    public static final String KEY = "aos.fx";
    public static final int KEY_TAP = 1;
    public static final int LISTEN_START = 9;
    public static final int LISTEN_STOP = 10;
    public static final int LOCK = 14;
    public static final int LONG = 3;
    public static final int MAX_MS = 60;
    public static final int N_EV = 17;
    public static final int N_SND = 12;
    public static final int OPEN = 6;
    public static final int PAGE = 8;
    public static final float PEAK_1DB = 0.891f;
    public static final int RATE = 22050;
    public static final int REJECT = 5;
    public static final int REPLY = 11;
    public static final int RINGER_NORMAL = 2;
    public static final int RINGER_SILENT = 0;
    public static final int RINGER_VIBRATE = 1;
    public static final int SEND = 16;
    public static final int S_BOOT = 10;
    public static final int S_CLOSE = 4;
    public static final int S_CONFIRM = 5;
    public static final int S_ERROR = 6;
    public static final int S_LISTEN = 7;
    public static final int S_NONE = -1;
    public static final int S_OPEN = 3;
    public static final int S_REPLY = 9;
    public static final int S_SNAP = 11;
    public static final int S_SWIPE = 2;
    public static final int S_TAP = 0;
    public static final int S_TICK = 1;
    public static final int S_UNLISTEN = 8;
    public static final int TAP = 0;
    public static final int TEST_GAP_MS = 150;
    public static final int TICK = 2;
    public static final int TOGGLE = 15;
    public static final int TP_NONE = 0;
    public static final int TP_VIBRATOR = 2;
    public static final int TP_VIEW = 1;
    public static final int W_CLICK = 3;
    public static final int W_SINE = 0;
    public static final int W_SQUARE = 2;
    public static final int W_TRI = 1;
    public static final String[] SND_NAMES = {"tap", "tick", "swipe", "open", "close", "confirm", "error", "listen", "unlisten", "reply", "boot", "snap"};
    public static final String[] STRENGTHS = {"off", "light", "normal", "strong"};
    public static final String[] STYLES = {"aos", "retro", "soft"};
    public static final int[] TEST_SEQ = {0, 2, 4};
    static final float[][][] DEF = {new float[][]{new float[]{0.0f, 18.0f, 1800.0f, 1700.0f, 0.0f, 0.2f}}, new float[][]{new float[]{0.0f, 24.0f, 2600.0f, 2100.0f, 3.0f, 0.891f}}, new float[][]{new float[]{0.0f, 42.0f, 900.0f, 1500.0f, 0.0f, 0.15f}}, new float[][]{new float[]{0.0f, 52.0f, 620.0f, 1240.0f, 0.0f, 0.17f}}, new float[][]{new float[]{0.0f, 52.0f, 1240.0f, 620.0f, 0.0f, 0.15f}}, new float[][]{new float[]{0.0f, 26.0f, 880.0f, 880.0f, 0.0f, 0.16f}, new float[]{22.0f, 34.0f, 1320.0f, 1320.0f, 0.0f, 0.16f}}, new float[][]{new float[]{0.0f, 26.0f, 330.0f, 330.0f, 1.0f, 0.18f}, new float[]{26.0f, 32.0f, 247.0f, 247.0f, 1.0f, 0.18f}}, new float[][]{new float[]{0.0f, 46.0f, 700.0f, 1400.0f, 0.0f, 0.16f}}, new float[][]{new float[]{0.0f, 46.0f, 1400.0f, 700.0f, 0.0f, 0.14f}}, new float[][]{new float[]{0.0f, 22.0f, 1568.0f, 1568.0f, 0.0f, 0.12f}, new float[]{18.0f, 40.0f, 2093.0f, 2093.0f, 0.0f, 0.12f}}, new float[][]{new float[]{0.0f, 24.0f, 523.0f, 523.0f, 1.0f, 0.11f}, new float[]{16.0f, 24.0f, 784.0f, 784.0f, 1.0f, 0.11f}, new float[]{32.0f, 28.0f, 1047.0f, 1047.0f, 1.0f, 0.11f}}, new float[][]{new float[]{0.0f, 30.0f, 1900.0f, 1500.0f, 3.0f, 0.891f}}};
    static final float[][][] RETRO = {new float[][]{new float[]{0.0f, 14.0f, 1400.0f, 2000.0f, 2.0f, 0.09f}}, new float[][]{new float[]{0.0f, 22.0f, 3000.0f, 2000.0f, 3.0f, 0.891f}}, new float[][]{new float[]{0.0f, 40.0f, 500.0f, 1800.0f, 1.0f, 0.14f}}, new float[][]{new float[]{0.0f, 20.0f, 660.0f, 990.0f, 2.0f, 0.08f}, new float[]{20.0f, 26.0f, 990.0f, 1480.0f, 2.0f, 0.08f}}, new float[][]{new float[]{0.0f, 20.0f, 1480.0f, 990.0f, 2.0f, 0.07f}, new float[]{20.0f, 26.0f, 990.0f, 660.0f, 2.0f, 0.07f}}, new float[][]{new float[]{0.0f, 16.0f, 1047.0f, 1047.0f, 2.0f, 0.08f}, new float[]{18.0f, 16.0f, 1319.0f, 1319.0f, 2.0f, 0.08f}, new float[]{36.0f, 22.0f, 1568.0f, 1760.0f, 2.0f, 0.08f}}, new float[][]{new float[]{0.0f, 30.0f, 220.0f, 160.0f, 2.0f, 0.1f}, new float[]{30.0f, 28.0f, 160.0f, 110.0f, 2.0f, 0.1f}}, new float[][]{new float[]{0.0f, 50.0f, 400.0f, 1600.0f, 1.0f, 0.14f}}, new float[][]{new float[]{0.0f, 50.0f, 1600.0f, 400.0f, 1.0f, 0.12f}}, new float[][]{new float[]{0.0f, 14.0f, 1760.0f, 2349.0f, 2.0f, 0.07f}, new float[]{16.0f, 30.0f, 2349.0f, 2093.0f, 2.0f, 0.07f}}, new float[][]{new float[]{0.0f, 18.0f, 392.0f, 523.0f, 2.0f, 0.07f}, new float[]{18.0f, 18.0f, 523.0f, 784.0f, 2.0f, 0.07f}, new float[]{36.0f, 24.0f, 784.0f, 1175.0f, 2.0f, 0.07f}}, new float[][]{new float[]{0.0f, 28.0f, 1800.0f, 1300.0f, 3.0f, 0.891f}}};
    static final float[][][] SOFT = {new float[][]{new float[]{0.0f, 22.0f, 1200.0f, 1150.0f, 0.0f, 0.12f}}, new float[][]{new float[]{0.0f, 26.0f, 2000.0f, 1800.0f, 3.0f, 0.63f}}, new float[][]{new float[]{0.0f, 48.0f, 700.0f, 1000.0f, 0.0f, 0.09f}}, new float[][]{new float[]{0.0f, 56.0f, 520.0f, 780.0f, 0.0f, 0.1f}}, new float[][]{new float[]{0.0f, 56.0f, 780.0f, 520.0f, 0.0f, 0.09f}}, new float[][]{new float[]{0.0f, 28.0f, 660.0f, 660.0f, 0.0f, 0.1f}, new float[]{24.0f, 34.0f, 990.0f, 990.0f, 0.0f, 0.1f}}, new float[][]{new float[]{0.0f, 30.0f, 300.0f, 300.0f, 0.0f, 0.11f}, new float[]{28.0f, 32.0f, 225.0f, 225.0f, 0.0f, 0.11f}}, new float[][]{new float[]{0.0f, 50.0f, 560.0f, 840.0f, 0.0f, 0.1f}}, new float[][]{new float[]{0.0f, 50.0f, 840.0f, 560.0f, 0.0f, 0.09f}}, new float[][]{new float[]{0.0f, 24.0f, 1047.0f, 1047.0f, 0.0f, 0.08f}, new float[]{20.0f, 40.0f, 1397.0f, 1397.0f, 0.0f, 0.08f}}, new float[][]{new float[]{0.0f, 26.0f, 392.0f, 392.0f, 0.0f, 0.07f}, new float[]{18.0f, 26.0f, 523.0f, 523.0f, 0.0f, 0.07f}, new float[]{34.0f, 26.0f, 659.0f, 659.0f, 0.0f, 0.07f}}, new float[][]{new float[]{0.0f, 30.0f, 1800.0f, 1500.0f, 3.0f, 0.7f}}};

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Cfg {
        public boolean sounds = true;
        public boolean haptics = true;
        public boolean follow = false;
        public int volume = 100;
        public String strength = "normal";
        public String style = "aos";
    }

    private FxModel() {
    }

    public static int[] map(int i) {
        switch (i) {
            case 0:
                return new int[]{1, 0};
            case 1:
                return new int[]{2, 1};
            case 2:
                return new int[]{3, 1};
            case 3:
                return new int[]{4, -1};
            case 4:
                return new int[]{5, 5};
            case 5:
                return new int[]{6, 4};
            case 6:
                return new int[]{8, 3};
            case 7:
                return new int[]{9, 4};
            case 8:
                return new int[]{7, 2};
            case 9:
                return new int[]{8, 7};
            case 10:
                return new int[]{9, 8};
            case 11:
                return new int[]{0, 9};
            case 12:
                return new int[]{6, 6};
            case BOOT /* 13 */:
                return new int[]{0, 10};
            case LOCK /* 14 */:
                return new int[]{10, 4};
            case 15:
                return new int[]{7, 11};
            case 16:
                return new int[]{5, 2};
            default:
                return new int[]{0, -1};
        }
    }

    public static Cfg cfg(String str) {
        Cfg cfg = new Cfg();
        if (str == null || str.isEmpty()) {
            return cfg;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            cfg.sounds = jSONObject.optBoolean("snd", true);
            cfg.haptics = jSONObject.optBoolean("hap", true);
            cfg.follow = jSONObject.optBoolean("follow", false);
            cfg.volume = Math.max(0, Math.min(100, jSONObject.optInt("vol", 100)));
            int optInt = jSONObject.optInt("v", 1);
            if (optInt < 2) {
                if (jSONObject.optBoolean("follow", true) && jSONObject.optInt("vol", 60) == 60) {
                    cfg.follow = false;
                    cfg.volume = 100;
                } else if (!jSONObject.has("follow")) {
                    cfg.follow = false;
                }
            } else if (optInt < 3 && cfg.volume == 80) {
                cfg.volume = 100;
            }
            String optString = jSONObject.optString("str", "normal");
            cfg.strength = Arrays.asList(STRENGTHS).contains(optString) ? optString : "normal";
            String optString2 = jSONObject.optString("style", "aos");
            cfg.style = Arrays.asList(STYLES).contains(optString2) ? optString2 : "aos";
        } catch (Exception e) {
        }
        return cfg;
    }

    public static String save(Cfg cfg) {
        try {
            return new JSONObject().put("snd", cfg.sounds).put("hap", cfg.haptics).put("follow", cfg.follow).put("vol", cfg.volume).put("str", cfg.strength).put("style", cfg.style).put("v", 3).toString();
        } catch (Exception e) {
            return "";
        }
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Phone {
        public boolean dnd;
        public boolean hapticSetting;
        public int ringer;
        public String tier;
        public boolean touchSounds;

        public Phone() {
            this.touchSounds = true;
            this.hapticSetting = true;
            this.ringer = 2;
            this.tier = "BALANCED";
        }

        public Phone(boolean z, boolean z2, int i, boolean z3, String str) {
            this.touchSounds = true;
            this.hapticSetting = true;
            this.ringer = 2;
            this.tier = "BALANCED";
            this.touchSounds = z;
            this.hapticSetting = z2;
            this.ringer = i;
            this.dnd = z3;
            this.tier = str;
        }
    }

    public static boolean soundOn(Cfg cfg, Phone phone) {
        return cfg.sounds && cfg.volume > 0 && !"BATTERY".equals(phone.tier) && !phone.dnd && phone.ringer == 2 && (!cfg.follow || phone.touchSounds);
    }

    public static String why(Cfg cfg, Phone phone) {
        if (!cfg.sounds || cfg.volume <= 0) {
            return "off";
        }
        return "BATTERY".equals(phone.tier) ? "battery" : phone.dnd ? "dnd" : phone.ringer != 2 ? "ringer" : (!cfg.follow || phone.touchSounds) ? "ok" : "sys";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static float hapticScale(Cfg cfg) {
        char c;
        if (!cfg.haptics) {
            return 0.0f;
        }
        String str = cfg.strength;
        switch (str.hashCode()) {
            case -891980137:
                if (str.equals("strong")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 109935:
                if (str.equals("off")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 102970646:
                if (str.equals("light")) {
                    c = 1;
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
                return 0.0f;
            case 1:
                return 0.45f;
            case 2:
                return 1.0f;
            default:
                return 0.75f;
        }
    }

    public static int amplitude(float f) {
        if (f <= 0.0f) {
            return 0;
        }
        return Math.max(1, Math.min(255, Math.round(f * 255.0f)));
    }

    public static float gain(Cfg cfg) {
        return Math.max(0.0f, Math.min(1.0f, cfg.volume / 100.0f));
    }

    public static boolean hapticOn(Cfg cfg, Phone phone) {
        return hapticScale(cfg) > 0.0f && (phone.hapticSetting || !"normal".equals(cfg.strength));
    }

    public static int[] decide(int i, Cfg cfg, Phone phone) {
        int[] map = map(i);
        return new int[]{hapticOn(cfg, phone) ? map[0] : 0, soundOn(cfg, phone) ? map[1] : -1};
    }

    public static int testHapticPath(Cfg cfg, Phone phone) {
        if (hapticScale(cfg) <= 0.0f) {
            return 0;
        }
        return ("normal".equals(cfg.strength) && phone.hapticSetting) ? 1 : 2;
    }

    public static boolean testHapticHint(Cfg cfg, Phone phone) {
        return hapticScale(cfg) > 0.0f && "normal".equals(cfg.strength) && !phone.hapticSetting;
    }

    public static float[][][] set(String str) {
        return "retro".equals(str) ? RETRO : "soft".equals(str) ? SOFT : DEF;
    }

    public static int ms(int i) {
        return ms("aos", i);
    }

    public static int ms(String str, int i) {
        int i2 = 0;
        for (float[] fArr : set(str)[i]) {
            i2 = Math.max(i2, Math.round(fArr[0] + fArr[1]));
        }
        return i2;
    }

    public static short[] pcm(int i) {
        return pcm("aos", i);
    }

    public static short[] pcm(String str, int i) {
        float[] fArr;
        float[][] fArr2;
        int ms = (ms(str, i) * RATE) / 1000;
        float[] fArr3 = new float[ms];
        float[][] fArr4 = set(str)[i];
        int length = fArr4.length;
        int i2 = 0;
        int i3 = 0;
        float f = 0.0f;
        while (i3 < length) {
            float[] fArr5 = fArr4[i3];
            char c = 4;
            char c2 = 5;
            if (fArr5[4] == 3.0f) {
                f = Math.max(f, fArr5[5]);
                click(fArr3, fArr5);
                fArr = fArr3;
                fArr2 = fArr4;
            } else {
                int round = Math.round((fArr5[i2] * 22050.0f) / 1000.0f);
                int round2 = Math.round((fArr5[1] * 22050.0f) / 1000.0f);
                int min = Math.min(round2 / 3, Math.round(88.2f));
                double d = 0.0d;
                int i4 = i2;
                while (i4 < round2) {
                    int i5 = round + i4;
                    if (i5 >= ms) {
                        break;
                    }
                    char c3 = c;
                    int i6 = round;
                    double d2 = i4;
                    float[] fArr6 = fArr5;
                    char c4 = c2;
                    float[] fArr7 = fArr3;
                    float[][] fArr8 = fArr4;
                    d += ((fArr6[2] + ((fArr6[3] - fArr6[2]) * (d2 / round2))) * 6.283185307179586d) / 22050.0d;
                    fArr7[i5] = fArr7[i5] + ((float) ((fArr6[c3] == 2.0f ? Math.tanh(Math.sin(d) * 4.0d) / Math.tanh(4.0d) : fArr6[c3] == 1.0f ? Math.asin(Math.sin(d)) * 0.6366197723675814d : Math.sin(d)) * (i4 < min ? 0.5d - (Math.cos((d2 * 3.141592653589793d) / min) * 0.5d) : Math.pow(1.0d - ((i4 - min) / Math.max(1, round2 - min)), 2.0d)) * fArr6[c4]));
                    i4++;
                    c = c3;
                    round = i6;
                    fArr5 = fArr6;
                    c2 = c4;
                    fArr3 = fArr7;
                    fArr4 = fArr8;
                }
                fArr = fArr3;
                fArr2 = fArr4;
            }
            i3++;
            fArr3 = fArr;
            fArr4 = fArr2;
            i2 = 0;
        }
        float[] fArr9 = fArr3;
        if (f > 0.0f) {
            float f2 = 0.0f;
            for (int i7 = 0; i7 < ms; i7++) {
                fArr9[i7] = (float) Math.tanh(fArr9[i7] * 1.8d);
                f2 = Math.max(f2, Math.abs(fArr9[i7]));
            }
            if (f2 > 0.0f) {
                for (int i8 = 0; i8 < ms; i8++) {
                    fArr9[i8] = fArr9[i8] * (f / f2);
                }
            }
        }
        short[] sArr = new short[ms];
        for (int i9 = 0; i9 < ms; i9++) {
            sArr[i9] = (short) Math.round(Math.max(-1.0f, Math.min(1.0f, fArr9[i9])) * 32767.0f);
        }
        return sArr;
    }

    static void click(float[] fArr, float[] fArr2) {
        double d;
        double d2;
        int i = 0;
        int round = Math.round((fArr2[0] * 22050.0f) / 1000.0f);
        int round2 = Math.round((fArr2[1] * 22050.0f) / 1000.0f);
        char c = 2;
        int max = Math.max(2, Math.round(6.6150007f));
        double d3 = 0.0d;
        while (i < round2) {
            int i2 = round + i;
            if (i2 < fArr.length) {
                double d4 = i;
                double d5 = d4 / round2;
                d3 += ((fArr2[c] + ((fArr2[3] - fArr2[c]) * d5)) * 6.283185307179586d) / 22050.0d;
                if (i < max) {
                    d = 1.0d;
                    d2 = d4 / max;
                } else {
                    d = 1.0d;
                    d2 = 1.0d;
                }
                fArr[i2] = fArr[i2] + ((float) ((Math.sin(d3) + (Math.sin(2.0d * d3) * 0.35d)) * d2 * Math.exp((-5.5d) * d5) * (d - d5)));
                i++;
                c = 2;
            } else {
                return;
            }
        }
    }

    public static float peak(short[] sArr) {
        int i = 0;
        for (short s : sArr) {
            i = Math.max(i, Math.abs((int) s));
        }
        return i / 32767.0f;
    }

    public static byte[] wav(short[] sArr) {
        int length = sArr.length * 2;
        ByteBuffer order = ByteBuffer.allocate(length + 44).order(ByteOrder.LITTLE_ENDIAN);
        order.put("RIFF".getBytes(StandardCharsets.US_ASCII)).putInt(length + 36).put("WAVEfmt ".getBytes(StandardCharsets.US_ASCII)).putInt(16).putShort((short) 1).putShort((short) 1).putInt(RATE).putInt(44100).putShort((short) 2).putShort((short) 16).put("data".getBytes(StandardCharsets.US_ASCII)).putInt(length);
        for (short s : sArr) {
            order.putShort(s);
        }
        return order.array();
    }
}
