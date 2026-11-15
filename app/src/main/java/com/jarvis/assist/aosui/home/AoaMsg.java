package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Telephony;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jarvis.assist.aosui.Backend;
import com.jarvis.assist.aosui.core.AoaPages;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.AppsDrawer;
import com.jarvis.assist.aosui.ui.Kit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AoaMsg extends AoaPage {
    JSONArray all;
    String appTab;
    final HashMap<String, JSONObject> byKey;
    List<AoaPages.Conv> convs;
    AoaPages.Conv open;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AoaMsg(Context context, Backend backend, HomeCtx homeCtx, HomeData homeData) {
        super(context, backend, homeCtx, homeData, "msg", "MESSAGES");
        this.convs = new ArrayList();
        this.byKey = new HashMap<>();
        this.appTab = "all";
        refresh();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.AoaPage
    public void refresh() {
        String str;
        JSONArray jSONArray;
        String str2 = "";
        super.refresh();
        try {
            str = this.be.notifList();
        } catch (Exception e) {
            str = null;
        }
        if (str == null) {
            jSONArray = null;
        } else {
            try {
                jSONArray = new JSONArray(str);
            } catch (Exception e2) {
                this.all = null;
            }
        }
        this.all = jSONArray;
        if (this.all == null && this.d.notifs != null) {
            this.all = this.d.notifs;
        }
        try {
            String defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(getContext());
            if (defaultSmsPackage != null) {
                str2 = defaultSmsPackage;
            }
        } catch (Exception e3) {
        }
        String str3 = str2;
        ArrayList arrayList = new ArrayList();
        this.byKey.clear();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (this.all != null) {
            for (int i = 0; i < this.all.length(); i++) {
                JSONObject optJSONObject = this.all.optJSONObject(i);
                if (optJSONObject != null && AoaPages.conversation(optJSONObject.optString("pkg"), optJSONObject.optString("cat"), optJSONObject.optBoolean("msgStyle"), optJSONObject.optString("title"), optJSONObject.optString("text"), str3)) {
                    this.byKey.put(optJSONObject.optString("key"), optJSONObject);
                    arrayList.add(new String[]{optJSONObject.optString("pkg"), optJSONObject.optString("app"), optJSONObject.optString("title"), optJSONObject.optString("text"), String.valueOf(optJSONObject.optLong("at")), optJSONObject.optString("key")});
                    linkedHashMap.put(optJSONObject.optString("pkg"), optJSONObject.optString("app").toUpperCase(Locale.getDefault()));
                }
            }
        }
        this.convs = AoaPages.group(arrayList);
        this.sub.setText(this.all == null ? "NO ACCESS" : this.convs.size() + " CONVERSATIONS · " + arrayList.size() + " UNREAD");
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new String[]{"all", "ALL"});
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            if (arrayList2.size() >= 5) {
                break;
            } else {
                arrayList2.add(new String[]{(String) entry.getKey(), WidgetView.fitText(new Txt(Fonts.MONO, this.dn * 7.0f, 0.0f), (String) entry.getValue(), px(60.0f))});
            }
        }
        Iterator it = arrayList2.iterator();
        boolean z = false;
        while (it.hasNext()) {
            z |= ((String[]) it.next())[0].equals(this.appTab);
        }
        if (!z) {
            this.appTab = "all";
        }
        setTabs((String[][]) arrayList2.toArray(new String[0]), this.appTab);
        if (this.open != null) {
            Iterator<AoaPages.Conv> it2 = this.convs.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                AoaPages.Conv next = it2.next();
                if (next.pkg.equals(this.open.pkg) && next.who.equals(this.open.who)) {
                    this.open = next;
                    break;
                }
            }
        }
        showTab(this.appTab);
    }

    @Override // com.jarvis.assist.aosui.home.AoaPage
    void showTab(String str) {
        this.appTab = str;
        this.body.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        if (this.all == null) {
            linearLayout.addView(row(null, "NOTIFICATION ACCESS · ALLOW", "ALLOW", true, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMsg$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AoaMsg.this.m219lambda$showTab$0$comaugmaosohomeAoaMsg();
                }
            }));
            this.body.addView(scroll(linearLayout));
            return;
        }
        if (this.open != null) {
            thread(linearLayout);
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        int i = 0;
        for (AoaPages.Conv conv : this.convs) {
            if ("all".equals(str) || conv.pkg.equals(str)) {
                linearLayout.addView(convRow(conv, simpleDateFormat));
                i++;
            }
        }
        if (i == 0) {
            linearLayout.addView(row(null, "NO MESSAGES WAITING", null, false, null));
        }
        this.body.addView(scroll(linearLayout));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showTab$0$com-augma-oso-home-AoaMsg, reason: not valid java name */
    public /* synthetic */ void m219lambda$showTab$0$comaugmaosohomeAoaMsg() {
        this.be.askPerm("notifAccess");
    }

    View convRow(final AoaPages.Conv conv, SimpleDateFormat simpleDateFormat) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(16);
        linearLayout.setPadding(px(2.0f), px(8.0f), px(2.0f), px(8.0f));
        linearLayout.setClickable(true);
        linearLayout.setFocusable(true);
        linearLayout.setBackground(new Drawable() { // from class: com.jarvis.assist.aosui.home.AoaMsg.1
            final Paint p = new Paint();

            {
                this.p.setColor(AoaPage.ln(0.1f));
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                Rect bounds = getBounds();
                canvas.drawRect(bounds.left, bounds.bottom - Math.max(1.0f, AoaMsg.this.dn), bounds.right, bounds.bottom, this.p);
            }

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -3;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }
        });
        TextView mono = mono(AoaPages.initials(conv.who), 10.0f, 0.06f, Theme.AOA_TEAL);
        mono.setGravity(17);
        mono.setBackground(new Drawable() { // from class: com.jarvis.assist.aosui.home.AoaMsg.2
            final Paint p = new Paint(1);

            {
                this.p.setStyle(Paint.Style.STROKE);
                this.p.setStrokeWidth(Math.max(1.0f, AoaMsg.this.dn));
                this.p.setColor(Theme.a(Theme.AOA_TEAL, 0.7f));
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                Rect bounds = getBounds();
                canvas.drawCircle(bounds.exactCenterX(), bounds.exactCenterY(), (Math.min(bounds.width(), bounds.height()) / 2.0f) - AoaMsg.this.dn, this.p);
            }

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -3;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }
        });
        linearLayout.addView(mono, new LinearLayout.LayoutParams(px(36.0f), px(36.0f)));
        LinearLayout linearLayout2 = new LinearLayout(getContext());
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(px(12.0f), 0, px(8.0f), 0);
        TextView mono2 = mono(conv.who.toUpperCase(Locale.getDefault()) + "  ·  " + conv.app.toUpperCase(Locale.getDefault()), 9.0f, 0.12f, -1511182);
        mono2.setSingleLine(true);
        mono2.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout2.addView(mono2);
        TextView mono3 = mono(conv.items.isEmpty() ? "" : conv.items.get(0)[1], 8.5f, 0.04f, -6902604);
        mono3.setSingleLine(true);
        mono3.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout2.addView(mono3, lp(-1, -2, 4.0f));
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(0, -2, 1.0f));
        LinearLayout linearLayout3 = new LinearLayout(getContext());
        linearLayout3.setOrientation(1);
        linearLayout3.setGravity(8388613);
        TextView mono4 = mono(conv.at > 0 ? AoaPages.when(conv.at, System.currentTimeMillis()) : "", 8.0f, 0.1f, -9399652);
        mono4.setSingleLine(true);
        String[] strArr = {"09 NOV", "YDAY", "21:12", "WED", mono4.getText().toString()};
        float f = 0.0f;
        for (int i = 0; i < 5; i++) {
            f = Math.max(f, mono4.getPaint().measureText(strArr[i]));
        }
        mono4.setGravity(8388613);
        linearLayout3.addView(mono4, new LinearLayout.LayoutParams(((int) Math.ceil(f)) + px(6.0f), -2));
        TextView mono5 = mono(String.valueOf(conv.items.size()), 7.5f, 0.0f, -1);
        mono5.setGravity(17);
        mono5.setPadding(px(6.0f), px(2.0f), px(6.0f), px(2.0f));
        mono5.setBackground(Kit.pill(Theme.AOA_ORANGE, Theme.AOA_ORANGE, 0.0f, 0.0f));
        LinearLayout.LayoutParams lp = lp(-2, -2, 5.0f);
        lp.gravity = 8388613;
        linearLayout3.addView(mono5, lp);
        linearLayout.addView(linearLayout3);
        linearLayout.setContentDescription(conv.who + ", " + conv.app + ", " + conv.items.size() + " unread");
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AoaMsg$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AoaMsg.this.m218lambda$convRow$1$comaugmaosohomeAoaMsg(conv, view);
            }
        });
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$convRow$1$com-augma-oso-home-AoaMsg, reason: not valid java name */
    public /* synthetic */ void m218lambda$convRow$1$comaugmaosohomeAoaMsg(AoaPages.Conv conv, View view) {
        Feedback.fx(view, 0);
        this.open = conv;
        showTab(this.appTab);
    }

    void thread(LinearLayout linearLayout) {
        LinearLayout linearLayout2;
        float f;
        AoaMsg aoaMsg;
        final String str;
        final AoaMsg aoaMsg2;
        LinearLayout linearLayout3;
        int i;
        JSONArray jSONArray;
        final AoaMsg aoaMsg3 = this;
        final AoaPages.Conv conv = aoaMsg3.open;
        String str2 = conv.items.isEmpty() ? "" : conv.items.get(0)[0];
        JSONObject jSONObject = aoaMsg3.byKey.get(str2);
        LinearLayout linearLayout4 = new LinearLayout(aoaMsg3.getContext());
        linearLayout4.setGravity(16);
        TextView mono = aoaMsg3.mono("‹ ALL", 8.0f, 0.16f, Theme.AOA_TEAL);
        mono.setPadding(0, aoaMsg3.px(6.0f), aoaMsg3.px(14.0f), aoaMsg3.px(6.0f));
        mono.setClickable(true);
        mono.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AoaMsg$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AoaMsg.this.m220lambda$thread$2$comaugmaosohomeAoaMsg(view);
            }
        });
        linearLayout4.addView(mono);
        linearLayout4.addView(aoaMsg3.mono(conv.who.toUpperCase(Locale.getDefault()) + "  ·  " + conv.app.toUpperCase(Locale.getDefault()), 9.0f, 0.14f, Theme.AOA_ORANGE), new LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout.addView(linearLayout4);
        LinearLayout linearLayout5 = new LinearLayout(aoaMsg3.getContext());
        linearLayout5.setOrientation(1);
        linearLayout5.setPadding(0, aoaMsg3.px(8.0f), 0, aoaMsg3.px(8.0f));
        JSONArray optJSONArray = jSONObject == null ? null : jSONObject.optJSONArray("lines");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        if (optJSONArray == null || optJSONArray.length() <= 0) {
            linearLayout2 = linearLayout5;
            f = 1.0f;
            for (int size = conv.items.size() - 1; size >= 0; size--) {
                linearLayout2.addView(bubble(conv.who, conv.items.get(size)[1], Long.parseLong(conv.items.get(size)[2]), simpleDateFormat));
            }
            aoaMsg = this;
        } else {
            int i2 = 0;
            while (i2 < optJSONArray.length()) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                if (optJSONObject != null) {
                    linearLayout3 = linearLayout5;
                    i = i2;
                    jSONArray = optJSONArray;
                    linearLayout3.addView(aoaMsg3.bubble(optJSONObject.optString("who"), optJSONObject.optString("text"), optJSONObject.optLong("at"), simpleDateFormat));
                } else {
                    linearLayout3 = linearLayout5;
                    i = i2;
                    jSONArray = optJSONArray;
                }
                i2 = i + 1;
                linearLayout5 = linearLayout3;
                optJSONArray = jSONArray;
                aoaMsg3 = this;
            }
            linearLayout2 = linearLayout5;
            f = 1.0f;
            aoaMsg = this;
        }
        LinearLayout linearLayout6 = new LinearLayout(aoaMsg.getContext());
        linearLayout6.setOrientation(1);
        linearLayout6.addView(aoaMsg.scroll(linearLayout2), new LinearLayout.LayoutParams(-1, 0, f));
        if (jSONObject == null || !jSONObject.optBoolean("reply")) {
            str = str2;
            aoaMsg2 = aoaMsg;
        } else {
            LinearLayout linearLayout7 = new LinearLayout(aoaMsg.getContext());
            final EditText editText = new EditText(aoaMsg.getContext());
            editText.setHint("REPLY");
            editText.setTypeface(Fonts.MONO);
            editText.setTextSize(1, 11.0f);
            editText.setTextColor(-1511182);
            editText.setHintTextColor(-9399652);
            editText.setInputType(16385);
            editText.setImeOptions(4);
            editText.setBackground(new AppsDrawer.Brk(aoaMsg.dn, false, null));
            editText.setPadding(aoaMsg.px(12.0f), aoaMsg.px(10.0f), aoaMsg.px(12.0f), aoaMsg.px(10.0f));
            linearLayout7.addView(editText, new LinearLayout.LayoutParams(0, -2, f));
            final TextView mono2 = aoaMsg.mono("↵", 14.0f, 0.0f, Theme.AOA_ORANGE);
            mono2.setGravity(17);
            mono2.setBackground(Kit.pill(Theme.a(Theme.AOA_ORANGE, 0.08f), Theme.AOA_ORANGE, aoaMsg.dn, 0.0f));
            mono2.setContentDescription("Send reply");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(aoaMsg.px(46.0f), -1);
            layoutParams.setMarginStart(aoaMsg.px(8.0f));
            linearLayout7.addView(mono2, layoutParams);
            str = str2;
            final LinearLayout linearLayout8 = linearLayout2;
            final Runnable runnable = new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMsg$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    AoaMsg.this.m221lambda$thread$3$comaugmaosohomeAoaMsg(editText, mono2, str, linearLayout8, simpleDateFormat, conv);
                }
            };
            AoaMsg aoaMsg4 = this;
            mono2.setOnClickListener(new View.OnClickListener() { // from class: com.jarvis.assist.aosui.home.AoaMsg$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    runnable.run();
                }
            });
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.jarvis.assist.aosui.home.AoaMsg$$ExternalSyntheticLambda4
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView, int i3, KeyEvent keyEvent) {
                    return AoaMsg.lambda$thread$5(runnable, textView, i3, keyEvent);
                }
            });
            linearLayout6.addView(linearLayout7, aoaMsg4.lp(-1, -2, 8.0f));
            aoaMsg2 = aoaMsg4;
        }
        linearLayout6.addView(aoaMsg2.button("OPEN IN " + conv.app.toUpperCase(Locale.getDefault()), false, new Runnable() { // from class: com.jarvis.assist.aosui.home.AoaMsg$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                AoaMsg.this.m222lambda$thread$6$comaugmaosohomeAoaMsg(str);
            }
        }), aoaMsg2.lp(-1, -2, 8.0f));
        FrameLayout panel = aoaMsg2.panel(linearLayout6, 10);
        linearLayout6.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, 0, 1.0f);
        layoutParams2.topMargin = aoaMsg2.px(8.0f);
        linearLayout.addView(panel, layoutParams2);
        aoaMsg2.body.addView(linearLayout, new FrameLayout.LayoutParams(-1, -1));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$thread$2$com-augma-oso-home-AoaMsg, reason: not valid java name */
    public /* synthetic */ void m220lambda$thread$2$comaugmaosohomeAoaMsg(View view) {
        Feedback.fx(view, 0);
        this.open = null;
        showTab(this.appTab);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$thread$3$com-augma-oso-home-AoaMsg, reason: not valid java name */
    public /* synthetic */ void m221lambda$thread$3$comaugmaosohomeAoaMsg(EditText editText, TextView textView, String str, LinearLayout linearLayout, SimpleDateFormat simpleDateFormat, AoaPages.Conv conv) {
        String trim = editText.getText().toString().trim();
        if (trim.isEmpty()) {
            return;
        }
        Feedback.fx(textView, 16);
        if (this.be.notifReply(str, trim)) {
            editText.setText("");
            linearLayout.addView(bubble("YOU", trim, System.currentTimeMillis(), simpleDateFormat));
        } else {
            this.be.toast("COULDN'T REPLY · OPEN IN " + conv.app.toUpperCase(Locale.getDefault()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$thread$5(Runnable runnable, TextView textView, int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        runnable.run();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$thread$6$com-augma-oso-home-AoaMsg, reason: not valid java name */
    public /* synthetic */ void m222lambda$thread$6$comaugmaosohomeAoaMsg(String str) {
        this.be.notifOpen(str);
        this.open = null;
    }

    View bubble(String str, String str2, long j, SimpleDateFormat simpleDateFormat) {
        boolean equals = "YOU".equals(str);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setPadding(px(10.0f), px(6.0f), px(10.0f), px(7.0f));
        linearLayout.setBackground(Kit.pill(equals ? Theme.rgba(255, 122, 26, 0.08f) : 0, equals ? Theme.rgba(255, 122, 26, 0.45f) : Theme.a(Theme.AOA_TEAL, 0.5f), this.dn, 0.0f));
        linearLayout.addView(mono((str.isEmpty() ? "" : str.toUpperCase(Locale.getDefault()) + " · ") + (j > 0 ? simpleDateFormat.format(new Date(j)) : ""), 6.5f, 0.16f, -9399652));
        TextView mono = mono(str2, 9.5f, 0.03f, -1511182);
        mono.setLineSpacing(0.0f, 1.25f);
        linearLayout.addView(mono, lp(-2, -2, 3.0f));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = px(8.0f);
        layoutParams.gravity = equals ? 8388613 : 8388611;
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }
}
