package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.AoaPages;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.OsoWheel;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Svg;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.AoaMusic;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaMusic extends AoaPage {
    static final int DIM2 = -12958899;
    static final int DIMC = -9471096;
    static final int G1 = -6641998;
    static final String SP = "com.spotify.music";
    static MusicSrc fake;
    Bitmap art;
    String artKey;
    Cards cards;
    int libSel;
    JSONObject m;
    String mode;
    long now;
    float orbT;
    int pollN;
    int qSel;
    String scr;
    Long seekMs;
    final Runnable sendSeek;
    final MusicSrc src;
    ScrollView sv;
    final Runnable tick;
    Integer volV;
    Wheel wheel;
    static final int LINE = Theme.rgba(200, 214, 226, 0.13f);
    static final String[][] LIB = {new String[]{"PLAYLISTS", "spotify:collection"}, new String[]{"ARTISTS", "spotify:collection:artists"}, new String[]{"ALBUMS", "spotify:collection:albums"}, new String[]{"SONGS", "spotify:collection:tracks"}, new String[]{"PODCASTS", "spotify:collection:podcasts"}, new String[]{"SOURCES", null}};
    static final String[] ICON = {"M-6 -4h12M-6 0h12M-6 4h12", "M0 5l-5-5a3 3 0 0 1 5-4 3 3 0 0 1 5 4z", "M-6 -5l6 5-6 5zM0 -5l6 5-6 5z", "M-6 4a8 8 0 0 1 12 0M-3 1a4 4 0 0 1 6 0", "M-5 -6l9 6-9 6z", "M-5 5v-6M0 5v-10M5 5v-4", "M6 -5l-6 5 6 5zM0 -5l-6 5 6 5z", "M-6 -4h8M-6 0h12M-6 4h6"};

    /* JADX INFO: Access modifiers changed from: package-private */
    public AoaMusic(Context context, Backend backend, HomeCtx homeCtx, HomeData homeData) {
        super(context, backend, homeCtx, homeData, "music", "MUSIC");
        this.m = new JSONObject();
        this.artKey = "";
        this.scr = "now";
        this.mode = "SEEK";
        this.now = System.currentTimeMillis();
        this.tick = new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMusic$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AoaMusic.this.tick();
            }
        };
        this.sendSeek = new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMusic$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                AoaMusic.this.sendSeek();
            }
        };
        this.src = pickSrc(context, backend);
        this.col.getChildAt(0).setVisibility(8);
        this.tabs.setVisibility(8);
        this.cards = new Cards(context);
        this.sv = new ScrollView(context);
        this.sv.setVerticalScrollBarEnabled(false);
        this.sv.setClipToPadding(false);
        this.sv.addView(this.cards);
        this.sv.setPadding(0, 0, 0, px(320.0f));
        this.body.addView(this.sv, new FrameLayout.LayoutParams(-1, -1));
        this.wheel = new Wheel(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(px(300.0f), px(300.0f), 81);
        layoutParams.bottomMargin = px(20.0f);
        this.body.addView(this.wheel, layoutParams);
        TextView mono = mono("", 7.0f, 0.26f, DIM2);
        mono.setTag("hint");
        this.body.addView(mono, new FrameLayout.LayoutParams(-2, -2, 81));
        refresh();
    }

    static MusicSrc pickSrc(Context context, final Backend backend) {
        String str = Store.get("aos.music.fake", "");
        if (Backend.debug() && !str.isEmpty() && !"0".equals(str)) {
            try {
                if (fake == null || !str.equals(String.valueOf(fake.getClass().getField("mode").get(fake)))) {
                    fake = (MusicSrc) Class.forName("com.jarvis.assist.aosui.home.FakeMusic").getConstructor(String.class).newInstance(str);
                }
                return fake;
            } catch (Throwable th) {
            }
        }
        final AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        return new MusicSrc() { // from class: com.jarvis.assist.aosui.home.AoaMusic.1
            @Override // com.jarvis.assist.aosui.home.MusicSrc
            public boolean act(String str2, String str3) {
                return Backend.this.mediaAct(str2, str3);
            }

            @Override // com.jarvis.assist.aosui.home.MusicSrc
            public String media() {
                return Backend.this.media();
            }

            @Override // com.jarvis.assist.aosui.home.MusicSrc
            public int[] vol() {
                return audioManager == null ? new int[]{0, 0} : new int[]{audioManager.getStreamVolume(3), audioManager.getStreamMaxVolume(3)};
            }

            @Override // com.jarvis.assist.aosui.home.MusicSrc
            public void setVol(int i) {
                if (audioManager != null) {
                    try {
                        audioManager.setStreamVolume(3, i, 0);
                    } catch (Exception e) {
                    }
                }
            }
        };
    }

    @Override // com.jarvis.assist.aosui.home.AoaPage
    void showTab(String str) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.AoaPage
    public void refresh() {
        super.refresh();
        try {
            this.m = new JSONObject(this.src.media());
        } catch (Exception e) {
            this.m = new JSONObject();
        }
        String optString = this.m.optString("art");
        if (!optString.equals(this.artKey)) {
            this.artKey = optString;
            this.art = null;
            int indexOf = optString.indexOf(44);
            if (indexOf > 0) {
                try {
                    byte[] decode = Base64.decode(optString.substring(indexOf + 1), 0);
                    this.art = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                } catch (Exception e2) {
                }
            }
        }
        hint();
        this.cards.requestLayout();
        this.cards.invalidate();
        this.wheel.invalidate();
        MAIN.removeCallbacks(this.tick);
        MAIN.postDelayed(this.tick, this.m.optBoolean("playing") ? 250L : 1000L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void tick() {
        if (isAttachedToWindow()) {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.m.optBoolean("playing") && Perf.k().motion) {
                this.orbT += ((float) (currentTimeMillis - this.now)) / 1000.0f;
            }
            this.now = currentTimeMillis;
            int i = this.pollN + 1;
            this.pollN = i;
            if (i % 20 == 0) {
                refresh();
                return;
            }
            this.cards.invalidate();
            this.wheel.invalidate();
            MAIN.postDelayed(this.tick, this.m.optBoolean("playing") ? 250L : 1000L);
        }
    }

    @Override // com.jarvis.assist.aosui.home.AoaPage
    void stop() {
        MAIN.removeCallbacks(this.tick);
        MAIN.removeCallbacks(this.sendSeek);
    }

    void hint() {
        String str;
        TextView textView = (TextView) this.body.findViewWithTag("hint");
        if (textView != null) {
            if ("lib".equals(this.scr)) {
                str = "SCROLL = MOVE · CENTRE = SELECT · MENU = BACK";
            } else {
                str = ("VOL".equals(this.mode) ? "SCROLL = VOLUME" : "QUEUE".equals(this.mode) ? "SCROLL = QUEUE · CENTRE = SKIP TO" : "SCROLL = SEEK") + " · CENTRE = PAUSE · MENU = LIBRARY";
            }
            textView.setText(str);
        }
    }

    boolean access() {
        return this.m.optBoolean("access", true);
    }

    boolean idle() {
        return this.m.optString("title").isEmpty() && this.m.optString("pkg").isEmpty();
    }

    boolean ok() {
        return access() && !idle();
    }

    long dur() {
        return this.m.optLong("durMs");
    }

    long pos() {
        return this.seekMs != null ? this.seekMs.longValue() : OsoWheel.pos(this.m.optLong("posMs"), this.m.optLong("posAt"), this.m.optBoolean("playing"), dur(), this.now);
    }

    int[] volume() {
        int[] vol = this.src.vol();
        if (this.volV != null) {
            vol[0] = this.volV.intValue();
        }
        return vol;
    }

    JSONArray queue() {
        JSONArray optJSONArray = this.m.optJSONArray("queue");
        return optJSONArray == null ? new JSONArray() : optJSONArray;
    }

    ArrayList<JSONObject> upNext() {
        JSONArray queue = queue();
        int length = queue.length();
        long[] jArr = new long[length];
        int i = 0;
        while (true) {
            long j = -1;
            if (i >= length) {
                break;
            }
            if (queue.optJSONObject(i) != null) {
                j = queue.optJSONObject(i).optLong("id");
            }
            jArr[i] = j;
            i++;
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        for (int upNextFrom = OsoWheel.upNextFrom(jArr, this.m.optLong("activeId", -1L)); upNextFrom < queue.length(); upNextFrom++) {
            if (queue.optJSONObject(upNextFrom) != null) {
                arrayList.add(queue.optJSONObject(upNextFrom));
            }
        }
        return arrayList;
    }

    int activeIdx() {
        JSONArray queue = queue();
        long optLong = this.m.optLong("activeId", -1L);
        for (int i = 0; i < queue.length(); i++) {
            if (queue.optJSONObject(i) != null && queue.optJSONObject(i).optLong("id") == optLong) {
                return i;
            }
        }
        return -1;
    }

    String custom(String... strArr) {
        JSONArray optJSONArray = this.m.optJSONArray("custom");
        if (optJSONArray == null) {
            return null;
        }
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                String lowerCase = (optJSONObject.optString("id") + " " + optJSONObject.optString("name")).toLowerCase(Locale.ROOT);
                for (String str : strArr) {
                    if (lowerCase.contains(str)) {
                        return optJSONObject.optString("id");
                    }
                }
            }
        }
        return null;
    }

    static String up(String str) {
        return str == null ? "" : str.toUpperCase(Locale.getDefault());
    }

    void act(String str, String str2) {
        this.src.act(str, str2);
        MAIN.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMusic$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AoaMusic.this.refresh();
            }
        }, 350L);
    }

    void seekBy(int i) {
        if (!this.m.optBoolean("canSeek") || dur() <= 0) {
            return;
        }
        this.seekMs = Long.valueOf(OsoWheel.seek(pos(), i, dur()));
        MAIN.removeCallbacks(this.sendSeek);
        MAIN.postDelayed(this.sendSeek, 300L);
        this.cards.invalidate();
        this.wheel.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$sendSeek$0$com-augma-oso-home-AoaMusic, reason: not valid java name */
    public /* synthetic */ void m223lambda$sendSeek$0$comaugmaosohomeAoaMusic() {
        this.seekMs = null;
        refresh();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendSeek() {
        if (this.seekMs == null) {
            return;
        }
        this.src.act("seek", String.valueOf(this.seekMs.longValue()));
        MAIN.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMusic$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                AoaMusic.this.m223lambda$sendSeek$0$comaugmaosohomeAoaMusic();
            }
        }, 600L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$volBy$1$com-augma-oso-home-AoaMusic, reason: not valid java name */
    public /* synthetic */ void m224lambda$volBy$1$comaugmaosohomeAoaMusic() {
        this.volV = null;
    }

    void volBy(int i) {
        int[] volume = volume();
        if (volume[1] <= 0) {
            return;
        }
        int vol = OsoWheel.vol(volume[0], i, volume[1]);
        this.volV = Integer.valueOf(vol);
        this.src.setVol(vol);
        MAIN.postDelayed(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMusic$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AoaMusic.this.m224lambda$volBy$1$comaugmaosohomeAoaMusic();
            }
        }, 800L);
        this.cards.invalidate();
        this.wheel.invalidate();
    }

    void start(String str) {
        try {
            getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)).addFlags(268435456));
        } catch (Exception e) {
            act("open", SP);
        }
    }

    void step(int i) {
        if ("lib".equals(this.scr)) {
            this.libSel = (((this.libSel + i) % LIB.length) + LIB.length) % LIB.length;
            this.cards.invalidate();
            this.wheel.invalidate();
        } else {
            if ("VOL".equals(this.mode)) {
                volBy(i);
                return;
            }
            if (!"QUEUE".equals(this.mode)) {
                seekBy(i);
                return;
            }
            int size = upNext().size();
            if (size > 0) {
                this.qSel = Math.max(0, Math.min(size - 1, this.qSel + i));
            }
            this.cards.invalidate();
            this.wheel.invalidate();
        }
    }

    void selectRow() {
        String[] strArr = LIB[this.libSel];
        if (strArr[1] != null) {
            start(strArr[1]);
            return;
        }
        JSONArray optJSONArray = this.m.optJSONArray("sessions");
        String optString = this.m.optString("pkg");
        if (optJSONArray == null || optJSONArray.length() <= 1) {
            act("open", SP);
        } else {
            int i = 0;
            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                if (optJSONArray.optString(i2).equals(optString)) {
                    i = i2;
                }
            }
            act("select", optJSONArray.optString((i + 1) % optJSONArray.length()));
        }
        this.scr = "now";
        hint();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00b2, code lost:
    
        if (r0 != null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00c1, code lost:
    
        act("custom", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00bf, code lost:
    
        if (r0 != null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00a1, code lost:
    
        if (r0.equals("VOL") != false) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void tap(int r18) {
        /*
            Method dump skipped, instructions count: 528
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.AoaMusic.tap(int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Cards extends View {
        float contentH;
        float homeR;
        float hx;
        float hy;
        final Paint p;
        final Path pa;
        final RectF rf;

        Cards(Context context) {
            super(context);
            this.p = new Paint(3);
            this.rf = new RectF();
            this.pa = new Path();
            this.contentH = AoaMusic.this.px(600.0f);
        }

        Txt t(Typeface typeface, float f, float f2, int i) {
            Txt txt = new Txt(typeface, AoaMusic.this.dn * f, f2 * f * AoaMusic.this.dn);
            txt.p.setColor(i);
            return txt;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), (int) this.contentH);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float cap;
            float width = getWidth();
            float secHead = secHead(canvas, width, 0.0f, "lib".equals(AoaMusic.this.scr) ? "02 / LIBRARY" : "01 / NOW PLAYING", "lib".equals(AoaMusic.this.scr) ? "PLAYLISTS · ARTISTS · SONGS" : (AoaMusic.this.ok() && AoaMusic.SP.equals(AoaMusic.this.m.optString("pkg"))) ? "SPOTIFY · · LIVE" : AoaMusic.this.ok() ? AoaMusic.up(AoaMusic.this.m.optString("app")) + " · · LIVE" : "SPOTIFY · · OFFLINE");
            if ("lib".equals(AoaMusic.this.scr)) {
                cap = cap(canvas, libCard(canvas, width, secHead), "MENU LIST");
            } else if (!AoaMusic.this.access()) {
                cap = empty(canvas, width, secHead, "NOTIFICATION ACCESS", "ANDROID SHOWS PLAYERS ONLY TO APPS WITH IT · NOTHING LEAVES THE PHONE · TAP THE CENTRE");
            } else if (AoaMusic.this.idle()) {
                cap = empty(canvas, width, secHead, "NOTHING PLAYING", "PRESS PLAY OR OPEN SPOTIFY · IT SHOWS UP HERE WITH ITS QUEUE AND CONTROLS");
            } else {
                cap = cap(canvas, trackCard(canvas, width, secHead), "NOW PLAYING CARD");
                ArrayList<JSONObject> upNext = AoaMusic.this.upNext();
                if (!upNext.isEmpty()) {
                    cap = cap(canvas, queueCard(canvas, width, cap, upNext), "QUEUE LIST");
                }
            }
            float px = (int) (cap + AoaMusic.this.px(8.0f));
            if (Math.abs(px - this.contentH) > 2.0f) {
                this.contentH = px;
                post(new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMusic$Cards$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AoaMusic.Cards.this.requestLayout();
                    }
                });
            }
        }

        float secHead(Canvas canvas, float f, float f2, String str, String str2) {
            float px = f2 + AoaMusic.this.px(12.0f);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(1.0f, AoaMusic.this.dn));
            this.p.setColor(Theme.AOA_ORANGE);
            canvas.drawRect(0.0f, px, AoaMusic.this.px(9.0f), px + AoaMusic.this.px(9.0f), this.p);
            Txt t = t(Fonts.RAJ_600, 11.0f, 0.42f, -1511182);
            t.p.setFakeBoldText(true);
            t.draw(canvas, str, AoaMusic.this.px(17.0f), px + AoaMusic.this.px(4.5f) + (t.cap / 2.0f), -1511182);
            Txt t2 = t(Fonts.MONO, 7.5f, 0.16f, Theme.AOA_TEAL);
            t2.align(Paint.Align.RIGHT);
            t2.draw(canvas, "‹ HOME", f, px + AoaMusic.this.px(4.5f) + (t2.cap / 2.0f), Theme.AOA_TEAL);
            this.homeR = AoaMusic.this.px(14.0f) + px;
            float px2 = px + AoaMusic.this.px(11.0f);
            float f3 = f / 352.0f;
            this.p.setColor(Theme.rgba(200, 214, 226, 0.25f));
            this.pa.rewind();
            float f4 = 3.0f * f3;
            float f5 = px2 + (f3 * 14.0f);
            this.pa.moveTo(f4, f5);
            float f6 = f3 * 349.0f;
            this.pa.quadTo(f3 * 176.0f, px2 + (4.0f * f3), f6, f5);
            canvas.drawPath(this.pa, this.p);
            this.p.setColor(Theme.AOA_ORANGE);
            float f7 = f3 * 2.0f;
            canvas.drawCircle(f4, f5, f7, this.p);
            this.p.setColor(Theme.AOA_TEAL);
            canvas.drawCircle(f6, f5, f7, this.p);
            Txt t3 = t(Fonts.MONO, 7.0f, 0.29f, -1511182);
            t3.align(Paint.Align.CENTER);
            float w = ((t3.w(str2) / f3) + 14.0f) / 2.0f;
            float f8 = 331.0f - w;
            float f9 = (f8 - w) * f3;
            float f10 = (w + f8) * f3;
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(-16381941);
            float f11 = (13.0f * f3) + px2;
            canvas.drawRect(f9, (f3 * 1.0f) + px2, f10, f11, this.p);
            float f12 = f11;
            t3.draw(canvas, str2, f8 * f3, px2 + (10.0f * f3), -1511182);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setColor(Theme.rgba(200, 214, 226, 0.45f));
            float[] fArr = {f9, f10};
            int i = 0;
            while (i < 2) {
                float f13 = fArr[i];
                float f14 = f13 == f9 ? f4 : f3 * (-3.0f);
                float f15 = px2 + f3;
                canvas.drawLine(f13, f15, f13, f12, this.p);
                float f16 = f13 + f14;
                canvas.drawLine(f13, f15, f16, f15, this.p);
                float f17 = f12;
                canvas.drawLine(f13, f17, f16, f12, this.p);
                i++;
                f12 = f17;
            }
            return px2 + AoaMusic.this.px(22.0f);
        }

        void card(Canvas canvas, float f, float f2, float f3) {
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(Theme.rgba(12, 15, 20, 0.86f));
            canvas.drawRect(0.0f, f2, f, f3, this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(1.0f, AoaMusic.this.dn));
            this.p.setColor(AoaMusic.LINE);
            canvas.drawRect(0.0f, f2, f, f3, this.p);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(Theme.AOA_ORANGE);
            canvas.drawRect(AoaMusic.this.px(8.0f), f2 - AoaMusic.this.dn, AoaMusic.this.px(24.0f), f2 + AoaMusic.this.dn, this.p);
            this.p.setColor(-1511182);
            canvas.drawRect(f - AoaMusic.this.px(17.0f), f2 + AoaMusic.this.px(7.0f), f - AoaMusic.this.px(8.0f), f2 + AoaMusic.this.px(7.0f) + AoaMusic.this.dn, this.p);
        }

        float cap(Canvas canvas, float f, String str) {
            Txt t = t(Fonts.MONO, 8.0f, 0.24f, AoaMusic.DIM2);
            float px = f + AoaMusic.this.px(5.0f) + t.cap;
            t.draw(canvas, str, AoaMusic.this.px(1.0f), px, AoaMusic.DIM2);
            return px + AoaMusic.this.px(6.0f);
        }

        float hd(Canvas canvas, float f, float f2, float f3, String str, String str2, String str3, String str4) {
            Txt t = t(Fonts.MONO, 8.0f, 0.22f, -1511182);
            Txt t2 = t(Fonts.MONO, 8.0f, 0.2f, AoaMusic.DIMC);
            t.p.setFakeBoldText(true);
            float f4 = 0.6f * f2;
            t.draw(canvas, WidgetView.fitText(t, str, f4), f, f3 + t.cap, -1511182);
            t2.draw(canvas, WidgetView.fitText(t2, str2, f4), f, f3 + t.line() + AoaMusic.this.px(2.0f) + t2.cap, AoaMusic.DIMC);
            if (!str3.isEmpty()) {
                Txt t3 = t(Fonts.RAJ_400, 22.0f, 0.06f, -1511182);
                Txt t4 = t(Fonts.MONO, 9.0f, 0.13f, AoaMusic.DIMC);
                t3.align(Paint.Align.RIGHT);
                t4.align(Paint.Align.RIGHT);
                float px = (f + f2) - AoaMusic.this.px(14.0f);
                if (!str4.isEmpty()) {
                    t4.draw(canvas, " " + str4, px, f3 + AoaMusic.this.px(2.0f) + t3.cap, AoaMusic.DIMC);
                    px -= t4.w(" " + str4);
                }
                t3.draw(canvas, str3, px, f3 + AoaMusic.this.px(2.0f) + t3.cap, -1511182);
            }
            return f3 + t.line() + t2.line() + AoaMusic.this.px(4.0f);
        }

        float trackCard(Canvas canvas, float f, float f2) {
            float f3;
            float f4;
            Canvas canvas2;
            long j;
            boolean z;
            float f5;
            float px = f2 + AoaMusic.this.px(6.0f);
            float px2 = AoaMusic.this.px(11.0f);
            float px3 = f - AoaMusic.this.px(22.0f);
            float px4 = AoaMusic.this.px(9.0f) + px;
            long dur = AoaMusic.this.dur();
            long max = Math.max(0L, Math.min(Math.max(dur, 0L), AoaMusic.this.pos()));
            float f6 = dur > 0 ? ((float) max) / ((float) dur) : 0.0f;
            int activeIdx = AoaMusic.this.activeIdx();
            int length = AoaMusic.this.queue().length();
            float px5 = AoaMusic.this.px(9.0f) + px + AoaMusic.this.px(32.0f) + AoaMusic.this.px(6.0f) + AoaMusic.this.px(104.0f) + AoaMusic.this.px(26.0f) + AoaMusic.this.px(50.0f) + AoaMusic.this.px(4.0f) + AoaMusic.this.px(9.0f);
            card(canvas, f, px, px5);
            float px6 = AoaMusic.this.px(6.0f) + hd(canvas, px2, px3, px4, activeIdx >= 0 ? "TRACK · " + OsoWheel.pad(activeIdx + 1) + " / " + OsoWheel.pad(length) : "TRACK", AoaMusic.this.m.optString("album").isEmpty() ? AoaMusic.up(AoaMusic.this.m.optString("app")) : "ALBUM · " + AoaMusic.up(AoaMusic.this.m.optString("album")), OsoWheel.fmt(max), "");
            float px7 = AoaMusic.this.px(104.0f);
            float f7 = px2 + px7;
            this.rf.set(px2, px6, f7, px6 + px7);
            if (AoaMusic.this.art != null) {
                f3 = px6;
                canvas.drawBitmap(AoaMusic.this.art, (Rect) null, this.rf, this.p);
                f4 = px7;
            } else {
                f3 = px6;
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(-16118253);
                canvas.drawRect(this.rf, this.p);
                this.p.setStyle(Paint.Style.STROKE);
                f4 = px7;
                this.p.setColor(Theme.rgba(228, 235, 241, 0.55f));
                canvas.drawCircle(this.rf.centerX(), this.rf.centerY(), 0.29f * f4, this.p);
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(-1511182);
                canvas.drawCircle(this.rf.centerX(), this.rf.centerY(), AoaMusic.this.px(3.0f), this.p);
            }
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setColor(AoaMusic.LINE);
            canvas.drawRect(this.rf, this.p);
            float px8 = f7 + AoaMusic.this.px(11.0f);
            float f8 = px2 + px3;
            float f9 = f8 - px8;
            Txt t = t(Fonts.RAJ_600, 15.0f, 0.06f, -1511182);
            t.p.setFakeBoldText(true);
            String[] wrap2 = wrap2(t, AoaMusic.up(AoaMusic.this.m.optString("title")), f9);
            String str = "title";
            int length2 = wrap2.length;
            float f10 = f3;
            for (int i = 0; i < length2; i++) {
                String str2 = wrap2[i];
                float f11 = f9;
                f10 += t.cap + AoaMusic.this.px(4.0f);
                Txt txt = t;
                txt.draw(canvas, str2, px8, f10, -1511182);
                f8 = f8;
                str = str;
                t = txt;
                f9 = f11;
                wrap2 = wrap2;
                length2 = length2;
            }
            float f12 = f8;
            String str3 = str;
            float f13 = f9;
            Txt t2 = t(Fonts.RAJ_400, 10.5f, 0.1f, AoaMusic.DIMC);
            float px9 = f10 + AoaMusic.this.px(6.0f) + t2.cap;
            t2.draw(canvas, WidgetView.fitText(t2, AoaMusic.up(AoaMusic.this.m.optString("artist")) + (AoaMusic.this.m.optString("album").isEmpty() ? "" : " · " + AoaMusic.up(AoaMusic.this.m.optString("album"))), f13), px8, px9, AoaMusic.DIMC);
            if (AoaMusic.this.volume()[1] > 0) {
                float px10 = px9 + AoaMusic.this.px(16.0f);
                Txt t3 = t(Fonts.RAJ_600, 8.0f, 0.2f, -1511182);
                Txt t4 = t(Fonts.MONO, 7.0f, 0.0f, AoaMusic.DIMC);
                t3.p.setFakeBoldText(true);
                j = dur;
                f5 = 7.0f;
                t3.draw(canvas, "VOLUME", px8, px10 + (t3.cap / 2.0f), -1511182);
                float px11 = px8 + AoaMusic.this.px(50.0f);
                float f14 = px8 + f13;
                float px12 = f14 - AoaMusic.this.px(24.0f);
                this.p.setStyle(Paint.Style.FILL);
                this.p.setColor(Theme.rgba(200, 214, 226, 0.1f));
                canvas.drawRect(px11, px10 - AoaMusic.this.dn, px12, px10 + AoaMusic.this.dn, this.p);
                this.p.setColor("VOL".equals(AoaMusic.this.mode) ? Theme.AOA_TEAL : -1511182);
                canvas.drawRect(px11, px10 - AoaMusic.this.dn, (((px12 - px11) * r27[0]) / r27[1]) + px11, px10 + AoaMusic.this.dn, this.p);
                t4.align(Paint.Align.RIGHT);
                z = true;
                canvas2 = canvas;
                t4.draw(canvas2, String.valueOf(Math.round((r27[0] * 100.0f) / r27[1])), f14, px10 + (t4.cap / 2.0f), AoaMusic.DIMC);
            } else {
                canvas2 = canvas;
                j = dur;
                z = true;
                f5 = 7.0f;
            }
            float px13 = f3 + f4 + AoaMusic.this.px(13.0f);
            Txt t5 = t(Fonts.MONO, f5, 0.2f, AoaMusic.DIMC);
            String str4 = OsoWheel.fmt(max) + " / " + OsoWheel.fmt(j);
            float px14 = AoaMusic.this.px(5.0f);
            clock(canvas2, px2 + px14, px13, px14);
            clock(canvas2, f12 - px14, px13, px14);
            float w = t5.w("WAVEFORM · POSITION");
            float w2 = t5.w(str4);
            float px15 = ((((px3 - (px14 * 4.0f)) - w) - w2) - AoaMusic.this.px(36.0f)) / 3.0f;
            float px16 = px2 + (px14 * 2.0f) + AoaMusic.this.px(6.0f);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(AoaMusic.LINE);
            canvas2.drawRect(px16, px13, px16 + px15, AoaMusic.this.dn + px13, this.p);
            float px17 = px16 + px15 + AoaMusic.this.px(6.0f);
            t5.draw(canvas, "WAVEFORM · POSITION", px17, px13 + (t5.cap / 2.0f), AoaMusic.DIMC);
            float px18 = px17 + w + AoaMusic.this.px(6.0f);
            canvas.drawRect(px18, px13, px18 + px15, px13 + AoaMusic.this.dn, this.p);
            float px19 = px18 + px15 + AoaMusic.this.px(6.0f);
            t5.draw(canvas, str4, px19, px13 + (t5.cap / 2.0f), AoaMusic.DIMC);
            float px20 = px19 + w2 + AoaMusic.this.px(6.0f);
            canvas.drawRect(px20, px13, px20 + px15, px13 + AoaMusic.this.dn, this.p);
            float px21 = px13 + AoaMusic.this.px(10.0f);
            float px22 = AoaMusic.this.px(46.0f);
            float f15 = px3 / 330.0f;
            float f16 = px22 / 46.0f;
            int hashCode = AoaMusic.this.m.optString(str3).hashCode();
            this.p.setStrokeWidth(1.4f * f15);
            int i2 = 0;
            while (i2 < 110) {
                float f17 = px2 + (((i2 * 3) + 3) * f15);
                float bar = OsoWheel.bar(i2, hashCode);
                float f18 = i2;
                float f19 = 110 * f6;
                this.p.setColor((f18 > f19 ? 1 : (f18 == f19 ? 0 : -1)) < 0 ? z : false ? Theme.a(-1511182, Math.min(1.0f, ((f18 * 0.6f) / Math.max(1.0f, f19)) + 0.4f)) : Theme.rgba(200, 214, 226, 0.22f));
                float f20 = bar / 2.0f;
                canvas.drawLine(f17, px21 + ((23.0f - f20) * f16), f17, ((f20 + 23.0f) * f16) + px21, this.p);
                i2++;
                z = true;
            }
            float marker = px2 + OsoWheel.marker(f6, 110, px3);
            this.p.setStrokeWidth(Math.max(1.0f, AoaMusic.this.dn));
            this.p.setColor(Theme.AOA_ORANGE);
            canvas.drawLine(marker, px21, marker, px21 + px22, this.p);
            this.p.setStyle(Paint.Style.FILL);
            this.p.setColor(-16381941);
            canvas.drawCircle(marker, px21 + AoaMusic.this.px(3.0f), AoaMusic.this.px(3.0f), this.p);
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setColor(Theme.AOA_ORANGE);
            canvas.drawCircle(marker, px21 + AoaMusic.this.px(3.0f), AoaMusic.this.px(3.0f), this.p);
            float px23 = px21 + px22 + AoaMusic.this.px(6.0f);
            Txt t6 = t(Fonts.MONO, 8.0f, 0.16f, AoaMusic.DIMC);
            t6.draw(canvas, OsoWheel.fmt(max), px2, px23 + t6.cap, AoaMusic.DIMC);
            t6.align(Paint.Align.CENTER);
            t6.draw(canvas, "▼ NOW", px2 + (px3 / 2.0f), px23 + t6.cap, Theme.AOA_ORANGE);
            t6.align(Paint.Align.RIGHT);
            t6.draw(canvas, "−" + OsoWheel.fmt(j - max), f12, px23 + t6.cap, AoaMusic.DIMC);
            return px5;
        }

        void clock(Canvas canvas, float f, float f2, float f3) {
            this.p.setStyle(Paint.Style.STROKE);
            this.p.setStrokeWidth(Math.max(1.0f, AoaMusic.this.dn));
            this.p.setColor(AoaMusic.DIMC);
            canvas.drawCircle(f, f2, f3 - (AoaMusic.this.dn / 2.0f), this.p);
            canvas.drawLine(f, f2 - (0.7f * f3), f + (0.36f * f3), f2 - (f3 * 0.1f), this.p);
        }

        String[] wrap2(Txt txt, String str, float f) {
            String str2;
            if (txt.w(str) <= f) {
                return new String[]{str};
            }
            String[] split = str.split(" ");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < split.length) {
                if (sb.length() == 0) {
                    str2 = split[i];
                } else {
                    str2 = String.valueOf(sb) + " " + split[i];
                }
                if (txt.w(str2) > f) {
                    break;
                }
                sb.setLength(0);
                sb.append(str2);
                i++;
            }
            if (sb.length() == 0) {
                return new String[]{WidgetView.fitText(txt, str, f)};
            }
            StringBuilder sb2 = new StringBuilder();
            while (i < split.length) {
                if (sb2.length() > 0) {
                    sb2.append(' ');
                }
                sb2.append(split[i]);
                i++;
            }
            return new String[]{sb.toString(), WidgetView.fitText(txt, sb2.toString(), f)};
        }

        float queueCard(Canvas canvas, float f, float f2, ArrayList<JSONObject> arrayList) {
            float f3;
            int i;
            int i2;
            int i3;
            float f4;
            int i4;
            float f5;
            float f6;
            float f7;
            Txt txt;
            float px = f2 + AoaMusic.this.px(6.0f);
            float px2 = AoaMusic.this.px(11.0f);
            float px3 = f - AoaMusic.this.px(22.0f);
            float px4 = AoaMusic.this.px(26.0f);
            int min = Math.min(3, arrayList.size());
            float px5 = AoaMusic.this.px(9.0f) + px + AoaMusic.this.px(32.0f) + AoaMusic.this.px(4.0f) + (min * px4) + AoaMusic.this.px(9.0f);
            card(canvas, f, px, px5);
            float hd = hd(canvas, px2, px3, px + AoaMusic.this.px(9.0f), "QUEUE · UP NEXT", AoaMusic.up(AoaMusic.this.m.optString("app")) + " · SESSION QUEUE", String.valueOf(arrayList.size()), "LEFT") + AoaMusic.this.px(4.0f);
            boolean equals = "QUEUE".equals(AoaMusic.this.mode);
            int i5 = equals ? AoaMusic.this.qSel : 0;
            int queueWindow = OsoWheel.queueWindow(i5, arrayList.size(), equals);
            Txt t = t(Fonts.MONO, 7.0f, 0.14f, Theme.AOA_ORANGE);
            Txt t2 = t(Fonts.RAJ_600, 12.0f, 0.08f, -1511182);
            Txt t3 = t(Fonts.MONO, 8.0f, 0.1f, AoaMusic.DIMC);
            t2.p.setFakeBoldText(true);
            int i6 = 0;
            while (i6 < min) {
                int i7 = queueWindow + i6;
                if (i7 >= arrayList.size()) {
                    return px5;
                }
                int i8 = queueWindow;
                JSONObject jSONObject = arrayList.get(i7);
                boolean z = i7 == i5;
                float f8 = (i6 * px4) + hd;
                float f9 = px3;
                if (z) {
                    f3 = hd;
                    this.p.setStyle(Paint.Style.FILL);
                    i = i5;
                    i3 = i7;
                    i2 = i6;
                    float f10 = px4;
                    this.p.setColor(Theme.rgba(78, 160, 186, 0.1f));
                    float f11 = px2 + f9;
                    float f12 = f8 + f10;
                    int i9 = min;
                    f6 = f8;
                    i4 = i9;
                    f5 = px5;
                    f4 = f10;
                    f7 = 8.0f;
                    canvas.drawRect(px2, f6, f11, f12 - AoaMusic.this.px(2.0f), this.p);
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setColor(Theme.rgba(78, 160, 186, 0.45f));
                    canvas.drawRect(px2, f6, f11, f12 - AoaMusic.this.px(2.0f), this.p);
                } else {
                    f3 = hd;
                    i = i5;
                    i2 = i6;
                    i3 = i7;
                    f4 = px4;
                    i4 = min;
                    f5 = px5;
                    f6 = f8;
                    f7 = 8.0f;
                }
                boolean z2 = i3 == 0;
                String pad = z2 ? "NEXT" : OsoWheel.pad(i3 + 1);
                float w = t.w(pad) + AoaMusic.this.px(f7);
                float px6 = ((f4 - AoaMusic.this.px(2.0f)) / 2.0f) + f6;
                this.p.setStyle(Paint.Style.STROKE);
                this.p.setColor(z2 ? Theme.AOA_ORANGE : -11624262);
                canvas.drawRect(px2 + AoaMusic.this.px(2.0f), px6 - AoaMusic.this.px(6.0f), AoaMusic.this.px(2.0f) + px2 + w, px6 + AoaMusic.this.px(6.0f), this.p);
                Txt txt2 = t;
                txt2.draw(canvas, pad, px2 + AoaMusic.this.px(6.0f), px6 + (t.cap / 2.0f), z2 ? Theme.AOA_ORANGE : -11624262);
                float px7 = AoaMusic.this.px(2.0f) + px2 + w + AoaMusic.this.px(f7);
                float f13 = px2 + f9;
                float px8 = (f13 - px7) - AoaMusic.this.px(4.0f);
                String fitText = WidgetView.fitText(t2, AoaMusic.up(jSONObject.optString("title")), px8);
                t2.draw(canvas, fitText, px7, px6 + (t2.cap / 2.0f), -1511182);
                float w2 = (px8 - t2.w(fitText)) - AoaMusic.this.px(f7);
                if (w2 > AoaMusic.this.px(20.0f)) {
                    Txt txt3 = t3;
                    txt3.draw(canvas, WidgetView.fitText(t3, AoaMusic.up(jSONObject.optString("sub")), w2), px7 + t2.w(fitText) + AoaMusic.this.px(f7), px6 + (t3.cap / 2.0f), AoaMusic.DIMC);
                    txt = txt3;
                } else {
                    txt = t3;
                }
                int i10 = i2;
                if (i10 < i4 - 1 && !z) {
                    this.p.setStyle(Paint.Style.FILL);
                    this.p.setColor(AoaMusic.LINE);
                    float f14 = f6 + f4;
                    canvas.drawRect(px2, f14 - AoaMusic.this.dn, f13, f14, this.p);
                }
                int i11 = i10 + 1;
                t3 = txt;
                t = txt2;
                queueWindow = i8;
                px3 = f9;
                i5 = i;
                px4 = f4;
                min = i4;
                px5 = f5;
                i6 = i11;
                hd = f3;
            }
            return px5;
        }

        float libCard(Canvas canvas, float f, float f2) {
            float f3;
            boolean z;
            float f4;
            float f5;
            float f6;
            float f7;
            String str;
            float px = f2 + AoaMusic.this.px(6.0f);
            float px2 = AoaMusic.this.px(11.0f);
            float px3 = f - AoaMusic.this.px(22.0f);
            float px4 = AoaMusic.this.px(32.0f);
            float px5 = AoaMusic.this.px(9.0f) + px + AoaMusic.this.px(32.0f) + AoaMusic.this.px(6.0f) + (AoaMusic.LIB.length * px4) + AoaMusic.this.px(9.0f);
            card(canvas, f, px, px5);
            JSONArray optJSONArray = AoaMusic.this.m.optJSONArray("sessions");
            int length = optJSONArray == null ? 0 : optJSONArray.length();
            float hd = hd(canvas, px2, px3, AoaMusic.this.px(9.0f) + px, "MUSIC · MENU", "SCROLL · SELECT · BACK", length > 0 ? OsoWheel.pad(length) : "", length > 0 ? "SESSIONS" : "") + AoaMusic.this.px(6.0f);
            Txt t = t(Fonts.RAJ_600, 14.0f, 0.2f, -1511182);
            Txt t2 = t(Fonts.MONO, 8.0f, 0.16f, AoaMusic.DIMC);
            boolean z2 = true;
            t.p.setFakeBoldText(true);
            t2.align(Paint.Align.RIGHT);
            int i = 0;
            while (i < AoaMusic.LIB.length) {
                boolean z3 = i == AoaMusic.this.libSel ? z2 : false;
                float f8 = (i * px4) + hd;
                if (z3) {
                    this.p.setStyle(Paint.Style.FILL);
                    z = z2;
                    this.p.setColor(Theme.rgba(255, 122, 26, 0.42f));
                    float f9 = px2 + px3;
                    float f10 = f8 + px4;
                    f3 = px3;
                    f7 = 2.0f;
                    f5 = px5;
                    f4 = px4;
                    canvas.drawRect(px2, f8, f9, f10 - AoaMusic.this.px(2.0f), this.p);
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setColor(Theme.AOA_ORANGE);
                    canvas.drawRect(px2, f8, f9, f10 - AoaMusic.this.px(2.0f), this.p);
                    f6 = f8;
                } else {
                    f3 = px3;
                    z = z2;
                    f4 = px4;
                    f5 = px5;
                    f6 = f8;
                    f7 = 2.0f;
                    if (i < AoaMusic.LIB.length - 1) {
                        this.p.setStyle(Paint.Style.FILL);
                        this.p.setColor(AoaMusic.LINE);
                        float f11 = f6 + f4;
                        canvas.drawRect(px2, f11 - AoaMusic.this.dn, px2 + f3, f11, this.p);
                    }
                }
                float px6 = f6 + ((f4 - AoaMusic.this.px(f7)) / f7);
                t.draw(canvas, AoaMusic.LIB[i][0], px2 + AoaMusic.this.px(8.0f), px6 + (t.cap / f7), -1511182);
                str = "SPOTIFY";
                if ("SOURCES".equals(AoaMusic.LIB[i][0])) {
                    str = AoaMusic.up(AoaMusic.this.m.optString("app").isEmpty() ? "SPOTIFY" : AoaMusic.this.m.optString("app"));
                }
                t2.draw(canvas, str + (z3 ? " ›" : ""), (px2 + f3) - AoaMusic.this.px(8.0f), px6 + (t2.cap / f7), z3 ? -1 : AoaMusic.DIMC);
                i++;
                px4 = f4;
                px5 = f5;
                px3 = f3;
                z2 = z;
            }
            return px5;
        }

        float empty(Canvas canvas, float f, float f2, String str, String str2) {
            float px = f2 + AoaMusic.this.px(6.0f);
            float px2 = AoaMusic.this.px(11.0f);
            float px3 = f - AoaMusic.this.px(22.0f);
            float px4 = px + AoaMusic.this.px(110.0f);
            card(canvas, f, px, px4);
            float hd = hd(canvas, px2, px3, px + AoaMusic.this.px(9.0f), "PLAYER · SPOTIFY", "NO ACTIVE SESSION", "", "") + AoaMusic.this.px(8.0f);
            Txt t = t(Fonts.RAJ_600, 21.0f, 0.06f, -1511182);
            t.p.setFakeBoldText(true);
            t.draw(canvas, WidgetView.fitText(t, str, px3), px2, hd + t.cap, -1511182);
            Txt t2 = t(Fonts.MONO, 8.0f, 0.16f, AoaMusic.DIMC);
            t2.draw(canvas, WidgetView.fitText(t2, str2, px3), px2, hd + t.cap + AoaMusic.this.px(14.0f) + t2.cap, AoaMusic.DIMC);
            return cap(canvas, px4, "NOW PLAYING CARD");
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() == 0) {
                this.hx = motionEvent.getX();
                this.hy = motionEvent.getY();
                return true;
            }
            if (motionEvent.getActionMasked() == 1 && Math.hypot(motionEvent.getX() - this.hx, motionEvent.getY() - this.hy) < AoaMusic.this.px(12.0f) && this.hy < this.homeR && this.hx > getWidth() - AoaMusic.this.px(80.0f)) {
                Feedback.fx(this, 0);
                AoaMusic.this.close();
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public final class Wheel extends View {
        float a0;
        boolean decided;
        final OsoWheel.Dial dial;
        final Path[] icons;
        float knob;
        float last;
        float moved;
        final Paint p;
        final Path pa;
        boolean pressed;
        final RectF rf;
        boolean swipe;
        boolean turning;
        float x0;
        float y0;

        Wheel(Context context) {
            super(context);
            this.p = new Paint(1);
            this.rf = new RectF();
            this.pa = new Path();
            this.icons = new Path[8];
            this.knob = 180.0f;
            this.dial = new OsoWheel.Dial();
            for (int i = 0; i < 8; i++) {
                this.icons[i] = Svg.path(AoaMusic.ICON[i]);
            }
            setContentDescription("Control wheel: turn to seek or change volume; MENU, LIKE, NEXT, OUTPUT, PLAY, EQ, PREV, QUEUE; centre pauses");
        }

        float u() {
            return getWidth() / 340.0f;
        }

        float X(float f) {
            return (getWidth() / 2.0f) + (f * u());
        }

        float Y(float f) {
            return (getHeight() / 2.0f) + (f * u());
        }

        float px(float f, float f2) {
            return (float) (f * Math.sin(Math.toRadians(f2)));
        }

        float py(float f, float f2) {
            return (float) ((-f) * Math.cos(Math.toRadians(f2)));
        }

        Path sec(float f, float f2, float f3, float f4) {
            float u = u();
            float width = getWidth() / 2.0f;
            float height = getHeight() / 2.0f;
            Path path = new Path();
            float f5 = f4 * u;
            RectF rectF = new RectF(width - f5, height - f5, width + f5, f5 + height);
            float f6 = f3 * u;
            RectF rectF2 = new RectF(width - f6, height - f6, width + f6, height + f6);
            path.arcTo(rectF, f - 90.0f, f2 - f, true);
            path.arcTo(rectF2, f2 - 90.0f, f - f2, false);
            path.close();
            return path;
        }

        void text(Canvas canvas, String str, float f, float f2, float f3, Typeface typeface, int i, float f4, float f5, boolean z) {
            Txt txt = new Txt(typeface, u() * f3 * 1.0f, f4 * f3 * u());
            txt.p.setFakeBoldText(z);
            txt.align(Paint.Align.CENTER);
            int save = canvas.save();
            canvas.rotate(f5, X(f), Y(f2));
            txt.draw(canvas, str, X(f), Y(f2) + (txt.cap / 2.0f), i);
            canvas.restoreToCount(save);
        }

        void arcText(Canvas canvas, float f, float f2, float f3, String str, float f4, int i, float f5) {
            Txt txt = new Txt(Fonts.MONO, u() * f4, f5 * f4 * u());
            txt.p.setColor(i);
            txt.align(Paint.Align.LEFT);
            u();
            this.pa.rewind();
            float f6 = -f;
            this.rf.set(X(f6), Y(f6), X(f), Y(f));
            this.pa.addArc(this.rf, f2 - 90.0f, f3 - f2);
            canvas.drawTextOnPath(str, this.pa, Math.max(0.0f, (new PathMeasure(this.pa, false).getLength() - txt.w(str)) / 2.0f), txt.cap / 2.0f, txt.p);
        }

        /* JADX WARN: Removed duplicated region for block: B:52:0x0242  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x025e  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x0266  */
        /* JADX WARN: Removed duplicated region for block: B:81:0x0247  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        protected void onDraw(android.graphics.Canvas r38) {
            /*
                Method dump skipped, instructions count: 2132
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.home.AoaMusic.Wheel.onDraw(android.graphics.Canvas):void");
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            float u = u();
            float x = (motionEvent.getX() - (getWidth() / 2.0f)) / u;
            float y = (motionEvent.getY() - (getHeight() / 2.0f)) / u;
            float hypot = (float) Math.hypot(x, y);
            switch (motionEvent.getActionMasked()) {
                case 0:
                    if (hypot > 175.0f) {
                        return false;
                    }
                    this.pressed = true;
                    this.moved = 0.0f;
                    this.swipe = false;
                    this.decided = false;
                    this.turning = false;
                    this.x0 = motionEvent.getX();
                    this.y0 = motionEvent.getY();
                    this.a0 = OsoWheel.angle(x, y);
                    this.last = this.a0;
                    if (hypot >= 56.0f) {
                        this.knob = this.a0;
                    }
                    invalidate();
                    return true;
                case 1:
                    this.pressed = false;
                    invalidate();
                    if (!this.swipe) {
                        if (this.moved < 6.0f) {
                            AoaMusic.this.tap(OsoWheel.hit((this.x0 - (getWidth() / 2.0f)) / u, (this.y0 - (getHeight() / 2.0f)) / u));
                        }
                        return true;
                    }
                    float x2 = motionEvent.getX() - this.x0;
                    if (Math.abs(x2) > AoaMusic.this.dn * 60.0f) {
                        AoaMusic.this.go(AoaPages.next(AoaMusic.this.kind, x2 < 0.0f ? 1 : -1));
                    }
                    return true;
                case 2:
                    float abs = Math.abs(motionEvent.getX() - this.x0);
                    float abs2 = Math.abs(motionEvent.getY() - this.y0);
                    if (!this.decided && Math.hypot(abs, abs2) >= AoaMusic.this.dn * 24.0f) {
                        this.decided = true;
                        this.swipe = abs > abs2 * 2.0f && this.moved < 15.0f;
                        getParent().requestDisallowInterceptTouchEvent(!this.swipe);
                        if (this.swipe) {
                            this.pressed = false;
                            invalidate();
                        }
                    }
                    if (this.swipe) {
                        return false;
                    }
                    if (hypot > 12.0f) {
                        float angle = OsoWheel.angle(x, y);
                        float wrap = OsoWheel.wrap(angle - this.last);
                        this.last = angle;
                        this.moved += Math.abs(wrap);
                        this.knob = angle;
                        int turn = this.dial.turn(wrap);
                        if (turn != 0) {
                            this.turning = true;
                            Feedback.fx(this, 2);
                            AoaMusic.this.step(turn);
                        }
                        invalidate();
                    }
                    return true;
                case 3:
                    this.pressed = false;
                    invalidate();
                    return true;
                default:
                    return true;
            }
        }
    }
}
