package com.jarvis.assist.aosui.core;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import com.jarvis.assist.R;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Fonts {
    public static Typeface MONO;
    public static Typeface RAJ_400;
    public static Typeface RAJ_600;

    private Fonts() {
    }

    public static synchronized void init(Context context) {
        synchronized (Fonts.class) {
            if (MONO != null) {
                return;
            }
            RAJ_600 = load(context, R.font.rajdhani_700, Typeface.create("sans-serif-condensed", 1));
            RAJ_400 = load(context, R.font.rajdhani_400, Typeface.create("sans-serif-condensed", 0));
            MONO = load(context, R.font.sharetechmono_400, Typeface.MONOSPACE);
        }
    }

    static Typeface load(Context context, int i, Typeface typeface) {
        try {
            Typeface font = context.getResources().getFont(i);
            return font == null ? typeface : font;
        } catch (RuntimeException e) {
            Log.w("aOS", "font " + i + " not loaded, using a system face");
            return typeface;
        }
    }
}
