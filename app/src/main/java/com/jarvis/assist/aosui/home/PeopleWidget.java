package com.jarvis.assist.aosui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Base64;
import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.MemPolicy;
import com.jarvis.assist.aosui.core.Strings;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
final class PeopleWidget extends WidgetView {
    final Paint bp;
    Txt ini;
    Txt m7;
    String photoKey;
    final ArrayList<Bitmap> photos;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PeopleWidget(Context context, HomeCtx homeCtx) {
        super(context, homeCtx);
        this.photos = new ArrayList<>();
        this.bp = new Paint(3);
        this.photoKey = "";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String id() {
        return "people";
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public String source() {
        return "STARRED CONTACTS";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void styles() {
        super.styles();
        float f = this.h.zoom;
        this.ini = new Txt(Fonts.RAJ_600, 10.5f * f, 0.0f).align(Paint.Align.CENTER);
        float f2 = f * 7.0f;
        this.m7 = new Txt(Fonts.MONO, Math.max(8.0f, f2), f2 * 0.14f).align(Paint.Align.CENTER);
    }

    JSONArray people() {
        JSONObject jSONObject = this.h.d.starred;
        JSONArray optJSONArray = jSONObject == null ? null : jSONObject.optJSONArray("people");
        return optJSONArray == null ? new JSONArray() : optJSONArray;
    }

    boolean ok() {
        return this.h.d.starred != null && this.h.d.starred.optBoolean("ok");
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void bind() {
        JSONArray people = people();
        String jSONArray = people.toString();
        if (jSONArray.equals(this.photoKey)) {
            return;
        }
        this.photoKey = jSONArray;
        this.photos.clear();
        for (int i = 0; i < people.length(); i++) {
            String optString = people.optJSONObject(i) == null ? "" : people.optJSONObject(i).optString("photo");
            Bitmap bitmap = null;
            try {
                int indexOf = optString.indexOf("base64,");
                if (indexOf > 0) {
                    bitmap = decode(Base64.decode(optString.substring(indexOf + 7), 0), Math.round(Math.max(1.0f, this.h.g.S) * 30.0f));
                }
            } catch (Exception e) {
            }
            this.photos.add(bitmap);
        }
    }

    static Bitmap decode(byte[] bArr, int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        if (options.outWidth <= 0) {
            return null;
        }
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = MemPolicy.sample(options.outWidth, options.outHeight, i);
        options2.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jarvis.assist.aosui.home.WidgetView
    public void trim() {
        this.photos.clear();
        this.photoKey = "";
    }

    int cols() {
        return Math.max(1, (int) Math.floor(((this.W - this.padL) - this.padR) / 52.0f));
    }

    int shown() {
        return Math.min(Math.min(this.h.g.ph < 150.0f ? 4 : cols(), cols()), people().length());
    }

    float app() {
        return Math.max(24.0f, Math.min(30.0f, this.h.g.pw * 0.09f));
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected int rows() {
        return 2;
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected float rowH(int i) {
        return i == 0 ? headH() : (!ok() || people().length() == 0) ? this.m9.line() : ((this.H - this.padT) - this.padB) - headH();
    }

    @Override // com.jarvis.assist.aosui.home.WidgetView
    protected void drawRow(Canvas canvas, int i, float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        float f7;
        float f8;
        if (i == 0) {
            head(canvas, f, f2, f3, "phone", th().BLUE, Strings.t("w.people.h"), null);
            return;
        }
        if (!ok()) {
            msg(canvas, f, f2, f3, f4, Strings.t("w.people.perm"), "x:contacts", th().DIM);
            return;
        }
        if (people().length() == 0) {
            msg(canvas, f, f2, f3, f4, Strings.t("w.people.none"), null, th().DIM);
            return;
        }
        PeopleWidget peopleWidget = this;
        Canvas canvas2 = canvas;
        float cols = f3 / peopleWidget.cols();
        float app = peopleWidget.app();
        float f9 = 2.0f;
        float line = app + 2.0f + peopleWidget.m7.line();
        float f10 = f2 + ((f4 - line) / 2.0f);
        int i2 = 0;
        while (i2 < peopleWidget.shown()) {
            JSONObject optJSONObject = peopleWidget.people().optJSONObject(i2);
            if (optJSONObject == null) {
                f6 = cols;
                f7 = f9;
                f8 = f10;
            } else {
                float f11 = cols / f9;
                float f12 = f + (i2 * cols) + f11;
                float f13 = app / f9;
                float f14 = f10 + f13;
                String optString = optJSONObject.optString("name");
                Bitmap bitmap = i2 < peopleWidget.photos.size() ? peopleWidget.photos.get(i2) : null;
                if (bitmap == null) {
                    f5 = f13;
                    f6 = cols;
                    f7 = f9;
                } else {
                    f7 = f9;
                    int save = canvas2.save();
                    canvas2.translate(f12 - f13, f14 - f13);
                    float min = app / Math.min(bitmap.getWidth(), bitmap.getHeight());
                    canvas2.scale(min, min);
                    f5 = f13;
                    f6 = cols;
                    peopleWidget.bp.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    peopleWidget.bp.setAlpha(Math.round(peopleWidget.alpha * 255.0f));
                    canvas2.drawCircle(bitmap.getWidth() / f7, bitmap.getHeight() / f7, Math.min(bitmap.getWidth(), bitmap.getHeight()) / f7, peopleWidget.bp);
                    canvas2.restoreToCount(save);
                }
                peopleWidget.p.setShader(null);
                peopleWidget.p.setStyle(Paint.Style.STROKE);
                peopleWidget.p.setStrokeWidth(1.0f);
                peopleWidget.p.setColor(peopleWidget.col(peopleWidget.th().LINE2));
                canvas2.drawCircle(f12, f14, f5 - 0.5f, peopleWidget.p);
                if (bitmap == null) {
                    peopleWidget.ini.draw(canvas2, HomeRules.initials(optString), f12, f14 + (peopleWidget.ini.cap / f7), peopleWidget.col(peopleWidget.th().INK));
                }
                String upperCase = optString.toUpperCase(Locale.ROOT);
                Txt txt = peopleWidget.m7;
                if (upperCase.length() > 9) {
                    upperCase = upperCase.substring(0, 9);
                }
                txt.draw(canvas, upperCase, f12, peopleWidget.m7.asc + f10 + app + f7, peopleWidget.col(peopleWidget.th().DIM));
                f8 = f10;
                peopleWidget.hit(f12 - f11, f8, f12 + f11, f10 + line, "x:p" + i2, optString);
            }
            i2++;
            peopleWidget = this;
            canvas2 = canvas;
            f9 = f7;
            f10 = f8;
            cols = f6;
        }
    }
}
