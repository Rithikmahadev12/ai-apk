package com.jarvis.assist.aosui.home;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import com.jarvis.assist.aosui.core.MemPolicy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class VirtualA11y extends AccessibilityNodeProvider {
    static final int INVALID = Integer.MIN_VALUE;
    final View host;
    final Src src;
    int focused = INVALID;
    int hovered = INVALID;
    final Rect r = new Rect();
    final int[] loc = new int[2];

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /tmp/augmaos/classes.dex */
    public interface Src {
        void bounds(int i, Rect rect);

        boolean click(int i);

        int count();

        String label(int i);

        default boolean longClick(int i) {
            return false;
        }

        default boolean hasLong(int i) {
            return false;
        }

        default View[] real() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VirtualA11y(View view, Src src) {
        this.host = view;
        this.src = src;
    }

    @Override // android.view.accessibility.AccessibilityNodeProvider
    public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
        boolean z = false;
        z = false;
        z = false;
        if (i == -1) {
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain(this.host);
            this.host.onInitializeAccessibilityNodeInfo(obtain);
            int count = this.src.count();
            for (int i2 = 0; i2 < count; i2++) {
                obtain.addChild(this.host, i2);
            }
            View[] real = this.src.real();
            if (real != null) {
                for (View view : real) {
                    if (view != null && view.getVisibility() == 0) {
                        obtain.addChild(view);
                    }
                }
            }
            return obtain;
        }
        if (i < 0 || i >= this.src.count()) {
            return null;
        }
        AccessibilityNodeInfo obtain2 = AccessibilityNodeInfo.obtain(this.host, i);
        obtain2.setPackageName(this.host.getContext().getPackageName());
        obtain2.setClassName("android.widget.Button");
        obtain2.setParent(this.host);
        obtain2.setContentDescription(this.src.label(i));
        this.src.bounds(i, this.r);
        obtain2.setBoundsInParent(this.r);
        this.host.getLocationOnScreen(this.loc);
        this.r.offset(this.loc[0], this.loc[1]);
        obtain2.setBoundsInScreen(this.r);
        if (this.host.isShown() && this.r.width() > 0 && this.r.height() > 0) {
            z = true;
        }
        obtain2.setVisibleToUser(z);
        obtain2.setEnabled(true);
        obtain2.setFocusable(true);
        obtain2.setClickable(true);
        obtain2.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
        if (this.src.hasLong(i)) {
            obtain2.setLongClickable(true);
            obtain2.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK);
        }
        if (this.focused == i) {
            obtain2.setAccessibilityFocused(true);
            obtain2.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        } else {
            obtain2.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
        }
        return obtain2;
    }

    @Override // android.view.accessibility.AccessibilityNodeProvider
    public boolean performAction(int i, int i2, Bundle bundle) {
        if (i == -1) {
            return this.host.performAccessibilityAction(i2, bundle);
        }
        if (i < 0 || i >= this.src.count()) {
            return false;
        }
        switch (i2) {
            case 16:
                boolean click = this.src.click(i);
                if (click) {
                    send(i, 1);
                }
                return click;
            case MemPolicy.DROP_FAR_WIDGETS /* 32 */:
                boolean longClick = this.src.longClick(i);
                if (longClick) {
                    send(i, 2);
                }
                return longClick;
            case MemPolicy.CLEAR_ICONS /* 64 */:
                if (this.focused == i) {
                    return false;
                }
                this.focused = i;
                this.host.invalidate();
                send(i, 32768);
                return true;
            case MemPolicy.TRIM_CHAT /* 128 */:
                if (this.focused != i) {
                    return false;
                }
                this.focused = INVALID;
                this.host.invalidate();
                send(i, 65536);
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hover(MotionEvent motionEvent) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.host.getContext().getSystemService("accessibility");
        if (accessibilityManager == null || !accessibilityManager.isEnabled() || !accessibilityManager.isTouchExplorationEnabled()) {
            return false;
        }
        int count = this.src.count() - 1;
        while (true) {
            if (count < 0) {
                count = INVALID;
                break;
            }
            this.src.bounds(count, this.r);
            if (this.r.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                break;
            }
            count--;
        }
        switch (motionEvent.getActionMasked()) {
            case 7:
            case 9:
                if (count != this.hovered) {
                    int i = this.hovered;
                    this.hovered = count;
                    if (count != INVALID) {
                        send(count, MemPolicy.TRIM_CHAT);
                    }
                    if (i != INVALID) {
                        send(i, 256);
                    }
                }
                return count != INVALID;
            case 8:
            default:
                return false;
            case 10:
                if (this.hovered != INVALID) {
                    send(this.hovered, 256);
                    this.hovered = INVALID;
                }
                return false;
        }
    }

    void send(int i, int i2) {
        AccessibilityManager accessibilityManager;
        ViewParent parent = this.host.getParent();
        if (parent == null || (accessibilityManager = (AccessibilityManager) this.host.getContext().getSystemService("accessibility")) == null || !accessibilityManager.isEnabled()) {
            return;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
        obtain.setPackageName(this.host.getContext().getPackageName());
        obtain.setClassName("android.widget.Button");
        obtain.setSource(this.host, i);
        String label = this.src.label(i);
        if (label != null) {
            obtain.getText().add(label);
            obtain.setContentDescription(label);
        }
        parent.requestSendAccessibilityEvent(this.host, obtain);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void pane(View view, String str) {
        if (Build.VERSION.SDK_INT >= 28) {
            view.setAccessibilityPaneTitle(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void heading(View view) {
        if (Build.VERSION.SDK_INT >= 28) {
            view.setAccessibilityHeading(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void contentChanged() {
        if (this.focused != INVALID && this.focused >= this.src.count()) {
            this.focused = INVALID;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.host.getContext().getSystemService("accessibility");
        if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
            return;
        }
        this.host.sendAccessibilityEvent(2048);
    }
}
