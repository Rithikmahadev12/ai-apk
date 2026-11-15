package com.jarvis.assist.aosui.ai;

public class Host {
    public org.json.JSONObject ai(Object p0) { return null; }
    public org.json.JSONObject ai(Object p0, Object p1) { return null; }
    public java.lang.String aiHasKey() { return null; }
    public void aiPing(Object p0) { }
    public void aiWarm() { }
    public java.lang.String apps() { return null; }
    public java.lang.String battery() { return null; }
    public java.lang.String contacts(Object p0) { return null; }
    public boolean contactsOk() { return false; }
    public java.lang.String fetchPage(Object p0) { return null; }
    public java.lang.String fetchRates() { return null; }
    public java.lang.String fetchSearch(Object p0) { return null; }
    public java.lang.String get(Object p0) { return null; }
    public java.lang.String healthSources() { return null; }
    public java.lang.String healthToday() { return null; }
    public java.lang.String healthWeek() { return null; }
    public void launch(Object p0, Object p1) { }
    public void launchRole(Object p0) { }
    public boolean lockAsk(Object p0) { return false; }
    public boolean lockScreen() { return false; }
    public java.lang.String lockState() { return null; }
    public void log(Object p0) { }
    public java.lang.String ls(Object p0) { return null; }
    public void lsSet(Object p0, Object p1) { }
    public java.lang.String media() { return null; }
    public boolean mediaAct(Object p0, Object p1) { return false; }
    public java.lang.String nextAlarm() { return null; }
    public java.lang.String notifList() { return null; }
    public void notifOpen(Object p0) { }
    public boolean notifReply(Object p0, Object p1) { return false; }
    public long now() { return 0; }
    public java.lang.String permState() { return null; }
    public java.lang.String phoneCal(Object p0) { return null; }
    public java.lang.String phoneCalAdd(Object p0) { return null; }
    public void set(Object p0, Object p1) { }
    public boolean setAlarm(Object p0, Object p1, Object p2) { return false; }
    public boolean setTimer(Object p0, Object p1) { return false; }
    public void settings(Object p0) { }
    public java.lang.String t(Object p0) { return null; }
    public boolean torchSet(Object p0) { return false; }
    public void url(Object p0) { }

    public interface Stream {
        void chunk(String s);
    }
}
