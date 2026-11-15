package com.jarvis.assist.aosui.core;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class DayPart {
    public static final int EVENING = 1020;

    private DayPart() {
    }

    public static int eveFrom(String str) {
        try {
            Matcher matcher = Pattern.compile("^\\s*(\\d{1,2})[:.](\\d{2})").matcher(json(str).optString("at", ""));
            if (!matcher.find()) {
                return EVENING;
            }
            int parseInt = Integer.parseInt(matcher.group(1));
            int parseInt2 = Integer.parseInt(matcher.group(2));
            return (parseInt >= 24 || parseInt2 >= 60) ? EVENING : (parseInt * 60) + parseInt2;
        } catch (Exception e) {
            return EVENING;
        }
    }

    static JSONObject json(String str) {
        JSONObject jSONObject;
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    jSONObject = new JSONObject(str);
                    return jSONObject;
                }
            } catch (Exception e) {
                return new JSONObject();
            }
        }
        jSONObject = new JSONObject();
        return jSONObject;
    }

    public static boolean evening(int i, int i2) {
        return i >= i2;
    }

    public static String greetKey(int i, int i2) {
        return i < 300 ? "greet.night" : evening(i, i2) ? "greet.evening" : i < 720 ? "greet.morning" : "greet.afternoon";
    }

    public static int minuteOfDay(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return (calendar.get(11) * 60) + calendar.get(12);
    }
}
