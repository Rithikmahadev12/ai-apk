package com.jarvis.assist.aosui.home;

import android.content.Context;
import com.jarvis.assist.aosui.core.AppGridModel;
import com.jarvis.assist.aosui.home.AdvWidgets;
import java.util.LinkedHashMap;

/* loaded from: /tmp/augmaos/classes.dex */
public final class Widgets {
    static final LinkedHashMap<String, Factory> F = new LinkedHashMap<>();

    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Factory {
        WidgetView make(Context context, HomeCtx homeCtx);
    }

    private Widgets() {
    }

    static {
        register("greeting", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda0
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new GreetingWidget(context, homeCtx);
            }
        });
        register("health", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda11
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new HealthWidget(context, homeCtx);
            }
        });
        register("weather", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda13
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new WeatherWidget(context, homeCtx);
            }
        });
        register("calendar", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda14
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new CalendarWidget(context, homeCtx);
            }
        });
        register("recent", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda15
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new RecentWidget(context, homeCtx);
            }
        });
        register("media", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda16
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new MediaWidget(context, homeCtx);
            }
        });
        register("alarms", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda17
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new AlarmsWidget(context, homeCtx);
            }
        });
        register("notifs", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda18
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new NotifsWidget(context, homeCtx);
            }
        });
        register("battery", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda19
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new BatteryWidget(context, homeCtx);
            }
        });
        register("sleep", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda20
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new SleepWidget(context, homeCtx);
            }
        });
        register("activity", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda1
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new ActivityWidget(context, homeCtx);
            }
        });
        register("people", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda2
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new PeopleWidget(context, homeCtx);
            }
        });
        register("screentime", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda3
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new ScreenTimeWidget(context, homeCtx);
            }
        });
        register("worldclock", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda4
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new WorldClockWidget(context, homeCtx);
            }
        });
        register("assistant", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda5
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new AssistantWidget(context, homeCtx);
            }
        });
        register("storage", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda6
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new StorageWidget(context, homeCtx);
            }
        });
        register(AppGridModel.ID, new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda7
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new AppGridWidget(context, homeCtx);
            }
        });
        register("gmail", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda8
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new GmailWidget(context, homeCtx);
            }
        });
        register("advhead", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda9
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new AdvWidgets.Head(context, homeCtx);
            }
        });
        register("advtiles", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda10
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new AdvWidgets.Tiles(context, homeCtx);
            }
        });
        register("advday", new Factory() { // from class: com.jarvis.assist.aosui.home.Widgets$$ExternalSyntheticLambda12
            @Override // com.jarvis.assist.aosui.home.Widgets.Factory
            public final WidgetView make(Context context, HomeCtx homeCtx) {
                return new AdvWidgets.Day(context, homeCtx);
            }
        });
    }

    public static void register(String str, Factory factory) {
        F.put(str, factory);
    }

    public static boolean has(String str) {
        return F.containsKey(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static WidgetView make(String str, Context context, HomeCtx homeCtx) {
        if (str.startsWith("aw:")) {
            return new PhoneWidget(context, homeCtx, str);
        }
        Factory factory = F.get(str);
        if (factory == null) {
            return null;
        }
        return factory.make(context, homeCtx);
    }
}
