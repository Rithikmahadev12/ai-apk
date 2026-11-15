package com.jarvis.assist.aosui;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.jarvis.assist.aosui.ai.NativeAssist;
import com.jarvis.assist.aosui.home.NativeHome;
import com.jarvis.assist.aosui.settings.NativeSettings;
import java.util.function.Consumer;

public class Home extends Activity {

    // fields AosHome.java references via Home.this
    public java.util.function.Consumer<String> picNative;
    public NativeHome nat;
    public NativeSettings sett;
    public NativeAssist asst;
    public com.jarvis.assist.aosui.brief.BriefPage brief;
    public Voice vo;
    public android.view.View safe;
    public boolean micWanted;

    public static java.lang.ref.WeakReference<Home> front;
    public static java.util.List<java.lang.ref.WeakReference<Home>> others = new java.util.ArrayList<>();

    // AppWidget refs — AosHome uses these unqualified so they must be in superclass
    public AppWidgetHost awHost;

    public Home() { }

    // static helpers
    public static void finishOthers() { }
    public static Home front() { return front != null ? front.get() : null; }
    public static void lambda$answer$22(Object p0, Object p1) { }
    public static void lambda$frameSpikes$0(Object p0, Object p1, Object p2) { }
    public static void lambda$oauth$1(Object p0, Object p1, Object p2) { }
    public static boolean lambda$onCreate$8(Object p0) { return false; }
    public void lambda$onCreate$9(Object p0) { }
    public static void lambda$onRequestPermissionsResult$19(Object p0) { }
    public static boolean lambda$onResume$16(Object p0) { return false; }
    public static boolean lambda$onResume$17(Object p0) { return false; }
    public static void notif(Object p0) { }
    public static java.lang.String screen(Object p0) { return null; }
    public static java.lang.String screenRes(Object p0) { return null; }
    public static int[] slideAnims(Object p0) { return null; }

    // instance methods AosHome calls via Home.this
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
    public void back() { }
    public void onConfig() { }
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
    public android.view.View awView(int i) { return null; }
    public void awDrop(int i) { }
    public void awShown(int i) { }
    public java.lang.String deviceInfo() { return null; }
    public void applyPerf() { }
    public boolean saving() { return false; }
    public Voice voice() { return null; }

    // lambda dispatch methods AosHome anonymous classes call via Home.this
    public void m5lambda$applyPerf$14$comaugmaosoHome() { }
    public void m6lambda$event$20$comaugmaosoHome(Object p0) { }
    public void m7lambda$new$12$comaugmaosoHome() { }
    public void m8lambda$new$13$comaugmaosoHome(Object p0) { }
    public void m9lambda$new$15$comaugmaosoHome(Object p0, Object p1) { }
    public void m10lambda$oauth$2$comaugmaosoHome(Object p0, Object p1) { }
    public void m11lambda$onActivityResult$3$comaugmaosoHome(Object p0, Object p1, Object p2) { }
    public void m12lambda$onActivityResult$4$comaugmaosoHome(Object p0, Object p1) { }
    public void m13lambda$onActivityResult$5$comaugmaosoHome(Object p0, Object p1) { }
    public void m14lambda$onActivityResult$6$comaugmaosoHome(Object p0, Object p1) { }
    public void m15lambda$onActivityResult$7$comaugmaosoHome(Object p0, Object p1) { }
    public void m16lambda$takeShare$10$comaugmaosoHome() { }
    public void m17lambda$takeShare$11$comaugmaosoHome(Object p0, Object p1) { }
    public void m18lambda$toast$21$comaugmaosoHome(Object p0) { }
    public void m19lambda$voice$18$comaugmaosoHome(Object p0, Object p1) { }
    public void m20lambda$onDraw$0$comaugmaosoHome$1(Object p0) { }

    // inner Phone class — also uses Home.this so must be nested here
    public class Phone {
        public Phone() { }
        // lambda methods Phone anonymous classes call via Home.Phone.this
        public void m35lambda$listen$2$comaugmaosoHome$Phone() { }
        public void m37lambda$listenStop$3$comaugmaosoHome$Phone() { }
        public void m48lambda$sayStop$1$comaugmaosoHome$Phone() { }
        public void m52lambda$speak$0$comaugmaosoHome$Phone(Object p0, Object p1) { }

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
        public void open(String s1, String s2) { }
    }
}
