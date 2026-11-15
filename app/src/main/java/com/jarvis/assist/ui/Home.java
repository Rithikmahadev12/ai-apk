package com.jarvis.assist.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jarvis.assist.core.Fonts;
import com.jarvis.assist.core.Palette;
import com.jarvis.assist.core.Store;
import com.jarvis.assist.core.Vault;
import com.jarvis.assist.network.JarvisNet;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Home — AugmaOS UI shell running Jarvis backend.
 *
 * Screen flow:
 *   SETUP → PICKER → HUD
 *
 * HUD tabs: CHAT · VAULT · WALLET · MEMORY · REMIND · HABITS · TODOS · SETTINGS
 */
public class Home extends AppCompatActivity {

    // ── Screen IDs ────────────────────────────────────────────────────────────
    private static final int S_SETUP  = 0;
    private static final int S_PICKER = 1;
    private static final int S_HUD    = 2;
    private int mScreen = S_SETUP;

    // ── Root ──────────────────────────────────────────────────────────────────
    private FrameLayout mRoot;

    // ── Setup screen ──────────────────────────────────────────────────────────
    private View        mSetupView;
    private EditText    mEtUrl;
    private Button      mBtnConnect;
    private TextView    mTvUrlErr;
    private ProgressBar mPbUrl;

    // ── Picker screen ─────────────────────────────────────────────────────────
    private View         mPickerView;
    private LinearLayout mPickerList;
    private EditText     mEtManual;
    private ProgressBar  mPbPicker;
    private TextView     mTvPickerErr;

    // ── HUD screen ────────────────────────────────────────────────────────────
    private View         mHudView;
    private OrbView      mOrb;
    private TextView     mTvClock;
    private TextView     mTvDate;
    private LinearLayout mContent;
    private String       mActiveTab = "CHAT";

    // ── Clock ─────────────────────────────────────────────────────────────────
    private final Handler mClockH = new Handler(Looper.getMainLooper());

    // ── Chat ──────────────────────────────────────────────────────────────────
    private final List<JarvisNet.ChatMsg> mChatHistory = new ArrayList<>();
    private LinearLayout mChatMsgs;

    // ── Vault ─────────────────────────────────────────────────────────────────
    private List<Vault.Entry> mVaultAll      = new ArrayList<>();
    private List<Vault.Entry> mVaultFiltered = new ArrayList<>();
    private String             mVaultCat     = "All";
    private String             mVaultSearch  = "";
    private LinearLayout       mVaultList;

    // ─────────────────────────────────────────────────────────────────────────
    //  LIFECYCLE
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mRoot = new FrameLayout(this);
        mRoot.setBackgroundColor(Palette.BG_DEEP);
        setContentView(mRoot);

        buildSetup();
        buildPicker();
        buildHud();

