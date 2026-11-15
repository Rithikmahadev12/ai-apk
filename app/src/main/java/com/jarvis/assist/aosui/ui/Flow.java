package com.jarvis.assist.aosui.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: /tmp/augmaos/classes.dex */
public class Flow extends ViewGroup {
    public boolean center;
    public boolean end;
    public int gap;

    public Flow(Context context, int i, boolean z) {
        super(context);
        this.gap = i;
        this.center = z;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = (View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < getChildCount(); i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                childAt.measure((layoutParams == null || layoutParams.width <= 0) ? View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE) : View.MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824), (layoutParams == null || layoutParams.height <= 0) ? View.MeasureSpec.makeMeasureSpec(0, 0) : View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824));
                if (i6 > 0 && childAt.getMeasuredWidth() + i6 > size) {
                    i4 += i5 + this.gap;
                    i5 = 0;
                    i6 = 0;
                }
                i6 += childAt.getMeasuredWidth() + this.gap;
                i3 = Math.max(i3, i6 - this.gap);
                i5 = Math.max(i5, childAt.getMeasuredHeight());
            }
        }
        if (View.MeasureSpec.getMode(i) != 1073741824 && !this.end && !this.center) {
            size = Math.min(size, i3);
        }
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), i4 + i5 + getPaddingTop() + getPaddingBottom());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingLeft = ((i3 - i) - getPaddingLeft()) - getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int i5 = 0;
        while (i5 < childCount) {
            int i6 = 0;
            int i7 = 0;
            int i8 = i5;
            while (i8 < childCount) {
                View childAt = getChildAt(i8);
                if (childAt.getVisibility() != 8) {
                    int measuredWidth = childAt.getMeasuredWidth();
                    if (i6 > 0 && this.gap + i6 + measuredWidth > paddingLeft) {
                        break;
                    }
                    i6 += (i6 > 0 ? this.gap : 0) + measuredWidth;
                    i7 = Math.max(i7, childAt.getMeasuredHeight());
                }
                i8++;
            }
            int paddingLeft2 = getPaddingLeft() + (this.center ? (paddingLeft - i6) / 2 : this.end ? paddingLeft - i6 : 0);
            for (int i9 = i5; i9 < i8; i9++) {
                View childAt2 = getChildAt(i9);
                if (childAt2.getVisibility() != 8) {
                    childAt2.layout(paddingLeft2, paddingTop, childAt2.getMeasuredWidth() + paddingLeft2, childAt2.getMeasuredHeight() + paddingTop);
                    paddingLeft2 += childAt2.getMeasuredWidth() + this.gap;
                }
            }
            paddingTop += i7 + this.gap;
            i5 = i8 == i5 ? i5 + 1 : i8;
        }
    }
}
