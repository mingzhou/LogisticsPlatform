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

package org.espier.ios6.ui.utils;

import android.content.Context;
import android.os.Build;

/**
 * Created with IntelliJ IDEA.
 * User: houhuihua
 * Date: 13-1-4
 * To change this template use File | Settings | File Templates.
 */
public class Common {
    public static final int SDK_4_1_VERSION = 16;
    public static final int SDK_4_0_VERSION = 14;
    public static final int SDK_3_0_VERSION = 11;
    public static final int SDK_2_3_VERSION = 9;

    public static final int MODE_PRIVATE = 0;
    public static final int MODE_WORLD_READABLE = 1;
    public static final int MODE_WORLD_WRITEABLE = 2;
    public static final int MODE_APPEND = 32768;
    public static final int BIND_AUTO_CREATE = 1;
    public static final int BIND_DEBUG_UNBIND = 2;
    public static final int BIND_NOT_FOREGROUND = 4;

    public static final String OPERATES_SETTING_CHANGE_ACTION = "operates_name_setting_change";

    public final static String ICON_DESIGNER_SCALING_RULE = "Scaling_rule";

    public static final String PREFERENCE_FILENAME = "home_settings";
    public static final String SETTING_PREFERENCES = "preferences";
    public static final String EFFECT_PRIORITY = "effect_priority";
    public static final String SENSOR_SWITCH = "sensor_switch";
    public static final String STATUSBAR_SWITCH = "statusbar_switch";
    public static final String STATUSBAR_24H_SWITCH = "statusbar_24h_switch";
    public static final String TOOLBAR_SHADOW_SWITCH = "toolbar_shadow_switch";
    public static final String PAD_SWITCH = "pad_switch";
    public static final String ORIENTATION_CHANGED_TIP = "orientation_changed_tip";
    public static final String OPERATES_SETTING = "operates_setting";


    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
