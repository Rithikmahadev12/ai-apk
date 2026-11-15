package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import com.jarvis.assist.aosui.core.Strings;

/* loaded from: /tmp/augmaos/classes.dex */
final class PhoneWidget extends WidgetView {
    final int awId;
    final String wid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PhoneWidget(Context context, HomeCtx homeCtx, String str) {
        super(context, homeCtx);
        int i;
        this.wid = str;
        try {
            i = Integer.parseInt(str.substring(3));
        } catch (Exception e) {
            i = -1;
        }
        this.awId = i;
        this.animRows = false;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return this.wid;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return Strings.t("w.aw.src");
    }

    String label() {
        String str = this.h.d.awLabel.get(this.wid);
        return str == null ? "WIDGET" : str;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 1;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return headH();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void layoutRows() {
        this.rowY[0] = this.padT;
        this.rowHs[0] = headH();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        head(canvas, f, f2, f3, "grid", th().BLUE, label(), null);
    }
}
