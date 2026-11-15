package com.jarvis.assist.aosui;

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

    public interface Ev {
        void on(String event, String data);
    }
    public static class Sink {
        public void on(String event, String data) { }
    }

    public static void openVoiceSettings(android.content.Context ctx) { }
    public interface Ev {
        void on(String event, String data);
    }
}
