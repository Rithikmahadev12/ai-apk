package com.jarvis.assist.aosui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.jarvis.assist.aosui.core.Fonts;
import com.jarvis.assist.aosui.core.Perf;
import com.jarvis.assist.aosui.core.Store;
import com.jarvis.assist.aosui.core.Theme;
import com.jarvis.assist.aosui.home.HomeCtx;
import com.jarvis.assist.aosui.home.HomeData;
import com.jarvis.assist.aosui.home.HomeView;

import java.util.Calendar;

public class AosUiActivity extends Activity implements HomeView.Host {

    private HomeView mHomeView;
    private HomeCtx  mCtx;
    private HomeData mData;

    public interface TabListener { void onTab(String action); }
    public TabListener tabListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        Fonts.init(this);
        Store.init(this);

        // Backend is null — we stub it; HomeData only needs it for network calls
        mData = new HomeData(new JarvisBackend(this));
        mData.name  = Store.get("jarvis.username", "JARVIS");
        mData.greet = greeting();

        // Wheel slots (arc menu, 8 items)
        mData.wheel.clear();
        mData.wheel.add(new HomeData.Entry("chat",     "CHAT",     "AI",    "spark"));
        mData.wheel.add(new HomeData.Entry("vault",    "VAULT",    "PRIV",  "lock"));
        mData.wheel.add(new HomeData.Entry("wallet",   "WALLET",   "SOL",   "wallet"));
        mData.wheel.add(new HomeData.Entry("memory",   "MEMORY",   "MEM",   "brain"));
        mData.wheel.add(new HomeData.Entry("remind",   "REMIND",   "TIME",  "bell"));
        mData.wheel.add(new HomeData.Entry("habits",   "HABITS",   "TRACK", "heart"));
        mData.wheel.add(new HomeData.Entry("todos",    "TODOS",    "QUEUE", "list"));
        mData.wheel.add(new HomeData.Entry("settings", "SETTINGS", "SYS",   "gear"));

        // Dock slots (bottom quick launch, 5 items)
        mData.dock.clear();
        mData.dock.add(new HomeData.Entry("chat",     "CHAT",     "AI",   "spark"));
        mData.dock.add(new HomeData.Entry("vault",    "VAULT",    "PRIV", "lock"));
        mData.dock.add(new HomeData.Entry("wallet",   "WALLET",   "SOL",  "wallet"));
        mData.dock.add(new HomeData.Entry("memory",   "MEMORY",   "MEM",  "brain"));
        mData.dock.add(new HomeData.Entry("settings", "SETTINGS", "SYS",  "gear"));

        mCtx = new HomeCtx(mData);

        // Apply AugmaOS dark theme values directly
        Theme th = mCtx.th;
        th.BG         = 0xFF050C10;
        th.CARD       = 0xFF0D1A24;
        th.INK        = 0xFFE8F4FF;
        th.INK_RAW    = 0xFFE8F4FF;
        th.LINE       = 0xFF112233;
        th.LINE2      = 0xFF1A3A55;
        th.BLUE       = 0xFF3A8EFF;
        th.RED        = 0xFFE03535;
        th.ACCENT_RAW = 0xFFFF7A26;
        th.GLOW       = 0x224BD8FA;
        th.SEG        = 0xFF0D1E30;
        th.ACT        = 0xFF0F2840;
        th.GRID       = 0xFF0A1520;
        th.FRAME      = 0xFF0D1A24;
        th.DIM        = 0xFF4A6680;
        th.light      = false;
        th.aoa        = false;

        Perf.set("profile", "HIGH");

        mHomeView = new HomeView(this, mCtx, this);
        setContentView(mHomeView);
        mHomeView.post(() -> mHomeView.relayout());
    }

    // ── HomeView.Host ─────────────────────────────────────────────────────────
    @Override public void go(String action) {
        if (tabListener != null) tabListener.onTab(action);
    }
    @Override public void page(int i)      {}
    @Override public void swipe(boolean up){}
    @Override public void longOrb()        {}
    @Override public void doubleTap()      {}

    // ── Lifecycle ─────────────────────────────────────────────────────────────
    @Override protected void onResume()  { super.onResume();  if (mHomeView != null) mHomeView.restoreLayers(); }
    @Override protected void onPause()   { super.onPause();   if (mHomeView != null) mHomeView.dropLayers();    }
    @Override protected void onDestroy() { super.onDestroy(); if (mHomeView != null) mHomeView.release();       }

    private static String greeting() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return h < 12 ? "GOOD MORNING" : h < 17 ? "GOOD AFTERNOON" : "GOOD EVENING";
    }
}
