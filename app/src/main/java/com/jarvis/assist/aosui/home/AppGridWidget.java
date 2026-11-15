package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import com.jarvis.assist.aosui.core.AppGridModel;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Strings;
import com.jarvis.assist.aosui.core.Theme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class AppGridWidget extends WidgetView {
    List<String> apps;
    final Runnable inval;
    Txt lab;
    final HashMap<String, String> labels;
    Txt plus;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppGridWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.apps = new ArrayList();
        this.labels = new HashMap<>();
        this.inval = new Runnable() { // from class: com.jarvis.assist.aosui.home.AppGridWidget$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AppGridWidget.this.invalidate();
            }
        };
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return AppGridModel.ID;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return Strings.t("w.appgrid.src");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void styles() {
        super.styles();
        float f = this.h.zoom;
        this.lab = new Txt(Fonts.RAJ_600, Math.max(8.0f, 9.5f * f), 0.3f).align(Paint.Align.CENTER);
        this.plus = new Txt(Fonts.RAJ_400, f * 18.0f, 0.0f).align(Paint.Align.CENTER);
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void bind() {
        this.apps = AppGridModel.parse(Store.get(AppGridModel.KEY, ""));
        this.labels.clear();
        String appsCached = this.h.d.be.appsCached();
        if (appsCached != null) {
            try {
                JSONArray jSONArray = new JSONArray(appsCached);
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        this.labels.put(optJSONObject.optString("pkg") + "/" + optJSONObject.optString("cls"), optJSONObject.optString("label"));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    String label(String str) {
        String str2 = this.labels.get(str);
        if (str2 != null && !str2.isEmpty()) {
            return str2;
        }
        String substring = str.substring(0, str.indexOf(47));
        int lastIndexOf = substring.lastIndexOf(46);
        return lastIndexOf >= 0 ? substring.substring(lastIndexOf + 1) : substring;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : (((this.H - this.padT) - this.padB) - headH()) - 6.0f;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String spoken() {
        StringBuilder append = new StringBuilder(Strings.t("w.appgrid.h")).append(". ");
        Iterator<String> it = this.apps.iterator();
        while (it.hasNext()) {
            append.append(label(it.next())).append(". ");
        }
        if (this.apps.isEmpty()) {
            append.append(Strings.t("w.appgrid.empty"));
        }
        return append.toString().trim();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference failed for: r10v3 */
    /* JADX WARN: Type inference failed for: r10v6 */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        boolean z;
        r10 != null ? r10 : null;
        Shader shader = null;
        if (i == 0) {
            head(canvas, f, f2, f3, "grid", th().BLUE, Strings.t("w.appgrid.h"), this.apps.isEmpty() ? null : String.valueOf(this.apps.size()));
            return;
        }
        Canvas canvas2 = canvas;
        AppGridWidget appGridWidget = this;
        int[] grid = AppGridModel.grid(appGridWidget.apps.size());
        int i2 = grid[0];
        boolean z2 = true;
        int i3 = grid[1];
        float f5 = f3 / i2;
        float f6 = f4 / i3;
        float max = Math.max(18.0f, Math.min(40.0f, Math.min(0.6f * f5, (f6 - appGridWidget.lab.line()) - 6.0f)));
        int i4 = 0;
        while (i4 < i2 * i3) {
            float f7 = f5 / 2.0f;
            float f8 = f + ((i4 % i2) * f5) + f7;
            float line = f2 + ((i4 / i2) * f6) + ((((f6 - max) - 3.0f) - appGridWidget.lab.line()) / 2.0f);
            if (i4 < appGridWidget.apps.size()) {
                String str = appGridWidget.apps.get(i4);
                int indexOf = str.indexOf(47);
                z = z2;
                String label = appGridWidget.label(str);
                appGridWidget.p.setShader(shader);
                appGridWidget.p.setStyle(Paint.Style.FILL);
                appGridWidget.p.setColor(appGridWidget.col(Theme.a(-3615006, 0.06f)));
                float f9 = max / 2.0f;
                canvas2.drawCircle(f8, line + f9, f9, appGridWidget.p);
                Bitmap bitmap = IconCache.get(appGridWidget.getContext()).get(str.substring(0, indexOf), str.substring(indexOf + 1), Math.round(appGridWidget.h.g.S * max), appGridWidget.inval);
                if (bitmap != null) {
                    appGridWidget.p.setAlpha(Math.round(appGridWidget.alpha * 255.0f));
                    appGridWidget.rf.set(f8 - f9, line, f8 + f9, line + max);
                    canvas2.drawBitmap(bitmap, (Rect) null, appGridWidget.rf, appGridWidget.p);
                    appGridWidget.p.setAlpha(255);
                }
                float f10 = line + max + 3.0f;
                appGridWidget.lab.draw(canvas2, fitText(appGridWidget.lab, label, f5 - 4.0f), f8, appGridWidget.lab.asc + f10, appGridWidget.col(appGridWidget.th().INK));
                appGridWidget.hit(f8 - f7, line, f8 + f7, f10 + appGridWidget.lab.line(), "g" + i4, label);
                r10 = null;
            } else {
                Canvas canvas3 = canvas2;
                z = z2;
                r10 = null;
                appGridWidget.p.setShader(null);
                appGridWidget.p.setStyle(Paint.Style.STROKE);
                appGridWidget.p.setStrokeWidth(1.0f);
                appGridWidget.p.setColor(appGridWidget.col(appGridWidget.th().LINE2));
                float f11 = max / 2.0f;
                float f12 = line + f11;
                canvas3.drawCircle(f8, f12, f11 - 0.5f, appGridWidget.p);
                appGridWidget.plus.draw(canvas3, "+", f8, f12 + (appGridWidget.plus.cap / 2.0f), appGridWidget.col(appGridWidget.th().DIM));
                if (i4 == appGridWidget.apps.size()) {
                    appGridWidget.hit(f8 - f7, line, f8 + f7, line + max + 3.0f + appGridWidget.lab.line(), "x:appgrid", Strings.t("w.appgrid.add"));
                }
            }
            i4++;
            appGridWidget = this;
            canvas2 = canvas;
            shader = r10;
            z2 = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String longTap(float f, float f2) {
        return "x:appgrid";
    }
}