        if (Store.hasServer() && Store.hasUser()) {
            show(S_HUD); startActivity(new android.content.Intent(this, com.jarvis.assist.aosui.AosUiActivity.class)); finish(); startActivity(new android.content.Intent(this, com.jarvis.assist.aosui.AosUiActivity.class)); finish();
        } else if (Store.hasServer()) {
            show(S_PICKER); loadProfiles(Store.serverUrl());
        } else {
            show(S_SETUP);
        }
    }

    @Override protected void onResume() {
        super.onResume();
        startClock();
    }

    @Override protected void onPause() {
        super.onPause();
        mClockH.removeCallbacksAndMessages(null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SCREEN SWITCHING
    // ─────────────────────────────────────────────────────────────────────────

    private void show(int s) {
        mScreen = s;
        if (mSetupView  != null) mSetupView.setVisibility( s == S_SETUP  ? View.VISIBLE : View.GONE);
        if (mPickerView != null) mPickerView.setVisibility(s == S_PICKER ? View.VISIBLE : View.GONE);
        if (mHudView    != null) mHudView.setVisibility(   s == S_HUD    ? View.VISIBLE : View.GONE);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SCREEN 1 — SETUP
    // ─────────────────────────────────────────────────────────────────────────

    private void buildSetup() {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Palette.BG_DEEP);
        sv.setVerticalScrollBarEnabled(false);

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setGravity(Gravity.CENTER_HORIZONTAL);
        col.setPadding(dp(32), dp(90), dp(32), dp(40));

        // Wordmark
        TextView brand = Kit.brandTv(this, "JARVIS", Palette.CYAN, 52f);
        brand.setGravity(Gravity.CENTER);
        brand.setLetterSpacing(0.35f);
        col.addView(brand, Kit.matchWrap());

        TextView sub = Kit.monoTv(this, "// AOS INTERFACE", Palette.TEXT_DIM, 10f);
        sub.setGravity(Gravity.CENTER);
        sub.setLetterSpacing(0.2f);
        col.addView(sub, Kit.matchWrap());

        col.addView(Kit.divider(this, Palette.CYAN, 1, 36, 28), Kit.matchWrap());

        col.addView(Kit.monoTv(this, "// SERVER ENDPOINT", Palette.TEXT_DIM, 10f), Kit.matchWrap());
        col.addView(Kit.vSpace(this, 8), Kit.matchWrap());

        mEtUrl = Kit.input(this, "http://your-server:3000");
        col.addView(mEtUrl, Kit.matchWrap());

        mTvUrlErr = Kit.monoTv(this, "", Palette.RED, 11f);
        mTvUrlErr.setVisibility(View.GONE);
        col.addView(mTvUrlErr, Kit.matchWrap());

        mPbUrl = Kit.progress(this);
        col.addView(mPbUrl, Kit.matchWrap());

        col.addView(Kit.vSpace(this, 20), Kit.matchWrap());

        mBtnConnect = Kit.btn(this, "CONNECT", Palette.CYAN, Palette.BG_DEEP);
        mBtnConnect.setOnClickListener(v -> doConnect());
        col.addView(mBtnConnect, Kit.matchWrap());

        sv.addView(col, new ScrollView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mSetupView = sv;
        mRoot.addView(sv, Kit.fMatchMatch());
    }

    private void doConnect() {
        String url = mEtUrl.getText().toString().trim();
        if (url.isEmpty()) { showUrlErr("ENTER SERVER URL"); return; }
        if (!url.startsWith("http")) { showUrlErr("URL MUST START WITH HTTP"); return; }
        mTvUrlErr.setVisibility(View.GONE);
        mPbUrl.setVisibility(View.VISIBLE);
        mBtnConnect.setEnabled(false);
        Store.set(Store.KEY_SERVER_URL, url);

        JarvisNet.fetchProfiles(url, new JarvisNet.Cb<List<JarvisNet.Profile>>() {
            @Override public void ok(List<JarvisNet.Profile> r) {
                mPbUrl.setVisibility(View.GONE);
                mBtnConnect.setEnabled(true);
                show(S_PICKER);
                fillProfiles(r);
            }
            @Override public void err(String e) {
                mPbUrl.setVisibility(View.GONE);
                mBtnConnect.setEnabled(true);
                showUrlErr(e);
            }
        });
    }

    private void showUrlErr(String msg) {
        mTvUrlErr.setText(msg);
        mTvUrlErr.setVisibility(View.VISIBLE);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SCREEN 2 — PICKER
    // ─────────────────────────────────────────────────────────────────────────

    private void buildPicker() {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Palette.BG_DEEP);
        sv.setVerticalScrollBarEnabled(false);

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(dp(24), dp(64), dp(24), dp(40));

        TextView title = Kit.brandTv(this, "SELECT PROFILE", Palette.CYAN, 28f);
        title.setLetterSpacing(0.15f);
        col.addView(title, Kit.matchWrap());

        col.addView(Kit.monoTv(this, "// AUTHORIZED OPERATORS", Palette.TEXT_DIM, 10f), Kit.matchWrap());
        col.addView(Kit.divider(this, Palette.PURPLE, 1, 14, 14), Kit.matchWrap());

        mPickerList = new LinearLayout(this);
        mPickerList.setOrientation(LinearLayout.VERTICAL);
        col.addView(mPickerList, Kit.matchWrap());

        mPbPicker = Kit.progress(this);
        col.addView(mPbPicker, Kit.matchWrap());

        mTvPickerErr = Kit.monoTv(this, "", Palette.RED, 11f);
        mTvPickerErr.setVisibility(View.GONE);
        col.addView(mTvPickerErr, Kit.matchWrap());

        col.addView(Kit.divider(this, Palette.BORDER, 1, 20, 8), Kit.matchWrap());
        col.addView(Kit.monoTv(this, "// MANUAL ENTRY", Palette.TEXT_DIM, 10f), Kit.matchWrap());
        col.addView(Kit.vSpace(this, 8), Kit.matchWrap());

        mEtManual = Kit.input(this, "callsign");
        col.addView(mEtManual, Kit.matchWrap());

        Button manualBtn = Kit.btn(this, "ACCESS SYSTEM", Palette.PURPLE, Palette.TEXT_HI);
        manualBtn.setOnClickListener(v -> {
            String n = mEtManual.getText().toString().trim();
            if (!n.isEmpty()) signIn(n);
        });
        col.addView(manualBtn, Kit.matchWrap());

        col.addView(Kit.vSpace(this, 12), Kit.matchWrap());

        Button backBtn = Kit.btn(this, "← CHANGE SERVER", Palette.BG_CARD, Palette.TEXT_DIM);
        backBtn.setOnClickListener(v -> show(S_SETUP));
        col.addView(backBtn, Kit.matchWrap());

        sv.addView(col, new ScrollView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mPickerView = sv;
        mRoot.addView(sv, Kit.fMatchMatch());
    }

    private void loadProfiles(String url) {
        mPbPicker.setVisibility(View.VISIBLE);
        JarvisNet.fetchProfiles(url, new JarvisNet.Cb<List<JarvisNet.Profile>>() {
            @Override public void ok(List<JarvisNet.Profile> r) {
                mPbPicker.setVisibility(View.GONE);
                fillProfiles(r);
            }
            @Override public void err(String e) {
                mPbPicker.setVisibility(View.GONE);
                mTvPickerErr.setText(e);
                mTvPickerErr.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fillProfiles(List<JarvisNet.Profile> list) {
        mPickerList.removeAllViews();
        for (JarvisNet.Profile p : list) {
            LinearLayout card = buildProfileCard(p);
            card.setOnClickListener(v -> signIn(p.name));
            mPickerList.addView(card, Kit.matchWrap());
            mPickerList.addView(Kit.vSpace(this, 8), Kit.matchWrap());
        }
    }

    private LinearLayout buildProfileCard(JarvisNet.Profile p) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundColor(Palette.BG_CARD);
        card.setPadding(dp(16), dp(14), dp(16), dp(14));
        card.setGravity(Gravity.CENTER_VERTICAL);

        // Avatar
        FrameLayout av = new FrameLayout(this);
        int avColor = p.owner ? Palette.CYAN : Palette.PURPLE;
        av.setBackground(Kit.circle(avColor));
        TextView init = Kit.brandTv(this,
            p.name.isEmpty() ? "?" : String.valueOf(p.name.charAt(0)).toUpperCase(),
            Palette.BG_DEEP, 15f);
        init.setGravity(Gravity.CENTER);
        av.addView(init, new FrameLayout.LayoutParams(dp(40), dp(40)));
        card.addView(av, new LinearLayout.LayoutParams(dp(40), dp(40)));
        card.addView(Kit.hSpace(this, 12));

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);

        TextView nm = Kit.brandTv(this, p.name.toUpperCase(), Palette.TEXT_HI, 15f);
        nm.setLetterSpacing(0.08f);
        info.addView(nm, Kit.matchWrap());

        TextView role = Kit.monoTv(this,
            p.owner ? "// OWNER" : "// " + p.role.toUpperCase(),
            p.owner ? Palette.CYAN : Palette.TEXT_DIM, 10f);
        info.addView(role, Kit.matchWrap());

        card.addView(info, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView arrow = Kit.monoTv(this, "→", Palette.CYAN, 16f);
        card.addView(arrow, Kit.wrapWrap());

        return card;
    }

    private void signIn(String name) {
        Store.set(Store.KEY_USER_NAME, name);
        show(S_HUD); startActivity(new android.content.Intent(this, com.jarvis.assist.aosui.AosUiActivity.class)); finish();
        loadHud();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SCREEN 3 — HUD
    // ─────────────────────────────────────────────────────────────────────────

    private void buildHud() {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Palette.BG_DEEP);
        sv.setVerticalScrollBarEnabled(false);
        sv.setFillViewport(true);

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setBackgroundColor(Palette.BG_DEEP);

        // ── Orb + header overlay ──────────────────────────────────────────────
        int sw = getResources().getDisplayMetrics().widthPixels;
        FrameLayout topFrame = new FrameLayout(this);

        mOrb = new OrbView(this);
        mOrb.setOnTabClick(tab -> {
            mActiveTab = tab;
            mOrb.setActiveTab(tab);
            updateTabStrip();
            refreshTab();
        });
        topFrame.addView(mOrb, new FrameLayout.LayoutParams(sw, sw));

        // Header overlay
        topFrame.addView(buildHudHeader(), new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP));

        // Status corner
        topFrame.addView(buildStatusCorner(), new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.TOP | Gravity.END));

        col.addView(topFrame, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, sw));

        // ── Tab nav strip ─────────────────────────────────────────────────────
        col.addView(buildTabStrip(), new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // ── Content area ──────────────────────────────────────────────────────
        mContent = new LinearLayout(this);
        mContent.setOrientation(LinearLayout.VERTICAL);
        mContent.setPadding(dp(16), dp(10), dp(16), dp(40));
        col.addView(mContent, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        sv.addView(col, new ScrollView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mHudView = sv;
        mRoot.addView(sv, Kit.fMatchMatch());
    }

    private LinearLayout buildHudHeader() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(16), dp(14), dp(16), dp(8));

        // Status dot
        View dot = new View(this);
        dot.setBackground(Kit.circle(Palette.GREEN));
        row.addView(dot, new LinearLayout.LayoutParams(dp(10), dp(10)));
        row.addView(Kit.hSpace(this, 8));

        TextView name = Kit.brandTv(this, Store.userName().toUpperCase(), Palette.CYAN, 18f);
        name.setLetterSpacing(0.25f);
        row.addView(name, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        LinearLayout clkCol = new LinearLayout(this);
        clkCol.setOrientation(LinearLayout.VERTICAL);
        clkCol.setGravity(Gravity.END);

        mTvClock = Kit.monoTv(this, "00:00:00", Palette.TEXT_HI, 20f);
        clkCol.addView(mTvClock, Kit.wrapWrap());

        mTvDate = Kit.monoTv(this, "", Palette.TEXT_DIM, 10f);
        clkCol.addView(mTvDate, Kit.wrapWrap());

        row.addView(clkCol, Kit.wrapWrap());
        return row;
    }

    private LinearLayout buildStatusCorner() {
        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setGravity(Gravity.END);
        col.setPadding(0, dp(14), dp(10), 0);
        String[] lines   = {"SYS.OK","NET.ON","AI.RDY","V1.0.0"};
        int[]    colors  = {Palette.GREEN, Palette.CYAN, Palette.PURPLE, Palette.TEXT_DIM};
        for (int i = 0; i < lines.length; i++) {
            TextView t = Kit.monoTv(this, lines[i], colors[i], 8f);
            t.setGravity(Gravity.END);
            col.addView(t, Kit.wrapWrap());
            col.addView(Kit.vSpace(this, 2), Kit.wrapWrap());
        }
        return col;
    }

    // ── Tab strip ─────────────────────────────────────────────────────────────

    private HorizontalScrollView mTabStripHsv;
    private LinearLayout         mTabStripRow;

    private static final String[] ALL_TABS = {
        "CHAT","VAULT","WALLET","MEMORY","REMIND","HABITS","TODOS","SETTINGS"
    };

    private HorizontalScrollView buildTabStrip() {
        mTabStripHsv = new HorizontalScrollView(this);
        mTabStripHsv.setBackgroundColor(Palette.BG_CARD);
        mTabStripHsv.setHorizontalScrollBarEnabled(false);

        mTabStripRow = new LinearLayout(this);
        mTabStripRow.setOrientation(LinearLayout.HORIZONTAL);
        mTabStripRow.setPadding(dp(10), 0, dp(10), 0);

        for (String tab : ALL_TABS) {
            TextView chip = buildTabChip(tab);
            mTabStripRow.addView(chip, Kit.wrapWrap());
            mTabStripRow.addView(Kit.hSpace(this, 2), Kit.wrapWrap());
        }

        mTabStripHsv.addView(mTabStripRow, new HorizontalScrollView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, dp(44)));

        return mTabStripHsv;
    }

    private TextView buildTabChip(String tab) {
        boolean active = tab.equals(mActiveTab);
        TextView tv = Kit.monoTv(this, tab,
            active ? Palette.BG_DEEP : Palette.TEXT_DIM, 10.5f);
        tv.setBackgroundColor(active ? Palette.CYAN : Color.TRANSPARENT);
        tv.setPadding(dp(14), dp(10), dp(14), dp(10));
        tv.setGravity(Gravity.CENTER);
        tv.setLetterSpacing(0.08f);
        tv.setTag(tab);
        tv.setOnClickListener(v -> {
            mActiveTab = tab;
            mOrb.setActiveTab(tab);
            updateTabStrip();
            refreshTab();
        });
        return tv;
    }

    private void updateTabStrip() {
        if (mTabStripRow == null) return;
        for (int i = 0; i < mTabStripRow.getChildCount(); i++) {
            View v = mTabStripRow.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                boolean active = mActiveTab.equals(tv.getTag());
                tv.setBackgroundColor(active ? Palette.CYAN : Color.TRANSPARENT);
                tv.setTextColor(active ? Palette.BG_DEEP : Palette.TEXT_DIM);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HUD LOAD
    // ─────────────────────────────────────────────────────────────────────────

    private void loadHud() {
        startClock();
        mVaultAll = Vault.load(this);

        // Set orb greeting
        int hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greet = hr < 12 ? "Good Morning" : hr < 17 ? "Good Afternoon" : "Good Evening";
        mOrb.setGreeting(greet, Store.userName());
        mOrb.setActiveTab(mActiveTab);
        updateTabStrip();
        refreshTab();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB ROUTER
    // ─────────────────────────────────────────────────────────────────────────

    private void refreshTab() {
        mContent.removeAllViews();
        switch (mActiveTab) {
            case "CHAT":     buildChatTab();     break;
            case "VAULT":    buildVaultTab();    break;
            case "WALLET":   loadWalletTab();    break;
            case "MEMORY":   loadMemoryTab();    break;
            case "REMIND":   loadRemindTab();    break;
            case "HABITS":   loadHabitsTab();    break;
            case "TODOS":    loadTodosTab();     break;
            case "SETTINGS": buildSettingsTab(); break;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB — CHAT
    // ─────────────────────────────────────────────────────────────────────────

    private void buildChatTab() {
        mChatHistory.clear();

        mChatMsgs = new LinearLayout(this);
        mChatMsgs.setOrientation(LinearLayout.VERTICAL);
        mContent.addView(mChatMsgs, Kit.matchWrap());

        addBubble("JARVIS",
            "Systems online. How can I assist, " + Store.userName() + "?", false);

        mContent.addView(Kit.vSpace(this, 10), Kit.matchWrap());

        LinearLayout inputRow = new LinearLayout(this);
        inputRow.setOrientation(LinearLayout.HORIZONTAL);
        inputRow.setGravity(Gravity.CENTER_VERTICAL);

        EditText et = Kit.input(this, "Message...");
        et.setMaxLines(3);
        mContent.addView(inputRow, Kit.matchWrap());

        LinearLayout.LayoutParams etLp = new LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        inputRow.addView(et, etLp);
        inputRow.addView(Kit.hSpace(this, 8));

        Button send = Kit.btn(this, "→", Palette.CYAN, Palette.BG_DEEP);
        send.setLayoutParams(Kit.wrapWrap());
        send.setPadding(dp(14), dp(12), dp(14), dp(12));
        send.setOnClickListener(v -> {
            String msg = et.getText().toString().trim();
            if (msg.isEmpty()) return;
            et.setText("");
            addBubble(Store.userName(), msg, true);
            mChatHistory.add(new JarvisNet.ChatMsg("user", msg));

            TextView typing = Kit.monoTv(this, "// processing...", Palette.TEXT_DIM, 11f);
            typing.setTag("TYPING");
            mChatMsgs.addView(typing, Kit.matchWrap());

            JarvisNet.sendChat(Store.userName(), msg, mChatHistory,
                new JarvisNet.Cb<String>() {
                    @Override public void ok(String r) {
                        removeTag("TYPING");
                        mChatHistory.add(new JarvisNet.ChatMsg("assistant", r));
                        addBubble("JARVIS", r, false);
                    }
                    @Override public void err(String e) {
                        removeTag("TYPING");
                        addBubble("SYS", "ERROR: " + e, false);
                    }
                });
        });
        inputRow.addView(send, Kit.wrapWrap());
    }

    private void addBubble(String sender, String text, boolean isUser) {
        LinearLayout bub = new LinearLayout(this);
        bub.setOrientation(LinearLayout.VERTICAL);
        bub.setBackgroundColor(isUser ? 0xFF0D1A2A : Palette.BG_CARD);
        bub.setPadding(dp(14), dp(10), dp(14), dp(10));
        LinearLayout.LayoutParams lp = Kit.matchWrap();
        lp.bottomMargin = dp(8);
        bub.setLayoutParams(lp);

        TextView sv = Kit.monoTv(this, sender.toUpperCase() + " //",
            isUser ? Palette.PURPLE : Palette.CYAN, 9f);
        bub.addView(sv, Kit.matchWrap());

        TextView mt = Kit.bodyTv(this, text, Palette.TEXT_HI, 14f);
        bub.addView(mt, Kit.matchWrap());

        mChatMsgs.addView(bub);
    }

    private void removeTag(String tag) {
        for (int i = mChatMsgs.getChildCount() - 1; i >= 0; i--) {
            if (tag.equals(mChatMsgs.getChildAt(i).getTag())) {
                mChatMsgs.removeViewAt(i); break;
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB — VAULT
    // ─────────────────────────────────────────────────────────────────────────

    private void buildVaultTab() {
        // Category pills
        HorizontalScrollView pillsHsv = new HorizontalScrollView(this);
        pillsHsv.setHorizontalScrollBarEnabled(false);
        LinearLayout pillsRow = new LinearLayout(this);
        pillsRow.setOrientation(LinearLayout.HORIZONTAL);
        String[] cats = {"All","Social","Finance","Work","Other"};
        for (String cat : cats) {
            boolean sel = cat.equals(mVaultCat);
            TextView pill = Kit.pill(this, cat,
                sel ? Palette.PURPLE : Palette.BORDER,
                sel ? Palette.BG_DEEP : Palette.TEXT_DIM);
            pill.setOnClickListener(v -> {
                mVaultCat = cat;
                applyVaultFilter();
                buildVaultTab();
            });
            LinearLayout.LayoutParams plp = Kit.wrapWrap();
            plp.rightMargin = dp(6);
            pillsRow.addView(pill, plp);
        }
        pillsHsv.addView(pillsRow, new HorizontalScrollView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContent.addView(pillsHsv, Kit.matchWrap());
        mContent.addView(Kit.vSpace(this, 10), Kit.matchWrap());

        // Search
        EditText search = Kit.input(this, "// SEARCH VAULT");
        search.setText(mVaultSearch);
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int i, int c, int a) {}
            public void onTextChanged(CharSequence s, int i, int b, int c) {}
            public void afterTextChanged(Editable s) {
                mVaultSearch = s.toString().toLowerCase();
                applyVaultFilter();
                renderVaultList();
            }
        });
        mContent.addView(search, Kit.matchWrap());
        mContent.addView(Kit.vSpace(this, 8), Kit.matchWrap());

        // Add button
        Button addBtn = Kit.btn(this, "+ ADD ENTRY", Palette.PURPLE, Palette.TEXT_HI);
        addBtn.setOnClickListener(v -> showVaultAdd());
        mContent.addView(addBtn, Kit.matchWrap());
        mContent.addView(Kit.vSpace(this, 10), Kit.matchWrap());

        mVaultList = new LinearLayout(this);
        mVaultList.setOrientation(LinearLayout.VERTICAL);
        mContent.addView(mVaultList, Kit.matchWrap());

        applyVaultFilter();
        renderVaultList();
    }

    private void applyVaultFilter() {
        mVaultFiltered.clear();
        for (Vault.Entry e : mVaultAll) {
            boolean c = mVaultCat.equals("All") || e.category.equalsIgnoreCase(mVaultCat);
            boolean s = mVaultSearch.isEmpty() ||
                e.label.toLowerCase().contains(mVaultSearch) ||
                e.username.toLowerCase().contains(mVaultSearch);
            if (c && s) mVaultFiltered.add(e);
        }
    }

    private void renderVaultList() {
        if (mVaultList == null) return;
        mVaultList.removeAllViews();
        if (mVaultFiltered.isEmpty()) {
            mVaultList.addView(Kit.statusLabel(this, "// NO ENTRIES FOUND", Palette.TEXT_DIM));
            return;
        }
        for (Vault.Entry e : mVaultFiltered) {
            mVaultList.addView(buildVaultCard(e), Kit.matchWrap());
            mVaultList.addView(Kit.vSpace(this, 8), Kit.matchWrap());
        }
    }

    private LinearLayout buildVaultCard(Vault.Entry e) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(Palette.BG_CARD);
        card.setPadding(dp(16), dp(14), dp(16), dp(14));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);

        TextView lbl = Kit.brandTv(this, e.label.toUpperCase(), Palette.TEXT_HI, 15f);
        top.addView(lbl, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView catPill = Kit.pill(this, e.category, Palette.PURPLE_20, Palette.PURPLE);
        top.addView(catPill, Kit.wrapWrap());
        card.addView(top, Kit.matchWrap());

        if (!e.username.isEmpty()) {
            card.addView(Kit.monoTv(this, "// " + e.username, Palette.TEXT_MID, 11f), Kit.matchWrap());
        }

        // Password row
        LinearLayout pwRow = new LinearLayout(this);
        pwRow.setOrientation(LinearLayout.HORIZONTAL);
        pwRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams pwLp = Kit.matchWrap();
        pwLp.topMargin = dp(6);
        pwRow.setLayoutParams(pwLp);

        TextView pwTv = Kit.monoTv(this,
            e.revealed ? (Vault.decrypt(e.encPw) != null ? Vault.decrypt(e.encPw) : "••••") : "••••••••••••",
            e.revealed ? Palette.GREEN : Palette.TEXT_DIM, 13f);
        pwRow.addView(pwTv, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        Button revBtn = Kit.smallBtn(this, e.revealed ? "HIDE" : "SHOW", Palette.PURPLE, Palette.TEXT_HI);
        revBtn.setOnClickListener(v -> { e.revealed = !e.revealed; renderVaultList(); });
        pwRow.addView(revBtn, Kit.wrapWrap());
        pwRow.addView(Kit.hSpace(this, 6));

        Button copyBtn = Kit.smallBtn(this, "COPY", Palette.CYAN, Palette.BG_DEEP);
        copyBtn.setOnClickListener(v -> {
            String pw = Vault.decrypt(e.encPw);
            if (pw != null) {
                android.content.ClipboardManager cm =
                    (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) cm.setPrimaryClip(android.content.ClipData.newPlainText("pw", pw));
                Toast.makeText(this, "COPIED", Toast.LENGTH_SHORT).show();
            }
        });
        pwRow.addView(copyBtn, Kit.wrapWrap());
        card.addView(pwRow);

        Button delBtn = Kit.smallBtn(this, "× DELETE", Palette.RED, Palette.TEXT_HI);
        LinearLayout.LayoutParams dlp = Kit.wrapWrap();
        dlp.topMargin = dp(8);
        delBtn.setLayoutParams(dlp);
        delBtn.setOnClickListener(v -> {
            Vault.delete(this, e.id);
            mVaultAll.removeIf(x -> x.id.equals(e.id));
            applyVaultFilter();
            renderVaultList();
        });
        card.addView(delBtn);

        return card;
    }

    private void showVaultAdd() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(20), dp(10), dp(20), dp(10));
        form.setBackgroundColor(Palette.BG_CARD);

        EditText etLbl = Kit.input(this, "Label (site / app)");
        EditText etUsr = Kit.input(this, "Username / Email");
        EditText etPw  = Kit.input(this, "Password");
        etPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditText etCat = Kit.input(this, "Category: Social / Finance / Work / Other");
        EditText etNt  = Kit.input(this, "Notes (optional)");

        form.addView(etLbl); form.addView(etUsr);
        form.addView(etPw);  form.addView(etCat); form.addView(etNt);

        new AlertDialog.Builder(this)
            .setTitle("NEW VAULT ENTRY")
            .setView(form)
            .setPositiveButton("SAVE", (d, w) -> {
                String lbl = etLbl.getText().toString().trim();
                String pw  = etPw.getText().toString();
                if (!lbl.isEmpty() && !pw.isEmpty()) {
                    Vault.Entry ne = Vault.save(this, lbl,
                        etUsr.getText().toString().trim(), pw,
                        etCat.getText().toString().trim(),
                        etNt.getText().toString().trim());
                    if (ne != null) {
                        mVaultAll.add(ne);
                        applyVaultFilter();
                        renderVaultList();
                    }
                }
            })
            .setNegativeButton("CANCEL", null)
            .show();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB — WALLET
    // ─────────────────────────────────────────────────────────────────────────

    private void loadWalletTab() {
        mContent.addView(Kit.sectionHeader(this, "SOLANA WALLET"), Kit.matchWrap());
        ProgressBar pb = Kit.progress(this);
        pb.setVisibility(View.VISIBLE);
        mContent.addView(pb, Kit.matchWrap());

        JarvisNet.fetchWallet(Store.userName(), new JarvisNet.Cb<JSONObject>() {
            @Override public void ok(JSONObject r) {
                mContent.removeView(pb);
                renderKvCard(r);
            }
            @Override public void err(String e) {
                mContent.removeView(pb);
                mContent.addView(Kit.statusLabel(Home.this, "// ERR: " + e, Palette.RED));
            }
        });
    }

    private void renderKvCard(JSONObject obj) {
        try {
            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String k = keys.next();
                mContent.addView(Kit.dataRow(this, k.toUpperCase(), obj.optString(k, "—")));
            }
        } catch (Exception ignored) {}
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB — MEMORY
    // ─────────────────────────────────────────────────────────────────────────

    private void loadMemoryTab() {
        mContent.addView(Kit.sectionHeader(this, "MEMORY BANK"), Kit.matchWrap());
        ProgressBar pb = Kit.progress(this);
        pb.setVisibility(View.VISIBLE);
        mContent.addView(pb, Kit.matchWrap());

        JarvisNet.fetchMemory(Store.userName(), new JarvisNet.Cb<List<String>>() {
            @Override public void ok(List<String> r) {
                mContent.removeView(pb);
                if (r.isEmpty()) {
                    mContent.addView(Kit.statusLabel(Home.this, "// NO MEMORY ENTRIES", Palette.TEXT_DIM));
                    return;
                }
                for (String item : r) mContent.addView(Kit.dataRow(Home.this, "▸", item));
            }
            @Override public void err(String e) {
                mContent.removeView(pb);
                mContent.addView(Kit.statusLabel(Home.this, "// ERR: " + e, Palette.RED));
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB — REMINDERS
    // ─────────────────────────────────────────────────────────────────────────

    private void loadRemindTab() {
        mContent.addView(Kit.sectionHeader(this, "ACTIVE REMINDERS"), Kit.matchWrap());
        ProgressBar pb = Kit.progress(this);
        pb.setVisibility(View.VISIBLE);
        mContent.addView(pb, Kit.matchWrap());

        JarvisNet.fetchReminders(Store.userName(), new JarvisNet.Cb<List<JarvisNet.Reminder>>() {
            @Override public void ok(List<JarvisNet.Reminder> r) {
                mContent.removeView(pb);
                if (r.isEmpty()) {
                    mContent.addView(Kit.statusLabel(Home.this, "// NO ACTIVE REMINDERS", Palette.TEXT_DIM));
                    return;
                }
                for (JarvisNet.Reminder rem : r) mContent.addView(buildReminderCard(rem));
            }
            @Override public void err(String e) {
                mContent.removeView(pb);
                mContent.addView(Kit.statusLabel(Home.this, "// ERR: " + e, Palette.RED));
            }
        });
    }

    private LinearLayout buildReminderCard(JarvisNet.Reminder r) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundColor(r.isDue ? 0xFF0F1A10 : Palette.BG_CARD);
        card.setPadding(dp(14), dp(12), dp(14), dp(12));
        card.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = Kit.matchWrap();
        lp.bottomMargin = dp(8);
        card.setLayoutParams(lp);

        View dot = new View(this);
        dot.setBackground(Kit.circle(r.isDue ? Palette.GREEN : Palette.TEXT_DIM));
        card.addView(dot, new LinearLayout.LayoutParams(dp(10), dp(10)));
        card.addView(Kit.hSpace(this, 12));

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.addView(Kit.bodyTv(this, r.label, Palette.TEXT_HI, 14f), Kit.matchWrap());
        if (r.fireAt != null && !r.fireAt.isEmpty())
            info.addView(Kit.monoTv(this, "// " + r.fireAt, Palette.TEXT_DIM, 10f), Kit.matchWrap());

        card.addView(info, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        if (r.isDue) {
            TextView due = Kit.pill(this, "DUE", Palette.GREEN_20, Palette.GREEN);
            card.addView(due, Kit.wrapWrap());
        }
        return card;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB — HABITS
    // ─────────────────────────────────────────────────────────────────────────

    private void loadHabitsTab() {
        mContent.addView(Kit.sectionHeader(this, "HABITS DUE TODAY"), Kit.matchWrap());
        ProgressBar pb = Kit.progress(this);
        pb.setVisibility(View.VISIBLE);
        mContent.addView(pb, Kit.matchWrap());

        JarvisNet.fetchHabits(Store.userName(), new JarvisNet.Cb<List<String>>() {
            @Override public void ok(List<String> r) {
                mContent.removeView(pb);
                if (r.isEmpty()) {
                    mContent.addView(Kit.statusLabel(Home.this, "// ALL HABITS CLEAR", Palette.GREEN));
                    return;
                }
                for (String h : r) mContent.addView(Kit.dataRow(Home.this, "◈", h));
            }
            @Override public void err(String e) {
                mContent.removeView(pb);
                mContent.addView(Kit.statusLabel(Home.this, "// ERR: " + e, Palette.RED));
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB — TODOS
    // ─────────────────────────────────────────────────────────────────────────

    private void loadTodosTab() {
        mContent.addView(Kit.sectionHeader(this, "TODO QUEUE"), Kit.matchWrap());
        ProgressBar pb = Kit.progress(this);
        pb.setVisibility(View.VISIBLE);
        mContent.addView(pb, Kit.matchWrap());

        JarvisNet.fetchTodos(Store.userName(), new JarvisNet.Cb<List<JarvisNet.Todo>>() {
            @Override public void ok(List<JarvisNet.Todo> r) {
                mContent.removeView(pb);
                if (r.isEmpty()) {
                    mContent.addView(Kit.statusLabel(Home.this, "// QUEUE EMPTY", Palette.TEXT_DIM));
                    return;
                }
                for (JarvisNet.Todo t : r) mContent.addView(buildTodoCard(t));
            }
            @Override public void err(String e) {
                mContent.removeView(pb);
                mContent.addView(Kit.statusLabel(Home.this, "// ERR: " + e, Palette.RED));
            }
        });
    }

    private LinearLayout buildTodoCard(JarvisNet.Todo t) {
        boolean done = "done".equalsIgnoreCase(t.status);
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundColor(Palette.BG_CARD);
        card.setPadding(dp(14), dp(12), dp(14), dp(12));
        card.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = Kit.matchWrap();
        lp.bottomMargin = dp(8);
        card.setLayoutParams(lp);

        View dot = new View(this);
        dot.setBackground(Kit.circle(done ? Palette.GREEN : Palette.PURPLE));
        card.addView(dot, new LinearLayout.LayoutParams(dp(10), dp(10)));
        card.addView(Kit.hSpace(this, 12));

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);

        TextView txt = Kit.bodyTv(this, t.text, done ? Palette.TEXT_DIM : Palette.TEXT_HI, 13f);
        if (done) txt.setPaintFlags(txt.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        info.addView(txt, Kit.matchWrap());
        if (t.addedAt != null && !t.addedAt.isEmpty())
            info.addView(Kit.monoTv(this, "// " + t.addedAt, Palette.TEXT_DIM, 9f), Kit.matchWrap());

        card.addView(info, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView chip = Kit.pill(this, t.status,
            done ? Palette.GREEN_20 : Palette.PURPLE_20,
            done ? Palette.GREEN : Palette.PURPLE);
        card.addView(chip, Kit.wrapWrap());

        return card;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB — SETTINGS
    // ─────────────────────────────────────────────────────────────────────────

    private void buildSettingsTab() {
        mContent.addView(Kit.sectionHeader(this, "SYSTEM"), Kit.matchWrap());
        mContent.addView(Kit.dataRow(this, "SERVER",   Store.serverUrl()), Kit.matchWrap());
        mContent.addView(Kit.vSpace(this, 6),          Kit.matchWrap());
        mContent.addView(Kit.dataRow(this, "OPERATOR", Store.userName()),  Kit.matchWrap());
        mContent.addView(Kit.vSpace(this, 24),         Kit.matchWrap());

        // AI settings
        mContent.addView(Kit.sectionHeader(this, "AI PROVIDER"), Kit.matchWrap());

        EditText etAiUrl = Kit.input(this, "Base URL (e.g. https://api.openai.com/v1)");
        etAiUrl.setText(Store.aiUrl());
        mContent.addView(etAiUrl, Kit.matchWrap());

        EditText etAiKey = Kit.input(this, "API Key");
        etAiKey.setText(Store.aiKey());
        etAiKey.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mContent.addView(etAiKey, Kit.matchWrap());

        EditText etModel = Kit.input(this, "Model (e.g. gpt-4o-mini)");
        etModel.setText(Store.aiModel());
        mContent.addView(etModel, Kit.matchWrap());

        Button saveAi = Kit.btn(this, "SAVE AI CONFIG", Palette.CYAN, Palette.BG_DEEP);
        saveAi.setOnClickListener(v -> {
            Store.set(Store.KEY_AI_URL,   etAiUrl.getText().toString().trim());
            Store.set(Store.KEY_AI_KEY,   etAiKey.getText().toString().trim());
            Store.set(Store.KEY_AI_MODEL, etModel.getText().toString().trim());
            Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
        });
        mContent.addView(saveAi, Kit.matchWrap());

        mContent.addView(Kit.vSpace(this, 24), Kit.matchWrap());

        Button switchProfile = Kit.btn(this, "SWITCH PROFILE", Palette.PURPLE, Palette.TEXT_HI);
        switchProfile.setOnClickListener(v -> {
            show(S_PICKER);
            loadProfiles(Store.serverUrl());
        });
        mContent.addView(switchProfile, Kit.matchWrap());

        mContent.addView(Kit.vSpace(this, 10), Kit.matchWrap());

        Button disconnect = Kit.btn(this, "DISCONNECT + WIPE SESSION", Palette.RED, Palette.TEXT_HI);
        disconnect.setOnClickListener(v -> {
            Store.clearSession();
            show(S_SETUP);
        });
        mContent.addView(disconnect, Kit.matchWrap());
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  CLOCK
    // ─────────────────────────────────────────────────────────────────────────

    private void startClock() {
        mClockH.removeCallbacksAndMessages(null);
        Runnable tick = new Runnable() {
            @Override public void run() {
                if (mTvClock == null) return;
                Date now = new Date();
                mTvClock.setText(new SimpleDateFormat("HH:mm:ss", Locale.US).format(now));
                if (mTvDate != null)
                    mTvDate.setText(new SimpleDateFormat("EEE dd MMM", Locale.US)
                        .format(now).toUpperCase());
                mClockH.postDelayed(this, 1000);
            }
        };
        mClockH.post(tick);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private int dp(float v) {
        return Kit.dp(this, v);
    }
}
