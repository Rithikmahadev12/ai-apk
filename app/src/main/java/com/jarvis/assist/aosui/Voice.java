package com.jarvis.assist.aosui;

import android.content.Context;

public class Voice {
    public Sink sink = new Sink();
    public Voice() { }
    public Voice(Object p0, Ev ev) { }
    public void destroy() { }
    public void stopSpeak() { }
    public void stopListen() { }
    public void destroyRec() { }
    public void listen() { }
    public void stop() { }
    public void cancel() { }
    public void speak(Object arr, boolean b) { }
    public static void openVoiceSettings(Context ctx) { }
    public static void openVoiceSettings(Context ctx, String setting) { }

    public interface Ev {
        void on(String event, String data);
    }
    public static class Sink {
        public void on(String event, String data) { }
    }
    public void recogInfo(PhoneAI.Done done) { }
    public void voices(PhoneAI.Done done) { }
    public void downloadOffline(android.content.Context ctx) { }
    public static String[] GEM_VOICES = new String[]{};
}
