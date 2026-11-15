package com.jarvis.assist.aosui.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: /tmp/augmaos/classes.dex */
public final class PerfModel {
    public static final String[] TIERS = {"HIGH", "BALANCED", "LIGHT"};
    public static final String[] MODES = {"auto", "HIGH", "BALANCED", "LIGHT", "BATTERY"};
    public static final String[] RATES = {"auto", "120", "90", "60", "30"};

    private PerfModel() {
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Knobs {
        public float blur;
        public String boot;
        public boolean calm;
        public int carMs;
        public boolean dock;
        public int dockDiv;
        public float dockSp;
        public float dpr;
        public int fps;
        public int lockFps;
        public boolean motion;
        public boolean orb;
        public boolean orbGlow;
        public int orbLayers;
        public int orbPts;
        public boolean shadow;

        Knobs(float f, int i, int i2, boolean z, float f2, int i3, int i4, float f3, boolean z2, int i5, String str, int i6) {
            this.dpr = f;
            this.orbLayers = i;
            this.orbPts = i2;
            this.orbGlow = z;
            this.dockSp = f2;
            this.dockDiv = i3;
            this.fps = i4;
            this.blur = f3;
            this.shadow = z2;
            this.carMs = i5;
            this.boot = str;
            this.lockFps = i6;
        }

        Knobs copy() {
            Knobs knobs = new Knobs(this.dpr, this.orbLayers, this.orbPts, this.orbGlow, this.dockSp, this.dockDiv, this.fps, this.blur, this.shadow, this.carMs, this.boot, this.lockFps);
            knobs.calm = this.calm;
            knobs.orb = this.orb;
            knobs.dock = this.dock;
            knobs.motion = this.motion;
            return knobs;
        }
    }

    public static List<String> rates(int i, int[] iArr) {
        boolean z;
        ArrayList arrayList = new ArrayList();
        for (String str : RATES) {
            if (!str.equals("auto")) {
                int parseInt = Integer.parseInt(str);
                if (iArr != null) {
                    z = false;
                    for (int i2 : iArr) {
                        if (i2 == parseInt) {
                            z = true;
                        }
                    }
                } else {
                    z = false;
                }
                if (parseInt <= i) {
                    if (parseInt > 60 && !z) {
                    }
                }
            }
            arrayList.add(str);
        }
        return arrayList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static Knobs knobs(String str) {
        char c;
        switch (str.hashCode()) {
            case 2217378:
                if (str.equals("HIGH")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 72432886:
                if (str.equals("LIGHT")) {
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
                return new Knobs(2.0f, 5, 72, true, 1.0f, 1, 120, 1.0f, true, 600, "full", 24);
            case 1:
                return new Knobs(1.0f, 3, 40, false, 2.0f, 3, 30, 0.0f, false, BootSeq.UN_HOLD_MS, "skip", 0);
            default:
                return new Knobs(1.5f, 4, 56, true, 1.4f, 2, 60, 0.5f, true, 450, "short", 15);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int idx(String str) {
        for (int i = 0; i < TIERS.length; i++) {
            if (TIERS[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public static String down(String str) {
        return TIERS[Math.min(2, Math.max(0, idx(str)) + 1)];
    }

    public static String up(String str) {
        return TIERS[Math.max(0, idx(str) - 1)];
    }

    public static String worse(String str, String str2) {
        return TIERS[Math.max(idx(str), idx(str2))];
    }

    public static String pick(boolean z, int i, long j, int i2, int i3, double d, double d2) {
        String str;
        double d3 = j / 1.073741824E9d;
        String str2 = "HIGH";
        if (z || ((i > 0 && i <= 96) || ((d3 > 0.0d && d3 < 2.5d) || (i2 > 0 && i2 <= 4)))) {
            str = "LIGHT";
        } else {
            if (i2 >= 8 && (d3 <= 0.0d || d3 >= 5.5d)) {
                if (i3 <= 0) {
                    i3 = 99;
                }
                if (i3 >= 29) {
                    str = "HIGH";
                }
            }
            str = "BALANCED";
        }
        if (d <= 0.0d && d2 <= 0.0d) {
            return str;
        }
        if (d2 > 8.0d || d < 40.0d) {
            str2 = "LIGHT";
        } else if (d2 > 4.0d || d < 55.0d) {
            str2 = "BALANCED";
        }
        return worse(str, str2);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.jarvis.assist.aosui.core.PerfModel.Knobs compute(java.lang.String r6, java.lang.String r7, java.lang.String r8, int r9, boolean r10, boolean r11, boolean r12, boolean r13, boolean r14, boolean r15, boolean r16, boolean r17, java.lang.String[] r18) {
        /*
            r1 = r18
            java.lang.String r0 = "auto"
            boolean r2 = r0.equals(r6)
            java.lang.String r3 = "BALANCED"
            java.lang.String r4 = "LIGHT"
            java.lang.String r5 = "BATTERY"
            if (r2 == 0) goto L14
            if (r7 != 0) goto L1d
            r7 = r3
            goto L1d
        L14:
            boolean r7 = r5.equals(r6)
            if (r7 == 0) goto L1c
            r7 = r4
            goto L1d
        L1c:
            r7 = r6
        L1d:
            int r2 = idx(r7)
            if (r2 >= 0) goto L24
            goto L25
        L24:
            r3 = r7
        L25:
            if (r10 != 0) goto L2b
            if (r11 == 0) goto L2a
            goto L2b
        L2a:
            r4 = r3
        L2b:
            com.jarvis.assist.aosui.core.PerfModel$Knobs r7 = knobs(r4)
            com.jarvis.assist.aosui.core.PerfModel$Knobs r7 = r7.copy()
            r10 = 60
            if (r9 > 0) goto L38
            r9 = r10
        L38:
            r2 = 120(0x78, float:1.68E-43)
            int r9 = java.lang.Math.min(r2, r9)
            r2 = 30
            int r9 = java.lang.Math.max(r2, r9)
            boolean r0 = r0.equals(r8)     // Catch: java.lang.NumberFormatException -> L54
            if (r0 != 0) goto L52
            if (r8 != 0) goto L4d
            goto L52
        L4d:
            int r8 = java.lang.Integer.parseInt(r8)     // Catch: java.lang.NumberFormatException -> L54
            goto L56
        L52:
            r8 = -1
            goto L56
        L54:
            r0 = move-exception
            r8 = r10
        L56:
            if (r8 >= 0) goto L5f
            int r8 = r7.fps
            int r8 = java.lang.Math.min(r8, r9)
            goto L67
        L5f:
            if (r8 > 0) goto L62
            goto L63
        L62:
            r10 = r8
        L63:
            int r8 = java.lang.Math.min(r10, r9)
        L67:
            r7.fps = r8
            boolean r6 = r5.equals(r6)
            r8 = 1
            r9 = 0
            if (r6 != 0) goto L78
            if (r11 != 0) goto L78
            if (r12 == 0) goto L76
            goto L78
        L76:
            r6 = r9
            goto L79
        L78:
            r6 = r8
        L79:
            r7.calm = r6
            if (r14 == 0) goto L83
            boolean r6 = r7.calm
            if (r6 != 0) goto L83
            r6 = r8
            goto L84
        L83:
            r6 = r9
        L84:
            r7.orb = r6
            if (r15 == 0) goto L8e
            boolean r6 = r7.calm
            if (r6 != 0) goto L8e
            r6 = r8
            goto L8f
        L8e:
            r6 = r9
        L8f:
            r7.dock = r6
            if (r16 == 0) goto L98
            boolean r6 = r7.calm
            if (r6 != 0) goto L98
            goto L99
        L98:
            r8 = r9
        L99:
            r7.motion = r8
            if (r13 != 0) goto La0
            r6 = 0
            r7.blur = r6
        La0:
            if (r17 == 0) goto La6
            boolean r6 = r7.calm
            if (r6 == 0) goto Laa
        La6:
            java.lang.String r6 = "skip"
            r7.boot = r6
        Laa:
            if (r1 == 0) goto Lb1
            int r6 = r1.length
            if (r6 <= 0) goto Lb1
            r1[r9] = r4
        Lb1:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.core.PerfModel.compute(java.lang.String, java.lang.String, java.lang.String, int, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, java.lang.String[]):com.jarvis.assist.aosui.core.PerfModel$Knobs");
    }

    /* loaded from: /tmp/augmaos/classes.dex */
    public static final class Gov {
        float acc;
        int bad;
        public boolean canUp;
        int cool;
        int good;
        boolean justUp;
        int n;
        public float target;
        boolean tried;
        float[] win = new float[512];

        public Gov(float f) {
            this.target = f;
        }

        public String push(float f) {
            if (f <= 0.0f || f > 250.0f) {
                return null;
            }
            if (this.n < this.win.length) {
                float[] fArr = this.win;
                int i = this.n;
                this.n = i + 1;
                fArr[i] = f;
            }
            this.acc += f;
            if (this.acc < 2000.0f) {
                return null;
            }
            Arrays.sort(this.win, 0, this.n);
            float f2 = this.win[(int) Math.floor(this.n * 0.9d)];
            this.n = 0;
            this.acc = 0.0f;
            if (this.cool > 0) {
                this.cool--;
                return null;
            }
            if (f2 > this.target * 1.5f) {
                this.good = 0;
                int i2 = this.bad + 1;
                this.bad = i2;
                if (i2 < 3) {
                    return null;
                }
                this.bad = 0;
                this.cool = 5;
                if (this.justUp) {
                    this.tried = true;
                }
                this.justUp = false;
                return "down";
            }
            this.bad = 0;
            int i3 = this.good + 1;
            this.good = i3;
            if (i3 < 15 || !this.canUp || this.tried) {
                if (this.good >= 15) {
                    this.justUp = false;
                    this.good = 15;
                }
                return null;
            }
            this.good = 0;
            this.cool = 5;
            this.justUp = true;
            this.tried = true;
            return "up";
        }
    }
}
