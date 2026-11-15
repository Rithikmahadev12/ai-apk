package com.jarvis.assist.core;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Fonts — AugmaOS font stack
 *   BRAND  = Rajdhani 700   (titles, wordmarks)
 *   BODY   = Rajdhani 400   (subtitles, body)
 *   MONO   = ShareTechMono  (data, HUD readouts, code)
 */
public class Fonts {

    public static Typeface BRAND;
    public static Typeface BODY;
    public static Typeface MONO;

    public static void init(Context ctx) {
        try {
            BRAND = Typeface.createFromAsset(ctx.getAssets(), "fonts/rajdhani_700.ttf");
            BODY  = Typeface.createFromAsset(ctx.getAssets(), "fonts/rajdhani_400.ttf");
            MONO  = Typeface.createFromAsset(ctx.getAssets(), "fonts/sharetechmono_400.ttf");
        } catch (Exception e) {
            BRAND = Typeface.create("sans-serif-condensed", Typeface.BOLD);
            BODY  = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
            MONO  = Typeface.MONOSPACE;
        }
    }
}
