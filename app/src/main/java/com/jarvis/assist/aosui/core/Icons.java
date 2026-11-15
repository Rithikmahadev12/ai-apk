package com.jarvis.assist.aosui.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.jarvis.assist.aosui.ui.Kit;
import java.util.HashMap;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Icons {
    static final String[][] SRC = {new String[]{"heart", "M8 13.5S2 9.8 2 5.8A3 3 0 0 1 8 4.6a3 3 0 0 1 6 1.2c0 4-6 7.7-6 7.7z"}, new String[]{"weather", "M4.5 12h7a2.5 2.5 0 0 0 .3-5A3.6 3.6 0 0 0 5 6.4 2.8 2.8 0 0 0 4.5 12zM11 2.5v1M14 4l-.7.7M8.3 4l.6.6"}, new String[]{"check", Kit.CHECK}, new String[]{"cal", "M2.5 4h11v9.5h-11zM2.5 7h11M5.5 2.5v3M10.5 2.5v3"}, new String[]{"gear", "M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM8 1.5v2M8 12.5v2M1.5 8h2M12.5 8h2M3.4 3.4l1.4 1.4M11.2 11.2l1.4 1.4M3.4 12.6l1.4-1.4M11.2 4.8l1.4-1.4"}, new String[]{"phone", "M3 2.5h3l1.2 3-1.8 1.2a8 8 0 0 0 3.9 3.9l1.2-1.8 3 1.2v3c0 .6-.5 1-1 1A11.5 11.5 0 0 1 2 3.5c0-.5.4-1 1-1z"}, new String[]{"msg", "M2.5 3h11v7.5h-6l-3 2.5v-2.5h-2z"}, new String[]{"grid", "M2.5 2.5h4v4h-4zM9.5 2.5h4v4h-4zM2.5 9.5h4v4h-4zM9.5 9.5h4v4h-4z"}, new String[]{"cam", "M2 5h3l1.2-2h3.6L11 5h3v8H2zM8 6.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5z"}, new String[]{"wifi", "M1.5 6a9.5 9.5 0 0 1 13 0M3.7 8.4a6.4 6.4 0 0 1 8.6 0M6 10.8a3 3 0 0 1 4 0M8 13.2h0"}, new String[]{"bt", "M4.5 5l7 6-3.5 3V2l3.5 3-7 6"}, new String[]{"torch", "M5 2h6v3l-1.5 2.5V14h-3V7.5L5 5zM5 5h6M8 9.5v1.5"}, new String[]{"bell", "M4 11V7a4 4 0 0 1 8 0v4l1.2 1.5H2.8zM6.5 14h3M2 2l12 12"}, new String[]{"rot", "M13 8a5 5 0 1 1-1.5-3.5M13 2.5v3h-3"}, new String[]{"moon", "M13 9.5A5.5 5.5 0 1 1 6.5 3a4.3 4.3 0 0 0 6.5 6.5z"}, new String[]{"globe", "M8 2a6 6 0 1 0 0 12A6 6 0 0 0 8 2zM2 8h12M8 2c2 2 2 10 0 12M8 2c-2 2-2 10 0 12"}, new String[]{"batt", "M2 5h10.5v6H2zM12.5 7h1.5v2h-1.5M3.5 6.5h6v3h-6z"}, new String[]{"sun", "M8 5a3 3 0 1 0 0 6 3 3 0 0 0 0-6zM8 1v2M8 13v2M1 8h2M13 8h2M3 3l1.4 1.4M11.6 11.6L13 13M3 13l1.4-1.4M11.6 4.4L13 3"}, new String[]{"cloud", "M4.5 12.5h7.5a3 3 0 0 0 .3-6A4.2 4.2 0 0 0 4.2 7a2.8 2.8 0 0 0 .3 5.5z"}, new String[]{"rain", "M4.5 10h7.5a3 3 0 0 0 .3-6A4.2 4.2 0 0 0 4.2 4.5 2.8 2.8 0 0 0 4.5 10zM5.5 12l-.8 2M8.5 12l-.8 2M11.5 12l-.8 2"}, new String[]{"storm", "M4.5 10h7.5a3 3 0 0 0 .3-6A4.2 4.2 0 0 0 4.2 4.5 2.8 2.8 0 0 0 4.5 10zM8.5 10.5l-1.8 2.5h2.6l-1.8 2.5"}, new String[]{"snow", "M4.5 10h7.5a3 3 0 0 0 .3-6A4.2 4.2 0 0 0 4.2 4.5 2.8 2.8 0 0 0 4.5 10zM5.5 12.5h0M8 13.5h0M10.5 12.5h0M6.7 15h0M9.3 15h0"}, new String[]{"fog", "M2.5 6h11M1.5 9h13M3.5 12h9"}, new String[]{"image", "M2.5 3h11v10h-11zM2.5 11l3.5-3.5 3 3 2-2 2.5 2.5M10.5 5.5a1 1 0 1 0 0 2 1 1 0 0 0 0-2z"}, new String[]{"clock", "M8 2a6 6 0 1 0 0 12A6 6 0 0 0 8 2zM8 4.5V8l2.5 1.5"}, new String[]{"music", "M6 12V3.5l7-1.5v8.5M6 12a2 2 0 1 1-4 0 2 2 0 0 1 4 0zM13 10.5a2 2 0 1 1-4 0 2 2 0 0 1 4 0z"}, new String[]{"pin", "M8 14.5s-4.5-4.6-4.5-8a4.5 4.5 0 0 1 9 0c0 3.4-4.5 8-4.5 8zM8 4.8a1.7 1.7 0 1 0 0 3.4 1.7 1.7 0 0 0 0-3.4z"}, new String[]{"folder", "M2 4h4.5l1.5 1.5h6V13H2z"}, new String[]{"pulse", "M1.5 8.5h3l1.5-4 3 8 1.5-4h4"}, new String[]{"flame", "M8 14.5a4.5 4.5 0 0 0 4.5-4.5c0-3-3-4-3-7.5-2 1.5-3 3.5-2.5 5.5-1-.5-1.5-1.5-1.5-2.5C4 7 3.5 8.5 3.5 10A4.5 4.5 0 0 0 8 14.5z"}, new String[]{"drawer", "M3 3h2v2H3zM7 3h2v2H7zM11 3h2v2h-2zM3 7h2v2H3zM7 7h2v2H7zM11 7h2v2h-2zM3 11h2v2H3zM7 11h2v2H7zM11 11h2v2h-2z"}, new String[]{"spark", "M8 1.5l1.6 4.9L14.5 8l-4.9 1.6L8 14.5l-1.6-4.9L1.5 8l4.9-1.6z"}, new String[]{"mail", "M2 4h12v8.5H2zM2 4.5l6 4.5 6-4.5"}, new String[]{"lock", "M4 7.5h8v6.5H4zM5.5 7.5V5a2.5 2.5 0 0 1 5 0v2.5"}, new String[]{"home", "M2.5 7.5L8 3l5.5 4.5M4 6.5V13h3v-3.5h2V13h3V6.5"}, new String[]{"play", "M5 3.5v9l7.5-4.5z"}, new String[]{"stop", "M4.5 4.5h7v7h-7z"}, new String[]{"list", "M5.5 4h8M5.5 8h8M5.5 12h8M2.5 4h0M2.5 8h0M2.5 12h0"}, new String[]{"back", Kit.LEFT}, new String[]{"notif", "M4 11V7a4 4 0 0 1 8 0v4l1.2 1.5H2.8zM6.5 14h3"}};
    static final HashMap<String, Path> P = new HashMap<>();
    static final Path EMPTY = new Path();

    private Icons() {
    }

    public static synchronized Path get(String str) {
        synchronized (Icons.class) {
            Path path = P.get(str);
            if (path != null) {
                return path;
            }
            for (String[] strArr : SRC) {
                if (strArr[0].equals(str)) {
                    Path path2 = Svg.path(strArr[1]);
                    P.put(str, path2);
                    return path2;
                }
            }
            return "grid".equals(str) ? EMPTY : get("grid");
        }
    }

    public static void draw(Canvas canvas, Paint paint, String str, float f, float f2, float f3, int i, float f4) {
        Path path = get(str);
        float f5 = f3 / 16.0f;
        int save = canvas.save();
        float f6 = f3 / 2.0f;
        canvas.translate(f - f6, f2 - f6);
        canvas.scale(f5, f5);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(f4 / f5);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(i);
        canvas.drawPath(path, paint);
        canvas.restoreToCount(save);
    }

    public static void draw(Canvas canvas, Paint paint, String str, float f, float f2, float f3, int i) {
        draw(canvas, paint, str, f, f2, f3, i, 1.3f);
    }
}
