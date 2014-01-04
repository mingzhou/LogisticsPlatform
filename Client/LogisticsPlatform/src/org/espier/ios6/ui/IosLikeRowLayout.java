/*
 * Copyright (C) 2013 FMSoft (http://www.fmsoft.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.espier.ios6.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class IosLikeRowLayout extends LinearLayout {

    public IosLikeRowLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public IosLikeRowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // we fix the left child, and than adjust right child position
        // when it over the parent bound.
        View rightView = findLastRightChild();
        if (null != rightView) {
            final LinearLayout.LayoutParams lp =
                (LinearLayout.LayoutParams) rightView.getLayoutParams();

            int rightBound = rightView.getRight() + getPaddingRight() + lp.rightMargin;
            if (rightBound > getRight()) {
                int left = rightView.getLeft() - (rightView.getRight() - getRight());
                int right = getRight();

                left -= getPaddingLeft() + lp.leftMargin;
                right -= getPaddingRight() + lp.rightMargin;

                rightView.layout(left, rightView.getTop(), right, rightView.getBottom());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // we fix the left child, and than adjust right child position
        // when it over the parent bound.
        View rightView = findLastRightChild();
        if (null != rightView) {
            final LinearLayout.LayoutParams lp =
                (LinearLayout.LayoutParams) rightView.getLayoutParams();

            int rightBound = rightView.getRight() + getPaddingRight() + lp.rightMargin;
            if (rightBound > getRight()) {
                int width = rightView.getWidth();
                width = width > getWidth() ? getWidth() : width;

                rightView.measure(
                        MeasureSpec.makeMeasureSpec(width,
                                MeasureSpec.getMode(widthMeasureSpec)),
                        MeasureSpec.makeMeasureSpec(rightView.getHeight(),
                                MeasureSpec.getMode(heightMeasureSpec)));
            }
        }
    }

    private View findLastRightChild() {
        int x = 0;
        View child = null;
        View right = null;

        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            if (null == child || View.GONE == child.getVisibility() ||
                    0 == child.getWidth()) {
                continue;
            }

            if (child.getRight() >= x) {
                right = child;
                x = child.getRight();
            }
        }

        return right;
    }
}
