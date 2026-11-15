package com.jarvis.assist.aosui;

public class Home extends android.app.Activity {
    public Consumer picNative;
    public Home() { }
    static void finishOthers() { }
    static Home front() { return null; }
    static void lambda$answer$22(Object p0, Object p1) { }
    static void lambda$frameSpikes$0(Object p0, Object p1, Object p2) { }
    static void lambda$oauth$1(Object p0, Object p1, Object p2) { }
    static boolean lambda$onCreate$8(Object p0) { return false; }
    private void lambda$onCreate$9(Object p0) { }
    static void lambda$onRequestPermissionsResult$19(Object p0) { }
    static boolean lambda$onResume$16(Object p0) { return false; }
    static boolean lambda$onResume$17(Object p0) { return false; }
    static void notif(Object p0) { }
    static java.lang.String screen(Object p0) { return null; }
    static java.lang.String screenRes(Object p0) { return null; }
    static int[] slideAnims(Object p0) { return null; }
    public boolean land() { return false; }
    public void lists() { }
    public void load(Object p0) { }
    public android.widget.LinearLayout.LayoutParams lpm(Object p0, Object p1) { return null; }
    public android.view.View metric(Object p0, Object p1, Object p2, Object p3) { return null; }
    public android.widget.LinearLayout metrics(Object p0) { return null; }
    public android.widget.TextView mono(Object p0, Object p1, Object p2) { return null; }
    public void notifs() { }
    public long now() { return 0; }
    public boolean onBack() { return false; }
    public void onConfig() { }
    public void onDestroy() { }
    public void onPause() { }
    public void onPerm() { }
    public void onVoiceEvent(Object p0, Object p1) { }
    public void open() { }
    public void pad() { }
    public void phone() { }
    public void read() { }
    public android.widget.LinearLayout vcol() { return null; }
    public android.view.View view() { return null; }
    public void weather() { }
    public void toast(String s) { }
    public void awView(int i) { }
    public void awDrop(int i) { }
    public void awShown(int i) { }
    public java.lang.String deviceInfo() { return null; }
    public void applyPerf() { }
    public void runOnUiThread(Runnable r) { }
    public android.content.Context getApplicationContext() { return null; }
    public android.content.pm.PackageManager getPackageManager() { return null; }
    public void startActivityForResult(android.content.Intent i, int req) { }
    public void finish() { }

    public static class Phone {
        public Phone() { }
        public void launchRole(String s) { }
        public void launch(String s1, String s2) { }
        public void openApp(String s) { }
        public void openSettings(String s) { }
        public void settings(String s) { }
        public void openAsk(String s) { }
        public void openTalk() { }
        public boolean openAssistant(String s, boolean z) { return false; }
        public java.lang.String assistantInfo() { return null; }
        public java.lang.String aiHasKey() { return null; }
        public void expandNotifs() { }
        public void webSearch(String s) { }
        public void info(String s) { }
        public void uninstall(String s) { }
        public boolean torch() { return false; }
        public java.lang.String net() { return null; }
        public boolean lockScreen() { return false; }
        public java.lang.String lockState() { return null; }
        public void hapticFx(String s) { }
        public java.lang.String apps() { return null; }
        public java.lang.String battery() { return null; }
        public java.lang.String nextAlarm() { return null; }
        public void askCal() { }
        public java.lang.String media() { return null; }
        public boolean mediaAct(String s1, String s2) { return false; }
        public java.lang.String starred() { return null; }
        public java.lang.String screenTime() { return null; }
        public java.lang.String storage() { return null; }
        public boolean contactsOk() { return false; }
        public java.lang.String contacts(String s) { return null; }
        public void askPerm(String s) { }
        public boolean isHome() { return false; }
        public void url(String s) { }
        public void notifOpen(String s) { }
        public java.lang.String permState() { return null; }
        public java.lang.String appWidgets() { return null; }
        public java.lang.String roleApp(String s) { return null; }
        public java.lang.String get(String s) { return null; }
        public void set(String s1, String s2) { }
        public void listen() { }
        public void listenStop() { }
        public void listenCancel() { }
        public void speak(String s, boolean b) { }
        public void sayStop() { }
        public void voiceSettings(String s) { }
        public java.lang.String shareTake() { return null; }
        public java.lang.String clipRead() { return null; }
        public void copy(String s) { }
        public boolean notifReply(String s1, String s2) { return false; }
        public boolean setAlarm(int h, int m, String s) { return false; }
        public boolean setTimer(int s, String label) { return false; }
        public boolean torchSet(boolean b) { return false; }
        public java.lang.String healthSources() { return null; }
        public void voiceInfo(java.util.function.Consumer<String> c) { }
        public void ttsVoices(java.util.function.Consumer<String> c) { }
        public java.lang.String gemVoices() { return null; }
        public void healthConnect(String s1, String s2, java.util.function.Consumer<String> c) { }
        public void healthDisconnect(String s) { }
        public void healthImportApple(java.util.function.Consumer<String> c) { }
        public void healthToday2(boolean b, java.util.function.Consumer<String> c) { }
        public void healthSetPref(String s) { }
        public java.lang.String appVersion() { return null; }
        public void openLockWallpaper() { }
        public void aiClearKey() { }
        public void geminiClearKey() { }
        public void oaiModels(String s, java.util.function.Consumer<String> c) { }
        public void oaiClear() { }
        public java.lang.String weatherStatus() { return null; }
        public void weatherSetKey(String s1, String s2, java.util.function.Consumer<String> c) { }
        public void weatherClearKey(String s) { }
        public boolean powerSave() { return false; }
        public void settingsExport(String s, java.util.function.Consumer<String> c) { }
        public void settingsImport(java.util.function.Consumer<String> c) { }
        public void widgetProviders(java.util.function.Consumer<String> c) { }
        public void widgetAdd(String s, java.util.function.Consumer<String> c) { }
        public void widgetRemove(int i) { }
        public java.lang.String clip(String s) { return null; }
    }
}
