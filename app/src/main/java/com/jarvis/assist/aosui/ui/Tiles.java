package com.jarvis.assist.aosui.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: /tmp/augmaos/classes.dex */
public class Tiles extends ViewGroup {
    final int gap;
    final int min;

    public Tiles(Context context, int i, int i2) {
        super(context);
        this.min = i;
        this.gap = i2;
    }

    int cols(int i) {
        return Math.max(1, (i + this.gap) / (this.min + this.gap));
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int cols = cols(size);
        int i3 = cols - 1;
        int i4 = (size - (this.gap * i3)) / cols;
        int childCount = getChildCount();
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i5 < childCount) {
            View childAt = getChildAt(i5);
            childAt.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
            i7 = Math.max(i7, childAt.getMeasuredHeight());
            if (i5 % cols == i3 || i5 == childCount - 1) {
                i6 += i7 + (i5 == childCount + (-1) ? 0 : this.gap);
                i7 = 0;
            }
            i5++;
        }
        setMeasuredDimension(size, i6);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int cols = cols(i5);
        int i6 = (i5 - (this.gap * (cols - 1))) / cols;
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < getChildCount(); i9++) {
            View childAt = getChildAt(i9);
            int i10 = i9 % cols;
            if (i10 == 0 && i9 > 0) {
                i7 += i8 + this.gap;
                i8 = 0;
            }
            int i11 = i10 * (this.gap + i6);
            childAt.layout(i11, i7, i11 + i6, childAt.getMeasuredHeight() + i7);
            i8 = Math.max(i8, childAt.getMeasuredHeight());
        }
    }
}
