package com.jarvis.assist.aosui;

public class Weather {

    public Weather() { }
    static void clearKey(Object p0, Object p1) { }
    static org.json.JSONObject day(Object p0, Object p1, Object p2, Object p3) { return null; }
    static java.lang.String err(Object p0) { return null; }
    static org.json.JSONObject errJ(Object p0) { return null; }
    static void fetch(Object p0, Object p1, Object p2, Object p3, Object p4) { }
    static org.json.JSONObject fetchWith(Object p0, Object p1, Object p2, Object p3, Object p4) { return null; }
    static java.lang.String field(Object p0) { return null; }
    static org.json.JSONObject get(Object p0, Object p1) { return null; }
    static java.lang.String key(Object p0, Object p1) { return null; }
    static void lambda$fetch$1(Object p0, Object p1, Object p2, Object p3, Object p4) { }
    static void lambda$setKey$0(Object p0, Object p1, Object p2, Object p3) { }
    static org.json.JSONObject met(Object p0) { return null; }
    static java.lang.String metSym(Object p0) { return null; }
    static java.lang.String night(Object p0, Object p1) { return null; }
    static org.json.JSONObject now(Object p0, Object p1) { return null; }
    static org.json.JSONObject openMeteo(Object p0) { return null; }
    static org.json.JSONObject owm(Object p0, Object p1) { return null; }
    static java.lang.String owmId(Object p0, Object p1) { return null; }
    static org.json.JSONObject pt(Object p0, Object p1, Object p2) { return null; }
    static java.lang.String r2(Object p0) { return null; }
    static void setKey(Object p0, Object p1, Object p2, Object p3) { }
    static java.lang.String status(Object p0) { return null; }
    static java.lang.String ua(Object p0) { return null; }
    static org.json.JSONObject wapi(Object p0) { return null; }
    static java.lang.String wapiCode(Object p0, Object p1) { return null; }
    static java.lang.String wmo(Object p0, Object p1) { return null; }


    public interface Done {
        void on(String event, String data);
    }
    public static java.lang.String status(android.content.Context ctx) { return null; }
    public static void setKey(android.content.Context ctx, String key, String loc, Done d) { }
    public static void clearKey(android.content.Context ctx, String key) { }
}
