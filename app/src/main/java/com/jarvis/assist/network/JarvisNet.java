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
 *
 * Actual server response shapes (from server.js):
 *   GET /api/profiles        → { profiles: [{name, title, voiceAliases}] }
 *   POST /api/chat           → { reply: "..." }
 *   GET /api/memory/:user    → { memories: [{fact, savedAt}] }
 *   GET /api/reminders       → { items: [{id, label, fireAt, isDue}] }
 *   GET /api/habits/due      → { prompt: "string or null" }
 *   GET /api/wallet/balances → { sol: 0.0, usdc: 0.0, ... }
 */
public class JarvisNet {

    private static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
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
        public String name  = "";
        public String title = "";
        public String role  = "user";
        public boolean owner = false;
    }

    public static class Reminder {
        public String  id     = "";
        public String  label  = "";
        public String  fireAt = "";
        public boolean isDue  = false;
    }

    public static class Todo {
        public String id      = "";
        public String text    = "";
        public String status  = "pending";
        public String addedAt = "";
    }

    public static class ChatMsg {
        public String role, content;
        public ChatMsg(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String base() {
        String u = Store.serverUrl();
        return u.endsWith("/") ? u.substring(0, u.length() - 1) : u;
    }

    private static String baseOf(String serverUrl) {
        return serverUrl.endsWith("/")
            ? serverUrl.substring(0, serverUrl.length() - 1)
            : serverUrl;
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
        RequestBody rb = RequestBody.create(body, JSON_TYPE);
        Request req = new Request.Builder().url(url).post(rb).build();
        try (Response resp = CLIENT.newCall(req).execute()) {
            if (!resp.isSuccessful()) throw new Exception("HTTP " + resp.code());
            return resp.body() != null ? resp.body().string() : "";
        }
    }

    // ── Profiles ──────────────────────────────────────────────────────────────
    // Server returns: { profiles: [{name, title, voiceAliases, role}] }

    public static void fetchProfiles(String serverUrl, Cb<List<Profile>> cb) {
        bg(() -> {
            try {
                String raw = get(baseOf(serverUrl) + "/api/profiles");

                JSONArray arr;
                try {
                    JSONObject wrapper = new JSONObject(raw);
                    if (wrapper.has("profiles")) {
                        arr = wrapper.getJSONArray("profiles");
                    } else {
                        // fallback: bare array
                        arr = new JSONArray(raw);
                    }
                } catch (Exception ex) {
                    arr = new JSONArray(raw);
                }

                List<Profile> list = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    Profile p  = new Profile();
                    p.name  = o.optString("name",  "");
                    p.title = o.optString("title", "");
                    p.role  = o.optString("role",  "user");
                    p.owner = p.role.equalsIgnoreCase("owner");
                    if (!p.name.isEmpty()) list.add(p);
                }
                ui(() -> cb.ok(list));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Chat ──────────────────────────────────────────────────────────────────
    // Server returns: { reply: "..." }

    public static void sendChat(String userName, String message,
                                List<ChatMsg> history, Cb<String> cb) {
        bg(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("userName",  userName);
                body.put("message",   message);
                body.put("sessionId", userName + "_android");
                body.put("source",    "android");

                if (history != null && !history.isEmpty()) {
                    JSONArray hist = new JSONArray();
                    for (ChatMsg m : history) {
                        JSONObject mo = new JSONObject();
                        mo.put("role",    m.role);
                        mo.put("content", m.content);
                        hist.put(mo);
                    }
                    body.put("history", hist);
                }

                String raw  = post(base() + "/api/chat", body.toString());
                JSONObject r = new JSONObject(raw);
                String reply = r.optString("reply",
                               r.optString("message",
                               r.optString("response", raw)));
                ui(() -> cb.ok(reply));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Memory ────────────────────────────────────────────────────────────────
    // Server returns: { memories: [{fact, savedAt}] }

    public static void fetchMemory(String userName, Cb<List<String>> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/memory/" + userName);
                JSONObject obj = new JSONObject(raw);
                List<String> items = new ArrayList<>();

                if (obj.has("memories")) {
                    JSONArray arr = obj.getJSONArray("memories");
                    for (int i = 0; i < arr.length(); i++) {
                        Object entry = arr.get(i);
                        if (entry instanceof JSONObject) {
                            // {fact: "...", savedAt: "..."}
                            items.add(((JSONObject) entry).optString("fact", entry.toString()));
                        } else {
                            items.add(entry.toString());
                        }
                    }
                } else if (obj.has("data")) {
                    Object d = obj.get("data");
                    if (d instanceof JSONArray) {
                        JSONArray arr = (JSONArray) d;
                        for (int i = 0; i < arr.length(); i++) items.add(arr.getString(i));
                    } else {
                        items.add(d.toString());
                    }
                }
                ui(() -> cb.ok(items));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Reminders ─────────────────────────────────────────────────────────────
    // Server returns: { items: [{id, label, fireAt, isDue}] }

    public static void fetchReminders(String userName, Cb<List<Reminder>> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/reminders?userName=" + userName);
                JSONObject wrapper = new JSONObject(raw);

                JSONArray arr;
                if (wrapper.has("items")) {
                    arr = wrapper.getJSONArray("items");
                } else if (wrapper.has("reminders")) {
                    arr = wrapper.getJSONArray("reminders");
                } else {
                    arr = new JSONArray(raw);
                }

                List<Reminder> list = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    Reminder r   = new Reminder();
                    r.id     = o.optString("id",     String.valueOf(i));
                    r.label  = o.optString("label",  o.optString("text", o.optString("title", "")));
                    r.fireAt = o.optString("fireAt", o.optString("time", ""));
                    r.isDue  = o.optBoolean("isDue",  o.optBoolean("due", false));
                    if (!r.label.isEmpty()) list.add(r);
                }
                ui(() -> cb.ok(list));
            } catch (Exception e) {
                ui(() -> cb.err(e.getMessage()));
            }
        });
    }

    // ── Habits ────────────────────────────────────────────────────────────────
    // Server returns: { prompt: "string or null" }

    public static void fetchHabits(String userName, Cb<List<String>> cb) {
        bg(() -> {
            try {
                String raw = get(base() + "/api/habits/due?userName=" + userName);
                List<String> list = new ArrayList<>();

                try {
                    JSONObject obj = new JSONObject(raw);
                    if (obj.has("prompt")) {
                        String prompt = obj.optString("prompt", "");
                        if (!prompt.isEmpty() && !prompt.equals("null")) {
                            list.add(prompt);
                        }
                    } else if (obj.has("habits")) {
                        JSONArray arr = obj.getJSONArray("habits");
                        for (int i = 0; i < arr.length(); i++) {
                            Object item = arr.get(i);
                            if (item instanceof JSONObject) {
                                list.add(((JSONObject) item).optString("name",
                                         ((JSONObject) item).optString("label", item.toString())));
                            } else {
                                list.add(item.toString());
                            }
                        }
                    }
                } catch (Exception ex) {
                    // bare array fallback
                    JSONArray arr = new JSONArray(raw);
                    for (int i = 0; i < arr.length(); i++) list.add(arr.getString(i));
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

                JSONArray arr;
                try {
                    JSONObject wrapper = new JSONObject(raw);
                    if (wrapper.has("todos")) {
                        arr = wrapper.getJSONArray("todos");
                    } else if (wrapper.has("items")) {
                        arr = wrapper.getJSONArray("items");
                    } else {
                        arr = new JSONArray(raw);
                    }
                } catch (Exception ex) {
                    arr = new JSONArray(raw);
                }

                List<Todo> list = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    Todo t       = new Todo();
                    t.id      = o.optString("id",      String.valueOf(i));
                    t.text    = o.optString("text",    o.optString("label", ""));
                    t.status  = o.optString("status",  "pending");
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
                String raw     = get(base() + "/api/wallet/balances?userName=" + userName);
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
                String raw     = get(base() + "/api/brief?userName=" + userName);
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
                String u = baseOf(serverUrl) + "/api/profiles";
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
