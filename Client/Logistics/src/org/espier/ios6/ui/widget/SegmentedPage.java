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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public abstract class SegmentedPage extends LinearLayout {
    private OnSegmentedPageDataChangedListener mSegmentedPageDataChangedListener;

    public interface OnSegmentedPageDataChangedListener {
        /**
         * Notify the application when the data changed
         *
         * @param page
         *            this
         * @param o
         *            data
         * @param type
         *            data type
         */
        void pageChangedNotify(SegmentedPage page, Object o, int type);
    }

    public SegmentedPage(Context context) {
        super(context);

        setupView(context, null);
    }

    public SegmentedPage(Context context, AttributeSet attr) {
        super(context, attr);

        setupView(context, null);
    }

    /**
     * Setup the page view tree
     *
     * @param context
     * @param attr
     */
    abstract protected void setupView(Context context, AttributeSet attr);

    public void setOnSegmentedPageDataChangedListener(
            OnSegmentedPageDataChangedListener listener) {
        mSegmentedPageDataChangedListener = listener;
    }

    public OnSegmentedPageDataChangedListener getOnSegmentedPageDataChangedListener() {
        return mSegmentedPageDataChangedListener;
    }

    public boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    protected void toPageChangedNotify(SegmentedPage page, Object o) {
        toPageChangedNotify(page, o, 0);
    }

    protected void toPageChangedNotify(SegmentedPage page, Object o, int type) {
        if (isVisible() && mSegmentedPageDataChangedListener != null) {
            mSegmentedPageDataChangedListener.pageChangedNotify(page, o, type);
        }
    }
}
