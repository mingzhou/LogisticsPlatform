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

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SimpleActivitySettingCb implements CommonSettingInfo.SettingCallback {

    protected Intent mIntent;

    public SimpleActivitySettingCb(Intent intent) {
        mIntent = intent;
    }

    @Override
    public void onClick(Context context, CommonSettingInfo info, Object obj) {
        Log.d("SimpleActivitySettingCb", "onClick");

        if (mIntent != null) {
            try {
                context.startActivity(mIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getValue(Context context, CommonSettingInfo info) {
        String ret = "";
        return ret;
    }

    @Override
    public void updateStatus(Context context, CommonSettingInfo info, Object obj) {
    }

}
