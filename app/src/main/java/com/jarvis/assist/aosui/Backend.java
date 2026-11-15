package com.jarvis.assist.aosui;

import android.app.Activity;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
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
import com.jarvis.assist.aosui.core.HealthPick;
import com.jarvis.assist.aosui.core.Memory;
import java.util.Calendar;
import java.util.Objects;
import java.util.function.Consumer;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Backend {
    public volatile Runnable aiBusy;
    final Home.Phone b;
    final Home h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Backend(Home home) {
        this.h = home;
        Objects.requireNonNull(home);
        this.b = new Home.Phone();
    }

    public Context ctx() {
        return this.h;
    }

    public Activity activity() {
        return this.h;
    }

    public void launchRole(String str) {
        this.b.launchRole(str);
    }

    public void launch(String str, String str2) {
        this.b.launch(str, str2);
    }

    public void openApp(String str) {
        this.b.openApp(str);
    }

    public void openSettings(String str) {
        this.b.openSettings(str);
    }

    public void settings(String str) {
        this.b.settings(str);
    }

    public void openAsk(String str) {
        this.b.openAsk(str);
    }

    public void openTalk() {
        this.b.openTalk();
    }

    public boolean openAssistant(String str, boolean z) {
        return this.b.openAssistant(str, z);
    }

    public String assistantInfo() {
        return this.b.assistantInfo();
    }

    public String aiHasKey() {
        return this.b.aiHasKey();
    }

    public void expandNotifs() {
        this.b.expandNotifs();
    }

    public void webSearch(String str) {
        this.b.webSearch(str);
    }

    public void info(String str) {
        this.b.info(str);
    }

    public void uninstall(String str) {
        this.b.uninstall(str);
    }

    public boolean torch() {
        return this.b.torch();
    }

    public String net() {
        return this.b.net();
    }

    public boolean lockScreen() {
        return this.b.lockScreen();
    }

    public String lockState() {
        return this.b.lockState();
    }

    public void hapticFx(String str) {
        this.b.hapticFx(str);
    }

    public String apps() {
        return this.b.apps();
    }

    public String appsCached() {
        return AppIndex.cached();
    }

    public void appsReady(Runnable runnable) {
        AppIndex.whenReady(runnable);
    }

    public String battery() {
        return this.b.battery();
    }

    public String nextAlarm() {
        return this.b.nextAlarm();
    }

    public String phoneCal(int i) {
        return PhoneCal.json(this.h, i);
    }

    public void askCal() {
        this.b.askCal();
    }

    public String healthToday(boolean z) {
        return HealthSources.today(this.h.getApplicationContext(), z);
    }

    public String loc() {
        return Loc.get(this.h);
    }

    public void locRefresh(Consumer<String> consumer) {
        Loc.refresh(this.h, 600000L, consumer);
    }

    public void weatherFetch(String str, double d, double d2, final Consumer<String> consumer) {
        Context applicationContext = this.h.getApplicationContext();
        Objects.requireNonNull(consumer);
        Weather.fetch(applicationContext, str, d, d2, new Weather.Done() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda10
            @Override // com.jarvis.assist.aosui.Weather.Done
            public final void on(String str2) {
                consumer.accept(str2);
            }
        });
    }

    public String notifList() {
        return Notifs.list();
    }

    public String roleApp(String str) {
        return this.b.roleApp(str);
    }

    public String get(String str) {
        return this.b.get(str);
    }

    public String media() {
        return this.b.media();
    }

    public boolean mediaAct(String str, String str2) {
        return this.b.mediaAct(str, str2);
    }

    public String starred() {
        return this.b.starred();
    }

    public String screenTime() {
        return this.b.screenTime();
    }

    public long dataToday() {
        try {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.h.getSystemService(NetworkStatsManager.class);
            if (networkStatsManager == null) {
                return -1L;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            int[] iArr = {0, 1};
            long j = 0;
            for (int i = 0; i < 2; i++) {
                try {
                    NetworkStats.Bucket querySummaryForDevice = networkStatsManager.querySummaryForDevice(iArr[i], null, calendar.getTimeInMillis(), System.currentTimeMillis());
                    if (querySummaryForDevice != null) {
                        j += querySummaryForDevice.getRxBytes() + querySummaryForDevice.getTxBytes();
                    }
                } catch (Exception e) {
                }
            }
            return j;
        } catch (Throwable th) {
            return -1L;
        }
    }

    public String storage() {
        return this.b.storage();
    }

    public boolean contactsOk() {
        return this.b.contactsOk();
    }

    public String contacts(String str) {
        return this.b.contacts(str);
    }

    public void askPerm(String str) {
        this.b.askPerm(str);
    }

    public boolean isHome() {
        return this.b.isHome();
    }

    public void toast(String str) {
        this.h.toast(str);
    }

    public void url(String str) {
        this.b.url(str);
    }

    public void notifOpen(String str) {
        this.b.notifOpen(str);
    }

    public String permState() {
        return this.b.permState();
    }

    public String appWidgets() {
        return this.b.appWidgets();
    }

    public View appWidgetView(int i) {
        return this.h.awView(i);
    }

    public void dropAppWidgetView(int i) {
        this.h.awDrop(i);
    }

    public void appWidgetShown(int i) {
        this.h.awShown(i);
    }

    public String deviceInfo() {
        return this.h.deviceInfo();
    }

    public void applyPerf() {
        this.h.applyPerf();
    }

    public Drawable icon(String str, String str2) {
        try {
            return this.h.getPackageManager().getActivityIcon(new ComponentName(str, str2));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean debug() {
        return false;
    }

    public JSONObject aiAsk(JSONObject jSONObject) {
        return PhoneAI.askSync(this.h.getApplicationContext(), jSONObject, this.aiBusy);
    }

    public JSONObject aiAsk(JSONObject jSONObject, Host.Stream stream) {
        return PhoneAI.askSync(this.h.getApplicationContext(), jSONObject, this.aiBusy, stream);
    }

    public void aiWarm() {
        PhoneAI.warm(this.h);
    }

    public void aiRewarm() {
        PhoneAI.rewarm(this.h);
    }

    public void aiPing(boolean z) {
        PhoneAI.ping(this.h, z);
    }

    public void aiSetKey(String str, final Consumer<String> consumer) {
        PhoneAI.setKey(this.h, str, new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda0
            @Override // com.jarvis.assist.aosui.PhoneAI.Done
            public final void on(String str2) {
                Backend.this.m2lambda$aiSetKey$1$comaugmaosoBackend(consumer, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aiSetKey$1$com-augma-oso-Backend, reason: not valid java name */
    public /* synthetic */ void m2lambda$aiSetKey$1$comaugmaosoBackend(final Consumer consumer, final String str) {
        this.h.runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(str);
            }
        });
    }

    public void aiModels(String str, final Consumer<String> consumer) {
        PhoneAI.refreshModels(this.h, str, new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda4
            @Override // com.jarvis.assist.aosui.PhoneAI.Done
            public final void on(String str2) {
                Backend.this.m1lambda$aiModels$3$comaugmaosoBackend(consumer, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$aiModels$3$com-augma-oso-Backend, reason: not valid java name */
    public /* synthetic */ void m1lambda$aiModels$3$comaugmaosoBackend(final Consumer consumer, final String str) {
        this.h.runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(str);
            }
        });
    }

    public void geminiSetKey(String str, final Consumer<String> consumer) {
        PhoneAI.geminiSetKey(this.h, str, new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda1
            @Override // com.jarvis.assist.aosui.PhoneAI.Done
            public final void on(String str2) {
                Backend.this.m3lambda$geminiSetKey$5$comaugmaosoBackend(consumer, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$geminiSetKey$5$com-augma-oso-Backend, reason: not valid java name */
    public /* synthetic */ void m3lambda$geminiSetKey$5$comaugmaosoBackend(final Consumer consumer, final String str) {
        this.h.runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$oaiSet$7$com-augma-oso-Backend, reason: not valid java name */
    public /* synthetic */ void m4lambda$oaiSet$7$comaugmaosoBackend(final Consumer consumer, final String str) {
        this.h.runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(str);
            }
        });
    }

    public void oaiSet(String str, final Consumer<String> consumer) {
        PhoneAI.oaiSet(this.h, str, new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda8
            @Override // com.jarvis.assist.aosui.PhoneAI.Done
            public final void on(String str2) {
                Backend.this.m4lambda$oaiSet$7$comaugmaosoBackend(consumer, str2);
            }
        });
    }

    public void aiSetOrder(String str) {
        PhoneAI.setOrder(this.h, str);
    }

    public void set(String str, String str2) {
        this.b.set(str, str2);
    }

    public void listen() {
        this.b.listen();
    }

    public void listenStop() {
        this.b.listenStop();
    }

    public void listenCancel() {
        this.b.listenCancel();
    }

    public void speak(String str) {
        this.b.speak(str, false);
    }

    public void speakLocal(String str) {
        this.b.speak(str, true);
    }

    public void sayStop() {
        this.b.sayStop();
    }

    public void voiceSettings(String str) {
        this.b.voiceSettings(str);
    }

    public void pickImage(Consumer<String> consumer) {
        this.h.picNative = consumer;
        try {
            this.h.startActivityForResult(Intent.createChooser(new Intent("android.intent.action.GET_CONTENT").setType("image/*").addCategory("android.intent.category.OPENABLE").putExtra("android.intent.extra.ALLOW_MULTIPLE", true), null), 7701);
        } catch (Exception e) {
            this.h.picNative = null;
            consumer.accept("{\"ok\":false,\"error\":\"NO PICKER\"}");
        }
    }

    public String shareTake() {
        return this.b.shareTake();
    }

    public String clipRead() {
        return this.b.clipRead();
    }

    public void copy(String str) {
        this.b.copy(str);
    }

    public boolean notifReply(String str, String str2) {
        return this.b.notifReply(str, str2);
    }

    public boolean setAlarm(int i, int i2, String str) {
        return this.b.setAlarm(i, i2, str);
    }

    public boolean setTimer(int i, String str) {
        return this.b.setTimer(i, str);
    }

    public boolean torchSet(boolean z) {
        return this.b.torchSet(z);
    }

    public String phoneCalAdd(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (PhoneCal.canWrite(this.h)) {
                return PhoneCal.insert(this.h, jSONObject.optString("title"), jSONObject.optLong("start"), jSONObject.optLong("end"), jSONObject.optBoolean("allDay"), jSONObject.optString("location"), jSONObject.optString("notes"), jSONObject.optBoolean("alert"));
            }
            this.b.askCal();
            return "{\"ok\":false,\"why\":\"permission\"}";
        } catch (Exception e) {
            return "{\"ok\":false,\"why\":\"bad json\"}";
        }
    }

    public String healthWeek() {
        return HealthSources.week(this.h.getApplicationContext(), false);
    }

    public String healthSources() {
        return this.b.healthSources();
    }

    public String fetchSearch(String str) {
        return WebPage.search(str);
    }

    public String fetchRates() {
        return WebPage.rates();
    }

    public String fetchPage(String str) {
        return WebPage.fetch(str);
    }

    public void voiceInfo(Consumer<String> consumer) {
        this.b.voiceInfo(consumer);
    }

    public void ttsVoices(Consumer<String> consumer) {
        this.b.ttsVoices(consumer);
    }

    public String gemVoices() {
        return this.b.gemVoices();
    }

    public void healthConnect(String str, String str2, Consumer<String> consumer) {
        this.b.healthConnect(str, str2, consumer);
    }

    public void healthDisconnect(String str) {
        this.b.healthDisconnect(str);
    }

    public void healthImportApple(Consumer<String> consumer) {
        this.b.healthImportApple(consumer);
    }

    public void healthToday2(boolean z, Consumer<String> consumer) {
        this.b.healthToday2(z, consumer);
    }

    public void healthTestHc(final Consumer<String> consumer) {
        final Context applicationContext = this.h.getApplicationContext();
        final Handler handler = new Handler(Looper.getMainLooper());
        Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                Backend.lambda$healthTestHc$9(applicationContext, handler, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$healthTestHc$9(Context context, Handler handler, final Consumer consumer) {
        final String testLine = HealthPick.testLine(Health.test(context));
        handler.post(new Runnable() { // from class: com.jarvis.assist.aosui.Backend$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(testLine);
            }
        });
    }

    public void healthSetPref(String str) {
        this.b.healthSetPref(str);
    }

    public String appVersion() {
        return this.b.appVersion();
    }

    public void feedback() {
        Crash.share(this.h, "aOS feedback", true);
    }

    public void openLockWallpaper() {
        this.b.openLockWallpaper();
    }

    public void aiClearKey() {
        this.b.aiClearKey();
    }

    public void geminiClearKey() {
        this.b.geminiClearKey();
    }

    public void oaiModels(String str, Consumer<String> consumer) {
        this.b.oaiModels(str, consumer);
    }

    public void oaiClear() {
        this.b.oaiClear();
    }

    public String weatherStatus() {
        return this.b.weatherStatus();
    }

    public void weatherSetKey(String str, String str2, Consumer<String> consumer) {
        this.b.weatherSetKey(str, str2, consumer);
    }

    public void weatherClearKey(String str) {
        this.b.weatherClearKey(str);
    }

    public boolean powerSave() {
        return this.b.powerSave();
    }

    public void settingsExport(String str, Consumer<String> consumer) {
        this.b.settingsExport(str, consumer);
    }

    public void settingsImport(Consumer<String> consumer) {
        this.b.settingsImport(consumer);
    }

    public void widgetProviders(Consumer<String> consumer) {
        this.b.widgetProviders(consumer);
    }

    public void widgetAdd(String str, Consumer<String> consumer) {
        this.b.widgetAdd(str, consumer);
    }

    public void widgetRemove(int i) {
        this.b.widgetRemove(i);
    }

    public String clip(String str) {
        return this.b.clip(str);
    }

    public void finish() {
        this.h.finish();
    }
}
