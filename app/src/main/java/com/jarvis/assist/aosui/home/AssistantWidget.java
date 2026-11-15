package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Strings;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: /tmp/augmaos/classes.dex */
final class AssistantWidget extends WidgetView {
    static final int[] CHIPS = {3, 0, 1, 2};
    Txt last;
    final ArrayList<Object[]> lines;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AssistantWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.lines = new ArrayList<>();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "assistant";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return this.h.d.name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void styles() {
        super.styles();
        this.last = new Txt(Fonts.RAJ_400, this.h.zoom * 12.0f, 0.0f);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void bind() {
        this.lines.clear();
        String t = this.h.d.lastReply.isEmpty() ? Strings.t("w.assistant.none") : this.h.d.lastReply.replace('\n', ' ');
        float max = Math.max(40.0f, (this.W - this.padL) - this.padR);
        GreetingWidget.wrap(this.last, new String[]{t}, new int[]{0}, max, this.lines, 3);
        if (this.lines.size() > 2) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : this.lines.get(1)) {
                sb.append((String) ((Object[]) obj)[0]);
            }
            this.lines.remove(2);
            this.lines.set(1, new Object[]{new Object[]{fitText(this.last, sb.toString().trim() + " …", max), 0}});
        }
    }

    float chipH() {
        return this.m9.line() + 4.0f + 4.0f;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 3;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : i == 1 ? Math.max(1, this.lines.size()) * this.last.size * 1.3f : chipH();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        if (i == 0) {
            head(canvas, f, f2, f3, "spark", th().BLUE, this.h.d.name, null);
            return;
        }
        if (i == 1) {
            Iterator<Object[]> it = this.lines.iterator();
            float f5 = f2;
            while (it.hasNext()) {
                float f6 = f;
                for (Object obj : it.next()) {
                    String str = (String) ((Object[]) obj)[0];
                    this.last.draw(canvas, str, f6, (((this.last.size * 1.3f) - this.last.line()) / 2.0f) + f5 + this.last.asc, col(th().INK));
                    f6 += this.last.w(str);
                }
                f5 += this.last.size * 1.3f;
            }
            hit(f, f2, f + f3, f2 + f4, "ai", "Ask " + this.h.d.name);
            return;
        }
        AssistantWidget assistantWidget = this;
        float chipH = assistantWidget.chipH();
        int[] iArr = CHIPS;
        int length = iArr.length;
        int i2 = 0;
        float f7 = f;
        while (i2 < length) {
            int i3 = iArr[i2];
            String t = Strings.t("w.assistant.c" + i3);
            float w = assistantWidget.m9.w(t) + 20.0f;
            float f8 = f7 + w;
            if (f8 <= f + f3 + 0.5f) {
                assistantWidget.p.setShader(null);
                assistantWidget.p.setStyle(Paint.Style.STROKE);
                assistantWidget.p.setStrokeWidth(1.0f);
                assistantWidget.p.setColor(assistantWidget.col(assistantWidget.th().LINE2));
                float f9 = f2 + chipH;
                assistantWidget.rf.set(f7 + 0.5f, f2 + 0.5f, f8 - 0.5f, f9 - 0.5f);
                float f10 = chipH / 2.0f;
                canvas.drawRoundRect(assistantWidget.rf, f10, f10, assistantWidget.p);
                assistantWidget.m9.draw(canvas, t, 10.0f + f7, f2 + f10 + (assistantWidget.m9.cap / 2.0f), assistantWidget.col(assistantWidget.th().INK));
                float f11 = f7;
                assistantWidget.hit(f11, f2, f8, f9, "x:a" + i3, t);
                f7 = f11 + w + 6.0f;
                i2++;
                assistantWidget = this;
            } else {
                return;
            }
        }
    }
}
