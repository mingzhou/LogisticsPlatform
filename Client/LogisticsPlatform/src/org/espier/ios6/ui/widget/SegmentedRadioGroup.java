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
import android.widget.RadioGroup;

import com.logistics.R;

public class SegmentedRadioGroup extends RadioGroup {

    public SegmentedRadioGroup(Context context) {
        super(context);
    }

    public SegmentedRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        changeButtonsImages();
    }

    private void changeButtonsImages() {
        int count = getChildCount();
        if (count == 0) {
            return;
        }

        if (count > 1) {
            getChildAt(0).setBackgroundResource(R.drawable.segment_radio_left);
            for (int i = 1; i < count - 1; i++) {
                getChildAt(i).setBackgroundResource(
                        R.drawable.segment_radio_middle);
            }
            getChildAt(count - 1).setBackgroundResource(
                    R.drawable.segment_radio_right);
        } else {
            getChildAt(0).setBackgroundResource(R.drawable.segment_button);
        }
    }
}
