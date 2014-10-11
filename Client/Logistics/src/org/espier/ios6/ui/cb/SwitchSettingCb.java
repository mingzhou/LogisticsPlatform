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

package org.espier.ios6.ui.cb;

import org.espier.ios6.ui.CommonSettingInfo;
import org.espier.ios6.ui.IosLikeToggleButton;

import android.content.Context;
import android.util.Log;

public abstract class SwitchSettingCb implements CommonSettingInfo.SettingCallback {

    protected abstract void toggle(Context context, CommonSettingInfo info, Boolean b);

    @Override
    public void onClick(Context context, CommonSettingInfo info, Object obj) {
        Log.d("CommonSwitchSettingCb", "obj: " + obj);
        if (obj instanceof Boolean) {
            toggle(context, info, (Boolean) obj);

            if ((Boolean) obj) {
                Log.d("SwitchSettingCb", "+++ Turn on");
            }
            else {
                Log.d("SwitchSettingCb", "--- Turn off");
            }
        }
    }

    @Override
    public void updateStatus(Context context, CommonSettingInfo info, Object obj) {
        Log.d("CommonSwitchSettingCb", "obj: " + obj);
        if (obj instanceof IosLikeToggleButton) {
            IosLikeToggleButton button = (IosLikeToggleButton) obj;
            button.setChecked(isEnabled(context, info));
        }
    }

    @Override
    public Object getValue(Context context, CommonSettingInfo info) {
        return isEnabled(context, info);
    }

    abstract protected boolean isEnabled(Context context, CommonSettingInfo info);

}
