package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.ai.Dial;
import com.jarvis.assist.aosui.core.AoaPages;
import com.jarvis.assist.aosui.core.Corners;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Theme;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaClock extends AoaPage {
    static long swAcc;
    static boolean swRun;
    static long swStart;
    static long tEnd;
    static boolean tRun;
    LinearLayout ctl;
    Face face;
    final Runnable tick;
    static long tLeft = Dial.RESOLVE_MS;
    static long tSet = Dial.RESOLVE_MS;
    static final ArrayList<Long> laps = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AoaClock(Context context, Backend backend, HomeCtx homeCtx, HomeData homeData) {
        super(context, backend, homeCtx, homeData, "clock", "CLOCK");
        this.tick = new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                AoaClock.this.tick();
            }
        };
        String str = "clock";
        String[][] strArr = {new String[]{"clock", "CLOCK"}, new String[]{"alarms", "ALARMS"}, new String[]{"timer", "TIMER"}, new String[]{"stop", "STOPWATCH"}, new String[]{"world", "WORLD"}};
        if (tRun) {
            str = "timer";
        } else if (swRun) {
            str = "stop";
        }
        setTabs(strArr, str);
        showTab(this.tab);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void tick() {
        MAIN.removeCallbacks(this.tick);
        if (isAttachedToWindow()) {
            header();
            boolean z = false;
            if (tRun && System.currentTimeMillis() >= tEnd) {
                tRun = false;
                tLeft = 0L;
                Feedback.fx(this, 3);
                this.be.toast("TIMER DONE");
                if ("timer".equals(this.tab)) {
                    controls();
                }
            }
            if (this.face != null) {
                this.face.invalidate();
            }
            if ("stop".equals(this.tab) && swRun && !Perf.k().calm) {
                z = true;
            }
            MAIN.postDelayed(this.tick, z ? 50L : 1000 - (System.currentTimeMillis() % 1000));
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        tick();
    }

    @Override // com.jarvis.assist.aosui.home.AoaPage
    void stop() {
        MAIN.removeCallbacks(this.tick);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.jarvis.assist.aosui.home.AoaPage
    void showTab(String str) {
        boolean z;
        this.body.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        this.face = null;
        this.ctl = null;
        long currentTimeMillis = System.currentTimeMillis();
        long j = 0;
        this.sub.setText(this.d.alarmAt > 0 ? "NEXT ALARM " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(this.d.alarmAt)) : "NO ALARM SET");
        switch (str.hashCode()) {
            case -1415196606:
                if (str.equals("alarms")) {
                    z = false;
                    break;
                }
                z = -1;
                break;
            case 113318802:
                if (str.equals("world")) {
                    z = true;
                    break;
                }
                z = -1;
                break;
            default:
                z = -1;
                break;
        }
        switch (z) {
            case false:
                linearLayout.addView(mono("NEXT", 7.5f, 0.16f, Theme.AOA_ORANGE), lp(-2, -2, 0.0f));
                if (this.d.alarmAt > 0) {
                    linearLayout.addView(row("ALARM", new SimpleDateFormat("EEE HH:mm", Locale.getDefault()).format(new Date(this.d.alarmAt)).toUpperCase(Locale.getDefault()), "IN " + Corners.dur(this.d.alarmAt - currentTimeMillis), true, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            AoaClock.this.m213lambda$showTab$0$comaugmaosohomeAoaClock();
                        }
                    }));
                } else {
                    linearLayout.addView(row("ALARM", "NONE SET", null, false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            AoaClock.this.m214lambda$showTab$1$comaugmaosohomeAoaClock();
                        }
                    }));
                }
                LinearLayout linearLayout2 = new LinearLayout(getContext());
                linearLayout2.addView(button("ALL ALARMS", false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        AoaClock.this.m215lambda$showTab$2$comaugmaosohomeAoaClock();
                    }
                }), new LinearLayout.LayoutParams(0, -2, 1.0f));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
                layoutParams.setMarginStart(px(8.0f));
                linearLayout2.addView(button("+ NEW ALARM", true, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        AoaClock.this.m216lambda$showTab$3$comaugmaosohomeAoaClock();
                    }
                }), layoutParams);
                linearLayout.addView(linearLayout2, lp(-1, -2, 14.0f));
                break;
            case true:
                linearLayout.addView(mono("WORLD", 7.5f, 0.16f, Theme.AOA_ORANGE), lp(-2, -2, 0.0f));
                ArrayList<String> clocks = this.d.clocks();
                long offset = TimeZone.getDefault().getOffset(currentTimeMillis);
                Iterator<String> it = clocks.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(next));
                    long offset2 = (r2.getOffset(currentTimeMillis) - offset) / 60000;
                    long j2 = j;
                    linearLayout.addView(row(String.format(Locale.ROOT, "%s%dH", offset2 >= j ? "+" : "−", Long.valueOf(Math.abs(offset2) / 60)) + (offset2 % 60 != j2 ? String.format(Locale.ROOT, "%02d", Long.valueOf(Math.abs(offset2) % 60)) : ""), next.substring(next.lastIndexOf(47) + 1).replace('_', ' ').toUpperCase(Locale.ROOT), String.format(Locale.ROOT, "%02d:%02d", Integer.valueOf(calendar.get(11)), Integer.valueOf(calendar.get(12))), false, null));
                    j = j2;
                }
                break;
            default:
                this.face = new Face(getContext(), str);
                linearLayout.addView(this.face, new LinearLayout.LayoutParams(-1, px(260.0f)));
                this.ctl = new LinearLayout(getContext());
                this.ctl.setOrientation(1);
                linearLayout.addView(this.ctl, lp(-1, -2, 10.0f));
                controls();
                break;
        }
        this.body.addView(scroll(linearLayout));
        tick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showTab$0$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m213lambda$showTab$0$comaugmaosohomeAoaClock() {
        start(new Intent("android.intent.action.SHOW_ALARMS"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showTab$1$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m214lambda$showTab$1$comaugmaosohomeAoaClock() {
        start(new Intent("android.intent.action.SHOW_ALARMS"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showTab$2$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m215lambda$showTab$2$comaugmaosohomeAoaClock() {
        start(new Intent("android.intent.action.SHOW_ALARMS"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showTab$3$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m216lambda$showTab$3$comaugmaosohomeAoaClock() {
        start(new Intent("android.intent.action.SET_ALARM"));
    }

    void controls() {
        if (this.ctl == null) {
            return;
        }
        this.ctl.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getContext());
        if ("timer".equals(this.tab)) {
            LinearLayout linearLayout2 = new LinearLayout(getContext());
            int[] iArr = {1, 5, 10, 15, 30};
            for (int i = 0; i < 5; i++) {
                final int i2 = iArr[i];
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
                if (linearLayout2.getChildCount() > 0) {
                    layoutParams.setMarginStart(px(5.0f));
                }
                linearLayout2.addView(button(i2 + " MIN", false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        AoaClock.this.m207lambda$controls$4$comaugmaosohomeAoaClock(i2);
                    }
                }), layoutParams);
            }
            this.ctl.addView(linearLayout2);
            add(linearLayout, button(tRun ? "PAUSE" : "START", true, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    AoaClock.this.m208lambda$controls$5$comaugmaosohomeAoaClock();
                }
            }));
            add(linearLayout, button("RESET", false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    AoaClock.this.m209lambda$controls$6$comaugmaosohomeAoaClock();
                }
            }));
            add(linearLayout, button("RING VIA CLOCK", false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    AoaClock.this.m210lambda$controls$7$comaugmaosohomeAoaClock();
                }
            }));
            this.ctl.addView(linearLayout, lp(-1, -2, 8.0f));
            return;
        }
        if ("stop".equals(this.tab)) {
            add(linearLayout, button(swRun ? "STOP" : "START", true, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    AoaClock.this.m211lambda$controls$8$comaugmaosohomeAoaClock();
                }
            }));
            add(linearLayout, button("LAP", false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    AoaClock.this.m212lambda$controls$9$comaugmaosohomeAoaClock();
                }
            }));
            add(linearLayout, button("RESET", false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    AoaClock.this.m205lambda$controls$10$comaugmaosohomeAoaClock();
                }
            }));
            this.ctl.addView(linearLayout);
            int i3 = 0;
            while (i3 < laps.size() && i3 < 20) {
                int i4 = i3 + 1;
                this.ctl.addView(row(String.format(Locale.ROOT, "LAP %02d", Integer.valueOf(laps.size() - i3)), AoaPages.stopwatch(laps.get(i3).longValue()), i4 < laps.size() ? "+" + AoaPages.stopwatch(laps.get(i3).longValue() - laps.get(i4).longValue()) : null, i3 == 0, null));
                i3 = i4;
            }
            return;
        }
        this.ctl.addView(row("ZONE", TimeZone.getDefault().getID().toUpperCase(Locale.ROOT).replace('_', ' '), null, false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaClock$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                AoaClock.this.m206lambda$controls$11$comaugmaosohomeAoaClock();
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$controls$4$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m207lambda$controls$4$comaugmaosohomeAoaClock(int i) {
        tRun = false;
        long j = i * 60000;
        tLeft = j;
        tSet = j;
        controls();
        this.face.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$controls$5$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m208lambda$controls$5$comaugmaosohomeAoaClock() {
        boolean z;
        if (!tRun) {
            if (tLeft > 0) {
                tEnd = System.currentTimeMillis() + tLeft;
                z = true;
            }
            controls();
            tick();
        }
        tLeft = Math.max(0L, tEnd - System.currentTimeMillis());
        z = false;
        tRun = z;
        controls();
        tick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$controls$6$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m209lambda$controls$6$comaugmaosohomeAoaClock() {
        tRun = false;
        tLeft = tSet;
        controls();
        this.face.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$controls$7$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m210lambda$controls$7$comaugmaosohomeAoaClock() {
        start(new Intent("android.intent.action.SET_TIMER").putExtra("android.intent.extra.alarm.LENGTH", (int) Math.max(1L, (tRun ? tEnd - System.currentTimeMillis() : tLeft) / 1000)).putExtra("android.intent.extra.alarm.SKIP_UI", false));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$controls$8$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m211lambda$controls$8$comaugmaosohomeAoaClock() {
        boolean z;
        long currentTimeMillis = System.currentTimeMillis();
        if (swRun) {
            swAcc += currentTimeMillis - swStart;
            z = false;
        } else {
            swStart = currentTimeMillis;
            z = true;
        }
        swRun = z;
        controls();
        tick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$controls$9$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m212lambda$controls$9$comaugmaosohomeAoaClock() {
        if (swRun) {
            laps.add(0, Long.valueOf((swAcc + System.currentTimeMillis()) - swStart));
            controls();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$controls$10$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m205lambda$controls$10$comaugmaosohomeAoaClock() {
        swRun = false;
        swAcc = 0L;
        laps.clear();
        controls();
        this.face.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$controls$11$com-augma-oso-home-AoaClock, reason: not valid java name */
    public /* synthetic */ void m206lambda$controls$11$comaugmaosohomeAoaClock() {
        start(new Intent("android.intent.action.SHOW_ALARMS"));
    }

    void add(LinearLayout linearLayout, View view) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        if (linearLayout.getChildCount() > 0) {
            layoutParams.setMarginStart(px(6.0f));
        }
        linearLayout.addView(view, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Face extends View {
        final Txt big;
        final String mode;
        final Paint p;
        final RectF rf;
        final Txt small;

        Face(Context context, String str) {
            super(context);
            this.p = new Paint(1);
            this.rf = new RectF();
            this.mode = str;
            this.big = new Txt(Fonts.MONO, ("clock".equals(str) ? 44 : 34) * AoaClock.this.dn, AoaClock.this.dn * 2.0f).align(Paint.Align.CENTER);
            this.small = new Txt(Fonts.MONO, AoaClock.this.dn * 8.0f, AoaClock.this.dn * 1.4f).align(Paint.Align.CENTER);
            setImportantForAccessibility(1);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x007f  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0085  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        protected void onDraw(android.graphics.Canvas r20) {
            /*
                Method dump skipped, instructions count: 524
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.AoaClock.Face.onDraw(android.graphics.Canvas):void");
        }
    }
}
