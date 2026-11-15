package com.jarvis.assist.network;

import android.os.Handler;
import android.os.Looper;

import com.jarvis.assist.core.Store;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * JarvisNet — all HTTP calls to the Jarvis backend.
 * Mirrors AugmaOS AiNet/Backend pattern from DEX.
 *
 * Endpoints used:
 *   GET  /api/profiles
 *   GET  /api/chat?userName=&message=
 *   POST /api/chat
 *   GET  /api/memory/:name
 *   GET  /api/reminders?userName=
 *   GET  /api/habits/due?userName=
 *   GET  /api/todos?userName=
 *   GET  /api/wallet/balances?userName=
 *   GET  /api/brief?userName=
 */
public class JarvisNet {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30,    java.util.concurrent.TimeUnit.SECONDS)
        .build();

    private static final ExecutorService POOL = Executors.newCachedThreadPool();
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    // ── Callbacks ─────────────────────────────────────────────────────────────

    public interface Cb<T> {
        void ok(T result);
        void err(String reason);
    }

    // ── Data models ───────────────────────────────────────────────────────────

    public static class Profile {
        public String name, role;
        public boolean owner;
    }

    public static class Reminder {
        public String id, label, fireAt;
        public boolean isDue;
    }

    public static class Todo {
        public String id, text, status, addedAt;
    }

    public static class ChatMsg {
        public String role, content;
        public ChatMsg(String role, String content) {
            this.role = role; this.content = content;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String base() {
        String u = Store.serverUrl();
        return u.endsWith("/") ? u.substring(0, u.length() - 1) : u;
    }

    private static void bg(Runnable r) { POOL.execute(r); }
    private static void ui(Runnable r) { MAIN.post(r); }

    private static String get(String url) throws Exception {
        Request req = new Request.Builder().url(url).get().build();
        try (Response resp = CLIENT.newCall(req).execute()) {
            if (!resp.isSuccessful()) throw new Exception("HTTP " + resp.code());
            return resp.body() != null ? resp.body().string() : "";
        }
    }

    private static String post(String url, String body) throws Exception {
        RequestBody rb = RequestBody.create(body, JSON);
        Request req = new Request.Builder().url(url).post(rb).build();
        try (Response resp = CLIENT.newCall(req).execute()) {
            if (!resp.isSuccessful()) throw new Exception("HTTP " + resp.code());
            return resp.body() != null ? resp.body().string() : "";
        }
    }

    // ── Profiles ──────────────────────────────────────────────────────────────

    public static void fetchProfiles(String serverUrl, Cb<List<Profile>> cb) {
        bg(() -> {
            try {
                String url = (serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length()-1) : serverUrl)
                    + "/api/profiles";
                Request req = new Request.Builder().url(url).get().build();
                String raw;
                try (Response resp = CLIENT.newCall(req).execute()) {
                    if (!resp.isSuccessful()) throw new Exception("HTTP " + resp.code());
                    raw = resp.body() != null ? resp.body().string() : "[]";
                }
                JSONArray arr = new JSONArray(raw);
                List<Profile> list = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    Profile p = new Profile();
                    p.name  = o.optString("name", "");
                    p.role  = o.optString("role", "user");
                    p.owner = o.optBoolean("owner", false);
                    list.add(p);
                }
                ui(() -> cb.ok(list));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Chat ──────────────────────────────────────────────────────────────────

    public static void sendChat(String userName, String message,
                                List<ChatMsg> history, Cb<String> cb) {
        bg(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("userName", userName);
                body.put("message", message);

                if (history != null && !history.isEmpty()) {
                    JSONArray hist = new JSONArray();
                    for (ChatMsg m : history) {
                        JSONObject mo = new JSONObject();
                        mo.put("role", m.role);
                        mo.put("content", m.content);
                        hist.put(mo);
                    }
                    body.put("history", hist);
                }

                String raw = post(base() + "/api/chat", body.toString());
                JSONObject resp = new JSONObject(raw);
                String reply = resp.optString("reply",
                    resp.optString("message",
                    resp.optString("response", raw)));
                ui(() -> cb.ok(reply));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Memory ────────────────────────────────────────────────────────────────

    public static void fetchMemory(String userName, Cb<List<String>> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/memory/" + userName);
                JSONObject obj = new JSONObject(raw);
                List<String> items = new ArrayList<>();

                // Handle both array and object responses
                if (obj.has("memories")) {
                    JSONArray arr = obj.getJSONArray("memories");
                    for (int i = 0; i < arr.length(); i++) items.add(arr.getString(i));
                } else if (obj.has("data")) {
                    Object d = obj.get("data");
                    if (d instanceof JSONArray) {
                        JSONArray arr = (JSONArray) d;
                        for (int i = 0; i < arr.length(); i++) items.add(arr.getString(i));
                    } else {
                        items.add(d.toString());
                    }
                } else {
                    items.add(raw);
                }
                ui(() -> cb.ok(items));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Reminders ─────────────────────────────────────────────────────────────

    public static void fetchReminders(String userName, Cb<List<Reminder>> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/reminders?userName=" + userName);
                JSONArray arr = new JSONArray(raw);
                List<Reminder> list = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    Reminder r = new Reminder();
                    r.id     = o.optString("id", String.valueOf(i));
                    r.label  = o.optString("label", o.optString("text", o.optString("title", "")));
                    r.fireAt = o.optString("fireAt", o.optString("time", ""));
                    r.isDue  = o.optBoolean("isDue", o.optBoolean("due", false));
                    if (!r.label.isEmpty()) list.add(r);
                }
                ui(() -> cb.ok(list));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Habits ────────────────────────────────────────────────────────────────

    public static void fetchHabits(String userName, Cb<List<String>> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/habits/due?userName=" + userName);
                JSONArray arr = new JSONArray(raw);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    Object item = arr.get(i);
                    if (item instanceof JSONObject) {
                        JSONObject o = (JSONObject) item;
                        list.add(o.optString("name", o.optString("label", o.toString())));
                    } else {
                        list.add(item.toString());
                    }
                }
                ui(() -> cb.ok(list));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Todos ─────────────────────────────────────────────────────────────────

    public static void fetchTodos(String userName, Cb<List<Todo>> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/todos?userName=" + userName);
                JSONArray arr = new JSONArray(raw);
                List<Todo> list = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    Todo t = new Todo();
                    t.id      = o.optString("id", String.valueOf(i));
                    t.text    = o.optString("text", o.optString("label", ""));
                    t.status  = o.optString("status", "pending");
                    t.addedAt = o.optString("addedAt", o.optString("createdAt", ""));
                    if (!t.text.isEmpty()) list.add(t);
                }
                ui(() -> cb.ok(list));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Wallet ────────────────────────────────────────────────────────────────

    public static void fetchWallet(String userName, Cb<JSONObject> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/wallet/balances?userName=" + userName);
                JSONObject obj = new JSONObject(raw);
                ui(() -> cb.ok(obj));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Brief ─────────────────────────────────────────────────────────────────

    public static void fetchBrief(String userName, Cb<JSONObject> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/brief?userName=" + userName);
                JSONObject obj = new JSONObject(raw);
                ui(() -> cb.ok(obj));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Ping ──────────────────────────────────────────────────────────────────

    public static void ping(String serverUrl, Cb<Boolean> cb) {
        bg(() -> {
            try {
                String u = (serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length()-1) : serverUrl)
                    + "/api/profiles";
                Request req = new Request.Builder().url(u).get()
                    .addHeader("Connection", "close").build();
                try (Response resp = CLIENT.newCall(req).execute()) {
                    ui(() -> cb.ok(resp.isSuccessful() || resp.code() == 404));
                }
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }
}
