package com.jarvis.assist.aosui;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import com.jarvis.assist.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Process;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.FrameMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;
import com.jarvis.assist.aosui.Apps;
import com.jarvis.assist.aosui.HealthSources;
import com.jarvis.assist.aosui.Home;
import com.jarvis.assist.aosui.PhoneAI;
import com.jarvis.assist.aosui.Voice;
import com.jarvis.assist.aosui.Weather;
import com.jarvis.assist.aosui.ai.Keys;
import com.jarvis.assist.aosui.ai.NativeAssist;
import com.jarvis.assist.aosui.brief.BriefPage;
import com.jarvis.assist.aosui.core.Advanced;
import com.jarvis.assist.aosui.core.Feedback;
import com.jarvis.assist.aosui.core.FxModel;
import com.jarvis.assist.aosui.core.HomeRules;
import com.jarvis.assist.aosui.core.HomeTheme;
import com.jarvis.assist.aosui.core.MemPolicy;
import com.jarvis.assist.aosui.core.Memory;
import com.jarvis.assist.aosui.core.OnboardModel;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.VaultLink;
import com.jarvis.assist.aosui.home.GetVault;
import com.jarvis.assist.aosui.home.NativeHome;
import com.jarvis.assist.aosui.settings.NativeSettings;
import com.jarvis.assist.aosui.ui.Kit;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: /tmp/augmaos/classes.dex */
public class AosHome extends Home {
    static final int AW_HOST = 2565;
    static final long IMG_MAX_BYTES = 41943040;
    static final long IMG_MAX_PX = 100000000;
    Consumer<String> appleCb;
    NativeAssist asst;
    Consumer<String> awCb;
    boolean awListening;
    int awPending;
    Object backCb;
    BriefPage brief;
    Consumer<String> expCb;
    String expJson;
    AppWidgetHost host;
    Consumer<String> impCb;
    boolean micWanted;
    NativeHome nat;
    Consumer<String> picNative;
    final BroadcastReceiver power;
    SharedPreferences prefs;
    boolean resumed;
    View safe;
    final BroadcastReceiver saver;
    NativeSettings sett;
    volatile String shared;
    final Object thermal;
    int touchX;
    int touchY;
    final Memory.Trim trim;
    Voice vo;
    static WeakReference<Home> front = new WeakReference<>(null);
    static final ArrayList<WeakReference<Home>> others = new ArrayList<>();
    static final String TOK = Long.toHexString(new SecureRandom().nextLong());
    final HashMap<Integer, AppWidgetHostView> awv = new HashMap<>();
    final ViewTreeObserver.OnDrawListener firstDraw = new FirstDrawListener();
    final Runnable appsChanged = new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            m7lambda$new$12$comaugmaosoHome();
        }
    };

    public AosHome() {
        this.thermal = Build.VERSION.SDK_INT >= 29 ? new PowerManager.OnThermalStatusChangedListener() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda7
            @Override // android.os.PowerManager.OnThermalStatusChangedListener
            public final void onThermalStatusChanged(int i) {
                m8lambda$new$13$comaugmaosoHome(i);
            }
        } : null;
        this.power = new BroadcastReceiver() { // from class: com.jarvis.assist.aosui.Home.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (nat != null) {
                    nat.onPowerConnected();
                }
            }
        };
        this.saver = new BroadcastReceiver() { // from class: com.jarvis.assist.aosui.Home.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Perf.powerSave(saving());
                if (sett != null) {
                    sett.onEvent("onPowerSave");
                }
            }
        };
        this.trim = new Memory.Trim() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda8
            @Override // com.jarvis.assist.aosui.core.Memory.Trim
            public final void trim(int i, int i2) {
                m9lambda$new$15$comaugmaosoHome(i, i2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Home front() {
        return front.get();
    }

    static void finishOthers() {
        synchronized (others) {
            Iterator<WeakReference<Home>> it = others.iterator();
            while (it.hasNext()) {
                Home home = it.next().get();
                if (home != null && !home.isFinishing()) {
                    try {
                        home.finish();
                    } catch (Exception e) {
                    }
                }
            }
            others.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$frameSpikes$0(Window window, FrameMetrics frameMetrics, int i) {
        long metric = frameMetrics.getMetric(8) / 1000000;
        if (metric < 32) {
            return;
        }
        Log.i("aOS.frame", "total " + metric + " input " + (frameMetrics.getMetric(1) / 1000000) + " anim " + (frameMetrics.getMetric(2) / 1000000) + " layout " + (frameMetrics.getMetric(3) / 1000000) + " draw " + (frameMetrics.getMetric(4) / 1000000) + " sync " + (frameMetrics.getMetric(5) / 1000000) + " gpu " + (frameMetrics.getMetric(6) / 1000000) + " swap " + (frameMetrics.getMetric(7) / 1000000) + " delay " + (frameMetrics.getMetric(0) / 1000000));
    }

    void frameSpikes() {
        try {
            getWindow().addOnFrameMetricsAvailableListener(new Window.OnFrameMetricsAvailableListener() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda22
                @Override // android.view.Window.OnFrameMetricsAvailableListener
                public final void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int i) {
                    Home.lambda$frameSpikes$0(window, frameMetrics, i);
                }
            }, new Handler(Looper.getMainLooper()));
        } catch (Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void notif(String str) {
        NativeHome.noteNotif(str);
        Home front2 = front();
        if (front2 == null || front2.nat == null) {
            return;
        }
        front2.nat.onNotif();
    }

    AppWidgetHost host() {
        if (this.host == null) {
            try {
                this.host = new AppWidgetHost(getApplicationContext(), AW_HOST);
            } catch (Throwable th) {
            }
        }
        return this.host;
    }

    AppWidgetManager awm() {
        try {
            return AppWidgetManager.getInstance(this);
        } catch (Throwable th) {
            return null;
        }
    }

    void awListen(boolean z) {
        if (z == this.awListening) {
            return;
        }
        try {
            if (z) {
                host().startListening();
            } else {
                host().stopListening();
            }
            this.awListening = z;
        } catch (Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View awView(int i) {
        AppWidgetHostView appWidgetHostView = this.awv.get(Integer.valueOf(i));
        if (appWidgetHostView != null) {
            return appWidgetHostView;
        }
        try {
            AppWidgetProviderInfo appWidgetInfo = awm().getAppWidgetInfo(i);
            if (appWidgetInfo == null) {
                return null;
            }
            if (!isFinishing()) {
                awListen(true);
            }
            AppWidgetHostView createView = host().createView(this, i, appWidgetInfo);
            this.awv.put(Integer.valueOf(i), createView);
            return createView;
        } catch (Throwable th) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void awShown(int i) {
        AppWidgetHostView appWidgetHostView = this.awv.get(Integer.valueOf(i));
        AppWidgetManager awm = awm();
        if (appWidgetHostView == null || awm == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(appWidgetHostView);
        while (!arrayList.isEmpty()) {
            View view = (View) arrayList.remove(arrayList.size() - 1);
            if (view instanceof AdapterView) {
                Adapter adapter = ((AdapterView) view).getAdapter();
                if (view.getId() != -1 && (adapter == null || adapter.getCount() == 0)) {
                    try {
                        awm.notifyAppWidgetViewDataChanged(i, view.getId());
                    } catch (Throwable th) {
                    }
                }
            } else if (view instanceof ViewGroup) {
                int i2 = 0;
                while (true) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    if (i2 < viewGroup.getChildCount()) {
                        arrayList.add(viewGroup.getChildAt(i2));
                        i2++;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void awDrop(int i) {
        AppWidgetHostView remove = this.awv.remove(Integer.valueOf(i));
        if (remove == null || !(remove.getParent() instanceof ViewGroup)) {
            return;
        }
        ((ViewGroup) remove.getParent()).removeView(remove);
    }

    JSONArray awList() {
        try {
            return new JSONArray(this.prefs.getString("aw.list", "[]"));
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    void awDone(int i, boolean z) {
        Consumer<String> consumer = this.awCb;
        this.awCb = null;
        this.awPending = 0;
        if (!z) {
            try {
                host().deleteAppWidgetId(i);
            } catch (Throwable th) {
            }
            answer(consumer, "{\"ok\":false}");
            return;
        }
        try {
            AppWidgetProviderInfo appWidgetInfo = awm().getAppWidgetInfo(i);
            String loadLabel = appWidgetInfo == null ? "Widget" : appWidgetInfo.loadLabel(getPackageManager());
            JSONArray awList = awList();
            awList.put(new JSONObject().put("id", i).put("label", loadLabel).put("cn", appWidgetInfo == null ? "" : appWidgetInfo.provider.flattenToShortString()));
            this.prefs.edit().putString("aw.list", awList.toString()).apply();
            answer(consumer, new JSONObject().put("ok", true).put("id", i).put("label", loadLabel).toString());
        } catch (Throwable th2) {
            answer(consumer, "{\"ok\":false}");
        }
    }

    void awConfigure(int i) {
        int i2;
        try {
            AppWidgetProviderInfo appWidgetInfo = awm().getAppWidgetInfo(i);
            if (appWidgetInfo == null || appWidgetInfo.configure == null) {
                i2 = i;
            } else {
                i2 = i;
                try {
                    host().startAppWidgetConfigureActivityForResult(this, i2, 0, 7602, null);
                    return;
                } catch (Throwable th) {
                }
            }
        } catch (Throwable th2) {
            i2 = i;
        }
        awDone(i2, true);
    }

    void openSettingsGoogle() {
        try {
            startActivity(new Intent(this, (Class<?>) Apps.AppSettings.class).putExtra("s", "google").addFlags(335544320));
        } catch (Exception e) {
        }
    }

    boolean oauth(Intent intent) {
        final Uri data = intent == null ? null : intent.getData();
        if (data != null && "aos".equals(data.getScheme()) && "google".equals(data.getHost())) {
            openSettingsGoogle();
            return true;
        }
        if (data == null || !"aos".equals(data.getScheme())) {
            return false;
        }
        if (!"whoop".equals(data.getHost()) && !"oura".equals(data.getHost())) {
            return false;
        }
        final Context applicationContext = getApplicationContext();
        Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                m10lambda$oauth$2$comaugmaosoHome(applicationContext, data);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$oauth$2$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m10lambda$oauth$2$comaugmaosoHome(final Context context, final Uri uri) {
        String finish = HealthSources.finish(context, uri);
        final boolean contains = finish.contains("\"ok\":true");
        Log.i("aOSHealth", uri.getHost() + " sign-in: " + (contains ? "ok" : "failed"));
        event("onHealthSources");
        runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                Context context2 = context;
                Uri uri2 = uri;
                boolean z = contains;
                Toast.makeText(context2, uri2.getHost().toUpperCase(Locale.ROOT) + (r3 ? " connected" : " sign-in failed"), 0).show();
            }
        });
        try {
            startActivity(new Intent(context, (Class<?>) Apps.AppSettings.class).putExtra("s", "health:" + uri.getHost()).putExtra("res", finish).addFlags(268435456));
        } catch (Exception e) {
            toast("Could not open that");
        }
    }

    static String screen(Intent intent) {
        String stringExtra = intent == null ? null : intent.getStringExtra("s");
        if (stringExtra == null || stringExtra.length() > 40 || !stringExtra.matches("^[a-z]+(:[a-z]+)?$")) {
            return null;
        }
        return stringExtra;
    }

    static String screenRes(Intent intent) {
        String stringExtra = intent == null ? null : intent.getStringExtra("res");
        if (stringExtra == null || stringExtra.length() > 4000) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(stringExtra);
            return new JSONObject().put("ok", jSONObject.optBoolean("ok")).put("error", jSONObject.optString("error", "").replaceAll("[^A-Za-z0-9 .,:_-]", "")).toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 60) {
            event("onPerm");
            return;
        }
        if (i == 7601) {
            if (this.awPending == 0) {
                return;
            }
            int i3 = this.awPending;
            if (i2 == -1) {
                awConfigure(i3);
                return;
            } else {
                awDone(i3, false);
                return;
            }
        }
        if (i == 7602) {
            if (this.awPending == 0) {
                return;
            }
            awDone(this.awPending, i2 == -1);
            return;
        }
        Uri uri = null;
        if (i == 7501) {
            final String str = this.expJson;
            final Consumer<String> consumer = this.expCb;
            this.expJson = null;
            this.expCb = null;
            if (i2 == -1 && intent != null) {
                uri = intent.getData();
            }
            if (uri == null || str == null) {
                answer(consumer, "{\"ok\":false,\"error\":\"CANCELLED\"}");
                return;
            } else {
                Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        m11lambda$onActivityResult$3$comaugmaosoHome(uri, str, consumer);
                    }
                });
                return;
            }
        }
        if (i == 7502) {
            final Consumer<String> consumer2 = this.impCb;
            this.impCb = null;
            if (i2 == -1 && intent != null) {
                uri = intent.getData();
            }
            if (uri == null) {
                answer(consumer2, "{\"ok\":false,\"error\":\"CANCELLED\"}");
                return;
            } else {
                Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        m12lambda$onActivityResult$4$comaugmaosoHome(uri, consumer2);
                    }
                });
                return;
            }
        }
        if (i == 7401) {
            final Consumer<String> consumer3 = this.appleCb;
            this.appleCb = null;
            if (i2 == -1 && intent != null) {
                uri = intent.getData();
            }
            if (uri != null) {
                Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        m14lambda$onActivityResult$6$comaugmaosoHome(uri, consumer3);
                    }
                });
                return;
            } else {
                if (consumer3 != null) {
                    answer(consumer3, "{\"ok\":false,\"error\":\"NO FILE\"}");
                    return;
                }
                return;
            }
        }
        if (i == 7701 && this.picNative != null) {
            final Consumer<String> consumer4 = this.picNative;
            this.picNative = null;
            final ArrayList arrayList = new ArrayList();
            if (i2 == -1 && intent != null) {
                if (intent.getClipData() != null) {
                    for (int i4 = 0; i4 < intent.getClipData().getItemCount(); i4++) {
                        arrayList.add(intent.getClipData().getItemAt(i4).getUri());
                    }
                } else if (intent.getData() != null) {
                    arrayList.add(intent.getData());
                }
            }
            Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    m15lambda$onActivityResult$7$comaugmaosoHome(arrayList, consumer4);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onActivityResult$3$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m11lambda$onActivityResult$3$comaugmaosoHome(Uri uri, String str, Consumer consumer) {
        try {
            OutputStream openOutputStream = getContentResolver().openOutputStream(uri, "wt");
            try {
                openOutputStream.write(str.getBytes("UTF-8"));
                answer(consumer, "{\"ok\":true}");
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
            } finally {
            }
        } catch (Exception e) {
            answer(consumer, "{\"ok\":false,\"error\":\"COULD NOT WRITE FILE\"}");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onActivityResult$4$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m12lambda$onActivityResult$4$comaugmaosoHome(Uri uri, Consumer consumer) {
        try {
            InputStream openInputStream = getContentResolver().openInputStream(uri);
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[8192];
                do {
                    int read = openInputStream.read(bArr);
                    if (read <= 0) {
                        answer(consumer, new JSONObject().put("ok", true).put("text", byteArrayOutputStream.toString("UTF-8")).toString());
                        if (openInputStream != null) {
                            openInputStream.close();
                            return;
                        }
                        return;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                } while (byteArrayOutputStream.size() <= 524288);
                answer(consumer, "{\"ok\":false,\"error\":\"FILE TOO BIG\"}");
                if (openInputStream != null) {
                    openInputStream.close();
                }
            } finally {
            }
        } catch (Exception e) {
            answer(consumer, "{\"ok\":false,\"error\":\"COULD NOT READ FILE\"}");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onActivityResult$5$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m13lambda$onActivityResult$5$comaugmaosoHome(Consumer consumer, int i) {
        if (consumer != null) {
            answer(consumer, "{\"progress\":" + i + "}");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onActivityResult$6$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m14lambda$onActivityResult$6$comaugmaosoHome(Uri uri, final Consumer consumer) {
        String importApple = HealthSources.importApple(getApplicationContext(), uri, new HealthSources.Progress() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda11
            @Override // com.jarvis.assist.aosui.HealthSources.Progress
            public final void on(int i) {
                m13lambda$onActivityResult$5$comaugmaosoHome(consumer, i);
            }
        });
        if (consumer != null) {
            answer(consumer, importApple);
        }
        event("onHealthSources");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onActivityResult$7$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m15lambda$onActivityResult$7$comaugmaosoHome(List list, Consumer consumer) {
        String str;
        try {
            str = new JSONObject().put("ok", !list.isEmpty()).put("images", images(list)).toString();
        } catch (Exception e) {
            str = "{\"ok\":false}";
        }
        consumer.accept(str);
    }

    boolean aoaReveal() {
        return HomeTheme.aoa(Store.get(Advanced.KEY, ""));
    }

    Bundle revealFromTouch() {
        int round = Math.round(getResources().getDisplayMetrics().density * 40.0f);
        View decorView = getWindow().getDecorView();
        int i = this.touchX - round;
        int i2 = this.touchY - round;
        int i3 = round * 2;
        return ActivityOptions.makeClipRevealAnimation(decorView, i, i2, i3, i3).toBundle();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.touchX = (int) motionEvent.getX();
            this.touchY = (int) motionEvent.getY();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        backOn();
        getClass();
        if (true && Crash.safe(this)) {
            this.safe = SafeMode.view((android.app.Activity)this);
            setContentView(this.safe);
            return;
        }
        this.prefs = getSharedPreferences("home", 0);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(1792);
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = (Build.VERSION.SDK_INT < 30 || false) ? 1 : 3;
            window.setAttributes(attributes);
        }
        Memory.init(this);
        Memory.on(this.trim);
        AppIndex.init(this);
        try {
            registerReceiver(this.saver, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"), 4);
        } catch (Exception e) {
        }
        try {
            registerReceiver(this.power, new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED"), 2);
        } catch (Exception e2) {
        }
        if (false) {
            synchronized (others) {
                others.removeIf(new Predicate() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda12
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return Home.lambda$onCreate$8((WeakReference) obj);
                    }
                });
                others.add(new java.lang.ref.WeakReference<>(this));
            }
        }
        if ((this.getClass().getName().contains("AppSettings"))) {
            slideDeclare();
            this.sett = new NativeSettings(new Backend((Home)this));
            setContentView(this.sett.view());
            applyPerf();
            String action = getIntent() != null ? getIntent().getAction() : null;
            r0 = "android.intent.action.VIEW_PERMISSION_USAGE".equals(action) || "androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE".equals(action);
            this.sett.open(r0 ? "privacy" : screen(getIntent()), r0 ? null : screenRes(getIntent()));
            oauth(getIntent());
            return;
        }
        if ((this.getClass().getName().contains("AppAssist"))) {
            this.asst = new NativeAssist(new Backend((Home)this));
            setContentView(this.asst.view());
            applyPerf();
            String action2 = getIntent() != null ? getIntent().getAction() : null;
            String stringExtra = getIntent() != null ? getIntent().getStringExtra("ask") : null;
            String str = (stringExtra == null || TOK.equals(getIntent().getStringExtra("tok"))) ? stringExtra : null;
            boolean z = "android.intent.action.ASSIST".equals(action2) || "android.intent.action.VOICE_COMMAND".equals(action2) || (getIntent() != null && getIntent().getBooleanExtra("voice", false));
            NativeAssist nativeAssist = this.asst;
            if (z && str == null) {
                r0 = true;
            }
            nativeAssist.start(str, r0);
            takeShare(getIntent());
            return;
        }
        if ((this.getClass().getName().contains("AppBrief"))) {
            this.brief = new BriefPage(new Backend((Home)this));
            setContentView(this.brief.view());
            applyPerf();
            this.brief.open();
            return;
        }
        AppIndex.on(this.appsChanged);
        Crash.starting(this);
        this.nat = new NativeHome(new Backend((Home)this));
        setContentView(this.nat.view());
        getWindow().getDecorView().getViewTreeObserver().addOnDrawListener(this.firstDraw);
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                PowerManager powerManager = (PowerManager) getSystemService(PowerManager.class);
                if (powerManager != null) {
                    powerManager.addThermalStatusListener(getMainExecutor(), (PowerManager.OnThermalStatusChangedListener) this.thermal);
                }
            } catch (Exception e3) {
            }
        }
        oauth(getIntent());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$onCreate$8(WeakReference weakReference) {
        return weakReference.get() == null;
    }

    private /* synthetic */ void lambda$onCreate$9(String str) {
        this.asst.debug(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.jarvis.assist.aosui.Home$1, reason: invalid class name */
    /* loaded from: /tmp/augmaos/classes.dex */
    public class FirstDrawListener implements ViewTreeObserver.OnDrawListener {
        boolean done;

        FirstDrawListener() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$onDraw$0$com-augma-oso-Home$1, reason: not valid java name */
        public /* synthetic */ void m20lambda$onDraw$0$comaugmaosoHome$1(View view) {
            view.getViewTreeObserver().removeOnDrawListener(this);
        }

        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            if (this.done) {
                return;
            }
            this.done = true;
            Crash.drawn(Home.this);
            final View decorView = getWindow().getDecorView();
            decorView.post(new Runnable() { // from class: com.jarvis.assist.aosui.Home$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    m20lambda$onDraw$0$comaugmaosoHome$1(decorView);
                }
            });
        }
    }

    void takeShare(Intent intent) {
        String action = intent == null ? null : intent.getAction();
        if ("android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action)) {
            CharSequence charSequenceExtra = intent.getCharSequenceExtra("android.intent.extra.TEXT");
            String stringExtra = intent.getStringExtra("android.intent.extra.SUBJECT");
            final ArrayList arrayList = new ArrayList();
            try {
                if ("android.intent.action.SEND".equals(action)) {
                    Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.STREAM");
                    if (uri != null) {
                        arrayList.add(uri);
                    }
                } else {
                    ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                    if (parcelableArrayListExtra != null) {
                        arrayList.addAll(parcelableArrayListExtra);
                    }
                }
            } catch (Exception e) {
            }
            String trim = charSequenceExtra == null ? "" : charSequenceExtra.toString().trim();
            if (trim.isEmpty() && stringExtra != null) {
                trim = stringExtra.trim();
            }
            if (trim.length() > 4000) {
                trim = trim.substring(0, 4000);
            }
            Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    m17lambda$takeShare$11$comaugmaosoHome(trim, arrayList);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$takeShare$10$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m16lambda$takeShare$10$comaugmaosoHome() {
        if (this.asst != null) {
            this.asst.onShare();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$takeShare$11$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m17lambda$takeShare$11$comaugmaosoHome(String str, List list) {
        try {
            this.shared = new JSONObject().put("text", str).put("images", images(list)).toString();
            if (this.asst != null) {
                runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        m16lambda$takeShare$10$comaugmaosoHome();
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    JSONArray images(List<Uri> list) {
        JSONArray jSONArray = new JSONArray();
        long j = 0;
        for (Uri uri : list) {
            if (jSONArray.length() >= 3) {
                break;
            }
            if (uri != null && "content".equals(uri.getScheme())) {
                long j2 = -1;
                try {
                    try {
                        AssetFileDescriptor openAssetFileDescriptor = getContentResolver().openAssetFileDescriptor(uri, "r");
                        if (openAssetFileDescriptor != null) {
                            try {
                                j2 = openAssetFileDescriptor.getLength();
                            } catch (Throwable th) {
                                if (openAssetFileDescriptor != null) {
                                    try {
                                        openAssetFileDescriptor.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                }
                                throw th;
                                break;
                            }
                        }
                        if (openAssetFileDescriptor != null) {
                            openAssetFileDescriptor.close();
                        }
                    } catch (Exception e) {
                    }
                    if (j2 > IMG_MAX_BYTES) {
                        Log.w("aOS", "shared image skipped: too large");
                    } else {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        InputStream openInputStream = getContentResolver().openInputStream(uri);
                        try {
                            BitmapFactory.decodeStream(openInputStream, null, options);
                            if (openInputStream != null) {
                                openInputStream.close();
                            }
                            if (options.outWidth > 0 && options.outHeight > 0 && options.outWidth * options.outHeight <= IMG_MAX_PX) {
                                int i = 1;
                                while (true) {
                                    int i2 = i * 2;
                                    if (Math.max(options.outWidth, options.outHeight) / i2 < 1568) {
                                        break;
                                    }
                                    i = i2;
                                }
                                BitmapFactory.Options options2 = new BitmapFactory.Options();
                                options2.inSampleSize = i;
                                openInputStream = getContentResolver().openInputStream(uri);
                                try {
                                    Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream, null, options2);
                                    if (openInputStream != null) {
                                        openInputStream.close();
                                    }
                                    if (decodeStream != null) {
                                        float min = Math.min(1.0f, 1568.0f / Math.max(decodeStream.getWidth(), decodeStream.getHeight()));
                                        if (min < 1.0f) {
                                            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeStream, Math.max(1, Math.round(decodeStream.getWidth() * min)), Math.max(1, Math.round(decodeStream.getHeight() * min)), true);
                                            if (createScaledBitmap != decodeStream) {
                                                decodeStream.recycle();
                                            }
                                            decodeStream = createScaledBitmap;
                                        }
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        decodeStream.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                                        int width = decodeStream.getWidth();
                                        int height = decodeStream.getHeight();
                                        decodeStream.recycle();
                                        if (byteArrayOutputStream.size() + j > 4194304) {
                                            break;
                                        }
                                        j += byteArrayOutputStream.size();
                                        jSONArray.put(new JSONObject().put("mime", "image/jpeg").put("w", width).put("h", height).put("data", Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2)));
                                    }
                                } finally {
                                }
                            }
                        } finally {
                        }
                    }
                } catch (FileNotFoundException e2) {
                    Exception e = e2;
                    Log.w("aOS", "shared image not readable: " + e.getClass().getSimpleName());
                } catch (SecurityException e3) {
                    Exception e = e3;
                    Log.w("aOS", "shared image not readable: " + e.getClass().getSimpleName());
                } catch (Throwable th3) {
                    Log.w("aOS", "shared image skipped: " + th3.getClass().getSimpleName());
                }
            }
        }
        return jSONArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$12$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m7lambda$new$12$comaugmaosoHome() {
        NativeHome nativeHome = this.nat;
        if (nativeHome != null) {
            nativeHome.onApps();
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.nat != null) {
            this.nat.onConfig();
        }
        if (this.asst != null) {
            this.asst.onConfig();
        }
        if (this.sett != null) {
            this.sett.onEvent("onConfig");
        }
        if (this.brief != null) {
            this.brief.onConfig();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$13$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m8lambda$new$13$comaugmaosoHome(int i) {
        if (this.nat != null) {
            this.nat.onThermal(i);
        }
    }

    int thermalNow() {
        if (Build.VERSION.SDK_INT < 29) {
            return 0;
        }
        try {
            PowerManager powerManager = (PowerManager) getSystemService(PowerManager.class);
            if (powerManager == null) {
                return 0;
            }
            return powerManager.getCurrentThermalStatus();
        } catch (Exception e) {
            return 0;
        }
    }

    JSONObject perfPref() {
        try {
            return new JSONObject(this.prefs.getString("aos.perf", "{}"));
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyPerf() {
        runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                m5lambda$applyPerf$14$comaugmaosoHome();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$applyPerf$14$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m5lambda$applyPerf$14$comaugmaosoHome() {
        try {
            Window window = getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            int i = 0;
            float f = 0.0f;
            if (perfPref().optBoolean("high")) {
                Display.Mode mode = defaultDisplay.getMode();
                Display.Mode[] supportedModes = defaultDisplay.getSupportedModes();
                int length = supportedModes.length;
                float f2 = 0.0f;
                int i2 = 0;
                while (i < length) {
                    Display.Mode mode2 = supportedModes[i];
                    if (mode2.getPhysicalWidth() == mode.getPhysicalWidth() && mode2.getPhysicalHeight() == mode.getPhysicalHeight() && mode2.getRefreshRate() > f2) {
                        f2 = mode2.getRefreshRate();
                        i2 = mode2.getModeId();
                    }
                    i++;
                }
                i = i2;
                f = f2;
            }
            if (attributes.preferredDisplayModeId == i && attributes.preferredRefreshRate == f) {
                return;
            }
            attributes.preferredDisplayModeId = i;
            attributes.preferredRefreshRate = f;
            window.setAttributes(attributes);
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String deviceInfo() {
        JSONObject jSONObject = new JSONObject();
        try {
            ActivityManager activityManager = (ActivityManager) getSystemService(ActivityManager.class);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            if (activityManager != null) {
                activityManager.getMemoryInfo(memoryInfo);
                jSONObject.put("memClass", activityManager.getMemoryClass()).put("lowRam", activityManager.isLowRamDevice()).put("totalMem", memoryInfo.totalMem);
            }
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            JSONArray jSONArray = new JSONArray();
            TreeSet treeSet = new TreeSet();
            for (Display.Mode mode : defaultDisplay.getSupportedModes()) {
                treeSet.add(Integer.valueOf(Math.round(mode.getRefreshRate())));
            }
            Iterator it = treeSet.iterator();
            while (it.hasNext()) {
                jSONArray.put(((Integer) it.next()).intValue());
            }
            jSONObject.put("cores", Runtime.getRuntime().availableProcessors()).put("rates", jSONArray).put("rate", Math.round(defaultDisplay.getRefreshRate())).put("sdk", Build.VERSION.SDK_INT).put("abis", new JSONArray((Collection) Arrays.asList(Build.SUPPORTED_ABIS))).put("thermal", thermalNow()).put("powerSave", saving());
        } catch (Exception e) {
        }
        return jSONObject.toString();
    }

    boolean saving() {
        try {
            PowerManager powerManager = (PowerManager) getSystemService("power");
            if (powerManager != null) {
                return powerManager.isPowerSaveMode();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        if (this.nat != null) {
            this.nat.onStart();
        }
        if (this.nat != null) {
            awListen(true);
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        Home front2 = front();
        if (this.nat != null && (front2 == null || front2 == (Home)this)) {
            this.nat.onStop();
        }
        if (this.nat != null) {
            awListen(false);
        }
        if (this.vo != null && this.nat == null) {
            this.vo.destroy();
            this.vo = null;
        }
        super.onStop();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$15$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m9lambda$new$15$comaugmaosoHome(int i, int i2) {
        if (MemPolicy.has(i2, 16) && this.vo != null && !this.resumed) {
            this.vo.destroy();
            this.vo = null;
        }
        if (this.asst != null) {
            this.asst.trim(i, i2);
        }
        if (this.sett != null) {
            this.sett.trim(i, i2);
        }
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (this.safe == null && !oauth(intent)) {
            if (getClass().getName().endsWith("AppAssist") && intent != null && ("android.intent.action.SEND".equals(intent.getAction()) || "android.intent.action.SEND_MULTIPLE".equals(intent.getAction()))) {
                takeShare(intent);
                return;
            }
            if (getClass().getName().endsWith("AppAssist") && intent != null && ("android.intent.action.ASSIST".equals(intent.getAction()) || intent.getBooleanExtra("voice", false))) {
                talk();
                return;
            }
            if (getClass().getName().endsWith("AppSettings") && intent != null && ("android.intent.action.VIEW_PERMISSION_USAGE".equals(intent.getAction()) || "androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE".equals(intent.getAction()))) {
                setIntent(intent);
                if (this.sett != null) {
                    this.sett.open("privacy", null);
                    return;
                }
                return;
            }
            if (getClass().getName().endsWith("AppSettings") && intent != null && screen(intent) != null) {
                setIntent(intent);
                if (this.sett != null) {
                    this.sett.open(screen(intent), screenRes(intent));
                    return;
                }
                return;
            }
            if (this.brief != null) {
                this.brief.open();
                return;
            }
            if (this.asst != null && intent != null && intent.getStringExtra("ask") != null && TOK.equals(intent.getStringExtra("tok"))) {
                this.asst.ask(intent.getStringExtra("ask"), null);
            } else if (this.nat != null) {
                this.nat.onHomeKey();
            }
        }
    }

    void talk() {
        if (this.asst != null) {
            this.asst.onTalk();
        }
    }

    void backOn() {
        if (Build.VERSION.SDK_INT < 33) {
            return;
        }
        try {
            OnBackInvokedCallback onBackInvokedCallback = new OnBackInvokedCallback() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda14
                @Override // android.window.OnBackInvokedCallback
                public final void onBackInvoked() {
                    back();
                }
            };
            this.backCb = onBackInvokedCallback;
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(0, onBackInvokedCallback);
        } catch (Throwable th) {
            this.backCb = null;
        }
    }

    void backOff() {
        if (Build.VERSION.SDK_INT < 33 || this.backCb == null) {
            return;
        }
        try {
            getOnBackInvokedDispatcher().unregisterOnBackInvokedCallback((OnBackInvokedCallback) this.backCb);
        } catch (Throwable th) {
        }
        this.backCb = null;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        back();
    }

    static int[] slideAnims(boolean z) {
        return Perf.k().calm ? new int[]{0, 0} : z ? new int[]{R.anim.aos_slide_in_right, R.anim.aos_slide_out_left} : new int[]{R.anim.aos_slide_in_left, R.anim.aos_slide_out_right};
    }

    void slideIn() {
        if (Build.VERSION.SDK_INT >= 34) {
            return;
        }
        int[] slideAnims = slideAnims(true);
        overridePendingTransition(slideAnims[0], slideAnims[1]);
    }

    void slideDeclare() {
        if (Build.VERSION.SDK_INT < 34) {
            return;
        }
        int[] slideAnims = slideAnims(true);
        int[] slideAnims2 = slideAnims(false);
        overrideActivityTransition(0, slideAnims[0], slideAnims[1]);
        overrideActivityTransition(1, slideAnims2[0], slideAnims2[1]);
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        if (!((this.getClass().getName().contains("AppSettings"))) || Build.VERSION.SDK_INT >= 34) {
            return;
        }
        int[] slideAnims = slideAnims(false);
        overridePendingTransition(slideAnims[0], slideAnims[1]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void back() {
        if (this.safe != null) {
            return;
        }
        if (this.nat != null) {
            this.nat.onBack();
            return;
        }
        if (this.asst != null) {
            if (this.asst.onBack()) {
                return;
            }
            finish();
        } else if (this.sett != null) {
            if (this.sett.onBack()) {
                return;
            }
            finish();
        } else if (this.brief == null) {
            finish();
        } else {
            if (this.brief.onBack()) {
                return;
            }
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.resumed = false;
        if (front() == (Home)this) {
            front.clear();
        }
        if (this.nat != null) {
            this.nat.onPause();
        }
        if (this.asst != null) {
            this.asst.onPause();
        }
        if (this.sett != null) {
            this.sett.onPause();
        }
        if (this.brief != null) {
            this.brief.onPause();
        }
        if (this.vo != null) {
            this.vo.stopSpeak();
            this.vo.destroyRec();
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        boolean anyMatch;
        super.onResume();
        this.resumed = true;
        front = new java.lang.ref.WeakReference<>(this);
        hideBars();
        if (this.micWanted && checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
            micAnswered();
        }
        if (true) {
            synchronized (others) {
                others.removeIf(new Predicate() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda15
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return Home.lambda$onResume$16((WeakReference) obj);
                    }
                });
                anyMatch = others.stream().anyMatch(new Predicate() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda16
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return Home.lambda$onResume$17((WeakReference) obj);
                    }
                });
            }
            if (anyMatch) {
                Memory.reclaimSoon();
            }
        }
        if (this.nat != null) {
            finishOthers();
            applyPerf();
            this.nat.onResume();
        } else {
            if (this.asst != null) {
                applyPerf();
                this.asst.onResume();
                return;
            }
            if (this.sett != null) {
                applyPerf();
                this.sett.onResume();
            }
            if (this.brief != null) {
                applyPerf();
                this.brief.onResume();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$onResume$16(WeakReference weakReference) {
        return weakReference.get() == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$onResume$17(WeakReference weakReference) {
        Home home = (Home) weakReference.get();
        return home != null && (home.isFinishing() || home.isDestroyed());
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            hideBars();
        }
    }

    void hideBars() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 30) {
            window.setDecorFitsSystemWindows(false);
            WindowInsetsController insetsController = window.getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                insetsController.setSystemBarsBehavior(2);
                return;
            }
            return;
        }
        window.getDecorView().setSystemUiVisibility(5894);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        backOff();
        if (this.nat != null) {
            this.nat.onDestroy();
            this.nat = null;
        }
        if (this.asst != null) {
            this.asst.onDestroy();
            this.asst = null;
        }
        if (this.sett != null) {
            this.sett.onDestroy();
            this.sett = null;
        }
        if (this.brief != null) {
            this.brief.onDestroy();
            this.brief = null;
        }
        if (this.vo != null) {
            this.vo.destroy();
        }
        this.vo = null;
        if (front() == (Home)this) {
            front.clear();
        }
        Memory.off(this.trim);
        for (AppWidgetHostView appWidgetHostView : this.awv.values()) {
            if (appWidgetHostView.getParent() instanceof ViewGroup) {
                ((ViewGroup) appWidgetHostView.getParent()).removeView(appWidgetHostView);
            }
        }
        this.awv.clear();
        AppIndex.off(this.appsChanged);
        try {
            unregisterReceiver(this.saver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(this.power);
        } catch (Exception e2) {
        }
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                ((PowerManager) getSystemService(PowerManager.class)).removeThermalStatusListener((PowerManager.OnThermalStatusChangedListener) this.thermal);
            } catch (Exception e3) {
            }
        }
        super.onDestroy();
        if (false) {
            Memory.reclaimSoon();
        }
    }

    void micAnswered() {
        if (this.micWanted) {
            this.micWanted = false;
            voice();
            if (checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                this.vo.listen();
            } else {
                this.vo.sink.on("onVoice", "{error:'PERMISSION',msg:'Allow the microphone to talk to the assistant'}");
                this.vo.sink.on("onVoice", "{end:1}");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$voice$18$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m19lambda$voice$18$comaugmaosoHome(String str, String str2) {
        NativeAssist nativeAssist = this.asst;
        if (nativeAssist != null) {
            nativeAssist.onVoiceEvent(str, str2);
        }
        BriefPage briefPage = this.brief;
        if (briefPage != null) {
            briefPage.onVoiceEvent(str, str2);
        }
    }

    Voice voice() {
        if (this.vo == null) {
            this.vo = new Voice(this, new Voice.Ev() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda13
                @Override // com.jarvis.assist.aosui.Voice.Ev
                public final void on(String str, String str2) {
                    m19lambda$voice$18$comaugmaosoHome(str, str2);
                }
            });
        }
        return this.vo;
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 41 && Loc.ok(this)) {
            Loc.refresh(this, 0L, new Consumer() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Home.lambda$onRequestPermissionsResult$19((String) obj);
                }
            });
        }
        if (i == 46) {
            micAnswered();
        }
        event("onPerm");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onRequestPermissionsResult$19(String str) {
    }

    void orAppDetails(Intent intent, String str) {
        try {
            startActivity(intent.addFlags(268435456));
        } catch (ActivityNotFoundException | SecurityException e) {
            toast(str);
            safeStart(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", getPackageName(), null)).addFlags(268435456));
        }
    }

    void safeStart(Intent intent) {
        try {
            startActivity(intent);
        } catch (Exception e) {
            try {
                Toast.makeText(this, "Could not open that", 0).show();
            } catch (Exception e2) {
            }
        }
    }

    void event(final String str) {
        runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                m6lambda$event$20$comaugmaosoHome(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$event$20$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m6lambda$event$20$comaugmaosoHome(String str) {
        if (this.nat != null && "onPerm".equals(str)) {
            this.nat.onPerm();
        }
        if (this.asst != null && "onPerm".equals(str)) {
            this.asst.onPerm();
        }
        if (this.brief != null && "onPerm".equals(str)) {
            this.brief.onPerm();
        }
        if (this.sett != null) {
            this.sett.onEvent(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$toast$21$com-augma-oso-Home, reason: not valid java name */
    public /* synthetic */ void m18lambda$toast$21$comaugmaosoHome(String str) {
        try {
            Toast.makeText(this, str, 0).show();
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void toast(final String str) {
        runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                m18lambda$toast$21$comaugmaosoHome(str);
            }
        });
    }

    void answer(final Consumer<String> consumer, final String str) {
        if (consumer != null) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    consumer.accept(str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public class Phone {
        boolean torchOn = false;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Phone() {
        }

        boolean micOk() {
            return checkSelfPermission("android.permission.RECORD_AUDIO") == 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$speak$0$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m52lambda$speak$0$comaugmaosoHome$Phone(String str, boolean z) {
            try {
                voice().speak(new JSONArray(str), z);
            } catch (JSONException e) {
            }
        }

        public void speak(final String str, final boolean z) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    m52lambda$speak$0$comaugmaosoHome$Phone(str, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$sayStop$1$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m48lambda$sayStop$1$comaugmaosoHome$Phone() {
            if (vo != null) {
                vo.stopSpeak();
            }
        }

        public void sayStop() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda44
                @Override // java.lang.Runnable
                public final void run() {
                    m48lambda$sayStop$1$comaugmaosoHome$Phone();
                }
            });
        }

        public void listen() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    m35lambda$listen$2$comaugmaosoHome$Phone();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$listen$2$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m35lambda$listen$2$comaugmaosoHome$Phone() {
            if (micOk()) {
                voice().listen();
            } else {
                micWanted = true;
                askPerm("mic");
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$listenStop$3$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m37lambda$listenStop$3$comaugmaosoHome$Phone() {
            if (vo != null) {
                vo.stopListen();
            }
        }

        public void listenStop() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    m37lambda$listenStop$3$comaugmaosoHome$Phone();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$listenCancel$4$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m36lambda$listenCancel$4$comaugmaosoHome$Phone() {
            if (vo != null) {
                vo.destroyRec();
            }
        }

        public void listenCancel() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    m36lambda$listenCancel$4$comaugmaosoHome$Phone();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$voiceInfo$5$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m57lambda$voiceInfo$5$comaugmaosoHome$Phone(Consumer consumer, String str) {
            answer(consumer, str);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$voiceInfo$6$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m58lambda$voiceInfo$6$comaugmaosoHome$Phone(final Consumer consumer) {
            voice().recogInfo(new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda35
                @Override // com.jarvis.assist.aosui.PhoneAI.Done
                public final void on(String str) {
                    m57lambda$voiceInfo$5$comaugmaosoHome$Phone(consumer, str);
                }
            });
        }

        public void voiceInfo(final Consumer<String> consumer) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    m58lambda$voiceInfo$6$comaugmaosoHome$Phone(consumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$ttsVoices$7$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m53lambda$ttsVoices$7$comaugmaosoHome$Phone(Consumer consumer, String str) {
            answer(consumer, str);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$ttsVoices$8$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m54lambda$ttsVoices$8$comaugmaosoHome$Phone(final Consumer consumer) {
            voice().voices(new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda6
                @Override // com.jarvis.assist.aosui.PhoneAI.Done
                public final void on(String str) {
                    m53lambda$ttsVoices$7$comaugmaosoHome$Phone(consumer, str);
                }
            });
        }

        public void ttsVoices(final Consumer<String> consumer) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    m54lambda$ttsVoices$8$comaugmaosoHome$Phone(consumer);
                }
            });
        }

        public String gemVoices() {
            return new JSONArray((Collection) Arrays.asList(Voice.GEM_VOICES)).toString();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$voiceSettings$9$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m59lambda$voiceSettings$9$comaugmaosoHome$Phone(String str) {
            if ("download".equals(str)) {
                voice().downloadOffline(Home.this);
            } else {
                Voice.openVoiceSettings(Home.this, str);
            }
        }

        public void voiceSettings(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    m59lambda$voiceSettings$9$comaugmaosoHome$Phone(str);
                }
            });
        }

        public String apps() {
            return AppIndex.now();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$launch$10$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m33lambda$launch$10$comaugmaosoHome$Phone(String str, String str2) {
            try {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setComponent(new ComponentName(str, str2));
                intent.setFlags(270532608);
                if (aoaReveal() && !Perf.k().calm) {
                    startActivity(intent, revealFromTouch());
                } else if (!Kit.ADV || Perf.k().calm) {
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    startActivity(intent, ActivityOptions.makeScaleUpAnimation(getWindow().getDecorView(), touchX, touchY, 1, 1).toBundle());
                }
            } catch (Exception e) {
                toast("Could not open that app");
            }
        }

        public void launch(final String str, final String str2) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda42
                @Override // java.lang.Runnable
                public final void run() {
                    m33lambda$launch$10$comaugmaosoHome$Phone(str, str2);
                }
            });
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        Intent roleIntent(String str) {
            char c;
            PackageManager packageManager = getPackageManager();
            if (str == null) {
                str = "";
            }
            switch (str.hashCode()) {
                case -1367751899:
                    if (str.equals("camera")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -1221262756:
                    if (str.equals("health")) {
                        c = 17;
                        break;
                    }
                    c = 65535;
                    break;
                case -1138529534:
                    if (str.equals("calculator")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case -567451565:
                    if (str.equals("contacts")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case -196315310:
                    if (str.equals("gallery")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -178324674:
                    if (str.equals("calendar")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 114009:
                    if (str.equals("sms")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 3083120:
                    if (str.equals("dial")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3344023:
                    if (str.equals("maps")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 3649301:
                    if (str.equals("wifi")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case 94755854:
                    if (str.equals("clock")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case 96619420:
                    if (str.equals("email")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 97434231:
                    if (str.equals("files")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case 104263205:
                    if (str.equals("music")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 105008833:
                    if (str.equals("notes")) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case 106642798:
                    if (str.equals("phone")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 150940456:
                    if (str.equals("browser")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 1223440372:
                    if (str.equals("weather")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                    return new Intent("android.intent.action.DIAL");
                case 2:
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_MESSAGING");
                case 3:
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_BROWSER");
                case 4:
                    return new Intent("android.media.action.STILL_IMAGE_CAMERA");
                case 5:
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_EMAIL");
                case 6:
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_MAPS");
                case 7:
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_MUSIC");
                case '\b':
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_CALENDAR");
                case '\t':
                    return new Intent("android.intent.action.SHOW_ALARMS");
                case '\n':
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_GALLERY");
                case 11:
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_CONTACTS");
                case '\f':
                    return Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_CALCULATOR");
                case FxModel.BOOT /* 13 */:
                    return new Intent(Build.VERSION.SDK_INT >= 29 ? "android.settings.panel.action.WIFI" : "android.settings.WIFI_SETTINGS");
                case FxModel.LOCK /* 14 */:
                    if (Build.VERSION.SDK_INT >= 29) {
                        Intent makeMainSelectorActivity = Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_FILES");
                        if (makeMainSelectorActivity.resolveActivity(packageManager) != null) {
                            return makeMainSelectorActivity;
                        }
                    }
                    return new Intent("android.intent.action.VIEW").setDataAndType(Uri.parse("content://com.android.providers.downloads.documents/root/downloads"), "vnd.android.document/root");
                case 15:
                    if (Build.VERSION.SDK_INT >= 31) {
                        Intent makeMainSelectorActivity2 = Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_WEATHER");
                        if (makeMainSelectorActivity2.resolveActivity(packageManager) != null) {
                            return makeMainSelectorActivity2;
                        }
                    }
                    return new Intent("android.intent.action.WEB_SEARCH").putExtra("query", "weather");
                case 16:
                    Intent intent = new Intent("android.intent.action.CREATE_NOTE");
                    if (intent.resolveActivity(packageManager) != null) {
                        return intent;
                    }
                    String[] strArr = {"com.google.android.keep", "com.samsung.android.app.notes", "com.microsoft.office.onenote"};
                    for (int i = 0; i < 3; i++) {
                        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(strArr[i]);
                        if (launchIntentForPackage != null) {
                            return launchIntentForPackage;
                        }
                    }
                    return null;
                case FxModel.N_EV /* 17 */:
                    Intent intent2 = new Intent("android.health.connect.action.HEALTH_HOME_SETTINGS");
                    return intent2.resolveActivity(packageManager) != null ? intent2 : Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_FITNESS");
                default:
                    return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$launchRole$11$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m34lambda$launchRole$11$comaugmaosoHome$Phone(String str) {
            try {
                Intent roleIntent = roleIntent(str);
                if (roleIntent == null) {
                    return;
                }
                if (!aoaReveal() || Perf.k().calm) {
                    startActivity(roleIntent.addFlags(268435456));
                } else {
                    startActivity(roleIntent.addFlags(268435456), revealFromTouch());
                }
            } catch (Exception e) {
                toast("No app for that yet");
            }
        }

        public void launchRole(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    m34lambda$launchRole$11$comaugmaosoHome$Phone(str);
                }
            });
        }

        public String nextAlarm() {
            try {
                AlarmManager alarmManager = (AlarmManager) getSystemService(AlarmManager.class);
                AlarmManager.AlarmClockInfo nextAlarmClock = alarmManager == null ? null : alarmManager.getNextAlarmClock();
                String str = "";
                String creatorPackage = (nextAlarmClock == null || nextAlarmClock.getShowIntent() == null) ? "" : nextAlarmClock.getShowIntent().getCreatorPackage();
                JSONObject put = new JSONObject().put("at", nextAlarmClock == null ? 0L : nextAlarmClock.getTriggerTime());
                if (creatorPackage != null) {
                    str = creatorPackage;
                }
                return put.put("pkg", str).toString();
            } catch (Exception e) {
                return "{\"at\":0,\"pkg\":\"\"}";
            }
        }

        public boolean setAlarm(int i, int i2, String str) {
            try {
                Intent putExtra = new Intent("android.intent.action.SET_ALARM").putExtra("android.intent.extra.alarm.HOUR", i).putExtra("android.intent.extra.alarm.MINUTES", i2).putExtra("android.intent.extra.alarm.SKIP_UI", true);
                if (str != null && !str.isEmpty()) {
                    putExtra.putExtra("android.intent.extra.alarm.MESSAGE", str);
                }
                startActivity(putExtra.addFlags(268435456));
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public boolean setTimer(int i, String str) {
            try {
                Intent putExtra = new Intent("android.intent.action.SET_TIMER").putExtra("android.intent.extra.alarm.LENGTH", i).putExtra("android.intent.extra.alarm.SKIP_UI", true);
                if (str != null && !str.isEmpty()) {
                    putExtra.putExtra("android.intent.extra.alarm.MESSAGE", str);
                }
                startActivity(putExtra.addFlags(268435456));
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        boolean has(String str) {
            return checkSelfPermission(str) == 0;
        }

        public String permState() {
            try {
                String string = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
                AppOpsManager appOpsManager = (AppOpsManager) getSystemService(AppOpsManager.class);
                int unsafeCheckOpNoThrow = appOpsManager != null ? Build.VERSION.SDK_INT >= 29 ? appOpsManager.unsafeCheckOpNoThrow("android:get_usage_stats", Process.myUid(), getPackageName()) : appOpsManager.checkOpNoThrow("android:get_usage_stats", Process.myUid(), getPackageName()) : 3;
                JSONArray optJSONArray = new JSONObject(Health.perm(Home.this)).optJSONArray("granted");
                boolean z = true;
                JSONObject put = new JSONObject().put("calendar", has("android.permission.READ_CALENDAR")).put("contacts", has("android.permission.READ_CONTACTS")).put("location", Loc.ok(Home.this)).put("locFix", !Loc.get(Home.this).isEmpty()).put("mic", has("android.permission.RECORD_AUDIO")).put("notifAccess", string != null && string.contains(getPackageName())).put("usage", unsafeCheckOpNoThrow == 0).put("health", optJSONArray != null && optJSONArray.length() > 0).put("healthAvailable", Health.available(Home.this));
                if (optJSONArray == null || optJSONArray.length() <= 0) {
                    z = false;
                }
                return put.put("healthGate", healthGate(z)).put("home", isHome()).toString();
            } catch (Exception e) {
                return "{}";
            }
        }

        String healthGate(boolean z) {
            return OnboardModel.healthGate(Build.VERSION.SDK_INT, Health.available(Home.this), z);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isHome() {
            if (Build.VERSION.SDK_INT >= 29) {
                RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
                return roleManager != null && roleManager.isRoleHeld("android.app.role.HOME");
            }
            ResolveInfo resolveActivity = getPackageManager().resolveActivity(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 65536);
            return (resolveActivity == null || resolveActivity.activityInfo == null || !getPackageName().equals(resolveActivity.activityInfo.packageName)) ? false : true;
        }

        public void askPerm(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    m23lambda$askPerm$12$comaugmaosoHome$Phone(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* renamed from: lambda$askPerm$12$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m23lambda$askPerm$12$comaugmaosoHome$Phone(String str) {
            char c;
            if (str == null) {
                str = "";
            }
            try {
                switch (str.hashCode()) {
                    case -1221262756:
                        if (str.equals("health")) {
                            c = 6;
                            break;
                        }
                        c = 65535;
                        break;
                    case -567451565:
                        if (str.equals("contacts")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case -178324674:
                        if (str.equals("calendar")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 108103:
                        if (str.equals("mic")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case 3208415:
                        if (str.equals("home")) {
                            c = 7;
                            break;
                        }
                        c = 65535;
                        break;
                    case 111574433:
                        if (str.equals("usage")) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case 952290484:
                        if (str.equals("notifAccess")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1901043637:
                        if (str.equals("location")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        requestPermissions(new String[]{"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"}, 42);
                        return;
                    case 1:
                        requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 43);
                        return;
                    case 2:
                        requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 41);
                        return;
                    case 3:
                        requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 46);
                        return;
                    case 4:
                        orAppDetails(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), "Turn on notification access for aOS under Special app access");
                        return;
                    case 5:
                        orAppDetails(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), "Turn on usage access for aOS under Special app access");
                        return;
                    case 6:
                        String healthGate = healthGate(false);
                        if (!Health.available(Home.this) || "unsupported".equals(healthGate)) {
                            openSettings("health");
                            return;
                        } else {
                            requestPermissions(Health.PERMS, 51);
                            return;
                        }
                    case 7:
                        RoleManager roleManager = Build.VERSION.SDK_INT >= 29 ? (RoleManager) getSystemService(RoleManager.class) : null;
                        try {
                            if (Build.VERSION.SDK_INT >= 29 && roleManager != null && roleManager.isRoleAvailable("android.app.role.HOME")) {
                                startActivityForResult(roleManager.createRequestRoleIntent("android.app.role.HOME"), 60);
                                return;
                            }
                        } catch (ActivityNotFoundException e) {
                        } catch (SecurityException e2) {
                        }
                        try {
                            startActivity(new Intent("android.settings.HOME_SETTINGS").addFlags(268435456));
                            return;
                        } catch (ActivityNotFoundException | SecurityException e3) {
                            safeStart(new Intent("android.settings.SETTINGS").addFlags(268435456));
                            toast("Pick aOS under Apps › Default apps › Home app");
                            return;
                        }
                    default:
                        return;
                }
            } catch (Exception e4) {
                event("onPerm");
            }
        }

        public void expandNotifs() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    m25lambda$expandNotifs$13$comaugmaosoHome$Phone();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$expandNotifs$13$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m25lambda$expandNotifs$13$comaugmaosoHome$Phone() {
            try {
                Object systemService = getSystemService("statusbar");
                systemService.getClass().getMethod("expandNotificationsPanel", new Class[0]).invoke(systemService, new Object[0]);
            } catch (Exception e) {
            }
        }

        public String roleApp(String str) {
            ResolveInfo resolveActivity;
            try {
                Intent roleIntent = roleIntent(str);
                if (roleIntent != null && (resolveActivity = getPackageManager().resolveActivity(roleIntent, 65536)) != null && resolveActivity.activityInfo != null) {
                    return resolveActivity.activityInfo.packageName;
                }
                return "";
            } catch (Exception e) {
                return "";
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$webSearch$14$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m61lambda$webSearch$14$comaugmaosoHome$Phone(String str) {
            try {
                startActivity(new Intent("android.intent.action.WEB_SEARCH").putExtra("query", str).addFlags(268435456));
            } catch (Exception e) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://duckduckgo.com/?q=" + Uri.encode(str))).addFlags(268435456));
                } catch (Exception e2) {
                    toast("Could not open that");
                }
            }
        }

        public void webSearch(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    m61lambda$webSearch$14$comaugmaosoHome$Phone(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$openAsk$15$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m43lambda$openAsk$15$comaugmaosoHome$Phone(String str) {
            try {
                Home home = Home.this;
                Intent className = new Intent().setClassName(getPackageName(), getPackageName() + ".Apps$AppAssist");
                if (str == null) {
                    str = "";
                }
                home.startActivity(className.putExtra("ask", str).putExtra("tok", Home.TOK).addFlags(872415232));
            } catch (Exception e) {
                toast("Could not open that");
            }
        }

        public void openAsk(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    m43lambda$openAsk$15$comaugmaosoHome$Phone(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$openTalk$16$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m47lambda$openTalk$16$comaugmaosoHome$Phone() {
            try {
                startActivity(new Intent().setClassName(getPackageName(), getPackageName() + ".Apps$AppAssist").putExtra("voice", true).addFlags(872415232));
            } catch (Exception e) {
                toast("Could not open that");
            }
        }

        public void openTalk() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    m47lambda$openTalk$16$comaugmaosoHome$Phone();
                }
            });
        }

        public void info(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    m32lambda$info$17$comaugmaosoHome$Phone(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$info$17$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m32lambda$info$17$comaugmaosoHome$Phone(String str) {
            safeStart(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + str)).addFlags(268435456));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$uninstall$18$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m55lambda$uninstall$18$comaugmaosoHome$Phone(String str) {
            safeStart(new Intent("android.intent.action.DELETE", Uri.parse("package:" + str)).addFlags(268435456));
        }

        public void uninstall(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    m55lambda$uninstall$18$comaugmaosoHome$Phone(str);
                }
            });
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public void settings(String str) {
            char c;
            if (str == null) {
                str = "";
            }
            switch (str.hashCode()) {
                case -1884274053:
                    if (str.equals("storage")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -677011630:
                    if (str.equals("airplane")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case -331239923:
                    if (str.equals("battery")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 99610:
                    if (str.equals("dnd")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 108971:
                    if (str.equals("nfc")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case 3075958:
                    if (str.equals("dark")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case 3076014:
                    if (str.equals("date")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case 3208415:
                    if (str.equals("home")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3649301:
                    if (str.equals("wifi")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 109211285:
                    if (str.equals("saver")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case 109627663:
                    if (str.equals("sound")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 570410817:
                    if (str.equals("internet")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case 1099603663:
                    if (str.equals("hotspot")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case 1671764162:
                    if (str.equals("display")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 1901043637:
                    if (str.equals("location")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 1968882350:
                    if (str.equals("bluetooth")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            String str2 = "android.settings.DISPLAY_SETTINGS";
            switch (c) {
                case 0:
                    str2 = "android.settings.HOME_SETTINGS";
                    break;
                case 1:
                    str2 = Build.VERSION.SDK_INT >= 29 ? "android.settings.panel.action.WIFI" : "android.settings.WIFI_SETTINGS";
                    break;
                case 2:
                    str2 = "android.settings.BLUETOOTH_SETTINGS";
                    break;
                case 3:
                    str2 = "android.intent.action.POWER_USAGE_SUMMARY";
                    break;
                case 4:
                    str2 = "android.settings.INTERNAL_STORAGE_SETTINGS";
                    break;
                case 5:
                    str2 = "android.settings.ZEN_MODE_PRIORITY_SETTINGS";
                    break;
                case 6:
                    str2 = "android.settings.SOUND_SETTINGS";
                    break;
                case 7:
                    str2 = "android.settings.LOCATION_SOURCE_SETTINGS";
                    break;
                case '\b':
                case 15:
                    break;
                case '\t':
                    str2 = "android.settings.DATE_SETTINGS";
                    break;
                case '\n':
                    str2 = Build.VERSION.SDK_INT >= 29 ? "android.settings.panel.action.INTERNET_CONNECTIVITY" : "android.settings.WIRELESS_SETTINGS";
                    break;
                case 11:
                    str2 = "android.settings.TETHER_SETTINGS";
                    break;
                case '\f':
                    str2 = "android.settings.AIRPLANE_MODE_SETTINGS";
                    break;
                case FxModel.BOOT /* 13 */:
                    str2 = "android.settings.BATTERY_SAVER_SETTINGS";
                    break;
                case FxModel.LOCK /* 14 */:
                    str2 = Build.VERSION.SDK_INT >= 29 ? "android.settings.panel.action.NFC" : "android.settings.NFC_SETTINGS";
                    break;
                default:
                    str2 = "android.settings.SETTINGS";
                    break;
            }
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    m49lambda$settings$19$comaugmaosoHome$Phone(str2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$settings$19$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m49lambda$settings$19$comaugmaosoHome$Phone(String str) {
            try {
                startActivity(new Intent(str).addFlags(268435456));
            } catch (Exception e) {
                safeStart(new Intent("android.settings.SETTINGS").addFlags(268435456));
            }
        }

        public boolean torch() {
            try {
                CameraManager cameraManager = (CameraManager) getSystemService("camera");
                for (String str : cameraManager.getCameraIdList()) {
                    Boolean bool = (Boolean) cameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    if (bool != null && bool.booleanValue()) {
                        this.torchOn = this.torchOn ? false : true;
                        cameraManager.setTorchMode(str, this.torchOn);
                        return this.torchOn;
                    }
                }
            } catch (Exception e) {
            }
            return this.torchOn;
        }

        public boolean torchSet(boolean z) {
            try {
                CameraManager cameraManager = (CameraManager) getSystemService("camera");
                for (String str : cameraManager.getCameraIdList()) {
                    Boolean bool = (Boolean) cameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    if (bool != null && bool.booleanValue()) {
                        cameraManager.setTorchMode(str, z);
                        this.torchOn = z;
                        return true;
                    }
                }
            } catch (Exception e) {
            }
            return false;
        }

        public String shareTake() {
            String str = shared;
            shared = null;
            return str == null ? "" : str;
        }

        public String clipRead() {
            try {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(ClipboardManager.class);
                if (clipboardManager != null && clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClip() != null && clipboardManager.getPrimaryClip().getItemCount() != 0) {
                    CharSequence coerceToText = clipboardManager.getPrimaryClip().getItemAt(0).coerceToText(Home.this);
                    String trim = coerceToText == null ? "" : coerceToText.toString().trim();
                    if (trim.matches("(AIza|sk-ant-|sk-or-|gsk_|sk-)[0-9A-Za-z_\\-]{20,}")) {
                        return "";
                    }
                    return trim.length() > 4000 ? trim.substring(0, 4000) : trim;
                }
                return "";
            } catch (Exception e) {
                return "";
            }
        }

        public boolean notifReply(String str, String str2) {
            return Notifs.reply(getApplicationContext(), str, str2);
        }

        public void url(String str) {
            if (str == null) {
                str = "";
            }
            final Uri parse = Uri.parse(str);
            if (String.valueOf(parse.getScheme()).matches("https?|mailto|tel|geo|sms|smsto")) {
                runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda25
                    @Override // java.lang.Runnable
                    public final void run() {
                        m56lambda$url$20$comaugmaosoHome$Phone(parse);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$url$20$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m56lambda$url$20$comaugmaosoHome$Phone(Uri uri) {
            try {
                startActivity(new Intent("android.intent.action.VIEW", uri).addFlags(268435456));
            } catch (Exception e) {
                toast("Could not open that");
            }
        }

        public void askCal() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    m22lambda$askCal$21$comaugmaosoHome$Phone();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$askCal$21$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m22lambda$askCal$21$comaugmaosoHome$Phone() {
            requestPermissions(new String[]{"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"}, 42);
        }

        public String net() {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return networkCapabilities == null ? "OFF" : networkCapabilities.hasTransport(1) ? "WIFI" : networkCapabilities.hasTransport(0) ? "CELL" : "NET";
            } catch (Exception e) {
                return "—";
            }
        }

        public String storage() {
            try {
                StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
                return "{\"free\":" + statFs.getAvailableBytes() + ",\"total\":" + statFs.getTotalBytes() + "}";
            } catch (Exception e) {
                return "null";
            }
        }

        public String battery() {
            Intent registerReceiver = registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (registerReceiver == null) {
                return "{}";
            }
            int intExtra = registerReceiver.getIntExtra("level", -1);
            int intExtra2 = registerReceiver.getIntExtra("scale", 100);
            int intExtra3 = registerReceiver.getIntExtra("status", -1);
            boolean z = true;
            int max = (intExtra * 100) / Math.max(1, intExtra2);
            if (intExtra3 != 2 && intExtra3 != 5) {
                z = false;
            }
            return "{\"pct\":" + max + ",\"charging\":" + z + "}";
        }

        public String get(String str) {
            return prefs.getString(str, "");
        }

        public void set(String str, String str2) {
            prefs.edit().putString(str, str2).apply();
            if ("aos.perf".equals(str)) {
                applyPerf();
            }
        }

        public String clip(String str) {
            try {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(ClipboardManager.class);
                if (clipboardManager != null && clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClip() != null && clipboardManager.getPrimaryClip().getItemCount() != 0) {
                    CharSequence coerceToText = clipboardManager.getPrimaryClip().getItemAt(0).coerceToText(Home.this);
                    String trim = coerceToText == null ? "" : coerceToText.toString().trim();
                    if (!trim.isEmpty() && trim.length() <= 600 && !trim.matches("(?s).*\\s.*") && !trim.contains("://")) {
                        return "ai".equals(str) ? Keys.plausible(trim) ? trim : "" : trim.length() >= 16 ? trim : "";
                    }
                    return "";
                }
                return "";
            } catch (Exception e) {
                return "";
            }
        }

        public void copy(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    m24lambda$copy$22$comaugmaosoHome$Phone(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$copy$22$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m24lambda$copy$22$comaugmaosoHome$Phone(String str) {
            try {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(ClipboardManager.class);
                if (clipboardManager == null || str == null) {
                    return;
                }
                clipboardManager.setPrimaryClip(ClipData.newPlainText("aOS", str));
            } catch (Exception e) {
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$openSettings$23$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m46lambda$openSettings$23$comaugmaosoHome$Phone(String str) {
            try {
                Home home = Home.this;
                Intent intent = new Intent(Home.this, (Class<?>) Apps.AppSettings.class);
                if (str == null) {
                    str = "";
                }
                home.startActivity(intent.putExtra("s", str).addFlags(603979776));
                slideIn();
            } catch (Exception e) {
                toast("Could not open that");
            }
        }

        public void openSettings(final String str) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    m46lambda$openSettings$23$comaugmaosoHome$Phone(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$openApp$24$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m41lambda$openApp$24$comaugmaosoHome$Phone() {
            GetVault.open(Home.this);
        }

        public void openApp(final String str) {
            if (VaultLink.route(str, false) != VaultLink.Route.NONE) {
                runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        m41lambda$openApp$24$comaugmaosoHome$Phone();
                    }
                });
            } else {
                final boolean equals = "Settings".equals(str);
                runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        m42lambda$openApp$25$comaugmaosoHome$Phone(str, equals);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$openApp$25$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m42lambda$openApp$25$comaugmaosoHome$Phone(String str, boolean z) {
            try {
                startActivity(new Intent().setClassName(getPackageName(), getPackageName() + ".Apps$App" + str).addFlags((z ? 0 : 268500992) | 67108864 | 536870912));
                if (z) {
                    slideIn();
                }
            } catch (Exception e) {
                toast("Could not open that");
            }
        }

        public void notifOpen(String str) {
            Notifs.openIt(str);
        }

        public boolean contactsOk() {
            return checkSelfPermission("android.permission.READ_CONTACTS") == 0;
        }

        public String contacts(String str) {
            JSONArray jSONArray = new JSONArray();
            if (!contactsOk()) {
                return jSONArray.toString();
            }
            String trim = str == null ? "" : str.trim();
            if (trim.length() < 2) {
                return jSONArray.toString();
            }
            try {
                Cursor query = getContentResolver().query(Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(trim)), new String[]{"display_name", "data1", "starred", "photo_thumb_uri"}, null, null, "starred DESC, display_name ASC");
                try {
                    HashSet hashSet = new HashSet();
                    while (query != null) {
                        if (!query.moveToNext() || jSONArray.length() >= 30) {
                            break;
                        }
                        String string = query.getString(0);
                        String string2 = query.getString(1);
                        if (string2 != null) {
                            if (hashSet.add(string + string2.replaceAll("\\s", ""))) {
                                jSONArray.put(new JSONObject().put("name", string).put("num", string2).put("star", query.getInt(2) == 1).put("photo", photo(query.getString(3))));
                            }
                        }
                    }
                    if (query != null) {
                        query.close();
                    }
                } finally {
                }
            } catch (Exception e) {
            }
            return jSONArray.toString();
        }

        String photo(String str) {
            if (str == null) {
                return "";
            }
            try {
                InputStream openInputStream = getContentResolver().openInputStream(Uri.parse(str));
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int read = openInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    String str2 = "data:image/jpeg;base64," + Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2);
                    if (openInputStream != null) {
                        openInputStream.close();
                    }
                    return str2;
                } finally {
                }
            } catch (Exception e) {
                return "";
            }
        }

        public String media() {
            return Media.state(Home.this);
        }

        public boolean mediaAct(String str, String str2) {
            return Media.act(Home.this, str, str2);
        }

        public void hapticFx(String str) {
            Feedback.legacy(getWindow() == null ? null : getWindow().getDecorView(), str);
        }

        void healthSettings() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    m30lambda$healthSettings$26$comaugmaosoHome$Phone();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$healthSettings$26$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m30lambda$healthSettings$26$comaugmaosoHome$Phone() {
            try {
                startActivity(new Intent("android.health.connect.action.MANAGE_HEALTH_PERMISSIONS").putExtra("android.intent.extra.PACKAGE_NAME", getPackageName()).addFlags(268435456));
            } catch (Exception e) {
                try {
                    startActivity(new Intent("android.health.connect.action.HEALTH_HOME_SETTINGS").addFlags(268435456));
                } catch (Exception e2) {
                    toast("Could not open that");
                }
            }
        }

        public String healthSources() {
            return HealthSources.status(Home.this);
        }

        public void healthConnect(final String str, final String str2, final Consumer<String> consumer) {
            final Context applicationContext = getApplicationContext();
            Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    m28lambda$healthConnect$28$comaugmaosoHome$Phone(applicationContext, str, str2, consumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$healthConnect$28$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m28lambda$healthConnect$28$comaugmaosoHome$Phone(Context context, String str, String str2, Consumer consumer) {
            String connect = HealthSources.connect(context, str, str2);
            try {
                final String optString = new JSONObject(connect).optString("auth", "");
                if (!optString.isEmpty()) {
                    runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda43
                        @Override // java.lang.Runnable
                        public final void run() {
                            m27lambda$healthConnect$27$comaugmaosoHome$Phone(optString);
                        }
                    });
                }
            } catch (Exception e) {
                toast("Could not open that");
            }
            answer(consumer, connect);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$healthConnect$27$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m27lambda$healthConnect$27$comaugmaosoHome$Phone(String str) {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)).addCategory("android.intent.category.BROWSABLE").addFlags(268435456));
            } catch (Exception e) {
                toast("Could not open that");
            }
        }

        public void healthDisconnect(String str) {
            if ("hc".equals(str)) {
                healthSettings();
            } else {
                HealthSources.wipe(getApplicationContext(), str);
            }
        }

        public void healthImportApple(final Consumer<String> consumer) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    m29lambda$healthImportApple$29$comaugmaosoHome$Phone(consumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$healthImportApple$29$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m29lambda$healthImportApple$29$comaugmaosoHome$Phone(Consumer consumer) {
            appleCb = consumer;
            try {
                startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT").addCategory("android.intent.category.OPENABLE").setType("*/*").putExtra("android.intent.extra.MIME_TYPES", new String[]{"application/zip", "application/x-zip-compressed", "text/xml", "application/xml"}), 7401);
            } catch (Exception e) {
                appleCb = null;
                answer(consumer, "{\"ok\":false,\"error\":\"NO FILE PICKER\"}");
            }
        }

        public void healthToday2(final boolean z, final Consumer<String> consumer) {
            final Context applicationContext = getApplicationContext();
            Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    m31lambda$healthToday2$30$comaugmaosoHome$Phone(applicationContext, z, consumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$healthToday2$30$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m31lambda$healthToday2$30$comaugmaosoHome$Phone(Context context, boolean z, Consumer consumer) {
            answer(consumer, HealthSources.today(context, z));
        }

        public void healthSetPref(String str) {
            HealthSources.setPref(getApplicationContext(), str);
        }

        public String appVersion() {
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                return new JSONObject().put("name", packageInfo.versionName).put("code", Build.VERSION.SDK_INT >= 28 ? packageInfo.getLongVersionCode() : packageInfo.versionCode).put("installed", packageInfo.firstInstallTime).put("updated", packageInfo.lastUpdateTime).toString();
            } catch (Exception e) {
                return "{}";
            }
        }

        public void openLockWallpaper() {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    m45lambda$openLockWallpaper$31$comaugmaosoHome$Phone();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$openLockWallpaper$31$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m45lambda$openLockWallpaper$31$comaugmaosoHome$Phone() {
            try {
                startActivity(new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER").putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(getPackageName(), getPackageName() + ".LockWallpaper")).addFlags(268435456));
            } catch (Exception e) {
                try {
                    startActivity(new Intent("android.service.wallpaper.LIVE_WALLPAPER_CHOOSER").addFlags(268435456));
                } catch (Exception e2) {
                    toast("Could not open that");
                }
            }
        }

        public String aiHasKey() {
            return PhoneAI.status(Home.this);
        }

        public void aiSetKey(String str, final Consumer<String> consumer) {
            PhoneAI.setKey(Home.this, str, new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda19
                @Override // com.jarvis.assist.aosui.PhoneAI.Done
                public final void on(String str2) {
                    m21lambda$aiSetKey$32$comaugmaosoHome$Phone(consumer, str2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$aiSetKey$32$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m21lambda$aiSetKey$32$comaugmaosoHome$Phone(Consumer consumer, String str) {
            answer(consumer, str);
        }

        public void aiClearKey() {
            PhoneAI.clear(Home.this);
        }

        public void geminiSetKey(String str, final Consumer<String> consumer) {
            PhoneAI.geminiSetKey(Home.this, str, new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda29
                @Override // com.jarvis.assist.aosui.PhoneAI.Done
                public final void on(String str2) {
                    m26lambda$geminiSetKey$33$comaugmaosoHome$Phone(consumer, str2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$geminiSetKey$33$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m26lambda$geminiSetKey$33$comaugmaosoHome$Phone(Consumer consumer, String str) {
            answer(consumer, str);
        }

        public void geminiClearKey() {
            PhoneAI.geminiClear(Home.this);
        }

        public void aiSetOrder(String str) {
            PhoneAI.setOrder(Home.this, str);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$oaiSet$34$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m40lambda$oaiSet$34$comaugmaosoHome$Phone(Consumer consumer, String str) {
            answer(consumer, str);
        }

        public void oaiSet(String str, final Consumer<String> consumer) {
            PhoneAI.oaiSet(Home.this, str, new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda17
                @Override // com.jarvis.assist.aosui.PhoneAI.Done
                public final void on(String str2) {
                    m40lambda$oaiSet$34$comaugmaosoHome$Phone(consumer, str2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$oaiModels$35$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m39lambda$oaiModels$35$comaugmaosoHome$Phone(Consumer consumer, String str) {
            answer(consumer, str);
        }

        public void oaiModels(String str, final Consumer<String> consumer) {
            PhoneAI.oaiModels(Home.this, str, new PhoneAI.Done() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda41
                @Override // com.jarvis.assist.aosui.PhoneAI.Done
                public final void on(String str2) {
                    m39lambda$oaiModels$35$comaugmaosoHome$Phone(consumer, str2);
                }
            });
        }

        public void oaiClear() {
            PhoneAI.oaiClear(Home.this);
        }

        public String weatherStatus() {
            return Weather.status(getApplicationContext());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$weatherSetKey$36$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m60lambda$weatherSetKey$36$comaugmaosoHome$Phone(Consumer consumer, String str) {
            answer(consumer, str);
        }

        public void weatherSetKey(String str, String str2, final Consumer<String> consumer) {
            Weather.setKey(getApplicationContext(), str, str2, new Weather.Done() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda32
                @Override // com.jarvis.assist.aosui.Weather.Done
                public final void on(String str3) {
                    m60lambda$weatherSetKey$36$comaugmaosoHome$Phone(consumer, str3);
                }
            });
        }

        public void weatherClearKey(String str) {
            Weather.clearKey(getApplicationContext(), str);
        }

        boolean adminOn() {
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService("device_policy");
            return devicePolicyManager != null && devicePolicyManager.isAdminActive(new ComponentName(Home.this, (Class<?>) LockAdmin.class));
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x003d  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0050  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0051  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.String lockState() {
            /*
                r12 = this;
                java.lang.String r0 = "service"
                java.lang.String r1 = ""
                java.lang.String r2 = "admin"
                boolean r3 = com.jarvis.assist.aosui.LockService.ready()     // Catch: org.json.JSONException -> L5e
                boolean r4 = r12.adminOn()     // Catch: org.json.JSONException -> L5e
                com.jarvis.assist.aosui.Home r5 = com.jarvis.assist.aosui.Home.this     // Catch: org.json.JSONException -> L5e
                android.content.SharedPreferences r5 = r5.prefs     // Catch: org.json.JSONException -> L5e
                java.lang.String r6 = "aos.lockvia"
                java.lang.String r5 = r5.getString(r6, r1)     // Catch: org.json.JSONException -> L5e
                org.json.JSONObject r6 = new org.json.JSONObject     // Catch: org.json.JSONException -> L5e
                r6.<init>()     // Catch: org.json.JSONException -> L5e
                java.lang.String r7 = "ready"
                r8 = 0
                r9 = 1
                if (r3 != 0) goto L28
                if (r4 == 0) goto L26
                goto L28
            L26:
                r10 = r8
                goto L29
            L28:
                r10 = r9
            L29:
                org.json.JSONObject r6 = r6.put(r7, r10)     // Catch: org.json.JSONException -> L5e
                org.json.JSONObject r6 = r6.put(r0, r3)     // Catch: org.json.JSONException -> L5e
                org.json.JSONObject r6 = r6.put(r2, r4)     // Catch: org.json.JSONException -> L5e
                java.lang.String r7 = "canService"
                int r10 = android.os.Build.VERSION.SDK_INT     // Catch: org.json.JSONException -> L5e
                r11 = 28
                if (r10 < r11) goto L3e
                r8 = r9
            L3e:
                org.json.JSONObject r6 = r6.put(r7, r8)     // Catch: org.json.JSONException -> L5e
                java.lang.String r7 = "via"
                boolean r5 = r2.equals(r5)     // Catch: org.json.JSONException -> L5e
                if (r5 == 0) goto L4e
                if (r4 == 0) goto L4e
            L4c:
                r0 = r2
                goto L55
            L4e:
                if (r3 == 0) goto L51
                goto L55
            L51:
                if (r4 == 0) goto L54
                goto L4c
            L54:
                r0 = r1
            L55:
                org.json.JSONObject r0 = r6.put(r7, r0)     // Catch: org.json.JSONException -> L5e
                java.lang.String r0 = r0.toString()     // Catch: org.json.JSONException -> L5e
                return r0
            L5e:
                r0 = move-exception
                java.lang.String r0 = "{}"
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jarvis.assist.aosui.Home.Phone.lockState():java.lang.String");
        }

        public boolean lockScreen() {
            boolean adminOn = adminOn();
            if (!("admin".equals(prefs.getString("aos.lockvia", "")) && adminOn) && LockService.lock()) {
                return true;
            }
            if (!adminOn) {
                return false;
            }
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    m38lambda$lockScreen$37$comaugmaosoHome$Phone();
                }
            });
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$lockScreen$37$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m38lambda$lockScreen$37$comaugmaosoHome$Phone() {
            try {
                ((DevicePolicyManager) getSystemService("device_policy")).lockNow();
            } catch (Exception e) {
            }
        }

        public boolean powerSave() {
            return saving();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$settingsExport$38$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m50lambda$settingsExport$38$comaugmaosoHome$Phone(String str, Consumer consumer) {
            expJson = str;
            expCb = consumer;
            try {
                startActivityForResult(new Intent("android.intent.action.CREATE_DOCUMENT").addCategory("android.intent.category.OPENABLE").setType("application/json").putExtra("android.intent.extra.TITLE", "aos-settings-" + new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date()) + ".json"), 7501);
            } catch (Exception e) {
                expJson = null;
                expCb = null;
                answer(consumer, "{\"ok\":false,\"error\":\"NO FILE PICKER\"}");
            }
        }

        public void settingsExport(final String str, final Consumer<String> consumer) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    m50lambda$settingsExport$38$comaugmaosoHome$Phone(str, consumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$settingsImport$39$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m51lambda$settingsImport$39$comaugmaosoHome$Phone(Consumer consumer) {
            impCb = consumer;
            try {
                startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT").addCategory("android.intent.category.OPENABLE").setType("*/*").putExtra("android.intent.extra.MIME_TYPES", new String[]{"application/json", "text/plain", "application/octet-stream"}), 7502);
            } catch (Exception e) {
                impCb = null;
                answer(consumer, "{\"ok\":false,\"error\":\"NO FILE PICKER\"}");
            }
        }

        public void settingsImport(final Consumer<String> consumer) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    m51lambda$settingsImport$39$comaugmaosoHome$Phone(consumer);
                }
            });
        }

        public String starred() {
            JSONArray jSONArray = new JSONArray();
            if (!contactsOk()) {
                return "{\"ok\":false,\"why\":\"permission\"}";
            }
            try {
                Cursor query = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"contact_id", "display_name", "data1", "photo_thumb_uri", "is_super_primary"}, "starred=1", null, "display_name ASC, is_super_primary DESC");
                try {
                    HashSet hashSet = new HashSet();
                    while (query != null && query.moveToNext() && jSONArray.length() < 8) {
                        if (query.getString(2) != null && hashSet.add(Long.valueOf(query.getLong(0)))) {
                            jSONArray.put(new JSONObject().put("name", query.getString(1)).put("num", query.getString(2)).put("photo", photo(query.getString(3))));
                        }
                    }
                    String jSONObject = new JSONObject().put("ok", true).put("people", jSONArray).toString();
                    if (query != null) {
                        query.close();
                    }
                    return jSONObject;
                } finally {
                }
            } catch (Exception e) {
                return "{\"ok\":false}";
            }
        }

        public String screenTime() {
            int unsafeCheckOpNoThrow;
            try {
                AppOpsManager appOpsManager = (AppOpsManager) getSystemService(AppOpsManager.class);
                if (appOpsManager == null) {
                    unsafeCheckOpNoThrow = 3;
                } else {
                    unsafeCheckOpNoThrow = Build.VERSION.SDK_INT >= 29 ? appOpsManager.unsafeCheckOpNoThrow("android:get_usage_stats", Process.myUid(), getPackageName()) : appOpsManager.checkOpNoThrow("android:get_usage_stats", Process.myUid(), getPackageName());
                }
                if (unsafeCheckOpNoThrow != 0) {
                    return "{\"ok\":false,\"why\":\"usage\"}";
                }
                UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(UsageStatsManager.class);
                if (usageStatsManager == null) {
                    return "{\"ok\":false}";
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                Map<String, UsageStats> queryAndAggregateUsageStats = usageStatsManager.queryAndAggregateUsageStats(calendar.getTimeInMillis(), System.currentTimeMillis());
                ArrayList arrayList = new ArrayList();
                long j = 0;
                for (UsageStats usageStats : queryAndAggregateUsageStats.values()) {
                    if (usageStats.getTotalTimeInForeground() >= 60000 && !usageStats.getPackageName().equals(getPackageName()) && usageStats.getLastTimeUsed() >= calendar.getTimeInMillis()) {
                        arrayList.add(usageStats);
                        j += usageStats.getTotalTimeInForeground();
                    }
                }
                arrayList.sort(new Comparator() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda23
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((UsageStats) obj2).getTotalTimeInForeground(), ((UsageStats) obj).getTotalTimeInForeground());
                        return compare;
                    }
                });
                JSONArray jSONArray = new JSONArray();
                PackageManager packageManager = getPackageManager();
                for (int i = 0; i < Math.min(3, arrayList.size()); i++) {
                    String packageName = ((UsageStats) arrayList.get(i)).getPackageName();
                    jSONArray.put(new JSONObject().put("pkg", packageName).put("label", appLabel(packageManager, packageName)).put("ms", ((UsageStats) arrayList.get(i)).getTotalTimeInForeground()));
                }
                return new JSONObject().put("ok", true).put("total", j).put("top", jSONArray).toString();
            } catch (Throwable th) {
                return "{\"ok\":false}";
            }
        }

        String appLabel(PackageManager packageManager, String str) {
            String str2;
            try {
                str2 = String.valueOf(packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 0)));
            } catch (Exception e) {
                str2 = "";
            }
            if (str2.isEmpty() || str2.equals(str)) {
                try {
                    List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER").setPackage(str), 0);
                    if (!queryIntentActivities.isEmpty()) {
                        str2 = String.valueOf(queryIntentActivities.get(0).loadLabel(packageManager));
                    }
                } catch (Exception e2) {
                }
            }
            return HomeRules.appLabel(str, str2);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$widgetProviders$41$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m63lambda$widgetProviders$41$comaugmaosoHome$Phone(Consumer consumer) {
            Drawable drawable;
            JSONArray jSONArray = new JSONArray();
            try {
                PackageManager packageManager = getPackageManager();
                float f = getResources().getDisplayMetrics().density;
                for (AppWidgetProviderInfo appWidgetProviderInfo : awm().getInstalledProviders()) {
                    try {
                        drawable = appWidgetProviderInfo.loadPreviewImage(Home.this, getResources().getDisplayMetrics().densityDpi);
                    } catch (Throwable th) {
                        drawable = null;
                    }
                    if (drawable == null) {
                        try {
                            drawable = appWidgetProviderInfo.loadIcon(Home.this, getResources().getDisplayMetrics().densityDpi);
                        } catch (Throwable th2) {
                        }
                    }
                    String str = "";
                    try {
                        try {
                            str = packageManager.getApplicationLabel(packageManager.getApplicationInfo(appWidgetProviderInfo.provider.getPackageName(), 0)).toString();
                        } catch (Throwable th3) {
                        }
                    } catch (Exception e) {
                    }
                    jSONArray.put(new JSONObject().put("cn", appWidgetProviderInfo.provider.flattenToShortString()).put("label", appWidgetProviderInfo.loadLabel(packageManager)).put("app", str).put("w", Math.round(appWidgetProviderInfo.minWidth / f)).put("h", Math.round(appWidgetProviderInfo.minHeight / f)).put("img", thumb(drawable, 160)));
                }
            } catch (Throwable th4) {
            }
            answer(consumer, jSONArray.toString());
        }

        public void widgetProviders(final Consumer<String> consumer) {
            Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    m63lambda$widgetProviders$41$comaugmaosoHome$Phone(consumer);
                }
            });
        }

        String thumb(Drawable drawable, int i) {
            if (drawable == null) {
                return "";
            }
            try {
                int max = Math.max(1, drawable.getIntrinsicWidth());
                int max2 = Math.max(1, drawable.getIntrinsicHeight());
                float f = 1.0f;
                float min = Math.min(1.0f, i / Math.max(max, max2));
                if (max > 1 && max2 > 1) {
                    i = max;
                    f = min;
                    Bitmap createBitmap = Bitmap.createBitmap(Math.max(1, Math.round(i * f)), Math.max(1, Math.round(max2 * f)), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    drawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                    drawable.draw(canvas);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    createBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                    createBitmap.recycle();
                    return "data:image/png;base64," + Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2);
                }
                max2 = i;
                Bitmap createBitmap2 = Bitmap.createBitmap(Math.max(1, Math.round(i * f)), Math.max(1, Math.round(max2 * f)), Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(createBitmap2);
                drawable.setBounds(0, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                drawable.draw(canvas2);
                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                createBitmap2.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream2);
                createBitmap2.recycle();
                return "data:image/png;base64," + Base64.encodeToString(byteArrayOutputStream2.toByteArray(), 2);
            } catch (Throwable th) {
                return "";
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$widgetAdd$42$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m62lambda$widgetAdd$42$comaugmaosoHome$Phone(String str, Consumer consumer) {
            int i;
            if (str == null) {
                str = "";
            }
            try {
                ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
                AppWidgetHost host = host();
                if (unflattenFromString != null && host != null && awm() != null) {
                    if (awPending != 0) {
                        try {
                            host.deleteAppWidgetId(awPending);
                        } catch (Throwable th) {
                        }
                    }
                    i = host.allocateAppWidgetId();
                    try {
                        awPending = i;
                        awCb = consumer;
                        if (awm().bindAppWidgetIdIfAllowed(i, unflattenFromString)) {
                            awConfigure(i);
                        } else {
                            startActivityForResult(new Intent("android.appwidget.action.APPWIDGET_BIND").putExtra("appWidgetId", i).putExtra("appWidgetProvider", unflattenFromString), 7601);
                        }
                        return;
                    } catch (Throwable th2) {
                        Home home = Home.this;
                        if (i != 0) {
                            home.awDone(i, false);
                            return;
                        } else {
                            home.answer(consumer, "{\"ok\":false}");
                            return;
                        }
                    }
                }
                answer(consumer, "{\"ok\":false}");
            } catch (Throwable th3) {
                i = 0;
            }
        }

        public void widgetAdd(final String str, final Consumer<String> consumer) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    m62lambda$widgetAdd$42$comaugmaosoHome$Phone(str, consumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$widgetRemove$43$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m64lambda$widgetRemove$43$comaugmaosoHome$Phone(int i) {
            try {
                host().deleteAppWidgetId(i);
            } catch (Throwable th) {
            }
            AppWidgetHostView remove = awv.remove(Integer.valueOf(i));
            if (remove != null && (remove.getParent() instanceof ViewGroup)) {
                ((ViewGroup) remove.getParent()).removeView(remove);
            }
            JSONArray awList = awList();
            JSONArray jSONArray = new JSONArray();
            for (int i2 = 0; i2 < awList.length(); i2++) {
                if (awList.optJSONObject(i2) != null && awList.optJSONObject(i2).optInt("id") != i) {
                    jSONArray.put(awList.optJSONObject(i2));
                }
            }
            prefs.edit().putString("aw.list", jSONArray.toString()).apply();
        }

        public void widgetRemove(final int i) {
            runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    m64lambda$widgetRemove$43$comaugmaosoHome$Phone(i);
                }
            });
        }

        public String appWidgets() {
            JSONArray awList = awList();
            JSONArray jSONArray = new JSONArray();
            AppWidgetManager awm = awm();
            for (int i = 0; i < awList.length(); i++) {
                JSONObject optJSONObject = awList.optJSONObject(i);
                if (optJSONObject != null && awm != null) {
                    try {
                        if (awm.getAppWidgetInfo(optJSONObject.optInt("id")) != null) {
                            jSONArray.put(optJSONObject);
                        }
                    } catch (Throwable th) {
                    }
                }
            }
            return jSONArray.toString();
        }

        public String assistantInfo() {
            return PhoneAssist.info(Home.this);
        }

        public boolean openAssistant(String str, boolean z) {
            boolean open = PhoneAssist.open(Home.this, str, z);
            if (!open) {
                runOnUiThread(new Runnable() { // from class: com.jarvis.assist.aosui.Home$Phone$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        m44lambda$openAssistant$44$comaugmaosoHome$Phone();
                    }
                });
            }
            return open;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$openAssistant$44$com-augma-oso-Home$Phone, reason: not valid java name */
        public /* synthetic */ void m44lambda$openAssistant$44$comaugmaosoHome$Phone() {
            try {
                Toast.makeText(Home.this, "No assistant app on this phone can take that", 0).show();
            } catch (Exception e) {
            }
        }
    }
}


