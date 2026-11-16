package com.jarvis.assist.aosui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.jarvis.assist.aosui.Home;
import com.jarvis.assist.aosui.PhoneAI;
import com.jarvis.assist.aosui.Weather;
import com.jarvis.assist.aosui.ai.Host;
import java.util.Calendar;
import java.util.Objects;
import java.util.function.Consumer;
import org.json.JSONObject;

public class Backend {
    public volatile Runnable aiBusy;
    final Home.Phone b;
    final Home h;

    public Backend(Home home) {
        this.h = home;
        Objects.requireNonNull(home);
        this.b = home.new Phone();
    }

    public Context ctx() { return this.h; }
    public Activity activity() { return this.h; }
    public void launchRole(String str) { this.b.launchRole(str); }
    public void launch(String str, String str2) { this.b.launch(str, str2); }
    public void openApp(String str) { this.b.openApp(str); }
    public void openSettings(String str) { this.b.openSettings(str); }
    public void settings(String str) { this.b.settings(str); }
    public void openAsk(String str) { this.b.openAsk(str); }
    public void openTalk() { this.b.openTalk(); }
    public boolean openAssistant(String str, boolean z) { return this.b.openAssistant(str, z); }
    public java.lang.String assistantInfo() { return this.b.assistantInfo(); }
    public java.lang.String aiHasKey() { return this.b.aiHasKey(); }
    public void expandNotifs() { this.b.expandNotifs(); }
    public void webSearch(String str) { this.b.webSearch(str); }
    public void info(String str) { this.b.info(str); }
    public void uninstall(String str) { this.b.uninstall(str); }
    public boolean torch() { return this.b.torch(); }
    public java.lang.String net() { return this.b.net(); }
    public boolean lockScreen() { return this.b.lockScreen(); }
    public java.lang.String lockState() { return this.b.lockState(); }
    public void hapticFx(String str) { this.b.hapticFx(str); }
    public java.lang.String apps() { return this.b.apps(); }
    public java.lang.String appsCached() { return null; }
    public void appsReady(Runnable runnable) { }
    public java.lang.String battery() { return this.b.battery(); }
    public java.lang.String nextAlarm() { return this.b.nextAlarm(); }
    public java.lang.String phoneCal(int i) { return null; }
    public void askCal() { this.b.askCal(); }
    public java.lang.String healthToday(boolean z) { return null; }
    public java.lang.String loc() { return null; }
    public void locRefresh(Consumer<String> consumer) { }
    public void weatherFetch(String str, double d, double d2, Consumer<String> consumer) { }
    public java.lang.String notifList() { return null; }
    public java.lang.String roleApp(String str) { return this.b.roleApp(str); }
    public java.lang.String get(String str) { return this.b.get(str); }
    public java.lang.String media() { return this.b.media(); }
    public boolean mediaAct(String str, String str2) { return this.b.mediaAct(str, str2); }
    public java.lang.String starred() { return this.b.starred(); }
    public java.lang.String screenTime() { return this.b.screenTime(); }
    public long dataToday() { return -1L; }
    public java.lang.String storage() { return this.b.storage(); }
    public boolean contactsOk() { return this.b.contactsOk(); }
    public java.lang.String contacts(String str) { return this.b.contacts(str); }
    public void askPerm(String str) { this.b.askPerm(str); }
    public boolean isHome() { return this.b.isHome(); }
    public void toast(String str) { this.h.toast(str); }
    public void url(String str) { this.b.url(str); }
    public void notifOpen(String str) { this.b.notifOpen(str); }
    public java.lang.String permState() { return this.b.permState(); }
    public java.lang.String appWidgets() { return this.b.appWidgets(); }
    public View appWidgetView(int i) { return this.h.awView(i); }
    public void dropAppWidgetView(int i) { this.h.awDrop(i); }
    public void appWidgetShown(int i) { this.h.awShown(i); }
    public java.lang.String deviceInfo() { return this.h.deviceInfo(); }
    public void applyPerf() { this.h.applyPerf(); }
    public Drawable icon(String str, String str2) {
        try {
            return this.h.getPackageManager().getActivityIcon(new ComponentName(str, str2));
        } catch (Exception e) { return null; }
    }
    public static boolean debug() { return false; }
    public JSONObject aiAsk(JSONObject jSONObject) { return null; }
    public JSONObject aiAsk(JSONObject jSONObject, Host.Stream stream) { return null; }
    public void aiWarm() { }
    public void aiRewarm() { }
    public void aiPing(boolean z) { }
    public void aiSetKey(String str, Consumer<String> consumer) { }
    public void aiModels(String str, Consumer<String> consumer) { }
    public void geminiSetKey(String str, Consumer<String> consumer) { }
    public void oaiSet(String str, Consumer<String> consumer) { }
    public void aiSetOrder(String str) { }
    public void set(String str, String str2) { this.b.set(str, str2); }
    public void listen() { this.b.listen(); }
    public void listenStop() { this.b.listenStop(); }
    public void listenCancel() { this.b.listenCancel(); }
    public void speak(String str) { this.b.speak(str, false); }
    public void speakLocal(String str) { this.b.speak(str, true); }
    public void sayStop() { this.b.sayStop(); }
    public void voiceSettings(String str) { this.b.voiceSettings(str); }
    public void pickImage(Consumer<String> consumer) { }
    public java.lang.String shareTake() { return this.b.shareTake(); }
    public java.lang.String clipRead() { return this.b.clipRead(); }
    public void copy(String str) { this.b.copy(str); }
    public boolean notifReply(String str, String str2) { return this.b.notifReply(str, str2); }
    public boolean setAlarm(int i, int i2, String str) { return this.b.setAlarm(i, i2, str); }
    public boolean setTimer(int i, String str) { return this.b.setTimer(i, str); }
    public boolean torchSet(boolean z) { return this.b.torchSet(z); }
    public java.lang.String phoneCalAdd(String str) { return null; }
    public java.lang.String healthWeek() { return null; }
    public java.lang.String healthSources() { return this.b.healthSources(); }
    public java.lang.String fetchSearch(String str) { return null; }
    public java.lang.String fetchRates() { return null; }
    public java.lang.String fetchPage(String str) { return null; }
    public void voiceInfo(Consumer<String> consumer) { this.b.voiceInfo(consumer); }
    public void ttsVoices(Consumer<String> consumer) { this.b.ttsVoices(consumer); }
    public java.lang.String gemVoices() { return this.b.gemVoices(); }
    public void healthConnect(String str, String str2, Consumer<String> consumer) { this.b.healthConnect(str, str2, consumer); }
    public void healthDisconnect(String str) { this.b.healthDisconnect(str); }
    public void healthImportApple(Consumer<String> consumer) { this.b.healthImportApple(consumer); }
    public void healthToday2(boolean z, Consumer<String> consumer) { this.b.healthToday2(z, consumer); }
    public void healthTestHc(Consumer<String> consumer) { }
    public void healthSetPref(String str) { this.b.healthSetPref(str); }
    public java.lang.String appVersion() { return this.b.appVersion(); }
    public void feedback() { }
    public void openLockWallpaper() { this.b.openLockWallpaper(); }
    public void aiClearKey() { this.b.aiClearKey(); }
    public void geminiClearKey() { this.b.geminiClearKey(); }
    public void oaiModels(String str, Consumer<String> consumer) { this.b.oaiModels(str, consumer); }
    public void oaiClear() { this.b.oaiClear(); }
    public java.lang.String weatherStatus() { return this.b.weatherStatus(); }
    public void weatherSetKey(String str, String str2, Consumer<String> consumer) { this.b.weatherSetKey(str, str2, consumer); }
    public void weatherClearKey(String str) { this.b.weatherClearKey(str); }
    public boolean powerSave() { return this.b.powerSave(); }
    public void settingsExport(String str, Consumer<String> consumer) { this.b.settingsExport(str, consumer); }
    public void settingsImport(Consumer<String> consumer) { this.b.settingsImport(consumer); }
    public void widgetProviders(Consumer<String> consumer) { this.b.widgetProviders(consumer); }
    public void widgetAdd(String str, Consumer<String> consumer) { this.b.widgetAdd(str, consumer); }
    public void widgetRemove(int i) { this.b.widgetRemove(i); }
    public java.lang.String clip(String str) { return this.b.clip(str); }
    public void finish() { this.h.finish(); }
}
