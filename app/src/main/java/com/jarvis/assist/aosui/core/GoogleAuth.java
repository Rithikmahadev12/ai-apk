package com.jarvis.assist.aosui.core;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class GoogleAuth {
    public static final String AUTH = "https://accounts.google.com/o/oauth2/v2/auth";
    public static final String CACHE = "aos.google";
    public static final String SCOPES = "https://www.googleapis.com/auth/gmail.readonly https://www.googleapis.com/auth/drive.metadata.readonly";
    public static final String TOKEN = "https://oauth2.googleapis.com/token";

    private GoogleAuth() {
    }

    public static boolean configured(String str) {
        return str != null && str.trim().endsWith(".apps.googleusercontent.com");
    }

    public static String authUrl(String str, String str2, String str3, String str4) {
        return "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=" + enc(str) + "&redirect_uri=" + enc(str2) + "&scope=" + enc(SCOPES) + "&state=" + enc(str3) + "&code_challenge=" + enc(str4) + "&code_challenge_method=S256&access_type=offline&prompt=consent";
    }

    public static String[] parse(String str) {
        String str2;
        String str3 = null;
        if (str == null) {
            return new String[]{null, null, "empty"};
        }
        String[] split = str.split(" ");
        if (split.length < 2 || !"GET".equals(split[0])) {
            str2 = "bad request";
        } else {
            int indexOf = split[1].indexOf(63);
            if (indexOf >= 0) {
                String[] split2 = split[1].substring(indexOf + 1).split("&");
                String str4 = null;
                String str5 = null;
                for (String str6 : split2) {
                    int indexOf2 = str6.indexOf(61);
                    if (indexOf2 > 0) {
                        String substring = str6.substring(0, indexOf2);
                        String dec = dec(str6.substring(indexOf2 + 1));
                        if ("code".equals(substring)) {
                            str3 = dec;
                        } else if ("state".equals(substring)) {
                            str4 = dec;
                        } else if ("error".equals(substring)) {
                            str5 = dec;
                        }
                    }
                }
                return new String[]{str3, str4, str5};
            }
            str2 = "no query";
        }
        return new String[]{null, null, str2};
    }

    public static int unread(String str) {
        try {
            return new JSONObject(str).optInt("messagesUnread", -1);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String[] message(String str) {
        String str2;
        String str3;
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONArray optJSONArray = jSONObject.optJSONObject("payload") == null ? null : jSONObject.optJSONObject("payload").optJSONArray("headers");
            if (optJSONArray != null) {
                str2 = "";
                str3 = str2;
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        if ("From".equalsIgnoreCase(optJSONObject.optString("name"))) {
                            str2 = optJSONObject.optString("value");
                        } else if ("Subject".equalsIgnoreCase(optJSONObject.optString("name"))) {
                            str3 = optJSONObject.optString("value");
                        }
                    }
                }
            } else {
                str2 = "";
                str3 = str2;
            }
            int indexOf = str2.indexOf(60);
            if (indexOf > 0) {
                str2 = str2.substring(0, indexOf).trim();
            }
            String replaceAll = str2.replaceAll("^\"|\"$", "");
            if (str3.isEmpty()) {
                str3 = "(no subject)";
            }
            return new String[]{replaceAll, str3, String.valueOf(jSONObject.optLong("internalDate", 0L))};
        } catch (Exception e) {
            return new String[]{"", "", "0"};
        }
    }

    public static List<String[]> files(String str) {
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray optJSONArray = new JSONObject(str).optJSONArray("files");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        arrayList.add(new String[]{optJSONObject.optString("name"), optJSONObject.optString("modifiedTime"), optJSONObject.optString("mimeType")});
                    }
                }
            }
        } catch (Exception e) {
        }
        return arrayList;
    }

    static String enc(String str) {
        if (str == null) {
            str = "";
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    static String dec(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            return str;
        }
    }
}
