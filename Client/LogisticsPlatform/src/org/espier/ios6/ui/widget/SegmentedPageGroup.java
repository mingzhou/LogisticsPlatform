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

package org.espier.ios6.ui.widget;

import java.util.HashMap;

import org.espier.ios6.ui.widget.SegmentedPage.OnSegmentedPageDataChangedListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SegmentedPageGroup extends FrameLayout {
    private RadioGroup mRadioGroup;
    private OnPageShownListener mPageShowListener;
    private View mSelectedPage;
    private View mDefaultSelectedPage;
    private HashMap<Integer, Integer> mData = new HashMap<Integer, Integer>();
    private OnSegmentedPageDataChangedListener mSegmentedPageDataChangedListener;

    public interface OnPageShownListener {
        void pageShown(View page);
    }

    public SegmentedPageGroup(Context context) {
        super(context);

        init(context, null, 0);
    }

    public SegmentedPageGroup(Context context, AttributeSet attr) {
        this(context, attr, 0);

    }

    public SegmentedPageGroup(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);

        init(context, attr, defStyle);
    }

    private void init(Context context, AttributeSet attr, int defStyle) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            v.setVisibility(View.INVISIBLE);
        }
    }

    public void setOnSegmentedPageDataChangedListener(
            OnSegmentedPageDataChangedListener listener) {
        mSegmentedPageDataChangedListener = listener;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v instanceof SegmentedPage) {
                ((SegmentedPage) v)
                        .setOnSegmentedPageDataChangedListener(listener);
            }
        }
    }

    public OnSegmentedPageDataChangedListener getOnSegmentedPageDataChangedListener() {
        return mSegmentedPageDataChangedListener;
    }

    public void setOnPageShownListener(OnPageShownListener listener) {
        mPageShowListener = listener;
    }

    public OnPageShownListener getOnPageShownListener() {
        return mPageShowListener;
    }

    private void attachRadioButton(int[] radioButton, int[] page) {
        if (radioButton.length != page.length) {
            throw new RuntimeException();
        }

        for (int i = 0; i < radioButton.length; i++) {
            mData.put(radioButton[i], page[i]);
        }
    }

    /**
     * set RadioGroup to SegmentedPageGroup
     *
     * @param group
     *            RadioGroup
     * @param radioButton
     *            Id of Buttons of radio group
     * @param page
     *            Id of pages of SegmentedPageGroup
     */
    public void setRadioGroup(RadioGroup group, int[] radioButton, int[] page) {
        if (group == null) {
            throw new NullPointerException();
        }

        attachRadioButton(radioButton, page);

        mRadioGroup = group;
        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group == mRadioGroup) {
                    showPageById(mData.get(checkedId));
                }
            }
        });

        mDefaultSelectedPage = getChildAt(0);
        int checkedBtnId = mRadioGroup.getCheckedRadioButtonId();
        if (checkedBtnId != 0) {
            Integer pageId = mData.get(checkedBtnId);
            if (pageId == null) {
                mSelectedPage = mDefaultSelectedPage;
            } else {
                mSelectedPage = findViewById(pageId);
            }
        } else {
            mSelectedPage = mDefaultSelectedPage;
        }

        showPageById(mSelectedPage.getId());
    }

    private void showPageById(int pageId) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v.getId() == pageId) {
                v.setVisibility(View.VISIBLE);
                if (mPageShowListener != null) {
                    mPageShowListener.pageShown(v);
                }
            } else {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    public View getSelectedPage() {
        return mSelectedPage;
    }
}
