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
import android.util.DisplayMetrics;
import android.widget.ScrollView;

public class IosLikeScrollView extends ScrollView {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

    // copy from View(API 9) for support android 2.2 (API 8)
    //private static final int OVER_SCROLL_ALWAYS = 0x00000000;

    @SuppressWarnings("unused")
    private int mMaxYOverscrollDistance;

    public IosLikeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // support only > API 9 (android 2.3)
        //setOverScrollMode(OVER_SCROLL_ALWAYS);

        initBounceListView();
    }

    private void initBounceListView() {

        final DisplayMetrics metrics = getContext().getResources()
                .getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

}
