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
import android.graphics.drawable.Drawable;

/**
 *
 * <p>
 * IOS like setting list row data information.
 * </p>
 *
 * @author mingming
 *
 */
public interface SettingInfo {

    /**
     * Row have a switch.
     *
     * (icon) text --  -- switch.
     */
    int TYPE_SWITCH = 0;

    /**
     * Row have a label text and footer auto have a callback
     * create by {@link Intent} to enter a activity.
     *
     * (icon) text --  -- arrow icon.
     */
    int TYPE_SIMPLE_ACTIVITY = 1;

    /**
     * Row have a label text and footer is a text than
     * can show current status(auto have a status callback to get status),
     * and auto have a callback create by {@link Intent} to enter a activity.
     *
     * (icon) text --  -- status & arrow icon.
     */
    int TYPE_STATUS_ACTIVITY = 2;

    /**
     * Row header is a sound-off icon, body is volume control seekbar,
     * footer is sound-on icon.
     *
     * sound-off icon -- seekbar -- sound-on icon.
     */
    // TODO: the sound icon is fix, and similar to TYPE_BRIGHTNESS_SETTING.
    // Maybe we can merge that.
    int TYPE_VOLUME_SETTING = 3;

    /**
     * Row header can show a choice icon, body is a text, no footer.
     *
     * choice icon -- text.
     */
    int TYPE_CHOICE = 4;

    /**
     * Row header can show a choice icon, body is a text, no footer.
     * Specific for {@link WifiValueType}.
     *
     * choice icon -- text.
     */
    int TYPE_WIFI_CHOICE = 5;

    /**
     * Row header is a bright-less icon, body is bright control seekbar,
     * footer is bright-more icon.
     *
     * bright-less icon -- seekbar -- bright-more icon.
     */
    // TODO: the bright icon is fix, and similar to TYPE_VOLUME_SETTING.
    // Maybe we can merge that.
    int TYPE_BRIGHTNESS_SETTING = 6;

    /**
     * Row have a label text and footer is label text too(it's can dynamic show by callback).
     *
     * (icon) text --  -- text.
     */
    int TYPE_LABEL = 7;

    /**
     * It's similar to TYPE_SIMPLE_ACTIVITY, but not auto have a callback.
     * This is more applicability, e,g popup a dialog and so on.
     *
     * (icon) text --  -- arrow icon.
     */
    int TYPE_ENTER = 8;

    /**
     * Row only have image or text(align center) to show a choice.
     *
     * -- image (or text) -- .
     */
    int TYPE_IMAGE_CHIOCE = 9;

    /**
     * This is just a custom view.
     */
    int TYPE_CUSTOM_VIEW = 10;

    int TYPE_LABLE_WITHOUT_ICON= 11;


    int TYPE_CHOICE_WITH_FOOT = 12;

    /**
     * This is just a label, don't use it.
     */
    int INNER_TYPE_BEGIN = 1000;

    /**
     * This is show a description information blow the setting list group.
     *
     * center align text.
     */
    int INNER_TYPE_GROUP_INFO = 1000;

    /**
     * This type can let you dynamic create the setting list.
     * Use by some dynamic list, e.g wi-fi list and so on.
     *
     * You must implement {@link SettingInfoListGetter} to create the list.
     */
    // TODO: But I found this is useless, may be we can adjust it.
    int INNER_TYPE_DYNAMIC = 1001;

    String getId();

    int getType();

    Drawable getIcon();

    String getTitle();

    Object getValue(Context context);

    /**
     * Set foot info
     */
    public void setFootDrawable(Drawable l, Drawable t, Drawable r, Drawable b);
    Drawable [] getFootDrawable();
    void setFoot(String foot);
    void setTitle(String title);
    String getFoot();
    void setToggle(boolean b);
    boolean isToggleOn();

    void onClick(Context context, Object obj);

    void updateStatus(Context context, Object obj);

    public interface WifiValueType {
        boolean isChoosed();
        boolean isOpen();
        int getLevel();
    }
}
