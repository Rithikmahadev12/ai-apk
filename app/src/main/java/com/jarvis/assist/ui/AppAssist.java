package com.jarvis.assist.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.jarvis.assist.core.Fonts;
import com.jarvis.assist.core.Palette;
import com.jarvis.assist.core.Store;
import com.jarvis.assist.network.JarvisNet;

import java.util.ArrayList;
import java.util.List;

/**
 * AppAssist — full-screen AI assistant activity.
 * Mirrors com.augma.oso.Apps$AppAssist from DEX.
 */
public class AppAssist extends AppCompatActivity {

    private LinearLayout mMsgs;
    private EditText     mInput;
    private ScrollView   mScroll;
    private final List<JarvisNet.ChatMsg> mHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Palette.BG_DEEP);

        // Header
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setBackgroundColor(Palette.BG_CARD);
        header.setPadding(dp(16), dp(14), dp(16), dp(14));
        header.setGravity(Gravity.CENTER_VERTICAL);

        Button back = Kit.smallBtn(this, "←", Palette.BG_DEEP, Palette.CYAN);
        back.setOnClickListener(v -> finish());
        header.addView(back, Kit.wrapWrap());
        header.addView(Kit.hSpace(this, 10));
        header.addView(Kit.brandTv(this, "ASSISTANT", Palette.CYAN, 18f), Kit.wrapWrap());
        root.addView(header, Kit.matchWrap());

        // Messages
        mScroll = new ScrollView(this);
        mScroll.setBackgroundColor(Palette.BG_DEEP);
        mScroll.setVerticalScrollBarEnabled(false);

        mMsgs = new LinearLayout(this);
        mMsgs.setOrientation(LinearLayout.VERTICAL);
        mMsgs.setPadding(dp(16), dp(10), dp(16), dp(10));
        mScroll.addView(mMsgs, new ScrollView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams scrollLp = Kit.matchWrap();
        scrollLp.weight = 1f;
        root.addView(mScroll, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        // Input row
        LinearLayout inputRow = new LinearLayout(this);
        inputRow.setOrientation(LinearLayout.HORIZONTAL);
        inputRow.setBackgroundColor(Palette.BG_CARD);
        inputRow.setPadding(dp(12), dp(8), dp(12), dp(8));
        inputRow.setGravity(Gravity.CENTER_VERTICAL);

        mInput = Kit.input(this, "Ask anything...");
        mInput.setMaxLines(4);
        inputRow.addView(mInput, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        inputRow.addView(Kit.hSpace(this, 8));

        Button send = Kit.smallBtn(this, "SEND", Palette.CYAN, Palette.BG_DEEP);
        send.setOnClickListener(v -> sendMsg());
        inputRow.addView(send, Kit.wrapWrap());
        root.addView(inputRow, Kit.matchWrap());

        setContentView(root);

        addBubble("JARVIS", "Full assistant mode active.", false);
    }

    private void sendMsg() {
        String msg = mInput.getText().toString().trim();
        if (msg.isEmpty()) return;
        mInput.setText("");
        addBubble(Store.userName(), msg, true);
        mHistory.add(new JarvisNet.ChatMsg("user", msg));

        JarvisNet.sendChat(Store.userName(), msg, mHistory,
            new JarvisNet.Cb<String>() {
                @Override public void ok(String r) {
                    mHistory.add(new JarvisNet.ChatMsg("assistant", r));
                    addBubble("JARVIS", r, false);
                    mScroll.post(() -> mScroll.fullScroll(View.FOCUS_DOWN));
                }
                @Override public void err(String e) {
                    addBubble("SYS", "ERROR: " + e, false);
                }
            });
    }

    private void addBubble(String sender, String text, boolean isUser) {
        LinearLayout bub = new LinearLayout(this);
        bub.setOrientation(LinearLayout.VERTICAL);
        bub.setBackgroundColor(isUser ? 0xFF0D1A2A : Palette.BG_CARD);
        bub.setPadding(dp(14), dp(10), dp(14), dp(10));
        LinearLayout.LayoutParams lp = Kit.matchWrap();
        lp.bottomMargin = dp(8);
        bub.setLayoutParams(lp);

        bub.addView(Kit.monoTv(this, sender.toUpperCase() + " //",
            isUser ? Palette.PURPLE : Palette.CYAN, 9f), Kit.matchWrap());
        bub.addView(Kit.bodyTv(this, text, Palette.TEXT_HI, 14f), Kit.matchWrap());
        mMsgs.addView(bub);
        mScroll.post(() -> mScroll.fullScroll(View.FOCUS_DOWN));
    }

    private int dp(float v) { return Kit.dp(this, v); }
}
