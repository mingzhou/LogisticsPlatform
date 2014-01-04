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

import org.espier.ios6.ui.utils.Common;
import org.espier.ios6.ui.utils.Compatibility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.logistics.R;

public class IosLikeListView extends ListView {

    private static LayoutAnimationController sLAC;

    public IosLikeListView(Context context) {
        this(context, null);
    }

    public IosLikeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // support only > API 9 (android 2.3)

        if (Common.getSdkVersion() >= Common.SDK_2_3_VERSION) {
            Compatibility.setOverScrollMode(this, 2);
        }

        setDivider(context.getResources().getDrawable(
                R.drawable.listview_divider));

        if (sLAC == null) {
            sLAC = new LayoutAnimationController(context, null);
            sLAC.setDelay(.3f);
            sLAC.setOrder(LayoutAnimationController.ORDER_NORMAL);
            sLAC.setAnimation(getContext(), R.anim.slide_down);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

        case MotionEvent.ACTION_DOWN:

            final int x = (int) ev.getX();
            final int y = (int) ev.getY();
            final int itemnum = pointToPosition(x, y);

            if (itemnum != AdapterView.INVALID_POSITION) {
                View view = getChildAt(itemnum);
                if(view instanceof IosLikeRowLayout) {
                    if (itemnum == 0) {
                        if (itemnum == (getAdapter().getCount() - 1)) {
                            setSelector(R.drawable.ioslike_listview_single_item);
                        } else {
                            setSelector(R.drawable.ioslike_listview_first_item);
                        }
                    } else if (itemnum == (getAdapter().getCount() - 1)) {
                        setSelector(R.drawable.ioslike_listview_last_item);
                    } else {
                        setSelector(R.drawable.ioslike_listview_item);
                    }
                } else {
                    setSelector(R.drawable.listview_selector);
                }
            }
            break;

        case MotionEvent.ACTION_UP:
            break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        // TODO :
        // setLayoutAnimation(sLAC);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int i = 0;
        int maxHeight = 0;
        int minHeight = 0;
        int itemHeight = 0;
        int totalHeight = 0;
        View listItem = null;

        try {
            listItem = listAdapter.getView(0, null, listView);
            listItem.measure(0, 0);
            minHeight = listItem.getMeasuredHeight();
            maxHeight = minHeight;
        } catch (Exception e) {
            minHeight = 0;
            maxHeight = 0;
        }

        // find item max height and total height.
        for (i = 0; i < listAdapter.getCount(); i++) {
            listItem = listAdapter.getView(i, null, listView);
            try {
                listItem.measure(0, 0);
            } catch (Exception e) {
                // ignore it.
            }

            itemHeight = listItem.getMeasuredHeight();

            if (maxHeight < itemHeight) {
                maxHeight = itemHeight;
            }

            if (minHeight > itemHeight) {
                minHeight = itemHeight;
            }

            totalHeight += itemHeight;
        }

        // if item height don't same, we adjust all item to the max height.
        /*
        if (minHeight != maxHeight) {
            totalHeight = 0;
            int height = MeasureSpec.makeMeasureSpec(maxHeight,
                    MeasureSpec.EXACTLY);

            for (i = 0; i < listAdapter.getCount(); i++) {
                listItem = listAdapter.getView(i, null, listView);
                try {
                    listItem.measure(0, height);
                } catch (Exception e) {
                    // ignore it.
                }

                totalHeight += listItem.getMeasuredHeight();
            }
        }
        */

        // adjust list view height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
